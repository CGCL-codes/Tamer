package com.monad.homerun.admin.impl;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * Quartz wrapper for server updater
 */
public class ServerTickleJob implements Job {

    public ServerTickleJob() {
    }

    public void execute(JobExecutionContext ctx) {
        Activator.adminMgr.updateServerState(AdminManager.SRV_EVENT_TICKLE);
    }
}
