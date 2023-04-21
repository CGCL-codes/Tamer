package org.jwatter.toolkit.generate.code;

public class Assignment extends Statement implements Formattable {

    public Assignment(String variable, Expression assigned) {
        super(variable + " = " + assigned.format());
    }
}
