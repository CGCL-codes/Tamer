package issrg.editor2;

import javax.swing.*;
import java.awt.event.*;
import java.util.ResourceBundle;

/**
 * A component that extends a JTextField. Will restrain the user input
 * only to enter Digits and '.' so as to help to create a valid OID. 
 * When an unwanted key is pressed the input is ignored.
 *
 * @author  Christian Azzopardi
 */
public class OIDinputTextField extends JTextField implements KeyListener, FocusListener {

    public boolean isValidFormat = false;

    /**
     * The Constructor Simply adds a keyListener to the TextField.
     */
    public OIDinputTextField() {
        addKeyListener(this);
        addFocusListener(this);
    }

    /**
     * On keyTyped Event, the method will do the required validations. 
     * This will allow Digits and back Space (in order to delete any input).
     */
    public void keyTyped(KeyEvent ke) {
        char c = ke.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_PERIOD))) {
            ke.consume();
            return;
        }
        char arrayString[] = this.getText().toCharArray();
        if (arrayString.length == 0 && ke.getKeyChar() == '.') {
            ke.consume();
            return;
        }
        if (arrayString.length > 0) {
            if (arrayString[arrayString.length - 1] == '.' && ke.getKeyChar() == '.') {
                ke.consume();
                return;
            }
        }
        isValidFormat = true;
    }

    public void keyPressed(KeyEvent ke) {
    }

    public void keyReleased(KeyEvent ke) {
    }

    public void focusGained(FocusEvent fe) {
    }

    public void focusLost(FocusEvent fe) {
        char arrayString[] = this.getText().toCharArray();
        if (arrayString.length > 0) {
            if (arrayString[arrayString.length - 1] == '.') {
                isValidFormat = false;
            }
        }
    }
}
