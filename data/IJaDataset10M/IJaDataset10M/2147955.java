package com.aratana.ui.fields;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Campo para a inser��o de dinheiro.
 * 
 * @author Dyorgio da Silva Nascimento
 */
@SuppressWarnings("serial")
public class MoneyField extends BasicField<Double> {

    private static final BigDecimal HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);

    public static final DecimalFormat format = new DecimalFormat("#,##0.00");

    private static String empty = "0,00";

    public MoneyField() {
        setHorizontalAlignment(RIGHT);
        setDocument(new PlainDocument() {

            @Override
            public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
                String text = new StringBuilder(allSelected ? "" : MoneyField.this.getText().replaceAll("[^0-9]", "")).append(str.replaceAll("[^0-9]", "")).toString();
                super.remove(0, getLength());
                if (text.isEmpty()) {
                    text = "0";
                }
                super.insertString(0, format.format(new BigDecimal(text).divide(HUNDRED)), a);
                allSelected = false;
            }

            @Override
            public void remove(final int offs, final int len) throws BadLocationException {
                super.remove(offs, len);
                if (len != getLength()) {
                    insertString(0, "", null);
                }
            }
        });
        addCaretListener(new CaretListener() {

            boolean update = false;

            @Override
            public void caretUpdate(final CaretEvent e) {
                if (!update && e.getDot() == e.getMark() && !allSelected) {
                    update = true;
                    allSelected = getSelectionStart() != getSelectionEnd() && getSelectionEnd() == getText().length();
                    setCaretPosition(getText().length());
                    update = false;
                }
            }
        });
        setText("0,00");
        setCaretPosition(4);
    }

    @Override
    public Double getStringValue(String trim) throws Exception {
        return format.parse(trim).doubleValue();
    }

    @Override
    public String getValueString(Double value) {
        return value == null ? empty : format.format(value);
    }
}
