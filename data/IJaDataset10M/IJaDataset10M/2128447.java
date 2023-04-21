package cytoscape.hyperedge.unittest;

import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.hyperedge.EdgeTypeMap;
import cytoscape.hyperedge.HyperEdge;
import cytoscape.hyperedge.event.EventNote;
import cytoscape.hyperedge.impl.utils.HEUtils;
import giny.model.GraphObject;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Test the various HyperEdgeManager operations.
 * @author Michael L. Creech
 * @version 1.1
 */
public class HyperEdgeManagerTest extends TestBase {

    private static final String IN_HYPER_EDGE = " in HyperEdge ";

    private static final String HE3_NAME = "he3";

    private static final String HE2_NAME = "he2";

    private static final String TEST1_LOC = "hyperedge-manager-test1.xml";

    private static final String TEST2_LOC = "hyperedge-manager-test2.xml";

    private static final String NET3_NAME = "net3";

    private static final String NET4_NAME = "net4";

    private static final String NET6_NAME = "net6";

    private static final String NET7_NAME = "net7";

    private static final String M2_NAME = "M2";

    private static final String B_NAME = "B";

    private static final String C_NAME = "C";

    private static final String D_NAME = "D";

    private static final String E_NAME = "E";

    private static final int THREE = 3;

    private static final int FOUR = 4;

    private static final int FIVE = 5;

    private static final int SIX = 6;

    private static final int SEVEN = 7;

    private static final int EIGHT = 8;

    private static final int NINE = 9;

    private static final int TWELVE = 12;

    private static final int THIRTEEN = 13;

    /**
     * HyperEdge 2's edge to substrate.
     */
    private CyEdge he2Sub;

    /**
     * HyperEdge 2's edge to product.
     */
    private CyEdge he2Prod;

    /**
     * HyperEdge 3's edge to substrate.
     */
    private CyEdge he3Sub;

    /**
     * HyperEdge 3's to to product.
     */
    private CyEdge he3Prod;

    /**
     * HyperEdge 3's edge to mediator 1.
     */
    private CyEdge he3Med1;

    /**
     * HyperEdge 3's edge to mediator 2.
     */
    private CyEdge he3Med2;

    /**
     * HyperEdge 2's UUID of the edge to the substrate.
     */
    private String he2SubUuid;

    /**
     * HyperEdge 2's UUID of the edge to the product.
     */
    private String he2ProdUuid;

    /**
     * HyperEdge 3's UUID of the edge to the substrate.
     */
    private String he3SubUuid;

    /**
     * HyperEdge 3's UUID of the edge to the product.
     */
    private String he3ProdUuid;

    /**
     * HyperEdge 3's UUID of the edge to the mediator 1.
     */
    private String he3Med1Uuid;

    /**
     * HyperEdge 3's UUID of the edge to the mediator 2.
     */
    private String he3Med2Uuid;

    /**
     * HyperEdge 2.
     */
    private HyperEdge he2;

    /**
     * HyperEdge 3.
     */
    private HyperEdge he3;

    /**
     * Network 6.
     */
    private CyNetwork net6;

    /**
     * Network 7.
     */
    private CyNetwork net7;

    private CyNode bNode;

    private CyNode cNode;

    private CyNode dNode;

    private CyNode eNode;

    private PersistenceHelper pHelper = new PersistenceHelper();

    private boolean saved;

    /** 
     * Bonehead Checkstyle requires constructor and javadoc.
     */
    public HyperEdgeManagerTest() {
        super();
    }

    /**
     * JUnit method for running tests for this class.
     * @return the Test to peform.
     */
    public static Test suite() {
        return new TestSuite(HyperEdgeManagerTest.class);
    }

