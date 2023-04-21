package com.patientis.client.forms;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import com.patientis.business.controllers.DefaultCustomController;
import com.patientis.client.action.BaseAction;
import com.patientis.client.service.security.SecurityService;
import com.patientis.ejb.common.IChainStore;
import com.patientis.framework.api.services.ClinicalServer;
import com.patientis.framework.controls.ISButtonPreviousNextCancel;
import com.patientis.framework.controls.ISControlPanel;
import com.patientis.framework.controls.ISPanel;
import com.patientis.framework.controls.ISTextController;
import com.patientis.framework.controls.exceptions.ISCancelActionException;
import com.patientis.framework.controls.forms.IContainer;
import com.patientis.framework.controls.forms.ISDialog;
import com.patientis.framework.controls.forms.ISFrame;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.framework.scripting.ISMediator;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormTypeModel;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.common.ServiceCall;
import com.patientis.model.order.OrderModel;
import com.patientis.model.reference.ActionReference;
import com.patientis.model.reference.RecordItemReference;
import com.patientis.model.reference.ViewReference;
import com.patientis.model.security.ApplicationControlModel;
import com.patientis.model.security.ApplicationViewModel;

/**
 * @author patientos
 *
 */
public class OrdersSignController extends DefaultCustomController {

    private FormModel form = null;

    /**
	 * 
	 */
    @Override
    public boolean hasClientButtonPanel() {
        return true;
    }

    /**
	 * @see com.patientis.business.controllers.DefaultCustomController#clientInitializeMonitor(com.patientis.model.common.IBaseModel, com.patientis.framework.controls.ISControlPanel)
	 */
    @Override
    public void clientInitializeMonitor(IBaseModel model, ISControlPanel formDetailsControlPanel) throws Exception {
        form = (FormModel) model;
    }

    /**
	 * @see com.patientis.business.controllers.DefaultCustomController#clientReceiveMessage()
	 */
    @Override
    public IReceiveMessage clientReceiveMessage() throws Exception {
        return new IReceiveMessage() {

            public boolean receive(ISEvent event, Object value) throws Exception {
                switch(event) {
                    case EXECUTEACTION:
                        BaseAction action = (BaseAction) value;
                        switch(action.getActionReference()) {
                            case BUTTONWIZARDSIGN:
                                PatientFormController.addSignature(getFrameOrDialog(), form);
                                for (OrderModel order : form.getOrders()) {
                                    if (order.getOrderForm() != null) {
                                        order.getOrderForm().giveFormRecord(RecordItemReference.SYSTEMSIGNATURE.getRefId()).copyAllFrom(form.giveFormRecord(RecordItemReference.SYSTEMSIGNATURE.getRefId()));
                                    }
                                }
                            case SYSTEMSAVEAS:
                                saveOrders();
                                BaseAction sign = new BaseAction();
                                sign.setActionReference(ActionReference.CANCELSUBMITFORM);
                                getMainMediator().receive(ISEvent.EXECUTEACTION, sign);
                                return true;
                        }
                }
                return false;
            }
        };
    }

    /**
	 * 
	 */
    public void saveOrders() throws Exception {
        List<OrderModel> removeList = new ArrayList<OrderModel>();
        boolean exceptions = false;
        for (OrderModel order : form.getOrders()) {
            try {
                if (order.getOrderForm() != null) {
                    FormModel orderForm = new FormModel();
                    orderForm.copyAllFrom(order.getOrderForm());
                    orderForm.setPatientId(order.getPatientId());
                    orderForm.setVisitId(order.getVisitId());
                    orderForm.giveOrder().copyAllFrom(order);
                    ClinicalServer.store(order.getPatientId(), order.getVisitId(), orderForm);
                    removeList.add(order);
                }
            } catch (Exception ex) {
                exceptions = true;
                Log.exception(ex);
            }
        }
        form.getOrders().removeAll(removeList);
        BaseAction action = new BaseAction();
        action.setActionReference(ActionReference.REFRESHVIEW);
        action.setContextRefId(40037074);
        getMainMediator().receive(ISEvent.EXECUTEACTION, action);
        if (exceptions) {
            throw new ISCancelActionException();
        }
    }

    /**
	 * 
	 */
    @Override
    public ISControlPanel getClientButtonPanel(Component frameOrDialog, FormModel form, ISMediator mediator) throws Exception {
        ApplicationViewModel headerView = SecurityService.getApplicationViewByRefFromCache(ViewReference.BUTTONPANELSIGNCANCEL);
        return new ISControlPanel(mediator, form, headerView, frameOrDialog);
    }

    /**
	 * (non-Javadoc)
	 * @see com.patientis.business.controllers.DefaultCustomController#serverProcessPreSave(com.patientis.model.clinical.FormModel, com.patientis.model.clinical.FormTypeModel, com.patientis.ejb.common.IChainStore, com.patientis.model.common.ServiceCall)
	 */
    @Override
    public void serverProcessPreSave(FormModel form, FormTypeModel formType, IChainStore chain, ServiceCall call) throws Exception {
        throw new ISCancelActionException();
    }
}
