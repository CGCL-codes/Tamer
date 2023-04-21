package de.schlund.pfixxml.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;

/**
 * Utility class that provides methods for converting collections.  
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class Generics {

    private Generics() {
    }

    ;

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> convertCollection(Collection<?> collection) {
        for (Iterator<?> i = collection.iterator(); i.hasNext(); ) {
            T element = (T) i.next();
            if (element == null) {
            }
        }
        return (Collection<T>) collection;
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> convertSet(Set<?> set) {
        for (Iterator<?> i = set.iterator(); i.hasNext(); ) {
            T element = (T) i.next();
            if (element == null) {
            }
        }
        return (Set<T>) set;
    }

    @SuppressWarnings("unchecked")
    public static <T> SortedSet<T> convertSortedSet(SortedSet<?> set) {
        for (Iterator<?> i = set.iterator(); i.hasNext(); ) {
            T element = (T) i.next();
            if (element == null) {
            }
        }
        return (SortedSet<T>) set;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> convertList(List<?> list) {
        for (Iterator<?> i = list.iterator(); i.hasNext(); ) {
            T element = (T) i.next();
            if (element == null) {
            }
        }
        return (List<T>) list;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T1, T2> Map<T1, T2> convertMap(Map<?, ?> map) {
        for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
            Entry entry = (Entry) i.next();
            T1 key = (T1) entry.getKey();
            T2 value = (T2) entry.getValue();
            if (key == null && value == null) {
            }
        }
        return (Map<T1, T2>) map;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T1, T2> SortedMap<T1, T2> convertSortedMap(SortedMap<?, ?> map) {
        for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
            Entry entry = (Entry) i.next();
            T1 key = (T1) entry.getKey();
            T2 value = (T2) entry.getValue();
            if (key == null && value == null) {
            }
        }
        return (SortedMap<T1, T2>) map;
    }
}
