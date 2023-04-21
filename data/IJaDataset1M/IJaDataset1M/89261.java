package cn.edu.wuse.musicxml.symbol;

import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

public class DotSymbol extends MusicSymbol {

    private String placement;

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        glyph.setFont(glyph.getFont().deriveFont(fontstyle | fontweight, fontsize));
        g2.setFont(glyph.getFont());
        g2.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector vector = glyph.getFont().createGlyphVector(frc, new int[] { glyph.getDOT() });
        g2.drawGlyphVector(vector, (float) point.getX(), (float) point.getY());
    }

    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }
}
