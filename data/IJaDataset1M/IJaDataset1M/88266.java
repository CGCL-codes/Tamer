package com.fivebrms.hibernate.entity.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "w9Form")
public class W9Form implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contNum")
    private long contNum;

    @Column(name = "pin")
    private String pin;

    @Column(name = "dateCreated")
    private Timestamp dateCreated;

    @Column(name = "dateModifiedLast")
    private Timestamp dateModifiedLast;

    @Column(name = "status")
    private String status;

    @Column(name = "isIndividual")
    private boolean isIndividual;

    @Column(name = "isPartnership")
    private boolean isPartnership;

    @Column(name = "indivPartName")
    private String indivPartName;

    @Column(name = "indivPartSsNum")
    private String indivPartSsNum;

    @Column(name = "isCorporation")
    private boolean isCorporation;

    @Column(name = "isBusiness")
    private boolean isBusiness;

    @Column(name = "corpBusName")
    private String corpBusName;

    @Column(name = "corpBusIdentification")
    private String corpBusIdentification;

    @Column(name = "corpBusAddress")
    private String corpBusAddress;

    @Column(name = "signature")
    public String signature;

    public long getContNum() {
        return contNum;
    }

    public void setContNum(long contNum) {
        this.contNum = contNum;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModifiedLast() {
        return dateModifiedLast;
    }

    public void setDateModifiedLast(Timestamp dateModifiedLast) {
        this.dateModifiedLast = dateModifiedLast;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIndividual() {
        return isIndividual;
    }

    public void setIndividual(boolean isIndividual) {
        this.isIndividual = isIndividual;
    }

    public boolean isPartnership() {
        return isPartnership;
    }

    public void setPartnership(boolean isPartnership) {
        this.isPartnership = isPartnership;
    }

    public String getIndivPartName() {
        return indivPartName;
    }

    public void setIndivPartName(String indivPartName) {
        this.indivPartName = indivPartName;
    }

    public String getIndivPartSsNum() {
        return indivPartSsNum;
    }

    public void setIndivPartSsNum(String indivPartSsNum) {
        this.indivPartSsNum = indivPartSsNum;
    }

    public boolean isCorporation() {
        return isCorporation;
    }

    public void setCorporation(boolean isCorporation) {
        this.isCorporation = isCorporation;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    public void setBusiness(boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    public String getCorpBusName() {
        return corpBusName;
    }

    public void setCorpBusName(String corpBusName) {
        this.corpBusName = corpBusName;
    }

    public String getCorpBusIdentification() {
        return corpBusIdentification;
    }

    public void setCorpBusIdentification(String corpBusIdentification) {
        this.corpBusIdentification = corpBusIdentification;
    }

    public String getCorpBusAddress() {
        return corpBusAddress;
    }

    public void setCorpBusAddress(String corpBusAddress) {
        this.corpBusAddress = corpBusAddress;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
