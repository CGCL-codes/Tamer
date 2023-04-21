package de.buelowssiege.jayaddress.misc;

/**
 * Represents one addressentry
 * 
 * @author Maximilian Schwerin
 * @created 13. Juli 2002
 */
public class AddressEntry {

    private String firstName = "";

    private String lastName = "";

    private String title = "";

    private String organization = "";

    private String birthday = "";

    private PostalAddress[] postalAddress = null;

    private PhoneNumber[] phoneNumbers = null;

    private String[] emails = null;

    private String[] webAddress = null;

    private String commentary = "";

    private String PGPKey = "";

    /**
     * This is the constructor for the <code>AddressEntry</code>
     */
    public AddressEntry() {
    }

    /**
     * This is the constructor for the <code>AddressEntry</code>
     * 
     * @param fn
     *            Description of the Parameter
     * @param ln
     *            Description of the Parameter
     * @param t
     *            Description of the Parameter
     * @param c
     *            Description of the Parameter
     * @param b
     *            Description of the Parameter
     * @param pa
     *            Description of the Parameter
     * @param pn
     *            Description of the Parameter
     * @param e
     *            Description of the Parameter
     * @param w
     *            Description of the Parameter
     * @param co
     *            Description of the Parameter
     */
    public AddressEntry(String fn, String ln, String t, String c, String b, PostalAddress[] pa, PhoneNumber[] pn, String[] e, String[] w, String co) {
        setFirstname(fn);
        setLastname(ln);
        setTitle(t);
        setOrganization(c);
        setBirthday(b);
        setPostals(pa);
        setPhones(pn);
        setEmails(e);
        setWebs(w);
        setCommentary(co);
    }

    /**
     * Sets the firstName attribute of the AddressEntry object
     */
    public void setFirstname(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the lastName attribute of the AddressEntry object
     */
    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the title attribute of the AddressEntry object
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the organization attribute of the AddressEntry object
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * Sets the birthday attribute of the AddressEntry object
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * Sets the postals attribute of the AddressEntry object
     */
    public void setPostals(PostalAddress[] postalAddress) {
        this.postalAddress = postalAddress;
    }

    /**
     * Sets the phones attribute of the AddressEntry object
     */
    public void setPhones(PhoneNumber[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    /**
     * Sets the emails attribute of the AddressEntry object
     */
    public void setEmails(String[] emails) {
        this.emails = emails;
    }

    /**
     * Sets the webs attribute of the AddressEntry object
     */
    public void setWebs(String[] webAddress) {
        this.webAddress = webAddress;
    }

    /**
     * Sets the commentary attribute of the AddressEntry object
     */
    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    /**
     * Sets the pGPKey attribute of the AddressEntry object
     */
    public void setPGPKey(String PGPKey) {
        this.PGPKey = PGPKey;
    }

    /**
     * Gets the firstName attribute of the AddressEntry object
     */
    public String getFirstname() {
        return (firstName);
    }

    /**
     * Gets the lastName attribute of the AddressEntry object
     */
    public String getLastname() {
        return (lastName);
    }

    /**
     * Gets the title attribute of the AddressEntry object
     */
    public String getTitle() {
        return (title);
    }

    /**
     * Gets the organization attribute of the AddressEntry object
     */
    public String getOrganization() {
        return (organization);
    }

    /**
     * Gets the birthday attribute of the AddressEntry object
     */
    public String getBirthday() {
        return (birthday);
    }

    /**
     * Gets the postals attribute of the AddressEntry object
     */
    public PostalAddress[] getPostals() {
        return (postalAddress);
    }

    /**
     * Gets the phones attribute of the AddressEntry object
     */
    public PhoneNumber[] getPhones() {
        return (phoneNumbers);
    }

    /**
     * Gets the emails attribute of the AddressEntry object
     */
    public String[] getEmails() {
        return (emails);
    }

    /**
     * Gets the webs attribute of the AddressEntry object
     */
    public String[] getWebs() {
        return (webAddress);
    }

    /**
     * Gets the commentary attribute of the AddressEntry object
     */
    public String getCommentary() {
        return (commentary);
    }

    /**
     * Gets the pGPKey attribute of the AddressEntry object
     */
    public String getPGPKey() {
        return (PGPKey);
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public boolean isEqual(AddressEntry address) {
        boolean equals = getFirstname().equals(address.getFirstname());
        equals = equals && getLastname().equals(address.getLastname());
        equals = equals && getTitle().equals(address.getTitle());
        equals = equals && getOrganization().equals(address.getOrganization());
        equals = equals && getBirthday().equals(address.getBirthday());
        equals = equals && getCommentary().equals(address.getCommentary());
        equals = equals && getPGPKey().equals(address.getPGPKey());
        return (equals);
    }

    /**
     * Its selfexplaining isnt it! :-)
     */
    public boolean equals(Object other) {
        return (other != null && other instanceof AddressEntry && isEqual((AddressEntry) other));
    }
}
