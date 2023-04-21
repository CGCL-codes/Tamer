package ch.ethz.mxquery.MinimalConformance.Expressions.Operators.CompExpr.ValComp.BooleanOp;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class BooleanGTclass extends XQueryTestBase {

    public void test_op_boolean_greater_than2args_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than2args-1                    :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Thu Dec 16 10:48:17 GMT-05:00 2004                :)\n(:Purpose: Evaluates The \"op:boolean-greater-than\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:boolean(lower bound)                        :)\n(:$arg2 = xs:boolean(lower bound)                        :)\n(:*******************************************************:)\n\nxs:boolean(\"false\") gt xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than2args_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than2args-2                    :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Thu Dec 16 10:48:17 GMT-05:00 2004                :)\n(:Purpose: Evaluates The \"op:boolean-greater-than\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:boolean(mid range)                          :)\n(:$arg2 = xs:boolean(lower bound)                        :)\n(:*******************************************************:)\n\nxs:boolean(\"1\") gt xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than2args_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than2args-3                    :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Thu Dec 16 10:48:17 GMT-05:00 2004                :)\n(:Purpose: Evaluates The \"op:boolean-greater-than\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:boolean(upper bound)                        :)\n(:$arg2 = xs:boolean(lower bound)                        :)\n(:*******************************************************:)\n\nxs:boolean(\"0\") gt xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than2args_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than2args-4                    :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Thu Dec 16 10:48:17 GMT-05:00 2004                :)\n(:Purpose: Evaluates The \"op:boolean-greater-than\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:boolean(lower bound)                        :)\n(:$arg2 = xs:boolean(mid range)                          :)\n(:*******************************************************:)\n\nxs:boolean(\"false\") gt xs:boolean(\"1\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than2args_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than2args-5                    :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Thu Dec 16 10:48:17 GMT-05:00 2004                :)\n(:Purpose: Evaluates The \"op:boolean-greater-than\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:boolean(lower bound)                        :)\n(:$arg2 = xs:boolean(upper bound)                        :)\n(:*******************************************************:)\n\nxs:boolean(\"false\") gt xs:boolean(\"0\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than2args_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than2args-6                    :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Thu Dec 16 10:48:17 GMT-05:00 2004                :)\n(:Purpose: Evaluates The \"op:boolean-greater-than\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:boolean(lower bound)                        :)\n(:$arg2 = xs:boolean(lower bound)                        :)\n(:*******************************************************:)\n\nxs:boolean(\"false\") le xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than2args_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than2args-7                    :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Thu Dec 16 10:48:17 GMT-05:00 2004                :)\n(:Purpose: Evaluates The \"op:boolean-greater-than\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:boolean(mid range)                          :)\n(:$arg2 = xs:boolean(lower bound)                        :)\n(:*******************************************************:)\n\nxs:boolean(\"1\") le xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than2args_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than2args-8                    :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Thu Dec 16 10:48:17 GMT-05:00 2004                :)\n(:Purpose: Evaluates The \"op:boolean-greater-than\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:boolean(upper bound)                        :)\n(:$arg2 = xs:boolean(lower bound)                        :)\n(:*******************************************************:)\n\nxs:boolean(\"0\") le xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than2args_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than2args-9                    :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Thu Dec 16 10:48:17 GMT-05:00 2004                :)\n(:Purpose: Evaluates The \"op:boolean-greater-than\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:boolean(lower bound)                        :)\n(:$arg2 = xs:boolean(mid range)                          :)\n(:*******************************************************:)\n\nxs:boolean(\"false\") le xs:boolean(\"1\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than2args_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than2args-10                   :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Thu Dec 16 10:48:17 GMT-05:00 2004                :)\n(:Purpose: Evaluates The \"op:boolean-greater-than\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:boolean(lower bound)                        :)\n(:$arg2 = xs:boolean(upper bound)                        :)\n(:*******************************************************:)\n\nxs:boolean(\"false\") le xs:boolean(\"0\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-1                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"not(true)\", \"true\" respectively.:)\n(: Use of gt operator.                                   :)\n(:*******************************************************:)\n \nfn:not(xs:boolean(\"true\")) gt xs:boolean(\"true\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-2                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"not(true)\", \"true\" respectively.:)\n(: Use of le operator.                                   :)\n(:*******************************************************:)\n \nfn:not(xs:boolean(\"true\")) le xs:boolean(\"true\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-3                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"not(true)\", \"false\" respectively.:)\n(: Use of gt operator.                                   :)\n(:*******************************************************:)\n \nfn:not(xs:boolean(\"true\")) gt xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-4                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"not(true)\", \"false\" respectively.:)\n(: Use of le operator.                                   :)\n(:*******************************************************:)\n \nfn:not(xs:boolean(\"true\")) le xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-5                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"not(false)\", \"true\" respectively.:)\n(: Use of gt operator.                                   :)\n(:*******************************************************:)\n \nfn:not(xs:boolean(\"false\")) gt xs:boolean(\"true\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-6                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"not(false)\", \"true\" respectively.:)\n(: Use of le operator.                                   :)\n(:*******************************************************:)\n \nfn:not(xs:boolean(\"false\")) le xs:boolean(\"true\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-7                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"not(false)\", \"false\" respectively.:)\n(: Use of gt operator.                                   :)\n(:*******************************************************:)\n \nfn:not(xs:boolean(\"false\")) gt xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-8                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"not(false)\", \"false\" respectively.:)\n(: Use of le operator.                                   :)\n(:*******************************************************:)\n \nfn:not(xs:boolean(\"false\")) le xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-9                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"(7 eq 7)\", \"true\" respectively.:)\n(: Use of gt operator.                                   :)\n(:*******************************************************:)\n \n(7 eq 7) gt xs:boolean(\"true\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-10                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"(7 eq 7)\", \"true\" respectively.:)\n(: Use of le operator.                                   :)\n(:*******************************************************:)\n \n(7 eq 7) le xs:boolean(\"true\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_11() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-11                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"(7 eq 7)\", \"false\" respectively.:)\n(: Use of gt operator.                                   :)\n(:*******************************************************:)\n \n(7 eq 7) gt xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_12() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-boolean-greater-than-12                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 15, 2005                                    :)\n(:Purpose: Evaluates The \"boolean-greater-than\" function :)\n(: with operands set to \"(7 eq 7)\", \"false\" respectively.:)\n(: Use of le operator.                                   :)\n(:*******************************************************:)\n \n(7 eq 7) le xs:boolean(\"false\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_001() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-001.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is greater than arg2:Boolean :)\n(:*****************************************************************:)\n\nxs:boolean(\"true\") gt xs:boolean(\"true\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_002() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-002.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is greater than arg2:Boolean :)\n(:*****************************************************************:)\n\nxs:boolean(\"1\") gt xs:boolean(\"true\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_003() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-003.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is greater than arg2:Boolean :)\n(:*****************************************************************:)\n\nxs:boolean(\"0\") gt xs:boolean(\"true\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_004() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-004.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is greater than arg2:Boolean :)\n(:*****************************************************************:)\n\nxs:boolean(\"true\") gt xs:boolean(\"1\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_005() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-005.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is greater than arg2:Boolean :)\n(:*****************************************************************:)\n\nxs:boolean(\"true\") gt xs:boolean(\"0\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_006() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-006.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is greater than arg2:Boolean :)\n(:*****************************************************************:)\n\nxs:boolean(\"true\") gt xs:boolean(\"false\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_007() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-007.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is greater than arg2:Boolean :)\n(:*****************************************************************:)\n\nxs:boolean(\"false\") gt xs:boolean(\"true\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_008() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-008.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is less or equal to arg2:Boolean :)\n(:*********************************************************************:)\n\nxs:boolean(\"true\") le xs:boolean(\"true\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_009() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-009.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is less or equal to arg2:Boolean :)\n(:*********************************************************************:)\n\nxs:boolean(\"1\") le xs:boolean(\"true\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_010() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-010.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is less or equal to arg2:Boolean :)\n(:*********************************************************************:)\n\nxs:boolean(\"0\") le xs:boolean(\"true\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_011() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-011.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is less or equal to arg2:Boolean :)\n(:*********************************************************************:)\n\nxs:boolean(\"true\") le xs:boolean(\"1\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_012() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-012.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is less or equal to arg2:Boolean :)\n(:*********************************************************************:)\n\nxs:boolean(\"true\") le xs:boolean(\"0\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_013() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-013.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is less or equal to arg2:Boolean :)\n(:*********************************************************************:)\n\nxs:boolean(\"true\") le xs:boolean(\"false\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_boolean_greater_than_more_args_014() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: op-boolean-greater-than-more-args-014.xq          :)\n(: Written By: Pulkita Tyagi                             :)\n(: Date: Thu Jun  2 00:16:48 2005                        :)\n(: Purpose: To check if arg1: Boolean is less or equal to arg2:Boolean :)\n(:*********************************************************************:)\n\nxs:boolean(\"false\") le xs:boolean(\"true\")\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_BooleanGT_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-BooleanGT-1                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: Compare two xs:boolean values.               :)\n(:*******************************************************:)\ntrue() gt false()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_BooleanGT_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-BooleanGT-2                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: Compare two xs:boolean values.               :)\n(:*******************************************************:)\ntrue() ge false()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_BooleanGT_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-BooleanGT-3                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: Compare two xs:boolean values.               :)\n(:*******************************************************:)\ntrue() ge true()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;
}
