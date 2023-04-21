package com.dcivision.dms.web;

import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.bean.AbstractBaseObject;
import com.dcivision.workflow.bean.WorkflowStep;

public class MaintDmsPopComplexForm extends com.dcivision.setup.web.AbstractActionPermissionForm {

    public static final String REVISION = "$Revision: 1.1 $";

    private String workflowRecordID = null;

    private String stepSeq = null;

    private String stepName = null;

    private String stepID = null;

    private String actionType = null;

    private String actionID = null;

    private String description = null;

    private String workflowRule = "1";

    private String priority = GlobalConstant.PRIORITY_MEDIUM;

    private String allowAssignDueDate = null;

    private String allowAssignPriority = null;

    private String allowStop = GlobalConstant.FALSE;

    private String nextStepAllowAssignDueDate = null;

    private String onCompleteAckMethod = null;

    private String onNoResponseFor = null;

    private String onNoResponseCalendarID = null;

    private String onNoResponseAfter = null;

    private String onNoResponseAction = null;

    private String onNoResponseNextStepID = null;

    private String onNoResponseAckMethod = null;

    private String filterBy = null;

    private String followBranch = null;

    private String[] parentStepIDs = null;

    private String[] userRecords = null;

    private String[] userGroups = null;

    private String[] userRoles = null;

    private String[] actorTypes = null;

    private String assignActorType = null;

    private String actorType = null;

    private String actorId = null;

    private String loopBackStepID = null;

    private String parentStepID = null;

    private String parentStepName = null;

    private String parentType = null;

    private String currentStepID = null;

    private String currentType = null;

    private String workflowGroupID = null;

    private String groupID = null;

    private String moveToStepID = null;

    private String overDueType = WorkflowStep.OVER_DUE_TYPE_NA;

    private String insertStepType = WorkflowStep.INSERT_NEW_STEP;

    private String isMerge = "N";

    private String notifyOwnerTaskArrivalByMail = null;

    private String notifyAllTaskCompletedByMail = null;

    private String notifyOwnerTaskOverdueByMail = null;

    private String notifyAllTaskOverdueByMail = null;

    private String notifyOwnerTaskArrivalBySystem = null;

    private String notifyAllTaskCompletedBySystem = null;

    private String notifyOwnerTaskOverdueBySystem = null;

    private String notifyAllTaskOverdueBySystem = null;

    private String categoryID = null;

    private String[] sectionID = null;

    private String[] sectionRID = null;

    private String routeListValue = null;

    private String priorityRuleListValue = null;

    private String notificationRuleListValue = null;

    private String parameterListValue = null;

    private String options = null;

    private String formRecordID = null;

    private String operand1 = null;

    private String operand2 = null;

    private String compare = null;

    private String formEquation = null;

    private String operationTypeForFormEquat = null;

    private String dateFormat = null;

    private String formEquationListValue = null;

    private String reserveStepID = null;

    private String bindFormItemValue = null;

    private String otherNoPermissionData = null;

    private boolean isReleased = false;

    private boolean isHaveColloCantSteps = false;

    private String loopBackALLOffset = GlobalConstant.FALSE;

    private String formName = null;

    private String criterionNum = null;

    private String protectedStep = null;

    private String protectedNavModeEdit = null;

    private String protectedNavModeView = null;

    private String protectedOpModeEdit = null;

    private String protectedOpModeView = null;

    private String authenticatePassword = null;

    public MaintDmsPopComplexForm() {
        super();
    }

    public String getWorkflowRecordID() {
        return (this.workflowRecordID);
    }

    public void setWorkflowRecordID(String workflowRecordID) {
        this.workflowRecordID = workflowRecordID;
    }

    public String getStepSeq() {
        return (this.stepSeq);
    }

    public void setStepSeq(String stepSeq) {
        this.stepSeq = stepSeq;
    }

