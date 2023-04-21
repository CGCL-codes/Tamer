package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassPathScanner {

    private static final String PROTOCOL_FILE = "file";

    private static final String PROTOCOL_JAR = "jar";

    private static final String PREFIX_FILE = "file:";

    private static final String JAR_URL_SEPERATOR = "!/";

    private static final String CLASS_FILE = ".class";

    private final String packageName;

    private final ClassFilter filter;

    public ClassPathScanner(String packageName) {
        this(packageName, null);
    }

    public ClassPathScanner(String packageName, ClassFilter filter) {
        this.packageName = packageName;
        this.filter = filter;
    }

    public List<Class<?>> scan() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        Enumeration<URL> en = null;
        try {
            en = getClass().getClassLoader().getResources(dotToPath(packageName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (en.hasMoreElements()) {
            URL url = en.nextElement();
            if (PROTOCOL_FILE.equals(url.getProtocol())) {
                File root = new File(url.getFile());
                findInDirectory(list, root, root, packageName);
            } else if (PROTOCOL_JAR.equals(url.getProtocol())) {
                findInJar(list, getJarFile(url), packageName);
            }
        }
        return list;
    }

    public File getJarFile(URL url) {
        String file = url.getFile();
        if (file.startsWith(PREFIX_FILE)) file = file.substring(PREFIX_FILE.length());
        int end = file.indexOf(JAR_URL_SEPERATOR);
        if (end != (-1)) file = file.substring(0, end);
        return new File(file);
    }

    void findInJar(List<Class<?>> results, File file, String packageName) {
        JarFile jarFile = null;
        String packagePath = dotToPath(packageName) + "/";
        try {
            jarFile = new JarFile(file);
            Enumeration<JarEntry> en = jarFile.entries();
            while (en.hasMoreElements()) {
                JarEntry je = en.nextElement();
                String name = je.getName();
                if (name.startsWith(packagePath) && name.endsWith(CLASS_FILE)) {
                    String className = name.substring(0, name.length() - CLASS_FILE.length());
                    add(results, pathToDot(className));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                }
            }
        }
    }

    void findInDirectory(List<Class<?>> results, File rootDir, File dir, String packageName) {
        File[] files = dir.listFiles();
        String rootPath = rootDir.getPath();
        for (File file : files) {
            if (file.isFile()) {
                String classFileName = file.getPath();
                if (classFileName.endsWith(CLASS_FILE)) {
                    String className = classFileName.substring(rootPath.length() - packageName.length(), classFileName.length() - CLASS_FILE.length());
                    add(results, pathToDot(className));
                }
            } else if (file.isDirectory()) {
                findInDirectory(results, rootDir, file, packageName);
            }
        }
    }

    void add(List<Class<?>> results, String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            return;
        }
        if (filter == null || filter.accept(clazz)) results.add(clazz);
    }

    String dotToPath(String s) {
        return s.replace('.', '/');
    }

    String pathToDot(String s) {
        return s.replace('/', '.').replace('\\', '.');
    }
}
