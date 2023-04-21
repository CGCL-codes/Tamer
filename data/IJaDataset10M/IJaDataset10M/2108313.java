package net.sourceforge.simpleworklog.shared.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ignat Alexeyenko
 *         Date: 16.01.2010
 */
public class UserDetails implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private boolean logged;

    private Set<Role> roles;

    public UserDetails() {
    }

    public UserDetails(Long id, String firstName, String lastName, boolean logged, Set<Role> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.logged = logged;
        this.roles = new HashSet<Role>();
        this.roles.addAll(roles);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isLogged() {
        return logged;
    }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }
}
