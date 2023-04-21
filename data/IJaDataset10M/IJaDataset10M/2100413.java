package net.sf.mp.demo.conference.domain.admin;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;
import org.openxava.annotations.*;
import net.sf.mp.demo.conference.domain.conference.Address;

/**
 *
 * <p>Title: Country</p>
 *
 * <p>Description: Domain Object describing a Country entity</p>
 *
 */
@Entity(name = "Country")
@Table(name = "country")
@Views({ @View(name = "base", members = "" + "id  ; " + "name  ; " + "isoName  ; "), @View(name = "Create", extendsView = "base"), @View(name = "Update", extendsView = "base", members = ""), @View(extendsView = "base", members = ""), @View(name = "countryDEFAULT_VIEW", members = " id ;" + "name  ; " + "isoName  ; ") })
@Tabs({ @Tab(properties = " name " + ",  isoName "), @Tab(name = "CountryTab", properties = " name " + ",  isoName "), @Tab(name = "CountryTabWithRef", properties = " name " + ",  isoName ") })
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Country {

    @Hidden
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 45, nullable = false, unique = false)
    @Required
    private String name;

    @Column(name = "iso_name", length = 45, nullable = false, unique = false)
    @Required
    private String isoName;

    @OneToMany(targetEntity = net.sf.mp.demo.conference.domain.conference.Address.class, fetch = FetchType.LAZY, mappedBy = "countryId", cascade = CascadeType.REMOVE)
    private Set<Address> addresses = new HashSet<Address>();

    /**
    * Default constructor
    */
    public Country() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoName() {
        return isoName;
    }

    public void setIsoName(String isoName) {
        this.isoName = isoName;
    }

    public Set<Address> getAddresses() {
        if (addresses == null) {
            addresses = new HashSet<Address>();
        }
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public void addAddresses(Address address) {
        getAddresses().add(address);
    }
}
