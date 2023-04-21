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

public class AlphaBetaOnSameBoardAsNegMaxTest extends TestCase {

    @Test
    public void testAlphaBetaDebug() {
        StraightLogic logic = new StraightLogic();
        Player xPlayer = PlayerFactory.createPlayerWithPly(PlayerFactory.MiniMax, Player.XMARK, 2);
        xPlayer.logic(logic);
        xPlayer.score(new BoardEvaluation());
        Player oPlayer = PlayerFactory.createPlayerWithPly(PlayerFactory.MiniMax, Player.OMARK, 2);
        oPlayer.logic(logic);
        oPlayer.score(new BoardEvaluation());
        TicTacToeBoard board = new TicTacToeBoard();
        TicTacToeState state = new TicTacToeState(board, logic);
        new PlaceMark(1, 1, (Player) xPlayer).execute(state);
        new PlaceMark(0, 0, (Player) oPlayer).execute(state);
        new PlaceMark(0, 2, (Player) xPlayer).execute(state);
        algs.model.gametree.AlphaBetaEvaluation abe = new algs.model.gametree.AlphaBetaEvaluation(2);
        IGameMove move = abe.bestMove(state, oPlayer, xPlayer);
        System.out.println("best move:" + move);
        assertEquals(2, ((PlaceMark) move).getColumn());
        assertEquals(0, ((PlaceMark) move).getRow());
        abe.toString();
    }
}
