package com.ohioedge.j2ee.api.org.proc;

import org.j2eebuilder.BuilderHelperBean;
import org.j2eebuilder.InstanceNotFoundException;
import org.j2eebuilder.NonManagedBeansDefinition;
import org.j2eebuilder.model.TransientObject;
import org.j2eebuilder.view.*;
import org.j2eebuilder.util.*;
import org.j2eebuilder.model.ManagedTransientObject;
import com.ohioedge.j2ee.api.org.proc.ejb.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Map;
import org.j2eebuilder.InterfaceDefinition;
import org.j2eebuilder.InstanceException;
import org.j2eebuilder.component.InstanceAttributeBean;
import org.j2eebuilder.component.InstanceAttributeDelegate;
import org.j2eebuilder.util.LogManager;
import org.j2eebuilder.view.BusinessDelegateException;

/**
 * @(#)ActivityDelegate.java	1.3.1 10/15/2002
 * ActivityDelegate is a java bean with the main function of facilitating
 * communication between JSPs and Activity EJB.
 *
 * Manages ActivityDataBean
 *
 * @version 1.3.1
 */
public class ActivityDelegate extends org.j2eebuilder.view.DefaultSharedOrganizationBusinessDelegate {

    private static transient LogManager log = new LogManager(ActivityDelegate.class);

    /**
     *	find activity status types this mechanism has access to.
     *	returned is a collection with no duplicates
     */
    public Collection<ActivityStatusTypeBean> getColOfActivityStatusTypeVO(Integer activityTypeHierarchyID, Integer mechanismID, Request requestHelperBean) throws BusinessDelegateException {
        Collection activityStatusTypes = new HashSet();
        try {
            Collection privileges = (new ActivityTypeHierarchyMechanismDelegate()).findPrivileges(activityTypeHierarchyID, mechanismID);
            return (new ActivityStatusTypeDelegate()).getColOfActivityStatusTypeVO(privileges, requestHelperBean);
        } catch (Exception e) {
            log.error(".getColOfActivityStatusTypeVO():", e);
        }
        return activityStatusTypes;
    }

    /**
     * if isAssigned return true
     * if isNotAssigned, but isAssignor then create assignment and return true
     */
    private boolean validateAssignment(ActivityBean activityVO, Integer mechanismID, Request requestHelperBean) {
        boolean bValid = false;
        if (this.isAssigned(activityVO, mechanismID)) {
            bValid = true;
        } else {
            if (this.isAssignor(activityVO.getOrganizationID(), activityVO.getActivityTypeHierarchyID(), mechanismID, requestHelperBean)) {
                String msg = createAssignment(activityVO.getActivityID(), mechanismID, requestHelperBean);
                if (msg.equals("Successful")) {
                    bValid = true;
                }
            }
        }
        return bValid;
    }

    /**
     *	Check if this activity is assigned to the current mechanism.
     *	If not and if the current mechanism has assignor privilege,
     *	then automatically assign it to the current mechanism.
     */
    public String createAssignment(Integer activityID, Integer mechanismID, Request requestHelperBean) {
        String msg = null;
        try {
            Integer assigneeID = mechanismID;
            Integer assignorID = mechanismID;
            Integer approvedBy = mechanismID;
            Integer createdBy = mechanismID;
            java.sql.Timestamp assignedOn = new java.sql.Timestamp((new java.util.Date()).getTime());
            java.sql.Timestamp startedOn = assignedOn;
            java.sql.Timestamp completedOn = assignedOn;
            java.sql.Timestamp approvedOn = assignedOn;
            String comments = "Self-assigned and approved.";
            String notes = null;
            AssignmentDelegate assignmentDelegate = new AssignmentDelegate();
            assignmentDelegate.create(activityID, assigneeID, assignorID, assignedOn, startedOn, completedOn, approvedBy, approvedOn, comments, notes, createdBy, requestHelperBean);
            msg = "Successful";
        } catch (Exception e) {
            msg = "" + e.toString();
        }
        return msg;
    }

