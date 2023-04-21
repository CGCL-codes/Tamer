package application;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;

public class SecurityManager {

    public static Logger logger = Logger.getLogger(SecurityManager.class);

    public void getSecBeans() throws IOException, ClassNotFoundException {
        String packageName = "apps";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        for (Class c : classes) {
            if (c.isAnnotationPresent(ManagedBean.class)) {
                ManagedBean a = (ManagedBean) c.getAnnotation(ManagedBean.class);
                logger.info("Maged Bean: " + c.getCanonicalName());
                logger.info("Managed Bean name: " + a.name());
                Config.getInstance().getBackBeans().put(c.getCanonicalName(), a.name());
            }
        }
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
