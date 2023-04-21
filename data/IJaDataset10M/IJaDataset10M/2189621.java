package ch.ethz.mxquery.MinimalConformance.Functions.ContextFunc;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class ContextImplicitTimezoneFuncclass extends XQueryTestBase {

    public void test_fn_implicit_timezone_1() throws Exception {
        try {
            String query = new StringBuilder().append("(:Test: fn-implicit-timezone-1                           :)\n(:Description Evaluation of \"fn:implicit-timezone\" with   :)\n(:incorrect arity.                                       :)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:implicit-timezone(\"Argument 1\")").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPST0017" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_implicit_timezone_2() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-2                           :)\n(:Description: Normal call to \"fn:implicit-timezone\".     :)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("-PT5H").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_3() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-3                               :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of an addition operation.                            :)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() + fn:implicit-timezone())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("-PT10H").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_4() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-4                               :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part  :)\n(:of a subtraction operation.                                :)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() - fn:implicit-timezone())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("PT0S").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_5() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-5                               :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(:of a multiplication operation.                            :)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() * xs:double(2))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("-PT10H").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_6() throws Exception {
        try {
            String query = new StringBuilder().append("(:Test: fn-implicit-timezone-6                               :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(:of a multiplication operation.  Second argument is NaN:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() * (0 div 0E0))").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOCA0005" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_implicit_timezone_7() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-7                               :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a multiplication operation.  Second argument is 0:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() * 0)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("PT0S").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_8() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-8                               :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a multiplication operation.  Second argument is -0:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() * -0)").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("PT0S").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_9() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-9                               :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a division operation.  :)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() div xs:double(2))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("-PT2H30M").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_10() throws Exception {
        try {
            String query = new StringBuilder().append("(:Test: fn-implicit-timezone-10                              :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a division operation.  Second argument results in NaN :)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() div ( 0 div 0E0))").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOCA0005" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_implicit_timezone_11() throws Exception {
        try {
            String query = new StringBuilder().append("(:Test: fn-implicit-timezone-11                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a division operation.  Second argument is 0.          :)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() div  0 )").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FODT0002" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_implicit_timezone_12() throws Exception {
        try {
            String query = new StringBuilder().append("(:Test: fn-implicit-timezone-12                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a division operation.  Second argument is -0.         :)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() div  -0 )").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FODT0002" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_implicit_timezone_13() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-13                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a division operation.  Both operands includes the fn:implicit-timezone.:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\n(implicit-timezone() + xs:dayTimeDuration('PT1S')) div\n(implicit-timezone() + xs:dayTimeDuration('PT1S'))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_14() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-14                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a division operation.  Second operand is a call to xs:dayTimeDuration function.:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:implicit-timezone() div  xs:dayTimeDuration(\"P0DT60M00S\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("-5").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_15() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-15                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of an addition operation.  First operand is a call to xs:time function.:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(xs:time(\"05:00:00\") + fn:implicit-timezone())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("00:00:00").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_16() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-16                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a subtraction operation.  First operand is a call to xs:time function.:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(xs:time(\"05:00:00\") - fn:implicit-timezone())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("10:00:00").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_17() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-17                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a subtraction operation.  First operand is a call to xs:date function.:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(xs:date(\"2000-10-30\") - fn:implicit-timezone())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("2000-10-30").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_18() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-18                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of an addition operation.  First operand is a call to xs:date function.:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(xs:date(\"2000-10-30\") + fn:implicit-timezone())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("2000-10-29").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_19() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-19                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of a subtraction operation.  First operand is a call to xs:dateTime function.:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(xs:dateTime(\"2000-10-30T11:12:00\") - fn:implicit-timezone())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("2000-10-30T16:12:00").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_20() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-20                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as part :)\n(: of an addition operation.  First operand is a call to xs:dateTime function.:)\n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(xs:dateTime(\"2000-10-30T11:12:00\") + fn:implicit-timezone())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("2000-10-30T06:12:00").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_21() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-21                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as an   :)\n(: an argument to the adjust-date-to-timezone function.     :)                  \n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:adjust-date-to-timezone(xs:date(\"2000-10-30\"),fn:implicit-timezone()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("2000-10-30-05:00").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_22() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-22                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as an   :)\n(: an argument to the adjust-time-to-timezone function.     :)                  \n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:adjust-time-to-timezone(xs:time(\"10:00:00\"),fn:implicit-timezone()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("10:00:00-05:00").toString() }, testcase.result());
    }

    ;

    public void test_fn_implicit_timezone_23() throws Exception {
        String query = new StringBuilder().append("(:Test: fn-implicit-timezone-23                             :)\n(:Description: Evaluation of \"fn:implicit-timezone\" as an   :)\n(: an argument to the adjust-dateTime-to-timezone function. :)                  \n\n(:insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:string(fn:adjust-dateTime-to-timezone(xs:dateTime(\"2002-03-07T10:00:00\"),fn:implicit-timezone()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Inspect", new String[] { new StringBuilder().append("2002-03-07T10:00:00-05:00").toString() }, testcase.result());
    }

    ;

    public void test_K_ContextImplicitTimezoneFunc_1() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ContextImplicitTimezoneFunc-1                 :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:41+02:00                       :)\n(: Purpose: A test whose essence is: `implicit-timezone(\"WRONG PARAM\")`. :)\n(:*******************************************************:)\nimplicit-timezone(\"WRONG PARAM\")").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPST0017" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_ContextImplicitTimezoneFunc_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ContextImplicitTimezoneFunc-2                 :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:41+02:00                       :)\n(: Purpose: Simple test of implicit-timezone().          :)\n(:*******************************************************:)\nseconds-from-duration(implicit-timezone()) le 0\n				   or\n				   seconds-from-duration(implicit-timezone()) gt 0").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_ContextImplicitTimezoneFunc_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ContextImplicitTimezoneFunc-3                 :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:41+02:00                       :)\n(: Purpose: Test that implicit-timezone() do return a value. :)\n(:*******************************************************:)\nexists(seconds-from-duration(implicit-timezone()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;
}
