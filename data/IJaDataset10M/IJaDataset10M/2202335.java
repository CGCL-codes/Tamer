package edu.ucsd.ncmir.spl.gui;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class OSXAdaptor implements InvocationHandler {

    protected Object _target_object;

    protected Method _target_method;

    protected String _proxy_signature;

    static Object _mac_OSX_application;

    protected OSXAdaptor(String proxy_signature, Object target, Method handler) {
        this._proxy_signature = proxy_signature;
        this._target_object = target;
        this._target_method = handler;
    }

    public static void setQuitHandler(Object target, Method quit_handler) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        setHandler(new OSXAdaptor("handleQuit", target, quit_handler));
    }

    public static void setAboutHandler(Object target, Method about_handler) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if ((target != null) && (about_handler != null)) setHandler(new OSXAdaptor("handleAbout", target, about_handler));
        Method enable_about_method = _mac_OSX_application.getClass().getDeclaredMethod("setEnabledAboutMenu", new Class[] { boolean.class });
        enable_about_method.invoke(_mac_OSX_application, new Object[] { Boolean.valueOf(true) });
    }

    public static void setPreferencesHandler(Object target, Method prefs_handler) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        boolean enable_prefs_menu = (target != null) && (prefs_handler != null);
        if (enable_prefs_menu) setHandler(new OSXAdaptor("handlePreferences", target, prefs_handler));
        Method enable_prefs_method = _mac_OSX_application.getClass().getDeclaredMethod("setEnabledPreferencesMenu", new Class[] { boolean.class });
        enable_prefs_method.invoke(_mac_OSX_application, new Object[] { Boolean.valueOf(enable_prefs_menu) });
    }

    public static void setFileHandler(Object target, Method file_handler) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        setHandler(new OSXAdaptor("handleOpenFile", target, file_handler) {

            @Override
            public boolean callTarget(Object apple_event) {
                if (apple_event != null) try {
                    Method getFilenameMethod = apple_event.getClass().getDeclaredMethod("getFilename", (Class[]) null);
                    String filename = (String) getFilenameMethod.invoke(apple_event, (Object[]) null);
                    this._target_method.invoke(this._target_object, new Object[] { filename });
                } catch (Exception ex) {
                }
                return true;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static void setHandler(OSXAdaptor adapter) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class application_class = Class.forName("com.apple.eawt.Application");
        if (_mac_OSX_application == null) _mac_OSX_application = application_class.getConstructor((Class[]) null).newInstance((Object[]) null);
        Class applicationListenerClass = Class.forName("com.apple.eawt.ApplicationListener");
        Method addListenerMethod = application_class.getDeclaredMethod("addApplicationListener", new Class[] { applicationListenerClass });
        Object osxAdapterProxy = Proxy.newProxyInstance(OSXAdaptor.class.getClassLoader(), new Class[] { applicationListenerClass }, adapter);
        addListenerMethod.invoke(_mac_OSX_application, new Object[] { osxAdapterProxy });
    }

    public boolean callTarget(Object appleEvent) throws InvocationTargetException, IllegalAccessException {
        Object result = _target_method.invoke(_target_object, (Object[]) null);
        boolean r = true;
        if (result != null) r = Boolean.valueOf(result.toString()).booleanValue();
        return r;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isCorrectMethod(method, args)) {
            boolean handled = callTarget(args[0]);
            setApplicationEventHandled(args[0], handled);
        }
        return null;
    }

    protected boolean isCorrectMethod(Method method, Object[] args) {
        return ((_target_method != null) && _proxy_signature.equals(method.getName()) && (args.length == 1));
    }

    protected void setApplicationEventHandled(Object event, boolean handled) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (event != null) {
            Method setHandledMethod = event.getClass().getDeclaredMethod("setHandled", new Class[] { boolean.class });
            setHandledMethod.invoke(event, new Object[] { Boolean.valueOf(handled) });
        }
    }
}
