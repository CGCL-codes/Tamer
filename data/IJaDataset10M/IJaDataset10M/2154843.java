package org.elascript.parser;

import org.elascript.*;

public interface Lexer {

    public Token nextToken() throws ParsingException;

    public int peekLine();
}
