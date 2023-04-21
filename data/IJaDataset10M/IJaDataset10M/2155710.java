package com.bpreece.webserver;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 *
 * @author Ly
 */
public abstract class ModuleController {

    private static final Map<String, ModuleInfo> moduleCache = new TreeMap<String, ModuleInfo>();

    private static Collection<ModuleManager> moduleManagers = new ArrayList<ModuleManager>();

    private static void cache(File moduleFile) throws IOException {
        ModuleInfo newModule = new ModuleInfo(moduleFile);
        moduleCache.put(newModule.name, newModule);
    }

    /**
     * Returns an unmodifiable <code>Map</code> of all modules currently in
     * the module cache.  The key is the module's <em>name</em>, as found in
     * the
     * @param name
     * @return
     */
    public static Map<String, ModuleInfo> getAll(String name) {
        return Collections.unmodifiableMap(moduleCache);
    }

    public static void loadModules(File moduleDirectory) throws IOException {
        Log.config("Loading modules from {0}", moduleDirectory);
        FilenameFilter moduleFilter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".module");
            }
        };
        for (File moduleFile : moduleDirectory.listFiles(moduleFilter)) {
            ModuleController.load(moduleFile);
        }
        FileFilter directoryFilter = new FileFilter() {

            public boolean accept(File pathname) {
                return !pathname.getName().startsWith(".") && pathname.isDirectory();
            }
        };
        for (File subdirectory : moduleDirectory.listFiles(directoryFilter)) {
            loadModules(subdirectory);
        }
        fireModulesLoaded();
    }

    public static void load(File moduleFile) throws IOException {
        Log.info("Loading {0}", moduleFile.getAbsoluteFile());
        Properties properties = new Properties();
        properties.load(new FileReader(moduleFile));
        String jarFileName = properties.getProperty("jarFile");
        String moduleName = properties.getProperty("name");
        String className = properties.getProperty("className");
        ModuleInfo module = moduleCache.get(moduleName);
        if (module != null) {
            Log.warn("The module class {0} required by {1} is already loaded by {2}", className, moduleFile, module.infoFile);
        }
        if (jarFileName != null && !jarFileName.isEmpty()) {
            File jarFile = new File(moduleFile.getParentFile(), jarFileName);
            URL jarFileURL = jarFile.toURI().toURL();
            URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            try {
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(sysloader, jarFileURL);
            } catch (Exception ex) {
                Log.warn("Failed adding {0} to system class path", moduleFile);
                throw new IOException(ex);
            }
        }
        try {
            Log.config("Loading class {0}", className);
            final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            Class<?> moduleClass = Class.forName(className, true, systemClassLoader);
        } catch (SecurityException ex) {
            Log.warn("Security error loading {0} from {1}", className, moduleFile);
            throw new IOException(ex);
        } catch (ClassNotFoundException ex) {
            Log.warn("Can't find class {0} for {1}", className, moduleFile);
            throw new IOException(ex);
        }
        cache(moduleFile);
    }

    public static void addModuleManager(ModuleManager listener) {
        moduleManagers.add(listener);
    }

    public static void removeModuleManager(ModuleManager listener) {
        moduleManagers.remove(listener);
    }

    private static void fireModulesLoaded() {
        for (ModuleManager listener : moduleManagers) {
            listener.moduleLoadComplete();
        }
    }

    private ModuleController() {
        throw new UnsupportedOperationException("DON'T DO THAT!!!");
    }
}
