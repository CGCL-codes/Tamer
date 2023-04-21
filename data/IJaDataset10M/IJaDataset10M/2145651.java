package de.javatt.data.scenario.gui;

import java.awt.AWTException;

/**
 * KeyInput simulates a user typing a text on keyboard.
 * 
 * @author Matthias Kempa
 *  
 */
public class KeyInput extends KeyRobot {

    private static final long serialVersionUID = 1;

    /**
     * The text that shall be typed.
     */
    private String keyInputText;

    public KeyInput() {
        super();
    }

    /**
    * Sets the text that shall be typed. This text can contain key commands.
    * @param str
    */
    public void setInputText(String str) {
        keyInputText = str;
    }

    public void testRun() {
        try {
            if (keyInputText != null) {
                keyPress(keyInputText);
                logInfo("Text typed: " + keyInputText);
            }
        } catch (AWTException awtEx) {
            logError(awtEx.getMessage());
        }
    }
}
