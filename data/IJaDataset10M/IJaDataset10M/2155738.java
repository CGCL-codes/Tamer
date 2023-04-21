package org.modss.facilitator.util.glyph;

import java.awt.*;

/**
 * Implements a text glyph.
 */
public class TextGlyph extends Glyph {

    public String text = null;

    public Font font = null;

    public TextGlyph(String text, Font font) {
        this.text = text;
        this.font = font;
    }

    public void draw(Graphics g) {
        g.setColor(getColor());
        if (text == null) {
            return;
        }
        Font oldFont = g.getFont();
        if (font != null) {
            g.setFont(font);
        }
        g.drawString(text, loc.x, loc.y);
    }

    public String toString() {
        return "TextGlyph[loc=" + loc + ", text=" + text + ",font=" + font + "]";
    }
}
