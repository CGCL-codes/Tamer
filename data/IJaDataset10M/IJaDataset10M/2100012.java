package de.sicari.starter.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class starts a new child {@link Process} and pipes its
 * input and output into defined streams.
 *
 * @see Runtime
 *
 * @author Matthias Pressfreund
 * @version "$Id: ProcessRunner.java 204 2007-07-11 19:26:55Z jpeters $"
 */
public class ProcessRunner {

    /**
     * The command to execute
     */
    protected String[] cmd_;

    /**
     * The stream to pipe the standard output of the process into
     */
    protected OutputStream out_;

    /**
     * The stream to pipe the error output of the process into
     */
    protected OutputStream err_;

    /**
     * The stream to be piped into the standard input of the process
     */
    protected InputStream in_;

    /**
     * The new child process
     */
    protected Process process_;

    /**
     * Create a <code>ProcessRunner</code>.
     *
     * @param cmd The command array for building the {@link Process}
     * @param out The <code>OutputStream</code> to pipe the standard output
     *   of the <code>Process</code> into
     * @param err The <code>OutputStream</code> to pipe the error output of
     *   the <code>Process</code> into
     * @param in The <code>InputStream</code> to pipe into the input of the
     *   <code>Process</code>
     */
    public ProcessRunner(String[] cmd, OutputStream out, OutputStream err, InputStream in) {
        cmd_ = cmd;
        out_ = out;
        err_ = err;
        in_ = in;
    }

    /**
     * Run a {@link Process}, catch its standard and error
     * <code>OutputStream</code> and pipe input to its
     * <code>InputStream</code>.
     *
     * @return The <code>Process</code> exit code
     *
     * @throws Exception
     *   if an error occurres during process execution
     */
    public int start() throws Exception {
        process_ = Runtime.getRuntime().exec(cmd_);
        new Pipe("OUT", process_.getInputStream(), out_).start();
        new Pipe("ERR", process_.getErrorStream(), err_).start();
        new Pipe("IN", in_, process_.getOutputStream()).start();
        return process_.waitFor();
    }

    /**
     * Kill the subprocess.
     */
    public void stop() {
        if (process_ != null) {
            process_.destroy();
        }
    }

    /**
     * This thread simply pipes data from an <code>InputStream</code> into
     * an <code>OutputStream</code>.
     */
    protected static class Pipe extends Thread {

        /**
         * The stream to read from
         */
        protected InputStream pin_;

        /**
         * The stream to write to
         */
        protected OutputStream pout_;

        /**
         * Create a <code>Pipe</code>.
         */
        public Pipe(String name, InputStream in, OutputStream out) {
            super(ProcessRunner.Pipe.class.getName() + " " + name);
            pin_ = in;
            pout_ = out;
        }

        /**
         * The actual <code>Pipe</code> implementation.
         */
        @Override
        public void run() {
            int data;
            if ((pin_ == null) || (pout_ == null)) {
                return;
            }
            try {
                while ((data = pin_.read()) >= 0) {
                    pout_.write(data);
                    pout_.flush();
                }
            } catch (IOException ioe) {
            }
        }
    }
}
