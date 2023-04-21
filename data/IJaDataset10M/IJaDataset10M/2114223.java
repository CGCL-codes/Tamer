package org.skippyjon.flickoblog.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Map;
import java.util.Collections;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.io.IOException;
import java.io.File;

/**
 * ImplementationsFinder
 *
 * @author Alexandre Normand
 *         Date: 20-Mar-2008
 * @version 1.0
 * @TODO Description
 * <p/>
 * <p>Copyright: Copyright (c) 2008 Alexandre Normand
 * http://flickr.com/people/alex_normand/ </p>
 * <p/>
 * <p>
 * The contents of this file are used with permission, subject to
 * the Mozilla Public License Version 1.1 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * http://www.mozilla.org/MPL/MPL-1.1.html
 * </p>
 * <p>
 * Software distributed under the License is distributed on an
 * "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 * </p>
 */
public abstract class ImplementationsFinder {

    private static final Log LOGGER = LogFactory.getLog(ImplementationsFinder.class);

    public static List<Class> findImplementations(ClassLoader aClassLoader, Class aInterface) throws Exception {
        List<Class> implementations = new ArrayList<Class>();
        Object[] classPaths;
        try {
            classPaths = ((java.net.URLClassLoader) aClassLoader).getURLs();
        } catch (ClassCastException cce) {
            classPaths = System.getProperty("java.class.path", "").split(File.pathSeparator);
        }
        for (int i = 0; i < classPaths.length; i++) {
            Enumeration files = null;
            JarFile module = null;
            File classPath = new File((URL.class).isInstance(classPaths[i]) ? ((URL) classPaths[i]).getFile() : classPaths[i].toString());
            if (classPath.isDirectory()) {
                List<String> dirListing = new ArrayList();
                recursivelyListDir(dirListing, classPath, new StringBuffer());
                files = Collections.enumeration(dirListing);
            } else if (classPath.getName().endsWith(".jar")) {
                try {
                    module = new JarFile(classPath);
                } catch (MalformedURLException mue) {
                    throw new ClassNotFoundException("Bad classpath. Error: " + mue.getMessage());
                } catch (IOException io) {
                    throw new ClassNotFoundException("jar file '" + classPath.getName() + "' could not be instantiate from file path. Error: " + io.getMessage());
                }
                files = module.entries();
            }
            while (files != null && files.hasMoreElements()) {
                String fileName = files.nextElement().toString();
                if (fileName.endsWith(".class")) {
                    String className = fileName.replaceAll("/", ".").substring(0, fileName.length() - 6);
                    LOGGER.debug("Processing class: " + className);
                    Class classReference = null;
                    try {
                        classReference = Class.forName(className, false, aClassLoader);
                    } catch (NoClassDefFoundError e) {
                        LOGGER.debug("Skipping class '" + className + "' for reason " + e.getMessage());
                        continue;
                    }
                    if (classReference.isInterface()) {
                        continue;
                    }
                    if (aInterface.isAssignableFrom(classReference)) {
                        implementations.add(classReference);
                    }
                }
            }
            if (module != null) {
                try {
                    module.close();
                } catch (IOException ioe) {
                    throw new ClassNotFoundException("The module jar file '" + classPath.getName() + "' could not be closed. Error: " + ioe.getMessage());
                }
            }
        }
        return implementations;
    }

    /**
   * Recursively lists a directory while generating relative paths. This is a helper function for findClasses.
   * Note: Uses a StringBuffer to avoid the excessive overhead of multiple String concatentation
   *
   * @param dirListing     A list variable for storing the directory listing as a list of Strings
   * @param dir                 A File for the directory to be listed
   * @param relativePath A StringBuffer used for building the relative paths
   */
    private static void recursivelyListDir(List<String> dirListing, File dir, StringBuffer relativePath) {
        int prevLen;
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                prevLen = relativePath.length();
                recursivelyListDir(dirListing, file, relativePath.append(prevLen == 0 ? "" : "/").append(file.getName()));
                relativePath.delete(prevLen, relativePath.length());
            }
        } else {
            dirListing.add(relativePath.toString());
        }
    }
}
