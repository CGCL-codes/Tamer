package DatabaseAccess;

/**
 *
 * @author mr duy
 */
public class Child {

    private String ChildID;

    private String ClassID;

    private String LastName;

    private String MiddleName;

    private String FirstName;

    private String Sex;

    private String Address;

    private String DateOfBirth;

    private String CurrentMedications;

    private String PastIllness;

    private String DoctorName;

    private String ParentName;

    private String ParentWorkNumber;

    private String ParentMobileNumber;

    private String ParentEmailAddress;

    private String NextOfKinContact;

    private String DateRegistration;

    private String DateReceived;

    public Child() {
    }

    public Child(String ChildID, String ClassID, String LastName, String MiddleName, String FirstName, String Sex, String Address, String DateOfBirth, String CurrentMedications, String PastIllness, String DoctorName, String ParentName, String ParentWorkNumber, String ParentMobileNumber, String ParentEmailAddress, String NextOfKinContact, String DateRegistration, String DateReceived) {
        this.ChildID = ChildID;
        this.ClassID = ClassID;
        this.LastName = LastName;
        this.MiddleName = MiddleName;
        this.FirstName = FirstName;
        this.Sex = Sex;
        this.Address = Address;
        this.DateOfBirth = DateOfBirth;
        this.CurrentMedications = CurrentMedications;
        this.PastIllness = PastIllness;
        this.DoctorName = DoctorName;
        this.ParentName = ParentName;
        this.ParentWorkNumber = ParentWorkNumber;
        this.ParentMobileNumber = ParentMobileNumber;
        this.ParentEmailAddress = ParentEmailAddress;
        this.NextOfKinContact = NextOfKinContact;
        this.DateRegistration = DateRegistration;
        this.DateReceived = DateReceived;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getChildID() {
        return ChildID;
    }

    public void setChildID(String ChildID) {
        this.ChildID = ChildID;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String ClassID) {
        this.ClassID = ClassID;
    }

    public String getCurrentMedications() {
        return CurrentMedications;
    }

    public void setCurrentMedications(String CurrentMedications) {
        this.CurrentMedications = CurrentMedications;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    public String getDateReceived() {
        return DateReceived;
    }

    public void setDateReceived(String DateReceived) {
        this.DateReceived = DateReceived;
    }

    public String getDateRegistration() {
        return DateRegistration;
    }

    public void setDateRegistration(String DateRegistration) {
        this.DateRegistration = DateRegistration;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String DoctorName) {
        this.DoctorName = DoctorName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String MiddleName) {
        this.MiddleName = MiddleName;
    }

    public String getNextOfKinContact() {
        return NextOfKinContact;
    }

    public void setNextOfKinContact(String NextOfKinContact) {
        this.NextOfKinContact = NextOfKinContact;
    }

    public String getParentEmailAddress() {
        return ParentEmailAddress;
    }

    public void setParentEmailAddress(String ParentEmailAddress) {
        this.ParentEmailAddress = ParentEmailAddress;
    }

    public String getParentMobileNumber() {
        return ParentMobileNumber;
    }

    public void setParentMobileNumber(String ParentMobileNumber) {
        this.ParentMobileNumber = ParentMobileNumber;
    }

    public String getParentName() {
        return ParentName;
    }

    public void setParentName(String ParentName) {
        this.ParentName = ParentName;
    }

    public String getParentWorkNumber() {
        return ParentWorkNumber;
    }

    public void setParentWorkNumber(String ParentWorkNumber) {
        this.ParentWorkNumber = ParentWorkNumber;
    }

    public String getPastIllness() {
        return PastIllness;
    }

    public void setPastIllness(String PastIllness) {
        this.PastIllness = PastIllness;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String Sex) {
        this.Sex = Sex;
    }
}
