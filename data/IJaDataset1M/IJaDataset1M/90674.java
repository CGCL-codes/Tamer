package org.fxplayer.service.folder;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.fxplayer.FXPlayerConstants;
import org.fxplayer.domain.Folder;
import org.fxplayer.domain.FolderLog;
import org.fxplayer.domain.ServerConfiguration;
import org.fxplayer.domain.FolderLog.Status;
import org.fxplayer.service.AbstractService;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.quartz.TriggerUtils;

/**
 * The Class FolderService.
 */
public class FolderService extends AbstractService implements FXPlayerConstants {

    private static final String SCAN_TRIGGER_LISTENER = "ScanTriggerListener";

    /** The Constant schedFactory. */
    private static final SchedulerFactory schedFactory = new org.quartz.impl.StdSchedulerFactory();

    /** The scheduler. */
    private Scheduler scheduler;

    /**
	 * Instantiates a new folder service.
	 */
    public FolderService() {
        try {
            scheduler = schedFactory.getScheduler();
            scheduler.start();
            scheduler.addTriggerListener(new TriggerListener() {

                @Override
                public String getName() {
                    return SCAN_TRIGGER_LISTENER;
                }

                @Override
                public void triggerComplete(final Trigger trigger, final JobExecutionContext ctx, final int arg2) {
                    final ServerConfiguration cfg = FX_PLAYER_CONFIGURATION_DAO.get();
                    if (cfg.getScanInterval() == 0) try {
                        scheduler.standby();
                    } catch (final SchedulerException e) {
                        LOG.fatal("Exception while standby scheduler", e);
                    }
                }

                @Override
                public void triggerFired(final Trigger trigger, final JobExecutionContext ctx) {
                }

                @Override
                public void triggerMisfired(final Trigger trigger) {
                }

                @Override
                public boolean vetoJobExecution(final Trigger trigger, final JobExecutionContext ctx) {
                    return false;
                }
            });
        } catch (final SchedulerException e) {
            LOG.fatal("Exception initializing FolderService", e);
        }
    }

    /**
	 * Delete folder.
	 * @param folderId
	 *          the folder id
	 */
    public void deleteFolder(final int folderId) {
        try {
            if (getCurrentLog(folderId) == null) {
                final boolean schedStandBy = scheduler.isInStandbyMode();
                scheduler.start();
                try {
                    LOG.trace("Starting DeleteJob for Folder:id=" + folderId);
                    final JobDetail jobDetail = new JobDetail("folder_" + folderId, Scheduler.DEFAULT_GROUP, DeleteJob.class);
                    jobDetail.getJobDataMap().put("folderId", folderId);
                    jobDetail.getJobDataMap().put("scannerStatuses", new LinkedHashMap<String, Object>());
                    final String jobId = "folder_" + folderId;
                    scheduler.removeJobListener(jobId);
                    scheduler.removeTriggerListener(jobId);
                    scheduler.deleteJob(jobId, Scheduler.DEFAULT_GROUP);
                    scheduler.addJob(jobDetail, true);
                    final Date date = new Date();
                    final Trigger trigger = TriggerUtils.makeImmediateTrigger(0, 0);
                    trigger.setName(jobId);
                    trigger.setJobName(jobId);
                    trigger.addTriggerListener(SCAN_TRIGGER_LISTENER);
                    trigger.setStartTime(date);
                    scheduler.scheduleJob(trigger);
                } catch (final SchedulerException e) {
                    LOG.fatal("Exception getting JobDetail:jobId=" + folderId, e);
                }
                if (schedStandBy) scheduler.standby();
            }
        } catch (final SchedulerException e) {
            LOG.fatal("Exception scanning folder " + folderId, e);
        }
    }

    /**
	 * Gets the current log.
	 * @param folderId
	 *          the folder id
	 * @return the current log
	 */
    @SuppressWarnings("unchecked")
    public FolderLog getCurrentLog(final int folderId) {
        try {
            final JobDataMap jobDataMap = scheduler.getJobDetail("folder_" + folderId, Scheduler.DEFAULT_GROUP).getJobDataMap();
            final FolderLog folderLog = (FolderLog) ((Map<String, Object>) jobDataMap.get("scannerStatuses")).get(folderId);
            return folderLog;
        } catch (final SchedulerException e) {
            return null;
        } catch (final NullPointerException e) {
            return null;
        }
    }

