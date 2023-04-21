package org.jogre.grandPrixJumping.server;

import java.util.Vector;
import java.util.ListIterator;
import nanoxml.XMLElement;
import org.jogre.common.Table;
import org.jogre.common.IGameOver;
import org.jogre.common.JogreModel;
import org.jogre.common.PlayerList;
import org.jogre.common.comm.CommGameOver;
import org.jogre.common.comm.CommNextPlayer;
import org.jogre.common.util.JogreUtils;
import org.jogre.server.ServerConnectionThread;
import org.jogre.server.ServerController;
import org.jogre.server.ServerProperties;
import org.jogre.grandPrixJumping.common.JumpingCard;
import org.jogre.grandPrixJumping.common.CommJumpingActivate;
import org.jogre.grandPrixJumping.common.CommJumpingChooseSort;
import org.jogre.grandPrixJumping.common.CommJumpingDone;
import org.jogre.grandPrixJumping.common.CommJumpingModifyFence;
import org.jogre.grandPrixJumping.common.CommJumpingMoveCards;
import org.jogre.grandPrixJumping.common.CommJumpingSetShadowHorse;
import org.jogre.grandPrixJumping.common.JumpingFaultHistoryElement;

/**
 * Server controller for the Grand Prix Jumping game
 *
 * @author Richard Walter
 * @version Beta 0.3
 */
public class JumpingServerController extends ServerController {

    private JumpingCard unknownCard;

    /**
     * Constructor to create a Grand Prix Jumping Server controller.
     *
     * @param gameKey  Game key.
     */
    public JumpingServerController(String gameKey) {
        super(gameKey);
        unknownCard = new JumpingCard();
    }

    /**
	 * Create a new model when the game starts.
	 *
	 * @see org.jogre.server.ServerController#startGame(int)
	 */
    public void startGame(int tableNum) {
        Table theTable = getTable(tableNum);
        boolean openHands = ("t".equals(theTable.getProperty("openHands")));
        boolean allowEdits = ("t".equals(theTable.getProperty("allowEdits")));
        String initialLayout = theTable.getProperty("initialLayout");
        setModel(tableNum, new JumpingServerModel(openHands, allowEdits, initialLayout));
    }

    /**
	 * Handle receving messages from the clients.
	 *
	 * @param conn      Connection to a client.
	 * @param message   Message.
	 * @param tableNum  Table number of message.
	 */
    public void parseTableMessage(ServerConnectionThread conn, XMLElement message, int tableNum) {
        String messageType = message.getName();
        if (messageType.equals(CommJumpingMoveCards.XML_NAME)) {
            handleMoveCards(conn, tableNum, new CommJumpingMoveCards(message));
        } else if (messageType.equals(CommJumpingChooseSort.XML_NAME)) {
            handleChooseSort(conn, tableNum, new CommJumpingChooseSort(message));
        } else if (messageType.equals(CommJumpingDone.XML_NAME)) {
            handleDone(conn, tableNum, new CommJumpingDone(message));
        } else if (messageType.equals(CommJumpingActivate.XML_NAME)) {
            handleActivate(conn, tableNum, new CommJumpingActivate(message));
        } else if (messageType.equals(CommJumpingModifyFence.XML_NAME)) {
            handleModifyFence(conn, tableNum, new CommJumpingModifyFence(message));
        } else if (messageType.equals(CommJumpingSetShadowHorse.XML_NAME)) {
            handleSetShadowHorse(conn, tableNum, new CommJumpingSetShadowHorse(message));
        }
    }

