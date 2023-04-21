package com.siberhus.stars.ejb;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import net.sourceforge.stripes.util.ReflectUtil;
import net.sourceforge.stripes.util.StringUtil;
import com.siberhus.stars.StarsRuntimeException;
import com.siberhus.stars.stripes.StarsConfiguration;

public class DefaultJndiLocator implements JndiLocator {

    public static final String JNDI_PROPERTIES = "JNDI.Properties";

    public static final String JNDI_DEFAULT_LOOKUP_TABLE = "JNDI.DefaultLookupTable";

    private StarsConfiguration configuration;

    private Map<Class<?>, String> localJndiMap = new HashMap<Class<?>, String>();

    private Context context;

    @Override
    public void init(Configuration configuration) throws Exception {
        this.configuration = (StarsConfiguration) configuration;
        initJndiDefaultLookupTable();
        Properties jndiProperties = createJndiProperties();
        context = initialContext(jndiProperties);
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Context initialContext(Properties properties) throws NamingException {
        return new InitialContext(properties);
    }

    @Override
    public Object lookup(String jndiName) throws NamingException {
        try {
            return context.lookup(jndiName);
        } catch (NamingException e) {
            throw new StarsRuntimeException("Unable to find a resource with name [" + jndiName + "] in the initial context.");
        }
    }

    @Override
    public Object lookup(Class<?> clazz) throws NamingException {
        String jndi = localJndiMap.get(clazz);
        if (jndi != null) {
            return lookup(jndi);
        } else {
            return lookup(clazz.getName());
        }
    }

    protected Map<Class<?>, String> getLocalJndiMap() {
        return localJndiMap;
    }

    private Properties createJndiProperties() {
        Properties jndiProperties = new Properties();
        String mapString = configuration.getBootstrapPropertyResolver().getProperty(JNDI_PROPERTIES);
        if (mapString != null) {
            String[] items = StringUtil.standardSplit(mapString);
            for (String item : items) {
                item = item.trim();
                String kv[] = item.split("=");
                jndiProperties.put(kv[0].trim(), kv[1].trim());
            }
        }
        return jndiProperties;
    }

    private void initJndiDefaultLookupTable() {
        String mapString = configuration.getBootstrapPropertyResolver().getProperty(JNDI_DEFAULT_LOOKUP_TABLE);
        if (mapString != null) {
            String[] items = StringUtil.standardSplit(mapString);
            for (String item : items) {
                item = item.trim();
                String className = null, lookup = null;
                try {
                    String kv[] = item.split("=");
                    className = kv[0];
                    lookup = kv[1];
                    localJndiMap.put(ReflectUtil.findClass(className), lookup);
                } catch (ClassNotFoundException e) {
                    throw new StripesRuntimeException("Could not find class [" + className + "] specified by the configuration parameter [" + item + "]. This value must contain fully qualified class names separated " + " by commas.");
                }
            }
        }
    }
}
