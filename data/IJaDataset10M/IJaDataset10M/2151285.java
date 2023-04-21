package spring25.web.controller;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public final class BoundObject4 {

    private String input1;

    private String selectPath;

    public BoundObject4() {
        super();
    }

    public BoundObject4(final String _input1) {
        super();
        this.input1 = _input1;
    }

    public String getInput1() {
        return input1;
    }

    public void setInput1(final String _input1) {
        this.input1 = _input1;
    }

    public String getSelectPath() {
        return (selectPath);
    }

    public void setSelectPath(final String _selectPath) {
        this.selectPath = _selectPath;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
