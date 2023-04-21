package org.ozoneDB.adminGui.widget;

import java.awt.*;
import javax.swing.*;

/**
 * Contains static methods for showing message dialogs
 *
 * @author Per Nyfelt
 */
public class MessageBox {

    private static void show(Component parentComponent, String message, String title, int messageType) {
        JOptionPane pane = new JOptionPane(message, messageType);
        pane.setBackground(Color.WHITE);
        setBgColor(pane.getComponents(), Color.WHITE);
        JDialog dialog = pane.createDialog(parentComponent, title);
        dialog.show();
    }

    private static void setBgColor(Component[] cmps, Color bgColor) {
        int numComponents = cmps.length;
        for (int i = 0; i < numComponents; i++) {
            Component c = cmps[i];
            if (c instanceof Container) {
                setBgColor(((Container) c).getComponents(), bgColor);
            }
            c.setBackground(bgColor);
        }
    }

    /** Shows a messagedialog with given messsage, title "Message" (= default) and no icon */
    public static void show(String message) {
        show(null, message, "Message", JOptionPane.PLAIN_MESSAGE);
    }

    /** Shows a messagedialog of type ERROR_MESSAGE with given title and message */
    public static void showError(String title, String message) {
        show(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /** Shows a messagedialog of type INFORMATION_MESSAGE given with title and message */
    public static void showInfo(String title, String message) {
        show(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /** Shows a messagedialog of type WARNING_MESSAGE with given title and message */
    public static void showWarning(String title, String message) {
        show(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
}
