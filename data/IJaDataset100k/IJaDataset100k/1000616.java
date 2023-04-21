package org.allesta.wsabi.util;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DOCUMENT ME!
 *
 * @author Allesta, LLC
 * @version $Revision: 1.1 $ 
 */
public abstract class AbstractContextLoader {

    private static final Log logger = LogFactory.getLog(AbstractContextLoader.class);

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param context DOCUMENT ME!
     * @param obj DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     */
    protected void register(String key, ServletContext context, Object obj) throws ServletException {
        Map moduleRegistry = null;
        Object registryObj = context.getAttribute(WebKeys.MODULE_REGISTRY);
        if (registryObj == null) {
            moduleRegistry = new FastHashMap();
        } else {
            moduleRegistry = (Map) registryObj;
        }
        if (moduleRegistry.containsKey(key)) {
            Object modueObj = moduleRegistry.get(key);
            if (!modueObj.equals(obj)) {
                throw new ServletException("Found invalid module object");
            }
        } else {
            moduleRegistry.put(key, obj);
        }
        context.setAttribute(WebKeys.MODULE_REGISTRY, moduleRegistry);
    }
}
