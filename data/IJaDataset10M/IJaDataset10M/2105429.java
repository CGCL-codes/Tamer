package com.bluebrim.base.shared;

import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-10-30 16:48:50)
 * @author: Dennis
 */
public class CoAppendingListProxy extends AbstractList implements java.io.Serializable {

    private List m_list1;

    private List m_list2;

    public CoAppendingListProxy(List l1, List l2) {
        super();
        set(l1, l2);
    }

    public Object get(int index) {
        if (index < m_list1.size()) {
            return m_list1.get(index);
        } else {
            return m_list2.get(index - m_list1.size());
        }
    }

    public Iterator iterator() {
        return new Iterator() {

            Iterator m_i1 = m_list1.iterator();

            Iterator m_i2 = m_list2.iterator();

            Iterator m_i = m_i1;

            public boolean hasNext() {
                return m_i1.hasNext() || m_i2.hasNext();
            }

            public Object next() {
                if (m_i1.hasNext()) {
                    return m_i1.next();
                } else {
                    m_i = m_i2;
                    return m_i2.next();
                }
            }

            public void remove() {
                m_i.remove();
            }
        };
    }

    public void set(List l1, List l2) {
        com.bluebrim.base.shared.debug.CoAssertion.assertTrue(((l1 == null) && (l2 == null)) || ((l1 != null) && (l2 != null)), "Illegal parameters");
        m_list1 = l1;
        m_list2 = l2;
    }

    public int size() {
        return m_list1.size() + m_list2.size();
    }
}
