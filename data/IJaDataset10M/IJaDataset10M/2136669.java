package org.homeunix.thecave.moss.data.collection;

import java.util.AbstractList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A list which is automatically generated, with a Set backing it.  Can
 * be used to convert a Set to a List, but allow easy updating of the list
 * when the backing Set changes.  Call updateList() to update. 
 * 
 * @author wyatt
 *
 */
public class ListSet<T> extends AbstractList<T> {

    private final Set<T> backing;

    private List<T> list = new LinkedList<T>();

    public ListSet(Set<T> backingSet) {
        this.backing = backingSet;
    }

    @Override
    public T get(int index) {
        return getList().get(index);
    }

    @Override
    public int size() {
        return getList().size();
    }

    public void updateList() {
        list.clear();
        list.addAll(backing);
    }

    private List<T> getList() {
        if (list == null) updateList();
        return list;
    }
}
