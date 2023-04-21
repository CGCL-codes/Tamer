package com.patientis.client.scheduling;

import java.awt.BorderLayout;
import com.patientis.client.action.BaseAction;
import com.patientis.client.action.TableAction;
import com.patientis.client.common.BaseController;
import com.patientis.client.service.reference.ReferenceService;
import com.patientis.client.service.scheduling.SchedulingService;
import com.patientis.framework.controls.IControlPanel;
import com.patientis.framework.controls.IFormDisplay;
import com.patientis.framework.controls.ISControlPanel;
import com.patientis.framework.controls.ISPanel;
import com.patientis.framework.controls.exceptions.ISCancelActionException;
import com.patientis.client.common.PromptsController;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DefaultBaseModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.reference.ApplicationDialogReference;
import com.patientis.model.reference.ContextReference;
import com.patientis.model.reference.AccessReference;
import com.patientis.model.reference.ResourceScheduleTypeReference;
import com.patientis.model.reference.ScheduleTemplateReference;
import com.patientis.model.reference.ViewReference;
import com.patientis.model.scheduling.ScheduleTemplateModel;
import com.patientis.model.scheduling.ScheduleTemplateTimeModel;
import com.patientis.client.service.security.SecurityService;

/**
 * Schedule Templates primary function is as a mediator of actionable messages.
 * 
 */
public class ScheduleTemplatesController extends BaseController {

    /**
	 * Last selected
	 */
    private ScheduleTemplateModel selectedScheduleTemplate = new ScheduleTemplateModel();

    /**
	 * Dynamic
	 */
    private ISPanel formPanel = new ISPanel(new BorderLayout());

    /**
	 * Use getInstance()
	 */
    private ScheduleTemplatesController() {
    }

    /**
	 * Get a new instance of the controller
	 * 
	 * @return
	 */
    public static ScheduleTemplatesController getInstance() {
        ScheduleTemplatesController controller = new ScheduleTemplatesController();
        ScheduleTemplateModel ScheduleTemplateModelVariable = new ScheduleTemplateModel();
        controller.setDefaultBaseModel(ScheduleTemplateModelVariable);
        return controller;
    }

    /**
	 * Start the application  
	 * 
	 * @throws Exception
	 */
    public void start() throws Exception {
        startFrame(ApplicationDialogReference.SCHEDULETEMPLATESDIALOG);
        loadAccessListResultsByRef(AccessReference.SCHEDULETEMPLATEMODELLIST.getRefId());
        enableOkApplyCancelOnChange();
        setDisableOkUntilChanges(true);
        registerGlobalRefresh(ContextReference.SCHEDULETEMPLATESSCHEDULETEMPLATEMODELLISTTABLE);
    }

    /**
	 * @see com.patientis.client.common.BaseController#getControlPanel(java.lang.Long)
	 */
    @Override
    public IControlPanel getControlPanel(Long viewId) throws Exception {
        IFormDisplay display = SecurityService.getApplicationViewFromCache(viewId);
        if (display.getViewRefId() == ViewReference.SCHEDULETEMPLATESLISTPANEL.getRefId()) {
            return new ISControlPanel(getFormMediator(), new DefaultBaseModel(), display, getFrame());
        } else if (display.getViewRefId() == ViewReference.EMPTYFORMTEMPLATE.getRefId()) {
            return new ISControlPanel(formPanel);
        } else {
            return super.getControlPanel(viewId);
        }
    }

    /**
	 * Form specific message mediation
	 */
    @Override
    protected void mediateMessages() {
        getFormMediator().register(new IReceiveMessage() {

            public boolean receive(ISEvent event, Object value) throws Exception {
                switch(event) {
                    case EXECUTEACTION:
                        BaseAction action = (BaseAction) value;
                        switch(action.getActionReference()) {
                            case REFRESHVIEW:
                                if (action.hasContext(ContextReference.SCHEDULETEMPLATESSCHEDULETEMPLATEMODELLISTTABLE)) {
                                    loadAccessListResultsByRef(AccessReference.SCHEDULETEMPLATEMODELLIST.getRefId());
                                }
                                return false;
                            case CANCELSUBMITFORM:
                                cancelCloseForm();
                                return true;
                            case APPLYFORMCHANGES:
                                save();
                                return true;
                            case CANCELFORMCHANGES:
                                clearScheduleTemplate(new ScheduleTemplateModel());
                                refreshAllTables();
                                disableApplyOk();
                                return true;
                            case SYSTEMADD:
                                if (action.hasContext(ContextReference.SCHEDULETEMPLATESSCHEDULETEMPLATEMODELLISTTABLE)) {
                                    return addScheduleTemplate();
                                } else if (action.hasContext(ContextReference.SCHEDULETEMPLATESSCHEDULETEMPLATEMODELSCHEDULETEMPLATETIME0TABLE)) {
                                    return addScheduleTime();
                                }
                                return false;
                            case SYSTEMREMOVE:
                                if (action.hasContext(ContextReference.SCHEDULETEMPLATESSCHEDULETEMPLATEMODELLISTTABLE)) {
                                    return removeScheduleTemplate();
                                } else {
                                    removeSelectedTableRows(action.getContextRefId());
                                    enableApplyOk();
                                    return true;
                                }
                        }
                        break;
                    case EXECUTETABLEACTION:
                        TableAction tableAction = (TableAction) value;
                        switch(tableAction.getActionReference()) {
                            case TABLEROWSELECTED:
                                if (tableAction.hasContext(ContextReference.SCHEDULETEMPLATESSCHEDULETEMPLATEMODELLISTTABLE)) {
                                    selectedScheduleTemplate = tableAction.hasSelectedRows() ? (ScheduleTemplateModel) tableAction.getSelectedModel() : new ScheduleTemplateModel();
                                    clearScheduleTemplate(selectedScheduleTemplate);
                                    disableOkApplyCancelOnChange();
                                    return false;
                                }
                        }
                }
                return false;
            }

            public String toString() {
                return "ScheduleTemplateBuilderController.mediateMessages";
            }
        }, this);
    }

