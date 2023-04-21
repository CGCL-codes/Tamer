package algs.model.performance.chapter7;

import algs.debug.DottyDebugger;
import algs.model.problems.fifteenpuzzle.GoodEvaluator;
import algs.model.problems.fifteenpuzzle.FifteenPuzzleNode;
import algs.model.searchtree.debug.AStarSearch;
import algs.model.searchtree.Solution;

public class AlternateFigure7_12Main {

    public static void main(String[] args) {
        FifteenPuzzleNode impossibleStart = new FifteenPuzzleNode(new int[][] { { 5, 1, 2, 4 }, { 14, 9, 3, 7 }, { 13, 10, 12, 6 }, { 15, 11, 8, 0 } });
        FifteenPuzzleNode goal = new FifteenPuzzleNode(new int[][] { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 0 } });
        AStarSearch as = new AStarSearch(new GoodEvaluator());
        DottyDebugger std = new DottyDebugger();
        as.debug(std);
        Solution sol = as.search(impossibleStart, goal);
        System.out.println(std.getInputString());
        System.out.println(as.numClosed + " closed nodes, " + as.numOpen + " open nodes.");
        System.out.println(sol.toString());
    }
}
