package consciouscode.seedling.config;

import static consciouscode.seedling.NodeReference.forPath;
import consciouscode.seedling.NodeReference;
import consciouscode.seedling.config.properties.PropertiesConfigResource;
import consciouscode.seedling.junit.StandaloneSeedlingTestCase;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

public abstract class ConfigLayerTestCase extends StandaloneSeedlingTestCase {

    protected File myConfigRootFile;

    protected ConfigLayer myLayer;

    public void testCollectNodeNames() throws Exception {
        String[] contents = { "branch", "branch2", "InnerConfig", "subclass", "SimpleNode" };
        HashSet<String> names = new HashSet<String>();
        myLayer.collectNodeNames("/", names);
        assertEqualContents(contents, names);
        File branch = new File(myConfigRootFile, "branch");
        contents = branch.list();
        names.clear();
        myLayer.collectNodeNames("/branch", names);
        contents = new String[] { "DeepNode" };
        assertEqualContents(contents, names);
    }

    public void testGetNodeConfiguration() throws Exception {
        verifyNoConfigResource("/NonExistantFile");
        verifyNoConfigResource("/branch/NonExistantFile");
        verifyNoConfigResource("/nonExistantBranch/NonExistantFile");
        verifyOneConfigResource("/");
        verifyOneConfigResource("/SimpleNode");
        Iterator<ConfigResource> configs = resources("/branch").iterator();
        assertTrue(configs.next() instanceof PropertiesConfigResource);
        assertTrue(configs.next() instanceof ImplicitBranchConfigResource);
        verifyOneConfigResource("/branch/DeepNode");
        configs = resources("/branch2").iterator();
        assertTrue("no ConfigResource found", configs.hasNext());
        verifyOneConfigResource("/branch2/DeepNode");
    }

    void verifyNoConfigResource(String path) throws Exception {
        Iterable<ConfigResource> configs = resources(path);
        assertFalse("unexpected ConfigResource", configs.iterator().hasNext());
    }

    void verifyOneConfigResource(String path) throws Exception {
        Iterator<ConfigResource> resources = resources(path).iterator();
        ConfigResource config = resources.next();
        PropertiesConfigResource props = (PropertiesConfigResource) config;
        assertEquals(path, props.getProperty("path"));
        assertFalse("unexpected ConfigResource", resources.hasNext());
    }

    private Iterable<ConfigResource> resources(String path) throws ConfigurationException {
        NodeReference ref = forPath(getGlobalRoot(), path);
        Iterable<ConfigResource> configResources = myLayer.getConfigResources(ref);
        assertNotEquals(null, configResources);
        return configResources;
    }
}
