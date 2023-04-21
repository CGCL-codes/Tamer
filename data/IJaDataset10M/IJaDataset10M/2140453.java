package de.intarsys.pdf.font;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import de.intarsys.cwt.font.afm.AFM;
import de.intarsys.cwt.font.afm.AFMChar;
import de.intarsys.pdf.cos.COSBasedObject;
import de.intarsys.pdf.cos.COSDictionary;
import de.intarsys.pdf.cos.COSName;
import de.intarsys.pdf.cos.COSObject;
import de.intarsys.pdf.encoding.Encoding;
import de.intarsys.pdf.encoding.SymbolEncoding;
import de.intarsys.tools.locator.ClassResourceLocator;
import de.intarsys.tools.locator.ILocator;

/**
 * basic implementation for type 1 support
 */
public class PDFontType1 extends PDSingleByteFont {

    /**
	 * The meta class implementation
	 */
    public static class MetaClass extends PDFont.MetaClass {

        protected MetaClass(Class instanceClass) {
            super(instanceClass);
        }

        @Override
        protected COSBasedObject doCreateCOSBasedObject(COSObject object) {
            return new PDFontType1(object);
        }
    }

    /** Map of known alternative names for the builtin fonts */
    public static final Map<String, String> FONT_ALIASES;

    public static String FONT_Courier = "Courier";

    public static String FONT_Courier_Bold = "Courier-Bold";

    public static String FONT_Courier_BoldOblique = "Courier-BoldOblique";

    public static String FONT_Courier_Oblique = "Courier-Oblique";

    public static String FONT_Helvetica = "Helvetica";

    public static String FONT_Helvetica_Bold = "Helvetica-Bold";

    public static String FONT_Helvetica_BoldOblique = "Helvetica-BoldOblique";

    public static String FONT_Helvetica_Oblique = "Helvetica-Oblique";

    public static String FONT_Symbol = "Symbol";

    public static String FONT_Times_Bold = "Times-Bold";

    public static String FONT_Times_BoldItalic = "Times-BoldItalic";

    public static String FONT_Times_Italic = "Times-Italic";

    public static String FONT_Times_Roman = "Times-Roman";

    public static String FONT_ZapfDingbats = "ZapfDingbats";

    public static final String[] FONT_BUILTINS = new String[] { FONT_Courier, FONT_Courier_Bold, FONT_Courier_BoldOblique, FONT_Courier_Oblique, FONT_Helvetica, FONT_Helvetica_Bold, FONT_Helvetica_BoldOblique, FONT_Helvetica_Oblique, FONT_Symbol, FONT_Times_Bold, FONT_Times_BoldItalic, FONT_Times_Italic, FONT_Times_Roman, FONT_ZapfDingbats };

    /** The meta class instance */
    public static final MetaClass META = new MetaClass(MetaClass.class.getDeclaringClass());

    private static Map<String, AFM> builtins = new HashMap<String, AFM>();

