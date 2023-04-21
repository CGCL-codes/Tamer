package ch.ethz.mxquery.test.fulltext.FTDemoTests;

import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.testsuite.XQueryTestBase;

public class AnyAllOptionWithWindowTest extends XQueryTestBase {

    private static final String input = "FTTests/Files/books.xml";

    public void test_1() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book/title[. contains text {'House', 'Six'} all words window 2 words]", false, true, false, false, false);
        doQuery(expr);
        assertEquals("<title>Bringing Down the House: How Six Students Took Vegas for Millions.</title>", resultBuffer.toString().trim());
    }

    public void test_2() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book/title[. contains text {'House', 'Six'} all words window 0 words]", false, true, false, false, false);
        doQuery(expr);
        assertEquals("", resultBuffer.toString().trim());
    }

    public void test_3() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book[. contains text {'Atticus', 'Radley'} all words window 2 sentences]/title", false, true, false, false, false);
        doQuery(expr);
        assertEquals("<title>To Kill a Mocking Bird</title>", resultBuffer.toString().trim());
    }

    public void test_4() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book[. contains text {'Atticus', 'Radley'} all words window 0 sentences]/title", false, true, false, false, false);
        doQuery(expr);
        assertEquals("", resultBuffer.toString().trim());
    }

    public void test_5() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book[. contains text {'Atticus', 'Bob'} all words window 2 paragraphs]/title", false, true, false, false, false);
        doQuery(expr);
        assertEquals("<title>To Kill a Mocking Bird</title>", resultBuffer.toString().trim());
    }

    public void test_6() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book[. contains text {'Depression', 'Sheriff'} all words window 2 paragraphs]/title", false, true, false, false, false);
        doQuery(expr);
        assertEquals("", resultBuffer.toString().trim());
    }

    public void test_1a() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book/title[. contains text {'the House', 'Six Students'} all window 5 words]", false, true, false, false, false);
        doQuery(expr);
        assertEquals("<title>Bringing Down the House: How Six Students Took Vegas for Millions.</title>", resultBuffer.toString().trim());
    }

    public void test_2a() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book/title[. contains text {'the House', 'Six Students'} all window 2 words]", false, true, false, false, false);
        doQuery(expr);
        assertEquals("", resultBuffer.toString().trim());
    }

    public void test_3a() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book[. contains text {'Atticus', 'Radley'} all words window 2 sentences]/title", false, true, false, false, false);
        doQuery(expr);
        assertEquals("<title>To Kill a Mocking Bird</title>", resultBuffer.toString().trim());
    }

    public void test_4a() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book[. contains text {'Atticus', 'Radley'} all words window 0 sentences]/title", false, true, false, false, false);
        doQuery(expr);
        assertEquals("", resultBuffer.toString().trim());
    }

    public void test_5a() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book[. contains text {'Scout Finch', 'Mayella Ewell'} all window 1 paragraphs]/title", false, true, false, false, false);
        doQuery(expr);
        assertEquals("<title>To Kill a Mocking Bird</title>", resultBuffer.toString().trim());
    }

    public void test_6a() throws Exception {
        PreparedStatement expr = prepareQuery("fn:doc(\"" + input + "\")/books/book[. contains text {'Depression', 'sheriff'} all words window 2 paragraphs]/title", false, true, false, false, false);
        doQuery(expr);
        assertEquals("", resultBuffer.toString().trim());
    }
}
