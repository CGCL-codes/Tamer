package com.faralam.apptsvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "servicetype")
public class ServiceType {

    private Integer idserviceType;

    private String serviceTypeName;

    private String serviceDescription;

    private AgeRequirement ageRequirment;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idserviceType", unique = true, nullable = false)
    public Integer getIdserviceType() {
        return idserviceType;
    }

    public void setIdserviceType(Integer uUID) {
        idserviceType = uUID;
    }

    private enum AgeRequirement {

        G, PG13, R, XXX
    }

    @Column(name = "AgeRequirement")
    @Enumerated(EnumType.STRING)
    public AgeRequirement getAgeRequirment() {
        return ageRequirment;
    }

    public void setAgeRequirment(AgeRequirement ageRequirment) {
        this.ageRequirment = ageRequirment;
    }

    @Column(name = "ServiceTypeName")
    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceName) {
        serviceTypeName = serviceName;
    }

    @Column(name = "ServiceDescription")
    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("ServiceType: ");
        buf.append(ageRequirment);
        buf.append(" ");
        return buf.toString();
    }
}