    /**
	 * Get the selected order
	 * 
	 * @return
	 */
    private ScheduleTemplateModel getScheduleTemplate() {
        return (ScheduleTemplateModel) getDefaultBaseModel();
    }

    /**
	 * Clear the order and replaced with selected order
	 */
    private void clearScheduleTemplate(ScheduleTemplateModel selectedScheduleTemplate) throws Exception {
        getScheduleTemplate().getTimes().clear();
        getScheduleTemplate().clear();
        getScheduleTemplate().copyAllFrom(new ScheduleTemplateModel());
        getScheduleTemplate().copyAllFrom(selectedScheduleTemplate);
        enableOkApplyCancelOnChange(getScheduleTemplate().getTimes());
        refreshAllTablesExcept(ContextReference.SCHEDULETEMPLATESSCHEDULETEMPLATEMODELLISTTABLE);
    }

    /**
	 * Save the ScheduleTemplateModelVariable
	 * 
	 * @throws Exception
	 */
    private void save() throws Exception {
        validateControls();
        SchedulingService.store(getScheduleTemplate());
        ReferenceService.clearCache();
        loadAccessListResultsByRef(AccessReference.SCHEDULETEMPLATEMODELLIST.getRefId());
        clearScheduleTemplate(new ScheduleTemplateModel());
        disableOkApplyCancelOnChange();
    }

    /**
	 * Add a slot
	 * 
	 * @param action
	 * @return
	 * @throws Exception
	 */
    private boolean addScheduleTemplate() throws Exception {
        ScheduleTemplateModel newScheduleTemplate = new ScheduleTemplateModel();
        newScheduleTemplate.setScheduleTypeRef(new DisplayModel(ResourceScheduleTypeReference.WORKINGHOURS.getRefId()));
        clearScheduleTemplate(newScheduleTemplate);
        enableApplyOk();
        return true;
    }

    private boolean addScheduleTime() throws Exception {
        DateTimeModel startDt = null;
        DateTimeModel stopDt = null;
        String startTime = "xxxx";
        while (!Converter.isTime(startTime)) {
            startTime = PromptsController.getInput(getFrameOrDialog(), getScheduleTemplate().getScheduleTypeRef().getDisplay() + " from ? to ? \n\nEnter time from", "09:00");
            if (startTime == null) {
                throw new ISCancelActionException();
            } else if (Converter.isTime(startTime)) {
                startDt = Converter.convertTime(DateTimeModel.getNow(), startTime);
            }
        }
        String stopTime = "xxxx";
        while (!Converter.isTime(stopTime)) {
            stopTime = PromptsController.getInput(getFrameOrDialog(), getScheduleTemplate().getScheduleTypeRef().getDisplay() + " from " + startTime + " to ? \n\nEnter time to", "17:00");
            if (stopTime == null) {
                throw new ISCancelActionException();
            } else if (Converter.isTime(stopTime)) {
                stopDt = Converter.convertTime(DateTimeModel.getNow(), stopTime);
            }
        }
        ScheduleTemplateTimeModel time = new ScheduleTemplateTimeModel();
        time.setScheduleStartDt(startDt);
        time.setScheduleStopDt(stopDt);
        getScheduleTemplate().getTimes().add(time);
        enableApplyOk();
        refreshTable(ContextReference.SCHEDULETEMPLATESSCHEDULETEMPLATEMODELSCHEDULETEMPLATETIME0TABLE);
        return true;
    }

    /**
	 * Remove the selected appointment type
	 * 
	 * @return
	 * @throws Exception
	 */
    private boolean removeScheduleTemplate() throws Exception {
        if (selectedScheduleTemplate.isNotNew()) {
            if (PromptsController.questionIsOKCancelOK(getFrame(), "Remove " + selectedScheduleTemplate.getDisplayListText(), "Remove")) {
                selectedScheduleTemplate.setDeleted();
                SchedulingService.store(selectedScheduleTemplate);
                loadAccessListResultsByRef(AccessReference.SCHEDULETEMPLATEMODELLIST.getRefId());
            }
        }
        return false;
    }
}
