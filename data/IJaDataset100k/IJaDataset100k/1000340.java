package org.gjt.sp.jedit.actions;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.awt.event.ActionEvent;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;

public class shift_right extends EditAction {

    public void actionPerformed(ActionEvent evt) {
        View view = getView(evt);
        Buffer buffer = view.getBuffer();
        buffer.beginCompoundEdit();
        try {
            JEditTextArea textArea = view.getTextArea();
            if (!textArea.isEditable()) {
                view.getToolkit().beep();
                return;
            }
            int tabSize = buffer.getTabSize();
            boolean noTabs = buffer.getBooleanProperty("noTabs");
            Element map = buffer.getDefaultRootElement();
            int start = textArea.getSelectionStartLine();
            int end = textArea.getSelectionEndLine();
            for (int i = start; i <= end; i++) {
                Element lineElement = map.getElement(i);
                int lineStart = lineElement.getStartOffset();
                String line = buffer.getText(lineStart, lineElement.getEndOffset() - lineStart - 1);
                int whiteSpace = MiscUtilities.getLeadingWhiteSpace(line);
                int whiteSpaceWidth = MiscUtilities.getLeadingWhiteSpaceWidth(line, tabSize) + tabSize;
                buffer.remove(lineStart, whiteSpace);
                buffer.insertString(lineStart, MiscUtilities.createWhiteSpace(whiteSpaceWidth, (noTabs ? 0 : tabSize)), null);
            }
        } catch (BadLocationException bl) {
            Log.log(Log.ERROR, this, bl);
        } finally {
            buffer.endCompoundEdit();
        }
    }
}
