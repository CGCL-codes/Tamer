package net.sf.parcinj;

/**
 * Class representing a token type which is a terminal symbol.
 */
public class TerminalSymbolType implements TokenType {

    /**
   * Creates a terminal symbol type which is a key word. That is, the create
   * instance returns <code>keyWord</code> and <code>true</code>
   * for {@link #asString()} and {@link #isKeyWord()}, respectively.
   */
    public static final TerminalSymbolType keyWord(String keyWord) {
        return new TerminalSymbolType(keyWord, true);
    }

    /**
   * Creates a terminal symbol type which is a delimiter. That is, the create
   * instance returns <code>delimiter</code> and <code>false</code>
   * for {@link #asString()} and {@link #isKeyWord()}, respectively.
   */
    public static final TerminalSymbolType delimiter(String delimiter) {
        return new TerminalSymbolType(delimiter, false);
    }

    private final String _stringRepresentation;

    private final boolean _keyWord;

    private TerminalSymbolType(String stringRepresentation, boolean keyWord) {
        _stringRepresentation = stringRepresentation;
        _keyWord = keyWord;
    }

    /**
   * Returns the text representation of the terminal symbol. It might be used
   * by the {@link Lexer} classes for parsing.
   */
    public String asString() {
        return _stringRepresentation;
    }

    /**
   * Returns <code>true</code> if this is a key word.
   */
    public boolean isKeyWord() {
        return _keyWord;
    }

    /**
   * Returns {@link #asString()}.
   */
    @Override
    public String toString() {
        return asString();
    }
}
