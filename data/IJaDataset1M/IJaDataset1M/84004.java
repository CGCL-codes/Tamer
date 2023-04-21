package org.jogre.rotary.client;

import nanoxml.XMLElement;
import java.awt.event.MouseEvent;
import java.awt.Point;
import org.jogre.rotary.common.RotaryModel;
import org.jogre.rotary.common.RotaryMoveResult;
import org.jogre.rotary.common.RotaryPiece;
import org.jogre.rotary.common.CommRotaryMove;
import org.jogre.client.JogreController;
import org.jogre.common.IGameOver;
import org.jogre.common.comm.CommGameOver;

/**
 * Controller for the rotary game.
 *
 * @author  Richard Walter
 * @version Beta 0.3
 */
public class RotaryController extends JogreController {

    protected RotaryModel model;

    protected RotaryComponent component;

    protected int currentPhase = PHASE_SELECTING;

    protected static final int PHASE_SELECTING = 0;

    protected static final int PHASE_MOVING = 1;

    protected static final int PHASE_ROTATING = 2;

    /**
	 * Default constructor for the rotary controller which takes a
	 * model and a view.
	 *
	 * @param rotaryModel      Rotary model.
	 * @param rotaryComponent  Rotary view.
	 */
    public RotaryController(RotaryModel model, RotaryComponent component) {
        super(model, component);
        this.model = model;
        this.component = component;
    }

    /**
	 * Start method which restarts the model.
	 *
	 * @see org.jogre.common.JogreModel#start()
	 */
    public void start() {
        model.reset();
        currentPhase = PHASE_SELECTING;
    }

    /**
	 * Implementation of the mouse moved interface.
	 *
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
    public void mouseMoved(MouseEvent e) {
        boolean redrawNeeded = false;
        if (isGamePlaying() && isThisPlayersTurn()) {
            if (currentPhase == PHASE_SELECTING) {
                Point boardPoint = component.getBoardCoords(e.getX(), e.getY());
                RotaryPiece piece = model.getBoardPiece(boardPoint.x, boardPoint.y);
                boolean movable = ((piece != null) && (piece.getOwner() == getCurrentPlayerSeatNum()) && (piece.hasValidMoves()));
                redrawNeeded = component.setMouseMovePoint(boardPoint, movable);
            } else if (currentPhase == PHASE_ROTATING) {
                int rotationValue = component.getRotationValue(e.getX(), e.getY());
                RotaryPiece activePiece = component.getActivePiece();
                redrawNeeded = activePiece.setRotation(rotationValue);
            }
            if (redrawNeeded) {
                component.repaint();
            }
        }
    }

    /**
	 * Implementation of the mouse pressed interface.
	 *
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
    public void mousePressed(MouseEvent e) {
        if (isGamePlaying() && isThisPlayersTurn()) {
            if (currentPhase == PHASE_SELECTING) {
                Point boardPoint = component.getBoardCoords(e.getX(), e.getY());
                RotaryPiece piece = model.getBoardPiece(boardPoint.x, boardPoint.y);
                boolean movable = ((piece != null) && (piece.getOwner() == getCurrentPlayerSeatNum()) && (piece.hasValidMoves()));
                if (movable) {
                    component.setActivePiece(piece);
                    mouseDragged(e);
                }
            }
        }
    }

    /**
	 * Implementation of the mouse released interface.
	 *
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
    public void mouseReleased(MouseEvent e) {
        if (isGamePlaying() && isThisPlayersTurn()) {
            if (currentPhase == PHASE_SELECTING) {
                component.setActiveResult(e.getX(), e.getY());
                RotaryMoveResult result = component.getActiveResult();
                if ((result == null) || ((result.getDistance() == 0) && !component.getActiveResultInIconArea())) {
                    component.setActivePiece(null);
                    component.repaint();
                    return;
                }
                int postMoveAction = result.getPostMoveAction();
                boolean doesPromote = (postMoveAction == RotaryMoveResult.ACTION_MUST_PROMOTE) || ((postMoveAction == RotaryMoveResult.ACTION_MAY_PROMOTE) && component.getActiveResultInIconArea());
                result.setActualPromotion(doesPromote);
                model.executeMove(result);
                if (doesPromote || ((postMoveAction == RotaryMoveResult.ACTION_ROTATE) && component.getActiveResultInIconArea())) {
                    currentPhase = PHASE_ROTATING;
                    component.setRotationMode(true);
                } else {
                    sendMoveToServer(result);
                }
            } else {
                RotaryMoveResult result = component.getActiveResult();
                if (result.isCurrentRotationLegal()) {
                    sendMoveToServer(result);
                } else {
                    currentPhase = PHASE_SELECTING;
                    component.setActivePiece(null);
                    component.setRotationMode(false);
                    component.repaint();
                    return;
                }
            }
        }
    }

    /**
	 * Implementation of the mouse dragged interface.
	 *
	 * @see java.awt.event.MouseMotionListener#mouseReleased(java.awt.event.MouseEvent)
	 */
    public void mouseDragged(MouseEvent e) {
        if (isGamePlaying() && isThisPlayersTurn()) {
            if (component.setActiveResult(e.getX(), e.getY())) {
                component.repaint();
            }
        }
    }

    /**
	 * Receive a message from the server that indicates the other player's
	 * move.
	 *
	 * @param message    The message from the server
	 * @see org.jogre.client.JogreController#receiveObject(nanoxml.XMLElement)
	 */
    public void receiveObject(XMLElement message) {
        String messageType = message.getName();
        if (messageType.equals(CommRotaryMove.XML_NAME)) {
            CommRotaryMove theMove = new CommRotaryMove(message);
            model.executeMove(theMove.getFromCol(), theMove.getFrowRow(), theMove.getDistance(), theMove.getDirection(), theMove.getEndRotation(), theMove.getDoesPromote());
            if (model.updateLegalMoves(getSeatNum()) == 0) {
                conn.send(new CommGameOver(IGameOver.LOSE));
            }
        }
    }

    private void sendMoveToServer(RotaryMoveResult moveResult) {
        sendObject(new CommRotaryMove(conn.getUsername(), moveResult).flatten());
        component.setActivePiece(null);
        component.setRotationMode(false);
        currentPhase = PHASE_SELECTING;
        component.repaint();
        nextPlayer();
    }
}
