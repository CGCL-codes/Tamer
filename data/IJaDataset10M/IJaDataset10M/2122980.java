package org.freebxml.omar.common.spi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.freebxml.omar.common.*;

/**
 *
 * @author  najmi
 */
public class QueryManagerFactory {

    private static QueryManagerFactory instance;

    public static final String QUERY_MANAGER_CLASS_PROPERTY = "omar.common.QueryManagerFactory.queryManagerClass";

    private QueryManager qm = null;

    private static final Log log = LogFactory.getLog(QueryManagerFactory.class);

    /** Creates a new instance of QueryManager */
    protected QueryManagerFactory() {
    }

    public static synchronized QueryManagerFactory getInstance() {
        if (instance == null) {
            instance = new QueryManagerFactory();
        }
        return instance;
    }

    public QueryManager getQueryManager() {
        if (qm == null) {
            synchronized (this) {
                if (qm == null) {
                    try {
                        String pluginClass = CommonProperties.getInstance().getProperty(QUERY_MANAGER_CLASS_PROPERTY);
                        qm = (QueryManager) createPluginInstance(pluginClass);
                    } catch (Exception e) {
                        String errmsg = "[QueryManager] Cannot instantiate " + "QueryManager plugin. Please check that " + "property '" + QUERY_MANAGER_CLASS_PROPERTY + "' is correctly set in omar-common.properties file.";
                        log.error(errmsg, e);
                    }
                }
            }
        }
        return qm;
    }

    /**
    * Creates the instance of the pluginClass
    */
    private Object createPluginInstance(String pluginClass) throws Exception {
        Object plugin = null;
        if (log.isDebugEnabled()) {
            log.debug("pluginClass = " + pluginClass);
        }
        Class theClass = Class.forName(pluginClass);
        try {
            Constructor constructor = theClass.getConstructor((java.lang.Class[]) null);
            plugin = constructor.newInstance(new Object[0]);
        } catch (Exception e) {
            Method factory = theClass.getDeclaredMethod("getInstance", (java.lang.Class[]) null);
            plugin = factory.invoke(null, new Object[0]);
        }
        return plugin;
    }
}
