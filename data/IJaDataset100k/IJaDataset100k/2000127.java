package org.gems.designer.model.props;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.gems.designer.model.meta.MetaType;

public interface CustomProperty {

    public void setCategory(String cat);

    public void setDescription(String desc);

    public Class getJavaType();

    public Object getValue();

    public Object getPropertyValue();

    public void setValue(Object v);

    public IPropertyDescriptor getPropertyDescriptor(String id, String name);

    public MetaType getType();

    public String getMemento();

    public void loadMemento(String memento);
}