    /**
     * Main for test.
     * @param args standard args to main program
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    private void testSetup(final InMemoryAndRestoredTestType test) {
        manager.reset(false);
        setUp1(!saved);
        bNode = Cytoscape.getCyNode(B_NAME, true);
        cNode = Cytoscape.getCyNode(C_NAME, true);
        dNode = Cytoscape.getCyNode(D_NAME, true);
        eNode = Cytoscape.getCyNode(E_NAME, true);
        net6 = Cytoscape.createNetwork(NET6_NAME);
        net7 = Cytoscape.createNetwork(NET7_NAME);
        he2 = factory.createHyperEdge(aNode, EdgeTypeMap.SUBSTRATE, bNode, EdgeTypeMap.PRODUCT, net6);
        he2.setName(HE2_NAME);
        he2Sub = he2.getAnEdge(aNode);
        he2Prod = he2.getAnEdge(bNode);
        if (!saved) {
            he2SubUuid = he2Sub.getIdentifier();
            he2ProdUuid = he2Prod.getIdentifier();
        }
        final List<String> edges = new ArrayList<String>();
        edges.add(EdgeTypeMap.SUBSTRATE);
        edges.add(EdgeTypeMap.PRODUCT);
        edges.add(EdgeTypeMap.ACTIVATING_MEDIATOR);
        edges.add(EdgeTypeMap.INHIBITING_MEDIATOR);
        final List<CyNode> nodes = new ArrayList<CyNode>();
        nodes.add(aNode);
        nodes.add(bNode);
        nodes.add(cNode);
        nodes.add(dNode);
        he3 = factory.createHyperEdge(nodes, edges, net7);
        he3.setName(HE3_NAME);
        he3Sub = he3.getAnEdge(aNode);
        he3Prod = he3.getAnEdge(bNode);
        he3Med1 = he3.getAnEdge(cNode);
        he3Med2 = he3.getAnEdge(dNode);
        if (!saved) {
            he3SubUuid = he3Sub.getIdentifier();
            he3ProdUuid = he3Prod.getIdentifier();
            he3Med1Uuid = he3Med1.getIdentifier();
            he3Med2Uuid = he3Med2.getIdentifier();
        }
        new ChangeTester(he1, EventNote.Type.HYPEREDGE, EventNote.SubType.ADDED, true, net6);
        he1.addToNetwork(net6);
        he1.addToNetwork(net7);
        he1.removeFromNetwork(net1);
        Assert.assertTrue(containsHEParts(net6, he1));
        new ChangeTester(hd1, EventNote.Type.HYPEREDGE, EventNote.SubType.ADDED, true, net6);
        hd1.addToNetwork(net6);
        hd1.removeFromNetwork(net1);
        test.extraSetup();
        if (!saved) {
            pHelper.saveTestHelper(TEST1_LOC, net6);
            pHelper.saveTestHelper(TEST2_LOC, net7);
            saved = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void tearDown1(final boolean fireEvents) {
        super.tearDown1(fireEvents);
        final Set<CyNetwork> nets = (Set<CyNetwork>) Cytoscape.getNetworkSet();
        for (CyNetwork net : nets) {
            Cytoscape.destroyNetwork(net);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void reconnectInstanceVariables() {
        super.reconnectInstanceVariables();
        he2 = null;
        he3 = null;
        he2Sub = null;
        he2Prod = null;
        he3Sub = null;
        he3Prod = null;
        he3Med1 = null;
        he3Med2 = null;
        CyEdge edge;
        String eUuid;
        final Iterator<CyEdge> edgeIt = manager.getEdgesByNetwork(null);
        while (edgeIt.hasNext()) {
            edge = edgeIt.next();
            eUuid = edge.getIdentifier();
            if (he2SubUuid.equals(eUuid)) {
                he2Sub = edge;
            } else if (he2ProdUuid.equals(eUuid)) {
                he2Prod = edge;
            } else if (he3SubUuid.equals(eUuid)) {
                he3Sub = edge;
            } else if (he3ProdUuid.equals(eUuid)) {
                he3Prod = edge;
            } else if (he3Med1Uuid.equals(eUuid)) {
                he3Med1 = edge;
            } else if (he3Med2Uuid.equals(eUuid)) {
                he3Med2 = edge;
            }
        }
        String hName;
        HyperEdge he;
        final Iterator<HyperEdge> heIt = manager.getHyperEdgesByNetwork(null);
        while (heIt.hasNext()) {
            he = heIt.next();
            hName = he.getName();
            if (HE2_NAME.equals(hName)) {
                he2 = he;
            } else if (HE3_NAME.equals(hName)) {
                he3 = he;
            }
        }
        bNode = Cytoscape.getCyNode(B_NAME, false);
        cNode = Cytoscape.getCyNode(C_NAME, false);
        dNode = Cytoscape.getCyNode(D_NAME, false);
        eNode = Cytoscape.getCyNode(E_NAME, false);
        Assert.assertNotNull(he2);
        Assert.assertNotNull(he3);
        Assert.assertNotNull(he2Sub);
        Assert.assertNotNull(he2Prod);
        Assert.assertNotNull(he3Sub);
        Assert.assertNotNull(he3Prod);
        Assert.assertNotNull(he3Med1);
        Assert.assertNotNull(bNode);
        Assert.assertNotNull(cNode);
        Assert.assertNotNull(dNode);
        Assert.assertNotNull(eNode);
        final Set<CyNetwork> nets = (Set<CyNetwork>) Cytoscape.getNetworkSet();
        HEUtils.log("nets size = " + nets.size());
        for (CyNetwork net : nets) {
            HEUtils.log("NET = " + net.getTitle());
            final String reconnectMsg = "reconnect Network with name = ";
            if (NET6_NAME.equals(net.getTitle())) {
                HEUtils.log(reconnectMsg + NET6_NAME);
                net6 = net;
            } else if (NET7_NAME.equals(net.getTitle())) {
                HEUtils.log(reconnectMsg + NET7_NAME);
                net7 = net;
            }
        }
    }

    /**
     * Overall HyperEdgeManager tester.
     */
    public void testHyperEdgeManager() {
        runTest(new TestAddAndRemoveFromCyNetwork());
        runTest(new TestGetEdgesByCyNetwork());
        runTest(new TestGetEdgesByNode());
        runTest(new TestGetCyNetwork());
        runTest(new TestGetHyperEdgeForConnectorNode());
        runTest(new TestGetHyperEdgesByEdgeTypes());
        runTest(new TestGetHyperEdgesByCyNetwork());
        runTest(new TestGetHyperEdgesByNode());
        runTest(new TestGetHyperEdgesByNodes());
        runTest(new TestGetHyperEdgeVersion());
        runTest(new TestGetHyperEdgeVersionNumber());
        runTest(new TestGetNodesByEdgeTypes());
        runTest(new TestGetNumEdges());
        runTest(new TestGetNumHyperEdges());
        runTest(new TestGetNumNodes());
        runTest(new TestInHyperEdge());
        runTest(new TestIsConnectorNode());
        runTest(new TestReset());
    }

