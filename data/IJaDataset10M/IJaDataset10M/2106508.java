package org.pz.platypus;

import java.net.*;
import java.lang.reflect.*;
import com.google.common.base.*;
import com.google.common.annotations.*;
import org.pz.platypus.exceptions.*;
import org.pz.platypus.utilities.Filename;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The principal function for loading and running output plugins.
 *
 * Loads a JAR file plugin and calls the constructor for the class whose name is the output format
 * with a leading capital letter (Fontlist, Pdf). Then calls process() in the same class. Then exits.
 *
 * @author alb
 */
public class PluginLoader {

    String location;

    GDD gdd;

    public PluginLoader(final String pluginLocation, final GDD Gdd) {
        location = pluginLocation;
        gdd = checkNotNull(Gdd, "GDD passed to PluginLoader.java is null.");
    }

    public void runPlugin() {
        URL pluginUrl = createPluginUrl();
        URL[] urls = { pluginUrl };
        URLClassLoader pluginLoader = new URLClassLoader(urls);
        Thread.currentThread().setContextClassLoader(pluginLoader);
        try {
            String className;
            String jarName = checkNotNull(Filename.getBaseName(location));
            final String capitalizedClass = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, jarName);
            className = "org.pz.platypus.plugin." + jarName.toLowerCase() + "." + capitalizedClass;
            Class pluginStart = Class.forName(className, false, pluginLoader);
            Object plugin = pluginStart.newInstance();
            Class[] classParams = { GDD.class };
            Method method1;
            try {
                gdd.diag("Calling process() in:" + " " + className);
                method1 = pluginStart.getMethod("process", classParams);
            } catch (NoSuchMethodException nsme) {
                gdd.log.severe(gdd.getLit("ERROR.INVALID_PLUGIN_NO_PROCESS_METHOD"));
                throw new StopExecutionException(null);
            }
            try {
                method1.invoke(plugin, gdd);
            } catch (InvalidConfigFileException icfe) {
                return;
            } catch (InvocationTargetException ite) {
                System.err.println("Invocation target exception " + ite);
                ite.printStackTrace();
            }
        } catch (ClassNotFoundException cnf) {
            gdd.log.severe("Plugin class not found " + cnf);
            throw new StopExecutionException(null);
        } catch (InstantiationException ie) {
            System.err.println(ie);
        } catch (IllegalAccessException ie) {
            System.err.println(ie);
        }
    }

    /**
     * Converts a plugin's JAR file name+address into a URL suitable for class loading
     * @return a valid URL, if all went well.
     */
    @VisibleForTesting
    protected URL createPluginUrl() {
        try {
            URL urlForPlugin = new URL("file:" + location);
            gdd.diag(gdd.getLit("PLUGIN_URL") + " " + urlForPlugin.toString());
            return (urlForPlugin);
        } catch (MalformedURLException e) {
            gdd.log.severe(gdd.getLit("ERROR.INVALID_PLUGIN_URL") + ": " + location + "\n" + e);
            throw new StopExecutionException(null);
        }
    }
}
