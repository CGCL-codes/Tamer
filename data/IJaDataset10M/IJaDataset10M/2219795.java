package com.bluebrim.formula.shared;

import javax.swing.text.*;
import com.bluebrim.text.shared.*;

public class CoFormulaStyledDocument extends CoStyledDocument implements CoFormulaStyledDocumentIF {

    public static final String FORMULA_VARIABLE = "formula_variable";

    private MutableAttributeSet attr = new CoSimpleAttributeSet();

    public CoFormulaStyledDocument() {
        super();
    }

    public void addString(String str) {
        try {
            insertString(getLength(), str, attr);
        } catch (BadLocationException e) {
        }
    }

    public int getLength() {
        return super.getLength();
    }

    public void insertFormulaVariable(int offset, int length, String name) {
        if (length > 0) {
            try {
                remove(offset, length);
            } catch (Throwable t) {
                return;
            }
        }
        try {
            MutableAttributeSet as = new CoSimpleAttributeSet();
            as.addAttribute(FORMULA_VARIABLE, name);
            insertString(offset, name, as);
        } catch (Throwable t) {
        }
    }

    public boolean isAtomic(Element elem) {
        return (elem.getAttributes().isDefined(FORMULA_VARIABLE));
    }

    public String toString() {
        try {
            return getText(0, getLength());
        } catch (BadLocationException ble) {
            System.out.println("err i formula styled document::toString");
            return "";
        }
    }
}
