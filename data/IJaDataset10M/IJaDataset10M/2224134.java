package cn.edu.bit.whitesail.utils;

import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 * @version 0.1
 * @author baifan
 * @since JDK 1.6
 */
public class MyContainer<T> implements Container<T> {

    private final Set<T> container = new HashSet<T>();

    @Override
    public boolean add(T t) {
        synchronized (container) {
            return container.add(t);
        }
    }

    @Override
    public boolean contains(T t) {
        synchronized (container) {
            return container.contains(t);
        }
    }
}
