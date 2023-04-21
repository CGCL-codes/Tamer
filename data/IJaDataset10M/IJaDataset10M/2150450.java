package org.lindenb.swing;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * ListSelectionAdapter
 *
 */
public abstract class ListSelectionAdapter implements ListSelectionListener {

    private List<Object> list = new ArrayList<Object>();

    /**
	 * ListSelectionAdapter
	 */
    public ListSelectionAdapter() {
    }

    /**
	 * @param arg0 values to be inserted
	 */
    public ListSelectionAdapter(Object... values) {
        for (Object o : values) list.add(o);
    }

    public int getObjectCount() {
        return this.list.size();
    }

    public Object getObject(int index) {
        return this.list.get(index);
    }

    public <T> T getObject(Class<T> clazz, int index) {
        return clazz.cast(getObject(index));
    }

    @Override
    public abstract void valueChanged(ListSelectionEvent event);
}
