package com.patientis.business.clinical;

import java.awt.Component;
import com.patientis.business.controllers.DefaultCustomFormRecordListController;
import com.patientis.business.results.PatientFormRecordListController;
import com.patientis.client.action.BaseAction;
import com.patientis.client.common.PromptsController;
import com.patientis.client.state.State;
import com.patientis.framework.api.services.ClinicalServer;
import com.patientis.framework.api.standard.StandardFormTypeReference;
import com.patientis.framework.api.standard.StandardRecordItemReference;
import com.patientis.framework.controls.ISButton;
import com.patientis.framework.controls.toolbars.ISToolbarButton;
import com.patientis.framework.logging.Log;
import com.patientis.framework.scripting.ISEvent;
import com.patientis.framework.scripting.ServiceUtility;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.clinical.RecordItemModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.patient.PatientModel;
import com.patientis.model.security.ApplicationControlModel;

/**
 * @author gcaulton
 *
 */
public class StandardAllergyListController extends PatientFormRecordListController {

    /**
	 * @see com.patientis.business.controllers.DefaultCustomController#clientReceiveExtendedMessage(com.patientis.framework.scripting.ISEvent, java.lang.Object)
	 */
    @Override
    public boolean clientReceiveExtendedMessage(ISEvent event, Object a) {
        switch(event) {
            case EXECUTEACTION:
                BaseAction action = (BaseAction) a;
                if (action.hasContext(acm.getContextRefId())) {
                    switch(action.getActionReference()) {
                        case SYSTEMUPDATECALCULATION:
                            updateAllergyReviewList(action);
                    }
                }
        }
        return false;
    }

    /**
	 * 
	 */
    public void updateAllergyReviewList(BaseAction action) {
        try {
            FormModel allergyForm = ClinicalServer.getActiveListForm(form.getPatientId(), 0L, StandardFormTypeReference.StandardActiveListPatientAllergies);
            if (allergyForm.isNotNew()) {
                RecordItemModel reviewBy = ClinicalServer.getRecordItemForRecordItemRefId(StandardRecordItemReference.StandardAllergyLastReviewedBy);
                FormRecordModel reviewRec = reviewBy.createFormRecord();
                reviewRec.setValueRef(State.getUser().getUserRef());
                allergyForm.giveFormRecord(reviewBy.getRecordItemRef().getId()).copyAllFrom(reviewRec);
                RecordItemModel reviewDate = ClinicalServer.getRecordItemForRecordItemRefId(StandardRecordItemReference.StandardAllergyLastReviewDate);
                FormRecordModel reviewDateRec = reviewDate.createFormRecord();
                reviewDateRec.setValueDate(DateTimeModel.getNow());
                allergyForm.giveFormRecord(reviewDate.getRecordItemRef().getId()).copyAllFrom(reviewDateRec);
                ClinicalServer.store(allergyForm);
                ISToolbarButton button = (ISToolbarButton) action.getSource();
                button.setText("Reviewed " + DateTimeModel.getNow().toString(DateTimeModel.getDefaultTimeAMPMFormat()));
                button.setEnabled(false);
            } else {
                PromptsController.warning("You must enter allergies or No Known Allergies", "Allergies not found");
            }
        } catch (Exception ex) {
            Log.exception(ex);
        }
    }

    /**
	 * 
	 * @param action
	 * @throws Exception
	 */
    public void removeActivelistRecord(Component frameOrDialog, final BaseAction action, final PatientModel patient) throws Exception {
        if (selectedRecords.size() > 0) {
            String reason = PromptsController.getInput(frameOrDialog, "Enter the reason for removing the allergy from this profile");
            if (Converter.isNotEmpty(reason)) {
                ApplicationControlModel acm = ServiceUtility.getApplicationControl(action);
                ServiceUtility.removeActivelistRecords(acm, frameOrDialog, selectedRecords, action, patient);
            }
        }
    }
}
