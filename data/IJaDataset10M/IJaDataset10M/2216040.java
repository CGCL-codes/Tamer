package de.kopis.jusenet.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import de.kopis.jusenet.Application;

public class SubscribeAction extends AbstractAction {

    public SubscribeAction() {
        super("Subscribe");
        putValue(Action.LONG_DESCRIPTION, "Subscribe to group");
        putValue(Action.SHORT_DESCRIPTION, "Subscribe");
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/general/Add16.gif")));
    }

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().subscribe();
    }
}
