package org.quantumleaphealth.screen.test;

import org.quantumleaphealth.model.patient.Diagnosis;
import org.quantumleaphealth.model.patient.PatientHistory;
import org.quantumleaphealth.ontology.CharacteristicCode;
import static org.quantumleaphealth.ontology.BreastCancerCharacteristicCodes.ANATOMIC_SITE;
import static org.quantumleaphealth.ontology.BreastCancerCharacteristicCodes.BREAST_CANCER;
import static org.quantumleaphealth.ontology.BreastCancerCharacteristicCodes.METASTATIC;
import org.quantumleaphealth.screen.DiagnosisMatchable;
import org.testng.annotations.Test;

/**
 * Tests <code>DiagnosisMatchable</code>
 * @author Tom Bechtold
 * @version 2008-07-08
 * @see DiagnosisMatchable
 */
@Test(description = "Diagnosis matchable abstract", groups = { "screen", "composite" })
public class TestDiagnosisMatchable extends TestUtils {

    /**
     * Test GetDiagnosis
     */
    public void testGetDiagnosis() {
        int[] unique = getUniqueIntegers(2);
        PatientHistory patientHistory = new PatientHistory();
        assert DiagnosisMatchable.getDiagnosis(patientHistory, new CharacteristicCode(unique[0])) == null : "Retrieved random diagnosis";
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.getValueHistory().setValue(ANATOMIC_SITE, new CharacteristicCode(unique[0]));
        patientHistory.getDiagnoses().add(diagnosis);
        assert DiagnosisMatchable.getDiagnosis(patientHistory, new CharacteristicCode(unique[0])) != null : "Cannot retrieve diagnosis";
        assert DiagnosisMatchable.getDiagnosis(patientHistory, new CharacteristicCode(unique[1])) == null : "Retrieved another random diagnosis";
    }

    /**
     * Test IsMetastatic
     */
    public void testIsMetastatic() {
        assert !DiagnosisMatchable.isMetastatic(null) : "Null history is metastatic";
        PatientHistory patientHistory = new PatientHistory();
        assert !DiagnosisMatchable.isMetastatic(patientHistory) : "New history is metastatic";
        patientHistory.getValueHistory().setValue(BREAST_CANCER, METASTATIC);
        assert DiagnosisMatchable.isMetastatic(patientHistory) : "Cannot find metastatic history";
    }

    /**
     * Test GetDiagnosisValue
     */
    public void testGetDiagnosisValue() {
        int[] unique = getUniqueIntegers(3);
        assert DiagnosisMatchable.getDiagnosisValue(null, null) == null : "Characteristic found in null diagnosis";
        assert DiagnosisMatchable.getDiagnosisValue(null, new CharacteristicCode(unique[0])) == null : "Random characteristic found in null diagnosis";
        Diagnosis diagnosis = new Diagnosis();
        assert DiagnosisMatchable.getDiagnosisValue(diagnosis, null) == null : "Null characteristic found in empty diagnosis";
        diagnosis.getValueHistory().setValue(new CharacteristicCode(unique[0]), new CharacteristicCode(unique[1]));
        assert DiagnosisMatchable.getDiagnosisValue(diagnosis, new CharacteristicCode(unique[1])) == null : "Wrong characteristic found in diagnosis";
        assert DiagnosisMatchable.getDiagnosisValue(diagnosis, new CharacteristicCode(unique[2])) == null : "Different characteristic found in diagnosis";
        assert new CharacteristicCode(unique[1]).equals(DiagnosisMatchable.getDiagnosisValue(diagnosis, new CharacteristicCode(unique[0]))) : "Characteristic not found in diagnosis";
    }
}
