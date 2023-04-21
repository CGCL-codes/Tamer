package org.az.model;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

@Entity
@Table(name = "accounts")
public class Account extends IdEntityBean {

    private static final long serialVersionUID = 309692241512005722L;

    /**
     * aka EMAIL
     */
    private String username;

    private String password;

    private User defaultProfile;

    /**
     * Date of account creation.
     */
    private Date creationDate;

    /**
     * Last time the user was using his account.
     */
    private Date lastAccessDate;

    public enum Status {

        NEW, NOT_CONFIRMED, CONFIRMED, DISABLED, ERROR
    }

    @Field(index = Index.UN_TOKENIZED, store = Store.NO)
    private Status status = Status.NEW;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Account() {
        super();
        creationDate = new Date();
    }

    @Column(name = "pwd")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Column(unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    @JoinColumn(name = "defaultUserProfile", unique = true)
    @OneToOne(cascade = CascadeType.ALL)
    public User getDefaultProfile() {
        return defaultProfile;
    }

    public void setDefaultProfile(User defaultProfile) {
        this.defaultProfile = defaultProfile;
    }
}
