package org.bs.mdi.core;

import org.bs.mdi.gui.*;

/**
 * An interface for main window implementations.
 */
public interface MainWindow extends Window {

    public static final int QUESTION = 0;

    public static final int INFO = 1;

    public static final int WARNING = 2;

    public static final int ERROR = 3;

    /**
	 * Creates a new document window.
	 * @return	a new document window
	 */
    public DocumentWindow createDocumentWindow();

    /**
	 * Gets a list of all currently opened document windows.
	 * @return	a list of document windows
	 */
    public java.util.List getDocumentWindows();

    /**
	 * Shows a file open dialog for the given file formats.
	 * @param formats	the file formats which are used to setup the file filters
	 * @return	the selected filename, or null in case of error
	 */
    public String showFileOpenDialog(FileFormat[] formats);

    /**
	 * Shows a file save dialog for the given file formats.
	 * @param formats	the file formats which are used to setup the file filters
	 * @param initialName	the initial file name
	 * @return	the selected filename, or null in case of error
	 */
    public String showFileSaveDialog(FileFormat[] formats, String initialName);

    /**
	 * Displays a message box. The given message is shown on the screen and
	 * the user has only the option to confirm the message (by clicking an
	 * "OK"-Button, for example).
	 * @param type	the type: <code>QUESTION</code>, <code>INFO</code>, 
	 * 	<code>WARNING</code> or <code>ERROR</code>,   
	 * @param window	the window which this message concerns, or 
	 * 	<code>null</code> if it concerns the entire application.
	 * @param message	the message to be displayed (note: the message is 
	 * 	<strong>not</strong> automatically translated by passing it to
	 * 	{@link Application#tr})
	 */
    public void showMessage(int type, Window window, String message);

    /**
	 * Displays a dialog box. The given message is shown on the screen and the
	 * user can choose between several options to react on the event.
	 * The options may be displayed as clickable buttons, for example.
	 * @param type	the type: <code>QUESTION</code>, <code>INFO</code>, 
	 * 	<code>WARNING</code> or <code>ERROR</code>, 
	 * @param window	the document window which this message concerns, or 
	 * 	<code>null</code> if it concerns the entire application.
	 * @param message	the message to be displayed (note: the message is 
	 * 	<strong>not</strong> automatically translated by passing it to
	 * 	{@link Application#tr})
	 * @param choices	the choices to be presented to the user
	 * @param defaultChoice	the index of the default choice
	 * @return	the index of the chosen option
	 */
    public int showDialog(int type, Window window, String message, GuiItem[] choices, int defaultChoice);

    /**
	 * Shows a dialog allowing the user to setup printing options.
	 */
    public void showPrintSetup();

    /**
	 * Shows a print preview dialog for the current document.
	 */
    public void showPrintPreview();

    /**
	 * Prints the current document. The implementation may display
	 * a printing dialog before the print job is actually created.
	 */
    public void printDocument();

    /**
	 * Shows the about dialog.
	 */
    public void showAboutDialog();

    /**
	 * Sets the statusbar text.
	 * @param status	the statusbar text
	 */
    public void setStatus(String status);

    /**
	 * Sets the busy flag. The main window may for example show
	 * an hourglass mouse cursor to give the user the indication that
	 * the program is busy now.
	 * @param busy	true if the busy indication should be enabled, false otherwise
	 */
    public void setBusy(boolean busy);

    /**
	 * Gets the default progress monitor for this main window.
	 * This progress monitor is responsible for giving the user
	 * some feedback about the tasks which your application
	 * is currently performing.
	 * @return the default progress monitor
	 */
    public ProgressMonitor getProgressMonitor();
}
