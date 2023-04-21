package net.cloneshop.md.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "pacientfile")
public class PatientFile implements Serializable, JPAEntity {

    private static final long serialVersionUID = 1L;

    private Long ID;

    private Date fileDate;

    private String fname;

    private String lname;

    private Date dateOfBirth;

    private Integer weight;

    private Integer height;

    private Boolean sex;

    private String ocupation;

    private String address;

    private String referedBy;

    private List<Appointment> appointmentList = new ArrayList<Appointment>(0);

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    @Column(name = "filedate")
    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    @Column(name = "fname")
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    @Column(name = "lname")
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    @Column(name = "dateofbirth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Column(name = "weight")
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Column(name = "height")
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Column(name = "sex")
    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    @Column(name = "ocupation")
    public String getOcupation() {
        return ocupation;
    }

    public void setOcupation(String ocupation) {
        this.ocupation = ocupation;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "referdBy")
    public void setReferedBy(String referedBy) {
        this.referedBy = referedBy;
    }

    public String getReferedBy() {
        return referedBy;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }
}
