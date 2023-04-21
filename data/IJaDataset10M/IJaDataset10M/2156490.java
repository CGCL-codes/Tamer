package org.offseam.core;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.offseam.RestFacesException;
import org.offseam.adapter.PortletFacesRequestUrlInfo;
import org.offseam.adapter.RequestUrlInfo;
import org.offseam.util.Util;

/**
 *
 * @author agori
 */
public class RestContextFactory {

    private static final Logger logger = Util.getLogger(Util.APPLICATION);

    private static ThreadLocal<RestContext> contextThreadLocal = new ThreadLocal<RestContext>();

    public static RestContext getExistingContextInstance() {
        return contextThreadLocal.get();
    }

    public static void setContextInstance(RestContext context) {
        contextThreadLocal.set(context);
    }

    public static RestContext getContextInstance() {
        RestContext context = contextThreadLocal.get();
        if (null == context) {
            RestApplication app = RestApplication.getCurrentInstance();
            if (null == app) {
                String message = "RestApplication instance was null when we attempted to create the RestContext instance.";
                logger.severe(message);
                throw new RestFacesException(message);
            }
            if (app.isPortlet()) {
                context = new RestContext() {

                    @Override
                    protected RequestUrlInfo getRequestInfo() {
                        return new PortletFacesRequestUrlInfo();
                    }
                };
            } else {
                context = new RestContext();
            }
            context.setApplication(app);
            contextThreadLocal.set(context);
            if (logger.isLoggable(Level.FINEST)) {
                logger.log(Level.FINEST, "created RestContext " + context + " on thread" + Thread.currentThread());
            }
        }
        return context;
    }

    public static void releaseCurrentInstance() {
        if (contextThreadLocal.get() != null) {
            if (logger.isLoggable(Level.FINEST)) {
                StringBuilder sb = new StringBuilder();
                sb.append("releasing RestContext ").append(contextThreadLocal.get()).append(" on thread ").append(Thread.currentThread());
                logger.log(Level.FINEST, sb.toString());
            }
            contextThreadLocal.set(null);
        }
    }
}
