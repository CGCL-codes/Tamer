package de.javatt.data.scenario.swt;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Widget;

public class ComboWrapper extends ControlWrapper {

    public int getSelection(Widget widget) {
        Combo cmb = (Combo) widget;
        return cmb.getSelectionIndex();
    }

    public String[] getItems(Widget widget) {
        Combo cmb = (Combo) widget;
        return cmb.getItems();
    }

    public void setSelection(Widget widget, int index) {
        Combo cmb = (Combo) widget;
        cmb.select(index);
    }
}
