package shake.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import shake.annotation.Sort;

public class AnnotationSort {

    private static Class toDelegateClass(Object obj) {
        if (obj instanceof DelegateClass) {
            return ((DelegateClass) obj).getDelegate();
        }
        return obj.getClass();
    }

    public static void sort(Collection collection) {
        sort(collection, new ArrayList<SortItem>());
    }

    public static void sort(Collection collection, List<SortItem> override) {
        List<SortItem> items = createSorteItems(collection, override);
        collection.clear();
        while (!items.isEmpty()) {
            SortItem item = next(items);
            if (item.object != null) collection.add(item.object);
            items.remove(item);
            for (SortItem i : items) {
                i.around.remove(item.targetClass);
                i.within.remove(item.targetClass);
            }
        }
    }

    private static List<SortItem> createSorteItems(Collection<Object> collection, List<SortItem> override) {
        Set<Class> targetClass = toDelegateClass(collection);
        Set<Class> sortKeyClass = toSortKeyClass(targetClass, override);
        ArrayList<SortItem> items = createSortItems(collection, override, sortKeyClass);
        addDummySortItmes(items, override, sortKeyClass);
        return items;
    }

    private static void addDummySortItmes(ArrayList<SortItem> items, List<SortItem> override, Set<Class> sortKeyClass) {
        for (Class key : sortKeyClass) {
            items.add(createSortItem(null, override, sortKeyClass, key));
        }
    }

    private static ArrayList<SortItem> createSortItems(Collection<Object> collection, List<SortItem> override, Set<Class> sortKeyClass) {
        ArrayList<SortItem> items = new ArrayList<SortItem>();
        for (Object o : collection) {
            items.add(createSortItem(o, override, sortKeyClass));
        }
        return items;
    }

    private static SortItem createSortItem(Object o, List<SortItem> override, Set<Class> sortKeyClass) {
        Class delegate = toDelegateClass(o);
        return createSortItem(o, override, sortKeyClass, delegate);
    }

    private static SortItem createSortItem(Object o, List<SortItem> override, Set<Class> sortKeyClass, Class delegate) {
        Class target = toTargetClass(delegate, sortKeyClass);
        Sort com = getCompare(delegate, override);
        Set<Class> around = new HashSet<Class>();
        around.addAll(Arrays.asList(com.around()));
        Set<Class> within = new HashSet<Class>();
        within.addAll(Arrays.asList(com.within()));
        return new SortItem(o, target, around, within);
    }

    private static Class toTargetClass(Class delegate, Set<Class> sortKeyClass) {
        Class c = delegate;
        while (c != null) {
            if (sortKeyClass.contains(c)) {
                return c;
            }
            c = c.getSuperclass();
        }
        return delegate;
    }

    private static Set<Class> toSortKeyClass(Set<Class> targetClass, List<SortItem> override) {
        Set<Class> sortkey = new HashSet<Class>();
        LinkedList<Class> entry = new LinkedList<Class>();
        entry.addAll(targetClass);
        for (SortItem item : override) {
            sortkey.add(item.targetClass);
            entry.add(item.targetClass);
            sortkey.addAll(item.within);
            entry.addAll(item.within);
            sortkey.addAll(item.around);
            entry.addAll(item.around);
        }
        while (!entry.isEmpty()) {
            Class<?> s = entry.poll();
            while (s != null) {
                if (s.isAnnotationPresent(Sort.class)) {
                    sortkey.add(s);
                    Sort com = s.getAnnotation(Sort.class);
                    for (Class a : com.around()) {
                        if (!sortkey.contains(a)) {
                            entry.add(a);
                        }
                    }
                    for (Class a : com.within()) {
                        if (!sortkey.contains(a)) {
                            entry.add(a);
                        }
                    }
                    sortkey.addAll(Arrays.asList(com.around()));
                    sortkey.addAll(Arrays.asList(com.within()));
                    break;
                }
                s = s.getSuperclass();
            }
        }
        return sortkey;
    }

    private static SortItem next(List<SortItem> itmes) {
        for (SortItem item : itmes) {
            if (item.within.isEmpty() && nobadyAround(item.targetClass, itmes)) {
                return item;
            }
        }
        throw new RuntimeException("don't sort itmes:" + itmes);
    }

    private static boolean nobadyAround(Class key, List<SortItem> itmes) {
        for (SortItem item : itmes) {
            if (item.around.contains(key)) {
                return false;
            }
        }
        return true;
    }

    private static Set<Class> toDelegateClass(Collection<?> collection) {
        Set<Class> set = new HashSet<Class>();
        for (Object o : collection) {
            set.add(toDelegateClass(o));
        }
        return set;
    }

    static class CompareImpl implements Sort {

        Class[] around;

        Class[] within;

        public CompareImpl(Class[] around, Class[] within) {
            this.around = around;
            this.within = within;
        }

        public CompareImpl(Set<Class> around, Set<Class> within) {
            this.around = around.toArray(new Class[around.size()]);
            this.within = within.toArray(new Class[within.size()]);
        }

        public Class[] around() {
            return around;
        }

        public Class[] within() {
            return within;
        }

        public Class<? extends Annotation> annotationType() {
            return Sort.class;
        }
    }

    ;

    private static Sort getCompare(Class<?> t, List<SortItem> override) {
        while (t != null) {
            for (SortItem item : override) {
                if (item.targetClass.equals(t)) {
                    return new CompareImpl(item.around, item.within);
                }
            }
            if (t.isAnnotationPresent(Sort.class)) {
                return t.getAnnotation(Sort.class);
            }
            t = t.getSuperclass();
        }
        return new CompareImpl(new Class[0], new Class[0]);
    }
}
