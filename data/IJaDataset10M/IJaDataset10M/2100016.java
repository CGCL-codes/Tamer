package org.auramp.plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;
import org.auramp.io.ResourceManager;
import org.auramp.io.logging.AuraLogger;
import org.auramp.plugin.PluginItem.Type;
import org.auramp.ui.event.EventManager;
import org.auramp.util.OSToolkit;
import com.trolltech.qt.core.QObject;

/**
 * Responsible for handling the loading and unloading of plugins
 * from Aura as well as scanning for available plugins to load.
 * 
 * @author David Cawley
 */
public class PluginManager extends QObject {

    private static final String PLUGIN_DIR = "/plugins";

    private static boolean verifysigs = false;

    private static HashMap<PluginItem, PluginImpl> pluginMap = new HashMap<PluginItem, PluginImpl>();

    /** The current version of Aura */
    public static final int VER_AURA_CURRENT = 10;

    /** The version lunch 1.0 release of Aura */
    public static final int VER_AURA_LUNCH_1_0 = 10;

    private PluginManager() {
    }

    /**
	 * Indicates whether or not to verify signed jars while loading plugins
	 * 
	 * @param verify true to verify jars otherwise false
	 */
    public static void setVerifySignatures(boolean verify) {
        verifysigs = verify;
    }

    /**
	 * Scans the known plugin filter path for jars that could contain
	 * Aura compatable plugin filters.
	 * 
	 * @return a Collection of plugin items for each possible plugin filter
	 */
    public static List<PluginItem> scanForFilters() {
        LinkedList<PluginItem> ret = new LinkedList<PluginItem>();
        File pdir = new File(ResourceManager.getExecDir() + PLUGIN_DIR + File.separator + "filters");
        if (!pdir.exists() || !pdir.isDirectory()) {
            return ret;
        }
        for (File f : pdir.listFiles()) {
            if (!f.isFile() || !f.getName().toLowerCase().endsWith(".jar")) {
                continue;
            }
            try {
                JarFile jf = new JarFile(f, verifysigs);
                try {
                    PluginItem itm = new PluginItem(jf);
                    if (itm.getType() == Type.Filter) {
                        ret.add(itm);
                    }
                } catch (PluginManagementException e) {
                    AuraLogger.logWarnng("Invalid filter plugin detected: " + jf.getName());
                }
            } catch (IOException e) {
            }
        }
        return ret;
    }

    /**
	 * Scans the known plugin path for jars that could contain
	 * Aura compatable plugins.
	 * 
	 * @return a Collection of plugin items for each possible plugin
	 */
    public static List<PluginItem> scanForPlugins() {
        LinkedList<PluginItem> ret = new LinkedList<PluginItem>();
        File pdir = new File(ResourceManager.getExecDir() + PLUGIN_DIR);
        if (!pdir.exists() || !pdir.isDirectory()) {
            return ret;
        }
        for (File f : pdir.listFiles()) {
            if (!f.isFile() || !f.getName().toLowerCase().endsWith(".jar")) {
                continue;
            }
            try {
                JarFile jf = new JarFile(f, verifysigs);
                try {
                    PluginItem itm = new PluginItem(jf);
                    if (itm.getType() == Type.Plugin) {
                        ret.add(itm);
                    }
                } catch (PluginManagementException e) {
                    AuraLogger.logWarnng("Invalid plugin detected: " + jf.getName());
                }
            } catch (IOException e) {
            }
        }
        return ret;
    }

    public static boolean isPluginLoaded(PluginItem plugin) {
        return pluginMap.get(plugin) != null;
    }

    /**
	 * Attempts to load the plugin represented by the given {@link PluginItem}.
	 * 
	 * @param plugin the {@link PluginItem} representing the plugin to load 
	 * @return true if the plugin was loaded without error otherwise false
	 * @throws PluginManagementException when the plugin is already loaded
	 */
    public static boolean loadPlugin(PluginItem plugin) throws PluginManagementException {
        PluginImpl plug = pluginMap.get(plugin);
        if (plug != null) {
            throw new PluginManagementException("Plugin main class already loaded");
        }
        PluginImpl pimp = loadPluginImpl(plugin);
        pluginMap.put(plugin, pimp);
        boolean result = pimp.load(VER_AURA_CURRENT, OSToolkit.getOperatingSystem(), OSToolkit.getOSVersion());
        if (result) {
            EventManager.current().pluginLoaded.emit(plugin);
        }
        return result;
    }

    /**
	 * Attempts to unload the plugin represented by the given {@link PluginItem}.
	 * 
	 * @param plugin the {@link PluginItem} representing the plugin to unload
	 * @return true if the plugin was unloaded without error otherwise false.
	 * @throws PluginManagementException when the plugin has not yet been loaded
	 */
    public static boolean unloadPlugin(PluginItem plugin) throws PluginManagementException {
        PluginImpl plug = pluginMap.get(plugin);
        if (plug == null) {
            throw new PluginManagementException("Plugin main class not yet loaded");
        }
        pluginMap.remove(plugin);
        boolean result = plug.unload();
        if (result) {
            EventManager.current().pluginUnloaded.emit(plugin);
        }
        return result;
    }

    /**
	 * Determines if the plugin represented by the given {@link PluginItem}
	 * has an interface for user configuration.
	 * 
	 * @param plugin the {@link PluginItem} representing the plugin to determine is configureable
	 * @return true if the plugin has a configuration interface, otherwise false
	 * @throws PluginManagementException when the plugin has not yet been loaded
	 */
    public static boolean isConfigurable(PluginItem plugin) throws PluginManagementException {
        PluginImpl plug = pluginMap.get(plugin);
        if (plug == null) {
            throw new PluginManagementException("Plugin main class not yet loaded");
        }
        return plug.configureable();
    }

    /**
	 * Loads the plugin represented by the given {@link PluginItem} and returns
	 * a reference to its main class.
	 * 
	 * @param item the {@link PluginItem} representing the plugin to load
	 * @return A reference to the plugin's main class
	 * @throws PluginManagementException if an error occurred loading the
	 * plugin's main class and resolving its dependencies.
	 */
    private static PluginImpl loadPluginImpl(PluginItem item) throws PluginManagementException {
        PluginClassLoader cl = new PluginClassLoader(item);
        try {
            Class<?> plmain = cl.loadClass(item.getMainClass());
            Object pobj = plmain.newInstance();
            if (pobj instanceof PluginImpl) {
                PluginImpl plug = PluginImpl.class.cast(pobj);
                return plug;
            } else {
                throw new PluginManagementException("Plugin main class does not implement PluginImpl interface");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new PluginManagementException("Class not found on by class loader");
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new PluginManagementException("Could not instantiate plugin");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new PluginManagementException("Could not invoke plugin constructor");
        }
    }
}
