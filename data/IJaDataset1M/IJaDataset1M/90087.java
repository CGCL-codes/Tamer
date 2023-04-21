package com.global360.sketchpadbpmn.propertiespanel;

import java.awt.Component;
import javax.swing.text.JTextComponent;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DoubleTableCell extends StringTableCell {

    private static final long serialVersionUID = 1L;

    private DoubleProperty value = new DoubleProperty();

    public DoubleTableCell() {
    }

    public Component getComponent(Object value, boolean isEnabled) {
        JTextComponent result = (JTextComponent) super.getComponent(value, isEnabled);
        boolean isValid = this.value.setContents(value.toString());
        if (!isValid) {
            result.setBackground(PropertiesPanel.INVALID_CELL_BACKGROUND_COLOR);
        }
        initializeComponent(this.component, isEnabled);
        return result;
    }
}
