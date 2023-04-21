package com.patientis.business.plugins;

import com.patientis.business.controllers.DefaultCustomController;
import com.patientis.data.common.BaseData;
import com.patientis.ejb.common.IChainStore;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormTypeModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.common.Converter;
import com.patientis.model.common.ServiceCall;
import com.patientis.model.reference.RefModel;
import java.util.ArrayList;
import javax.naming.NamingException;

/**
 * @author wfacey
 *
 */
public class EHADTController extends DefaultCustomController {

    /**
	 * FormTypeRefId for Ernest ADT Diagnoses 
	 */
    private static final long EH_ADT_PATIENT_FORM_TYPE = 50001569L;

    private static final String EH_ADT_DIAGNOSIS_RECORD_ITEM_PREFIX = "DIAGNOSIS";

    private static final String EH_ADT_DIAGNOSIS_RANK_RECORD_ITEM = "DIAGNOSISRANK";

    private static final String EH_ADT_DIAGNOSIS_CODE_RECORD_ITEM = "DIAGNOSISCODEIDENTIFIER";

    private static final String EH_ADT_DIAGNOSIS_TERM_RECORD_ITEM = "DIAGNOSISTERM";

    /**
	 * 
	 */
    public EHADTController() {
    }

    private static boolean isADTPatientInfoFormType(FormTypeModel formType) {
        return (formType.getFormTypeRefId() == EH_ADT_PATIENT_FORM_TYPE);
    }

    private static boolean isRecordItem(FormRecordModel record, String recordItemPrefix) {
        boolean recordItem = false;
        if (record.getRecordItemRef() != null && Converter.isNotEmpty(record.getRecordItemRef().getIdvalue())) {
            recordItem = RefModel.getDefaultRefKey(record.getRecordItemRef().getIdvalue()).startsWith(recordItemPrefix);
        }
        return recordItem;
    }

    public static boolean isDiagnosisRecordItem(FormRecordModel record) {
        return isRecordItem(record, EH_ADT_DIAGNOSIS_RECORD_ITEM_PREFIX);
    }

    public static boolean isDiagnosisRankRecordItem(FormRecordModel record) {
        return isRecordItem(record, EH_ADT_DIAGNOSIS_RANK_RECORD_ITEM);
    }

    public static boolean isDiagnosisCodeRecordItem(FormRecordModel record) {
        return isRecordItem(record, EH_ADT_DIAGNOSIS_CODE_RECORD_ITEM);
    }

    public static boolean isDiagnosisTermRecordItem(FormRecordModel record) {
        return isRecordItem(record, EH_ADT_DIAGNOSIS_TERM_RECORD_ITEM);
    }

    /**
	 * @see com.patientis.business.common.ICustomController#serverProcessPreSave(com.patientis.model.clinical.FormModel, com.patientis.model.clinical.FormTypeModel, com.patientis.ejb.common.IChainStore, com.patientis.model.common.ServiceCall)
	 * This method will remove all outdated diagnosis records and merge the most recent form records with the current form records.
	 * Forms of type Ernest Patient Chart Demographics View may have duplicate form record diagnosis codes.
	 * Forms of type Ernest Patient Chart Demographics View have only the updated data and must be merged with the existing form.
	 */
    @Override
    public void serverProcessPreSave(FormModel form, FormTypeModel formType, IChainStore chain, ServiceCall call) throws Exception {
        call.debug("serverProcessPreSave called");
        if (isADTPatientInfoFormType(formType)) {
            call.debug("form record count is " + form.getRecords().size());
            removeOutdatedDiagnosisRecords(form, call);
            call.debug("form record count after removeOutdatedDiagnosisRecords is " + form.getRecords().size());
            formatDiagnosisCode(form, call);
            mergeFormByVisitOrPatient(form, formType, call);
        }
    }

