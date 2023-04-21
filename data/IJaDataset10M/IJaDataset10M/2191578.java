package consciouscode.seedling.config;

import static consciouscode.seedling.SeedlingConstants.ROOT_CONFIG_BASENAME;
import consciouscode.seedling.NodePath;
import consciouscode.seedling.NodeReference;
import consciouscode.util.resource.Resource;
import consciouscode.util.resource.ResourceTree;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A {@link ConfigLayer} that reads resources from a {@link ResourceTree},
 * using a {@link ConfigLoader}.
 */
public class StandardConfigLayer implements ConfigLayer {

    public StandardConfigLayer(ResourceTree resources, ConfigLoader loader) {
        resources.getClass();
        loader.getClass();
        myResources = resources;
        myLoader = loader;
    }

    protected StandardConfigLayer() {
        myResources = null;
        myLoader = null;
    }

    public ResourceTree getResourceTree() {
        return myResources;
    }

    public ConfigLoader getConfigLoader() {
        return myLoader;
    }

    public Iterable<ConfigResource> getConfigResources(NodeReference nodeRef) throws ConfigurationException {
        ArrayList<ConfigResource> resources = new ArrayList<ConfigResource>();
        try {
            collectOuterConfigResources(resources, nodeRef);
            collectInnerConfigResources(resources, nodeRef);
        } catch (ConfigLoadingException cause) {
            throw new ConfigurationException(nodeRef, null, null, cause);
        }
        return resources;
    }

    public void collectNodeNames(String branchPath, Collection<String> names) {
        Resource resource = myResources.getResource(branchPath);
        if (resource == null) return;
        Iterator<Resource> children = resource.childResources();
        while (children.hasNext()) {
            Resource child = children.next();
            String resourceName = child.getName();
            if (resourceName.startsWith(".")) continue;
            String name = myLoader.nodeNameForResource(resourceName);
            if (name != null) {
                if (!name.equals(ROOT_CONFIG_BASENAME)) {
                    names.add(name);
                }
            } else if (child.isDirectory()) {
                names.add(resourceName);
            }
        }
    }

    public URL getResourceUrl(String path) {
        return myResources.getResourceUrl(path);
    }

    @Override
    public String toString() {
        return "[StandardConfigLayer " + myResources + ']';
    }

    protected ConfigResource loadConfig(NodePath configPath) throws ConfigLoadingException {
        if (configPath.isRoot()) {
            configPath = configPath.child(ROOT_CONFIG_BASENAME);
        }
        return myLoader.loadConfigResource(myResources, configPath);
    }

    protected ConfigResource loadImplicitBranchConfig(NodePath configPath) {
        String resourcePath = configPath.toString();
        Resource dirResource = myResources.getResource(resourcePath);
        if (dirResource != null && dirResource.isDirectory()) {
            return new ImplicitBranchConfigResource(dirResource);
        }
        return null;
    }

    /**
     * "Outer" resources are those that are only related to the desired node.
     */
    private void collectOuterConfigResources(List<ConfigResource> resources, NodeReference nodeRef) throws ConfigLoadingException {
        NodePath configPath = nodeRef.toLocalPath();
        ConfigResource resource = loadConfig(configPath);
        if (resource != null) resources.add(resource);
        if (!nodeRef.isRoot()) {
            resource = loadImplicitBranchConfig(configPath);
            if (resource != null) resources.add(resource);
        }
    }

    /**
     * "Inner" resources are those that are provided by configuration of the
     * containing branches.
     */
    private void collectInnerConfigResources(List<ConfigResource> resources, NodeReference nodeRef) throws ConfigurationException, ConfigLoadingException {
        ArrayList<ConfigResource> parentResources = new ArrayList<ConfigResource>();
        LinkedList<String> innerPath = new LinkedList<String>();
        while (!nodeRef.isRoot()) {
            innerPath.addFirst(nodeRef.getNodeName());
            nodeRef = nodeRef.parent();
            collectOuterConfigResources(parentResources, nodeRef);
            for (ConfigResource parentRes : parentResources) {
                ConfigEvaluator eval = parentRes.getEvaluator(nodeRef);
                ConfigResource innerConfig = eval.getInnerConfig(parentRes, innerPath);
                if (innerConfig != null) {
                    resources.add(innerConfig);
                }
            }
            parentResources.clear();
        }
    }

    private final ResourceTree myResources;

    private final ConfigLoader myLoader;
}
