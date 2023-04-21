package com.dcivision.workflow.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.MessageResourcesFactory;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemFunctionConstant;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.Utility;
import com.dcivision.framework.bean.AbstractBaseObject;
import com.dcivision.framework.web.AbstractActionForm;
import com.dcivision.workflow.WorkflowErrorConstant;
import com.dcivision.workflow.bean.MtmWfStepNotificationRule;
import com.dcivision.workflow.bean.MtmWfStepNotificationTarget;
import com.dcivision.workflow.bean.MtmWorkflowStepPriorityRule;
import com.dcivision.workflow.bean.MtmWorkflowStepUserActor;
import com.dcivision.workflow.bean.MtmWorkflowStepWorkflowStep;
import com.dcivision.workflow.bean.WorkflowAction;
import com.dcivision.workflow.bean.WorkflowRecord;
import com.dcivision.workflow.bean.WorkflowStep;
import com.dcivision.workflow.bean.WorkflowStepFormEquat;
import com.dcivision.workflow.bean.WorkflowStepRoute;
import com.dcivision.workflow.core.AutomaticTaskFactory;
import com.dcivision.workflow.core.SystemWorkflowConstant;
import com.dcivision.workflow.core.WorkflowOperationManager;
import com.dcivision.workflow.core.WorkflowProcessor;
import com.dcivision.workflow.core.WorkflowRetrievalManager;
import com.dcivision.workflow.core.WorkflowStepManager;
import com.dcivision.workflow.dao.MtmWfStepNotificationRuleDAObject;
import com.dcivision.workflow.dao.MtmWfStepNotificationTargetDAObject;
import com.dcivision.workflow.dao.MtmWorkflowStepPriorityRuleDAObject;
import com.dcivision.workflow.dao.MtmWorkflowStepUserActorDAObject;
import com.dcivision.workflow.dao.MtmWorkflowStepWorkflowStepDAObject;
import com.dcivision.workflow.dao.WorkflowActionDAObject;
import com.dcivision.workflow.dao.WorkflowRecordDAObject;
import com.dcivision.workflow.dao.WorkflowStepDAObject;
import com.dcivision.workflow.dao.WorkflowStepFormEquatDAObject;
import com.dcivision.workflow.dao.WorkflowStepRouteDAObject;

/**
  MaintWorkflowStepAction.java

  This class is for maint workflow step.

    @author          Angus Shiu
    @company         DCIVision Limited
    @creation date   29/07/2003
    @version         $Revision: 1.13 $
*/
public class MaintAppletStepAction extends com.dcivision.framework.web.AbstractMaintAction {

    public static final String REVISION = "$Revision: 1.13 $";

    public static final String WORKFLOW_STEP_TYPES = "workflowStepTypes";

    public static final String WORKFLOW_STEP_PARENT_LIST = "workflowStepParentList";

    /** Creates a new instance of MaintWorkflowStepAction */
    public MaintAppletStepAction() {
        super();
    }

    /** getFunctionCode
   *
   * Abstract function which sub-class should implement to return the corresponding
   * function code.
   *
   * @return   The function code
   */
    public String getFunctionCode() {
        return (SystemFunctionConstant.WORKFLOW_RECORD);
    }

    /** getMajorDAOClassName
   *
   * Abstract function which sub-class should implement to return the corresponding
   * major DAO class name used in this class.
   *
   * @return   The DAO class name
   */
    public String getMajorDAOClassName() {
        return ("com.dcivision.workflow.dao.WorkflowStepDAObject");
    }

    public void clearUniqueFields(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        super.clearUniqueFields(mapping, form, request, response);
        MaintWorkflowStepForm workflowStepForm = (MaintWorkflowStepForm) form;
        workflowStepForm.setID(null);
        workflowStepForm.setStepSeq(null);
        workflowStepForm.setStepName(null);
        workflowStepForm.setActionType(null);
        workflowStepForm.setActionID(null);
        workflowStepForm.setDescription(null);
    }

