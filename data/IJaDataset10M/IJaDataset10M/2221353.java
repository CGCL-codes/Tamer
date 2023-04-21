package org.middleheaven.math.expression;

public class LiteralTerm implements Term {

    private String symbol;

    public LiteralTerm(String symbol) {
        super();
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public String toString() {
        return symbol;
    }
}
