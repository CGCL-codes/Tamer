package org.streets.context.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.streets.commons.util.IteratorAdapter;
import org.streets.context.ResourceLocator;

public class ResourceLocatorImpl implements ResourceLocator {

    private static final String CLASS_SUFFIX = ".class";

    private final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

    private final ClasspathURLConverter converter;

    public ResourceLocatorImpl(ClasspathURLConverter converter) {
        this.converter = converter;
    }

    public synchronized Collection<Object> locateResources(String folderName, String suffixs, FetchType fetchType) {
        String packagePath = folderName.replace('.', '/') + "/";
        try {
            Set<String> suffix = new HashSet<String>();
            for (String s : suffixs.split(",")) {
                suffix.add(s);
            }
            return findReourcesWithinPath(packagePath, suffix, fetchType);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Collection<Object> findReourcesWithinPath(String folderPath, Set<String> suffix, FetchType fetchType) throws IOException {
        Collection<Object> result = CollectionFactory.newList();
        Enumeration<URL> urls = contextClassLoader.getResources(folderPath);
        IteratorAdapter<URL> iter = new IteratorAdapter<URL>(urls);
        if (iter.hasNext()) {
            URL url = iter.next();
            URL converted = converter.convert(url);
            scanURL(folderPath, result, converted, suffix, fetchType);
        }
        return result;
    }

    private void scanURL(String packagePath, Collection<Object> resources, URL url, Set<String> suffix, FetchType fetchType) throws IOException {
        URLConnection connection = url.openConnection();
        JarFile jarFile;
        if (connection instanceof JarURLConnection) {
            jarFile = ((JarURLConnection) connection).getJarFile();
        } else {
            jarFile = getAlternativeJarFile(url);
        }
        if (jarFile != null) {
            scanJarFile(packagePath, resources, jarFile, suffix, fetchType);
        } else {
            String packageName = packagePath.replace("/", ".");
            if (packageName.endsWith(".")) {
                packageName = packageName.substring(0, packageName.length() - 1);
            }
            scanDir(packageName, new File(url.getFile()), resources, suffix, fetchType);
        }
    }

    private void scanJarFile(String packagePath, Collection<Object> resources, JarFile jarFile, Set<String> suffixs, FetchType fetchType) {
        Enumeration<JarEntry> e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry ze = e.nextElement();
            String name = ze.getName();
            if (!name.startsWith(packagePath)) continue;
            if (name.contains("$")) continue;
            String suffix = FilenameUtils.getExtension(name);
            if (!suffixs.contains(suffix)) continue;
            if (suffix.equalsIgnoreCase("class")) {
                String className = name.substring(0, name.length() - CLASS_SUFFIX.length()).replace("/", ".");
                resources.add(className);
            } else {
                if (fetchType == FetchType.NAME) {
                    resources.add(name);
                } else {
                    try {
                        resources.add(jarFile.getInputStream(ze));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Scan a dir for classes. Will recursively look in the supplied directory and all sub directories.
     *
     * @param folderName         Name of package that this directory corresponds to.
     * @param dir                 Dir to scan for clases.
     * @param componentClassNames List of class names that have been found.
     */
    private void scanDir(String folderName, File dir, Collection<Object> resources, Set<String> suffixs, FetchType fetchType) {
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                String fileName = file.getName();
                if (file.isDirectory()) {
                    scanDir(folderName + "." + fileName, file, resources, suffixs, fetchType);
                } else {
                    String suffix = FilenameUtils.getExtension(fileName);
                    if (!suffixs.contains(suffix)) continue;
                    if (suffix.equalsIgnoreCase("class")) {
                        String className = (folderName + "." + fileName.substring(0, fileName.length() - CLASS_SUFFIX.length())).replace("/", ".");
                        resources.add(className);
                    } else {
                        if (fetchType == FetchType.NAME) {
                            resources.add(file.getAbsolutePath());
                        } else {
                            try {
                                resources.add(new FileInputStream(file));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * For URLs to JARs that do not use JarURLConnection - allowed by the servlet spec - attempt to produce a JarFile
     * object all the same. Known servlet engines that function like this include Weblogic and OC4J. This is not a full
     * solution, since an unpacked WAR or EAR will not have JAR "files" as such.
     *
     * @param url URL of jar
     * @return JarFile or null
     * @throws java.io.IOException If error occurs creating jar file
     */
    private JarFile getAlternativeJarFile(URL url) throws IOException {
        String urlFile = url.getFile();
        int separatorIndex = urlFile.indexOf("!/");
        if (separatorIndex == -1) {
            separatorIndex = urlFile.indexOf('!');
        }
        if (separatorIndex != -1) {
            String jarFileUrl = urlFile.substring(0, separatorIndex);
            if (jarFileUrl.startsWith("file:")) {
                jarFileUrl = jarFileUrl.substring("file:".length());
            }
            return new JarFile(jarFileUrl);
        }
        return null;
    }
}