    private void runTest(final InMemoryAndRestoredTestType test) {
        testSetup(test);
        test.runIt();
        tearDown1(false);
    }

    private boolean containsHEParts(final CyNetwork gp, final HyperEdge he) {
        CyNode node;
        CyEdge edge;
        final Iterator<CyEdge> edgeIt = he.getEdges(null);
        while (edgeIt.hasNext()) {
            edge = edgeIt.next();
            if (!gp.containsEdge(edge)) {
                logDidNotFindGo("edge", edge, he, (CyNetwork) gp);
                return false;
            }
        }
        final Iterator<CyNode> nodeIt = he.getNodes(null);
        while (nodeIt.hasNext()) {
            node = nodeIt.next();
            if (!gp.containsNode(node)) {
                logDidNotFindGo("node", node, he, (CyNetwork) gp);
                return false;
            }
        }
        return true;
    }

    private void logDidNotFindGo(final String nodeOrEdge, final GraphObject go, final HyperEdge he, final CyNetwork net) {
        HEUtils.log("Didn't find " + nodeOrEdge + " " + go.getIdentifier() + IN_HYPER_EDGE + he.getName() + " that should belong to the CyNetwork " + net.getTitle());
    }

    private boolean containsNoHEParts(final CyNetwork gp, final Iterator<CyEdge> edgeIt, final String heName) {
        CyEdge edge;
        while (edgeIt.hasNext()) {
            edge = edgeIt.next();
            if (gp.containsEdge(edge)) {
                HEUtils.log("Found edge " + edge.getIdentifier() + IN_HYPER_EDGE + heName + " that shouldn't belong to the CyNetwork " + ((CyNetwork) gp).getTitle());
                return false;
            }
        }
        return true;
    }

    private interface InMemoryAndRestoredTestType {

        void runIt();

        void extraSetup();
    }

    private final class TestGetCyNetwork implements InMemoryAndRestoredTestType {

        private TestGetCyNetwork() {
        }

        public void runIt() {
            testIterator(he1.getNetworks(), 2);
            testIterator(he3.getNetworks(), 1);
            testIterator(he2.getNetworks(), 1);
            testIterator(hd1.getNetworks(), 1);
        }

        public void extraSetup() {
        }
    }

