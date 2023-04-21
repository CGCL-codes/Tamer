package net.sourceforge.simpleworklog.server.entity.persistent;

import net.sourceforge.simpleworklog.shared.entity.Role;
import java.util.Collections;
import java.util.Set;

/**
 * @author Ignat Alexeyenko
 *         Date: 09.01.2010
 *         Time: 11:21:40
 */
public class Account {

    private Long id;

    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles;
    }
}
