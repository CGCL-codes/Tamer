package tetris;

import java.awt.event.*;
import tetris.Screen.NotAvailablePlaceForPieceException;
import tetris.Screen.OutOfScreenBoundsException;

/**
 * This class was designed to manage the keys of the interface with the user 
 * and to determine the movements that must be executed in according to the user
 * action.
 * @author gustavo
 */
public abstract class Controller implements KeyListener, MouseMotionListener, MouseListener {

    protected int keyPause, keyGoLeft, keyGoRight, keyGoDown, keyRotate, keyDown, keyHold;

    public static final Integer keysStart[] = { KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_SPACE, KeyEvent.VK_H, KeyEvent.VK_P };

    private boolean mouseController = false;

    public Controller() {
        keyPause = KeyEvent.VK_P;
        keyGoLeft = KeyEvent.VK_LEFT;
        keyGoRight = KeyEvent.VK_RIGHT;
        keyDown = KeyEvent.VK_DOWN;
        keyGoDown = KeyEvent.VK_UP;
        keyRotate = KeyEvent.VK_SPACE;
        keyHold = KeyEvent.VK_H;
    }

    /**
     * Default setter to the class to the attributes <em>keyGoLeft</em>
     * em>keyGoRight</em> <em>keyGoDown</em> <em>keyRotate</em>.
     * @param newLeft defines the action 'go left' in according to the user event.
     * @param newRight defines the action 'go right' in according to the user event.
     * @param newDown defines the action 'go down' in according to the user event.
     * @param newRotate defines the action 'rotate' in according to the user event.
     */
    public void setControllers(Integer[] newControls) {
        keyGoLeft = newControls[0];
        keyDown = newControls[1];
        keyGoRight = newControls[2];
        keyGoDown = newControls[3];
        keyRotate = newControls[4];
        keyHold = newControls[5];
        keyPause = newControls[6];
    }

    /**
     * It returns the state of the mouse, if its static or moving.
     * @return true if it is moving or false if it is static. 
     */
    public boolean isMouseController() {
        return mouseController;
    }

    /**
     * Default setter of the parameter <em>mouseControler</em> in according to
     * the new condition.
     * @param newMouseController defines the new state of the mouse. 
     */
    public void setMouseController(boolean newMouseController) {
        mouseController = newMouseController;
    }

    /**
     * Classes to be overwrite, that are used but not implemented in this class
     * Controller.java.
     */
    public abstract void rotate();

    public abstract void goDown() throws OutOfScreenBoundsException, NotAvailablePlaceForPieceException;

    public abstract void goToBottom() throws OutOfScreenBoundsException, NotAvailablePlaceForPieceException;

    public abstract void goLeft() throws OutOfScreenBoundsException, NotAvailablePlaceForPieceException;

    public abstract void goRight() throws OutOfScreenBoundsException, NotAvailablePlaceForPieceException;

    public abstract void stopToggle() throws OutOfScreenBoundsException, NotAvailablePlaceForPieceException;

    public abstract void hold() throws OutOfScreenBoundsException, NotAvailablePlaceForPieceException;

    protected abstract void goToX(int newX) throws OutOfScreenBoundsException, NotAvailablePlaceForPieceException;

    /**
     * Listeners functions, they check the action that must be executed in according 
     * to the key pressed by the user.
     * @param e verifies if there is an user action.
     */
    public void keyPressed(KeyEvent e) {
        int keyUsed = e.getKeyCode();
        try {
            if (keyUsed == keyPause) {
                stopToggle();
            }
        } catch (Exception ex) {
        }
        try {
            if (keyUsed == keyGoLeft && mouseController == false) {
                goLeft();
            }
        } catch (Exception ex) {
        }
        try {
            if (keyUsed == keyGoRight && mouseController == false) {
                goRight();
            }
        } catch (Exception ex) {
        }
        try {
            if (keyUsed == keyGoDown && mouseController == false) {
                goToBottom();
            }
        } catch (Exception ex) {
        }
        try {
            if (keyUsed == keyDown && mouseController == false) {
                goDown();
            }
        } catch (Exception ex) {
        }
        try {
            if (keyUsed == keyRotate && mouseController == false) {
                rotate();
            }
        } catch (Exception ex) {
        }
        try {
            if (keyUsed == keyHold && mouseController == false) {
                hold();
            }
        } catch (Exception ex) {
        }
        Main.getInstance().terminateControllerAction();
    }

    /**
     * It determines the movement of translation that will be executed in 
     * accoding to the action produced by the user.
     * @param e verifies if there is an user action.
     */
    public void mouseMoved(MouseEvent e) {
        if (mouseController) {
            try {
                goToX(e.getX());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Main.getInstance().terminateControllerAction();
    }

    /**
     * It determines if the movement of rotation will be executed in according 
     * to the action produced by the user.
     * @param e verifies if there is an user action.
     */
    public void mouseClicked(MouseEvent e) {
        if (mouseController) {
            try {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    rotate();
                }
                if (e.getButton() == MouseEvent.BUTTON2) {
                    hold();
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    goToBottom();
                }
            } catch (Exception ex) {
                System.out.println(e.getButton());
            }
        }
        Main.getInstance().terminateControllerAction();
    }

    /**
     * These methodes do not have a use in this class, but must be included 
     * because of restrictions among class comunication.
     * @param e verifies if there is an user action.
     */
    public abstract void keyTyped(KeyEvent e);

    public abstract void keyReleased(KeyEvent e);

    public abstract void mouseDragged(MouseEvent e);

    public abstract void mousePressed(MouseEvent e);

    public abstract void mouseReleased(MouseEvent e);

    public abstract void mouseEntered(MouseEvent e);

    public abstract void mouseExited(MouseEvent e);
}
