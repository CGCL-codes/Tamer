package fi.vtt.noen.mfw.bundle.common;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import java.util.Properties;

/**
 * Base class for createing OSGI bundleactivators for the plugins.
 *
 * @author Teemu Kanstren
 */
public abstract class BaseActivator implements BundleActivator {

    protected final Logger log;

    protected BaseActivator(Logger log) {
        this.log = log;
    }

    protected void registerPlugin(BundleContext bc, KnowledgeSource plugin, Properties props, String name) throws Exception {
        String[] names = new String[2];
        names[0] = KnowledgeSource.class.getName();
        names[1] = name;
        bc.registerService(names, plugin, props);
    }
}
