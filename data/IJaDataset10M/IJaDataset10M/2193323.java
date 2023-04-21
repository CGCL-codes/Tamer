package tests.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Bajales Raul
 *
 */
public class Seller {

    private String companyName;

    private String address;

    /**
     * Constructor
     */
    public Seller() {
        super();
    }

    /**
     * @param companyName
     * @param address
     */
    public Seller(String companyName, String address) {
        super();
        this.companyName = companyName;
        this.address = address;
    }

    /**
     * @return Returns the address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return Returns the companyName.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName The companyName to set.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
