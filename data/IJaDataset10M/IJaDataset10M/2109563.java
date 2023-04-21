package net.sourceforge.dragonchess.backend;

import net.sourceforge.dragonchess.connectivity.DCMessage;
import net.sourceforge.dragonchess.main.DCConstants;
import net.sourceforge.dragonchess.main.DCCoord;
import net.sourceforge.dragonchess.main.DCExtCoord;
import net.sourceforge.dragonchess.main.DCFrontEndDump;
import net.sourceforge.dragonchess.main.DCMove;
import net.sourceforge.dragonchess.main.DCMoveList;

/**
 * Encodes DCMessages. Contains methods that return DCMessages that, when
 * interpreted by a decoder on the other end, result in similar methods being
 * called there.
 */
public class DCBackEndEncoder {

    /**
	 * Sends a message notifying both players and spectators of the current
	 * gamestate. This happens everytime the game state changes in the backend,
	 * and also when a player or spectator registers.
	 * @param	state	the game state to send
	 * @return	DCMessage to send
	 */
    public DCMessage setGameState(int state) {
        Integer stateInteger = new Integer(state);
        DCMessage message = new DCMessage(DCConstants.MSG_GAMESTATE, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, stateInteger);
        return message;
    }

    /**
	 * Sends a message notifying both players and specators of the currently
	 * active player. 
	 * @param	player	the currently active player
	 * @return	DCMessage to send
	 */
    public DCMessage setActivePlayer(int player) {
        Integer playerInteger = new Integer(player);
        DCMessage message = new DCMessage(DCConstants.MSG_ACTIVE_PLAYER, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, playerInteger);
        return message;
    }

    /**
	 * Creates a message notifying both players and the spectators that a
	 * player has registered. This happens whenever a player successfully
	 * registers himself with the backend. This message follows a message to
	 * the registered player that his registration was a success :
	 * MSG_REGISTER_SUCCESS
	 * @param	name		String nickname of the registered player
	 * @param	address		String address of the registered player
	 * @param	playerNumber	which player the player is registered as,
	 * 							either DCConstants.GOLD or DCConstants.SCARLET
	 * @return	DCMessage to send
	 */
    public DCMessage newPlayerRegistered(String name, String address, int playerNumber) {
        Integer playerNumberInteger = new Integer(playerNumber);
        Object[] data = new Object[3];
        data[0] = name;
        data[1] = address;
        data[2] = playerNumberInteger;
        DCMessage message = new DCMessage(DCConstants.MSG_NEW_PLAYER_REGISTERED, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, data);
        return message;
    }

    /**
	 * Player has unregistered. 
	 * @param	player	int with player that has unregistered
	 * @param	reason	String with reason for unregistration
	 * @return	message DCMessage to return
	 */
    public DCMessage playerUnregistered(int player, String reason) {
        Object[] data = { new Integer(player), reason };
        DCMessage message = new DCMessage(DCConstants.MSG_PLAYER_UNREGISTERED, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, data);
        return message;
    }

    /**
	 * Creates a message notifying the player that has tried to register that
	 * his attempt has succeeded. DCGame uses this message to link 
	 * connectionNr to playerNr
	 * @param	name		String nickname of the registered player
	 * @param	address		String address of the registered player
	 * @param	playerNumber	which player the player is registered as,
	 * 							either DCConstants.GOLD or DCConstants.SCARLET
	 * @param	connectionNumber number specifying connection in DCGame
	 * @return	DCMessage to send
	 */
    public DCMessage registerSuccess(String name, String address, int playerNumber, int connectionNumber) {
        Integer playerNumberInteger = new Integer(playerNumber);
        Object[] data = new Object[3];
        data[0] = name;
        data[1] = address;
        data[2] = playerNumberInteger;
        DCMessage message = new DCMessage(DCConstants.MSG_REGISTER_SUCCESS, DCConstants.TO_FRONTEND, playerNumber, false, data);
        message.setConnection(connectionNumber);
        return message;
    }

    /**
	 * Creates a message notifying the player that has tried to register that
	 * his attempt has failed. This happens when a player tries to register
	 * himself with the backend, but the backend already has two registered
	 * players.
	 * @param	connectionNumber	the connection number that DCGame should
	 * 								send the message to
	 * @return	DCMessage to send
	 */
    public DCMessage registerFailure(int connectionNumber) {
        DCMessage message = new DCMessage(DCConstants.MSG_REGISTER_FAILURE, DCConstants.TO_FRONTEND, DCConstants.PLAYER_NONE, false);
        message.setConnection(connectionNumber);
        return message;
    }

