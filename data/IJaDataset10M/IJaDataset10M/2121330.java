package gnu.java.awt.peer.x;

import java.awt.AWTError;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import gnu.java.awt.peer.ClasspathFontPeer;
import gnu.x11.Display;
import gnu.x11.Fontable;

/**
 * The bridge from AWT to X fonts.
 *
 * @author Roman Kennke (kennke@aicas.com)
 */
public class XFontPeer extends ClasspathFontPeer {

    /**
   * The font mapping as specified in the file fonts.properties.
   */
    private static Properties fontProperties;

    static {
        fontProperties = new Properties();
        InputStream in = XFontPeer.class.getResourceAsStream("fonts.properties");
        try {
            fontProperties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
   * The FontMetrics implementation for XFontPeer.
   */
    private class XFontMetrics extends FontMetrics {

        /**
     * The ascent of the font.
     */
        int ascent;

        /**
     * The descent of the font.
     */
        int descent;

        /**
     * The maximum of the character advances.
     */
        private int maxAdvance;

        /**
     * The internal leading.
     */
        int leading;

        /**
     * Cached string metrics. This caches string metrics locally so that the
     * server doesn't have to be asked each time.
     */
        private HashMap metricsCache;

        /**
     * The widths of the characters indexed by the characters themselves.
     */
        private int[] charWidths;

        /**
     * Creates a new XFontMetrics for the specified font.
     *
     * @param font the font
     */
        protected XFontMetrics(Font font) {
            super(font);
            metricsCache = new HashMap();
            Fontable.FontReply info = getXFont().info();
            ascent = info.font_ascent();
            descent = info.font_descent();
            maxAdvance = info.max_bounds().character_width();
            leading = 0;
            if (info.min_byte1() == 0 && info.max_byte1() == 0) readCharWidthsLinear(info); else readCharWidthsNonLinear(info);
        }

        /**
     * Reads the character widths when specified in a linear fashion. That is
     * when the min-byte1 and max-byte2 fields are both zero in the X protocol.
     *
     * @param info the font info reply
     */
        private void readCharWidthsLinear(Fontable.FontReply info) {
            int startIndex = info.min_char_or_byte2();
            int endIndex = info.max_char_or_byte2();
            charWidths = new int[endIndex + 1];
            for (int i = 0; i < startIndex; i++) {
                charWidths[i] = 0;
            }
            int index = startIndex;
            Iterator charInfos = info.char_infos().iterator();
            while (charInfos.hasNext()) {
                Fontable.FontReply.CharInfo charInfo = (Fontable.FontReply.CharInfo) charInfos.next();
                charWidths[index] = charInfo.character_width();
                index++;
            }
        }

        private void readCharWidthsNonLinear(Fontable.FontReply info) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        /**
     * Returns the ascent of the font.
     *
     * @return the ascent of the font
     */
        public int getAscent() {
            return ascent;
        }

        /**
     * Returns the descent of the font.
     *
     * @return the descent of the font
     */
        public int getDescent() {
            return descent;
        }

        /**
     * Returns the overall height of the font. This is the distance from
     * baseline to baseline (usually ascent + descent + leading).
     *
     * @return the overall height of the font
     */
        public int getHeight() {
            return ascent + descent;
        }

        /**
     * Returns the leading of the font.
     *
     * @return the leading of the font
     */
        public int getLeading() {
            return leading;
        }

        /**
     * Returns the maximum advance for this font.
     *
     * @return the maximum advance for this font
     */
        public int getMaxAdvance() {
            return maxAdvance;
        }

        /**
     * Determines the width of the specified character <code>c</code>.
     *
     * @param c the character
     *
     * @return the width of the character
     */
        public int charWidth(char c) {
            int width;
            if (c > charWidths.length) width = charWidths['?']; else width = charWidths[c];
            return width;
        }

        /**
     * Determines the overall width of the specified string.
     *
     * @param c the char buffer holding the string
     * @param offset the starting offset of the string in the buffer
     * @param length the number of characters in the string buffer 
     *
     * @return the overall width of the specified string
     */
        public int charsWidth(char[] c, int offset, int length) {
            int width = 0;
            if (c.length > 0 && length > 0) {
                String s = new String(c, offset, length);
                width = stringWidth(s);
            }
            return width;
        }

        /**
     * Determines the overall width of the specified string.
     *
     * @param s the string
     *
     * @return the overall width of the specified string
     */
        public int stringWidth(String s) {
            int width = 0;
            if (s.length() > 0) {
                if (metricsCache.containsKey(s)) {
                    width = ((Integer) metricsCache.get(s)).intValue();
                } else {
                    Fontable.TextExtentReply extents = getXFont().text_extent(s);
                    width = extents.overall_width();
                    metricsCache.put(s, new Integer(width));
                }
            }
            return width;
        }
    }

    /**
   * The LineMetrics implementation for the XFontPeer.
   */
    private class XLineMetrics extends LineMetrics {

        /**
     * Returns the ascent of the font.
     *
     * @return the ascent of the font
     */
        public float getAscent() {
            return fontMetrics.ascent;
        }

        public int getBaselineIndex() {
            throw new UnsupportedOperationException();
        }

        public float[] getBaselineOffsets() {
            throw new UnsupportedOperationException();
        }

        /**
     * Returns the descent of the font.
     *
     * @return the descent of the font
     */
        public float getDescent() {
            return fontMetrics.descent;
        }

        /**
     * Returns the overall height of the font. This is the distance from
     * baseline to baseline (usually ascent + descent + leading).
     *
     * @return the overall height of the font
     */
        public float getHeight() {
            return fontMetrics.ascent + fontMetrics.descent;
        }

        /**
     * Returns the leading of the font.
     *
     * @return the leading of the font
     */
        public float getLeading() {
            return fontMetrics.leading;
        }

        public int getNumChars() {
            throw new UnsupportedOperationException();
        }

        public float getStrikethroughOffset() {
            return 0.F;
        }

        public float getStrikethroughThickness() {
            return 1.F;
        }

        public float getUnderlineOffset() {
            return 0.F;
        }

        public float getUnderlineThickness() {
            return 1.F;
        }
    }

    /**
   * The X font.
   */
    private gnu.x11.Font xfont;

    private String name;

    private int style;

    private int size;

    /**
   * The font metrics for this font.
   */
    XFontMetrics fontMetrics;

    /**
   * Creates a new XFontPeer for the specified font name, style and size.
   *
   * @param name the font name
   * @param style the font style (bold / italic / normal)
   * @param size the size of the font
   */
    public XFontPeer(String name, int style, int size) {
        super(name, style, size);
        this.name = name;
        this.style = style;
        this.size = size;
    }

    /**
   * Creates a new XFontPeer for the specified font name and style
   * attributes.
   *
   * @param name the font name
   * @param atts the font attributes
   */
    public XFontPeer(String name, Map atts) {
        super(name, atts);
        String family = name;
        if (family == null || family.equals("")) family = (String) atts.get(TextAttribute.FAMILY);
        if (family == null) family = "SansSerif";
        int size = 12;
        Float sizeFl = (Float) atts.get(TextAttribute.SIZE);
        if (sizeFl != null) size = sizeFl.intValue();
        int style = 0;
        Float posture = (Float) atts.get(TextAttribute.POSTURE);
        if (posture != null && !posture.equals(TextAttribute.POSTURE_REGULAR)) style |= Font.ITALIC;
        Float weight = (Float) atts.get(TextAttribute.WEIGHT);
        if (weight != null && weight.compareTo(TextAttribute.WEIGHT_REGULAR) > 0) style |= Font.BOLD;
        this.name = name;
        this.style = style;
        this.size = size;
    }

    /**
   * Initializes the font peer with the specified attributes. This method is
   * called from both constructors.
   *
   * @param name the font name
   * @param style the font style
   * @param size the font size
   */
    private void init(String name, int style, int size) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getDefaultScreenDevice();
        if (dev instanceof XGraphicsDevice) {
            Display display = ((XGraphicsDevice) dev).getDisplay();
            String fontDescr = encodeFont(name, style, size);
            if (XToolkit.DEBUG) System.err.println("XLFD font description: " + fontDescr);
            xfont = new gnu.x11.Font(display, fontDescr);
        } else {
            throw new AWTError("Local GraphicsEnvironment is not XWindowGraphicsEnvironment");
        }
    }

    public boolean canDisplay(Font font, char c) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public int canDisplayUpTo(Font font, CharacterIterator i, int start, int limit) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public String getSubFamilyName(Font font, Locale locale) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public String getPostScriptName(Font font) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public int getNumGlyphs(Font font) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public int getMissingGlyphCode(Font font) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public byte getBaselineFor(Font font, char c) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public String getGlyphName(Font font, int glyphIndex) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public GlyphVector createGlyphVector(Font font, FontRenderContext frc, CharacterIterator ci) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public GlyphVector createGlyphVector(Font font, FontRenderContext ctx, int[] glyphCodes) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public GlyphVector layoutGlyphVector(Font font, FontRenderContext frc, char[] chars, int start, int limit, int flags) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
   * Returns the font metrics for the specified font.
   *
   * @param font the font for which to fetch the font metrics
   *
   * @return the font metrics for the specified font
   */
    public FontMetrics getFontMetrics(Font font) {
        if (font.getPeer() != this) throw new AWTError("The specified font has a different peer than this");
        if (fontMetrics == null) fontMetrics = new XFontMetrics(font);
        return fontMetrics;
    }

    /**
   * Frees the font in the X server.
   */
    protected void finalize() {
        if (xfont != null) xfont.close();
    }

    public boolean hasUniformLineMetrics(Font font) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
   * Returns the line metrics for this font and the specified string and
   * font render context.
   */
    public LineMetrics getLineMetrics(Font font, CharacterIterator ci, int begin, int limit, FontRenderContext rc) {
        return new XLineMetrics();
    }

    public Rectangle2D getMaxCharBounds(Font font, FontRenderContext rc) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    public Rectangle2D getStringBounds(Font font, CharacterIterator ci, int begin, int limit, FontRenderContext frc) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
   * Encodes a font name + style + size specification into a X logical font
   * description (XLFD) as described here:
   *
   * http://www.meretrx.com/e93/docs/xlfd.html
   *
   * This is implemented to look up the font description in the
   * fonts.properties of this package.
   *
   * @param name the font name
   * @param atts the text attributes
   *
   * @return the encoded font description
   */
    static String encodeFont(String name, Map atts) {
        String family = name;
        if (family == null || family.equals("")) family = (String) atts.get(TextAttribute.FAMILY);
        if (family == null) family = "SansSerif";
        int size = 12;
        Float sizeFl = (Float) atts.get(TextAttribute.SIZE);
        if (sizeFl != null) size = sizeFl.intValue();
        int style = 0;
        Float posture = (Float) atts.get(TextAttribute.POSTURE);
        if (posture != null && !posture.equals(TextAttribute.POSTURE_REGULAR)) style |= Font.ITALIC;
        Float weight = (Float) atts.get(TextAttribute.WEIGHT);
        if (weight != null && weight.compareTo(TextAttribute.WEIGHT_REGULAR) > 0) style |= Font.BOLD;
        return encodeFont(name, style, size);
    }

    /**
   * Encodes a font name + style + size specification into a X logical font
   * description (XLFD) as described here:
   *
   * http://www.meretrx.com/e93/docs/xlfd.html
   *
   * This is implemented to look up the font description in the
   * fonts.properties of this package.
   *
   * @param name the font name
   * @param style the font style
   * @param size the font size
   *
   * @return the encoded font description
   */
    static String encodeFont(String name, int style, int size) {
        StringBuilder key = new StringBuilder();
        key.append(validName(name));
        key.append('.');
        switch(style) {
            case Font.BOLD:
                key.append("bold");
                break;
            case Font.ITALIC:
                key.append("italic");
                break;
            case (Font.BOLD | Font.ITALIC):
                key.append("bolditalic");
                break;
            case Font.PLAIN:
            default:
                key.append("plain");
        }
        String protoType = fontProperties.getProperty(key.toString());
        int s = validSize(size);
        return protoType.replaceFirst("%d", String.valueOf(s * 10));
    }

    /**
   * Checks the specified font name for a valid font name. If the font name
   * is not known, then this returns 'sansserif' as fallback.
   *
   * @param name the font name to check
   *
   * @return a valid font name
   */
    static String validName(String name) {
        String retVal;
        if (name.equalsIgnoreCase("sansserif") || name.equalsIgnoreCase("serif") || name.equalsIgnoreCase("monospaced") || name.equalsIgnoreCase("dialog") || name.equalsIgnoreCase("dialoginput")) {
            retVal = name.toLowerCase();
        } else {
            retVal = "sansserif";
        }
        return retVal;
    }

    /**
   * Translates an arbitrary point size to a size that is typically available
   * on an X server. These are the sizes 8, 10, 12, 14, 18 and 24.
   *
   * @param size the queried size
   * @return the real available size
   */
    private static final int validSize(int size) {
        int val;
        if (size <= 9) val = 8; else if (size <= 11) val = 10; else if (size <= 13) val = 12; else if (size <= 17) val = 14; else if (size <= 23) val = 18; else val = 24;
        return val;
    }

    /**
   * Returns the X Font reference. This lazily loads the font when first
   * requested.
   *
   * @return the X Font reference
   */
    gnu.x11.Font getXFont() {
        if (xfont == null) {
            init(name, style, size);
        }
        return xfont;
    }
}
