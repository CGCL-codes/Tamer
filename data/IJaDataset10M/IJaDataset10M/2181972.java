package org.pwsafe.passwordsafej;

import javax.swing.JButton;

/**
 *
 * @author Kevin Preece
 */
public class OkButton extends JButton {

    /**
	 * 
	 */
    public OkButton() {
        super();
        setText(I18nHelper.getInstance().formatMessage("button.ok"));
    }
}
