package com.dmanski.infrastrukture;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Das restricted Textfeld ist ein reines Textfeld
 * mit den Einschr�nkungen auf maximale Zeichenanzahl
 * und auf 
 * Integer (Nur) alles andere ist nicht implementiert 
 * 
 * @author 15.02.2009 Daniel Manski  D.Manski@dmanski.com 
 *
 */
public class JRestrictedTextField extends JTextField {

    private static final long serialVersionUID = 9158957621834098957L;

    /**
 * Neues Document f�r das Textfeld um die Beschr�nkungen umzusetzen 
 */
    private class RestrictedDocument extends PlainDocument {

        private static final long serialVersionUID = 1L;

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (typ != null) {
                if (typ.equals(Integer.class)) {
                    try {
                        Integer.parseInt(str);
                    } catch (NumberFormatException e) {
                        return;
                    }
                }
            }
            if (offs >= size || getLength() >= size) {
                return;
            }
            if (str.length() > size) {
                str = str.substring(0, size - offs);
            }
            if (str.length() + getLength() > size) {
                str = str.substring(0, size - getLength());
            }
            super.insertString(offs, str, a);
        }
    }

    private final int size;

    private final Class<? extends Number> typ;

    /**
	 * Konstruktor mit der Beschr�nkung auf die Zeichenanzahl
	 * @param aFieldSize
	 */
    public JRestrictedTextField(int aFieldSize) {
        super();
        size = aFieldSize;
        this.setDragEnabled(true);
        typ = null;
    }

    /**
	 * Konstruktor mit beschr�nkung auf einen Typ
	 * @param aFieldSize Zeichenanzahl
	 * @param aClass Zurzeit nur Integer
	 */
    public JRestrictedTextField(int aFieldSize, Class<? extends Number> aClass) {
        super();
        size = aFieldSize;
        this.setDragEnabled(true);
        typ = aClass;
        this.setDocument(new RestrictedDocument());
    }
}
