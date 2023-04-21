package org.apache.poi.hssf.record.cf;

import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

/**
 * Pattern Formatting Block of the Conditional Formatting Rule Record.
 * 
 * @author Dmitriy Kumshayev
 */
public final class PatternFormatting implements Cloneable {

    /**  No background */
    public static final short NO_FILL = 0;

    /**  Solidly filled */
    public static final short SOLID_FOREGROUND = 1;

    /**  Small fine dots */
    public static final short FINE_DOTS = 2;

    /**  Wide dots */
    public static final short ALT_BARS = 3;

    /**  Sparse dots */
    public static final short SPARSE_DOTS = 4;

    /**  Thick horizontal bands */
    public static final short THICK_HORZ_BANDS = 5;

    /**  Thick vertical bands */
    public static final short THICK_VERT_BANDS = 6;

    /**  Thick backward facing diagonals */
    public static final short THICK_BACKWARD_DIAG = 7;

    /**  Thick forward facing diagonals */
    public static final short THICK_FORWARD_DIAG = 8;

    /**  Large spots */
    public static final short BIG_SPOTS = 9;

    /**  Brick-like layout */
    public static final short BRICKS = 10;

    /**  Thin horizontal bands */
    public static final short THIN_HORZ_BANDS = 11;

    /**  Thin vertical bands */
    public static final short THIN_VERT_BANDS = 12;

    /**  Thin backward diagonal */
    public static final short THIN_BACKWARD_DIAG = 13;

    /**  Thin forward diagonal */
    public static final short THIN_FORWARD_DIAG = 14;

    /**  Squares */
    public static final short SQUARES = 15;

    /**  Diamonds */
    public static final short DIAMONDS = 16;

    /**  Less Dots */
    public static final short LESS_DOTS = 17;

    /**  Least Dots */
    public static final short LEAST_DOTS = 18;

    private int field_15_pattern_style;

    private static final BitField fillPatternStyle = BitFieldFactory.getInstance(0xFC00);

    private int field_16_pattern_color_indexes;

    private static final BitField patternColorIndex = BitFieldFactory.getInstance(0x007F);

    private static final BitField patternBackgroundColorIndex = BitFieldFactory.getInstance(0x3F80);

    public PatternFormatting() {
        field_15_pattern_style = 0;
        field_16_pattern_color_indexes = 0;
    }

    /** Creates new FontFormatting */
    public PatternFormatting(LittleEndianInput in) {
        field_15_pattern_style = in.readUShort();
        field_16_pattern_color_indexes = in.readUShort();
    }

    /**
     * setting fill pattern
     *
     * @see #NO_FILL
     * @see #SOLID_FOREGROUND
     * @see #FINE_DOTS
     * @see #ALT_BARS
     * @see #SPARSE_DOTS
     * @see #THICK_HORZ_BANDS
     * @see #THICK_VERT_BANDS
     * @see #THICK_BACKWARD_DIAG
     * @see #THICK_FORWARD_DIAG
     * @see #BIG_SPOTS
     * @see #BRICKS
     * @see #THIN_HORZ_BANDS
     * @see #THIN_VERT_BANDS
     * @see #THIN_BACKWARD_DIAG
     * @see #THIN_FORWARD_DIAG
     * @see #SQUARES
     * @see #DIAMONDS
     *
     * @param fp  fill pattern 
     */
    public void setFillPattern(int fp) {
        field_15_pattern_style = fillPatternStyle.setValue(field_15_pattern_style, fp);
    }

    /**
     * @return fill pattern
     */
    public int getFillPattern() {
        return fillPatternStyle.getValue(field_15_pattern_style);
    }

    /**
     * set the background fill color.
     */
    public void setFillBackgroundColor(int bg) {
        field_16_pattern_color_indexes = patternBackgroundColorIndex.setValue(field_16_pattern_color_indexes, bg);
    }

    /**
     * @see org.apache.poi.hssf.usermodel.HSSFPalette#getColor(short)
     * @return get the background fill color
     */
    public int getFillBackgroundColor() {
        return patternBackgroundColorIndex.getValue(field_16_pattern_color_indexes);
    }

    /**
     * set the foreground fill color
     */
    public void setFillForegroundColor(int fg) {
        field_16_pattern_color_indexes = patternColorIndex.setValue(field_16_pattern_color_indexes, fg);
    }

    /**
     * @see org.apache.poi.hssf.usermodel.HSSFPalette#getColor(short)
     * @return get the foreground fill color
     */
    public int getFillForegroundColor() {
        return patternColorIndex.getValue(field_16_pattern_color_indexes);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("    [Pattern Formatting]\n");
        buffer.append("          .fillpattern= ").append(Integer.toHexString(getFillPattern())).append("\n");
        buffer.append("          .fgcoloridx= ").append(Integer.toHexString(getFillForegroundColor())).append("\n");
        buffer.append("          .bgcoloridx= ").append(Integer.toHexString(getFillBackgroundColor())).append("\n");
        buffer.append("    [/Pattern Formatting]\n");
        return buffer.toString();
    }

    public Object clone() {
        PatternFormatting rec = new PatternFormatting();
        rec.field_15_pattern_style = field_15_pattern_style;
        rec.field_16_pattern_color_indexes = field_16_pattern_color_indexes;
        return rec;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(field_15_pattern_style);
        out.writeShort(field_16_pattern_color_indexes);
    }
}
