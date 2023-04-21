package org.gjt.sp.jedit.actions;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.Point;
import java.util.Vector;
import org.gjt.sp.jedit.gui.CompleteWord;
import org.gjt.sp.jedit.textarea.*;
import org.gjt.sp.jedit.*;

public class complete_word extends EditAction {

    public void actionPerformed(ActionEvent evt) {
        View view = getView(evt);
        Buffer buffer = view.getBuffer();
        String noWordSep = (String) buffer.getProperty("noWordSep");
        if (noWordSep == null) noWordSep = "";
        JEditTextArea textArea = view.getTextArea();
        if (!textArea.isEditable()) {
            view.getToolkit().beep();
            return;
        }
        int lineIndex = textArea.getCaretLine();
        String line = textArea.getLineText(lineIndex);
        int dot = textArea.getCaretPosition() - textArea.getLineStartOffset(lineIndex);
        if (dot == 0) {
            view.getToolkit().beep();
            return;
        }
        int wordStart = TextUtilities.findWordStart(line, dot - 1, noWordSep);
        String word = line.substring(wordStart, dot);
        if (word.length() == 0) {
            view.getToolkit().beep();
            return;
        }
        view.showWaitCursor();
        Vector completions = new Vector();
        int wordLen = word.length();
        for (int i = 0; i < textArea.getLineCount(); i++) {
            line = textArea.getLineText(i);
            if (line.startsWith(word)) {
                if (i == lineIndex && wordStart == 0) continue;
                String _word = getWord(line, 0, noWordSep);
                if (_word.length() != wordLen) {
                    if (completions.indexOf(_word) == -1) completions.addElement(_word);
                }
            }
            int len = line.length() - word.length();
            for (int j = 0; j < len; j++) {
                char c = line.charAt(j);
                if (!Character.isLetterOrDigit(c) && noWordSep.indexOf(c) == -1) {
                    if (i == lineIndex && wordStart == (j + 1)) continue;
                    if (line.regionMatches(j + 1, word, 0, wordLen)) {
                        String _word = getWord(line, j + 1, noWordSep);
                        if (_word.length() != wordLen) {
                            if (completions.indexOf(_word) == -1) completions.addElement(_word);
                        }
                    }
                }
            }
        }
        MiscUtilities.quicksort(completions, new MiscUtilities.StringICaseCompare());
        view.hideWaitCursor();
        if (completions.size() == 0) view.getToolkit().beep(); else if (completions.size() == 1) {
            textArea.setSelectedText(((String) completions.elementAt(0)).substring(wordLen));
        } else {
            Point location = new Point(textArea.offsetToX(lineIndex, wordStart), textArea.getPainter().getFontMetrics().getHeight() * (lineIndex - textArea.getFirstLine() + 1));
            SwingUtilities.convertPointToScreen(location, textArea);
            new CompleteWord(view, word, completions, location);
        }
    }

    private String getWord(String line, int offset, String noWordSep) {
        int wordEnd = TextUtilities.findWordEnd(line, offset + 1, noWordSep);
        return line.substring(offset, wordEnd);
    }
}
