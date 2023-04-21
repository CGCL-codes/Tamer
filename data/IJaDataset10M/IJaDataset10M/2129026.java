package ch.ethz.mxquery.MinimalConformance.Functions.DurationDateTimeFunc.ComponentExtractionDDT;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class DayFromDateFuncclass extends XQueryTestBase {

    public void test_fn_day_from_date1args_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date1args-1                              :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:38 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:date(lower bound)                            :)\n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1970-01-01Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date1args_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date1args-2                              :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:38 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:date(mid range)                              :)\n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1983-11-17Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("17").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date1args_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date1args-3                              :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:38 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:date(upper bound)                            :)\n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"2030-12-31Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("31").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-1                                  :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:As per example 1 of the F&O  specs                     :)\n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1999-05-31-05:00\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("31").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-2                                  :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:As per example 2 of the F&O  specs                     :)\n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"2000-01-01+05:00\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-3                                  :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:involving a \"numeric-less-than\" operation (lt operator):)\n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1999-12-31Z\")) lt fn:day-from-date(xs:date(\"1999-12-31Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-4                                  :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:involving a \"numeric-less-than\" operation (le operator):)\n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1999-12-31Z\")) le fn:day-from-date(xs:date(\"1999-12-31Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-5                                  :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:using the empty sequence as an argument. Use count     :) \n(:function to avoid empty file.                          :)\n(:*******************************************************:)\n\nfn:count(fn:day-from-date(()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-6                                  :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:that returns 31.                                       :) \n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1999-05-31Z\")) \n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("31").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-7                                  :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:used as arguments to an avg function     .             :) \n(:*******************************************************:)\n\nfn:avg((fn:day-from-date(xs:date(\"1999-12-31Z\")),fn:day-from-date(xs:date(\"1999-12-29Z\")))) ").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("30").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-8                                  :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"+\" expression.                           :) \n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1970-01-01Z\")) + fn:day-from-date(xs:date(\"1970-01-01Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-9                                  :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"-\" expression.                           :) \n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1970-01-01Z\")) - fn:day-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-10                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"*\" expression.                           :) \n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1970-01-03Z\")) * fn:day-from-date(xs:date(\"0002-01-01Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("3").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_11() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-11                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"div\" expression.                         :) \n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1970-01-02Z\")) div fn:day-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_12() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-12                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"idiv\" expression.                        :) \n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1970-01-02Z\")) idiv fn:day-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_13() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-13                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"mod\" expression.                         :) \n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1970-01-02Z\")) mod fn:day-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_14() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-14                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"numeric-unary-plus\" expression.          :) \n(:*******************************************************:)\n\n+fn:day-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_15() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-15                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"numeric-unary-minus\" expression.         :) \n(:*******************************************************:)\n\n-fn:day-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("-1").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_16() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-16                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"numeric-equal\" expression (eq operator)  :) \n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1970-01-02Z\")) eq fn:day-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_17() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: month-from-date-17                               :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"month-from-date\" function      :)\n(:as part of a \"numeric-equal\" expression (ne operator)  :) \n(:*******************************************************:)\n\nfn:month-from-date(xs:date(\"1970-01-01Z\")) ne fn:month-from-date(xs:date(\"1970-01-03Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_18() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-18                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"numeric-equal\" expression (le operator)  :) \n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1970-01-01Z\")) le fn:day-from-date(xs:date(\"1970-01-02Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_fn_day_from_date_19() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: day-from-date-19                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"day-from-date\" function        :)\n(:as part of a \"numeric-equal\" expression (ge operator)  :) \n(:*******************************************************:)\n\nfn:day-from-date(xs:date(\"1970-01-03Z\")) ge fn:day-from-date(xs:date(\"1970-01-01Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_DayFromDateFunc_1() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-DayFromDateFunc-1                             :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `day-from-date()`.  :)\n(:*******************************************************:)\nday-from-date()").toString();
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

    public void test_K_DayFromDateFunc_2() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-DayFromDateFunc-2                             :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `day-from-date((), \"Wrong param\")`. :)\n(:*******************************************************:)\nday-from-date((), \"Wrong param\")").toString();
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

    public void test_K_DayFromDateFunc_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-DayFromDateFunc-3                             :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `empty(day-from-date(()))`. :)\n(:*******************************************************:)\nempty(day-from-date(()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_DayFromDateFunc_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-DayFromDateFunc-4                             :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `day-from-date(()) instance of xs:integer?`. :)\n(:*******************************************************:)\nday-from-date(()) instance of xs:integer?").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_DayFromDateFunc_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-DayFromDateFunc-5                             :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `day-from-date(xs:date(\"2000-02-03\")) eq 3`. :)\n(:*******************************************************:)\nday-from-date(xs:date(\"2000-02-03\")) eq 3").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;
}
