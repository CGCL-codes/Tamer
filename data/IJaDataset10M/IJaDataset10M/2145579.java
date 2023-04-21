package com.patientis.business.shipping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.patientis.business.shipping.exception.EndicaException;
import com.patientis.client.service.clinical.ClinicalService;
import com.patientis.client.service.reference.ReferenceService;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.clinical.RecordItemModel;
import endica.lable.service.ChangePassPhraseRequest;
import endica.lable.service.ChangePassPhraseRequestResponse;

/**
 * When a new Endica accont is created the user is sent an email
 * with a temporary pass phrase.  As part of the USPS/Endica security
 * framework this pass phrase MUST be chages using the ChangePassPraseRequest
 * service provided by Endica.  This client allows that passphrase to be changed
 * 
 * @author ilink
 *
 */
public class ChangePassPhraseClient extends BaseClient {

    /**
	 * Call the Endica service to change the passphrase of the system account
	 * 
	 * @param form - The form containing the new passphrase
	 * @throws EndicaException - Thrown if the update fails for any reason
	 */
    public static void changePassPhrase(FormModel form) throws EndicaException {
        String newPhrase = null;
        try {
            newPhrase = form.getStringValueForRecordItem(ReferenceService.getRefId("RecordItem", "ENDICALABELSERVICENEWPASSPHRASE(SYSTEMENDICA)"));
        } catch (Exception e) {
            throw new EndicaException("Unable to retrieve the new passphrase from the form");
        }
        ChangePassPhraseRequest request = new ChangePassPhraseRequest();
        request.setCertifiedIntermediary(ClientHelper.getIntermediary());
        request.setNewPassPhrase(newPhrase);
        request.setRequesterID(ClientHelper.getRequesterId());
        request.setRequestID(ClientHelper.getUniqueRequestId());
        ChangePassPhraseRequestResponse response = sendRequest().changePassPhrase(request);
        if (response.getStatus() != 0) throw new EndicaException(response.getErrorMessage() + ".  Error code: " + response.getStatus());
    }

    public static void main(String[] args) {
        try {
            FormModel form = ClinicalService.prepareNewForm(50007748);
            List<Long> recordItemRefIds = ClinicalService.getRecordItemRefIdsByFormType(form.getFormTypeRefId());
            Set<FormRecordModel> records = new HashSet<FormRecordModel>();
            System.out.println("size: " + recordItemRefIds.size());
            for (int i = 0; i < recordItemRefIds.size(); i++) {
                RecordItemModel recordItem = ClinicalService.getRecordItemForRecordItemRefId(recordItemRefIds.get(i));
                FormRecordModel record = recordItem.createFormRecord();
                record.setValueString("NEWpassTEST");
                records.add(record);
            }
            form.setRecords(records);
            ChangePassPhraseClient.changePassPhrase(form);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
