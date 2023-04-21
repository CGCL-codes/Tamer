package info.gdeDengi.common;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the authorities database table.
 * 
 */
@Embeddable
public class AuthorityPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, length = 50)
    private String authority;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    public AuthorityPK() {
    }

    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AuthorityPK)) {
            return false;
        }
        AuthorityPK castOther = (AuthorityPK) other;
        return this.authority.equals(castOther.authority) && this.username.equals(castOther.username);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.authority.hashCode();
        hash = hash * prime + this.username.hashCode();
        return hash;
    }
}
