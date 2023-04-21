package org.gudy.azureus2.plugins.ui.components;

/**
 * @author parg
 *
 */
public interface UIPropertyChangeEvent {

    public UIComponent getSource();

    public String getPropertyType();

    public Object getNewPropertyValue();

    public Object getOldPropertyValue();
}
