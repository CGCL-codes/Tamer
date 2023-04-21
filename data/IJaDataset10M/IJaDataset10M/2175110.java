package com.leclercb.commons.api.event.propertychange;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

public class WeakPropertyChangeListener implements PropertyChangeListener {

    private PropertyChangeSupported support;

    private WeakReference<PropertyChangeListener> reference;

    public WeakPropertyChangeListener(PropertyChangeSupported support, PropertyChangeListener listener) {
        this.support = support;
        this.reference = new WeakReference<PropertyChangeListener>(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PropertyChangeListener listener = this.reference.get();
        if (listener == null) this.support.removePropertyChangeListener(this); else listener.propertyChange(evt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this.reference != null) return this.reference.equals(obj);
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if (this.reference != null) return this.reference.hashCode();
        return super.hashCode();
    }
}
