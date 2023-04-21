package org.ks.util;

import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import org.ks.ui.frame.ErrorDialog;

/**
 * @author hcl
 *
 */
public final class UIUtils {

    public static char[] getPassword(boolean mustGet) {
        final JPasswordField jpf = new JPasswordField();
        JOptionPane jop = new JOptionPane(jpf, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = jop.createDialog("Password for keystore:");
        dialog.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        jpf.requestFocusInWindow();
                    }
                });
            }
        });
        dialog.setVisible(true);
        int result = (Integer) jop.getValue();
        dialog.dispose();
        char[] password = null;
        if (result == JOptionPane.OK_OPTION) {
            password = jpf.getPassword();
        } else if (mustGet) {
            return getPassword(mustGet);
        }
        return password;
    }

    public static void showErrorDialog(Container frame, Exception e) {
        if (frame instanceof JFrame) {
            ErrorDialog dialog = ErrorDialog.getInstance((JFrame) frame, e);
            dialog.setVisible(true);
        }
    }
}
