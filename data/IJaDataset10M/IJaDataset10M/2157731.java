package ch.nostromo.tiffanys.tests.problems;

import ch.nostromo.tiffanys.engines.tiffanys.ai.ZobristKeyImpl;
import ch.nostromo.tiffanys.game.GameController;
import ch.nostromo.tiffanys.game.GameControllerException;
import ch.nostromo.tiffanys.game.player.PlayerEngine;
import ch.nostromo.tiffanys.tests.TestBase;

public class Zobrist extends TestBase {

    public void testPerformance() throws ClassNotFoundException, GameControllerException {
        PlayerEngine white = this.getEngineWithFixedDepth(2);
        PlayerEngine black = this.getEngineWithFixedDepth(2);
        String fenInput = "r2qk2r/ppp2ppp/2n2n2/2bppb2/2BPPB2/2N2N2/PPP2PPP/R2Q1RK1 b kq - 0 6";
        GameController gc = new GameController(white, black, fenInput);
        int[] board = gc.getCurrentBoard().getBoardArray();
        for (int i = 0; i < 5000000; i++) {
            ZobristKeyImpl.computeKeyOld(board, 0);
        }
    }
}
