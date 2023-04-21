package org.sqlexp.enablement;

import org.sqlexp.util.syntax.ISyntaxViewer;

/**
 * SQL syntax tokenizer.
 * @author Matthieu Réjou
 */
public class SyntaxTokenizer extends org.sqlexp.util.syntax.SyntaxTokenizer {

    /**
	 * Constructs a new syntax tokenizer on the given text viewer.
	 * @param syntaxViewer providing text to parse
	 */
    public SyntaxTokenizer(final ISyntaxViewer syntaxViewer) {
        super(syntaxViewer);
    }
}
