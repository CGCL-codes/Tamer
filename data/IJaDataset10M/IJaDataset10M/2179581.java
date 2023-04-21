package org.dyno.visual.swing.types;

import org.dyno.visual.swing.base.ResourceImage;
import org.dyno.visual.swing.plugin.spi.ICellEditorFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

public class ImageEditor extends ImageWrapper implements ICellEditorFactory {

    private static final long serialVersionUID = -4403435758517308113L;

    public CellEditor createPropertyEditor(Object bean, Composite parent) {
        return new IconCellEditor(parent);
    }

    public Object decodeValue(Object value) {
        if (value == null) return null;
        if (value.equals("")) return null;
        if (value.equals("null")) return null;
        String string = (String) value;
        return new ResourceImage(string);
    }

    public Object encodeValue(Object value) {
        if (value == null) return null;
        return value.toString();
    }
}
