package com.patientis.ejb.billing;

import java.util.List;
import javax.ejb.Local;
import com.patientis.ejb.common.IChainStore;
import com.patientis.model.clinical.FormModel;
import com.patientis.model.clinical.FormRecordModel;
import com.patientis.model.clinical.TermChargeModel;
import com.patientis.model.clinical.TermModel;
import com.patientis.model.common.DateTimeModel;
import com.patientis.model.common.ServiceCall;
import com.patientis.model.billing.ChargeItemModel;
import com.patientis.model.billing.ChargeAlgorithmModel;
import com.patientis.model.billing.ChargeModel;
import com.patientis.model.billing.FacilityAccountModel;
import com.patientis.model.billing.FeeScheduleModel;
import com.patientis.model.billing.InvoiceFormatModel;
import com.patientis.model.billing.InvoiceModel;
import com.patientis.model.billing.InvoiceTypeModel;

/**
 * PatientBean is a controller for all business logic and a facade for data access
 *
 * Design Patterns: <a href="/functionality/rm/1000067.html">Service Beans</a>
 * <br/>
 */
@Local
public interface IBillingLocal {

    /**
	 * Save the ChargeItemModel model creating a new chargeItem or updating existing rows.
	 * 
	 * @param chargeItem ChargeItem
	 * @param beforeChargeItem chargeItem model before changes or null if new chargeItem
	 * @param call service call
	 * @return new or existing chargeItem id
	 * @throws Exception
	 */
    public Long store(final ChargeItemModel chargeItem, final ServiceCall call) throws Exception;

    /**
	 * Save the ChargeItemModel model creating a new chargeItem or updating existing rows.
	 * 
	 * @param chargeItem ChargeItem
	 * @param beforeChargeItem chargeItem model before changes or null if new chargeItem
	 * @param call service call
	 * @return new or existing chargeItem id
	 * @throws Exception
	 */
    public Long store(final ChargeAlgorithmModel chargeModifier, final ServiceCall call) throws Exception;

    /**
	 * Return a list of chargeItems for the valued chargeCode.
	 * @return chargeItems
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ChargeItemModel> getChargeItemsByChargeCode(final String chargeCode, final ServiceCall call) throws Exception;

    /**
	 * Save the ChargeModel model creating a new charge or updating existing rows.
	 * 
	 * @param charge Charge
	 * @param beforeCharge charge model before changes or null if new charge
	 * @param call service call
	 * @return new or existing charge id
	 * @throws Exception
	 */
    public Long store(final ChargeModel charge, final ServiceCall call) throws Exception;

    /**
	 * Return a list of charges for the valued patientId.
	 * @return charges
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ChargeModel> getChargesByPatientId(final long patientId, final ServiceCall call) throws Exception;

    /**
	 * Return a list of chargeAlgorithms for the valued chargeAlgorithmRefId.
	 * @return chargeAlgorithms
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ChargeAlgorithmModel> getChargeAlgorithmsByChargeAlgorithmRefId(final long chargeAlgorithmRefId, final ServiceCall call) throws Exception;

    /**
	 * 
	 * @param startDt
	 * @param stopdt
	 * @param patientId
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<ChargeModel> getChargesByDatePatient(final DateTimeModel startDt, final DateTimeModel stopdt, final long patientId, final ServiceCall call) throws Exception;

    /**
	 * Return a list of recordItems for the valued recordItemRefId.
	 * @return recordItems
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ChargeItemModel> getMatchingChargeItems(final String match, final ServiceCall call) throws Exception;

    /**
	 * Save the InvoiceFormatModel model creating a new invoiceFormat or updating existing rows.
	 * 
	 * @param invoiceFormat InvoiceFormat
	 * @param beforeInvoiceFormat invoiceFormat model before changes or null if new invoiceFormat
	 * @param call service call
	 * @return new or existing invoiceFormat id
	 * @throws Exception
	 */
    public Long store(final InvoiceFormatModel invoiceFormat, final ServiceCall call) throws Exception;

    /**
	 * Save the InvoiceModel model creating a new invoice or updating existing rows.
	 * 
	 * @param invoice Invoice
	 * @param beforeInvoice invoice model before changes or null if new invoice
	 * @param call service call
	 * @return new or existing invoice id
	 * @throws Exception
	 */
    public Long store(final InvoiceModel invoice, final ServiceCall call) throws Exception;

    /**
	 * Save the FacilityAccountModel model creating a new facilityAccount or updating existing rows.
	 * 
	 * @param facilityAccount FacilityAccount
	 * @param beforeFacilityAccount facilityAccount model before changes or null if new facilityAccount
	 * @param call service call
	 * @return new or existing facilityAccount id
	 * @throws Exception
	 */
    public Long store(final FacilityAccountModel facilityAccount, final ServiceCall call) throws Exception;

    /**
	 * Save the FeeScheduleModel model creating a new feeSchedule or updating existing rows.
	 * 
	 * @param feeSchedule FeeSchedule
	 * @param beforeFeeSchedule feeSchedule model before changes or null if new feeSchedule
	 * @param call service call
	 * @return new or existing feeSchedule id
	 * @throws Exception
	 */
    public Long store(final FeeScheduleModel feeSchedule, final ServiceCall call) throws Exception;

