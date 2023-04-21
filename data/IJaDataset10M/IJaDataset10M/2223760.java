package de.bea.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class LoadProperty {

    private static final Log logger = LogFactory.getLog(LoadProperty.class);

    /**
     * Constructor for LoadProperty.
     */
    private LoadProperty() {
        super();
    }

    /**
    * versucht auf Basis der �bergebenen Parameter 
    * eine Property-Instance zu erzeugen, wenn die 
    * Datei nicht gefunden wird, ist der R�ckgabewert null.
    * @param clazz - Klasse auf die sich bezogen wird
    * @param filename - der Dateiname ohne Extention
    * @return Properties
    */
    public static Properties loadProperty(Class clazz, String fileName) {
        assert fileName != null;
        InputStream is = clazz.getResourceAsStream(fileName + ".properties");
        if (is != null) {
            Properties prop = new Properties();
            try {
                prop.load(is);
                is.close();
            } catch (IOException ioe) {
                logger.error("Fehler beim initialisieren der Property-Instanz ", ioe);
            }
            return prop;
        }
        return null;
    }
}
