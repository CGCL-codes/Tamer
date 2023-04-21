package raptor.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import raptor.Raptor;
import raptor.action.RaptorAction;
import raptor.action.RaptorActionFactory;

public class CreateClassActions {

    /**
	 * Grabbed this from
	 * http://www.javaworld.com/javaworld/javatips/jw-javatip113.html?page=2
	 * Amazing something from 1999 works so well. All of the other things I have
	 * tried had major flaws.
	 * 
	 * @param packageName
	 * @return
	 */
    public static Class<?>[] getClasses(String packageName) {
        List<Class<?>> result = new ArrayList<Class<?>>(50);
        String name = packageName;
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        name = name.replace('.', '/');
        URL url = Package.getPackage(packageName).getClass().getResource(name);
        File directory = new File(url.getFile());
        if (directory.exists()) {
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                if (files[i].endsWith(".class") && !files[i].contains("$")) {
                    String fileName = packageName + "." + files[i].substring(0, files[i].length() - 6);
                    try {
                        result.add(Class.forName(fileName));
                    } catch (Throwable t) {
                        System.err.println(t);
                    }
                }
            }
        }
        return result.toArray(new Class[0]);
    }

    public static void main(String args[]) throws Exception {
        Class<?>[] classes = getClasses("raptor.action.chat");
        for (Class<?> clazz : classes) {
            RaptorAction action = (RaptorAction) clazz.newInstance();
            Properties properties = RaptorActionFactory.save(action);
            File file = new File(Raptor.RESOURCES_DIR + "/actions/" + action.getName() + ".properties");
            if (!file.exists()) {
                properties.store(new FileOutputStream(file), "Raptor System Action");
                System.err.println("Created " + file);
            }
        }
        classes = getClasses("raptor.action.game");
        for (Class<?> clazz : classes) {
            RaptorAction action = (RaptorAction) clazz.newInstance();
            Properties properties = RaptorActionFactory.save(action);
            File file = new File(Raptor.RESOURCES_DIR + "/actions/" + action.getName() + ".properties");
            if (!file.exists()) {
                properties.store(new FileOutputStream(file), "Raptor System Action");
                System.err.println("Created " + file);
            }
        }
    }
}
