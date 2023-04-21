package codesheet.document;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IPositionUpdater;
import core.Globals;

public class DocumentPositionUpdater implements IPositionUpdater {

    public void update(DocumentEvent event) {
        if (Globals.ACTIVE_SHEET == null) return;
        Globals.ACTIVE_SHEET.getThread().documentChanged();
    }
}
