package org.jowidgets.spi.impl.javafx.util;

import org.jowidgets.common.color.ColorValue;
import org.jowidgets.common.color.IColorConstant;
import org.jowidgets.util.EmptyCheck;

public final class ColorCSSConverter {

    private ColorCSSConverter() {
    }

    public static IColorConstant cssToColor(final String csscode) {
        if (!EmptyCheck.isEmpty(csscode)) {
            final String hex = csscode.substring(csscode.indexOf("#") + 1, csscode.indexOf(";"));
            final int r = Integer.parseInt(hex.substring(0, 1), 16);
            final int g = Integer.parseInt(hex.substring(2, 3), 16);
            final int b = Integer.parseInt(hex.substring(4, 5), 16);
            return new ColorValue(r, g, b);
        } else {
            return null;
        }
    }

    public static String colorToCSS(final IColorConstant color) {
        if (color != null) {
            final String colorString = Integer.toHexString(0x100 | color.getDefaultValue().getRed()).substring(1) + "" + Integer.toHexString(0x100 | color.getDefaultValue().getGreen()).substring(1) + "" + Integer.toHexString(0x100 | color.getDefaultValue().getBlue()).substring(1);
            return colorString;
        }
        return "";
    }
}
