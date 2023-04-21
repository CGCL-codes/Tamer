package org.granite.osgi.classloador;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.granite.logging.Logger;
import org.granite.messaging.service.annotations.RemoteDestination;
import org.osgi.framework.Bundle;

/**
 * Granite DataService classloader
 * scan packages then load qualified GDS classes.
 * @author <a href="mailto:gembin@gmail.com">gembin@gmail.com</a>
 * @since 1.1.0
 */
public class ServiceClassLoader {

    private static final Logger log = Logger.getLogger(ServiceClassLoader.class);

    private static final String CLASS_SUFFIX = ".class";

    private Set<String> classesSet = new HashSet<String>();

    /**
	 * a Bundle which is used to load classes
	 */
    private Bundle bundle;

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    /**
	 * @param className
	 * @return path of a class
	 */
    private static String packageForPath(String className) {
        return className.replace('.', '/');
    }

    /**
	 * @param path
	 * @return package of a class
	 */
    private static String pathForPackage(String path) {
        return path.replace('/', '.').replace('\\', '.');
    }

    /**
	 * resolve package to find all the classes in a specified package of a bundle
	 * @param packageNamePath
	 * @param recursive
	 */
    private void resolvePackage(String packageNamePath, boolean recursive) {
        @SuppressWarnings("unchecked") Enumeration<String> en = bundle.getEntryPaths(packageNamePath);
        if (en != null) {
            while (en.hasMoreElements()) {
                String entryPath = en.nextElement();
                if (recursive && entryPath.endsWith("/")) {
                    resolvePackage(entryPath, recursive);
                } else if (entryPath.endsWith(CLASS_SUFFIX)) {
                    String className = entryPath.substring(0, entryPath.length() - CLASS_SUFFIX.length());
                    classesSet.add(pathForPackage(className));
                }
            }
        }
    }

    /**
	 * @param className
	 * @return valid Service class annotated with @RemoteDestination
	 */
    public Class<?> loadClass(String className) {
        try {
            Class<?> clazz = bundle.loadClass(className);
            if (clazz.isAnnotationPresent(RemoteDestination.class)) {
                if (log.isInfoEnabled()) log.info(clazz.toString() + " is a valid GDS Service");
                return clazz;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Scan the packages and load all the qualified GraniteDS classes
	 * @param packages
	 * @return a set of valid Service classes annotated with @RemoteDestination
	 */
    public Set<Class<?>> loadClasses(String[] packages) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        if (packages != null) {
            for (int i = 0; i < packages.length; i++) {
                String packageName = packages[i];
                if (bundle != null) {
                    boolean recursive = packageName.endsWith("*");
                    if (recursive) packageName = packageName.substring(0, packageName.length() - 2);
                    resolvePackage(packageForPath(packageName), recursive);
                    Iterator<String> it = classesSet.iterator();
                    while (it.hasNext()) {
                        Class<?> clazz = null;
                        try {
                            clazz = bundle.loadClass(it.next());
                            if (clazz != null && clazz.isAnnotationPresent(RemoteDestination.class)) {
                                if (log.isInfoEnabled()) log.info(clazz.toString() + " is a valid GDS Service");
                                classes.add(clazz);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (log.isInfoEnabled()) log.info("Bundle is not specified, cannot load classes!!");
                }
            }
        }
        return classes;
    }
}
