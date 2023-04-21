package org.gwtoolbox.widget.client.data;

import org.gwtoolbox.commons.util.client.listener.ChangeListener;
import java.util.Date;

/**
 * @author Uri Boness
 */
public abstract class AbstractRecord implements Record {

    private ChangeListener.Collection<Record> changeListeners = new ChangeListener.Collection<Record>();

    private ChangeListener.Collection<FieldChangeEvent> fieldChangeListeners = new ChangeListener.Collection<FieldChangeEvent>();

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }

    public void clearChangeListeners() {
        changeListeners.clear();
    }

    public void addFieldChangeListener(ChangeListener<FieldChangeEvent> listener) {
        fieldChangeListeners.add(listener);
    }

    public void removeFieldChangeListener(ChangeListener<FieldChangeEvent> listener) {
        fieldChangeListeners.remove(listener);
    }

    public void clearFieldChangeListeners() {
        fieldChangeListeners.clear();
    }

    public Long getLongValue(String fieldName) {
        return (Long) getValue(fieldName);
    }

    public String getStringValue(String fieldName) {
        return (String) getValue(fieldName);
    }

    public Integer getIntValue(String fieldName) {
        return (Integer) getValue(fieldName);
    }

    public Double getDoubleValue(String fieldName) {
        return (Double) getValue(fieldName);
    }

    public Float getFloatValue(String fieldName) {
        return (Float) getValue(fieldName);
    }

    public Boolean getBooleanValue(String fieldName) {
        return (Boolean) getValue(fieldName);
    }

    public Date getDateValue(String fieldName) {
        return (Date) getValue(fieldName);
    }

    protected void notifyChange() {
        changeListeners.fireOnChange(this);
    }

    protected void notifyFieldChanged(String fieldName) {
        fieldChangeListeners.fireOnChange(new FieldChangeEvent(fieldName, this));
    }
}
