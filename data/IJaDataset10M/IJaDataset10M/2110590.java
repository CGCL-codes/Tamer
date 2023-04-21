package com.continuent.tungsten.manager.client.api;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;

public class TMLToken extends CommonToken {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Object value = null;

    public TMLToken(CharStream input, int type, int channel, int start, int stop) {
        super(input, type, channel, start, stop);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
