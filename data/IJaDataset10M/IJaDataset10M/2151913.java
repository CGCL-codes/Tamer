package org.netxilia.server.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Use to simulate a list larger than its normal size. All the missing elements are lazily resolved by the element
 * resolver (for example returning the same element). Thus a list with identical elements can be easily simulated
 * without taking up the need memory.
 * 
 * WARNING: as the same object (maybe with its internal state modified) is returned each time, the object is valid after
 * a call to get method and BEFORE another call to the same method!
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 * @param <T>
 */
public class LazyArrayList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 3473954523203990445L;

    private int totalSize;

    private ILazyElementResolver<T> lazyElementResolver;

    public LazyArrayList(int totalSize, ILazyElementResolver<T> lazyElementResolver) {
        this.totalSize = totalSize;
        this.lazyElementResolver = lazyElementResolver;
    }

    @Override
    public T get(int index) {
        if (index >= totalSize) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + totalSize);
        if (index < super.size()) return super.get(index);
        return lazyElementResolver.get(index);
    }

    @Override
    public int size() {
        return Math.max(totalSize, super.size());
    }

    public static void main(String[] args) {
        List<String> list = new LazyArrayList<String>(5, new ILazyElementResolver<String>() {

            public String get(int index) {
                return "lazy";
            }
        });
        list.add("a");
        list.add("b");
        list.add("c");
        for (String s : list) System.out.println(s);
    }
}
