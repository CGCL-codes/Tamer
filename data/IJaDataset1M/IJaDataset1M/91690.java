package com.cafe.serve.view.login;

import java.awt.Frame;
import java.awt.Dialog.ModalityType;
import net.sourceforge.jaulp.layout.CloseWindow;

public class LoginPanelTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.addWindowListener(new CloseWindow());
        frame.setTitle("Login Frame");
        LoginDialog loginDialog = new LoginDialog(frame, "Login Dialog", ModalityType.TOOLKIT_MODAL);
        loginDialog.pack();
        loginDialog.setVisible(true);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
