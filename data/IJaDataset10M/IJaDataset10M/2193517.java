package org.sss;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 类自动加载
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 377 $ $Date: 2009-03-14 05:31:31 -0400 (Sat, 14 Mar 2009) $
 */
public class SSSClassLoader extends URLClassLoader {

    private static final String PATH_LIB = "lib";

    private static final String PATH_CLASSES = "classes";

    private static final String PATH_RESOURCES = "resources";

    @SuppressWarnings("unchecked")
    public SSSClassLoader(ClassLoader parent, URL url) throws MalformedURLException {
        super(new URL[] {}, parent);
        File libPath = new File(url.getPath(), PATH_LIB);
        if (libPath.exists() && libPath.isDirectory()) {
            File[] libFiles = libPath.listFiles();
            for (int i = 0, length = libFiles.length; i < length; i++) super.addURL(libFiles[i].toURI().toURL());
        }
        File classesPath = new File(url.getPath(), PATH_CLASSES);
        if (classesPath.exists() && classesPath.isDirectory()) super.addURL(classesPath.toURI().toURL());
        File resourcesPath = new File(url.getPath(), PATH_RESOURCES);
        if (resourcesPath.exists() && resourcesPath.isDirectory()) super.addURL(resourcesPath.toURI().toURL());
    }
}
