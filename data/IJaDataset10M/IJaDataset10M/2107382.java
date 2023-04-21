package org.swemof.gui.echo.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.WindowPane;
import org.swemof.dao.DAOException;
import org.swemof.gui.echo.Application;

/**
 * Utility class for displaying various messages to the user.
 */
public class MessageUtil {

    private static final Insets CONTENT_INSETS = new Insets(10);

    /**
     * Shows the given warning message.
     * 
     * @param message
     */
    public static void showWarningMessage(String message) {
        showMessage("Warning", message);
    }

    /**
     * Shows the given error message.
     * 
     * @param message
     */
    public static void showErrorMessage(String message) {
        showMessage("Error", message);
    }

    /**
     * Shows the given exception.
     * 
     * @param e
     */
    public static void showError(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String title;
        if (e instanceof DAOException) {
            title = "Persistence Error";
        } else {
            title = "Error";
        }
        showMessage(title, sw.toString());
    }

    /**
     * Shows the given message.
     * 
     * @param title
     * @param message
     */
    public static void showMessage(String title, String message) {
        WindowPane window = new WindowPane();
        window.setTitle(title);
        window.setWidth(new Extent(450));
        window.setHeight(new Extent(200));
        window.setModal(true);
        window.setMaximizeEnabled(false);
        ContentPane content = new ContentPane();
        content.setInsets(CONTENT_INSETS);
        Label msgLabel = new Label(message);
        msgLabel.setFormatWhitespace(true);
        content.add(msgLabel);
        window.add(content);
        Application.addWindow(window);
    }
}
