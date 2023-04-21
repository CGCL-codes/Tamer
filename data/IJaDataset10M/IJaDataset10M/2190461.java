package xquery;

import java.awt.event.ActionEvent;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.browser.VFSBrowser;

/**
 * @author Wim Le Page
 * @author Pieter Wellens
 * @version Feb 11, 2004
 *
 */
public class FolderSelectionPanel extends SelectionPanel {

    /**
	 * @param view
	 * @param propLabel
	 */
    public FolderSelectionPanel(View view, String propLabel) {
        super(view, propLabel);
    }

    public void actionPerformed(ActionEvent event) {
        String[] selections = GUIUtilities.showVFSFileDialog(jEdit.getActiveView(), "", VFSBrowser.CHOOSE_DIRECTORY_DIALOG, false);
        if (selections != null) {
            sourceField.setText(selections[0]);
            jEdit.setProperty(propLabel + ".last-source", selections[0]);
        }
    }
}
