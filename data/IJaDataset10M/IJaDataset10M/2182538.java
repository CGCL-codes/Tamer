package ch.ethz.mxquery.Optional.FullAxis;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class ancestor_or_selfAxisclass extends XQueryTestBase {

    public void test_ancestorself_1() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: ancestorself-1 :)\n(: Description: Evaluation of the ancestor-or-self axis for which the context node is not a node. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(200)/ancestor-or-self::*").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0019" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_ancestorself_2() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-2 :)\n(: Description: Evaluation of the ancestor-or-self axis for which the given node does not exists. :)\n(: Uses fn:count to avoid empty file. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:count($input-context1/works/employee[1]/ancestor-or-self::noSuchNode)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_3() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-3 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"is\" expression (return true). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/ancestor-or-self::works) is ($input-context1/works)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_4() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-4 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"is\" expression (return false). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/ancestor-or-self::works) is ($input-context1/works/employee[1])").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_5() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-5 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"node before\" expression (return true). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/ancestor-or-self::works) << ($input-context1/works/employee[1])").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_6() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-6 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"node before\" expression and both operands are the same (return false). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/ancestor-or-self::works) << ($input-context1/works/employee[1]/ancestor-or-self::works)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_7() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-7 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"node before\" expression both operands are differents (return false). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/ancestor-or-self::works) << ($input-context1/works/employee[1])").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_8() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-8 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"node after\" expression (returns true). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]) >> ($input-context1/works/employee[1]/ancestor-or-self::works)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_9() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-9 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"node after\" expression with both operands the same (returns false). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/ancestor-or-self::works) >> ($input-context1/works/employee[1]/ancestor-or-self::works)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_10() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-10 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"node after\" expression with both operands the same (returns false). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/ancestor-or-self::works) >> ($input-context1/works/employee[1]/hours)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_11() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-11 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"union \" operation. Both operands are the same. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/*/day/ancestor-or-self::overtime) | ($input-context1/works/employee[12]/*/day/ancestor-or-self::overtime)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Fragment", new String[] { new StringBuilder().append("<overtime>\n     <day>Monday</day>\n     <day>Tuesday</day>\n   </overtime>").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_12() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-12 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"union\" operation. Both operands are different :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/*/day[1]/ancestor-or-self::overtime) | ($input-context1/works/employee[12]/*/day[2]/ancestor-or-self::overtime)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Fragment", new String[] { new StringBuilder().append("<overtime>\n     <day>Monday</day>\n     <day>Tuesday</day>\n   </overtime>").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_13() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-13 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"intersect\" operation. Both operands are the same. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/overtime/day/ancestor-or-self::employee) intersect ($input-context1/works/employee[12]/overtime/day/ancestor-or-self::employee)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Fragment", new String[] { new StringBuilder().append("<employee name=\"John Doe 12\" gender=\"male\">\n   <empnum>E4</empnum>\n   <pnum>P4</pnum>\n   <hours>40</hours>\n   <overtime>\n     <day>Monday</day>\n     <day>Tuesday</day>\n   </overtime>\n  </employee>").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_14() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-14 :)\n(: Description: Evaluation of the ancestor-self axis that is part of an \"except\" operation. Both operands are the same. :)\n(: Uses fn:count to avoid empty file :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:count(($input-context1/works/employee[12]/overtime/day[ancestor-or-self::overtime]) except ($input-context1/works/employee[12]/overtime/day[ancestor-or-self::overtime]))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_15() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-15 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of an \"except\" operation. Both operands are different. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/overtime/day[ancestor-or-self::overtime]) except ($input-context1/works/employee[12]/overtime/day[1])").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Fragment", new String[] { new StringBuilder().append("<day>Tuesday</day>").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_16() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-16 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of a boolean expression (\"and\" and fn:true(). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/overtime/day[ancestor-or-self::overtime]) and fn:true()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_17() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-17 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of a boolean expression (\"and\" and fn:false()). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/overtime/day[ancestor-or-self::overtime]) and fn:false()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_18() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-18 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of a boolean expression (\"or\" and fn:true()). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/overtime/day[ancestor-or-self::overtime]) or fn:true()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_19() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-19 :)\n(: Description: Evaluation of the ancestor-or-self axis that is part of a boolean expression (\"or\" and fn:false()). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/overtime/day[ancestor-or-self::overtime]) or fn:false()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_20() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-20 :)\n(: Description: Evaluation of the ancestor-or-self axis that used as part of the deep-equal-function. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:deep-equal($input-context1/works/employee[12]/overtime/ancestor-or-self::works,$input-context1/works/employee[12]/overtime/ancestor-or-self::works)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_ancestorself_21() throws Exception {
        String query = new StringBuilder().append("(: Name: ancestorself-21 :)\n(: Description: Evaluation of the ancestor-or-self axis used together with a newly constructed element. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nlet $var := <anElement>Some Content</anElement>\nreturn\n $var/ancestor-or-self::*").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Fragment", new String[] { new StringBuilder().append("<anElement>Some Content</anElement>").toString() }, testcase.result());
    }

    ;

    public void test_unabbreviatedSyntax_11() throws Exception {
        String query = new StringBuilder().append("(: Name: unabbreviatedSyntax-11 :)\n(: Description: Evaluate selecting an ancestor or self (ancestor-or-self::employee)- Select the \"employee\" ancestors of the context node and if the :)\n(: context is \"employee\" select it as well.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfor $h in ($input-context1/works/employee[1]/hours) \n return $h/ancestor-or-self::employee").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Fragment", new String[] { new StringBuilder().append("<employee name=\"Jane Doe 1\" gender=\"female\">\n   <empnum>E1</empnum>\n   <pnum>P1</pnum>\n   <hours>40</hours>\n  </employee>").toString() }, testcase.result());
    }

    ;
}
