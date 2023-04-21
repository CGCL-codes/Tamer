package consciouscode.seedling;

import java.net.URL;
import consciouscode.seedling.config.ConfigResourceFactory;

/**
    A ConfigTree decorator that validates inputs to method calls.
*/
public class ValidatingConfigTree implements ConfigTree {

    public ValidatingConfigTree(ConfigTree implementingTree) {
        myTree = implementingTree;
    }

    public ConfigResourceFactory getResourceFactory() {
        return myTree.getResourceFactory();
    }

    public URL getResourceUrl(String path) {
        if (path == null) {
            throw new IllegalArgumentException("path must not be null");
        }
        return myTree.getResourceUrl(path);
    }

    /**
       @return <code>null</code> if no config file exists for the node.
     */
    public NodeConfiguration getNodeConfiguration(String nodeAddress) throws ConfigurationException {
        if ((nodeAddress == null) || !nodeAddress.startsWith("/")) {
            throw new IllegalArgumentException("Bad nodeAddress: " + nodeAddress);
        }
        return myTree.getNodeConfiguration(nodeAddress);
    }

    private ConfigTree myTree;
}
