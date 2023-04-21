package org.nakedobjects.plugins.dnd.viewer;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;
import org.nakedobjects.plugins.dnd.viewer.util.Properties;
import org.nakedobjects.runtime.context.NakedObjectsContext;

public class AwtText implements Text {

    private static final String ASCENT_ADJUST = Properties.PROPERTY_BASE + "ascent-adjust";

    private static final String FONT_PROPERTY_STEM = Properties.PROPERTY_BASE + "font.";

    private static final Logger LOG = Logger.getLogger(AwtText.class);

    private static final String SPACING_PROPERTYSTEM = Properties.PROPERTY_BASE + "spacing.";

    private final boolean ascentAdjust;

    private Font font;

    private final Frame fontMetricsComponent = new Frame();

    private final int lineSpacing;

    private int maxCharWidth;

    private final FontMetrics metrics;

    private final String propertyName;

    protected AwtText(final String propertyName, final String defaultFont) {
        final NakedObjectConfiguration cfg = NakedObjectsContext.getConfiguration();
        font = cfg.getFont(FONT_PROPERTY_STEM + propertyName, Font.decode(defaultFont));
        LOG.info("font " + propertyName + " loaded as " + font);
        this.propertyName = propertyName;
        if (font == null) {
            font = cfg.getFont(FONT_PROPERTY_STEM + "default", new Font("SansSerif", Font.PLAIN, 12));
        }
        metrics = fontMetricsComponent.getFontMetrics(font);
        maxCharWidth = metrics.getMaxAdvance() + 1;
        if (maxCharWidth == 0) {
            maxCharWidth = (charWidth('X') + 3);
        }
        lineSpacing = cfg.getInteger(SPACING_PROPERTYSTEM + propertyName, 0);
        ascentAdjust = cfg.getBoolean(ASCENT_ADJUST, false);
        LOG.debug("font " + propertyName + " height=" + metrics.getHeight() + ", leading=" + metrics.getLeading() + ", ascent=" + metrics.getAscent() + ", descent=" + metrics.getDescent() + ", line spacing=" + lineSpacing);
    }

    public int charWidth(final char c) {
        return metrics.charWidth(c);
    }

    public int getAscent() {
        return metrics.getAscent() - (ascentAdjust ? metrics.getDescent() : 0);
    }

    /**
     * Returns the Font from the AWT used for drawing within the AWT.
     * 
     * @see Font
     */
    public Font getAwtFont() {
        return font;
    }

    public int getDescent() {
        return metrics.getDescent();
    }

    public int getLineHeight() {
        return metrics.getHeight() + getLineSpacing();
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public String getName() {
        return propertyName;
    }

    public int getMidPoint() {
        return getAscent() / 2;
    }

    public int getTextHeight() {
        return metrics.getHeight() - (ascentAdjust ? metrics.getDescent() : 0);
    }

    public int stringHeight(final String text, final int maxWidth) {
        int noLines = 0;
        final StringTokenizer lines = new StringTokenizer(text, "\n\r");
        while (lines.hasMoreTokens()) {
            final String line = lines.nextToken();
            final StringTokenizer words = new StringTokenizer(line, " ");
            final StringBuffer l = new StringBuffer();
            int width = 0;
            while (words.hasMoreTokens()) {
                final String nextWord = words.nextToken();
                final int wordWidth = stringWidth(nextWord);
                width += wordWidth;
                if (width >= maxWidth) {
                    noLines++;
                    l.setLength(0);
                    width = wordWidth;
                }
                l.append(nextWord);
                l.append(" ");
                width += stringWidth(" ");
            }
            noLines++;
        }
        return noLines * getLineHeight();
    }

    public int stringWidth(final String text, final int maxWidth) {
        int width = 0;
        final StringTokenizer lines = new StringTokenizer(text, "\n\r");
        while (lines.hasMoreTokens()) {
            final String line = lines.nextToken();
            final StringTokenizer words = new StringTokenizer(line, " ");
            final StringBuffer l = new StringBuffer();
            int lineWidth = 0;
            while (words.hasMoreTokens()) {
                final String nextWord = words.nextToken();
                final int wordWidth = stringWidth(nextWord);
                lineWidth += wordWidth;
                if (lineWidth >= maxWidth) {
                    return maxWidth;
                }
                if (lineWidth > width) {
                    width = lineWidth;
                }
                l.append(nextWord);
                l.append(" ");
                lineWidth += stringWidth(" ");
            }
        }
        return width;
    }

    private final java.util.Hashtable stringWidthByString = new java.util.Hashtable();

    public int stringWidth(final String text) {
        int[] cachedStringWidth = (int[]) stringWidthByString.get(text);
        if (cachedStringWidth == null) {
            cachedStringWidth = new int[] { stringWidthInternal(text) };
            stringWidthByString.put(text, cachedStringWidth);
        }
        return cachedStringWidth[0];
    }

    private int stringWidthInternal(final String text) {
        int stringWidth = metrics.stringWidth(text);
        if (stringWidth > text.length() * maxCharWidth) {
            LOG.debug("spurious width of string; calculating manually: " + stringWidth + " for " + this + ": " + text);
            stringWidth = 0;
            for (int i = 0; i < text.length(); i++) {
                int charWidth = charWidth(text.charAt(i));
                if (charWidth > maxCharWidth) {
                    LOG.debug("spurious width of character; using max width: " + charWidth + " for " + text.charAt(i));
                    charWidth = maxCharWidth;
                }
                stringWidth += charWidth;
                LOG.debug(i + " " + stringWidth);
            }
        }
        return stringWidth;
    }

    @Override
    public String toString() {
        return font.toString();
    }
}
