package org.gjt.sp.jedit.actions;

import java.awt.event.ActionEvent;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;

public class insert_literal extends EditAction {

    public void actionPerformed(ActionEvent evt) {
        View view = getView(evt);
        Buffer buffer = view.getBuffer();
        JEditTextArea textArea = view.getTextArea();
        if (!textArea.isEditable()) {
            view.getToolkit().beep();
            return;
        }
        String str = evt.getActionCommand();
        if (str == null) {
            view.getCommandLine().promptOneChar(jEdit.getProperty("view.status.insert-literal"), this);
            return;
        } else {
            view.showStatus(null);
            if (!str.equals("\0")) {
                int repeatCount = view.getInputHandler().getRepeatCount();
                StringBuffer buf = new StringBuffer();
                for (int i = 0; i < repeatCount; i++) buf.append(str);
                textArea.overwriteSetSelectedText(buf.toString());
            }
        }
    }

    public boolean isRepeatable() {
        return false;
    }
}
