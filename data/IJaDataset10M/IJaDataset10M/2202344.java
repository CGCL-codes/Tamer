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
 * Administrator entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "Administrator", schema = "dbo", catalog = "StudentManager")
public class Administrator implements java.io.Serializable {

    private Long adminId;

    private College college;

    private String adminName;

    private String adminPassword;

    private Timestamp lastLoginTime;

    /** default constructor */
    public Administrator() {
    }

    /** minimal constructor */
    public Administrator(Long adminId, String adminName, String adminPassword) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminPassword = adminPassword;
    }

    /** full constructor */
    public Administrator(Long adminId, College college, String adminName, String adminPassword, Timestamp lastLoginTime) {
        this.adminId = adminId;
        this.college = college;
        this.adminName = adminName;
        this.adminPassword = adminPassword;
        this.lastLoginTime = lastLoginTime;
    }

    @Id
    @Column(name = "Admin_id", unique = true, nullable = false, precision = 18, scale = 0)
    @GeneratedValue
    public Long getAdminId() {
        return this.adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "College_id")
    public College getCollege() {
        return this.college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    @Column(name = "Admin_Name", nullable = false, length = 20)
    public String getAdminName() {
        return this.adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Column(name = "Admin_Password", nullable = false, length = 20)
    public String getAdminPassword() {
        return this.adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Column(name = "LastLoginTime", length = 23)
    public Timestamp getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
