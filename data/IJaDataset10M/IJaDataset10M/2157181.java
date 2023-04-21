package com.siberhus.stars.ejb.glassfish;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.siberhus.stars.StarsRuntimeException;
import com.siberhus.stars.ejb.DefaultEjbLocator;

public class Glassfish3EjbLocator extends DefaultEjbLocator {

    private final Logger log = LoggerFactory.getLogger(Glassfish3EjbLocator.class);

    private static final Map<Class<?>, String> GLOBAL_EJB_JNDI_MAP = new ConcurrentHashMap<Class<?>, String>();

    @Override
    public void init(Configuration configuration) throws Exception {
        super.init(configuration);
        updateLocalJndiMap(configuration.getServletContext().getContextPath(), jndiLocator.getContext(), "");
    }

    @Override
    public Object lookup(String contextPath, Class<?> beanInterface, String beanName, String lookup, String name, String mappedName) throws NamingException {
        log.debug("Found EJB interface: {}", beanInterface);
        if (lookup != null && !"".equals(lookup.trim())) {
            return jndiLocator.lookup(lookup);
        }
        String jndiName = GLOBAL_EJB_JNDI_MAP.get(beanInterface);
        log.debug("Looing up JNDI name: {}", jndiName);
        return jndiLocator.lookup(jndiName);
    }

    private void updateLocalJndiMap(String contextPath, Context ctx, String parent) {
        try {
            NamingEnumeration<Binding> list = ctx.listBindings("");
            while (list.hasMore()) {
                Binding item = list.next();
                String className = item.getClassName();
                String name = item.getName();
                if ("com.sun.enterprise.naming.impl.TransientContext".equals(className)) {
                    parent = name;
                } else if ("com.sun.ejb.containers.JavaGlobalJndiNamingObjectProxy".equals(className) || "javax.naming.Reference".equals(className)) {
                    int startIdx = name.indexOf("!");
                    if (startIdx != -1) {
                        String infClassName = name.substring(startIdx + 1, name.length());
                        try {
                            Class<?> infClass = ReflectUtil.findClass(infClassName);
                            String jndiName = "java:global" + contextPath + "/" + name;
                            log.info("Resolved JNDI name for EJB: {} is {}", new Object[] { infClass, jndiName });
                            GLOBAL_EJB_JNDI_MAP.put(infClass, jndiName);
                        } catch (ClassNotFoundException e) {
                        }
                    }
                }
                Object o = item.getObject();
                if (o instanceof javax.naming.Context) {
                    updateLocalJndiMap(contextPath, (Context) o, parent + "/");
                }
            }
        } catch (NamingException e) {
            throw new StarsRuntimeException(e);
        }
    }
}
