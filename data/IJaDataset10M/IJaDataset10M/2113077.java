package es.rvp.java.simpletag.gui.configuration;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import es.rvp.java.simpletag.gui.fonts.FontApplication;

/**
 * Cargador de configuraciones.
 *
 * @author Rodrigo Villamil Perez
 */
public class ApplicationConfigLoader {

    protected static final Logger LOGGER = Logger.getLogger(ApplicationConfigLoader.class);

    private static ApplicationConfig applicationConfig;

    /**
	 * Carga la configuracion de la aplicacion de un recurso.
	 */
    public static void loadConfigFromResource(final String resourceName) throws ApplicationConfigException {
        final InputStream inputStream = ApplicationConfig.class.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new ApplicationConfigException("Resource not found. Error on loading resource " + resourceName);
        }
        loadConfigFromInputStream(inputStream);
    }

    /**
	 * Carga la configuracion de la aplicacion de un fichero.
	 */
    private static void loadConfigFromInputStream(final InputStream inputStream) throws ApplicationConfigException {
        final Digester digester = new Digester();
        digester.setValidating(false);
        digester.addObjectCreate("applicationConfig", ApplicationConfig.class);
        digester.addObjectCreate("applicationConfig/fontConfig", FontConfig.class);
        digester.addObjectCreate("applicationConfig/fontConfig/fontApplication", FontApplication.class);
        digester.addBeanPropertySetter("applicationConfig/fontConfig/fontApplication/id", "id");
        digester.addBeanPropertySetter("applicationConfig/fontConfig/fontApplication/style", "style");
        digester.addBeanPropertySetter("applicationConfig/fontConfig/fontApplication/heigh", "heigh");
        digester.addBeanPropertySetter("applicationConfig/fontConfig/fontApplication/description", "description");
        digester.addSetNext("applicationConfig/fontConfig/fontApplication", "addFont");
        digester.addSetNext("applicationConfig/fontConfig", "setFontConfig");
        try {
            applicationConfig = (ApplicationConfig) digester.parse(inputStream);
        } catch (final IOException e) {
            throw new ApplicationConfigException(e.getMessage() + ". Error on loading config from InputStream");
        } catch (final SAXException e) {
            throw new ApplicationConfigException(e.getMessage() + ". Error on loading config from InputStream");
        }
    }

    @Override
    public String toString() {
        return "ApplicationConfigLoader [applicationConfig=" + ApplicationConfigLoader.applicationConfig + "]";
    }

    public static ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    /**
	 * Para testing
	 */
    public static void main(final String[] args) throws ApplicationConfigException {
        ApplicationConfigLoader.loadConfigFromResource("/applicationConfig.xml");
        for (final FontApplication font : applicationConfig.getFontConfig().getFonts()) {
            System.err.println(font.toString());
        }
    }
}
