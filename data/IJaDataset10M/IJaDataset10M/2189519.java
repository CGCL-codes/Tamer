package swproxy.plugins;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import swproxy.Config;
import swproxy.plugins.api.Pluginable;
import swproxy.plugins.api.URICheck;
import swproxy.plugins.api.exceptions.PluginInstantiationException;

public class PluginManager {

    private static Logger logger = Logger.getLogger(PluginManager.class);

    private final int[][] compatibleApiVersions = { { 0, 9 } };

    private List<String> configuredPluginList;

    private Config config = null;

    private List<Pair<Pluginable, Boolean>> pluginList = new ArrayList<Pair<Pluginable, Boolean>>();

    private PluginListSynchronizer<Pair<Pluginable, Boolean>> pluginListSynchronizer = new PluginListSynchronizer<Pair<Pluginable, Boolean>>(pluginList);

    /**
	 * C'tor.
	 */
    public PluginManager() {
        config = new Config();
        config.setPluginManager(this);
        checkConfiguredPluginList();
    }

    public URICheck checkURI(URICheck uriCheck) {
        for (Pair<Pluginable, Boolean> element : pluginListSynchronizer.startReading()) if (element.element2) try {
            element.element1.checkURI(uriCheck);
        } catch (Exception e) {
            handlePluginException(element, e);
        }
        pluginListSynchronizer.readyReading();
        return uriCheck;
    }

    public void configChanged() {
        if (checkConfiguredPluginList()) return;
        for (Pair<Pluginable, Boolean> element : pluginListSynchronizer.startReading()) if (element.element2) try {
            element.element1.configChanged();
        } catch (Exception e) {
            handlePluginException(element, e);
        }
        pluginListSynchronizer.readyReading();
    }

    public void exitPlugins() {
        for (Pair<Pluginable, Boolean> element : pluginListSynchronizer.startReading()) if (element.element2) exitPlugin(element);
        pluginListSynchronizer.readyReading();
    }

    public Pluginable getInstanceOf(String uniqueCanonicalNameEnd) {
        for (Pair<Pluginable, Boolean> element : pluginListSynchronizer.startReading()) if (element.element2) if (element.element1.getClass().getCanonicalName().endsWith(uniqueCanonicalNameEnd)) return element.element1;
        pluginListSynchronizer.readyReading();
        return null;
    }

    public Object message(Object source, String type, Object param) {
        for (Pair<Pluginable, Boolean> element : pluginListSynchronizer.startReading()) if (element.element2) try {
            element.element1.message(source, type, param);
        } catch (Exception e) {
            handlePluginException(element, e);
        }
        pluginListSynchronizer.readyReading();
        config.message(source, type, param);
        return null;
    }

    public void setConfig(Config config) {
        for (Pair<Pluginable, Boolean> element : pluginListSynchronizer.startReading()) if (element.element2) try {
            element.element1.setConfig(config);
        } catch (Exception e) {
            handlePluginException(element, e);
        }
        pluginListSynchronizer.readyReading();
    }

    public boolean isApiVersionCompatible(int[] version) {
        if (version == null) return false;
        for (int[] compatibleVersion : compatibleApiVersions) {
            if (version.length == compatibleVersion.length) {
                boolean allMatched = true;
                for (int i = 0; i < version.length; ++i) if (version[i] != compatibleVersion[i]) {
                    allMatched = false;
                    break;
                }
                if (allMatched) return true;
            }
        }
        return false;
    }

    private void handlePluginException(Pair<Pluginable, Boolean> plugin, Exception e) {
        handlePluginException(e);
        exitPlugin(plugin);
    }

    private void handlePluginException(Exception e) {
        if (e instanceof RuntimeException && !(e instanceof NullPointerException) && !(e instanceof ClassNotFoundException)) {
            logger.fatal("A critical runtime-exception reached the plugin-manager. Shutting down immediately.", e);
            e.printStackTrace();
            System.exit(1);
        }
        logger.error("A plugin caused an Exception.", e);
    }

