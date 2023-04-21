package hexxa.handlers;

import hexxa.gui.board.BoardGraphical;
import hexxa.gui.shell.WindowManager;
import hexxa.gui.utils.CellInfo;
import hexxa.structs.Player;
import hexxa.structs.Player.PlayerType;
import hexxa.structs.board.BoardStructure;
import hexxa.utils.Images;

/**
 * This is static class handles a game with a BoardGraphical
 * @author David Ach
 */
public class GameHandler {

    /**
	 * Hexxagon has only 2 players, this is the first player
	 */
    private static Player _player1;

    /**
	 * Hexxagon has only 2 players, this is the second player
	 */
    private static Player _player2;

    /**
	 * Which player has the right to move
	 */
    private static Player _turn;

    /**
	 * True if the game is active
	 */
    private static boolean _active = false;

    /**
	 * The board which the game will be handled on.
	 */
    private static BoardGraphical board;

    /**
	 * Initialize the players and the board
	 * @param player1_type Which type will be the player 1
	 * @param player2_type Which type will be the player 2
	 */
    public static void start(String player1_type, String player2_type) {
        _player1 = new Player(PlayerType.valueOf(player1_type.toUpperCase()), Images.PLAYER_1, CellInfo.getBoardStructure().count(BoardStructure.PLAYER_1));
        _player2 = new Player(PlayerType.valueOf(player2_type.toUpperCase()), Images.PLAYER_2, CellInfo.getBoardStructure().count(BoardStructure.PLAYER_2));
        WindowManager.getMainWindow().openBoardGraphical();
        board = (BoardGraphical) WindowManager.getMainWindow().getPanel();
        _active = true;
        _turn = _player2;
        switchTurn();
    }

    /**
	 * Stop the game and opens the menu
	 */
    public static void stop() {
        _active = false;
        WindowManager.getMainWindow().openMainMenu();
    }

    /**
	 * Switches the current turn, and makes all the needed
	 * things for each player.
	 */
    public static void switchTurn() {
        if (!_active) {
            return;
        }
        if (_turn == _player1) {
            _turn = _player2;
        } else {
            _turn = _player1;
        }
        if (_turn.getType() != PlayerType.HUMAN) {
            board.activateLamp();
            new TreeHandler(board.createBoardVirtual(), _turn);
        } else {
            board.disableLamp();
        }
    }

    public static Player getTurn() {
        return _turn;
    }

    public static Player getPlayer1() {
        return _player1;
    }

    public static Player getPlayer2() {
        return _player2;
    }

    public static BoardGraphical getBoard() {
        return board;
    }
}
