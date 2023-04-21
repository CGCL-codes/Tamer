package spring25.web.controller;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public final class BoundObject7 {

    private String attr1;

    public BoundObject7() {
        super();
    }

    public BoundObject7(final String _attr1) {
        super();
        this.attr1 = _attr1;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(final String _attr1) {
        this.attr1 = _attr1;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