    /**
	 * Handle the MoveCards message from a client.
	 *
	 * @param conn			Connection to a client
	 * @param tableNum		The table the client is at.
	 * @param theMoveMsg	The message requesting the cards moving.
	 */
    private void handleMoveCards(ServerConnectionThread conn, int tableNum, CommJumpingMoveCards theMoveMsg) {
        JumpingServerModel model = (JumpingServerModel) getModel(tableNum);
        String userName = theMoveMsg.getUsername();
        int moveCode = theMoveMsg.getMoveCode();
        CommJumpingMoveCards replyMsg = theMoveMsg;
        if (moveCode == CommJumpingMoveCards.SORT_LEFT_TO_RIGHT) {
            model.moveSortCard(model.LEFT_TO_RIGHT, (JumpingCard) theMoveMsg.getCards().firstElement());
        } else if (moveCode == CommJumpingMoveCards.SORT_RIGHT_TO_LEFT) {
            model.moveSortCard(model.RIGHT_TO_LEFT, (JumpingCard) theMoveMsg.getCards().firstElement());
        } else if (moveCode == CommJumpingMoveCards.HAND_TO_TRACK) {
            int seatNum = getSeatNum(userName, tableNum);
            if (seatNum == model.getCurrJumpOwner()) {
                Vector cards = theMoveMsg.getCards();
                playCards(model, cards);
                model.commitHorseMovement(seatNum, model.getCurrJumps());
                model.moveToDiscard(cards);
            }
        } else if (moveCode == CommJumpingMoveCards.DUAL_RIDER_TO_HAND) {
            int seatNum = getSeatNum(userName, tableNum);
            Vector cards = theMoveMsg.getCards();
            JumpingCard myCard = (JumpingCard) cards.get(0);
            JumpingCard hisCard = (JumpingCard) cards.get(1);
            if (!model.verifyDualRiderCards(myCard, hisCard)) {
                myCard = model.getDualRiderCard(0);
                hisCard = model.getDualRiderCard(1);
            }
            model.addCardToHand(seatNum, myCard, false);
            model.addCardToHand(1 - seatNum, hisCard, false);
            model.clearDualRiderCards();
            if (!model.isOpenHands() && !myCard.isImmediate()) {
                replyMsg = new CommJumpingMoveCards(userName, unknownCard, hisCard, CommJumpingMoveCards.DUAL_RIDER_TO_HAND);
            }
        } else if (moveCode == CommJumpingMoveCards.HAND_TO_DISCARD) {
            int seatNum = getSeatNum(userName, tableNum);
            Vector cards = theMoveMsg.getCards();
            model.removeCardsFromHand(cards, seatNum);
            model.moveToDiscard(cards);
            if (!model.isOpenHands()) {
                replyMsg = new CommJumpingMoveCards(userName, unknownCard, cards.size(), CommJumpingMoveCards.HAND_TO_DISCARD);
            }
        }
        conn.transmitToTablePlayers(userName, tableNum, replyMsg);
    }

    /**
	 * Handle the ChooseSort message from a client.
	 *
	 * @param conn			Connection to a client
	 * @param tableNum		The table the client is at.
	 * @param theChooseMsg	The message indicating the choice.
	 */
    private void handleChooseSort(ServerConnectionThread conn, int tableNum, CommJumpingChooseSort theChooseMsg) {
        JumpingServerModel model = (JumpingServerModel) getModel(tableNum);
        String userName = theChooseMsg.getUsername();
        boolean choice = theChooseMsg.getChoice();
        int seatNum = getSeatNum(userName, tableNum);
        if (model.getChooserSeatNum() == seatNum) {
            model.moveSortCardsToHand((choice ? 0 : 1), seatNum, false, false);
            model.moveSortCardsToHand((choice ? 1 : 0), (1 - seatNum), false, false);
            model.clearCurrentJumps(seatNum);
            model.changePhase(model.PLAYING_CARDS_CHOOSER);
            conn.transmitToTablePlayers(userName, tableNum, theChooseMsg);
        }
    }

    /**
	 * Handle the Done message from a client.
	 *
	 * @param conn			Connection to a client
	 * @param tableNum		The table the client is at.
	 * @param theDoneMsg	The message indicating the done button pressed
	 */
    private void handleDone(ServerConnectionThread conn, int tableNum, CommJumpingDone theDoneMsg) {
        JumpingServerModel model = (JumpingServerModel) getModel(tableNum);
        String userName = theDoneMsg.getUsername();
        if (model.isOfficialDiscardActive()) {
            model.setOfficialDiscards(-1);
        } else if (model.isSortingCards()) {
            model.changePhase(model.CHOOSING_SORTED);
        } else if (model.isChooserPlayingCards()) {
            model.clearCurrentJumps(model.getSorterSeatNum());
            if (model.numExcessCards(model.getChooserSeatNum()) > 0) {
                model.changePhase(model.CHOOSER_DISCARDING);
            } else {
                model.changePhase(model.PLAYING_CARDS_SORTER);
            }
        } else if (model.isSorterPlayingCards()) {
            if (model.numExcessCards(model.getSorterSeatNum()) > 0) {
                model.changePhase(model.SORTER_DISCARDING);
            } else {
                checkForEndOfTurn(model, model.chooserHasImmediateCards(), model.PLAY_IMM_CARDS_CHOOSER);
            }
        } else if (model.isChooserPlayingImmediate()) {
            checkForEndOfTurn(model, model.sorterHasImmediateCards(), model.PLAY_IMM_CARDS_SORTER);
        } else if (model.isSorterPlayingImmediate()) {
            checkForEndOfTurn(model, model.chooserHasImmediateCards(), model.PLAY_IMM_CARDS_CHOOSER);
        } else if (model.isChooserDiscarding()) {
            model.changePhase(model.PLAYING_CARDS_SORTER);
        } else if (model.isSorterDiscarding()) {
            checkForEndOfTurn(model, model.chooserHasImmediateCards(), model.PLAY_IMM_CARDS_CHOOSER);
        } else if (model.isCreatingTrack()) {
            model.setPlayerEditing(getSeatNum(userName, tableNum), false);
            if (!model.playerStillEditingTrack(0) && !model.playerStillEditingTrack(1)) {
                model.changePhase(model.SORTING_CARDS);
                model.setCurrentSorter(0);
            }
        }
        conn.transmitToTablePlayers(userName, tableNum, theDoneMsg);
        if (model.isSortingCards()) {
            if (model.checkGameOver()) {
                gameOver(conn, tableNum, getTable(tableNum).getPlayerList().getInGamePlayers(), model.getResultArray(), model.getScoreString(), null);
            } else {
                model.discardImmediates();
                sendNewSortingCards(conn, tableNum, userName, model);
            }
        }
    }

