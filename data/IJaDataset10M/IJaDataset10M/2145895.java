package org.gjt.sp.jedit.actions;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.awt.event.ActionEvent;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.*;

public class untab extends EditAction {

    public void actionPerformed(ActionEvent evt) {
        View view = getView(evt);
        JEditTextArea textArea = view.getTextArea();
        if (!textArea.isEditable() || textArea.getSelectionStart() == textArea.getSelectionEnd()) {
            view.getToolkit().beep();
            return;
        }
        textArea.setSelectedText(doUntab(textArea.getSelectedText(), view.getBuffer().getTabSize()));
    }

    private String doUntab(String in, int tabSize) {
        StringBuffer buf = new StringBuffer();
        int width = 0;
        for (int i = 0; i < in.length(); i++) {
            switch(in.charAt(i)) {
                case '\t':
                    int count = tabSize - (width % tabSize);
                    width += count;
                    while (--count >= 0) buf.append(' ');
                    break;
                case '\n':
                    width = 0;
                    buf.append(in.charAt(i));
                    break;
                default:
                    width++;
                    buf.append(in.charAt(i));
                    break;
            }
        }
        return buf.toString();
    }
}
