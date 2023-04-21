package net.sourceforge.comeback.prolog2JAR.adaptergen;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.CheckClassAdapter;

/**
 * Helper class used to do byte-code verification after adapters have been 
 * generated. This class expects ASM and the framework classes to be on the 
 * system classpath. Furthermore, the JAR files to be investigated needs to be
 * passed in as command line arguments.
 *
 * @author Michael Rudolf
 */
public class AdapterVerifier {

    /**
     * No instances needed.
     */
    private AdapterVerifier() {
    }

    /**
     * Starts adapter verification by investigating the JAR files at the paths
     * given in the array argument.
     *
     * @param args  the command line arguments pointing to the JAR files to be
     *              investigated
     */
    public static void main(String[] args) throws IOException {
        if (args == null || args.length < 1) {
            System.out.println("Usage: AdapterVerifier <JAR File>");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            File f = new File(args[i]);
            JarFile file = new JarFile(f);
            addToClassPath(f);
            Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    name = name.substring(0, name.length() - 6).replace('/', '.');
                    System.out.println("Verifying " + name);
                    CheckClassAdapter.verify(new ClassReader(file.getInputStream(entry)), false, new PrintWriter(System.out));
                }
            }
        }
    }

    private static void addToClassPath(File file) {
        try {
            URL url = file.toURI().toURL();
            Logger.getLogger(AdapterVerifier.class.getName()).log(Level.INFO, "adding " + file + " to classpath");
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Class clazz = URLClassLoader.class;
            Method method = clazz.getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(classLoader, new Object[] { url });
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
