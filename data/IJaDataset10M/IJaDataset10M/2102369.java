package ops.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import ops.controller.ControllerManager;
import ops.controller.LoginController;
import ops.i18n.Resources;
import ops.util.ViewHelper;
import ops.view.forms.Form;
import ops.view.forms.component.LimitedSizeInput;
import ops.view.forms.component.PasswordInput;

public class LoginPanel extends JPanel {

    private LoginController loginController = ControllerManager.getInstance().getLoginController();

    private JButton loginButton;

    private Form form;

    public LoginPanel() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        Resources resources = Resources.getInstance();
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        loginButton = new JButton(resources.getString("login"));
        loginButton.addActionListener(new ButtonListener());
        form = new Form();
        form.addInput("user", new LimitedSizeInput(resources.getString("username") + ":", 20, true));
        form.addInput("pass", new PasswordInput(resources.getString("password") + ":", 20, false));
        panel.add(form.getPanel());
        panel.add(loginButton);
        add(panel, BorderLayout.CENTER);
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!form.validate()) {
                ViewHelper.showFormValidationError();
                return;
            }
            loginController.login(form.getStringFor("user"), form.getStringFor("pass"));
        }
    }
}
