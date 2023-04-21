package jsyntaxpane.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import jsyntaxpane.SyntaxDocument;
import jsyntaxpane.util.Configuration;

/**
 * Undo action
 */
public class UndoAction extends DefaultSyntaxAction {

    public UndoAction() {
        super("UNDO");
    }

    @Override
    public void actionPerformed(JTextComponent target, SyntaxDocument sDoc, int dot, ActionEvent e) {
        if (sDoc != null) {
            sDoc.doUndo();
        }
    }
}
