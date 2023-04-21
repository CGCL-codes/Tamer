package de.intarsys.pdf.font;

public class PDFontStyle {

    /** The number of font styles */
    public static final int COUNT = 4;

    /** The enumeration of supported font styles */
    public static final PDFontStyle UNDEFINED = new PDFontStyle("?", -1);

    public static final PDFontStyle REGULAR = new PDFontStyle("Regular", 0);

    public static final PDFontStyle ITALIC = new PDFontStyle("Italic", 1);

    public static final PDFontStyle BOLD = new PDFontStyle("Bold", 2);

    public static final PDFontStyle BOLD_ITALIC = new PDFontStyle("BoldItalic", 3);

    public static PDFontStyle getFontStyle(String name) {
        if (name == null) {
            return REGULAR;
        }
        name = name.trim().toLowerCase();
        boolean bold = false;
        boolean italic = false;
        if (name.indexOf("bold") >= 0) {
            bold = true;
        }
        if (name.indexOf("italic") >= 0) {
            italic = true;
        }
        if (name.indexOf("oblique") >= 0) {
            italic = true;
        }
        if (bold) {
            if (italic) {
                return BOLD_ITALIC;
            } else {
                return BOLD;
            }
        } else {
            if (italic) {
                return ITALIC;
            } else {
                return REGULAR;
            }
        }
    }

    /** The external representation of the font style */
    private final String label;

    private final int index;

    private PDFontStyle(String label, int index) {
        this.label = label;
        this.index = index;
    }

    public PDFontStyle getBoldFlavor() {
        if (this == PDFontStyle.ITALIC) {
            return PDFontStyle.BOLD_ITALIC;
        } else {
            return PDFontStyle.BOLD;
        }
    }

    protected int getIndex() {
        return index;
    }

    public PDFontStyle getItalicFlavor() {
        if (this == PDFontStyle.BOLD) {
            return PDFontStyle.BOLD_ITALIC;
        } else {
            return PDFontStyle.ITALIC;
        }
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
