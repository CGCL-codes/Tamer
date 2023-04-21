package org.lightcommons.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.lightcommons.logger.Log;
import org.lightcommons.logger.LogFactory;

/**
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * @author GL
 */
public class PackageUtils {

    private static final String PROTOCOL_FILE = "file";

    private static final String PROTOCOL_JAR = "jar";

    private static final String PREFIX_FILE = "file:";

    private static final String JAR_URL_SEPERATOR = "!/";

    private static final String CLASS_FILE = ".class";

    private static final Log log = LogFactory.getLog(PackageUtils.class);

    @SuppressWarnings("deprecation")
    private static List<URL> list(URL url) {
        try {
            List<URL> urls = new ArrayList<URL>();
            if (PROTOCOL_FILE.equals(url.getProtocol())) {
                File dir = new File(URLDecoder.decode(url.getPath(), "UTF-8"));
                String[] files = dir.list();
                for (String f : files) {
                    urls.add(new File(dir.getPath() + '/' + f).toURL());
                }
            } else if (PROTOCOL_JAR.equals(url.getProtocol())) {
                String path = URLDecoder.decode(url.getPath(), "UTF-8");
                int e = path.lastIndexOf(".jar!") + 4;
                String jarUrl = path.substring(0, e);
                String jar = jarUrl;
                if (jar.startsWith("file:/")) {
                    jar = jar.substring(6);
                }
                String name = path.substring(e + 2);
                JarFile jarFile = new JarFile(jar);
                Enumeration<JarEntry> jes = jarFile.entries();
                while (jes.hasMoreElements()) {
                    JarEntry je = jes.nextElement();
                    if (je.getName().startsWith(name)) {
                        URL url2 = new URL("jar:" + jarUrl + "!/" + je.getName());
                        urls.add(url2);
                    }
                }
            }
            return urls;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static List<URL> getResources(String pkgName) {
        List<URL> urls = new ArrayList<URL>();
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            String path = pkgName.replace('.', '/');
            Enumeration<URL> resources = cld.getResources(path);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                urls.addAll(list(url));
            }
            return urls;
        } catch (Throwable x) {
            throw new RuntimeException(x);
        }
    }

    public static List<Class<?>> getClasses(String packageName) {
        Assert.notNull(packageName, "packageName can not be null for PackageUtils.getClasses");
        List<Class<?>> list = new ArrayList<Class<?>>();
        Enumeration<URL> en = null;
        try {
            en = PackageUtils.class.getClassLoader().getResources(nameToPath(packageName));
            while (en.hasMoreElements()) {
                URL url = en.nextElement();
                if (PROTOCOL_FILE.equals(url.getProtocol())) {
                    File root = new File(url.getFile());
                    findInDirectory(list, root, root, packageName);
                } else if (PROTOCOL_JAR.equals(url.getProtocol())) findInJar(list, getJarFile(url), packageName);
            }
        } catch (IOException e) {
            log.error("无效的包名:" + packageName, e.getCause());
        }
        return list;
    }

    public static File getJarFile(URL url) {
        String file = url.getFile();
        if (file.startsWith(PREFIX_FILE)) file = file.substring(PREFIX_FILE.length());
        int end = file.indexOf(JAR_URL_SEPERATOR);
        if (end != (-1)) file = file.substring(0, end);
        return new File(file);
    }

    private static void findInDirectory(List<Class<?>> results, File rootDir, File dir, String packageName) {
        File[] files = dir.listFiles();
        String rootPath = rootDir.getPath();
        for (File file : files) if (file.isFile()) {
            String classFileName = file.getPath();
            if (classFileName.endsWith(CLASS_FILE)) {
                String className = classFileName.substring(rootPath.length() - packageName.length(), classFileName.length() - CLASS_FILE.length());
                try {
                    Class<?> clz = Class.forName(pathToName(className));
                    results.add(clz);
                } catch (ClassNotFoundException e) {
                    log.error("无法获取类:" + className, e.getCause());
                }
            }
        } else if (file.isDirectory()) findInDirectory(results, rootDir, file, packageName);
    }

    private static void findInJar(List<Class<?>> results, File file, String packageName) {
        JarFile jarFile = null;
        String packagePath = nameToPath(packageName) + "/";
        try {
            jarFile = new JarFile(file);
            Enumeration<JarEntry> en = jarFile.entries();
            while (en.hasMoreElements()) {
                JarEntry je = en.nextElement();
                String name = je.getName();
                if (name.startsWith(packagePath) && name.endsWith(CLASS_FILE)) {
                    String className = name.substring(0, name.length() - CLASS_FILE.length());
                    try {
                        Class<?> clz = Class.forName(pathToName(className));
                        results.add(clz);
                    } catch (ClassNotFoundException e) {
                        log.error("无法获取类:" + className, e.getCause());
                    }
                }
            }
        } catch (IOException e) {
            log.error("无法读取 Jar 文件:" + file.getName(), e);
        } finally {
            if (jarFile != null) try {
                jarFile.close();
            } catch (IOException e) {
            }
        }
    }

    /**
	 * 将类名的字符串形式转换为路径表现形式
	 * @param className 类名
	 * @return 路径的字符串
	 */
    private static String nameToPath(String className) {
        return className.replace('.', '/');
    }

    /**
	 * 将路径转换为类的字符串表现形式
	 * @param path
	 * @return 类名的字符串形式
	 */
    private static String pathToName(String path) {
        return path.replace('/', '.').replace('\\', '.');
    }

    public static List<Method> getSortedMethodList(Class<?> clazz) {
        List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());
        Collections.sort(methods, new Comparator<Method>() {

            public int compare(Method m1, Method m2) {
                String lower1 = m1.toString().toLowerCase();
                String lower2 = m2.toString().toLowerCase();
                return lower1.compareTo(lower2);
            }
        });
        return methods;
    }
}