    private final class TestGetHyperEdgeForConnectorNode implements InMemoryAndRestoredTestType {

        private TestGetHyperEdgeForConnectorNode() {
        }

        public void runIt() {
            final CyNode he1Con = he1.getConnectorNode();
            final CyNode he2Con = he2.getConnectorNode();
            Assert.assertNull(manager.getHyperEdgeForConnectorNode(null));
            Assert.assertNull(manager.getHyperEdgeForConnectorNode(sNode));
            HyperEdge he = manager.getHyperEdgeForConnectorNode(he1Con);
            Assert.assertTrue(he == he1);
            he = manager.getHyperEdgeForConnectorNode(he2Con);
            Assert.assertTrue(he == he2);
        }

        public void extraSetup() {
        }
    }

    private final class TestGetHyperEdgesByCyNetwork implements InMemoryAndRestoredTestType {

        private TestGetHyperEdgesByCyNetwork() {
        }

        public void runIt() {
            testIterator(manager.getHyperEdgesByNetwork(null), FOUR);
            final CyNetwork net3 = Cytoscape.createNetwork(NET3_NAME);
            testIterator(manager.getHyperEdgesByNetwork(net3), 0);
            testIterator(manager.getHyperEdgesByNetwork(net6), THREE);
            testIterator(manager.getHyperEdgesByNetwork(net7), 2);
        }

        public void extraSetup() {
        }
    }

    private final class TestGetHyperEdgesByNode implements InMemoryAndRestoredTestType {

        private TestGetHyperEdgesByNode() {
        }

        public void runIt() {
            testIterator(manager.getHyperEdgesByNode(null, null), FOUR);
            testIterator(manager.getHyperEdgesByNode(null, net6), THREE);
            testIterator(manager.getHyperEdgesByNode(null, net7), 2);
            testIterator(manager.getHyperEdgesByNode(sNode, null), 2);
            testIterator(manager.getHyperEdgesByNode(aNode, null), 2);
            testIterator(manager.getHyperEdgesByNode(sNode, net6), 2);
            testIterator(manager.getHyperEdgesByNode(aNode, net6), 1);
            testIterator(manager.getHyperEdgesByNode(dNode, net6), 0);
        }

        public void extraSetup() {
        }
    }

    private final class TestGetHyperEdgesByNodes implements InMemoryAndRestoredTestType {

        private TestGetHyperEdgesByNodes() {
        }

        public void runIt() {
            testIterator(manager.getHyperEdgesByNodes(null, null), FOUR);
            testIterator(manager.getHyperEdgesByNodes(null, net6), THREE);
            testIterator(manager.getHyperEdgesByNodes(null, net7), 2);
            final Collection<CyNode> col = new ArrayList<CyNode>();
            testIterator(manager.getHyperEdgesByNodes(col, null), 0);
            col.add(sNode);
            testIterator(manager.getHyperEdgesByNodes(col, null), 2);
            testIterator(manager.getHyperEdgesByNodes(col, net6), 2);
            col.add(pNode);
            testIterator(manager.getHyperEdgesByNodes(col, null), 1);
            testIterator(manager.getHyperEdgesByNodes(col, net6), 1);
            col.clear();
            col.add(aNode);
            col.add(bNode);
            testIterator(manager.getHyperEdgesByNodes(col, null), 2);
            testIterator(manager.getHyperEdgesByNodes(col, net6), 1);
            testIterator(manager.getHyperEdgesByNodes(col, net7), 1);
            col.add(cNode);
            testIterator(manager.getHyperEdgesByNodes(col, net6), 0);
            testIterator(manager.getHyperEdgesByNodes(col, net7), 1);
            col.add(sNode);
            testIterator(manager.getHyperEdgesByNodes(col, null), 0);
        }

        public void extraSetup() {
        }
    }

    private final class TestGetHyperEdgeVersion implements InMemoryAndRestoredTestType {

        private TestGetHyperEdgeVersion() {
        }

        public void runIt() {
            final String version = manager.getHyperEdgeVersion();
            Assert.assertTrue((version != null) && (version.length() > 0));
        }

        public void extraSetup() {
        }
    }

    private final class TestGetHyperEdgeVersionNumber implements InMemoryAndRestoredTestType {

        private TestGetHyperEdgeVersionNumber() {
        }

        public void runIt() {
            final Double versionNum = manager.getHyperEdgeVersionNumber();
            Assert.assertTrue((versionNum != null));
        }