    private void exitPlugin(Pair<Pluginable, Boolean> plugin) {
        if (!plugin.element2) return;
        logger.debug("Shutting down " + plugin.element1.getClass().getCanonicalName());
        plugin.element2 = false;
        pluginListSynchronizer.removeDeleyed(plugin);
        try {
            plugin.element1.exitPlugin();
            logger.info("Plugin shutted down successfully: " + plugin.element1.getClass().getCanonicalName());
        } catch (RuntimeException e) {
            logger.error("Plugin failed to shut down: " + plugin.element1.getClass().getCanonicalName());
            handlePluginException(e);
        }
    }

    private void loadPlugins(URL[] urls, List<String> classnames) {
        URLClassLoader classloader = new URLClassLoader(urls);
        for (String classname : classnames) {
            Class<?> pluginClass = null;
            try {
                pluginClass = classloader.loadClass(classname);
            } catch (ClassNotFoundException e) {
                logger.error(e);
                continue;
            }
            logger.debug("Plugin-class loaded: " + pluginClass.getCanonicalName());
            if (Pluginable.class.isAssignableFrom(pluginClass)) {
                Pair<Pluginable, Boolean> plugin = null;
                try {
                    plugin = new Pair<Pluginable, Boolean>((Pluginable) pluginClass.newInstance(), true);
                    if (isApiVersionCompatible(plugin.element1.getApiVersion())) {
                        logger.trace(pluginClass.getCanonicalName() + " instanciated.");
                        plugin.element1.setManager(this);
                        plugin.element1.setConfig(config);
                        pluginList.add(plugin);
                    } else {
                        logger.error("Plugin-Api-version of " + pluginClass.getCanonicalName() + " not compatible: " + intArrayToString(plugin.element1.getApiVersion()) + ". Compatible are: " + intArrayArrayToString(compatibleApiVersions));
                    }
                } catch (Exception e) {
                    if (e instanceof PluginInstantiationException && plugin == null) logger.error(pluginClass.getCanonicalName() + " signalized an error while instantiating. Not instantiated.", e); else {
                        logger.error("Error while instanciating plugin: " + pluginClass.getCanonicalName(), e);
                        handlePluginException(plugin, e);
                    }
                }
            } else {
                logger.error(pluginClass.getCanonicalName() + " does not implement Pluginable. Class not instanciated.");
            }
        }
    }

    private void runPlugins() {
        for (Pair<Pluginable, Boolean> element : pluginListSynchronizer.startReading()) if (element.element2) {
            logger.debug("Running " + element.element1.getClass().getCanonicalName());
            try {
                element.element1.runPlugin();
                logger.info("Plugin started successfully: " + element.element1.getClass().getCanonicalName());
            } catch (Exception e) {
                handlePluginException(element, e);
            }
        }
        pluginListSynchronizer.readyReading();
    }

    private boolean checkConfiguredPluginList() {
        List<String> newConfiguredPluginList = config.getValueList("plugin", "");
        if (!newConfiguredPluginList.equals(configuredPluginList)) {
            exitPlugins();
            pluginListSynchronizer.clear();
            configuredPluginList = newConfiguredPluginList;
            loadPlugins(new URL[] {}, configuredPluginList);
            config.runConfig();
            runPlugins();
            return true;
        }
        return false;
    }

    public static String intArrayToString(int[] array) {
        if (array == null) return "null";
        if (array.length == 0) return "null";
        String s = "" + array[0];
        for (int i = 1; i < array.length; ++i) {
            s += "." + array[i];
        }
        return s;
    }

    public static String intArrayArrayToString(int[][] arrayArray) {
        if (arrayArray == null) return "null";
        String s = "{";
        boolean first = true;
        for (int[] element : arrayArray) {
            if (first) {
                s += intArrayToString(element);
                first = false;
            } else s += ", " + intArrayToString(element);
        }
        s += "}";
        return s;
    }
}