    boolean isUpdateAllowed(ActivityBean activityVO, Integer mechanismID, Request requestHelperBean) throws BusinessDelegateException {
        boolean isPrivileged = false;
        Integer activityTypeHierarchyID = activityVO.getActivityTypeHierarchyID();
        Integer curLevel = null;
        try {
            curLevel = activityVO.getCurrentActivityStatusTypeLevel();
            if (curLevel == null) {
                curLevel = new Integer(0);
            }
            Integer astLevel = null;
            Collection<ActivityStatusTypeBean> statusTypes = this.getColOfActivityStatusTypeVO(activityTypeHierarchyID, mechanismID, requestHelperBean);
            if (statusTypes != null) {
                for (ActivityStatusTypeBean ast : statusTypes) {
                    astLevel = ast.getLevel();
                    if (astLevel == null) {
                        astLevel = new Integer(0);
                    }
                    if (astLevel.intValue() > curLevel.intValue()) {
                        isPrivileged = true;
                        break;
                    }
                }
            }
        } catch (Exception re) {
            log.error(".isUpdateAllowed():", re);
        }
        log.debug("**!!ISUPDATEALLOWED:->" + isPrivileged);
        return isPrivileged;
    }

    public boolean isAssigned(ActivityBean activityVO, Integer mechanismID) {
        if (activityVO != null) {
            for (AssignmentBean assignmentVO : activityVO.getColOfAssignmentVO()) {
                if (assignmentVO.getAssigneeID().equals(mechanismID)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *	This method checks to see if the Mechanism has a "Assignor"
     *	privilege. Returns a boolean value true of false.
     */
    public boolean isAssignor(Integer organizationID, Integer activityTypeHierarchyID, Integer mechanismID, Request requestHelperBean) {
        boolean bAssignor = false;
        try {
            bAssignor = ((new ActivityTypeHierarchyMechanismDelegate()).isAssignor(organizationID, activityTypeHierarchyID, mechanismID)).booleanValue();
        } catch (Exception re) {
            log.error(".isAssignor():", re);
        }
        log.debug("**!!ISASSIGNOR_PRIV:->" + bAssignor);
        return bAssignor;
    }

    /**
     *	This method checks to see if the Mechanism has a "Originator"
     *	privilege. Returns a boolean value true of false.
     */
    boolean isOriginator(Integer organizationID, Integer activityTypeHierarchyID, Integer mechanismID, Request requestHelperBean) {
        boolean bOriginator = false;
        try {
            log.debug("isOriginator(organizationID[" + organizationID + "], mechanismID[" + mechanismID + "], requestHelperBean[" + requestHelperBean + "]): determining if mechanism has originator privilege.");
            bOriginator = (new ActivityTypeHierarchyMechanismDelegate()).isOriginator(organizationID, activityTypeHierarchyID, mechanismID).booleanValue();
        } catch (Exception re) {
            log.error("isOriginator(organizationID[" + organizationID + "], mechanismID[" + mechanismID + "], requestHelperBean[" + requestHelperBean + "]): Unable to determine if mechanism has originator privilege - reeturning false.", re);
        }
        return bOriginator;
    }

    public ActivityDelegate() {
    }

    /**
     *
     * 1.  activity can not be created in released status. if
     * nextSuccessorID is not null then reset it
     * to null. Ideally, it should return back a message
     * return "Activity can not be created in released status.
     * NextSuccessorID was set to null.";
     * However, JSP page is not able to reset nextSuccessorID to
     * null using <jsp:setProperty ..."*">. As the setNextSuccessorID
     * parameter is Integer and the JSP container is not accepting null. It
     * requires valid integer.
     *
     * 2.  if there exists open order for this customerRequirement
     * or for this buyer (if buyer is not null), then do not
     * create. Ask user to close the existing open activity
     * first.
     *
     * 3. this method is also used by campaignActivator/campaignDelegate for
     * creating activity. however mechanism could be null after user logs off
     * this is causing requestHelperBean.getSessionObject().getMechanismID() to fail
     */
    public org.j2eebuilder.model.ManagedTransientObject create(org.j2eebuilder.ComponentDefinition componentDefinition, ManagedTransientObject valueObject, Request requestHelperBean) throws BusinessDelegateException {
        try {
            ActivityBean activityBean = (ActivityBean) valueObject;
            if (activityBean.getActivityID() != null) {
                throw new BusinessDelegateException("Reset the currently loaded activity before creating a new activity or use update to save changes to the current activity.");
            }
            if (activityBean.getActivityTypeHierarchyID() == null) {
                activityBean.setActivityTypeHierarchyVO(ActivityTypeHierarchyDelegate.findActivityTypeHierarchyVO(activityBean.getParentActivityTypeID(), activityBean.getActivityTypeID()));
                if (activityBean.getActivityTypeHierarchyVO() == null) {
                    throw new BusinessDelegateException("Primary key attribute activityTypeHierarchyID can not be null.");
                }
                activityBean.setActivityTypeHierarchyID(activityBean.getActivityTypeHierarchyVO().getActivityTypeHierarchyID());
            } else {
                activityBean.setParentActivityTypeID(activityBean.getActivityTypeHierarchyVO().getParentActivityTypeID());
                activityBean.setActivityTypeID(activityBean.getActivityTypeHierarchyVO().getActivityTypeID());
            }
            if (activityBean.getActivityTypeHierarchyVO() == null) {
                throw new BusinessDelegateException("Primary key attribute [activityTypeHierarchyID] can not be null.");
            }
            if (activityBean.getOrganizationID() == null) {
                throw new BusinessDelegateException("Primary key attribute [organizationID] can not be null.");
            }
            try {
                Integer selectedInputID = requestHelperBean.getIntegerParameter("inputID")[0];
                Integer selectedInputTypeID = requestHelperBean.getIntegerParameter("inputTypeID")[0];
                String selectedActivityName = requestHelperBean.getStringParameter("inputName")[0];
                String selectedActivityDescription = requestHelperBean.getStringParameter("inputDescription")[0];
                activityBean.setInputID(selectedInputID);
                activityBean.setInputTypeID(selectedInputTypeID);
                activityBean.setName(selectedActivityName);
                activityBean.setDescription(selectedActivityDescription);
            } catch (org.j2eebuilder.util.RequestParameterException rpe) {
                log.debug("No user selected input found. Error[" + rpe.toString() + "]");
            }
            log.debug("activitydelegate: inputID[" + activityBean.getInputID() + "] inputTypeID[" + activityBean.getInputTypeID() + "]");
            if (activityBean.getInputID() == null) {
                try {
                    Map<Integer, Integer> input = ((com.ohioedge.j2ee.sales.SessionBean) requestHelperBean.getSessionObject()).getInputID(requestHelperBean);
                    Integer inputID = null;
                    Integer inputTypeID = null;
                    for (Map.Entry<Integer, Integer> mapEntry : input.entrySet()) {
                        inputID = mapEntry.getKey();
                        inputTypeID = mapEntry.getValue();
                    }
                    if (inputID == null) {
                        throw new BusinessDelegateException("Primary key attribute [inputID] can not be null. Unable to locate it from the active session.");
                    }
                    if (inputTypeID == null) {
                        throw new BusinessDelegateException("Primary key attribute [inputTypeID] can not be null. Unable to locate it from the active session.");
                    }
                    activityBean.setInputID(inputID);
                    activityBean.setInputTypeID(inputTypeID);
                } catch (Exception e) {
                    throw new BusinessDelegateException(e);
                }
            }
            if (activityBean.getInputTypeID() == null) {
                throw new BusinessDelegateException("Primary key attribute [inputTypeID] can not be null.");
            }
            if (activityBean.getCampaignID() == null) {
                throw new BusinessDelegateException("Primary key attribute [campaignID] can not be null.");
            }
            if (this.isOriginator(activityBean.getOrganizationID(), activityBean.getActivityTypeHierarchyID(), requestHelperBean.getSessionObject().getMechanismID(), requestHelperBean)) {
                activityBean.setActivityDate(new java.sql.Timestamp(new java.util.Date().getTime()));
                return super.create(componentDefinition, valueObject, requestHelperBean);
            } else {
                throw new BusinessDelegateException("No privilege to originate/create activities.");
            }
        } catch (Exception e) {
            log.printStackTrace(e, LogManager.ERROR);
            throw new BusinessDelegateException(e);
        }
    }

    public org.j2eebuilder.model.ManagedTransientObject statelessCreate(org.j2eebuilder.ComponentDefinition componentDefinition, ManagedTransientObject valueObject, Request requestHelperBean) throws BusinessDelegateException {
        try {
            ActivityBean activityBean = (ActivityBean) valueObject;
            if (activityBean.getActivityID() != null) {
                throw new BusinessDelegateException("statelessCreate(): Reset the currently loaded activity before creating a new (first) activity.");
            }
            if (activityBean.getActivityTypeHierarchyID() == null) {
                activityBean.setActivityTypeHierarchyVO(ActivityTypeHierarchyDelegate.findActivityTypeHierarchyVO(activityBean.getParentActivityTypeID(), activityBean.getActivityTypeID()));
                if (activityBean.getActivityTypeHierarchyVO() == null) {
                    throw new BusinessDelegateException("statelessCreate(): Primary key attribute activityTypeHierarchyID can not be null.");
                }
                activityBean.setActivityTypeHierarchyID(activityBean.getActivityTypeHierarchyVO().getActivityTypeHierarchyID());
            } else {
                activityBean.setParentActivityTypeID(activityBean.getActivityTypeHierarchyVO().getParentActivityTypeID());
                activityBean.setActivityTypeID(activityBean.getActivityTypeHierarchyVO().getActivityTypeID());
            }
            if (activityBean.getActivityTypeHierarchyVO() == null) {
                throw new BusinessDelegateException("statelessCreate(): Primary key attribute [activityTypeHierarchyID] can not be null.");
            }
            if (activityBean.getOrganizationID() == null) {
                throw new BusinessDelegateException("statelessCreate(): Primary key attribute [organizationID] can not be null.");
            }
            log.debug("statelessCreate(): activityBean.getOrganizationID()[" + activityBean.getOrganizationID() + "]");
            if (activityBean.getInputID() == null) {
                throw new BusinessDelegateException("statelessCreate(): Primary key attribute [inputID] can not be null.");
            }
            if (activityBean.getInputTypeID() == null) {
                throw new BusinessDelegateException("statelessCreate(): Primary key attribute [inputTypeID] can not be null.");
            }
            if (activityBean.getCampaignID() == null) {
                throw new BusinessDelegateException("statelessCreate(): Primary key attribute [campaignID] can not be null.");
            }
            if (this.isOriginator(activityBean.getOrganizationID(), activityBean.getActivityTypeHierarchyID(), requestHelperBean.getSessionObject().getMechanismID(), requestHelperBean)) {
                activityBean.setActivityDate(new java.sql.Timestamp(new java.util.Date().getTime()));
                return super.statelessCreate(componentDefinition, activityBean, requestHelperBean);
            } else {
                throw new BusinessDelegateException("statelessCreate(): No privilege to originate/create activities. requesterID[" + requestHelperBean.getSessionObject().getMechanismID() + "]");
            }
        } catch (Exception e) {
            throw new BusinessDelegateException(e);
        }
    }

    public org.j2eebuilder.model.ManagedTransientObject update(org.j2eebuilder.ComponentDefinition componentDefinition, ManagedTransientObject valueObject, Request requestHelperBean) throws BusinessDelegateException {
        try {
            Integer mechanismID = requestHelperBean.getSessionObject().getMechanismID();
            ActivityBean activityBean = (ActivityBean) valueObject;
            log.debug("updating started. for activityBean activityID[" + activityBean.getActivityID() + "] inputTypeID[" + activityBean.getInputTypeID() + "] input[" + activityBean.getInputID() + "] name[" + activityBean.getName() + "] description[" + activityBean.getDescription() + "]");
            if (this.validateAssignment(activityBean, mechanismID, requestHelperBean) && this.isUpdateAllowed(activityBean, mechanismID, requestHelperBean)) {
                ManagedTransientObject updatedManagedComponentObject = super.update(componentDefinition, valueObject, requestHelperBean);
                ActivityDataDelegate activityDataDelegate = (ActivityDataDelegate) org.j2eebuilder.BuilderHelperBean.getCurrentInstance().getBusinessDelegate("ActivityData", requestHelperBean, NonManagedBeansDefinition.TRANSIENT_STATE_PAGE);
                InstanceAttributeDelegate instanceAttributeDelegate = (InstanceAttributeDelegate) org.j2eebuilder.BuilderHelperBean.getCurrentInstance().getBusinessDelegate("InstanceAttribute", requestHelperBean, NonManagedBeansDefinition.TRANSIENT_STATE_PAGE);
                Collection<InstanceAttributeBean> colOfUpdatedActivityDataIncludingNew = activityDataDelegate.getActivityData(activityBean, requestHelperBean);
                if (colOfUpdatedActivityDataIncludingNew != null) {
                    for (InstanceAttributeBean instanceAttributeVO : colOfUpdatedActivityDataIncludingNew) {
                        try {
                            log.debug("ActivityDelegate: About to update instanceAttributeVO :" + instanceAttributeVO + " : name [" + instanceAttributeVO.getAttributeName() + "] Value[" + instanceAttributeVO.getAttributeValue() + "]");
                            org.j2eebuilder.view.SetCommandBean.getCurrentInstance().execute(org.j2eebuilder.BuilderHelperBean.getCurrentInstance().findComponentDefinitionByName("InstanceAttribute", requestHelperBean), instanceAttributeDelegate, instanceAttributeVO, NonManagedBeansDefinition.TRANSIENT_STATE_PAGE, requestHelperBean);
                            org.j2eebuilder.view.UpdateCommandBean.getCurrentInstance().execute(org.j2eebuilder.BuilderHelperBean.getCurrentInstance().findComponentDefinitionByName("InstanceAttribute", requestHelperBean), instanceAttributeDelegate, instanceAttributeVO, requestHelperBean, NonManagedBeansDefinition.TRANSIENT_STATE_PAGE);
                        } catch (InstanceNotFoundException e) {
                            log.debug("ActivityDelegate: About to create instanceAttributeVO :" + instanceAttributeVO + " : name [" + instanceAttributeVO.getAttributeName() + "] Value[" + instanceAttributeVO.getAttributeValue() + "]");
                            org.j2eebuilder.view.CreateCommand.getCurrentInstance().execute(org.j2eebuilder.BuilderHelperBean.getCurrentInstance().findComponentDefinitionByName("InstanceAttribute", requestHelperBean), instanceAttributeDelegate, instanceAttributeVO, requestHelperBean, NonManagedBeansDefinition.TRANSIENT_STATE_PAGE);
                        }
                    }
                }
                log.debug("updating done. about to fire off valueObjectChange for activityBean activityID[" + activityBean.getActivityID() + "] inputTypeID[" + activityBean.getInputTypeID() + "] input[" + activityBean.getInputID() + "] name[" + activityBean.getName() + "] description[" + activityBean.getDescription() + "]");
            } else {
                throw new BusinessDelegateException("No privilege to update Activity");
            }
        } catch (Exception e) {
            log.printStackTrace(e, LogManager.ERROR);
            throw new BusinessDelegateException(e);
        }
        return valueObject;
    }

    public org.j2eebuilder.model.ManagedTransientObject delete(org.j2eebuilder.ComponentDefinition componentDefinition, org.j2eebuilder.model.ManagedTransientObject valueObject, Request requestHelperBean) throws BusinessDelegateException {
        throw new BusinessDelegateException("Not permitted. More than one entity depends on [" + this.getClass().getName() + "]. Cascade delete check is not implemented.");
    }

    /**
     *	A component can provide a customize list of criteria to be displayed in
     *	the search window. This is a default implementation.
     **/
    public Collection getSearchCriteriaType(org.j2eebuilder.ComponentDefinition componentDefinition, ManagedTransientObject valueObject, Request requestHelperBean) throws InstanceException {
        java.util.Collection searchBy = new java.util.HashSet();
        searchBy.add("findAllOpenAndAssigned");
        return searchBy;
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
        Integer organizationID = null;
        try {
            organizationID = requestHelperBean.getSessionObject().getOrganizationID();
        } catch (org.j2eebuilder.view.SessionException rpe) {
            throw new BusinessDelegateException(rpe);
        }
        Integer mechanismID = null;
        try {
            mechanismID = requestHelperBean.getSessionObject().getMechanismID();
        } catch (org.j2eebuilder.view.SessionException rpe) {
            throw new BusinessDelegateException(rpe);
        }
        java.sql.Timestamp activityDate = null;
        try {
            activityDate = ((java.sql.Timestamp[]) requestHelperBean.getArrayOfParameter("activityDate", java.sql.Timestamp.class))[0];
        } catch (org.j2eebuilder.util.RequestParameterException rpe) {
            log.debug("Search parameter activityDate is null.");
        }
        if (activityDate == null) {
            activityDate = UtilityBean.getCurrentInstance().getCurrentTimestamp();
        }
        Collection col = new java.util.HashSet();
        try {
            if ("findByInputID".equals(criteriaType)) {
                Integer inputID = null;
                try {
                    TransientObject componentBean = BuilderHelperBean.getCurrentInstance().getCurrentTransientObject("Activity", NonManagedBeansDefinition.TRANSIENT_STATE_SESSION, requestHelperBean);
                    BuilderHelperBean.getCurrentInstance().applyInterfaceDefinition(componentDefinition, requestHelperBean, (ManagedTransientObject) componentBean, NonManagedBeansDefinition.TRANSIENT_STATE_REQUEST);
                    inputID = ((com.ohioedge.j2ee.api.org.proc.ActivityBean) componentBean).getInputID();
                } catch (org.j2eebuilder.BuilderException rpe) {
                    throw new BusinessDelegateException(rpe);
                }
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query(componentDefinition.getName(), "findByInputID", new Class[] { java.lang.Integer.class }, new Object[] { inputID }, requestHelperBean, true);
            } else if ("findAllOpenAndAssigned".equals(criteriaType)) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query(componentDefinition.getName(), "findAssignedOpenByOrganizationID", new Class[] { java.lang.Integer.class }, new Object[] { organizationID }, requestHelperBean, true);
            } else if ("findAssignedOpenByOrganizationIDAndAssigneeID".equals(criteriaType)) {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query(componentDefinition.getName(), "findAssignedOpenByOrganizationIDAndAssigneeID", new Class[] { java.lang.Integer.class, java.lang.Integer.class }, new Object[] { organizationID, mechanismID }, requestHelperBean, true);
            } else {
                col = org.j2eebuilder.InstanceLocator.getCurrentInstance().query(componentDefinition.getName(), "findAssignedOpenByOrganizationIDAndAssigneeIDAndActivityDate", new Class[] { java.lang.Integer.class, java.lang.Integer.class, java.sql.Timestamp.class }, new Object[] { organizationID, mechanismID, activityDate }, requestHelperBean, true);
            }
        } catch (Exception e) {
            throw new BusinessDelegateException(e);
        }
        return col;
    }
}
