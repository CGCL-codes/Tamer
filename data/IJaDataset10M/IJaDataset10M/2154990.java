package com.wangyu001.timer.task;

/**
 * 不涉及事务的任务
 * @author Administrator
 *
 */
public abstract class NoTsTask extends Task {

    public void execute() throws Exception {
        try {
            this.work();
            this.lastStartTime = System.currentTimeMillis();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
