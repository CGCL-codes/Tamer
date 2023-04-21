package net.sourceforge.dragonchess.backend;

import net.sourceforge.dragonchess.connectivity.DCMessage;
import net.sourceforge.dragonchess.connectivity.DCMessageable;
import net.sourceforge.dragonchess.main.DCConstants;
import net.sourceforge.dragonchess.main.DCCoord;
import net.sourceforge.dragonchess.main.DCOptions;

/**
 * This class decodes DCMessages backend method calls. It only has one method
 * which takes a DCMessage, extracts the appropriate data from it, and calls
 * the appropriate method of the {@link DCLocalGameEnv} to which it is linked.
 *
 * @author Davy Herben
 * @version 021206
 */
public class DCBackEndDecoder implements DCMessageable {

    /** target on which to call methods. */
    private DCLocalGameEnv gameEnv;

    /**
	 * Class constructor. Creates a new decoder that calls methods of the
	 * specified DCLocalGameEnv
	 * @param	gameEnv 	The DCLocalGameEnv to decode to
	 */
    public DCBackEndDecoder(DCLocalGameEnv gameEnv) {
        this.gameEnv = gameEnv;
    }

    /**
	 * takes a DCMessage to decode.
	 * @param	message		the message to decode
	 */
    public void sendMessage(DCMessage message) {
        int type = message.getType();
        int player = message.getPlayer();
        if (DCOptions.getDebugBackend()) {
            System.out.println("[BEDECODER] Received message type [" + type + "]");
        }
        Object[] data;
        switch(type) {
            case DCConstants.MSG_REGISTER_PLAYER:
                data = (Object[]) message.getData();
                gameEnv.registerPlayer((String) data[0], (String) data[1], ((Integer) data[2]).intValue(), message.getConnection());
                break;
            case DCConstants.MSG_START_GAME:
                gameEnv.startGame(message.getPlayer());
                break;
            case DCConstants.MSG_SELECT_PIECE:
                gameEnv.selectPiece((DCCoord) message.getData(), player);
                break;
            case DCConstants.MSG_MOVE_PIECE:
                gameEnv.movePiece((DCCoord) message.getData(), player);
                break;
            case DCConstants.MSG_REQUEST_UNDO:
                data = (Object[]) message.getData();
                gameEnv.undoMovePiece(((Integer) data[0]).intValue(), (String) data[1]);
                break;
            case DCConstants.MSG_RESIGN_GAME:
                data = (Object[]) message.getData();
                gameEnv.resignGame(((Integer) data[0]).intValue(), (String) data[1]);
                break;
            case DCConstants.MSG_UNREGISTER_PLAYER:
                gameEnv.unregisterPlayer(message.getPlayer(), (String) message.getData());
                break;
            case DCConstants.MSG_PLAYER_CHAT_SEND:
                data = (Object[]) message.getData();
                gameEnv.chatMessage(message.getPlayer(), ((Boolean) data[0]).booleanValue(), (String) data[1]);
                break;
            default:
                if (DCOptions.getDebugBackend()) {
                    System.err.println("[BECODER] Ignoring unknown message type [" + type + "]");
                }
        }
    }
}
