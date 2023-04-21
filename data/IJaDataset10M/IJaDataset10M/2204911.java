package de.kopis.jusenet.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import de.kopis.jusenet.Application;

public class ExitAction extends AbstractAction {

    public ExitAction() {
        super("Exit");
        putValue(Action.LONG_DESCRIPTION, "Exit the application");
        putValue(Action.SHORT_DESCRIPTION, "Exit");
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
        putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/images/general/Exit16.gif")));
    }

    public void actionPerformed(ActionEvent e) {
        Application.getInstance().quit(0);
    }
}
