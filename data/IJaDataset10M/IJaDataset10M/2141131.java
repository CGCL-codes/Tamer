package ch.nostromo.lib.osx;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * The Class ClientOSXAdapter.
 */
public class OSXAdapter implements InvocationHandler {

    /** The target object. */
    protected Object targetObject;

    /** The target method. */
    protected Method targetMethod;

    /** The proxy signature. */
    protected String proxySignature;

    /** The mac osx application. */
    static Object macOSXApplication;

    /**
   * Sets the quit handler.
   * 
   * @param target the target
   * @param quitHandler the quit handler
   */
    public static void setQuitHandler(Object target, Method quitHandler) {
        setHandler(new OSXAdapter("handleQuit", target, quitHandler));
    }

    /**
   * Sets the about handler.
   * 
   * @param target the target
   * @param aboutHandler the about handler
   */
    public static void setAboutHandler(Object target, Method aboutHandler) {
        boolean enableAboutMenu = (target != null && aboutHandler != null);
        if (enableAboutMenu) {
            setHandler(new OSXAdapter("handleAbout", target, aboutHandler));
        }
        try {
            Method enableAboutMethod = macOSXApplication.getClass().getDeclaredMethod("setEnabledAboutMenu", new Class[] { boolean.class });
            enableAboutMethod.invoke(macOSXApplication, new Object[] { Boolean.valueOf(enableAboutMenu) });
        } catch (Exception ex) {
            System.err.println("OSXAdapter could not access the About Menu");
            ex.printStackTrace();
        }
    }

    /**
   * Sets the preferences handler.
   * 
   * @param target the target
   * @param prefsHandler the prefs handler
   */
    public static void setPreferencesHandler(Object target, Method prefsHandler) {
        boolean enablePrefsMenu = (target != null && prefsHandler != null);
        if (enablePrefsMenu) {
            setHandler(new OSXAdapter("handlePreferences", target, prefsHandler));
        }
        try {
            Method enablePrefsMethod = macOSXApplication.getClass().getDeclaredMethod("setEnabledPreferencesMenu", new Class[] { boolean.class });
            enablePrefsMethod.invoke(macOSXApplication, new Object[] { Boolean.valueOf(enablePrefsMenu) });
        } catch (Exception ex) {
            System.err.println("OSXAdapter could not access the About Menu");
            ex.printStackTrace();
        }
    }

    /**
   * Sets the file handler.
   * 
   * @param target the target
   * @param fileHandler the file handler
   */
    public static void setFileHandler(Object target, Method fileHandler) {
        setHandler(new OSXAdapter("handleOpenFile", target, fileHandler) {

            @Override
            public boolean callTarget(Object appleEvent) {
                if (appleEvent != null) {
                    try {
                        Method getFilenameMethod = appleEvent.getClass().getDeclaredMethod("getFilename", (Class[]) null);
                        String filename = (String) getFilenameMethod.invoke(appleEvent, (Object[]) null);
                        this.targetMethod.invoke(this.targetObject, new Object[] { filename });
                    } catch (Exception ex) {
                    }
                }
                return true;
            }
        });
    }

    /**
   * Sets the handler.
   * 
   * @param adapter the new handler
   */
    public static void setHandler(OSXAdapter adapter) {
        try {
            Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
            if (macOSXApplication == null) {
                macOSXApplication = applicationClass.getConstructor((Class[]) null).newInstance((Object[]) null);
            }
            Class<?> applicationListenerClass = Class.forName("com.apple.eawt.ApplicationListener");
            Method addListenerMethod = applicationClass.getDeclaredMethod("addApplicationListener", new Class[] { applicationListenerClass });
            Object osxAdapterProxy = Proxy.newProxyInstance(OSXAdapter.class.getClassLoader(), new Class[] { applicationListenerClass }, adapter);
            addListenerMethod.invoke(macOSXApplication, new Object[] { osxAdapterProxy });
        } catch (ClassNotFoundException cnfe) {
            System.err.println("This version of Mac OS X does not support the Apple EAWT.  ApplicationEvent handling has been disabled (" + cnfe + ")");
        } catch (Exception ex) {
            System.err.println("Mac OS X Adapter could not talk to EAWT:");
            ex.printStackTrace();
        }
    }

    /**
   * Instantiates a new client osx adapter.
   * 
   * @param proxySignature the proxy signature
   * @param target the target
   * @param handler the handler
   */
    protected OSXAdapter(String proxySignature, Object target, Method handler) {
        this.proxySignature = proxySignature;
        this.targetObject = target;
        this.targetMethod = handler;
    }

    /**
   * Call target.
   * 
   * @param appleEvent the apple event
   * 
   * @return true, if successful
   * 
   * @throws InvocationTargetException the invocation target exception
   * @throws IllegalAccessException the illegal access exception
   */
    public boolean callTarget(Object appleEvent) throws InvocationTargetException, IllegalAccessException {
        Object result = this.targetMethod.invoke(this.targetObject, (Object[]) null);
        if (result == null) {
            return true;
        }
        return Boolean.valueOf(result.toString()).booleanValue();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isCorrectMethod(method, args)) {
            boolean handled = callTarget(args[0]);
            setApplicationEventHandled(args[0], handled);
        }
        return null;
    }

    /**
   * Checks if is correct method.
   * 
   * @param method the method
   * @param args the args
   * 
   * @return true, if is correct method
   */
    protected boolean isCorrectMethod(Method method, Object[] args) {
        return (this.targetMethod != null && this.proxySignature.equals(method.getName()) && args.length == 1);
    }

    /**
   * Sets the application event handled.
   * 
   * @param event the event
   * @param handled the handled
   */
    protected void setApplicationEventHandled(Object event, boolean handled) {
        if (event != null) {
            try {
                Method setHandledMethod = event.getClass().getDeclaredMethod("setHandled", new Class[] { boolean.class });
                setHandledMethod.invoke(event, new Object[] { Boolean.valueOf(handled) });
            } catch (Exception ex) {
                System.err.println("OSXAdapter was unable to handle an ApplicationEvent: " + event);
                ex.printStackTrace();
            }
        }
    }

    /**
   * Checks if is oSX.
   * 
   * @return true, if is oSX
   */
    public static boolean isOSX() {
        return (System.getProperty("os.name").toLowerCase().startsWith("mac"));
    }
}
