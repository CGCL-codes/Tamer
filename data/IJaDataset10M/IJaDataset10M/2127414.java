package org.scopemvc.controller.basic;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.View;

/**
 * <P>
 *
 * ViewContext handles show/hide of views, errors and has some concept of the
 * application's context. eg a Swing implementation would show views inside
 * JFrames or JDialogs and show errors using JOptionPanes. A servlet
 * implementation would know about the HTTP response and push views into its
 * stream. </P> <P>
 *
 * There's a place in here to store properties per context. This is used in the
 * servlet implementation to maintain state over a single request. </P> <P>
 *
 * Several static methods are used to set the context for an application either
 * on a global basis (eg Swing) or per-thread (eg servlet): see {@link
 * #getViewContext} etc. </P>
 *
 * @author <a href="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</a>
 * @version $Revision: 1.14 $ $Date: 2002/10/24 00:31:56 $
 * @created 05 August 2002
 */
public abstract class ViewContext {

    private static final Log LOG = LogFactory.getLog(ViewContext.class);

    private static ViewContext globalContext;

    private static ThreadLocal localContext = new ThreadLocal();

    /**
     * The properties associated with the ViewContext
     */
    private Map properties = new HashMap();

    /**
     * Return the current ViewContext. First try the per-thread context and if
     * none, returns the global context. May return null if no context is set.
     *
     * @return The viewContext value
     */
    public static ViewContext getViewContext() {
        ViewContext context = (ViewContext) localContext.get();
        if (context != null) {
            return context;
        }
        return globalContext;
    }

    /**
     * Set the global ViewContext.
     *
     * @param inContext The new globalContext value
     */
    public static void setGlobalContext(ViewContext inContext) {
        globalContext = inContext;
    }

    /**
     * Set a ViewContext for the current thread.
     *
     * @param inContext The new threadContext value
     */
    public static void setThreadContext(ViewContext inContext) {
        if (inContext == null) {
            throw new IllegalArgumentException("can't set a null ViewContext for a Thread -- see clearThreadContext()");
        }
        localContext.set(inContext);
    }

    /**
     * Clear the ViewContext for the current thread.
     */
    public static void clearThreadContext() {
        localContext.set(null);
    }

    /**
     * Fetch an arbitrary object under a known key.
     *
     * @param inKey The property key
     * @return The property value
     */
    public Object getProperty(String inKey) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getProperty: " + inKey);
        }
        return properties.get(inKey);
    }

    /**
     * Show the view
     *
     * @param inView The view to show
     */
    public abstract void showView(View inView);

    /**
     * Hide the view
     *
     * @param inView The view to hide
     */
    public abstract void hideView(View inView);

    /**
     * Show an error message.
     *
     * @param inErrorTitle The title for the error message
     * @param inErrorMessage The error message
     */
    public abstract void showError(String inErrorTitle, String inErrorMessage);

    /**
     * Exit the application
     */
    public abstract void exit();

    /**
     * Start a progress indicator for long operations.
     */
    public abstract void startProgress();

    /**
     * Stop the progress indicator.
     */
    public abstract void stopProgress();

    /**
     * Store an arbitrary object under a known key.
     *
     * @param inKey The element to be added to the Property attribute
     * @param inObject The element to be added to the Property attribute
     */
    public void addProperty(String inKey, Object inObject) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("addProperty: " + inKey + ", " + inObject);
        }
        if (inKey == null) {
            throw new IllegalArgumentException("Can't use a null key.");
        }
        properties.put(inKey, inObject);
    }

    /**
     * Fetch and remove an arbitrary object under a known key.
     *
     * @param inKey The property key
     * @return The removed value, or null if the key was not defined
     */
    public Object removeProperty(String inKey) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removeProperty: " + inKey);
        }
        return properties.remove(inKey);
    }

    /**
     * Clear all properties from this context.
     */
    public void clearProperties() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("clearProperties");
        }
        properties.clear();
    }
}
