package com.leclercb.taskunifier.gui.commons.values;

import org.jdesktop.swingx.renderer.BooleanValue;

public class BooleanValueBoolean implements BooleanValue {

    public static final BooleanValueBoolean INSTANCE = new BooleanValueBoolean();

    private BooleanValueBoolean() {
    }

    @Override
    public boolean getBoolean(Object value) {
        if (value == null || !(value instanceof Boolean)) return false;
        return (Boolean) value;
    }
}
