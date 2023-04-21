package org.oclc.da.ndiipp.packagecontainer.pvt;

import org.oclc.da.exceptions.DAExceptionCodes;
import org.oclc.da.exceptions.DASystemException;
import org.oclc.da.logging.Logger;
import org.oclc.da.ndiipp.common.CallerIdentity;
import org.oclc.da.ndiipp.common.pvt.SimpleCallerIdentity;
import org.oclc.da.ndiipp.helper.LoginService;
import org.oclc.da.ndiipp.helper.PackageContainerHelper;
import org.oclc.da.ndiipp.helper.SystemHelper;
import org.oclc.da.ndiipp.helper.factory.PublicFactory;
import org.oclc.da.ndiipp.helper.factory.ToolsetFactory;
import org.oclc.da.ndiipp.helper.jobs.JobUtils;
import org.oclc.da.ndiipp.institution.InstitutionRef;
import org.oclc.da.ndiipp.schedule.ScheduleConst;
import org.oclc.da.ndiipp.schedule.ScheduleManager;
import org.oclc.da.ndiipp.session.SessionRef;
import org.oclc.da.ndiipp.spider.SpiderStatus;
import org.oclc.da.properties.SystemProps;
import org.quartz.JobExecutionException;

/**
 * EventAging
 *
 * Launching code for aging (deleting old) events for each institution. 
 *  
 * @author LH
 * @version 1.0, 
 * @created Dec 6, 2006
 */
public class PackageContainerStatusUpdate {

    private static final Logger logger = Logger.newInstance();

    String agentName = ScheduleConst.TYPE_PACKAGECONTAINER_STATUS;

    JobUtils jobUtils = null;

    private boolean running = false;

    private int status = SpiderStatus.NOT_RUNNING;

    boolean stop = false;

    /**
     * Constructor
     */
    public PackageContainerStatusUpdate() {
        jobUtils = new JobUtils();
    }

    /**
     * Runs package aging and package disk synchronization.
     * @param identities identities
     *
     */
    public void run(CallerIdentity identities) {
        running = true;
        status = SpiderStatus.RUNNING;
        this.run();
        running = false;
        status = SpiderStatus.SUCCESS;
    }

    /**
     * Runs package container status update
     *
     */
    public void run() {
        running = true;
        status = SpiderStatus.RUNNING;
        CallerIdentity identities = null;
        LoginService ls = null;
        try {
            logger.trace(this, "run", "run packagecontainer status update");
            ls = new LoginService();
            InstitutionRef[] instRefs = ls.getInstitutions();
            for (int i = 0; i < instRefs.length; i++) {
                String inst = instRefs[i].getGUID();
                PublicFactory pf = ToolsetFactory.load(ToolsetFactory.CONFIG_TOOLSET_PUBLIC);
                String s = pf.getLoginService().loginAgent(agentName, SystemProps.AGENTS_KEY, inst);
                if (s.startsWith("Invalid")) {
                    DASystemException ex = new DASystemException(DAExceptionCodes.INVOCATION_ERROR, new String[] { agentName, "agent authentication error: " + s });
                    logger.log(this, "packageContainerStatusUpdate", "s.startsWith(\"Invalid\")", ex);
                    throw new JobExecutionException(ex);
                }
                SessionRef sessionRef = new SessionRef(s);
                identities = new SimpleCallerIdentity(sessionRef);
                try {
                    PackageContainerHelper pch = new PackageContainerHelper(identities);
                    pch.resetHarvestInProcessToCanceled();
                } catch (Exception ex) {
                }
                SystemHelper sh = new SystemHelper(identities);
                sh.logout();
            }
            identities = null;
        } catch (Exception e) {
            status = SpiderStatus.FAILURE;
        } finally {
            if (identities != null) {
                SystemHelper sh = new SystemHelper(identities);
                sh.logout();
            }
        }
        running = false;
        status = SpiderStatus.SUCCESS;
    }

    /**
     * Cancel age and sync; not implemented.
     *
     */
    public void cancel() {
        stop = true;
    }

    /**
     * Is the AgeAndSync logic running?
     * @return true is this is running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Status
     * @return Status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Runs immediately
     *
     */
    public void schedule() {
        ScheduleManager scmgr = jobUtils.getScheduleManager(ScheduleConst.TYPE_PACKAGECONTAINER_STATUS);
        jobUtils.schedule(scmgr, ScheduleConst.TYPE_PACKAGECONTAINER_STATUS);
    }

    /**
     * Runs at hour and minute specified 
     * @param hourToRun
     * @param minuteToRun
     */
    public void schedule(int hourToRun, int minuteToRun) {
        ScheduleManager scmgr = jobUtils.getScheduleManager(ScheduleConst.TYPE_PACKAGECONTAINER_STATUS);
        jobUtils.schedule(scmgr, ScheduleConst.TYPE_PACKAGECONTAINER_STATUS, hourToRun, minuteToRun);
    }

    /**
     * Is this job already scheduled?
     * @return is scheduled
     */
    public boolean isScheduled() {
        return jobUtils.isScheduled(ScheduleConst.TYPE_PACKAGECONTAINER_STATUS);
    }
}
