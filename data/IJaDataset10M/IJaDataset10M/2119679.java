package org.apache.shindig.social.opensocial.jpa;

import org.apache.shindig.social.opensocial.model.Person;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;

@Entity
@Table(name = "person_address")
@DiscriminatorValue(value = "sharedaddress")
public class PersonAddressDb extends AddressDb {

    @Basic
    @Column(name = "primary_address")
    private Boolean primary;

    @ManyToOne(targetEntity = PersonDb.class)
    @JoinColumn(name = "person_id", referencedColumnName = "oid")
    private Person person;

    @Basic
    @Column(name = "type", length = 255)
    private String type;

    public PersonAddressDb() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    /**
   * @return the person
   */
    public Person getPerson() {
        return person;
    }

    /**
   * @param person the person to set
   */
    public void setPerson(Person person) {
        this.person = person;
    }
}
