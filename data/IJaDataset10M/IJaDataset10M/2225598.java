package net.sf.contrail.core.impl;

import java.util.Comparator;
import net.sf.contrail.core.Item;
import net.sf.contrail.core.ContrailQuery.SortDirection;
import net.sf.contrail.core.ContrailQuery.SortPredicate;

@SuppressWarnings("unchecked")
public class PropertyComparator<T extends Item> implements Comparator<T> {

    String _propertyName;

    boolean _ascending = true;

    public PropertyComparator(SortPredicate sortPredicate) {
        _propertyName = sortPredicate.getPropertyName();
        if (SortDirection.DESCENDING == sortPredicate.getDirection()) _ascending = false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int compare(T o1, T o2) {
        Object p1 = o1.getProperty(_propertyName);
        Object p2 = o2.getProperty(_propertyName);
        if (p1 == null) return (p2 == null) ? 0 : _ascending ? -1 : 1;
        if (p2 == null) return _ascending ? 1 : -1;
        int result = ((Comparable) p1).compareTo(p2);
        if (_ascending) return result;
        if (result == 0) return 0;
        return (0 < result) ? -1 : 1;
    }
}
