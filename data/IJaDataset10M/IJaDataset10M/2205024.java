package org.zhouer.zterm.view;

import java.awt.Component;
import java.awt.FlowLayout;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

/**
 * PasswordDialog is a dialog which asks user for passwords.
 * 
 * @author h45
 */
public class PasswordPane extends JOptionPane {

    private static final long serialVersionUID = 475389458121763833L;

    private JPasswordField passField;

    private JLabel passLabel;

    /**
	 * Constructor with a prompt
	 * 
	 * @param prompt
	 *            message showed on the panel
	 */
    public PasswordPane(final String prompt) {
        this.setOptionType(JOptionPane.DEFAULT_OPTION);
        final JPanel panel = new JPanel();
        this.passLabel = new JLabel(prompt);
        this.passField = new JPasswordField(10);
        panel.setLayout(new FlowLayout());
        panel.add(this.passLabel);
        panel.add(this.passField);
        this.setMessage(panel);
    }

    /**
	 * Prompts the user for input in a password dialog.
	 * 
	 * @param parentComponent the parent Component for the dialog
	 * @param message the Object to display
	 * @param title the String to display in the dialog title bar
	 * @return user's input, or null meaning the user canceled the input
	 */
    public static String showPasswordDialog(Component parentComponent, Object message, String title) {
        final PasswordPane passwordPane = new PasswordPane(message.toString());
        final JDialog dialog = passwordPane.createDialog(parentComponent, title);
        final Runnable focusPasswordField = new Runnable() {

            public void run() {
                passwordPane.requestFocusInField();
            }
        };
        final Runnable showDialog = new Runnable() {

            public void run() {
                SwingUtilities.invokeLater(focusPasswordField);
                dialog.setVisible(true);
            }
        };
        try {
            SwingUtilities.invokeAndWait(showDialog);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (passwordPane.getValue() != null) {
            if (passwordPane.getValue() instanceof Integer) {
                if (passwordPane.getValue().equals(new Integer(JOptionPane.OK_OPTION))) {
                    return passwordPane.getPassword();
                }
            }
        }
        return null;
    }

    /**
	 * Getter of password
	 * 
	 * @return password.
	 */
    public String getPassword() {
        return String.valueOf(this.passField.getPassword());
    }

    /**
	 * Request focus in the password field.
	 */
    public void requestFocusInField() {
        passField.requestFocusInWindow();
    }
}
