package org.vqwiki.lex;

/**
 * This interface can be implemented in any way you like, it doesn't have to be for
 * a JLex generated lexer. As long as the implementing class implements a constructor
 * that takes a single InputStream or Reader parameter and returns one token at a
 * time from the yylex() method, it will work.
 */
public interface Lexer {

    public String yylex() throws java.io.IOException;
}
