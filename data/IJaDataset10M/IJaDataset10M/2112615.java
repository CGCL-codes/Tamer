package algs.chapter7.figure17;

import algs.model.gametree.IGameMove;
import algs.model.problems.tictactoe.debug.TicTacToeDebugger;
import algs.model.problems.tictactoe.model.BoardEvaluation;
import algs.model.problems.tictactoe.model.PlaceMark;
import algs.model.problems.tictactoe.model.Player;
import algs.model.problems.tictactoe.model.PlayerFactory;
import algs.model.problems.tictactoe.model.StraightLogic;
import algs.model.problems.tictactoe.model.TicTacToeBoard;
import algs.model.problems.tictactoe.model.TicTacToeState;

public class Main {

    public static void main(String[] args) {
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
        new PlaceMark(2, 0, (Player) oPlayer).execute(state);
        new PlaceMark(2, 2, (Player) xPlayer).execute(state);
        algs.model.gametree.debug.MinimaxEvaluation me = new algs.model.gametree.debug.MinimaxEvaluation(2);
        TicTacToeDebugger std = new TicTacToeDebugger();
        me.debug(std);
        IGameMove move = me.bestMove(state, oPlayer, xPlayer);
        System.out.println(std.getInputString());
        System.err.println("best move:" + move);
        assert (0 == ((PlaceMark) move).getColumn());
        assert (0 == ((PlaceMark) move).getRow());
    }
}
