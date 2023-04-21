package pt.igeo.snig.mig.editor.record.contact;

import java.io.Serializable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import fi.mmm.yhteinen.swing.core.YApplicationEvent;
import fi.mmm.yhteinen.swing.core.YController;
import pt.igeo.snig.mig.editor.constants.Constants;
import pt.igeo.snig.mig.editor.defaults.DefaultValuesManager;
import pt.igeo.snig.mig.editor.list.FixedList;
import pt.igeo.snig.mig.editor.list.ListValueManager;
import pt.igeo.snig.mig.editor.record.Record;
import pt.igeo.snig.mig.editor.record.xPathHelper;
import pt.igeo.snig.mig.editor.utils.PrivateAccessor;
import pt.igeo.snig.mig.undoManager.UndoElement;
import pt.igeo.snig.mig.undoManager.UndoListener;
import pt.igeo.snig.mig.undoManager.UndoManager;

/**
 * Representation of a Contact.
 * 
 * @author Ant�nio Silva
 * @version $ Revision: 11274 $
 * @since 1.0
 */
public class Contact implements Serializable, UndoListener {

    /**
	 * java requirement
	 */
    private static final long serialVersionUID = 6549078351908381401L;

    /** the organisation name */
    private String organisationName;

    /** function - see Role in FixedListManager */
    private FixedList role;

    /** subject's name */
    private String individualName = "";

    /** telephone number */
    private String telephone = "";

    /** contact address */
    private String address = "";

    /** address city */
    private String city = "";

    /** address postal code */
    private String postalCode = "";

    /** address country */
    private String country = "";

    /** e-mail */
    private String email = "";

    /** fax */
    private String fax = "";

    /** xpath helper - doesn't get copied in serialization */
    private transient xPathHelper xHelp = xPathHelper.getInstance();

    public static transient String[] CSVHeader = { "OrganisationName", "Role", "IndividualName", "City", "Country", "Telephone", "Address" };

    /**
	 * Contact constructor
	 * 
	 * @param contact (node MD_Metadata/contact/)
	 */
    public Contact(Node contact) {
        Node elName = xHelp.readPath(contact, "CI_ResponsibleParty/individualName");
        setIndividualName(xHelp.readCharacterString(elName));
        Node elOrgName = xHelp.readPath(contact, "CI_ResponsibleParty/organisationName");
        setOrganisationName(xHelp.readCharacterString(elOrgName));
        Node elRole = xHelp.readPath(contact, "CI_ResponsibleParty/role");
        setRole(xHelp.readFixedList(elRole));
        Node elTelephone = xHelp.readPath(contact, "CI_ResponsibleParty/contactInfo/CI_Contact/phone/CI_Telephone/voice");
        setTelephone(xHelp.readCharacterString(elTelephone));
        Node elFax = xHelp.readPath(contact, "CI_ResponsibleParty/contactInfo/CI_Contact/phone/CI_Telephone/facsimile");
        setFax(xHelp.readCharacterString(elFax));
        Node elAddress = xHelp.readPath(contact, "CI_ResponsibleParty/contactInfo/CI_Contact/address/CI_Address/deliveryPoint");
        setAddress(xHelp.readCharacterString(elAddress));
        Node elCity = xHelp.readPath(contact, "CI_ResponsibleParty/contactInfo/CI_Contact/address/CI_Address/city");
        setCity(xHelp.readCharacterString(elCity));
        Node elPostalCode = xHelp.readPath(contact, "CI_ResponsibleParty/contactInfo/CI_Contact/address/CI_Address/postalCode");
        setPostalCode(xHelp.readCharacterString(elPostalCode));
        Node elCountry = xHelp.readPath(contact, "CI_ResponsibleParty/contactInfo/CI_Contact/address/CI_Address/country");
        setCountry(xHelp.readCharacterString(elCountry));
        Node elEmail = xHelp.readPath(contact, "CI_ResponsibleParty/contactInfo/CI_Contact/address/CI_Address/electronicMailAddress");
        setEmail(xHelp.readCharacterString(elEmail));
    }

    /**
	 * Empty constructor
	 */
    public Contact() {
        setIndividualName(DefaultValuesManager.getInstance().getString("ContactName"));
        setRole(ListValueManager.getInstance().getFixedLists(Constants.roleCodeList).iterator().next());
    }

    /** @return the contact's address */
    public String getAddress() {
        return address;
    }

