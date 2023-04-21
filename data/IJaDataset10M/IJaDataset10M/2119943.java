package edu.upmc.opi.caBIG.caTIES.installer.pipes.deid.DoNothing;

import java.util.Date;
import org.apache.log4j.Logger;
import edu.upmc.opi.caBIG.caTIES.server.deid.CaTIES_DeIdentifier;

public class CaTIES_DoNothingDeIdentifier implements CaTIES_DeIdentifier {

    /**
	 * The Constant logger.
	 */
    private static final Logger logger = Logger.getLogger(CaTIES_DoNothingDeIdentifier.class);

    /**
	 * The Constant name.
	 */
    public static final String name = CaTIES_DoNothingDeIdentifier.class.getName();

    /**
	 * The Constant revision.
	 */
    public static final String revision = "3.0";

    public String deIdentify(String text) {
        logger.debug("\n" + text + "\n");
        return text;
    }

    public void finalizeDeIdentification() {
    }

    public String getApplicationName() {
        return name;
    }

    public String getApplicationRevision() {
        return revision;
    }

    public void initializeDeIdentification() {
        ;
    }

    public String deIdentify() {
        return null;
    }

    public void setOrganizationAbbreviationName(String organizationAbbreviationName) {
    }

    public void setOrganizationAddressCity(String city) {
    }

    public void setOrganizationAddressCountry(String country) {
    }

    public void setOrganizationAddressEmailAddress(String emailAddress) {
    }

    public void setOrganizationAddressFaxNumber(String faxNumber) {
    }

    public void setOrganizationAddressPhoneNumber(String phoneNumber) {
    }

    public void setOrganizationAddressState(String state) {
    }

    public void setOrganizationAddressStreet(String street) {
    }

    public void setOrganizationAddressZipCode(String zipCode) {
    }

    public void setOrganizationName(String organizationName) {
    }

    public void setDocumentAccessionNumber(String accessionNumber) {
    }

    public void setDocumentCollectionDateTime(Date collectionDateTime) {
    }

    public void setDocumentDocumentText(String text) {
    }

    public void setPatientAgeAtSpecimenCollection(Integer ageAtSpecimenCollection) {
    }

    public void setPatientBirthDate(Date birthDate) {
    }

    public void setPatientEthnicity(String ethnicity) {
    }

    public void setPatientFirstName(String firstName) {
    }

    public void setPatientGender(String gender) {
    }

    public void setPatientLastName(String lastName) {
    }

    public void setPatientMaritalStatus(String maritalStatus) {
    }

    public void setPatientMedicalRecordNumber(String medicalRecordNumber) {
    }

    public void setPatientMiddleName(String middleName) {
    }

    public void setPatientRace(String race) {
    }

    public void setPatientSocialSecurityNumber(String socialSecurityNumber) {
    }

    public void setDocumentUuid(String DocumentUuid) {
    }
}
