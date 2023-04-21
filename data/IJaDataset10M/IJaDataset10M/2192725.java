package org.paccman.ui.common;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import org.paccman.ui.common.*;

/**
 *
 * @author jfer
 */
public class AmountCellRenderer extends DefaultTableCellRenderer {

    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    protected boolean showCurrency;

    protected boolean showSign;

    protected Color colorWhenNegative;

    public AmountCellRenderer(boolean showSign, Color colorWhenNegative, boolean showCurrency) {
        super();
        this.showSign = showSign;
        this.colorWhenNegative = colorWhenNegative;
        this.showCurrency = showCurrency;
        DecimalFormat df = (DecimalFormat) formatter;
        String pattern = df.toPattern();
        if (!showCurrency) {
            pattern = pattern.replaceAll("¤", "").trim();
        }
        pattern += " ";
        df.applyPattern(pattern);
    }

    public void setValue(Object value) {
        if (value == null) {
            setText("");
        } else {
            assert value instanceof BigDecimal : "Bad_value_type_for_column._BigDecimal_expected.";
            BigDecimal amount = (BigDecimal) value;
            if (!showSign && (amount.signum() < 0)) {
                amount = amount.negate();
            }
            setText(formatter.format(amount));
        }
    }

    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel retValue;
        BigDecimal amount = (BigDecimal) value;
        retValue = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value == null) {
            return retValue;
        }
        if ((amount.signum() < 0) && (colorWhenNegative != null)) {
            retValue.setForeground(colorWhenNegative);
        } else {
            retValue.setForeground(Color.BLACK);
        }
        setHorizontalAlignment(RIGHT);
        return retValue;
    }
}
