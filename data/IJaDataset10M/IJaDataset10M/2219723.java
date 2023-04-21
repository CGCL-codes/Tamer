package com.yugte.ums.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.Pattern;

/**
 * @author Imran pariyani
 * 
 * {@link http://pariyani.com} pariyani
 */
@Entity
@Table(name = "user")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class User implements Serializable {

    private static final long serialVersionUID = 6916816325466379714L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * Timestamp of the creation of this user.
     */
    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Timestamp dateCreated;

    /**
     * Timestamp of the last change of this users's properties. (XXX DB does't
     * accept NOT NULL, DB does't accept two default values as
     * CURRENT_TIMESTAMP)
     */
    @Column(name = "last_updated_date", nullable = false, columnDefinition = "date_updated TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00'")
    private Timestamp dateUpdated;

    /**
	 * @return the id
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return the dateCreated
	 */
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    /**
	 * @param dateCreated the dateCreated to set
	 */
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
	 * @return the dateUpdated
	 */
    public Timestamp getDateUpdated() {
        return dateUpdated;
    }

    /**
	 * @param dateUpdated the dateUpdated to set
	 */
    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    /**
	 * @return the passwordHash
	 */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
	 * @param passwordHash the passwordHash to set
	 */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
	 * @return the userName
	 */
    public String getUserName() {
        return userName;
    }

    /**
	 * @param userName the userName to set
	 */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "user_password", length = 32, nullable = false, columnDefinition = "password varchar(32) DEFAULT NULL")
    private String passwordHash;

    @NotEmpty
    @Length(min = 4, max = 34)
    @Pattern(regex = "^[a-zA-Z0-9_]{4,34}$", message = "Invalid screen name.")
    private String userName;
}
