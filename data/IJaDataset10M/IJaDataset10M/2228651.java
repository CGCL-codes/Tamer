package com.csaba.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import com.csaba.swing.MainWindow;
import com.csaba.swing.gui.GUIContext;
import com.csaba.swing.gui.login.LoginWizardModel;
import com.csaba.swing.gui.util.GUIUtil;
import com.csaba.swing.gui.wizard.WizardDialog;

@SuppressWarnings("serial")
public class LoginAction extends AbstractAction {

    public LoginAction() {
        super(MainWindow.getString("LoginAction.menuItem"));
        setEnabled(true);
        putValue(SMALL_ICON, GUIContext.getIcon("login", 16));
        putValue(LARGE_ICON_KEY, GUIContext.getIcon("login", 32));
        GUIUtil.setMnemonic(this);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final WizardDialog dialog = new WizardDialog(MainWindow.getFrame(), MainWindow.getString("LoginAction.titleLogin"), MainWindow.getString("LoginAction.description"), GUIContext.getIcon("login", 32), new LoginWizardModel());
        dialog.setVisible(true);
    }
}
