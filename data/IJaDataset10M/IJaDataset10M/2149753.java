package org.authorsite.mailarchive.model.impl;

import java.text.*;
import java.util.*;
import org.apache.commons.lang.builder.*;
import org.apache.log4j.*;
import org.authorsite.mailarchive.model.*;

/**
 * Plain Java implementation of <code>Person</code> interface.
 * 
 * @hibernate.query name="PersonMainName" query="select from PersonImpl as person where person.mainName = :mainName"
 * @hibernate.query name="PersonMainNameLike" query="select from PersonImpl as person where person.mainName like :mainName"
 * @hibernate.query name="PersonMainNameGivenName" query="select from PersonImpl as person where person.mainName = :mainName and person.givenName = :givenName"
 * @hibernate.query name="PersonMainNameGivenNameLike" query="select from PersonImpl as person where person.mainName like :mainName and person.givenName like :givenName"
 *  
 * @hibernate.class table="Person" proxy="org.authorsite.mailarchive.model.Person"
 * @hibernate.cache usage="nonstrict-read-write"
 * @author jejking
 * @version $Revision: 1.14 $
 */
public final class PersonImpl implements Person, Versionable, Comparable {

    private Integer ID;

    private Integer version;

    private String prefix;

    private String givenName;

    private String mainName;

    private String otherNames;

    private String suffix;

    private Date dateOfBirth;

    private Date dateOfDeath;

    private int genderCode;

    private Set emailAddresses;

    private static Logger logger = Logger.getLogger(PersonImpl.class);

    /**
	 * Default constructor.
	 *
	 */
    public PersonImpl() {
        emailAddresses = new HashSet();
    }

    public PersonImpl(String mainName) {
        this();
        setMainName(mainName);
    }

    public PersonImpl(String mainName, String givenName) {
        this(mainName);
        setGivenName(givenName);
    }

    public PersonImpl(String mainName, String givenName, String otherNames) {
        this(mainName, givenName);
        setOtherNames(otherNames);
    }

    /**
	 * @hibernate.id column="PersonID" generator-class="native"
	 * @see org.authorsite.mailarchive.model.Identifiable#getID()
	 */
    public Integer getID() {
        return ID;
    }

    /**
	 * @see org.authorsite.mailarchive.model.Identifiable#setID(int)
	 */
    public void setID(Integer newID) {
        logger.debug("Got new ID: " + newID);
        ID = newID;
    }

    /**
	 * @hibernate.version
	 * @see org.authorsite.mailarchive.model.impl.Versionable#getVersion()
	 */
    public Integer getVersion() {
        return version;
    }

    /**
	 * @see org.authorsite.mailarchive.model.impl.Versionable#setVersion(java.lang.Integer)
	 */
    public void setVersion(Integer newVersion) {
        version = newVersion;
    }

    /**
	 * @hibernate.property type="string" column="prefix" length="50"
	 * @see org.authorsite.mailarchive.domain.Person#getPrefix()
	 */
    public String getPrefix() {
        return prefix;
    }

    /**
	 * @see org.authorsite.mailarchive.domain.Person#setPrefix(java.lang.String)
	 */
    public void setPrefix(String newPrefix) {
        prefix = newPrefix;
    }

    /**
	 * @hibernate.property type="string" column="givenName" length="100"
         * @hibernate.column name="givenname" index="idx_given_name"
	 * @see org.authorsite.mailarchive.domain.Person#getGivenName()
	 */
    public String getGivenName() {
        return givenName;
    }

    /**
	 * @see org.authorsite.mailarchive.domain.Person#setGivenName(java.lang.String)
	 */
    public void setGivenName(String newGivenName) {
        givenName = newGivenName;
    }

    /**
	 * @hibernate.property type="string" column="mainName" length="500"
         * @hibernate.column name="mainname" index="idx_main_name"
	 * @see org.authorsite.mailarchive.domain.Person#getMainName()
	 */
    public String getMainName() {
        return mainName;
    }

    /**
	 * @see org.authorsite.mailarchive.domain.Person#setMainName(java.lang.String)
	 */
    public void setMainName(String newMainName) {
        mainName = newMainName;
    }

    /**
	 * @hibernate.property type="string" length="1000"
	 * @see org.authorsite.mailarchive.domain.Person#getOtherNames()
	 */
    public String getOtherNames() {
        return otherNames;
    }

