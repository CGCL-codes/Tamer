package ch.ethz.mxquery.test;

import ch.ethz.mxquery.testsuite.XQueryTestBase;

public class NamespaceTest extends XQueryTestBase {

    public void test_namespace1() throws Exception {
        String query = UriToString("anandTests/queries/namespace_1.xq");
        doQuery(prepareQuery(query, false, false, false, false, false));
        String expectedResult = UriToString("anandTests/expectedResult/namespace.xml");
        assertEquals(expectedResult, resultBuffer.toString().trim());
    }

    public void test_func_namespace() throws Exception {
        String query = UriToString("anandTests/queries/func.xq");
        doQuery(prepareQuery(query, false, false, false, false, false));
        String expectedResult = "anand";
        assertEquals(resultBuffer.toString().trim(), expectedResult);
    }
}
