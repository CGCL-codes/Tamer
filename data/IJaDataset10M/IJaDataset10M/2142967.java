package org.jtools.net;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;

public class HandlerUtils {

    private static final Logger LOG = Logger.getLogger(HandlerUtils.class.getName());

    private static final class AppendableFactory implements URLStreamHandlerFactory {

        private final ArrayList<URLStreamHandlerFactory> factories = new ArrayList<URLStreamHandlerFactory>();

        public java.net.URLStreamHandler createURLStreamHandler(String protocol) {
            URLStreamHandler result;
            for (URLStreamHandlerFactory factory : factories) {
                result = factory.createURLStreamHandler(protocol);
                if (result != null) return result;
            }
            return null;
        }

        public void add(URLStreamHandlerFactory factory) {
            SecurityManager security = System.getSecurityManager();
            if (security != null) security.checkSetFactory();
            factories.add(factory);
        }
    }

    private static final class TrivialFactory implements URLStreamHandlerFactory {

        private final String protocol;

        private final URLStreamHandler urlStreamHandler;

        public TrivialFactory(String protocol, URLStreamHandler urlStreamHandler) {
            this.protocol = protocol;
            this.urlStreamHandler = urlStreamHandler;
        }

        public URLStreamHandler createURLStreamHandler(String protocol) {
            return protocol.equals(this.protocol) ? urlStreamHandler : null;
        }
    }

    private static Throwable installNormal(final AppendableFactory appendable) {
        try {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {

                public Object run() {
                    URL.setURLStreamHandlerFactory(appendable);
                    return null;
                }
            });
            return null;
        } catch (Throwable e) {
            return e;
        }
    }

    private static Throwable installReplaceExisting(final AppendableFactory appendable) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {

                public Object run() throws SecurityException, NoSuchFieldException, IllegalAccessException {
                    SecurityManager security = System.getSecurityManager();
                    if (security != null) security.checkSetFactory();
                    Class<URL> urlClass = URL.class;
                    Field streamHandlerLockField = urlClass.getDeclaredField("streamHandlerLock");
                    boolean streamHandlerLockFieldAccessible = streamHandlerLockField.isAccessible();
                    if (!streamHandlerLockFieldAccessible) streamHandlerLockField.setAccessible(true);
                    Field factoryField = urlClass.getDeclaredField("factory");
                    boolean factoryFieldAccessible = factoryField.isAccessible();
                    if (!factoryFieldAccessible) factoryField.setAccessible(true);
                    Field handlersField = urlClass.getDeclaredField("handlers");
                    boolean handlersFieldAccessible = handlersField.isAccessible();
                    if (!handlersFieldAccessible) handlersField.setAccessible(true);
                    Object streamHandlerLock = streamHandlerLockField.get(null);
                    Hashtable handlers = (Hashtable) handlersField.get(null);
                    synchronized (streamHandlerLock) {
                        URLStreamHandlerFactory old = (URLStreamHandlerFactory) factoryField.get(null);
                        factoryField.set(null, appendable);
                        appendable.add(old);
                        handlers.clear();
                    }
                    if (!handlersFieldAccessible) handlersField.setAccessible(false);
                    if (!factoryFieldAccessible) factoryField.setAccessible(false);
                    if (!streamHandlerLockFieldAccessible) streamHandlerLockField.setAccessible(false);
                    return null;
                }
            });
            return null;
        } catch (PrivilegedActionException e) {
            return e.getCause();
        } catch (Throwable e) {
            return e;
        }
    }

    private static AppendableFactory install() {
        final AppendableFactory appendable = new AppendableFactory();
        Throwable installResult = null;
        installResult = installNormal(appendable);
        if (installResult == null) {
            LOG.fine("Appendable URLStreamHandlerFactory installed.");
            return appendable;
        }
        LOG.fine("Regular installation of appendable URLStreamHandlerFactory failed. Reason: " + installResult.toString());
        installResult = installReplaceExisting(appendable);
        if (installResult == null) {
            LOG.fine("Appendable URLStreamHandlerFactory installed. Existing URLStreamHandlerFactory wrapped.");
            return appendable;
        }
        LOG.fine("Wrapping installation of appendable URLStreamHandlerFactory failed. Reason: " + installResult.toString());
        LOG.warning("Installation of appendable URLStreamHandlerFactory failed.");
        return null;
    }

    private static final AppendableFactory installed = install();

    private static boolean installWithAppendable(final URLStreamHandlerFactory factory) {
        try {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {

                public Object run() {
                    installed.add(factory);
                    return null;
                }
            });
            return true;
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean install(String protocol, URLStreamHandler handler) {
        if (installed != null) return installWithAppendable(new TrivialFactory(protocol, handler));
        return false;
    }

    public static boolean install(URLStreamHandlerFactory handlerFactory) {
        if (installed != null) return installWithAppendable(handlerFactory);
        return false;
    }
}
