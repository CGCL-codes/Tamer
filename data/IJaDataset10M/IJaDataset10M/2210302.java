package com.bluesky.my4gl.core.parser.java.expression;

import java.util.regex.Pattern;

public abstract class Expression {

    protected String expression;

    public void parse(String expression) {
        this.expression = expression;
    }
}
