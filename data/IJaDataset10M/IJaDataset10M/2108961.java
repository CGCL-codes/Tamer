package de.knowwe.report.message;

import de.knowwe.core.report.KDOMWarning;

public class InvalidNumberWarning extends KDOMWarning {

    private final String text;

    public InvalidNumberWarning(String t) {
        this.text = t;
    }

    @Override
    public String getVerbalization() {
        return "Invalid Number: " + text;
    }
}
