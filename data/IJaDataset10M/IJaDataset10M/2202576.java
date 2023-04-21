package com.dcivision.framework.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.Utility;
import com.dcivision.framework.bean.AbstractBaseObject;
import com.dcivision.framework.dao.AbstractDAObject;
import com.dcivision.workflow.bean.WorkflowProgress;
import com.dcivision.workflow.bean.WorkflowStep;
import com.dcivision.workflow.core.WorkflowProgressManager;
import com.dcivision.workflow.web.MaintWorkflowRecordForm;

/**
  AbstractMaintAction.java

  This class is to provide action class function for normal insert, update,
  delete and forward to list page functions.

    @author          Rollo Chan
    @company         DCIVision Limited
    @creation date   10/07/2003
    @version         $Revision: 1.43.2.1 $
*/
public abstract class AbstractMaintAction extends AbstractAction {

    protected static final String MESSAGE_RECORD_INSERT = "common.message.record_inserted";

    protected static final String MESSAGE_RECORD_UPDATE = "common.message.record_updated";

    protected static final String MESSAGE_RECORD_DELETE = "common.message.record_deleted";

    protected static final String MESSAGE_RECORD_COPY = "common.message.record_copied";

    protected static final String MESSAGE_AUTO_ROUTED_TO_DEFAULT = "workflow.message.auto_routed_to_default";

    protected static final String MESSAGE_FORM_EQUATION_HAS_ERROR = "workflow.message.form_equation_has_error";

    protected static final String SPECIAL_MESSAGE = "SPECIAL_ACTION_MESSAGE";

