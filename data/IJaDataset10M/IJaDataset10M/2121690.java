package org.dance.editor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import org.dance.editor.lexer.Lexer;
import org.dance.editor.lexer.Token;

class DoubleClickListener implements MouseListener {

    private JEditorPane editorPane;

    public DoubleClickListener(JEditorPane editorPane) {
        this.editorPane = editorPane;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getClickCount() == 2) {
            HighlightedDocument doc = (HighlightedDocument) editorPane.getDocument();
            if (doc == null) return;
            int caretPosition = editorPane.viewToModel(e.getPoint());
            int currentPosition = caretPosition;
            int beginPosition = -1;
            boolean differentAttributesFound = false;
            try {
                switch(doc.getText(currentPosition, 1).charAt(0)) {
                    case ' ':
                    case '\t':
                    case '\n':
                    case '\r':
                        return;
                }
                while (currentPosition > 0 && !differentAttributesFound) {
                    char currentChar = doc.getText(currentPosition - 1, 1).charAt(0);
                    switch(currentChar) {
                        case ' ':
                        case '\t':
                        case '\n':
                        case '\r':
                            differentAttributesFound = true;
                            break;
                        default:
                            if (doc.getCharacterElement(currentPosition - 1).getAttributes().isEqual(doc.getCharacterElement(caretPosition).getAttributes())) {
                                currentPosition--;
                                beginPosition = currentPosition;
                            } else {
                                differentAttributesFound = true;
                            }
                    }
                }
                currentPosition = caretPosition;
                int endPosition = caretPosition;
                differentAttributesFound = false;
                while (currentPosition < editorPane.getText().length() - 1 && !differentAttributesFound) {
                    char currentChar = doc.getText(currentPosition + 1, 1).charAt(0);
                    switch(currentChar) {
                        case ' ':
                        case '\t':
                        case '\n':
                        case '\r':
                            differentAttributesFound = true;
                            break;
                        default:
                            if (doc.getCharacterElement(currentPosition + 1).getAttributes().isEqual(doc.getCharacterElement(caretPosition).getAttributes())) {
                                currentPosition++;
                                endPosition = currentPosition;
                            } else {
                                differentAttributesFound = true;
                            }
                    }
                }
                editorPane.setSelectionStart(beginPosition);
                editorPane.setSelectionEnd(Math.min(endPosition + 1, editorPane.getText().length() - 1));
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
