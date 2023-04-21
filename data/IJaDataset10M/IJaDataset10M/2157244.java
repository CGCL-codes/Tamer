package org.josef.web.jsf.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import org.josef.util.CDebug;

/**
 * Java Server Faces Utility class related to navigation.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 3404 $
 */
public final class JsfNavigationUtil {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(JsfNavigationUtil.class.getName());

    /**
     * Private constructor prevents creation of an instance outside this class.
     */
    private JsfNavigationUtil() {
    }

    /**
     * Forwards (dispatches) to the supplied path.
     * <br />Postcondition: A forward as been performed or a warning message
     * is logged.
     * @param path Context relative path to the specified resource, which must
     *  start with a slash ("/") character.
     * @throws IllegalArgumentException When the supplied path is empty.
     * @throws NullPointerException When the supplied path is null.
     */
    public static void forward(final String path) {
        CDebug.checkParameterNotEmpty(path, "path");
        try {
            FacesContext.getCurrentInstance().getExternalContext().dispatch(path);
        } catch (final IOException exception) {
            LOGGER.log(Level.WARNING, "Could not forward to:" + path, exception);
        }
    }

    /**
     * Refreshes the current page by redirecting to it.
     * <br />Postcondition: A refresh has been performed or a warning message
     * is logged.
     */
    public static void refresh() {
        final String url = JsfRequestUtil.getRequest().getRequestURI();
        try {
            JsfUtil.getExternalContext().redirect(url);
        } catch (final IOException exception) {
            LOGGER.log(Level.WARNING, "Could not refresh:" + url, exception);
        }
    }

    /**
     * Redirects to the supplied url.
     * <br />Postcondition: A redirect has been performed or a warning message
     * is logged.
     * @param url The url to redirect to.
     * @throws IllegalArgumentException When the supplied url is empty.
     * @throws NullPointerException When the supplied url is null.
     */
    public static void redirect(final String url) {
        CDebug.checkParameterNotEmpty(url, "url");
        try {
            JsfUtil.getExternalContext().redirect(url);
        } catch (final IOException exception) {
            LOGGER.log(Level.WARNING, "Could not redirect to:" + url, exception);
        }
    }

    /**
     * Gets the navigation handler.
     * @return The navigation handler.
     */
    public static NavigationHandler getNavigationHandler() {
        return JsfUtil.getApplication().getNavigationHandler();
    }
}
