package org.quantumleaphealth.screen;

import static org.quantumleaphealth.ontology.BreastCancerCharacteristicCodes.BILATERAL_SYNCHRONOUS;
import static org.quantumleaphealth.ontology.BreastCancerCharacteristicCodes.ANATOMIC_SITE;
import static org.quantumleaphealth.ontology.BreastCancerCharacteristicCodes.LEFT_BREAST;
import static org.quantumleaphealth.ontology.BreastCancerCharacteristicCodes.RIGHT_BREAST;
import org.quantumleaphealth.model.patient.Diagnosis;
import org.quantumleaphealth.model.patient.PatientHistory;
import org.quantumleaphealth.model.trial.BooleanCriterion;
import org.quantumleaphealth.ontology.CharacteristicCode;

/**
 * Match to whether the patient's <code>diagnoses</code> list
 * contains bilateral entries.
 * @author Tom Bechtold
 * @version 2008-07-07
 */
public class BilateralBooleanMatchable extends BooleanMatchable {

    /**
     * Store instance variables
     * @param booleanCriterion the criterion
     * @throws IllegalArgumentException if parameter is <code>null</code> or not bilateral synchronous
     */
    public BilateralBooleanMatchable(BooleanCriterion booleanCriterion) throws IllegalArgumentException {
        super(booleanCriterion);
        if (!BILATERAL_SYNCHRONOUS.equals(booleanCriterion.getCharacteristicCode())) throw new IllegalArgumentException("Criterion not bilateral synchronous: " + booleanCriterion);
    }

    /**
     * @return whether both left and right breast diagnoses are stored in the patient's history
     * @param history the patient's history
     * @see org.quantumleaphealth.screen.BooleanMatchable#isCharacteristic(org.quantumleaphealth.model.patient.PatientHistory)
     */
    @Override
    protected boolean isCharacteristic(PatientHistory history) {
        boolean left = false;
        boolean right = false;
        for (Diagnosis diagnosis : history.getDiagnoses()) {
            CharacteristicCode location = diagnosis.getValueHistory().getValue(ANATOMIC_SITE);
            left = left || LEFT_BREAST.equals(location);
            right = right || RIGHT_BREAST.equals(location);
        }
        return left && right;
    }

    /**
     * Version UID for serializable class
     */
    private static final long serialVersionUID = -7834964956988452757L;
}
