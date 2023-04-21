package consciouscode.seedling.config;

import consciouscode.seedling.NodePath;
import consciouscode.util.resource.ResourceTree;
import org.apache.commons.lang.ArrayUtils;

/**
 * A composite {@link ConfigLoader} that attempts loading a node's
 * {@link ConfigResource} from each child loader in sequence.
 * Only the first successful resource is returned.
 */
public class ConfigLoaderSequence implements ConfigLoader {

    public ConfigLoaderSequence() {
        myLoaders = new ConfigLoader[0];
    }

    public ConfigLoaderSequence(ConfigLoader... loaders) {
        myLoaders = loaders;
    }

    public ConfigLoader[] getLoaders() {
        return myLoaders;
    }

    public synchronized void addLoader(ConfigLoader loader) {
        myLoaders = (ConfigLoader[]) ArrayUtils.add(myLoaders, loader);
    }

    public ConfigResource loadConfigResource(ResourceTree resources, NodePath fullPath) throws ConfigLoadingException {
        for (ConfigLoader loader : myLoaders) {
            ConfigResource resource = loader.loadConfigResource(resources, fullPath);
            if (resource != null) return resource;
        }
        return null;
    }

    /**
     * Determines whether the given resource will be used to configure a node,
     * and if so, returns the name of the node.
     * Typically a resource must have an extension recognized by one of this
     * factory's {@link ConfigLoader}s.
     *
     * @param resourceName name of a file that may configure a node.
     *
     * @return the node name, or {@code null} if the resource won't configure
     * a node.
     */
    public String nodeNameForResource(String resourceName) {
        for (ConfigLoader loader : myLoaders) {
            String name = loader.nodeNameForResource(resourceName);
            if (name != null) return name;
        }
        return null;
    }

    private ConfigLoader[] myLoaders;
}
