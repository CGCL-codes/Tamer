package ch.ethz.mxquery.MinimalConformance.Functions.AllStringFunc.GeneralStringFunc;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class EscapeHTMLURIFuncclass extends XQueryTestBase {

    public void test_fn_escape_html_uri1args_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test:fn-escape-html-uri1args-1:)\n(:Written By:Joanne Tong:)\n(:Date:2005-09-29T15:36:54+01:00:)\n(:Purpose:Test escape-html-uri from example defined in functions and operators specification:)\n(:*******************************************************:)\n\nescape-html-uri(\"http://www.example.com/00/Weather/CA/Los Angeles#ocean\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("http://www.example.com/00/Weather/CA/Los Angeles#ocean").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri1args_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test:fn-escape-html-uri1args-2:)\n(:Written By:Joanne Tong:)\n(:Date:2005-09-29T15:38:20+01:00:)\n(:Purpose:Test escape-html-uri from example defined in functions and operators specification:)\n(:*******************************************************:)\n\nescape-html-uri(\"javascript:if (navigator.browserLanguage == 'fr') window.open('http://www.example.com/~bébé');\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("javascript:if (navigator.browserLanguage == 'fr') window.open('http://www.example.com/~b%C3%A9b%C3%A9');").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri1args_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test:fn-escape-html-uri1args-3:)\n(:Written By:Joanne Tong:)\n(:Date:2005-09-29T15:39:13+01:00:)\n(:Purpose:Test escape-html-uri with zero-length string argument:)\n(:*******************************************************:)\n\nescape-html-uri('')").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri1args_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(:Test:fn-escape-html-uri1args-4:)\n(:Written By:Joanne Tong:)\n(:Date:2005-09-29T15:39:50+01:00:)\n(:Purpose:Test escape-html-uri with empty sequence argument:)\n(:*******************************************************:)\n\nescape-html-uri(())").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri1args_5() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(:Test:fn-escape-html-uri1args-5:)\n(:Written By:Joanne Tong:)\n(:Date:2005-09-29T15:40:38+01:00:)\n(:Purpose:Test escape-html-uri with invalid argument types:)\n(:*******************************************************:)\n\nescape-html-uri(12)").toString();
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

    public void test_fn_escape_html_uri1args_6() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(:Test:fn-escape-html-uri1args-6:)\n(:Written By:Joanne Tong:)\n(:Date:2005-09-29T15:41:21+01:00:)\n(:Purpose:Test escape-html-uri with incorrect arity:)\n(:*******************************************************:)\n\nescape-html-uri('',())").toString();
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

    public void test_fn_escape_html_uri_1() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-1:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the lower cases letters.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"abcdedfghijklmnopqrstuvwxyz\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("abcdedfghijklmnopqrstuvwxyz").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_2() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-2:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the upper cases letters.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"ABCDEFGHIJKLMNOPQRSTUVWXYZ\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("ABCDEFGHIJKLMNOPQRSTUVWXYZ").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_3() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-3:)\n(: Description: Examines that the fn:escape-html-uri function does not escape digits.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"a0123456789\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("a0123456789").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_4() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-4:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the space.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_5() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-5:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \"!\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example!example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example!example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_6() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-6:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \"#\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example#example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example#example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_7() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-7:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \"$\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example$example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example$example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_8() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-8:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \"'\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example'example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example'example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_9() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-9:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \"(\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example(example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example(example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_10() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-10:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \")\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example)example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example)example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_11() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-11:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \"*\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example*example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example*example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_12() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-12:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \"+\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example+example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example+example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_13() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-13:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \",\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example,example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example,example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_14() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-14:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \"-\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example-example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example-example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_15() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-15:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \".\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example.example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example.example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_16() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-16:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \"/\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example/example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example/example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_17() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-17:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \";\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example;example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example;example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_18() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-18:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \":\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example:example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example:example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_19() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-19:)\n(: Description: Examines that the fn:escape-html-uri function does not escape the \"@\" symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example@example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example@example").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_20() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-20:)\n(: Description: Examines that the fn:escape-html-uri function does escape the euro symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example&#xe9;&#x20AC;example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example%C3%A9%E2%82%ACexample").toString() }, testcase.result());
    }

    ;

    public void test_fn_escape_html_uri_21() throws Exception {
        String query = new StringBuilder().append("(: Name: fn-escape-html-uri-21:)\n(: Description: Examines that the fn:escape-html-uri function does escape the euro symbol.:)\n\n(: insert-start :)\ndeclare variable $input-context1 external;\n(: insert-end :)\n\nfn:escape-html-uri(\"example&#x20AC;example\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context1", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example%E2%82%ACexample").toString() }, testcase.result());
    }

    ;

    public void test_K_EscapeHTMLURIFunc_1() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-EscapeHTMLURIFunc-1                           :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `escape-html-uri()`. :)\n(:*******************************************************:)\nescape-html-uri()").toString();
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

    public void test_K_EscapeHTMLURIFunc_2() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-EscapeHTMLURIFunc-2                           :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `escape-html-uri(\"http://example.com/\", \"wrong param\")`. :)\n(:*******************************************************:)\nescape-html-uri(\"http://example.com/\", \"wrong param\")").toString();
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

    public void test_K_EscapeHTMLURIFunc_3() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-EscapeHTMLURIFunc-3                           :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: A test whose essence is: `escape-html-uri(()) eq \"\"`. :)\n(:*******************************************************:)\nescape-html-uri(()) eq \"\"").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_EscapeHTMLURIFunc_4() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-EscapeHTMLURIFunc-4                           :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: Combine fn:concat and fn:escape-html-uri.    :)\n(:*******************************************************:)\nescape-html-uri(\"http://www.example.com/00/Weather/CA/Los Angeles#ocean\")\n			eq \"http://www.example.com/00/Weather/CA/Los Angeles#ocean\"").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_EscapeHTMLURIFunc_5() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-EscapeHTMLURIFunc-5                           :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: Combine fn:concat and fn:escape-html-uri.    :)\n(:*******************************************************:)\nescape-html-uri(\"javascript:if (navigator.browserLanguage == 'fr') window.open('http://www.example.com/~bébé');\")\n			eq \"javascript:if (navigator.browserLanguage == 'fr') window.open('http://www.example.com/~b%C3%A9b%C3%A9');\"").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_EscapeHTMLURIFunc_6() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-EscapeHTMLURIFunc-6                           :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:40+02:00                       :)\n(: Purpose: Invoke fn:normalize-space() on the return value of fn:escape-html-uri(). :)\n(:*******************************************************:)\nnormalize-space(iri-to-uri((\"example.com\", current-time())[1] treat as xs:string))").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("example.com").toString() }, testcase.result());
    }

    ;
}
