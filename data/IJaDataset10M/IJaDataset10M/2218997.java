package com.patientis.model.patient;

import com.patientis.framework.controls.exceptions.ISValidateControlException;
import com.patientis.model.common.Converter;

/**
 * PatientProvider
 * 
 */
public class PatientProviderModel extends PatientProviderDataModel {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public PatientProviderModel() {
    }

    /**
	 * @see com.patientis.model.patient.PatientProviderDataModel#validateDataModel()
	 */
    @Override
    public void validateDataModel() throws ISValidateControlException {
        super.validateDataModel();
        if (Converter.isDisplayEmpty(getProviderRef())) {
            throw new ProviderMissingException();
        }
    }
}
