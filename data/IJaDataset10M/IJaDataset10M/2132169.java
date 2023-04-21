package org.openedc.core.domain.business;

import java.util.Date;
import java.util.List;
import org.openedc.core.domain.model.Accrual;
import org.openedc.core.domain.model.PatientEligibilityForm;
import org.openedc.core.domain.model.PatientIdentificationForm;
import org.openedc.core.domain.model.User;

/**
 *
 * @author openedc
 */
public interface CompletePatientRegistrationUseCase {

    Accrual startRegistration(User user) throws Exception;

    PatientIdentificationForm getInitializedPatientIdentificationForm(User user, Accrual accrual, String studyIdentifier, String institutionIdentifier, Date dateOfBirth) throws Exception;

    List<String> validatePatientIdentificationForm(PatientIdentificationForm patientIdentificationForm) throws Exception;

    PatientEligibilityForm getIntializedPatientEligibilityForm(User user, Accrual accrual) throws Exception;

    List<String> validatePatientEligibilityForm(User user, PatientEligibilityForm patientEligibilityForm) throws Exception;

    void completeRegistration(User user, Accrual accrual, PatientIdentificationForm patientIdentificationForm, PatientEligibilityForm patientEligibilityForm) throws Exception;
}
