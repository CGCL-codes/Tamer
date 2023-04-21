package org.opennms.netmgt;

import org.springframework.context.ApplicationContext;

public class Registry {

    private static ApplicationContext s_appContext;

    public static void setAppContext(ApplicationContext appContext) {
        s_appContext = appContext;
    }

    public static boolean containsBean(String beanName) {
        return s_appContext.containsBean(beanName);
    }

    public static Object getBean(String beanName) {
        return s_appContext.getBean(beanName);
    }
}
