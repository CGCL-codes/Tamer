package edu.regis.jprobe.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import edu.regis.jprobe.model.Utilities;
import static edu.regis.jprobe.model.Utilities.debugMsg;

public class ProbeClassFileTransformer implements ClassFileTransformer {

    private Map<String, ClassInfo> loadedClasses;

    private Map<String, ClassLoaderInfo> classLoaders;

    private static final String JVMOPT_LOG_OPTION = "edu.regis.jprobe.log.loader";

    private static final int STRING_BUFFER_SIZE = 4096;

    private boolean logClassLoads = false;

    private int scanCount = 0;

    private long totalClassSize = 0;

    private long transformTime = 0;

    private long numberOfPackages = 0;

    private ClassLoader bootstrapLoader;

    private ProbeState status;

    protected ProbeClassFileTransformer(ProbeState status) {
        this.status = status;
        debugMsg("Initializing Probe Class File Transformer");
        loadedClasses = Collections.synchronizedMap(new HashMap<String, ClassInfo>());
        classLoaders = Collections.synchronizedMap(new HashMap<String, ClassLoaderInfo>());
        String logLoads = System.getProperty(JVMOPT_LOG_OPTION);
        if ("true".equalsIgnoreCase(logLoads)) {
            logClassLoads = true;
            Utilities.debug = true;
        }
        bootstrapLoader = ClassLoader.getSystemClassLoader();
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!status.isInitialized()) return null;
        long start = System.nanoTime();
        try {
            String classLoaderName = "";
            if (loader != null) {
                classLoaderName = loader.getClass().getCanonicalName();
            } else {
                classLoaderName = bootstrapLoader.getClass().getName();
                loader = bootstrapLoader;
            }
            if (className == null) return null;
            String name = normalizeName(className);
            if (logClassLoads) {
                long tid = Thread.currentThread().getId();
                Utilities.debugMsg("[" + tid + "] - ClassLoader(" + classLoaderName + ") is Loading Class " + className + " size is " + classfileBuffer.length);
            }
            loadedClasses.put(name, new ClassInfo(name, classfileBuffer.length, loader));
            totalClassSize += classfileBuffer.length;
            ClassLoaderInfo loads = classLoaders.get(classLoaderName);
            if (loads == null) {
                classLoaders.put(classLoaderName, new ClassLoaderInfo(loader));
            } else {
                loads.incrementLoadCount();
                classLoaders.put(classLoaderName, loads);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            transformTime += System.nanoTime() - start;
        }
        return null;
    }

    protected Package[] getPackages(ClassLoader loader) {
        Class sysclass = loader.getClass();
        Object parm[] = new Object[0];
        Object pkg = null;
        while (sysclass != null) {
            try {
                Method method = sysclass.getDeclaredMethod("getPackages", (Class[]) null);
                method.setAccessible(true);
                pkg = method.invoke(loader, parm);
                break;
            } catch (NoSuchMethodException e) {
                sysclass = sysclass.getSuperclass();
            } catch (Exception e) {
                Utilities.debugMsg("Error Getting Packages, Error is " + e.getMessage());
                break;
            }
        }
        if (pkg != null && pkg instanceof Package[]) {
            return (Package[]) pkg;
        }
        return null;
    }

    public String getClassPathURLs() {
        StringBuilder clsb = new StringBuilder(STRING_BUFFER_SIZE);
        try {
            Set classLoaderkeys = classLoaders.keySet();
            Set<String> urlSet = new HashSet<String>();
            synchronized (classLoaders) {
                Iterator classLoaderIter = classLoaderkeys.iterator();
                while (classLoaderIter.hasNext()) {
                    String classLoaderName = (String) classLoaderIter.next();
                    ClassLoaderInfo loads = classLoaders.get(classLoaderName);
                    ClassLoader ldr = loads.getClassLoader();
                    if (ldr instanceof URLClassLoader) {
                        URL urls[] = ((URLClassLoader) ldr).getURLs();
                        for (int i = 0; i < urls.length; i++) {
                            urlSet.add(urls[i].toString());
                        }
                    }
                }
            }
            Iterator urlIter = urlSet.iterator();
            List<String> urlList = new ArrayList<String>();
            while (urlIter.hasNext()) {
                urlList.add((String) urlIter.next());
            }
            Collections.sort(urlList);
            for (int i = 0; i < urlList.size(); i++) {
                clsb.append(urlList.get(i));
                clsb.append("\n");
            }
        } catch (Exception e) {
            clsb.append("Exception " + e.getMessage() + " Occured Obtaining Class Loader URL List\n" + Utilities.formatException(e, this));
        }
        return clsb.toString();
    }

