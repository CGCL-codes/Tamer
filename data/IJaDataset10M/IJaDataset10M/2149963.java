package com.ibm.realtime.flexotask.editor.dialogs;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * A PropertyDescriptor whose cell editor builds a list of classes
 */
public class ClassListPropertyDescriptor extends PropertyDescriptor {

    /**
   * Pass-through constructor
   */
    public ClassListPropertyDescriptor(Object id, String displayName) {
        super(id, displayName);
    }

    public CellEditor createPropertyEditor(Composite parent) {
        return new ClassListCellEditor(parent);
    }
}
