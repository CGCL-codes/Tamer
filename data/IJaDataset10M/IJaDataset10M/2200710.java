package kanakata.ui;

import java.awt.Component;
import java.awt.Font;

public final class FontHelper {

    public static void deriveFont(Component component, float size) {
        Font font = component.getFont();
        component.setFont(font.deriveFont(size));
    }
}
