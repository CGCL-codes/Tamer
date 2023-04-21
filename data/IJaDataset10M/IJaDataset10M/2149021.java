package org.cyberaide.emolst.interfaces;

import org.cyberaide.emolst.app.MolstDate;
import org.cyberaide.emolst.app.MolstFileBean;
import org.cyberaide.emolst.jdbc.EventBean;
import org.cyberaide.emolst.jdbc.MedicalPersonnelBean;
import org.cyberaide.emolst.jdbc.MolstFormBean;
import org.cyberaide.emolst.jdbc.MolstReviewBean;
import org.cyberaide.emolst.jdbc.PatientBean;
import org.cyberaide.emolst.jdbc.SignatureBean;

/**
 * Provides a list of methods to be implemented that services 
 * in the system will use.
 * 
 * This is currently only implemented with a database though 
 * JDBC connections.  However future implimentations might use 
 * XDS to store file information.  
 * 
 */
public interface MolstInterface {

    /**
	 * Takes three parameters to add an entry to the event log
	 * 
	 * @param type		- the type of event
	 * @param message	- the message string that describes the event
	 * @param time		- the date/time the event occurred
	 */
    public void log(String type, String message, MolstDate time);

    /**
	 * Returns all log entries that match the type specified and
	 * occurred within the date window specified
	 * 
	 * @param type		- the type of event
	 * @param begin		- the beginning date to search
	 * @param end		- the end date to search
	 */
    public EventBean[] getLog(String type, MolstDate begin, MolstDate end);

    /**
	 * Provides a method to check if a login name and password
	 * match up with some personnel in the system
	 * 
	 * @param	loginName 	- the user's login name
	 * @param	password 	- the user's password
	 * @return	MedicalPersonnelBean - null if invalid credentials
	 */
    public MedicalPersonnelBean login(String loginName, String password);

    /**
	 * Provides a method to retrieve a signature object from the system
	 * 
	 * @param	pk		 		- the unique identifier of the signature
	 * @return	signatureBean	- the SignatureBean representation of this
	 * 								signature
	 */
    public SignatureBean LoadSignatureByPK(int pk);

    /**
	 * Provides a method to retrieve a doctor's collection of patients
	 * from the system based on the doctor's unique identifier
	 * 
	 * @param	docKey 		- the doctor's unique identifier
	 * @return	patients	- the collection of patients
	 */
    public PatientBean[] getPatientsByDoctor(int docKey);

    /**
	 * Provides an advanced search method to retrieve patients
	 * that match a series of parameters
	 * 
	 * @param	lname 		- Last name
	 * @param	fname 		- First name
	 * @param	address		- 
	 * @param	city 		- 
	 * @param	state 		- 
	 * @param	zipcode		- 
	 * @param	gender 		- 
	 * @param 	offset		- Rows retrieved from the database will start at (offset*limit)
	 * @param 	limit		- Specifies the maximum number of results returned.
	 * @param 	col			- Specifies the column to sort by.  This is used by the UI layer to keep the result set consistent with the user preference
	 * @param 	ascending	- Specifies the order to sort by.  This is used by the UI layer to keep the result set consistent with the user preference
	 * @param	accessLevel	- Specifies a doctor to filter by.  A value of -1 will cause this parameter to not be used as a filter.  That is to say, results for all doctors will be returned.
	 * @return				- An array containing 0 or more PatientBean objects.
	 */
    public PatientBean[] getPatients(String lname, String fname, String address, String city, String state, String zipcode, String gender, int offset, int limit, String col, boolean ascending, int fkDoctor);

    /**
	 * Provides a way to get the total number of rows a search result 
	 * will return when not limited.  This is useful for pagination.
	 * 
	 * @param	lname 		- Last name
	 * @param	fname 		- First name
	 * @param	address		- 
	 * @param	city 		- 
	 * @param	state 		- 
	 * @param	zipcode		- 
	 * @param	gender 		- 
	 * @param	fkDoctor	- Specifies a doctor to filter by.  A value of -1 will cause this parameter to not be used as a filter.  That is to say, results for all doctors will be returned.
	 * @return				- An array containing 0 or more PatientBean objects.
	 */
    public int getPatientsCount(String lname, String fname, String address, String city, String state, String zipcode, String gender, int fkDoctor);

