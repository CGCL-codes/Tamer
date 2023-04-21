package casa.component.domain.math.expression;

public class Token {

    public static final int EOF = 0, INTEGER = 1, FLOAT = 2, ID = 3, OPERATOR_1 = 4, OPERATOR_2 = 5, OPERATOR_3 = 6, OPEN_PARENTHESIS = 7, CLOSE_PARENTHESIS = 8;

    protected int tokenClass;

    protected Object value;

    protected int line, column;

    public Token(int tokenClass, Object value, int line, int column) {
        this.tokenClass = tokenClass;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public Token(int tokenClass, int line, int column) {
        this(tokenClass, null, line, column);
    }

    public int getTokenClass() {
        return tokenClass;
    }

    public Object getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String toString() {
        return "token (class: " + tokenClass + ", value: " + value + ", line: " + line + ", column: " + column + ")";
    }
}
