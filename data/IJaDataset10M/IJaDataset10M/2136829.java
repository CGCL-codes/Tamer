package consciouscode.seedling;

import java.util.Properties;
import consciouscode.seedling.junit.StandaloneSeedlingTestCase;

public class ScopeBranchTest extends StandaloneSeedlingTestCase {

    public static final String AUTO_SUITES = "common";

    public static final String LOCAL_SCOPE = "local";

    public void setUp() throws Exception {
        super.setUp();
        myScopeManager = makeScopeManager(LOCAL_SCOPE);
        myGlobalRoot.installChild("Scopes", myScopeManager);
        myLocalRoot = myScopeManager.createScope();
        myLocalRoot.setLog(myLogger);
    }

    public void tearDown() throws Exception {
        myScopeManager = null;
        myLocalRoot = null;
        super.tearDown();
    }

    public void testGetMissingNode() {
        try {
            myLocalRoot.getNode("/nowhere");
            fail("Expected exception");
        } catch (NodeInstantiationException e) {
        }
    }

    public void testGetNodeFromHigherScope() throws Exception {
        String nodeAddress = "/testNode";
        SimpleBean proto = new SimpleBean();
        proto.setText("some value");
        proto.setInt(42);
        proto.setInteger(new Integer(587));
        myConfigTree.setNodeConfiguration(nodeAddress, proto.getConfig());
        Object node = myLocalRoot.getNode(nodeAddress);
        assertNotNull(node);
        assertEquals(SimpleBean.class, node.getClass());
        SimpleBean bean = (SimpleBean) node;
        assertEquals(proto, bean);
        Object node2 = myLocalRoot.getNode(nodeAddress);
        assertSame(node, node2);
        assertSame(node, myGlobalRoot.getInstalledNode(nodeAddress));
        assertNull(myLocalRoot.getLocalInstalledChild("testNode"));
    }

    public void testGetLocalNode() throws Exception {
        String nodeAddress = "/local";
        SimpleBean proto = new SimpleBean();
        proto.setText("some value");
        proto.setInt(42);
        proto.setInteger(new Integer(587));
        Properties props = proto.getProperties();
        props.setProperty(SeedlingConstants.SCOPE_PROP, LOCAL_SCOPE);
        myConfigTree.setNodeConfiguration(nodeAddress, props);
        Object node = myLocalRoot.getNode(nodeAddress);
        assertNotNull(node);
        assertEquals(SimpleBean.class, node.getClass());
        SimpleBean bean = (SimpleBean) node;
        assertEquals(proto, bean);
        Object node2 = myLocalRoot.getNode(nodeAddress);
        assertSame(node, node2);
        assertNull(myGlobalRoot.getInstalledNode(nodeAddress));
        assertNotNull(myLocalRoot.getLocalInstalledChild("local"));
        assertSame(node, myLocalRoot.getLocalInstalledChild("local"));
    }

    public void testInstallBadScope() throws Exception {
        String nodeAddress = "/local";
        SimpleBean proto = new SimpleBean();
        proto.setText("some value");
        proto.setInt(42);
        proto.setInteger(new Integer(587));
        Properties props = proto.getProperties();
        props.setProperty(SeedlingConstants.SCOPE_PROP, "bad_scope");
        myConfigTree.setNodeConfiguration(nodeAddress, props);
        try {
            myLocalRoot.getNode(nodeAddress);
            fail("Expected NodeInstantiationException");
        } catch (NodeInstantiationException e) {
        }
    }

    public void testAliasesBetweenScopes() throws Exception {
        String aliasPath = "/Alias";
        String globalPath = "/GlobalBean";
        String localPath = "/LocalBean";
        SimpleBean globalBean = new SimpleBean();
        globalBean.setText("I am global!");
        SeedlingUtils.installNode(myGlobalRoot, globalPath, globalBean);
        SimpleBean localBean = new SimpleBean();
        localBean.setText("I am local!");
        SeedlingUtils.installNode(myLocalRoot, localPath, localBean);
        Properties props = new Properties();
        props.setProperty(SeedlingConstants.ALIAS_PROP, globalPath);
        props.setProperty(SeedlingConstants.SCOPE_PROP, LOCAL_SCOPE);
        myConfigTree.setNodeConfiguration(aliasPath, props);
        Object aliasBean = myLocalRoot.getNode(aliasPath);
        assertSame(globalBean, aliasBean);
        assertSame(aliasBean, myLocalRoot.uninstallChild("Alias"));
        try {
            aliasBean = myGlobalRoot.getNode(aliasPath);
            fail("Expected NodeInstantiationException, got " + aliasBean);
        } catch (NodeInstantiationException e) {
        }
        props = new Properties();
        props.setProperty(SeedlingConstants.ALIAS_PROP, localPath);
        props.setProperty(SeedlingConstants.SCOPE_PROP, LOCAL_SCOPE);
        myConfigTree.setNodeConfiguration(aliasPath, props);
        aliasBean = myLocalRoot.getNode(aliasPath);
        assertSame(localBean, aliasBean);
        assertSame(aliasBean, myLocalRoot.uninstallChild("Alias"));
        try {
            aliasBean = myGlobalRoot.getNode(aliasPath);
            fail("Expected NodeInstantiationException, got " + aliasBean);
        } catch (NodeInstantiationException e) {
        }
        props = new Properties();
        props.setProperty(SeedlingConstants.ALIAS_PROP, localPath);
        myConfigTree.setNodeConfiguration(aliasPath, props);
        try {
            aliasBean = myGlobalRoot.getNode(aliasPath);
            fail("Expected NodeInstantiationException, got " + aliasBean);
        } catch (NodeInstantiationException e) {
        }
        try {
            aliasBean = myLocalRoot.getNode(aliasPath);
            fail("Expected NodeInstantiationException, got " + aliasBean);
        } catch (NodeInstantiationException e) {
        }
    }

    public void testRemoteNestedNode() throws Exception {
        String localAddress = "/local";
        String remoteAddress = "/remote";
        SimpleBean localProto = new SimpleBean();
        localProto.setText("some value");
        localProto.setInt(42);
        localProto.setInteger(new Integer(587));
        Properties props = localProto.getProperties();
        props.setProperty(SeedlingConstants.SCOPE_PROP, LOCAL_SCOPE);
        props.setProperty("otherNode", remoteAddress);
        myConfigTree.setNodeConfiguration(localAddress, props);
        SimpleBean remoteProto = new SimpleBean();
        remoteProto.setText("I am remote!");
        props = remoteProto.getProperties();
        myConfigTree.setNodeConfiguration(remoteAddress, props);
        Object node = myLocalRoot.getNode(localAddress);
        assertNotNull(node);
        assertEquals(SimpleBean.class, node.getClass());
        SimpleBean bean = (SimpleBean) node;
        assertEquals(localProto, bean);
        assertEquals(remoteProto, bean.getOtherNode());
        assertSame(node, myLocalRoot.getLocalInstalledChild("local"));
        assertSame(bean.getOtherNode(), myGlobalRoot.getInstalledNode(remoteAddress));
    }

    private ScopeManager myScopeManager;

    private RootNode myLocalRoot;
}
