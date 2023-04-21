package com.patientis.business.phone;

import java.awt.Component;
import java.sql.Timestamp;
import java.util.List;
import javax.swing.JComponent;
import com.patientis.business.controllers.DefaultCustomController;
import com.patientis.client.action.BaseAction;
import com.patientis.data.common.ISParameter;
import com.patientis.framework.api.services.ReferenceServer;
import com.patientis.framework.api.standard.StandardRecordItemReference;
import com.patientis.framework.controls.IComponent;
import com.patientis.framework.controls.ISControlPanel;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.IReceiveMessage;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.framework.scripting.ISMediator;
import com.patientis.framework.utility.SwingUtil;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.clinical.FormTypeModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.reference.FormStatusReference;
import com.patientis.model.reference.ValueDataTypeReference;
import com.patientis.model.security.ApplicationControlModel;

/**
 * @author gcaulton
 *
 */
public class StandardPatientPhoneCallAddendum extends DefaultCustomController {

    private static long recordItemRefId = 40037055;

    private IComponent addendumComponent = null;

    private FormModel form = null;

    /**
	 * 
	 */
    @Override
    public void clientInitializeControl(IBaseModel model, ISControlPanel formDetailsControlPanel, Component c, ApplicationControlModel acm) throws Exception {
        try {
            if (model instanceof FormModel) {
                addendumComponent = (IComponent) SwingUtil.getTextComponent((JComponent) c);
                form = (FormModel) model;
            }
        } catch (Exception ex) {
            Log.exception(ex);
        }
    }

    /**
	 * @see com.patientis.client.common.BaseController#mediateMessages()
	 */
    public IReceiveMessage clientReceiveMessage() {
        return new IReceiveMessage() {

            public boolean receive(ISEvent event, Object value) throws Exception {
                switch(event) {
                    case EXECUTEACTION:
                        BaseAction action = (BaseAction) value;
                        switch(action.getActionReference()) {
                            case SYSTEMFORMPREPARINGSAVE:
                                preSave(form);
                                return false;
                        }
                }
                return false;
            }
        };
    }

    public void preSave(FormModel form) throws Exception {
        if (addendumComponent != null) {
            String addendum = Converter.convertDisplayString(addendumComponent.getValue());
            if (Converter.isNotEmpty(addendum)) {
                int nextseq = form.getMaxRecordSequence() + 1;
                FormRecordModel addendumRecord = new FormRecordModel();
                addendumRecord.setDataTypeRef(new DisplayModel(ValueDataTypeReference.STRING.getRefId()));
                addendumRecord.setRecordItemRef(new DisplayModel(recordItemRefId));
                addendumRecord.setValueString(addendum);
                addendumRecord.setRecordSequence(nextseq);
                form.getRecords().add(addendumRecord);
            }
        }
    }
}
