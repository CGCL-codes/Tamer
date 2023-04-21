package au.com.cahaya.asas.ds.party.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import au.com.cahaya.asas.ds.StandardKeyModel;
import au.com.cahaya.asas.ds.party.model.types.PartyTypesCache;
import au.com.cahaya.asas.ds.party.model.types.PartyUsageType;

/**
 *
 *
 * @author Mathew Pole
 * @since June 2008
 * @version $Revision$
 */
@Entity
@Table(name = "partycontact")
@AttributeOverrides({ @AttributeOverride(name = "myKey", column = @Column(name = "pcokey")), @AttributeOverride(name = "myActive", column = @Column(name = "pcoactivedate")), @AttributeOverride(name = "myInactive", column = @Column(name = "pcoinactivedate")), @AttributeOverride(name = "myUsername", column = @Column(name = "pcousername")), @AttributeOverride(name = "myIpAddress", column = @Column(name = "pcoipaddress")), @AttributeOverride(name = "myTimestamp", column = @Column(name = "pcotimestamp")) })
@NamedQueries({ @NamedQuery(name = PartyContactModel.cQueryByPartyContact, query = "select pco from PartyContactModel pco join pco.myContact con where con.myContact = :contact and pco.myParty = :party"), @NamedQuery(name = PartyContactModel.cQueryContactSimilar, query = "select pco from PartyContactModel pco join pco.myContact con where con.myContact like :contact") })
public class PartyContactModel extends StandardKeyModel {

    /**  */
    public static final String cQueryByPartyContact = "pcoByPartyContact";

    /**  */
    public static final String cQueryContactSimilar = "pcoContactSimilar";

    /** The private logger for this class */
    @Transient
    private Logger myLog = LoggerFactory.getLogger(PartyContactModel.class);

    /** Party that this contact is for */
    @ManyToOne
    @JoinColumn(name = "pcoptykey", nullable = false, insertable = true, updatable = true)
    private PartyModel myParty;

    /** Contact details */
    @ManyToOne
    @JoinColumn(name = "pcoconkey", nullable = false, insertable = true, updatable = false)
    private ContactModel myContact;

    /** The manner in which this contact should be used. */
    @ManyToOne
    @JoinColumn(name = "pcoputkey", nullable = false, insertable = true, updatable = true)
    private PartyUsageTypeModel myUsageType;

    /** Is this a preferred way of contacting this party? */
    @Column(name = "pcopreferred")
    private boolean myPreferred;

    /**
   * Constructor
   */
    public PartyContactModel() {
        super();
    }

    /**
   * @param party
   * @param contact
   * @param usageType
   * @param preferred
   * @param username
   */
    public PartyContactModel(PartyModel party, ContactModel contact, PartyUsageTypeModel usageType, boolean preferred, String username) {
        this();
        myParty = party;
        myContact = contact;
        myUsageType = usageType;
        myPreferred = preferred;
    }

    /**
   * @param party
   * @param contact
   * @param usageType
   * @param preferred
   * @param username
   */
    public PartyContactModel(PartyModel party, ContactModel contact, PartyUsageType usageType, boolean preferred, String username) {
        this(party, contact, PartyTypesCache.instance().getPartyUsageType(usageType), preferred, username);
    }

    /**
   * @return the party
   */
    public PartyModel getParty() {
        return myParty;
    }

    /**
   * @param party the party to set
   */
    public void setParty(PartyModel party) {
        myParty = party;
    }

    /**
   * @return the contact
   */
    public ContactModel getContact() {
        return myContact;
    }

    /**
   * @param contact the contact to set
   */
    public void setContact(ContactModel contact) {
        myContact = contact;
    }

    /**
   * @return the usageType
   */
    public PartyUsageTypeModel getUsageType() {
        return myUsageType;
    }

    /**
   * @param usageType the usageType to set
   */
    public void setUsageType(PartyUsageTypeModel usageType) {
        myUsageType = usageType;
    }

    /**
   * @return the preferred
   */
    public boolean isPreferred() {
        return myPreferred;
    }

    /**
   * @param preferred the preferred to set
   */
    public void setPreferred(boolean preferred) {
        myPreferred = preferred;
    }

    /**
   * Are the details of this name record the same?
   * This checks only name, family name, given name and type.
   */
    public boolean equalDetails(PartyContactModel partyContact) {
        return ((getUsageType() == null && partyContact.getUsageType() == null) || (getUsageType() != null && getUsageType().equals(partyContact.getUsageType()))) && (isPreferred() && partyContact.isPreferred()) && ((getContact().getContact() == null && partyContact.getContact().getContact() == null) || (getContact().getContact() != null && getContact().equalDetails(partyContact.getContact())));
    }

    /**
   * 
   */
    public String toString() {
        StringBuffer sb = new StringBuffer(myContact.toString());
        sb.append(" (");
        if (isPreferred()) {
            sb.append("p,");
        }
        sb.append(getUsageType());
        sb.append(")");
        if (!isActive()) {
            sb.append(" ");
            sb.append(cInactiveIndicator);
        }
        return sb.toString();
    }
}
