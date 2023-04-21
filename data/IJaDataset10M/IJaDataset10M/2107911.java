package org.matsim.core.utils.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * Runs an executable and waits until the executable finishes.
 *
 * @author mrieser
 */
public abstract class ExeRunner {

    static final Logger log = Logger.getLogger(ExeRunner.class);

    /**
	 * Runs an executable and waits until the executable finishes or until
	 * a certain amount of time has passed (timeout).
	 *
	 * @param cmd The system command to execute. E.g. "/bin/sh some-script.sh arg1 arg2"
	 * @param stdoutFileName specifies the file where stdout of the executable is redirected to.
	 * @param timeout A timeout in seconds. If the executable is still running after
	 * 					<code>timeout</code> seconds, the executable is stopped and this method returns.
	 * @return exit-code of the executable.
	 */
    public static int run(final String cmd, final String stdoutFileName, final int timeout) {
        return run(cmd, stdoutFileName, timeout, null);
    }

    /**
	 * Runs an executable and waits until the executable finishes or until
	 * a certain amount of time has passed (timeout).
	 *
	 * @param cmd The system command to execute. E.g. "/bin/sh some-script.sh arg1 arg2"
	 * @param stdoutFileName specifies the file where stdout of the executable is redirected to.
	 * @param timeout A timeout in seconds. If the executable is still running after
	 * 					<code>timeout</code> seconds, the executable is stopped and this method returns.
	 * @param workingDirectory the working directory to be used when calling the command
	 * @return exit-code of the executable.
	 */
    public static int run(final String cmd, final String stdoutFileName, final int timeout, final String workingDirectory) {
        ExternalExecutor myExecutor = new ExternalExecutor(cmd, stdoutFileName, workingDirectory);
        synchronized (myExecutor) {
            try {
                long timeoutMillis = 1000L * timeout;
                long startTime = System.currentTimeMillis();
                myExecutor.start();
                while (System.currentTimeMillis() - startTime < timeoutMillis) {
                    timeoutMillis -= System.currentTimeMillis() - startTime;
                    myExecutor.wait(timeoutMillis);
                    if (myExecutor.getState().equals(Thread.State.TERMINATED)) {
                        break;
                    }
                }
                if (!myExecutor.getState().equals(Thread.State.TERMINATED)) {
                    myExecutor.timeout = true;
                    myExecutor.interrupt();
                    myExecutor.join();
                }
            } catch (InterruptedException e) {
                log.info("ExeRunner.run() got interrupted while waiting for timeout", e);
            }
        }
        return myExecutor.erg;
    }

    private static class ExternalExecutor extends Thread {

        final String cmd;

        final String stdoutFileName;

        final String stderrFileName;

        final String workingDirectory;

        private Process p = null;

        public volatile boolean timeout = false;

        public int erg = -1;

        public ExternalExecutor(final String cmd, final String stdoutFileName, final String workingDirectory) {
            this.cmd = cmd;
            this.stdoutFileName = stdoutFileName;
            this.workingDirectory = workingDirectory;
            if (stdoutFileName.endsWith(".log")) {
                this.stderrFileName = stdoutFileName.substring(0, stdoutFileName.length() - 4) + ".err";
            } else {
                this.stderrFileName = stdoutFileName + ".err";
            }
        }

        public void killProcess() {
            if (this.p != null) {
                this.p.destroy();
            }
        }

        @Override
        public void run() {
            try {
                if (this.workingDirectory == null) {
                    this.p = Runtime.getRuntime().exec(this.cmd);
                } else {
                    this.p = Runtime.getRuntime().exec(this.cmd, null, new File(this.workingDirectory));
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(this.p.getInputStream()));
                BufferedReader err = new BufferedReader(new InputStreamReader(this.p.getErrorStream()));
                BufferedWriter writerIn = new BufferedWriter(new FileWriter(this.stdoutFileName));
                BufferedWriter writerErr = new BufferedWriter(new FileWriter(this.stderrFileName));
                StreamHandler outputHandler = new StreamHandler(in, writerIn);
                StreamHandler errorHandler = new StreamHandler(err, writerErr);
                outputHandler.start();
                errorHandler.start();
                log.info("Starting external exe with command: " + this.cmd);
                log.info("Output of the externel exe is written to: " + this.stdoutFileName);
                boolean processRunning = true;
                while (processRunning && !this.timeout) {
                    try {
                        this.p.waitFor();
                        this.erg = this.p.exitValue();
                        log.info("external exe returned " + this.erg);
                        processRunning = false;
                    } catch (InterruptedException e) {
                        log.info("Thread waiting for external exe to finish was interrupted");
                        this.erg = -3;
                    }
                }
                if (this.timeout) {
                    log.info("Timeout reached, killing process...");
                    killProcess();
                }
                try {
                    outputHandler.join();
                } catch (InterruptedException e) {
                    log.info("got interrupted while waiting for outputHandler to die.", e);
                }
                try {
                    errorHandler.join();
                } catch (InterruptedException e) {
                    log.info("got interrupted while waiting for errorHandler to die.", e);
                }
                writerIn.flush();
                writerIn.close();
                writerErr.flush();
                writerErr.close();
            } catch (IOException e) {
                e.printStackTrace();
                this.erg = -2;
            }
        }
    }

    static class StreamHandler extends Thread {

        private final BufferedReader in;

        private final BufferedWriter out;

        public StreamHandler(final BufferedReader in) {
            this(in, new BufferedWriter(new OutputStreamWriter(System.out)));
        }

        public StreamHandler(final BufferedReader in, final PrintStream out) {
            this(in, new BufferedWriter(new OutputStreamWriter(out)));
        }

        public StreamHandler(final BufferedReader in, final BufferedWriter out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                String line = null;
                while ((line = this.in.readLine()) != null) {
                    this.out.write(line);
                    this.out.write("\n");
                }
                this.out.flush();
            } catch (IOException e) {
                log.info("StreamHandler got interrupted");
                e.printStackTrace();
            }
        }
    }
}
