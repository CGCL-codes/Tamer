package de.usd.nova.compiler;

import java.io.InputStream;
import antlr.CharScanner;
import antlr.Token;
import antlr.TokenStream;
import antlr.TokenStreamException;

/**
 * Die Klasse filtert fehlerhafte Tokens des Lexers beim Syntax-Highlighting (Satc)
 * Debug-Status
 * wird nicht produktiv eingesetzt
 * Das Prinzip ist, das erkannte Tokens weitergeben werden
 * wirft der Lexer Exceptions, werden diese geblockt
 */
public class ErrorFilterSatcStream extends CharScanner {

    TokenStream filterin;

    public ErrorFilterSatcStream(TokenStream in) {
        filterin = in;
    }

    public ErrorFilterSatcStream(InputStream in) {
        throw new RuntimeException("Syntax highlighting derzeit nicht unterst�tzt. Feature fehlt!");
    }

    /**
	 * gibt das n�chste Token aus
	 */
    public Token nextToken() throws TokenStreamException {
        try {
            return filterin.nextToken();
        } catch (NOVALexerException e) {
            NOVALexerException lexe = (NOVALexerException) e;
            return lexe.getErrorToken();
        }
    }
}
