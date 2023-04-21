package graphlab.ui.components.GPropertyEditor.editors.inplace;

import graphlab.main.lang.ArrayX;
import java.awt.*;

/**
 * @author Azin Azadi
 */
public class ArrayXEditor extends GComboEditor {

    ArrayX x;

    public ArrayXEditor(ArrayX x) {
        this.x = x;
    }

    public Component getEditorComponent(Object value) {
        x = (ArrayX) value;
        return super.getEditorComponent(value);
    }

    public Object getSelectedItem() {
        Object o = super.getSelectedItem();
        x.setValue(o);
        return x;
    }

    public Object[] getValues() {
        return x.getValidValues();
    }
}
