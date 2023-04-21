package jgnash.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.prefs.Preferences;

/**
 * Factory methods for setting network connection timeout and creating a URLConnection with timeout set correctly.
 * 
 * @author Craig Cavanaugh
 * @version $Id: ConnectionFactory.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
public class ConnectionFactory {

    private static final String TIMEOUT = "timeout";

    private ConnectionFactory() {
    }

    /**
     * Sets the network timeout in seconds
     * 
     * @param seconds time in seconds before a timeout occurs
     */
    public static synchronized void setConnectionTimeout(int seconds) {
        if (seconds < 1 || seconds > 120) {
            throw new IllegalArgumentException("Invalid timeout connection");
        }
        Preferences pref = Preferences.userNodeForPackage(ConnectionFactory.class);
        pref.putInt(TIMEOUT, seconds);
    }

    /**
     * Return the network connection timeout in seconds
     * 
     * @return timeout in seconds
     */
    public static synchronized int getConnectionTimeout() {
        Preferences pref = Preferences.userNodeForPackage(ConnectionFactory.class);
        return pref.getInt(TIMEOUT, 30);
    }

    public static synchronized URLConnection getConnection(String url) {
        URLConnection connection = null;
        try {
            connection = getConnection(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static synchronized URLConnection getConnection(final URL url) {
        URLConnection connection = null;
        try {
            connection = url.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connection != null) {
            connection.setConnectTimeout(getConnectionTimeout() * 1000);
            connection.setReadTimeout(getConnectionTimeout() * 1000);
        }
        return connection;
    }
}
