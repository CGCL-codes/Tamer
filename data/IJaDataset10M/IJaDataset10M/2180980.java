package fr.esrf.Tango.factory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;
import fr.esrf.TangoApi.IApiUtilDAO;
import fr.esrf.TangoApi.IConnectionDAO;
import fr.esrf.TangoApi.IDatabaseDAO;
import fr.esrf.TangoApi.IDeviceAttributeDAO;
import fr.esrf.TangoApi.IDeviceAttribute_3DAO;
import fr.esrf.TangoApi.IDeviceDataDAO;
import fr.esrf.TangoApi.IDeviceDataHistoryDAO;
import fr.esrf.TangoApi.IDeviceProxyDAO;
import fr.esrf.TangoApi.IIORDumpDAO;
import fr.esrf.TangoApi.ITacoTangoDeviceDAO;

/**
 * 
 * @author BARBA-ROSSA
 * 
 */
public class TangoFactory {

    public static final String FACTORY_PROPERTIES = "tango_factory.properties";

    public static final String TANGO_FACTORY = "TANGO_FACTORY";

    private static TangoFactory singleton = new TangoFactory();

    private ITangoFactory tangoFactory;

    private boolean isDefaultFactory = true;

    private TangoFactory() {
        initTangoFactory();
    }

    /**
     * Load properties with impl specification and create instances
     * 
     */
    private void initTangoFactory() {
        final Properties properties = getPropertiesFile();
        String factoryClassName = properties.getProperty(TANGO_FACTORY);
        if (factoryClassName == null) {
            factoryClassName = "fr.esrf.TangoApi.factory.DefaultTangoFactoryImpl";
        }
        tangoFactory = (ITangoFactory) getObject(factoryClassName);
        isDefaultFactory = false;
    }

    public static TangoFactory getSingleton() {
        return singleton;
    }

    public IConnectionDAO getConnectionDAO() {
        return tangoFactory.getConnectionDAO();
    }

    public IDeviceProxyDAO getDeviceProxyDAO() {
        return tangoFactory.getDeviceProxyDAO();
    }

    public ITacoTangoDeviceDAO getTacoTangoDeviceDAO() {
        return tangoFactory.getTacoTangoDeviceDAO();
    }

    public IDatabaseDAO getDatabaseDAO() {
        return tangoFactory.getDatabaseDAO();
    }

    public IDeviceAttributeDAO getDeviceAttributeDAO() {
        return tangoFactory.getDeviceAttributeDAO();
    }

    public IDeviceAttribute_3DAO getDeviceAttribute_3DAO() {
        return tangoFactory.getDeviceAttribute_3DAO();
    }

    public IDeviceDataDAO getDeviceDataDAO() {
        return tangoFactory.getDeviceDataDAO();
    }

    public IDeviceDataHistoryDAO getDeviceDataHistoryDAO() {
        return tangoFactory.getDeviceDataHistoryDAO();
    }

    public IApiUtilDAO getApiUtilDAO() {
        return tangoFactory.getApiUtilDAO();
    }

    public IIORDumpDAO getIORDumpDAO() {
        return tangoFactory.getIORDumpDAO();
    }

    /**
     * We get the properties file which contains default properties
     * 
     * @return Properties
     */
    private static Properties getPropertiesFile() {
        try {
            final InputStream stream = TangoFactory.class.getClassLoader().getResourceAsStream(FACTORY_PROPERTIES);
            final Properties properties = new Properties();
            if (stream != null) {
                final BufferedInputStream bufStream = new BufferedInputStream(stream);
                properties.clear();
                properties.load(bufStream);
            }
            return properties;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * We instanciate the Component
     * 
     * @param className
     * @return Object
     */
    private static Object getObject(final String className) {
        try {
            final Class<?> clazz = Class.forName(className);
            final Constructor<?> contructor = clazz.getConstructor(new Class[] {});
            return contructor.newInstance(new Object[] {});
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isDefaultFactory() {
        return isDefaultFactory;
    }

    public void setDefaultFactory(final boolean isDefaultFactory) {
        this.isDefaultFactory = isDefaultFactory;
    }
}