    /**
	 * Add charge - do not fail on error
	 * 
	 * @param value
	 * @param form
	 * @param patient
	 * @return
	 */
    public ChargeModel prepareNewCharge(final TermModel term, final FormRecordModel record, final long chargeItemId, final double quantity, final double chargeAmount, final long patientId, final long visitId, final FeeScheduleModel feeSchedule, final IChainStore chain, final ServiceCall call) throws Exception;

    /**
	 * Return a list of charges for the valued formRecordId.
	 * @return charges
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ChargeModel> getChargesByFormRecordId(final long formRecordId, final ServiceCall call) throws Exception;

    /**
	 * Return a list of charges for the valued visitId.
	 * @return charges
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ChargeModel> getChargesByVisitId(final long visitId, final ServiceCall call) throws Exception;

    /**
	 * Return the single facilityAccount model for the primary key.
	 * 
	 * @param id facilityAccount id
	 * @param continueSession
	 * @return facilityAccount model
	 * @throws Exception if facilityAccount not found
	 */
    public FacilityAccountModel getFacilityAccount(final long facilityAccountId, final ServiceCall call) throws Exception;

    /**
	 * Return the facilityAccount for the valued accountNumber.
	 * 
	 * @return facilityAccounts
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<FacilityAccountModel> getFacilityAccounts(final ServiceCall call) throws Exception;

    /**
	 * Return a list of invoices for the valued billPatientId.
	 * 
	 * @return invoices
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<InvoiceModel> getInvoicesByBillPatientId(final long billPatientId, final ServiceCall call) throws Exception;

    /**
	 * Return a list of invoices for the valued patientAccountId.
	 * @return invoices
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<InvoiceModel> getInvoicesByPatientVisitId(final long patientAccountId, final ServiceCall call) throws Exception;

    /**
	 * 
	 * @param wizard
	 * @param form
	 * @param patient
	 * @return
	 * @throws Exception
	 */
    public List<Long> storeFormCharges(final long patientId, final long visitId, final FormModel form, final IChainStore chain, final ServiceCall call) throws Exception;

    /**
	 * 
	 * @param accountId
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public InvoiceModel prepareNewInvoice(final long visitId, final long payerProgramRefId, final ServiceCall call) throws Exception;

    /**
	 * Return a list of charges for the valued accountId.
	 * 
	 * @return charges
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<Long> getPayerProgramRefIdsByVisitPayerStatus(final long visitId, final long chargeStatusRefId, final ServiceCall call) throws Exception;

    /**
	 * Return the single chargeItem model for the primary key.
	 * 
	 * @param id chargeItem id
	 * @param continueSession
	 * @return chargeItem model
	 * @throws Exception if chargeItem not found
	 */
    public ChargeItemModel getChargeItem(final long chargeItemId, final ServiceCall call) throws Exception;

    /**
	 * Return a list of charges for the valued formId.
	 * @return charges
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ChargeModel> getChargesByFormId(final long formId, final ServiceCall call) throws Exception;

    /**
	 * 
	 * @param patientFilterForm
	 * @return
	 * @throws Exception
	 */
    public List<ChargeModel> getChargesByFilter(final FormModel messageFilterForm, final ServiceCall call) throws Exception;

    /**
	 * 
	 * @param patientFilterForm
	 * @return
	 * @throws Exception
	 */
    public List<InvoiceModel> getInvoicesByFilter(final FormModel messageFilterForm, final ServiceCall call) throws Exception;

    /**
	 * 
	 * @param formFolderRefIds
	 * @param includeChildFolders
	 * @param call
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public List<ChargeItemModel> getChargeItemsByFormFolderHierarchy(final List<Long> formFolderRefIds, final boolean includeChildFolders, final ServiceCall call) throws Exception;

    /**
	 * Save the InvoiceTypeModel model creating a new invoiceType or updating existing rows.
	 * 
	 * @param invoiceType InvoiceType
	 * @param beforeInvoiceType invoiceType model before changes or null if new invoiceType
	 * @param call service call
	 * @return new or existing invoiceType id
	 * @throws Exception
	 */
    public Long store(final InvoiceTypeModel invoiceType, final ServiceCall call) throws Exception;

    /**
	 * Return a invoiceType for the valued invoiceTypeRefId.
	 * @return invoiceTypes
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public InvoiceTypeModel getInvoiceTypeForInvoiceTypeRefId(final long invoiceTypeRefId, final ServiceCall call) throws Exception;

    /**
	 * Return a chargeItem for the valued chargeItemTextIndex.
	 * @return chargeItems
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public ChargeItemModel getChargeItemForChargeItemTextIndex(final String chargeItemTextIndex, final ServiceCall call) throws Exception;

    /**
	 * Return a feeSchedule for the valued feeScheduleRefId.
	 * @return feeSchedules
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
    public FeeScheduleModel getFeeScheduleForFeeScheduleRefId(final long feeScheduleRefId, final ServiceCall call) throws Exception;
}