    /**
	 * Provides a method to retrieve a patient's information
	 * in the form of a PatientBean object from the system
	 * 
	 * @param	patientID 	- the patient's unique identifier
	 * @return	patient		- the PatientBean representation of the patient
	 */
    public PatientBean getPatientInfo(int patientID);

    /**
	 * Provides a method to add patients to the system
	 * 
	 * @param	patient	 	- the PatientBean representation of the patient
	 * @return	int			- the pk of the patient just created.
	 */
    public int createPatient(PatientBean patient);

    /**
	 * Provides a method to add personnel to the system
	 * 
	 * @param	person	 	- the MedicalPersonnelBean representation of
	 * 							the person
	 */
    public int createPersonnel(MedicalPersonnelBean person);

    /**
	 * Provides a method to retrieve information about a system user
	 * @param personnelPK	- the unique identifier of the personnel
	 * @return				- the information in a <code>MedicalPersonnelBean</code>
	 */
    public MedicalPersonnelBean getPersonnelInfo(int personnelPK);

    /**
	 * Provides a method to get all personnel with a certain access level
	 * @param accessLevel	- the access level to search by
	 * @return				- An array of medical personnel
	 */
    public MedicalPersonnelBean[] getPersonnelByAccess(int accessLevel);

    /**
	 * Provides a way to search users with a list of filtering parameters 
	 * an allows the results to be limited to s subset of to total results.
	 * 
	 * @param lname			-Last name
	 * @param fname			-First name
	 * @param address
	 * @param city
	 * @param state
	 * @param accessLevel	- Permission level user has in the system.  This value should be -1 to eliminate it as a search criteria.  Valid numbers are 1 or greater.
	 * @param email			- email address
	 * @param positiontype	- literal string that describes their position
	 * @param loginName
	 * @param phone			- phone number
	 * @param zipcode		- 
	 * @param offset		- Rows retrieved from the database will start at (offset*limit)
	 * @param limit			- Specifies the maximum number of results returned.
	 * @param col			- Specifies the column to sort by.  This is used by the UI layer to keep the result set consistent with the user preference
	 * @param ascending		- Specifies the order to sort by.  This is used by the UI layer to keep the result set consistent with the user preference
	 * @return 0 or more personnel beans.
	 */
    public MedicalPersonnelBean[] getPersonnel(String lname, String fname, String address, String city, String state, int accessLevel, String email, String positionType, String loginName, String phone, String zipcode, int offset, int limit, String col, boolean ascending);

    /**
	 * Provides a way to determine the total number of search results 
	 * with specified parameters without limits in place.  This is used 
	 * for pagination of users.
	 * 
	 * @param lname			-Last name
	 * @param fname			-First name
	 * @param address
	 * @param city
	 * @param state
	 * @param accessLevel	- Permission level user has in the system.  This value should be -1 to eliminate it as a search criteria.  Valid numbers are 1 or greater.
	 * @param email			- email address
	 * @param positiontype	- literal string that describes their position
	 * @param loginName
	 * @param phone			- phone number
	 * @param zipcode		- 
	 * @return The total number of results returned from this search.
	 */
    public int getPersonnelCount(String lname, String fname, String address, String city, String state, int accessLevel, String email, String positionType, String loginName, String phone, String zipcode);

    /**
	 * Provides a method to retrieve information about a system user
	 * @param username		- the unique identifier of the personnel
	 * @return				- the information in a <code>MedicalPersonnelBean</code>
	 */
    public MedicalPersonnelBean getPersonnelInfo(String username);

    /**
	 * Provides a way to tell if a user is active or if they 
	 * have been "removed" from the system.
	 * @param personnelPK	-the unique identifier of the personnel
	 * @return				-boolean result of if they are active
	 */
    public boolean isUserActive(int personnelPK);

    /**
	 * Provides a method to check if a username exists in the system
	 * 
	 * @param username		- the username to check
	 * @return boolean		- true or false value whether the username exists
	 */
    public boolean usernameExists(String username);

