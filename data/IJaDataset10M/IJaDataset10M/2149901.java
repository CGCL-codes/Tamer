package com.sun.pdfview.font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sun.pdfview.PDFObject;
import com.sun.pdfview.PDFParseException;

/**
 * a Font definition for PDF files
 * @author Mike Wessler
 */
public abstract class PDFFont {

    /** the font SubType of this font */
    private String subtype;

    /** the postscript name of this font */
    private String baseFont;

    /** the font encoding (maps character ids to glyphs) */
    private PDFFontEncoding encoding;

    /** the font descriptor */
    private PDFFontDescriptor descriptor;

    /** the CMap that maps this font to unicode values */
    private PDFCMap unicodeMap;

    /** a cache of glyphs indexed by character */
    private Map<Character, PDFGlyph> charCache;

    /**
     * get the PDFFont corresponding to the font described in a PDFObject.
     * The object is actually a dictionary containing the following keys:<br>
     * Type = "Font"<br>
     * Subtype = (Type1 | TrueType | Type3 | Type0 | MMType1 | CIDFontType0 |
     * CIDFontType2)<br>
     * FirstChar = #<br>
     * LastChar = #<br>
     * Widths = array of #<br>
     * Encoding = (some name representing a dictionary in the resources | an
     * inline dictionary)
     * <p>
     * For Type1 and TrueType fonts, the dictionary also contains:<br>
     * BaseFont = (some name, or XXXXXX+Name as a subset of font Name)
     * <p>
     * For Type3 font, the dictionary contains:<br>
     * FontBBox = (rectangle)<br>
     * FontMatrix = (array, typically [0.001, 0, 0, 0.001, 0, 0])<br>
     * CharProcs = (dictionary)
     * Resources = (dictionary)
     */
    public static synchronized PDFFont getFont(PDFObject obj, HashMap<String, PDFObject> resources) throws IOException {
        PDFFont font = (PDFFont) obj.getCache();
        if (font != null) {
            return font;
        }
        String baseFont = null;
        PDFFontEncoding encoding = null;
        PDFFontDescriptor descriptor = null;
        String subType = obj.getDictRef("Subtype").getStringValue();
        if (subType == null) {
            subType = obj.getDictRef("S").getStringValue();
        }
        PDFObject baseFontObj = obj.getDictRef("BaseFont");
        PDFObject encodingObj = obj.getDictRef("Encoding");
        PDFObject descObj = obj.getDictRef("FontDescriptor");
        if (baseFontObj != null) {
            baseFont = baseFontObj.getStringValue();
        } else {
            baseFontObj = obj.getDictRef("Name");
            if (baseFontObj != null) {
                baseFont = baseFontObj.getStringValue();
            }
        }
        if (encodingObj != null) {
            encoding = new PDFFontEncoding(subType, encodingObj);
        }
        if (descObj != null) {
            descriptor = new PDFFontDescriptor(descObj);
        } else {
            descriptor = new PDFFontDescriptor(baseFont);
        }
        if (subType.equals("Type0")) {
            font = new Type0Font(baseFont, obj, descriptor);
        } else if (subType.equals("Type1")) {
            if (descriptor == null) {
                font = new BuiltinFont(baseFont, obj);
            } else if (descriptor.getFontFile() != null) {
                font = new Type1Font(baseFont, obj, descriptor);
            } else if (descriptor.getFontFile3() != null) {
                font = new Type1CFont(baseFont, obj, descriptor);
            } else {
                font = new BuiltinFont(baseFont, obj, descriptor);
            }
        } else if (subType.equals("TrueType")) {
            if (descriptor.getFontFile2() != null) {
                font = new TTFFont(baseFont, obj, descriptor);
            } else {
                font = new BuiltinFont(baseFont, obj, descriptor);
            }
        } else if (subType.equals("Type3")) {
            font = new Type3Font(baseFont, obj, resources, descriptor);
        } else if (subType.equals("CIDFontType2")) {
            font = new CIDFontType2(baseFont, obj, descriptor);
        } else if (subType.equals("CIDFontType0")) {
            font = new CIDFontType2(baseFont, obj, descriptor);
        } else {
            throw new PDFParseException("Don't know how to handle a '" + subType + "' font");
        }
        font.setSubtype(subType);
        font.setEncoding(encoding);
        obj.setCache(font);
        return font;
    }

