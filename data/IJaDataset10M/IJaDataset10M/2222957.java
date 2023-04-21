package ch.ethz.mxquery.MinimalConformance.Expressions.Operators.CompExpr.GenComprsn;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class GenCompEqclass extends XQueryTestBase {

    public void test_generalexpression1() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Empty sequence:)\n(:  operator = =:)\n(:  operand2 = Empty sequence:)\n(:*******************************************************:)\n\n() = ()\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression2() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Empty sequence:)\n(:  operator = =:)\n(:  operand2 = Atomic Value:)\n(:*******************************************************:)\n\n() = 10000\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression3() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Empty sequence:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic value:)\n(:*******************************************************:)\n\n() = (50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression4() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Empty sequence:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic values:)\n(:*******************************************************:)\n\n() = (10000,50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression5() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Empty sequence:)\n(:  operator = =:)\n(:  operand2 = Element Constructor:)\n(:*******************************************************:)\n\n() = <a>10000</a>\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression6() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Empty sequence:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element constructor:)\n(:*******************************************************:)\n\n() = (<a>10000</a>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression7() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Empty sequence:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element constructors:)\n(:*******************************************************:)\n\n() = (<a>10000</a>,<b>50000</b>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression8() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Empty sequence:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element nodes:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n() = ($input-context1/works/employee[1]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression9() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Empty sequence:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (single source):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n() = ($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression10() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Empty sequence:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (multiple sources):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n() = ($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/grade[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression11() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Atomic Value:)\n(:  operator = =:)\n(:  operand2 = Empty sequence:)\n(:*******************************************************:)\n\n10000 = ()\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression12() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                     :)\n(:Purpose: Test of a General Expression                  :)\n(:with the operands set as follows                       :)\n(:  operand1 = Atomic Value                              :)\n(:  operator = \"=\"                                       :)\n(:  operand2 = empty sequence                            :)\n(:*******************************************************:)\n\n10000 = ()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression13() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Atomic Value:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic values:)\n(:*******************************************************:)\n\n10000 = (10000,50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression14() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Atomic Value:)\n(:  operator = =:)\n(:  operand2 = Element Constructor:)\n(:*******************************************************:)\n\n10000 = <a>10000</a>\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression15() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Atomic Value:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element constructor:)\n(:*******************************************************:)\n\n10000 = (<a>10000</a>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression16() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Atomic Value:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element constructors:)\n(:*******************************************************:)\n\n10000 = (<a>10000</a>,<b>50000</b>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression17() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Atomic Value:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element nodes:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n10000 = ($input-context1/works/employee[1]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression18() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Atomic Value:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (single source):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n10000 = ($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression19() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Atomic Value:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (multiple sources):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n10000 = ($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/grade[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression20() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic value:)\n(:  operator = =:)\n(:  operand2 = Empty sequence:)\n(:*******************************************************:)\n\n(50000) = ()\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression21() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic value:)\n(:  operator = =:)\n(:  operand2 = Atomic Value:)\n(:*******************************************************:)\n\n(50000) = 10000\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression22() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic value:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic value:)\n(:*******************************************************:)\n\n(50000) = (50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression23() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic value:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic values:)\n(:*******************************************************:)\n\n(50000) = (10000,50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression24() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic value:)\n(:  operator = =:)\n(:  operand2 = Element Constructor:)\n(:*******************************************************:)\n\n(50000) = <a>10000</a>\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression25() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic value:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element constructor:)\n(:*******************************************************:)\n\n(50000) = (<a>10000</a>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression26() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic value:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element constructors:)\n(:*******************************************************:)\n\n(50000) = (<a>10000</a>,<b>50000</b>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression27() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic value:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element nodes:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(50000) = ($input-context1/works/employee[1]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression28() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic value:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (single source):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(50000) = ($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression29() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic value:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (multiple sources):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n(50000) = ($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/grade[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression30() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic values:)\n(:  operator = =:)\n(:  operand2 = Empty sequence:)\n(:*******************************************************:)\n\n(10000,50000) = ()\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression31() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic values:)\n(:  operator = =:)\n(:  operand2 = Atomic Value:)\n(:*******************************************************:)\n\n(10000,50000) = 10000\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression32() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic values:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic value:)\n(:*******************************************************:)\n\n(10000,50000) = (50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression33() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic values:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic values:)\n(:*******************************************************:)\n\n(10000,50000) = (10000,50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression34() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic values:)\n(:  operator = =:)\n(:  operand2 = Element Constructor:)\n(:*******************************************************:)\n\n(10000,50000) = <a>10000</a>\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression35() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic values:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element constructor:)\n(:*******************************************************:)\n\n(10000,50000) = (<a>10000</a>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression36() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic values:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element constructors:)\n(:*******************************************************:)\n\n(10000,50000) = (<a>10000</a>,<b>50000</b>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression37() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic values:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element nodes:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(10000,50000) = ($input-context1/works/employee[1]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression38() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic values:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (single source):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(10000,50000) = ($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression39() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single atomic values:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (multiple sources):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n(10000,50000) = ($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/grade[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression40() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Element Constructor:)\n(:  operator = =:)\n(:  operand2 = Empty sequence:)\n(:*******************************************************:)\n\n<a>10000</a> = ()\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression41() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Element Constructor:)\n(:  operator = =:)\n(:  operand2 = Atomic Value:)\n(:*******************************************************:)\n\n<a>10000</a> = 10000\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression42() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Element Constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic value:)\n(:*******************************************************:)\n\n<a>10000</a> = (50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression43() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Element Constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic values:)\n(:*******************************************************:)\n\n<a>10000</a> = (10000,50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression44() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Element Constructor:)\n(:  operator = =:)\n(:  operand2 = Element Constructor:)\n(:*******************************************************:)\n\n<a>10000</a> = <a>10000</a>\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression45() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Element Constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element constructor:)\n(:*******************************************************:)\n\n<a>10000</a> = (<a>10000</a>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression46() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Element Constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element constructors:)\n(:*******************************************************:)\n\n<a>10000</a> = (<a>10000</a>,<b>50000</b>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression47() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Element Constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element nodes:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n<a>10000</a> = ($input-context1/works/employee[1]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression48() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Element Constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (single source):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n<a>10000</a> = ($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression49() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Element Constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (multiple sources):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n<a>10000</a> = ($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/grade[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression50() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element constructor:)\n(:  operator = =:)\n(:  operand2 = Empty sequence:)\n(:*******************************************************:)\n\n(<a>10000</a>) = ()\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression51() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element constructor:)\n(:  operator = =:)\n(:  operand2 = Atomic Value:)\n(:*******************************************************:)\n\n(<a>10000</a>) = 10000\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression52() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic value:)\n(:*******************************************************:)\n\n(<a>10000</a>) = (50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression53() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic values:)\n(:*******************************************************:)\n\n(<a>10000</a>) = (10000,50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression54() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element constructor:)\n(:  operator = =:)\n(:  operand2 = Element Constructor:)\n(:*******************************************************:)\n\n(<a>10000</a>) = <a>10000</a>\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression55() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element constructor:)\n(:*******************************************************:)\n\n(<a>10000</a>) = (<a>10000</a>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression56() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element constructors:)\n(:*******************************************************:)\n\n(<a>10000</a>) = (<a>10000</a>,<b>50000</b>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression57() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element nodes:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(<a>10000</a>) = ($input-context1/works/employee[1]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression58() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (single source):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(<a>10000</a>) = ($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression59() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element constructor:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (multiple sources):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n(<a>10000</a>) = ($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/grade[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression60() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element constructors:)\n(:  operator = =:)\n(:  operand2 = Empty sequence:)\n(:*******************************************************:)\n\n(<a>10000</a>,<b>50000</b>) = ()\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression61() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element constructors:)\n(:  operator = =:)\n(:  operand2 = Atomic Value:)\n(:*******************************************************:)\n\n(<a>10000</a>,<b>50000</b>) = 10000\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression62() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element constructors:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic value:)\n(:*******************************************************:)\n\n(<a>10000</a>,<b>50000</b>) = (50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression63() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element constructors:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic values:)\n(:*******************************************************:)\n\n(<a>10000</a>,<b>50000</b>) = (10000,50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression64() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element constructors:)\n(:  operator = =:)\n(:  operand2 = Element Constructor:)\n(:*******************************************************:)\n\n(<a>10000</a>,<b>50000</b>) = <a>10000</a>\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression65() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element constructors:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element constructor:)\n(:*******************************************************:)\n\n(<a>10000</a>,<b>50000</b>) = (<a>10000</a>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression66() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element constructors:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element constructors:)\n(:*******************************************************:)\n\n(<a>10000</a>,<b>50000</b>) = (<a>10000</a>,<b>50000</b>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression67() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element constructors:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element nodes:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(<a>10000</a>,<b>50000</b>) = ($input-context1/works/employee[1]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression68() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element constructors:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (single source):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(<a>10000</a>,<b>50000</b>) = ($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression69() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element constructors:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (multiple sources):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n(<a>10000</a>,<b>50000</b>) = ($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/grade[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression70() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element nodes:)\n(:  operator = =:)\n(:  operand2 = Empty sequence:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1]) = ()\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression71() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element nodes:)\n(:  operator = =:)\n(:  operand2 = Atomic Value:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1]) = 10000\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression72() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element nodes:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic value:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1]) = (50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression73() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element nodes:)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic values:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1]) = (10000,50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression74() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element nodes:)\n(:  operator = =:)\n(:  operand2 = Element Constructor:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1]) = <a>10000</a>\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression75() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element nodes:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element constructor:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1]) = (<a>10000</a>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression76() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element nodes:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element constructors:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1]) = (<a>10000</a>,<b>50000</b>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression77() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element nodes:)\n(:  operator = =:)\n(:  operand2 = Sequence of single element nodes:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1]) = ($input-context1/works/employee[1]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression78() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element nodes:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (single source):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1]) = ($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression79() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of single element nodes:)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (multiple sources):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1]) = ($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/grade[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression80() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (single source):)\n(:  operator = =:)\n(:  operand2 = Empty sequence:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1]) = ()\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression81() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (single source):)\n(:  operator = =:)\n(:  operand2 = Atomic Value:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1]) = 10000\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression82() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (single source):)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic value:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1]) = (50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression83() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (single source):)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic values:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1]) = (10000,50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression84() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (single source):)\n(:  operator = =:)\n(:  operand2 = Element Constructor:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1]) = <a>10000</a>\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression85() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (single source):)\n(:  operator = =:)\n(:  operand2 = Sequence of single element constructor:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1]) = (<a>10000</a>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression86() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (single source):)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element constructors:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1]) = (<a>10000</a>,<b>50000</b>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression87() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (single source):)\n(:  operator = =:)\n(:  operand2 = Sequence of single element nodes:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1]) = ($input-context1/works/employee[1]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression88() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (single source):)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (single source):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1]) = ($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression89() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (single source):)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (multiple sources):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1]) = ($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/grade[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression90() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (multiple sources):)\n(:  operator = =:)\n(:  operand2 = Empty sequence:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/hours[1]) = ()\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression91() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (multiple sources):)\n(:  operator = =:)\n(:  operand2 = Atomic Value:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/hours[1]) = 10000\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression92() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (multiple sources):)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic value:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/hours[1]) = (50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression93() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (multiple sources):)\n(:  operator = =:)\n(:  operand2 = Sequence of single atomic values:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/hours[1]) = (10000,50000)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression94() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (multiple sources):)\n(:  operator = =:)\n(:  operand2 = Element Constructor:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/hours[1]) = <a>10000</a>\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression95() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (multiple sources):)\n(:  operator = =:)\n(:  operand2 = Sequence of single element constructor:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/hours[1]) = (<a>10000</a>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression96() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (multiple sources):)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element constructors:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/hours[1]) = (<a>10000</a>,<b>50000</b>)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression97() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (multiple sources):)\n(:  operator = =:)\n(:  operand2 = Sequence of single element nodes:)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/hours[1]) = ($input-context1/works/employee[1]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression98() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (multiple sources):)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (single source):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/hours[1]) = ($input-context1/works/employee[1]/hours[1],$input-context1/works/employee[6]/hours[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_generalexpression99() throws Exception {
        String query = new StringBuilder().append("\n(:*******************************************************:)\n(:Written By: Carmelo Montanez (Automatic Generation)    :)\n(:Date: June 2, 2005                                :)\n(:Purpose: Test of a General Expression      :)\n(:with the operands set as follows          :)\n(:  operand1 = Sequence of multiple element nodes (multiple sources):)\n(:  operator = =:)\n(:  operand2 = Sequence of multiple element nodes (multiple sources):)\n(:*******************************************************:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\ndeclare variable $input-context2 external;\n(: insert-end :)\n\n($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/hours[1]) = ($input-context1/works/employee[1]/hours[1],$input-context2/staff/employee[6]/grade[1])\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "works", true);
        testcase.addVariable("input-context2", "staff", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-1                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparisons involving the empty sequence. :)\n(:*******************************************************:)\nnot(() = ())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-2                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparisons involving the empty sequence. :)\n(:*******************************************************:)\nnot((() = ()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-3                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparisons involving the empty sequence. :)\n(:*******************************************************:)\nnot((() = 1))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-4                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparisons involving the empty sequence. :)\n(:*******************************************************:)\nnot(1 = ())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_5() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-5                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: A syntactically invalid expression that reminds of a general comparison operator. :)\n(:*******************************************************:)\n1 == 1").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPST0003" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-6                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Example from the XPath 2.0 specification.    :)\n(:*******************************************************:)\n(1, 2) = (2, 3)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-7                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Example from the XPath 2.0 specification.    :)\n(:*******************************************************:)\n(2, 3) = (3, 4)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-8                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Example from the XPath 2.0 specification.    :)\n(:*******************************************************:)\nnot((1, 2) = (3, 4))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-9                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Example from the XPath 2.0 specification.    :)\n(:*******************************************************:)\n(xs:untypedAtomic(\"1\"), xs:untypedAtomic(\"2\")) =\n			       (xs:untypedAtomic(\"2.0\"), 2.0)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-10                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Example from the XPath 2.0 specification.    :)\n(:*******************************************************:)\nnot((xs:untypedAtomic(\"1\"), xs:untypedAtomic(\"2\")) =\n				   (xs:untypedAtomic(\"2.0\"), 3.0))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_11() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-11                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison causing numeric promotion from xs:untypedAtomic. :)\n(:*******************************************************:)\nxs:untypedAtomic(\"1\") = 1").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_12() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-12                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison causing numeric promotion from xs:untypedAtomic. :)\n(:*******************************************************:)\nnot(xs:untypedAtomic(\"2\") = 1)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_13() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-13                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison causing numeric promotion from xs:untypedAtomic. :)\n(:*******************************************************:)\nxs:untypedAtomic(\"1\") = 1").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_14() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-14                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison causing numeric promotion from xs:untypedAtomic. :)\n(:*******************************************************:)\n1 = xs:untypedAtomic(\"1\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_15() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-15                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison causing numeric promotion from xs:untypedAtomic. :)\n(:*******************************************************:)\n1 = xs:untypedAtomic(\"1\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_16() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-16                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: A general comparison involving the error() function. :)\n(:*******************************************************:)\nerror() = 3").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_17() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-17                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: A general comparison involving the error() function. :)\n(:*******************************************************:)\n(error(), 3) = 3").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_18() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-18                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: A general comparison involving the error() function. :)\n(:*******************************************************:)\n3 = error()").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_19() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-19                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: A general comparison involving the error() function. :)\n(:*******************************************************:)\n3 = (error(), 3)").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_20() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-20                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: An expression involving the '=' operator that trigger certain optimization paths in some implementations. :)\n(:*******************************************************:)\ncount(remove(remove((current-time(), 1), 1), 1)) = 0").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_21() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-21                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Test that fn:count combined with expressions that might disable compile time evaluations(optimization) as well as the '=' operator, is conformant. :)\n(:*******************************************************:)\nnot(0 = count((1, 2, timezone-from-time(current-time()))))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_22() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-22                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Test that fn:count combined with expressions that might disable compile time evaluations(optimization) as well as the '=' operator, is conformant. :)\n(:*******************************************************:)\n0 != count((1, 2, timezone-from-time(current-time())))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_23() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-23                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Comparison where type of operands are incompatible. :)\n(:*******************************************************:)\n\n		(xs:anyURI(\"example.com/\"), 1, QName(\"example.com\", \"ncname\"), false(), xs:hexBinary(\"FF\"))\n		=\n		(xs:anyURI(\"example.com/NOT\"), 0, QName(\"example.com\", \"p:ncname\"), true(), xs:hexBinary(\"EF\"))").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_24() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-24                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison involving xs:untypedAtomic/xs:string. :)\n(:*******************************************************:)\n\"a string\" = \"a string\"").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_25() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-25                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison involving xs:untypedAtomic/xs:string. :)\n(:*******************************************************:)\nxs:untypedAtomic(\"a string\") = \"a string\"").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_26() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-26                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison involving xs:untypedAtomic/xs:string. :)\n(:*******************************************************:)\n\"a string\" = xs:untypedAtomic(\"a string\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_27() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-27                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison involving xs:untypedAtomic/xs:string. :)\n(:*******************************************************:)\nnot(xs:untypedAtomic(\"a string\") = \"a stringDIFF\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_28() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-28                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison involving xs:untypedAtomic/xs:string. :)\n(:*******************************************************:)\nnot(\"a string\" = xs:untypedAtomic(\"a stringDIFF\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_29() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-29                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison involving xs:untypedAtomic/xs:string. :)\n(:*******************************************************:)\nnot(\"a string\" = \"a stringDIFF\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_30() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-30                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison with remove() as operand, resulting in incompatible operand types. :)\n(:*******************************************************:)\nremove((6, \"a string\"), 1) = 6").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_31() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-31                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison with remove() as operand, resulting in incompatible operand types. :)\n(:*******************************************************:)\n6 = remove((\"a string\", 6), 2)").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_32() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-32                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison involving remove(), resulting in operands that require conversion to numeric from xs:untypedAtomic. :)\n(:*******************************************************:)\nremove((6, \"a string\"), 2) = xs:untypedAtomic(\"6\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_33() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-33                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison involving remove(), resulting in operands that require conversion to numeric from xs:untypedAtomic. :)\n(:*******************************************************:)\nxs:untypedAtomic(\"6\") = remove((\"a string\", 6), 1)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_34() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-34                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison involving remove(), resulting in operands that require conversion to numeric from xs:untypedAtomic. Implementations supporting the static typing feature may raise XPTY0004. :)\n(:*******************************************************:)\n(remove((xs:untypedAtomic(\"6\"), \"a string\"), 2)) = 6").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_35() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-35                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison involving remove(), resulting in operands that require conversion to numeric from xs:untypedAtomic. Implementations supporting the static typing feature may raise XPTY0004. :)\n(:*******************************************************:)\n6 = (remove((\"a string\", xs:untypedAtomic(\"6\")), 1))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_36() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-36                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison where the operands are various various sequence of xs:integers. :)\n(:*******************************************************:)\n1 = 1").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_37() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-37                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison where the operands are various various sequence of xs:integers. :)\n(:*******************************************************:)\n(1, 2, 3) = 1").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_38() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-38                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison where the operands are various various sequence of xs:integers. :)\n(:*******************************************************:)\n(1, 2, 3) = 2").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_39() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-39                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison where the operands are various various sequence of xs:integers. :)\n(:*******************************************************:)\n(1, 2, 3) = 3").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_40() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-40                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison where the operands are various various sequence of xs:integers. :)\n(:*******************************************************:)\n2 = (1, 2, 3)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_41() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-41                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison where the operands are various various sequence of xs:integers. :)\n(:*******************************************************:)\n1 = (1, 2, 3)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_42() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-42                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison where the operands are various various sequence of xs:integers. :)\n(:*******************************************************:)\n3 = (1, 2, 3)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_43() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-43                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison causing a xs:untypedAtomic value to be cast to xs:boolean, and then compared. :)\n(:*******************************************************:)\nxs:untypedAtomic(\"false\") = false()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_44() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-44                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison causing a xs:untypedAtomic value to be cast to xs:boolean, and then compared. :)\n(:*******************************************************:)\nfalse() = xs:untypedAtomic(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_45() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-45                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison causing a xs:untypedAtomic value to be cast to xs:boolean, and then compared. :)\n(:*******************************************************:)\nnot(xs:untypedAtomic(\"true\") = false())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_46() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-46                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison causing a xs:untypedAtomic value to be cast to xs:boolean, and then compared. :)\n(:*******************************************************:)\n(true() = xs:untypedAtomic(\"true\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_GenCompEq_47() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-47                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\n\"1\" = 1").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_48() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-48                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\n1 = \"1\"").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_49() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-49                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\nfalse() = 5").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_50() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-50                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\n5 = false()").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_51() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-51                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\nxs:untypedAtomic(\"three\") = 3").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FORG0001" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_52() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-52                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\nxs:string(\"false\") = false()").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_53() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-53                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\nfalse() = xs:string(\"false\")").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_54() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-54                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\nxs:untypedAtomic(\"falseERR\") = false()").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FORG0001" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_55() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-55                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\n\n		(xs:untypedAtomic(\"1\"), xs:anyURI(\"example.com\")) =\n		(xs:untypedAtomic(\"2.0\"), 3.0)").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_56() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-56                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\nfalse() = xs:untypedAtomic(\"falseERR\")").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FORG0001" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_57() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-57                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\n3 = xs:untypedAtomic(\"three\")").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FORG0001" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_58() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-58                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\nxs:anyURI(\"example.com/\") = false()").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_GenCompEq_59() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-GenCompEq-59                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: General comparison which fails due to invalid operator combination or casting. :)\n(:*******************************************************:)\nfalse() = xs:anyURI(\"example.com/\")").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;
}