    /**
	 * Updates medical personnel with information stored in another medical personnel object
	 * @param person		- the person in the database that will be updated
	 * @param update		- the information that will be applied to the person
	 */
    public void updatePersonnel(MedicalPersonnelBean person);

    /**
	 * Removes a user by setting their active status to false
	 * @param pk	-User identified by PK to be removed.
	 */
    public void removeUser(int pk);

    /**
	 * Removes a user recored from the DB
	 * @param pk	-User identified by PK to be removed.
	 */
    public void reallyRemoveUser(int pk);

    /**
	 * Deletes a patient with the specified identifier
	 * 
	 * @param patientID	- the unique identifier of the patient
	 */
    public void deletePatient(int patientID);

    /**
	 * Updates the patient's information
	 * 
	 * @param patient	- the PatientBean representation of the patient
	 */
    public void updatePatient(PatientBean patient);

    /**
	 * Gets all the personnel from the database
	 * @return	-An array of medical personnel
	 */
    public MedicalPersonnelBean[] getAllPersonnel();

    /**
	 * Gets a form associated with a patient
	 * @return	- A molst form bean
	 */
    public MolstFormBean getFormByPatient(int patientPK);

    /**
	 * Gets a form by its unique identifier.
	 * @param formPK	- identifier
	 * @return			- A molst form bean
	 */
    public MolstFormBean getForm(int formPK);

    public boolean isFormComplete(int formPK);

    public boolean isSectionCompleteA(int formPK);

    public boolean isSectionCompleteB(int formPK);

    public boolean isSectionCompleteC(int formPK);

    public boolean isSectionCompleteD(int formPK);

    public boolean isSectionCompleteE(int formPK);

    /**
	 * Commit a form
	 * @param form
	 * @return		-unique identifier for this form.  Or -1 if there ws an error.
	 */
    public int saveForm(MolstFormBean form);

    /**
	 * Gets signature information
	 * @param pk	-the uniqure identifier of the signature
	 * @return		-the signature information.
	 */
    public SignatureBean getSignature(int pk);

    /**
	 * Saves a signature
	 * @param sig	-the signature information
	 * @return		-the primary key for the signature just saved or -1 if there was an error that prevented saving.
	 */
    public int saveSignature(SignatureBean sig);

    /**
	 * Saves a review
	 * @param bean	-Review bean inforamtion
	 * @return		-identifier of saved review
	 */
    public int saveReview(MolstReviewBean bean);

    /**
	 * Generates a list of all users, and be limited and sorted 
	 * by column for use in pagination. 
	 * @param userPK		-PK of the patient to get reviews for
	 * @param offset		-(page# - 1) * limit
	 * @param limit			-amount of results returned
	 * @param col			-column to sort by
	 * @param ascending		-direction in which to sort
	 * @return				-array of MolstReviewBean 's
	 */
    public MolstReviewBean[] getReviews(int userPK, int offset, int limit, String col, boolean ascending);

    /**
	 * Generates the total number of reviews for a patient
	 * @param userPK	- The unique identifier of a patient
	 * @return The total number of reviews for a patient
	 */
    public int getReviewsCount(int userPK);

    /**
	 * This will provides functionality for uploading a file to the system.
	 *  
	 * @param file	-The file to upload
	 * @return		-The database id or something of the uploaded file
	 */
    public int uploadDocument(MolstFileBean file);

    /**
	 * This will return a file bean.  The file byte array 
	 * will contain no data to save transfer time, however 
	 * the location field will contain a relative path to 
	 * the file location on the server.
	 * 
	 * @param fileID	-unique identifier of this file
	 * @return			-A file bean with a location
	 */
    public MolstFileBean getFile(int fileID);

    /**
	 * This will return a file bean.  The file byte array 
	 * will contain no data to save transfer time, however 
	 * the location field will contain a relative path to 
	 * the file location on the server.
	 * 
	 * @param patientID	-unique id of the patient for who this file belongs
	 * @param section	-what type of file it is (arbitrary and decided by the UI)
	 * @return			-A file bean with a location
	 */
    public MolstFileBean getFileByPatient(int patientID, int section);
}