    /**
	 * @see org.authorsite.mailarchive.domain.Person#setOtherNames(java.lang.String)
	 */
    public void setOtherNames(String newOtherNames) {
        otherNames = newOtherNames;
    }

    /**
	 * @hibernate.property type="string" length="100"
	 * @see org.authorsite.mailarchive.domain.Person#getSuffix()
	 */
    public String getSuffix() {
        return suffix;
    }

    /**
	 * @see org.authorsite.mailarchive.domain.Person#setSuffix(java.lang.String)
	 */
    public void setSuffix(String newSuffix) {
        suffix = newSuffix;
    }

    /**
	 * @hibernate.property type="integer"
	 * @see org.authorsite.mailarchive.domain.Person#getGenderCode()
	 */
    public int getGenderCode() {
        return genderCode;
    }

    /**
	 * Sets the gender code according to ISO 5218.
	 * 
	 * <p>If an invalid is passed in, sets the gender code to 0, "not known".</p> 
	 * 
	 * @see org.authorsite.mailarchive.domain.Person#setGenderCode(int)
	 * @param newGenderCode 0 for not known, 1 for male, 2 for female, 9 for not specfied
	 */
    public void setGenderCode(int newGenderCode) {
        if (newGenderCode < 0 || (newGenderCode > 2 && newGenderCode != 9)) {
            logger.debug("Received invalid GenderCode: " + newGenderCode + ", setting to 0, PersonImpl " + getID());
            newGenderCode = 0;
        }
        genderCode = newGenderCode;
    }

    /**
	 * @hibernate.property type="date" 
	 * @see org.authorsite.mailarchive.domain.Person#getDateOfBirth()
	 * @return clone of date of birth or null
	 */
    public Date getDateOfBirth() {
        if (dateOfBirth == null) {
            return null;
        } else {
            return dateOfBirth;
        }
    }

    /**
	 * Sets person's date of birth.
	 * 
	 * @param newDateOfBirth date of birth, must not be null or after dateOfDeath
	 * @see org.authorsite.mailarchive.domain.Person#setDateOfBirth(java.util.Date)
	 * @throws IllegalArgumentException of newDateOfBirth is null or after date of death, if this is set.
	 */
    public void setDateOfBirth(Date newDateOfBirth) {
        if (newDateOfBirth != null && dateOfDeath != null) {
            if (newDateOfBirth.after(dateOfDeath)) {
                logger.warn("Received dateOfBirth after dateOfDeath, PersonImpl " + getID());
                throw new IllegalArgumentException("Person cannot normally pass through time in reverse.");
            }
        }
        dateOfBirth = newDateOfBirth;
    }

    /**
	 * @hibernate.property type="date"
	 * @see org.authorsite.mailarchive.domain.Person#getDateOfDeath()
	 * @return clone of date of death or null
	 */
    public Date getDateOfDeath() {
        if (dateOfDeath == null) {
            return null;
        } else {
            return (Date) dateOfDeath.clone();
        }
    }

    /**
	 * Sets person's date of death.
	 * 
	 * @see org.authorsite.mailarchive.domain.Person#setDateOfDeath(java.util.Date)
	 * @param newDateOfDeath, must not be null or before dateOfBirth
	 * @throws IllegalArgumentException if newDateOfDeath is null or before dateOfBirth
	 */
    public void setDateOfDeath(Date newDateOfDeath) {
        if (newDateOfDeath != null && dateOfBirth != null) {
            if (newDateOfDeath.before(dateOfBirth)) {
                logger.warn("Received dateOfDeath before dateOfBirth, PersonImpl " + getID());
                throw new IllegalArgumentException("Person cannot normally pass through time in reverse");
            }
        }
        dateOfDeath = newDateOfDeath;
    }

    /**
	 * @hibernate.set inverse="true"
	 * @hibernate.collection-one-to-many class="org.authorsite.mailarchive.model.impl.EmailAddressImpl"
	 * @hibernate.collection-key column="PersonID"
         * @hibernate.collection-cache usage="nonstrict-read-write"
	 *  
	 * @see org.authorsite.mailarchive.model.Person#getEmailAddresses()
	 * @return unmodifiable set of <code>EmailAddressImpl</code> instances
	 */
    public Set getEmailAddresses() {
        return emailAddresses;
    }

