package com.patientis.business.controllers;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;
import com.patientis.business.settings.DefaultCustomOrderListSettings;
import com.patientis.client.action.BaseAction;
import com.patientis.client.action.TableAction;
import com.patientis.client.forms.PatientFormController;
import com.patientis.client.service.order.OrderService;
import com.patientis.client.state.State;
import com.patientis.data.common.ISParameter;
import com.patientis.framework.api.services.ClinicalServer;
import com.patientis.framework.api.services.OrderServer;
import com.patientis.framework.api.services.PatientServer;
import com.patientis.framework.api.services.ReferenceServer;
import com.patientis.framework.api.standard.StandardRecordItemReference;
import com.patientis.framework.controls.ISControlPanel;
import com.patientis.framework.controls.ISPanel;
import com.patientis.framework.controls.custom.ISRibbonBuilder;
import com.patientis.framework.controls.exceptions.ISCancelActionException;
import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.framework.controls.table.ISColumn;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scheduler.DefaultInstanceScheduler;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.framework.scripting.ISMediator;
import com.patientis.framework.scripting.References;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.framework.utility.SwingUtil;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.clinical.FormTypeModel;
import com.patientis.model.common.BaseModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DefaultBaseModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.common.ITransitionModel;
import com.patientis.model.order.OrderInstanceModel;
import com.patientis.model.order.OrderInstanceTransitionModel;
import com.patientis.model.order.OrderModel;
import com.patientis.model.order.OrderTypeModel;
import com.patientis.model.order.OrderTypeTransitionModel;
import com.patientis.model.patient.PatientModel;
import com.patientis.model.reference.ActionReference;
import com.patientis.model.reference.ContextReference;
import com.patientis.model.reference.FormTypeReference;
import com.patientis.model.reference.OrderInstanceStatusReference;
import com.patientis.model.reference.OrderTransitionReference;
import com.patientis.model.reference.RefModel;
import com.patientis.model.security.ApplicationControlModel;
import com.patientis.model.security.ApplicationViewModel;

/**
 * @author gcaulton
 *
 */
public class DefaultCustomOrdersController extends DefaultCustomController {

    /**
	 * 
	 */
    protected ISControlPanel formDetailsControlPanel = null;

    /**
	 * 
	 */
    protected DefaultBaseModelRenderer renderer = null;

    /**
	 * 
	 */
    protected enum OrderMode {

        APPLICATION, PATIENTPROFILE, PATIENTFORM, APPLICATIONINSTANCE
    }

    ;

    /**
	 * 
	 */
    protected OrderMode orderMode = null;

    /**
	 * 
	 */
    protected FormModel form = null;

    /**
	 * 
	 */
    protected PatientModel patient = null;

    /**
	 * 
	 */
    protected ISMediator controlMediator = null;

    /**
	 * Select selected result list
	 */
    protected List<OrderModel> selectedOrders = new ArrayList<OrderModel>();

    /**
	 * 
	 */
    protected List<OrderModel> lastOrderList = new ArrayList<OrderModel>();

    /**
	 * 
	 */
    protected ISRibbonBuilder ribbon = null;

    /**
	 * 
	 */
    protected int controlContextRefId = 0;

    protected DefaultCustomOrderListSettings settings = null;

    /**
	 * 
	 */
    protected ApplicationControlModel acm = null;

    /**
	 * 
	 */
    protected static List<OrderTypeModel> orderTypes = null;

    protected static final String PATIENTNAME = "Patient";

    protected static final String ORDERNAME = "Order Name";

    protected static final String ORDERINGPRESCRIBER = "Prescriber";

    protected static final String ORDERALERTS = "Alerts";

    protected static final String ORDERINSTRUCTIONS = "Instructions";

    protected static final String ORDERNAMEDOSEROUTEFREQUENCY = "Order";

    protected static final String ORDERNEXTADMINTIME = "Next admin";

    protected static final String ORDERSCHEDULE = "Scedule";

    protected static final String ORDERDIAGNOSIS = "Diagnosis";

    protected static final String ORDERSTARTDATE = "Start";

    protected static final String ORDERSTOPDATE = "Stop";

    protected static final String ORDERDURATION = "Duration";

    protected static final String MEDDISPENSE = "Med Dispense";

    protected static final String PHARMACYDISPENSE = "Rx Dispense";

    protected static final String ORDERSTATE = "State";

    protected static final String ORDERSTATUS = "Status";

    protected static final String ORDERINSTANCESTATE = "Instance state";

    protected static final String ORDERINSTANCEDATE = "Instance date";

    protected static final String CHARTFORM = "Chart Form";

    protected static final String ORDERTITLE = "Title";

