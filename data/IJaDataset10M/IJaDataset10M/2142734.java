package gui;

import java.awt.Image;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MacOSXAdapter {

    public static void load() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Hanasu");
        try {
            Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
            Object application = applicationClass.getMethod("getApplication").invoke(null);
            applicationClass.getMethod("setEnabledPreferencesMenu", boolean.class).invoke(application, Boolean.FALSE);
            Image appImage = java.awt.Toolkit.getDefaultToolkit().getImage(MacOSXAdapter.class.getResource("64.png"));
            applicationClass.getMethod("setDockIconImage", Image.class).invoke(application, appImage);
            Class<?> applicationAdapterClass = Class.forName("com.apple.eawt.ApplicationListener");
            Object applicationAdapter = Proxy.newProxyInstance(System.class.getClassLoader(), new Class<?>[] { applicationAdapterClass }, new InvocationHandler() {

                @SuppressWarnings("deprecation")
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (method.getName().equals("handleAbout")) {
                        AboutWindow window = new AboutWindow();
                        window.show();
                    }
                    args[0].getClass().getMethod("setHandled", boolean.class).invoke(args[0], Boolean.TRUE);
                    return null;
                }
            });
            applicationClass.getMethod("addApplicationListener", Class.forName("com.apple.eawt.ApplicationListener")).invoke(application, applicationAdapter);
        } catch (Exception e) {
            System.err.println("Mac OS X integration failed: " + e.getLocalizedMessage() + ".");
        }
    }
}
