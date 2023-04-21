package tcg.common.util;

import java.io.IOException;
import org.apache.log4j.Logger;
import tcg.common.LoggerManager;

public class ProcessExecution implements Runnable {

    private String cmd_ = "";

    private boolean keepRunning_ = false;

    private Thread thread_ = null;

    private Process process = null;

    private Logger logger_ = LoggerManager.getLogger(ProcessExecution.class.toString());

    public ProcessExecution(String cmd) {
        cmd_ = cmd;
    }

    public ProcessExecution(String cmd, Logger logger) {
        cmd_ = cmd;
        logger_ = logger;
    }

    protected void finalize() throws Throwable {
        try {
            if (process != null) {
                process.destroy();
            }
        } finally {
            super.finalize();
        }
    }

    public void start() {
        if (thread_ != null && keepRunning_ && thread_.isAlive()) {
            return;
        }
        thread_ = new Thread(this);
        thread_.start();
    }

    public void stop(long millis) {
        if (thread_ == null) {
            return;
        }
        keepRunning_ = false;
        try {
            thread_.interrupt();
            thread_.join(millis);
        } catch (InterruptedException ie) {
        }
        thread_ = null;
    }

    public void run() {
        logger_.info("Execute: " + cmd_);
        try {
            process = Runtime.getRuntime().exec(cmd_);
        } catch (IOException ioe) {
            logger_.error("Can not execute command. Exception: " + ioe.toString());
            return;
        }
        StreamGobbler stdout = new StreamGobbler(process.getInputStream(), "STDOUT", null);
        StreamGobbler stderr = new StreamGobbler(process.getErrorStream(), "STDERR", logger_);
        stdout.start();
        stderr.start();
        try {
            process.waitFor();
        } catch (InterruptedException ie) {
        }
        stdout.stop(100);
        stdout.stop(100);
        process.destroy();
        process = null;
    }
}
