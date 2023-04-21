package lib.gui;

import java.awt.*;
import javax.swing.*;

/**
 * This class holds the definitions of static dialogs for
 * general use.
 * 
 * @author Paul Austen
 */
public class Dialogs {

    /**
	 * A dialog with an information message and an OK button
	 * 
	 * @param frame The parent frame of this dialog.
	 * @param title The title text of the dialog.
	 * @param message The message to be displayed in the dalog.
	 */
    public static void showOKDialog(Frame frame, String title, String message) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
	 * A dialog with an error message and an OK button.
	 * 
	 * @param frame The parent frame of this dialog.
	 * @param title The title text of the dialog.
	 * @param message The message to be displayed in the dalog.
   */
    public static void showErrorDialog(Frame frame, String title, String message) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
   * A confirmation dialog with Yes, No and cancel buttons.
   * 
	 * @param frame The parent frame of this dialog.
	 * @param title The title text of the dialog.
	 * @param message The message to be displayed in the dalog.
	 * 
	 * @return An integer of value JOptionPane.YES_OPTION or 
	 * 					JOptionPane.NO_OPTION.
   */
    public static int showYesNoDialog(Frame frame, String title, String message) {
        return JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_NO_OPTION);
    }

    /**
	 * A dialog into which the user may enter a line of text.
	 * 
	 * @param comp The parent component for the dialog.
	 * @param title The title text of the Dialog.
	 * @param message The message text to be displayed in the dialog.
	 * @param currentValue The initial value for the line of text to be entered.
	 * @return The string entered or null if cancel button selected.
	 */
    public static String showInputTextDialog(Component comp, String message, String title, String currentValue) {
        ImageIcon icon = new ImageIcon();
        while (true) {
            String text = (String) JOptionPane.showInputDialog(comp, message, title, JOptionPane.INFORMATION_MESSAGE, icon, null, currentValue);
            return text;
        }
    }

    /**
   * A dialog into which the user may enter a line of text.
   * 
   * @param comp The parent component for the dialog.
   * @param title The title text of the Dialog.
   * @param message The message text to be displayed in the dialog.
   * @return The string entered or null if cancel button selected.
   */
    public static String showInputTextDialog(Component comp, String message, String title) {
        return showInputTextDialog(comp, message, title, "");
    }

    /**
	 * A dialog into which to enter a long number.
	 * 
	 * @param comp The parent component for the dialog.
	 * @param title The title text of the Dialog.
	 * @param message The message text to be displayed in the dialog.
	 * @param currentValue The initial value for the line of text to be entered.
	 * @param min The minimum valid number that may be enterd by the user.
	 * @param max The maximum valid number that may be enterd by the user.
	 * @return The long number entered by the user or Long.MIN_VALUE if the
	 * cancel button is selected.
	 */
    public static long showInputNumberDialog(Component comp, String message, String title, long currentValue, long min, long max) {
        long numberEntered;
        ImageIcon icon = new ImageIcon();
        while (true) {
            String text = (String) JOptionPane.showInputDialog(comp, message, title, JOptionPane.INFORMATION_MESSAGE, icon, null, "" + currentValue);
            if (text == null) {
                return Long.MIN_VALUE;
            }
            try {
                numberEntered = Long.parseLong(text);
                if (numberEntered < min) {
                    JOptionPane.showMessageDialog(comp, "" + min + " is the minimum value that can be entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (numberEntered > max) {
                    JOptionPane.showMessageDialog(comp, "" + max + " is the maximum value that can be entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                return numberEntered;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(comp, "Only numbers may be entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
   * A dialog into which to enter a long number.
   * 
   * @param comp The parent component for the dialog.
   * @param title The title text of the Dialog.
   * @param message The message text to be displayed in the dialog.
   * @param currentValue The initial value for the number to be entered.
   * @return The long number entered by the user or Long.MIN_VALUE if the 
	 * cancel button is selected.
   */
    public static long showInputNumberDialog(Component comp, String message, String title, long currentValue) {
        return showInputNumberDialog(comp, message, title, currentValue, Long.MIN_VALUE + 1, Long.MAX_VALUE);
    }

    /**
   * A dialog into which to enter a long number.
   * 
   * @param comp The parent component for the dialog.
   * @param message The message text to be displayed in the dialog.
   * @param title The title text of the Dialog.
   * @return The long number entered by the user or Long.MIN_VALUE if the 
	 * cancel button is selected.
   */
    public static long showInputNumberDialog(Component comp, String message, String title) {
        return showInputNumberDialog(comp, message, title, 0, Long.MIN_VALUE + 1, Long.MAX_VALUE);
    }

    /**
	 * A dialog into which to enter a double number.
	 * @param comp The parent component for the dialog.
	 * @param message The message text to be displayed in the dialog.
	 * @param title The title text of the Dialog.
	 * @param currentValue The initial value for the number to be entered.
	 * @param min The minimum valid number that may be enterd by the user.
	 * @param max The maximum valid number that may be enterd by the user.
	 * @return The double number entered by the user or Double.NEGATIVE_INFINITY
	 * if the cancel button is selected.
	 */
    public static double showInputDoubleDialog(Component comp, String message, String title, double currentValue, double min, double max) {
        double numberEntered;
        ImageIcon icon = new ImageIcon();
        while (true) {
            String text = (String) JOptionPane.showInputDialog(comp, message, title, JOptionPane.INFORMATION_MESSAGE, icon, null, "" + currentValue);
            if (text == null) {
                return Double.NEGATIVE_INFINITY;
            }
            try {
                numberEntered = Double.parseDouble(text);
                if (numberEntered < min) {
                    JOptionPane.showMessageDialog(comp, "" + min + " is the minimum value that can be entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                if (numberEntered > max) {
                    JOptionPane.showMessageDialog(comp, "" + max + " is the maximum value that can be entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                return numberEntered;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(comp, "Only numbers may be entered.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
   * A dialog into which to enter a double number.
   * @param comp The parent component for the dialog.
   * @param message The message text to be displayed in the dialog.
   * @param title The title text of the Dialog.
   * @param currentValue The initial value for the number to be entered.
   * @return The double number entered by the user or Double.NEGATIVE_INFINITY
   * if the cancel button is selected.
   */
    public static double showInputDoubleDialog(Component comp, String message, String title, double currentValue) {
        return showInputDoubleDialog(comp, message, title, currentValue, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
   * A dialog into which to enter a double number.
   * @param comp The parent component for the dialog.
   * @param message The message text to be displayed in the dialog.
   * @param title The title text of the Dialog.
   * @return The double number entered by the user or Double.NEGATIVE_INFINITY
   * if the cancel button is selected.
   */
    public static double showInputDoubleDialog(Component comp, String message, String title) {
        return showInputDoubleDialog(comp, message, title, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }
}
