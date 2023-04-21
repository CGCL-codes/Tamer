package de.esoco.microsafe.model;

import j2meunit.framework.AssertionFailedError;
import j2meunit.framework.Test;
import j2meunit.framework.TestCase;
import j2meunit.framework.TestMethod;
import j2meunit.framework.TestSuite;
import de.esoco.j2me.model.MutableHierarchyNode;
import de.esoco.j2me.model.HierarchyNode;
import de.esoco.j2me.model.NodeAccessException;
import de.esoco.j2me.storage.HierarchicalRecordStorage;
import de.esoco.j2me.storage.StorageException;
import de.esoco.j2me.util.Log;
import de.esoco.microsafe.MicroSafe;
import de.esoco.microsafe.crypto.InvalidKeyException;
import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import org.bouncycastle.crypto.CryptoException;

/********************************************************************
 * Test cases for HierarchicalRecordStorage.
 *
 * @author eso
 * @see    de.esoco.j2me.storage.HierarchicalRecordStorage
 */
public class MicroSafeModelStorageTest extends TestCase {

    static final String RECORDSTORE_NAME = "MicroSafe-StorageTest";

    MicroSafe aApp = null;

    MicroSafeModel aModel = null;

    HierarchicalRecordStorage aHRS = null;

    Hashtable aRandomAccess = new Hashtable();

    byte[] aKey = "12345678".getBytes();

    /***************************************
	 * Default constructor.
	 */
    public MicroSafeModelStorageTest() {
    }

    /***************************************
	 * TestOne constructor comment.
	 *
	 * @see TestCase#TestCase(String, TestMethod)
	 */
    public MicroSafeModelStorageTest(String sTestName, TestMethod rTestMethod) {
        super(sTestName, rTestMethod);
    }

    /***************************************
	 * The number of test steps in this test case.
	 *
	 * @return The number of steps
	 */
    public int countTestSteps() {
        return 10;
    }

    /***************************************
	 * Builds up the node hierarchy used by the other tests.
	 *
	 * @throws StorageException
	 * @throws NodeAccessException
	 */
    public void createNodeHierarchy() throws StorageException, NodeAccessException {
        MutableHierarchyNode aNode;
        aNode = createChildNode(aModel, 1, false);
        aNode = createChildNode(aNode, 11, false);
        createChildNode(aNode, 111, true);
        createChildNode(aNode, 112, true);
        createChildNode(aNode, 113, true);
        aNode = createChildNode(aModel, 2, false);
        createChildNode(aNode, 21, true);
        aNode = createChildNode(aNode, 22, false);
        createChildNode(aNode, 221, true);
    }

    /***************************************
	 * Tries to delete an eventually existing remnant test RecordStore.
	 *
	 * @see j2meunit.framework.TestCase#setUp()
	 */
    public void setUp() {
        aApp = new MicroSafe();
        Log.setLogLevel(Log.ALL);
        try {
            RecordStore.deleteRecordStore(RECORDSTORE_NAME);
        } catch (RecordStoreException e1) {
        }
        try {
            RecordStore aRS = RecordStore.openRecordStore(RECORDSTORE_NAME, true);
            aHRS = new HierarchicalRecordStorage(aRS);
            aModel = new MicroSafeModel(aHRS);
            aModel.initFromStorage();
        } catch (Exception e) {
            fail("Exception during setUp: " + e.getMessage());
        }
    }

    /***************************************
	 * To create the test suite.
	 *
	 * @see TestCase#suite()
	 */
    public Test suite() {
        TestSuite aSuite = new TestSuite();
        aSuite.addTest(new MicroSafeModelStorageTest("testModelStorage", new TestMethod() {

            public void run(TestCase tc) {
                ((MicroSafeModelStorageTest) tc).testModelStorage();
            }
        }));
        return aSuite;
    }

