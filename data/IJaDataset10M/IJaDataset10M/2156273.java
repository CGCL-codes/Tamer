package org.javia.arity;

class Token {

    static final int PREFIX = 1, LEFT = 2, RIGHT = 3, SUFIX = 4;

    final int priority;

    final int assoc;

    final int id;

    final byte vmop;

    double value;

    String name = null;

    int arity;

    int position;

    Token(int id, int priority, int assoc, int vmop) {
        this.id = id;
        this.priority = priority;
        this.assoc = assoc;
        this.vmop = (byte) vmop;
        arity = id == Lexer.CALL ? 1 : Symbol.CONST_ARITY;
    }

    Token setPos(int pos) {
        this.position = pos;
        return this;
    }

    Token setValue(double value) {
        this.value = value;
        return this;
    }

    Token setAlpha(String alpha) {
        name = alpha;
        return this;
    }

    public boolean isDerivative() {
        int len;
        return name != null && (len = name.length()) > 0 && name.charAt(len - 1) == '\'';
    }

    public String toString() {
        switch(id) {
            case Lexer.NUMBER:
                return "" + value;
            case Lexer.CALL:
                return name + '(' + arity + ')';
            case Lexer.CONST:
                return name;
        }
        return "" + id;
    }
}
