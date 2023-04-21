package org.isistan.flabot.util.edition;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;

/**
 * Interface for dialog edition tabs.
 * Implementors should provide a default constructor.
 * @author da Costa Cambio
 *
 */
public interface EditionItem<T> {

    /**
	 * Asks the edition item if it can be initialized for the given element
	 * @param element
	 * @return
	 */
    boolean accepts(T element);

    /**
	 * Tells the edition tab to initialize.
	 * Will be called only once before any other method is called (with
	 * the exception of accepts()).
	 */
    void initialize(Composite container, T element);

    /**
	 * Called after initialization, when user sees the edition item.
	 * This can be used to defer some initialization.
	 * @param container
	 * @param element
	 */
    void activate();

    /**
	 * This method should be called if an edition element is no
	 * longer needed. 
	 *
	 */
    void dispose();

    /**
	 * The command that performs the action or null if there is nothing to do.
	 * Will be called only if canCreateCommand() returns true
	 * @return
	 */
    Command getCommand();

    /**
	 * Determines if the item modification command can be created.
	 * @return
	 */
    boolean canCreateCommand();

    /**
	 * The status of this edition item.
	 * @return
	 */
    EditionItemStatus getStatus();

    /**
	 * Adds a new listener to be notified of edition item changes.
	 * @param listener
	 */
    boolean addListener(EditionItemChangeListener<T> listener);

    /**
	 * Removes an existing listener.
	 * @param listener
	 */
    boolean removeListener(EditionItemChangeListener<T> listener);

    /**
	 * Returns the listeners
	 * @return
	 */
    EditionItemChangeListener<T>[] getListeners();
}
