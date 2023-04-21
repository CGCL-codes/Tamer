package jp.dodododo.reloadable.servlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContextEvent;
import jp.dodododo.reloadable.filter.FilterWrapper;

public class ServletContextListenerWrapperContainer {

    private static final List<ServletContextListenerWrapper> SERVLET_CONTEXT_LISTENER_LIST = new ArrayList<ServletContextListenerWrapper>();

    public static void register(ServletContextListenerWrapper listenerWrapper) {
        SERVLET_CONTEXT_LISTENER_LIST.add(listenerWrapper);
    }

    public static void remove(FilterWrapper filterWrapper) {
        SERVLET_CONTEXT_LISTENER_LIST.remove(filterWrapper);
    }

    public static void initAll(ServletContextEvent event) {
        for (Iterator<ServletContextListenerWrapper> iter = SERVLET_CONTEXT_LISTENER_LIST.iterator(); iter.hasNext(); ) {
            ServletContextListenerWrapper listenerWrapper = iter.next();
            try {
                listenerWrapper.initTarget(event);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void destroyAll(ServletContextEvent event) {
        for (Iterator<ServletContextListenerWrapper> iter = SERVLET_CONTEXT_LISTENER_LIST.iterator(); iter.hasNext(); ) {
            ServletContextListenerWrapper listenerWrapper = iter.next();
            try {
                listenerWrapper.destroyTarget(event);
            } catch (Throwable ignore) {
                ignore.printStackTrace();
            }
        }
    }
}
