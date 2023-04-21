package net.sf.jweather.metar;

import java.io.*;
import org.apache.log4j.Logger;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Responsible for downloading the METAR reports 
 *
 * <p>
 * <code>
 * String metarData = MetarFetcher.fetch("KLAX");<br>
 * </code>
 * </p>
 *
 * @author David Castro, dcastro@apu.edu
 * @version $Revision: 1.2 $
 * @see <a href="Metar.html">Metar</a>
 */
public class MetarFetcher {

    private static Logger log4j = Logger.getLogger(MetarFetcher.class);

    private static String metarData = null;

    static final String httpMetarURL = "http://weather.noaa.gov/pub/data/observations/metar/stations/";

    public static String fetch(String station) {
        return fetch(station, 0);
    }

    public static String fetch(String station, int timeout) {
        metarData = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String url = httpMetarURL + station.toUpperCase() + ".TXT";
        HttpGet httpget = new HttpGet(url);
        log4j.debug("executing request " + httpget.getURI());
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = "";
        try {
            responseBody = httpclient.execute(httpget, responseHandler);
        } catch (ClientProtocolException e) {
            log4j.error("Client protocol exception retrieving URL: " + url);
            return "";
        } catch (IOException e) {
            log4j.error("IO exception retrieving URL: " + url);
            return "";
        }
        log4j.debug(responseBody);
        httpclient.getConnectionManager().shutdown();
        metarData = new String(responseBody) + "\n";
        log4j.debug("MetarFetcher: metar data: " + metarData);
        return metarData;
    }
}
