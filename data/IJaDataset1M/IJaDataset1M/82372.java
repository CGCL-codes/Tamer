package it.newinstance.watchdog.meetup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;

/**
 * @author Luigi R. Viggiano
 * @version $Id: RemoteConfigLoader.java 58 2008-05-02 15:55:37Z luigi.viggiano $
 */
public class RemoteConfigLoader {

    private Logger logger = Logger.getLogger(getClass());

    private URL url;

    private long longevity;

    private long expireTime;

    public void setURL(String url) throws MalformedURLException {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            logger.error("UserIdsURL not valid", e);
            throw e;
        }
    }

    public void setLongevity(long longevity) {
        this.longevity = longevity;
    }

    public String[] values() {
        String[] values = null;
        long now = System.currentTimeMillis();
        if (url != null && now > expireTime) {
            InputStream input = null;
            try {
                URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                input = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                Set<String> readValues = new TreeSet<String>();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("#") || line.length() == 0) continue;
                    readValues.add(line);
                }
                values = readValues.toArray(new String[0]);
                expireTime = System.currentTimeMillis() + longevity;
                logger.debug("Configuration loaded successfully from " + url);
            } catch (IOException e) {
                logger.warn("I/O Error reading from url: " + url);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        logger.warn("Error closing inputstream", e);
                    }
                }
            }
        }
        return values;
    }
}
