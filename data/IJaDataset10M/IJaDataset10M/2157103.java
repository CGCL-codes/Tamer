package PolicyAlgebra.Plugin;

import PolicyAlgebra.Controller.Controller;
import PolicyAlgebra.Controller.ControllerException;
import PolicyAlgebra.Util.JarClassLoader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;

/** 
 * This class is responsible for loading plugins and provides easy
 * to use methods for fetching them.
 */
public class PluginLoader {

    private static PluginLoader loaderSingleton = null;

    private Controller ctrl;

    /** 
	 * Private constructor. 
	 */
    private PluginLoader() {
        ctrl = Controller.getInstance();
    }

    /** 
	 * Gets an instance of the plugin loader. 
	 * 
	 * @return An instance of the plugin loader.
	 */
    public static PluginLoader getInstance() {
        if (loaderSingleton == null) {
            loaderSingleton = new PluginLoader();
        }
        return loaderSingleton;
    }

    /** 
	 * Loads a plugin and returns a object of that type. 
	 * 
	 * @param pluginName The name of the plugin.
	 * @return An instance of the specified plugin.
	 * @throws PluginException If something goes wrong when loading the plugin.
	 */
    public Plugin loadPlugin(String pluginName) throws PluginException {
        Plugin plugin = null;
        String fileName = null;
        String className = null;
        try {
            fileName = ctrl.getPluginFileName(pluginName);
        } catch (ControllerException e) {
            throw new PluginException("Failed to load plugin: " + pluginName + " (failed during resolving of filename of jar file from alias: '" + pluginName + "')" + "Reason: " + e.getMessage());
        }
        try {
            className = ctrl.getPluginClassName(pluginName);
        } catch (ControllerException e) {
            throw new PluginException("Failed to load plugin: " + pluginName + " (failed during resolving of class name from alias: '" + pluginName + "')" + "Reason: " + e.getMessage());
        }
        JarClassLoader classLoader;
        try {
            classLoader = new JarClassLoader(ctrl.getInstallationLocation() + "/plugins/" + fileName);
        } catch (IOException e) {
            throw new PluginException("Failed to create a proper reference to the plugin " + pluginName + ", must abort.\nReason: " + e.getMessage());
        }
        try {
            Class c = classLoader.loadClass(className);
            plugin = (Plugin) c.newInstance();
        } catch (ClassNotFoundException e) {
            throw new PluginException("Failed to locate class for requested plugin (" + pluginName + ")");
        } catch (Exception e) {
            throw new PluginException("Failed to create class for requested plugin (" + pluginName + " = " + className + ")\n" + "Got a: " + e.getClass().getName());
        }
        return plugin;
    }
}
