package com.symbolicplotter.ui.actions;

import com.symbolicplotter.ui.*;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Action clears plot
 */
public class ClearAction extends AbstractAction {

    protected MainFrame parent;

    public ClearAction(MainFrame parent) {
        super("Clear");
        this.parent = parent;
        putValue(SHORT_DESCRIPTION, "Clear all graphs");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        parent.getSettings().getDataSources().clear();
        parent.getGraphPanel().repaint();
    }
}