    /**
	 * Check to see if the end of turn is ok, and handle it.
	 *
	 * @param hasImmCards		Indicates if the "other" player has immediate cards.
	 * @param immCardNextState	If <hasImmCards> is true, then this is the state
	 *							that the model will be changed to rather than
	 *							SORTING_CARDS state.
	 */
    private void checkForEndOfTurn(JumpingServerModel model, boolean hasImmCards, int immCardNextState) {
        if (hasImmCards) {
            model.changePhase(immCardNextState);
        } else {
            model.swapChooserAndSorter();
            model.changePhase(model.SORTING_CARDS);
        }
    }

    /**
	 * Handle the Activate message from a client.
	 *
	 * @param conn			Connection to a client
	 * @param tableNum		The table the client is at.
	 * @param theActMsg		The message indicating the Activate data
	 */
    private void handleActivate(ServerConnectionThread conn, int tableNum, CommJumpingActivate theActMsg) {
        JumpingServerModel model = (JumpingServerModel) getModel(tableNum);
        String userName = theActMsg.getUsername();
        int seatNum = getSeatNum(userName, tableNum);
        CommJumpingMoveCards returnMoveMsg = null;
        CommJumpingMoveCards broadcastMoveMsg = null;
        if (theActMsg.isRibbon()) {
            JumpingCard newCard = doRibbonActivate(model, seatNum, theActMsg);
            if (newCard != null) {
                returnMoveMsg = new CommJumpingMoveCards(userName, newCard, CommJumpingMoveCards.DECK_TO_HAND);
                if (newCard.isImmediate()) {
                    broadcastMoveMsg = returnMoveMsg;
                } else {
                    broadcastMoveMsg = new CommJumpingMoveCards(userName, unknownCard, CommJumpingMoveCards.DECK_TO_HAND);
                }
            }
        } else if (theActMsg.isOfficial()) {
            doOfficialActivate(model, seatNum, theActMsg);
        } else if (theActMsg.isDualRider()) {
            Vector newCards = doDualRiderActivate(model, seatNum, theActMsg);
            returnMoveMsg = new CommJumpingMoveCards(userName, newCards, CommJumpingMoveCards.DECK_TO_DUAL_RIDER);
            broadcastMoveMsg = new CommJumpingMoveCards(userName, unknownCard, newCards.size(), CommJumpingMoveCards.DECK_TO_DUAL_RIDER);
        }
        conn.transmitToTablePlayers(userName, tableNum, theActMsg);
        if (returnMoveMsg != null) {
            int newDeckSize = model.getDeckSize();
            returnMoveMsg.setDeckSize(newDeckSize);
            broadcastMoveMsg.setDeckSize(newDeckSize);
            conn.transmitToTablePlayer(userName, tableNum, returnMoveMsg);
            conn.transmitToTablePlayers(userName, tableNum, (model.isOpenHands() ? returnMoveMsg : broadcastMoveMsg));
        }
    }

    /**
	 * Send a new set of sorting cards out to all players at a table
	 *
	 * @param conn			Connection to a client
	 * @param tableNum		The table the client is at.
	 * @param requesterName	The name to use for the MoveCards message generated.
	 * @param model			The model.
	 */
    private void sendNewSortingCards(ServerConnectionThread conn, int tableNum, String requesterName, JumpingServerModel model) {
        Vector newSortCards = model.getNNewCards(7);
        model.setNewSortCards(newSortCards);
        CommJumpingMoveCards moveMsg = new CommJumpingMoveCards(requesterName, newSortCards, CommJumpingMoveCards.DECK_TO_SORT);
        moveMsg.setDeckSize(model.getDeckSize());
        conn.transmitToTablePlayers(tableNum, moveMsg);
    }

    /**
	 * Play a bunch of cards into the current jump
	 */
    private boolean playCards(JumpingServerModel model, Vector cards) {
        ListIterator iter = cards.listIterator();
        while (iter.hasNext()) {
            JumpingCard card = (JumpingCard) iter.next();
            if (!model.addCardToCurrentJumps(card)) {
                System.out.println("Error: Model says can't play card " + card + " !");
                return false;
            }
        }
        return true;
    }

