package com.testonica.kickelhahn.core.ui.svf;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.testonica.kickelhahn.core.formats.svf.SVFCommand;
import com.testonica.kickelhahn.core.formats.svf.SVFFormatException;

/**
 * @author Sergei Devadze
 */
class CommandCellEditor extends SVFTableCellEditor {

    protected Object parseValue(String text) {
        try {
            return SVFCommand.parseCommand(text);
        } catch (SVFFormatException e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(textField), e.getMessage(), "Invalid command entered", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
