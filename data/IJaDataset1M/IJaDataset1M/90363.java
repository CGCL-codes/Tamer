package org.oclc.da.ndiipp.helper.jobs;

import java.util.Calendar;
import java.util.Date;
import org.oclc.da.common.query.QueryConst;
import org.oclc.da.exceptions.DAException;
import org.oclc.da.exceptions.DAExceptionCodes;
import org.oclc.da.exceptions.DASystemException;
import org.oclc.da.informationobject.InformationObjectType;
import org.oclc.da.logging.Logger;
import org.oclc.da.ndiipp.common.CallerIdentity;
import org.oclc.da.ndiipp.common.DataContainer;
import org.oclc.da.ndiipp.common.DataDictionary;
import org.oclc.da.ndiipp.common.pvt.DataDictionaryDataContainer;
import org.oclc.da.ndiipp.common.pvt.SimpleCallerIdentity;
import org.oclc.da.ndiipp.common.pvt.UniversalDataContainer;
import org.oclc.da.ndiipp.factory.AuthenticatedFactory;
import org.oclc.da.ndiipp.factory.ServiceFactory;
import org.oclc.da.ndiipp.helper.LoginService;
import org.oclc.da.ndiipp.helper.SystemHelper;
import org.oclc.da.ndiipp.helper.factory.PublicFactory;
import org.oclc.da.ndiipp.helper.factory.ToolsetFactory;
import org.oclc.da.ndiipp.institution.InstitutionRef;
import org.oclc.da.ndiipp.schedule.Schedule;
import org.oclc.da.ndiipp.schedule.ScheduleConst;
import org.oclc.da.ndiipp.schedule.ScheduleManager;
import org.oclc.da.ndiipp.schedule.ScheduleRef;
import org.oclc.da.ndiipp.session.SessionRef;
import org.oclc.da.properties.SystemProps;

/**
 * JobUtils
 *
 * Utilities for quartz jobs. 
 *  
 * @author LH
 * @version 1.0, 
 * @created Dec 6, 2006
 */
public class JobUtils {

    private static final Logger logger = Logger.newInstance();

    /**Constructor
     */
    public JobUtils() {
    }

    /**
     * Run job immediately
     * @param scmgr schedule manager
     * @param jobName job name
     *
     */
    public void schedule(ScheduleManager scmgr, String jobName) {
        schedule(scmgr, jobName, -1, -1);
    }

    /**
     * Runs jobName at the hour and minute specified (24 hour clock) daily
     * @param scmgr manager
     * @param jobName  Get the constants for jobName from ScheduleConst.TYPE*,
     * such as ScheduleConst.TYPE_PACKAGE_AGING. 
     * @param hourToRun hours to run
     * @param minuteToRun minutes to run
     */
    public void schedule(ScheduleManager scmgr, String jobName, int hourToRun, int minuteToRun) {
        try {
            logger.trace(this, "schedule", jobName + " task being scheduled");
            Schedule schedule = scmgr.create(new ScheduleRef());
            DataDictionaryDataContainer dddc = new DataDictionaryDataContainer(schedule, new DataDictionary(new ScheduleConst()));
            dddc.setAttr(ScheduleConst.TYPE, jobName);
            dddc.setAttr(ScheduleConst.RECURRING_FLAG, ScheduleConst.RECURRING_FLAG_DAILY);
            if (hourToRun > -1) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hourToRun);
                cal.set(Calendar.MINUTE, minuteToRun);
                Date startDate = cal.getTime();
                dddc.setAttr(ScheduleConst.START_DATE_TIME, startDate);
            }
            scmgr.add(schedule);
        } catch (DAException ex) {
            logger.log(this, "schedule", "scmgr.create(new ScheduleRef()) or scmgr.add(schedule) for job " + jobName, ex);
        }
        logger.trace(this, "schedule", jobName + " task scheduled");
    }

    /**
     * is a schedule of type 'aging' already in the system?
     * @param jobName  Get the constants for jobName from ScheduleConst.TYPE*,
     * such as ScheduleConst.TYPE_PACKAGE_AGING. 
     * @return is scheduled?
     */
    public boolean isScheduled(String jobName) {
        ScheduleManager scmgr = getScheduleManager(jobName);
        return isScheduled(scmgr, jobName);
    }

    /**
     * is a schedule of type 'aging' already in the system?
     * @param scmgr manager
     * @param jobName  Get the constants for jobName from ScheduleConst.TYPE*,
     * such as ScheduleConst.TYPE_PACKAGE_AGING. 
     * @return is scheduled?
     */
    public boolean isScheduled(ScheduleManager scmgr, String jobName) {
        try {
            String[] fields = new String[] { ScheduleConst.TYPE };
            String[] values = new String[] { jobName };
            DataContainer query = new DataContainer(InformationObjectType.QUERY);
            query.setAttr(QueryConst._QUERY_TYPE, QueryConst._QUERY_TYPE_PARAMETERIZED);
            query.setAttr(QueryConst._OBJECT_TYPE, InformationObjectType.SCHEDULE.toString());
            query.setAttr(QueryConst._FIELDS, fields);
            query.setAttr(QueryConst._VALUES, values);
            UniversalDataContainer udc = new UniversalDataContainer(null, query);
            int count = scmgr.searchCount(udc);
            if (count > 0) {
                logger.trace(this, "isScheduled", jobName + "task already scheduled");
                return true;
            }
            logger.trace(this, "isScheduled", jobName + " task is not scheduled");
            return false;
        } catch (DAException ex) {
            return false;
        }
    }

    /**
     * Gets the schedule manager
     * @param agentName  Use a constnat of the type: ScheduleConst.TYPE_PACKAGE_AGING
     * @return shcedule manager
     */
    public ScheduleManager getScheduleManager(String agentName) {
        AuthenticatedFactory authFactory = null;
        LoginService ls = new LoginService();
        InstitutionRef[] instRefs = ls.getInstitutions();
        String inst = instRefs[0].getGUID();
        PublicFactory pf = ToolsetFactory.load(ToolsetFactory.CONFIG_TOOLSET_PUBLIC);
        String s = pf.getLoginService().loginAgent(agentName, SystemProps.AGENTS_KEY, inst);
        if (s.startsWith("Invalid")) {
            DASystemException ex = new DASystemException(DAExceptionCodes.INVOCATION_ERROR, new String[] { agentName, "agent authentication error: " + s });
            logger.log(this, "getScheduleManager", "s.startsWith(\"Invalid\")", ex);
            return null;
        }
        SessionRef sessionRef = new SessionRef(s);
        CallerIdentity identities = new SimpleCallerIdentity(sessionRef);
        authFactory = ServiceFactory.load(ServiceFactory.CONFIG_TOOLSET_AUTHENTICATED, sessionRef);
        ScheduleManager scmgr = authFactory.getScheduleManager();
        SystemHelper sh = new SystemHelper(identities);
        sh.logout();
        return scmgr;
    }
}
