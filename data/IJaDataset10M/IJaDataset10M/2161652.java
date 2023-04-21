package org.jpedal.utils.repositories;

import java.io.Serializable;

/**
 * Provides the functionality/convenience of a Vector for boolean
 *
 * Much faster because not synchronized and no cast
 * Does not double in size each time
 */
public class Vector_boolean implements Serializable {

    int increment_size = 1000;

    protected int current_item = 0;

    int max_size = 250;

    private boolean[] items = new boolean[max_size];

    public Vector_boolean() {
    }

    protected int incrementSize(int increment_size) {
        if (increment_size < 8000) increment_size = increment_size * 4; else if (increment_size < 16000) increment_size = increment_size * 2; else increment_size = increment_size + 2000;
        return increment_size;
    }

    public Vector_boolean(int number) {
        max_size = number;
        items = new boolean[max_size];
    }

    /**
	 * extract underlying data
	 */
    public final boolean[] get() {
        return items;
    }

    /**
	 * set an element
	 */
    public final void setElementAt(boolean new_name, int id) {
        if (id >= max_size) checkSize(id);
        items[id] = new_name;
    }

    /**
	 * return the size
	 */
    public final int size() {
        return items.length;
    }

    /**
	 * add an item
	 */
    public final void addElement(boolean value) {
        checkSize(current_item);
        items[current_item] = value;
        current_item++;
    }

    /**
	 * replace underlying data
	 */
    public final void set(boolean[] new_items) {
        items = new_items;
    }

    /**
	 * remove element at
	 */
    public final void removeElementAt(int id) {
        if (id >= 0) {
            for (int i = id; i < max_size - 2; i++) items[i] = items[i + 1];
            items[max_size - 1] = false;
        } else items[0] = false;
        current_item--;
    }

    /**
	 * clear the array
	 */
    public final void clear() {
        if (current_item > 0) {
            for (int i = 0; i < current_item; i++) items[i] = false;
        } else {
            for (int i = 0; i < max_size; i++) items[i] = false;
        }
        current_item = 0;
    }

    /**
	 * remove element at
	 */
    public final boolean elementAt(int id) {
        if (id >= max_size) checkSize(id);
        return items[id];
    }

    /**
	 * set an element
	 */
    public final void insertElementAt(boolean new_name, int id) {
        current_item = items.length;
        checkSize(current_item + 1);
        for (int i = current_item; i > id; i--) items[i + 1] = items[i];
        items[id] = new_name;
        current_item++;
    }

    /**
	 * check the size of the array and increase if needed
	 */
    private final void checkSize(int i) {
        if (i >= max_size) {
            int old_size = max_size;
            max_size = max_size + increment_size;
            if (max_size <= i) max_size = i + increment_size + 2;
            boolean[] temp = items;
            items = new boolean[max_size];
            System.arraycopy(temp, 0, items, 0, old_size);
            increment_size = incrementSize(increment_size);
        }
    }

    public void trim() {
        boolean[] newItems = new boolean[current_item];
        System.arraycopy(items, 0, newItems, 0, current_item);
        items = newItems;
        max_size = current_item;
    }

    /**reset pointer used in add to remove items above*/
    public void setSize(int currentItem) {
        current_item = currentItem;
    }
}
