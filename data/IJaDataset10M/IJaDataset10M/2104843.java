package ch.ethz.mxquery.MinimalConformance.Functions.DurationDateTimeFunc.ComponentExtractionDDT;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class YearFromDateFuncclass extends XQueryTestBase {

    public void test_fn_year_from_date1args_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date1args-1                             :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:38 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:date(lower bound)                            :)\n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1970").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date1args_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date1args-2                             :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:38 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:date(mid range)                              :)\n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1983-11-17Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1983").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date1args_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date1args-3                             :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Wed Apr 13 09:47:38 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(: with the arguments set as follows:                    :)\n(:$arg = xs:date(upper bound)                            :)\n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"2030-12-31Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2030").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-1                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:As per example 1 of the F&O  specs                     :)\n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1999-05-31Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1999").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-2                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function:)\n(:As per example 2 of the F&O  specs                     :)\n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"2000-01-01Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2000").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-3                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:involving a \"numeric-less-than\" operation (lt operator):)\n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1999-12-31Z\")) lt fn:year-from-date(xs:date(\"1999-12-31Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-3                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:involving a \"numeric-less-than\" operation (le operator):)\n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1999-12-31Z\")) le fn:year-from-date(xs:date(\"1999-12-31Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-5                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:using the empty sequence as an argument. Use count     :) \n(:function to avoid empty file.                          :)\n(:*******************************************************:)\n\nfn:count(fn:year-from-date(()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-6                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:that returns 1.                                        :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"0001-05-31Z\")) \n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-7                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:that returns a negative number                         :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"-1999-05-31Z\")) \n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("-1999").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-8                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"+\" expression.                           :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\")) + fn:year-from-date(xs:date(\"1970-01-01Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("3940").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-9                                 :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"-\" expression.                           :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\")) - fn:year-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-10                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"*\" expression.                           :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\")) * fn:year-from-date(xs:date(\"0002-01-01Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("3940").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_11() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-11                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"div\" expression.                         :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\")) div fn:year-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_12() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-12                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"idiv\" expression.                        :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\")) idiv fn:year-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_13() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-13                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"mod\" expression.                         :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\")) mod fn:year-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("0").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_14() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-14                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"numeric-unary-plus\" expression.          :) \n(:*******************************************************:)\n\n+fn:year-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1970").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_15() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-15                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"numeric-unary-minus\" expression.         :) \n(:*******************************************************:)\n\n-fn:year-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("-1970").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_16() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-16                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"numeric-equal\" expression (eq operator)  :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\")) eq fn:year-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_17() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-17                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"numeric-equal\" expression (ne operator)  :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\")) ne fn:year-from-date(xs:date(\"1970-01-01Z\"))\n").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_18() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-18                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"numeric-equal\" expression (le operator)  :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\")) le fn:year-from-date(xs:date(\"1970-01-01Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_fn_year_from_date_19() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: year-from-date-19                                :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: June 6, 2005                                     :)\n(:Purpose: Evaluates The \"year-from-date\" function       :)\n(:as part of a \"numeric-equal\" expression (ge operator)  :) \n(:*******************************************************:)\n\nfn:year-from-date(xs:date(\"1970-01-01Z\")) ge fn:year-from-date(xs:date(\"1970-01-01Z\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_YearFromDateFunc_1() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-YearFromDateFunc-1                            :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `year-from-date()`. :)\n(:*******************************************************:)\nyear-from-date()").toString();
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

    public void test_K_YearFromDateFunc_2() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-YearFromDateFunc-2                            :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `year-from-date((), \"Wrong param\")`. :)\n(:*******************************************************:)\nyear-from-date((), \"Wrong param\")").toString();
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

    public void test_K_YearFromDateFunc_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-YearFromDateFunc-3                            :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `empty(year-from-date(()))`. :)\n(:*******************************************************:)\nempty(year-from-date(()))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_YearFromDateFunc_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-YearFromDateFunc-4                            :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `year-from-date(()) instance of xs:integer?`. :)\n(:*******************************************************:)\nyear-from-date(()) instance of xs:integer?").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_YearFromDateFunc_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-YearFromDateFunc-5                            :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `year-from-date(xs:date(\"2000-02-03\")) eq 2000`. :)\n(:*******************************************************:)\nyear-from-date(xs:date(\"2000-02-03\")) eq 2000").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;
}