        public void extraSetup() {
        }
    }

    private final class TestGetHyperEdgesByEdgeTypes implements InMemoryAndRestoredTestType {

        private TestGetHyperEdgesByEdgeTypes() {
        }

        public void runIt() {
            testIterator(manager.getHyperEdgesByEdgeTypes(null, null), FOUR);
            testIterator(manager.getHyperEdgesByEdgeTypes(null, net6), THREE);
            testIterator(manager.getHyperEdgesByEdgeTypes(null, net7), 2);
            final Collection<String> col = new ArrayList<String>();
            testIterator(manager.getHyperEdgesByEdgeTypes(col, null), 0);
            col.add(EdgeTypeMap.SUBSTRATE);
            testIterator(manager.getHyperEdgesByEdgeTypes(col, null), FOUR);
            testIterator(manager.getHyperEdgesByEdgeTypes(col, net6), THREE);
            testIterator(manager.getHyperEdgesByEdgeTypes(col, net7), 2);
            col.add(EdgeTypeMap.PRODUCT);
            testIterator(manager.getHyperEdgesByEdgeTypes(col, null), THREE);
            testIterator(manager.getHyperEdgesByEdgeTypes(col, net6), 2);
            testIterator(manager.getHyperEdgesByEdgeTypes(col, net7), 2);
            col.add(EdgeTypeMap.ACTIVATING_MEDIATOR);
            testIterator(manager.getHyperEdgesByEdgeTypes(col, null), 2);
            testIterator(manager.getHyperEdgesByEdgeTypes(col, net6), 1);
            testIterator(manager.getHyperEdgesByEdgeTypes(col, net7), 2);
            col.add("mediator1");
            testIterator(manager.getHyperEdgesByEdgeTypes(col, null), 0);
        }

        public void extraSetup() {
        }
    }

    private final class TestGetNodesByEdgeTypes implements InMemoryAndRestoredTestType {

        private TestGetNodesByEdgeTypes() {
        }

        public void runIt() {
            testIterator(manager.getNodesByEdgeTypes(null, null), SEVEN);
            testIterator(manager.getNodesByEdgeTypes(null, net6), FIVE);
            testIterator(manager.getNodesByEdgeTypes(null, net7), SEVEN);
            final Collection<String> col = new ArrayList<String>();
            testIterator(manager.getNodesByEdgeTypes(col, null), 0);
            col.add(EdgeTypeMap.SUBSTRATE);
            testIterator(manager.getNodesByEdgeTypes(col, null), 2);
            testIterator(manager.getNodesByEdgeTypes(col, net6), 2);
            testIterator(manager.getNodesByEdgeTypes(col, net7), 2);
            col.add(EdgeTypeMap.INHIBITING_MEDIATOR);
            testIterator(manager.getNodesByEdgeTypes(col, null), FOUR);
            col.remove(EdgeTypeMap.INHIBITING_MEDIATOR);
            col.add(EdgeTypeMap.ACTIVATING_MEDIATOR);
            testIterator(manager.getNodesByEdgeTypes(col, net7), FOUR);
            testIterator(manager.getNodesByEdgeTypes(col, net6), THREE);
            col.clear();
            col.add("jojo");
            testIterator(manager.getNodesByEdgeTypes(col, null), 0);
        }

        public void extraSetup() {
        }
    }

    private final class TestGetEdgesByNode implements InMemoryAndRestoredTestType {

        private TestGetEdgesByNode() {
        }

        public void runIt() {
            testIterator(manager.getEdgesByNode(null, null), manager.getNumEdges(null));
            testIterator(manager.getEdgesByNode(null, net6), manager.getNumEdges(net6));
            testIterator(manager.getEdgesByNode(null, net7), manager.getNumEdges(net7));
            testIterator(manager.getEdgesByNode(sNode, null), THREE);
            testIterator(manager.getEdgesByNode(aNode, null), 2);
            testIterator(manager.getEdgesByNode(sNode, net7), 1);
            testIterator(manager.getEdgesByNode(aNode, net6), 1);
            testIterator(manager.getEdgesByNode(dNode, net6), 0);
        }

        public void extraSetup() {
        }
    }

    private final class TestGetNumHyperEdges implements InMemoryAndRestoredTestType {

        private TestGetNumHyperEdges() {
        }