    /**
	 * @param address The contact's address
	 */
    public void setAddress(String address) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.address, "address"));
        this.address = address;
    }

    /** @return the contact's city */
    public String getCity() {
        return city;
    }

    /**
	 * @param city The contact's city
	 */
    public void setCity(String city) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.city, "city"));
        this.city = city;
    }

    /** @return the contact's country */
    public String getCountry() {
        return country;
    }

    /**
	 * @param country The contact's country
	 */
    public void setCountry(String country) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.country, "country"));
        this.country = country;
    }

    /** @return the contact's email */
    public String getEmail() {
        return email;
    }

    /**
	 * @param email The contact's email
	 */
    public void setEmail(String email) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.email, "email"));
        this.email = email;
    }

    /** @return the contact's individual name */
    public String getIndividualName() {
        return individualName;
    }

    /**
	 * @param individualName The contact's individual name
	 */
    public void setIndividualName(String individualName) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.individualName, "individualName"));
        this.individualName = individualName;
    }

    /** @return the contact's organisation name */
    public String getOrganisationName() {
        return organisationName;
    }

    /**
	 * @param organisationName The contact's organisation name
	 */
    public void setOrganisationName(String organisationName) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.organisationName, "organisationName"));
        this.organisationName = organisationName;
    }

    /** @return the contact's postal code */
    public String getPostalCode() {
        return postalCode;
    }

    /**
	 * @param postalCode The contact's postal code
	 */
    public void setPostalCode(String postalCode) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.postalCode, "postalCode"));
        this.postalCode = postalCode;
    }

    /** @return the contact's role */
    public FixedList getRole() {
        return role;
    }

    /**
	 * @param role The contact's role
	 */
    public void setRole(FixedList role) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.role, "role"));
        this.role = role;
    }

    /** @return the contact's telephone */
    public String getTelephone() {
        return telephone;
    }

    /**
	 * @param telephone The contact's telephone
	 */
    public void setTelephone(String telephone) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.telephone, "telephone"));
        this.telephone = telephone;
    }

    /**
	 * getter for facsimile
	 * 
	 * @return the fax number
	 */
    public String getFax() {
        return fax;
    }

    /**
	 * setter for facsimile
	 * 
	 * @param fax
	 */
    public void setFax(String fax) {
        UndoManager.getInstance().addUndoElement(new UndoElement(this, this.fax, "fax"));
        this.fax = fax;
    }

    /**
	 * @param document
	 * @return this contact's dom tree
	 */
    public Node getDomTree(Document document) {
        Node el1 = document.createElement("gmd:contact");
        el1.appendChild(getResponsibleParty(document));
        return el1;
    }

    /**
	 * 
	 * @param document
	 * @return this contacts CI_Responsible party subnode
	 */
    public Node getResponsibleParty(Document document) {
        if (xHelp == null) xHelp = xPathHelper.getInstance();
        Node[] help = xHelp.writePath(document, "gmd:CI_ResponsibleParty/gmd:individualName", true);
        Node root = help[0];
        Node writer = help[1];
        xHelp.writeCharacterString(writer, getIndividualName());
        writer = xHelp.writePath(root, "gmd:organisationName", true);
        xHelp.writeCharacterString(writer, getOrganisationName());
        Node contact = xHelp.writePath(root, "gmd:contactInfo/gmd:CI_Contact", true);
        Node telephone = xHelp.writePath(contact, "gmd:phone/gmd:CI_Telephone", true);
        Node address = xHelp.writePath(contact, "gmd:address/gmd:CI_Address", true);
        writer = xHelp.writePath(telephone, "gmd:voice", true);
        xHelp.writeCharacterString(writer, getTelephone());
        writer = xHelp.writePath(telephone, "gmd:facsimile", true);
        xHelp.writeCharacterString(writer, getFax());
        writer = xHelp.writePath(address, "gmd:deliveryPoint", true);
        xHelp.writeCharacterString(writer, getAddress());
        writer = xHelp.writePath(address, "gmd:city", true);
        xHelp.writeCharacterString(writer, getCity());
        writer = xHelp.writePath(address, "gmd:postalCode", true);
        xHelp.writeCharacterString(writer, getPostalCode());
        writer = xHelp.writePath(address, "gmd:country", true);
        xHelp.writeCharacterString(writer, getCountry());
        writer = xHelp.writePath(address, "gmd:electronicMailAddress", true);
        xHelp.writeCharacterString(writer, getEmail());
        writer = xHelp.writePath(root, "gmd:role", true);
        xHelp.writeFixedList(writer, getRole());
        return root;
    }

    /**
	 * Returns the CSV representation
	 * 
	 */
    public String[] toCSV() {
        String[] csv = { getOrganisationName(), getRole().getCodeValue(), getIndividualName(), getCity(), getCountry(), getTelephone(), getPostalCode(), getEmail(), getAddress() };
        return csv;
    }

    /**
	 * Fill fields from CSV representation
	 */
    public void fromCSV(String[] csv) {
        setOrganisationName(csv[0]);
        getRole().setCodeValue(csv[1]);
        setIndividualName(csv[2]);
        setCity(csv[3]);
        setCountry(csv[4]);
        setTelephone(csv[5]);
        setPostalCode(csv[6]);
        setEmail(csv[7]);
        setAddress(csv[8]);
    }

    /**
	 * Returns a more friendly string representation
	 * 
	 * @return the string representing the contact
	 */
    public String toString() {
        return "[ORG_NAME: " + getOrganisationName() + ", ROLE: " + getRole().getCodeValue() + ", INDIVIDUAL_NAME: " + getIndividualName() + ", CITY: " + getCity() + ", COUNTRY: " + getCountry() + ", TELEPHONE: " + getTelephone() + ", POSTAL CODE: " + getPostalCode() + ", EMAIL: " + getEmail() + ", ADDRESS: " + getAddress() + "]";
    }

    @Override
    public Object undo(Object olderObject, String fieldName, Record record) {
        Object ret = PrivateAccessor.getPrivateField(this, fieldName);
        PrivateAccessor.setPrivateField(this, olderObject, fieldName);
        YController.sendApplicationEvent(new YApplicationEvent(Constants.Refresh, this));
        YController.sendApplicationEvent(new YApplicationEvent(Constants.recordChangedEvent, record));
        return ret;
    }
}
