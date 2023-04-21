package org.casaburo.utils.textPopupMenu;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

class PopSelectAllAction extends BasicAction {

    public PopSelectAllAction(String text, Icon icon) {
        super(text, icon);
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl A"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        comp.selectAll();
    }

    @Override
    public boolean isEnabled() {
        return comp != null && comp.isEnabled() && comp.getText().length() > 0 && (comp.getSelectedText() == null || comp.getSelectedText().length() < comp.getText().length());
    }
}
