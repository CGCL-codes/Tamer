package de.fzi.injectj.frontend.eclipse.abstraction;

import java.util.Vector;

/**
 * @author Sebastian Mies
 * 
 * This a basic abstraction of the SWT List Views
 * It encapsulates the simplest form of List operations in an abstract matter*/
public abstract class ListModel {

    private Vector listeners = new Vector();

    protected void fireUpdateEvent() {
        for (int i = 0; i < listeners.size(); i++) ((Updateable) listeners.get(i)).updateContent();
    }

    /**
	 * Remove an update listener
	 */
    public void removeUpdateListener(Updateable listener) {
        listeners.remove(listener);
    }

    /**
	 * Add an update listener<br>
	 * It is sent, if an item has been changed<br>
	 */
    public void addUpdateListener(Updateable listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    /**
	 * Adds an object to the list
	 * @return the position where the object has been added and -1, if operation was unsuccessful.
	 */
    public abstract int add(Object obj);

    /**
	 * Removes an object from the list
	 */
    public abstract void remove(Object obj);

    /**
	 * Removes an object from the list
	 */
    public abstract void remove(int i);

    /**
	 * Retrieve one object from the list
	 */
    public abstract Object get(int i);

    /**
	 * Set an object
	 * @return the position where the object has been added and -1, if operation was unsuccessful.
	 */
    public abstract int set(int i, Object obj);

    /**
	 * Move an object
	 * @return the position where the object has been added and -1, if operation was unsuccessful.
	 */
    public abstract int move(int from, int to);

    /**
	 * Returns the number of objects in the list
	 * @return the number of objects
	 */
    public abstract int size();

    /**
	 * Inserts an object at a specific position
	 * This is not abstract since we can use add and move here.
	 * @return the position where the object has been added and -1, if operation was unsuccessful.
	 */
    public int insert(Object obj, int pos) {
        int i = add(obj);
        if (i != -1) i = move(i, pos);
        return i;
    }

    /**
	 * Checks if this object fits to the list contents
	 * @return true, if object is of valid type
	 */
    public abstract boolean check(Object obj);

    /**
	 * Get an String and Image representation of the object
	 */
    public abstract ModelLabel getLabel(Object obj);

    /**
	 * Create an editor for a specific object from the list.
	 * This may depend on which classtype this object is
	 */
    public abstract ModelEditor createEditor(Class cls);

    /**
	 * Creates an default editor for adding new items to the list
	 * This may cover an Editor that let the user choose the type of item
	 */
    public abstract ModelEditor createDefaultEditor();

    /**
	 * Returns whether the list should be sortable 
	 * @return true = sortable
	 */
    public abstract boolean isSortable();

    public void update() {
        fireUpdateEvent();
    }
}
