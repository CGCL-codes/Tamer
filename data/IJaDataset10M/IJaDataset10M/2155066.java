package ch.ethz.mxquery.test.fulltext;

import ch.ethz.mxquery.exceptions.ErrorCodes;
import ch.ethz.mxquery.exceptions.StaticException;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.testsuite.XQueryTestBase;

public class FTLogicalTest extends XQueryTestBase {

    static String queries = "FTTests/queries/Use_Case_Logical/";

    static String expected_result = "FTTests/expectedResult/Use_Case_Logical/";

    public void test_1() throws Exception {
        String query = UriToString(queries + "Q1_Or_Query.xq");
        PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
        doQuery(expr);
        String expectedResult = UriToString(expected_result + "Q1_Or_Query.xml");
        assertEquals(expectedResult, resultBuffer.toString());
    }

    public void test_2() throws Exception {
        String query = UriToString(queries + "Q2_And_Query.xq");
        PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
        doQuery(expr);
        String expectedResult = UriToString(expected_result + "Q2_And_Query.xml");
        assertEquals(expectedResult, resultBuffer.toString());
    }

    public void test_3() throws Exception {
        String query = UriToString(queries + "Q3_And_Query_Ordered.xq");
        PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
        doQuery(expr);
        String expectedResult = UriToString(expected_result + "Q3_And_Query_Ordered.xml");
        assertEquals(expectedResult, resultBuffer.toString());
    }

    public void test_4() throws Exception {
        String query = UriToString(queries + "Q4_Unary_Not_Query.xq");
        try {
            PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
            doQuery(expr);
            String expectedResult = UriToString(expected_result + "Q4_Unary_Not_Query.xml");
            assertEquals(expectedResult, resultBuffer.toString());
        } catch (StaticException se) {
            assertEquals(ErrorCodes.FTST002_FTUnaryNotOperator_RESTRICTION_NOT_OBEYED, se.getErrorCode());
        }
    }

    public void test_5() throws Exception {
        String query = UriToString(queries + "Q5_And_Not_Query.xq");
        PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
        doQuery(expr);
        String expectedResult = UriToString(expected_result + "Q5_And_Not_Query.xml");
        assertEquals(expectedResult, resultBuffer.toString());
    }

    public void test_6() throws Exception {
        String query = UriToString(queries + "Q6_And_Not_Query_Where_Second_Operand_Is_Subset_of_First_Operand.xq");
        PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
        doQuery(expr);
        assertEquals("", resultBuffer.toString());
    }

    public void test_7() throws Exception {
        String query = UriToString(queries + "Q7_Mild_Not_Query_Where_Second_Operand_Is_Subset_of_First_Operand.xq");
        try {
            PreparedStatement expr = prepareQuery(query, false, true, false, false, false);
            doQuery(expr);
            String expectedResult = UriToString(expected_result + "Q7_Mild_Not_Query_Where_Second_Operand_Is_Subset_of_First_Operand.xml");
            assertEquals(expectedResult, resultBuffer.toString());
        } catch (StaticException se) {
            assertEquals(ErrorCodes.FTST001_FTMildNotOperator_NOT_SUPPORTED, se.getErrorCode());
        }
    }
}
