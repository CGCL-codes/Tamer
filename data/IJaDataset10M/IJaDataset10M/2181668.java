package org.ji18n.web.registry;

import static org.ji18n.core.registry.Registry.CORE_CONFIG_XML;
import static org.ji18n.core.registry.Registry.DEFAULT_CONFIG_XML;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.ji18n.core.registry.Registry;

/**
 * Starts and stops the Registry, using the default config resources
 * defined there, unless overridden via setting a context-param named:
 * <code>org.ji18n.CONFIG_XML</code>.
 * 
 * @version $Id: RegistryListener.java 159 2008-07-03 01:28:51Z david_ward2 $
 * @author david at ji18n.org
 */
public class RegistryListener implements ServletContextListener {

    public RegistryListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        Set<String> set = new LinkedHashSet<String>();
        String param = sce.getServletContext().getInitParameter("org.ji18n.CONFIG_XML");
        if (param == null) set.addAll(Arrays.asList(DEFAULT_CONFIG_XML)); else {
            set.add(CORE_CONFIG_XML);
            param = param.replaceAll("\\s", " ");
            StringTokenizer st = new StringTokenizer(param, " ,;|");
            while (st.hasMoreTokens()) {
                String config = st.nextToken().trim();
                if (config.length() > 0) set.add(config);
            }
        }
        Registry.instance().start(set.toArray(new String[set.size()]));
    }

    public void contextDestroyed(ServletContextEvent sce) {
        Registry.instance().stop();
    }
}
