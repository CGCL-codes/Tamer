package org.berlin.pino.win;

import javax.swing.JTextArea;
import org.berlin.seesaw.swing.TeeterTextArea;

/**
 */
public class PinoOutputTextArea extends TeeterTextArea {

    /**
     * Constructor for PinoOutputTextArea.
     * @param textArea JTextArea
     */
    public PinoOutputTextArea(JTextArea textArea) {
        super(textArea);
    }

    /**
     * Method defaultSettings.
     * @see org.berlin.seesaw.swing.ITeeterTextArea#defaultSettings()
     */
    @Override
    public void defaultSettings() {
        this.setColumnsAndRows(70, 30);
        this.setLineWrap(false);
        this.setCaretPosition(0);
        this.setEditable(true);
    }
}
