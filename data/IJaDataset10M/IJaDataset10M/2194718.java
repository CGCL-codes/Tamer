package ee.ut.logic.gui;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class is a listener for button "Puhasta"
 * @author Janno Liivak
 * @version 1.2
 */
public class ClearButtonListener implements ActionListener {

    /** applet */
    Applet logic;

    /** input location */
    TextArea inputArea;

    /** output location */
    TextArea outputArea;

    /**
	 * Constructs listener for button "Puhasta"
	 * @param Applet
	 * @param TextArea
	 * @param TextArea
	 */
    public ClearButtonListener(Applet applet, TextArea input, TextArea output) {
        logic = applet;
        inputArea = input;
        outputArea = output;
    }

    /**
	 * What to do when a button "Puhasta" is pressed
	 * @param event is performed action event
	 */
    public void actionPerformed(ActionEvent event) {
        inputArea.setText("");
        outputArea.setText("");
    }
}
