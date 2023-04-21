package com.pavelfatin.sleeparchiver.swing;

import javax.swing.*;
import java.awt.*;

public class MyFormattedTextField extends JFormattedTextField {

    public MyFormattedTextField(AbstractFormatter formatter) {
        super(formatter);
    }

    public void setPrototype(Object prototype) {
        Object value = getValue();
        setValue(prototype);
        Dimension original = getPreferredSize();
        Dimension updated = new Dimension(original.width + widthOf("_ _ "), original.height);
        setPreferredSize(updated);
        setValue(value);
    }

    private int widthOf(String text) {
        FontMetrics metrics = getFontMetrics(getFont());
        return metrics.stringWidth(text);
    }
}
