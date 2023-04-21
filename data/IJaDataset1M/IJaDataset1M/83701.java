package com.uglygreencar.games.seaHunter.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import com.uglygreencar.games.seaHunter.PhaseShoot;
import com.uglygreencar.games.seaHunter.SeaHunter;

public final class GameScreen extends Panel {

    private SeaHunter seaHunter;

    private GameBoard playerOneGameBoard;

    private StatisticsBoard playerOneStatisticsBoard;

    private Label playerOneLabel;

    private GameBoard playerTwoGameBoard;

    private StatisticsBoard playerTwoStatisticsBoard;

    private Label playerTwoLabel;

    private Label titleLabel;

    private Label messageLabel;

    public static final int GAME_PHASE_PLACE_SHIPS = 0;

    public static final int GAME_PHASE_SHOOT = 1;

    public static final int GAME_PHASE_PLAYER_ONE_SHOOT = 2;

    public static final int GAME_PHASE_PLAYER_TWO_SHOOT = 3;

    public static final int GAME_PHASE_GAME_OVER = 4;

    public int gamePhase = GameScreen.GAME_PHASE_PLACE_SHIPS;

    private final Rectangle player1BoardRectangle = new Rectangle(5, 35, 310, 310);

    private final Rectangle player2BoardRectangle = new Rectangle(330, 35, 310, 310);

    private DonePanel donePanel;

    public static final int SPECIAL_MESSAGE_BOOM = 0;

    public static final int SPECIAL_MESSAGE_SINKING = 1;

    public static final int SPECIAL_MESSAGE_SINKING_HQ = 2;

