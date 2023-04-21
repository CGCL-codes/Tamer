package com._5i56.timer.task;

import org.apache.log4j.Logger;

/**
 * 不涉及事务的任务
 * @author Administrator
 *
 */
public abstract class NoTsTask extends Task {

    protected static final Logger l = Logger.getLogger(NoTsTask.class);

    public void execute() throws Exception {
        try {
            this.work();
            this.lastStartTime = System.currentTimeMillis();
        } catch (Exception e) {
            throw e;
        }
    }
}
