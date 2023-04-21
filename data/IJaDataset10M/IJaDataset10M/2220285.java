package vobs.plugins;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.xml.namespace.*;
import org.apache.axis.client.*;
import org.apache.log4j.*;
import org.jdom.*;
import org.jdom.output.*;
import vobs.store.*;
import wdc.settings.*;

public class WeatherForecastPlugin {

    private static Logger mLog = Logger.getLogger(WeatherForecastPlugin.class.getName());

    private static String serviceUrl = null;

    private SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
   * Constructs a request to deliver data to a file on the container's filesystem
   */
    public WeatherForecastPlugin() {
        isoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        shortDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public void process(Map params, String paramsString, String basketObjectId) {
        String lat = ((String[]) params.get("lat"))[0];
        String lon = ((String[]) params.get("lon"))[0];
        try {
            String weatherUrlStr = Settings.get("vo_store.weatherForecastPlugin.weatherServiceUrl") + "Weather/" + lat + "/" + lon;
            mLog.debug("Querying " + weatherUrlStr);
            URL weatherServiceUrl = new URL(weatherUrlStr);
            InputStream inp = weatherServiceUrl.openStream();
            FileStoreSave.storeFile(inp, basketObjectId, "XML file", inp.available() + "", basketObjectId, "fileAction");
            inp.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static final void main(String[] params) throws IOException {
    }
}
