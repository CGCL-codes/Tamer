package org.bellard.qemoon.runtime;

import java.io.IOException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class RuntimeWrapper {

    private String pid;

    private Logger logger = null;

    private Process qemuProcess = null;

    public static void main(String[] args) throws Exception {
        new RuntimeWrapper().exec(args);
    }

    public RuntimeWrapper() {
        this(null, Logger.getRootLogger());
    }

    public RuntimeWrapper(String pid, Logger logger) {
        if (pid == null) {
            this.pid = Thread.currentThread().getName();
        } else {
            this.pid = pid;
        }
        this.logger = logger;
    }

    public int exec(String[] cmdarray) throws IOException, InterruptedException {
        return exec(cmdarray, null);
    }

    public int exec(String[] cmdarray, String[] envp) throws IOException, InterruptedException {
        StringBuffer cmdString = new StringBuffer();
        for (int i = 0; i < cmdarray.length; i++) {
            cmdString.append(cmdarray[i]).append(' ');
        }
        logger.info(pid + " " + cmdString.toString());
        qemuProcess = null;
        if (envp != null) {
            qemuProcess = Runtime.getRuntime().exec(cmdarray, envp);
        } else {
            qemuProcess = Runtime.getRuntime().exec(cmdarray);
        }
        StreamThread in = new StreamThread(pid, logger, Level.INFO, qemuProcess.getInputStream());
        StreamThread err = new StreamThread(pid, logger, Level.WARN, qemuProcess.getErrorStream());
        in.start();
        err.start();
        int exitValue = qemuProcess.waitFor();
        String msg = "Shell exited with status " + exitValue;
        if (exitValue != 0) {
            logger.error(pid + " " + msg);
        } else {
            logger.info(pid + " " + msg);
        }
        return exitValue;
    }

    /**
	 * @return the qemuProcess
	 */
    public Process getQemuProcess() {
        return qemuProcess;
    }

    /**
	 * @param qemuProcess the qemuProcess to set
	 */
    public void setQemuProcess(Process qemuProcess) {
        this.qemuProcess = qemuProcess;
    }
}
