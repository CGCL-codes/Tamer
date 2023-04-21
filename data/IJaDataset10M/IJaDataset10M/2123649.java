package com.lowagie.text.pdf.font;

import com.lowagie.text.pdf.PdfFontMetrics;
import com.lowagie.text.pdf.PdfName;

/**
 * This class contains the metrics of the font <VAR>Times Bold</VAR>.
 * <P>
 * You can find these metrics in the following file:
 * ftp://ftp.adobe.com/pub/adobe/type/win/all/afmfiles/base17/0.2/tib_____.afm
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 * @since   iText0.30  
 */
public class TimesBold extends PdfFontMetrics {

    /** Contains the widths of the TimesBold characters. */
    public static final int[] METRIC_STANDARD = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 250, 333, 555, 500, 500, 1000, 833, 333, 333, 333, 500, 570, 250, 333, 250, 278, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 333, 333, 570, 570, 570, 500, 930, 722, 667, 722, 722, 667, 611, 778, 778, 389, 500, 778, 667, 944, 722, 778, 611, 778, 722, 556, 667, 722, 722, 1000, 722, 722, 667, 333, 278, 333, 581, 500, 333, 500, 556, 444, 556, 444, 333, 500, 556, 278, 333, 556, 278, 833, 556, 500, 556, 556, 444, 389, 333, 556, 500, 722, 500, 500, 444, 394, 220, 394, 520, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 333, 500, 500, 167, 500, 500, 500, 500, 278, 500, 500, 333, 333, 556, 556, 0, 500, 500, 500, 250, 0, 540, 350, 333, 500, 500, 500, 1000, 1000, 0, 500, 0, 333, 333, 333, 333, 333, 333, 333, 333, 0, 333, 333, 0, 0, 333, 333, 1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1000, 0, 300, 0, 0, 0, 0, 667, 778, 1000, 330, 0, 0, 0, 0, 0, 722, 0, 0, 0, 278, 0, 0, 278, 500, 722, 556, 0, 0, 0, 0 };

    /** Contains the kerning information of certain pairs of characters. */
    public static final int[][] KERNING_STANDARD = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, { 65, -55, 84, -30, 86, -45, 87, -30, 89, -55 }, null, null, null, null, null, null, { 32, -74, 39, -63, 100, -20, 114, -20, 115, -37, 118, -20 }, null, null, null, null, { 39, -55, 186, -45 }, null, { 39, -55, 186, -55 }, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, { 39, -74, 67, -55, 71, -55, 79, -45, 81, -45, 84, -95, 85, -50, 86, -145, 87, -130, 89, -100, 112, -25, 117, -50, 118, -100, 119, -90, 121, -74 }, { 65, -30, 85, -10 }, null, { 46, -20, 65, -35, 86, -40, 87, -40, 89, -40 }, null, { 44, -92, 46, -110, 65, -90, 97, -25, 101, -25, 111, -25 }, null, null, null, { 46, -20, 65, -30, 97, -15, 101, -15, 111, -15, 117, -15 }, { 79, -30, 101, -25, 111, -25, 117, -15, 121, -45 }, { 39, -110, 84, -92, 86, -92, 87, -92, 89, -92, 121, -55, 186, -20 }, null, { 65, -20 }, { 65, -40, 84, -40, 86, -50, 87, -50, 88, -40, 89, -50 }, { 44, -92, 46, -110, 65, -74, 97, -10, 101, -20, 111, -20 }, { 46, -20, 85, -10 }, { 79, -30, 84, -40, 85, -30, 86, -55, 87, -35, 89, -35 }, null, { 44, -74, 45, -92, 46, -90, 58, -74, 59, -74, 65, -90, 79, -18, 97, -92, 101, -92, 105, -18, 111, -92, 114, -74, 117, -92, 119, -74, 121, -74 }, { 44, -50, 46, -50, 65, -60 }, { 44, -129, 45, -74, 46, -145, 58, -92, 59, -92, 65, -135, 71, -30, 79, -45, 97, -92, 101, -100, 105, -37, 111, -100, 117, -92 }, { 44, -92, 45, -37, 46, -92, 58, -55, 59, -55, 65, -120, 79, -10, 97, -65, 101, -65, 105, -18, 111, -75, 117, -50, 121, -60 }, null, { 44, -92, 45, -92, 46, -92, 58, -92, 59, -92, 65, -110, 79, -35, 97, -85, 101, -111, 105, -37, 111, -111, 117, -92 }, null, null, null, null, null, null, { 65, -10, 96, -63 }, { 118, -25 }, { 46, -40, 98, -10, 117, -20, 118, -15 }, null, { 119, -15 }, { 118, -15 }, { 39, 55, 44, -15, 46, -15, 105, -25, 111, -25, 186, 50, 245, -35 }, { 46, -15 }, { 121, -15 }, { 118, -10 }, null, { 101, -10, 111, -15, 121, -15 }, null, null, { 118, -40 }, { 118, -10, 119, -10 }, null, null, { 44, -92, 45, -37, 46, -100, 99, -18, 101, -18, 103, -10, 110, -15, 111, -18, 112, -10, 113, -18, 118, -10 }, null, null, null, { 44, -55, 46, -70, 97, -10, 101, -10, 111, -10 }, { 44, -55, 46, -70, 111, -10 }, null, { 44, -55, 46, -70, 101, -10, 111, -25 }, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, { 65, -10 }, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };

    /** Contains the widths of the TimesBold characters. */
    public static final int[] METRIC_WIN_ANSI = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 250, 333, 555, 500, 500, 1000, 833, 333, 333, 333, 500, 570, 250, 333, 250, 278, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 333, 333, 570, 570, 570, 500, 930, 722, 667, 722, 722, 667, 611, 778, 778, 389, 500, 778, 667, 944, 722, 778, 611, 778, 722, 556, 667, 722, 722, 1000, 722, 722, 667, 333, 278, 333, 581, 500, 333, 500, 556, 444, 556, 444, 333, 500, 556, 278, 333, 556, 278, 833, 556, 500, 556, 556, 444, 389, 333, 556, 500, 722, 500, 500, 444, 394, 220, 394, 520, 350, 350, 350, 333, 500, 500, 1000, 500, 500, 333, 1000, 556, 333, 1000, 350, 350, 350, 350, 333, 333, 500, 500, 350, 500, 1000, 333, 1000, 389, 333, 722, 350, 444, 722, 250, 333, 500, 500, 500, 500, 220, 500, 333, 747, 300, 500, 570, 333, 747, 333, 400, 570, 300, 300, 333, 556, 540, 250, 333, 300, 330, 500, 750, 750, 750, 500, 722, 722, 722, 722, 722, 722, 1000, 722, 667, 667, 667, 667, 389, 389, 389, 389, 722, 722, 778, 778, 778, 778, 778, 570, 778, 722, 722, 722, 722, 722, 611, 556, 500, 500, 500, 500, 500, 500, 722, 444, 444, 444, 444, 444, 278, 278, 278, 278, 500, 556, 500, 500, 500, 500, 500, 570, 500, 556, 556, 556, 556, 500, 556, 500 };

    /** Contains the kerning information of certain pairs of characters. */
    public static final int[][] KERNING_WIN_ANSI = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, { 65, -55, 84, -30, 86, -45, 87, -30, 89, -55 }, null, null, null, null, null, null, { 32, -74, 39, -63, 100, -20, 114, -20, 115, -37, 118, -20, 146, -63, 160, -74 }, null, null, null, null, { 39, -55, 146, -55, 148, -45 }, null, { 39, -55, 146, -55, 148, -55 }, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, { 39, -74, 67, -55, 71, -55, 79, -45, 81, -45, 84, -95, 85, -50, 86, -145, 87, -130, 89, -100, 112, -25, 117, -50, 118, -100, 119, -90, 121, -74, 146, -74 }, { 65, -30, 85, -10 }, null, { 46, -20, 65, -35, 86, -40, 87, -40, 89, -40 }, null, { 44, -92, 46, -110, 65, -90, 97, -25, 101, -25, 111, -25 }, null, null, null, { 46, -20, 65, -30, 97, -15, 101, -15, 111, -15, 117, -15 }, { 79, -30, 101, -25, 111, -25, 117, -15, 121, -45 }, { 39, -110, 84, -92, 86, -92, 87, -92, 89, -92, 121, -55, 146, -110, 148, -20 }, null, { 65, -20 }, { 65, -40, 84, -40, 86, -50, 87, -50, 88, -40, 89, -50 }, { 44, -92, 46, -110, 65, -74, 97, -10, 101, -20, 111, -20 }, { 46, -20, 85, -10 }, { 79, -30, 84, -40, 85, -30, 86, -55, 87, -35, 89, -35 }, null, { 44, -74, 45, -92, 46, -90, 58, -74, 59, -74, 65, -90, 79, -18, 97, -92, 101, -92, 105, -18, 111, -92, 114, -74, 117, -92, 119, -74, 121, -74, 173, -92 }, { 44, -50, 46, -50, 65, -60 }, { 44, -129, 45, -74, 46, -145, 58, -92, 59, -92, 65, -135, 71, -30, 79, -45, 97, -92, 101, -100, 105, -37, 111, -100, 117, -92, 173, -74 }, { 44, -92, 45, -37, 46, -92, 58, -55, 59, -55, 65, -120, 79, -10, 97, -65, 101, -65, 105, -18, 111, -75, 117, -50, 121, -60, 173, -37 }, null, { 44, -92, 45, -92, 46, -92, 58, -92, 59, -92, 65, -110, 79, -35, 97, -85, 101, -111, 105, -37, 111, -111, 117, -92, 173, -92 }, null, null, null, null, null, null, null, { 118, -25 }, { 46, -40, 98, -10, 117, -20, 118, -15 }, null, { 119, -15 }, { 118, -15 }, { 39, 55, 44, -15, 46, -15, 105, -25, 111, -25, 146, 55, 148, 50 }, { 46, -15 }, { 121, -15 }, { 118, -10 }, null, { 101, -10, 111, -15, 121, -15 }, null, null, { 118, -40 }, { 118, -10, 119, -10 }, null, null, { 44, -92, 45, -37, 46, -100, 99, -18, 101, -18, 103, -10, 110, -15, 111, -18, 112, -10, 113, -18, 118, -10, 173, -37 }, null, null, null, { 44, -55, 46, -70, 97, -10, 101, -10, 111, -10 }, { 44, -55, 46, -70, 111, -10 }, null, { 44, -55, 46, -70, 101, -10, 111, -25 }, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, { 65, -10, 145, -63 }, { 32, -74, 39, -63, 100, -20, 114, -20, 115, -37, 118, -20, 146, -63, 160, -74 }, { 65, -10 }, null, null, null, null, null, null, null, null, null, null, null, null, { 65, -55, 84, -30, 86, -45, 87, -30, 89, -55 }, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };

    /** Contains the widths of the Helvetica characters. */
    public static final int[] METRIC_MAC_ROMAN = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 250, 333, 555, 500, 500, 1000, 833, 333, 333, 333, 500, 570, 250, 333, 250, 278, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 333, 333, 570, 570, 570, 500, 930, 722, 667, 722, 722, 667, 611, 778, 778, 389, 500, 778, 667, 944, 722, 778, 611, 778, 722, 556, 667, 722, 722, 1000, 722, 722, 667, 333, 278, 333, 581, 500, 333, 500, 556, 444, 556, 444, 333, 500, 556, 278, 333, 556, 278, 833, 556, 500, 556, 556, 444, 389, 333, 556, 500, 722, 500, 500, 444, 394, 220, 394, 520, 0, 722, 722, 722, 667, 722, 778, 722, 500, 500, 500, 500, 500, 500, 444, 444, 444, 444, 444, 278, 278, 278, 278, 556, 500, 500, 500, 500, 500, 556, 556, 556, 556, 500, 400, 500, 500, 500, 350, 540, 556, 747, 747, 1000, 333, 333, 0, 1000, 778, 0, 570, 0, 0, 500, 556, 0, 0, 0, 0, 0, 300, 330, 0, 722, 500, 500, 333, 570, 0, 500, 0, 0, 0, 0, 1000, 250, 722, 722, 778, 1000, 722, 500, 1000, 500, 500, 333, 333, 570, 0, 500, 722, 167, 500, 333, 333, 556, 556, 500, 250, 333, 500, 1000, 722, 667, 722, 667, 667, 389, 389, 389, 389, 778, 778, 0, 778, 722, 722, 722, 278, 333, 333, 333, 333, 333, 333, 333, 333, 333, 333 };

    /** Contains the kerning information of certain pairs of characters. */
    public static final int[][] KERNING_MAC_ROMAN = { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, { 65, -55, 84, -30, 86, -45, 87, -30, 89, -55 }, null, null, null, null, null, null, { 32, -74, 39, -63, 100, -20, 114, -20, 115, -37, 118, -20, 202, -74, 213, -63 }, null, null, null, null, { 39, -55, 211, -45, 213, -55 }, null, { 39, -55, 211, -55, 213, -55 }, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, { 39, -74, 67, -55, 71, -55, 79, -45, 81, -45, 84, -95, 85, -50, 86, -145, 87, -130, 89, -100, 112, -25, 117, -50, 118, -100, 119, -90, 121, -74, 213, -74 }, { 65, -30, 85, -10 }, null, { 46, -20, 65, -35, 86, -40, 87, -40, 89, -40 }, null, { 44, -92, 46, -110, 65, -90, 97, -25, 101, -25, 111, -25 }, null, null, null, { 46, -20, 65, -30, 97, -15, 101, -15, 111, -15, 117, -15 }, { 79, -30, 101, -25, 111, -25, 117, -15, 121, -45 }, { 39, -110, 84, -92, 86, -92, 87, -92, 89, -92, 121, -55, 211, -20, 213, -110 }, null, { 65, -20 }, { 65, -40, 84, -40, 86, -50, 87, -50, 88, -40, 89, -50 }, { 44, -92, 46, -110, 65, -74, 97, -10, 101, -20, 111, -20 }, { 46, -20, 85, -10 }, { 79, -30, 84, -40, 85, -30, 86, -55, 87, -35, 89, -35 }, null, { 44, -74, 45, -92, 46, -90, 58, -74, 59, -74, 65, -90, 79, -18, 97, -92, 101, -92, 105, -18, 111, -92, 114, -74, 117, -92, 119, -74, 121, -74 }, { 44, -50, 46, -50, 65, -60 }, { 44, -129, 45, -74, 46, -145, 58, -92, 59, -92, 65, -135, 71, -30, 79, -45, 97, -92, 101, -100, 105, -37, 111, -100, 117, -92 }, { 44, -92, 45, -37, 46, -92, 58, -55, 59, -55, 65, -120, 79, -10, 97, -65, 101, -65, 105, -18, 111, -75, 117, -50, 121, -60 }, null, { 44, -92, 45, -92, 46, -92, 58, -92, 59, -92, 65, -110, 79, -35, 97, -85, 101, -111, 105, -37, 111, -111, 117, -92 }, null, null, null, null, null, null, null, { 118, -25 }, { 46, -40, 98, -10, 117, -20, 118, -15 }, null, { 119, -15 }, { 118, -15 }, { 39, 55, 44, -15, 46, -15, 105, -25, 111, -25, 211, 50, 213, 55, 245, -35 }, { 46, -15 }, { 121, -15 }, { 118, -10 }, null, { 101, -10, 111, -15, 121, -15 }, null, null, { 118, -40 }, { 118, -10, 119, -10 }, null, null, { 44, -92, 45, -37, 46, -100, 99, -18, 101, -18, 103, -10, 110, -15, 111, -18, 112, -10, 113, -18, 118, -10 }, null, null, null, { 44, -55, 46, -70, 97, -10, 101, -10, 111, -10 }, { 44, -55, 46, -70, 111, -10 }, null, { 44, -55, 46, -70, 101, -10, 111, -25 }, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, { 65, -55, 84, -30, 86, -45, 87, -30, 89, -55 }, null, null, null, null, null, null, null, { 65, -10 }, null, { 65, -10, 212, -63 }, { 32, -74, 39, -63, 100, -20, 114, -20, 115, -37, 118, -20, 202, -74, 213, -63 }, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };

    /**
	 * Constructs a Metrics-object for this font.
	 * 
	 * @param	encoding		the encoding of the font
	 * @param	fontsize		the size of the font
	 *
	 * @since   iText0.30
	 */
    public TimesBold(int encoding, int fontsize) {
        super(encoding, fontsize);
        switch(encoding) {
            case STANDARD:
                setWidth(METRIC_STANDARD);
                setKerning(KERNING_STANDARD);
                break;
            case MAC_ROMAN:
                setWidth(METRIC_MAC_ROMAN);
                setKerning(KERNING_STANDARD);
                break;
            default:
                setWidth(METRIC_WIN_ANSI);
                setKerning(KERNING_WIN_ANSI);
        }
    }

    /**
	 * Gets the name of this font.
	 *
	 * @return	a <CODE>PdfName</CODE>
	 *
	 * @since   iText0.30
	 */
    public final PdfName name() {
        return PdfName.TIMES_BOLD;
    }

    /**
	 * Gets the fonttype of a font of the same family, but with a different style.
	 * 
	 * @param	style
	 * @return	a fonttype
	 *
	 * @since   iText0.30
	 */
    public final int getStyle(int style) {
        switch(style) {
            case NORMAL:
                return TIMES_ROMAN;
            case ITALIC:
                return TIMES_ITALIC;
            case BOLDITALIC:
                return TIMES_BOLDITALIC;
            default:
                return TIMES_BOLD;
        }
    }
}
