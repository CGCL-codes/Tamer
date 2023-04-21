package org.jogre.spades.client;

import info.clearthought.layout.TableLayout;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import nanoxml.XMLElement;
import org.jogre.client.TableConnectionThread;
import org.jogre.client.awt.GameImages;
import org.jogre.client.awt.JogrePanel;
import org.jogre.client.awt.JogreTableFrame;
import org.jogre.common.IGameOver;
import org.jogre.common.Player;
import org.jogre.common.PlayerList;
import org.jogre.common.Table;
import org.jogre.common.comm.Comm;
import org.jogre.common.comm.CommGameOver;
import org.jogre.common.comm.CommNextPlayer;
import org.jogre.common.comm.CommStartGame;
import org.jogre.common.games.Card;
import org.jogre.common.games.Deck;
import org.jogre.common.util.JogreLabels;
import org.jogre.common.util.JogreLogger;
import org.jogre.spades.common.CommSpadesHandOver;
import org.jogre.spades.common.CommSpadesMakeBid;
import org.jogre.spades.common.CommSpadesPlayCard;
import org.jogre.spades.common.CommSpadesRoundOver;

/**
 * Spades table frame.
 *
 * @author Garrett Lehman (Gman)
 * @version Alpha 0.2.3
 */
public class SpadesTableFrame extends JogreTableFrame {

    private SpadesModel model = null;

    private SpadesBoardComponent boardComponent = null;

    private SpadesBiddingComponent biddingComponent = null;

    private SpadesPlayerHandComponent playerComponent = null;

    private SpadesPlayerComponent[] playerComponents = null;

    private SpadesScoreAndBagsTableModel scoreAndBagModel = null;

    private JTable scoreAndBagList = null;

    private JScrollPane scoreAndBagListScrollPane = null;

    private SpadesController controller = null;

    private JogreLogger logger = new JogreLogger(this.getClass());

    /**
	 * Contructor for the spades table frame
	 *
	 * @param conn
	 *            Connection
	 */
    public SpadesTableFrame(TableConnectionThread conn) {
        super(conn);
        this.model = new SpadesModel();
        this.boardComponent = new SpadesBoardComponent(model);
        this.biddingComponent = new SpadesBiddingComponent();
        this.playerComponent = new SpadesPlayerHandComponent(model);
        this.playerComponents = new SpadesPlayerComponent[4];
        for (int i = 0; i < 4; i++) this.playerComponents[i] = new SpadesPlayerComponent(model);
        Object[][] cellData = { { "0 (0)", "0 (0)" } };
        String[] columnNames = { "Team 1", "Team 2" };
        scoreAndBagModel = new SpadesScoreAndBagsTableModel(cellData, columnNames);
        scoreAndBagList = new JTable(scoreAndBagModel);
        scoreAndBagListScrollPane = new JScrollPane(scoreAndBagList);
        scoreAndBagList.setPreferredScrollableViewportSize(new Dimension(150, 150));
        scoreAndBagList.setCellSelectionEnabled(false);
        JTableHeader header = scoreAndBagList.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        this.model.addObserver(playerComponent);
        this.model.addObserver(boardComponent);
        for (int i = 0; i < 4; i++) this.model.addObserver(playerComponents[i]);
        this.controller = new SpadesController(model, playerComponent, boardComponent, scoreAndBagModel);
        this.controller.setConnection(conn);
        this.boardComponent.setController(controller);
        this.biddingComponent.setController(controller);
        this.playerComponent.setController(controller);
        setupMVC(model, playerComponent, controller);
        int playerComponentHeight = Card.CARD_PIXEL_HEIGHT + Card.CARD_SPACING + 20;
        double pref = TableLayout.PREFERRED;
        double[][] size = { { 5, pref, 5, pref, 5, pref, 5 }, { 5, pref, 5, pref, 5, 50, playerComponentHeight, 5 } };
        JogrePanel gamePanel = new JogrePanel(size);
        gamePanel.add(playerComponent, "3,6,c,c");
        gamePanel.add(boardComponent, "3,3,c,c");
        gamePanel.add(playerComponents[0], "3,5,c,c");
        gamePanel.add(playerComponents[1], "1,3,c,c");
        gamePanel.add(playerComponents[2], "3,1,c,c");
        gamePanel.add(playerComponents[3], "5,3,c,c");
        gamePanel.add(biddingComponent, "5,5,5,6,c,t");
        gamePanel.add(scoreAndBagListScrollPane, "1,5,1,6,c,t");
        setGamePanel(gamePanel);
        pack();
    }

