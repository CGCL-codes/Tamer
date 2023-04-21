package com.simpledata.bc.uicomponents.tools;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

/**
 * A default class for JTextArea.
 * change its color while editing
 */
public abstract class JTextAreaBC extends JTextArea implements JTextComponentBC {

    protected JTextAreaBC() {
        JTextFieldBC.addDefaultListeners(this, false, false);
    }

    /**
	 * @see com.simpledata.bc.uicomponents.tools.JTextComponentBC#getJTextComponent()
	 */
    public JTextComponent getJTextComponent() {
        return this;
    }
}
