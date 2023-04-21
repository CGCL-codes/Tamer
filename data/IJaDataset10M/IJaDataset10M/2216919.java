package ar.com.omnipresence.security.client;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Martín Straus
 */
public class RoleTO implements Serializable {

    private String name;

    private String description;

    private Set<UserTO> users = new HashSet<UserTO>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserTO> users) {
        this.users = users;
    }
}
