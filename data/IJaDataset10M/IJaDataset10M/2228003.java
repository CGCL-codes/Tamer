package algs.example.chapter7.fifteenSolitaire.fixed;

import java.util.Iterator;
import algs.model.list.DoubleLinkedList;
import algs.model.searchtree.DepthFirstSearch;
import algs.model.searchtree.IMove;
import algs.model.searchtree.Solution;

/**
 * Show solution to basic fifteen solitaire.
 * 
 * @author George Heineman
 * @version 1.0, 6/15/08
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) {
        JumpingSolitaireState start = new JumpingSolitaireState(new boolean[] { true, true, true, true, false, true, true, true, true, true, true, true, true, true, true });
        boolean blank[] = new boolean[] { false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };
        blank[12] = true;
        JumpingSolitaireState goal = new JumpingSolitaireState(blank);
        DepthFirstSearch dfs = new DepthFirstSearch();
        Solution sol = dfs.search(start, goal);
        System.out.println(dfs.numOpen + " open, " + dfs.numClosed + " closed");
        DoubleLinkedList<IMove> mv = sol.moves();
        for (Iterator<IMove> it = mv.iterator(); it.hasNext(); ) {
            IMove m = it.next();
            m.execute(start);
            System.out.println(start);
        }
    }
}
