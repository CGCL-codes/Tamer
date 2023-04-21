package com.alesj.blade.data;

import org.jboss.seam.annotations.Name;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Set;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
@Entity
@Name("customer")
@Inheritance
@DiscriminatorValue("customer")
public class Customer extends User {

    String address;

    String city;

    String zip;

    String country;

    String email;

    String phone;

    Double defaultWidth;

    Double defaultHeight;

    Set<Request> inprogressRequests;

    Set<Request> finishedRequests;

    Set<Request> historyRequests;

    public Customer() {
    }

    @Column(name = "ADDRESS", length = 50)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "CITY", length = 50)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "ZIP", length = 50)
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Column(name = "COUNTRY", length = 50)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "EMAIL", length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "PHONE", length = 50)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getDefaultWidth() {
        return defaultWidth;
    }

    public void setDefaultWidth(Double defaultWidth) {
        this.defaultWidth = defaultWidth;
    }

    public Double getDefaultHeight() {
        return defaultHeight;
    }

    public void setDefaultHeight(Double defaultHeight) {
        this.defaultHeight = defaultHeight;
    }

    @OneToMany(mappedBy = "customer")
    @Where(clause = "processDate is null")
    @Cascade(value = org.hibernate.annotations.CascadeType.LOCK)
    public Set<Request> getInprogressRequests() {
        return inprogressRequests;
    }

    public void setInprogressRequests(Set<Request> inprogressRequests) {
        this.inprogressRequests = inprogressRequests;
    }

    @OneToMany(mappedBy = "customer")
    @Where(clause = "processDate is not null AND viewDate is null")
    @Cascade(value = org.hibernate.annotations.CascadeType.LOCK)
    public Set<Request> getFinishedRequests() {
        return finishedRequests;
    }

    public void setFinishedRequests(Set<Request> finishedRequests) {
        this.finishedRequests = finishedRequests;
    }

    @OneToMany(mappedBy = "customer")
    @Where(clause = "processDate is not null AND viewDate is not null")
    @Cascade(value = org.hibernate.annotations.CascadeType.LOCK)
    public Set<Request> getHistoryRequests() {
        return historyRequests;
    }

    public void setHistoryRequests(Set<Request> historyRequests) {
        this.historyRequests = historyRequests;
    }

    public String toString() {
        return "Customer#" + getId() + "(" + userName + ")";
    }
}
