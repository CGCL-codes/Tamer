package net.sourceforge.btthud.util;

import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;
import javax.swing.text.*;
import java.util.*;

public class BulkStyledDocument extends DefaultStyledDocument {

    public static final String STYLE_COMMAND = "thud.command";

    public static final String STYLE_HUD_MESSAGE = "thud.hud_message";

    private int maxLines = 1000;

    private static final int NUM_TAB_STOPS = 20;

    private Font font;

    public BulkStyledDocument(final int maxLines, final Font font) {
        this.maxLines = maxLines;
        setFont(font);
        final Style styleBase = getStyle(StyleContext.DEFAULT_STYLE);
        Style styleTemp;
        styleTemp = addStyle(STYLE_COMMAND, styleBase);
        StyleConstants.setForeground(styleTemp, Color.blue);
        styleTemp = addStyle(STYLE_HUD_MESSAGE, styleBase);
        StyleConstants.setForeground(styleTemp, Color.black);
        StyleConstants.setBackground(styleTemp, Color.white);
    }

    public void setFont(final Font font) {
        final FontRenderContext frc = new FontRenderContext(null, false, false);
        final GlyphVector spaceVec = font.createGlyphVector(frc, " ");
        final GlyphMetrics spaceMetrics = spaceVec.getGlyphMetrics(0);
        final int spaceAdvance = (int) spaceMetrics.getAdvance();
        final TabStop[] tabStops = new TabStop[NUM_TAB_STOPS];
        for (int i = 0; i < NUM_TAB_STOPS; i++) {
            tabStops[i] = new TabStop(i * spaceAdvance * 8);
        }
        StyleConstants.setTabSet(getStyle(StyleContext.DEFAULT_STYLE), new TabSet(tabStops));
    }

    public void setMaxLines(final int maxLines) {
        this.maxLines = maxLines;
    }

    /**
	 * Insert a parsed string in bulk.
	 *
	 * @param es The array containing the ElementSpecs
	 */
    public void insertParsedString(final ElementSpec[] es) {
        try {
            insert(getLength(), es);
        } catch (final Exception e) {
            System.out.println("Error: insertParsedString: " + e);
        }
        dropExtraLines();
    }

    public void insertString(final int offset, final String str, final AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);
        dropExtraLines();
    }

    private void dropExtraLines() {
        final Element root = getDefaultRootElement();
        while (root.getElementCount() > maxLines) {
            try {
                remove(0, root.getElement(0).getEndOffset());
            } catch (final BadLocationException e) {
                System.err.println("Error: dropExtraLines: " + e);
            }
        }
    }
}
