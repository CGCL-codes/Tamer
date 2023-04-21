package com.obdobion.algebrain;

import java.util.*;

public class TokOperator extends Token {

    public TokOperator() {
        super();
    }

    public boolean accepts(char s) {
        if (s == '=') return true;
        return false;
    }

    public EquPart morph() throws EquException {
        EquPart part = Equ.operator(this);
        if (part == null) return this;
        return part;
    }

    public void resolve(Stack values) throws EquException {
        throw new EquException("Unknown token encountered in equation: " + getValue().toString());
    }

    public String toString() {
        return "nonop(" + super.toString() + ")";
    }
}
