package net.sourceforge.hlm.simple.generic;

import java.util.*;
import net.sourceforge.hlm.generic.*;
import net.sourceforge.hlm.helpers.internal.*;

public class SimpleList<T> implements GenericList<T>, FixedList<T>, SelectableList<T> {

    public SimpleList(Class<? extends T> itemType) {
        this.itemType = itemType;
    }

    public Class<? extends T> getItemType() {
        return this.itemType;
    }

    public int getCount() {
        return this.items.size();
    }

    public T get(int index) {
        return this.items.get(index);
    }

    public void add(T item, T before) {
        if (before == null) {
            this.items.add(item);
        } else {
            this.items.add(this.items.indexOf(before), item);
        }
    }

    public T add(T before) {
        T item = this.create(null, before);
        this.add(item, before);
        return item;
    }

    public <A extends T> A add(Class<A> type, T before) {
        A item = this.create(type, before);
        this.add(item, before);
        return item;
    }

    public void remove(T item) {
        this.items.remove(item);
    }

    public void move(T item, T before) {
        this.remove(item);
        this.add(item, before);
    }

    public void clear() {
        this.items.clear();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public Iterator<T> iterator() {
        return this.items.iterator();
    }

    protected <A extends T> A create(Class<A> type, T before) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return SimpleObjectFormatter.toString(this);
    }

    public Class<? extends T> itemType;

    public List<T> items = new ArrayList<T>();
}
