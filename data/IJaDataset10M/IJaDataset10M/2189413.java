package com.wgo.precise.client.ui.view.editor.usertypes;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import com.wgo.precise.client.ui.model.PropertyDefWrapper;
import com.wgo.precise.client.ui.model.PropertySetWrapper;
import com.wgo.precise.client.ui.view.facade.IModelTableEditor;

class UserTypesTableContentProvider implements IStructuredContentProvider, IModelTableEditor<PropertyDefWrapper> {

    private PropertySetWrapper type;

    private TableViewer viewer;

    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        if (v instanceof TableViewer) {
            this.viewer = (TableViewer) v;
            if (newInput instanceof PropertySetWrapper) {
                type = (PropertySetWrapper) newInput;
            } else if (oldInput != null) {
                type = null;
            }
        }
    }

    public void dispose() {
        viewer = null;
        type = null;
    }

    public Object[] getElements(Object parent) {
        if (type != null) {
            if (parent instanceof PropertySetWrapper) {
                return ((PropertySetWrapper) parent).getPopertyDefinitions().toArray();
            }
            System.out.println(getClass().getName() + ".getElements(Object): parent not known");
        }
        return new Object[0];
    }

    public void addElement(PropertyDefWrapper element) {
        type.addPropertyDefinition(element);
        viewer.refresh();
    }

    public void removeElement(PropertyDefWrapper element) {
        viewer.remove(element);
        type.removePropertyDefinition(element);
    }

    public void updateElement(PropertyDefWrapper element) {
        type.updatePropertyDefinition(element);
        viewer.refresh(element);
    }

    public boolean insertAfter(PropertyDefWrapper insert, PropertyDefWrapper after) {
        boolean ok = type.insertAfter(insert, after);
        viewer.refresh();
        return ok;
    }
}
