package org.o14x.migale.script.impl;

import java.util.HashMap;
import org.o14x.migale.script.SpecialObjects;
import org.o14x.migale.script.View;

public class ViewImpl extends HashMap implements View {

    private ThreadLocal m_elt;

    private ThreadLocal m_evt;

    public ViewImpl() {
        m_elt = new ThreadLocal();
        m_evt = new ThreadLocal();
    }

    public Object get(Object key) {
        Object value = null;
        if (SpecialObjects.M_ELT.equals(key)) {
            value = m_elt.get();
        } else if (SpecialObjects.M_EVT.equals(key)) {
            value = m_evt.get();
        } else if (SpecialObjects.M_PARENTFRAME.equals(key)) {
            value = super.get(key);
            if (value == null) {
                View parentView = (View) super.get(SpecialObjects.M_PARENT);
                if (parentView != null) {
                    value = parentView.get(key);
                }
            }
        } else {
            value = super.get(key);
        }
        return value;
    }

    public Object put(Object key, Object value) {
        Object oldValue = get(key);
        if (SpecialObjects.M_ELT.equals(key)) {
            m_elt.set(value);
        } else if (SpecialObjects.M_EVT.equals(key)) {
            m_evt.set(value);
        } else {
            super.put(key, value);
        }
        return oldValue;
    }
}
