package com.ohioedge.j2ee.api.org.proc.ejb;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;

/**
 * @(#)ActivityScheduleStatusTypeHome.java	1.3.1 10/15/2002
 * @version 1.3.1
 * @since Ohioedge Component API 1.2
 */
public interface ActivityScheduleStatusTypeHome extends EJBLocalHome {

    public ActivityScheduleStatusType create(Integer id, String name, String description, Integer level, Integer createdBy, java.sql.Timestamp createdOn) throws CreateException;

    public ActivityScheduleStatusType findByPrimaryKey(ActivityScheduleStatusTypePK pk) throws FinderException;

    public Collection findByActivityTypeHierarchyIDAndMechanismID(Integer activityTypeHierarchyID, Integer mechanismID) throws FinderException;

    /**
	findRouteStatusType is used by RobotBean. This method should return
	StatusType that states that the status of activitySchedule can not be
	further	routed or modified. In other words this status type states that
	the activitySchedule is in its final status and can not be further changed.
	**/
    public Collection findRouteStatusType() throws FinderException;

    public Collection findByName(String name) throws FinderException;

    public Collection findByDescription(String description) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByPrivilegeID(Integer mechPrivID) throws FinderException;
}
