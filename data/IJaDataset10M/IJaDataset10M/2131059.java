package org.apache.rat.pd.heuristic.comment;

import org.apache.rat.pd.heuristic.HeuristicCheckerResult;
import org.apache.rat.pd.heuristic.IHeuristicChecker;
import org.apache.rat.pd.heuristic.comment.FortranCommentHeuristicChecker;
import junit.framework.TestCase;

public class FortranCommentHeuristicCheckerTest extends TestCase {

    private IHeuristicChecker checker;

    private static final String SIMPLE_COMMENT_1 = "!this is simple comment\n";

    private static final String SIMPLE_COMMENT_2 = "C   this is simple comment\n";

    private static final String SIMPLE_COMMENT_3 = "c this is simple comment \n";

    private static final String SIMPLE_COMMENT_4 = "* this is simple comment \n";

    private static final String VALID_COMMENT_1 = "something\nC this is simple comment\n";

    private static final String NOT_VALID_COMMENT_1 = "!this is non-valid comment";

    private static final String NOT_VALID_COMMENT_2 = "* this is comment \n something";

    private static final String NOT_VALID_COMMENT_3 = "c this is simple comment\n something";

    private static final String NOT_VALID_COMMENT_4 = "C\nthis is simple comment\n";

    private static final String NOT_VALID_COMMENT_5 = "Cthis is simple comment\n";

    protected void setUp() throws Exception {
        super.setUp();
        checker = new FortranCommentHeuristicChecker(5, System.out);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        checker = null;
    }

    public void testCheckByHeuristic() {
        assertEquals("There must be true returned because this is regulal comment", true, checker.checkByHeuristic(SIMPLE_COMMENT_1).isCheckOnInternetNeaded());
        assertEquals("There must be true returned because this is regulal comment", true, checker.checkByHeuristic(SIMPLE_COMMENT_2).isCheckOnInternetNeaded());
        assertEquals("There must be true returned because this is regulal comment", true, checker.checkByHeuristic(SIMPLE_COMMENT_3).isCheckOnInternetNeaded());
        assertEquals("There must be true returned because this is regulal comment", true, checker.checkByHeuristic(SIMPLE_COMMENT_4).isCheckOnInternetNeaded());
        HeuristicCheckerResult heuristicResult = checker.checkByHeuristic(VALID_COMMENT_1);
        assertEquals("There must be true returned because we have comment in this part of code which we are checking", true, heuristicResult.isCheckOnInternetNeaded());
        assertEquals("Code for searching must be equals to this:", "C this is simple comment\n", heuristicResult.getCodeSuggestedToBeChecked());
        assertEquals("There must be false returned because it does not end by the end of a line so it is not a regular comment", false, checker.checkByHeuristic(NOT_VALID_COMMENT_1).isCheckOnInternetNeaded());
        assertEquals("There must be false returned because there is something after comment", false, checker.checkByHeuristic(NOT_VALID_COMMENT_2).isCheckOnInternetNeaded());
        assertEquals("There must be false returned because there is something after comment", false, checker.checkByHeuristic(NOT_VALID_COMMENT_3).isCheckOnInternetNeaded());
        assertEquals("There must be false returned because we do not have more then one line comment in Fortran", false, checker.checkByHeuristic(NOT_VALID_COMMENT_4).isCheckOnInternetNeaded());
        assertEquals("There must be false returned because this code does not begine with C which is followed by empty caracter", false, checker.checkByHeuristic(NOT_VALID_COMMENT_5).isCheckOnInternetNeaded());
    }
}
