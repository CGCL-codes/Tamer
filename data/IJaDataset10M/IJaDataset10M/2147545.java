package net.sourceforge.bibtexlipse.core.editors.bibxml;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class BibXMLWhitespaceDetector implements IWhitespaceDetector {

    public boolean isWhitespace(char c) {
        return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
    }
}
