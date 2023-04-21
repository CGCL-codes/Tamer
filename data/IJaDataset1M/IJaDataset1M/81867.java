package org.icepdf.core.pobjects.fonts.ofont;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import java.util.Collection;
import org.icepdf.core.pobjects.fonts.CMap;
import org.icepdf.core.pobjects.fonts.Encoding;
import org.icepdf.core.pobjects.fonts.FontFile;
import org.icepdf.core.pobjects.graphics.TextState;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Rectangle2Df;
import org.icepdf.core.util.FontUtil;

/**
 * OFont is an Android Font wrapper used to aid in the paint of glyphs.
 *
 * @since 3.0
 */
public class OFont implements FontFile {

    private static final Logger log = Logger.getLogger(OFont.class.toString());

    private static final Matrix at = new Matrix();

    private static final Collection<OFont> fontnames = new HashSet<OFont>();

    private final Paint androidFont;

    private final Typeface androidTypeface;

    private final String name;

    private final String family;

    private Rectangle2Df maxCharBounds = new Rectangle2Df(0.0f, 0.0f, 1.0f, 1.0f);

    private HashMap<String, PointF> echarAdvanceCache;

    protected float[] widths;

    protected Map<Integer, Float> cidWidths;

    protected float missingWidth;

    protected int firstCh;

    protected float ascent;

    protected float descent;

    protected Encoding encoding;

    protected CMap toUnicode;

    protected char[] cMap;

    /**
     * Create a font from a given string argument containing the font parameters
     *
     * @param str   string to decode
     */
    public OFont(final String str) {
        final StringTokenizer tokenizer = new StringTokenizer(str, "-");
        final String nameToken = tokenizer.nextToken();
        String sizeToken = "12";
        if (tokenizer.hasMoreTokens()) {
            final String token = tokenizer.nextToken();
            if (Character.isDigit(token.charAt(0))) {
                sizeToken = token;
            } else {
                if (tokenizer.hasMoreTokens()) {
                    sizeToken = tokenizer.nextToken();
                }
            }
        }
        this.androidFont = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.androidTypeface = Typeface.create(nameToken, FontUtil.guessAndroidFontStyle(str));
        this.androidFont.setTypeface(this.androidTypeface);
        this.androidFont.setTextSize(Integer.parseInt(sizeToken));
        this.name = nameToken;
        this.family = nameToken;
        this.echarAdvanceCache = new HashMap<String, PointF>(256);
        fontnames.add(this);
    }

    /**
     * Create a new font from a given typeface, font name, font family and font size
     *
     * @param androidTypeface   Android typeface to use
     * @param name  font name
     * @param family    font family
     * @param size  font size
     */
    public OFont(final Typeface androidTypeface, final String name, final String family, final float size) {
        this.androidTypeface = androidTypeface;
        this.androidFont = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.androidFont.setTypeface(androidTypeface);
        this.androidFont.setTextSize(size);
        this.name = name;
        this.family = family;
        this.echarAdvanceCache = new HashMap<String, PointF>(256);
        fontnames.add(this);
    }

    private OFont(final OFont font) {
        this.echarAdvanceCache = font.echarAdvanceCache;
        this.androidFont = new Paint(font.androidFont);
        this.androidTypeface = font.androidTypeface;
        this.name = font.name;
        this.family = font.family;
        this.encoding = font.encoding;
        this.toUnicode = font.toUnicode;
        this.missingWidth = font.missingWidth;
        this.firstCh = font.firstCh;
        this.ascent = font.ascent;
        this.descent = font.descent;
        this.widths = font.widths;
        this.cidWidths = font.cidWidths;
        this.cMap = font.cMap;
        this.maxCharBounds = font.maxCharBounds;
    }

    /**
     * Get all fonts which have been created using this class
     *
     * @return  set of fonts
     */
    public static Collection<OFont> getFonts() {
        return fontnames;
    }

    public FontFile deriveFont(Encoding encoding, CMap toUnicode) {
        OFont font = new OFont(this);
        this.echarAdvanceCache.clear();
        font.encoding = encoding;
        font.toUnicode = toUnicode;
        return font;
    }

    public FontFile deriveFont(float[] widths, int firstCh, float missingWidth, float ascent, float descent, char[] diff) {
        OFont font = new OFont(this);
        this.echarAdvanceCache.clear();
        font.missingWidth = this.missingWidth;
        font.firstCh = firstCh;
        font.ascent = ascent;
        font.descent = descent;
        font.widths = widths;
        font.cMap = diff;
        return font;
    }

    public FontFile deriveFont(Map<Integer, Float> widths, int firstCh, float missingWidth, float ascent, float descent, char[] diff) {
        OFont font = new OFont(this);
        this.echarAdvanceCache.clear();
        font.missingWidth = this.missingWidth;
        font.firstCh = firstCh;
        font.ascent = ascent;
        font.descent = descent;
        font.cidWidths = widths;
        font.cMap = diff;
        return font;
    }

