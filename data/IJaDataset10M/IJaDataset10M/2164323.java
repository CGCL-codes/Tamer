package de.hu.gralog.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import de.hu.gralog.gui.MainPad;

public class ShowWindowLayout extends AbstractAction {

    public ShowWindowLayout() {
        super("Show Window Layout", null);
        super.putValue(SHORT_DESCRIPTION, "Show Window Layout");
    }

    public void actionPerformed(ActionEvent e) {
        MainPad.getInstance().showWindowLayoutFrame();
    }
}
