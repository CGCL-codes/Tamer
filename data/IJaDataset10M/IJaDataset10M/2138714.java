package org.bs.mdi.core;

import java.awt.datatransfer.*;
import org.bs.mdi.action.*;

/**
 * Displays a piece of the document's information. 
 */
public interface View extends ActionObserver, ActionObservable, ActionProcessor {

    /**
	 * Returns the document which this view is assigned to.
	 * This is just a convenience function which is roughly equivalent to
	 * <code>getData().getDocument</code>.
	 * @return	the document
	 */
    public Document getDocument();

    /**
	 * Returns the Data object associated with this view.
	 * @return	the corresponding Data object
	 */
    public Data getData();

    /**
	 * Associates this View with the given Data object.
	 * This method also sets up the observer relations between data and view.
	 * @param data	the Data object
	 */
    public void setData(Data data);

    /**
	 * Gets the parent View object, or null if there is no parent object
	 * (i.e. this is probably a {@link RootView} object).
	 * @return	the parent object
	 */
    public View getParentView();

    /**
	 * Gets the {@link RootView} object which this view belongs to.
	 * @return	the root view (or null in the unlikely case that no root view
	 * has been assigned to this view)
	 */
    public RootView getRootView();

    /**
	 * Adds a child element.
	 * @param child	the child element
	 */
    public void addChild(View child);

    /**
	 * Adds a child at the specified index.
	 * @param child	the child element
	 * @param index	the index of the child
	 */
    public void addChild(View child, int index);

    /**
	 * Removes a child element.
	 * @param child	the child element
	 */
    public void removeChild(View child);

    /**
	 * Counts all child elements.
	 * @return	the number of child elements
	 */
    public int countChildren();

    /**
	 * Gets the child element at the specified index.
	 * @param index	the index
	 * @return	the child element at the specified position
	 */
    public View getChild(int index);

    /**
	 * Gets the index of the specified child.
	 * @param child	the child element
	 * @return	the index of the child, or -1 if the specified view 
	 * is not a child of this view.
	 */
    public int getChildIndex(View child);

    /**
	 * Creates an action for copying information to the clipboard.
	 * This method is supposed to carry together all information it is representing,
	 * for example by querying its Data object and its children elements.
	 * Finally, it should convert this information to a {@link Transferable} object
	 * and use it to create a {@link ClipboardCopyAction} which encapsulates the
	 * Transferable.
	 * Note: Do not directly copy anything to the Clipboard in this method, just create
	 * the ClipboardCopyAction as decribed above. The framework handles the actual
	 * clipboard transfer for you.
	 * @return	an action encapsulating the information contained in this view
	 */
    public ClipboardCopyAction createCopyAction();

    /**
	 * Copies information to the clipboard.
	 * This method creates a new ClipboardCopyAction by using {@link #createCopyAction()}
	 * and transfers the information of this action to the clipboard.
	 */
    public void copy();

    /**
	 * Creates an action for cutting from this view and copying it to the clipboard.
	 * This method is supposed to carry together all information it is representing,
	 * for example by querying its Data object and its children elements.
	 * Finally, it should convert this information to a {@link Transferable} object
	 * and use it to create a {@link ClipboardCutAction} which encapsulates the
	 * Transferable.
	 * @return	an action encapsulating the information contained in this view and which
	 * knows how to cut it
	 */
    public ClipboardCutAction createCutAction();

    /**
	 * Cuts information from this view and copies it to the clipboard.
	 * This method creates a new ClipboardCutAction by using {@link #createCutAction()}
	 * and transfers the information of this action to the clipboard. In addition, it
	 * performs a delete operation using the action retrieved from calling
	 * {@link #createDeleteAction()}.
	 */
    public void cut();

    /**
	 * Pastes information from the clipboard into this view.
	 * This is done in two stages: first, the {@link Transferable} object which we
	 * got from the clipboard is converted to a {@link ClipboardPasteAction} using the
	 * {@link #createPasteAction(Transferable)} method. Then, the created ClipboardPasteAction
	 * is is fired using the {@link #fireAction(Action)} method.
	 * Note that the conversion process may fail. In this case, an exception is thrown.
	 * @param data	the data to be inserted
	 * @throws ClipboardConversionException	when the conversion process fails
	 */
    public void paste(Transferable data) throws ClipboardConversionException;

    /**
	 * Converts the clipboard content (in the form of a {@link Transferable}
	 * object) to a ClipboardPasteAction which knows how to paste the information.
	 * @param data	the data to be converted
	 * @return	the ClipboardPasteAction which knows how to insert the data into the view
	 * @throws ClipboardConversionException	if the conversion process fails, for example
	 * if the given data is in a non-compatible format
	 */
    public ClipboardPasteAction createPasteAction(Transferable data) throws ClipboardConversionException;

    /**
	 * Generates a {@link DeleteAction} for this view.
	 * This action knows how to delete elements from this view.
	 * @return	an action which knows how to perform the delete operation
	 */
    public DeleteAction createDeleteAction();

    /**
	 * Deletes information from this view.
	 * This method creates a new DeleteAction by using {@link #createDeleteAction()}
	 * and fires this action.
	 */
    public void delete();

    /**
	 * Returns true if content can be copied to the clipboard.
	 * @return	true if a copy operation is possible
	 */
    public boolean isCopyPossible();

    /**
	 * Enables or disables copying to the clipboard.
	 * @param possible	true if copying is possible, false otherwise
	 */
    public void setCopyPossible(boolean possible);

    /**
	 * Returns true if content can be cutted to the clipboard.
	 * @return	true if a cut operation is possible
	 */
    public boolean isCutPossible();

    /**
	 * Enables or disables cutting to the clipboard.
	 * @param possible	true if cutting is possible, false otherwise
	 */
    public void setCutPossible(boolean possible);

    /**
	 * Returns true if content can be pasted from the clipboard.
	 * @return	true if a paste operation is possible
	 */
    public boolean isPastePossible();

    /**
	 * Enables or disables pasting from the clipboard.
	 * @param possible	true if pasting is possible, false otherwise
	 */
    public void setPastePossible(boolean possible);

    /**
	 * Returns true if content can be deleted.
	 * @return	true if a delete operation is possible
	 */
    public boolean isDeletePossible();

    /**
	 * Enables or disables deleting operations.
	 * @param possible	true if deleting is possible, false otherwise
	 */
    public void setDeletePossible(boolean possible);

    /**
	 * Called from the Data object to indicate that the documents data has changed.<p>
	 * The view update or redraw its contents the reflect that changes.
	 */
    public void syncWithData();

    /**
	 * Fires an action.
	 * This method is to be called whenever a new action
	 * (which originates from this object) should be processed.
	 * @param action	the action
	 */
    public void fireAction(Action action);
}
