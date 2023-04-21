package org.jpropeller.view;

import org.jpropeller.view.proxy.ViewProxy;

/**
 *	An object handling a view of an underlying model, where the model can be changed,
 *	and events are fired when the model is changed
 * 
 *  A {@link View} must display the underlying model, but may also allow editing of
 *  the model. Methods are provided to check for pending edits, and to commit or
 *  cancel these edits - where a {@link View} does not perform editing, it simply
 *  needs to return false when checked for pendin edits, and do nothing on commit
 *  and cancel.
 * 
 * 	Undo/redo type behaviour is NOT implemented by the {@link View}, this will be
 * 	implemented at a higher level if necessary. As a result, the view is,
 * 	as far as possible, an "instant" editor, and makes changes directly on the
 * 	underlying object. However a mechanism is in place for allowing multi-stage
 * 	editing, where instant editing is not possible, for example where it would
 * 	leave the edited object in an inconsistent intermediate state (see notes on this
 * 	below)
 * 
 * 	Consistent state of edited object MUST be retained at all points, so that if 
 * 	object editor is removed, even without commit() or cancel() being called first, the object
 * 	will still be valid. Usually edited objects will however enforce this themselves,
 * 	the main point is that editors must NOT rely on commit()/cancel() being called, this is
 * 	provided to allow for convenience for users of the editor, not the editor itself (see below)
 * 
 * 	Where it is unavoidable to use multi-stage editing, for example where a text field
 * 	is used to enter numeric values, and this cannot be committed until the text field contains
 * 	a valid number, the editor should return true from isEditing() while there is a pending
 * 	change of this type, so that code using the editor can commit or cancel before stopping 
 * 	the editor (for example hiding it, or setting a new object). 
 * 	This is convenient for users since it ensures they do not lose pending changes without
 *  
 * 	being notified.
 * @param <M>
 * 		The type of model viewed
 */
public interface View<M> {

    /**
	 * Get the {@link ViewProxy} for this {@link View}.
	 * The actual value displayed/edited by this view is
	 * accessed using the {@link ViewProxy#model()} prop.
	 * @return
	 * 		The {@link View}'s {@link ViewProxy}
	 */
    public ViewProxy<? extends M> getProxy();

    /**
	 * @return True if any editing is in progress which has not been committed. This
	 * should usually return false unless the editing is valid (could be committed rather
	 * than cancelled)
	 * Where an editor is about to be hidden/removed it is friendly to the user to inform
	 * them if there is pending editing, and ask whether they wish to commit or cancel that
	 * editing, which can be done by use of commit() and cancel().
	 * Some uses of editors may wish to simply try to commit editing anyway, only informing
	 * the user if this is not possible, others may wish to always cancel editing in progress,
	 * etc.
	 */
    public boolean isEditing();

    /**
	 * Commit the editing, attempting to make any changes to the object required to
	 * reflect editing which is in progress in the editor itself
	 * @throws CompletionException Will be thrown unless all pending editing 
	 * can be completed and leaves object in consistent state. e.g. may fail 
	 * if a text box is used for numeric entry and contains invalid format, 
	 * or if a set of selections do not make sense together.
	 */
    public void commit() throws CompletionException;

    /**
	 * This must cancel any editing in progress in the editor itself, resetting the
	 * editor to the current state of the object.
	 */
    public void cancel();
}
