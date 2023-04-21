package pl.edu.amu.xtr.view.xmleditor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class WhitespaceDetector implements IWhitespaceDetector {

    public boolean isWhitespace(char c) {
        return Character.isWhitespace(c);
    }
}
