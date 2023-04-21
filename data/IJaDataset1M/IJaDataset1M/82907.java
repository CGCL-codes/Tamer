package jsyntaxpane.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;
import jsyntaxpane.SyntaxDocument;

/**
 * Finder class.  This class contains the general Find, Find Next,
 * Find Previous, and the Find Marker Actions.
 * 
 * Note that all Actions are subclasses of this class because all actions
 * require the find text to be shared among them.  This is the best approach
 * to have all Action classes share this same data.
 *
 * @author Ayman Al-Sairafi
 */
public class FindReplaceAction extends DefaultSyntaxAction {

    public FindReplaceAction() {
        super("FIND_REPLACE");
    }

    @Override
    public void actionPerformed(JTextComponent target, SyntaxDocument sdoc, int dot, ActionEvent e) {
        DocumentSearchData dsd = DocumentSearchData.getFromEditor(target);
        dsd.showReplaceDialog(target);
    }
}
