package com.ohioedge.j2ee.api.org.proc;

import org.j2eebuilder.view.*;
import org.j2eebuilder.util.*;
import org.j2eebuilder.model.ManagedTransientObject;
import org.j2eebuilder.BuilderException;
import com.ohioedge.j2ee.api.org.proc.ejb.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.RowSet;
import javax.naming.NamingException;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)ActivityTypeHierarchyInputFactoryDelegate.java	1.3.1 10/15/2002
 * ActivityTypeHierarchyInputFactoryDelegate is a java bean with the main function of facilitating
 * communication between JSPs and ActivityTypeHierarchyInputFactory EJB
 * @version 1.3.1
 * Note: inputFactoryID in ActivityTypeHierarchyInputFactoryDelegate is different than inputFactoryID of activityBean
 * In the activityBean, if inputFactoryType is Customer than inputFactoryID is value of
 * customerID.
 */
public class ActivityTypeHierarchyInputFactoryDelegate extends org.j2eebuilder.view.DefaultSharedOrganizationBusinessDelegate {

    private static transient LogManager log = new LogManager(ActivityTypeHierarchyInputFactoryDelegate.class);

    public java.util.Map findMapOfActivityTypeHierarchyAndActivityVORowSet(ActivityTypeHierarchyInputFactoryBean activityTypeHierarchyInputFactoryVO, Integer mechanismID, Request requestHelperBean) throws BusinessDelegateException {
        Integer activityTypeHierarchyID = activityTypeHierarchyInputFactoryVO.getActivityTypeHierarchyID();
        if (activityTypeHierarchyID == null) {
            throw new BusinessDelegateException("Null activityTypeHierarchyID were found in ActivityTypeHierarchyInputFactoryBeans.");
        }
        String queryString = activityTypeHierarchyInputFactoryVO.getInputFactoryContent();
        if (queryString == null || queryString.trim().length() == 0) {
            throw new BusinessDelegateException("Null query string was found. Make sure that the query file associated with this report exists.");
        }
        RowSet rowSet = null;
        java.util.Map map = new java.util.HashMap();
        try {
            ActivityManager activityManager = (ActivityManager) ServiceLocatorBean.getCurrentInstance().connectToService("ActivityManager");
            log.debug("ActivityTypeHierarchyInputFactoryBean is calling ActivityTypeHierarchyBean.search - findBottomLevel:" + activityTypeHierarchyID);
            Collection<ActivityTypeHierarchyBean> colOfUOBActivityTypeHierarchyVO = (new ActivityTypeHierarchyDelegate()).search(activityTypeHierarchyInputFactoryVO.getActivityTypeHierarchyVO().getOrganizationID(), activityTypeHierarchyID, null, "findBottomLevel", requestHelperBean);
            log.debug("ActivityTypeHierarchyInputFactoryBean is DONE calling ActivityTypeHierarchyBean.search - findBottomLevel:" + activityTypeHierarchyID);
            if (colOfUOBActivityTypeHierarchyVO != null && colOfUOBActivityTypeHierarchyVO.size() > 0) {
                for (ActivityTypeHierarchyBean activityTypeHierarchyVO : colOfUOBActivityTypeHierarchyVO) {
                    if (activityTypeHierarchyVO == null) {
                        throw new BusinessDelegateException("Could not retrieve ActivityTypeHierarchyUOB value object(s) activityTypeHierarchy from activityTypeHierarchyID[" + activityTypeHierarchyID + "]");
                    }
                    rowSet = (RowSet) activityManager.findRowSetOfActivityVO(queryString, activityTypeHierarchyVO.getParentActivityTypeID(), activityTypeHierarchyVO.getActivityTypeID());
                    map.put(activityTypeHierarchyVO, rowSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessDelegateException("findMapOfActivityTypeHierarchyAndActivityVORowSet():" + e.getMessage());
        }
        return map;
    }

    public java.util.Map findMapOfActivityTypeHierarchyAndAssignedActivityVORowSet(ActivityTypeHierarchyInputFactoryBean activityTypeHierarchyInputFactoryVO, Integer mechanismID, Request requestHelperBean) throws BusinessDelegateException {
        Integer activityTypeHierarchyID = activityTypeHierarchyInputFactoryVO.getActivityTypeHierarchyID();
        if (activityTypeHierarchyID == null) {
            throw new BusinessDelegateException("Null activityTypeHierarchyID were found in ActivityTypeHierarchyInputFactoryBeans.");
        }
        String queryString = activityTypeHierarchyInputFactoryVO.getInputFactoryContent();
        if (queryString == null || queryString.trim().length() == 0) {
            throw new BusinessDelegateException("Null query string was found. Make sure that the query file associated with this report exists.");
        }
        RowSet rowSet = null;
        java.util.Map map = new java.util.HashMap();
        try {
            ActivityManager activityManager = (ActivityManager) ServiceLocatorBean.getCurrentInstance().connectToService("ActivityManager");
            log.debug("ActivityTypeHierarchyInputFactoryBean is calling ActivityTypeHierarchyBean.search - findBottomLevel:" + activityTypeHierarchyID);
            Collection<ActivityTypeHierarchyBean> colOfUOBActivityTypeHierarchyVO = (new ActivityTypeHierarchyDelegate()).search(activityTypeHierarchyInputFactoryVO.getActivityTypeHierarchyVO().getOrganizationID(), activityTypeHierarchyID, null, "findBottomLevel", requestHelperBean);
            log.debug("ActivityTypeHierarchyInputFactoryBean is DONE calling ActivityTypeHierarchyBean.search - findBottomLevel:" + activityTypeHierarchyID);
            if (colOfUOBActivityTypeHierarchyVO != null && colOfUOBActivityTypeHierarchyVO.size() > 0) {
                for (ActivityTypeHierarchyBean activityTypeHierarchyVO : colOfUOBActivityTypeHierarchyVO) {
                    if (activityTypeHierarchyVO == null) {
                        throw new BusinessDelegateException("Could not retrieve ActivityTypeHierarchyUOB value object(s) activityTypeHierarchy from activityTypeHierarchyID[" + activityTypeHierarchyID + "]");
                    }
                    rowSet = (RowSet) activityManager.findRowSetOfActivityVOByAssigneeID(queryString, activityTypeHierarchyVO.getParentActivityTypeID(), activityTypeHierarchyVO.getActivityTypeID(), mechanismID);
                    map.put(activityTypeHierarchyVO, rowSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessDelegateException("findMapOfActivityTypeHierarchyAndActivityVORowSet():" + e.getMessage());
        }
        return map;
    }

    public ActivityTypeHierarchyInputFactoryDelegate() {
    }

    public org.j2eebuilder.model.ManagedTransientObject create(org.j2eebuilder.ComponentDefinition componentDefinition, ManagedTransientObject valueObject, Request requestHelperBean) throws BusinessDelegateException {
        try {
            ActivityTypeHierarchyInputFactoryBean activityTypeHierarchyInputFactoryBean = (ActivityTypeHierarchyInputFactoryBean) valueObject;
            if (activityTypeHierarchyInputFactoryBean.getActivityTypeHierarchyID() == null) {
                throw new BusinessDelegateException("create():PrimaryKey attribute[activityTypeHierarchyID] is null.");
            }
            if (activityTypeHierarchyInputFactoryBean.getInputFactoryID() == null) {
                throw new BusinessDelegateException("create():PrimaryKey attribute[inputFactoryID] is null.");
            }
            return super.create(componentDefinition, valueObject, requestHelperBean);
        } catch (Exception e) {
            throw new BusinessDelegateException("create:" + e.toString());
        }
    }

    /**
     *
     *	default search method. takes in search criteria and
     *	returns collection.
     *	To restrict access to the mechanism,
     *	organizationID parameter is received from user's session
     */
    public Collection search(Integer activityTypeHierarchyID, Integer actTypeID, Integer inFactID, Integer mechID, String criteria) {
        Collection col = new java.util.HashSet();
        try {
            if (activityTypeHierarchyID != null) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query("ActivityTypeHierarchyInputFactory", "findByActivityTypeHierarchyID", new Class[] { java.lang.Integer.class }, new Object[] { activityTypeHierarchyID }, null, false);
            } else if (inFactID != null && mechID != null) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query("ActivityTypeHierarchyInputFactory", "findByInputFactoryIDAndMechanismID", new Class[] { java.lang.Integer.class, java.lang.Integer.class }, new Object[] { inFactID, mechID }, null, false);
            } else if (inFactID != null && mechID == null) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query("ActivityTypeHierarchyInputFactory", "findByInputFactoryID", new Class[] { java.lang.Integer.class }, new Object[] { inFactID }, null, false);
            } else if (inFactID == null && mechID != null) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query("ActivityTypeHierarchyInputFactory", "findByMechanismID", new Class[] { java.lang.Integer.class }, new Object[] { mechID }, null, false);
            }
        } catch (Exception e) {
            log.error("search():", e);
        }
        return col;
    }

    /**
     *	Collection search
     *
     **/
    public Collection search(org.j2eebuilder.ComponentDefinition componentDefinition, org.j2eebuilder.model.ManagedTransientObject valueObject, org.j2eebuilder.view.Request requestHelperBean) throws BusinessDelegateException {
        String criteria = null;
        try {
            criteria = requestHelperBean.getStringParameter(componentDefinition.getName() + "_criteria")[0];
        } catch (org.j2eebuilder.util.RequestParameterException rpe) {
            log.debug("Search parameter criteria is null.");
        }
        String criteriaType = null;
        try {
            criteriaType = requestHelperBean.getStringParameter(componentDefinition.getName() + "_criteriaType")[0];
        } catch (org.j2eebuilder.util.RequestParameterException rpe) {
            log.debug("Search parameter criteriaType is null.");
        }
        Integer activityTypeHierarchyID = null;
        try {
            activityTypeHierarchyID = requestHelperBean.getIntegerParameter("activityTypeHierarchyID")[0];
        } catch (org.j2eebuilder.util.RequestParameterException rpe) {
            log.debug("Search parameter activityTypeHierarchyID is null.");
        }
        Integer inputFactoryID = null;
        try {
            inputFactoryID = requestHelperBean.getIntegerParameter("inputFactoryID")[0];
        } catch (org.j2eebuilder.util.RequestParameterException rpe) {
            log.debug("Search parameter inputFactoryID is null.");
        }
        Integer mechanismID = null;
        try {
            mechanismID = requestHelperBean.getSessionObject().getMechanismID();
        } catch (org.j2eebuilder.view.SessionException rpe) {
            throw new BusinessDelegateException(rpe);
        }
        Collection col = new java.util.HashSet();
        try {
            if (activityTypeHierarchyID != null) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query(componentDefinition.getName(), "findByActivityTypeHierarchyID", new Class[] { java.lang.Integer.class }, new Object[] { activityTypeHierarchyID }, requestHelperBean, true);
            } else if (inputFactoryID != null && mechanismID != null) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query(componentDefinition.getName(), "findByInputFactoryIDAndMechanismID", new Class[] { java.lang.Integer.class, java.lang.Integer.class }, new Object[] { inputFactoryID, mechanismID }, requestHelperBean, true);
            } else if (inputFactoryID != null && mechanismID == null) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query(componentDefinition.getName(), "findByInputFactoryID", new Class[] { java.lang.Integer.class }, new Object[] { inputFactoryID }, requestHelperBean, true);
            } else if (inputFactoryID == null && mechanismID != null) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query(componentDefinition.getName(), "findByMechanismID", new Class[] { java.lang.Integer.class }, new Object[] { mechanismID }, requestHelperBean, true);
            }
        } catch (Exception e) {
            throw new BusinessDelegateException(e);
        }
        return col;
    }
}
