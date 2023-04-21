package ch.iserver.ace.application.editor;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class CollaborativeDocument extends DefaultStyledDocument {

    public CollaborativeDocument() {
    }

    public void lock() {
        writeLock();
    }

    public void unlock() {
        writeUnlock();
    }

    protected void styleChanged(Style style) {
        if (!style.getName().equals("default")) {
            reapplyStyles(style);
        }
    }

    public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Style pStyle = getStyle("myStyle");
        super.replace(offset, length, text, pStyle);
    }

    public void reapplyStyles(Style style) {
        Element sectionElem = getDefaultRootElement();
        int paraCount = sectionElem.getElementCount();
        for (int i = 0; i < paraCount; i++) {
            Element paraElem = sectionElem.getElement(i);
            AttributeSet attr = paraElem.getAttributes();
            String sn = (String) attr.getAttribute(StyleConstants.NameAttribute);
            if (style.getName().equals(sn)) {
                int rangeStart = paraElem.getStartOffset();
                int rangeEnd = paraElem.getEndOffset();
                setParagraphAttributes(rangeStart, rangeEnd - rangeStart, style, true);
            }
            for (int j = 0; j < paraElem.getElementCount(); j++) {
                Element contentElem = paraElem.getElement(j);
                attr = contentElem.getAttributes();
                sn = (String) attr.getAttribute(StyleConstants.NameAttribute);
                if (style.getName().equals(sn)) {
                    int rangeStart = contentElem.getStartOffset();
                    int rangeEnd = contentElem.getEndOffset();
                    setCharacterAttributes(rangeStart, rangeEnd - rangeStart, style, true);
                }
            }
        }
    }
}
