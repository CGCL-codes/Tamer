package org.opt4j.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * The {@link ClassPath} is also known as the {@code ClassPathHacker} and
 * enables adding new files to the classpath.
 * 
 * @author lukasiewycz
 * 
 */
public class ClassPath {

    private static final Class<?>[] parameters = new Class[] { URL.class };

    /**
	 * Adds a new {@link File} to the classpath.
	 * 
	 * @param s
	 *            the name of the file
	 * @throws IOException
	 */
    public static void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }

    /**
	 * Adds a new {@link File} to the classpath.
	 * 
	 * @param f
	 *            the file
	 * @throws IOException
	 */
    @SuppressWarnings("deprecation")
    public static void addFile(File f) throws IOException {
        addURL(f.toURL());
    }

    /**
	 * Adds a new {@link File} to the classpath.
	 * 
	 * @param u
	 *            the {@link URL} of the file
	 * @throws IOException
	 */
    public static void addURL(URL u) throws IOException {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { u });
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }
}
