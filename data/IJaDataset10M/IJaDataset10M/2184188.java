package com.antlersoft.query;

import java.util.Comparator;
import java.util.Enumeration;
import com.antlersoft.util.IteratorEnumeration;

public class SetIntersection extends SetOperator {

    protected Object determineNext(SetOperatorSortedEnum e) {
        Object result = null;
        while (e.nextPairInOrder()) {
            if (e.m_comp.compare(e.m_current_a, e.m_current_b) == 0) {
                result = e.m_current_a;
                break;
            }
        }
        return result;
    }

    protected Enumeration getEnumerationFromSets() {
        return new IteratorEnumeration(m_set_both.iterator());
    }
}
