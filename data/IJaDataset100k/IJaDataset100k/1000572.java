package algs.model.tests.gametree;

import org.junit.Test;
import algs.model.gametree.IGameMove;
import algs.model.problems.tictactoe.model.BoardEvaluation;
import algs.model.problems.tictactoe.model.PlaceMark;
import algs.model.problems.tictactoe.model.Player;
import algs.model.problems.tictactoe.model.PlayerFactory;
import algs.model.problems.tictactoe.model.StraightLogic;
import algs.model.problems.tictactoe.model.TicTacToeBoard;
import algs.model.problems.tictactoe.model.TicTacToeState;
import junit.framework.TestCase;

public class BetaPruneTest extends TestCase {

    @Test
    public void testAlphaBeta() {
        StraightLogic logic = new StraightLogic();
        Player xPlayer = PlayerFactory.createPlayerWithPly(PlayerFactory.AlphaBeta, Player.XMARK, 2);
        xPlayer.logic(logic);
        xPlayer.score(new BoardEvaluation());
        Player oPlayer = PlayerFactory.createPlayerWithPly(PlayerFactory.AlphaBeta, Player.OMARK, 2);
        oPlayer.logic(logic);
        oPlayer.score(new BoardEvaluation());
        TicTacToeBoard board = new TicTacToeBoard();
        TicTacToeState state = new TicTacToeState(board, logic);
        algs.model.gametree.AlphaBetaEvaluation ae = new algs.model.gametree.AlphaBetaEvaluation(2);
        IGameMove move = ae.bestMove(state, xPlayer, oPlayer);
        System.out.println("best move:" + move);
        assertEquals(1, ((PlaceMark) move).getColumn());
        assertEquals(1, ((PlaceMark) move).getRow());
        System.out.println(ae.numStates + " nodes expanded");
    }
}