    /**
     * Get the subtype of this font.
     * @return the subtype, one of: Type0, Type1, TrueType or Type3
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * Set the font subtype
     */
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    /**
     * Get the postscript name of this font
     * @return the postscript name of this font
     */
    public String getBaseFont() {
        return baseFont;
    }

    /**
     * Set the postscript name of this font
     * @param baseFont the postscript name of the font
     */
    public void setBaseFont(String baseFont) {
        this.baseFont = baseFont;
    }

    /**
     * Get the encoding for this font
     * @return the encoding which maps from this font to actual characters
     */
    public PDFFontEncoding getEncoding() {
        return encoding;
    }

    /**
     * Set the encoding for this font
     */
    public void setEncoding(PDFFontEncoding encoding) {
        this.encoding = encoding;
    }

    /**
     * Get the descriptor for this font
     * @return the font descriptor
     */
    public PDFFontDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Set the descriptor font descriptor
     */
    public void setDescriptor(PDFFontDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * Get the CMap which maps the characters in this font to unicode names
     */
    public PDFCMap getUnicodeMap() {
        return unicodeMap;
    }

    /**
     * Set the CMap which maps the characters in this font to unicode names
     */
    public void setUnicodeMap(PDFCMap unicodeMap) {
        this.unicodeMap = unicodeMap;
    }

    /**
     * Get the glyphs associated with a given String in this font
     *
     * @param text the text to translate into glyphs
     */
    public List<PDFGlyph> getGlyphs(String text) {
        List<PDFGlyph> outList = null;
        if (encoding != null) {
            outList = encoding.getGlyphs(this, text);
        } else {
            char[] arry = text.toCharArray();
            outList = new ArrayList<PDFGlyph>(arry.length);
            for (int i = 0; i < arry.length; i++) {
                char src = (char) (arry[i] & 0xff);
                outList.add(getCachedGlyph(src, null));
            }
        }
        return outList;
    }

    /**
     * Get a glyph for a given character code.  The glyph is returned
     * from the cache if available, or added to the cache if not
     *
     * @param src the character code of this glyph
     * @param name the name of the glyph, or null if the name is unknown
     * @return a glyph for this character
     */
    public PDFGlyph getCachedGlyph(char src, String name) {
        if (charCache == null) {
            charCache = new HashMap<Character, PDFGlyph>();
        }
        PDFGlyph glyph = (PDFGlyph) charCache.get(new Character(src));
        if (glyph == null) {
            glyph = getGlyph(src, name);
            charCache.put(new Character(src), glyph);
        }
        return glyph;
    }

    /**
     * Create a PDFFont given the base font name and the font descriptor
     * @param baseFont the postscript name of this font
     * @param descriptor the descriptor for the font
     */
    protected PDFFont(String baseFont, PDFFontDescriptor descriptor) {
        setBaseFont(baseFont);
        setDescriptor(descriptor);
    }

    /**
     * Get the glyph for a given character code and name
     *
     * The preferred method of getting the glyph should be by name.  If the
     * name is null or not valid, then the character code should be used.
     * If the both the code and the name are invalid, the undefined glyph 
     * should be returned.
     *
     * Note this method must *always* return a glyph.  
     *
     * @param src the character code of this glyph
     * @param name the name of this glyph or null if unknown
     * @return a glyph for this character
     */
    protected abstract PDFGlyph getGlyph(char src, String name);

    /**
     * Turn this font into a pretty String
     */
    @Override
    public String toString() {
        return getBaseFont();
    }

    /** 
     * Compare two fonts base on the baseFont
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PDFFont)) {
            return false;
        }
        return ((PDFFont) o).getBaseFont().equals(getBaseFont());
    }

    /**
     * Hash a font based on its base font
     */
    @Override
    public int hashCode() {
        return getBaseFont().hashCode();
    }
}