    /**
	 * Creates a message notifying the client about the currently registered
	 * players. This is sent when a player registers while there is already
	 * someone else registered.
	 * @param	player	player number to store info about
	 * @param	name	String with name of player
	 * @param	address	String with address of player
	 * @param	connectionNumber	connection number
	 * @return	DCMessage to send
	 */
    public DCMessage setPlayerInfo(int player, String name, String address, int connectionNumber) {
        Object[] data = new Object[3];
        data[0] = new Integer(player);
        data[1] = name;
        data[2] = address;
        DCMessage message = new DCMessage(DCConstants.MSG_SET_PLAYER_INFO, DCConstants.TO_FRONTEND, DCConstants.PLAYER_NONE, false, data);
        message.setConnection(connectionNumber);
        return message;
    }

    /**
	 * Creates a message notifying the player that has selected a piece, that
	 * the selection has been successful.
	 * @param	player	player to send message to
	 * @param	location	The DCCoord with the location that has been
	 * selected
	 * @return	DCMessage to send
	 */
    public DCMessage pieceSelected(int player, DCCoord location) {
        DCMessage message = new DCMessage(DCConstants.MSG_PIECE_SELECTED, DCConstants.TO_FRONTEND, player, false, location);
        return message;
    }

    /**
	 * Creates a message notifying the player that has tried to select a piece,
	 * that the selection has been unsuccessful. This happens if the gameState
	 * was wrong, if the player selecting was not the active player, or if the
	 * location selected does not contain a valid piece.
	 * @param	player	the player to send to
	 * @param	location	the location that was rejected
	 * @param	reason		the reason for the rejection (defined in
	 * DCConstants)
	 * @return	DCMessage to send
	 */
    public DCMessage pieceNotSelected(int player, DCCoord location, int reason) {
        Object[] data = { location, new Integer(reason) };
        DCMessage message = new DCMessage(DCConstants.MSG_PIECE_NOT_SELECTED, DCConstants.TO_FRONTEND, player, false, data);
        return message;
    }

    /**
	 * Creates a message notifying the player that his piece has been
	 * deselected. This happens when a move is performed, when a move is undone
	 * or when the piece has been deselected manually
	 * @param	player	the player to send to
	 * @param	location	DCCoord with location of the deselected piece
	 * @param list list of moves
	 * @return	DCMessage to send
	 */
    public DCMessage pieceDeselected(int player, DCCoord location, DCMoveList list) {
        Object[] data = new Object[2];
        data[0] = location;
        data[1] = list;
        DCMessage message = new DCMessage(DCConstants.MSG_PIECE_DESELECTED, DCConstants.TO_FRONTEND, player, false, data);
        return message;
    }

    /**
	 * Sends a list of all valid targets to the player selecting a piece.
	 * @param	player	the player to send to
	 * @param	list	the list of DCMoves
	 * @return	DCMessage to send
	 */
    public DCMessage sendValidTargets(int player, DCMoveList list) {
        DCMessage message = new DCMessage(DCConstants.MSG_VALID_TARGETS, DCConstants.TO_FRONTEND, player, false, list);
        return message;
    }

    /**
	 * Creates a message indicating that the piece has not been moved. Happens
	 * when a player has tried to move a piece, but he either has no right to,
	 * or the move is invalid
	 * @param	player		player to send to
	 * @param location 
	 * @param	reason		reason for the rejection (Defined in DCConstants)
	 * @return
	 */
    public DCMessage pieceNotMoved(int player, DCCoord location, int reason) {
        Object[] data = { location, new Integer(reason) };
        DCMessage message = new DCMessage(DCConstants.MSG_PIECE_NOT_MOVED, DCConstants.TO_FRONTEND, player, false, data);
        return message;
    }

    /**
	 * Creates a message indicating that the piece has been moved. Happens when
	 * a player moves a piece.
	 * @param	move		DCMove that has been performed
	 * @return	DCMessage to send
	 */
    public DCMessage pieceMoved(DCMove move) {
        DCMessage message = new DCMessage(DCConstants.MSG_PIECE_MOVED, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, move);
        return message;
    }

    /**
	 * Creates a message indicating that a move has been undone. Contains the
	 * move to be undone. In the case of a CAPT or CAFAR, this message does not
	 * tell the frontend which piece should be restored, the pieceRestored
	 * method does this
	 * @param	move	DCMove to undo
	 * @return	DCMessage to send
	 */
    public DCMessage moveUndone(DCMove move) {
        DCMessage message = new DCMessage(DCConstants.MSG_MOVE_UNDONE, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, move);
        return message;
    }

    /**
	 * Creates a message indicating that ? move has not been undone. Can happen
	 * if the history is empty so there is nothing to undo, or if the other
	 * player refuses the undo request
	 * @param	reason		int with reason (DCConstants.UNDO_...)
	 * @param	player		int with player requesting the undo
	 * @return	DCMessage to send
	 */
    public DCMessage moveNotUndone(int reason, int player) {
        DCMessage message = new DCMessage(DCConstants.MSG_MOVE_NOT_UNDONE, DCConstants.TO_FRONTEND, player, false, new Integer(reason));
        return message;
    }

