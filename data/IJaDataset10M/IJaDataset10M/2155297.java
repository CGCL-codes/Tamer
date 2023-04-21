package org.icepdf.core.pobjects.fonts.ofont;

import android.graphics.Typeface;
import org.icepdf.core.util.Hashtable;
import org.icepdf.core.pobjects.Name;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.pobjects.fonts.AFM;
import org.icepdf.core.pobjects.fonts.FontDescriptor;
import org.icepdf.core.util.Library;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.icepdf.core.util.FontUtil;

/**
 *
 *
 */
public class Font extends org.icepdf.core.pobjects.fonts.Font {

    /**
     * default font size
     */
    private static final float DEFAULT_FONT_SIZE = 12.0f;

    private static final Logger logger = Logger.getLogger(Font.class.toString());

    private Encoding encoding;

    private String encodingName;

    private Vector widths;

    private Map<Integer, Float> cidWidths;

    private char[] cMap;

    private CMap toUnicodeCMap;

    protected AFM afm;

    protected int style = Typeface.NORMAL;

    static final String type1Diff[][] = { { "Bookman-Demi", "URWBookmanL-DemiBold", "Arial" }, { "Bookman-DemiItalic", "URWBookmanL-DemiBoldItal", "Arial" }, { "Bookman-Light", "URWBookmanL-Ligh", "Arial" }, { "Bookman-LightItalic", "URWBookmanL-LighItal", "Arial" }, { "Courier", "Nimbus Mono L Regular", "Nimbus Mono L" }, { "Courier-Oblique", "Nimbus Mono L Regular Oblique", "Nimbus Mono L" }, { "Courier-Bold", "Nimbus Mono L Bold", "Nimbus Mono L" }, { "Courier-BoldOblique", "Nimbus Mono L Bold Oblique", "Nimbus Mono L" }, { "AvantGarde-Book", "URWGothicL-Book", "Arial" }, { "AvantGarde-BookOblique", "URWGothicL-BookObli", "Arial" }, { "AvantGarde-Demi", "URWGothicL-Demi", "Arial" }, { "AvantGarde-DemiOblique", "URWGothicL-DemiObli", "Arial" }, { "Helvetica", "Nimbus Sans L Regular", "Nimbus Sans L" }, { "Helvetica-Oblique", "Nimbus Sans L Regular Italic", "Nimbus Sans L" }, { "Helvetica-Bold", "Nimbus Sans L Bold", "Nimbus Sans L" }, { "Helvetica-BoldOblique", "Nimbus Sans L Bold Italic", "Nimbus Sans L" }, { "Helvetica-Narrow", "Nimbus Sans L Regular Condensed", "Nimbus Sans L" }, { "Helvetica-Narrow-Oblique", "Nimbus Sans L Regular Condensed Italic", "Nimbus Sans L" }, { "Helvetica-Narrow-Bold", "Nimbus Sans L Bold Condensed", "Nimbus Sans L" }, { "Helvetica-Narrow-BoldOblique", "Nimbus Sans L Bold Condensed Italic", "Nimbus Sans L" }, { "Helvetica-Condensed", "Nimbus Sans L Regular Condensed", "Nimbus Sans L" }, { "Helvetica-Condensed-Oblique", "Nimbus Sans L Regular Condensed Italic", "Nimbus Sans L" }, { "Helvetica-Condensed-Bold", "Nimbus Sans L Bold Condensed", "Nimbus Sans L" }, { "Helvetica-Condensed-BoldOblique", "Nimbus Sans L Bold Condensed Italic", "Nimbus Sans L" }, { "Palatino-Roman", "URWPalladioL-Roma", "Arial" }, { "Palatino-Italic", "URWPalladioL-Ital", "Arial" }, { "Palatino-Bold", "URWPalladioL-Bold", "Arial" }, { "Palatino-BoldItalic", "URWPalladioL-BoldItal", "Arial" }, { "NewCenturySchlbk-Roman", "CenturySchL-Roma", "Arial" }, { "NewCenturySchlbk-Italic", "CenturySchL-Ital", "Arial" }, { "NewCenturySchlbk-Bold", "CenturySchL-Bold", "Arial" }, { "NewCenturySchlbk-BoldItalic", "CenturySchL-BoldItal", "Arial" }, { "Times-Roman", "Nimbus Roman No9 L Regular", "Nimbus Roman No9 L" }, { "Times-Italic", "Nimbus Roman No9 L Regular Italic", "Nimbus Roman No9 L" }, { "Times-Bold", "Nimbus Roman No9 L Medium", "Nimbus Roman No9 L" }, { "Times-BoldItalic", "Nimbus Roman No9 L Medium Italic", "Nimbus Roman No9 L" }, { "Symbol", "Standard Symbols L", "Standard Symbols L" }, { "ZapfChancery-MediumItalic", "URWChanceryL-MediItal", "Arial" }, { "ZapfDingbats", "Dingbats", "Dingbats" } };