        public void runIt() {
            Assert.assertTrue(manager.getNumHyperEdges(null) == FOUR);
            Assert.assertTrue(manager.getNumHyperEdges(net6) == THREE);
            Assert.assertTrue(manager.getNumHyperEdges(net7) == 2);
            final CyNetwork net4 = Cytoscape.createNetwork(NET4_NAME);
            Assert.assertTrue(manager.getNumHyperEdges(net4) == 0);
        }

        public void extraSetup() {
        }
    }

    private final class TestGetNumNodes implements InMemoryAndRestoredTestType {

        private TestGetNumNodes() {
        }

        public void runIt() {
            Assert.assertTrue(manager.getNumNodes(null) == EIGHT);
            Assert.assertTrue(manager.getNumNodes(net6) == SIX);
            Assert.assertTrue(manager.getNumNodes(net7) == EIGHT);
            final CyNetwork net4 = Cytoscape.createNetwork(NET4_NAME);
            Assert.assertTrue(manager.getNumNodes(net4) == 0);
        }

        public void extraSetup() {
            final CyNode m2 = Cytoscape.getCyNode(M2_NAME, true);
            he1.addEdge(m2, EdgeTypeMap.INHIBITING_MEDIATOR);
        }
    }

    private final class TestGetNumEdges implements InMemoryAndRestoredTestType {

        private TestGetNumEdges() {
        }

        public void runIt() {
            Assert.assertTrue(manager.getNumEdges(null) == TWELVE);
            Assert.assertTrue(manager.getNumEdges(net6) == EIGHT);
            Assert.assertTrue(manager.getNumEdges(net7) == SEVEN);
            final CyNetwork net4 = Cytoscape.createNetwork(NET4_NAME);
            Assert.assertTrue(manager.getNumEdges(net4) == 0);
        }

        public void extraSetup() {
        }
    }

    private final class TestGetEdgesByCyNetwork implements InMemoryAndRestoredTestType {

        private TestGetEdgesByCyNetwork() {
        }

        public void runIt() {
            testIterator(manager.getEdgesByNetwork(null), THIRTEEN);
            final CyNetwork net3 = Cytoscape.createNetwork(NET3_NAME);
            testIterator(manager.getEdgesByNetwork(net3), 0);
            testIterator(manager.getEdgesByNetwork(net6), NINE);
            testIterator(manager.getEdgesByNetwork(net7), EIGHT);
        }

        public void extraSetup() {
            final CyNode m2 = Cytoscape.getCyNode(M2_NAME, true);
            he1.addEdge(m2, EdgeTypeMap.INHIBITING_MEDIATOR);
        }
    }

    private final class TestInHyperEdge implements InMemoryAndRestoredTestType {

        private TestInHyperEdge() {
        }

        public void runIt() {
            Assert.assertFalse(manager.inHyperEdge(null, null));
            Assert.assertFalse(manager.inHyperEdge(null, net6));
            Assert.assertFalse(manager.inHyperEdge(eNode, null));
            Assert.assertTrue(manager.inHyperEdge(sNode, null));
            Assert.assertTrue(manager.inHyperEdge(sNode, net6));
            Assert.assertTrue(manager.inHyperEdge(sNode, net7));
            Assert.assertFalse(manager.inHyperEdge(dNode, net6));
            Assert.assertTrue(manager.inHyperEdge(dNode, net7));
        }

        public void extraSetup() {
        }
    }

    private final class TestIsConnectorNode implements InMemoryAndRestoredTestType {

        private TestIsConnectorNode() {
        }

        public void runIt() {
            final CyNode he1Con = he1.getConnectorNode();
            final CyNode hd1Con = hd1.getConnectorNode();
            final CyNode he3Con = he3.getConnectorNode();
            Assert.assertFalse(manager.isConnectorNode(null, null));
            Assert.assertFalse(manager.isConnectorNode(sNode, null));
            Assert.assertFalse(manager.isConnectorNode(bNode, net6));
            Assert.assertFalse(manager.isConnectorNode(he3Con, net6));
            Assert.assertFalse(manager.isConnectorNode(hd1Con, net7));
            Assert.assertTrue(manager.isConnectorNode(he1Con, net6));
            Assert.assertTrue(manager.isConnectorNode(he1Con, net7));
            Assert.assertTrue(manager.isConnectorNode(he1Con, null));
            Assert.assertTrue(manager.isConnectorNode(hd1Con, null));
            Assert.assertTrue(manager.isConnectorNode(he3Con, null));
        }

