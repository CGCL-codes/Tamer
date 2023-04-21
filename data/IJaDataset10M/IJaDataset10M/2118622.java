package org.vardb.util.runtime;

import org.vardb.util.CException;
import org.vardb.util.CStringHelper;

@SuppressWarnings("serial")
public class CCommandLineException extends CException {

    protected String command;

    protected Integer exitValue;

    protected String out;

    protected String err;

    public CCommandLineException(Throwable t, String command, Integer exitValue, String out, String err) {
        super(t);
        this.command = command;
        this.exitValue = exitValue;
        this.out = out;
        this.err = err;
    }

    public CCommandLineException(String message, String command, CCommandLine.Output output) {
        super(message);
        this.command = command;
        this.exitValue = output.getExitValue();
        this.out = output.getOut();
        this.err = output.getErr();
    }

    @Override
    public String getMessage() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(super.getMessage() + "\n");
        buffer.append("Problem executing command: " + this.command + "\n");
        if (this.exitValue != null) buffer.append("exit value: [" + this.exitValue + "]\n");
        if (CStringHelper.hasContent(this.out)) buffer.append("output stream: " + this.out + "\n");
        if (CStringHelper.hasContent(this.err)) buffer.append("error stream: " + this.err + "\n");
        return buffer.toString();
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public int getExitValue() {
        return this.exitValue;
    }

    public void setExitValue(final int exitValue) {
        this.exitValue = exitValue;
    }

    public String getOut() {
        return this.out;
    }

    public void setOut(final String out) {
        this.out = out;
    }

    public String getErr() {
        return this.err;
    }

    public void setErr(final String err) {
        this.err = err;
    }
}
