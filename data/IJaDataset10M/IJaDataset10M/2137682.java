package com.ideo.sweetdevria.taglib.grid.export.formatter.cellStyle;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

public class CellStyleWrapper implements Comparable {

    private HSSFCellStyle cs;

    private int hashCode;

    public CellStyleWrapper(HSSFCellStyle cs) {
        this.cs = cs;
    }

    public boolean equals(Object obj) {
        CellStyleWrapper csWrp_;
        HSSFCellStyle cs_;
        try {
            csWrp_ = (CellStyleWrapper) obj;
        } catch (ClassCastException e) {
            return false;
        }
        cs_ = csWrp_.getHSSFCellStyle();
        return (cs.getAlignment() == cs_.getAlignment()) && (cs.getBorderBottom() == cs_.getBorderBottom()) && (cs.getBorderLeft() == cs_.getBorderLeft()) && (cs.getBorderRight() == cs_.getBorderRight()) && (cs.getBorderTop() == cs_.getBorderTop()) && (cs.getBottomBorderColor() == cs_.getBottomBorderColor()) && (cs.getDataFormat() == cs_.getDataFormat()) && (cs.getFillBackgroundColor() == cs_.getFillBackgroundColor()) && (cs.getFillForegroundColor() == cs_.getFillForegroundColor()) && (cs.getFillPattern() == cs_.getFillPattern()) && (cs.getFontIndex() == cs_.getFontIndex()) && (cs.getHidden() == cs_.getHidden()) && (cs.getIndention() == cs_.getIndention()) && (cs.getLeftBorderColor() == cs_.getLeftBorderColor()) && (cs.getLocked() == cs_.getLocked()) && (cs.getRightBorderColor() == cs_.getRightBorderColor()) && (cs.getRotation() == cs_.getRotation()) && (cs.getTopBorderColor() == cs_.getTopBorderColor()) && (cs.getVerticalAlignment() == cs_.getVerticalAlignment()) && (cs.getWrapText() == cs_.getWrapText());
    }

    private int v(int i) {
        if (i == 0) {
            return 1;
        } else {
            return i;
        }
    }

    public int hashCode() {
        if (hashCode == 0) {
            hashCode = 17;
            hashCode = 37 * v(cs.getBorderBottom());
            hashCode = 37 * v(cs.getBorderLeft());
            hashCode = 37 * v(cs.getBorderRight());
            hashCode = 37 * v(cs.getBorderTop());
            hashCode = 37 * v(cs.getBottomBorderColor());
            hashCode = 37 * v(cs.getDataFormat());
            hashCode = 37 * v(cs.getFillBackgroundColor());
            hashCode = 37 * v(cs.getFillForegroundColor());
            hashCode = 37 * v(cs.getFillPattern());
            hashCode = 37 * v(cs.getFontIndex());
            hashCode = 37 * (cs.getHidden() ? 1 : (-1));
            hashCode = 37 * v(cs.getIndention());
            hashCode = 37 * v(cs.getLeftBorderColor());
            hashCode = 37 * (cs.getLocked() ? 1 : (-1));
            hashCode = 37 * v(cs.getRightBorderColor());
            hashCode = 37 * v(cs.getRotation());
            hashCode = 37 * v(cs.getTopBorderColor());
            hashCode = 37 * v(cs.getVerticalAlignment());
            hashCode = 37 * (cs.getWrapText() ? 1 : (-1));
        }
        return hashCode;
    }

    public int compareTo(Object obj) {
        int diff = 0;
        CellStyleWrapper csWrp_;
        HSSFCellStyle cs_;
        try {
            csWrp_ = (CellStyleWrapper) obj;
        } catch (ClassCastException e) {
            return -1;
        }
        cs_ = csWrp_.getHSSFCellStyle();
        diff = cs.getAlignment() - cs_.getAlignment();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getBorderBottom() - cs_.getBorderBottom();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getBorderLeft() - cs_.getBorderLeft();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getBorderRight() - cs_.getBorderRight();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getBorderTop() - cs_.getBorderTop();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getBottomBorderColor() - cs_.getBottomBorderColor();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getDataFormat() - cs_.getDataFormat();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getFillBackgroundColor() - cs_.getFillBackgroundColor();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getFillForegroundColor() - cs_.getFillForegroundColor();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getFillPattern() - cs_.getFillPattern();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getFontIndex() - cs_.getFontIndex();
        if (diff != 0) {
            return diff;
        }
        if (cs.getHidden() != cs_.getHidden()) {
            return -1;
        }
        diff = cs.getIndention() - cs_.getIndention();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getLeftBorderColor() - cs_.getLeftBorderColor();
        if (diff != 0) {
            return diff;
        }
        if (cs.getLocked() != cs_.getLocked()) {
            return -1;
        }
        diff = cs.getRightBorderColor() - cs_.getRightBorderColor();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getRotation() - cs_.getRotation();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getTopBorderColor() - cs_.getTopBorderColor();
        if (diff != 0) {
            return diff;
        }
        diff = cs.getVerticalAlignment() - cs_.getVerticalAlignment();
        if (diff != 0) {
            return diff;
        }
        if (cs.getWrapText() != cs_.getWrapText()) {
            return -1;
        }
        return 0;
    }

    public HSSFCellStyle getHSSFCellStyle() {
        return cs;
    }
}