    /***************************************
	 * Deletes the test RecordStore.
	 *
	 * @see j2meunit.framework.TestCase#tearDown()
	 */
    public void tearDown() {
        try {
            aModel.close();
            RecordStore.deleteRecordStore(RECORDSTORE_NAME);
        } catch (Exception e) {
            fail("Exception during tearDown: " + e.getMessage());
        } finally {
            aApp = null;
        }
    }

    /***************************************
	 * The main test method.
	 */
    public void testModelStorage() {
        try {
            createNodeHierarchy();
            testStepFinished();
            readNodeHierarchy();
            testStepFinished();
            accessNodeHierarchy();
            testStepFinished();
            encryptNodeHierarchy();
            testStepFinished();
            accessEncryptedNodeHierarchy();
            testStepFinished();
            checkKeyVerification();
            testStepFinished();
            reEncryptNodeHierarchy();
            testStepFinished();
            decryptNodeHierarchy();
            testStepFinished();
            removeEncryption();
            testStepFinished();
        } catch (Exception e) {
            if (MicroSafe.DEBUG) {
                e.printStackTrace();
            }
            fail("Exception during test: " + e.getMessage() + " [" + e + "]");
        }
    }

    /***************************************
	 * Test access to encrypted nodes.
	 *
	 * @throws CryptoException
	 * @throws StorageException
	 * @throws NodeAccessException
	 * @throws IOException
	 */
    void accessEncryptedNodeHierarchy() throws CryptoException, StorageException, NodeAccessException, IOException {
        readNodeHierarchy();
        try {
            accessNodeHierarchy();
            throw new IllegalAccessException();
        } catch (AssertionFailedError e) {
        } catch (IllegalAccessException e) {
            fail("Access to encrypted nodes without key: Exception expected");
        }
        aModel.setEncryptionKey(aKey);
        accessNodeHierarchy();
    }

    /***************************************
	 * Test the writing and reading of a EncryptableNode tree to a
	 * HierarchicalRecordStorage.
	 *
	 * @throws StorageException
	 * @throws NodeAccessException
	 */
    void accessNodeHierarchy() throws StorageException, NodeAccessException {
        HierarchyNode aChild;
        aChild = aModel.getChild(0);
        verifyChildNode(aChild, 1, false, 1);
        aChild = aChild.getChild(0);
        verifyChildNode(aChild, 11, false, 3);
        verifyChildNode(aChild.getChild(0), 111, true, 0);
        verifyChildNode(aChild.getChild(1), 112, true, 0);
        verifyChildNode(aChild.getChild(2), 113, true, 0);
        aChild = aModel.getChild(1);
        verifyChildNode(aChild, 2, false, 2);
        verifyChildNode(aChild.getChild(0), 21, true, 0);
        verifyChildNode(aChild.getChild(1), 22, false, 1);
        verifyChildNode(aChild.getChild(1).getChild(0), 221, true, 0);
    }

    /***************************************
	 * Test access to encrypted nodes.
	 *
	 * @throws CryptoException
	 * @throws StorageException
	 * @throws NodeAccessException
	 * @throws IOException
	 */
    void checkKeyVerification() throws CryptoException, StorageException, NodeAccessException, IOException {
        readNodeHierarchy();
        try {
            aModel.setEncryptionKey("wrongkey".getBytes());
            fail("Access to encrypted nodes without key: Exception expected");
        } catch (InvalidKeyException e) {
        }
        aModel.setEncryptionKey(aKey);
        accessNodeHierarchy();
    }

    /***************************************
	 * Internal method to add a new child to a parent node.
	 *
	 * @param  rParent The Parent to add the child to
	 * @param  nID     The ID value to generate the Node data from
	 * @param  bLeaf   If TRUE the child will be a leaf node with text ata,
	 *                 otherwise it will be a group node
	 *
	 * @return The new child node
	 *
	 * @throws NodeAccessException
	 * @throws StorageException
	 */
    MutableHierarchyNode createChildNode(MutableHierarchyNode rParent, int nID, boolean bLeaf) throws NodeAccessException, StorageException {
        String sType = (bLeaf ? HierarchyNode.NT_TEXT : HierarchyNode.NT_GROUP);
        MutableHierarchyNode aChild = rParent.createChild(sType, "Node" + nID);
        if (bLeaf) {
            aChild.setData(new String("This is the data of Node " + nID).getBytes());
        }
        rParent.insertChild(aChild, rParent.getChildCount());
        return aChild;
    }

