package org.gjt.sp.jedit.actions;

import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;
import org.gjt.sp.jedit.textarea.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;

public class delete_word extends EditAction {

    public void actionPerformed(ActionEvent evt) {
        View view = getView(evt);
        Buffer buffer = view.getBuffer();
        JEditTextArea textArea = view.getTextArea();
        if (!textArea.isEditable()) {
            view.getToolkit().beep();
            return;
        }
        int start = textArea.getSelectionStart();
        if (start != textArea.getSelectionEnd()) {
            textArea.setSelectedText("");
            return;
        }
        int line = textArea.getCaretLine();
        int lineStart = textArea.getLineStartOffset(line);
        int caret = start - lineStart;
        String lineText = textArea.getLineText(textArea.getCaretLine());
        if (caret == lineText.length()) {
            if (lineStart + caret == buffer.getLength()) {
                view.getToolkit().beep();
                return;
            }
            caret++;
        } else {
            String noWordSep = (String) buffer.getProperty("noWordSep");
            caret = TextUtilities.findWordEnd(lineText, caret + 1, noWordSep);
        }
        try {
            buffer.remove(start, (caret + lineStart) - start);
        } catch (BadLocationException bl) {
            Log.log(Log.ERROR, this, bl);
        }
    }
}
