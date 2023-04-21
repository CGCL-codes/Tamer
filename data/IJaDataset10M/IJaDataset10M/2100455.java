package shag.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import shag.App;

/**
 * MacBridge contains utility methods for integrating a Shag app better within
 * the native Mac OS X environment.
 * 
 * @author   dondi
 * @version  $Revision: 1.3 $ $Date: 2005/04/01 21:01:07 $
 */
public class MacBridge {

    /**
     * Enables or disables the native Preferences menu item.
     */
    public static void setPreferencesEnabled(boolean b) {
        if (!PlatformIdentifier.isMac()) return;
        try {
            getApplicationClass().getMethod("setEnabledPreferencesMenu", new Class[] { Boolean.TYPE }).invoke(getApplication(), new Object[] { Boolean.valueOf(b) });
        } catch (Exception exc) {
        }
    }

    /**
     * Bridges the current Shag app to the native Mac OS X environment
     * under the given name.
     */
    public static void adapt(String name) {
        if (!PlatformIdentifier.isMac()) return;
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", name);
        try {
            Class<?> applicationClass = getApplicationClass();
            Object applicationObject = getApplication();
            applicationClass.getMethod("addAboutMenuItem", (Class[]) null).invoke(applicationObject, (Object[]) null);
            applicationClass.getMethod("addPreferencesMenuItem", (Class[]) null).invoke(applicationObject, (Object[]) null);
            Class<?> applicationHandlerClass = Class.forName("com.apple.eawt.ApplicationListener");
            Object methodHandler = Proxy.newProxyInstance(applicationHandlerClass.getClassLoader(), new Class[] { applicationHandlerClass }, _MacProxy);
            Method registerMethod = applicationClass.getMethod("addApplicationListener", new Class[] { applicationHandlerClass });
            registerMethod.invoke(applicationObject, new Object[] { methodHandler });
            Class<?> appEventClass = Class.forName("com.apple.eawt.ApplicationEvent");
            _SetHandledMethod = appEventClass.getMethod("setHandled", new Class[] { Boolean.TYPE });
        } catch (Exception exc) {
        }
    }

    /**
     * Convenience method for retrieving the com.apple.eawt.Application class.
     */
    private static Class<?> getApplicationClass() throws Exception {
        return (Class.forName("com.apple.eawt.Application"));
    }

    /**
     * Convenience method for retrieving the com.apple.eawt.Application singleton object.
     */
    private static Object getApplication() throws Exception {
        return (getApplicationClass().getMethod("getApplication", (Class[]) null).invoke(null, (Object[]) null));
    }

    /**
     * Convenience method for marking a com.apple.eawt.ApplicationEvent object
     * as being handled.
     */
    private static void handleEvent(Object appEventObject) throws Throwable {
        _SetHandledMethod.invoke(appEventObject, new Object[] { Boolean.TRUE });
    }

    /**
     * Object that handles external method calls from the Mac environment.
     */
    private static InvocationHandler _MacProxy = new InvocationHandler() {

        /**
         * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
         */
        public Object invoke(Object proxy, Method meth, Object[] args) throws Throwable {
            if (meth.getName().equals("handleQuit")) {
                handleEvent(args[0]);
                App.get().quit();
            } else if (meth.getName().equals("handleAbout")) {
                handleEvent(args[0]);
                App.get().showAboutBox();
            } else if (meth.getName().equals("handlePreferences")) {
                handleEvent(args[0]);
            }
            return (null);
        }
    };

    /**
     * Cached pointer to the com.apple.eawt.ApplicationEvent.setHandled() method.
     */
    private static Method _SetHandledMethod = null;
}
