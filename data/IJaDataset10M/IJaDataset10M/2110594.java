package au.jSummit.Modules.Polling;

import javax.swing.*;
import javax.swing.event.*;

public abstract class ErrorDialog extends JOptionPane {

    public static void showErrorDialog(JFrame parent, String errMsg) {
        showMessageDialog(parent, errMsg, "Poll Error", JOptionPane.ERROR_MESSAGE);
    }
}
