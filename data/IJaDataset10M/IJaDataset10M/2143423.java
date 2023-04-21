package org.am.snitch.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;
import org.am.snitch.constants.SnitchConstants;
import org.am.snitch.utilities.Utilities;

/**
 * Password Dialog.
 * <p>
 *
 * @author Axel Maroudas
 * @version 1.0
 *
 */
public class PasswordDialog extends JDialog {

    private JPasswordField passwordField;

    /**
     * @return the passwordField
     */
    public JPasswordField getPasswordField() {
        return passwordField;
    }

    /**
     * @param apasswordField the passwordField to set
     */
    public void setPasswordField(final JPasswordField apasswordField) {
        this.passwordField = apasswordField;
    }

    /**
     * Constructor.
     */
    public PasswordDialog() {
        super();
        JPanel myPanel;
        JButton okButton;
        Action actionListener;
        KeyStroke escape;
        KeyStroke enter;
        escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        setTitle(SnitchConstants.PASSWORD_DIALOG);
        setModal(true);
        myPanel = new JPanel();
        getContentPane().add(myPanel);
        myPanel.add(new JLabel(SnitchConstants.MSG_PASSWORD_DIALOG));
        passwordField = new JPasswordField(20);
        myPanel.add(passwordField);
        okButton = new JButton(SnitchConstants.MSG_OK);
        myPanel.add(okButton);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                if (passwordField.getPassword().length > 0) {
                    setVisible(false);
                }
            }
        });
        actionListener = new AbstractAction() {

            public void actionPerformed(final ActionEvent actionEvent) {
                setVisible(false);
            }
        };
        rootPane.registerKeyboardAction(actionListener, escape, JComponent.WHEN_IN_FOCUSED_WINDOW);
        rootPane.registerKeyboardAction(actionListener, enter, JComponent.WHEN_IN_FOCUSED_WINDOW);
        setSize(420, 70);
        setResizable(false);
        Utilities.centralize(this);
        pack();
        setVisible(true);
    }
}
