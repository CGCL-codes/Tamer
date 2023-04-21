package ch.ethz.mxquery.MinimalConformance.Expressions.SeqExpr.ConstructSeq;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class RangeExprclass extends XQueryTestBase {

    public void test_rangeExpr_1() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-1 :)\n(: Description: Evaluation of a single range expression using positive integers. :)\n\n(10, 1 to 4)\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("10 1 2 3 4").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_2() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-2 :)\n(: Description: Evaluation of a range expression of length one containing the single integer 10. :)\n\n10 to 10\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("10").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_3() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-3 :)\n(: Description: Evaluation of a range expression that results in a sequence of length 0.:)\n(: Uses \"fn:count\" to avoid empty file. :)\n\nfn:count(15 to 10)\n\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_4() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-4 :)\n(: Description: Evaluation of a range expression that uses the \"reverse\" function.:)\n\nfn:reverse(10 to 15)\n\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("15 14 13 12 11 10").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_5() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-5 :)\n(: Description: Evaluation of a range expression that uses the empty sequence function.:)\n(: Uses the count function to avoid empty file. :)\n\nfn:count((1, 2 to ()))\n\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_6() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-6 :)\n(: Description: Evaluation of a range expression, where both operands are addition operations.:)\n\n((1+2) to (2+2))\n\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("3 4").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_7() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-7 :)\n(: Description: Evaluation of a range expression, where the first operand are negative number.:)\n\n(-4,-3 to 2)\n\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("-4 -3 -2 -1 0 1 2").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_8() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-8 :)\n(: Description: Evaluation of a range expression, where both operands are negative integers.:)\n\n(-4, -3 to -1)\n\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("-4 -3 -2 -1").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_9() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-9 :)\n(: Description: Evaluation of a range expression, where the first operand is \"xs:integer\" function.:)\n\n(xs:integer(1) to 5)\n\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1 2 3 4 5").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_10() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-10 :)\n(: Description: Evaluation of a range expression, where the second operand is \"xs:integer\" function.:)\n\n(1 to xs:integer(5))\n\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1 2 3 4 5").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_11() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-11 :)\n(: Description: Evaluation of a range expression, where both operands are \"xs:integer\" functions.:)\n\n(xs:integer(1) to xs:integer(5))\n\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1 2 3 4 5").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_12() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-12 :)\n(: Description: Evaluation of a range expression, using the \"fn:min\" function :)\n\n(fn:min((1,2)) to 5)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1 2 3 4 5").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_13() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-13 :)\n(: Description: Evaluation of a range expression, using the \"fn:max\" function :)\n\n(fn:max((1,2)) to 5)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2 3 4 5").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_14() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-14 :)\n(: Description: Evaluation of a range expression, using the \"fn:min\" and \"fn:max\" functions :)\n\n(fn:min((1,2)) to fn:max((6,7)))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1 2 3 4 5 6 7").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_15() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-15 :)\n(: Description: Evaluation of a range expression as an argument to a \"fn:min\" function) :)\n\nfn:min((1 to 5))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_16() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-16 :)\n(: Description: Evaluation of a range expression as an argument to a \"fn:max\" function) :)\n\nfn:max((1 to 5))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("5").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_17() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-17 :)\n(: Description: Evaluation of a range expression as an argument to an \"fn:avg\" function) :)\n\nfn:avg((1 to 5))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("3").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_18() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-18 :)\n(: Description: Evaluation of a range expression as an argument to an \"fn:count\" function) :)\n\nfn:count((1 to 5))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("5").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_19() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-19 :)\n(: Description: Evaluation of a range expression, where the first operand is a multiplication operation. :)\n\n((3*2) to 10)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("6 7 8 9 10").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_20() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-20 :)\n(: Description: Evaluation of a range expression, where the second operand is a multiplication operation. :)\n\n(1 to (3*2))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1 2 3 4 5 6").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_21() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-21 :)\n(: Description: Evaluation of a range expression, where both operands are multiplication operations. :)\n\n((1*2) to (3*2))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2 3 4 5 6").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_22() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-22 :)\n(: Description: Evaluation of a range expression, where the first operand is a subtraction operation. :)\n\n((3 - 2) to 10)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1 2 3 4 5 6 7 8 9 10").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_23() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-23 :)\n(: Description: Evaluation of a range expression, where the second operand is a subtraction operation. :)\n\n(1 to (3 - 2))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_24() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-24 :)\n(: Description: Evaluation of a range expression, where both operands are subtraction operations. :)\n\n((2 - 1) to (7 - 1))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1 2 3 4 5 6").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_25() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-25 :)\n(: Description: Evaluation of a range expression, where the first operand is a division operation. :)\n\n((6 idiv 2) to 10)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("3 4 5 6 7 8 9 10").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_26() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-26 :)\n(: Description: Evaluation of a range expression, where the second operand is a division operation. :)\n\n(1 to (10 idiv 2))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1 2 3 4 5").toString() }, testcase.result());
    }

    ;

    public void test_rangeExpr_27() throws Exception {
        String query = new StringBuilder().append("(: Name: rangeExpr-27 :)\n(: Description: Evaluation of a range expression, where both operands are division operations. :)\n\n((5 idiv 5) to (8 idiv 2))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1 2 3 4").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-1                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: Since the left operand has the static cardinality zero-or-more, implementations using the static typing feature may raise XPTY0004. :)\n(:*******************************************************:)\n1 to 1 eq 1").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-2                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(30 to 3)`.   :)\n(:*******************************************************:)\nempty(30 to 3)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-3                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(0 to -3)`.   :)\n(:*******************************************************:)\nempty(0 to -3)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-4                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(1 to ())`.   :)\n(:*******************************************************:)\nempty(1 to ())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-5                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(() to 1)`.   :)\n(:*******************************************************:)\nempty(() to 1)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-6                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(-1 to -3)`.  :)\n(:*******************************************************:)\nempty(-1 to -3)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-7                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `count(1 to 4) eq 4`. :)\n(:*******************************************************:)\ncount(1 to 4) eq 4").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-8                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `count(0 to 4) eq 5`. :)\n(:*******************************************************:)\ncount(0 to 4) eq 5").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-9                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `count(-5 to -0) eq 6`. :)\n(:*******************************************************:)\ncount(-5 to -0) eq 6").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-10                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `count((10, 1 to 4)) eq 5`. :)\n(:*******************************************************:)\ncount((10, 1 to 4)) eq 5").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_11() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-11                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `subsequence(-3 to -1, 1, 1) eq -3`. :)\n(:*******************************************************:)\nsubsequence(-3 to -1, 1, 1) eq -3").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_12() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-12                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `subsequence(-3 to -1, 3, 1) eq -1`. :)\n(:*******************************************************:)\nsubsequence(-3 to -1, 3, 1) eq -1").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_13() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-13                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(reverse(4 to 1))`. :)\n(:*******************************************************:)\nempty(reverse(4 to 1))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_14() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-14                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `subsequence(reverse(1 to 3), 1, 1) eq 3`. :)\n(:*******************************************************:)\nsubsequence(reverse(1 to 3), 1, 1) eq 3").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_15() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-15                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `subsequence(reverse(1 to 3), 3, 1) eq 1`. :)\n(:*******************************************************:)\nsubsequence(reverse(1 to 3), 3, 1) eq 1").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_16() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-16                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `subsequence(reverse(1 to 4), 2, 1) eq 3`. :)\n(:*******************************************************:)\nsubsequence(reverse(1 to 4), 2, 1) eq 3").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_17() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-17                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `subsequence(reverse(1 to 4), 3, 1) eq 2`. :)\n(:*******************************************************:)\nsubsequence(reverse(1 to 4), 3, 1) eq 2").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_18() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-18                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `subsequence(reverse(-4 to -1), 2, 1) eq -2`. :)\n(:*******************************************************:)\nsubsequence(reverse(-4 to -1), 2, 1) eq -2").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_19() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-19                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `deep-equal((-1, -2, -3, -4), reverse(-4 to -1))`. :)\n(:*******************************************************:)\ndeep-equal((-1, -2, -3, -4), reverse(-4 to -1))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_20() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-20                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `deep-equal((), reverse(0 to -5))`. :)\n(:*******************************************************:)\ndeep-equal((), reverse(0 to -5))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_21() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-21                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `deep-equal((0, -1, -2, -3, -4, -5), reverse(-5 to 0))`. :)\n(:*******************************************************:)\ndeep-equal((0, -1, -2, -3, -4, -5), reverse(-5 to 0))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_22() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-22                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `count(reverse(-5 to -2)) eq 4`. :)\n(:*******************************************************:)\ncount(reverse(-5 to -2)) eq 4").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_23() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-23                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `count(reverse(-5 to -0)) eq 6`. :)\n(:*******************************************************:)\ncount(reverse(-5 to -0)) eq 6").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_24() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-24                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `count(reverse(1 to 4)) eq 4`. :)\n(:*******************************************************:)\ncount(reverse(1 to 4)) eq 4").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_25() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-25                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(1 to 0)`.    :)\n(:*******************************************************:)\nempty(1 to 0)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_26() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-26                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(0 to -5)`.   :)\n(:*******************************************************:)\nempty(0 to -5)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_27() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-27                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(-4 to -5)`.  :)\n(:*******************************************************:)\nempty(-4 to -5)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_28() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-28                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(reverse(1 to 0))`. :)\n(:*******************************************************:)\nempty(reverse(1 to 0))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_29() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-29                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(reverse(0 to -5))`. :)\n(:*******************************************************:)\nempty(reverse(0 to -5))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_30() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-30                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `empty(reverse(-4 to -5))`. :)\n(:*******************************************************:)\nempty(reverse(-4 to -5))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_31() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-31                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `(remove((2.e0, 4), 1) treat as xs:integer to 4) eq 4`. :)\n(:*******************************************************:)\n(remove((2.e0, 4), 1) treat as xs:integer to 4) eq 4").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_32() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-32                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `(4 to remove((2e0, 4), 1) treat as xs:integer) eq 4`. :)\n(:*******************************************************:)\n(4 to remove((2e0, 4), 1) treat as xs:integer) eq 4").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_RangeExpr_33() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-33                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `1.1 to 3`.         :)\n(:*******************************************************:)\n1.1 to 3").toString();
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

    public void test_K_RangeExpr_34() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-34                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `3 to 1.1`.         :)\n(:*******************************************************:)\n3 to 1.1").toString();
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

    public void test_K_RangeExpr_35() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-35                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `1.1 to 3.3`.       :)\n(:*******************************************************:)\n1.1 to 3.3").toString();
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

    public void test_K_RangeExpr_36() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-RangeExpr-36                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: A test whose essence is: `1 + 1.1 to 5`.     :)\n(:*******************************************************:)\n1 + 1.1 to 5").toString();
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

    public void test_K2_RangeExpr_1() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K2-RangeExpr-1                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-08-04T17:13:26Z                            :)\n(: Purpose: A test whose essence is: `1e3 to 3`.         :)\n(:*******************************************************:)\n1e3 to 3").toString();
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

    public void test_K2_RangeExpr_2() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K2-RangeExpr-2                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-08-04T17:13:26Z                            :)\n(: Purpose: A test whose essence is: `3 to 1e3`.         :)\n(:*******************************************************:)\n3 to 1e3").toString();
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
