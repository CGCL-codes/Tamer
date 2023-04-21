package org.hardtokenmgmt.core.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.ui.HardTokenManagementApplication;
import org.hardtokenmgmt.core.ui.IController;
import org.hardtokenmgmt.ui.ErrorController;

/**
 * This is a help class used to
 * cache all instanciated and to make them singeltons 
 * controllers to save resources.
 * 
 * 
 * @author Philip Vendil 2007 feb 16
 *
 * @version $Id$
 */
public class ControllerCache {

    /**
	 * Constant indicating controllers that shouldn't be cached.
	 */
    private static HashSet<String> notCachedControllers = new HashSet<String>();

    static {
        notCachedControllers.add(ErrorController.class.getName());
    }

    private static HashMap<String, IController> instantiatedControllers = new HashMap<String, IController>();

    /**
	 * Method returning a IController object given an classpath.
	 * If the controller already have been instanced once it
	 * is returned from the cache.
	 * 
	 * @return the controller or null if the controller couldn't be created
	 * 
	 */
    public static IController getControllerInstance(String classPath) {
        IController retval = null;
        if (notCachedControllers.contains(classPath) || instantiatedControllers.get(classPath) == null) {
            try {
                ClassLoader cl = HardTokenManagementApplication.class.getClassLoader();
                Class<?> c = cl.loadClass(classPath);
                retval = (IController) c.newInstance();
                if (!notCachedControllers.contains(classPath)) {
                    instantiatedControllers.put(classPath, retval);
                }
            } catch (Exception e) {
                LocalLog.getLogger().log(Level.SEVERE, "Error, application missconfiguration. Classpath to controller " + classPath + " couldn't be created ." + e.getClass().getName() + " : " + e.getMessage(), e);
            }
        } else {
            retval = (IController) instantiatedControllers.get(classPath);
        }
        return retval;
    }

    public static void removeControllerInstance(String classPath) {
        if (instantiatedControllers.get(classPath) != null) {
            instantiatedControllers.remove(classPath);
        }
    }
}
