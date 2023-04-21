package org.sourceforge.kga.simpleGui.actions;

import java.awt.event.ActionEvent;
import org.sourceforge.kga.simpleGui.Gui;

public class SaveAs extends KgaAction {

    private static final long serialVersionUID = 1L;

    public SaveAs(Gui gui) {
        super(gui, "saveas");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getGui().saveAs();
    }
}