    /**
	 * Handle activation of a ribbon card.  There are two things a player can do with a ribbon
	 * card.  Either reduce his fault points by 1/4, or pick a new card from the deck.
	 */
    private JumpingCard doRibbonActivate(JumpingServerModel model, int seatNum, CommJumpingActivate theActMsg) {
        int half = theActMsg.getHalf();
        JumpingCard theCard = theActMsg.getCard();
        JumpingCard newCard = null;
        model.removeCardFromHand(seatNum, theCard);
        model.moveToDiscard(theCard);
        if (half == 0) {
            model.addToFaults(seatNum, -1, JumpingFaultHistoryElement.RIBBON_CARD);
        } else if (half == 1) {
            newCard = model.getNextCard();
            model.addCardToHand(seatNum, newCard, false);
        }
        return newCard;
    }

    /**
	 * Handle activation of an official card.  There are two things a player can
	 * do with an official card.  Either increase his fault points by 1, or
	 * discard half of his hand.
	 */
    private void doOfficialActivate(JumpingServerModel model, int seatNum, CommJumpingActivate theActMsg) {
        int half = theActMsg.getHalf();
        JumpingCard theCard = model.getActualImmCard(seatNum, theActMsg.getCard());
        if (half == 0) {
            model.addToFaults(seatNum, 4, JumpingFaultHistoryElement.OFFICIAL_CARD);
            theCard.setMarked(true);
        } else if (half == 1) {
            model.setOfficialDiscards(seatNum);
            theCard.setMarked(true);
        }
    }

    /**
	 * Handle activation of a Dual Rider card.
	 */
    private Vector doDualRiderActivate(JumpingServerModel model, int seatNum, CommJumpingActivate theActMsg) {
        JumpingCard theCard = model.getActualImmCard(seatNum, theActMsg.getCard());
        theCard.setMarked(true);
        Vector newCards = model.getNNewCards(2);
        model.setDualRiderCards(newCards);
        return newCards;
    }

    /**
	 * Handle the ModifyFence message from a client.
	 *
	 * Note: Fence modification messages are generated by the clients simultaneously.
	 * The server is the place where the requests get serialized.  It is possible for
	 * one player to attempt to add a fence at the sime time that the other player
	 * attempts to add a water jump that covers that space.  In that situation, the
	 * first request that the server gets will be the one honored and the other will
	 * fail.  This means that the clients *cannot* decide on their own if a modification
	 * is valid; they must wait for the server to acknowledge the modification.  Therefore,
	 * if the modification is successfull, the modification message is sent to both the
	 * opponent *and* the originating player to indicate that the modification was valid.
	 *
	 * @param conn			Connection to a client
	 * @param tableNum		The table the client is at.
	 * @param theReqMsg		The message requesting the modification.
	 */
    private void handleModifyFence(ServerConnectionThread conn, int tableNum, CommJumpingModifyFence theModifyMsg) {
        String userName = theModifyMsg.getUsername();
        JumpingServerModel model = (JumpingServerModel) getModel(tableNum);
        int seatNum = getSeatNum(userName, tableNum);
        if (model.isCreatingTrack() && model.playerStillEditingTrack(seatNum)) {
            int space = theModifyMsg.getLocation();
            if ((space > 0) && (space < model.LAST_SPACE)) {
                if (model.modifyFence(theModifyMsg.getLocation(), theModifyMsg.getDirection())) {
                    model.setPlayerEditing(1 - seatNum, true);
                    conn.transmitToTablePlayers(tableNum, theModifyMsg);
                }
            }
        }
    }

    /**
	 * Handle the SetShadowHorse message from a client.
	 *
	 * @param conn			Connection to a client
	 * @param tableNum		The table the client is at.
	 * @param theSetMsg		The message changing the shadow horse location
	 */
    private void handleSetShadowHorse(ServerConnectionThread conn, int tableNum, CommJumpingSetShadowHorse theSetMsg) {
        String userName = theSetMsg.getUsername();
        JumpingServerModel model = (JumpingServerModel) getModel(tableNum);
        int seatNum = getSeatNum(userName, tableNum);
        if (model.setShadowHorseLoc(theSetMsg.getSpace())) {
            conn.transmitToTablePlayers(userName, tableNum, theSetMsg);
        }
    }

    /**
     * The server handles game over's internally and doesn't need clients to
     * tell it when the game is over.  Therefore, this method does nothing,
     * although it is needed to implement the abstract method defined in
     * ServerController.
     *
     * @see org.jogre.server.ServerController#gameOver(org.jogre.server.ServerConnectionThread, int, int)
     */
    public void gameOver(ServerConnectionThread conn, int tableNum, int resultType) {
    }
}
