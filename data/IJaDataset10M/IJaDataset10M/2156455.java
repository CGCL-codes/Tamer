package eu.davidgamez.mas.gui;

import javax.swing.JOptionPane;

public class MsgHandler {

    /** The application */
    private static MainFrame mainFrame;

    /** Prints out an information message to the console */
    public static void info(String msg) {
        System.out.println("INFO: " + msg);
    }

    /** Prints out a warning message to the console */
    public static void warning(String msg) {
        System.out.println("WARNING: " + msg);
    }

    /** Shows a dialog box with an error message */
    public static void error(String msg) {
        JOptionPane.showMessageDialog(mainFrame, msg, "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("ERROR: " + msg);
    }

    public static void error(Exception ex) {
        error(ex.getMessage());
        ex.printStackTrace();
    }

    /** Displays an error message in a dialog and prints out the exception stack trace */
    public static void error(String msg, Exception ex) {
        JOptionPane.showMessageDialog(mainFrame, msg, "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("ERROR: " + msg);
        ex.printStackTrace();
    }

    /** Displays a critical error message in a dialog */
    public static void critical(String msg) {
        JOptionPane.showMessageDialog(mainFrame, msg, "Critical Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("CRITICAL: " + msg);
    }

    /** Sets the reference to the main frame */
    public static void setMainFrame(MainFrame mainFrame) {
        MsgHandler.mainFrame = mainFrame;
    }
}
