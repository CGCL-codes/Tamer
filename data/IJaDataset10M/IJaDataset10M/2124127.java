package net.ar.guia.own.implementation.components;

import net.ar.guia.own.interfaces.*;

public class VisualCheckBox extends VisualButton implements CheckBoxComponent {

    public VisualCheckBox() {
        init(CheckBoxComponent.class);
    }

    public VisualCheckBox(String aText) {
        this();
        setCaption(aText);
    }
}
