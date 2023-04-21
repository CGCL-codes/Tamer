package com.globant.google.mendoza.malbec;

/**
 * A buyer address that does not already exist. Must be created before placing
 * the order.
 */
public class BuyerNewAddress implements BuyerInformation {

    /**The address name.
   */
    private String name;

    /**The address data.
   */
    private BuyerAddress address;

    /**The buyer phone for the specified address.
   */
    private String phone;

    /**
   * Creates a new BuyerNewAddress object.
   * @param theName the name of the new address. Cannot be null.
   * @param theAddress the data for the new address. Cannot be null.
   * @param thePhone the phone number for the address. Cannot be null.
   */
    public BuyerNewAddress(final String theName, final BuyerAddress theAddress, final String thePhone) {
        if (theName == null || theName.trim().equals("")) {
            throw new RuntimeException("New address name cannot be null nor empty");
        }
        if (theAddress == null) {
            throw new RuntimeException("New address data cannot be null");
        }
        if (thePhone == null || thePhone.trim().equals("")) {
            throw new RuntimeException("New address phone cannot be null nor empty");
        }
        this.name = theName;
        this.address = theAddress;
        this.phone = thePhone;
    }

    /**
   * Returns the address.
   * @return the address
   */
    public final BuyerAddress getAddress() {
        return address;
    }

    /**
   * Returns the address name.
   * @return the name
   */
    public final String getName() {
        return name;
    }

    /**
   * Returns the address phone.
   * @return the phone
   */
    public final String getPhone() {
        return phone;
    }

    /** Makes the visitor to enter an address.
   * @param visitor The visitor visiting the class.
   */
    public final void execute(final BuyerVisitor visitor) {
        visitor.enterNewAddress(this);
    }
}
