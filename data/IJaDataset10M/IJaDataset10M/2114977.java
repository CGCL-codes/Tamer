package com.aptana.ide.debug.internal.ui.console;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

/**
 * @author Max Stepanov
 *
 */
public class JSConsoleTracker implements IPatternMatchListenerDelegate {

    private TextConsole fConsole;

    public void connect(TextConsole console) {
        fConsole = console;
    }

    public void disconnect() {
        fConsole = null;
    }

    public void matchFound(PatternMatchEvent event) {
        try {
            int lineNumber = 0;
            String text = getMatchText(event);
            int index = text.lastIndexOf(':');
            try {
                lineNumber = Integer.parseInt(text.substring(index + 1));
            } catch (NumberFormatException ignore) {
            }
            text = text.substring(0, index);
            IHyperlink link = new JSConsoleHyperlink(fConsole, text, lineNumber);
            fConsole.addHyperlink(link, event.getOffset() + 1, event.getLength() - 2);
        } catch (BadLocationException ignore) {
        }
    }

    private String getMatchText(PatternMatchEvent event) throws BadLocationException {
        IDocument document = ((TextConsole) event.getSource()).getDocument();
        int lineNumber = document.getLineOfOffset(event.getOffset());
        IRegion lineInformation = document.getLineInformation(lineNumber);
        int lineOffset = lineInformation.getOffset();
        String line = document.get(lineOffset, lineInformation.getLength());
        int beginIndex = event.getOffset() - lineOffset + 1;
        line = line.substring(beginIndex, beginIndex + event.getLength() - 2);
        return line;
    }
}
