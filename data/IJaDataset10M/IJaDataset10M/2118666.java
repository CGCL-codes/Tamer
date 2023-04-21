package org.jogre.checkers.client;

import java.awt.Point;
import java.awt.event.MouseEvent;
import nanoxml.XMLElement;
import org.jogre.checkers.common.CommCheckersMove;
import org.jogre.client.JogreController;
import org.jogre.client.awt.JogreComponent;
import org.jogre.common.IGameOver;
import org.jogre.common.comm.CommGameOver;
import org.jogre.common.util.JogreUtils;

/**
 * @author  Bob Marks
 * @version Alpha 0.2.3
 *
 * Controller for the checkers game.
 */
public class CheckersController extends JogreController {

    protected CheckersModel checkersModel;

    protected CheckersBoardComponent boardComponent;

    /**
	 * @param gameModel
	 * @param boardComponent
	 */
    public CheckersController(CheckersModel checkersModel, CheckersBoardComponent boardComponent) {
        super(checkersModel, (JogreComponent) boardComponent);
        this.checkersModel = checkersModel;
        this.boardComponent = boardComponent;
    }

    /**
	 * @see org.jogre.common.JogreModel#start()
	 */
    public void start() {
        checkersModel.reset();
    }

    /**
	 * Implementation of the mouse pressed interface.  If the correct player is
	 * trying to lift the correct colour piece then take a note of its pressed
	 * board co-ordinate (0..7, 0..7).
	 *
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    public void mousePressed(MouseEvent e) {
        if (isGamePlaying() && isThisPlayersTurn()) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            boardComponent.setPressedPoint(CheckersBoardComponent.OFF_SCREEN_POINT);
            int width = boardComponent.getWidth();
            int height = boardComponent.getHeight();
            int cellSpacing = boardComponent.getCellSpacing();
            if (mouseX >= cellSpacing && mouseX < width && mouseY >= cellSpacing && mouseY < height) {
                Point pressedPoint = boardComponent.getBoardCoords(mouseX, mouseY);
                boardComponent.setPressedPoint(pressedPoint);
                checkersModel.updateAllowedMoves(pressedPoint.x, pressedPoint.y);
                if (pressedPoint.x >= 0 && pressedPoint.x < 8 && pressedPoint.y >= 0 && pressedPoint.y < 8) {
                    int checkersPieceColour = checkersModel.getPieceColour(pressedPoint.x, pressedPoint.y);
                    if (checkersModel.getPiece(pressedPoint.x, pressedPoint.y) != ICheckersModel.EMPTY && checkersPieceColour == getCurrentPlayerSeatNum()) {
                        boardComponent.setDragPoint(pressedPoint);
                        boardComponent.repaint();
                        return;
                    }
                }
            }
        }
        boardComponent.setPressedPoint(CheckersBoardComponent.OFF_SCREEN_POINT);
    }

    /**
	 * Implementation of the mouse released interface.
	 *
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
    public void mouseReleased(MouseEvent e) {
        if (isGamePlaying()) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            Point releasedPoint = null;
            int size = boardComponent.getWidth();
            int cellSpacing = boardComponent.getCellSpacing();
            if (mouseX >= cellSpacing && mouseX < size && mouseY >= cellSpacing && mouseY < size) {
                releasedPoint = boardComponent.getBoardCoords(mouseX, mouseY);
                Point pressedPoint = boardComponent.getPressedPoint();
                boolean moveIsAnAttack = checkersModel.getPieceMover().isAttackingMove(pressedPoint.x, pressedPoint.y, releasedPoint.x, releasedPoint.y);
                boolean validMove = checkersModel.executeMove(pressedPoint.x, pressedPoint.y, releasedPoint.x, releasedPoint.y);
                if (conn != null && validMove) {
                    int index1 = checkersModel.getBoardIndex(pressedPoint.x, pressedPoint.y);
                    int index2 = checkersModel.getBoardIndex(releasedPoint.x, releasedPoint.y);
                    CommCheckersMove commMove = new CommCheckersMove(index1, index2);
                    sendObject(commMove);
                    CheckersPieceMover pieceMover = checkersModel.getPieceMover();
                    int numOfAttackingMoves = pieceMover.countPossibleAttackingMoves(getSeatNum());
                    if (!(moveIsAnAttack && numOfAttackingMoves != 0)) {
                        if (!checkGameOver()) {
                            nextPlayer();
                        }
                    }
                }
            }
        }
        checkersModel.resetAllowedMoves();
        boardComponent.resetPoints();
        boardComponent.repaint();
    }

    /**
	 * Receive data objects from other clients (checkers move).
	 *
	 * @see org.jogre.client.JogreController#receiveObject(nanoxml.XMLElement)
	 */
    public void receiveObject(XMLElement object) {
        if (object.getName().equals(CommCheckersMove.XML_NAME)) {
            CommCheckersMove move = new CommCheckersMove(object);
            checkersModel.executeMove(move);
        }
    }

    /**
	 * Check to see if the game is over.
	 */
    private boolean checkGameOver() {
        CheckersPieceMover pieceMover = checkersModel.getPieceMover();
        int opponent = JogreUtils.invert(getSeatNum());
        int count = pieceMover.getPlayerCount(opponent);
        if (count == 0) {
            CommGameOver commGameOver = new CommGameOver(IGameOver.WIN);
            conn.send(commGameOver);
            return true;
        }
        return false;
    }

    /**
	 * Returns the opponent player to the one who is currently playing.
	 *
	 * @return
	 */
    public int getCurrentOpponentPlayer() {
        return getCurrentPlayerSeatNum() == ICheckersModel.PLAYER_ONE ? ICheckersModel.PLAYER_TWO : ICheckersModel.PLAYER_ONE;
    }
}
