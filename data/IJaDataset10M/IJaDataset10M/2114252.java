package org.tuotoo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.StringTokenizer;
import java.net.URL;
import java.lang.reflect.*;
import java.io.*;
import java.net.*;

/**
 * This class performs some basic operations related to Class objects.
 * 
 * @author Rolf Wendolsky
 */
public final class ClassUtil {

    private static final String JAR_FILE = "jar:file:";

    private static final String FILE = "file:";

    /**
     * Stores all loaded classes.
     */
    private static Hashtable ms_loadedClasses = new Hashtable();

    /**
     * Stores all loaded directories.
     */
    private static Vector ms_loadedDirectories = new Vector();

    /**
     * This class works without being initialised and is completely static.
     * Therefore, the constructor is not needed and private.
     */
    private ClassUtil() {
    }

    /**
     * Represents a package name.
     * 
     * @see http://java.sun.com/docs/books/jls/second_edition/html/packages.doc.html
     */
    public static final class Package {

        private String m_strPackage;

        public Package(Class a_class) {
            if (a_class == null || a_class.getName().indexOf(".") < 0) {
                m_strPackage = "";
            } else {
                m_strPackage = a_class.getName().substring(0, a_class.getName().lastIndexOf("."));
            }
        }

        public Package(String a_strPackage) {
            if (m_strPackage == null || m_strPackage.trim().length() == 0) {
                m_strPackage = "";
            } else {
                if (new StringTokenizer(m_strPackage).countTokens() > 1) {
                    throw new IllegalArgumentException("Package names may not contain whitespaces!");
                } else {
                    for (int i = 0; i < m_strPackage.length(); i++) {
                        if (Character.isLetterOrDigit(m_strPackage.charAt(i)) || m_strPackage.charAt(i) == '.') {
                            continue;
                        } else if (m_strPackage.charAt(i) == '\\' && m_strPackage.length() > (i + 5) && m_strPackage.charAt(i + 1) == 'u') {
                            boolean bUnicode = true;
                            for (int j = i + 2; j < (i + 5); j++) {
                                if (!Character.isDigit(m_strPackage.charAt(j))) {
                                    bUnicode = false;
                                    break;
                                }
                            }
                            if (bUnicode) {
                                i += 5;
                                continue;
                            }
                        }
                        throw new IllegalArgumentException("Illegal character in package name: " + m_strPackage.charAt(i));
                    }
                    m_strPackage = a_strPackage;
                }
            }
        }

        public String getPackage() {
            return m_strPackage;
        }
    }

    public static void addFileToClasspath(String a_file) throws IOException, IllegalAccessException {
        File f = new File(a_file);
        addFileToClasspath(f);
    }

    public static void addFileToClasspath(File a_file) throws IllegalAccessException {
        URL url;
        try {
            url = (URL) File.class.getMethod("toURL", new Class[0]).invoke(a_file, new Object[0]);
        } catch (Exception ex) {
            throw new IllegalAccessException(ex.getMessage());
        }
        addURLToClasspath(url);
    }

    public static void addURLToClasspath(URL a_url) throws IllegalAccessException {
        Object urlClassLoader;
        Class sysclass;
        try {
            urlClassLoader = ClassLoader.class.getMethod("getSystemClassLoader", new Class[0]).invoke((Object) null, new Object[0]);
            sysclass = Class.forName("java.net.URLClassLoader");
            Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
            Method.class.getMethod("setAccessible", new Class[] { boolean.class }).invoke(method, new Object[] { new Boolean(true) });
            method.invoke(urlClassLoader, new Object[] { a_url });
        } catch (Throwable t) {
            throw new IllegalAccessException("Error, could not add URL to system classloader");
        }
    }

    /**
     * Gets the name of a class without package (everything before the last "."
     * is removed).
     * 
     * @param a_class
     *            a Class
     * @return the name of the class without package
     */
    public static String getShortClassName(Class a_class) {
        String classname = a_class.getName();
        int pointIndex;
        pointIndex = classname.lastIndexOf('.');
        if (pointIndex >= 0) {
            classname = classname.substring(pointIndex + 1, classname.length());
        }
        return classname;
    }

