package com.thyante.thelibrarian.info;

import org.eclipse.swt.widgets.Display;
import com.thyante.thelibrarian.model.specification.IDownloadManager;

/**
 * This class provides the interface between the search database plug-ins
 * and The Librarian's user interface. It contains several UI-related
 * methods that plug-ins could invoke to cause an action in the user
 * interface (e.g. show a dialog).
 * 
 * @author Matthias-M. Christen
 */
public interface IUserInterface {

    /**
	 * Returns the user interface's display.
	 * @return The display
	 */
    public Display getDisplay();

    /**
	 * Returns a download manager that should be used for downloading
	 * data from the Internet.
	 * @return A download manager
	 */
    public IDownloadManager getDownloadManager();

    /**
	 * This method shows a dialog in the user interface presenting a list of
	 * search results from which the user could pick one.
	 * The search result picked by the user is returned by the method.
	 * @param rgDisambiguationList A list of search result objects.
	 * 	Overwrite the <code>toString</code> method of the objects in the
	 * 	<code>rgDisambiguationList</code> array if necessary; the <code>toString</code>
	 * 	method is used to display the objects in the user interface.
	 * @return The search result that the user picked or <code>null</code>
	 * 	if the dialog was canceled
	 */
    public <T> T pickResult(T[] rgDisambiguationList);

    /**
	 * Displays an Info message box.
	 * @param strTitle The message box title
	 * @param strText The message box text
	 */
    public void info(String strTitle, String strText);

    /**
	 * Displays an Error message box.
	 * @param strTitle the message box title
	 * @param strText The message box text
	 */
    public void error(String strTitle, String strText);
}
