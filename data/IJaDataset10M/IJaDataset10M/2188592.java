package org.gudy.azureus2.core3.util.protocol;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * @author Aaron Grunthal
 * @create 30.03.2008
 */
public class AzURLStreamHandlerFactory implements URLStreamHandlerFactory {

    private static final String packageName = AzURLStreamHandlerFactory.class.getPackage().getName();

    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (protocol.equals("file") || protocol.equals("jar")) return null;
        String clsName = packageName + "." + protocol + ".Handler";
        try {
            Class cls = Class.forName(clsName);
            return (URLStreamHandler) cls.newInstance();
        } catch (Throwable e) {
        }
        return null;
    }
}
