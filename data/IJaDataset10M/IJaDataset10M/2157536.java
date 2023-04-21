package org.helianto.inventory.service;

import java.util.List;
import org.helianto.core.filter.Filter;
import org.helianto.inventory.ProcessAgreement;
import org.helianto.inventory.ProcessRequirement;
import org.helianto.inventory.Tax;

/**
 * Inventory service interface.
 * 
 * @author Mauricio Fernandes de Castro
 */
public interface InventoryMgr {

    /**
	 * Find process requirement.
	 * 
	 * @param filter
	 */
    public List<ProcessRequirement> findProcessRequirements(Filter filter);

    /**
	 * Store agreement.
	 * 
	 * @param requirement
	 */
    public ProcessRequirement storeProcessRequirement(ProcessRequirement requirement);

    /**
	 * Find process agreement.
	 * 
	 * @param filter
	 */
    public List<ProcessAgreement> findProcessAgreement(Filter filter);

    /**
	 * Store process agreement.
	 * 
	 * @param agreement
	 */
    public ProcessAgreement storeProcessAgreement(ProcessAgreement agreement);

    /**
	 * Find taxes.
	 * 
	 * @param filter
	 */
    public List<Tax> findTaxes(Filter filter);

    /**
	 * Store tax.
	 * 
	 * @param tax
	 */
    public Tax storeTax(Tax tax);
}
