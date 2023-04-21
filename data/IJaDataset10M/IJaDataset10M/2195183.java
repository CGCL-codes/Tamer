package org.kwantu.appbrowser.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import org.kwantu.persistence.AbstractPersistentObject;

/**
 * KwantuApplicationUser references the logged in user and keeps a Set of 
 * {@link MyKwantuApplication} that belongs to the logged user.
 * 
 * @author siviwe
 */
@Entity
public class KwantuApplicationUser extends AbstractPersistentObject implements Serializable {

    private String username;

    private Set<MyKwantuApplication> userOwnedMyKwantuApplications;

    @OneToMany(mappedBy = "owningUser", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public Set<MyKwantuApplication> getUserOwnedMyKwantuApplications() {
        if (userOwnedMyKwantuApplications == null) {
            userOwnedMyKwantuApplications = new HashSet<MyKwantuApplication>();
        }
        return userOwnedMyKwantuApplications;
    }

    public void setUserOwnedMyKwantuApplications(final Set<MyKwantuApplication> userOwnedMyKwantuApplications) {
        this.userOwnedMyKwantuApplications = userOwnedMyKwantuApplications;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public boolean hasApplication(final KwantuApplication kwantuApplication) {
        for (MyKwantuApplication myApp : this.getUserOwnedMyKwantuApplications()) {
            if (kwantuApplication == myApp.getOwningKwantuApplication()) {
                return true;
            }
        }
        return false;
    }
}
