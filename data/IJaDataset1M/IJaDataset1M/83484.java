package org.netxilia.api.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Different utility methods for collections
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class CollectionUtils {

    @SuppressWarnings("rawtypes")
    public static final IListElementCreator NULL_ELEMENT_CREATOR = new IListElementCreator<Object>() {

        @Override
        public Object newElement(int index) {
            return null;
        }
    };

    public static <T> Iterable<T> iterable(final Iterator<T> iterator) {
        return new Iterable<T>() {

            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }

    public static String join(Collection<?> collection, String sep) {
        StringBuilder s = new StringBuilder();
        for (Object obj : collection) {
            if (s.length() > 0) {
                s.append(sep);
            }
            if (obj == null) {
                s.append("");
            } else {
                s.append(obj.toString());
            }
        }
        return s.toString();
    }

    public static <E> IListElementCreator<E> sameElementCreator(final E e) {
        return new IListElementCreator<E>() {

            @Override
            public E newElement(int index) {
                return e;
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> atLeastSize(List<T> list, int size, IListElementCreator<T> elementCreator) {
        if (size <= list.size()) {
            return list;
        }
        IListElementCreator<T> creator = elementCreator;
        if (creator == null) {
            creator = NULL_ELEMENT_CREATOR;
        }
        while (list.size() < size) {
            list.add(creator.newElement(list.size()));
        }
        return list;
    }
}
