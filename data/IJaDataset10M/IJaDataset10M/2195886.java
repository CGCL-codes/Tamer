package shake.servlet;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import com.google.inject.Injector;

public class ShakeServletContext {

    public static final String SHAKE_INJECTOR_NAME = "SHAKE_INJECTOR";

    private static ServletContext context;

    private static FilterConfig filterConfig;

    static ClassLoader hotDeployClassLoader;

    public static ClassLoader getScanClassLoader() {
        if (hotDeployClassLoader == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return hotDeployClassLoader;
    }

    public static void setHotDeployClassLoader(ClassLoader hotDeployClassLoader) {
        ShakeServletContext.hotDeployClassLoader = hotDeployClassLoader;
    }

    public static FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public static void setFilterConfig(FilterConfig filterConfig) {
        ShakeServletContext.filterConfig = filterConfig;
    }

    public static Injector getInjector() {
        return (Injector) getContext().getAttribute(SHAKE_INJECTOR_NAME);
    }

    public static void setInjector(Injector injector) {
        getContext().setAttribute(SHAKE_INJECTOR_NAME, injector);
    }

    public static ServletContext getContext() {
        return context;
    }

    public static void setContext(ServletContext servletContext) {
        context = servletContext;
    }
}
