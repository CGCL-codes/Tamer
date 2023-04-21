package bbalc.com.codec;

/**
 * This interfaces defines the possible game states, i.e. states of the game engine.
 * @author dirk
 */
public class IErrors {

    /**
	 * value out of range or argument not apropriate here
	 */
    public static final int RESULT_ERR_BAD_ARGUMENT = -17;

    /**
	 * defending player cannot be pushed to this square
	 */
    public static final int RESULT_ERR_CANNOT_PUSH_HERE = -39;

    /**
	 * AI engine is already calculating moves
	 */
    public static final int RESULT_ERR_DEEPTHOUGHT_BUSY = -25;

    /**
	 * team file is not found
	 */
    public static final int RESULT_ERR_FILE_NOT_FOUND = -9;

    /**
	 * AI engine is already initialised
	 */
    public static final int RESULT_ERR_GAME_ENGINE_ALREADY_INITIALISED = -26;

    /**
	 * AI engine is not initialised yet
	 */
    public static final int RESULT_ERR_GAME_ENGINE_NOT_INITIALISED = -27;

    /**
	 * team file is invalid
	 */
    public static final int RESULT_ERR_INVALID_TEAM = -10;

    /**
	 * the player has no ball
	 */
    public static final int RESULT_ERR_NO_BALL = -41;

    /**
	 * game engine has no com handle to communicate with
	 */
    public static final int RESULT_ERR_NO_COM_HANDLE = -2;

    /**
	 * the command does not exist
	 */
    public static final int RESULT_ERR_NO_SUCH_COMMAND = -256;

    /**
	 * the command has no parameters
	 */
    public static final int RESULT_ERR_INSUFFICIENT_PARAMETER = -511;

    /**
	 * the command has no parameters
	 */
    public static final int RESULT_ERR_NO_PARAMETER = -512;

    /**
	 * all apothecaries already used
	 */
    public static final int RESULT_ERR_NO_MORE_APOTHECARIES = -42;

    /**
	 * the player cannot move at all
	 */
    public static final int RESULT_ERR_NO_MORE_MA = -35;

    /**
	 * the player cannot sprint this far
	 */
    public static final int RESULT_ERR_NO_MORE_SPRINT = -37;

    /**
	 * no player is selected for this action
	 */
    public static final int RESULT_ERR_NO_PLAYER_SELECTED = -38;

    /**
	 * the player does not exist
	 */
    public static final int RESULT_ERR_NO_SUCH_PLAYER = -18;

    /**
	 * no synthesizer for this gamestate
	 */
    public static final int RESULT_ERR_NO_SUCH_SYNTHESIZER = -29;

    /**
	 * game engine has no team home
	 */
    public static final int RESULT_ERR_NO_TEAM_GUEST = -3;

    /**
	 * game engine has no team guest
	 */
    public static final int RESULT_ERR_NO_TEAM_HOME = -4;

    /**
	 * game engine has no teams at all
	 */
    public static final int RESULT_ERR_NO_TEAMS = -5;

    /**
	 * game engine is not initialised
	 */
    public static final int RESULT_ERR_NOT_INITIALISED = -1;

    /**
	 * it is not your turn
	 */
    public static final int RESULT_ERR_NOT_YOUR_TURN = -28;

    /**
	 * the field pos is out of bounds
	 */
    public static final int RESULT_ERR_OUT_OF_BOUNDS = -19;

    /**
	 * this player cannot move (this turn)
	 */
    public static final int RESULT_ERR_PLAYER_CANNOT_MOVE = -33;

    /**
	 * this square cannot be moved into because another player is already standing there 
	 */
    public static final int RESULT_ERR_SQUARE_NOT_EMPTY = -34;
}