    /*************************************************************************************
	*
	*
	**************************************************************************************/
    public GameScreen(SeaHunter seaHunter) {
        super();
        this.seaHunter = seaHunter;
        this.setLayout(null);
        this.playerOneGameBoard = new GameBoard(this, GameBoard.PLAYER_TYPE_PLAYER_1, Color.blue);
        this.playerOneGameBoard.getVisualComponent().setBounds(this.player1BoardRectangle);
        this.playerOneLabel = new Label("Player 1");
        this.playerOneLabel.setForeground(Color.yellow);
        this.playerOneLabel.setAlignment(Label.LEFT);
        this.playerOneLabel.setBounds(new Rectangle(5, 10, 100, 20));
        this.playerTwoGameBoard = new GameBoard(this, GameBoard.PLAYER_TYPE_COMPUTER, new Color(140, 0, 0));
        this.playerTwoGameBoard.getVisualComponent().setBounds(this.player2BoardRectangle);
        this.playerTwoLabel = new Label("Computer");
        this.playerTwoLabel.setForeground(Color.yellow);
        this.playerTwoLabel.setAlignment(Label.RIGHT);
        this.playerTwoLabel.setBounds(new Rectangle(530, 10, 110, 20));
        this.titleLabel = new Label("Sea Hunter");
        this.titleLabel.setFont(new Font("Helvetica", Font.BOLD, 26));
        this.titleLabel.setForeground(Color.white);
        this.titleLabel.setAlignment(Label.CENTER);
        this.titleLabel.setBounds(new Rectangle(215, 5, 215, 25));
        this.messageLabel = new Label();
        this.messageLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));
        this.messageLabel.setForeground(Color.white);
        this.messageLabel.setAlignment(Label.CENTER);
        this.messageLabel.setBounds(new Rectangle(5, 360, 635, 25));
        this.add(this.titleLabel);
        this.add(this.playerOneLabel);
        this.add(this.playerOneGameBoard.getVisualComponent());
        this.add(this.playerTwoLabel);
        this.add(this.playerTwoGameBoard.getVisualComponent());
        this.add(this.messageLabel);
        this.setPlayerBoardEnabledFlag(1, false);
        this.setPlayerBoardEnabledFlag(2, false);
    }

    public void startGame() {
        this.setPlayerBoardEnabledFlag(2, false);
        this.setPlayerBoardEnabledFlag(1, true);
        this.playerOneGameBoard.placeShips();
    }

    /****************************************************************************************
	* Method to set status messages.
	*
	*****************************************************************************************/
    public void setStatusMessage(String message) {
        this.messageLabel.setBackground(null);
        this.messageLabel.setForeground(Color.white);
        this.messageLabel.setText(message);
    }

    public void setSpecialStatusMessage(int constantMessageType) {
        switch(constantMessageType) {
            case GameScreen.SPECIAL_MESSAGE_BOOM:
                this.messageLabel.setBackground(Color.yellow);
                this.messageLabel.setForeground(Color.red);
                this.messageLabel.setText("BOOM!!!");
                break;
            case GameScreen.SPECIAL_MESSAGE_SINKING:
                this.messageLabel.setBackground(Color.white);
                this.messageLabel.setForeground(Color.black);
                this.messageLabel.setText("S.O.S.  Ship going down!!!");
                break;
            case GameScreen.SPECIAL_MESSAGE_SINKING_HQ:
                this.messageLabel.setBackground(null);
                this.messageLabel.setForeground(Color.white);
                this.messageLabel.setText("Great shot!!!");
                break;
        }
    }

    /****************************************************************************************
	* Method to disable/enable a player's game board.
	*
	*****************************************************************************************/
    public void setPlayerBoardEnabledFlag(int player, boolean enabled) {
        if (player == 1) {
            this.playerOneGameBoard.getVisualComponent().setEnabled(enabled);
        } else {
            this.playerTwoGameBoard.getVisualComponent().setEnabled(enabled);
        }
    }

    /**************************************************************************************
	*
	*
	***************************************************************************************/
    public void setGamePhase(int phase) {
        this.gamePhase = phase;
    }

    /**************************************************************************************
	*
	*
	***************************************************************************************/
    protected int getGamePhase() {
        return this.gamePhase;
    }

    /**************************************************************************************
	*
	*
	***************************************************************************************/
    public void playerOneFinishedShipPlacement() {
        this.playerTwoGameBoard.placeShips();
        this.finishedPlacingShips();
    }

    /**************************************************************************************
	* This gets called when the computer finishes placing ships.
	*
	***************************************************************************************/
    public void finishedPlacingShips() {
        this.setGamePhase(GameScreen.GAME_PHASE_PLAYER_ONE_SHOOT);
        this.playerOneGameBoard.initPhase(GameScreen.GAME_PHASE_SHOOT);
        this.playerTwoGameBoard.initPhase(GameScreen.GAME_PHASE_SHOOT);
        this.setPlayerBoardEnabledFlag(2, true);
        this.setStatusMessage("Turn: Player 1");
    }

    /**************************************************************************************
	*
	*
	***************************************************************************************/
    public void finishedShooting() {
        this.setStatusMessage("GAME OVER");
        try {
            Thread.sleep(PhaseShoot.SLEEP_TIME_PHASE_OVER);
        } catch (InterruptedException ie) {
            System.out.println("unable to sleep!!");
        }
        this.playerOneStatisticsBoard = new StatisticsBoard(this, this.playerTwoGameBoard.getStatistics());
        this.playerOneStatisticsBoard.getVisualComponent().setBounds(this.player1BoardRectangle);
        this.playerTwoStatisticsBoard = new StatisticsBoard(this, this.playerOneGameBoard.getStatistics());
        this.playerTwoStatisticsBoard.getVisualComponent().setBounds(this.player2BoardRectangle);
        this.remove(this.playerOneGameBoard.getVisualComponent());
        this.playerOneGameBoard.getVisualComponent().setVisible(false);
        this.validate();
        this.add(this.playerOneStatisticsBoard.getVisualComponent());
        this.playerOneStatisticsBoard.getVisualComponent().setVisible(true);
        this.validate();
        this.remove(this.playerTwoGameBoard.getVisualComponent());
        this.playerTwoGameBoard.getVisualComponent().setVisible(false);
        this.validate();
        this.add(this.playerTwoStatisticsBoard.getVisualComponent());
        this.playerTwoStatisticsBoard.getVisualComponent().setVisible(true);
        this.remove(this.messageLabel);
        this.donePanel = new DonePanel(this.seaHunter);
        this.donePanel.setBounds(new Rectangle(5, 360, 635, 30));
        this.donePanel.setVisible(true);
        this.add(this.donePanel);
        this.validate();
        this.repaint();
        this.seaHunter.validate();
    }

    /**************************************************************************************
	*
	*
	***************************************************************************************/
    public GameBoard getGameBoard(byte playerType) {
        switch(playerType) {
            case GameBoard.PLAYER_TYPE_PLAYER_1:
                return this.playerOneGameBoard;
            case GameBoard.PLAYER_TYPE_COMPUTER:
            case GameBoard.PLAYER_TYPE_PLAYER_2:
                return this.playerTwoGameBoard;
            default:
                System.out.println("GameScreen.getGameBoard() was passed an unknown playerType of: " + playerType);
                return null;
        }
    }
}
