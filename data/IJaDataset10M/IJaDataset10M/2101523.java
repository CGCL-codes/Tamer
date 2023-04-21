package com.leclercb.taskunifier.gui.api.models.beans.converters;

import java.awt.Color;
import com.thoughtworks.xstream.converters.SingleValueConverter;

public class ColorConverter implements SingleValueConverter {

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class cls) {
        return Color.class.isAssignableFrom(cls);
    }

    @Override
    public Object fromString(String value) {
        if (value == null || value.length() == 0) return null;
        return new Color(Integer.parseInt(value));
    }

    @Override
    public String toString(Object value) {
        return ((Color) value).getRGB() + "";
    }
}