    public Font(Library library, Hashtable entries) {
        super(library, entries);
        cMap = new char[256];
        for (char i = 0; i < 256; i++) {
            cMap[i] = i;
        }
        style = FontUtil.guessAndroidFontStyle(basefont);
        name = library.getName(entries, "Name");
        subtype = library.getName(entries, "Subtype");
        basefont = "Serif";
        if (entries.containsKey("BaseFont")) {
            Object o = entries.get("BaseFont");
            if (o instanceof Name) {
                basefont = ((Name) o).getName();
            }
        }
        basefont = cleanFontName(basefont);
        if (subtype.equals("Type3")) {
            basefont = "Symbol";
            encoding = Encoding.getSymbol();
        }
        if (subtype.equals("Type1")) {
            if (basefont.equals("Symbol")) {
                encoding = Encoding.getSymbol();
            } else if (basefont.equalsIgnoreCase("ZapfDingbats") && subtype.equals("Type1")) {
                encoding = Encoding.getZapfDingBats();
            } else {
                for (String[] aType1Diff : type1Diff) {
                    if (basefont.equals(aType1Diff[0])) {
                        encodingName = "standard";
                        encoding = Encoding.getStandard();
                        break;
                    }
                }
            }
        }
        if (subtype.equals("TrueType")) {
            if (basefont.equals("Symbol")) {
                encodingName = "winAnsi";
                encoding = Encoding.getWinAnsi();
            }
        }
    }

