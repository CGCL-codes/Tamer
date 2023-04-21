package com.faralam.apptsvc.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "affiliatedServiceProvider")
@Entity
@Table(name = "affiliatedServiceProvider")
public class AffiliatedServiceProvider {

    private enum Type {

        SELF, INTERNAL, EXTERNAL
    }

    private Integer idaffiliatedServiceProvider;

    private Integer sA_idserviceAccommodator;

    private Type type;

    private String contactName;

    private String contactPhone;

    private String level;

    private Set<Service> services = new HashSet<Service>();

    private ServiceAccommodator serviceAccomodator;

    public AffiliatedServiceProvider() {
    }

    @Id
    public Integer getIdaffiliatedServiceProvider() {
        return idaffiliatedServiceProvider;
    }

    public void setIdaffiliatedServiceProvider(Integer idaffiliatedServiceProvider) {
        this.idaffiliatedServiceProvider = idaffiliatedServiceProvider;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "serviceProvider")
    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sA_idserviceAccommodator", nullable = false, insertable = false, updatable = false)
    public ServiceAccommodator getServiceAccomodator() {
        return serviceAccomodator;
    }

    public void setServiceAccomodator(ServiceAccommodator serviceAccomodator) {
        this.serviceAccomodator = serviceAccomodator;
    }

    public Integer getsA_idserviceAccommodator() {
        return sA_idserviceAccommodator;
    }

    public void setsA_idserviceAccommodator(Integer sA_idserviceAccommodator) {
        this.sA_idserviceAccommodator = sA_idserviceAccommodator;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ServiceProvider :");
        buf.append(contactName);
        buf.append(" type=");
        buf.append(type);
        return buf.toString();
    }
}