    /**
	 * Recieve the table message.
	 *
	 * @see org.jogre.client.ITable#receiveTableMessage(nanoxml.XMLElement)
	 */
    public void receiveMessage(XMLElement message) {
        String messageType = message.getName();
        logger.debug("receiveMessage", "(" + this.conn.getUsername() + ") receive: " + messageType);
        if (messageType.equals(Comm.JOIN_TABLE)) {
            updatePlayerComponents(null);
        } else if (messageType.equals(Comm.PLAYER_STATE)) {
            updatePlayerComponents(null);
        } else if (messageType.equals(Comm.START_GAME)) {
            this.model.setSeatIndexes(this.controller.getSeatNum());
            CommStartGame startGame = new CommStartGame(message);
            String username = startGame.getUsername();
            updatePlayerComponents(username);
        } else if (messageType.equals(Comm.NEXT_PLAYER)) {
            CommNextPlayer nextPlayer = new CommNextPlayer(message);
            String username = nextPlayer.getUsername();
            updatePlayerComponents(username);
            updateBiddingComponent();
        } else if (messageType.equals(Deck.XML_NAME)) {
            Deck hand = new Deck(message);
            model.setHand(hand);
            model.setGameState(SpadesModel.GAME_STATE_BIDDING);
            updateBiddingComponent();
        } else if (messageType.equals(CommSpadesPlayCard.XML_NAME)) {
            CommSpadesPlayCard playCard = new CommSpadesPlayCard(message);
            int seatNum = controller.getSeatNum(playCard.getUsername());
            model.playCardOnTable(seatNum, playCard.getCard());
        } else if (messageType.equals(CommSpadesMakeBid.XML_NAME)) {
            CommSpadesMakeBid makeBid = new CommSpadesMakeBid(message);
            int seatNum = controller.getSeatNum(makeBid.getUsername());
            model.makeBidOnTable(seatNum, makeBid.getBid());
        } else if (messageType.equals(CommSpadesHandOver.XML_NAME)) {
            CommSpadesHandOver handOver = new CommSpadesHandOver(message);
            int seatNum = handOver.getTrick();
            int tricks = this.model.getTricks(seatNum) + 1;
            model.makeTrickOnTable(seatNum, tricks);
            model.resetHand();
        } else if (messageType.equals(CommSpadesRoundOver.XML_NAME)) {
            CommSpadesRoundOver roundOver = new CommSpadesRoundOver(message);
            int[] bags = new int[2];
            int[] scores = new int[2];
            scores[0] = roundOver.getTeam1Points();
            scores[1] = roundOver.getTeam2Points();
            bags[0] = roundOver.getTeam1Bags();
            bags[1] = roundOver.getTeam2Bags();
            getMessageComponent().receiveMessage("Round Over", "Team 1 scored " + scores[0] + " with " + bags[0] + " bags.");
            getMessageComponent().receiveMessage("Round Over", "Team 2 scored " + scores[1] + " with " + bags[1] + " bags.");
            this.controller.setRoundScoreAndBags(scores, bags);
            CommGameOver gameOver = roundOver.getGameOver();
            if (gameOver == null) this.controller.requestHand(); else gameOver(gameOver);
        }
    }

    /**
	 * Update player components. If username is provided, then set the turn to
	 * that user.
	 *
	 * @param username
	 *            Username
	 */
    public void updatePlayerComponents(String username) {
        int index = 0;
        PlayerList players = table.getPlayerList();
        for (int i = 0; i < 4; i++) {
            Player player = players.getPlayer(i);
            index = i;
            if (this.model.seatIndexesSet()) index = this.model.getSeatIndex(i);
            this.playerComponents[index].setPlayer(player);
            if (username != null) {
                if (player.getPlayerName().equals(username)) this.playerComponents[index].setTurn(true); else this.playerComponents[index].setTurn(false);
            }
        }
    }

    public void updateBiddingComponent() {
        if (controller.isThisPlayersTurn() && model.getGameState() == SpadesModel.GAME_STATE_BIDDING) {
            int team = 0;
            int otherTeam = 1;
            int seatNum = this.controller.getSeatNum();
            if (seatNum == 1 || seatNum == 3) {
                team = 1;
                otherTeam = 0;
            }
            int[] score = this.model.getScore();
            if ((score[team] - score[otherTeam]) >= 100) this.biddingComponent.toggleButtons(SpadesBiddingComponent.BIDDING_STATE_BEHIND_BY_100_POINTS); else {
                this.controller.turnHandOver(true);
                this.biddingComponent.toggleButtons(SpadesBiddingComponent.BIDDING_STATE_NORMAL);
            }
        }
    }

    public void startGame() {
        if (this.controller.isThisPlayersTurn()) this.updatePlayerComponents(this.controller.getCurrentPlayer());
    }

    /**
	 * Changed functionality to the game over method of the JogreTableFrame.
	 * This method changes the message that displays at the end of a game by
	 * using "team" terms instead of "player" terms.
	 *
	 * @see org.jogre.client.awt.JogreTableFrame#gameOver(org.jogre.common.comm.CommGameOver)
	 */
    public void gameOver(CommGameOver commGameOver) {
        JogreLabels labels = JogreLabels.getInstance();
        String message = null;
        String team = "Team 1";
        int seatNum = this.controller.getSeatNum();
        if (seatNum == 1 || seatNum == 3) team = "Team 2";
        int status = commGameOver.getStatus();
        if (status == IGameOver.DRAW) message = labels.get("game.ends.in.a.draw") + "."; else if (status == IGameOver.LOSE) message = team + " " + labels.get("loses.this.game") + "."; else if (status == IGameOver.WIN) message = team + " " + labels.get("wins.the.game") + ".";
        Table table = conn.getTableList().getTable(tableNum);
        table.gameOver();
        getMessageComponent().receiveMessage(labels.get("game.over"), message.toString());
        updatePlayerComponents(null);
    }
}