    /**
     * Returns the current class from a static context. This method is a
     * replacement to this.getClass() in a static environment, as <Code>this</Code>
     * is not available there.
     * 
     * @return the current class
     */
    public static Class getClassStatic() {
        return new ClassGetter().getCurrentClassStatic();
    }

    /**
     * Returns the name, including the package, of the calling method's class.
     * 
     * @return the name, including the package, of the calling method's class
     */
    public static String getClassNameStatic() {
        return getCallingClassStatic().getName();
    }

    /**
     * Returns the class that called the current method. This method is an
     * alternative to Object.getClass(), as the caller is not needed to be an
     * argument or an Object either.
     * 
     * @return the class that called the current method
     */
    public static Class getCallingClassStatic() {
        return new ClassGetter().getCallingClassStatic();
    }

    /**
     * Returns the content of the system property user.dir
     * 
     * @return the content of the system property user.dir
     */
    public static String getUserDir() {
        try {
            return System.getProperty("user.dir");
        } catch (SecurityException a_e) {
            return new File(".").toString();
        }
    }

    /**
     * Returns the current java class path.
     * 
     * @return the current java class path
     */
    public static String getClassPath() {
        return getClassPath(false);
    }

    /**
     * Gets all classes that extend the given class or implement the given
     * interface, including the class itself. It is recommended to store this
     * information somewhere and reuse it instead of calling this method again,
     * as all known classes have to be searched at each call. If classes from
     * special packages should be found, it is recommended to load those
     * packages at program start. Otherwise, some classes in those packages,
     * that inherit from classes in this or other packages, can not be found.
     * WARNING: this may be slow at the first call, especially for large
     * packages
     * 
     * @param a_class
     *            a Class
     * @return all known subclasses of the given class
     */
    public static Vector findSubclasses(Class a_class) {
        Enumeration classes;
        Vector subclasses;
        Class possibleSubclass;
        loadClasses(a_class);
        classes = loadClasses(getCallingClassStatic());
        subclasses = new Vector();
        while (classes.hasMoreElements()) {
            possibleSubclass = (Class) classes.nextElement();
            if (a_class.isAssignableFrom(possibleSubclass)) {
                subclasses.addElement(possibleSubclass);
            }
        }
        return subclasses;
    }

    /**
     * Loads all classes into cache that are in the same file structure as this
     * class and as the calling class. Recommended to be called at program start
     * to initially fill the cache. WARNING: this may be slow at the first call,
     * especially for large packages (like the JRE)
     * 
     * @return all loaded classes
     */
    public static Enumeration loadClasses() {
        Class callingClass;
        callingClass = getCallingClassStatic();
        loadClasses(callingClass);
        return ms_loadedClasses.elements();
    }

    public static Enumeration loadClasses(Class a_rootClass) {
        return loadClasses(a_rootClass, null);
    }

    public static Enumeration loadClasses(File a_classDirectory) {
        return loadClasses(null, a_classDirectory);
    }

    /**
     * Loads all classes into cache that are in the same file structure as the
     * given class and as the calling class. WARNING: this may be slow at the
     * first call, especially for large packages (like the JRE)
     * 
     * @param a_rootClass
     *            the class from that loading is started
     * @return all loaded classes
     */
    private static Enumeration loadClasses(Class a_rootClass, File a_directory) {
        PrintStream syserror;
        PrintStream dummyStream = new PrintStream(new ByteArrayOutputStream());
        Class thisClass, callingClass;
        thisClass = getClassStatic();
        callingClass = getCallingClassStatic();
        syserror = System.err;
        try {
            if (a_directory != null) {
                System.setErr(dummyStream);
                loadClassesInternal(a_rootClass, a_directory);
                System.setErr(syserror);
            } else if (a_rootClass != null) {
                System.setErr(dummyStream);
                loadClassesInternal(a_rootClass, a_directory);
                System.setErr(syserror);
                loadClassesInternal(thisClass, null);
                if (callingClass != a_rootClass && callingClass != thisClass) {
                    loadClassesInternal(callingClass, null);
                }
            }
        } catch (Throwable a_e) {
            System.setErr(syserror);
            if (a_e instanceof Exception && !(a_e instanceof RuntimeException)) {
                a_e.printStackTrace();
            }
        }
        return ms_loadedClasses.elements();
    }

