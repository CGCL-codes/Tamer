package net.sf.mpango.common.directory.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import net.sf.mpango.common.directory.enums.StateEnum;

/**
 * <p>
 * Class representing the information of a user.
 * </p>
 * 
 * @author etux
 * 
 */
@Entity(name = "User")
@Table(name = "USERS")
public class User {

    private Long identifier;

    private String email;

    private String username;

    private String password;

    private String resetKey;

    private Date dateOfBirth;

    private String gender;

    private String nonceToken;

    private StateEnum state;

    /**
	 * @return
	 */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATE", columnDefinition = "numeric(3,0)", nullable = false)
    public StateEnum getState() {
        return state;
    }

    /**
	 * @param state
	 */
    public void setState(StateEnum state) {
        this.state = state;
    }

    /**
	 * @return
	 */
    @Column(name = "PASSWORD", nullable = false)
    public String getPassword() {
        return password;
    }

    /**
	 * @param password
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * @return
	 */
    @Column(name = "RESETKEY", nullable = true)
    public String getResetKey() {
        return resetKey;
    }

    /**
	 * @param key for changing password
	 */
    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    /**
	 * @return
	 */
    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTH_DATE")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
	 * @param dateOfBirth
	 */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
	 * @return
	 */
    @Column(name = "GENDER")
    public String getGender() {
        return gender;
    }

    /**
	 * @param gender
	 */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
	 * @param username
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * @return
	 */
    @Column(name = "USERNAME", nullable = false)
    public String getUsername() {
        return username;
    }

    /**
	 * @param email
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * @return
	 */
    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    /**
	 * @param identifier
	 */
    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    /**
	 * @return
	 */
    @Id
    @GeneratedValue
    @Column(name = "ID")
    public Long getIdentifier() {
        return identifier;
    }

    /**
	 * @param nonceToken
	 */
    public void setNonceToken(String nonceToken) {
        this.nonceToken = nonceToken;
    }

    /**
	 * @return
	 */
    @Column(name = "NONCE", unique = true)
    public String getNonceToken() {
        return nonceToken;
    }

    @Override
    public String toString() {
        return "User [identifier=" + identifier + ", email=" + email + ", username=" + username + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        User other = (User) obj;
        if (email == null) {
            if (other.email != null) return false;
        } else if (!email.equals(other.email)) return false;
        return true;
    }
}
