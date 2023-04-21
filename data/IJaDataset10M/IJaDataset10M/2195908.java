package ch.ethz.mxquery.MinimalConformance.Functions.DurationDateTimeFunc.ComponentExtractionDDT;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class MinutesFromDurationFuncclass extends XQueryTestBase {

    public void test_fn_minutes_from_duration1args_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: minutes-from-duration1args-1                      :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:37 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:dayTimeDuration(lower bound)                :)\n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P0DT0H0M0S\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration1args_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: minutes-from-duration1args-2                      :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:37 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:dayTimeDuration(mid range)                  :)\n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P15DT11H59M59S\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("59").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration1args_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: minutes-from-duration1args-3                      :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:37 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:dayTimeDuration(upper bound)                :)\n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P31DT23H59M59S\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("59").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn minutes-from-duration-1                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:As per example 1 (for this function)of the F&O specs   :)\n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P3DT10H\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-2                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:As per example 2 (for this function) of the F&O  specs :)\n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"-P5DT12H30M\")) \n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("-30").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-3                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:involving a \"numeric-less-than\" operation (lt operator):)\n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P20DT20H20M\")) lt fn:minutes-from-duration(xs:dayTimeDuration(\"P03DT02H10M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-4                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:involving a \"numeric-less-than\" operation (le operator):)\n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P21DT10H10M\")) le fn:minutes-from-duration(xs:dayTimeDuration(\"P22DT10H09M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-5                          :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function   :)\n(:using the empty sequence as an argument. Use count     :) \n(:function to avoid empty file.                          :)\n(:*******************************************************:)\n\nfn:count(fn:minutes-from-duration(()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-6                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:that returns 1.                                        :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P01DT01H01M\")) \n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-7                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:used as arguments to an avg function.                  :) \n(:*******************************************************:)\n\nfn:avg((fn:minutes-from-duration(xs:dayTimeDuration(\"P23DT10H20M\")),fn:minutes-from-duration(xs:dayTimeDuration(\"P21DT10H10M\")))) ").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("15").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-8                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"+\" expression.                           :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P21DT10H10M\")) + fn:minutes-from-duration(xs:dayTimeDuration(\"P22DT11H30M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("40").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-9                       :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 11, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"-\" expression.                           :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P30DT10H20M\")) - fn:minutes-from-duration(xs:dayTimeDuration(\"P10DT09H10M\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("10").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-10                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"*\" expression.                           :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P20DT09H04M\")) * fn:minutes-from-duration(xs:dayTimeDuration(\"P03DT10H10M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("40").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_11() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-11                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"div\" expression.                         :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P20DT10H10M\")) div fn:minutes-from-duration(xs:dayTimeDuration(\"P05DT05H02M\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("5").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_12() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-12                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"idiv\" expression.                        :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P25DT10H20M\")) idiv fn:minutes-from-duration(xs:dayTimeDuration(\"P05DT02H04M\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("5").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_13() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-13                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"mod\" expression.                         :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P10DT10H20M\")) mod fn:minutes-from-duration(xs:dayTimeDuration(\"P03DT03H03M\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_14() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-14                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"numeric-unary-plus\" expression.          :) \n(:*******************************************************:)\n\n+fn:minutes-from-duration(xs:dayTimeDuration(\"P21DT10H10M\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("10").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_15() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-15                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"numeric-unary-minus\" expression.         :) \n(:*******************************************************:)\n\n-fn:minutes-from-duration(xs:dayTimeDuration(\"P20DT03H20M\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("-20").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_16() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-16                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"numeric-equal\" expression (eq operator)  :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P22DT10H10M\")) eq fn:minutes-from-duration(xs:dayTimeDuration(\"P22DT09H10M\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_17() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-17                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"numeric-equal\" expression (ne operator)  :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P23DT08H20M\")) ne fn:minutes-from-duration(xs:dayTimeDuration(\"P12DT05H22M\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_18() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-18                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"numeric-equal\" expression (le operator)  :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P20DT03H09M\")) le fn:minutes-from-duration(xs:dayTimeDuration(\"P21DT15H21M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_19() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-19                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 13, 2005                                    :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:as part of a \"numeric-equal\" expression (ge operator)  :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P21DT07H12M\")) ge fn:minutes-from-duration(xs:dayTimeDuration(\"P20DT01H13M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_20() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-20                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: March 24, 2006                                   :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:with wrong argument type.                              :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:duration(\"P1Y2M3DT10H30M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("30").toString() }, testcase.result());
    }

    ;

    public void test_fn_minutes_from_duration_21() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: fn-minutes-from-duration-21                      :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2006                                   :)\n(:Purpose: Evaluates The \"minutes-from-duration\" function:)\n(:to evaluate normalization of duration.                 :) \n(:*******************************************************:)\n\nfn:minutes-from-duration(xs:dayTimeDuration(\"P21DT10H65M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("5").toString() }, testcase.result());
    }

    ;

    public void test_K_MinutesFromDurationFunc_1() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-MinutesFromDurationFunc-1                     :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `minutes-from-duration()`. :)\n(:*******************************************************:)\nminutes-from-duration()").toString();
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

    public void test_K_MinutesFromDurationFunc_2() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-MinutesFromDurationFunc-2                     :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `minutes-from-duration((), \"Wrong param\")`. :)\n(:*******************************************************:)\nminutes-from-duration((), \"Wrong param\")").toString();
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

    public void test_K_MinutesFromDurationFunc_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-MinutesFromDurationFunc-3                     :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `empty(minutes-from-duration(()))`. :)\n(:*******************************************************:)\nempty(minutes-from-duration(()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_MinutesFromDurationFunc_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-MinutesFromDurationFunc-4                     :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `minutes-from-duration(()) instance of xs:integer?`. :)\n(:*******************************************************:)\nminutes-from-duration(()) instance of xs:integer?").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_MinutesFromDurationFunc_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-MinutesFromDurationFunc-5                     :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `minutes-from-duration(xs:dayTimeDuration(\"P3DT8H2M1.03S\")) eq 2`. :)\n(:*******************************************************:)\nminutes-from-duration(xs:dayTimeDuration(\"P3DT8H2M1.03S\")) eq 2").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_MinutesFromDurationFunc_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-MinutesFromDurationFunc-6                     :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: Simple test invoking minutes-from-duration() on a negative duration. :)\n(:*******************************************************:)\nminutes-from-duration(xs:dayTimeDuration(\"-P3DT8H2M1.03S\")) eq -2").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_MinutesFromDurationFunc_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-MinutesFromDurationFunc-7                     :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: Simple test invoking minutes-from-hours() on an negative xs:duration. :)\n(:*******************************************************:)\nminutes-from-duration(xs:duration(\"-P3Y4M8DT1H23M2.34S\")) eq -23").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;
}
