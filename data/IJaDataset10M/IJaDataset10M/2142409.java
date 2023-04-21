package com.ohua.engine.utils;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class OperatorLogFormatter extends Formatter {

    private String _opName = null;

    private String _opID = null;

    public OperatorLogFormatter(String opName, String opID) {
        _opName = opName;
        _opID = opID;
    }

    @Override
    public String format(LogRecord arg0) {
        return _opName + "(" + _opID + "): " + super.formatMessage(arg0) + "\n";
    }
}
