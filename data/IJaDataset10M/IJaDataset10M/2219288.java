package org.slizardo.madcommander.actions.mark;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.slizardo.madcommander.MainGUI;
import org.slizardo.madcommander.components.filelisting.FileListing;
import org.slizardo.madcommander.resources.languages.Translator;

class UnselectAllAction extends AbstractAction {

    public UnselectAllAction() {
        super(Translator.text("Unselect_all"));
    }

    public void actionPerformed(ActionEvent event) {
        FileListing listing = MainGUI.app.getSource();
        listing.unselectAll();
    }
}
