package org.bellard.qemoon.runtime;

import java.util.Observable;
import org.apache.log4j.Logger;
import org.bellard.qemoon.commands.CommandGenerator;
import org.bellard.qemoon.model.VM;

/**
 * 
 * @author cbourque
 */
public class QemuThread extends Observable implements Runnable {

    private Thread thread;

    private CommandGenerator commandGenerator;

    private VM vim;

    private String qemuPath;

    private boolean daemon;

    private Logger logger;

    private RuntimeWrapper runtime;

    /**
	 * Constructor
	 * 
	 * @param vm
	 * @param qemuPath
	 * @param daemon
	 * @param logger
	 */
    public QemuThread(VM vm, String qemuPath, boolean daemon, Logger logger) {
        this.qemuPath = qemuPath;
        this.vim = vm;
        this.daemon = daemon;
        this.logger = logger;
        init();
    }

    /**
	 * @return the runtime
	 */
    public RuntimeWrapper getRuntime() {
        return runtime;
    }

    /**
	 * @param runtime
	 *            the runtime to set
	 */
    public void setRuntime(RuntimeWrapper runtime) {
        this.runtime = runtime;
    }

    public void init() {
        this.commandGenerator = new CommandGenerator(qemuPath, vim.getPreferenceStore(), vim.getName());
    }

    public void start() {
        thread = new Thread(this);
        thread.setName(vim.getName());
        thread.setDaemon(daemon);
        thread.start();
    }

    public void run() {
        String[] cmd = commandGenerator.buidCommandArgument();
        runtime = new RuntimeWrapper(vim.getName(), logger);
        try {
            vim.setStarted(true);
            int exitValue = runtime.exec(cmd);
            if (countObservers() > 0) {
                setChanged();
                notifyObservers(new Integer(exitValue));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            vim.setStarted(false);
        }
    }

    /**
	 * @return the thread
	 */
    public Thread getThread() {
        return thread;
    }

    /**
	 * @param thread
	 *            the thread to set
	 */
    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
