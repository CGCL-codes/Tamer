package com.izforge.izpack.panels;

import javax.swing.ButtonGroup;

/**
 * Additional metadata for radio buttons.
 * 
 * @author Dennis Reil
 */
public class RadioButtonUIElement extends UIElement {

    public RadioButtonUIElement() {
        super();
    }

    ButtonGroup buttonGroup;

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public void setButtonGroup(ButtonGroup buttonGroup) {
        this.buttonGroup = buttonGroup;
    }
}
