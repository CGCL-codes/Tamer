package com.google.clearsilver.jsilver.autoescape;

/**
 * Global configuration options specific to <a href="http://go/autoescapecs">auto escaping</a>.
 */
public class AutoEscapeOptions {

    private boolean propagateEscapeStatus = false;

    private boolean logEscapedVariables = false;

    public boolean getLogEscapedVariables() {
        return logEscapedVariables;
    }

    public void setLogEscapedVariables(boolean logEscapedVariables) {
        this.logEscapedVariables = logEscapedVariables;
    }

    public boolean getPropagateEscapeStatus() {
        return propagateEscapeStatus;
    }

    public void setPropagateEscapeStatus(boolean propagateEscapeStatus) {
        this.propagateEscapeStatus = propagateEscapeStatus;
    }
}
