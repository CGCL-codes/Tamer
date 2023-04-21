package gov.lanl.Web;

import gov.lanl.Utility.ConfigProperties;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Class for periodically testing a set of actions on a web page
 * Is used for updating web pages automatically
 */
public class WebDriver extends Thread {

    private String url;

    private Vector action = new Vector();

    private int sleep = 600000;

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WebDriver.class);

    /**
     * Default constructor
     */
    public WebDriver() {
        super();
    }

    /**
     * Constructor
     */
    public WebDriver(Properties props) {
        super();
        setProperties(props);
    }

    /**
     * set properties
     */
    public void setProperties(Properties props) {
        sleep = new Integer(props.getProperty("Sleep", new Integer(sleep).toString())).intValue();
        url = props.getProperty("URL");
        String actions = props.getProperty("Actions");
        if (actions != null) {
            StringTokenizer token = new StringTokenizer(actions);
            while (token.hasMoreTokens()) {
                action.addElement(token.nextElement());
            }
        }
    }

    /**
     * set Sleep value
     */
    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    /**
     * start
     */
    public void start() {
        super.start();
    }

    /**
     * do the work
     */
    public void run() {
        while (true) {
            log.debug("run: actions = " + action.size());
            for (Enumeration e = action.elements(); e.hasMoreElements(); ) {
                try {
                    String urlstring = url + "?" + e.nextElement();
                    System.out.println("opening " + urlstring);
                    URL u = new URL(urlstring);
                    HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
                    urlConn.connect();
                    int respCode = urlConn.getResponseCode();
                    if (respCode <= 100 || respCode >= 300) {
                        log.debug(urlstring + " failed: " + respCode);
                    }
                    sleep(1000);
                } catch (java.io.IOException e1) {
                    log.error("run: ", e1);
                } catch (InterruptedException e1) {
                    log.error("run: ", e1);
                }
            }
            try {
                log.debug("Sleeping for " + sleep);
                sleep(sleep);
            } catch (InterruptedException x) {
                log.fatal("run halted: " + x);
                System.exit(1);
            }
        }
    }

    /**
     * test main
     */
    public static void main(String[] argv) {
        ConfigProperties props = new ConfigProperties();
        props.setProperties("test.properties", argv);
        WebDriver web = new WebDriver(props);
        web.start();
    }
}