    public void copyRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        super.copyRecord(mapping, form, request, response);
    }

    public void updateRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        WorkflowStep step = (WorkflowStep) form.getFormData();
        WorkflowStepManager stepManager = new WorkflowStepManager(this.getSessionContainer(request), this.getConnection(request));
        if (WorkflowStep.ACTION_TYPE_USER_DEFINED_TASK.equals(step.getActionType())) {
            stepManager.deleteStepRouteByStepID(step.getID());
            this.insertWorkflowStepRoutine(request, step.getID());
        } else if (WorkflowStep.ACTION_TYPE_FORM_DRIVEN.equals(step.getActionType())) {
            stepManager.deleteStepFormEquatByStepID(step.getID());
            this.insertWorkflowStepFormEquat(request, step.getID());
        }
        step = updateWorkflowStep(request, step);
        stepManager.deleteStepPriorityRuleByStepID(step.getID());
        this.insertWorkflowStepPriorityRule(request, form, step.getID());
        stepManager.deleteStepNotificationRuleByStepID(step.getID());
        this.insertWorkflowStepNotificationRule(request, form, step.getID());
    }

    public void deleteRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        WorkflowOperationManager operationManager = new WorkflowOperationManager(this.getSessionContainer(request), this.getConnection(request));
        WorkflowStepManager stepManager = new WorkflowStepManager(this.getSessionContainer(request), this.getConnection(request));
        WorkflowStep step = (WorkflowStep) form.getFormData();
        if (WorkflowStep.ACTION_TYPE_USER_DEFINED_TASK.equals(step.getActionType())) {
            stepManager.deleteStepRouteByStepID(step.getID());
        } else if (WorkflowStep.ACTION_TYPE_FORM_DRIVEN.equals(step.getActionType())) {
            stepManager.deleteStepFormEquatByStepID(step.getID());
        }
        operationManager.deleteSingleWorkflowStep(step.getID());
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ActionForward retValue;
        AbstractActionForm maintForm = (AbstractActionForm) form;
        MaintWorkflowStepForm stepForm = (MaintWorkflowStepForm) maintForm;
        String opMode = maintForm.getOpMode();
        String navMode = maintForm.getNavMode();
        try {
            retValue = super.execute(mapping, form, request, response);
        } catch (ApplicationException ex) {
            handleApplicationException(request, ex);
            return mapping.findForward(GlobalConstant.NAV_MODE_LIST);
        }
        return mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
    }

    public void insertRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        MtmWorkflowStepWorkflowStepDAObject stepStepDAO = (MtmWorkflowStepWorkflowStepDAObject) this.getDAObjectByClassName(request, "com.dcivision.workflow.dao.MtmWorkflowStepWorkflowStepDAObject");
        MtmWorkflowStepUserActorDAObject stepActorDAO = (MtmWorkflowStepUserActorDAObject) this.getDAObjectByClassName(request, "com.dcivision.workflow.dao.MtmWorkflowStepUserActorDAObject");
        MaintWorkflowStepForm maintForm = (MaintWorkflowStepForm) form;
        WorkflowStep step = (WorkflowStep) (maintForm.getFormData());
        step = createWorkflowStep(request, step);
        this.insertWorkflowStepPriorityRule(request, form, step.getID());
        this.insertWorkflowStepNotificationRule(request, form, step.getID());
    }

    /**
   * Move a single step or move all steps after the current step and the current step
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws ApplicationException
   */
    public void moveWorkflowStep(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        WorkflowOperationManager operationManager = new WorkflowOperationManager(this.getSessionContainer(request), this.getConnection(request));
        MtmWorkflowStepWorkflowStepDAObject stepStepDAO = (MtmWorkflowStepWorkflowStepDAObject) this.getDAObjectByClassName(request, "com.dcivision.workflow.dao.MtmWorkflowStepWorkflowStepDAObject");
        MtmWorkflowStepUserActorDAObject stepActorDAO = (MtmWorkflowStepUserActorDAObject) this.getDAObjectByClassName(request, "com.dcivision.workflow.dao.MtmWorkflowStepUserActorDAObject");
        MaintWorkflowStepForm maintForm = (MaintWorkflowStepForm) form;
        WorkflowStep step = (WorkflowStep) (maintForm.getFormData());
        String[] moveToStep = maintForm.getMoveToStepID().split("-");
        String[] connectStep = maintForm.getConnectStepID().split("-");
        if (maintForm.getOptions() != null) {
            operationManager.moveWorkflowStepsTree(step, TextUtility.parseIntegerObj(connectStep[1]), connectStep[0], TextUtility.parseIntegerObj(moveToStep[1]), moveToStep[0], TextUtility.parseIntegerObj(maintForm.getParentStepID()), maintForm.getParentType(), true, maintForm.getOperateType());
        } else {
            operationManager.moveWorkflowStepsTree(step, TextUtility.parseIntegerObj(connectStep[1]), connectStep[0], TextUtility.parseIntegerObj(moveToStep[1]), moveToStep[0], TextUtility.parseIntegerObj(maintForm.getParentStepID()), maintForm.getParentType(), false, maintForm.getOperateType());
        }
    }

    public void selectRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        WorkflowRetrievalManager retrievalManager = new WorkflowRetrievalManager(this.getSessionContainer(request), this.getConnection(request));
        WorkflowRecordDAObject recordDAO = new WorkflowRecordDAObject(this.getSessionContainer(request), this.getConnection(request));
        WorkflowRecord wfRecord = (WorkflowRecord) recordDAO.getObjectByID(TextUtility.parseIntegerObj(((MaintWorkflowStepForm) form).getWorkflowRecordID()));
        request.setAttribute("workflowRecord", wfRecord);
        Integer ID = TextUtility.parseIntegerObj(form.getID());
        Integer groupID = TextUtility.parseIntegerObj(((MaintWorkflowStepForm) form).getGroupID());
        AbstractBaseObject data = retrievalManager.getWorkflowStepInfo(ID, groupID);
        form.setFormData(data);
    }

    public void refreshFormData(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
    }

    private void retrieveWorkflowStepTypes(AbstractActionForm form, HttpServletRequest request) throws ApplicationException {
        List workflowStepTypesList = new ArrayList();
        workflowStepTypesList.add(new String[] { "workflow.label.step_type_A", WorkflowStep.ACTION_TYPE_APPROVAL });
        workflowStepTypesList.add(new String[] { "workflow.label.step_type_T", WorkflowStep.ACTION_TYPE_TASK_TO_DO });
        workflowStepTypesList.add(new String[] { "workflow.label.step_type_B", WorkflowStep.ACTION_TYPE_BOOLEAN });
        workflowStepTypesList.add(new String[] { "workflow.label.step_type_U", WorkflowStep.ACTION_TYPE_USER_DEFINED_TASK });
        try {
            MaintWorkflowStepForm maintForm = (MaintWorkflowStepForm) form;
            WorkflowRecordDAObject workflowRecordDAO = (WorkflowRecordDAObject) this.getDAObjectByClassName(request, "com.dcivision.workflow.dao.WorkflowRecordDAObject");
            WorkflowActionDAObject workflowActionDAO = (WorkflowActionDAObject) this.getDAObjectByClassName(request, "com.dcivision.workflow.dao.WorkflowActionDAObject");
            WorkflowRecord workflowRecord = (WorkflowRecord) workflowRecordDAO.getObjectByID(TextUtility.parseIntegerObj(maintForm.getWorkflowRecordID()));
            if (SystemWorkflowConstant.FMB_SUBMISSION_BY_FORM.equals(workflowRecord.getWorkflowCategoryID().toString())) {
                workflowStepTypesList.add(new String[] { "workflow.label.step_type_F", WorkflowStep.ACTION_TYPE_FORM_DRIVEN });
            }
            workflowStepTypesList.add(new String[] { "workflow.label.type_separator", "" });
            List workflowActionList = workflowActionDAO.getListByWorkflowCategoryID(workflowRecord.getWorkflowCategoryID());
            for (int i = 0; i < workflowActionList.size(); i++) {
                WorkflowAction workflowAction = (WorkflowAction) workflowActionList.get(i);
                workflowStepTypesList.add(new String[] { workflowAction.getActionLabel(), WorkflowStep.ACTION_TYPE_SYSTEM_AUTO + workflowAction.getID().toString() });
            }
        } catch (ApplicationException appEx) {
            throw appEx;
        } catch (Exception e) {
            log.error(e, e);
            throw new ApplicationException(ErrorConstant.COMMON_UNKNOWN_DAO, e);
        } finally {
            String[][] aWorkflowStepTypes = new String[workflowStepTypesList.size()][];
            for (int i = 0; i < workflowStepTypesList.size(); i++) {
                aWorkflowStepTypes[i] = (String[]) workflowStepTypesList.get(i);
            }
            request.setAttribute(WORKFLOW_STEP_TYPES, aWorkflowStepTypes);
        }
    }

    private void retrieveParentWorkflowStepArray(AbstractActionForm form, HttpServletRequest request) throws ApplicationException {
        List parentStepList = new ArrayList();
        try {
            SessionContainer sessionContainer = (SessionContainer) request.getSession().getAttribute(GlobalConstant.SESSION_CONTAINER_KEY);
            Locale locale = sessionContainer.getSessionLocale();
            WorkflowRetrievalManager retrievalManager = new WorkflowRetrievalManager(sessionContainer, this.getConnection(request));
            MtmWorkflowStepWorkflowStepDAObject stepStepDAO = (MtmWorkflowStepWorkflowStepDAObject) this.getDAObjectByClassName(request, "com.dcivision.workflow.dao.MtmWorkflowStepWorkflowStepDAObject");
            MaintWorkflowStepForm maintForm = (MaintWorkflowStepForm) form;
            WorkflowStep workflowStep = (WorkflowStep) maintForm.getFormData();
            if (!Utility.isEmpty(request.getParameter("currentWorkflowStepID"))) {
                List tmpWfStep2List = stepStepDAO.getListByParentStepID(new Integer(request.getParameter("currentWorkflowStepID")));
                for (int k = 0; k < tmpWfStep2List.size(); k++) {
                    MtmWorkflowStepWorkflowStep mtmWfStep2 = (MtmWorkflowStepWorkflowStep) tmpWfStep2List.get(k);
                    maintForm.setCurrentType(mtmWfStep2.getParentType());
                    break;
                }
            }
            List workflowStepList = retrievalManager.getWorkflowStepListByRecordIDAndExcludeStep(workflowStep.getWorkflowRecordID(), workflowStep.getID());
            for (int i = 0; i < workflowStepList.size(); i++) {
                WorkflowStep tmpStep = (WorkflowStep) workflowStepList.get(i);
                String actionType = tmpStep.getActionType();
                if (WorkflowStep.ACTION_TYPE_SUBMIT.equals(actionType)) {
                    String[] aOption = new String[2];
                    aOption[0] = tmpStep.getStepSeq() + ". " + tmpStep.getStepName() + "[" + MessageResourcesFactory.getMessage(locale, "workflow.label.step_status_S") + "]";
                    aOption[1] = WorkflowStep.PARENT_TYPE_SINGLE + "-" + tmpStep.getID();
                    parentStepList.add(aOption);
                } else if (WorkflowStep.ACTION_TYPE_APPROVAL.equals(actionType)) {
                    String[] aOption = new String[2];
                    aOption[0] = tmpStep.getStepSeq() + ". " + tmpStep.getStepName() + "[" + MessageResourcesFactory.getMessage(locale, "workflow.label.step_status_A") + "]";
                    aOption[1] = WorkflowStep.PARENT_TYPE_APPROVED + "-" + tmpStep.getID();
                    parentStepList.add(aOption);
                    aOption = new String[2];
                    aOption[0] = tmpStep.getStepSeq() + ". " + tmpStep.getStepName() + "[" + MessageResourcesFactory.getMessage(locale, "workflow.label.step_status_R") + "]";
                    aOption[1] = WorkflowStep.PARENT_TYPE_REJECTED + "-" + tmpStep.getID();
                    parentStepList.add(aOption);
                } else if (WorkflowStep.ACTION_TYPE_BOOLEAN.equals(actionType)) {
                    String[] aOption = new String[2];
                    aOption[0] = tmpStep.getStepSeq() + ". " + tmpStep.getStepName() + "[" + MessageResourcesFactory.getMessage(locale, "workflow.label.step_status_T") + "]";
                    aOption[1] = WorkflowStep.PARENT_TYPE_YES + "-" + tmpStep.getID();
                    parentStepList.add(aOption);
                    aOption = new String[2];
                    aOption[0] = tmpStep.getStepSeq() + ". " + tmpStep.getStepName() + "[" + MessageResourcesFactory.getMessage(locale, "workflow.label.step_status_F") + "]";
                    aOption[1] = WorkflowStep.PARENT_TYPE_NO + "-" + tmpStep.getID();
                    parentStepList.add(aOption);
                } else if (WorkflowStep.ACTION_TYPE_FORM_DRIVEN.equals(actionType)) {
                    String[] aOption = new String[2];
                    aOption[0] = tmpStep.getStepSeq() + ". " + tmpStep.getStepName() + "[" + MessageResourcesFactory.getMessage(locale, "workflow.label.step_status_D") + "]";
                    aOption[1] = WorkflowStep.PARENT_TYPE_FROM_VALUE_DRIVEN + "-" + tmpStep.getID();
                    parentStepList.add(aOption);
                } else if (WorkflowStep.ACTION_TYPE_USER_DEFINED_TASK.equals(actionType)) {
                    if (!Utility.isEmpty(tmpStep.getRouteListValue())) {
                        String[] routeArr = TextUtility.splitString(tmpStep.getRouteListValue(), "|");
                        for (int j = 0; j < routeArr.length; j++) {
                            String[] aOption = new String[2];
                            aOption[0] = tmpStep.getStepSeq() + ". " + tmpStep.getStepName() + "[" + routeArr[j] + "]";
                            aOption[1] = WorkflowStep.PARENT_TYPE_SINGLE + j + "-" + tmpStep.getID();
                            parentStepList.add(aOption);
                        }
                    }
                } else if (WorkflowStep.ACTION_TYPE_TASK_TO_DO.equals(actionType)) {
                    String[] aOption = new String[2];
                    aOption[0] = tmpStep.getStepSeq() + ". " + tmpStep.getStepName() + "[" + MessageResourcesFactory.getMessage(locale, "workflow.label.step_status_G") + "]";
                    aOption[1] = WorkflowStep.PARENT_TYPE_SINGLE + "-" + tmpStep.getID();
                    parentStepList.add(aOption);
                } else if (WorkflowStep.ACTION_TYPE_SYSTEM_AUTO.equals(actionType)) {
                    WorkflowAction action = retrievalManager.getWorkflowActionByActionID(tmpStep.getActionID());
                    WorkflowProcessor processor = AutomaticTaskFactory.getInstance(action);
                    if (processor.getNumOfRoutine() == 1) {
                        String[] aOption = new String[2];
                        aOption[0] = tmpStep.getStepSeq() + ". " + tmpStep.getStepName() + "[" + MessageResourcesFactory.getMessage(locale, "workflow.label.step_status_G") + "]";
                        aOption[1] = "S" + "-" + tmpStep.getID();
                        parentStepList.add(aOption);
                    } else {
                        for (int j = 0; j < processor.getNumOfRoutine(); j++) {
                            String[] aOption = new String[2];
                            aOption[0] = tmpStep.getStepSeq() + ". " + tmpStep.getStepName() + "[" + MessageResourcesFactory.getMessage(locale, processor.getLabelString(j)) + "]";
                            aOption[1] = "S" + j + "-" + tmpStep.getID();
                            parentStepList.add(aOption);
                        }
                    }
                } else if (WorkflowStep.ACTION_TYPE_END.equals(actionType)) {
                } else {
                    throw new ApplicationException(WorkflowErrorConstant.UNKNOWN_WORKFLOW_ACTION_TYPE);
                }
            }
        } catch (ApplicationException appEx) {
            throw appEx;
        } catch (Exception e) {
            log.error(e, e);
            throw new ApplicationException(ErrorConstant.COMMON_UNKNOWN_DAO, e);
        } finally {
            String[][] aParentStep = new String[parentStepList.size()][];
            for (int i = 0; i < parentStepList.size(); i++) {
                aParentStep[i] = (String[]) parentStepList.get(i);
            }
            request.setAttribute(WORKFLOW_STEP_PARENT_LIST, aParentStep);
        }
    }

    /**
   * Insert route of this step into Workflow_Step_Routine by stepID
   * @param request
   * @param stepID
   * @throws ApplicationException
   */
    private void insertWorkflowStepRoutine(HttpServletRequest request, Integer stepID) throws ApplicationException {
        WorkflowStepRouteDAObject wfStepRouteDAO = (WorkflowStepRouteDAObject) this.getDAObjectByClassName(request, "com.dcivision.workflow.dao.WorkflowStepRouteDAObject");
        String routeListValue = request.getParameter("routeListValue");
        if (!Utility.isEmpty(routeListValue) && !"null".equals(routeListValue)) {
            String[] routineArr = TextUtility.splitString(routeListValue, "|");
            for (int i = 0; i < routineArr.length; i++) {
                WorkflowStepRoute tmpRoutine = new WorkflowStepRoute();
                tmpRoutine.setWorkflowStepID(stepID);
                tmpRoutine.setRouteSeq(new Integer(i));
                tmpRoutine.setRouteName(routineArr[i]);
                wfStepRouteDAO.insertObject(tmpRoutine);
            }
        }
    }

    /**
   * Insert Wrokflow Step Form Equation
   * @param request
   * @param stepID
   * @throws ApplicationException
   */
    private void insertWorkflowStepFormEquat(HttpServletRequest request, Integer stepID) throws ApplicationException {
        WorkflowStepFormEquatDAObject formEquatDAO = (WorkflowStepFormEquatDAObject) this.getDAObjectByClassName(request, "com.dcivision.workflow.dao.WorkflowStepFormEquatDAObject");
        String operand1 = request.getParameter("operand1");
        String compare = request.getParameter("compare");
        String operand2 = request.getParameter("operand2");
        String formEquation = request.getParameter("formEquation");
        String operationType = request.getParameter("operationTypeForFormEquat");
        String dateFormat = request.getParameter("dateFormat");
        if (!Utility.isEmpty(compare)) {
            WorkflowStepFormEquat formEquat = new WorkflowStepFormEquat();
            formEquat.setWorkflowStepID(stepID);
            formEquat.setOperand1(operand1);
            formEquat.setCompare(compare);
            formEquat.setOperand2(operand2);
            formEquat.setFormEquation(formEquation);
            formEquat.setOperationType(operationType);
            formEquat.setDateFormat(dateFormat);
            formEquatDAO.insertObject(formEquat);
        }
    }

    /**
   * retrieve Move Step Info
   * @param form
   * @param request
   * @param currentStepID
   * @param parentStepID
   * @throws ApplicationException
   */
    private void retrieveMoveStepInfo(AbstractActionForm form, HttpServletRequest request, Integer currentStepID, Integer parentStepID) throws ApplicationException {
        WorkflowStepDAObject wfStepDAO = new WorkflowStepDAObject(this.getSessionContainer(request), this.getConnection(request));
        MaintWorkflowStepForm maintForm = (MaintWorkflowStepForm) form;
        WorkflowStep currentStep = (WorkflowStep) wfStepDAO.getObjectByID(currentStepID);
        WorkflowStep parentStep = (WorkflowStep) wfStepDAO.getObjectByID(parentStepID);
        maintForm.setStepName(currentStep.getStepName());
        maintForm.setStepID(currentStepID.toString());
        maintForm.setParentStepName(parentStep.getStepName());
        maintForm.setParentStepID(parentStepID.toString());
    }

    public WorkflowStep createWorkflowStep(HttpServletRequest request, WorkflowStep step) throws ApplicationException {
        WorkflowStepDAObject stepDAO = new WorkflowStepDAObject(this.getSessionContainer(request), this.getConnection(request));
        MtmWorkflowStepUserActorDAObject stepActorDAO = new MtmWorkflowStepUserActorDAObject(this.getSessionContainer(request), this.getConnection(request));
        MtmWorkflowStepWorkflowStepDAObject stepStepDAO = new MtmWorkflowStepWorkflowStepDAObject(this.getSessionContainer(request), this.getConnection(request));
        WorkflowActionDAObject actionDAO = new WorkflowActionDAObject(this.getSessionContainer(request), this.getConnection(request));
        String routeListValue = step.getRouteListValue();
        String[] routineArr = null;
        if (!Utility.isEmpty(routeListValue) && !"null".equals(routeListValue)) {
            routineArr = TextUtility.splitString(routeListValue, "|");
        }
        String actionType = step.getActionType();
        if (actionType != null && actionType.length() > 1) {
            String sActionID = actionType.substring(1);
            step.setActionType(actionType.substring(0, 1));
            step.setActionID(TextUtility.parseIntegerObj(sActionID));
        }
        StringBuffer sNotifyStr = new StringBuffer();
        StringBuffer sOverdueStr = new StringBuffer();
        if (step.isNotifyOwnerTaskArrivalByMail()) {
            sNotifyStr.append(step.getNotifyOwnerTaskArrivalByMail());
        }
        if (step.isNotifyOwnerTaskArrivalBySystem()) {
            sNotifyStr.append(step.getNotifyOwnerTaskArrivalBySystem());
        }
        if (step.isNotifyAllTaskCompletedByMail()) {
            sNotifyStr.append(step.getNotifyAllTaskCompletedByMail());
        }
        if (step.isNotifyAllTaskCompletedBySystem()) {
            sNotifyStr.append(step.getNotifyAllTaskCompletedBySystem());
        }
        step.setOnCompleteAckMethod(sNotifyStr.toString());
        if (step.isNotifyOwnerTaskOverdueByMail()) {
            sOverdueStr.append(step.getNotifyOwnerTaskOverdueByMail());
        }
        if (step.isNotifyOwnerTaskOverdueBySystem()) {
            sOverdueStr.append(step.getNotifyOwnerTaskOverdueBySystem());
        }
        if (step.isNotifyAllTaskOverdueByMail()) {
            sOverdueStr.append(step.getNotifyAllTaskOverdueByMail());
        }
        if (step.isNotifyAllTaskOverdueBySystem()) {
            sOverdueStr.append(step.getNotifyAllTaskOverdueBySystem());
        }
        step.setOnNoResponseAckMethod(sOverdueStr.toString());
        if (Utility.isEmpty(step.getUserGroups()) && Utility.isEmpty(step.getUserRoles())) {
            step.setFilterBy(null);
        }
        step = (WorkflowStep) stepDAO.insertObject(step);
        log.debug("after insert, step : " + step);
        String[] userRecordsAry = step.getUserRecords();
        if (userRecordsAry != null) {
            log.debug("insert user record actor records!!!");
            for (int i = 0; i < userRecordsAry.length; i++) {
                MtmWorkflowStepUserActor mtmWorkflowStepUserActor = new MtmWorkflowStepUserActor();
                mtmWorkflowStepUserActor.setWorkflowStepID(step.getID());
                mtmWorkflowStepUserActor.setActorType(MtmWorkflowStepUserActor.ACTOR_TYPE_USER);
                mtmWorkflowStepUserActor.setActorID(TextUtility.parseIntegerObj(userRecordsAry[i]));
                mtmWorkflowStepUserActor.setActionType(MtmWorkflowStepUserActor.ACTION_TYPE_EXECUTION);
                mtmWorkflowStepUserActor.setRecordStatus(GlobalConstant.RECORD_STATUS_ACTIVE);
                stepActorDAO.insertObject(mtmWorkflowStepUserActor);
            }
        }
        String[] userGroupsAry = step.getUserGroups();
        if (userGroupsAry != null) {
            log.debug("insert user group actor records!!!");
            for (int i = 0; i < userGroupsAry.length; i++) {
                MtmWorkflowStepUserActor mtmWorkflowStepUserActor = new MtmWorkflowStepUserActor();
                mtmWorkflowStepUserActor.setWorkflowStepID(step.getID());
                mtmWorkflowStepUserActor.setActorType(MtmWorkflowStepUserActor.ACTOR_TYPE_GROUP);
                mtmWorkflowStepUserActor.setActorID(TextUtility.parseIntegerObj(userGroupsAry[i]));
                mtmWorkflowStepUserActor.setActionType(MtmWorkflowStepUserActor.ACTION_TYPE_EXECUTION);
                if (MtmWorkflowStepUserActor.ACTOR_TYPE_RUNTIME_ASSIGN.equals(step.getActorType())) {
                    mtmWorkflowStepUserActor.setRecordStatus(GlobalConstant.RECORD_STATUS_ACTIVE);
                }
                stepActorDAO.insertObject(mtmWorkflowStepUserActor);
            }
        }
        String[] userRolesAry = step.getUserRoles();
        if (userRolesAry != null) {
            log.debug("insert user role actor records!!!");
            for (int i = 0; i < userRolesAry.length; i++) {
                MtmWorkflowStepUserActor mtmWorkflowStepUserActor = new MtmWorkflowStepUserActor();
                mtmWorkflowStepUserActor.setWorkflowStepID(step.getID());
                mtmWorkflowStepUserActor.setActorType(MtmWorkflowStepUserActor.ACTOR_TYPE_ROLE);
                mtmWorkflowStepUserActor.setActorID(TextUtility.parseIntegerObj(userRolesAry[i]));
                mtmWorkflowStepUserActor.setActionType(MtmWorkflowStepUserActor.ACTION_TYPE_EXECUTION);
                if (MtmWorkflowStepUserActor.ACTOR_TYPE_RUNTIME_ASSIGN.equals(step.getActorType())) {
                    mtmWorkflowStepUserActor.setRecordStatus(GlobalConstant.RECORD_STATUS_ACTIVE);
                }
                stepActorDAO.insertObject(mtmWorkflowStepUserActor);
            }
        }
        String[] actorTypes = step.getActorTypes();
        if (!Utility.isEmpty(actorTypes)) {
            for (int i = 0; i < actorTypes.length; i++) {
                String actorType = actorTypes[i];
                MtmWorkflowStepUserActor mtmWorkflowStepUserActor = new MtmWorkflowStepUserActor();
                mtmWorkflowStepUserActor.setWorkflowStepID(step.getID());
                mtmWorkflowStepUserActor.setActorType(actorType.substring(0, 1));
                if (MtmWorkflowStepUserActor.ACTOR_TYPE_ACTION_TAKER.equals(actorType.substring(0, 1)) || MtmWorkflowStepUserActor.ACTOR_TYPE_REPORT_TO_OF_ACTION_TAKER.equals(actorType.substring(0, 1))) {
                    mtmWorkflowStepUserActor.setActorID(Integer.valueOf(actorType.substring(1)));
                } else {
                    mtmWorkflowStepUserActor.setActorID(new Integer(0));
                }
                mtmWorkflowStepUserActor.setActionType(MtmWorkflowStepUserActor.ACTION_TYPE_EXECUTION);
                stepActorDAO.insertObject(mtmWorkflowStepUserActor);
            }
        }
        String assignActorType = step.getAssignActorType();
        if (!Utility.isEmpty(assignActorType)) {
            MtmWorkflowStepUserActor mtmWorkflowStepUserActor = new MtmWorkflowStepUserActor();
            mtmWorkflowStepUserActor.setWorkflowStepID(step.getID());
            mtmWorkflowStepUserActor.setActorType(assignActorType);
            mtmWorkflowStepUserActor.setActorID(new Integer(0));
            mtmWorkflowStepUserActor.setActionType(MtmWorkflowStepUserActor.ACTION_TYPE_EXECUTION);
            stepActorDAO.insertObject(mtmWorkflowStepUserActor);
        }
        return step;
    }

    public WorkflowStep updateWorkflowStep(HttpServletRequest request, WorkflowStep step) throws ApplicationException {
        log.info("-=-=-=-=-=-=-Enter update workflow step-=-=-=-=-=-=-");
        WorkflowStepDAObject stepDAO = new WorkflowStepDAObject(this.getSessionContainer(request), this.getConnection(request));
        MtmWorkflowStepUserActorDAObject stepActorDAO = new MtmWorkflowStepUserActorDAObject(this.getSessionContainer(request), this.getConnection(request));
        MtmWorkflowStepWorkflowStepDAObject stepStepDAO = new MtmWorkflowStepWorkflowStepDAObject(this.getSessionContainer(request), this.getConnection(request));
        WorkflowActionDAObject actionDAO = new WorkflowActionDAObject(this.getSessionContainer(request), this.getConnection(request));
        WorkflowStep oldStep = (WorkflowStep) stepDAO.getObjectByID(step.getID());
        String actionType = step.getActionType();
        if (actionType != null && actionType.length() > 1) {
            String sActionID = actionType.substring(1);
            step.setActionType(actionType.substring(0, 1));
            step.setActionID(TextUtility.parseIntegerObj(sActionID));
        }
        StringBuffer sNotifyStr = new StringBuffer();
        StringBuffer sOverdueStr = new StringBuffer();
        if (step.isNotifyOwnerTaskArrivalByMail()) {
            sNotifyStr.append(step.getNotifyOwnerTaskArrivalByMail());
        }
        if (step.isNotifyOwnerTaskArrivalBySystem()) {
            sNotifyStr.append(step.getNotifyOwnerTaskArrivalBySystem());
        }
        if (step.isNotifyAllTaskCompletedByMail()) {
            sNotifyStr.append(step.getNotifyAllTaskCompletedByMail());
        }
        if (step.isNotifyAllTaskCompletedBySystem()) {
            sNotifyStr.append(step.getNotifyAllTaskCompletedBySystem());
        }
        step.setOnCompleteAckMethod(sNotifyStr.toString());
        if (step.isNotifyOwnerTaskOverdueByMail()) {
            sOverdueStr.append(step.getNotifyOwnerTaskOverdueByMail());
        }
        if (step.isNotifyOwnerTaskOverdueBySystem()) {
            sOverdueStr.append(step.getNotifyOwnerTaskOverdueBySystem());
        }
        if (step.isNotifyAllTaskOverdueByMail()) {
            sOverdueStr.append(step.getNotifyAllTaskOverdueByMail());
        }
        if (step.isNotifyAllTaskOverdueBySystem()) {
            sOverdueStr.append(step.getNotifyAllTaskOverdueBySystem());
        }
        step.setOnNoResponseAckMethod(sOverdueStr.toString());
        if (Utility.isEmpty(step.getUserGroups()) && Utility.isEmpty(step.getUserRoles())) {
            step.setFilterBy(null);
        }
        step = (WorkflowStep) stepDAO.updateObject(step);
        stepActorDAO.deleteListByWorkflowStepID(step.getID());
        String[] userRecordsAry = step.getUserRecords();
        if (userRecordsAry != null) {
            log.debug("insert user record actor records!!!");
            for (int i = 0; i < userRecordsAry.length; i++) {
                MtmWorkflowStepUserActor mtmWorkflowStepUserActor = new MtmWorkflowStepUserActor();
                mtmWorkflowStepUserActor.setWorkflowStepID(step.getID());
                mtmWorkflowStepUserActor.setActorType(MtmWorkflowStepUserActor.ACTOR_TYPE_USER);
                mtmWorkflowStepUserActor.setActorID(TextUtility.parseIntegerObj(userRecordsAry[i]));
                mtmWorkflowStepUserActor.setActionType(MtmWorkflowStepUserActor.ACTION_TYPE_EXECUTION);
                stepActorDAO.insertObject(mtmWorkflowStepUserActor);
            }
        }
        String[] userGroupsAry = step.getUserGroups();
        if (userGroupsAry != null) {
            log.debug("insert user group actor records!!!");
            for (int i = 0; i < userGroupsAry.length; i++) {
                MtmWorkflowStepUserActor mtmWorkflowStepUserActor = new MtmWorkflowStepUserActor();
                mtmWorkflowStepUserActor.setWorkflowStepID(step.getID());
                mtmWorkflowStepUserActor.setActorType(MtmWorkflowStepUserActor.ACTOR_TYPE_GROUP);
                mtmWorkflowStepUserActor.setActorID(TextUtility.parseIntegerObj(userGroupsAry[i]));
                mtmWorkflowStepUserActor.setActionType(MtmWorkflowStepUserActor.ACTION_TYPE_EXECUTION);
                stepActorDAO.insertObject(mtmWorkflowStepUserActor);
            }
        }
        String[] userRolesAry = step.getUserRoles();
        if (userRolesAry != null) {
            log.debug("insert user role actor records!!!");
            for (int i = 0; i < userRolesAry.length; i++) {
                MtmWorkflowStepUserActor mtmWorkflowStepUserActor = new MtmWorkflowStepUserActor();
                mtmWorkflowStepUserActor.setWorkflowStepID(step.getID());
                mtmWorkflowStepUserActor.setActorType(MtmWorkflowStepUserActor.ACTOR_TYPE_ROLE);
                mtmWorkflowStepUserActor.setActorID(TextUtility.parseIntegerObj(userRolesAry[i]));
                mtmWorkflowStepUserActor.setActionType(MtmWorkflowStepUserActor.ACTION_TYPE_EXECUTION);
                stepActorDAO.insertObject(mtmWorkflowStepUserActor);
            }
        }
        String[] actorTypes = step.getActorTypes();
        if (!Utility.isEmpty(actorTypes)) {
            for (int i = 0; i < actorTypes.length; i++) {
                String actorType = actorTypes[i];
                MtmWorkflowStepUserActor mtmWorkflowStepUserActor = new MtmWorkflowStepUserActor();
                mtmWorkflowStepUserActor.setWorkflowStepID(step.getID());
                mtmWorkflowStepUserActor.setActorType(actorType.substring(0, 1));
                if (MtmWorkflowStepUserActor.ACTOR_TYPE_ACTION_TAKER.equals(actorType.substring(0, 1)) || MtmWorkflowStepUserActor.ACTOR_TYPE_REPORT_TO_OF_ACTION_TAKER.equals(actorType.substring(0, 1))) {
                    mtmWorkflowStepUserActor.setActorID(new Integer(actorType.substring(1)));
                } else {
                    mtmWorkflowStepUserActor.setActorID(new Integer(0));
                }
                mtmWorkflowStepUserActor.setActionType(MtmWorkflowStepUserActor.ACTION_TYPE_EXECUTION);
                stepActorDAO.insertObject(mtmWorkflowStepUserActor);
            }
        }
        String assignActorType = step.getAssignActorType();
        if (!Utility.isEmpty(assignActorType)) {
            MtmWorkflowStepUserActor mtmWorkflowStepUserActor = new MtmWorkflowStepUserActor();
            mtmWorkflowStepUserActor.setWorkflowStepID(step.getID());
            mtmWorkflowStepUserActor.setActorType(assignActorType);
            mtmWorkflowStepUserActor.setActorID(new Integer(0));
            mtmWorkflowStepUserActor.setActionType(MtmWorkflowStepUserActor.ACTION_TYPE_EXECUTION);
            stepActorDAO.insertObject(mtmWorkflowStepUserActor);
        }
        return step;
    }

    /**
   * setup the priority rule for this step priority.
   * insert record into Mtm_WF_Step_Priority_Rule table.
   * @param request
   * @param form
   * @param stepID
   * @throws ApplicationException
   */
    private void insertWorkflowStepPriorityRule(HttpServletRequest request, AbstractActionForm form, Integer stepID) throws ApplicationException {
        MtmWorkflowStepPriorityRuleDAObject ruleDAO = new MtmWorkflowStepPriorityRuleDAObject(this.getSessionContainer(request), this.getConnection(request));
        MaintWorkflowStepForm maintForm = (MaintWorkflowStepForm) form;
        String ruleListValue = maintForm.getPriorityRuleListValue();
        if (!Utility.isEmpty(ruleListValue)) {
            String[] ruleArr = TextUtility.splitString(ruleListValue, "|");
            for (int i = 0; i < ruleArr.length; i++) {
                String[] tmpRuleArr = TextUtility.splitString(ruleArr[i], ",");
                String ackMethod = "";
                MtmWorkflowStepPriorityRule priorityRule = new MtmWorkflowStepPriorityRule();
                priorityRule.setWorkflowStepID(stepID);
                priorityRule.setEscalateAtAmt(TextUtility.parseIntegerObj(tmpRuleArr[0]));
                priorityRule.setEscalateAtField(TextUtility.parseIntegerObj(tmpRuleArr[1]));
                priorityRule.setEscalateTrigger(tmpRuleArr[2]);
                priorityRule.setEscalateLevel(TextUtility.parseIntegerObj(tmpRuleArr[3]));
                if (!"null".equals(tmpRuleArr[4])) {
                    ackMethod = tmpRuleArr[4];
                }
                if (!"null".equals(tmpRuleArr[5])) {
                    if (Utility.isEmpty(ackMethod)) {
                        ackMethod = tmpRuleArr[5];
                    } else {
                        ackMethod += "," + tmpRuleArr[5];
                    }
                }
                priorityRule.setEscalateAckMethod(ackMethod);
                ruleDAO.insertObject(priorityRule);
            }
        }
    }

    /**
   * Add By Dick
   * TODO inssert notification rules for workflow step notification
   * @param request
   * @param form
   * @param stepID
   * @throws ApplicationException
   */
    private void insertWorkflowStepNotificationRule(HttpServletRequest request, AbstractActionForm form, Integer stepID) throws ApplicationException {
        MtmWfStepNotificationRuleDAObject notificationRuleDAO = new MtmWfStepNotificationRuleDAObject(this.getSessionContainer(request), this.getConnection(request));
        MtmWfStepNotificationTargetDAObject notificationTargetDAO = new MtmWfStepNotificationTargetDAObject(this.getSessionContainer(request), this.getConnection(request));
        MaintWorkflowStepForm maintForm = (MaintWorkflowStepForm) form;
        String NotifyRuleListValue = maintForm.getNotificationRuleListValue();
        if (!Utility.isEmpty(NotifyRuleListValue)) {
            String[] ruleArr = TextUtility.splitString(NotifyRuleListValue, "|");
            for (int i = 0; i < ruleArr.length; i++) {
                String[] tmpRuleArr = TextUtility.splitString(ruleArr[i], ",");
                String ackMethod = "";
                MtmWfStepNotificationRule notificationRule = new MtmWfStepNotificationRule();
                notificationRule.setWorkflowStepID(stepID);
                notificationRule.setNotifyTrigger(tmpRuleArr[0]);
                notificationRule.setNotifyAtAmt(TextUtility.parseIntegerObj(tmpRuleArr[1]));
                notificationRule.setNotifyAtField(TextUtility.parseIntegerObj(tmpRuleArr[2]));
                if (!"null".equals(tmpRuleArr[3])) {
                    ackMethod = tmpRuleArr[3];
                }
                if (!"null".equals(tmpRuleArr[4])) {
                    if (Utility.isEmpty(ackMethod)) {
                        ackMethod = tmpRuleArr[4];
                    } else {
                        ackMethod += "," + tmpRuleArr[4];
                    }
                }
                notificationRule.setNotifyAckMethod(ackMethod);
                notificationRule = (MtmWfStepNotificationRule) notificationRuleDAO.insertObject(notificationRule);
                String notificationOwnerStr = tmpRuleArr[5];
                if (!Utility.isEmpty(notificationOwnerStr)) {
                    String[] notificationOwnerElements = TextUtility.splitString(notificationOwnerStr, ":");
                    for (int j = 0; j < notificationOwnerElements.length; j++) {
                        String[] notificationOwnerElement = TextUtility.splitString(notificationOwnerElements[j], "$");
                        MtmWfStepNotificationTarget notificationTarget = new MtmWfStepNotificationTarget();
                        notificationTarget.setNotifyRuleID(notificationRule.getID());
                        notificationTarget.setTargetType(notificationOwnerElement[2]);
                        if ("-1".equals(notificationOwnerElement[0])) {
                            notificationTarget.setTargetID(stepID);
                        } else {
                            notificationTarget.setTargetID(new Integer(notificationOwnerElement[0]));
                        }
                        notificationTargetDAO.insertObject(notificationTarget);
                    }
                }
            }
        }
    }
}