    static {
        FONT_ALIASES = new HashMap<String, String>();
        FONT_ALIASES.put(FONT_Courier, FONT_Courier);
        FONT_ALIASES.put(FONT_Courier_Bold, FONT_Courier_Bold);
        FONT_ALIASES.put(FONT_Courier_Oblique, FONT_Courier_Oblique);
        FONT_ALIASES.put(FONT_Courier_BoldOblique, FONT_Courier_BoldOblique);
        FONT_ALIASES.put(FONT_Helvetica, FONT_Helvetica);
        FONT_ALIASES.put(FONT_Helvetica_Bold, FONT_Helvetica_Bold);
        FONT_ALIASES.put(FONT_Helvetica_Oblique, FONT_Helvetica_Oblique);
        FONT_ALIASES.put(FONT_Helvetica_BoldOblique, FONT_Helvetica_BoldOblique);
        FONT_ALIASES.put(FONT_Times_Roman, FONT_Times_Roman);
        FONT_ALIASES.put(FONT_Times_Bold, FONT_Times_Bold);
        FONT_ALIASES.put(FONT_Times_Italic, FONT_Times_Italic);
        FONT_ALIASES.put(FONT_Times_BoldItalic, FONT_Times_BoldItalic);
        FONT_ALIASES.put(FONT_ZapfDingbats, FONT_ZapfDingbats);
        FONT_ALIASES.put(FONT_Symbol, FONT_Symbol);
        FONT_ALIASES.put("Cour", FONT_Courier);
        FONT_ALIASES.put("CoBo", FONT_Courier_Bold);
        FONT_ALIASES.put("CoOb", FONT_Courier_Oblique);
        FONT_ALIASES.put("CoBO", FONT_Courier_BoldOblique);
        FONT_ALIASES.put("Helv", FONT_Helvetica);
        FONT_ALIASES.put("HeBo", FONT_Helvetica_Bold);
        FONT_ALIASES.put("HeOb", FONT_Helvetica_Oblique);
        FONT_ALIASES.put("HeBO", FONT_Helvetica_BoldOblique);
        FONT_ALIASES.put("TiRo", FONT_Times_Roman);
        FONT_ALIASES.put("TiBo", FONT_Times_Bold);
        FONT_ALIASES.put("TiIt", FONT_Times_Italic);
        FONT_ALIASES.put("TiBI", FONT_Times_BoldItalic);
        FONT_ALIASES.put("ZaDb", FONT_ZapfDingbats);
        FONT_ALIASES.put("Symb", FONT_Symbol);
        FONT_ALIASES.put("CourierNew", FONT_Courier);
        FONT_ALIASES.put("CourierNew,Bold", FONT_Courier_Bold);
        FONT_ALIASES.put("CourierNew,Italic", FONT_Courier_Oblique);
        FONT_ALIASES.put("CourierNew,BoldItalic", FONT_Courier_BoldOblique);
        FONT_ALIASES.put("Arial", FONT_Helvetica);
        FONT_ALIASES.put("Arial,Bold", FONT_Helvetica_Bold);
        FONT_ALIASES.put("Arial,Italic", FONT_Helvetica_Oblique);
        FONT_ALIASES.put("Arial,BoldItalic", FONT_Helvetica_BoldOblique);
        FONT_ALIASES.put("TimesNewRoman", FONT_Times_Roman);
        FONT_ALIASES.put("TimesNewRoman,Bold", FONT_Times_Bold);
        FONT_ALIASES.put("TimesNewRoman,Italic", FONT_Times_Italic);
        FONT_ALIASES.put("TimesNewRoman,BoldItalic", FONT_Times_BoldItalic);
        FONT_ALIASES.put("TimesNewRomanPS", FONT_Times_Roman);
        FONT_ALIASES.put("TimesNewRomanPSMT", FONT_Times_Roman);
        FONT_ALIASES.put("TimesNewRoman-Bold", FONT_Times_Bold);
        FONT_ALIASES.put("TimesNewRomanPS-Bold", FONT_Times_Bold);
        FONT_ALIASES.put("TimesNewRomanPS-BoldMT", FONT_Times_Bold);
        FONT_ALIASES.put("TimesNewRoman-Italic", FONT_Times_Italic);
        FONT_ALIASES.put("TimesNewRomanPS-Italic", FONT_Times_Italic);
        FONT_ALIASES.put("TimesNewRomanPS-ItalicMT", FONT_Times_Italic);
        FONT_ALIASES.put("TimesNewRoman-BoldItalic", FONT_Times_BoldItalic);
        FONT_ALIASES.put("TimesNewRomanPS-BoldItalic", FONT_Times_BoldItalic);
        FONT_ALIASES.put("TimesNewRomanPS-BoldItalicMT", FONT_Times_BoldItalic);
        FONT_ALIASES.put("CourierNewPSMT", FONT_Courier);
        FONT_ALIASES.put("Courier,BoldItalic", FONT_Courier_BoldOblique);
        FONT_ALIASES.put("CourierNew-BoldItalic", FONT_Courier_BoldOblique);
        FONT_ALIASES.put("CourierNewPS-BoldItalicMT", FONT_Courier_BoldOblique);
        FONT_ALIASES.put("Courier,Bold", FONT_Courier_Bold);
        FONT_ALIASES.put("CourierNew-Bold", FONT_Courier_Bold);
        FONT_ALIASES.put("CourierNewPS-BoldMT", FONT_Courier_Bold);
        FONT_ALIASES.put("Courier,Italic", FONT_Courier_Oblique);
        FONT_ALIASES.put("CourierNew-Italic", FONT_Courier_Oblique);
        FONT_ALIASES.put("CourierNewPS-ItalicMT", FONT_Courier_Oblique);
        FONT_ALIASES.put("Helvetica,Bold", FONT_Helvetica_Bold);
        FONT_ALIASES.put("Helvetica-Italic", FONT_Helvetica_Oblique);
        FONT_ALIASES.put("Helvetica,Italic", FONT_Helvetica_Oblique);
        FONT_ALIASES.put("Helvetica-BoldItalic", FONT_Helvetica_BoldOblique);
        FONT_ALIASES.put("Helvetica,BoldItalic", FONT_Helvetica_BoldOblique);
        FONT_ALIASES.put("ArialMT", FONT_Helvetica);
        FONT_ALIASES.put("Arial-Bold", FONT_Helvetica_Bold);
        FONT_ALIASES.put("Arial-BoldMT", FONT_Helvetica_Bold);
        FONT_ALIASES.put("Arial-Italic", FONT_Helvetica_Oblique);
        FONT_ALIASES.put("Arial-ItalicMT", FONT_Helvetica_Oblique);
        FONT_ALIASES.put("Arial-BoldItalic", FONT_Helvetica_BoldOblique);
        FONT_ALIASES.put("Arial-BoldItalicMT", FONT_Helvetica_BoldOblique);
    }

