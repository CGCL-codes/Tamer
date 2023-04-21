package org.personalsmartspaces.cm.reasoning.impl;

public class RuleMismatchException extends Exception {

    private String mistake;

    public RuleMismatchException(String string) {
        super(string);
        mistake = string;
    }

    public String getError() {
        return mistake;
    }
}
