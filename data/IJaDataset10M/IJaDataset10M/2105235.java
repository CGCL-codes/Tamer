package de.javagimmicks.swing.model;

import java.awt.Color;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ColoredOutputStreamDocumentAdapter extends OutputStreamDocumentAdapter {

    private final SimpleAttributeSet _attributes;

    public ColoredOutputStreamDocumentAdapter(Document document, Color color) {
        super(document);
        _attributes = new SimpleAttributeSet();
        StyleConstants.setForeground(_attributes, color);
    }

    @Override
    protected void applyText(Document document, String text) throws Exception {
        document.insertString(document.getLength(), text, _attributes);
    }
}
