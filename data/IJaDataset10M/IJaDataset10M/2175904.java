package org.j4me.examples.ui.components;

import org.j4me.logging.*;
import org.j4me.ui.*;
import org.j4me.ui.components.*;

/**
 * Example of a <code>RadioButton</code> component.
 */
public class RadioButtonExample extends Dialog {

    /**
	 * The previous screen.
	 */
    private DeviceScreen previous;

    /**
	 * The radio button.
	 */
    private RadioButton button;

    /**
	 * Constructs a screen that shows a <code>RadioButton</code> component in action.
	 * 
	 * @param previous is the screen to return to once this done.
	 */
    public RadioButtonExample(DeviceScreen previous) {
        this.previous = previous;
        setTitle("RadioButton Example");
        setMenuText("Back", null);
        button = new RadioButton();
        button.setLabel("Log Levels");
        button.append(Level.DEBUG.toString());
        button.append(Level.INFO.toString());
        button.append(Level.WARN.toString());
        button.append(Level.ERROR.toString());
        button.append(Level.OFF.toString());
        button.setSelectedIndex(1);
        append(button);
    }

    /**
	 * Takes the user to the previous screen.
	 */
    protected void declineNotify() {
        previous.show();
    }
}
