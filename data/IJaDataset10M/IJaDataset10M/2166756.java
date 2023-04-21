package org.apache.tomcat;

import java.io.InputStream;
import java.util.Properties;

public class Apr {

    private static String aprInfo = null;

    static {
        try {
            InputStream is = Apr.class.getResourceAsStream("/org/apache/tomcat/apr.properties");
            Properties props = new Properties();
            props.load(is);
            is.close();
            aprInfo = props.getProperty("tcn.info");
        } catch (Throwable t) {
            ;
        }
    }
}