    /**
	 * 
	 */
    protected Component panelc = null;

    /**
	 * 
	 * @param mode
	 */
    public DefaultCustomOrdersController(OrderMode mode) {
        this.orderMode = mode;
        columns = getDefaultOrderColumns();
    }

    /**
	 * 
	 * @return
	 */
    public static String[] getDefaultOrderColumns() {
        return new String[] { PATIENTNAME, ORDERNAME, ORDERINGPRESCRIBER, ORDERALERTS, ORDERINSTRUCTIONS, ORDERNAMEDOSEROUTEFREQUENCY, ORDERNEXTADMINTIME, ORDERSCHEDULE, ORDERSTARTDATE, ORDERSTOPDATE, ORDERSTATE, ORDERDIAGNOSIS, ORDERSTATUS, ORDERINSTANCESTATE, ORDERINSTANCEDATE, CHARTFORM, ORDERDURATION, MEDDISPENSE, PHARMACYDISPENSE, ORDERTITLE };
    }

    /**
	 * @see com.patientis.business.common.ICustomController#clientInitializeControl(com.patientis.model.common.IBaseModel, com.patientis.framework.controls.ISControlPanel, java.awt.Component)
	 */
    @Override
    public void clientInitializeControl(IBaseModel model, ISControlPanel formDetailsControlPanel, Component c, ApplicationControlModel acm) throws Exception {
        this.formDetailsControlPanel = formDetailsControlPanel;
        if (c != null) {
            if (SwingUtil.getParentFrame(c) != null) {
                setFrame(SwingUtil.getParentFrame(c));
            } else if (SwingUtil.getParentDialog(c) != null) {
                setDialog(SwingUtil.getParentDialog(c));
            }
        }
        panelc = c;
        controlContextRefId = acm.getContextRefId();
        switch(orderMode) {
            case PATIENTPROFILE:
            case PATIENTFORM:
                if (model instanceof FormModel) {
                    form = (FormModel) model;
                    patient = ServiceUtility.getPatientFromForm(form);
                }
                break;
        }
        if (formDetailsControlPanel != null) {
            this.controlMediator = formDetailsControlPanel.getMediator();
        }
        long settingsFormId = acm.getSettingFormValue(FormTypeReference.SYSTEMSETTINGSORDERSVIEW.getRefId()).getSettingsFormId();
        settingsForm = ClinicalServer.getForm(settingsFormId);
        createRenderer();
        if (formDetailsControlPanel != null) {
            renderer.initialize(new ArrayList<IBaseModel>(), formDetailsControlPanel.getMediator(), acm, getSettings());
        }
        refreshRecords();
        if (form != null) {
            monitorRefreshRecords(form.getPatientId(), form.getVisitId());
        }
    }

    /**
	 * Trigger an update for the view
	 */
    public boolean refreshRecords() throws Exception {
        List<OrderModel> orders = new ArrayList<OrderModel>();
        if (listFilter.getRecords().size() == 0) {
            listFilter.getRecords().addAll(settingsForm.getRecords());
        }
        switch(orderMode) {
            case APPLICATIONINSTANCE:
            case APPLICATION:
                orders = OrderServer.getOrdersByFilter(listFilter);
                if (orderMode == OrderMode.APPLICATIONINSTANCE) {
                    populateInstance(orders);
                }
                break;
            case PATIENTFORM:
                for (OrderModel order : form.getOrders()) {
                    try {
                        order.validateDataModel();
                        orders.add(order);
                    } catch (ISValidateControlException ex) {
                    }
                }
                break;
            case PATIENTPROFILE:
                if (patient.isNotNew()) {
                    listFilter.setPatientId(patient.getId());
                    listFilter.setVisitId(patient.getVisitId());
                    orders = OrderServer.getOrdersByFilter(listFilter);
                }
                break;
        }
        lastOrderList = orders;
        resortOrders();
        return true;
    }

    /**
	 * 
	 */
    public void resetParentDialog() {
        if (panelc != null && getFrameOrDialog() == null && panelc.getParent() != null) {
            if (SwingUtil.getParentFrameOrDialog(panelc) != null) {
                setDialog(SwingUtil.getParentDialog(panelc));
                setFrame(SwingUtil.getParentFrame(panelc));
            }
        }
    }

    /**
	 * 
	 * @throws Exception
	 */
    protected void resortOrders() throws Exception {
        for (OrderModel o : lastOrderList) {
            DateTimeModel sortDate = Converter.convertDateTimeModel(getAdminTime(o).getReferenceObject());
            if (sortDate == null || sortDate.isNull()) {
                sortDate = DateTimeModel.get1900();
            }
            o.setAlternateSort(sortDate);
            o.setAlternateSortDescending(true);
        }
        Collections.sort(lastOrderList);
        renderer.refresh(BaseModel.getIBaseModels(lastOrderList));
        displayToolbar(getContextToolbar());
    }

