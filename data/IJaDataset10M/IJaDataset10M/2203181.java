package com.peterhi.player.action;

import java.awt.event.*;
import javax.swing.Action;
import com.peterhi.player.*;

public class AboutAction extends BaseAction {

    private static final Action instance = new AboutAction();

    public static Action getInstance() {
        return instance;
    }

    public AboutAction() {
        super();
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
    }

    public void actionPerformed(ActionEvent e) {
        AboutDialog a = Application.getWindow().getDialog(AboutDialog.class);
        a.setVisible(true);
    }
}
