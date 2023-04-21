package webwatcher;

import java.io.*;
import java.util.Properties;

/**
 * @author   Sverre H. Huseby
 *           &lt;<A HREF="mailto:shh@thathost.com">shh@thathost.com</A>&gt;
 * @version  $Id: WebWatcher.java 16 2010-08-22 22:14:44Z miraculix0815 $
 */
public class WebWatcher {

    private PropertyProvider prop;

    public static String getPropertiesFilename() {
        if (isUnix()) {
            return getUserHome() + ".webwatcher.cfg";
        }
        return getUserHome() + "WebWatcher.cfg";
    }

    private static boolean isUnix() {
        return System.getProperty("file.separator").equals("/");
    }

    public static String getUserHome() {
        String home;
        String sep;
        sep = System.getProperty("file.separator", "/");
        home = System.getProperty("user.home", "");
        if (home.length() > 0 && !home.endsWith(sep)) home += sep;
        return home;
    }

    private static Properties getDefaultProperties() {
        Properties defaults = new Properties();
        if (isUnix()) {
            defaults.setProperty("browser", "netscape -remote openURL(%u,new-window)");
            defaults.setProperty("alternatebrowsercommand", "netscape %u");
            defaults.setProperty("pagesFile", getUserHome() + ".webwatcher.pages");
        } else {
            defaults.setProperty("browser", "c:\\Program files\\Netscape\\Communicator\\" + "Program\\netscape.exe %u");
            defaults.setProperty("alternatebrowsercommand", "c:\\Program files\\Netscape\\Communicator\\" + "Program\\netscape.exe %u");
            defaults.setProperty("pagesFile", getUserHome() + "WebWatcher.pages");
        }
        defaults.setProperty("refreshNew", "1");
        defaults.setProperty("autoRefresh", "1");
        defaults.setProperty("refreshInterval", "30");
        defaults.setProperty("doSort", "1");
        defaults.setProperty("changedFirst", "1");
        defaults.setProperty("ignoreCaseInSort", "0");
        defaults.setProperty("underline", "0");
        defaults.setProperty("useProxy", "0");
        defaults.setProperty("proxy.host", "");
        defaults.setProperty("proxy.port", "3128");
        defaults.setProperty("screenx", "0");
        defaults.setProperty("screeny", "0");
        defaults.setProperty("width", "200");
        defaults.setProperty("height", "300");
        return defaults;
    }

    GUI gui;

    WebPageCollection pages;

    public static void main(String[] args) {
        WebWatcher ww = new WebWatcher();
        ww.start();
    }

    public WebWatcher() {
    }

    public void start() {
        prop = new PropertyProvider(getDefaultProperties());
        prop.loadProperties();
        pages = new WebPageCollection(prop);
        try {
            pages.load(prop.getProperty("pagesFile"));
        } catch (IOException e) {
            System.err.println("error reading list of webpages: " + e.getMessage());
        }
        gui = new GUI(this, prop);
        gui.setTitle("WebWatcher");
        gui.pack();
        gui.setVisible(true);
    }

    public void doQuit() {
        try {
            prop.saveProperties();
            pages.save(prop.getProperty("pagesFile"));
        } catch (IOException e) {
        }
        gui.dispose();
        System.exit(0);
    }
}
