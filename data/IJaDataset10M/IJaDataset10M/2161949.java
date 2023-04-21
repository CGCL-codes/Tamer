package timba.util;

import javax.swing.JTextArea;

/**
 *
 * @author juan
 */
public abstract class Debug {

    private static final String ERR = "ERR> ";

    private static final String OUT = "OUT> ";

    private static final String BLOCK = "     ";

    private static JTextArea ta = null;

    private static boolean enabled = false;

    public static void setOut(JTextArea textArea) {
        ta = textArea;
    }

    public static void setEnabled(boolean b) {
        enabled = b;
    }

    public static void out(String msg) {
        if (enabled) {
            System.out.println(msg);
        }
        printInTextArea(msg, OUT);
    }

    public static void err(String msg) {
        if (enabled) {
            System.err.println(msg);
        }
        printInTextArea(msg, ERR);
    }

    public static void block(String msg) {
        if (enabled) {
            System.out.println(msg);
        }
        printInTextArea(msg, BLOCK);
    }

    private static void printInTextArea(String msg, String prefix) {
        if (ta != null) {
            ta.append(prefix + msg + "\n");
        }
    }
}
