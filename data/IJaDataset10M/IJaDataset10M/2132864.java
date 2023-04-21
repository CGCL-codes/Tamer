package ca.etsmtl.ihe.xdsitest.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

public class ProcessWrapper {

    public static class Result {

        private int exitValue;

        private Throwable error;

        private Throwable errorStderr;

        private Throwable errorStdout;

        public int getExitValue() {
            return this.exitValue;
        }

        public Throwable getError() {
            return this.error;
        }

        public Throwable getErrorStderr() {
            return this.errorStderr;
        }

        public Throwable getErrorStdout() {
            return this.errorStdout;
        }

        public boolean checkExecutionError() {
            return (this.error != null) || (this.errorStderr != null) || (this.errorStdout != null);
        }

        public boolean checkExitStatusOk() {
            return (this.exitValue == 0) && !checkExecutionError();
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("Result: ");
            if (this.error != null) {
                buf.append("general error (");
                buf.append(this.error);
                buf.append(")");
            } else {
                buf.append(this.exitValue);
                if (this.errorStderr != null) {
                    buf.append(", stderr error (");
                    buf.append(this.errorStderr);
                    buf.append(")");
                }
                if (this.errorStdout != null) {
                    buf.append(", stdout error (");
                    buf.append(this.errorStdout);
                    buf.append(")");
                }
            }
            return buf.toString();
        }
    }

    private static class ConsumerRunnable implements Runnable {

        private BufferedReader reader;

        private PrintWriter writer;

        private Throwable throwable;

        public void run() {
            try {
                while (true) {
                    String line;
                    line = this.reader.readLine();
                    if (line == null) {
                        break;
                    }
                    this.writer.println(line);
                }
            } catch (Throwable t) {
                this.throwable = t;
            }
        }
    }

    public static Result executeCommand(String[] cmdarray, String[] envp, File dir, PrintWriter writerStdout, PrintWriter writerStderr) {
        ProcessWrapper instance;
        Result result;
        boolean interrupted;
        instance = new ProcessWrapper(cmdarray, envp, dir, writerStdout, writerStderr);
        result = instance.start(true);
        interrupted = false;
        try {
            while (result == null) {
                try {
                    result = instance.join();
                } catch (InterruptedException e) {
                    interrupted = true;
                } finally {
                    instance.interrupt();
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return result;
    }

    private final ConsumerRunnable consumerStderr;

    private final ConsumerRunnable consumerStdout;

    private final String[] cmdarray;

    private final String[] envp;

    private final File dir;

    private Result result;

    private boolean destroyed;

    private Process process;

    private Thread threadStderr;

    private Thread threadStdout;

    public ProcessWrapper(String[] cmdarray, String[] envp, File dir, PrintWriter writerStdout, PrintWriter writerStderr) {
        this.consumerStderr = new ConsumerRunnable();
        this.consumerStdout = new ConsumerRunnable();
        this.cmdarray = cmdarray;
        this.envp = envp;
        this.dir = dir;
        this.consumerStderr.writer = writerStderr;
        this.consumerStdout.writer = writerStdout;
    }

    public Result start(boolean blocking) {
        if (this.result == null) {
            Result result = new Result();
            Thread threadStderr = (blocking) ? null : new Thread(consumerStderr);
            Thread threadStdout = new Thread(consumerStdout);
            this.destroyed = false;
            this.threadStderr = null;
            this.threadStdout = null;
            try {
                this.process = Runtime.getRuntime().exec(this.cmdarray, this.envp, this.dir);
            } catch (IOException e) {
                result.error = e;
                return result;
            }
            this.result = result;
            try {
                consumerStderr.reader = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));
                consumerStdout.reader = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
                if (threadStderr != null) {
                    threadStderr.start();
                    this.threadStderr = threadStderr;
                }
                threadStdout.start();
                this.threadStdout = threadStdout;
            } finally {
                if (this.threadStdout == null) {
                    interrupt();
                }
            }
            if (threadStderr == null) {
                consumerStderr.run();
            }
        }
        return null;
    }

    public Result join() throws InterruptedException {
        Result result;
        if (this.threadStderr != null) {
            this.threadStderr.join();
            this.threadStderr = null;
            this.result.errorStderr = this.consumerStderr.throwable;
        }
        if (this.threadStdout != null) {
            this.threadStdout.join();
            this.threadStdout = null;
            this.result.errorStdout = this.consumerStdout.throwable;
        }
        if (this.process != null) {
            this.result.exitValue = this.process.waitFor();
            this.process = null;
        }
        result = this.result;
        this.result = null;
        return result;
    }

    public boolean checkRunning() {
        if ((this.process != null) && !this.destroyed) {
            if ((this.threadStderr != null) && !this.threadStderr.isAlive()) {
                this.threadStderr = null;
                this.result.errorStderr = this.consumerStderr.throwable;
            }
            if ((this.threadStdout != null) && !this.threadStdout.isAlive()) {
                this.threadStdout = null;
                this.result.errorStdout = this.consumerStdout.throwable;
            }
            try {
                this.result.exitValue = this.process.exitValue();
                this.process = null;
            } catch (IllegalThreadStateException e) {
                if (this.result.checkExecutionError()) {
                    interrupt();
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public void interrupt() {
        if ((this.process != null) && !this.destroyed) {
            this.process.destroy();
            this.destroyed = true;
        }
    }

    public static void main(String[] args) throws Exception {
        final Thread mainThread = Thread.currentThread();
        final PrintWriter stderr = new PrintWriter(System.err);
        final PrintWriter stdout = new PrintWriter(System.out);
        ProcessWrapper instance;
        Result result;
        Thread timeBomb = new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    ;
                }
                System.err.println("kill");
                mainThread.interrupt();
            }
        });
        timeBomb.setDaemon(true);
        timeBomb.start();
        instance = new ProcessWrapper(args, null, null, stdout, stderr);
        result = instance.start(false);
        System.err.println("check running: " + instance.checkRunning());
        while (result == null) {
            try {
                result = instance.join();
            } catch (InterruptedException e) {
                instance.interrupt();
            }
        }
        System.err.println(result);
    }
}
