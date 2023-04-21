package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author user
 */
@Entity
@Table(name = "ISAE_USERS", catalog = "", schema = "APP")
@NamedQueries({ @NamedQuery(name = "IsaeUsers.findAll", query = "SELECT i FROM IsaeUsers i"), @NamedQuery(name = "IsaeUsers.findByUserId", query = "SELECT i FROM IsaeUsers i WHERE i.userId = :userId"), @NamedQuery(name = "IsaeUsers.findByUserName", query = "SELECT i FROM IsaeUsers i WHERE i.userName = :userName"), @NamedQuery(name = "IsaeUsers.findByUserPassword", query = "SELECT i FROM IsaeUsers i WHERE i.userPassword = :userPassword") })
public class IsaeUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "USER_ID", nullable = false, length = 25)
    private String userId;

    @Column(name = "USER_NAME", length = 50)
    private String userName;

    @Column(name = "USER_PASSWORD", length = 25)
    private String userPassword;

    public IsaeUsers() {
    }

    public IsaeUsers(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IsaeUsers)) {
            return false;
        }
        IsaeUsers other = (IsaeUsers) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.IsaeUsers[userId=" + userId + "]";
    }
}
