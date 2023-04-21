package com.bluebrim.layout.impl.client;

import com.bluebrim.gui.client.*;

/**
 * Dialog for displaying a list of CoTextIFs for selection.
 *
 * @author: Dennis
 */
class CoWorkPieceTextSelectionDialog extends CoWorkPieceContentIndexSelectionDialog {

    public CoWorkPieceTextSelectionDialog(CoUserInterfaceBuilder b) {
        super(b);
    }

    protected Object prepareElement(Object e) {
        com.bluebrim.text.shared.CoTextContentIF t = (com.bluebrim.text.shared.CoTextContentIF) e;
        String str = t.getTextExtract(50);
        return t.getName() + ": " + str;
    }
}
