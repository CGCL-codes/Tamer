package com.levigo.jbig2.util.log;

/**
 * @author <a href="mailto:m.krzikalla@levigo.de">Matthäus Krzikalla</a>
 */
public class JDKLoggerBridge implements LoggerBridge {

    public Logger getLogger(Class<?> classToBeLogged) {
        return new JDKLogger(java.util.logging.Logger.getLogger(classToBeLogged.getName()));
    }
}
