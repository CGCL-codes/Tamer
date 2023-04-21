package org.hibernate.search.test.configuration;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class Country {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    @OneToMany(mappedBy = "country")
    private Set<Address> addresses = new HashSet<Address>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }
}
