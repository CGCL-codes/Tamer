package algs.model.problems.fifteenpuzzle;

import algs.model.searchtree.DepthTransition;
import algs.model.searchtree.INode;
import algs.model.searchtree.IScore;

/**
 * Better evaluation function, as inspired from Nilsson, p. 66., and applied
 * to the Fifteen Puzzle
 * 
 * @author George Heineman
 * @version 1.0, 6/15/08
 * @since 1.0
 */
public class GoodEvaluator implements IScore {

    /** Goal state for the fifteen puzzle. */
    static FifteenPuzzleNode goal = new FifteenPuzzleNode(new int[][] { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 0 } });

    /** index values [r][c] for proper locations, based on goal above. */
    static int[][] diffs = new int[][] { { 3, 3 }, { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 1, 3 }, { 2, 0 }, { 2, 1 }, { 2, 2 }, { 2, 3 }, { 3, 0 }, { 3, 1 }, { 3, 2 } };

    /**
	 * Location of the successor for each position, by location. We can take 
	 * 4*c+r as the index into succs and use that to generate the (r,c) of 
	 * the successive locationclockwise around the central square.
	 */
    static int[][] succs = new int[][] { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 3 }, { 0, 0 }, { -1, -1 }, { -1, -1 }, { 2, 3 }, { 1, 0 }, { -1, -1 }, { -1, -1 }, { 3, 3 }, { 2, 0 }, { 3, 0 }, { 3, 1 }, { 3, 2 } };

    /** successors of individual tiles [deal with 15->1 successor evenly.] */
    static int[] succ = new int[] { 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 1 };

    /**
	 * @see algs.model.searchtree.IScore#score(INode)
	 */
    public void score(INode state) {
        state.score(eval(state));
    }

    /**
	 * h^(n) = P(n) + 4*S(n), where P(n) is the sum of the distances (via manhattan
	 * metric) that each tile is from "home". S(n) is a sequence score obtained
	 * by checking around the noncentral squares in turn, allotting 2 for every 
	 * tile not followed by its proper successor and 0 for every other tile, except
	 * that a piece in the center scores 1.
	 * <p>
	 * Compute f^(n) = g^(n) + h^(n) where g(n) is the length of the path form the start node
	 * n to the state.
	 * <p>
	 * @see algs.model.searchtree.IScore#score(INode)
	 * @param state    state being evaluated
	 */
    public int eval(INode state) {
        FifteenPuzzleNode node = (FifteenPuzzleNode) state;
        int Pn = 0;
        for (int r = 0; r <= FifteenPuzzleNode.MaxR; r++) {
            for (int c = 0; c <= FifteenPuzzleNode.MaxC; c++) {
                if (node.isEmpty(r, c)) {
                    continue;
                }
                int digit = node.cell(r, c);
                Pn += Math.abs(diffs[digit][0] - r);
                Pn += Math.abs(diffs[digit][1] - c);
            }
        }
        int Sn = 0;
        if (!node.isEmpty(1, 1)) {
            Sn = 1;
        }
        for (int r = 0; r <= FifteenPuzzleNode.MaxR; r++) {
            for (int c = 0; c <= FifteenPuzzleNode.MaxC; c++) {
                int idx = r * 4 + c;
                if ((r == 1 && (c == 1 || c == 2)) || (r == 2 && (c == 1 || c == 2))) {
                    continue;
                }
                if (node.cell(r, c) == FifteenPuzzleNode.EmptyMark) {
                    continue;
                }
                if (succ[node.cell(r, c)] != node.cell(succs[idx][0], succs[idx][1])) {
                    Sn += 2;
                }
            }
        }
        int gn = 0;
        DepthTransition t = (DepthTransition) state.storedData();
        if (t != null) {
            gn = t.depth;
        }
        return gn + Pn + 4 * Sn;
    }
}