    /**
	 * Gets the latest.
	 * @param folderId
	 *          the folder id
	 * @return the latest
	 */
    public FolderLog getLatest(final int folderId) {
        final FolderLog log;
        if (FOLDER_DAO.findById(folderId) != null) {
            log = getCurrentLog(folderId);
            try {
                if (log == null || log.getStatus() == FolderLog.Status.LOADING_FINISHED) return FOLDER_LOG_DAO.getLatest(folderId);
            } catch (final Throwable e) {
                return FOLDER_LOG_DAO.getLatest(folderId);
            }
            return log;
        }
        log = new FolderLog(folderId);
        log.setStatus(Status.DELETING_FINISHED);
        return log;
    }

    /**
	 * Gets the logs.
	 * @param folderId
	 *          the folder id
	 * @return the logs
	 */
    public Collection<FolderLog> getLogs(final int folderId) {
        retrieveUser();
        return FOLDER_LOG_DAO.listBy("folderId", folderId);
    }

    /**
	 * Gets the log summary.
	 * @return the log summary
	 */
    public FolderLog getLogSummary() {
        final FolderLog log = new FolderLog();
        log.setStatus(Status.LOADED);
        final Collection<Folder> folders = FOLDER_DAO.findAllOrderByPath();
        for (final Folder folder : folders) {
            FolderLog currentLog = getCurrentLog(folder.getId());
            if (currentLog == null) currentLog = FOLDER_LOG_DAO.getLatest(folder.getId());
            log.addLog(currentLog);
        }
        return log;
    }

    /**
	 * @param scanInterval
	 * @param jobId
	 * @throws SchedulerException
	 */
    private boolean scheduleJob(int scanInterval, final int folderId, boolean launchNow) throws SchedulerException {
        try {
            LOG.trace((launchNow ? "Starting" : "Scheduling") + " UpdateJob for Folder:id=" + folderId);
            if (scanInterval == 0) scanInterval = 1;
            final String jobId = "folder_" + folderId;
            JobDetail jobDetail = scheduler.getJobDetail(jobId, Scheduler.DEFAULT_GROUP);
            if (jobDetail == null) {
                jobDetail = new JobDetail(jobId, Scheduler.DEFAULT_GROUP, UpdateJob.class);
                jobDetail.getJobDataMap().put("folderId", folderId);
                jobDetail.getJobDataMap().put("scannerStatuses", new LinkedHashMap<String, Object>());
                scheduler.addJob(jobDetail, true);
            }
            final FolderLog log = FOLDER_LOG_DAO.getLatest(folderId);
            launchNow = launchNow || log != null && log.getStatus() != FolderLog.Status.LOADED;
            Date date;
            if (launchNow && getCurrentLog(folderId) == null) date = new Date(); else date = new Date(log.getLastLaunch());
            Trigger trigger = scheduler.getTrigger(jobId, Scheduler.DEFAULT_GROUP);
            final boolean createTrigger = trigger == null;
            if (createTrigger) {
                trigger = TriggerUtils.makeHourlyTrigger(scanInterval);
                trigger.setName(jobId);
                trigger.setJobName(jobId);
                trigger.addTriggerListener(SCAN_TRIGGER_LISTENER);
            }
            trigger.setStartTime(date);
            if (createTrigger) scheduler.scheduleJob(trigger); else if (launchNow) scheduler.rescheduleJob(jobId, Scheduler.DEFAULT_GROUP, trigger);
        } catch (final SchedulerException e) {
            LOG.fatal("Exception getting JobDetail:jobId=" + folderId, e);
        }
        return launchNow;
    }

    /**
	 * Update folder.
	 * @param folderId
	 *          the folder id
	 */
    public void updateFolder(final int folderId) {
        try {
            if (getCurrentLog(folderId) == null) {
                final boolean schedStandBy = scheduler.isInStandbyMode();
                scheduler.start();
                if (!scheduleJob(FX_PLAYER_CONFIGURATION_DAO.get().getScanInterval(), folderId, true) && schedStandBy) scheduler.standby();
            } else throw new RuntimeException("Cannot delete folder");
        } catch (final SchedulerException e) {
            LOG.fatal("Exception scanning folder " + folderId, e);
        }
    }

    /**
	 * Update scheduler.
	 */
    public void updateScheduler() {
        try {
            final ServerConfiguration cfg = FX_PLAYER_CONFIGURATION_DAO.get();
            scheduler.start();
            final Collection<Folder> folders = FOLDER_DAO.findAllOrderByPath();
            final int scanInterval = cfg.getScanInterval();
            boolean launchNow = false;
            for (final Folder folder : folders) launchNow = launchNow || scheduleJob(scanInterval, folder.getId(), false);
            if (scanInterval == 0 && !launchNow) scheduler.standby();
        } catch (final SchedulerException e) {
            LOG.fatal("Exception updating Scheduler", e);
        }
    }
}
