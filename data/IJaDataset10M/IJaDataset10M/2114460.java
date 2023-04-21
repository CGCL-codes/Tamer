package se.jayway.javafxbinder.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Represent a classpath consisting of a number of URLs.
 * 
 * @author Magnus Robertsson
 */
public class ClassPath {

    private final URL[] urls;

    private final URLClassLoader classLoader;

    /**
	 * Creates a classpath from an array of URLs.
	 * 
	 * @param urls URLs to jar files and/or directories.
	 */
    public ClassPath(URL[] urls) {
        this.urls = urls;
        this.classLoader = new URLClassLoader(urls, ClassPath.class.getClassLoader());
    }

    /**
	 * Parses a classpath from a path separated string.
	 *
	 * @param classPathStr A path separated string, e.g. ".;foo.jar;bar.jar"
	 * 
	 * @return A parsed ClassPath object.
	 * 
	 * @throws MalformedURLException If one of the class path entries was malformed.
	 */
    public static ClassPath parseClassPath(String classPathStr) throws MalformedURLException {
        String[] entries = classPathStr.split(System.getProperty("path.separator"));
        URL[] urls = new URL[entries.length];
        for (int i = 0; i < urls.length; i++) {
            File file = new File(entries[i]);
            String path = file.getAbsolutePath();
            urls[i] = new File(path).toURI().toURL();
        }
        return new ClassPath(urls);
    }

    /**
     * Returns the array of URL that makes up this classpath.
     * 
     * @return An array of URL objects.
     */
    public URL[] getURLs() {
        return urls;
    }

    /**
     * Returns a ClassLoader for this ClassPath.
     * 
     * @return A ClassLoader.
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
	 * Lists all classes matching the specified filter.
	 * 
	 * @param filter Used to filter classes by their names, e.g. "se.jayway".
	 * 
	 * @return A set of class files.
	 * 
	 * @throws IOException If there was a problem reading from the classpath.
	 */
    public Collection<ClassFile> findClassFiles(ClassNameFilter filter) throws IOException {
        List<ClassFile> classes = new ArrayList<ClassFile>();
        for (URL url : urls) {
            Collection<ClassFile> javaClasses = findClassFilesFromURL(url);
            for (ClassFile javaClass : javaClasses) {
                String className = javaClass.getClassName();
                if (filter == null || filter.matches(className)) {
                    classes.add(javaClass);
                } else {
                }
            }
        }
        return classes;
    }

    public Collection<ClassFile> findAllClassFiles() throws IOException {
        return findClassFiles(null);
    }

    private Collection<ClassFile> findClassFilesFromURL(URL url) throws IOException {
        String protocol = url.getProtocol();
        if (protocol.equals("file")) {
            try {
                File file = new File(url.toURI());
                if (file.isDirectory()) {
                    return findClassFilesInDirectory(file);
                } else if (file.getName().endsWith(".jar")) {
                    return findClassFilesInJarFile(file);
                } else {
                    throw new IllegalArgumentException("Unknown file " + file.getAbsolutePath());
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException("Could not convert URL as file: " + url, e);
            }
        } else {
            throw new IllegalArgumentException("Can't handle " + protocol + " URL");
        }
    }

    private Collection<ClassFile> findClassFilesInJarFile(File file) throws IOException {
        List<ClassFile> classes = new ArrayList<ClassFile>();
        JarFile jarFile = new JarFile(file);
        for (Enumeration<JarEntry> iter = jarFile.entries(); iter.hasMoreElements(); ) {
            JarEntry jarEntry = iter.nextElement();
            if (jarEntry.getName().endsWith(".class")) {
                String className = jarEntry.getName().replace('/', '.').substring(0, jarEntry.getName().length() - 6);
                String classUrl = "jar:file:" + file.getAbsolutePath() + "!/" + jarEntry.getName();
                classes.add(new URLClassFile(className, this, classUrl));
            }
        }
        return classes;
    }

    private Collection<ClassFile> findClassFilesInDirectory(File dir) throws IOException {
        List<ClassFile> classes = new ArrayList<ClassFile>();
        findClassFilesInDirectory(dir, null, classes);
        return classes;
    }

    private void findClassFilesInDirectory(File dir, String path, Collection<ClassFile> classes) throws IOException {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                findClassFilesInDirectory(file, addPackagePath(path, file.getName()), classes);
            } else {
                if (file.getName().endsWith(".class")) {
                    String simpleClassName = file.getName().substring(0, file.getName().length() - 6);
                    String className = path == null ? simpleClassName : path + "." + simpleClassName;
                    String classUrl = "file:" + file.getAbsolutePath();
                    classes.add(new URLClassFile(className, this, classUrl));
                }
            }
        }
    }

    private String addPackagePath(String path, String item) {
        return path == null ? item : path + "." + item;
    }
}
