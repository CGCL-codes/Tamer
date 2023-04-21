package com.comarch.depth.csharp.editor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * A c sharp aware white space detector.
 */
public class CSharpWhitespaceDetector implements IWhitespaceDetector {

    public boolean isWhitespace(char character) {
        return Character.isWhitespace(character);
    }
}
