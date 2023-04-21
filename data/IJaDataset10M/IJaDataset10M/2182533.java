package org.freedom.ooze.cruelganger.defaultimpl;

import java.lang.reflect.Array;
import org.freedom.ooze.common.IAdapter;
import org.freedom.ooze.cruelganger.core.IPool;

/**
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * @author liangs
 */
public class DefaultPool implements IAdapter, IPool {

    private Class itemType;

    private int capacity;

    private Object[] objects;

    public Class getItemType() {
        return itemType;
    }

    public int getCapacity() {
        return capacity;
    }

    public DefaultPool(Class itemType, int capacity) {
        this(capacity);
        setItemType(itemType);
    }

    public DefaultPool(int capacity) {
        this();
        this.capacity = capacity;
    }

    public int getSize() {
        if (objects == null) {
            return 0;
        } else {
            return objects.length;
        }
    }

    public Object getItem(int index) throws IndexOutOfBoundsException {
        if (objects == null) {
            return null;
        } else {
            return objects[index];
        }
    }

    public Object getAdapter(Class adapterClass) {
        if (adapterClass.equals(Array.class)) {
            return objects;
        } else {
            return null;
        }
    }

    public boolean removeItem(Object item) {
        if ((getSize() == 0) || (item == null) || (!containsItem(item))) {
            return false;
        }
        Object[] foo = new Object[getSize() - 1];
        int index = indexOf(item);
        System.arraycopy(objects, 0, foo, 0, index);
        System.arraycopy(objects, index + 1, foo, index, getSize() - index - 1);
        objects = foo;
        return true;
    }

    public boolean addItem(Object item) {
        if ((getSize() >= getCapacity()) || (item == null) || (containsItem(item))) {
            return false;
        }
        Object[] foo = new Object[getSize() + 1];
        System.arraycopy(objects, 0, foo, 0, getSize());
        foo[getSize()] = item;
        objects = foo;
        return true;
    }

    public boolean adjustCapacityTo(int newCapacity) {
        if (newCapacity < getSize()) {
            return false;
        }
        capacity = newCapacity;
        return true;
    }

    public boolean containsItem(Object item) {
        return indexOf(item) == -1 ? false : true;
    }

    private DefaultPool() {
        objects = new Object[0];
    }

    public int indexOf(Object item) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == item) {
                return i;
            }
        }
        return -1;
    }

    public void setItemType(Class itemType) {
        this.itemType = itemType;
    }

    public void initObjects() {
    }
}
