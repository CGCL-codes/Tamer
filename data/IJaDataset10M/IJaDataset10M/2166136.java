package net.jxta.document;

import java.io.Writer;
import java.io.Reader;
import java.io.IOException;

/**
 * Provides {@code char} array based interfaces for manipulating
 * {@code TextDocument}s.
 */
public interface TextDocumentCharArrayIO {

    /**
     * Returns the sequence of characters which represents the content of the
     * {@code TextDocument}.
     *
     * @return A character array containing the characters of the
     * {@code TextDocument}.
     */
    char[] getChars();
}
