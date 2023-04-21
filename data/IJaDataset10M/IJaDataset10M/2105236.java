package org.posper.resources;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author Aaron Luchko<aaron.luchko@oxn.ca>
 * 
 */
public class BasicProperties {

    private static Properties props;

    static {
        props = new Properties();
        try {
            props.load(BasicProperties.class.getResourceAsStream("/basic.properties"));
        } catch (IOException e) {
            Logger.getLogger(BasicProperties.class.getName()).log(Level.WARN, "Unhandled Exception: " + e.getMessage());
        }
    }

    /**
	 * @param posper_version
	 * @return
	 */
    public static String getProperty(String key) {
        return props.getProperty(key);
    }
}
