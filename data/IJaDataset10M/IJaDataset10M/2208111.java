package org.netbeans.core.startup.layers;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * This URLStreamHandlerFactory registers the patched handling of
 * <code>file</code> and <code>jar</code> protocol.
 * <p>
 * To have detailed information of the patches included see the documentation
 * of the following classes;
 * <ul>
 * <li> {@link org.netbeans.core.jse141urlpatch.FileURLStreamHandler FileURLStreamHandler}
 * <li> {@link org.netbeans.core.jse141urlpatch.JarURLStreamHandler JarURLStreamHandler}
 * </ul>
 * Both problem exist in JRE 1.4.1, but fixed in JRE 1.4.2 and above.
 * @author Laszlo Kishalmi
 * @see java.net.URL
 * @see java.net.URLStreamHandler
 */
public class PatchedURLStreamHandlerFactory implements URLStreamHandlerFactory {

    private URLStreamHandler fileHandler = new FileURLStreamHandler();

    private URLStreamHandler jarHandler = new JarURLStreamHandler();

    private static PatchedURLStreamHandlerFactory instance = new PatchedURLStreamHandlerFactory();

    private PatchedURLStreamHandlerFactory() {
    }

    /**
     * Returns the patched <code>URLStreamHandler</code> instance in case of the 
     * protocol is <code>file</code> or <code>jar</code>. In other cases it returns 
     * <code>null</code> which implices that JVM will use the default protocol handler 
     * for the given protocol.
     * @param protocol The name of the protocol.
     * @return The patched <code>URLStreamHandler</code> instance in case of the 
     * protocol is <code>file</code> or <code>jar</code>, <code>null</code> otherwise.
     */
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if ("file".equals(protocol)) return fileHandler;
        if ("jar".equals(protocol)) return jarHandler;
        return null;
    }

    /**
     * Returns the Singleton instance of <code>PatchedURLStreamHandlerFactory</code> in
     * the current JVM.
     * @return The Singleton instance of <code>PatchedURLStreamHandlerFactory</code> in
     * the current JVM.
     */
    public static PatchedURLStreamHandlerFactory getInstance() {
        return instance;
    }
}
