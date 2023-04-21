package us.gibb.dev.gwt.command.results;

import us.gibb.dev.gwt.command.Result;

public class IntegerResult implements Result {

    private static final long serialVersionUID = 1765485342906455982L;

    private Integer o;

    IntegerResult() {
    }

    public IntegerResult(Integer o) {
        this.o = o;
    }

    public Integer getInteger() {
        return o;
    }
}
