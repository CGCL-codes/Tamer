package org.gjt.sp.jedit.actions;

import java.awt.event.ActionEvent;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.EditAction;

public class to_lower extends EditAction {

    public void actionPerformed(ActionEvent evt) {
        JEditTextArea textArea = getView(evt).getTextArea();
        String text = textArea.getSelectedText();
        if (text == null) {
            textArea.getToolkit().beep();
            return;
        }
        textArea.setSelectedText(text.toLowerCase());
    }
}
