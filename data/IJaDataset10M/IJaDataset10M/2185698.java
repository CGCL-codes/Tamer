package net.wotonomy.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.wotonomy.foundation.NSArray;
import net.wotonomy.foundation.NSMutableArray;
import net.wotonomy.foundation.NSSelector;

/**
* EOSortOrdering defines a sort key and operation.
* DisplayGroups use lists of EOSortOrdering to determine
* how to order their items.  
*
* @author michael@mpowers.net
* @author $Author: cgruber $
* @version $Revision: 894 $
*/
public class EOSortOrdering implements Serializable, EOKeyValueArchiving {

    /**
    * Sorts items in ascending order.
    */
    public static final NSSelector CompareAscending = new CompareAscendingComparator();

    /**
    * Sorts items in descending order.
    */
    public static final NSSelector CompareDescending = new CompareDescendingComparator();

    /**
    * Sorts items' string representations in ascending order
    * in a case insensitive manner.
    */
    public static final NSSelector CompareCaseInsensitiveAscending = new CompareCaseInsensitiveAscendingComparator();

    /**
    * Sorts items' string representations in descending order
    * in a case insensitive manner.
    */
    public static final NSSelector CompareCaseInsensitiveDescending = new CompareCaseInsensitiveDescendingComparator();

    protected String key;

    protected NSSelector selector;

    /**
    * Factory-style constructor returns a new EOSortOrdering instance
    * with the specified key and selector.  Neither may be null.
    */
    public static EOSortOrdering sortOrderingWithKey(String key, NSSelector selector) {
        return new EOSortOrdering(key, selector);
    }

    /**
    * Constructor creates an EOSortOrdering that uses the 
    * specified key and selector.  Neither may be null.
    */
    public EOSortOrdering(String aKey, NSSelector aSelector) {
        key = aKey;
        selector = aSelector;
    }

    /**
    * Constructor creates an EOSortOrdering that uses the 
    * specified key and comparator.  Neither may be null.
    * Not in the spec.
    */
    public EOSortOrdering(String aKey, Comparator aComparator) {
        key = aKey;
        selector = new NSSelector(aKey, aComparator);
    }

    /**
    * Returns the property key.
    */
    public String key() {
        return key;
    }

    /**
    * Returns the selector.
    */
    public NSSelector selector() {
        return selector;
    }

    public String toString() {
        return "[EOSortOrdering: key='" + key + "' selector='" + selector + "']";
    }

    public boolean equals(Object anObject) {
        if (anObject instanceof EOSortOrdering) {
            EOSortOrdering x = (EOSortOrdering) anObject;
            if (selector().equals(x.selector())) {
                if (key().equals(x.key())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * Sorts the specified list in place according to the specified
    * list of EOSortOrderings.  The items will be sorted first by the
    * first ordering, and items with equal values for that property 
    * will be sorted by the next ordering, and so on.
    */
    public static void sortArrayUsingKeyOrderArray(List anObjectList, List aSortOrderingList) {
        List keys = new ArrayList(aSortOrderingList);
        Collections.reverse(keys);
        Iterator it = keys.iterator();
        EOSortOrdering sortOrdering;
        while (it.hasNext()) {
            sortOrdering = (EOSortOrdering) it.next();
            Collections.sort(anObjectList, new DelegatingComparator(sortOrdering.key(), sortOrdering.selector()));
        }
    }

    /**
    * Sorts the specified list in place according to the specified
    * list of EOSortOrderings.  The items will be sorted first by the
    * first ordering, and items with equal values for that property 
    * will be sorted by the next ordering, and so on.
    */
    public static NSArray sortedArrayUsingKeyOrderArray(List anObjectList, List aSortOrderingList) {
        NSArray result = new NSMutableArray();
        result.addAll(anObjectList);
        sortArrayUsingKeyOrderArray(result, aSortOrderingList);
        return result;
    }

    public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver arch) {
        String k = (String) arch.decodeObjectForKey("key");
        String sname = (String) arch.decodeObjectForKey("selectorName");
        NSSelector sel = null;
        if (sname.equals("compareAscending:")) sel = CompareAscending; else if (sname.equals("compareDescending:")) sel = CompareDescending; else if (sname.equals("compareCaseInsensitiveAscending:")) sel = CompareCaseInsensitiveAscending; else if (sname.equals("compareCaseInsensitiveDescending:")) sel = CompareCaseInsensitiveAscending; else {
            if (sname.endsWith(":")) sname = sname.substring(0, sname.length() - 1);
            sel = new NSSelector(sname, new Class[] { Object.class });
        }
        return new EOSortOrdering(k, sel);
    }

    public void encodeWithKeyValueArchiver(EOKeyValueArchiver arch) {
        arch.encodeObject("EOSortOrdering", "class");
        arch.encodeObject(key(), "key");
        if (selector.equals(CompareAscending)) arch.encodeObject("compareAscending:", "selectorName"); else if (selector.equals(CompareDescending)) arch.encodeObject("compareDescending:", "selectorName"); else if (selector.equals(CompareCaseInsensitiveAscending)) arch.encodeObject("compareCaseInsensitiveAscending:", "selectorName"); else if (selector.equals(CompareCaseInsensitiveAscending)) arch.encodeObject("compareCaseInsensitiveDescending:", "selectorName"); else arch.encodeObject(selector.name() + ":", "selectorName");
    }

    private static class CompareAscendingComparator extends NSSelector {

        public int compare(Object o1, Object o2) {
            if (o1 instanceof Comparable) {
                if (o2 instanceof Comparable) {
                    return ((Comparable) o1).compareTo(o2);
                }
            }
            if (o1 == null) {
                if (o2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (o2 == null) {
                return 1;
            }
            return o1.toString().compareTo(o2.toString());
        }

        public boolean equals(Object obj) {
            return (this == obj);
        }
    }

    private static class CompareDescendingComparator extends CompareAscendingComparator {

        public int compare(Object o1, Object o2) {
            return -1 * super.compare(o1, o2);
        }
    }

    private static class CompareCaseInsensitiveAscendingComparator extends NSSelector {

        public int compare(Object o1, Object o2) {
            if (o1 == null) {
                if (o2 == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (o2 == null) {
                return 1;
            }
            return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
        }

        public boolean equals(Object obj) {
            return (this == obj);
        }
    }

    private static class CompareCaseInsensitiveDescendingComparator extends CompareCaseInsensitiveAscendingComparator {

        public int compare(Object o1, Object o2) {
            return -1 * super.compare(o1, o2);
        }
    }

    private static class DelegatingComparator implements Comparator {

        private String key;

        private Comparator comparator;

        public DelegatingComparator(String aKey, Comparator aComparator) {
            key = aKey;
            comparator = aComparator;
        }

        public int compare(Object o1, Object o2) {
            Object v1, v2;
            if (o1 instanceof EOKeyValueCoding) {
                v1 = ((EOKeyValueCoding) o1).valueForKey(key);
            } else {
                v1 = EOKeyValueCodingSupport.valueForKey(o1, key);
            }
            if (o2 instanceof EOKeyValueCoding) {
                v2 = ((EOKeyValueCoding) o2).valueForKey(key);
            } else {
                v2 = EOKeyValueCodingSupport.valueForKey(o2, key);
            }
            return comparator.compare(v1, v2);
        }

        public boolean equals(Object obj) {
            return (this == obj);
        }
    }
}
