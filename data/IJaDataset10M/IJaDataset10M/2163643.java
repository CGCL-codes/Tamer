package com.patientis.business.patient;

import java.util.List;
import com.patientis.business.controllers.DefaultCustomController;
import com.patientis.business.security.Authentication;
import com.patientis.data.common.BaseData;
import com.patientis.ejb.common.IChainStore;
import com.patientis.framework.controls.exceptions.ISCancelActionException;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.clinical.FormTypeModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DisplayModel;
import com.patientis.model.common.ServiceCall;
import com.patientis.model.patient.PortalUserModel;
import com.patientis.model.reference.RecordItemReference;

/**
 * @author gcaulton
 *
 */
public class MessageFormProcessor extends DefaultCustomController {

    /**
	 * @see com.patientis.model.common.ICustomProcessor#process(com.patientis.model.clinical.FormModel, com.patientis.model.clinical.FormTypeModel)
	 */
    @Override
    public void serverProcessPreSave(FormModel form, FormTypeModel formType, IChainStore chain, ServiceCall call) throws Exception {
        FormRecordModel subject = form.giveFormRecord(RecordItemReference.SYSTEMPATIENTMESSAGESUBJECT.getRefId());
        if (Converter.isNotEmpty(subject.getValueString())) {
            form.setTitle(subject.getValueString());
        }
    }
}
