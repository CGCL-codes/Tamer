package com.rapidminer.gui.tools;

import java.awt.Color;
import java.awt.Component;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import com.rapidminer.tools.Tools;

/**
 * The default table cell renderer for all viewer tables. Provides the correct border and colors.
 * Numbers will be formatted with the generic number of fraction digits.
 * It is possible to restrict the maximum length of strings shown. This might speed up rendering
 * for very large strings, which of course can't be shown completely anyway.
 * 
 * @author Ingo Mierswa
 */
public class ColoredTableCellRenderer implements TableCellRenderer {

    private static final Color SELECTED_COLOR = UIManager.getColor("Tree.selectionBackground");

    private static final Color TEXT_SELECTED_COLOR = UIManager.getColor("Tree.selectionForeground");

    private static final Color TEXT_NON_SELECTED_COLOR = UIManager.getColor("Table.textForeground");

    private int maximalTextLength = Integer.MAX_VALUE;

    private boolean cutOnFirstLineBreak = false;

    private JTextField renderer = new JTextField();

    private int dateFormat = ExtendedJTable.NO_DATE_FORMAT;

    public ColoredTableCellRenderer() {
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
    }

    public void setColor(Color color) {
        renderer.setBackground(color);
    }

    public void setDateFormat(int dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
	 * Use this method to set the maximal text length. Enter Integer.MAX_VALUE to disable text cutting (default)
	 */
    public void setMaximalTextLength(int maxLength) {
        this.maximalTextLength = maxLength;
    }

    /**
	 * This enables or disables the cutting on the first linebreak of a string. Normally only one line
	 * is shown, so this might be useful to speed up text rendering.
	 */
    public void setCutOnFirstLineBreak(boolean enable) {
        this.cutOnFirstLineBreak = enable;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String text = null;
        if (value instanceof Number) {
            Number number = (Number) value;
            double numberValue = number.doubleValue();
            text = Tools.formatIntegerIfPossible(numberValue);
        } else {
            if (value != null) {
                if (value instanceof Date) {
                    switch(dateFormat) {
                        case ExtendedJTable.DATE_FORMAT:
                            text = Tools.formatDate((Date) value);
                            break;
                        case ExtendedJTable.TIME_FORMAT:
                            text = Tools.formatTime((Date) value);
                            break;
                        case ExtendedJTable.DATE_TIME_FORMAT:
                            text = Tools.formatDateTime((Date) value);
                            break;
                        default:
                            text = value.toString();
                            break;
                    }
                } else {
                    text = value.toString();
                    if (cutOnFirstLineBreak) {
                        int indexOfLineBreak = text.indexOf("\n");
                        if (indexOfLineBreak > 0) {
                            text = text.substring(0, indexOfLineBreak - 1);
                        }
                    }
                    if (text.length() > maximalTextLength) {
                        text = text.substring(0, maximalTextLength);
                    }
                }
            } else {
                text = "?";
            }
        }
        renderer.setText(text);
        if (isSelected) {
            renderer.setBackground(SELECTED_COLOR);
            renderer.setForeground(TEXT_SELECTED_COLOR);
        } else {
            renderer.setForeground(TEXT_NON_SELECTED_COLOR);
        }
        return renderer;
    }
}
