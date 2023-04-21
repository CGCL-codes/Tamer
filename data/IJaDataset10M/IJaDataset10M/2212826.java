package com.lts.event;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * A class that allows changes to go through if the method approve returns true.
 * 
 * <P>
 * This class overrides the {@link #insertString(javax.swing.text.DocumentFilter.FilterBypass, int, String, AttributeSet)},
 * {@link #remove(javax.swing.text.DocumentFilter.FilterBypass, int, int)}
 * and {@link #replace(javax.swing.text.DocumentFilter.FilterBypass, int, int, String, AttributeSet)}
 * methods so that each respective method computes that the revised string would
 * be and then call {@link #approve(String)} to decide if the call should proceed.
 * If the return value is true, then the superclass method is called, otherwise, 
 * the call is ignored.
 * </P>
 * 
 * @author cnh
 *
 */
public class SimpleDocumentFilter extends DocumentFilter {

    protected AbstractDocument myDocument;

    public void setDocument(AbstractDocument document) {
        myDocument = document;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        int length = myDocument.getLength();
        String current = myDocument.getText(0, length);
        String prefix = current.substring(0, offset);
        String suffix = current.substring(offset);
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(string);
        sb.append(suffix);
        String newValue = sb.toString();
        if (approve(newValue)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    protected boolean approve(String newValue) {
        return false;
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        String current = myDocument.getText(0, myDocument.getLength());
        String prefix = current.substring(0, offset);
        String suffix = current.substring(offset + length);
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(suffix);
        String newValue = sb.toString();
        if (approve(newValue)) {
            super.remove(fb, offset, length);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String current = myDocument.getText(0, myDocument.getLength());
        String prefix = current.substring(0, offset);
        String suffix = current.substring(offset + length);
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(text);
        sb.append(suffix);
        String newValue = sb.toString();
        if (approve(newValue)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
