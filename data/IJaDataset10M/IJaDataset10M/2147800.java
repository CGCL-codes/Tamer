package org.jboss.tutorial.composite.bean;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

/**
 *
 */
@Entity
public class Customer implements java.io.Serializable {

    CustomerPK pk;

    Set<Flight> flights;

    public Customer() {
    }

    @EmbeddedId
    public CustomerPK getPk() {
        return pk;
    }

    public void setPk(CustomerPK pk) {
        this.pk = pk;
    }

    @Transient
    public String getName() {
        return pk.getName();
    }

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER, mappedBy = "customers")
    @JoinTable(name = "flight_customer_table", joinColumns = { @JoinColumn(name = "FLIGHT_ID") }, inverseJoinColumns = { @JoinColumn(name = "CUSTOMER_ID"), @JoinColumn(name = "CUSTOMER_NAME") })
    public Set<Flight> getFlights() {
        return flights;
    }

    public void setFlights(Set<Flight> flights) {
        this.flights = flights;
    }
}
