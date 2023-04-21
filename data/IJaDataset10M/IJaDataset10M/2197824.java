package tiniweb.module.tiniTwitt;

import java.io.IOException;
import java.util.Properties;
import tiniweb.core.AbstractModule;
import tiniweb.core.Module;
import tiniweb.core.http.Request;
import tiniweb.core.util.Util;

/**
 *
 * @author Yannick Poirier 
 */
public class TiniTwitt extends AbstractModule {

    private Request myRequest;

    private Properties properties = null;

    public TiniTwitt() {
    }

    public int logHandler(Class javaClass, String str, int verbose) {
        if (this.properties == null || str == null) {
            return Module.DECLINED;
        } else if (verbose <= Integer.parseInt(this.properties.getProperty("verbose"))) {
            String body = javaClass.getName().concat(" : ".concat(str));
            new TwittThread(this.properties.getProperty("address"), this.myRequest, body, false);
            return Module.OK;
        }
        return Module.DECLINED;
    }

    public int init(String[] args) {
        this.properties = new Properties();
        try {
            properties = Util.getProperties("tiniTwitt.properties");
            myRequest = new Request();
            myRequest.setMethod("POST");
            myRequest.setRequestFile("/statuses/update.xml");
            myRequest.setHttpVersion("HTTP/1.1");
            myRequest.setHost("twitter.com");
            myRequest.setAuthorization("Basic ".concat(properties.getProperty("password")));
            myRequest.setUserAgent("tiniTwitt");
            myRequest.setAccept("*/*");
            myRequest.setContentType("application/x-www-form-urlencoded");
        } catch (IOException ex) {
            return Module.ERROR;
        }
        return Module.OK;
    }
}