    /**
	 * @param form
	 * @param formType
	 * @param call
	 * @throws Exception
	 * @throws NamingException
	 */
    private void mergeFormByVisitOrPatient(FormModel form, FormTypeModel formType, ServiceCall call) throws Exception, NamingException {
        FormModel storedForm = null;
        formDebugInfo(form, call);
        if (form.getVisitId() > 0L) {
            storedForm = BaseData.getClinical().getMostRecentFormForVisitFormType(form.getVisitId(), formType.getFormTypeRefId(), false, call);
        } else if (form.getPatientId() > 0L) {
            storedForm = BaseData.getClinical().getMostRecentForPatientFormType(form.getPatientId(), formType.getFormTypeRefId(), call);
        }
        if (storedForm != null && storedForm.isNotNew()) {
            formDebugInfo(storedForm, call);
            storedForm.mergeKeepInsertInfo(storedForm.getRecords(), form.getRecords(), false);
            formDebugInfo(storedForm, call);
            form.clear();
            form.copyAllFrom(storedForm);
            formDebugInfo(form, call);
        }
    }

    /**
	 * @param form
	 * @param call
	 */
    private void formDebugInfo(FormModel form, ServiceCall call) {
        call.debug("form " + form.getId() + " patient " + form.getPatientId() + " visit " + form.getVisitId());
        for (FormRecordModel frm : form.getRecords()) {
            System.out.print(frm.getValue() + ":FormId(" + frm.getFormId() + "):Form Type(" + form.getFormTypeRefId() + "):PatientId(" + frm.getPatientId() + "):VisitId(" + frm.getVisitId() + ")");
        }
    }

    /**
	 * Forms of type Ernest ADT Diagnoses may have duplicate form record diagnosis codes with updated data on the newer records.
	 * This method will remove all previous records for the code based on the Rank. I.e. The record with the maximum Rank will remain.
	 * 1. The form type has been confirmed by the calling method.
	 * 2. Get all outdated diagnosis form records.
	 * 3. Remove all outdated records from the form  
	 * 4. Reset all term sequence values 
	 * 
	 * @param form
	 * @param call
	 */
    private void removeOutdatedDiagnosisRecords(FormModel form, ServiceCall call) {
        ArrayList<FormRecordModel> outdatedRecords = new ArrayList<FormRecordModel>();
        for (FormRecordModel record : form.getRecords()) {
            if (isDiagnosisRecordItem(record) && (!form.isMostRecentRecordSequence(record))) {
                outdatedRecords.add(record);
            }
        }
        call.debug("outdatedRecords count is " + outdatedRecords.size());
        if (outdatedRecords.size() > 0) {
            form.getRecords().removeAll(outdatedRecords);
        }
        for (FormRecordModel record : form.getRecords()) {
            if (isDiagnosisRecordItem(record)) {
                record.setTermSequence(0);
            }
        }
    }

    private static void formatDiagnosisCode(FormModel form, ServiceCall call) {
        for (FormRecordModel record : form.getRecords()) {
            if (isDiagnosisCodeRecordItem(record)) {
                record.setValueString(formatDiagnosisCode(record.getValueString()));
            } else if (isDiagnosisTermRecordItem(record)) {
                record.setTermIdValue(formatDiagnosisCode(record.getTermIdValue()));
            }
        }
    }

    private static String formatDiagnosisCode(String code) {
        String formattedCode = code;
        int VCODE_LENGTH = 3;
        int ECODE_LENGTH = 4;
        int DISEASECODE_LENGTH = 3;
        if (Converter.isNotEmpty(code)) {
            if (code.startsWith("V") && code.length() > VCODE_LENGTH && !code.contains(".") && !code.contains(" ")) {
                formattedCode = String.format("%s.%s", code.substring(0, VCODE_LENGTH), code.substring(VCODE_LENGTH));
            } else if (code.startsWith("E") && code.length() > ECODE_LENGTH && !code.contains(".") && !code.contains(" ")) {
                formattedCode = String.format("%s.%s", code.substring(0, ECODE_LENGTH), code.substring(ECODE_LENGTH));
            } else if (Character.isDigit(code.charAt(0)) && code.length() > DISEASECODE_LENGTH && !code.contains(".") && !code.contains(" ")) {
                formattedCode = String.format("%s.%s", code.substring(0, DISEASECODE_LENGTH), code.substring(DISEASECODE_LENGTH));
            }
        }
        return formattedCode;
    }

    public static String formatDiagnosisCodeTest(String code) {
        return formatDiagnosisCode(code);
    }
}