    /***************************************
	 * Test decryption of nodes.
	 *
	 * @throws CryptoException
	 * @throws StorageException
	 * @throws NodeAccessException
	 */
    void decryptNodeHierarchy() throws CryptoException, StorageException, NodeAccessException {
        aModel.setEncryption(false);
        accessNodeHierarchy();
    }

    /***************************************
	 * Test encryption of nodes.
	 *
	 * @throws CryptoException
	 * @throws StorageException
	 * @throws NodeAccessException
	 */
    void encryptNodeHierarchy() throws CryptoException, StorageException, NodeAccessException {
        aModel.setEncryptionKey(aKey);
        aModel.setEncryption(true);
        accessNodeHierarchy();
    }

    /***************************************
	 * Reads back in the node hierarchy created by the method
	 * testCreateNodeHierarchy() (which must run first).
	 *
	 * @throws StorageException
	 * @throws NodeAccessException
	 * @throws IOException
	 */
    void readNodeHierarchy() throws StorageException, NodeAccessException, IOException {
        aHRS.reset();
        aModel = new MicroSafeModel(aHRS);
        aModel.initFromStorage();
        assertTrue("Wrong root title", aModel.getTitle().equals("MicroSafe"));
        assertTrue("Wrong root node type: " + aModel.getNodeType(), aModel.getNodeType() == HierarchyNode.NT_GROUP);
        assertTrue("Root child count != 2: " + aModel.getChildCount(), aModel.getChildCount() == 2);
    }

    /***************************************
	 * Test re-encryption of all nodes with another key.
	 *
	 * @throws CryptoException
	 * @throws StorageException
	 * @throws NodeAccessException
	 */
    void reEncryptNodeHierarchy() throws CryptoException, StorageException, NodeAccessException {
        aKey = "abcdefgh".getBytes();
        aModel.setEncryptionKey(aKey);
        accessNodeHierarchy();
    }

    /***************************************
	 * Test removing the encryption.
	 *
	 * @throws CryptoException
	 * @throws StorageException
	 * @throws NodeAccessException
	 */
    void removeEncryption() throws CryptoException, StorageException, NodeAccessException {
        aModel.setEncryption(true);
        aModel.setEncryptionKey(null);
        accessNodeHierarchy();
    }

    /***************************************
	 * Internal method to verify the contents of a child node.
	 *
	 * @param rNode       The node to verify
	 * @param nID         The ID the node data must match
	 * @param bLeaf       If TRUE the node is assumed to be a leaf node with
	 *                    data
	 * @param nChildCount The expected number of child nodes
	 */
    void verifyChildNode(HierarchyNode rNode, int nID, boolean bLeaf, int nChildCount) {
        String sExpTitle = "Node" + nID;
        String sExpData = "This is the data of Node " + nID;
        assertTrue("Wrong title: '" + rNode.getTitle() + "' ('" + sExpTitle + "' expected)", rNode.getTitle().equals(sExpTitle));
        assertTrue("" + nID + ":Child count != " + nChildCount, rNode.getChildCount() == nChildCount);
        if (bLeaf) {
            assertTrue("Wrong node type: " + rNode.getNodeType() + " (" + HierarchyNode.NT_TEXT + " expected)", rNode.getNodeType() == HierarchyNode.NT_TEXT);
            assertTrue("Wrong data: '" + new String(rNode.getData()) + "' ('" + sExpData + "' expected)", new String(rNode.getData()).equals(sExpData));
        } else {
            assertTrue("Wrong node type: " + rNode.getNodeType() + " (" + HierarchyNode.NT_GROUP + " expected)", rNode.getNodeType() == HierarchyNode.NT_GROUP);
        }
    }
}
