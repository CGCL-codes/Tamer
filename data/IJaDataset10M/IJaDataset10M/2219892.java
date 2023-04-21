package cz.langteacher.gui.mainwindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DoubleClickListener extends MouseAdapter {

    private DoubleClickAction action;

    public DoubleClickListener(DoubleClickAction action) {
        super();
        this.action = action;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
            action.doAction();
        }
    }
}
