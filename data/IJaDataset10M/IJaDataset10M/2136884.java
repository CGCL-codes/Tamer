package de.lema.bo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;
import org.hibernate.annotations.ForeignKey;

@Entity
@NamedQueries({ @NamedQuery(name = "LemaUserPermission.alle", query = "from de.lema.bo.LemaUserPermission c order by c.username"), @NamedQuery(name = "LemaUserPermission.alleMitRolle", query = "from de.lema.bo.LemaUserPermission c where c.role.id=:role") })
@PersistenceUnits(@PersistenceUnit(unitName = "lema"))
public class LemaUserPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String username;

    @ManyToOne(cascade = {  }, fetch = FetchType.EAGER, optional = true)
    @ForeignKey(name = "FK_USER_PERMISSION_ROLE")
    private LemaSecurityRole role;

    @Column(nullable = true)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null) {
            this.username = username.toUpperCase().trim();
        } else {
            this.username = null;
        }
    }

    public LemaSecurityRole getRole() {
        return role;
    }

    public void setRole(LemaSecurityRole role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LemaUserPermission other = (LemaUserPermission) obj;
        if (username == null) {
            if (other.username != null) return false;
        } else if (!username.equals(other.username)) return false;
        return true;
    }
}
