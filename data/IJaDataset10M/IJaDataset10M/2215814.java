package org.arch4j.ui.components;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * Centralized place to deal with Dialogs,
 * 
 * We 'deal' with Dialogs by making sure if we're already displaying
 * a modal dialog to not display another one.
 * This is not the perfect solution, but stacking multiple modal dialogs
 * can cause issues, maybe a better solution for later on would be
 * to add additional Dialogs to the currently displaying one.
 * 
 * The issue was that Modal Dialog's could be popped up from multiple-
 * panels and would never be aware of each other.
 * Also there are cases where the code doesn't have reference to or 
 * extend a PropertiesPanel so there would be no way to leverage
 * the smarts.
 * 
 * Now you can. 
 * 
 * @author jroome
 */
public class DialogManager {

    private static DialogManager manager = new DialogManager();

    /**
	 * Indicates if this panel is currently displaying a message box.
	 * It may be useful to check this field in focusLost event
	 * handling.
	 */
    private boolean isDisplayingMessage = false;

    private DialogManager() {
    }

    public static DialogManager getManager() {
        return manager;
    }

    /**
	 * @return boolean
	 */
    public boolean isDisplayingMessage() {
        return isDisplayingMessage;
    }

    /**
	 * Display an error to the user.
	 *
	 * @param anError The error message.
	 * @param aTitle  The title for the dialog.
	 */
    public void displayError(String anError, String aTitle) {
        displayError(null, anError, aTitle);
    }

    /**
	 * Display an error to the user.
	 *
	 * @param component The component to associate the dialog with.
	 * @param anError The error message.
	 * @param aTitle  The title for the dialog.
	 */
    public void displayError(Component component, String anError, String aTitle) {
        synchronized (this) {
            if (isDisplayingMessage) {
                return;
            }
            isDisplayingMessage = true;
            JOptionPane.showMessageDialog(component, anError, aTitle, JOptionPane.ERROR_MESSAGE);
            isDisplayingMessage = false;
        }
    }

    /**
	 * @param component
	 * @param anError
	 * @param aTitle
	 * @param ex
	 */
    public void displayErrorWithDetails(Component component, String anError, String aTitle, Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String detail = sw.toString();
        pw.close();
        displayErrorWithDetails(component, anError, aTitle, detail);
    }

    /**
	 * 
	 * @param component
	 * @param anError
	 * @param aTitle
	 * @param detailedMessage
	 */
    public void displayErrorWithDetails(Component component, String anError, String aTitle, String detailedMessage) {
        synchronized (this) {
            if (isDisplayingMessage) {
                return;
            }
            JOptionDetailsPane pane = new JOptionDetailsPane(anError, aTitle, detailedMessage);
            JDialog dialog = pane.createDialog(component, aTitle);
            isDisplayingMessage = true;
            dialog.show();
            isDisplayingMessage = false;
        }
    }

    /**
	 * Display an Confirmation dialog to the user
	 *
	 * @param aMessage The confirmation message.
	 * @param aTitle  The title for the dialog.
	 * @return OK == true
	 */
    public boolean displayConfirm(String aMessage, String aTitle) {
        return displayConfirm(null, aMessage, aTitle);
    }

    /**
	 * Display an Confirmation dialog to the user
	 *
	 * @param component The component to associate the dialog with.
	 * @param aMessage The confirmation message.
	 * @param aTitle  The title for the dialog.
	 * @return OK == true
	 */
    public boolean displayConfirm(Component component, String aMessage, String aTitle) {
        synchronized (this) {
            if (isDisplayingMessage) {
                return true;
            }
            isDisplayingMessage = true;
            int result = JOptionPane.showConfirmDialog(component, aMessage, aTitle, JOptionPane.OK_CANCEL_OPTION);
            isDisplayingMessage = false;
            return result == JOptionPane.OK_OPTION;
        }
    }

    /**
	 * Display a warning to the user.
	 *
	 * @param aWarning  The warning message.
	 * @param aTitle    The title for the dialog.
	 */
    public void displayWarning(String aWarning, String aTitle) {
        displayWarning(null, aWarning, aTitle);
    }

    /**
	 * Display a warning to the user.
	 *
	 * @param component The component to associate the dialog with.
	 * @param aWarning  The warning message.
	 * @param aTitle    The title for the dialog.
	 */
    public void displayWarning(Component component, String aWarning, String aTitle) {
        synchronized (this) {
            if (isDisplayingMessage) {
                return;
            }
            isDisplayingMessage = true;
            JOptionPane.showMessageDialog(component, aWarning, aTitle, JOptionPane.WARNING_MESSAGE);
            isDisplayingMessage = false;
        }
    }

    /**
	 * Display an informational message to the user.
	 *
	 * @param aMessage  The informational message.
	 * @param aTitle    The title for the dialog.
	 */
    public void displayInformation(String aMessage, String aTitle) {
        displayInformation(null, aMessage, aTitle);
    }

    /**
	 * Display an informational message to the user.
	 *
	 * @param component The component to associate the dialog with.
	 * @param aMessage  The informational message.
	 * @param aTitle    The title for the dialog.
	 */
    public void displayInformation(Component component, String aMessage, String aTitle) {
        synchronized (this) {
            if (isDisplayingMessage) {
                return;
            }
            isDisplayingMessage = true;
            JOptionPane.showMessageDialog(component, aMessage, aTitle, JOptionPane.INFORMATION_MESSAGE);
            isDisplayingMessage = false;
        }
    }

    /**
	 * Display an informational message to the user.
	 *
	 * @param component The component to associate the dialog with.
	 * @param aMessage  The informational message.
	 * @param aTitle    The title for the dialog.
	 * @param type		JOptionPane type
	 */
    public void displayMessage(Component component, Object aMessage, String aTitle, int type) {
        synchronized (this) {
            if (isDisplayingMessage) {
                return;
            }
            isDisplayingMessage = true;
            JOptionPane.showMessageDialog(component, aMessage, aTitle, type);
            isDisplayingMessage = false;
        }
    }
}
