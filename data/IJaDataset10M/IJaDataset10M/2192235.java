package com.jchapman.jempire.gui.log;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;

/**
 *
 * @author Jeff Chapman
 * @version 1.0
 */
class LogsDocument extends PlainDocument {

    private final int maxLines;

    LogsDocument(int messageMemory) {
        super();
        maxLines = messageMemory;
    }

    /**
     * Inserts a string of content.  This will cause a DocumentEvent
     * of type DocumentEvent.EventType.INSERT to be sent to the
     * registered DocumentListers, unless an exception is thrown.
     * The DocumentEvent will be delivered by calling the
     * insertUpdate method on the DocumentListener.
     * The offset and length of the generated DocumentEvent
     * will indicate what change was actually made to the Document.
     * <p align=center><img src="doc-files/Document-insert.gif"
     *  alt="Diagram shows insertion of 'quick' in 'The quick brown fox'">
     * <p>
     * If the Document structure changed as result of the insertion,
     * the details of what Elements were inserted and removed in
     * response to the change will also be contained in the generated
     * DocumentEvent.  It is up to the implementation of a Document
     * to decide how the structure should change in response to an
     * insertion.
     * <p>
     * If the Document supports undo/redo, an UndoableEditEvent will
     * also be generated.
     *
     * @param offset  the offset into the document to insert the content >= 0.
     *    All positions that track change at or after the given location
     *    will move.
     * @param str    the string to insert
     * @param a      the attributes to associate with the inserted
     *   content.  This may be null if there are no attributes.
     * @exception BadLocationException  the given insert position is not a valid
     * position within the document
     * @see javax.swing.event.DocumentEvent
     * @see javax.swing.event.DocumentListener
     * @see javax.swing.event.UndoableEditEvent
     * @see javax.swing.event.UndoableEditListener
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);
        Element rootElem = getDefaultRootElement();
        if (rootElem.getElementCount() > maxLines) {
            Element firstPara = rootElem.getElement(0);
            int startOffset = firstPara.getStartOffset();
            int length = firstPara.getEndOffset() - startOffset;
            remove(startOffset, length);
        }
    }
}
