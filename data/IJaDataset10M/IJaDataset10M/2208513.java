package trinidad;

import java.util.GregorianCalendar;

/**
 * AlfaBeta Zab�jca
 * Algorytm Alfa-Beta naiwny
 *   - zalecana g��boko��: 4-6
 */
public class AlgAlfaBetaSimple extends ComputerPlayerMisc implements ArtificalPlayer {

    private int nodesviewd;

    private GameStateX pass;

    private int maxDepth;

    public AlgAlfaBetaSimple(int maxdepth) {
        fieldClassV = new int[] { 164, 148, 140, 132, 116, 108, 104, 102 };
        maxDepth = maxdepth;
    }

    int umABEntry(GameState s, int depth, int alfa, int beta, int type) {
        nodesviewd++;
        if (depth == 0 || isTerminal(s) > 0) return getStateValue(s, type);
        GameStateX[] gsi = s.getSolution(type + 1);
        int maxx = 0;
        int tpa = (type + 1) % 2;
        for (int child = 1; child < gsi.length; child++) {
            int val = -umAB(gsi[child].getState(), depth - 1, -beta, -alfa, tpa);
            if (val > alfa) {
                maxx = child;
                alfa = val;
            }
            if (alfa >= beta) {
                pass = gsi[child];
                return beta;
            }
        }
        pass = gsi[maxx];
        return alfa;
    }

    int umAB(GameState s, int depth, int alfa, int beta, int type) {
        nodesviewd++;
        if (depth == 0 || isTerminal(s) > 0) return getStateValue(s, type);
        GameStateX[] gsi = s.getSolution(type + 1);
        int tpa = (type + 1) % 2;
        for (int child = 1; child < gsi.length; child++) {
            int val = -umAB(gsi[child].getState(), depth - 1, -beta, -alfa, tpa);
            if (val > alfa) alfa = val;
            if (alfa >= beta) return beta;
        }
        return alfa;
    }

    public Move[] makeMove(GameState state, int player) {
        nodesviewd = 0;
        GregorianCalendar t = new GregorianCalendar();
        long start = t.getTimeInMillis();
        int result = -umABEntry(state, maxDepth, -9000000, 9000000, player - 1);
        t = new GregorianCalendar();
        long stop = t.getTimeInMillis();
        System.out.println("time: " + (float) ((int) (stop - start)) / 1000 + " s");
        System.out.println("nodes: " + nodesviewd);
        return pass.getMoveCascade().toMoveArray();
    }
}
