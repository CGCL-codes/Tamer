package ch.ethz.mxquery.MinimalConformance.Expressions.Operators.CompExpr.ValComp.DurationDateTimeOp;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class gMonthEQclass extends XQueryTestBase {

    public void test_op_gMonth_equal2args_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal2args-1                            :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:07 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:gMonth-equal\" operator      :)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:gMonth(lower bound)                         :)\n(:$arg2 = xs:gMonth(lower bound)                         :)\n(:*******************************************************:)\n\nxs:gMonth(\"--01Z\") eq xs:gMonth(\"--01Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal2args_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal2args-2                            :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:07 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:gMonth-equal\" operator      :)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:gMonth(mid range)                           :)\n(:$arg2 = xs:gMonth(lower bound)                         :)\n(:*******************************************************:)\n\nxs:gMonth(\"--07Z\") eq xs:gMonth(\"--01Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal2args_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal2args-3                            :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:07 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:gMonth-equal\" operator      :)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:gMonth(upper bound)                         :)\n(:$arg2 = xs:gMonth(lower bound)                         :)\n(:*******************************************************:)\n\nxs:gMonth(\"--12Z\") eq xs:gMonth(\"--01Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal2args_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal2args-4                            :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:07 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:gMonth-equal\" operator      :)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:gMonth(lower bound)                         :)\n(:$arg2 = xs:gMonth(mid range)                           :)\n(:*******************************************************:)\n\nxs:gMonth(\"--01Z\") eq xs:gMonth(\"--07Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal2args_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal2args-5                            :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:07 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:gMonth-equal\" operator      :)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:gMonth(lower bound)                         :)\n(:$arg2 = xs:gMonth(upper bound)                         :)\n(:*******************************************************:)\n\nxs:gMonth(\"--01Z\") eq xs:gMonth(\"--12Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal2args_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal2args-6                            :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:07 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:gMonth-equal\" operator      :)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:gMonth(lower bound)                         :)\n(:$arg2 = xs:gMonth(lower bound)                         :)\n(:*******************************************************:)\n\nxs:gMonth(\"--01Z\") ne xs:gMonth(\"--01Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal2args_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal2args-7                            :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:07 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:gMonth-equal\" operator      :)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:gMonth(mid range)                           :)\n(:$arg2 = xs:gMonth(lower bound)                         :)\n(:*******************************************************:)\n\nxs:gMonth(\"--07Z\") ne xs:gMonth(\"--01Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal2args_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal2args-8                            :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:07 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:gMonth-equal\" operator      :)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:gMonth(upper bound)                         :)\n(:$arg2 = xs:gMonth(lower bound)                         :)\n(:*******************************************************:)\n\nxs:gMonth(\"--12Z\") ne xs:gMonth(\"--01Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal2args_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal2args-9                            :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:07 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:gMonth-equal\" operator      :)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:gMonth(lower bound)                         :)\n(:$arg2 = xs:gMonth(mid range)                           :)\n(:*******************************************************:)\n\nxs:gMonth(\"--01Z\") ne xs:gMonth(\"--07Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal2args_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal2args-10                           :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:07 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:gMonth-equal\" operator      :)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:gMonth(lower bound)                         :)\n(:$arg2 = xs:gMonth(upper bound)                         :)\n(:*******************************************************:)\n\nxs:gMonth(\"--01Z\") ne xs:gMonth(\"--12Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-2                                :)\n(:Date: June 16, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function         :)\n(:As per example 2 (for this function) of the F&O  specs :)\n(:*******************************************************:)\n\n(xs:gMonth(\"--12-05:00\") eq xs:gMonth(\"--12Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-3                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function that    :)\n(:return true and used together with fn:not (eq operator):)\n(:*******************************************************:)\n \nfn:not((xs:gMonth(\"--12Z\") eq xs:gMonth(\"--12Z\")))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-4                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 16, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function that    :)\n(:return true and used together with fn:not (ne operator):)\n(:*******************************************************:)\n \nfn:not(xs:gMonth(\"--05Z\") ne xs:gMonth(\"--06Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-5                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function that    :)\n(:return false and used together with fn:not (eq operator):)\n(:*******************************************************:)\n \nfn:not(xs:gMonth(\"--11Z\") eq xs:gMonth(\"--10Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-6                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 16, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function that    :)\n(:return false and used together with fn:not(ne operator):)\n(:*******************************************************:)\n \nfn:not(xs:gMonth(\"--05Z\") ne xs:gMonth(\"--05Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-7                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 16, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function used    :)\n(:together with \"and\" expression (eq operator).          :)\n(:*******************************************************:)\n \n(xs:gMonth(\"--04Z\") eq xs:gMonth(\"--02Z\")) and (xs:gMonth(\"--01Z\") eq xs:gMonth(\"--12Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-8                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 16, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function used    :)\n(:together with \"and\" expression (ne operator).          :)\n(:*******************************************************:)\n \n(xs:gMonth(\"--12Z\") ne xs:gMonth(\"--03Z\")) and (xs:gMonth(\"--05Z\") ne xs:gMonth(\"--08Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-9                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function used    :)\n(:together with \"or\" expression (eq operator).           :)\n(:*******************************************************:)\n \n(xs:gMonth(\"--02Z\") eq xs:gMonth(\"--02Z\")) or (xs:gMonth(\"--06Z\") eq xs:gMonth(\"--06Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-10                               :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function used    :)\n(:together with \"or\" expression (ne operator).           :)\n(:*******************************************************:)\n \n(xs:gMonth(\"--06Z\") ne xs:gMonth(\"--06Z\")) or (xs:gMonth(\"--08Z\") ne xs:gMonth(\"--09Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_11() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-11                               :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function used    :)\n(:together with \"fn:true\"/or expression (eq operator).   :)\n(:*******************************************************:)\n \n(xs:gMonth(\"--03Z\") eq xs:gMonth(\"--01Z\")) or (fn:true())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_12() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-12                               :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function used    :)\n(:together with \"fn:true\"/or expression (ne operator).   :)\n(:*******************************************************:)\n \n(xs:gMonth(\"--08Z\") ne xs:gMonth(\"--07Z\")) or (fn:true())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_13() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-13                               :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function used    :)\n(:together with \"fn:false\"/or expression (eq operator).  :)\n(:*******************************************************:)\n \n(xs:gMonth(\"--05Z\") eq xs:gMonth(\"--05Z\")) or (fn:false())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_gMonth_equal_14() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-gMonth-equal-14                               :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"gMonth-equal\" function used    :)\n(:together with \"fn:false\"/or expression (ne operator).  :)\n(:*******************************************************:)\n \n(xs:gMonth(\"--09Z\") ne xs:gMonth(\"--09Z\")) or (fn:false())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_K_gMonthEQ_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-gMonthEQ-1                                    :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Simple test of 'eq' for xs:gMonth, returning positive. :)\n(:*******************************************************:)\nxs:gMonth(\"--11  \") eq xs:gMonth(\"--11\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_gMonthEQ_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-gMonthEQ-2                                    :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Simple test of 'eq' for xs:gMonth.           :)\n(:*******************************************************:)\nnot(xs:gMonth(\"--11\") eq xs:gMonth(\"--01\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_gMonthEQ_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-gMonthEQ-3                                    :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Simple test of 'ne' for xs:gMonth.           :)\n(:*******************************************************:)\nxs:gMonth(\"--12\") ne xs:gMonth(\"--10\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_gMonthEQ_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-gMonthEQ-4                                    :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Simple test of 'ne' for xs:gMonth.           :)\n(:*******************************************************:)\nnot(xs:gMonth(\"--03\") ne xs:gMonth(\"--03\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_gMonthEQ_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-gMonthEQ-5                                    :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Test that zone offset -00:00 is equal to Z, in xs:gMonth. :)\n(:*******************************************************:)\nxs:gMonth(\"--01-00:00\") eq xs:gMonth(\"--01Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_gMonthEQ_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-gMonthEQ-6                                    :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Test that zone offset +00:00 is equal to Z, in xs:gMonth. :)\n(:*******************************************************:)\nxs:gMonth(\"--01+00:00\") eq xs:gMonth(\"--01Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_gMonthEQ_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-gMonthEQ-7                                    :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Test that zone offset Z is equal to Z, in xs:gMonth. :)\n(:*******************************************************:)\nxs:gMonth(\"--01Z\") eq xs:gMonth(\"--01Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_gMonthEQ_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-gMonthEQ-8                                    :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:37+02:00                       :)\n(: Purpose: Test that zone offset -00:00 is equal to +00:00, in xs:gMonth. :)\n(:*******************************************************:)\nxs:gMonth(\"--01-00:00\") eq xs:gMonth(\"--01+00:00\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;
}
