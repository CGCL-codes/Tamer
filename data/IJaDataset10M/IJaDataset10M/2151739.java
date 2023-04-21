package org.omegat.gui.messages;

import javax.swing.SwingUtilities;
import org.omegat.gui.main.MainWindow;

/**
 * Methods to send messages to UI objects
 * use indirect methods because Swing objects tend to lockup in
 * java 1.4 when accessed directly
 *
 * @author Keith Godfrey
 */
public class MessageRelay {

    public static void uiMessageDisplayEntry(MainWindow tf) {
        MMx msg = new MMx(tf, MMx.CMD_ACTIVATE_ENTRY);
        SwingUtilities.invokeLater(msg);
    }

    public static void uiMessageDoGotoEntry(MainWindow tf, String str) {
        MMx msg = new MMx(tf, MMx.CMD_GOTO_ENTRY, str);
        SwingUtilities.invokeLater(msg);
    }

    public static void uiMessageSetMessageText(MainWindow tf, String str) {
        MMx msg = new MMx(tf, MMx.CMD_SET_STATUS, str);
        SwingUtilities.invokeLater(msg);
    }

    public static void uiMessageDisplayError(MainWindow tf, String str, Throwable e) {
        MMx msg = new MMx(tf, MMx.CMD_ERROR_MESSAGE, str, e);
        SwingUtilities.invokeLater(msg);
    }
}
