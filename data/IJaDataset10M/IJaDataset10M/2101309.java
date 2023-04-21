package common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

/**
 * 
 * @author Silvio Donnini
 */
public class ClassPathLoader {

    private static ClassPathLoader instance;

    static {
        instance = new ClassPathLoader();
    }

    public static ClassPathLoader getInstance() {
        return instance;
    }

    private final Class[] parameters = new Class[] { URL.class };

    public void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }

    public void addFile(File f) throws IOException {
        addURL(f.toURI().toURL());
    }

    public void addURL(URL u) throws IOException {
        URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysClass = URLClassLoader.class;
        try {
            Method method = sysClass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(loader, new Object[] { u });
        } catch (Throwable t) {
            Logger.getLogger("application").warning("Error adding URL to classpath");
            throw new IOException("Error, could not add URL to classpath");
        }
    }
}