    /**
	 * Creates a message indicating that a piece has been promoted. Can happen
	 * after a move. Sent to all players and spectators
	 * @param	promotion	DCExtCoord with info of promoted piece
	 * @return	DCMessage to send
	 */
    public DCMessage piecePromoted(DCExtCoord promotion) {
        DCMessage message = new DCMessage(DCConstants.MSG_PIECE_PROMOTED, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, promotion);
        return message;
    }

    /**
	 * Creates a message indicating that a piece has been demoted. Can happen
	 * when undoing a move. Sent to all players and spectators
	 * @param	demotion	DCExtCoord with info of demoted piece
	 * @return	DCMessage to send
	 */
    public DCMessage pieceDemoted(DCExtCoord demotion) {
        DCMessage message = new DCMessage(DCConstants.MSG_PIECE_DEMOTED, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, demotion);
        return message;
    }

    /**
	 * Creates a message containing the current location of all pieces on the
	 * board. 
	 * @param 	player		player to send to
	 * @param	dump		position of all the pieces
	 */
    public DCMessage loadDCFrontEndDump(int player, DCFrontEndDump dump) {
        DCMessage message = new DCMessage(DCConstants.MSG_LOAD_DUMP, DCConstants.TO_FRONTEND, player, (player == DCConstants.PLAYER_BOTH) ? true : false, dump);
        return message;
    }

    /**
	 * Creates a message indicating the freeze status of a certain location.
	 * @param	location	location to set status for
	 * @param	frozen		boolean with freeze status
	 * @return	DCMessage to send
	 */
    public DCMessage setFreezeStatus(DCCoord location, boolean frozen) {
        Object[] data = { location, new Boolean(frozen) };
        DCMessage message = new DCMessage(DCConstants.MSG_FREEZE, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, data);
        return message;
    }

    /**
	 * Creates a message that informs the frontend that the specified player is
	 * in check.
	 * @param	player	player who is in check
	 * @return	DCMessage to send
	 */
    public DCMessage setCheck(int player) {
        DCMessage message = new DCMessage(DCConstants.MSG_CHECK, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, new Integer(player));
        return message;
    }

    /**
	 * Creates a message with the reason why the game is over and the player
	 * who has won. If the player is PLAYER_BOTH, it means the game was a draw.
	 * @param	reason	DCConstants GAMEOVER mode
	 * @param	winner	winning player
	 * @return	DCMessage to send
	 */
    public DCMessage gameOver(int reason, int winner) {
        Object[] data = { new Integer(reason), new Integer(winner) };
        DCMessage message = new DCMessage(DCConstants.MSG_GAMEOVER, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, data);
        return message;
    }

    /**
	 * Creates a message with a string representation of a move to add to the
	 * history, and the player who has made this move
	 * @param	String		move string
	 * @param	player		player who performed the move
	 * @return	DCMessage to send
	 */
    public DCMessage addHistoryString(String moveString, int player) {
        Object[] data = { moveString, new Integer(player) };
        DCMessage message = new DCMessage(DCConstants.MSG_ADD_HISTORY, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, data);
        return message;
    }

    /**
	 * Creates a message that asks the frontend to remove the last history
	 * string from its history list.
	 * @param	player	int with player to whom move belongs
	 * @return	DCMessage to send
	 */
    public DCMessage removeHistoryString(int player) {
        DCMessage message = new DCMessage(DCConstants.MSG_REMOVE_HISTORY, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, new Integer(player));
        return message;
    }

    /**
	 * Creates a message notifying the frontend that a piece has been moved
	 * from the garbage area to the board. Can happen in an undo move
	 * @param	coord	DCExtCoord with info on the restored piece
	 * @return	DCMessage to send
	 */
    public DCMessage pieceRestored(DCExtCoord coord) {
        DCMessage message = new DCMessage(DCConstants.MSG_PIECE_RESTORED, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, coord);
        return message;
    }

    /**
	 * Creates a message notifying all players that a game has started
	 * @param	player	player who has started game
	 * @return	DCMessage to send
	 */
    public DCMessage gameStarted(int player) {
        DCMessage message = new DCMessage(DCConstants.MSG_GAME_STARTED, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, true, new Integer(player));
        return message;
    }

    /**
	 * Creates a chat message to send to the frontend.
	 * @param	publicBoolean whether or not it is a public message
	 * @param	playerName	String with name of sending player
	 * @param	msg			the chat message to send
	 * @return 	DCMessage to send
	 */
    public DCMessage chatMessage(boolean publicBoolean, String playerName, String msg) {
        Object[] data = { new Boolean(publicBoolean), playerName, msg };
        DCMessage message = new DCMessage(DCConstants.MSG_PLAYER_CHAT_RECV, DCConstants.TO_FRONTEND, DCConstants.PLAYER_BOTH, publicBoolean, data);
        return message;
    }
}