    /**
	 * @see org.authorsite.mailarchive.model.Person#setEmailAddresses(java.util.Set)
	 * @param Set of <code>EmailAddress</code> instances
	 * @throws IllegalArgumentException if Set contains reference to any object which is not an instance of <code>EmailAddress</code> of if the <code>EmailAddress</code>
	 * contains a reference to a different <code>Person</code>
	 */
    public void setEmailAddresses(Set newEmailAddresses) {
        Iterator it = newEmailAddresses.iterator();
        while (it.hasNext()) {
            try {
                EmailAddress addr = (EmailAddress) it.next();
                if (addr.getPerson() == null || addr.getPerson().equals(this)) {
                    continue;
                } else {
                    logger.warn("Set newEmailAddresses contained reference to person who is not this person, PersonImpl " + getID());
                    throw new IllegalArgumentException("Email Address contained reference to a person who is not this person. Identity crisis!");
                }
            } catch (ClassCastException cce) {
                logger.warn("Set newEmailAddresses contained referenc to an object which is not an instancse of EmailAddress " + getID());
                throw new IllegalArgumentException("Set newEmailAddresses contained reference to an object which is not an instance of EmailAddress");
            }
        }
        emailAddresses = newEmailAddresses;
    }

    /**
	 * @see org.authorsite.mailarchive.model.Person#addEmailAddress(org.authorsite.mailarchive.model.EmailAddress)
	 * @throws IllegalArgumentException if the <code>EmailAddress</code> contains a reference to a person who is not this person, or null
	 */
    public void addEmailAddress(EmailAddress newEmailAddress) {
        if (newEmailAddress != null) {
            if (newEmailAddress.getPerson() != null && !newEmailAddress.getPerson().equals(this)) {
                logger.warn("newEmailAddress contained reference to person who is not this person, PersonImpl " + getID());
                throw new IllegalArgumentException("newEmailAddress is allocated to a person who is not this person. Identity crisis!");
            } else {
                emailAddresses.add(newEmailAddress);
            }
        }
    }

    /**
	 * @see org.authorsite.mailarchive.model.Person#removeEmailAddress(org.authorsite.mailarchive.model.EmailAddress)
	 * @throws IllegalArgumentException if emailAddressToRemove contains reference to a person who is not this person
	 */
    public void removeEmailAddress(EmailAddress emailAddressToRemove) {
        if (emailAddressToRemove == null) {
            return;
        }
        if (emailAddressToRemove.getPerson() != null && !emailAddressToRemove.getPerson().equals(this)) {
            logger.warn("emailAddressToRemove contained reference to person who is not this person, PersonImpl " + getID());
            throw new IllegalArgumentException("emailAddressToRemove is allocated to a person who is not this person. Identity crisis!");
        } else {
            emailAddresses.remove(emailAddressToRemove);
        }
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Person) {
            Person input = (Person) o;
            return new EqualsBuilder().append(givenName, input.getGivenName()).append(mainName, input.getMainName()).append(otherNames, input.getOtherNames()).append(dateOfBirth, input.getDateOfBirth()).append(dateOfDeath, input.getDateOfDeath()).isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(givenName).append(mainName).append(otherNames).append(dateOfBirth).append(dateOfDeath).toHashCode();
    }

    public String toString() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        StringBuffer sb = new StringBuffer();
        sb.append("PersonImpl: ");
        sb.append("\n");
        sb.append("Given Name:\t");
        sb.append(getGivenName());
        sb.append("\n");
        sb.append("Main Name:\t");
        sb.append(getMainName());
        sb.append("Other Names: \t");
        sb.append(getOtherNames());
        sb.append("Born: \t");
        if (dateOfBirth != null) {
            sb.append(df.format(dateOfBirth));
        }
        sb.append("\n");
        sb.append("Died:\t");
        if (dateOfDeath != null) {
            sb.append(df.format(dateOfDeath));
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(Object o) {
        PersonImpl input = (PersonImpl) o;
        return new CompareToBuilder().append(givenName, input.givenName).append(mainName, input.mainName).append(otherNames, input.otherNames).append(dateOfBirth, input.dateOfBirth).append(dateOfDeath, input.dateOfDeath).toComparison();
    }
}