    public static File getClassDirectory(String a_className) {
        if (a_className == null) {
            return null;
        }
        try {
            return getClassDirectory(Class.forName(a_className));
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    /**
     * Returns the class directory of the specified class. The class directory
     * is either the directory in that the highest package in the package
     * structure of the class is contained, or the jar-File in that the class is
     * contained. For extracting the contents of a jar-File, see
     * {@link java.util.zip.ZipFile}.
     * 
     * @param a_class
     *            a class
     * @return the class directory of the specified class, either a real
     *         directory or a Jar-file or null if the directory/jar-file does
     *         not exist
     */
    public static File getClassDirectory(Class a_class) {
        String classResource;
        String classDirectory;
        URL classUrl;
        File file;
        classResource = "/" + toRelativeResourcePath(a_class);
        classUrl = a_class.getResource(classResource);
        file = ResourceLoader.getSystemResource(classUrl.toString());
        if (file == null) {
            classDirectory = URLDecoder.decode(classUrl.toString());
            if (classDirectory.startsWith(JAR_FILE)) {
                classDirectory = classDirectory.substring(JAR_FILE.length(), classDirectory.lastIndexOf(classResource) - 1);
                if (classDirectory.charAt(2) == ':') {
                    classDirectory = classDirectory.substring(1, classDirectory.length());
                }
                classDirectory = ResourceLoader.replaceFileSeparatorsSystemSpecific(classDirectory);
                file = new File(classDirectory);
            } else if (classDirectory.startsWith(FILE)) {
                classDirectory = classDirectory.substring(FILE.length(), classDirectory.lastIndexOf(classResource));
                file = new File(classDirectory);
            } else {
                file = null;
            }
            if (file == null || !file.exists()) {
                return null;
            }
        }
        return file;
    }

    /**
     * Generates a relative resource path to the given class.
     * 
     * @param a_class
     *            Class
     * @return URL
     */
    public static String toRelativeResourcePath(Class a_class) {
        String classResource;
        classResource = a_class.getName();
        classResource = classResource.replace('.', '/');
        classResource += ".class";
        return classResource;
    }

    /**
     * Traverse a file, directory or zip/jar file recursive until a class file
     * is found and instantiated or all files are traversed. The instantiated
     * class is returned.
     * 
     * @param a_file
     *            a file, directory or zip/jar file
     * @return the first class that was instnatiated or null if no class could
     *         be found and instantiated
     */
    public static Class getFirstClassFound(File a_file) {
        Hashtable classInstance = new Hashtable();
        ResourceLoader.loadResources("/", a_file, new ClassInstantiator(3), true, true, classInstance);
        if (classInstance.size() == 1) {
            return (Class) classInstance.elements().nextElement();
        }
        return null;
    }

    /**
     * Returns the current java class path.
     * 
     * @param a_bPreventLoop
     *            true if a loop with calls in the ResourceLoader must be
     *            prevented
     * @return the current java class path
     */
    protected static String getClassPath(boolean a_bPreventLoop) {
        String thisClass = "";
        if (!a_bPreventLoop) {
            try {
                thisClass = getClassDirectory(ClassUtil.class).toString() + File.pathSeparator;
            } catch (Exception a_e) {
            }
        }
        try {
            return thisClass + System.getProperty("java.class.path");
        } catch (SecurityException a_e) {
            return thisClass;
        }
    }

    /**
     * This small inner class is needed to get information about static classes.
     */
    private static class ClassGetter extends SecurityManager {

        public Class getCurrentClassStatic() {
            return getClassContext()[2];
        }

        public Class getCallingClassStatic() {
            return getClassContext()[3];
        }
    }

    /**
     * Loads all classes into cache that are in the same file structure as the
     * given class. WARNING: this may be slow at the first call, especially for
     * large packages (like the JRE)
     * 
     * @param a_rootClass
     *            the class from that loading is started
     * @param an
     *            optional directory or jar/zip file to load the classes from
     * @throws IOException
     *             if an I/O error occurs
     */
    private static void loadClassesInternal(Class a_rootClass, File a_directory) throws IOException {
        File file;
        if (a_directory != null) {
            file = a_directory;
        } else if (a_rootClass == null || a_rootClass.getName().startsWith("java.") || a_rootClass.getName().startsWith("javax.") || (file = getClassDirectory(a_rootClass)) == null) {
            return;
        }
        if (ms_loadedDirectories.contains(file.getAbsolutePath())) {
            return;
        }
        ms_loadedDirectories.addElement(file.getAbsolutePath());
        ResourceLoader.loadResources("/", file, new ClassInstantiator(), true, false, ms_loadedClasses);
    }

    /**
     * Turns class files into Class objects.
     * 
     * @param a_classFile
     *            a class file with full directory path
     * @param a_classDirectory
     *            the directory where all class files and class directories
     *            reside
     * @return the class file as Class object
     */
    private static Class toClass(File a_classFile, File a_classDirectory) {
        Class classObject;
        String className;
        String classDirectory;
        int startIndex;
        if (a_classFile == null || !a_classFile.getName().endsWith(".class")) {
            return null;
        }
        if (a_classDirectory == null || !a_classDirectory.isDirectory()) {
            startIndex = 0;
        } else {
            classDirectory = a_classDirectory.toString();
            if (classDirectory.endsWith(System.getProperty("file.separator"))) {
                startIndex = classDirectory.length();
            } else {
                startIndex = classDirectory.length() + 1;
            }
        }
        try {
            className = a_classFile.toString();
            className = className.substring(startIndex, className.lastIndexOf(".class"));
            className = className.replace(File.separatorChar, '.');
            classObject = Class.forName(className);
        } catch (Throwable a_e) {
            classObject = null;
        }
        return classObject;
    }

    private static class ClassInstantiator implements IResourceInstantiator {

        private int m_invalidAfterFailure;

        private int m_currentFailure;

        public ClassInstantiator() {
            m_invalidAfterFailure = 0;
            m_currentFailure = 0;
        }

        public ClassInstantiator(int a_invalidAfterFailure) {
            m_invalidAfterFailure = a_invalidAfterFailure;
            m_currentFailure = 0;
        }

        public Object getInstance(File a_file, File a_topDirectory) throws IResourceInstantiator.ResourceInstantiationException {
            Class loadedClass = toClass(a_file, a_topDirectory);
            if (m_invalidAfterFailure > 0) {
                checkValidity(loadedClass, a_file.getName());
            }
            return loadedClass;
        }

        public Object getInstance(ZipEntry a_entry, ZipFile a_file) throws IResourceInstantiator.ResourceInstantiationException {
            Class loadedClass = toClass(new File((a_entry).toString()), (File) null);
            if (m_invalidAfterFailure > 0) {
                checkValidity(loadedClass, a_entry.getName());
            }
            return loadedClass;
        }

        private void checkValidity(Class a_loadedClass, String a_filename) throws IResourceInstantiator.ResourceInstantiationException {
            if (a_loadedClass == null && a_filename.endsWith(".class")) {
                m_currentFailure++;
            }
            if (m_currentFailure >= m_invalidAfterFailure) {
                throw new IResourceInstantiator.ResourceInstantiationException();
            }
        }
    }
}
