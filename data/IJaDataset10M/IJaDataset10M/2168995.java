package frame;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class KeyDemo extends Applet implements KeyListener, ActionListener {

    TextArea displayArea;

    TextField typingArea;

    public void init() {
        Button button = new Button("Clear");
        button.addActionListener(this);
        typingArea = new TextField(20);
        typingArea.addKeyListener(this);
        displayArea = new TextArea(5, 20);
        displayArea.setEditable(false);
        setLayout(new BorderLayout());
        add("Center", displayArea);
        add("North", typingArea);
        add("South", button);
    }

    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
        displayInfo(e, "KEY TYPED: ");
    }

    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
        displayInfo(e, "KEY PRESSED: ");
    }

    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
        displayInfo(e, "KEY RELEASED: ");
    }

    /** Handle the button click. */
    public void actionPerformed(ActionEvent e) {
        displayArea.setText("");
        typingArea.setText("");
        typingArea.requestFocus();
    }

    protected void displayInfo(KeyEvent e, String s) {
        String charString, keyCodeString, modString, tmpString;
        char c = e.getKeyChar();
        int keyCode = e.getKeyCode();
        int modifiers = e.getModifiers();
        if (Character.isISOControl(c)) {
            charString = "key character = (an unprintable control character)";
        } else {
            charString = "key character = '" + c + "'";
        }
        keyCodeString = "key code = " + keyCode + " (" + KeyEvent.getKeyText(keyCode) + ")";
        modString = "modifiers = " + modifiers;
        tmpString = KeyEvent.getKeyModifiersText(modifiers);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no modifiers)";
        }
        displayArea.append(s + "\n    " + charString + "\n    " + keyCodeString + "\n    " + modString + "\n");
    }
}