    /**
	 * @see com.patientis.business.common.ICustomController#clientReceiveMessage()
	 */
    public IReceiveMessage clientReceiveMessage() throws Exception {
        return new IReceiveMessage() {

            public boolean receive(ISEvent event, Object value) throws Exception {
                switch(event) {
                    case EXECUTEACTION:
                        BaseAction action = (BaseAction) value;
                        switch(action.getActionReference()) {
                            case SYSTEMEXECUTETRANSITION:
                                if (action.hasContext(controlContextRefId)) {
                                    if (selectedOrders != null && selectedOrders.size() > 0) {
                                        OrderModel order = selectedOrders.get(0);
                                        OrderTypeTransitionModel transition = OrderServer.getOrderTypeTransition(action.getTransitionId());
                                        executeTransition(action, transition.getOrderTransitionRef().getId(), contextRefId);
                                        refreshOrderDisplay(order);
                                        return true;
                                    }
                                }
                                return false;
                            case REFRESHVIEW:
                                if (action.hasContext(controlContextRefId) || action.hasContext(ContextReference.PATIENTCHARTTABBEDPANE)) {
                                    return refreshRecords();
                                }
                                return false;
                            case NEWORDER:
                            case SYSTEMADD:
                                if (action.hasContext(controlContextRefId)) {
                                    newOrder(action);
                                    return true;
                                }
                                return false;
                            case SYSTEMADDORDER:
                                if (action.hasContext(controlContextRefId)) {
                                    addOrder(action);
                                    return true;
                                }
                                return false;
                            case SYSTEMADVANCEDFILTER:
                                if (action.hasContext(controlContextRefId)) {
                                    openAdvancedFilter(action);
                                    return true;
                                }
                                ;
                                return false;
                            case SYSTEMPREPAREFROMSELECTION:
                            case SYSTEMPREPAREFORM:
                                if (action.hasContext(controlContextRefId)) {
                                    PatientModel selectedPatient = patient;
                                    if (orderMode == OrderMode.APPLICATION) {
                                        if (selectedOrders != null && selectedOrders.size() > 0) {
                                            OrderModel order = selectedOrders.get(0);
                                            selectedPatient = PatientServer.getPatientForVisitId(order.getVisitId());
                                            ServiceUtility.prepareForm(getFrameOrDialog(), action, selectedPatient);
                                            refreshRecords();
                                        }
                                    } else {
                                        ServiceUtility.prepareForm(getFrameOrDialog(), action, selectedPatient);
                                        refreshRecords();
                                    }
                                    return true;
                                }
                                ;
                                return false;
                            case SYSTEMOPENPATIENTCHART:
                                if (action.hasContext(controlContextRefId)) {
                                    openPatientChart(action);
                                    return true;
                                }
                                ;
                                return false;
                            case ORDERCHART:
                                if (action.hasContext(controlContextRefId)) {
                                    if (selectedOrders != null && selectedOrders.size() > 0) {
                                        OrderModel order = selectedOrders.get(0);
                                        if (chartOrder(action)) {
                                            OrderService.executeOrderTransition(order.getId(), action.getTransitionId());
                                            refreshOrderDisplay(order);
                                        }
                                    }
                                    return true;
                                }
                                ;
                                return false;
                            case ORDERSIGN:
                                if (action.hasContext(controlContextRefId)) {
                                    if (selectedOrders != null && selectedOrders.size() > 0) {
                                        signOrder(action);
                                    }
                                    return true;
                                }
                                ;
                                return false;
                            case ORDERMODIFY:
                                if (action.hasContext(controlContextRefId)) {
                                    if (selectedOrders != null && selectedOrders.size() > 0) {
                                        OrderModel order = selectedOrders.get(0);
                                        if (editOrder(action)) {
                                            OrderService.executeOrderTransition(order.getId(), action.getTransitionId());
                                            refreshOrderDisplay(order);
                                        }
                                    }
                                    return true;
                                }
                                ;
                                return false;
                            case ORDERCOMPLETE:
                            case ORDERCONSENTED:
                            case ORDERDISCONTINUE:
                            case ORDERHOLD:
                            case ORDERREVIEW:
                            case ORDERCANCEL:
                            case ORDERSTATUSUPDATE:
                                if (action.hasContext(controlContextRefId)) {
                                    if (selectedOrders != null && selectedOrders.size() > 0) {
                                        for (OrderModel order : selectedOrders) {
                                            try {
                                                OrderService.executeOrderTransition(order.getId(), action.getTransitionId());
                                                refreshOrderDisplay(order);
                                            } catch (Exception ex) {
                                                Log.exception(ex);
                                            }
                                        }
                                    }
                                    return true;
                                }
                                ;
                        }
                        break;
                    case EXECUTETABLEACTION:
                        TableAction tableAction = (TableAction) value;
                        switch(tableAction.getActionReference()) {
                            case RESULTGRIDEDITRESULT:
                                return true;
                            case SYSTEMEDIT:
                                if (tableAction.hasContext(controlContextRefId)) {
                                    return editOrder(tableAction);
                                }
                                ;
                                return false;
                            case SYSTEMRIGHTCLICK:
                                if (tableAction.hasContext(controlContextRefId)) {
                                    ServiceUtility.displayMenu(tableAction, getMenuItems(tableAction), getMainMediator());
                                }
                                ;
                                return false;
                            case TABLEROWSELECTED:
                                if (tableAction.hasContext(controlContextRefId)) {
                                    Object selectedValue = tableAction.getSelectedModel();
                                    if (selectedValue instanceof OrderModel) {
                                        selectedOrders.clear();
                                        BaseModel.toAnyList(tableAction.getSelectedModels(), selectedOrders);
                                        displayOrder((OrderModel) selectedValue);
                                        displayToolbar(getContextToolbar());
                                    }
                                    return true;
                                }
                                break;
                        }
                        break;
                }
                return false;
            }
        };
    }

