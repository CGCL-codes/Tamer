package user_personalInformation;

import java.io.Serializable;
import java.util.Vector;
import user_medicalInformation.MedicalInformation;

/**
 * 
 * @author debous
 * @Create 07/03/2011
 * @lastUpdate 02/04/2011
 * 
 * The patient class
 */
public class Patient extends PersonID implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2910477081339351674L;

    /**
	 * Patient medical information
	 */
    private MedicalInformation patientMedicalInformation;

    /**
	 * Patient social security number
	 */
    private String socialSecurityNumber;

    /**
	 * Patient date of birth
	 */
    private String patienttDateOfBirth;

    /**
	 * Patient title
	 */
    private String patientTitle;

    /**
	 * Patient gender
	 */
    private String patientSexe;

    /**
	 * Patient address
	 */
    private String patientAddress;

    /**
	 * Patient height
	 */
    private String patientHeight;

    /**
	 * Patient weight
	 */
    private String patientWeight;

    /**
	 * Patient blood group
	 */
    private String patientBloodGroup;

    /**
	 * Patient emergency phone number
	 */
    private String patientEmergencyPhoneNumber;

    /**
	 * Constructor
	 * @param patientTitle The Patient title
	 * @param personLastName The patient last name
	 * @param personFirstName The patient first name
	 * @param socialSecurityNumber The patient social security number
	 * @param patienttDateOfBirth The patient date of birth
	 * @param patientSexe The patient gender
	 * @param patientBloodGroup The patient blood group
	 * @param patientHeight The patient height
	 * @param patientWeight The patient weight
	 * @param patientAddress The patient addres
	 * @param patientEmergencyPhoneNumber The patient emergency phone number
	 * @param pathologies The patient all pathologies
	 * @param vaccines The patient all vaccines
	 * @param allergies The patient all allergies
	 * @param treatments The patient all treatments
	 * @param medicalNote The patient medical note
	 */
    public Patient(String patientTitle, String personLastName, String personFirstName, String socialSecurityNumber, String patienttDateOfBirth, String patientSexe, String patientBloodGroup, String patientHeight, String patientWeight, String patientAddress, String patientEmergencyPhoneNumber, Vector<String> pathologies, Vector<String> vaccines, Vector<String> allergies, Vector<String> treatments, String medicalNote) {
        super(personLastName, personFirstName);
        this.patientTitle = patientTitle;
        this.socialSecurityNumber = socialSecurityNumber;
        this.patienttDateOfBirth = patienttDateOfBirth;
        this.patientSexe = patientSexe;
        this.patientBloodGroup = patientBloodGroup;
        this.patientAddress = patientAddress;
        this.patientHeight = patientHeight;
        this.patientWeight = patientWeight;
        this.patientEmergencyPhoneNumber = patientEmergencyPhoneNumber;
        this.patientMedicalInformation = new MedicalInformation(pathologies, vaccines, allergies, treatments, medicalNote);
    }

    /**
	 * Get all patient's personal information
	 * @return vector of all patient personal information
	 */
    public Vector<String> getAllPatientInformation() {
        Vector<String> allInformation = new Vector<String>();
        allInformation.add(patientTitle);
        allInformation.add(personFirstName);
        allInformation.add(personLastName);
        allInformation.add(socialSecurityNumber);
        allInformation.add(patienttDateOfBirth);
        allInformation.add(patientSexe);
        allInformation.add(patientBloodGroup);
        allInformation.add(patientHeight);
        allInformation.add(patientWeight);
        allInformation.add(patientAddress);
        allInformation.add(patientEmergencyPhoneNumber);
        return allInformation;
    }

    /**
	 * Get all patient's allergies
	 * @return vector of all patient allergies
	 */
    public Vector<String> getAllPatientAllergies() {
        return patientMedicalInformation.getAllergies();
    }

    /**
	 * Get all patient's medical note
	 * @return The patient's medical note
	 */
    public String getAllPatientMedicalNote() {
        return patientMedicalInformation.getMedicalNote();
    }

    /**
	 * Get all patient's pathologies
	 * @return vector of all patient pathologies
	 */
    public Vector<String> getAllPatientPathologies() {
        return patientMedicalInformation.getPathologies();
    }

    /**
	 * Get all patient's treatments
	 * @return vector of all patient treatments
	 */
    public Vector<String> getAllPatientTreatments() {
        return patientMedicalInformation.getTreatments();
    }

    /**
	 * Get all patient's vaccines
	 * @return vector of all patient vaccines
	 */
    public Vector<String> getAllPatientVaccines() {
        return patientMedicalInformation.getVaccines();
    }

    /**
	 * Get the patient's address
	 * @return The patient's address
	 */
    public String getPatientAddress() {
        return patientAddress;
    }

    /**
	 * Get the patient's height
	 * @return The patient's height
	 */
    public String getPatientHeight() {
        return patientHeight;
    }

    /**
	 * Get the patient's gender
	 * @return The patient's gender
	 */
    public String getPatientSexe() {
        return patientSexe;
    }

    /**
	 * Get the patient's date of birth
	 * @return The patient's date of birth
	 */
    public String getPatienttDateOfBirth() {
        return patienttDateOfBirth;
    }

    /**
	 * Get the patient's title
	 * @return The patient's title
	 */
    public String getPatientTitle() {
        return patientTitle;
    }

    /**
	 * Get the patient's weight
	 * @return The patient's weight
	 */
    public String getPatientWeight() {
        return patientWeight;
    }

    /**
	 * Get the patient's social security number
	 * @return The patient's social security number
	 */
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    /**
	 * Get the patient's blood group
	 * @return The patient's blood group
	 */
    public String getPatientBloodGroup() {
        return patientBloodGroup;
    }

    /**
	 * Get the patient's emergency phone number
	 * @return The patient's emergency phone number
	 */
    public String getPatientEmergencyPhoneNumber() {
        return patientEmergencyPhoneNumber;
    }
}
