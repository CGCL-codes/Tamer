package es.cim.loginModule;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DadesProperties {

    private Properties propiedades = null;

    private static DadesProperties dadesProperties = new DadesProperties();

    /**
	 * Construye el objeto sin datos
	 */
    private DadesProperties() {
    }

    /**
	 * Obtiene singleton
	 * @return singleton
	 */
    public static DadesProperties getInstance() {
        return dadesProperties;
    }

    public String getProperty(String prop) throws Exception {
        if (propiedades == null) {
            getConfigurationForReg();
        }
        String URL = propiedades.getProperty(prop);
        return URL;
    }

    public synchronized Properties getConfigurationForReg() throws Exception {
        if (propiedades == null) {
            readProperties();
        }
        return propiedades;
    }

    /**
	 * Lee las propiedades de los ficheros de configuracion
	 * @throws Exception 
	 *
	 */
    private void readProperties() throws Exception {
        InputStream fisGlobal = null, fisModul = null;
        propiedades = new Properties();
        try {
            String pathConf = System.getProperty("ad.path.properties");
            fisGlobal = new FileInputStream(pathConf + "sistra/global.properties");
            propiedades.load(fisGlobal);
            fisModul = new FileInputStream(pathConf + "sistra/plugins/bus.properties");
            propiedades.load(fisModul);
        } catch (Exception e) {
            propiedades = null;
            throw new Exception("Excepcion accediendo a las propiedadades del modulo", e);
        } finally {
            try {
                if (fisGlobal != null) {
                    fisGlobal.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (fisModul != null) {
                    fisModul.close();
                }
            } catch (Exception ex) {
            }
        }
    }
}
