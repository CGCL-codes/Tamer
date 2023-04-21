package com.sun.jdi.connect;

/**
 * A target VM was successfully launched, but terminated with an
 * error before a connection could be established. This exception
 * provides the {@link java.lang.Process} object for the launched
 * target to help in diagnosing the problem.
 *
 * @author Gordon Hirsch
 * @since  1.3
 */
public class VMStartException extends Exception {

    Process process;

    public VMStartException(Process process) {
        super();
        this.process = process;
    }

    public VMStartException(String message, Process process) {
        super(message);
        this.process = process;
    }

    public Process process() {
        return process;
    }
}
