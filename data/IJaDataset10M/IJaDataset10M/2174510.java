package edu.upmc.opi.caBIG.caTIES.gate.authentication;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import edu.upmc.opi.caBIG.caTIES.client.vr.utils.UIUtilities;

public class LoginDialogTester {

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        JButton loginButton = new JButton("Test Login");
        JPanel loginButtonPanel = new JPanel();
        loginButtonPanel.add(loginButton);
        LoginDialogTestAction loginTesterAction = new LoginDialogTestAction();
        loginTesterAction.setMainFrame(mainFrame);
        loginButton.addActionListener(loginTesterAction);
        mainFrame.getContentPane().add(loginButtonPanel, BorderLayout.CENTER);
        mainFrame.setSize(new Dimension(800, 400));
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        UIUtilities.centerFrame(mainFrame);
        mainFrame.setVisible(true);
    }
}
