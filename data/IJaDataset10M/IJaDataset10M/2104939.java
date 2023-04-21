package net.n0fx.netserve.extension.registry;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import net.n0fx.netserve.classloader.util.ClassLoaderUtil;
import net.n0fx.netserve.configuration.ITypedProperties;
import net.n0fx.netserve.configuration.TypedProperties;
import net.n0fx.netserve.exception.NetServeRuntimeException;
import net.n0fx.netserve.extension.Configuration;
import net.n0fx.netserve.extension.IConfiguration;
import net.n0fx.netserve.extension.IConfigurationHandler;
import net.n0fx.netserve.extension.IRegistryInitializer;
import net.n0fx.netserve.server.Server;
import org.apache.log4j.Logger;
import org.igfay.jfig.JFigIF;

public class Registries implements IRegistryInitializer {

    private static final Logger log = Logger.getLogger(Registries.class);

    static {
        log.debug(Registries.class + " loaded by " + Registries.class.getClassLoader());
    }

    /**
	 * Registry
	 */
    public static final String SECTION_REGISTRIES = "Registries";

    /**
	 * All activated registries.
	 */
    private ArrayList registries = new ArrayList();

    private ArrayList handlers;

    private ArrayList usedURLs;

    /**
	 * Classloader for all registries.
	 */
    private static ClassLoader classloader;

    public void configure(JFigIF configuration) {
        final Map registries = configuration.getSection(SECTION_REGISTRIES);
        usedURLs = new ArrayList();
        handlers = new ArrayList();
        boolean disableFileLocking = false;
        if (registries != null) {
            for (final Iterator iter = registries.entrySet().iterator(); iter.hasNext(); ) {
                final Map.Entry registry = (Map.Entry) iter.next();
                try {
                    disableFileLocking = configureRegistry(configuration, usedURLs, handlers, disableFileLocking, registry);
                } catch (Exception e) {
                    log.error("Could not configure registry", e);
                }
            }
        }
    }

    private boolean configureRegistry(JFigIF configuration, ArrayList urls, ArrayList handlers, boolean disableFileLocking, final Map.Entry registry) {
        String section = (String) registry.getKey();
        if (isEnabled((String) registry.getValue())) {
            ITypedProperties props = getRegistryProperties(configuration, section);
            RegistryConfigurationHandler serverConfigurationHandler = getServerConfigurationHandler(props, section);
            RegistryConfiguration rc = new RegistryConfiguration(serverConfigurationHandler, props);
            handlers.add(rc);
            addLibrary(serverConfigurationHandler.getLibrary());
            disableFileLocking |= serverConfigurationHandler.isFileLockingDisabled();
        } else {
            logDisabledRegistry(registry);
        }
        return disableFileLocking;
    }

    private RegistryConfigurationHandler getServerConfigurationHandler(ITypedProperties props, String section) {
        RegistryConfigurationHandler confHandler = new RegistryConfigurationHandler();
        confHandler.configure(section, props);
        return confHandler;
    }

    private ITypedProperties getRegistryProperties(JFigIF configuration, String section) {
        ITypedProperties props = new TypedProperties(configuration.getSectionAsProperties(section));
        return props;
    }

    public void start(ClassLoader classloader) {
        Registries.classloader = classloader;
        for (Iterator iter = handlers.iterator(); iter.hasNext(); ) {
            try {
                RegistryConfiguration rc = (RegistryConfiguration) iter.next();
                IRegistry registry = getRegistryConfigurationHandler(rc.getHandler());
                IConfigurationHandler ch = getServiceConfigurationHandler(rc);
                IConfiguration config = new Configuration(rc.getHandler(), ch, classloader);
                registry.configureExtension(config);
                this.registries.add(registry);
            } catch (Throwable e) {
                log.error("Could not start registry", e);
            }
        }
        handlers.clear();
    }

    /**
	 * 
	 * @param handler
	 * @return
	 */
    private IConfigurationHandler getServiceConfigurationHandler(RegistryConfiguration handler) {
        try {
            String sch = handler.getHandler().getConfigurationHandler();
            IConfigurationHandler ch = (IConfigurationHandler) classloader.loadClass(sch).newInstance();
            ch.configure(handler.getProperties());
            return ch;
        } catch (final InstantiationException e) {
            throw new NetServeRuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new NetServeRuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new NetServeRuntimeException(e);
        }
    }

    /**
	 * Loads the custom {@link IServiceConfigurationHandler}. If no custom
	 * handler is specified a {@link DefaultServiceConfigurationHandler} will be
	 * returned
	 * 
	 * @param serverConfiguration
	 * @return IConfigurationHandler Configuration handler
	 */
    private IRegistry getRegistryConfigurationHandler(final RegistryConfigurationHandler registryConfiguration) {
        try {
            String registryHandler = registryConfiguration.getRegistry();
            return (IRegistry) classloader.loadClass(registryHandler).newInstance();
        } catch (final InstantiationException e) {
            throw new NetServeRuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new NetServeRuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new NetServeRuntimeException(e);
        }
    }

    private void addLibrary(String libraries) {
        URL[] urls = ClassLoaderUtil.convertToUrls(libraries);
        for (int i = 0; i < urls.length; i++) {
            if (!usedURLs.contains(urls[i])) {
                usedURLs.add(urls[i]);
            }
        }
    }

    /**
	 * Returns true if the input string is equals "enabled"
	 * 
	 * @param enabled
	 *            input string
	 * @return true if the input string is equals "enabled"
	 */
    private boolean isEnabled(final String enabled) {
        return Server.CONFIG_ENABLED.equals(enabled);
    }

    private void logDisabledRegistry(final Map.Entry registry) {
        if (log.isInfoEnabled()) {
            log.info("Controller [" + registry.getKey() + "] is " + registry.getValue());
        }
    }

    public ClassLoader getClassloader() {
        return classloader;
    }

    public static ClassLoader getRegistryClassLoader() {
        return classloader;
    }

    public void stop() {
    }

    public ArrayList getUsedUrls() {
        return usedURLs;
    }
}
