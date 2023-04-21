package br.com.senai.gui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JNumericField extends JTextField {

    public JNumericField(int cols) {
        super(cols);
    }

    @Override
    protected Document createDefaultModel() {
        return new NumericDocument();
    }

    static class NumericDocument extends PlainDocument {

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            } else {
                boolean digitosOk = true;
                for (char c : str.toCharArray()) {
                    if (Character.isDigit(c) == false) {
                        digitosOk = false;
                        break;
                    }
                }
                if (digitosOk) {
                    super.insertString(offs, str, a);
                }
            }
        }
    }
}
