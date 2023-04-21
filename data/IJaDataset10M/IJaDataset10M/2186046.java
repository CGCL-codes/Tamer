package com.googlecode.semrs.util;

import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Roger
 * Clase Utilitaria utilizada para parsear el archivo de 
 * Propiedades desde el classpath
 *
 */
@Deprecated
public class PropertiesUtil {

    private Properties props;

    private static final Log LOG = LogFactory.getLog(PropertiesUtil.class);

    public Properties getProperties() {
        InputStream inputStream = getClass().getResourceAsStream("/semrs-config.properties");
        props = new Properties();
        try {
            LOG.info("Loading System Properties...");
            props.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            LOG.error("Error getting properties file: " + e.toString());
            System.exit(1);
        }
        return props;
    }
}
