package org.mariella.glue.service;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Timestamp;

public class LostUpdateEntityQueryParameter {

    protected final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private Timestamp createdFrom;

    private Timestamp createdTo;

    private String createdBy;

    private Timestamp modifiedFrom;

    private Timestamp modifiedTo;

    private String modifiedBy;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public Timestamp getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(Timestamp createdFrom) {
        Timestamp old = this.createdFrom;
        this.createdFrom = createdFrom;
        propertyChangeSupport.firePropertyChange("createdFrom", old, createdFrom);
    }

    public Timestamp getCreatedTo() {
        return createdTo;
    }

    public void setCreatedTo(Timestamp createdTo) {
        Timestamp old = this.createdTo;
        this.createdTo = createdTo;
        propertyChangeSupport.firePropertyChange("createdTo", old, createdTo);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        String old = this.createdBy;
        this.createdBy = createdBy;
        propertyChangeSupport.firePropertyChange("createdBy", old, createdBy);
    }

    public Timestamp getModifiedFrom() {
        return modifiedFrom;
    }

    public void setModifiedFrom(Timestamp modifiedFrom) {
        Timestamp old = this.modifiedFrom;
        this.modifiedFrom = modifiedFrom;
        propertyChangeSupport.firePropertyChange("modifiedFrom", old, modifiedFrom);
    }

    public Timestamp getModifiedTo() {
        return modifiedTo;
    }

    public void setModifiedTo(Timestamp modifiedTo) {
        Timestamp old = this.modifiedTo;
        this.modifiedTo = modifiedTo;
        propertyChangeSupport.firePropertyChange("modifiedTo", old, modifiedTo);
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        String old = this.modifiedBy;
        this.modifiedBy = modifiedBy;
        propertyChangeSupport.firePropertyChange("modifiedBy", old, modifiedBy);
    }
}