    /**
     * Initiate the Font. Retrieve any needed attributes, basically setup the
     * font so it can be used by the content parser.
     */
    public void init() {
        if (inited) {
            return;
        }
        if (encoding != null) {
            for (char i = 0; i < 256; i++) {
                cMap[i] = encoding.get(i);
            }
        }
        Object objectUnicode = library.getObject(entries, "ToUnicode");
        if (objectUnicode != null && objectUnicode instanceof Stream) {
            toUnicodeCMap = new CMap(library, new Hashtable(), (Stream) objectUnicode);
            toUnicodeCMap.init();
        }
        Object o = library.getObject(entries, "Encoding");
        if (o != null) {
            if (o instanceof Hashtable) {
                Hashtable encoding = (Hashtable) o;
                setBaseEncoding(library.getName(encoding, "BaseEncoding"));
                Vector differences = (Vector) library.getObject(encoding, "Differences");
                if (differences != null) {
                    int c = 0;
                    for (Enumeration e = differences.elements(); e.hasMoreElements(); ) {
                        Object oo = e.nextElement();
                        if (oo instanceof Number) {
                            c = ((Number) oo).intValue();
                        } else if (oo instanceof Name) {
                            String n = oo.toString();
                            int c1 = Encoding.getUV(n);
                            if (c1 == -1) {
                                if (n.charAt(0) == 'a') {
                                    n = n.substring(1);
                                    try {
                                        c1 = Integer.parseInt(n);
                                    } catch (Exception ex) {
                                        logger.log(Level.FINE, "Error parings font differences");
                                    }
                                }
                            }
                            cMap[c] = (char) c1;
                            c++;
                        }
                    }
                }
            } else if (o instanceof Name) {
                setBaseEncoding(((Name) o).getName());
            }
        }
        widths = (Vector) (library.getObject(entries, "Widths"));
        if (widths != null) {
            o = library.getObject(entries, "FirstChar");
            if (o != null) {
                firstchar = (int) (library.getFloat(entries, "FirstChar"));
            }
        } else if (library.getObject(entries, "W") != null) {
            cidWidths = calculateCIDWidths();
            firstchar = 0;
            isAFMFont = false;
        } else {
            isAFMFont = false;
        }
        Object of = library.getObject(entries, "FontDescriptor");
        if (of instanceof FontDescriptor) {
            fontDescriptor = (FontDescriptor) of;
            fontDescriptor.init();
        }
        if (fontDescriptor == null && basefont != null) {
            Object afm = AFM.AFMs.get(basefont.toLowerCase());
            if (afm != null && afm instanceof AFM) {
                AFM fontMetrix = (AFM) afm;
                fontDescriptor = FontDescriptor.createDescriptor(library, fontMetrix);
                fontDescriptor.init();
            }
        }
        if (fontDescriptor != null && fontDescriptor.getFontName().length() > 0) {
            basefont = fontDescriptor.getFontName();
            basefont = cleanFontName(basefont);
        }
        if (fontDescriptor != null && (fontDescriptor.getFlags() & 64) != 0 && encoding == null) {
            encodingName = "standard";
            encoding = Encoding.getStandard();
        }
        Object desendantFont = library.getObject(entries, "DescendantFonts");
        if (desendantFont != null) {
            Vector tmp = (Vector) desendantFont;
            if (tmp.elementAt(0) instanceof Reference) {
                Object fontReference = library.getObject((Reference) tmp.elementAt(0));
                if (fontReference instanceof Font) {
                    Font desendant = (Font) fontReference;
                    desendant.toUnicodeCMap = this.toUnicodeCMap;
                    desendant.init();
                    this.cidWidths = desendant.cidWidths;
                    if (fontDescriptor == null) {
                        fontDescriptor = desendant.fontDescriptor;
                        basefont = fontDescriptor.getFontName();
                        basefont = cleanFontName(basefont);
                    }
                }
            }
        }
        if (subtype.equals("Type1")) {
            AFM a = AFM.AFMs.get(basefont.toLowerCase());
            if (a != null && a.getFontName() != null) {
                afm = a;
            }
        }
        if (subtype.equals("Type1")) {
            for (String[] aType1Diff : type1Diff) {
                if (basefont.equals(aType1Diff[0])) {
                    final OFont f = new OFont(Typeface.create(aType1Diff[1], style), basefont, basefont, DEFAULT_FONT_SIZE);
                    if (f.getFamily().equals(aType1Diff[2])) {
                        basefont = aType1Diff[1];
                        break;
                    }
                }
            }
        }
        isFontSubstitution = true;
        if (fontDescriptor != null && fontDescriptor.getEmbeddedFont() != null) {
            font = fontDescriptor.getEmbeddedFont();
            isFontSubstitution = false;
            isAFMFont = false;
        }
        if (font == null && basefont != null) {
            for (final OFont font1 : OFont.getFonts()) {
                StringTokenizer st = new StringTokenizer(font1.getName(), " ", false);
                String fontName = "";
                while (st.hasMoreElements()) {
                    fontName += st.nextElement();
                }
                if (fontName.equalsIgnoreCase(basefont)) {
                    font = new OFont(Typeface.create(fontName, style), fontName, font1.getFamily(), DEFAULT_FONT_SIZE);
                    basefont = font1.getName();
                    isFontSubstitution = true;
                    break;
                }
            }
        }
        if (font == null && basefont != null) {
            String fontFamily = FontUtil.guessFamily(basefont);
            for (final OFont font1 : OFont.getFonts()) {
                if (font1.getFamily().equalsIgnoreCase(fontFamily)) {
                    font = new OFont(Typeface.create(font1.getFamily(), style), font1.getName(), font1.getFamily(), DEFAULT_FONT_SIZE);
                    isFontSubstitution = true;
                    break;
                }
            }
        }
        if (font == null && basefont != null && basefont.indexOf("-") != -1) {
            font = new OFont(basefont);
            basefont = font.getName();
        }
        if (font == null) {
            font = new OFont(Typeface.create(basefont, style), basefont, basefont, DEFAULT_FONT_SIZE);
            basefont = font.getName();
        }
        if (!isFontSubstitution && font.getName().toLowerCase().indexOf(font.getFamily().toLowerCase()) < 0) {
            if ((font.getName().toLowerCase().indexOf("times new roman") != -1 || font.getName().toLowerCase().indexOf("timesnewroman") != -1 || font.getName().toLowerCase().indexOf("bodoni") != -1 || font.getName().toLowerCase().indexOf("garamond") != -1 || font.getName().toLowerCase().indexOf("minion web") != -1 || font.getName().toLowerCase().indexOf("stone serif") != -1 || font.getName().toLowerCase().indexOf("stoneserif") != -1 || font.getName().toLowerCase().indexOf("georgia") != -1 || font.getName().toLowerCase().indexOf("bitstream cyberbit") != -1)) {
                basefont = "serif";
                font = new OFont(Typeface.create(basefont, style), basefont, basefont, font.getSize());
            } else if ((font.getName().toLowerCase().indexOf("helvetica") != -1 || font.getName().toLowerCase().indexOf("arial") != -1 || font.getName().toLowerCase().indexOf("trebuchet") != -1 || font.getName().toLowerCase().indexOf("avant garde gothic") != -1 || font.getName().toLowerCase().indexOf("avantgardegothic") != -1 || font.getName().toLowerCase().indexOf("verdana") != -1 || font.getName().toLowerCase().indexOf("univers") != -1 || font.getName().toLowerCase().indexOf("futura") != -1 || font.getName().toLowerCase().indexOf("stone sans") != -1 || font.getName().toLowerCase().indexOf("stonesans") != -1 || font.getName().toLowerCase().indexOf("gill sans") != -1 || font.getName().toLowerCase().indexOf("gillsans") != -1 || font.getName().toLowerCase().indexOf("akzidenz") != -1 || font.getName().toLowerCase().indexOf("grotesk") != -1)) {
                basefont = "sansserif";
                font = new OFont(Typeface.create(basefont, style), basefont, basefont, font.getSize());
            } else if ((font.getName().toLowerCase().indexOf("courier") != -1 || font.getName().toLowerCase().indexOf("courier new") != -1 || font.getName().toLowerCase().indexOf("couriernew") != -1 || font.getName().toLowerCase().indexOf("prestige") != -1 || font.getName().toLowerCase().indexOf("eversonmono") != -1 || font.getName().toLowerCase().indexOf("Everson Mono") != -1)) {
                basefont = "monospaced";
                font = new OFont(Typeface.create(basefont, style), basefont, basefont, font.getSize());
            } else {
                basefont = "serif";
                font = new OFont(Typeface.create(basefont, style), basefont, basefont, font.getSize());
            }
        }
        setWidth();
        font = font.deriveFont(encoding, toUnicodeCMap);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(name + " - " + encodingName + " " + basefont + " " + font.toString() + " " + isFontSubstitution);
        }
        inited = true;
    }

    /**
     * Sets the encoding of the font
     *
     * @param baseencoding encoding name ususally MacRomanEncoding,
     *                     MacExpertEncoding, or WinAnsi- Encoding
     */
    private void setBaseEncoding(String baseencoding) {
        if (baseencoding == null) {
            encodingName = "none";
            return;
        } else if (baseencoding.equals("StandardEncoding")) {
            encodingName = "StandardEncoding";
            encoding = Encoding.getStandard();
        } else if (baseencoding.equals("MacRomanEncoding")) {
            encodingName = "MacRomanEncoding";
            encoding = Encoding.getMacRoman();
        } else if (baseencoding.equals("WinAnsiEncoding")) {
            encodingName = "WinAnsiEncoding";
            encoding = Encoding.getWinAnsi();
        } else if (baseencoding.equals("PDFDocEncoding")) {
            encodingName = "PDFDocEncoding";
            encoding = Encoding.getPDFDoc();
        }
        if (encoding != null) {
            for (char i = 0; i < 256; i++) {
                cMap[i] = encoding.get(i);
            }
        }
    }

    /**
     * String representation of the Font object.
     *
     * @return string representing Font object attributes.
     */
    @Override
    public String toString() {
        return "FONT= " + encodingName + " " + entries.toString();
    }

    /**
     * Gets the widths of the given <code>character</code> and appends it to the
     * current <code>advance</code>
     *
     * @param character character to find width of
     * @param advance   current advance of the character
     * @return width of specfied character.
     */
    private float getWidth(int character, float advance) {
        character -= firstchar;
        if (widths != null) {
            if (character >= 0 && character < widths.size()) {
                return ((Number) widths.elementAt(character)).floatValue() / 1000f;
            }
        } else if (afm != null) {
            Float i = afm.getWidths()[(character)];
            if (i != null) {
                return i / 1000f;
            }
        } else if (fontDescriptor != null) {
            if (fontDescriptor.getMissingWidth() > 0) {
                return fontDescriptor.getMissingWidth() / 1000f;
            }
        }
        return advance;
    }

    /**
     * Utility method for setting the widths for a particular font given the
     * specified encoding.
     */
    private void setWidth() {
        float missingWidth = 0;
        float ascent = 0.0f;
        float descent = 0.0f;
        if (fontDescriptor != null) {
            if (fontDescriptor.getMissingWidth() > 0) {
                missingWidth = fontDescriptor.getMissingWidth() / 1000f;
                ascent = fontDescriptor.getAscent() / 1000f;
                descent = fontDescriptor.getDescent() / 1000f;
            }
        }
        if (widths != null) {
            float[] newWidth = new float[256 - firstchar];
            for (int i = 0, max = widths.size(); i < max; i++) {
                if (widths.elementAt(i) != null) {
                    newWidth[i] = ((Number) widths.elementAt(i)).floatValue() / 1000f;
                }
            }
            font = font.deriveFont(newWidth, firstchar, missingWidth, ascent, descent, cMap);
        } else if (cidWidths != null) {
            font = font.deriveFont(cidWidths, firstchar, missingWidth, ascent, descent, null);
        } else if (afm != null) {
            font = font.deriveFont(afm.getWidths(), firstchar, missingWidth, ascent, descent, cMap);
        }
    }

    private String cleanFontName(String fontName) {
        if (fontName.indexOf('+') >= 0) {
            int index = fontName.indexOf('+');
            String tmp = fontName.substring(index + 1);
            try {
                Integer.parseInt(tmp);
                fontName = fontName.substring(0, index);
            } catch (NumberFormatException e) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("error cleaning font base name " + fontName);
                }
            }
        }
        while (fontName.indexOf('+') >= 0) {
            int index = fontName.indexOf('+');
            fontName = fontName.substring(index + 1, fontName.length());
        }
        if (subtype.equals("Type0") || subtype.equals("Type1") || subtype.equals("MMType1") || subtype.equals("TrueType")) {
            if (fontName != null) {
                fontName = fontName.replace(',', '-');
            }
        }
        return fontName;
    }

    private Map<Integer, Float> calculateCIDWidths() {
        HashMap<Integer, Float> cidWidths = new HashMap<Integer, Float>(75);
        Object o = library.getObject(entries, "W");
        if (o instanceof Vector) {
            Vector cidWidth = (Vector) o;
            Object current;
            Object peek;
            Vector subWidth;
            int currentChar;
            for (int i = 0, max = cidWidth.size() - 1; i < max; i++) {
                current = cidWidth.get(i);
                peek = cidWidth.get(i + 1);
                if (current instanceof Integer && peek instanceof Vector) {
                    currentChar = (Integer) current;
                    subWidth = (Vector) peek;
                    for (int j = 0, subMax = subWidth.size(); j < subMax; j++) {
                        if (subWidth.get(j) instanceof Integer) {
                            cidWidths.put(currentChar + j, (Integer) subWidth.get(j) / 1000f);
                        } else if (subWidth.get(j) instanceof Float) {
                            cidWidths.put(currentChar + j, (Float) subWidth.get(j) / 1000f);
                        }
                    }
                    i++;
                }
                if (current instanceof Integer && peek instanceof Integer) {
                    for (int j = (Integer) current; j <= (Integer) peek; j++) {
                        currentChar = j;
                        cidWidths.put(currentChar, (Integer) cidWidth.get(i + 2) / 1000f);
                    }
                    i += 2;
                }
            }
        }
        return cidWidths;
    }
}
