package com.antlersoft.parser;

public class Token implements Cloneable {

    public Symbol symbol;

    public String value;

    public Token(Symbol sym, String val) {
        symbol = sym;
        value = val;
    }

    public String toString() {
        return value;
    }

    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof Token) {
            Token t = (Token) o;
            result = (t.symbol == symbol && t.value.equals(value));
        }
        return result;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
