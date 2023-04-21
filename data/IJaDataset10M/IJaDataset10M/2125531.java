package org.plotnikov.pricemaker.gui.utils;

import java.awt.Component;
import java.awt.Cursor;
import javax.swing.JOptionPane;

/**
 *
 * @author alexey
 */
public class GUIUtils {

    public static final String DIALOG_CAPTION = "Создатель ценников";

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, DIALOG_CAPTION, JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message, DIALOG_CAPTION, JOptionPane.INFORMATION_MESSAGE);
    }

    public static int showConfirmMessage(String message) {
        return JOptionPane.showConfirmDialog(null, message, DIALOG_CAPTION, JOptionPane.YES_NO_OPTION);
    }

    public static void setNormalCursor(Component frame) {
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public static void setBusyCursor(Component frame) {
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
}
