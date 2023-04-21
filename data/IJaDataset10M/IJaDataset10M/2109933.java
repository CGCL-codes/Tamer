package org.jogre.quetris.client;

import java.util.Vector;
import nanoxml.XMLElement;
import org.jogre.common.JogreModel;
import org.jogre.common.comm.Comm;

/**
 * The Quetris model which contains 2 QuetrisPlayerModel's and accessor's
 * for each object.  
 * 
 * @author  Bob Marks
 * @version Beta 0.3
 */
public class QuetrisModel extends JogreModel {

    public static final int MAX_PLAYERS = 2;

    public static final int NO_PLAYER = -1;

    private QuetrisPlayerModel[] playerModels;

    private int seatNum = NO_PLAYER;

    /**
	 * Default constructor for the game
	 */
    public QuetrisModel() {
        playerModels = new QuetrisPlayerModel[MAX_PLAYERS];
        for (int i = 0; i < MAX_PLAYERS; i++) {
            playerModels[i] = new QuetrisPlayerModel(i);
        }
    }

    /**
	 * Starts the game.  The model must be passed in the player
	 * number.
	 * 
	 * @param player
	 */
    public void start(int player) {
        this.seatNum = player;
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (player == i) playerModels[i].start(); else playerModels[i].wipeAll();
        }
    }

    /**
	 * Return the main grid data;
	 * 
	 * @return  Main grid data.
	 */
    public QuetrisPlayerModel getPlayerModel(int player) {
        return playerModels[player];
    }

    /**
     * Return the player num.
     * 
     * @return
     */
    public int getSeatNum() {
        return seatNum;
    }

    /**
     * Set the player num.
     * 
     * @param player
     */
    public void setSeatNum(int player) {
        this.seatNum = player;
    }

    /** This method returns true if the current shape will fit on the screen
	 * @return
	 */
    public boolean shapeFits() {
        return true;
    }

    /** 
	 * This method moves a shape left.
	 */
    public void moveShapeLeft() {
        getPlayerModel(seatNum).moveShapeLeft();
    }

    /** 
	 * This method moves a shape right.
	 * 
	 * @return Returns false if the shape can't move
	 */
    public void moveShapeRight() {
        getPlayerModel(seatNum).moveShapeRight();
    }

    /** 
	 * Move shape clockwise.
	 */
    public void moveShapeCW() {
        getPlayerModel(seatNum).moveShapeCW();
    }

    /** 
	 * Move shape anti clockwise.
	 */
    public void moveShapeACW() {
        getPlayerModel(seatNum).moveShapeACW();
    }

    /**
	 * Flip vertically
	 */
    public void flipShapeVertically() {
        getPlayerModel(seatNum).flipShapeVertically();
    }

    /**
	 * Flip horizontally.
	 */
    public void flipShapeHorizontally() {
        getPlayerModel(seatNum).flipShapeHorizontally();
    }

    public XMLElement flatten() {
        XMLElement state = new XMLElement(Comm.MODEL);
        for (int i = 0; i < 2; i++) state.addChild(getPlayerModel(i).flatten());
        return state;
    }

    public void setState(XMLElement state) {
        Vector childElms = state.getChildren();
        for (int i = 0; i < childElms.size(); i++) {
            getPlayerModel(i).setState((XMLElement) childElms.get(i));
            getPlayerModel(i).refreshObservers();
        }
    }
}
