package lang4j.parser;

import org.antlr.runtime.Token;

/**
 * User: felix
 * Date: 21.04.2006
 * Time: 09:53:42
 */
public class Position {

    private int line;

    private int column;

    public Position(final int column, final int line) {
        this.column = column;
        this.line = line;
    }

    public Position(Token token) {
        if (token != null) {
            column = token.getCharPositionInLine();
            line = token.getLine();
        }
    }

    @Deprecated
    public Position(antlr.Token token) {
        this(token.getColumn(), token.getLine());
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }
}
