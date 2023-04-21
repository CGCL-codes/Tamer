package mfinder.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class工具类。
 */
public class ClassUtil {

    /** log */
    private static final Logger LOG = LoggerFactory.getLogger(ClassUtil.class);

    /** 文件名称编码 */
    public static final String DECODING = "UTF-8";

    /** JAVA Class文件后缀 */
    private static final String JAVA_CLASS_SUFFIX = ".class";

    /**
     * 从指定的包名中获取所有Class的名称集合。
     *
     * @param packageNames 指定的包的名称。
     *
     * @return 指定的包名中所有Class的名称集合。
     *
     * @throws ClassNotFoundException 如果无法定位该类。
     */
    public static Set<Class<?>> getClasses(String... packageNames) throws ClassNotFoundException {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        getClasses(classes, packageNames);
        return classes;
    }

    /**
     * 从指定的包名中获取所有Class的名称添加至指定的集合。
     *
     * @param classes 指定的Class名称集合。
     * @param packageNames 指定的包的名称。
     *
     * @throws ClassNotFoundException 如果无法定位该类。
     */
    private static void getClasses(Collection<Class<?>> classes, String... packageNames) throws ClassNotFoundException {
        boolean recursive = true;
        for (String packageName : packageNames) {
            if (StringUtil.isEmpty(packageName)) continue;
            String packageDirName = packageName.replace('.', '/');
            Enumeration<URL> dirs = null;
            try {
                dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
                while (dirs.hasMoreElements()) {
                    URL url = dirs.nextElement();
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String filePath = URLDecoder.decode(url.getFile(), DECODING);
                        getClassesByPackageFile(packageName, filePath, recursive, classes);
                    } else if ("jar".equals(protocol)) {
                        JarFile jar = null;
                        try {
                            jar = ((JarURLConnection) url.openConnection()).getJarFile();
                            Enumeration<JarEntry> entries = jar.entries();
                            while (entries.hasMoreElements()) {
                                JarEntry entry = entries.nextElement();
                                String name = entry.getName();
                                if (name.charAt(0) == '/') {
                                    name = name.substring(1);
                                }
                                if (name.startsWith(packageDirName)) {
                                    int idx = name.lastIndexOf('/');
                                    if (idx != -1) {
                                        packageName = name.substring(0, idx).replace('/', '.');
                                    }
                                    if ((idx != -1) || recursive) {
                                        if (name.endsWith(JAVA_CLASS_SUFFIX) && !entry.isDirectory()) {
                                            String className = name.substring(packageName.length() + 1, name.length() - 6);
                                            classes.add(loadClass(packageName + '.' + className));
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            LOG.error("IOException when loading files from : " + url, e);
                        }
                    }
                }
            } catch (IOException e) {
                LOG.error("IOException when get classes from : " + packageName, e);
            }
        }
    }

    /**
     * 以文件的形式来获取指定包中所有Class的名称添加至指定的集合。
     *
     * @param packageName 包名的目录形式。
     * @param packagePath 包所在的目录。
     * @param recursive 是否递归文件目录。
     * @param classes 指定的Class名称集合。
     *
     * @throws ClassNotFoundException 如果无法定位该类。
     */
    private static void getClassesByPackageFile(String packageName, String packagePath, final boolean recursive, Collection<Class<?>> classes) throws ClassNotFoundException {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirfiles = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(JAVA_CLASS_SUFFIX));
            }
        });
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                getClassesByPackageFile(packageName + '.' + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                classes.add(loadClass(packageName + '.' + className));
            }
        }
    }

    /**
     * 返回与带有给定字符串名的类或接口相关联的 Class 对象。
     *
     * @param className 所需类的完全限定名。
     *
     * @return 具有指定名的类的 Class 对象。
     *
     * @throws ClassNotFoundException 如果无法定位该类。
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(className);
    }
}
