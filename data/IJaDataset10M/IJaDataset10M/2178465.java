package nl.dualit.clazzified.store;

import java.util.Properties;

public class StoreConfiguration {

    private static Properties _properties = null;

    public static void setProperties(Properties props) {
        checkProperties(props);
        _properties = props;
    }

    public static Properties getProperties() {
        return _properties;
    }

    private static void checkProperties(Properties props) {
        if (props.getProperty("clazz.store.location") == null) throw new NullPointerException("clazz.store.location is null");
    }

    public static String getConnectionString() {
        return _properties.getProperty("clazz.store.location");
    }
}
