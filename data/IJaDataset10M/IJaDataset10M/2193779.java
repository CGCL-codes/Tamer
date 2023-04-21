package org.cloudlet.web.security.server.domain;

import org.cloudlet.web.service.server.jpa.BaseDomain;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class User extends BaseDomain {

    @NotNull(message = "你必须指定用户名")
    private String userName;

    private String password;

    private String passwordSalt;

    @OneToMany
    private List<Role> roles;

    public String getPassword() {
        return password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getUserName() {
        return userName;
    }

    public User setPassword(final String password) {
        this.password = password;
        return this;
    }

    public User setPasswordSalt(final String passwordSalt) {
        this.passwordSalt = passwordSalt;
        return this;
    }

    public User setRoles(final List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public User setUserName(final String userName) {
        this.userName = userName;
        return this;
    }
}
