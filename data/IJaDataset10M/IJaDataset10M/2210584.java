package pl.olek.textmash.matching;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.text.JTextComponent;

/**
 * 
 * @author anaszko
 *
 */
public class BracketMatchingResult {

    public int start;

    public int end;

    char cstart;

    char cend;

    public void paint(Graphics g, JTextComponent component) {
        try {
            Font pfont = component.getFont();
            FontMetrics pmet = g.getFontMetrics(pfont);
            int fontHeight = pmet.getHeight();
            int fontLeading = pmet.getLeading();
            g.setColor(Color.LIGHT_GRAY);
            if (end != -1) {
                Rectangle we = component.modelToView(end);
                int ew = pmet.charWidth(cend);
                g.drawRect(we.x, we.y + fontLeading, ew, fontHeight);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
