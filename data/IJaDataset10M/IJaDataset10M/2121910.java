package org.ujac.web.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ujac.form.Form;
import org.ujac.web.servlet.BaseActionRegistry;

/**
 * Name: DynamicActionRegistry<br>
 * Description: An ActionRegistry implementation, that dynamically
 *   searches classes at the default locations of a web archive 
 *   (WEB-INF/classes/../xyz.class, WEB-INF/lib/xyz.jar).
 * <br>Log: $Log$
 * <br>Log: Revision 1.4  2004/12/24 01:57:28  lauerc
 * <br>Log: At method register(): Disabled scanning of libraries from WEB-INF/lib.
 * <br>Log: At method probeClass(String, String, ClassLoader): Added log output.
 * <br>Log:
 * <br>Log: Revision 1.3  2004/12/16 23:50:16  lauerc
 * <br>Log: Minor enhancements.
 * <br>Log:
 * <br>Log: Revision 1.2  2004/11/28 01:43:47  lauerc
 * <br>Log: Added support for Forms.
 * <br>Log:
 * <br>Log: Revision 1.1  2004/11/27 10:45:25  lauerc
 * <br>Log: Initial revision.
 * <br>Log:
 * @author $Author: lauerc $
 * @version $Revision: 2008 $
 */
public class DynamicActionRegistry extends BaseActionRegistry {

    /** The loggger. */
    private Log log = LogFactory.getLog("DynamicActionRegistry");

    /**
   * Registers actions, forms and units.
   */
    public void register() {
        URLClassLoader cl = (URLClassLoader) getClass().getClassLoader();
        String webInfDir = getWarLocation() + "WEB-INF/";
        File webInfClasses = new File(webInfDir + "classes");
        if (webInfClasses.exists()) {
            registerActionsFromDirectory(webInfClasses, null, cl);
        }
    }

    /**
   * Recursively registers all actions, found in the given directory.
   * @param directory The directory to examine.
   * @param pkg The current package.
   * @param cl The class loader to use.
   */
    private void registerActionsFromDirectory(File directory, String pkg, ClassLoader cl) {
        File[] childs = directory.listFiles();
        if (childs == null) {
            return;
        }
        for (int i = 0; i < childs.length; i++) {
            File child = childs[i];
            String childName = child.getName();
            if (child.isDirectory()) {
                if (pkg == null) {
                    registerActionsFromDirectory(child, childName, cl);
                } else {
                    registerActionsFromDirectory(child, pkg + "." + childName, cl);
                }
                continue;
            }
            if (child.isFile() && childName.endsWith(".class")) {
                probeClass(pkg, childName, cl);
            }
        }
    }

    /**
   * Registers all actions, found in the given archive.
   * @param archive The archive to examine.
   * @param cl The class loader to use.
   */
    private void registerActionsFromArchive(File archive, ClassLoader cl) {
        try {
            JarFile jar = new JarFile(archive);
            Enumeration entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = (JarEntry) entries.nextElement();
                String entryName = entry.getName();
                if (!entry.isDirectory() && entryName.endsWith(".class")) {
                    probeClass(null, entryName.replace('/', '.'), cl);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
   * Probes the given class name for a legal action or form.
   * If successful, registers a instance of the class as an action or form.
   * @param pkg The package name.
   * @param classFileName The class file name.
   * @param cl The class loader to use.
   */
    private void probeClass(String pkg, String classFileName, ClassLoader cl) {
        String className = classFileName.substring(0, classFileName.length() - 6);
        String fqn = null;
        if (pkg == null) {
            fqn = className;
        } else {
            fqn = pkg + "." + className;
        }
        try {
            Class childClass = cl.loadClass(fqn);
            if (Modifier.isAbstract(childClass.getModifiers())) {
                return;
            }
            if (checkForType(childClass, Action.class)) {
                Action actionInstance = (Action) childClass.newInstance();
                registerAction(actionInstance.getName(), actionInstance);
                log.info("successfuly registered action '" + actionInstance.getName() + "'");
            } else if (checkForType(childClass, Form.class)) {
                Form formInstance = (Form) childClass.newInstance();
                registerForm(formInstance);
                log.info("successfuly registered form '" + formInstance.getName() + "'");
            }
        } catch (ClassNotFoundException ex) {
            log.warn("Unable to probe class '" + fqn + "': unable to load");
        } catch (InstantiationException ex) {
            log.warn("Unable to probe class '" + fqn + "': unable to instantiate");
        } catch (IllegalAccessException ex) {
            log.warn("Unable to probe class '" + fqn + "': unable to access");
        }
    }

    /**
   * Checks the given class for legal action.
   * @param actionClass The class to check.
   * @param typeClass The type class.
   * @return true if the class implements the action interface, else false.
   */
    private boolean checkForType(Class actionClass, Class typeClass) {
        if (actionClass == null) {
            return false;
        }
        Class[] interfaces = actionClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) {
            Class iface = interfaces[i];
            if (iface.equals(typeClass)) {
                return true;
            }
            if (checkForType(iface, typeClass)) {
                return true;
            }
        }
        return checkForType(actionClass.getSuperclass(), typeClass);
    }
}
