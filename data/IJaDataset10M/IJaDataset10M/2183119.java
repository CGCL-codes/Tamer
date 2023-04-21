package connex.session.plugin;

import java.util.Hashtable;
import java.util.Collection;
import java.util.Enumeration;

public class PluginRegistry {

    private Hashtable plugins = new Hashtable();

    private Hashtable descriptors = new Hashtable();

    /**
     * @directed
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @clientCardinality 1
     */
    private Plugin lnkPlugin;

    /**
     * @directed
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @clientCardinality 1
     */
    private PluginDescriptor lnkPluginDescriptor;

    public PluginRegistry() {
    }

    protected void registerPlugin(String id, Plugin obj) {
        plugins.put(id, obj);
    }

    protected Plugin getPlugin(String id) {
        if (plugins.containsKey(id)) {
            return (Plugin) plugins.get(id);
        }
        return null;
    }

    protected boolean isRegistered(String id) {
        if (descriptors.contains(id)) {
            return true;
        }
        return false;
    }

    protected void registerDescriptor(String id, PluginDescriptor pd) {
        descriptors.put(id, pd);
    }

    protected PluginDescriptor getDescriptor(String id) {
        return (PluginDescriptor) descriptors.get(id);
    }

    protected void removePlugin(String id) {
        Plugin p = (Plugin) plugins.remove(id);
        p.stopPlugin();
    }

    protected void getPluginDescriptor(int id) {
    }

    /**
     *Returns a Collection of all PluginDescriptor objects
     *one for each entry point detected
     */
    public Collection getAllDescriptors() {
        if (descriptors == null) {
            return null;
        }
        return descriptors.values();
    }

    public Collection getAllPlugins() {
        if (plugins == null) {
            return null;
        }
        return plugins.values();
    }

    protected void clear() {
        descriptors.clear();
        Enumeration pluginsnum = (Enumeration) plugins.elements();
        while (pluginsnum.hasMoreElements()) {
            Plugin pd = (Plugin) pluginsnum.nextElement();
            pd.stopPlugin();
        }
        plugins.clear();
    }
}
