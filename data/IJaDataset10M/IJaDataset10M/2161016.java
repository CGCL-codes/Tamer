package de.kopis.jusenet.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import de.kopis.jusenet.Application;

public class LoadGroupsAction extends AbstractAction {

    public LoadGroupsAction() {
        super("Load grouplist");
        putValue(Action.LONG_DESCRIPTION, "Get all groups from server");
        putValue(Action.SHORT_DESCRIPTION, "Load groups");
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F11, KeyEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/general/Import16.gif")));
    }

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().getGroups();
    }
}
