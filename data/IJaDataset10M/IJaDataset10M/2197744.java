package ch.ethz.mxquery.MinimalConformance.Functions.DurationDateTimeFunc.ComponentExtractionDDT;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class HoursFromDurationFuncclass extends XQueryTestBase {

    public void test_fn_hours_from_duration1args_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: hours-from-duration1args-1                        :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:37 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:dayTimeDuration(lower bound)                :)\n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P0DT0H0M0S\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration1args_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: hours-from-duration1args-2                        :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:37 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:dayTimeDuration(mid range)                  :)\n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P15DT11H59M59S\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("11").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration1args_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: hours-from-duration1args-3                        :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:37 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:dayTimeDuration(upper bound)                :)\n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P31DT23H59M59S\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("23").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn hours-from-duration-1                         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:As per example 1 (for this function)of the F&O specs   :)\n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P3DT10H\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("10").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-2                         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:As per example 2 (for this function) of the F&O  specs :)\n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P3DT12H32M12S\"))\n\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("12").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-3                         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as per example 3 of this function on the F&O specs.    :)\n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"PT123H\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("3").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-4                         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(: as per example 4 (for this function) in the F&O specs.:)\n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"-P3DT10H\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("-10").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-5                         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:using the empty sequence as an argument. Use count     :) \n(:function to avoid empty file.                          :)\n(:*******************************************************:)\n\nfn:count(fn:hours-from-duration(()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-6                         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:that returns 1.                                        :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P01DT01H\")) \n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-7                         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:used as arguments to an avg function.                  :) \n(:*******************************************************:)\n\nfn:avg((fn:hours-from-duration(xs:dayTimeDuration(\"P23DT10H\")),fn:hours-from-duration(xs:dayTimeDuration(\"P21DT08H\")))) ").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("9").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-8                         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"+\" expression.                           :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P21DT10H\")) + fn:hours-from-duration(xs:dayTimeDuration(\"P22DT20H\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("30").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-9                         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 11, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"-\" expression.                           :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P30DT10H\")) - fn:hours-from-duration(xs:dayTimeDuration(\"P10DT02H\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("8").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-10                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"*\" expression.                           :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P20DT05H\")) * fn:hours-from-duration(xs:dayTimeDuration(\"P03DT08H\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("40").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_11() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-11                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"div\" expression.                         :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P20DT10H\")) div fn:hours-from-duration(xs:dayTimeDuration(\"P05DT05H\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_12() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-12                         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function   :)\n(:as part of a \"idiv\" expression.                        :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P25DT10H\")) idiv fn:hours-from-duration(xs:dayTimeDuration(\"P05DT02H\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("5").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_13() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-13                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"mod\" expression.                         :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P10DT10H\")) mod fn:hours-from-duration(xs:dayTimeDuration(\"P03DT02H\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_14() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-14                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"numeric-unary-plus\" expression.          :) \n(:*******************************************************:)\n\n+fn:hours-from-duration(xs:dayTimeDuration(\"P21DT10H\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("10").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_15() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-15                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"numeric-unary-minus\" expression.         :) \n(:*******************************************************:)\n\n-fn:hours-from-duration(xs:dayTimeDuration(\"P20DT02H\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("-2").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_16() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-16                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"numeric-equal\" expression (eq operator)  :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P22DT09H\")) eq fn:hours-from-duration(xs:dayTimeDuration(\"P22DT09H\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_17() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-17                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"numeric-equal\" expression (ne operator)  :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P23DT07H\")) ne fn:hours-from-duration(xs:dayTimeDuration(\"P12DT05H\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_18() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-18                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"numeric-equal\" expression (le operator)  :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P20DT03H\")) le fn:hours-from-duration(xs:dayTimeDuration(\"P21DT01H\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_19() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-19                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:as part of a \"numeric-equal\" expression (ge operator)  :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:dayTimeDuration(\"P21DT07H\")) ge fn:hours-from-duration(xs:dayTimeDuration(\"P20DT08H\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_fn_hours_from_duration_20() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-hours-from-duration-20                        :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: March 24, 2006                                   :)\n(:Purpose: Evaluates The \"hours-from-duration\" function  :)\n(:with wrong argument type.                               :) \n(:*******************************************************:)\n\nfn:hours-from-duration(xs:duration(\"P1Y2M3DT10H30M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("10").toString() }, testcase.result());
    }

    ;

    public void test_K_HoursFromDurationFunc_1() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-HoursFromDurationFunc-1                       :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `hours-from-duration()`. :)\n(:*******************************************************:)\nhours-from-duration()").toString();
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

    public void test_K_HoursFromDurationFunc_2() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-HoursFromDurationFunc-2                       :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `hours-from-duration((), \"Wrong param\")`. :)\n(:*******************************************************:)\nhours-from-duration((), \"Wrong param\")").toString();
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

    public void test_K_HoursFromDurationFunc_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-HoursFromDurationFunc-3                       :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `empty(hours-from-duration(()))`. :)\n(:*******************************************************:)\nempty(hours-from-duration(()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_HoursFromDurationFunc_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-HoursFromDurationFunc-4                       :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `hours-from-duration(()) instance of xs:integer?`. :)\n(:*******************************************************:)\nhours-from-duration(()) instance of xs:integer?").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_HoursFromDurationFunc_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-HoursFromDurationFunc-5                       :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `hours-from-duration(xs:dayTimeDuration(\"P3DT8H2M1.03S\")) eq 8`. :)\n(:*******************************************************:)\nhours-from-duration(xs:dayTimeDuration(\"P3DT8H2M1.03S\")) eq 8").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_HoursFromDurationFunc_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-HoursFromDurationFunc-6                       :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: Simple test invoking hours-from-duration() on a negative duration. :)\n(:*******************************************************:)\nhours-from-duration(xs:dayTimeDuration(\"-P3DT8H2M1.03S\")) eq -8").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_HoursFromDurationFunc_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-HoursFromDurationFunc-7                       :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: Simple test invoking days-from-hours() on an negative xs:duration. :)\n(:*******************************************************:)\nhours-from-duration(xs:duration(\"-P3Y4M8DT1H23M2.34S\")) eq -1").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;
}
