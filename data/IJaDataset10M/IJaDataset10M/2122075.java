package com.student.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * DormRpAssociation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "Dorm_RP_Association", schema = "dbo", catalog = "StudentManager")
public class DormRpAssociation implements java.io.Serializable {

    private Long dormRpAssociationId;

    private RpList rpList;

    private Dorm dorm;

    private Timestamp dormRpTime;

    /** default constructor */
    public DormRpAssociation() {
    }

    /** minimal constructor */
    public DormRpAssociation(Long dormRpAssociationId) {
        this.dormRpAssociationId = dormRpAssociationId;
    }

    /** full constructor */
    public DormRpAssociation(Long dormRpAssociationId, RpList rpList, Dorm dorm, Timestamp dormRpTime) {
        this.dormRpAssociationId = dormRpAssociationId;
        this.rpList = rpList;
        this.dorm = dorm;
        this.dormRpTime = dormRpTime;
    }

    @Id
    @Column(name = "Dorm_RP_Association_id", unique = true, nullable = false, precision = 18, scale = 0)
    @GeneratedValue
    public Long getDormRpAssociationId() {
        return this.dormRpAssociationId;
    }

    public void setDormRpAssociationId(Long dormRpAssociationId) {
        this.dormRpAssociationId = dormRpAssociationId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RP_id")
    public RpList getRpList() {
        return this.rpList;
    }

    public void setRpList(RpList rpList) {
        this.rpList = rpList;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Dorm_id")
    public Dorm getDorm() {
        return this.dorm;
    }

    public void setDorm(Dorm dorm) {
        this.dorm = dorm;
    }

    @Column(name = "Dorm_RP_Time", length = 23)
    public Timestamp getDormRpTime() {
        return this.dormRpTime;
    }

    public void setDormRpTime(Timestamp dormRpTime) {
        this.dormRpTime = dormRpTime;
    }
}
