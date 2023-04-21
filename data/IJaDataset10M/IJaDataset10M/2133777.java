package com.ohioedge.j2ee.api.org.proc.ejb;

import org.j2eebuilder.view.*;
import org.j2eebuilder.util.*;
import org.j2eebuilder.model.ManagedTransientObject;
import java.net.*;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.util.Date;
import javax.ejb.TimedObject;
import javax.ejb.TimerService;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import org.j2eebuilder.util.LogManager;

/**
 * 
 * 
 * @version 1.3 Copyright (c) 2001, Ohioedge Modified by: tdixit Modified on:
 *          Modification Notes: Robot Bean modified by tdixit on 8/16/01. This
 *          bean is being modified to incorporate ActivitySchedule
 * @(#)WorkflowActivatorEJB.java 1.350 01/12/03 Robot class provides the
 *                               functionality to assign un-assigned
 *                               activityType inputs to the available mechanisms
 *                               (only with Worker roles). Robot supports two
 *                               types of assignments. First, random assignment
 *                               and second, assignments based on the mechanisms
 *                               familiarity with the input. NOTE: 1. Worker
 *                               role id - 1 is hardcoded into getWorkers.
 *                               Change for generality. 2. Input and
 *                               ActivityTypeHierarchyInputFactory represents
 *                               both parents(such as rowset) and children
 */
public class WorkflowActivatorEJB implements WorkflowActivator, TimedObject {

    private static transient LogManager log = new LogManager(WorkflowActivatorEJB.class);

    /**
	 * injected in ejb-jar.xml
	 */
    EntityManager entityManager = null;

    TimerHandle timerHandle = null;

    TimerService timerService = null;

    public String startTimer(Integer requesterID, String assignmentLogicID, Date pTimeOfCall, long intervalDuration, long pRemainingRepetitions) throws BusinessDelegateException {
        if (requesterID == null) {
            throw new BusinessDelegateException("Workflow timer could not be started. RequesterID is null.");
        }
        Timer timer = this.getRunningTask(getTimerInfo(requesterID));
        if (timer == null) {
            timer = timerService.createTimer(pTimeOfCall, intervalDuration, getTimerInfo(requesterID));
            this.timerHandle = timer.getHandle();
        }
        return (String) timer.getInfo();
    }

    public void stopTimer(Integer requesterID) throws BusinessDelegateException {
        if (requesterID == null) {
            throw new BusinessDelegateException("Workflow timer could not be stopped. RequesterID is null.");
        }
        try {
            Collection<Timer> timers = timerService.getTimers();
            for (Timer myTimer : timers) {
                if ((myTimer.getInfo().equals(getTimerInfo(requesterID)))) {
                    myTimer.cancel();
                    log.info("Workflow timer was successfully Cancelled for requesterID[" + getTimerInfo(requesterID) + "].");
                }
            }
        } catch (Exception e) {
            log.error("Exception while cancelling timer : " + e.toString());
        }
        return;
    }

    public String getRunningTasks() {
        StringBuffer buf = new StringBuffer();
        try {
            Collection<Timer> timers = timerService.getTimers();
            if (UtilityBean.getCurrentInstance().isNullOrEmpty(timers)) {
                buf.append("<TR>");
                buf.append("<TD>");
                buf.append("No services are currently running.");
                buf.append("</TD>");
                buf.append("</TR>");
            } else {
                for (Timer myTimer : timers) {
                    buf.append("<TR>");
                    buf.append("<TD>");
                    StringBuffer timerInfo = new StringBuffer();
                    timerInfo.append("<B>");
                    timerInfo.append(myTimer.getInfo());
                    timerInfo.append("</B>");
                    buf.append(timerInfo.toString());
                    buf.append("</TD>");
                    buf.append("</TR>");
                }
            }
        } catch (Exception e) {
            log.error("getRunningTasks:", e);
            buf.append("<TR>");
            buf.append("<TD>");
            buf.append("Unable to retrieve the list of running services.");
            buf.append("</TD>");
            buf.append("</TR>");
        }
        return buf.toString();
    }

    public Timer getRunningTask(String timerName) {
        try {
            Collection<Timer> timers = timerService.getTimers();
            for (Timer myTimer : timers) {
                if ((myTimer.getInfo().equals(timerName))) {
                    return myTimer;
                }
            }
        } catch (Exception e) {
            log.error("Exception while checking timer: " + e.toString());
        }
        return null;
    }

    public String getTimerInfo() {
        if (this.timerHandle != null) {
            Timer timer = this.timerHandle.getTimer();
            return (String) timer.getInfo();
        }
        return null;
    }

    String getTimerInfo(Integer requesterID) {
        StringBuffer timerNameBuffer = new StringBuffer();
        timerNameBuffer.append(requesterID);
        timerNameBuffer.append(".Workflow[");
        timerNameBuffer.append("Assign, Schedule & Route");
        timerNameBuffer.append("]");
        return timerNameBuffer.toString();
    }

    Integer getRequesterID(Timer timer) throws NumberFormatException {
        String timerInfo = (String) timer.getInfo();
        Integer foundRequesterID = Integer.valueOf(timerInfo.substring(0, timerInfo.indexOf(".")));
        return foundRequesterID;
    }

    public void ejbTimeout(Timer timer) {
        try {
            this.execute(getRequesterID(timer));
        } catch (NumberFormatException se) {
            log.error(se);
        } catch (BusinessDelegateException se) {
            log.error(se);
            try {
                org.j2eebuilder.util.ServiceLocatorBean.getCurrentInstance().getSessionContext().setRollbackOnly();
            } catch (org.j2eebuilder.util.ServiceLocatorException sle) {
                log.error(sle.toString());
            }
        }
    }

    public WorkflowActivatorEJB() {
    }

    /**
	 * runs the robot
	 */
    void execute(Integer requesterID) throws BusinessDelegateException {
        if (requesterID == null) {
            throw new BusinessDelegateException("Workflow timer could not be trigger. RequesterID is null.");
        }
        try {
            Assigner assigner = (Assigner) ServiceLocatorBean.getCurrentInstance().connectToService("Assigner");
            assigner.assign("Random", requesterID);
        } catch (Exception e) {
            log.log("execute(): Unable to assign [Random] for requesterID[" + requesterID + "] on[" + (new java.util.Date()) + "]", e, log.DEBUG);
            log.error("execute(): Unable to assign [Random] for requesterID[" + requesterID + "] on[" + (new java.util.Date()) + "]: " + e.toString());
        }
        try {
            Router router = (Router) ServiceLocatorBean.getCurrentInstance().connectToService("Router");
            router.route(requesterID);
        } catch (Exception e) {
            log.log("execute(): Unable to route for requesterID[" + requesterID + "] on[" + (new java.util.Date()) + "]", e, log.DEBUG);
            log.error("execute(): Unable to route for requesterID[" + requesterID + "] on[" + (new java.util.Date()) + "]: " + e.toString());
        }
        try {
            Scheduler scheduler = (Scheduler) ServiceLocatorBean.getCurrentInstance().connectToService("Scheduler");
            scheduler.schedule(requesterID);
        } catch (Exception e) {
            log.log("execute(): Unable to schedule for requesterID[" + requesterID + "] on[" + (new java.util.Date()) + "]", e, log.DEBUG);
            log.error("execute(): Unable to schedule for requesterID[" + requesterID + "] on[" + (new java.util.Date()) + "]:" + e.toString());
        }
        return;
    }
}
