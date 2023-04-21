package com.volantis.mcs.eclipse.ab.editors.xml;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;

/**
 * An ITextDoubleClickStrategy for the XMLEditor.
 *
 * DISCLAIMER: This class and its associated classes are a quick fix built to
 * provide the ability to edit themes and layouts without the Design
 * parts. As such there are more likely to be bugs, bits missing and
 * bits that could be better designed.
 */
public class XMLDoubleClickStrategy implements ITextDoubleClickStrategy {

    /**
     * The ITextViewer associated with this XMLDoubleClickStrategy.
     */
    protected ITextViewer fText;

    public void doubleClicked(ITextViewer part) {
        int pos = part.getSelectedRange().x;
        if (pos < 0) return;
        fText = part;
        if (!selectComment(pos)) {
            selectWord(pos);
        }
    }

    /**
     *
     * @param caretPos
     * @return boolean flag
     * @todo later complete this javadoc
     */
    protected boolean selectComment(int caretPos) {
        IDocument doc = fText.getDocument();
        int startPos, endPos;
        try {
            int pos = caretPos;
            char c = ' ';
            while (pos >= 0) {
                c = doc.getChar(pos);
                if (c == '\\') {
                    pos -= 2;
                    continue;
                }
                if (c == Character.LINE_SEPARATOR || c == '\"') break;
                --pos;
            }
            if (c != '\"') return false;
            startPos = pos;
            pos = caretPos;
            int length = doc.getLength();
            c = ' ';
            while (pos < length) {
                c = doc.getChar(pos);
                if (c == Character.LINE_SEPARATOR || c == '\"') break;
                ++pos;
            }
            if (c != '\"') return false;
            endPos = pos;
            int offset = startPos + 1;
            int len = endPos - offset;
            fText.setSelectedRange(offset, len);
            return true;
        } catch (BadLocationException x) {
        }
        return false;
    }

    /**
     *
     * @param caretPos
     * @return boolean flag
     * @todo later complete this javadoc
     */
    protected boolean selectWord(int caretPos) {
        IDocument doc = fText.getDocument();
        int startPos, endPos;
        try {
            int pos = caretPos;
            char c;
            while (pos >= 0) {
                c = doc.getChar(pos);
                if (!Character.isJavaIdentifierPart(c)) break;
                --pos;
            }
            startPos = pos;
            pos = caretPos;
            int length = doc.getLength();
            while (pos < length) {
                c = doc.getChar(pos);
                if (!Character.isJavaIdentifierPart(c)) break;
                ++pos;
            }
            endPos = pos;
            selectRange(startPos, endPos);
            return true;
        } catch (BadLocationException x) {
        }
        return false;
    }

    /**
     * Set the range of selected text in the text viewer associated with this
     * XMLDoubleClickStrategy.
     * @param startPos The starting character position of the range.
     * @param stopPos The stop character position of the range.
     */
    private void selectRange(int startPos, int stopPos) {
        int offset = startPos + 1;
        int length = stopPos - offset;
        fText.setSelectedRange(offset, length);
    }
}
