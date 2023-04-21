package common;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.NumberFormat;

/**
 * <p>Title: CustomTableCellRenderer</p>
 * <p>Description: Cell Renderer per gli oggetti TabularShape</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Itaco S.r.l.</p>
 * @author Andrea Annibali
 * @version 1.0
 */
public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    private Color[] backColColors, foregroundColColors;

    private Color defaultTColor, defaultBColor, selectedTextColor, selectedBackColor, focusTextColor, focusBackColor, oddBckTabRowRule = null, oddFgTabRowRule = null, evenBckTabRowRule = null, evenFgTabRowRule = null;

    private int[] colAlignment;

    private int columnWidth = 10, maxFracDigit = 2, minFracDigit = 2;

    NumberFormat nf = null;

    public CustomTableCellRenderer(int colWidth, int maxfd, int minfd, Color dTColor, Color dBColor, Color selTColor, Color selBColor, Color fTColor, Color fBColor, Color[] bckColColors, Color[] fgColColors, int[] colAlignment_, String obr, String ebr, String ofr, String efr) {
        defaultTColor = dTColor;
        defaultBColor = dBColor;
        columnWidth = colWidth;
        maxFracDigit = maxfd;
        minFracDigit = minfd;
        backColColors = bckColColors;
        foregroundColColors = fgColColors;
        colAlignment = colAlignment_;
        selectedTextColor = selTColor;
        selectedBackColor = selBColor;
        focusTextColor = fTColor;
        focusBackColor = fBColor;
        this.setOddBckTabRule(obr);
        this.setEvenBckTabRule(ebr);
        this.setOddFgTabRule(ofr);
        this.setEvenFgTabRule(efr);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        for (int i = 0; colAlignment != null && backColColors != null && foregroundColColors != null && i < table.getColumnCount(); i++) {
            if (column == i) {
                if (i < colAlignment.length) setHorizontalAlignment(colAlignment[i]);
                if (isSelected && hasFocus) {
                    super.setBackground(focusBackColor);
                    super.setForeground(focusTextColor);
                } else if (isSelected) {
                    super.setBackground(selectedBackColor);
                    super.setForeground(selectedTextColor);
                } else {
                    if (i < backColColors.length) cell.setBackground(backColColors[i]); else cell.setBackground(defaultBColor);
                    if (i < foregroundColColors.length) cell.setForeground(foregroundColColors[i]); else cell.setForeground(defaultTColor);
                    if (getOddBckTabRule() != null && !getOddBckTabRule().equals("")) {
                        if (row % 2 != 0) {
                            cell.setBackground(getOddBckTabRule());
                        }
                    }
                    if (getOddFgTabRule() != null && !getOddFgTabRule().equals("")) {
                        if (row % 2 != 0) {
                            cell.setForeground(getOddFgTabRule());
                        }
                    }
                    if (getEvenBckTabRule() != null && !getEvenBckTabRule().equals("")) {
                        if (row % 2 == 0) {
                            cell.setBackground(getEvenBckTabRule());
                        }
                    }
                    if (getEvenFgTabRule() != null && !getEvenFgTabRule().equals("")) {
                        if (row % 2 == 0) {
                            cell.setForeground(getEvenFgTabRule());
                        }
                    }
                }
            }
        }
        return cell;
    }

    public void setValue(Object value) {
        super.setValue(value);
        if (value instanceof String) {
            value = "" + ((String) value).substring(0, Math.min(this.columnWidth, ((String) value).length())) + " ";
        } else if (value instanceof Float) {
            if (nf == null) {
                nf = NumberFormat.getNumberInstance();
            }
            nf.setMaximumFractionDigits(maxFracDigit);
            nf.setMinimumFractionDigits(minFracDigit);
            setText(nf.format((Float) value));
        }
    }

    public Color getOddBckTabRule() {
        return oddBckTabRowRule;
    }

    public void setOddBckTabRule(String obr) {
        if (obr != null && !obr.equals("")) oddBckTabRowRule = new Color(Integer.parseInt(obr));
    }

    public Color getOddFgTabRule() {
        return oddFgTabRowRule;
    }

    public void setOddFgTabRule(String ofr) {
        if (ofr != null && !ofr.equals("")) oddFgTabRowRule = new Color(Integer.parseInt(ofr));
    }

    public Color getEvenBckTabRule() {
        return evenBckTabRowRule;
    }

    public void setEvenBckTabRule(String ebr) {
        if (ebr != null && !ebr.equals("")) evenBckTabRowRule = new Color(Integer.parseInt(ebr));
    }

    public Color getEvenFgTabRule() {
        return evenFgTabRowRule;
    }

    public void setEvenFgTabRule(String efr) {
        if (efr != null && !efr.equals("")) evenFgTabRowRule = new Color(Integer.parseInt(efr));
    }
}
