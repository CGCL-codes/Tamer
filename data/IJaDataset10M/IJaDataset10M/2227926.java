package de.hu.gralog.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import de.hu.gralog.gui.MainPad;

public class FileSaveAsAction extends AbstractAction {

    public FileSaveAsAction() {
        super("Save As...", null);
        super.putValue(SHORT_DESCRIPTION, "Save document As ...");
    }

    public void actionPerformed(ActionEvent arg0) {
        MainPad.getInstance().getDesktop().saveDocumentAs();
    }
}
