package net.sf.jpackit.pkg.classloader;

import net.sf.jpackit.pkg.classloader.url.JPackitURLStreamHandlerFactory;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * ClassLoader implementation, this custom classloader implementation  
 * works with jars stored in internal structure of package.
 * @author Kamil K. Shamgunov 
 */
public class JPackitClassLoader extends URLClassLoader {

    /** Creates a new instance of JPackitClassLoader */
    public JPackitClassLoader(URL[] urls) {
        super(urls);
    }

    /** Creates a new instance of JPackitClassLoader */
    public JPackitClassLoader(URL[] urls, ClassLoader parent, JPackitURLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    /** Creates a new instance of JPackitClassLoader */
    public JPackitClassLoader(URL[] urls, ClassLoader parentClassLoader) {
        super(urls, parentClassLoader);
    }
}
