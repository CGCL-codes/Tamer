package de.lema.ui.shared;

import java.util.HashMap;
import java.util.Map;

public class ReverseEnumMap<E extends Enum<E> & ReversibleEnum<E, V>, V> {

    private Map<V, E> map = new HashMap<V, E>();

    public ReverseEnumMap(Class<E> valueType) {
        for (E e : valueType.getEnumConstants()) {
            map.put(e.convert(), e);
        }
    }

    public E get(V value) {
        return map.get(value);
    }

    public static <E extends Enum<E> & ReversibleEnum<E, V>, V> ReverseEnumMap<E, V> create(Class<E> valueType) {
        return new ReverseEnumMap<E, V>(valueType);
    }
}
