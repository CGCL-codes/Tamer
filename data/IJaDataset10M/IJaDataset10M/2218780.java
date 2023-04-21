package net.sealisland.swing.util;

import java.awt.Dimension;
import java.awt.Insets;

public class DimensionToolkit {

    public static Dimension restrict(Dimension size, int minWidth, int maxWidth, int minHeight, int maxHeight) {
        size.width = Math.max(minWidth, Math.min(maxWidth, size.width));
        size.height = Math.max(minHeight, Math.min(maxHeight, size.height));
        return size;
    }

    public static Dimension infiniteWidth(Dimension size) {
        size.width = Short.MAX_VALUE;
        return size;
    }

    public static Dimension infiniteHeight(Dimension size) {
        size.height = Short.MAX_VALUE;
        return size;
    }

    public static Dimension infiniteSize() {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    public static Dimension add(Dimension size, Insets insets) {
        size.width += insets.left;
        size.width += insets.right;
        size.height += insets.top;
        size.height += insets.bottom;
        return size;
    }

    public static Dimension minimumWidthMaximumHeight(Dimension... sizes) {
        Dimension size = new Dimension(Short.MAX_VALUE, 0);
        for (Dimension size2 : sizes) {
            size.width = Math.min(size.width, size2.width);
            size.height = Math.max(size.height, size2.height);
        }
        return size;
    }
}
