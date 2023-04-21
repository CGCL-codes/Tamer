package com.patientis.business.med.transactions;

import java.util.List;
import com.patientis.ejb.common.IChainStore;
import com.patientis.model.scheduling.FrequencyModel;
import com.patientis.model.common.ServiceCall;
import com.patientis.model.common.IdentifierModel;
import com.patientis.model.interfaces.InterfaceTransactionModel;
import com.patientis.model.med.MedItemModel;
import com.patientis.model.med.MedModel;

/**
 * IServiceOrderProcessing is an interface for the support of order processing.
 *
 * Design Patterns: <a href="/functionality/rm/1000049.html">Order Processing</a>
 * <br/>  
 */
public interface IServiceMedProcessing {

    /**
	 * Get the frequency values for the schedule frequency.
	 * 
	 * @param scheduleFrequencyRefId schedulefrequency
	 * @return frequency
	 * @throws Exception
	 */
    public FrequencyModel getFrequencyByRef(long scheduleFrequencyRefId, ServiceCall call) throws Exception;

    /**
	 * Store a new interface transaction.
	 * 
	 * @param transaction model
	 * @param chain scope of transaction
	 * @param call service call 
	 * @return new interface transaction id
	 * @throws Exception
	 */
    public Long storeInterfaceTransaction(final InterfaceTransactionModel transaction, final IChainStore chain, final ServiceCall call) throws Exception;

    /**
	 * Adds the medication to the database if medication.isNew()
	 * returning the generated id.<br>
	 * Otherwise updates the medication effectively replacing with 
	 * this medication.  Throws an error if the medication is not found.<br>
	 * Commits transaction immediately.
	 * 
	 * @param medication medication model to be inserted or updated
	 * @param call service call
	 * @return medication id unique identifier for the medication
	 * @throws Exception
	 */
    public Long store(final MedItemModel medication, final IChainStore chain, final ServiceCall call) throws Exception;

    /**
	 * Adds the medication to the database if medication.isNew()
	 * returning the generated id.<br>
	 * Otherwise updates the medication effectively replacing with 
	 * this medication.  Throws an error if the medication is not found.<br>
	 * Commits transaction immediately.
	 * 
	 * @param medication medication model to be inserted or updated
	 * @param call service call
	 * @return medication id unique identifier for the medication
	 * @throws Exception
	 */
    public Long store(final MedModel medication, final IChainStore chain, final ServiceCall call) throws Exception;

    /**
	 * Execute a search using the med item model to define the criteria.
	 * 
	 * @param criteria med item search criteria
	 * @param call service call with context
	 * @return list of matching med items
	 * @throws Exception
	 */
    public List<MedModel> getMeds(final MedModel criteria, final ServiceCall call) throws Exception;

    /**
	 * Get the med items with the matching identifier
	 * 
	 * @param identifier
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public List<MedItemModel> getMedItems(final IdentifierModel identifier, final ServiceCall call) throws Exception;
}
