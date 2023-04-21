package org.gamio.standalone;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author Agemo Cui <agemocui@gamio.org>
 * @version $Rev: 19 $ $Date: 2008-09-26 19:00:58 -0400 (Fri, 26 Sep 2008) $
 */
public final class GamioInfo {

    private static GamioInfo instance = null;

    private Properties properties = null;

    public static GamioInfo getInstance() throws Exception {
        if (instance == null) {
            instance = new GamioInfo();
            instance.load();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    private void load() throws Exception {
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream("gamioinfo.properties");
            properties = new Properties();
            properties.load(in);
        } finally {
            if (in != null) in.close();
        }
    }
}
