package org.plc.util.model;

import java.beans.PropertyChangeListener;

/**
 * @author pierreluc
 * @date 2010-09-24
 *
 */
public interface Model {

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public boolean isModified();

    public boolean save();

    public void refresh();
}
