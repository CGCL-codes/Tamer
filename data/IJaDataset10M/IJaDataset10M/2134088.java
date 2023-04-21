package dbPhase.testing;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for dbPhase.testing");
        suite.addTest(new NodeTest("testNode"));
        suite.addTest(new NodeTest("testNodeInt"));
        suite.addTest(new NodeTest("testNodeWebId"));
        suite.addTest(new NodeTest("testAddNeighbor"));
        suite.addTest(new NodeTest("testRemoveNeighbor"));
        suite.addTest(new NodeTest("testGetWebId"));
        suite.addTest(new NodeTest("testConstructSimplifiedNodeDomain"));
        suite.addTest(new NodeTest("testAddDownPointer"));
        suite.addTest(new NodeTest("testAddUpPointer"));
        suite.addTest(new NodeTest("testRemoveDownPointer"));
        suite.addTest(new NodeTest("testRemoveUpPointer"));
        suite.addTest(new NodeTest("testSetFold"));
        suite.addTest(new NodeTest("testSetSurrogateFold"));
        suite.addTest(new NodeTest("testSetWebId"));
        suite.addTest(new NodeTest("testSetHeight"));
        suite.addTest(new NodeTest("testGetHeight"));
        suite.addTest(new NodeTest("testGetHeightFromWebId"));
        suite.addTest(new NodeTest("testAddChild"));
        suite.addTest(new NodeTest("testHashCode"));
        suite.addTest(new NodeTest("testGetChild"));
        suite.addTest(new NodeTest("testRemoveFromHyPeerWeb"));
        suite.addTest(new NodeTest("testGetParent"));
        suite.addTest(new NodeTest("testToString"));
        suite.addTest(new NodeTest("testEquals"));
        suite.addTest(new HyPeerWebTest("testGetHyPeerWebString"));
        suite.addTest(new HyPeerWebTest("testGetHyPeerWeb"));
        suite.addTest(new HyPeerWebTest("testClear"));
        suite.addTest(new HyPeerWebTest("testAddNode"));
        suite.addTest(new HyPeerWebTest("testContains"));
        suite.addTest(new HyPeerWebTest("testGetHyPeerWebDatabase"));
        suite.addTest(new HyPeerWebTest("testGetNode"));
        suite.addTest(new HyPeerWebTest("testRemoveNode"));
        suite.addTest(new HyPeerWebTest("testSize"));
        suite.addTest(new HyPeerWebDatabaseTest("testGetHyPeerWebDatabase"));
        suite.addTest(new HyPeerWebDatabaseTest("testGetHyPeerWebDatabaseString"));
        suite.addTest(new HyPeerWebDatabaseTest("testAddNeighborPair"));
        suite.addTest(new HyPeerWebDatabaseTest("testGetNode"));
        suite.addTest(new HyPeerWebDatabaseTest("testDumpNeighbors"));
        suite.addTest(new InsertionDeletionPointPairTest("testGetInsertionPoint"));
        suite.addTest(new InsertionDeletionPointPairTest("testSetInsertionPoint"));
        suite.addTest(new InsertionDeletionPointPairTest("testGetDeletionPoint"));
        suite.addTest(new InsertionDeletionPointPairTest("testSetDeletionPoint"));
        suite.addTest(new WebIdTest("testWebId"));
        suite.addTest(new WebIdTest("testSetId"));
        suite.addTest(new WebIdTest("testGetId"));
        suite.addTest(new WebIdTest("testHeight"));
        suite.addTest(new WebIdTest("testEquals"));
        suite.addTest(new WebIdTest("testToString"));
        suite.addTest(new SendVisitorTest("testInitialParameters"));
        suite.addTest(new SendVisitorTest("testSendVisitor"));
        suite.addTest(new SendVisitorTest("testRecipientsOfVisitor"));
        suite.addTest(new SendVisitorTest("clearContents"));
        suite.addTest(new SendVisitorTest("createHyPeerWebWith"));
        suite.addTest(new BroadCastTest("testBroadcastInitialParameters"));
        suite.addTest(new BroadCastTest("testBroadcastExhaustive"));
        Exp3 test3 = new Exp3();
        return suite;
    }
}
