package org.gello.compiler;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to execute external programs, adapted from the RSL JavaCompiler class.
 * @author Erik Horstkotte
 */
public class ProgramRunner {

    private static Logger log = Logger.getLogger(ProgramRunner.class.getName());

    /** Disable stdout capture */
    public static final int NO_STDOUT = 1;

    /** Special behavior flags */
    private int behavior_flags;

    /** The stdout written by the program */
    private String stdoutBuf = "";

    /** The stderr written by the program */
    private String stderrBuf = "";

    /** Creates a new instance of ProgramRunner */
    public ProgramRunner() {
        this.behavior_flags = 0;
    }

    /** Creates a new instance of ProgramRunner with the specified behavior flags */
    public ProgramRunner(int behavior_flags) {
        this.behavior_flags = behavior_flags;
    }

    /** Execute a command-line program, capturing its stdout and stderr.
     * @return The program's integer exit status.
     */
    public int execute(String cmd) {
        log.fine("cmd=" + cmd);
        stdoutBuf = "";
        stderrBuf = "";
        Runtime r = Runtime.getRuntime();
        Process p = null;
        try {
            p = r.exec(cmd);
            if ((behavior_flags & NO_STDOUT) == 0) {
                stdoutBuf = captureStream(p.getInputStream());
            }
            stderrBuf = captureStream(p.getErrorStream());
            log.fine("exitValue=" + p.exitValue());
            return p.exitValue();
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /** Gets the stdout written by the program */
    public String getStdout() {
        return stdoutBuf.toString();
    }

    /** Gets the stderr written by the program */
    public String getStderr() {
        return stderrBuf.toString();
    }

    /** Close a stream, ignoring exceptions. */
    protected void close(InputStream s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException ie) {
            }
        }
    }

    /** Close a Writer, ignoring exceptions. */
    protected void close(Writer w) {
        if (w != null) {
            try {
                w.close();
            } catch (IOException ie) {
            }
        }
    }

    /** Capture a string to a StringBuffer */
    protected String captureStream(InputStream s) throws IOException {
        BufferedInputStream in = new BufferedInputStream(s);
        StringWriter out = new StringWriter();
        try {
            int c;
            while ((c = in.read()) != -1) out.write(c);
        } finally {
            close(out);
            close(in);
        }
        return out.toString();
    }
}
