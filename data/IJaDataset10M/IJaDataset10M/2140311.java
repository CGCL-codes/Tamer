package com.rendion.ajl.servlet;

public class Check extends Input {

    private static final String TRUE = "true";

    private static final String CHECKED = "checked";

    protected Check(Tag tag) {
        super(tag);
    }

    @Override
    public void populate(Object value) {
        super.removeAttr(CHECKED);
        if (super.isTrue((String) value)) {
            super.replaceAttr(CHECKED, TRUE);
        }
    }
}
