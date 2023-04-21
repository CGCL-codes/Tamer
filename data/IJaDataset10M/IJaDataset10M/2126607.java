package com.ems.common.datatransformer.definition.parser;

import com.ems.common.datatransformer.definition.Definition;

/**
 * @author Chiknin
 */
public abstract class ParserDefinition extends Definition implements IParserDefinition {

    public static final String FAILMODE_EXCEPTION = "exception";

    public static final String FAILMODE_RECORD = "record";

    protected String failMode = FAILMODE_EXCEPTION;

    public String getFailMode() {
        return failMode;
    }

    public void setFailMode(String failMode) {
        this.failMode = failMode;
    }
}
