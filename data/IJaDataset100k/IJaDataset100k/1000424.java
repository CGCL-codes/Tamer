package org.gjt.sp.jedit.actions;

import java.awt.event.ActionEvent;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.*;

public class prev_page extends EditAction {

    private boolean select;

    public prev_page() {
        this(false);
    }

    public prev_page(boolean select) {
        this.select = select;
    }

    public void actionPerformed(ActionEvent evt) {
        View view = getView(evt);
        JEditTextArea textArea = view.getTextArea();
        int firstLine = textArea.getFirstLine();
        int visibleLines = textArea.getVisibleLines();
        int caret = textArea.getCaretPosition();
        int line = textArea.getCaretLine();
        if (firstLine < visibleLines) firstLine = visibleLines;
        int magic = textArea.getMagicCaretPosition();
        if (magic == -1) {
            magic = textArea.offsetToX(line, caret - textArea.getLineStartOffset(line));
        }
        textArea.setFirstLine(firstLine - visibleLines);
        line = Math.max(0, line - visibleLines);
        caret = textArea.getLineStartOffset(line) + textArea.xToOffset(line, magic + 1);
        if (select) textArea.select(textArea.getMarkPosition(), caret); else textArea.setCaretPosition(caret);
        textArea.setMagicCaretPosition(magic);
    }
}