    public FontFile deriveFont(Matrix at) {
        OFont font = new OFont(this);
        return font;
    }

    public boolean canDisplayEchar(char ech) {
        return true;
    }

    public FontFile deriveFont(float pointsize) {
        OFont font = new OFont(this);
        if (font.getSize() != pointsize) {
            this.echarAdvanceCache.clear();
        }
        font.maxCharBounds = this.maxCharBounds;
        return font;
    }

    public PointF echarAdvance(final char ech) {
        float advance;
        float advanceY;
        String text = String.valueOf(ech);
        PointF echarAdvance = echarAdvanceCache.get(text);
        if (echarAdvance == null) {
            char echGlyph = getCMapping(ech);
            final FontMetrics metrics = this.androidFont.getFontMetrics();
            final Rect bounds = new Rect();
            final char[] chars = { echGlyph };
            this.androidFont.getTextBounds(chars, 0, 1, bounds);
            this.maxCharBounds.x = 0;
            this.maxCharBounds.y = metrics.ascent;
            this.maxCharBounds.width = bounds.width();
            this.maxCharBounds.height = bounds.bottom;
            ascent = metrics.ascent;
            descent = metrics.descent;
            advance = bounds.width();
            advanceY = 0;
            echarAdvanceCache.put(text, new PointF(advance, advanceY));
        } else {
            advance = echarAdvance.x;
            advanceY = echarAdvance.y;
        }
        return new PointF(advance, advanceY);
    }

    /**
     * Gets the ToUnicode character value for the given character.
     *
     * @param currentChar character to find a corresponding CMap for.
     * @return a new Character based on the CMap tranformation.  If the character
     *         can not be found in the CMap the orginal value is returned.
     */
    private char getCMapping(char currentChar) {
        if (toUnicode != null) {
            return toUnicode.toSelector(currentChar);
        }
        return currentChar;
    }

    /**
     * Return the width of the given character
     *
     * @param character character to retreive width of
     * @return width of the given <code>character</code>
     */
    private char getCharDiff(char character) {
        if (cMap != null && character < cMap.length) {
            return cMap[character];
        } else {
            return character;
        }
    }

    private char findAlternateSymbol(char character) {
        for (int i = 0; i < org.icepdf.core.pobjects.fonts.ofont.Encoding.symbolAlaises.length; i++) {
            for (int j = 0; j < org.icepdf.core.pobjects.fonts.ofont.Encoding.symbolAlaises[i].length; j++) {
                if (org.icepdf.core.pobjects.fonts.ofont.Encoding.symbolAlaises[i][j] == character) {
                    return (char) org.icepdf.core.pobjects.fonts.ofont.Encoding.symbolAlaises[i][0];
                }
            }
        }
        return character;
    }

    public CMap getToUnicode() {
        return toUnicode;
    }

    public int getStyle() {
        return this.androidTypeface.getStyle();
    }

    public String getFamily() {
        return this.family;
    }

    public float getSize() {
        return androidFont.getFontMetrics(null);
    }

    public double getAscent() {
        return ascent;
    }

    public double getDescent() {
        return descent;
    }

    public Rectangle2Df getMaxCharBounds() {
        return maxCharBounds;
    }

    public Matrix getTransform() {
        return at;
    }

    public int getRights() {
        return 0;
    }

    public String getName() {
        return this.name;
    }

    public boolean isHinted() {
        return false;
    }

    public int getNumGlyphs() {
        return 256;
    }

    public char getSpaceEchar() {
        return 32;
    }

    public Rectangle2Df getEstringBounds(String estr, int beginIndex, int limit) {
        return null;
    }

    public String getFormat() {
        return null;
    }

    public void drawEstring(Canvas g, String displayText, float x, float y, long layout, int mode, int strokecolor) {
        displayText = toUnicode(displayText);
        if (TextState.MODE_FILL == mode || TextState.MODE_FILL_STROKE == mode || TextState.MODE_FILL_ADD == mode || TextState.MODE_FILL_STROKE_ADD == mode) {
            g.drawText(displayText, 0, displayText.length(), x, y, this.androidFont);
        }
        if (TextState.MODE_STROKE == mode || TextState.MODE_FILL_STROKE == mode || TextState.MODE_STROKE_ADD == mode || TextState.MODE_FILL_STROKE_ADD == mode) {
            g.drawText(displayText, 0, displayText.length(), x, y, this.androidFont);
        }
    }

    public String toUnicode(String displayText) {
        StringBuffer sb = new StringBuffer(displayText.length());
        for (int i = 0; i < displayText.length(); i++) {
            char c1 = displayText.charAt(i);
            char c = toUnicode == null ? getCharDiff(c1) : c1;
            c = getCMapping(c);
            sb.append(c);
        }
        return sb.toString();
    }
}
