package org.form4G.net;

import java.net.URL;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.form4G.net.microServlet.MicroServletHandler;

public class ProtocolFactory implements URLStreamHandlerFactory {

    private static Logger log = Logger.getLogger(ProtocolFactory.class.getName());

    private static Hashtable<String, Class<?>> tableProtocol = null;

    static {
        tableProtocol = new Hashtable<String, Class<?>>();
        tableProtocol.put("microservlet", MicroServletHandler.class);
    }

    public ProtocolFactory() {
    }

    public URLStreamHandler createURLStreamHandler(String protocol) {
        URLStreamHandler rr = null;
        Class<?> handlerClass = tableProtocol.get(protocol);
        if (handlerClass != null) {
            log.log(Level.FINE, protocol + " user handler " + handlerClass.getName());
            try {
                rr = (URLStreamHandler) handlerClass.newInstance();
            } catch (Exception ext) {
                log.log(Level.SEVERE, "in new instance" + handlerClass.getName() + " for protocol " + protocol, ext);
                rr = null;
            }
        }
        return (rr);
    }

    public static void addHandler(String protocol, Class<?> handlerClass) throws InstantiationException {
        if (handlerClass.isAssignableFrom(URLStreamHandler.class)) tableProtocol.put(protocol, handlerClass); else throw new InstantiationException(handlerClass.getName() + "not is assignable from class " + URLStreamHandler.class.getName());
        log.log(Level.FINE, "add handler " + handlerClass.getName() + " for protocol " + protocol);
    }

    public static Class<?> removeHandler(String protocol) {
        Class<?> rr = tableProtocol.remove(protocol);
        log.log(Level.FINE, "removeHandler handler " + rr.getName() + " for protocol " + protocol);
        return (rr);
    }

    public static Enumeration<String> elementsHandler() {
        return (tableProtocol.keys());
    }

    public static void setFactory() {
        ProtocolFactory microServletFactory = new ProtocolFactory();
        URL.setURLStreamHandlerFactory(microServletFactory);
    }
}
