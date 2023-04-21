package com.ohioedge.j2ee.api.org.proc.ejb;

import org.j2eebuilder.util.RowSetFactoryException;
import com.ohioedge.j2ee.api.org.proc.ActivityBean;
import com.ohioedge.j2ee.api.org.proc.ActivityScheduleBean;
import com.ohioedge.j2ee.api.org.proc.JunctionBean;
import org.j2eebuilder.model.ManagedTransientObject;
import org.j2eebuilder.view.SessionException;
import java.util.Collection;
import org.j2eebuilder.view.BusinessDelegateException;
import org.j2eebuilder.view.Request;

/**
 * @(#)Scheduler.java	1.3.1 10/15/2002
 * Schedule activity using schedule logic
 * @version 1.3.1
 * @since Ohioedge Component API 1.2
 */
public interface Scheduler {

    public String schedule(Integer requesterID) throws SessionException;

    public String schedule(Integer activityTypeHierarchyID, Request requestHelperBean) throws SessionException;

    public void createActivitySchedule(ActivityScheduleBean activityScheduleVO, Request requestHelperBean) throws BusinessDelegateException;

    public Boolean convergeActivities(java.util.Collection<Integer> schedulableActivities, Integer activityID, Request requestHelperBean) throws SessionException;
}