    /**
	 * 
	 * @param orderId
	 * @throws Exception
	 */
    protected void refreshOrderDisplay(OrderModel order) throws Exception {
        OrderModel savedOrder = OrderServer.getOrder(order.getId());
        order.copyAllFrom(savedOrder);
        ServiceUtility.repaintTable(controlContextRefId, getMainMediator());
        displayToolbar(getContextToolbar());
    }

    /**
	 * 
	 * @return
	 */
    protected List<ApplicationControlModel> getMenuItems(TableAction tableAction) {
        List<ApplicationControlModel> items = new ArrayList<ApplicationControlModel>();
        if (tableAction.getSelectedModel() instanceof OrderModel) {
            OrderModel rightClickOrder = (OrderModel) tableAction.getSelectedModel();
            if (selectedOrders.size() == 1) {
                ApplicationControlModel menuItem = ApplicationControlModel.createNewMenuItem("Open Patient Chart", ActionReference.SYSTEMOPENPATIENTCHART, controlContextRefId);
                if (orderMode == OrderMode.APPLICATION) {
                    items.add(menuItem);
                }
            }
        }
        return items;
    }

    /**
	 * Create a new result
	 */
    protected boolean editOrder(BaseAction action) throws Exception {
        if (selectedOrders != null && selectedOrders.size() > 0) {
            OrderModel order = selectedOrders.get(0);
            if (order.isNotNew()) {
                PatientModel patient = PatientServer.getPatientVisit(order.getPatientId(), order.getVisitId());
                ServiceUtility.editOrder(action.getParentFrameOrDialog(getFrameOrDialog()), order.getId(), patient);
            }
            return true;
        }
        return false;
    }

    /**
	 * Display the form
	 * @param form
	 * @throws Exception
	 */
    protected void displayOrder(OrderModel form) throws Exception {
    }

    /**
     * 
     * @param action
     * @throws Exception
     */
    public void addOrder(final BaseAction action) throws Exception {
    }

    /**
     * 
     * @param action
     * @throws Exception
     */
    public void newOrder(final BaseAction action) throws Exception {
        if (this.patient == null) {
            patient = ServiceUtility.findPatient(action.getParentFrameOrDialog(getFrameOrDialog()));
        }
        if (patient != null && patient.getVisitId() > 0L) {
            ApplicationControlModel acm = ServiceUtility.getApplicationControl(action);
            if (acm.getActionFormTypeRef().isNotNew()) {
                List<OrderModel> orders = ServiceUtility.searchOrders(action.getParentFrameOrDialog(getFrameOrDialog()), ServiceUtility.getApplicationControl(action), patient.getId(), patient.getVisitId(), false);
                ServiceUtility.orderEntry(action.getParentFrameOrDialog(getFrameOrDialog()), orders, patient.getVisitId(), acm.getActionFormTypeRef().getId());
            }
        }
    }

