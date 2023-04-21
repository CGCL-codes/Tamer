package javacream.system;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Application
 * 
 * @author Glenn Powell
 * 
 */
public class Application {

    public static int REQUEST_USER_ATTENTION_TYPE_CRITICAL = 1;

    public static int REQUEST_USER_ATTENTION_TYPE_INFORMATIONAL = 2;

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    private Object application;

    private Class<?> applicationListenerClass;

    private ConcurrentHashMap<ApplicationListener, Object> listenerMap = new ConcurrentHashMap<ApplicationListener, Object>();

    private boolean enabledAboutMenu = true;

    private boolean enabledPreferencesMenu;

    private boolean aboutMenuItemPresent = true;

    private boolean preferencesMenuItemPresent;

    public Application(String name) {
        if (isMacOS()) {
            try {
                System.setProperty("com.apple.macos.useScreenMenuBar", "true");
                System.setProperty("com.laf.useScreenMenuBar", "true");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", name);
                System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
                System.setProperty("com.apple.eawt.CocoaComponent.CompatibilityMode", "false");
                final File file = new File("/System/Library/Java");
                if (file.exists()) {
                    ClassLoader scl = ClassLoader.getSystemClassLoader();
                    Class<?> clc = scl.getClass();
                    if (URLClassLoader.class.isAssignableFrom(clc)) {
                        Method addUrl = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
                        addUrl.setAccessible(true);
                        addUrl.invoke(scl, new Object[] { file.toURI().toURL() });
                    }
                }
                Class<?> appClass = Class.forName("com.apple.eawt.Application");
                application = appClass.getMethod("getApplication", new Class[0]).invoke(null, new Object[0]);
                applicationListenerClass = Class.forName("com.apple.eawt.ApplicationListener");
            } catch (ClassNotFoundException e) {
                application = null;
            } catch (IllegalAccessException e) {
                logger.log(Level.WARNING, "Error initializing Application", e);
            } catch (NoSuchMethodException e) {
                logger.log(Level.WARNING, "Error initializing Application", e);
            } catch (InvocationTargetException e) {
                logger.log(Level.WARNING, "Error initializing Application", e);
            } catch (MalformedURLException e) {
                logger.log(Level.WARNING, "Error initializing Application", e);
            }
        }
    }

    public boolean isWindowsOS() {
        return Platform.getPlatform().getOS() == Platform.OS.WINDOWS;
    }

    public boolean isLinuxOS() {
        return Platform.getPlatform().getOS() == Platform.OS.LINUX;
    }

    public boolean isMacOS() {
        return Platform.getPlatform().getOS() == Platform.OS.MAC;
    }

    public void addAboutMenuItem() {
        if (isMacOS()) {
            callMethod(application, "addAboutMenuItem");
        } else {
            this.aboutMenuItemPresent = true;
        }
    }

