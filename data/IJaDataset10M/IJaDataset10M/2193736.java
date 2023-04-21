package net.sourceforge.obexftpfrontend.gui;

import java.awt.Window;
import java.awt.event.WindowListener;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * Generic UI Helper implementation.
 * @author Daniel F. Martins
 */
public abstract class AbstractUIHelper<W extends Window> {

    /** Logger. */
    private static final Logger log = Logger.getLogger(AbstractUIHelper.class);

    /** Window related to this helper. */
    protected W window;

    /**
     * Create a new instance of AbstractUIHelper.
     * @param window Window related to this helper.
     */
    public AbstractUIHelper(W window) {
        this.window = window;
    }

    /**
     * Show an error message to the user.
     * @param message Message to display.
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(window, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show a success message to the user.
     * @param message Message to display.
     */
    public void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(window, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show a confirmation message to the user.
     * @param message Message to display.
     * @param title Title of the message.
     * @return Whether the users clicked agreed with the message.
     */
    public boolean showConfirmMessage(String message, String title) {
        return JOptionPane.showConfirmDialog(window, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /**
     * Sends a fake window closing event to the related window object.
     */
    public void sendFakeWindowClosingEvent() {
        log.debug("Sending a windowClosing event to the registered listeners");
        for (WindowListener listener : window.getWindowListeners()) {
            listener.windowClosing(null);
        }
    }

    /**
     * Execute code before the window is shown to the user.
     */
    public void prepareWindow() {
    }
}
