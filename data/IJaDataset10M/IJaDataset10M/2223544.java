package com.intellij.uiDesigner.lw;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * @author yole
 */
public class ColorDescriptor extends Color {

    private Color myColor;

    private String mySwingColor;

    private String mySystemColor;

    private String myAWTColor;

    public static ColorDescriptor fromSwingColor(final String swingColor) {
        ColorDescriptor result = new ColorDescriptor(UIManager.getColor(swingColor));
        result.myColor = null;
        result.mySwingColor = swingColor;
        return result;
    }

    public static ColorDescriptor fromSystemColor(final String systemColor) {
        ColorDescriptor result = new ColorDescriptor(getColorField(SystemColor.class, systemColor));
        result.myColor = null;
        result.mySystemColor = systemColor;
        return result;
    }

    public static ColorDescriptor fromAWTColor(final String awtColor) {
        ColorDescriptor result = new ColorDescriptor(getColorField(Color.class, awtColor));
        result.myColor = null;
        result.myAWTColor = awtColor;
        return result;
    }

    private static Color getColorField(final Class aClass, final String fieldName) {
        try {
            final Field field = aClass.getDeclaredField(fieldName);
            return (Color) field.get(null);
        } catch (NoSuchFieldException e) {
            return Color.black;
        } catch (IllegalAccessException e) {
            return Color.black;
        }
    }

    public ColorDescriptor(final Color color) {
        super(color != null ? color.getRGB() : 0);
        myColor = color;
    }

    public Color getResolvedColor() {
        return new Color(getRGB());
    }

    public Color getColor() {
        return myColor;
    }

    public String getSwingColor() {
        return mySwingColor;
    }

    public String getSystemColor() {
        return mySystemColor;
    }

    public String getAWTColor() {
        return myAWTColor;
    }

    public String toString() {
        if (mySwingColor != null) {
            return mySwingColor;
        }
        if (mySystemColor != null) {
            return mySystemColor;
        }
        if (myAWTColor != null) {
            return myAWTColor;
        }
        if (myColor != null) {
            return "[" + getRed() + "," + getGreen() + "," + getBlue() + "]";
        }
        return "null";
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ColorDescriptor)) {
            return false;
        }
        ColorDescriptor rhs = (ColorDescriptor) obj;
        if (myColor != null) {
            return myColor.equals(rhs.myColor);
        }
        if (mySwingColor != null) {
            return mySwingColor.equals(rhs.mySwingColor);
        }
        if (mySystemColor != null) {
            return mySystemColor.equals(rhs.mySystemColor);
        }
        if (myAWTColor != null) {
            return myAWTColor.equals(rhs.myAWTColor);
        }
        return false;
    }

    public boolean isColorSet() {
        return myColor != null || mySwingColor != null || mySystemColor != null || myAWTColor != null;
    }
}
