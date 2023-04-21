package org.jtools.mapper;

import java.util.Map;
import org.jpattern.mapper.Mapper;

public class EntryChooser<T_Return, T_Key> implements Mapper<T_Return, Map<T_Key, ? extends T_Return>> {

    private final T_Key key;

    public EntryChooser(T_Key key) {
        this.key = key;
    }

    public T_Return map(Map<T_Key, ? extends T_Return> item) {
        if (item == null) return null;
        return item.get(key);
    }
}