    /**
	 * 
	 * @param orderActionRefId
	 * @return
	 */
    public boolean isLocalAction(long orderActionRefId) {
        switch(ActionReference.get(orderActionRefId)) {
            case ORDERREVIEW:
            case ORDERMODIFY:
            case ORDERNEW:
            case ORDERSIGN:
            case ORDERVIEW:
                return true;
            case ORDERCANCEL:
            case ORDERCONSENTED:
            case ORDERDISCONTINUE:
            case ORDERHOLD:
            case ORDERCOMPLETE:
            default:
                return false;
        }
    }

    /**
	 * Create a new result
	 */
    protected boolean signOrder(BaseAction action) throws Exception {
        if (selectedOrders != null && selectedOrders.size() > 0) {
            OrderModel order = selectedOrders.get(0);
            if (order.isNotNew()) {
                ServiceUtility.authenticateSignature(action.getParentFrameOrDialog(getFrameOrDialog()), State.getUser());
                for (OrderModel o : selectedOrders) {
                    OrderService.executeOrderTransition(order.getId(), action.getTransitionId());
                    OrderModel savedOrder = OrderServer.getOrder(o.getId());
                    o.copyAllFrom(savedOrder);
                }
                ServiceUtility.repaintTable(controlContextRefId, getMainMediator());
                displayToolbar(getContextToolbar());
                return true;
            }
        }
        return false;
    }

