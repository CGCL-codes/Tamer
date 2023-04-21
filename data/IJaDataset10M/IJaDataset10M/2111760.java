package org.exist.eclipse.browse.browse;

import org.eclipse.ui.IWorkbenchPage;

/**
 * This is the interface for the extension point 'collection'.
 * 
 * The time flow of the method is:
 * <ol>
 * <li>call {@link #init(IWorkbenchPage)}</li>
 * <li>call {@link #actionPerformed(IBrowseItem[]))}</li>
 * </ol>
 * 
 * @author Pascal Schmidiger
 * 
 */
public interface IBrowseListener {

    /**
	 * You have the possibility to start a view or something else.
	 * 
	 * @param page
	 */
    public void init(IWorkbenchPage page);

    /**
	 * You gets an array of {@link IBrowseItem}, which was selected. If your
	 * extension is not multi selected, then you will become only one element.
	 * 
	 * @param items
	 *            on which you will perform your action.
	 */
    public void actionPerformed(IBrowseItem[] items);
}
