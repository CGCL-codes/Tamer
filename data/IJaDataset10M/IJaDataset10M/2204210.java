package org.jogre.carTricks.client;

import nanoxml.XMLElement;
import org.jogre.client.JogreController;
import org.jogre.client.awt.JogreTableFrame;
import org.jogre.client.awt.ChatGameComponent;
import org.jogre.common.util.GameLabels;
import org.jogre.common.comm.CommGameOver;
import org.jogre.carTricks.common.CommCarTricksSetBid;
import org.jogre.carTricks.common.CommCarTricksPlayCard;
import org.jogre.carTricks.common.CommCarTricksRequestHand;
import org.jogre.carTricks.common.CommCarTricksSendHand;
import org.jogre.carTricks.common.CommCarTricksMoveCar;
import org.jogre.carTricks.common.CommCarTricksScore;
import org.jogre.carTricks.common.CarTricksPath;
import java.util.Vector;
import java.text.MessageFormat;

public class CarTricksMasterController extends JogreController {

    private CarTricksClientModel model;

    private CarTricksTrackController trackController;

    private CarTricksPlayerHandController handController;

    private CarTricksGameStateComponent gameStateComponent;

    private JogreTableFrame tableFrame;

    private ChatGameComponent theChatBox;

    private CarTricksScoreDialog scoreDialog = null;

    private Vector savedMessages = new Vector();

    private GameLabels labels;

    private String chatGameName;

    private MessageFormat chatNoMovementWreckFormat;

    private MessageFormat chatNoMovementAllEventFormat;

    private MessageFormat chatNoMovementBlockedFormat;

    private MessageFormat chatSingleMoveFormat;

    /**
	 * Constructor which creates the controller
	 *
	 * @param model					The game model
	 * @param trackController		The track controller
	 * @param handController		The hand controller
	 */
    public CarTricksMasterController(CarTricksClientModel model, CarTricksTrackController trackController, CarTricksPlayerHandController handController, CarTricksGameStateComponent gameStateComponent, JogreTableFrame tableFrame) {
        super(model, null);
        this.model = model;
        this.trackController = trackController;
        this.handController = handController;
        this.gameStateComponent = gameStateComponent;
        this.tableFrame = tableFrame;
        theChatBox = tableFrame.getMessageComponent();
        labels = GameLabels.getInstance();
        chatGameName = labels.get("chat.gameName");
        chatNoMovementWreckFormat = new MessageFormat(labels.get("chat.NoMovementWreck.format"));
        chatNoMovementAllEventFormat = new MessageFormat(labels.get("chat.NoMovementAllEvent.format"));
        chatNoMovementBlockedFormat = new MessageFormat(labels.get("chat.NoMovementBlocked.format"));
        chatSingleMoveFormat = new MessageFormat(labels.get("chat.SingleMove.format"));
    }

    /**
	 * Start a new game
	 *
	 * @see org.jogre.common.JogreModel#start()
	 */
    public void start() {
        model.resetGame();
        if (getSeatNum() >= 0) {
            conn.send(new CommCarTricksRequestHand(conn.getUsername()));
        } else {
            model.changePhase(CarTricksClientModel.SETTING_BID_SPECTATOR);
        }
    }

    /**
	 * Handle receving messages from the server
	 *
	 * @param	message		The message from the server
	 */
    public void receiveTableMessage(XMLElement message) {
        if (model.getTrackDatabase() == null) {
            savedMessages.add(message);
        } else {
            handleTableMessage(message);
        }
    }

    /**
	 * If there are any savedMessages, now is the time to handle them.
	 */
    public void clearSavedMessages() {
        while (!savedMessages.isEmpty()) {
            handleTableMessage((XMLElement) savedMessages.remove(0));
        }
    }

    /**
	 * Handle a table message from the server.
	 *
	 * @param	message		The message from the server
	 */
    private void handleTableMessage(XMLElement message) {
        String messageType = message.getName();
        if (messageType.equals(CommCarTricksSendHand.XML_NAME)) {
            handleSendHand(new CommCarTricksSendHand(message));
        } else if (messageType.equals(CommCarTricksPlayCard.XML_NAME)) {
            handlePlayCard(new CommCarTricksPlayCard(message));
        } else if (messageType.equals(CommCarTricksMoveCar.XML_NAME)) {
            handleMoveCar(new CommCarTricksMoveCar(message));
        } else if (messageType.equals(CommCarTricksScore.XML_NAME)) {
            handleScore(new CommCarTricksScore(message));
        }
    }

    /**
	 * Handle a message that has sent our hand of cards to us.
	 */
    private void handleSendHand(CommCarTricksSendHand theHandMsg) {
        model.initializeBid();
        model.setHand(theHandMsg.getHand());
        model.changePhase(CarTricksClientModel.SETTING_BID);
    }

    /**
	 * Handle a message that tells that a card has been played
	 */
    private void handlePlayCard(CommCarTricksPlayCard thePlayCardMsg) {
        int userId = getSeatNum(thePlayCardMsg.getUsername());
        model.playCard(userId, thePlayCardMsg.getCard(), thePlayCardMsg.isFinalCard());
    }

    /**
	 * Handle a message that tells us that a car has moved.
	 */
    private void handleMoveCar(CommCarTricksMoveCar theMoveCarMsg) {
        int userId = getSeatNum(theMoveCarMsg.getUsername());
        model.moveCar(userId, theMoveCarMsg.getPath());
    }

    /**
	 * Handle a message that tells us the game is over and what the score is.
	 */
    private void handleScore(CommCarTricksScore theScoreMsg) {
        model.changePhase(model.GAME_OVER);
        if (scoreDialog == null) {
            scoreDialog = new CarTricksScoreDialog(tableFrame, GameLabels.getInstance().get("score.dialog.title"), model.getNumPlayers(), conn.getTable().getPlayerList(), model.getCarPositions());
        }
        if (scoreDialog.addScore(getSeatNum(theScoreMsg.getUsername()), theScoreMsg.getBid())) {
            scoreDialog = null;
        }
    }

    /**
	 * This is called after every next_player message from the server.
	 * It will check to see if it is now time for the car to move, and if
	 * so and the car has only a single legal move, then this will submit
	 * the move message back to the server on behalf of the human player.
	 * It will also issue a chat message indicating that it has done so.
	 */
    public void checkForZeroLengthCarMove(XMLElement message) {
        Vector allPaths = model.getAllPaths();
        if (allPaths != null) {
            if (allPaths.size() == 1) {
                CarTricksPath thePath = (CarTricksPath) allPaths.firstElement();
                Object[] args = { labels.get("gamestate.movingCar." + model.getActiveCar(), ""), getPlayer(model.getWreckPlayerId()) };
                theChatBox.receiveMessage(chatGameName, getCorrectMessageFormat(thePath).format(args));
                if (isThisPlayersTurn()) {
                    trackController.sendPath(thePath);
                }
            }
        }
    }

    /**
	 * This routine will determine the reason for no car movement this turn
	 * and return the correct MessageFormat to be used in putting the chat
	 * message in the chat window.
	 */
    private MessageFormat getCorrectMessageFormat(CarTricksPath thePath) {
        if (model.wreckPlayed()) {
            return chatNoMovementWreckFormat;
        }
        if (model.onlyEventsPlayed()) {
            return chatNoMovementAllEventFormat;
        }
        if (thePath.pathLength() == 1) {
            return chatNoMovementBlockedFormat;
        }
        return chatSingleMoveFormat;
    }
}