    public String getStepName() {
        return (this.stepName);
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getActionType() {
        return (this.actionType);
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionID() {
        return (this.actionID);
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }

    public String getDescription() {
        return (this.description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkflowRule() {
        return (this.workflowRule);
    }

    public void setWorkflowRule(String workflowRule) {
        this.workflowRule = workflowRule;
    }

    public String getPriority() {
        return (this.priority);
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getOnCompleteAckMethod() {
        return (this.onCompleteAckMethod);
    }

    public void setOnCompleteAckMethod(String onCompleteAckMethod) {
        this.onCompleteAckMethod = onCompleteAckMethod;
    }

    public String getOnNoResponseFor() {
        return (this.onNoResponseFor);
    }

    public void setOnNoResponseFor(String onNoResponseFor) {
        this.onNoResponseFor = onNoResponseFor;
    }

    public String getOnNoResponseAfter() {
        return (this.onNoResponseAfter);
    }

    public void setOnNoResponseAfter(String onNoResponseAfter) {
        this.onNoResponseAfter = onNoResponseAfter;
    }

    public String getOnNoResponseAction() {
        return (this.onNoResponseAction);
    }

    public void setOnNoResponseAction(String onNoResponseAction) {
        this.onNoResponseAction = onNoResponseAction;
    }

    public String getOnNoResponseNextStepID() {
        return (this.onNoResponseNextStepID);
    }

    public void setOnNoResponseNextStepID(String onNoResponseNextStepID) {
        this.onNoResponseNextStepID = onNoResponseNextStepID;
    }

    public String getOnNoResponseAckMethod() {
        return (this.onNoResponseAckMethod);
    }

    public void setOnNoResponseAckMethod(String onNoResponseAckMethod) {
        this.onNoResponseAckMethod = onNoResponseAckMethod;
    }

    public String getFilterBy() {
        return this.filterBy;
    }

    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    public String getAllowStop() {
        return (this.allowStop);
    }

    public void setAllowStop(String allowStop) {
        if (allowStop != null) {
            this.allowStop = allowStop;
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryID() {
        return (this.categoryID);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        this.setID(null);
        this.setWorkflowRecordID(null);
        this.setStepSeq(null);
        this.setStepName(null);
        this.setActionType(null);
        this.setActionID(null);
        this.setDescription(null);
        this.setWorkflowRule("1");
        this.setCategoryID(null);
        this.setPriority(GlobalConstant.PRIORITY_MEDIUM);
        this.setAllowAssignDueDate(null);
        this.setAllowAssignPriority(null);
        this.setAllowStop(GlobalConstant.FALSE);
        this.setNextStepAllowAssignDueDate(null);
        this.setOnCompleteAckMethod(null);
        this.setOnNoResponseFor(null);
        this.setOnNoResponseCalendarID(null);
        this.setOnNoResponseAfter(null);
        this.setOnNoResponseAction(null);
        this.setOnNoResponseNextStepID(null);
        this.setOnNoResponseAckMethod(null);
        this.setFilterBy(null);
        this.setFollowBranch(null);
        this.setNotifyOwnerTaskArrivalByMail(null);
        this.setNotifyAllTaskCompletedByMail(null);
        this.setNotifyOwnerTaskOverdueByMail(null);
        this.setNotifyAllTaskOverdueByMail(null);
        this.setNotifyOwnerTaskArrivalBySystem(null);
        this.setNotifyAllTaskCompletedBySystem(null);
        this.setNotifyOwnerTaskOverdueBySystem(null);
        this.setNotifyAllTaskOverdueBySystem(null);
        this.setRouteListValue(null);
        this.setPriorityRuleListValue(null);
        this.setFormRecordID(null);
        this.setOperand1(null);
        this.setOperand2(null);
        this.setCompare(null);
        this.setFormEquation(null);
        this.setOperationTypeForFormEquat(null);
        this.setDateFormat(null);
        this.setFormEquationListValue(null);
        this.setParentStepIDs(null);
        this.setUserRecords(null);
        this.setUserGroups(null);
        this.setUserRoles(null);
        this.setActorTypes(null);
        this.setSectionID(null);
        this.setSectionRID(null);
        this.setActorType(null);
        this.setParentStepID(null);
        this.setLoopBackStepID(null);
        this.setParentType(null);
        this.setOverDueType(WorkflowStep.OVER_DUE_TYPE_NA);
        this.setAssignActorType(null);
        this.setReleased(false);
        this.setBindFormItemValue(null);
        this.setRecordStatus(null);
        this.setUpdateCount(null);
        this.setCreatorID(null);
        this.setCreateDate(null);
        this.setUpdaterID(null);
        this.setUpdateDate(null);
        this.setParameterListValue(null);
    }

    public AbstractBaseObject getFormData() throws ApplicationException {
        WorkflowStep tmpWorkflowStep = new WorkflowStep();
        tmpWorkflowStep.setID(TextUtility.parseIntegerObj(this.getID()));
        tmpWorkflowStep.setWorkflowRecordID(TextUtility.parseIntegerObj(this.getWorkflowRecordID()));
        tmpWorkflowStep.setStepSeq(TextUtility.parseIntegerObj(this.getStepSeq()));
        tmpWorkflowStep.setStepName(this.getStepName());
        tmpWorkflowStep.setActionType(this.getActionType());
        tmpWorkflowStep.setActionID(TextUtility.parseIntegerObj(this.getActionID()));
        tmpWorkflowStep.setDescription(this.getDescription());
        tmpWorkflowStep.setWorkflowRule(this.getWorkflowRule());
        tmpWorkflowStep.setCategoryID(this.getCategoryID());
        tmpWorkflowStep.setPriority(this.getPriority());
        tmpWorkflowStep.setAllowAssignDueDate(this.getAllowAssignDueDate());
        tmpWorkflowStep.setAllowAssignPriority(this.getAllowAssignPriority());
        tmpWorkflowStep.setAllowStop(this.getAllowStop());
        tmpWorkflowStep.setNextStepAllowAssignDueDate(this.getNextStepAllowAssignDueDate());
        tmpWorkflowStep.setOnCompleteAckMethod(this.getOnCompleteAckMethod());
        tmpWorkflowStep.setOnNoResponseFor(TextUtility.parseIntegerObj(this.getOnNoResponseFor()));
        tmpWorkflowStep.setOnNoResponseCalendarID(this.getOnNoResponseCalendarID());
        tmpWorkflowStep.setOnNoResponseAfter(parseTimestamp(this.getOnNoResponseAfter()));
        tmpWorkflowStep.setOnNoResponseAction(this.getOnNoResponseAction());
        tmpWorkflowStep.setOnNoResponseNextStepID(TextUtility.parseIntegerObj(this.getOnNoResponseNextStepID()));
        tmpWorkflowStep.setOnNoResponseAckMethod(this.getOnNoResponseAckMethod());
        tmpWorkflowStep.setFilterBy(TextUtility.parseIntegerObj(this.filterBy));
        tmpWorkflowStep.setFollowBranch(this.getFollowBranch());
        tmpWorkflowStep.setNotifyOwnerTaskArrivalByMail(this.getNotifyOwnerTaskArrivalByMail());
        tmpWorkflowStep.setNotifyAllTaskCompletedByMail(this.getNotifyAllTaskCompletedByMail());
        tmpWorkflowStep.setNotifyOwnerTaskOverdueByMail(this.getNotifyOwnerTaskOverdueByMail());
        tmpWorkflowStep.setNotifyAllTaskOverdueBySystem(this.getNotifyAllTaskOverdueBySystem());
        tmpWorkflowStep.setNotifyAllTaskOverdueByMail(this.getNotifyAllTaskOverdueByMail());
        tmpWorkflowStep.setNotifyOwnerTaskArrivalBySystem(this.getNotifyOwnerTaskArrivalBySystem());
        tmpWorkflowStep.setNotifyAllTaskCompletedBySystem(this.getNotifyAllTaskCompletedBySystem());
        tmpWorkflowStep.setNotifyOwnerTaskOverdueBySystem(this.getNotifyOwnerTaskOverdueBySystem());
        tmpWorkflowStep.setNotificationRuleListValue(this.getNotificationRuleListValue());
        tmpWorkflowStep.setReserveStepID(TextUtility.parseIntegerObj(this.getReserveStepID()));
        tmpWorkflowStep.setParentStepIDs(this.getParentStepIDs());
        tmpWorkflowStep.setUserRecords(this.getUserRecords());
        tmpWorkflowStep.setUserGroups(this.getUserGroups());
        tmpWorkflowStep.setUserRoles(this.getUserRoles());
        tmpWorkflowStep.setActorTypes(this.getActorTypes());
        tmpWorkflowStep.setAssignActorType(this.getAssignActorType());
        tmpWorkflowStep.setActorType(this.getActorType());
        tmpWorkflowStep.setActorId(this.getActorId());
        tmpWorkflowStep.setParentStepID(TextUtility.parseIntegerObj(this.getParentStepID()));
        tmpWorkflowStep.setLoopBackStepID(TextUtility.parseIntegerObj(this.getLoopBackStepID()));
        tmpWorkflowStep.setParentType(this.getParentType());
        tmpWorkflowStep.setCurrentStepID(TextUtility.parseIntegerObj(this.getCurrentStepID()));
        tmpWorkflowStep.setOverDueType(this.getOverDueType());
        tmpWorkflowStep.setRouteListValue(this.getRouteListValue());
        tmpWorkflowStep.setPriorityRuleListValue(this.getPriorityRuleListValue());
        tmpWorkflowStep.setFormRecordID(this.getFormRecordID());
        tmpWorkflowStep.setOperand1(this.getOperand1());
        tmpWorkflowStep.setOperand2(this.getOperand2());
        tmpWorkflowStep.setCompare(this.getCompare());
        tmpWorkflowStep.setFormEquation(this.getFormEquation());
        tmpWorkflowStep.setOperationTypeForFormEquat(this.getOperationTypeForFormEquat());
        tmpWorkflowStep.setDateFormat(this.getDateFormat());
        tmpWorkflowStep.setFormEquationListValue(this.getFormEquationListValue());
        tmpWorkflowStep.setRecordStatus(this.getRecordStatus());
        tmpWorkflowStep.setUpdateCount(TextUtility.parseIntegerObj(this.getUpdateCount()));
        tmpWorkflowStep.setCreatorID(TextUtility.parseIntegerObj(this.getCreatorID()));
        tmpWorkflowStep.setCreateDate(parseTimestamp(this.getCreateDate()));
        tmpWorkflowStep.setUpdaterID(TextUtility.parseIntegerObj(this.getUpdaterID()));
        tmpWorkflowStep.setUpdateDate(parseTimestamp(this.getUpdateDate()));
        tmpWorkflowStep.setOtherNoPermissionData(this.getOtherNoPermissionData());
        tmpWorkflowStep.setAllPermissionData(this.getAllPermissionData());
        tmpWorkflowStep.setUserNoPermissionData(this.getUserNoPermissionData());
        tmpWorkflowStep.setGroupNoPermissionData(this.getGroupNoPermissionData());
        tmpWorkflowStep.setRoleNoPermissionData(this.getRoleNoPermissionData());
        tmpWorkflowStep.setLoopBackALLOffset(this.getLoopBackALLOffset());
        tmpWorkflowStep.setBindFormItemValue(this.getBindFormItemValue());
        StringBuffer sb = new StringBuffer();
        StringBuffer sbR = new StringBuffer();
        String sectionIDSum = "";
        if (sectionID != null) {
            for (int i = 0; i < sectionID.length; i++) {
                sb.append(sectionID[i]).append(",");
            }
        } else {
            sb.append("0");
        }
        if (sectionRID != null) {
            for (int i = 0; i < sectionRID.length; i++) {
                sbR.append(sectionRID[i]).append(",");
            }
        } else {
            sbR.append("0");
        }
        sectionIDSum = sb.toString() + "|" + sbR.toString();
        tmpWorkflowStep.setSectionID(sectionIDSum);
        tmpWorkflowStep.setReleased(this.isReleased);
        return tmpWorkflowStep;
    }

    public void setFormData(AbstractBaseObject baseObj) throws ApplicationException {
        WorkflowStep tmpWorkflowStep = (WorkflowStep) baseObj;
        this.setID(TextUtility.formatIntegerObj(tmpWorkflowStep.getID()));
        this.setWorkflowRecordID(TextUtility.formatIntegerObj(tmpWorkflowStep.getWorkflowRecordID()));
        this.setStepSeq(TextUtility.formatIntegerObj(tmpWorkflowStep.getStepSeq()));
        this.setStepName(tmpWorkflowStep.getStepName());
        this.setActionType(tmpWorkflowStep.getActionType());
        this.setActionID(TextUtility.formatIntegerObj(tmpWorkflowStep.getActionID()));
        this.setDescription(tmpWorkflowStep.getDescription());
        this.setWorkflowRule(tmpWorkflowStep.getWorkflowRule());
        this.setCategoryID(tmpWorkflowStep.getCategroyID());
        this.setPriority(tmpWorkflowStep.getPriority());
        this.setAllowAssignDueDate(tmpWorkflowStep.getAllowAssignDueDate());
        this.setAllowAssignPriority(tmpWorkflowStep.getAllowAssignPriority());
        this.setAllowStop(tmpWorkflowStep.getAllowStop());
        this.setNextStepAllowAssignDueDate(tmpWorkflowStep.getNextStepAllowAssignDueDate());
        this.setOnCompleteAckMethod(tmpWorkflowStep.getOnCompleteAckMethod());
        this.setOnNoResponseFor(TextUtility.formatIntegerObj(tmpWorkflowStep.getOnNoResponseFor()));
        this.setOnNoResponseCalendarID(tmpWorkflowStep.getOnNoResponseCalendarID());
        this.setOnNoResponseAfter(formatTimestamp(tmpWorkflowStep.getOnNoResponseAfter()));
        this.setOnNoResponseAction(tmpWorkflowStep.getOnNoResponseAction());
        this.setOnNoResponseNextStepID(TextUtility.formatIntegerObj(tmpWorkflowStep.getOnNoResponseNextStepID()));
        this.setOnNoResponseAckMethod(tmpWorkflowStep.getOnNoResponseAckMethod());
        this.setFilterBy(TextUtility.formatIntegerObj(tmpWorkflowStep.getFilterBy()));
        this.setFollowBranch(tmpWorkflowStep.getFollowBranch());
        this.setNotifyOwnerTaskArrivalByMail(tmpWorkflowStep.getNotifyOwnerTaskArrivalByMail());
        this.setNotifyAllTaskCompletedByMail(tmpWorkflowStep.getNotifyAllTaskCompletedByMail());
        this.setNotifyOwnerTaskOverdueByMail(tmpWorkflowStep.getNotifyOwnerTaskOverdueByMail());
        this.setNotifyAllTaskOverdueByMail(tmpWorkflowStep.getNotifyAllTaskOverdueByMail());
        this.setNotifyOwnerTaskArrivalBySystem(tmpWorkflowStep.getNotifyOwnerTaskArrivalBySystem());
        this.setNotifyAllTaskCompletedBySystem(tmpWorkflowStep.getNotifyAllTaskCompletedBySystem());
        this.setNotifyOwnerTaskOverdueBySystem(tmpWorkflowStep.getNotifyOwnerTaskOverdueBySystem());
        this.setNotifyAllTaskOverdueBySystem(tmpWorkflowStep.getNotifyAllTaskOverdueBySystem());
        this.setReserveStepID(TextUtility.formatIntegerObj(tmpWorkflowStep.getReserveStepID()));
        this.setParentStepIDs(tmpWorkflowStep.getParentStepIDs());
        this.setUserRecords(tmpWorkflowStep.getUserRecords());
        this.setUserGroups(tmpWorkflowStep.getUserGroups());
        this.setUserRoles(tmpWorkflowStep.getUserRoles());
        this.setActorTypes(tmpWorkflowStep.getActorTypes());
        this.setAssignActorType(tmpWorkflowStep.getAssignActorType());
        this.setActorType(tmpWorkflowStep.getActorType());
        this.setActorId(tmpWorkflowStep.getActorId());
        this.setOverDueType(tmpWorkflowStep.getOverDueType());
        this.setRouteListValue(tmpWorkflowStep.getRouteListValue());
        this.setPriorityRuleListValue(tmpWorkflowStep.getPriorityRuleListValue());
        this.setCurrentType(tmpWorkflowStep.getCurrentType());
        this.setNotificationRuleListValue(tmpWorkflowStep.getNotificationRuleListValue());
        this.setFormRecordID(tmpWorkflowStep.getFormRecordID());
        this.setOperand1(tmpWorkflowStep.getOperand1());
        this.setOperand2(tmpWorkflowStep.getOperand2());
        this.setCompare(tmpWorkflowStep.getCompare());
        this.setFormEquation(tmpWorkflowStep.getFormEquation());
        this.setOperationTypeForFormEquat(tmpWorkflowStep.getOperationTypeForFormEquat());
        this.setDateFormat(tmpWorkflowStep.getDateFormat());
        this.setFormEquationListValue(tmpWorkflowStep.getFormEquationListValue());
        this.setReleased(tmpWorkflowStep.isReleased());
        this.setRecordStatus(tmpWorkflowStep.getRecordStatus());
        this.setUpdateCount(TextUtility.formatIntegerObj(tmpWorkflowStep.getUpdateCount()));
        this.setCreatorID(TextUtility.formatIntegerObj(tmpWorkflowStep.getCreatorID()));
        this.setCreateDate(formatTimestamp(tmpWorkflowStep.getCreateDate()));
        this.setUpdaterID(TextUtility.formatIntegerObj(tmpWorkflowStep.getUpdaterID()));
        this.setUpdateDate(formatTimestamp(tmpWorkflowStep.getUpdateDate()));
        this.setOtherNoPermissionData(tmpWorkflowStep.getOtherNoPermissionData());
        this.setGroupNoPermissionData(tmpWorkflowStep.getGroupNoPermissionData());
        this.setRoleNoPermissionData(tmpWorkflowStep.getRoleNoPermissionData());
        this.setUserNoPermissionData(tmpWorkflowStep.getUserNoPermissionData());
        this.setAllPermissionData(tmpWorkflowStep.getAllowAssignDueDate());
        this.setLoopBackALLOffset(tmpWorkflowStep.getLoopBackALLOffset());
        this.setBindFormItemValue(tmpWorkflowStep.getBindFormItemValue());
        if (tmpWorkflowStep.getSectionID() != null) {
            String sectionID, sectionRID;
            StringTokenizer sTokenSum = new StringTokenizer(tmpWorkflowStep.getSectionID(), "|");
            if (sTokenSum.hasMoreElements()) {
                sectionID = (String) sTokenSum.nextElement();
                StringTokenizer sToken = new StringTokenizer(sectionID, ",");
                String[] setSectionIDArray = new String[sToken.countTokens()];
                for (int i = 0; sToken.hasMoreElements(); i++) {
                    setSectionIDArray[i] = (String) sToken.nextElement();
                }
                this.setSectionID(setSectionIDArray);
            }
            if (sTokenSum.hasMoreElements()) {
                sectionRID = (String) sTokenSum.nextElement();
                StringTokenizer sToken = new StringTokenizer(sectionRID, ",");
                String[] setSectionIDArray = new String[sToken.countTokens()];
                for (int i = 0; sToken.hasMoreElements(); i++) {
                    setSectionIDArray[i] = (String) sToken.nextElement();
                }
                this.setSectionRID(setSectionIDArray);
            }
        } else {
            this.setSectionID(null);
            this.setSectionRID(null);
        }
    }

    public String getOverDueType() {
        return (this.overDueType);
    }

    public void setOverDueType(String overDueType) {
        this.overDueType = overDueType;
    }

    public String[] getParentStepIDs() {
        return (this.parentStepIDs);
    }

    public void setParentStepIDs(String[] parentStepIDs) {
        this.parentStepIDs = parentStepIDs;
    }

    public String getParentType() {
        return (this.parentType);
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getCurrentType() {
        return (this.currentType);
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    public String[] getUserRecords() {
        return (this.userRecords);
    }

    public void setUserRecords(String[] userRecords) {
        this.userRecords = userRecords;
    }

    public String[] getUserGroups() {
        return (this.userGroups);
    }

    public void setUserGroups(String[] userGroups) {
        this.userGroups = userGroups;
    }

    public String[] getUserRoles() {
        return (this.userRoles);
    }

    public void setUserRoles(String[] userRoles) {
        this.userRoles = userRoles;
    }

    public String[] getSectionID() {
        return (this.sectionID);
    }

    public void setSectionID(String[] sectionID) {
        this.sectionID = sectionID;
    }

    public String[] getSectionRID() {
        return (this.sectionRID);
    }

    public void setSectionRID(String[] sectionRID) {
        this.sectionRID = sectionRID;
    }

    public String getActorType() {
        return (this.actorType);
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public String getLoopBackStepID() {
        return (this.loopBackStepID);
    }

    public void setLoopBackStepID(String loopBackStepID) {
        this.loopBackStepID = loopBackStepID;
    }

    public String getInsertStepType() {
        return (this.insertStepType);
    }

    public void setInsertStepType(String insertStepType) {
        this.insertStepType = insertStepType;
    }

    public String getParentStepID() {
        return (this.parentStepID);
    }

    public void setParentStepID(String parentStepID) {
        this.parentStepID = parentStepID;
    }

    public String getCurrentStepID() {
        return (this.currentStepID);
    }

    public void setCurrentStepID(String currentStepID) {
        this.currentStepID = currentStepID;
    }

    public String getIsMerge() {
        return (this.isMerge);
    }

    public void setIsMerge(String isMerge) {
        this.isMerge = isMerge;
    }

    public String getNotifyOwnerTaskArrivalByMail() {
        return (this.notifyOwnerTaskArrivalByMail);
    }

    public void setNotifyOwnerTaskArrivalByMail(String notifyOwnerTaskArrivalByMail) {
        this.notifyOwnerTaskArrivalByMail = notifyOwnerTaskArrivalByMail;
    }

    public String getNotifyOwnerTaskArrivalBySystem() {
        return (this.notifyOwnerTaskArrivalBySystem);
    }

    public void setNotifyOwnerTaskArrivalBySystem(String notifyOwnerTaskArrivalBySystem) {
        this.notifyOwnerTaskArrivalBySystem = notifyOwnerTaskArrivalBySystem;
    }

    public String getNotifyAllTaskCompletedByMail() {
        return (this.notifyAllTaskCompletedByMail);
    }

    public void setNotifyAllTaskCompletedByMail(String notifyAllTaskCompletedByMail) {
        this.notifyAllTaskCompletedByMail = notifyAllTaskCompletedByMail;
    }

    public String getNotifyAllTaskCompletedBySystem() {
        return (this.notifyAllTaskCompletedBySystem);
    }

    public void setNotifyAllTaskCompletedBySystem(String notifyAllTaskCompletedBySystem) {
        this.notifyAllTaskCompletedBySystem = notifyAllTaskCompletedBySystem;
    }

    public String getNotifyOwnerTaskOverdueByMail() {
        return (this.notifyOwnerTaskOverdueByMail);
    }

    public void setNotifyOwnerTaskOverdueByMail(String notifyOwnerTaskOverdueByMail) {
        this.notifyOwnerTaskOverdueByMail = notifyOwnerTaskOverdueByMail;
    }

    public String getNotifyOwnerTaskOverdueBySystem() {
        return (this.notifyOwnerTaskOverdueBySystem);
    }

    public void setNotifyOwnerTaskOverdueBySystem(String notifyOwnerTaskOverdueBySystem) {
        this.notifyOwnerTaskOverdueBySystem = notifyOwnerTaskOverdueBySystem;
    }

    public String getNotifyAllTaskOverdueByMail() {
        return (this.notifyAllTaskOverdueByMail);
    }

    public void setNotifyAllTaskOverdueByMail(String notifyAllTaskOverdueByMail) {
        this.notifyAllTaskOverdueByMail = notifyAllTaskOverdueByMail;
    }

    public String getNotifyAllTaskOverdueBySystem() {
        return (this.notifyAllTaskOverdueBySystem);
    }

    public void setNotifyAllTaskOverdueBySystem(String notifyAllTaskOverdueBySystem) {
        this.notifyAllTaskOverdueBySystem = notifyAllTaskOverdueBySystem;
    }

    public String getWorkflowGroupID() {
        return (this.workflowGroupID);
    }

    public void setWorkflowGroupID(String workflowGroupID) {
        this.workflowGroupID = workflowGroupID;
    }

    public String getGroupID() {
        return (this.groupID);
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getRouteListValue() {
        return this.routeListValue;
    }

    public void setRouteListValue(String routeListValue) {
        this.routeListValue = routeListValue;
    }

    public String getParentStepName() {
        return this.parentStepName;
    }

    public void setParentStepName(String parentStepName) {
        this.parentStepName = parentStepName;
    }

    public String getStepID() {
        return this.stepID;
    }

    public void setStepID(String stepID) {
        this.stepID = stepID;
    }

    public String getOptions() {
        return this.options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getMoveToStepID() {
        return this.moveToStepID;
    }

    public void setMoveToStepID(String moveToStepID) {
        this.moveToStepID = moveToStepID;
    }

    public String getFormRecordID() {
        return this.formRecordID;
    }

    public void setFormRecordID(String formRecordID) {
        this.formRecordID = formRecordID;
    }

    public String getOperand1() {
        return this.operand1;
    }

    public void setOperand1(String operand1) {
        this.operand1 = operand1;
    }

    public String getOperand2() {
        return this.operand2;
    }

    public void setOperand2(String operand2) {
        this.operand2 = operand2;
    }

    public String getCompare() {
        return this.compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public String getFormEquation() {
        return this.formEquation;
    }

    public void setFormEquation(String formEquation) {
        this.formEquation = formEquation;
    }

    public String getOperationTypeForFormEquat() {
        return this.operationTypeForFormEquat;
    }

    public void setOperationTypeForFormEquat(String operationType) {
        this.operationTypeForFormEquat = operationType;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getAllowAssignDueDate() {
        return this.allowAssignDueDate;
    }

    public void setAllowAssignDueDate(String allowAssignDueDate) {
        this.allowAssignDueDate = allowAssignDueDate;
    }

    public String getNextStepAllowAssignDueDate() {
        return this.nextStepAllowAssignDueDate;
    }

    public void setNextStepAllowAssignDueDate(String nextStepAllowAssignDueDate) {
        this.nextStepAllowAssignDueDate = nextStepAllowAssignDueDate;
    }

    public String getFollowBranch() {
        return this.followBranch;
    }

    public void setFollowBranch(String followBranch) {
        this.followBranch = followBranch;
    }

    public String getAllowAssignPriority() {
        return this.allowAssignPriority;
    }

    public void setAllowAssignPriority(String allowAssignPriority) {
        this.allowAssignPriority = allowAssignPriority;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getPriorityRuleListValue() {
        return this.priorityRuleListValue;
    }

    public void setPriorityRuleListValue(String priorityRuleListValue) {
        this.priorityRuleListValue = priorityRuleListValue;
    }

    public String getFormEquationListValue() {
        return this.formEquationListValue;
    }

    public void setFormEquationListValue(String formEquationListValue) {
        this.formEquationListValue = formEquationListValue;
    }

    /**
	   * @return Returns the notificationRuleListValue.
	   */
    public String getNotificationRuleListValue() {
        return notificationRuleListValue;
    }

    /**
	   * @param notificationRuleListValue The notificationRuleListValue to set.
	   */
    public void setNotificationRuleListValue(String notificationRuleListValue) {
        this.notificationRuleListValue = notificationRuleListValue;
    }

    public String[] getActorTypes() {
        return actorTypes;
    }

    public void setActorTypes(String[] actorTypes) {
        this.actorTypes = actorTypes;
    }

    public String getAssignActorType() {
        return assignActorType;
    }

    public void setAssignActorType(String assignActorType) {
        this.assignActorType = assignActorType;
    }

    public String getReserveStepID() {
        return reserveStepID;
    }

    public void setReserveStepID(String reserveStepID) {
        this.reserveStepID = reserveStepID;
    }

    public boolean isReleased() {
        return isReleased;
    }

    public void setReleased(boolean isReleased) {
        this.isReleased = isReleased;
    }

    public String getOtherNoPermissionData() {
        return otherNoPermissionData;
    }

    public void setOtherNoPermissionData(String otherNoPermissionData) {
        this.otherNoPermissionData = otherNoPermissionData;
    }

    public boolean isHaveColloCantSteps() {
        return isHaveColloCantSteps;
    }

    public void setHaveColloCantSteps(boolean isHaveColloCantSteps) {
        this.isHaveColloCantSteps = isHaveColloCantSteps;
    }

    public String getLoopBackALLOffset() {
        return loopBackALLOffset;
    }

    public void setLoopBackALLOffset(String loopBackALLOffset) {
        this.loopBackALLOffset = loopBackALLOffset;
    }

    public String getBindFormItemValue() {
        return bindFormItemValue;
    }

    public void setBindFormItemValue(String bindFormItemValue) {
        this.bindFormItemValue = bindFormItemValue;
    }

    public String getAuthenticatePassword() {
        return authenticatePassword;
    }

    public void setAuthenticatePassword(String authenticatePassword) {
        this.authenticatePassword = authenticatePassword;
    }

    public String getProtectedNavModeEdit() {
        return protectedNavModeEdit;
    }

    public void setProtectedNavModeEdit(String protectedNavModeEdit) {
        this.protectedNavModeEdit = protectedNavModeEdit;
    }

    public String getProtectedNavModeView() {
        return protectedNavModeView;
    }

    public void setProtectedNavModeView(String protectedNavModeView) {
        this.protectedNavModeView = protectedNavModeView;
    }

    public String getProtectedOpModeEdit() {
        return protectedOpModeEdit;
    }

    public void setProtectedOpModeEdit(String protectedOpModeEdit) {
        this.protectedOpModeEdit = protectedOpModeEdit;
    }

    public String getProtectedOpModeView() {
        return protectedOpModeView;
    }

    public void setProtectedOpModeView(String protectedOpModeView) {
        this.protectedOpModeView = protectedOpModeView;
    }

    public String getProtectedStep() {
        return protectedStep;
    }

    public void setProtectedStep(String protectedStep) {
        this.protectedStep = protectedStep;
    }

    public String getOnNoResponseCalendarID() {
        return onNoResponseCalendarID;
    }

    public void setOnNoResponseCalendarID(String onNoResponseCalendarID) {
        this.onNoResponseCalendarID = onNoResponseCalendarID;
    }

    public String getParameterListValue() {
        return (this.parameterListValue);
    }

    public void setParameterListValue(String parameterListValue) {
        this.parameterListValue = parameterListValue;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getCriterionNum() {
        return criterionNum;
    }

    public void setCriterionNum(String criterionNum) {
        this.criterionNum = criterionNum;
    }
}
