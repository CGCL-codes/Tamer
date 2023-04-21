package ch.ethz.mxquery.test;

import ch.ethz.mxquery.testsuite.XQueryTestBase;

public class ContainsTests extends XQueryTestBase {

    public void test_1() throws Exception {
        String query = "fn:contains ( \"tattoo\", \"t\")";
        doQuery(prepareQuery(query, false, false, false, false, false));
        assertEquals("true", resultBuffer.toString().trim());
    }

    ;

    public void test_1a() throws Exception {
        String query = "fn:contains ( \"tattoo\", \"tat\")";
        doQuery(prepareQuery(query, false, false, false, false, false));
        assertEquals("true", resultBuffer.toString().trim());
    }

    ;

    public void test_1b() throws Exception {
        String query = "fn:contains ( \"tattoo\", \"too\")";
        doQuery(prepareQuery(query, false, false, false, false, false));
        assertEquals("true", resultBuffer.toString().trim());
    }

    ;

    public void test_2() throws Exception {
        String query = "fn:contains ( \"tattoo\", \"ttt\")";
        doQuery(prepareQuery(query, false, false, false, false, false));
        assertEquals("false", resultBuffer.toString().trim());
    }

    ;

    public void test_3() throws Exception {
        String query = "fn:contains ( \"\", ())";
        doQuery(prepareQuery(query, false, false, false, false, false));
        assertEquals("true", resultBuffer.toString().trim());
    }

    ;
}
