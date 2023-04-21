package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * A <CODE>PdfBorderDictionary</CODE> define the appearance of a Border (Annotations).
 *
 * @see		PdfDictionary
 */
public class PdfBorderDictionary extends PdfDictionary {

    public static final int STYLE_SOLID = 0;

    public static final int STYLE_DASHED = 1;

    public static final int STYLE_BEVELED = 2;

    public static final int STYLE_INSET = 3;

    public static final int STYLE_UNDERLINE = 4;

    /**
 * Constructs a <CODE>PdfBorderDictionary</CODE>.
 */
    public PdfBorderDictionary(float borderWidth, int borderStyle, PdfDashPattern dashes) {
        put(PdfName.W, new PdfNumber(borderWidth));
        switch(borderStyle) {
            case STYLE_SOLID:
                put(PdfName.S, PdfName.S);
                break;
            case STYLE_DASHED:
                if (dashes != null) put(PdfName.D, dashes);
                put(PdfName.S, PdfName.D);
                break;
            case STYLE_BEVELED:
                put(PdfName.S, PdfName.B);
                break;
            case STYLE_INSET:
                put(PdfName.S, PdfName.I);
                break;
            case STYLE_UNDERLINE:
                put(PdfName.S, PdfName.U);
                break;
            default:
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.border.style"));
        }
    }

    public PdfBorderDictionary(float borderWidth, int borderStyle) {
        this(borderWidth, borderStyle, null);
    }
}
