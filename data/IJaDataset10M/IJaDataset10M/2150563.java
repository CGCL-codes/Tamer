package org.apache.shindig.social.opensocial.jpa;

import org.apache.shindig.social.opensocial.model.Person;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Phone Entity, extends the ListField object (and list_field table), joining on the object ID.
 * Objects of this type will have "list_field_type" set to PhoneDb in list_field
 */
@Entity
@Table(name = "phone")
@PrimaryKeyJoinColumn(name = "oid")
@NamedQuery(name = PhoneDb.FINDBY_PHONE_NUMBER, query = "select p from PhoneDb p where p.value = :phonenumber ")
public class PhoneDb extends ListFieldDb {

    /**
   * The name of the JPA query to find phone numbers by phone number (bit odd)
   */
    public static final String FINDBY_PHONE_NUMBER = "q.pphone.findbynumber";

    /**
   * The name of the phone number parameter in JPA queries
   */
    public static final String PARAM_PHONE_NUMBER = "phonenumber";

    /**
   * The person who is associated with this phone number.
   */
    @ManyToOne(targetEntity = PersonDb.class)
    @JoinColumn(name = "person_id", referencedColumnName = "oid")
    protected Person person;
}
