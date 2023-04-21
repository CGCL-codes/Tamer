package com.patientis.pluginserver.patient;

import com.patientis.ejb.common.IChainStore;
import com.patientis.model.common.ServiceCall;
import com.patientis.model.patient.PatientModel;
import com.patientis.model.xml.PatientTransaction;

/**
 * One line class description
 *
 * 
 *   
 */
public interface IStorePatient {

    /**
	 * Store the result transaction
	 * 
	 * @param transaction
	 * @param chain
	 * @param call
	 * @return
	 * @throws Exception
	 */
    public PatientModel store(final PatientTransaction transaction, final IChainStore chain, final ServiceCall call) throws Exception;
}
