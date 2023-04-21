package com.g2d.editor.property;

import java.awt.Component;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import com.cell.reflect.Parser;

public class NumberCellEdit extends JSpinner implements PropertyCellEdit<Object> {

    final Class<?> type;

    public NumberCellEdit(Class<?> type, Object data) {
        super(new SpinnerNumberModel(new Double(0), new Double(-Double.MAX_VALUE), new Double(Double.MAX_VALUE), new Double(1)));
        this.type = type;
        if (data != null) {
            this.setValue(data);
        }
    }

    @Override
    public void setValue(Object value) {
        super.setValue(Parser.castNumber(value, Double.class));
    }

    public Object getValue() {
        if (type != null) {
            Object ret = Parser.castNumber(super.getValue(), type);
            return ret;
        }
        return 0;
    }

    public Component getComponent(ObjectPropertyEdit panel) {
        return this;
    }
}
