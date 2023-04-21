package de.kopis.jusenet.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import de.kopis.jusenet.Application;

public class AboutAction extends AbstractAction {

    public AboutAction() {
        super("About...");
        putValue(Action.LONG_DESCRIPTION, "About this application");
        putValue(Action.SHORT_DESCRIPTION, "About...");
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/general/About16.gif")));
    }

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().about();
    }
}
