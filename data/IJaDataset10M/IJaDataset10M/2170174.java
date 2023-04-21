package org.scribble.conformance.model;

import org.scribble.model.*;

/**
 * This class provides an iterator implementation over a behaviour list.
 */
public class BehaviourListIterator implements java.util.Iterator<Behaviour> {

    public BehaviourListIterator(BehaviourList list) {
        m_list = list;
    }

    protected BehaviourListIterator(BehaviourListIterator iter) {
        m_list = iter.m_list;
        m_index = iter.m_index;
        if (iter.m_subList != null) {
            m_subList = iter.m_subList.snapshot();
        }
    }

    public BehaviourListIterator snapshot() {
        return (new BehaviourListIterator(this));
    }

    public boolean hasNext() {
        boolean ret = false;
        if (m_index < m_list.getBehaviourList().size() || (m_subList != null && m_subList.hasNext())) {
            ret = true;
        }
        return (ret);
    }

    public Behaviour next() {
        Behaviour ret = null;
        boolean f_end = false;
        do {
            if (m_subList != null) {
                if (m_subList.hasNext()) {
                    ret = m_subList.next();
                } else {
                    m_subList = null;
                }
            } else if (m_index < m_list.getBehaviourList().size()) {
                ret = m_list.getBehaviourList().get(m_index++);
                if (ret instanceof BehaviourList) {
                    m_subList = ((BehaviourList) ret).getIterator();
                    ret = null;
                }
            } else {
                f_end = true;
            }
        } while (ret == null && f_end == false);
        return (ret);
    }

    public void remove() {
    }

    public ModelReference getComposedSource() {
        ModelReference ret = null;
        if (m_subList != null) {
            ret = m_subList.getComposedSource();
        } else if (m_list.getModelInclude() != null) {
            ret = m_list.getModelInclude().getReference();
        }
        return (ret);
    }

    public ModelInclude getModelInclude() {
        ModelInclude ret = m_list.getModelInclude();
        if (ret == null && m_subList != null) {
            ret = m_subList.getModelInclude();
        }
        return (ret);
    }

    public org.scribble.comparator.NameMap getNameMap() {
        org.scribble.comparator.NameMap ret = m_list;
        if (m_subList != null) {
            ret = m_subList.getNameMap();
        }
        return (ret);
    }

    private BehaviourList m_list = null;

    private int m_index = 0;

    private BehaviourListIterator m_subList = null;
}