    public AbstractMaintAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        AbstractActionForm maintForm = (AbstractActionForm) form;
        String opMode = (maintForm != null) ? maintForm.getOpMode() : request.getParameter("opMode");
        String navMode = (maintForm != null) ? maintForm.getNavMode() : request.getParameter("navMode");
        log.debug(((maintForm != null) ? maintForm.getClass().getName() : "") + ", OP Mode:" + opMode + ", Nav Mode:" + navMode);
        ActionForward forward = this.retrieveFunctionCode(request, response, mapping);
        if (forward != null) {
            return forward;
        }
        try {
            if (!Utility.isEmpty(opMode)) {
                if (GlobalConstant.OP_MODE_INSERT.equals(opMode)) {
                    try {
                        this.insertRecord(mapping, maintForm, request, response);
                        this.handleWorkflowRoutine(mapping, maintForm, request, response, opMode, navMode);
                        this.commit(request);
                        addActionTemplateMessage(request, MESSAGE_RECORD_INSERT);
                    } catch (ApplicationException appEx) {
                        this.rollback(request);
                        handleApplicationException(request, appEx);
                    }
                    if (isError(request)) {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_NEW);
                        return mapping.findForward(GlobalConstant.NAV_MODE_NEW);
                    } else {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_VIEW);
                        return mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
                    }
                } else if (GlobalConstant.OP_MODE_INSERT_NEW.equals(opMode)) {
                    try {
                        this.insertRecord(mapping, maintForm, request, response);
                        this.handleWorkflowRoutine(mapping, maintForm, request, response, opMode, navMode);
                        this.commit(request);
                        form.reset(mapping, request);
                        this.init(mapping, maintForm, request, response);
                        addActionTemplateMessage(request, MESSAGE_RECORD_INSERT);
                    } catch (ApplicationException appEx) {
                        this.rollback(request);
                        handleApplicationException(request, appEx);
                    }
                    if (isError(request)) {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_NEW);
                        return mapping.findForward(GlobalConstant.NAV_MODE_NEW);
                    } else {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_NEW);
                        return mapping.findForward(GlobalConstant.NAV_MODE_NEW);
                    }
                } else if (GlobalConstant.OP_MODE_UPDATE.equals(opMode)) {
                    try {
                        this.updateRecord(mapping, maintForm, request, response);
                        this.handleWorkflowRoutine(mapping, maintForm, request, response, opMode, navMode);
                        this.commit(request);
                        addActionTemplateMessage(request, MESSAGE_RECORD_UPDATE);
                    } catch (ApplicationException appEx) {
                        this.rollback(request);
                        handleApplicationException(request, appEx);
                    }
                    if (isError(request)) {
                        this.selectRecord(mapping, maintForm, request, response);
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_EDIT);
                        return mapping.findForward(GlobalConstant.NAV_MODE_EDIT);
                    } else {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_VIEW);
                        return mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
                    }
                } else if (GlobalConstant.OP_MODE_UPDATE_NEW.equals(opMode)) {
                    try {
                        this.updateRecord(mapping, maintForm, request, response);
                        this.handleWorkflowRoutine(mapping, maintForm, request, response, opMode, navMode);
                        this.commit(request);
                        form.reset(mapping, request);
                        this.init(mapping, maintForm, request, response);
                        addActionTemplateMessage(request, MESSAGE_RECORD_UPDATE);
                    } catch (ApplicationException appEx) {
                        this.rollback(request);
                        handleApplicationException(request, appEx);
                    }
                    if (isError(request)) {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_EDIT);
                        return mapping.findForward(GlobalConstant.NAV_MODE_EDIT);
                    } else {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_NEW);
                        return mapping.findForward(GlobalConstant.NAV_MODE_NEW);
                    }
                } else if (GlobalConstant.OP_MODE_DELETE.equals(opMode)) {
                    try {
                        this.deleteRecord(mapping, maintForm, request, response);
                        this.handleWorkflowRoutine(mapping, maintForm, request, response, opMode, navMode);
                        this.commit(request);
                        addActionTemplateMessage(request, MESSAGE_RECORD_DELETE);
                    } catch (ApplicationException appEx) {
                        this.rollback(request);
                        handleApplicationException(request, appEx);
                    }
                    if (isError(request)) {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_EDIT);
                        return mapping.findForward(GlobalConstant.NAV_MODE_EDIT);
                    } else {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_VIEW);
                        return mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
                    }
                } else if (GlobalConstant.OP_MODE_COPY.equals(opMode)) {
                    try {
                        this.copyRecord(mapping, maintForm, request, response);
                        this.handleWorkflowRoutine(mapping, maintForm, request, response, opMode, navMode);
                        this.commit(request);
                        addActionTemplateMessage(request, MESSAGE_RECORD_COPY);
                    } catch (ApplicationException appEx) {
                        this.rollback(request);
                        handleApplicationException(request, appEx);
                    }
                    if (isError(request)) {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_CHANGE);
                        return mapping.findForward(GlobalConstant.NAV_MODE_NEW);
                    } else {
                        maintForm.setNavMode(GlobalConstant.NAV_MODE_VIEW);
                        return mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
                    }
                } else {
                    throw new ApplicationException(ErrorConstant.COMMON_UNKNOWN_OPERATION);
                }
            } else {
                if (Utility.isEmpty(navMode) || GlobalConstant.NAV_MODE_NEW.equals(navMode)) {
                    this.init(mapping, maintForm, request, response);
                    maintForm.setNavMode(GlobalConstant.NAV_MODE_NEW);
                    return mapping.findForward(GlobalConstant.NAV_MODE_NEW);
                } else if (GlobalConstant.NAV_MODE_EDIT.equals(navMode)) {
                    try {
                        this.selectRecord(mapping, maintForm, request, response);
                    } catch (ApplicationException appEx) {
                        handleApplicationException(request, appEx);
                    }
                    maintForm.setNavMode(GlobalConstant.NAV_MODE_EDIT);
                    return mapping.findForward(GlobalConstant.NAV_MODE_EDIT);
                } else if (GlobalConstant.NAV_MODE_VIEW.equals(navMode)) {
                    try {
                        this.selectRecord(mapping, maintForm, request, response);
                    } catch (ApplicationException appEx) {
                        handleApplicationException(request, appEx);
                    }
                    maintForm.setNavMode(GlobalConstant.NAV_MODE_VIEW);
                    return mapping.findForward(GlobalConstant.NAV_MODE_VIEW);
                } else if (GlobalConstant.NAV_MODE_CHANGE.equals(navMode)) {
                    try {
                        this.selectRecord(mapping, maintForm, request, response);
                    } catch (ApplicationException appEx) {
                        handleApplicationException(request, appEx);
                    }
                    maintForm.setNavMode(GlobalConstant.NAV_MODE_CHANGE);
                    maintForm.setID(null);
                    maintForm.setCreateDate(null);
                    maintForm.setCreatorID(null);
                    maintForm.setUpdateDate(null);
                    maintForm.setUpdaterID(null);
                    maintForm.setUpdateCount(null);
                    maintForm.setRecordStatus(null);
                    clearUniqueFields(mapping, maintForm, request, response);
                    return mapping.findForward(GlobalConstant.NAV_MODE_NEW);
                } else if (GlobalConstant.NAV_MODE_LIST.equals(navMode)) {
                    maintForm.setNavMode(GlobalConstant.NAV_MODE_LIST);
                    return mapping.findForward(GlobalConstant.NAV_MODE_LIST);
                } else {
                    throw new ApplicationException(ErrorConstant.COMMON_UNKNOWN_NAVIGATION);
                }
            }
        } catch (ApplicationException ex) {
            handleApplicationException(request, ex);
            return mapping.findForward(GlobalConstant.NAV_MODE_LIST);
        }
    }

    public void clearUniqueFields(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
    }

    public void init(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
    }

    public void copyRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        this.insertRecord(mapping, form, request, response);
    }

    public void selectRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        AbstractDAObject dao = this.getMajorDAObject(request);
        AbstractBaseObject bean = dao.getObjectByID(TextUtility.parseIntegerObj(form.getID()));
        form.setFormData(bean);
    }

    public void insertRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        AbstractDAObject dao = this.getMajorDAObject(request);
        AbstractBaseObject bean = dao.insertObject(form.getFormData());
        form.setFormData(bean);
    }

    public void updateRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        AbstractDAObject dao = this.getMajorDAObject(request);
        AbstractBaseObject bean = dao.updateObject(form.getFormData());
        form.setFormData(bean);
    }

    public void deleteRecord(ActionMapping mapping, AbstractActionForm form, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        AbstractDAObject dao = this.getMajorDAObject(request);
        AbstractBaseObject bean = dao.deleteObject(form.getFormData());
        form.setFormData(bean);
    }

    public void handleWorkflowRoutine(ActionMapping mapping, AbstractActionForm maintForm, HttpServletRequest request, HttpServletResponse response, String opMode, String navMode) throws ApplicationException {
        if (maintForm instanceof WorkflowActionFormInterface && this instanceof WorkflowActionInterface && GlobalConstant.TRUE.equals(((WorkflowActionFormInterface) maintForm).getSubmitSystemWorkflow())) {
            WorkflowActionFormInterface wfaForm = (WorkflowActionFormInterface) maintForm;
            WorkflowProgressManager wfProgressManager = new WorkflowProgressManager(this.getSessionContainer(request), this.getConnection(request));
            MaintWorkflowRecordForm recordForm = new MaintWorkflowRecordForm();
            if (WorkflowStep.DYNAMIC_ASSIGN_DUE_DATE_MANDATORY.equals(wfaForm.getNextStepAllowAssignDueDate()) || WorkflowStep.DYNAMIC_ASSIGN_DUE_DATE_TRUE.equals(wfaForm.getNextStepAllowAssignDueDate())) {
                if (!Utility.isEmpty(wfaForm.getNextStepDueDates()) && Utility.timestampToCalendar(TextUtility.parseDBDateTimeToTimeStamp(wfaForm.getNextStepDueDates())).before(Utility.timestampToCalendar(Utility.getCurrentTimestamp()))) {
                    throw new ApplicationException(ErrorConstant.WORKFLOW_DUE_DATE_CANNOT_BE_LESS_THAN_CURRENT_DATE);
                }
            }
            if (wfaForm.getWorkflowRecordID().indexOf("|") > 0) {
                recordForm.setID(wfaForm.getWorkflowRecordID().substring(0, wfaForm.getWorkflowRecordID().indexOf("|")));
            } else {
                recordForm.setID(wfaForm.getWorkflowRecordID());
            }
            if (maintForm instanceof com.dcivision.form.web.MaintFormSubmissionForm) {
                com.dcivision.form.web.MaintFormSubmissionForm formSubmissionForm = (com.dcivision.form.web.MaintFormSubmissionForm) maintForm;
                recordForm.setObjParamMap(formSubmissionForm.getObjParamMap());
            }
            recordForm.setComment(wfaForm.getWorkflowComment());
            recordForm.setWorkflowObject(maintForm.getFormData());
            recordForm.setFileNames(wfaForm.getFileNames());
            recordForm.setDmsFileNames(wfaForm.getDmsFileNames());
            recordForm.setNextStepDueDates(wfaForm.getNextStepDueDates());
            recordForm.setNextStepAllowAssignDueDate(wfaForm.getNextStepAllowAssignDueDate());
            recordForm.setTrackID(wfaForm.getTrackID());
            WorkflowProgress progress = (WorkflowProgress) wfProgressManager.startWorkflowTrack(recordForm);
            com.dcivision.customize.workflow.CustomizationForAction customization = new com.dcivision.customize.workflow.CustomizationForAction();
            customization.workflowSubmissionCustomization(this.getSessionContainer(request), this.getConnection(request), request, recordForm, progress);
            if (WorkflowProgress.FORM_EQUATION_ERROR.equals(wfProgressManager.getAutoRoutedToDefault())) {
                this.addActionTemplateMessage(request, MESSAGE_FORM_EQUATION_HAS_ERROR);
            } else if (WorkflowProgress.ROUTED_TO_DEFAULT.equals(wfProgressManager.getAutoRoutedToDefault())) {
                this.addActionTemplateMessage(request, MESSAGE_AUTO_ROUTED_TO_DEFAULT);
            }
            request.setAttribute("assignStepMap", wfProgressManager.getAssignStepMap());
            if (progress != null) {
                request.setAttribute("WF_PROGRESS", progress);
                ((WorkflowActionInterface) this).executeWorkflowRoutine(mapping, wfaForm, request, response, opMode, navMode);
            }
        }
    }

    /**
   * handle some where need special message rather than "Insert..Update..in template"
   * if need special message,please set the key into request("SPECIAL_ACTION_MESSAGE,key");
   * @param request
   * @param key
   */
    protected void addActionTemplateMessage(HttpServletRequest request, String key) {
        if (Utility.isEmpty(request.getAttribute(SPECIAL_MESSAGE))) {
            addMessage(request, key);
        } else {
            addMessage(request, (String) request.getAttribute(SPECIAL_MESSAGE));
        }
    }
}

;
