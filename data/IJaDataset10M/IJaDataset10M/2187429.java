package org.butu.gui.widgets;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Combo;

public class ComboFacadeString {

    private ComboViewer viewer;

    public ComboFacadeString(Combo combo, Object input) {
        viewer = new ComboViewer(combo);
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setInput(input);
    }

    public String getValue() {
        return (String) ((StructuredSelection) viewer.getSelection()).getFirstElement();
    }

    public void setValue(String object) {
        viewer.setSelection(new StructuredSelection(new Object[] { object }));
    }
}
