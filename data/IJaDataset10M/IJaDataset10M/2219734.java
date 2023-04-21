package org.fao.fenix.domain.metadataitems;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Organization manage information about Provider and Source of a Resource
 * 
 * @author Tamburo
 */
@Entity
public class Organization {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String department;

    private String service;

    @OneToOne
    private Address addr;

    /**
     * 
     */
    public Organization() {
        super();
    }

    /**
     * * Code identify the Organization
     * 
     * @author Tamburo
     * @return Long
     */
    public Long getCode() {
        return id;
    }

    /**
     * * Set Code of the Organization
     * 
     * @author Tamburo
     * @return String
     */
    public void setCode(Long id) {
        this.id = id;
    }

    /**
     * * Department of the Organization
     * 
     * @author Tamburo
     * @return String
     */
    public String getDepartment() {
        return department;
    }

    /**
     * * Set department of the Organization
     * 
     * @author Tamburo
     * @return String
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * * Service of the Organization
     * 
     * @author Tamburo
     * @return String
     */
    public String getService() {
        return service;
    }

    /**
     * * Set service of the Organization
     * 
     * @author Tamburo
     * @return String
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return the addr
     */
    public Address getAddr() {
        return addr;
    }

    /**
     * @param addr
     *            the addr to set
     */
    public void setAddr(Address addr) {
        this.addr = addr;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
