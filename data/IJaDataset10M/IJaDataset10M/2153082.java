package org.deved.antlride.common.ui.text.rules;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * Ascriptaware white space detector.
 */
public class AntlrWhitespaceDetector implements IWhitespaceDetector {

    private static AntlrWhitespaceDetector instance;

    private AntlrWhitespaceDetector() {
    }

    public boolean isWhitespace(char character) {
        return Character.isWhitespace(character);
    }

    public static AntlrWhitespaceDetector instance() {
        if (instance == null) {
            instance = new AntlrWhitespaceDetector();
        }
        return instance;
    }
}
