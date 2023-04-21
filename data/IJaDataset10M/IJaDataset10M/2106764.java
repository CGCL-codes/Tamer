package org.qedeq.kernel.bo.logic.wf;

import org.qedeq.kernel.bo.logic.common.FormulaChecker;
import org.qedeq.kernel.bo.logic.common.LogicalCheckExceptionList;
import org.qedeq.kernel.se.base.list.Element;
import org.qedeq.kernel.se.common.DefaultModuleAddress;
import org.qedeq.kernel.se.common.ModuleContext;
import org.qedeq.kernel.xml.parser.BasicParser;

/**
 * For testing the {@link org.qedeq.kernel.bo.logic.FormulaChecker}.
 * Testing formulas made of predicate variables and predicate constants.
 *
 * @author  Michael Meyling
 */
public class FormulaCheckerPredicateFormulaTest extends AbstractFormulaChecker {

    private ModuleContext context;

    private FormulaChecker checker;

    protected void setUp() throws Exception {
        context = new ModuleContext(new DefaultModuleAddress("http://memory.org/sample.xml"), "getElement()");
        checker = new FormulaCheckerImpl();
    }

    protected void tearDown() throws Exception {
        context = null;
    }

    /**
     * Function: checkFormula(Element)
     * Type:     positive
     * Data:     A
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaPositive01() throws Exception {
        final Element ele = BasicParser.createElement("<PREDVAR id=\"A\"/>");
        assertFalse(checker.checkFormula(ele, context).hasErrors());
        assertFalse(checker.checkFormula(ele, context, getChecker()).hasErrors());
        assertFalse(checker.checkFormula(ele, context, getCheckerWithoutClass()).hasErrors());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     positive
     * Data:     V
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaPositive02() throws Exception {
        final Element ele = BasicParser.createElement("<PREDCON ref=\"true\"/>");
        assertFalse(checker.checkFormula(ele, context).hasErrors());
        assertFalse(checker.checkFormula(ele, context, getChecker()).hasErrors());
        assertFalse(checker.checkFormula(ele, context, getCheckerWithoutClass()).hasErrors());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30720
     * Data:     no function variable name
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative01() throws Exception {
        final Element ele = BasicParser.createElement("<PREDVAR />");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30720, list.get(0).getErrorCode());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30720
     * Data:     no function constant name
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative02() throws Exception {
        final Element ele = BasicParser.createElement("<PREDCON />");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30720, list.get(0).getErrorCode());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30730
     * Data:     function name missing (but list instead of name)
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative09() throws Exception {
        final Element ele = BasicParser.createElement("<PREDVAR > <VAR id=\"x\" /> </PREDVAR>");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30730, list.get(0).getErrorCode());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30730
     * Data:     function name missing (but list instead of name)
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative10() throws Exception {
        final Element ele = BasicParser.createElement("<PREDCON > <VAR id=\"x\" /> </PREDCON>");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30730, list.get(0).getErrorCode());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30730
     * Data:     function name missing
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative13() throws Exception {
        final Element ele = BasicParser.createElement("<PREDVAR> <A/> </PREDVAR>");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30730, list.get(0).getErrorCode());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30730
     * Data:     function name missing
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative14() throws Exception {
        final Element ele = BasicParser.createElement("<PREDCON> <A/> </PREDCON>");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30730, list.get(0).getErrorCode());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30770
     * Data:     f(x, {x| phi})
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative15() throws Exception {
        final Element ele = BasicParser.createElement("<PREDVAR id=\"f\">" + "<VAR id=\"x\"/>" + "<CLASS> <VAR id=\"x\"/> <PREDVAR id=\"\\phi\" /> </CLASS>" + "</PREDVAR>");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30770, list.get(0).getErrorCode());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30770
     * Data:     x in {x| phi}
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative16() throws Exception {
        final Element ele = BasicParser.createElement("<PREDCON ref=\"in\">" + "<VAR id=\"x\"/>" + "<CLASS> <VAR id=\"x\"/> <PREDVAR id=\"\\phi\" /> </CLASS>" + "</PREDCON>");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30770, list.get(0).getErrorCode());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30780
     * Data:     f({x| phi}, x)
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative19() throws Exception {
        final Element ele = BasicParser.createElement("<PREDVAR id=\"f\">" + "<CLASS> <VAR id=\"x\"/> <PREDVAR id=\"\\phi\" /> </CLASS>" + "<VAR id=\"x\"/>" + "</PREDVAR>");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30780, list.get(0).getErrorCode());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30780
     * Data:     {x| phi} in x
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative20() throws Exception {
        final Element ele = BasicParser.createElement("<PREDCON id=\"in\">" + "<CLASS> <VAR id=\"x\"/> <PREDVAR id=\"\\phi\" /> </CLASS>" + "<VAR id=\"x\"/>" + "</PREDCON>");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30780, list.get(0).getErrorCode());
    }

    /**
     * Function: checkFormula(Element)
     * Type:     negative, code 30590
     * Data:     F(x)  (unknown predicate constant)
     *
     * @throws  Exception   Test failed.
     */
    public void testPredicateFormulaNegative22() throws Exception {
        final Element ele = BasicParser.createElement("<PREDCON id=\"F\" />");
        LogicalCheckExceptionList list = checker.checkFormula(ele, context, getChecker());
        assertEquals(1, list.size());
        assertEquals(30590, list.get(0).getErrorCode());
    }
}
