package ch.ethz.mxquery.MinimalConformance.Expressions.Operators.ArithExpr.DurationDateTimeArith;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class DateSubtractDTDclass extends XQueryTestBase {

    public void test_op_subtract_dayTimeDuration_from_date2args_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date2args-1      :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:08 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:subtract-dayTimeDuration-from-date\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:date(lower bound)                           :)\n(:$arg2 = xs:dayTimeDuration(lower bound)               :)\n(:*******************************************************:)\n\nxs:date(\"1970-01-01Z\") - xs:dayTimeDuration(\"P0DT0H0M0S\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1970-01-01Z").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date2args_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date2args-2      :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:08 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:subtract-dayTimeDuration-from-date\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:date(mid range)                             :)\n(:$arg2 = xs:dayTimeDuration(lower bound)               :)\n(:*******************************************************:)\n\nxs:date(\"1983-11-17Z\") - xs:dayTimeDuration(\"P0DT0H0M0S\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1983-11-17Z").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date2args_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date2args-3      :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:08 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:subtract-dayTimeDuration-from-date\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:date(upper bound)                           :)\n(:$arg2 = xs:dayTimeDuration(lower bound)               :)\n(:*******************************************************:)\n\nxs:date(\"2030-12-31Z\") - xs:dayTimeDuration(\"P0DT0H0M0S\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2030-12-31Z").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date2args_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date2args-4      :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:08 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:subtract-dayTimeDuration-from-date\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:date(lower bound)                           :)\n(:$arg2 = xs:dayTimeDuration(mid range)                 :)\n(:*******************************************************:)\n\nxs:date(\"1970-01-01Z\") - xs:dayTimeDuration(\"P15DT11H59M59S\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1969-12-16Z").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date2args_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date2args-5      :)\n(:Written By: Carmelo Montanez                            :)\n(:Date: Tue Apr 12 16:29:08 GMT-05:00 2005                :)\n(:Purpose: Evaluates The \"op:subtract-dayTimeDuration-from-date\" operator:)\n(: with the arguments set as follows:                    :)\n(:$arg1 = xs:date(lower bound)                           :)\n(:$arg2 = xs:dayTimeDuration(upper bound)               :)\n(:*******************************************************:)\n\nxs:date(\"1970-01-01Z\") - xs:dayTimeDuration(\"P31DT23H59M59S\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1969-11-30Z").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-1          :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The \"subtract-dayTimeDuration-from-date\" operator :)\n(:As per example 1 (for this function)of the F&O specs.  :)\n(:*******************************************************:)\n\nxs:date(\"2000-10-30\") - xs:dayTimeDuration(\"P3DT1H15M\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("2000-10-26").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-2          :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The string value of \"subtract-dayTimeDuration-from-date\" operator :)\n(:used as part of a boolean expression (and operator) and the \"fn:false\" function. :)\n(:*******************************************************:)\n\nfn:string(xs:date(\"2000-12-12Z\") - xs:dayTimeDuration(\"P12DT10H07M\")) and fn:false()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-3          :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The string value of \"subtract-dayTimeDuration-from-date\" operator as :)\n(:part of a boolean expression (or operator) and the \"fn:boolean\" function. :)\n(:*******************************************************:)\n \nfn:string((xs:date(\"1999-10-23Z\") - xs:dayTimeDuration(\"P19DT13H10M\"))) or fn:false()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-4          :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The string value of \"subtract-dayTimeDuration-from-date\" operator that  :)\n(:return true and used together with fn:not.             :)\n(:*******************************************************:)\n \nfn:not(fn:string(xs:date(\"1998-09-12Z\") - xs:dayTimeDuration(\"P02DT07H01M\")))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-5          :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The string value of \"subtract-dayTimeDuration-from-date\" operator that  :)\n(:is used as an argument to the fn:boolean function.     :)\n(:*******************************************************:)\n \nfn:boolean(fn:string(xs:date(\"1962-03-12Z\") - xs:dayTimeDuration(\"P03DT08H06M\")))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-6          :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The \"subtract-dayTimeDuration-from-date\" operator that :)\n(:is used as an argument to the fn:number function.      :)\n(:*******************************************************:)\n \nfn:number(xs:date(\"1988-01-28Z\") - xs:dayTimeDuration(\"P10DT08H01M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("NaN").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-7          :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The \"subtract-dayTimeDuration-from-date\" operator used  :)\n(:as an argument to the \"fn:string\" function).           :)\n(:*******************************************************:)\n \nfn:string(xs:date(\"1989-07-05Z\") - xs:dayTimeDuration(\"P01DT09H02M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("1989-07-03Z").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_8() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-8          :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The \"subtract-dayTimeDuration-from-date\" operator that  :)\n(:returns a negative value.                              :)\n(:*******************************************************:)\n \n(xs:date(\"0001-01-01Z\") - xs:dayTimeDuration(\"P11DT02H02M\"))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("-0001-12-20Z").toString(), new StringBuilder().append("0000-12-20Z").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_9() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-9          :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The string value of \"subtract-dayTimeDuration-from-date\" operator used  :)\n(:together with an \"and\" expression.                      :)\n(:*******************************************************:)\n \nfn:string((xs:date(\"1993-12-09Z\") - xs:dayTimeDuration(\"P03DT01H04M\"))) and fn:string((xs:date(\"1993-12-09Z\") - xs:dayTimeDuration(\"P01DT01H03M\")))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_10() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-10         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The string value of \"subtract-dayTimeDuration-from-date\" operator used  :)\n(:together with an \"or\" expression.                      :)\n(:*******************************************************:)\n \nfn:string((xs:date(\"1985-07-05Z\") - xs:dayTimeDuration(\"P03DT01H04M\"))) or fn:string((xs:date(\"1985-07-05Z\") - xs:dayTimeDuration(\"P01DT01H03M\")))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_12() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-12         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The string value of \"subtract-dayTimeDuration-from-date\" operator used :)\n(:with a boolean expression and the \"fn:true\" function.   :)\n(:*******************************************************:)\n \nfn:string((xs:date(\"1980-03-02Z\") - xs:dayTimeDuration(\"P05DT08H11M\"))) and (fn:true())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_13() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-13         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The \"subtract-dayTimeDuration-from-date\" operator used  :)\n(:together with the numeric-equal-operator \"eq\".         :)\n(:*******************************************************:)\n \n(xs:date(\"1980-05-05Z\") - xs:dayTimeDuration(\"P23DT11H11M\")) eq xs:date(\"1980-05-05Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_14() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-14         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The \"subtract-dayTimeDuration-from-date\" operator used  :)\n(:together with the numeric-equal operator \"ne\".         :)\n(:*******************************************************:)\n \n(xs:date(\"1979-12-12Z\") - xs:dayTimeDuration(\"P08DT08H05M\")) ne xs:date(\"1979-12-12Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_15() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-15         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The \"subtract-dayTimeDuration-from-date\" operator used  :)\n(:together with the numeric-equal operator \"le\".         :)\n(:*******************************************************:)\n \n(xs:date(\"1978-12-12Z\") - xs:dayTimeDuration(\"P17DT10H02M\")) le xs:date(\"1978-12-12Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_op_subtract_dayTimeDuration_from_date_16() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test: op-subtract-dayTimeDuration-from-date-16         :)\n(:Written By: Carmelo Montanez                           :)\n(:Date: July 1, 2005                                     :)\n(:Purpose: Evaluates The \"subtract-dayTimeDuration-from-date\" operator used  :)\n(:together with the numeric-equal operator \"ge\".         :)\n(:*******************************************************:)\n \n(xs:date(\"1977-12-12Z\") - xs:dayTimeDuration(\"P18DT02H02M\")) ge  xs:date(\"1977-12-12Z\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("false").toString() }, testcase.result());
    }

    ;

    public void test_K_DateSubtractDTD_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-DateSubtractDTD-1                             :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:36+02:00                       :)\n(: Purpose: Simple testing involving operator '-' between xs:date and xs:dayTimeDuration. :)\n(:*******************************************************:)\nxs:date(\"1999-08-12\") - xs:dayTimeDuration(\"P23DT09H32M59S\")\n					 eq xs:date(\"1999-07-19\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;
}
