package be.djdb.aplugin;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import be.djdb.UnConstruct;

/**
* @author Lieven Roegiers
* @copyright 2011
* @from JAVA_mylibs
*/
@UnConstruct
public class MyIsplugblescan {

    List<Class> classes1;

    List<Class> plugble;

    /**
	 * 
	 * @param directory
	 * @param packageName
	 * @return
	 * @throws ClassNotFoundException
	 */
    public List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (isClassfile(file.getName())) {
                Class theclass = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                Annotation[] annotations = theclass.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Plugble) {
                        Plugble myAnnotation = (Plugble) annotation;
                        System.out.println("classname: " + myAnnotation.mustimplement());
                        Class myinterface = Class.forName(myAnnotation.mustimplement());
                        if (theclass.isInterface()) {
                        }
                        ;
                        System.out.println("classname: " + theclass.getName());
                        System.out.println("value: " + myAnnotation.programname());
                    }
                }
                try {
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                classes.add(theclass);
            }
        }
        return classes;
    }

    /**
	 * 
	 * @param name
	 * @return
	 */
    public static boolean isClassfile(String name) {
        return name.endsWith(".class");
    }

    /**
	 * 
	 * @param aclass
	 * @return
	 */
    private static boolean isplugin(Class aclass) {
        return aclass.isAnnotationPresent(MyPlug.class);
    }

    /**
	 * 
	 * @param aclass
	 * @return
	 */
    @UnConstruct
    private String getpluginname(Class aclass) {
        return aclass.getAnnotation(MyPlug.class).toString();
    }

    private void print(Class aclass) {
        System.out.println(" |-class:" + aclass.getName());
        for (Method m : aclass.getMethods()) {
            System.out.println("|-->Methode" + m.getName());
        }
    }

    private void methodescan(Class aclass) {
        int passed = 0, failed = 0;
        for (Method m : aclass.getMethods()) {
            if (m.isAnnotationPresent(Plugble.class)) {
                try {
                    m.invoke(null);
                    passed++;
                } catch (Throwable ex) {
                    System.out.printf("Test %s failed: %s %n", m, ex.getCause());
                    failed++;
                }
            }
        }
    }
}
