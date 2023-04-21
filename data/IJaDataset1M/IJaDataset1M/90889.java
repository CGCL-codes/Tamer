package org.sepa.commons.grammar.importers;

import org.sepa.commons.exceptions.SepaEndOfFile;

/**
 *
 * @author plb
 */
public interface LexicalParser {

    /**
     * Get the current serial possition
     * @return
     */
    int getPos();

    /**
     * Get the current column number
     * @return
     */
    int getCol();

    /**
     * Get the current row number
     * @return
     */
    int getRow();

    /**
     * Get the complete current line
     * @return
     */
    String getLine();

    /**
     * Get the current token
     * @return
     */
    Token getCurrentToken() throws SepaEndOfFile;

    /**
     * Get the current token's lexeme
     * @return
     */
    String getText();

    /**
     * Get the next token
     * @return
     */
    Token getNextToken() throws SepaEndOfFile;
}
