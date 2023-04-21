package net.nohaven.proj.javeau.ui.fw;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public abstract class GenericCompanion {

    protected void reportException(Component parent, Exception e) {
        e.printStackTrace();
        String message = e.getMessage();
        try {
            int start = message.lastIndexOf(':');
            start = message.lastIndexOf('.', start - 1);
            message = message.substring(start + 1).trim();
        } catch (Exception e1) {
            message = e.getMessage();
        }
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected static void reportError(Component parent, String error) {
        JOptionPane.showMessageDialog(parent, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void reportWarning(Component parent, String warning) {
        JOptionPane.showMessageDialog(parent, warning, "Error", JOptionPane.WARNING_MESSAGE);
    }

    protected void reportInfo(Component parent, String info) {
        JOptionPane.showMessageDialog(parent, info, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private static final Object[] YES_NO = { "Yes", "No" };

    protected boolean askConfirmation(Component parent, String message) {
        int n = JOptionPane.showOptionDialog(parent, message, "Confirmation request", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, YES_NO, YES_NO[1]);
        return n == 0;
    }

    protected String askText(Component parent, String message) {
        return askText(parent, message, "");
    }

    protected String askText(Component parent, String message, String preset) {
        return (String) JOptionPane.showInputDialog(parent, message, "Input request", JOptionPane.QUESTION_MESSAGE, null, null, preset);
    }

    protected static char[] askPassword(Component parent) {
        JPasswordField pwd = new JPasswordField(32);
        int action = JOptionPane.showConfirmDialog(parent, pwd, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
        if (action < 0) return null;
        return pwd.getPassword();
    }
}
