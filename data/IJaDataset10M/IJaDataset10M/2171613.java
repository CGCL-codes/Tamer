package org.xaware.server.engine.instruction;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xaware.server.engine.IResourceManager;
import org.xaware.server.engine.IScriptNode;
import org.xaware.server.engine.context.BizDocContext;
import org.xaware.server.resources.XAwareBeanFactory;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.testing.util.BaseBdpTestCase;

/**
 * This class is the unit test case for the element version of
 * the xa:switch instuction.
 * 
 * @author jtarnowski
 */
public class TestXaSwitchElemInst extends BaseBdpTestCase {

    private static final String expectedGoodResult = "<Root><X /></Root>";

    private static final String expectedNoMatchResult = "<Root />";

    private static final Namespace xa = XAwareConstants.xaNamespace;

    private final BizDocContext aContext;

    public TestXaSwitchElemInst() {
        super("TestXaSwitchElemInst");
        final IResourceManager rMgr = XAwareBeanFactory.getResourceManager();
        final InstructionTestHelper instHelper = new InstructionTestHelper(getClass(), getDataFolder(), rMgr);
        aContext = instHelper.getBizDocContext("XAElemSwitchInst_1.xbd");
    }

    /**
     * Directory where xbd's & expected results can be found
     */
    @Override
    protected String getDataFolder() {
        return "data/org/xaware/server/engine/instruction/";
    }

    /**
     * Test xa:switch with relative substitution in switch
     */
    public void testSwitch_relativeSubstitution_1() {
        clearInputParams();
        setInputXmlFileName(null);
        setBizDocFileName("XAElemSwitchInst_1.xbd");
        setExpectedOutputFileName("XAElemSwitchInstExpectedResult_1.xml");
        getTestHelper().setTestMethodName("testSwitch_relativeSubstitution_1");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        evaluateBizDoc();
    }

    /**
     * Test xa:switch with relative substitution in case
     */
    public void testSwitch_relativeSubstitution_2() {
        clearInputParams();
        setInputXmlFileName(null);
        setBizDocFileName("XAElemSwitchInst_2.xbd");
        setExpectedOutputFileName("XAElemSwitchInstExpectedResult_1.xml");
        getTestHelper().setTestMethodName("testSwitch_relativeSubstitution_2");
        setSaveOutput(false);
        setTransformOutputToExpected(false);
        evaluateBizDoc();
    }

    /**
     * Process the xa:switch and return a String representing the result
     * 
     * @param root -
     *            Element
     * @return String
     */
    private String processElementSwitch(final Element root) {
        String result = null;
        final Element xaSwitch = root.getChild("switch", xa);
        final IScriptNode sNode = new ScriptNode();
        sNode.setup(aContext, xaSwitch, null);
        final Instruction instruction = new XASwitchInst();
        instruction.setupInstruction(sNode, null);
        try {
            instruction.configure();
            instruction.initialize();
            final Format format = Format.getCompactFormat();
            final XMLOutputter out = new XMLOutputter(format);
            result = out.outputString(root);
        } catch (final XAwareException e) {
            fail("Exception in processSwitch: " + e);
        }
        return result;
    }

    /**
     * Second xa:case element matches. No substitution.
     */
    public void testSwitchInstSimpleCase() {
        final Element root = new Element("Root");
        final Element xaSwitch = new Element("switch", xa);
        xaSwitch.setAttribute("value", "16", xa);
        root.addContent(xaSwitch);
        Element xaCase = new Element("case", xa);
        xaCase.setAttribute("value", "8", xa);
        xaCase.addContent(new Element("A"));
        xaSwitch.addContent(xaCase);
        xaCase = new Element("case", xa);
        xaCase.setAttribute("value", "16", xa);
        xaCase.addContent(new Element("X"));
        xaSwitch.addContent(xaCase);
        final Element xaDefault = new Element("default", xa);
        xaDefault.addContent(new Element("Z"));
        xaSwitch.addContent(xaDefault);
        final String result = processElementSwitch(root);
        assertEquals("testSwitchInstSimpleCase() failed", expectedGoodResult, result);
    }

