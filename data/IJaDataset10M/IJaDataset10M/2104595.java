package com.ohioedge.j2ee.api.org.proc.ejb;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;

/**
 * @(#)ActivityTypeHome.java	1.3.1 10/15/2002
 */
public interface ActivityTypeHome extends EJBLocalHome {

    public ActivityType create(String name, String description, Integer activityTypeID, Integer organizationID, Integer sequenceID, String isLeaf, Integer createdBy, java.sql.Timestamp createdOn) throws CreateException;

    public ActivityType findByPrimaryKey(ActivityTypePK pk) throws FinderException;

    public Collection findByOrganizationID(Integer organizationID) throws FinderException;

    public Collection findByActivityTypeHierarchyID(Integer activityTypeHierarchyID) throws FinderException;

    public Collection findByIsLeaf(Integer orgID, String isLeaf) throws FinderException;

    public Collection findByName(Integer orgID, String name) throws FinderException;

    public Collection findByDescription(Integer orgID, String description) throws FinderException;

    /**
	* Used by perform robot
	*/
    public Collection findEmailActivityTypesByOrganizationIDAndIsLeaf(Integer orgID, String isLeaf) throws FinderException;

    public Collection findSMSActivityTypesByOrganizationIDAndIsLeaf(Integer orgID, String isLeaf) throws FinderException;

    public Collection findByOrganizationIDAndIsLeafAndInstruction(Integer orgID, String isLeaf) throws FinderException;

    public Collection findEntryByOrganizationID(Integer orgID) throws FinderException;

    public Collection findExitByOrganizationID(Integer orgID) throws FinderException;

    /**
	*	find destination activity types of the source activityTypeID
	*/
    public Collection findDestinationsByActivityTypeID(Integer sourceActivityTypeID) throws FinderException;
}
