package relex.feature;

/**
 * An atom that holds a string.
 *
 * Copyright (C) 2008 Linas Vepstas <linas@linas.org>
 */
public class StringNode extends Atom {

    private static final long serialVersionUID = 1087852981117134672L;

    protected String string;

    public String getValue() {
        return string;
    }

    public void setValue(String str) {
        string = str;
    }
}
