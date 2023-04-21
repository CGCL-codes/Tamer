package com.hanhuy.scurp;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

/**
 * @author pfnguyen
 */
public class DesktopBrowseAction {

    private static final Method getDesktop;

    private static final boolean isSupported;

    private static final Method browse;

    static {
        Method _getDesktop = null;
        Method _isDesktopSupported = null;
        Method _isSupported = null;
        Method _browse = null;
        boolean supported = false;
        try {
            Class c = Class.forName("java.awt.Desktop");
            Class actions = Class.forName("java.awt.Desktop$Action");
            Field browseActionField = actions.getDeclaredField("BROWSE");
            Object browseAction = browseActionField.get(null);
            _getDesktop = c.getDeclaredMethod("getDesktop");
            _isDesktopSupported = c.getDeclaredMethod("isDesktopSupported");
            supported = (Boolean) _isDesktopSupported.invoke(null);
            if (supported) {
                Object desktop = _getDesktop.invoke(null);
                _isSupported = c.getDeclaredMethod("isSupported", actions);
                supported = (Boolean) _isSupported.invoke(desktop, browseAction);
                _browse = c.getDeclaredMethod("browse", URI.class);
            }
        } catch (ClassNotFoundException e) {
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchFieldException e) {
        }
        getDesktop = _getDesktop;
        isSupported = supported;
        browse = _browse;
    }

    /**
     * "Backward compatibility" support for Java 1.5.  Use Java 6's
     *  Desktop.browse method to launch a browser with the specified URI.
     *  Does nothing in Java 1.5
     * @return false if unable to browse to specified URI
     */
    public static boolean browse(URI uri) {
        if (isSupported) {
            try {
                Object desktop = getDesktop.invoke(null);
                browse.invoke(desktop, uri);
                return true;
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return false;
    }
}
