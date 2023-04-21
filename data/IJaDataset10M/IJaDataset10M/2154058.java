package org.ini4j.spi;

import org.ini4j.Config;
import java.io.PrintWriter;

abstract class AbstractFormatter implements HandlerBase {

    private static final char OPERATOR = '=';

    private static final char COMMENT = '#';

    private static final char SPACE = ' ';

    private static final String NEWLINE = "\n";

    private Config _config = Config.getGlobal();

    private boolean _header = true;

    private PrintWriter _output;

    @Override
    public void handleComment(String comment) {
        for (String line : comment.split(NEWLINE)) {
            getOutput().print(COMMENT);
            getOutput().print(line);
            getOutput().print(getConfig().getLineSeparator());
        }
        if (_header) {
            getOutput().print(getConfig().getLineSeparator());
            setHeader(false);
        }
    }

    @Override
    public void handleOption(String optionName, String optionValue) {
        if (getConfig().isStrictOperator()) {
            if (getConfig().isEmptyOption() || (optionValue != null)) {
                getOutput().print(escapeFilter(optionName));
                getOutput().print(OPERATOR);
            }
            if (optionValue != null) {
                getOutput().print(escapeFilter(optionValue));
            }
            if (getConfig().isEmptyOption() || (optionValue != null)) {
                getOutput().print(getConfig().getLineSeparator());
            }
        } else {
            String value = ((optionValue == null) && getConfig().isEmptyOption()) ? "" : optionValue;
            if (value != null) {
                getOutput().print(escapeFilter(optionName));
                getOutput().print(SPACE);
                getOutput().print(OPERATOR);
                getOutput().print(SPACE);
                getOutput().print(escapeFilter(value));
                getOutput().print(getConfig().getLineSeparator());
            }
        }
        setHeader(false);
    }

    protected Config getConfig() {
        return _config;
    }

    protected void setConfig(Config value) {
        _config = value;
    }

    protected PrintWriter getOutput() {
        return _output;
    }

    protected void setOutput(PrintWriter value) {
        _output = value;
    }

    void setHeader(boolean value) {
        _header = value;
    }

    String escapeFilter(String input) {
        return getConfig().isEscape() ? EscapeTool.getInstance().escape(input) : input;
    }
}
