package org.jogre.connect4.client;

import java.awt.Point;
import java.awt.event.MouseEvent;
import org.jogre.client.JogreController;
import org.jogre.common.IGameOver;
import org.jogre.common.comm.CommGameOver;

/**
 * Controller for a game of connect 4.
 *
 * @author Bob Marks
 * @version Alpha 0.2.3
 */
public class Connect4Controller extends JogreController {

    protected Connect4Model model;

    protected Connect4BoardComponent boardComponent;

    /**
	 * Connect 4 controller.
	 * 
	 * @param model           Connect 4 model.
	 * @param boardComponent  Connect 4 visual board component.
	 */
    public Connect4Controller(Connect4Model model, Connect4BoardComponent boardComponent) {
        super(model, boardComponent);
        this.model = model;
        this.boardComponent = boardComponent;
    }

    /**
	 * @see org.jogre.common.JogreModel#start()
	 */
    public void start() {
        model.reset();
    }

    public void mousePressed(MouseEvent e) {
        if (isGamePlaying() && isThisPlayersTurn()) {
            Point board = boardComponent.getBoardCoords(e.getX(), e.getY());
            if (board.x >= 0 && board.x < Connect4Model.COLS && board.y >= 0 && board.y < Connect4Model.ROWS) {
                if (model.getData(board.x, 0) == Connect4Model.BLANK) {
                    int value = getCurrentPlayerSeatNum();
                    move(board.x, value);
                    checkGameOver();
                }
            }
        }
    }

    /** 
	 * Show the column that the mouse is on.
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
    public void mouseMoved(MouseEvent e) {
        if (isGamePlaying() && isThisPlayersTurn()) {
            Point point = boardComponent.getBoardCoords(e.getX(), e.getY());
            if (point.x != boardComponent.getCurMousePoint()) {
                if (model.getData(point.x, 0) != Connect4Model.BLANK) boardComponent.setCurMousePoint(-1); else boardComponent.setCurMousePoint(point.x);
                boardComponent.repaint();
            }
        }
    }

    public void move(int x, int value) {
        boardComponent.setCurMousePoint(-1);
        Point p = model.setData(x, value);
        nextPlayer();
        sendProperty("move", x, value);
    }

    /**
	 * Override the receive property method.
	 * 
	 * @see org.jogre.client.JogreController#receiveProperty(java.lang.String, int, int)
	 */
    public void receiveProperty(String key, int x, int value) {
        model.setData(x, value);
    }

    /**
	 * Check to see if the game is over or not.
	 */
    private void checkGameOver() {
        Point lastMove = this.model.getLastMove();
        int status = -1;
        if (model.isGameWon(getSeatNum(), lastMove.x, lastMove.y)) status = IGameOver.WIN; else if (model.isNoCellsLeft()) status = IGameOver.DRAW;
        if (status != -1 && conn != null) {
            CommGameOver gameOver = new CommGameOver(status);
            conn.send(gameOver);
        }
    }
}