        public void extraSetup() {
        }
    }

    private final class TestReset implements InMemoryAndRestoredTestType {

        private TestReset() {
        }

        public void runIt() {
            final CyNode hd1Con = hd1.getConnectorNode();
            final CyNode he3Con = he3.getConnectorNode();
            manager.reset(false);
            Assert.assertTrue(0 == manager.getNumEdges(null));
            Assert.assertTrue(0 == manager.getNumNodes(null));
            Assert.assertTrue(0 == manager.getNumHyperEdges(null));
            Assert.assertNotNull(Cytoscape.getCyNode(hd1Con.getIdentifier(), false));
            Assert.assertNotNull(Cytoscape.getCyNode(he3Con.getIdentifier(), false));
            manager.reset(false);
            manager.reset(true);
            testSetup(this);
            manager.reset(true);
            Assert.assertTrue(0 == manager.getNumEdges(null));
            Assert.assertTrue(0 == manager.getNumNodes(null));
            Assert.assertTrue(0 == manager.getNumHyperEdges(null));
            manager.reset(true);
            manager.reset(false);
        }

        public void extraSetup() {
        }
    }

    private final class TestAddAndRemoveFromCyNetwork implements InMemoryAndRestoredTestType {

        private TestAddAndRemoveFromCyNetwork() {
        }

        public void runIt() {
            Assert.assertTrue(containsHEParts(net6, he2));
            Assert.assertTrue(containsHEParts(net6, hd1));
            Assert.assertTrue(containsHEParts(net7, he1));
            Assert.assertTrue(containsHEParts(net7, he3));
            testIterator(manager.getHyperEdgesByNetwork(null), FOUR);
            final CyNetwork emptyNet = Cytoscape.createNetwork("emptyNet");
            Assert.assertFalse(he1.removeFromNetwork(emptyNet));
            new ChangeTester(he1, EventNote.Type.HYPEREDGE, EventNote.SubType.REMOVED, true, net6);
            final Iterator<CyEdge> edgesIt = he1.getEdges(null);
            String heName = he1.getName();
            Assert.assertTrue(he1.removeFromNetwork(net6));
            Assert.assertTrue(containsNoHEParts(net6, edgesIt, heName));
            Assert.assertFalse(he1.removeFromNetwork(net6));
            testIterator(manager.getHyperEdgesByNetwork(net6), 2);
            testIterator(manager.getHyperEdgesByNetwork(net7), 2);
            testIterator(manager.getHyperEdgesByNetwork(null), FOUR);
            new ChangeTester(he1, EventNote.Type.HYPEREDGE, EventNote.SubType.REMOVED, true, net7);
            Iterator<CyEdge> edgeIt = HEUtils.createCollection(he1.getEdges(null)).iterator();
            heName = he1.getName();
            Assert.assertTrue(he1.removeFromNetwork(net7));
            Assert.assertTrue(containsNoHEParts(net7, edgeIt, heName));
            testIterator(manager.getHyperEdgesByNetwork(net7), 1);
            testIterator(manager.getHyperEdgesByNetwork(null), THREE);
            edgeIt = HEUtils.createCollection(he3.getEdges(null)).iterator();
            heName = he3.getName();
            Assert.assertTrue(he3.removeFromNetwork(net7));
            Assert.assertTrue(containsNoHEParts(net7, edgeIt, heName));
            testIterator(manager.getHyperEdgesByNetwork(net7), 0);
            edgeIt = HEUtils.createCollection(hd1.getEdges(null)).iterator();
            heName = hd1.getName();
            Assert.assertTrue(hd1.removeFromNetwork(net6));
            Assert.assertTrue(containsNoHEParts(net6, edgesIt, heName));
            testIterator(manager.getHyperEdgesByNetwork(net6), 1);
            edgeIt = HEUtils.createCollection(he2.getEdges(null)).iterator();
            heName = he2.getName();
            Assert.assertTrue(he2.removeFromNetwork(net6));
            Assert.assertTrue(containsNoHEParts(net6, edgesIt, heName));
            testIterator(manager.getHyperEdgesByNetwork(net6), 0);
            testIterator(manager.getHyperEdgesByNetwork(null), 0);
        }

        public void extraSetup() {
        }
    }
}
