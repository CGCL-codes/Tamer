package org.gamenet.swing.controls;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;

public class ByteTextField extends JFormattedTextField implements PropertyChangeListener {

    private ByteValueHolder byteValueHolder = null;

    public ByteTextField(ByteValueHolder srcByteValueHolder) {
        super(new Byte(srcByteValueHolder.getValue()));
        this.byteValueHolder = srcByteValueHolder;
        this.setColumns(3);
        this.addPropertyChangeListener("value", this);
    }

    public void propertyChange(PropertyChangeEvent e) {
        byteValueHolder.setValue(((Number) ((JFormattedTextField) e.getSource()).getValue()).byteValue());
    }

    public ByteValueHolder getByteValueHolder() {
        return byteValueHolder;
    }
}
