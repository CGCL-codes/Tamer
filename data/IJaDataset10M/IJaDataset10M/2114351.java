package ch.ethz.mxquery.Optional.FullAxis;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class followingAxisclass extends XQueryTestBase {

    public void test_following_1() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: following-1 :)\n(: Description: Evaluation of the following axis for which the context node is not a node. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(200)/following::*").toString();
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

    public void test_following_2() throws Exception {
        String query = new StringBuilder().append("(: Name: following-2 :)\n(: Description: Evaluation of the following axis for which the given node does not exists. :)\n(: Uses fn:count to avoid empty file. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:count($input-context1/works/employee[1]/following::noSuchNode)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_following_3() throws Exception {
        String query = new StringBuilder().append("(: Name: following-3 :)\n(: Description: Evaluation of the following axis that is part of an \"is\" expression (return true). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/following::employee) is ($input-context1/works/employee[13])").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_following_4() throws Exception {
        String query = new StringBuilder().append("(: Name: following-4 :)\n(: Description: Evaluation of the following axis that is part of an \"is\" expression (return false). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]/following::employee) is ($input-context1/works[1]/employee[12])").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_following_5() throws Exception {
        String query = new StringBuilder().append("(: Name: following-5 :)\n(: Description: Evaluation of the following axis that is part of an \"node before\" expression (return true). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[11]/following::employee[1]) << ($input-context1/works[1]/employee[13])").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_following_6() throws Exception {
        String query = new StringBuilder().append("(: Name: following-6 :)\n(: Description: Evaluation of the following axis that is part of an \"node before\" expression and both operands are the same (return false). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]/following::employee) << ($input-context1/works[1]/employee[12]/following::employee)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_following_7() throws Exception {
        String query = new StringBuilder().append("(: Name: following-7 :)\n(: Description: Evaluation of the following axis that is part of an \"node before\" expression both operands are differents (return false). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]/following::employee) << ($input-context1/works[1]/employee[12]/overtime[1])").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_following_8() throws Exception {
        String query = new StringBuilder().append("(: Name: following-8 :)\n(: Description: Evaluation of the following axis that is part of an \"node after\" expression (returns true). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[13]) >> ($input-context1/works[1]/employee[12]/overtime[1]/day[1]/following::day)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_following_9() throws Exception {
        String query = new StringBuilder().append("(: Name: following-9 :)\n(: Description: Evaluation of the following axis that is part of an \"node after\" expression with both operands the same (returns false). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]/following::employee) >> ($input-context1/works[1]/employee[12]/following::employee)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_following_10() throws Exception {
        String query = new StringBuilder().append("(: Name: following-10 :)\n(: Description: Evaluation of the following axis that is part of an \"node after\" expression with different operands (returns false). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]) >> ($input-context1/works[1]/employee[12]/following::employee)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_following_11() throws Exception {
        String query = new StringBuilder().append("(: Name: following-11 :)\n(: Description: Evaluation of the following axis that is part of an \"union \" operation. Both operands are the same. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/*/day[1]/following::day) | ($input-context1/works/employee[12]/*/day[1]/following::day)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Fragment", new String[] { new StringBuilder().append("<day>Tuesday</day>").toString() }, testcase.result());
    }

    ;

    public void test_following_12() throws Exception {
        String query = new StringBuilder().append("(: Name: following-12 :)\n(: Description: Evaluation of the following axis that is part of an \"union\" operation. Both operands are different :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[12]/*/day[1]/following::day) | ($input-context1/works/employee[12]/*/day[1])").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Fragment", new String[] { new StringBuilder().append("<day>Monday</day><day>Tuesday</day>").toString() }, testcase.result());
    }

    ;

    public void test_following_13() throws Exception {
        String query = new StringBuilder().append("(: Name: following-13 :)\n(: Description: Evaluation of the following axis that is part of an \"intersect\" operation. Both operands are the same. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]/overtime[1]/day[1]/following::day) intersect ($input-context1/works[1]/employee[12]/overtime[1]/day[1]/following::day)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Fragment", new String[] { new StringBuilder().append("<day>Tuesday</day>").toString() }, testcase.result());
    }

    ;

    public void test_following_14() throws Exception {
        String query = new StringBuilder().append("(: Name: following-14 :)\n(: Description: Evaluation of the following axis that is part of an \"except\" operation. Both operands are the same. :)\n(: Uses fn:count to avoid empty file :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:count(($input-context1/works[1]/employee[12]/following::employee) except ($input-context1/works[1]/employee[12]/following::employee))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_following_15() throws Exception {
        String query = new StringBuilder().append("(: Name: following-15 :)\n(: Description: Evaluation of the following axis that is part of an \"except\" operation. Both operands are different. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]/overtime/day) except ($input-context1/works[1]/employee[12]/overtime/day[1]/following::day)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Fragment", new String[] { new StringBuilder().append("<day>Monday</day>").toString() }, testcase.result());
    }

    ;

    public void test_following_16() throws Exception {
        String query = new StringBuilder().append("(: Name: following-16 :)\n(: Description: Evaluation of the following axis that is part of a boolean expression (\"and\" and fn:true(). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]/following::employee) and fn:true()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_following_17() throws Exception {
        String query = new StringBuilder().append("(: Name: following-17 :)\n(: Description: Evaluation of the following axis that is part of a boolean expression (\"and\" and fn:false()). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]/following::employee) and fn:false()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_following_18() throws Exception {
        String query = new StringBuilder().append("(: Name: following-18 :)\n(: Description: Evaluation of the following axis that is part of a boolean expression (\"or\" and fn:true()). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]/following::employee) or fn:true()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_following_19() throws Exception {
        String query = new StringBuilder().append("(: Name: following-19 :)\n(: Description: Evaluation of the following axis that is part of a boolean expression (\"or\" and fn:false()). :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works[1]/employee[12]/following::employee) or fn:false()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_following_20() throws Exception {
        String query = new StringBuilder().append("(: Name: following-20 :)\n(: Description: Evaluation of the following axis that used as part of the deep-equal-function. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:deep-equal($input-context1/works[1]/employee[12]/following::employee,$input-context1/works[1]/employee[12]/following::employee)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works-mod", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_following_21() throws Exception {
        String query = new StringBuilder().append("(: Name: following-21 :)\n(: Description: Evaluation of the following axis used together with a newly constructed element. :)\n(: Uses fn:count to avoid empty file. :)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nlet $var := <anElement>Some Content</anElement>\nreturn\n fn:count($var/following::*)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;
}