    public void addApplicationListener(ApplicationListener applicationListener) {
        if (!Modifier.isPublic(applicationListener.getClass().getModifiers())) {
            throw new IllegalArgumentException("ApplicationListener must be a public class");
        }
        if (isMacOS()) {
            Object listener = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { applicationListenerClass }, new ApplicationListenerInvocationHandler(applicationListener));
            callMethod(application, "addApplicationListener", new Class[] { applicationListenerClass }, new Object[] { listener });
            listenerMap.put(applicationListener, listener);
        } else {
            listenerMap.put(applicationListener, applicationListener);
        }
    }

    public void addPreferencesMenuItem() {
        if (isMacOS()) {
            callMethod("addPreferencesMenuItem");
        } else {
            this.preferencesMenuItemPresent = true;
        }
    }

    public boolean getEnabledAboutMenu() {
        if (isMacOS()) {
            return callMethod("getEnabledAboutMenu").equals(Boolean.TRUE);
        } else {
            return enabledAboutMenu;
        }
    }

    public boolean getEnabledPreferencesMenu() {
        if (isMacOS()) {
            Object result = callMethod("getEnabledPreferencesMenu");
            return result.equals(Boolean.TRUE);
        } else {
            return enabledPreferencesMenu;
        }
    }

    public Point getMouseLocationOnScreen() {
        if (isMacOS()) {
            try {
                Method method = application.getClass().getMethod("getMouseLocationOnScreen", new Class[0]);
                return (Point) method.invoke(null, new Object[0]);
            } catch (NoSuchMethodException e) {
                logger.log(Level.WARNING, "Error obtaining mouse location", e);
            } catch (IllegalAccessException e) {
                logger.log(Level.WARNING, "Error obtaining mouse location", e);
            } catch (InvocationTargetException e) {
                logger.log(Level.WARNING, "Error obtaining mouse location", e);
            }
        }
        return new Point(0, 0);
    }

    public boolean isAboutMenuItemPresent() {
        if (isMacOS()) {
            return callMethod("isAboutMenuItemPresent").equals(Boolean.TRUE);
        } else {
            return aboutMenuItemPresent;
        }
    }

    public boolean isPreferencesMenuItemPresent() {
        if (isMacOS()) {
            return callMethod("isPreferencesMenuItemPresent").equals(Boolean.TRUE);
        } else {
            return this.preferencesMenuItemPresent;
        }
    }

    public void removeAboutMenuItem() {
        if (isMacOS()) {
            callMethod("removeAboutMenuItem");
        } else {
            this.aboutMenuItemPresent = false;
        }
    }

    public void removeApplicationListener(ApplicationListener applicationListener) {
        if (isMacOS()) {
            Object listener = listenerMap.get(applicationListener);
            callMethod(application, "removeApplicationListener", new Class[] { applicationListenerClass }, new Object[] { listener });
        }
        listenerMap.remove(applicationListener);
    }

    public void removePreferencesMenuItem() {
        if (isMacOS()) {
            callMethod("removeAboutMenuItem");
        } else {
            this.preferencesMenuItemPresent = false;
        }
    }

    public void setEnabledAboutMenu(boolean enabled) {
        if (isMacOS()) {
            callMethod(application, "setEnabledAboutMenu", new Class[] { Boolean.TYPE }, new Object[] { Boolean.valueOf(enabled) });
        } else {
            this.enabledAboutMenu = enabled;
        }
    }

    public void setEnabledPreferencesMenu(boolean enabled) {
        if (isMacOS()) {
            callMethod(application, "setEnabledPreferencesMenu", new Class[] { Boolean.TYPE }, new Object[] { Boolean.valueOf(enabled) });
        } else {
            this.enabledPreferencesMenu = enabled;
        }
    }

    public int requestUserAttention(int type) {
        if (type != REQUEST_USER_ATTENTION_TYPE_CRITICAL && type != REQUEST_USER_ATTENTION_TYPE_INFORMATIONAL) {
            throw new IllegalArgumentException("Requested user attention type is not allowed: " + type);
        }
        try {
            Object application = getNSApplication();
            Field critical = application.getClass().getField("UserAttentionRequestCritical");
            Field informational = application.getClass().getField("UserAttentionRequestInformational");
            Field actual = type == REQUEST_USER_ATTENTION_TYPE_CRITICAL ? critical : informational;
            return ((Integer) application.getClass().getMethod("requestUserAttention", new Class[] { Integer.TYPE }).invoke(application, new Object[] { actual.get(null) })).intValue();
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
            logger.log(Level.WARNING, "Error requesting user attention", e);
        } catch (IllegalAccessException e) {
            logger.log(Level.WARNING, "Error requesting user attention", e);
        } catch (InvocationTargetException e) {
            logger.log(Level.WARNING, "Error requesting user attention", e);
        } catch (NoSuchFieldException e) {
            logger.log(Level.WARNING, "Error requesting user attention", e);
        }
        return -1;
    }

    public void cancelUserAttentionRequest(int request) {
        try {
            Object application = getNSApplication();
            application.getClass().getMethod("cancelUserAttentionRequest", new Class[] { Integer.TYPE }).invoke(application, new Object[] { new Integer(request) });
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
            logger.log(Level.WARNING, "Error initializing Application", e);
        } catch (IllegalAccessException e) {
            logger.log(Level.WARNING, "Error initializing Application", e);
        } catch (InvocationTargetException e) {
            logger.log(Level.WARNING, "Error initializing Application", e);
        }
    }

    private Object getNSApplication() throws ClassNotFoundException {
        try {
            Class<?> applicationClass = Class.forName("com.apple.cocoa.application.NSApplication");
            return applicationClass.getMethod("sharedApplication", new Class[0]).invoke(null, new Object[0]);
        } catch (IllegalAccessException e) {
            logger.log(Level.WARNING, "Error obtaining NSApplication", e);
        } catch (InvocationTargetException e) {
            logger.log(Level.WARNING, "Error obtaining NSApplication", e);
        } catch (NoSuchMethodException e) {
            logger.log(Level.WARNING, "Error obtaining NSApplication", e);
        }
        return null;
    }

    public void setApplicationIconImage(BufferedImage image) {
        if (isMacOS()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", stream);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error setting Application icon image", e);
            }
            try {
                Class<?> nsDataClass = Class.forName("com.apple.cocoa.foundation.NSData");
                Constructor<?> constructor = nsDataClass.getConstructor(new Class[] { new byte[0].getClass() });
                Object nsData = constructor.newInstance(new Object[] { stream.toByteArray() });
                Class<?> nsImageClass = Class.forName("com.apple.cocoa.application.NSImage");
                Object nsImage = nsImageClass.getConstructor(new Class[] { nsDataClass }).newInstance(new Object[] { nsData });
                Object application = getNSApplication();
                application.getClass().getMethod("setApplicationIconImage", new Class[] { nsImageClass }).invoke(application, new Object[] { nsImage });
            } catch (ClassNotFoundException e) {
            } catch (NoSuchMethodException e) {
                logger.log(Level.WARNING, "Error setting Application icon image", e);
            } catch (IllegalAccessException e) {
                logger.log(Level.WARNING, "Error setting Application icon image", e);
            } catch (InvocationTargetException e) {
                logger.log(Level.WARNING, "Error setting Application icon image", e);
            } catch (InstantiationException e) {
                logger.log(Level.WARNING, "Error setting Application icon image", e);
            }
        }
    }

    public BufferedImage getApplicationIconImage() {
        if (isMacOS()) {
            try {
                Class<?> nsDataClass = Class.forName("com.apple.cocoa.foundation.NSData");
                Class<?> nsImageClass = Class.forName("com.apple.cocoa.application.NSImage");
                Object application = getNSApplication();
                Object nsImage = application.getClass().getMethod("applicationIconImage", new Class[0]).invoke(application, new Object[0]);
                Object nsData = nsImageClass.getMethod("TIFFRepresentation", new Class[0]).invoke(nsImage, new Object[0]);
                Integer length = (Integer) nsDataClass.getMethod("length", new Class[0]).invoke(nsData, new Object[0]);
                byte[] bytes = (byte[]) nsDataClass.getMethod("bytes", new Class[] { Integer.TYPE, Integer.TYPE }).invoke(nsData, new Object[] { Integer.valueOf(0), length });
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
                return image;
            } catch (ClassNotFoundException e) {
            } catch (NoSuchMethodException e) {
                logger.log(Level.WARNING, "Error getting Application icon image", e);
            } catch (IllegalAccessException e) {
                logger.log(Level.WARNING, "Error getting Application icon image", e);
            } catch (InvocationTargetException e) {
                logger.log(Level.WARNING, "Error getting Application icon image", e);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error getting Application icon image", e);
            }
        }
        return null;
    }

    private Object callMethod(String methodname) {
        return callMethod(application, methodname, new Class[0], new Object[0]);
    }

    private Object callMethod(Object object, String methodname) {
        return callMethod(object, methodname, new Class[0], new Object[0]);
    }

    private Object callMethod(Object object, String methodname, Class<?>[] classes, Object[] arguments) {
        try {
            if (classes == null) {
                classes = new Class[arguments.length];
                for (int i = 0; i < classes.length; i++) {
                    classes[i] = arguments[i].getClass();
                }
            }
            Method addListnerMethod = object.getClass().getMethod(methodname, classes);
            return addListnerMethod.invoke(object, arguments);
        } catch (NoSuchMethodException e) {
            logger.log(Level.WARNING, "Error calling Application method", e);
        } catch (IllegalAccessException e) {
            logger.log(Level.WARNING, "Error calling Application method", e);
        } catch (InvocationTargetException e) {
            logger.log(Level.WARNING, "Error calling Application method", e);
        }
        return null;
    }

    private static ApplicationEvent createApplicationEvent(final Object appleApplicationEvent) {
        return (ApplicationEvent) Proxy.newProxyInstance(Application.class.getClassLoader(), new Class[] { ApplicationEvent.class }, new InvocationHandler() {

            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return appleApplicationEvent.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(appleApplicationEvent, objects);
            }
        });
    }

    private static class ApplicationListenerInvocationHandler implements InvocationHandler {

        private ApplicationListener applicationListener;

        ApplicationListenerInvocationHandler(ApplicationListener applicationListener) {
            this.applicationListener = applicationListener;
        }

        public Object invoke(Object object, Method appleMethod, Object[] objects) throws Throwable {
            ApplicationEvent event = createApplicationEvent(objects[0]);
            try {
                Method method = applicationListener.getClass().getMethod(appleMethod.getName(), new Class[] { ApplicationEvent.class });
                return method.invoke(applicationListener, new Object[] { event });
            } catch (NoSuchMethodException e) {
                if (appleMethod.getName().equals("equals") && objects.length == 1) {
                    return Boolean.valueOf(object == objects[0]);
                }
                return null;
            }
        }
    }
}