    public String getLoadedClasses() {
        StringBuilder lcsb = new StringBuilder(STRING_BUFFER_SIZE);
        try {
            Set keys = loadedClasses.keySet();
            List<String> classList = new ArrayList<String>();
            synchronized (loadedClasses) {
                Iterator iter = keys.iterator();
                while (iter.hasNext()) {
                    String className = (String) iter.next();
                    Integer size = loadedClasses.get(className).getClassSize();
                    classList.add(className + " Size(" + size.toString() + ")");
                }
            }
            Collections.sort(classList);
            for (int i = 0; i < classList.size(); i++) {
                lcsb.append(classList.get(i));
                lcsb.append("\n");
            }
        } catch (Exception e) {
            lcsb.append("Exception " + e.getMessage() + " Occured Obtaining Loaded Class List\n" + Utilities.formatException(e, this));
        }
        return lcsb.toString();
    }

    public String getClassLoaders() {
        StringBuilder clsb = new StringBuilder(STRING_BUFFER_SIZE);
        try {
            Set classLoaderkeys = classLoaders.keySet();
            List<String> classLoaderList = new ArrayList<String>();
            synchronized (classLoaders) {
                Iterator classLoaderIter = classLoaderkeys.iterator();
                while (classLoaderIter.hasNext()) {
                    String classLoaderName = (String) classLoaderIter.next();
                    ClassLoaderInfo loads = classLoaders.get(classLoaderName);
                    classLoaderList.add(classLoaderName + " Loads(" + loads.getLoadCount() + ")");
                }
            }
            Collections.sort(classLoaderList);
            for (int i = 0; i < classLoaderList.size(); i++) {
                clsb.append(classLoaderList.get(i));
                clsb.append("\n");
            }
        } catch (Exception e) {
            clsb.append("Exception " + e.getMessage() + " Occured Obtaining Class Loader List\n" + Utilities.formatException(e, this));
        }
        return clsb.toString();
    }

    public String getPackages() {
        StringBuilder clsb = new StringBuilder(STRING_BUFFER_SIZE);
        try {
            Set classLoaderkeys = classLoaders.keySet();
            Set<String> classLoaderList = new HashSet<String>();
            ClassLoaderInfo loaders[] = null;
            synchronized (classLoaders) {
                loaders = new ClassLoaderInfo[classLoaders.size()];
                Iterator classLoaderIter = classLoaderkeys.iterator();
                int idx = 0;
                while (classLoaderIter.hasNext()) {
                    String classLoaderName = (String) classLoaderIter.next();
                    loaders[idx++] = classLoaders.get(classLoaderName);
                }
            }
            for (ClassLoaderInfo inf : loaders) {
                Package pkg[] = getPackages(inf.getClassLoader());
                if (pkg != null) {
                    for (int i = 0; i < pkg.length; i++) {
                        classLoaderList.add(pkg[i].getName());
                    }
                }
            }
            Iterator packageIter = classLoaderList.iterator();
            List<String> packageList = new ArrayList<String>();
            while (packageIter.hasNext()) {
                packageList.add((String) packageIter.next());
            }
            Collections.sort(packageList);
            numberOfPackages = packageList.size();
            for (int i = 0; i < classLoaderList.size(); i++) {
                clsb.append(packageList.get(i));
                clsb.append("\n");
            }
        } catch (Exception e) {
            clsb.append("Exception " + e.getMessage() + " Occured Obtaining Class Loader List.\n" + Utilities.formatException(e, this));
        }
        return clsb.toString();
    }

    protected String normalizeName(String name) {
        char namebytes[] = name.toCharArray();
        char normalName[] = new char[namebytes.length];
        for (int i = 0; i < namebytes.length; i++) {
            if (namebytes[i] == '/') {
                normalName[i] = '.';
            } else {
                normalName[i] = namebytes[i];
            }
        }
        return new String(normalName);
    }

    public ClassLoader getClassLoaderForClass(String name) {
        ClassInfo inf = loadedClasses.get(name);
        if (inf == null) return null;
        return inf.getClassLoader();
    }

    /**
	 * @return the totalClassSize
	 */
    public long getTotalClassSize() {
        return totalClassSize;
    }

    /**
	 * @return the transformTime
	 */
    public long getTransformTime() {
        return transformTime;
    }

    public long getNumberOfClassLoaders() {
        return classLoaders.size();
    }

    public long getNumberOfLoadedClasses() {
        return loadedClasses.size();
    }

    public long getNumberOfPackages() {
        return numberOfPackages;
    }
}
