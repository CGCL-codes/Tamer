package ch.ethz.mxquery.test.fulltext;

import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.StaticException;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.testsuite.XQueryTestBase;

public class FTElementTest extends XQueryTestBase {

    static String queries = "FTTests/queries/Use_Case_Element/";

    static String expected_result = "FTTests/expectedResult/Use_Case_Element/";

    public void test_1() throws Exception {
        String query = UriToString(queries + "Q1_Word_Query_in_Element.xq");
        PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
        doQuery(expr);
        String expectedResult = UriToString(expected_result + "Q1_Word_Query_in_Element.xml");
        assertEquals(expectedResult, resultBuffer.toString().trim());
    }

    public void test_2() throws Exception {
        String query = UriToString(queries + "Q2_Phrase_Query_in_Element.xq");
        PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
        doQuery(expr);
        String expectedResult = UriToString(expected_result + "Q2_Phrase_Query_in_Element.xml");
        assertEquals(expectedResult, resultBuffer.toString().trim());
    }

    public void test_3() throws Exception {
        String query = UriToString(queries + "Q3_Phrase_Query_on_Chinese_Characters_in_Element.xq");
        try {
            PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
            doQuery(expr);
            String expectedResult = UriToString(expected_result + "Q3_Phrase_Query_on_Chinese_Characters_in_Element.xml");
            assertEquals(expectedResult, resultBuffer.toString().trim());
            assertEquals(UriToString(expected_result + "Q3_Query_Excluding_Stop_Word_on_Stop_Word_List.xml"), resultBuffer.toString().trim());
        } catch (StaticException se) {
            assertEquals(ErrorCodes.FTST009_LANGUAGE_NOT_SUPPORTED, se.getErrorCode());
        }
    }

    public void test_4() throws Exception {
        String query = UriToString(queries + "Q4_Query_in_Different_Elements.xq");
        PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
        doQuery(expr);
        String expectedResult = UriToString(expected_result + "Q4_Query_in_Different_Elements.xml");
        assertEquals(expectedResult, resultBuffer.toString().trim());
    }

    public void test_5() throws Exception {
        String query = UriToString(queries + "Q5_Query_in_Element_Returning_Different_Elements.xq");
        PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
        doQuery(expr);
        String expectedResult = UriToString(expected_result + "Q5_Query_in_Element_Returning_Different_Elements.xml");
        assertEquals(expectedResult, resultBuffer.toString().trim());
    }

    public void test_6() throws Exception {
        String query = UriToString(queries + "Q6_Starts_with_Query.xq");
        try {
            PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
            doQuery(expr);
            String expectedResult = UriToString(expected_result + "Q6_Starts_with_Query.xml");
            assertEquals(expectedResult, resultBuffer.toString().trim());
        } catch (StaticException se) {
            assertEquals(ErrorCodes.FTST0012_FTContentOperator_NOT_SUPPORTED, se.getErrorCode());
        }
    }

    public void test_7() throws Exception {
        String query = UriToString(queries + "Q7_Entire_Element_Content.xq");
        try {
            PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
            doQuery(expr);
            String expectedResult = UriToString(expected_result + "Q7_Entire_Element_Content_Query.xml");
            assertEquals(expectedResult, resultBuffer.toString().trim());
        } catch (StaticException se) {
            assertEquals(ErrorCodes.FTST0012_FTContentOperator_NOT_SUPPORTED, se.getErrorCode());
        }
    }
}
