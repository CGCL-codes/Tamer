package org.gjt.sp.jedit.actions;

import java.awt.event.ActionEvent;
import org.gjt.sp.jedit.textarea.*;
import org.gjt.sp.jedit.*;

public class cut_string_register extends EditAction {

    public void actionPerformed(ActionEvent evt) {
        View view = getView(evt);
        JEditTextArea textArea = view.getTextArea();
        if (!textArea.isEditable()) {
            view.getToolkit().beep();
            return;
        }
        String selection = textArea.getSelectedText();
        if (selection == null) return;
        String actionCommand = evt.getActionCommand();
        if (actionCommand == null) {
            view.getCommandLine().promptOneChar(jEdit.getProperty("view.status.cut-string-register"), this);
        } else {
            view.showStatus(null);
            char ch = actionCommand.charAt(0);
            if (ch == '\0') {
                view.getToolkit().beep();
                return;
            }
            int repeatCount = view.getInputHandler().getRepeatCount();
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < repeatCount; i++) buf.append(selection);
            selection = buf.toString();
            Registers.setRegister(ch, new Registers.StringRegister(selection));
            textArea.setSelectedText(null);
        }
    }

    public boolean isRepeatable() {
        return false;
    }
}
