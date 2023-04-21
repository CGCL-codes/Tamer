package edu.harvard.fas.rbrady.tpteam.tpbridge.model.test;

import junit.framework.Test;
import org.eclipse.hyades.test.common.junit.DefaultTestArbiter;
import org.eclipse.hyades.test.common.junit.HyadesTestCase;
import org.eclipse.hyades.test.common.junit.HyadesTestSuite;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TPEntity;
import edu.harvard.fas.rbrady.tpteam.tpbridge.model.TreeNodeModel;

/**
 * Generated code for the test suite <b>TestTreeNodeModel</b> located at
 * <i>/edu.harvard.fas.rbrady.tpteam.testplugin/src/edu/harvard/fas/rbrady/tpteam/tpbridge/model/test/TestTreeNodeModel.testsuite</i>.
 */
public class TestTreeNodeModel extends HyadesTestCase {

    /** TreeNodeModel under test */
    private static TreeNodeModel mTreeNodeModel;

    /** TPEntity used in tests */
    private static TPEntity mParent;

    /**
	 * Constructor for TestTreeNodeModel.
	 * @param name
	 */
    public TestTreeNodeModel(String name) {
        super(name);
    }

    /**
	 * Returns the JUnit test suite that implements the <b>TestTreeNodeModel</b>
	 * definition.
	 */
    public static Test suite() {
        HyadesTestSuite testTreeNodeModel = new HyadesTestSuite("TestTreeNodeModel");
        testTreeNodeModel.setArbiter(DefaultTestArbiter.INSTANCE).setId("CA0EB60D618C58AA41FAF8C0FB4A11DB");
        testTreeNodeModel.addTest(new TestTreeNodeModel("testAddNodeOutsideModel").setId("CA0EB60D618C58AA902B2560FB4A11DB").setTestInvocationId("CA0EB60D618C58AA9E970290FB4A11DB"));
        testTreeNodeModel.addTest(new TestTreeNodeModel("testDeleteNodeOutsideModel").setId("CA0EB60D618C58AA2A643C60FB4C11DB").setTestInvocationId("CA0EB60D618C58AA34F37BA0FB4C11DB"));
        return testTreeNodeModel;
    }

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        mTreeNodeModel = new TreeNodeModel();
        mParent = new TPEntity("ID_PARENT", "PARENT", "PARENT_DESC", TPEntity.JUNIT_TEST);
        mTreeNodeModel.put(mParent.getID(), mParent);
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        mParent.removeChangeListener(mTreeNodeModel);
        mParent = null;
        mTreeNodeModel = null;
    }

    /**
	* testAddNodeOutsideModel
	* @throws Exception
	*/
    public void testAddNodeOutsideModel() throws Exception {
        assertNotNull("Error: TreeNodeModel under test failed to return parent.", mTreeNodeModel.get(mParent.getID()));
        TPEntity child = new TPEntity("ID_CHILD", "CHILD", "CHILD_DESC", TPEntity.EXEC_PASS);
        mParent.addChild(child);
        assertNotNull("Error: TreeNodeModel under test failed to automatically add child.", mTreeNodeModel.get(child.getID()));
        assertNotNull("Error: TreeNodeModel under test failed to return child with correct name.", mTreeNodeModel.get(child.getID()).getName().equals(child.getName()));
        assertNotNull("Error: TreeNodeModel under test failed to return child with correct parent.", mTreeNodeModel.get(child.getID()).getParent() == mParent);
    }

    /**
	* testDeleteNodeOutsideModel
	* @throws Exception
	*/
    public void testDeleteNodeOutsideModel() throws Exception {
        assertNotNull("Error: TreeNodeModel under test failed to return parent.", mTreeNodeModel.get(mParent.getID()));
        TPEntity child = new TPEntity("ID_CHILD", "CHILD", "CHILD_DESC", TPEntity.EXEC_PASS);
        mParent.addChild(child);
        assertNotNull("Error: TreeNodeModel under test failed to automatically add child.", mTreeNodeModel.get(child.getID()));
        mParent.removeChild(child);
        assertNull("Error: TreeNodeModel under test faild to automatically delete child.", mTreeNodeModel.get(child.getID()));
    }
}