    /**
	 * create a Type1 font object to be used in the pdf document
	 * 
	 * @param name
	 *            the name of the font to use
	 * 
	 * @return the new font created
	 */
    public static PDFontType1 createNew(String name) {
        PDFontType1 font = (PDFontType1) PDFontType1.META.createNew();
        String baseFontName = PDFontType1.FONT_ALIASES.get(name);
        if (baseFontName == null) {
            font.setBaseFont(name);
        } else {
            font.setBaseFont(baseFontName);
        }
        return font;
    }

    public static boolean isBuiltin(String name) {
        return FONT_ALIASES.get(name) != null;
    }

    protected static synchronized AFM lookupBuiltinAFM(String name) {
        String aliased = FONT_ALIASES.get(name);
        if (aliased == null) {
            return null;
        }
        AFM result = builtins.get(aliased);
        if (result == null) {
            ILocator locator = new ClassResourceLocator(PDFontType1.class, aliased + ".afm");
            try {
                result = AFM.createFromLocator(locator);
                builtins.put(aliased, result);
            } catch (IOException e) {
                PACKAGE.Log.log(Level.WARNING, "builtin font metrics '" + aliased + "' load error", e);
            }
        }
        return result;
    }

    /**
	 * Create the receiver class from an already defined {@link COSDictionary}.
	 * NEVER use the constructor directly.
	 * 
	 * @param object
	 *            the PDDocument containing the new object
	 */
    protected PDFontType1(COSObject object) {
        super(object);
    }

    @Override
    protected COSName cosGetExpectedSubtype() {
        return CN_Subtype_Type1;
    }

    @Override
    protected PDFontDescriptor createBuiltinFontDescriptor() {
        return new PDFontDescriptorAFM(lookupBuiltinAFM(getBaseFont().stringValue()));
    }

    @Override
    protected int[] createBuiltInWidths(int[] result) {
        AFM afm = lookupBuiltinAFM(getBaseFont().stringValue());
        if (afm == null) {
            return result;
        }
        if (getEncoding().isFontSpecificEncoding()) {
            for (int i = 0; i < 256; i++) {
                AFMChar afmChar = afm.getCharByCode(i);
                if (afmChar != null) {
                    result[i] = afmChar.getWidth();
                }
            }
        } else {
            for (int i = 0; i < 256; i++) {
                String glyphName = getEncoding().getGlyphName(i);
                AFMChar afmChar = afm.getCharByName(glyphName);
                if (afmChar != null) {
                    result[i] = afmChar.getWidth();
                }
            }
        }
        return result;
    }

    @Override
    protected Encoding createDefaultEncoding() {
        if ((getFontDescriptor() != null) && getFontDescriptor().isSymbolic()) {
            return SymbolEncoding.UNIQUE;
        }
        AFM afm = lookupBuiltinAFM(getBaseFont().stringValue());
        if (afm == null) {
            return super.createDefaultEncoding();
        } else {
            return new AFMEncoding(afm);
        }
    }

    @Override
    protected int createFirstChar() {
        Encoding encoding = getEncoding();
        for (int i = 0; i <= 255; i++) {
            if (encoding.getDecoded(i) != -1) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected int createLastChar() {
        Encoding encoding = getEncoding();
        for (int i = 255; i >= 0; i--) {
            if (encoding.getDecoded(i) != -1) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public String getFontNameNormalized() {
        String alias = FONT_ALIASES.get(super.getFontNameNormalized());
        if (alias != null) {
            return alias;
        }
        return super.getFontNameNormalized();
    }

    @Override
    public String getFontType() {
        return "Type1";
    }

    @Override
    public boolean isStandardFont() {
        if (cosGetField(DK_FontDescriptor).isNull()) {
            return true;
        }
        if (Arrays.asList(FONT_BUILTINS).contains(cosGetField(DK_BaseFont))) {
            return true;
        }
        return false;
    }
}
