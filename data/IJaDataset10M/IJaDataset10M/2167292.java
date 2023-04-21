package org.xaware.server.engine.instruction;

import org.jdom.Element;
import org.xaware.server.engine.IBizViewContext;
import org.xaware.server.engine.IResourceManager;
import org.xaware.server.engine.IScriptNode;
import org.xaware.server.engine.context.BizDocContext;
import org.xaware.server.engine.enums.SubstitutionFailureLevel;
import org.xaware.server.resources.XAwareBeanFactory;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.testing.util.BaseBdpTestCase;

/**
 * 
 * @author jweaver
 */
public class TestXaRemoveInst extends BaseBdpTestCase {

    static final String DATA_FOLDER = "data/org/xaware/server/engine/instruction/";

    static final String BIZ_DOC_FILE_NAME = "TestXaRemoveInst.xbd";

    static final String TEST_INPUT_YES = "TestXaRemoveInst";

    static final String TEST_INPUT_NO = "TestXaRemoveNoInst";

    static final String YES_EXPECTED_RESULTS = "TestXARemoveInstExpectedResult.xml";

    static final String NO_EXPECTED_RESULTS = "TestXARemoveNoInstExpectedResult.xml";

    static final String TEST_NODE_NAME = "myTestNode";

    IResourceManager rMgr = null;

    InstructionTestHelper instTestHelper = null;

    IBizViewContext aContext = null;

    public TestXaRemoveInst(final String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        rMgr = XAwareBeanFactory.getResourceManager();
        assertNotNull("Failed to establish the ResourceManager", rMgr);
        instTestHelper = new InstructionTestHelper(getClass(), DATA_FOLDER, rMgr);
        aContext = instTestHelper.getBizDocContext(BIZ_DOC_FILE_NAME);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Evaluate the Instruction Parser to make sure it created the xa:remove instruction, and it is labled a post
     * instruction
     */
    public void testXaRemoveIsPost() throws Exception {
        final Element testNode = new Element("TestNode");
        testNode.setAttribute(XAwareConstants.XAWARE_ATTR_REMOVE, "yes", XAwareConstants.xaNamespace);
        final IScriptNode sn = aContext.getScriptNode(testNode);
        try {
            sn.setEffectiveSubstitutionFailureLevel(SubstitutionFailureLevel.ERROR);
            sn.configure();
        } catch (final XAwareException e) {
            fail(e);
        }
        assertTrue("IScriptNode is not a BaseScriptNode", sn instanceof BaseScriptNode);
        final BaseScriptNode bsn = (BaseScriptNode) sn;
        assertTrue("There should not be any pre instructions in the ScriptNode", bsn.getInstructionList().size() == 0);
        assertTrue("There should not be any post instructions in the ScriptNode", bsn.getPostInstructionList().size() == 0);
        aContext.setupSecondPass(bsn.getRegistry());
        final IScriptNode sn2 = aContext.getScriptNode(testNode);
        try {
            sn2.setEffectiveSubstitutionFailureLevel(SubstitutionFailureLevel.ERROR);
            sn2.configure();
        } catch (final XAwareException e) {
            fail(e);
        }
        final BaseScriptNode bsn2 = (BaseScriptNode) sn2;
        assertTrue("There should not be any pre instructions in the ScriptNode", bsn2.getInstructionList().size() == 0);
        assertTrue("Script Node is missing the expected 'POST' instruction", bsn2.getPostInstructionList().size() > 0);
        final Object obj = bsn2.getPostInstructionList().get(0);
        assertTrue("The xa:remove instruction is missing in the 'POST' list", obj instanceof XARemoveSecondPassInst);
        assertTrue("There are more than one xa:remove 'POST' instruction", bsn2.getPostInstructionList().size() == 1);
    }

    /**
     * Test the xa:remove will promote its children and remove itself when it is "yes"
     */
    public void testSimpleXaRemove() {
        final BizDocContext bdc = instTestHelper.getBizDocContext(TEST_INPUT_YES);
        System.out.println("BizDocContext " + bdc);
        instTestHelper.evaluateInstruction(TEST_NODE_NAME, YES_EXPECTED_RESULTS);
    }

    /**
     * Test the xa:remove will neither disturn its children or remove itself when it is "no". However, it should remove
     * the xa:remove attribute
     */
    public void testXaRemoveNo() {
        final BizDocContext bdc = instTestHelper.getBizDocContext(TEST_INPUT_NO);
        System.out.println("BizDocContext " + bdc);
        instTestHelper.evaluateInstruction(TEST_NODE_NAME, NO_EXPECTED_RESULTS);
    }

    /**
     * For integration level tests.
     */
    @Override
    public String getDataFolder() {
        return DATA_FOLDER + "remove/";
    }

    /**
     * Integration level test. From QA's automation test suite.
     */
    public void test_QAa11_001_rmv_pos() {
        clearInputParams();
        setInputXmlFileName(null);
        setBizDocFileName("a11_001_rmv_pos.xbd");
        setExpectedOutputFileName("a11_001_rmv_pos.xml");
        getTestHelper().setTestMethodName("RMVa11_001_rmv_pos");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        evaluateBizDoc();
    }

    /**
     * Integration level test. From QA's automation test suite.
     */
    public void test_QAa11_012_rmv_pos() {
        clearInputParams();
        setInputXmlFileName(null);
        setBizDocFileName("a11_012_rmv_pos.xbd");
        setExpectedOutputFileName("a11_012_rmv_pos.xml");
        getTestHelper().setTestMethodName("RMVa11_012_rmv_pos");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        evaluateBizDoc();
    }
}
