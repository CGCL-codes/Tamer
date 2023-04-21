package org.colombbus.tangara.ide.model.cmdline;

/**
 * A default {@link Cmd} implementation.
 *
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
public class DefaultCmd implements Cmd {

    private String cmdLine;

    public DefaultCmd() {
    }

    public DefaultCmd(String cmdLine) {
        if (cmdLine == null) throw new IllegalArgumentException("cmdLine argument must not be null");
        this.cmdLine = cmdLine;
    }

    @Override
    public String getCmdLine() throws IllegalStateException {
        if (cmdLine == null) throw new IllegalStateException("cmdLine argument has not been initialized");
        return cmdLine;
    }

    @Override
    public void setCmdLine(String cmdLine) throws IllegalArgumentException {
        if (cmdLine == null) throw new IllegalArgumentException("cmdLine argument must not be null");
        this.cmdLine = cmdLine;
    }
}
