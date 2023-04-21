package com.totsp.mavenplugin.gwt.scripting;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class executes a commandline and watches it's output.
 * 
 * @author willpugh
 */
public class ProcessWatcher {

    public static final long DEFAULT_SLEEP = 200;

    Process process;

    String[] commands;

    String[] environment;

    File dir;

    StreamSucker out;

    StreamSucker err;

    private ProcessWatcher(String[] commands, String[] environment, File dir) {
        this.commands = commands;
        this.environment = environment;
        this.dir = dir;
    }

    public ProcessWatcher(String[] commands, String[] environment) {
        this(commands, environment, null);
    }

    public ProcessWatcher(String[] commands) {
        this(commands, null, null);
    }

    public void startProcess() throws IOException {
        process = Runtime.getRuntime().exec(commands, environment, dir);
        if (out == null) {
            out = new StreamSucker(new NulStream());
        }
        if (err == null) {
            err = new StreamSucker(new NulStream());
        }
        out.setIn(process.getInputStream());
        err.setIn(process.getErrorStream());
        out.start();
        err.start();
    }

    public void startProcess(OutputStream stdout, OutputStream stderr) throws IOException {
        if (stdout != null) out = new StreamSucker(stdout);
        if (stderr != null) err = new StreamSucker(stderr);
        startProcess();
    }

    public void startProcess(StringBuffer stdout, StringBuffer stderr) throws IOException {
        if (stdout != null) out = new StreamSucker(new StringBufferStream(stdout));
        if (stderr != null) err = new StreamSucker(new StringBufferStream(stderr));
        startProcess();
    }

    public void startProcess(StringBuilder stdout, StringBuilder stderr) throws IOException {
        if (stdout != null) out = new StreamSucker(new StringBuilderStream(stdout));
        if (stderr != null) err = new StreamSucker(new StringBuilderStream(stderr));
        startProcess();
    }

    public OutputStream getStdIn() {
        return process.getOutputStream();
    }

    public int exitValue() {
        return process.exitValue();
    }

    public void destroy() {
        process.destroy();
    }

    public int waitFor() throws InterruptedException {
        try {
            process.waitFor();
        } finally {
            out.shutdown();
            err.shutdown();
        }
        out.join();
        err.join();
        return process.exitValue();
    }

    public static class StreamSucker extends Thread {

        private final long sleeptime;

        private final OutputStream out;

        private InputStream in;

        volatile boolean allDone = false;

        public StreamSucker(OutputStream out, long sleeptime) {
            this.sleeptime = sleeptime;
            if (out == null) this.out = new NulStream(); else this.out = out;
        }

        public StreamSucker(OutputStream out) {
            this(out, DEFAULT_SLEEP);
        }

        public StreamSucker() {
            this(null, DEFAULT_SLEEP);
        }

        public void shutdown() {
            allDone = true;
        }

        public void siphonAvailableBytes(byte[] buf) throws IOException {
            int available = getIn().available();
            while (available > 0) {
                available = getIn().read(buf);
                getOut().write(buf, 0, available);
                available = getIn().available();
            }
        }

        public void run() {
            byte[] buf = new byte[4096];
            try {
                while (!allDone) {
                    synchronized (this) {
                        this.wait(getSleeptime());
                    }
                    siphonAvailableBytes(buf);
                }
                siphonAvailableBytes(buf);
            } catch (InterruptedException e) {
            } catch (IOException e) {
            } finally {
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public long getSleeptime() {
            return sleeptime;
        }

        public OutputStream getOut() {
            return out;
        }

        public InputStream getIn() {
            return in;
        }

        public void setIn(InputStream in) {
            this.in = in;
        }
    }

    public static class NulStream extends OutputStream {

        public void write(int i) throws IOException {
        }
    }

    public static class StringBufferStream extends OutputStream {

        final StringBuffer buf;

        public StringBufferStream(StringBuffer buf) {
            this.buf = buf;
        }

        public void write(int i) throws IOException {
            buf.append((char) i);
        }
    }

    public static class StringBuilderStream extends OutputStream {

        final StringBuilder buf;

        public StringBuilderStream(StringBuilder buf) {
            this.buf = buf;
        }

        public void write(int i) throws IOException {
            buf.append((char) i);
        }
    }
}
