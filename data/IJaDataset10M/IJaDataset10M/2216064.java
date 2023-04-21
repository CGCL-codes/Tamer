package eu.roelbouwman.personLib.model.address;

/**
 * The Class PhoneNumber.
 */
public class PhoneNumberWork extends VirtualAddress {

    /** The phone number. */
    private String phoneNumber;

    /**
	 * Instantiates a new phone number.
	 * 
	 * @param phone the phone
	 */
    public PhoneNumberWork(String phone) {
        this.phoneNumber = phone;
    }

    /**
	 * Gets the phone number.
	 * 
	 * @return the phone number
	 */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
	 * Sets the phone number.
	 * 
	 * @param phoneNumber the new phone number
	 */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