    /**
     * No xa:case element matches. Uses xa:default. No substitution.
     */
    public void testSwitchInstSimpleDefault() {
        final Element root = new Element("Root");
        final Element xaSwitch = new Element("switch", xa);
        xaSwitch.setAttribute("value", "16", xa);
        root.addContent(xaSwitch);
        Element xaCase = new Element("case", xa);
        xaCase.setAttribute("value", "8", xa);
        xaCase.addContent(new Element("A"));
        xaSwitch.addContent(xaCase);
        xaCase = new Element("case", xa);
        xaCase.setAttribute("value", "AA", xa);
        xaCase.addContent(new Element("B"));
        xaSwitch.addContent(xaCase);
        final Element xaDefault = new Element("default", xa);
        xaDefault.addContent(new Element("X"));
        xaSwitch.addContent(xaDefault);
        final String result = processElementSwitch(root);
        assertEquals("testSwitchInstSimpleDefault() failed", expectedGoodResult, result);
    }

    /**
     * No xa:case element matches. No xa:default present. No substitution.
     */
    public void testSwitchInstSimpleNoMatch() {
        final Element root = new Element("Root");
        final Element xaSwitch = new Element("switch", xa);
        xaSwitch.setAttribute("value", "16", xa);
        root.addContent(xaSwitch);
        Element xaCase = new Element("case", xa);
        xaCase.setAttribute("value", "8", xa);
        xaCase.addContent(new Element("A"));
        xaSwitch.addContent(xaCase);
        xaCase = new Element("case", xa);
        xaCase.setAttribute("value", "1", xa);
        xaCase.addContent(new Element("X"));
        xaSwitch.addContent(xaCase);
        final String result = processElementSwitch(root);
        assertEquals("testSwitchInstNoMatch() failed", expectedNoMatchResult, result);
    }

    /**
     * Evaluate the cases numeric relation. No substitution.
     */
    public void testSwitchInstEvalNumeric() {
        final Element root = new Element("Root");
        final Element xaSwitch = new Element("switch", xa);
        xaSwitch.setAttribute("value", "16", xa);
        root.addContent(xaSwitch);
        Element xaCase = new Element("case", xa);
        xaCase.setAttribute("value", "< 8", xa);
        xaCase.addContent(new Element("A"));
        xaSwitch.addContent(xaCase);
        xaCase = new Element("case", xa);
        xaCase.setAttribute("value", "> 8", xa);
        xaCase.addContent(new Element("X"));
        xaSwitch.addContent(xaCase);
        final Element xaDefault = new Element("default", xa);
        xaDefault.addContent(new Element("Z"));
        xaSwitch.addContent(xaDefault);
        final String result = processElementSwitch(root);
        assertEquals("testSwitchInstSimpleCase() failed", expectedGoodResult, result);
    }

    /**
     * Evaluate the cases non-numeric relation. No substitution.
     */
    public void testSwitchInstEvalNonNumeric() {
        final Element root = new Element("Root");
        final Element xaSwitch = new Element("switch", xa);
        xaSwitch.setAttribute("value", "XX", xa);
        root.addContent(xaSwitch);
        Element xaCase = new Element("case", xa);
        xaCase.setAttribute("value", "<X", xa);
        xaCase.addContent(new Element("A"));
        xaSwitch.addContent(xaCase);
        xaCase = new Element("case", xa);
        xaCase.setAttribute("value", "> X", xa);
        xaCase.addContent(new Element("X"));
        xaSwitch.addContent(xaCase);
        final Element xaDefault = new Element("default", xa);
        xaDefault.addContent(new Element("Z"));
        xaSwitch.addContent(xaDefault);
        final String result = processElementSwitch(root);
        assertEquals("testSwitchInstSimpleCase() failed", expectedGoodResult, result);
    }

    /**
     * Substitution on xa:switch only. There is a match
     */
    public void DONOTtestSwitchInstSubstituteSwitch() {
    }

    /**
     * Substitution on xa:switch, xa:case. There is a match
     */
    public void DONOTtestSwitchInstSubstituteAll() {
    }

    /**
     * Substitution on xa:switch, xa:case, xa:default No match so use xa:default
     */
    public void DONOTtestSwitchInstSubstituteDefault() {
    }
}
