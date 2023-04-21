package com.patientis.business.shipping;

import com.patientis.business.shipping.exception.EndicaException;
import com.patientis.client.service.common.BaseService;
import com.patientis.data.clinical.ClinicalData;
import com.patientis.data.common.BaseData;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.common.ServiceCall;
import endica.lable.service.PostageRateRequest;
import endica.lable.service.PostageRateResponse;
import endica.lable.service.SpecialServices;

/**
 * This class allows a user to get the cost of postage 
 * before requesting a label and their account being
 * charged
 * 
 * @author ilink
 *
 */
public class PostageRateClient extends BaseClient {

    /**
	 * Calls the endica PostageRateRequest service with the criteria
	 * supplied by the user
	 * 
	 * @param form - The system form containing the postage details to calculate the postage for
	 * @return  - The cost of postage
	 * @throws  - EndicaException This exception is thrown when the postage
	 * rate can not be calculated according to the supplied criteria.  The 
	 * exception message will give a clear reason for the failure
	 */
    public static String calculatePostageRate(FormModel form) throws EndicaException {
        PostageRateRequest request = new PostageRateRequest();
        request.setRequesterID(ClientHelper.getRequesterId());
        request.setCertifiedIntermediary(ClientHelper.getIntermediary());
        try {
            ServiceCall call = BaseService.createServiceCall();
            request.setMailClass(ClinicalData.getTermDisplay(form.getTermIdValueForRecordItem(BaseData.getReference().getRefId("RecordItem", "ENDICALABELSERVICECREATELABELMAILCLASS(SYSTEMENDICA)", call)), call));
            request.setWeightOz(Double.valueOf(form.getStringValueForRecordItem(BaseData.getReference().getRefId("RecordItem", "ENDICALABELSERVICECREATELABELWEIGHT(SYSTEMENDICA)", call))));
            request.setMailpieceShape(ClinicalData.getTermDisplay(form.getTermIdValueForRecordItem(BaseData.getReference().getRefId("RecordItem", "ENDICALABELSERVICECREATELABELSHAPE(SYSTEMENDICA)", call)), call));
            request.setToPostalCode(form.getStringValueForRecordItem(BaseData.getReference().getRefId("RecordItem", "ENDICALABELSERVICECREATELABELTOPOSTALCODE(SYSTEMENDICA)", call)));
            request.setFromPostalCode(form.getStringValueForRecordItem(BaseData.getReference().getRefId("RecordItem", "ENDICALABELSERVICECREATELABELFROMPOSTALCODE(SYSTEMENDICA)", call)));
            SpecialServices services = new SpecialServices();
            services.setSignatureConfirmation(ClinicalData.getTermDisplay(form.getTermIdValueForRecordItem(BaseData.getReference().getRefId("RecordItem", "ENDICALABELSERVICECREATELABELSIGNATURECONFIRMATION(SYSTEMENDICA)", call)), call));
            services.setCertifiedMail(ClinicalData.getTermDisplay(form.getTermIdValueForRecordItem(BaseData.getReference().getRefId("RecordItem", "ENDICALABELSERVICECREATELABELCERTIFIEDMAIL(SYSTEMENDICA)", call)), call));
            services.setDeliveryConfirmation(ClinicalData.getTermDisplay(form.getTermIdValueForRecordItem(BaseData.getReference().getRefId("RecordItem", "ENDICALABELSERVICECREATELABELDELIVERYCONFIRMATION(SYSTEMENDICA)", call)), call));
            services.setRegisteredMail(ClinicalData.getTermDisplay(form.getTermIdValueForRecordItem(BaseData.getReference().getRefId("RecordItem", "ENDICALABELSERVICECREATELABELREGISTEREDMAIL(SYSTEMENDICA)", call)), call));
            services.setReturnReceipt(ClinicalData.getTermDisplay(form.getTermIdValueForRecordItem(BaseData.getReference().getRefId("RecordItem", "ENDICALABELSERVICECREATELABELRETURNRECEIPT(SYSTEMENDICA)", call)), call));
            request.setServices(services);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EndicaException("Postage could not be calculated.  There was an error retrieving the criteria values from the form");
        }
        PostageRateResponse response = (PostageRateResponse) sendRequest().calculatePostageRate(request);
        if (response.getStatus() == 0) {
            return String.valueOf(response.getPostage().get(0).getRate());
        } else {
            throw new EndicaException(response.getErrorMessage() + ".  Error code: " + response.getStatus());
        }
    }
}
