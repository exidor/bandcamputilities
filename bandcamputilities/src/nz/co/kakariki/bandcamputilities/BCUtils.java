package nz.co.kakariki.bandcamputilities;

import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import java.io.FileReader;
import java.io.BufferedReader;
import java.net.CookieHandler;
import java.net.CookieManager;

import java.net.MalformedURLException;
import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;

//http://www.mkyong.com/java/how-to-automate-login-a-website-java-example/
public class BCUtils {
	
	public static String UC = "https://bandcamp.com/<username>/";

	@SuppressWarnings("unchecked")
	public Map<String, String> umap = ArrayUtils.toMap(new String[][]{
			{"following", "<div\\s+class=\"fan-name\"><a\\s+href=\"http://bandcamp.com/[a-zA-Z_]+\">([a-zA-Z\\s])</a></div>"},
            {"followers", "<div\\s+class=\"fan-name\"><a\\s+href=\"http://bandcamp.com/[a-zA-Z_]+\">([a-zA-Z\\s])</a></div>"},
            {"feed", "<li\\s+class=\"buy-now\\s+\"><a href=\"(https://[a-zA-Z0-9\\.\\/]*)\\?"},
            {"collection", "<li\\s+class=\"buy-now\\s+\"><a href=\"(https://[a-zA-Z0-9\\.\\/]*)\\?"}
           });
	/*		
	new HashMap<String,String>(){{
		add("following", "<div\\s+class=\"fan-name\"><a\\s+href=\"http://bandcamp.com/[a-zA-Z_]+\">([a-zA-Z\\s])</a></div>");
        add("fowllowers", "<div\\s+class=\"fan-name\"><a\\s+href=\"http://bandcamp.com/[a-zA-Z_]+\">([a-zA-Z\\s])</a></div>");
        add("feed", "<li\\s+class=\"buy-now\\s+\"><a href=\"(https://[a-zA-Z0-9\\.\\/]*)\\?");
        add("collection", "<li\\s+class=\"buy-now\\s+\"><a href=\"(https://[a-zA-Z0-9\\.\\/]*)\\?");
	}};
	*/


	public String username;
	
	
	public BCUtils(String un){
		username = un;
	}
	
	/**
	 * Retrieves standard type URL parsing required lines 
	 * @param ref URL Type
	 */
	public void readFeedLinks(String ref){
		ArrayList<String> feed = this.fetchMatches(UC.replace("<username>",username)+ref,Pattern.compile(umap.get(ref)));
		
		for (String line : feed){
			System.out.println(line);
		}
		//<li class="buy-now "><a href="https://thespacespectrum.bandcamp.com/album/live?from=feedfan-carlsumner" target="_blank" data-clickstat="fanfeed_buynow">buy now</a></li>
	}

	
	public Boolean login(){
		String page = "http://page.com/";
		try {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 3128));
			URL url = new URL(page.replaceFirst("<username>", username));
			HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);


			
		} catch (MalformedURLException mue) {
            mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
	    }
		return Boolean.FALSE;
	}
	
	public ArrayList<String> fetchMatches(String page,Pattern patn){

		ArrayList<String> toks = new ArrayList<String>();

		try {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 3128));
            URL url = new URL(page.replaceFirst("<username>", username));
            System.out.println(url);
            HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
            uc.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            //BufferedReader br = new BufferedReader(new FileReader("test.txt"));

            String line;
            while ((line = br.readLine()) != null){
            	Matcher m = patn.matcher(line);
            	System.out.println(line);
            	if (m.find()){
            		toks.add(m.group(1));
            	}
            }
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return toks;
	}
	
	public static void main(String[] args){
		BCUtils bcu = new BCUtils("easybiscuit");
		bcu.readFeedLinks("feed");
	}
}
