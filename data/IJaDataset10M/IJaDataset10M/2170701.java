package de.iritgo.openmetix.core.gui.swing;

import de.iritgo.openmetix.core.Engine;
import javax.swing.JRadioButton;

/**
 * IRadioButton is an extended JRadioButton that loads it's labels from the
 * application resources.
 *
 * @version $Id: IRadioButton.java,v 1.1 2005/04/24 18:10:43 grappendorf Exp $
 */
public class IRadioButton extends JRadioButton {

    /** If true, a ':' is appended to the label. */
    private boolean colon = false;

    /**
	 * Create a radio button with no text or icon.
	 */
    public IRadioButton() {
    }

    /**
	 * Create a radio button with text.
	 *
	 * @param text The text of the label.
	 */
    public IRadioButton(String textKey) {
        super(textKey);
    }

    /**
	 * Set the radio button text.
	 *
	 * @param textKey The text specified by a resource key.
	 */
    public void setText(String textKey) {
        super.setText(Engine.instance().getResourceService().getStringWithoutException(textKey) + (colon ? ":" : ""));
    }

    /**
	 * Determine wether a colon should be appended to the label.
	 *
	 * @param colon If true a colon is appended to the label.
	 */
    public void setColon(boolean colon) {
        this.colon = colon;
    }
}
