package org.gplugins.jpa.server;

import java.util.Collection;
import java.util.Iterator;
import org.gplugins.jpa.spi.JPAProvider;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default JPA manager GBean.  This is responsible for handling JPA
 * provider registration and lookups.
 *
 * @version $Rev: 46019 $ $Date: 2004-09-14 05:56:06 -0400 (Tue, 14 Sep 2004) $
 */
public class JPAManagerGBean implements JPAManager {

    private static final Log log = LogFactory.getLog(JPAManagerGBean.class);

    private final Collection jpaProviders;

    private String defaultJPAProvider;

    public JPAManagerGBean(Collection jpaProviders, String defaultJPAProvider) {
        this.jpaProviders = jpaProviders;
        this.defaultJPAProvider = defaultJPAProvider;
    }

    public void setDefaultJPAProviderClass(String className) {
        defaultJPAProvider = className;
    }

    public String getDefaultJPAProviderClass() {
        return defaultJPAProvider;
    }

    public JPAProvider getDefaultJPAProvider() {
        log.info("Default JPA Provider is " + (defaultJPAProvider == null ? "null" : defaultJPAProvider) + " with " + jpaProviders.size() + " providers registered");
        if (defaultJPAProvider != null) {
            for (Iterator it = jpaProviders.iterator(); it.hasNext(); ) {
                JPAProvider provider = (JPAProvider) it.next();
                if (provider.getProviderClass().equals(defaultJPAProvider)) {
                    return provider;
                }
            }
        }
        if (jpaProviders.size() > 0) {
            return (JPAProvider) jpaProviders.iterator().next();
        }
        return null;
    }

    public JPAProvider getJPAProvider(String className) {
        log.info("Default JPA Provider is " + (defaultJPAProvider == null ? "null" : defaultJPAProvider) + " with " + jpaProviders.size() + " providers registered");
        for (Iterator it = jpaProviders.iterator(); it.hasNext(); ) {
            JPAProvider provider = (JPAProvider) it.next();
            if (provider.getProviderClass().equals(className)) {
                return provider;
            }
        }
        return null;
    }

    public JPAProvider[] getAllJPAProviders() {
        return (JPAProvider[]) jpaProviders.toArray(new JPAProvider[jpaProviders.size()]);
    }

    public static final GBeanInfo GBEAN_INFO;

    static {
        GBeanInfoBuilder infoFactory = GBeanInfoBuilder.createStatic("JPA Provider Manager", JPAManagerGBean.class);
        infoFactory.addInterface(JPAManager.class);
        infoFactory.addReference("JPAProviders", JPAProvider.class, "GBean");
        infoFactory.addAttribute("defaultJPAProviderClass", String.class, true, true);
        infoFactory.setConstructor(new String[] { "JPAProviders", "defaultJPAProviderClass" });
        GBEAN_INFO = infoFactory.getBeanInfo();
    }

    public static GBeanInfo getGBeanInfo() {
        return GBEAN_INFO;
    }
}
