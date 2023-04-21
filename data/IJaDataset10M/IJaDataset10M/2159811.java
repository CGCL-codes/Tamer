package interfaces;

import org.fenggui.render.Font;
import org.fenggui.util.Alphabet;
import org.fenggui.util.Color;
import org.fenggui.util.fonttoolkit.FontFactory;

public class GUISource {

    public static Font smallFont, labelFont, buttonFont;

    public static final Color green = new Color(0.15f, 0.9f, 0.15f);

    public static void init(int displayWidth) {
        buttonFont = FontFactory.renderStandardFont(new java.awt.Font("Sans", java.awt.Font.BOLD, displayWidth / 35), true, Alphabet.getDefaultAlphabet());
        labelFont = FontFactory.renderStandardFont(new java.awt.Font("Sans", java.awt.Font.BOLD, displayWidth / 50), true, Alphabet.getDefaultAlphabet());
        smallFont = FontFactory.renderStandardFont(new java.awt.Font("Sans", java.awt.Font.BOLD, displayWidth / 65), true, Alphabet.getDefaultAlphabet());
    }
}