    /**
	 * 
	 * @param transitionRefId
	 * @return
	 * @throws Exception
	 */
    public boolean executeTransition(BaseAction action, long transitionRefId, int contextRefId) throws Exception {
        for (OrderModel order : selectedOrders) {
            OrderTypeModel orderType = OrderServer.getOrderTypeForOrderTypeRefId(order.getOrderTypeRef().getId());
            List<OrderTypeTransitionModel> transitions = orderType.getTransitionActions(order.getOrderStateRef().getId(), State.getRole().getRoleGroupRef().getId());
            for (OrderTypeTransitionModel transition : transitions) {
                if (transition.getOrderTransitionRef().isId(transitionRefId)) {
                    if (transition.getTransitionRef().isId(4950304)) {
                        if (order.getChartFormTypeRef().isNotNew()) {
                            chartForm(action, order, order.getChartFormTypeRef().getId());
                        } else if (transition.getTransitionFormTypeRef().isNotNew()) {
                            chartForm(action, order, transition.getTransitionFormTypeRef().getId());
                        } else if (orderType.getOrderFormTypeRef().isNotNew()) {
                            chartForm(action, order, orderType.getOrderFormTypeRef().getId());
                        }
                    } else if (transition.getTransitionFormTypeRef().isNotNew()) {
                        chartForm(action, order, transition.getTransitionFormTypeRef().getId());
                    }
                    OrderService.executeOrderTransition(order.getId(), transition.getId());
                    OrderModel savedOrder = OrderServer.getOrder(order.getId());
                    order.copyAllFrom(savedOrder);
                    ServiceUtility.repaintTable(controlContextRefId, getMainMediator());
                    displayToolbar(getContextToolbar());
                }
            }
            OrderInstanceModel orderInstance = order.getCurrentInstance();
            List<OrderInstanceTransitionModel> instanceTransitions = orderType.getInstanceTransitionActions(orderInstance.getInstanceStateRef().getId(), State.getRole().getRoleGroupRef().getId());
            for (OrderInstanceTransitionModel instanceTransition : instanceTransitions) {
                if (instanceTransition.getOrderInstanceTransitionRef().isId(transitionRefId)) {
                    if (order.getCurrentInstance().isNotNew()) {
                        if (instanceTransition.getFromStateRef().isId(orderInstance.getInstanceStateRef().getId())) {
                            if (instanceTransition.getTransitionFormTypeRef().isNotNew()) {
                                FormTypeModel formType = ClinicalServer.getFormTypeForFormTypeRefId(instanceTransition.getTransitionFormTypeRef().getId());
                                FormModel form = ClinicalServer.getForm(order.getFormId());
                                form.giveOrder().copyAllFrom(order);
                                if (formType.isNotNew() && order.getVisitId() > 0L) {
                                    PatientModel patient = PatientServer.getPatientForVisitId(order.getVisitId());
                                    PatientFormController controller = PatientFormController.getInstance();
                                    controller.start(action.getParentFrameOrDialog(getFrameOrDialog()), form, patient, formType, ServiceUtility.getApplicationControl(action));
                                    if (controller.isUserCancelled()) {
                                        throw new ISCancelActionException();
                                    }
                                }
                            }
                            OrderServer.executeOrderInstanceTransition(orderInstance.getId(), instanceTransition.getOrderInstanceTransitionRef().getId());
                            OrderModel savedOrder = OrderServer.getOrder(order.getId());
                            order.copyAllFrom(savedOrder);
                            List<OrderModel> orders = new ArrayList<OrderModel>();
                            orders.add(order);
                            populateInstance(orders);
                            ServiceUtility.repaintTable(controlContextRefId, getMainMediator());
                            displayToolbar(getContextToolbar());
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
	 * 
	 * @param order
	 * @param formTypeRefId
	 * @throws Exception
	 */
    protected void chartForm(BaseAction action, OrderModel order, long formTypeRefId) throws Exception {
        FormTypeModel formType = ClinicalServer.getFormTypeForFormTypeRefId(formTypeRefId);
        FormModel form = ClinicalServer.prepareNewForm(order.getPatientId(), order.getVisitId(), formType.getFormTypeRef().getId());
        form.getOrders().add(order);
        if (formType.isNotNew() && order.getVisitId() > 0L) {
            PatientModel patient = PatientServer.getPatientForVisitId(order.getVisitId());
            PatientFormController controller = PatientFormController.getInstance();
            controller.start(action.getParentFrameOrDialog(getFrameOrDialog()), form, patient, formType, ServiceUtility.getApplicationControl(action));
            if (controller.isUserCancelled()) {
                throw new ISCancelActionException();
            }
        }
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    protected ISPanel getContextToolbar() throws Exception {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new Exception();
        }
        List<ITransitionModel> transitionDisplays = new ArrayList<ITransitionModel>();
        if (orderMode == OrderMode.APPLICATIONINSTANCE) {
            transitionDisplays = new ArrayList<ITransitionModel>();
        } else {
            transitionDisplays = ServiceUtility.getCommonOrderTransitions(selectedOrders);
        }
        ApplicationViewModel toolbarView = ServiceUtility.createToolbarView(transitionDisplays, controlContextRefId);
        if (ribbon != null) {
            ribbon.releaseControls();
        }
        resetParentDialog();
        ribbon = new ISRibbonBuilder(getMainMediator(), new DefaultBaseModel(), new References(), getFrameOrDialog());
        ISPanel toolbar = ribbon.createToolbar(toolbarView);
        return toolbar;
    }

    /**
	 * 
	 * @param contextRef
	 * @param panel
	 * @throws Exception
	 */
    public void displayToolbar(ISPanel panel) throws Exception {
        BaseAction action = new BaseAction();
        action.setActionReference(ActionReference.SYSTEMREPLACETOOLBARBUTTONS);
        action.setContextRefId(controlContextRefId);
        action.setSource(panel);
        getMainMediator().receive(ISEvent.EXECUTEACTION, action);
    }

    /**
	 * Create a new result
	 */
    protected boolean chartOrder(BaseAction action) throws Exception {
        if (selectedOrders != null && selectedOrders.size() > 0) {
            OrderModel selectedOrder = selectedOrders.get(0);
            if (selectedOrder.isNotNew()) {
                if (selectedOrder.getVisitId() > 0) {
                    if (action.getTransitionModelId() > 0) {
                        OrderTypeTransitionModel transition = OrderServer.getOrderTypeTransition(action.getTransitionModelId());
                        if (transition.getTransitionFormTypeRef().isNotNew()) {
                            FormModel form = ClinicalServer.prepareNewForm(0, 0, transition.getTransitionFormTypeRef().getId());
                            form.giveOrder().copyAllFrom(selectedOrder);
                            ApplicationControlModel acm = ServiceUtility.getApplicationControl(action);
                            PatientModel patient = PatientServer.getPatientVisit(form.giveOrder().getPatientId(), form.giveOrder().getVisitId());
                            if (ServiceUtility.editForm(action.getParentFrameOrDialog(getFrameOrDialog()), form, form.getFormTypeRefId(), patient, acm) > 0L) {
                                return true;
                            }
                        }
                    } else if (action.getInstanceTransitionmodelId() > 0) {
                        ApplicationControlModel acm = ServiceUtility.getApplicationControl(action);
                        OrderInstanceTransitionModel instanceTransition = OrderServer.getOrderInstanceTransition(action.getInstanceTransitionmodelId());
                        if (instanceTransition.getTransitionFormTypeRef().isNotNew()) {
                            FormModel form = ClinicalServer.prepareNewForm(0, 0, instanceTransition.getTransitionFormTypeRef().getId());
                            form.giveOrder().copyAllFrom(selectedOrder);
                            PatientModel patient = PatientServer.getPatientVisit(form.giveOrder().getPatientId(), form.giveOrder().getVisitId());
                            if (ServiceUtility.editForm(action.getParentFrameOrDialog(getFrameOrDialog()), form, form.getFormTypeRefId(), patient, acm) > 0L) {
                                return true;
                            }
                        }
                    } else if (selectedOrder.getChartFormTypeRef().isNotNew()) {
                        DisplayModel orderInstance = getAdminTime(selectedOrder);
                        if (orderInstance.isNotNew()) {
                            FormModel form = ClinicalServer.prepareNewForm(selectedOrder.getPatientId(), selectedOrder.getVisitId(), selectedOrder.getChartFormTypeRef().getId());
                            form.giveOrder().copyAllFrom(selectedOrder);
                            ApplicationControlModel acm = ServiceUtility.getApplicationControl(action);
                            PatientModel patient = PatientServer.getPatientVisit(form.giveOrder().getPatientId(), form.giveOrder().getVisitId());
                            if (ServiceUtility.editForm(action.getParentFrameOrDialog(getFrameOrDialog()), form, form.getFormTypeRefId(), patient, acm) > 0L) {
                                OrderInstanceModel instance = OrderServer.getOrderInstance(orderInstance.getId());
                                instance.setInstanceStatusRef(new DisplayModel(OrderInstanceStatusReference.COMPLETED.getRefId()));
                                OrderServer.store(instance);
                                resortOrders();
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
	 * 
	 * @param action
	 * @throws Exception
	 */
    protected void openPatientChart(BaseAction action) throws Exception {
        if (selectedOrders.size() == 1) {
            OrderModel order = selectedOrders.get(0);
            if (order.getVisitId() > 0) {
                PatientModel patient = PatientServer.getPatientVisit(order.getPatientId(), order.getVisitId());
                ServiceUtility.openPatientChart(patient);
            }
        }
    }

    /**
	 * 
	 * @return
	 */
    public DefaultCustomOrderListSettings getSettings() {
        if (settings == null) {
            settings = new DefaultCustomOrderListSettings(settingsForm);
        }
        return settings;
    }

    /**
	 * 
	 */
    public void createRenderer() {
        renderer = new DefaultBaseModelRenderer() {

            /**
			 * 
			 * @param form
			 * @param col
			 * @return
			 */
            public String getTextDisplay(IBaseModel model, int col) {
                try {
                    OrderModel order = (OrderModel) model;
                    ISColumn column = getTableColumn(col);
                    if (column.getObject() != null) {
                        String fieldName = (String) column.getObject();
                        if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERALERTS)) {
                            return "";
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERINGPRESCRIBER)) {
                            if (order.getOrderProviderRef().isNew()) {
                                return order.getInsertUserRef().getShortDisplay();
                            } else {
                                return order.getOrderProviderRef().getShortDisplay();
                            }
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERINSTRUCTIONS)) {
                            order.getInstructions();
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERNAME)) {
                            return order.getOrderName();
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERTITLE)) {
                            return order.getTitle();
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERNAMEDOSEROUTEFREQUENCY)) {
                            return order.getHtmlDocumentDisplay(getOrderTypeIcon(order), true, true);
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERSCHEDULE)) {
                            return order.getFrequencyRef().getDisplay();
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERSTARTDATE)) {
                            return order.getStartDt().toString(DateTimeModel.getDefaultShortDateTimeFormat());
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERSTOPDATE)) {
                            return order.getStopDt().toString(DateTimeModel.getDefaultShortDateFormat());
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERSTATE)) {
                            return order.getOrderStateRef().getDisplay();
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERSTATUS)) {
                            return order.getOrderStateRef().getDisplay();
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, PATIENTNAME)) {
                            return PatientServer.getPatientName(order.getPatientId());
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERNEXTADMINTIME)) {
                            return getAdminTime(order).getDisplay();
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERDIAGNOSIS)) {
                            return getFormRecord(order, StandardRecordItemReference.StandardOrderingDiagnosis);
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, CHARTFORM)) {
                            return order.getChartFormTypeRef().getShortDisplay();
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERINSTANCEDATE)) {
                            return order.getCurrentInstance().getInstanceDt().toString(DateTimeModel.getDefaultDayMonthTimeAMPMFormat());
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERINSTANCESTATE)) {
                            return order.getCurrentInstance().getInstanceStateRef().getShortDisplay();
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, ORDERDURATION)) {
                            return order.getDurationDisplay();
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, PHARMACYDISPENSE)) {
                            try {
                                if (order.getMeds().size() > 0 && order.getStopDt().isNotNull() && order.getStopDt().isAfter(order.getStartDt())) {
                                    DefaultInstanceScheduler sch = DefaultInstanceScheduler.getInstance();
                                    int instances = sch.getScheduleInstances(order, order.getStopDt(), true).size();
                                    return Converter.convertInteger(order.giveMedOrder().getDispenseQuantity() * instances) + " " + order.giveMedOrder().getOrderDoseFormRef().getDisplay();
                                } else {
                                    return "";
                                }
                            } catch (Exception ex) {
                                return ex.getMessage();
                            }
                        } else if (Converter.isTrimmedSameIgnoreCase(fieldName, MEDDISPENSE)) {
                            if (order.getMeds().size() > 0) {
                                return order.giveMedOrder().getDispenseDisplay() + " " + order.getFrequencyRef().getDisplay();
                            } else {
                                return "";
                            }
                        } else {
                            return "? " + fieldName;
                        }
                    }
                } catch (Exception ex) {
                    Log.exception(ex);
                }
                return "";
            }

            /**
			 * 
			 * @param order
			 * @return
			 */
            public DisplayModel getOrderIcons(long visitId) {
                try {
                    return new DisplayModel();
                } catch (Exception ex) {
                    Log.exception(ex);
                    return new DisplayModel();
                }
            }
        };
    }

    /**
	 * 
	 * @param order
	 * @param recordItemRefId
	 * @return
	 */
    public String getFormRecord(OrderModel order, long recordItemRefId) {
        try {
            if (order.getFormId() == 0L) {
                return "";
            }
            FormModel orderForm = ClinicalServer.getForm(order.getFormId());
            FormRecordModel record = orderForm.giveFormRecord(recordItemRefId);
            return Converter.convertDisplayString(record.getValue());
        } catch (Exception ex) {
            Log.exception(ex);
            return ex.getMessage();
        }
    }

    /**
	 * 
	 * @param order
	 * @return
	 */
    public DisplayModel getAdminTime(OrderModel order) {
        try {
            String sql = " select order_instance_id, instance_dt " + " from order_instances " + " where order_id = :orderId " + " and instance_dt = ( select min(instance_dt) as instance_dt from order_instances " + " where order_id = :orderId and instance_status_ref_id not in (select order_instance_state_ref_id " + " from order_instance_states where order_instance_status_ref_id in (1062043, 1062044)) and active_ind = 1)";
            List objects = ReferenceServer.sqlQuery(sql, ISParameter.createList(new ISParameter("orderId", order.getId())));
            if (objects != null) {
                for (Object o : objects) {
                    Object[] items = (Object[]) o;
                    DisplayModel d = new DisplayModel();
                    d.setId(Converter.convertLong(items[0]));
                    d.setDisplay(Converter.convertDisplayString(Converter.convertDateTimeModel(items[1]), DateTimeModel.getDefaultDayMonthTimeAMPMFormat()));
                    d.setReferenceObject(Converter.convertDateTimeModel(items[1]));
                    return d;
                }
            }
            return new DisplayModel();
        } catch (Exception ex) {
            Log.exception(ex);
            return new DisplayModel();
        }
    }

    /**
	 * 
	 * @param order
	 * @return
	 */
    public void populateInstance(List<OrderModel> orders) {
        String sql = " select order_instance_id " + " from order_instances " + " where order_id = :orderId " + " and instance_dt = ( select min(instance_dt) as instance_dt from order_instances " + " where order_id = :orderId and instance_status_ref_id not in (select order_instance_state_ref_id " + " from order_instance_states where order_instance_status_ref_id in (1062043, 1062044)) and active_ind = 1)";
        for (OrderModel order : orders) {
            try {
                List objects = ReferenceServer.sqlQuery(sql, ISParameter.createList(new ISParameter("orderId", order.getId())));
                if (objects != null && objects.size() > 0) {
                    long orderInstanceId = Converter.convertLong(objects.get(0));
                    OrderInstanceModel orderInstance = OrderServer.getOrderInstance(orderInstanceId);
                    order.setCurrentInstance(orderInstance);
                }
            } catch (Exception ex) {
                Log.exception(ex);
            }
        }
    }

    /**
	 * 
	 * @param order
	 * @return
	 */
    protected String getOrderTypeIcon(OrderModel order) throws Exception {
        if (orderTypes == null) {
            String hql = "from OrderTypeModel";
            List<IBaseModel> models = ReferenceServer.getHqlParameter(OrderTypeModel.class, hql, ISParameter.createList());
            orderTypes = new ArrayList<OrderTypeModel>();
            BaseModel.toAnyList(models, orderTypes);
        }
        for (OrderTypeModel orderType : orderTypes) {
            if (orderType.getOrderTypeRef().isId(order.getOrderTypeRef().getId())) {
                if (orderType.getOrderIconRef().isNotNew()) {
                    return "<img src='image://" + RefModel.getDefaultRefKey(orderType.getOrderIconRef().getDisplay()) + "'/> ";
                }
            }
        }
        return "";
    }
}
