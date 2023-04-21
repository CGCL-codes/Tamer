package org.gjt.sp.jedit.actions;

import java.awt.event.ActionEvent;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.*;

public class next_char extends EditAction {

    private boolean select;

    public next_char() {
        this(false);
    }

    public next_char(boolean select) {
        this.select = select;
    }

    public void actionPerformed(ActionEvent evt) {
        View view = getView(evt);
        JEditTextArea textArea = view.getTextArea();
        int caret = textArea.getCaretPosition();
        if (caret == textArea.getBufferLength()) {
            view.getToolkit().beep();
            return;
        }
        if (select) textArea.select(textArea.getMarkPosition(), caret + 1); else textArea.setCaretPosition(caret + 1);
    }
}
