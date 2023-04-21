package listo.utils.swing;

import java.awt.*;

public class SwingUtils {

    public static Color setAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
