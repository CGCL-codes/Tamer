package br.com.framework.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import br.com.framework.EtexProperties;

public class ScannerClass {

    private static List<String> classes = new ArrayList<String>();

    private static Map<String, Object> mapDiretorios = new HashMap<String, Object>();

    public static List<String> getAllClassApp() {
        if (getClasses().size() == 0) {
            return scan(Thread.currentThread().getContextClassLoader(), Collections.EMPTY_SET, Collections.EMPTY_SET);
        } else {
            return getClasses();
        }
    }

    private static List<String> scan(ClassLoader classLoader, Set<String> locations, Set<String> packages) {
        if (!(classLoader instanceof URLClassLoader)) {
            return null;
        }
        URLClassLoader urlLoader = (URLClassLoader) classLoader;
        URL[] urls = urlLoader.getURLs();
        for (URL url : urls) {
            String path = url.getFile();
            File location = null;
            try {
                location = new File(url.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
            if (matchesAny(path, locations)) {
                if (location.isDirectory()) {
                    getClassesInDirectory(null, location, packages);
                }
            }
        }
        return getClasses();
    }

    private static void getClassesInDirectory(String parent, File location, Set<String> packagePatterns) {
        File[] files = location.listFiles();
        StringBuilder builder = null;
        for (File file : files) {
            builder = new StringBuilder(100);
            if (file.isDirectory() || file.getName().equals(EtexProperties.CONFIGURATION_PROPERTIES)) {
                getMapDiretorios().put(file.getName(), file);
            }
            builder.append(parent).append("/").append(file.getName());
            String packageOrClass = (parent == null ? file.getName() : builder.toString());
            if (file.isDirectory()) {
                getClassesInDirectory(packageOrClass, file, packagePatterns);
            } else if (file.getName().endsWith(".class")) {
                if (matchesAny(packageOrClass, packagePatterns)) {
                    getClasses().add(packageOrClass);
                }
            }
        }
    }

    private static boolean matchesAny(String text, Set<String> filters) {
        if (filters == null || filters.size() == 0) {
            return true;
        }
        for (String filter : filters) {
            if (text.indexOf(filter) != -1) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        List<String> classes = getAllClassApp();
        File file = (File) getMapDiretorios().get(EtexProperties.CONFIGURATION_PROPERTIES);
        System.out.println(file.getAbsolutePath());
    }

    private static List<String> getClasses() {
        return classes;
    }

    public static Map<String, Object> getMapDiretorios() {
        return mapDiretorios;
    }

    public static void setMapDiretorios(Map<String, Object> mapDiretorios) {
        ScannerClass.mapDiretorios = mapDiretorios;
    }
}
