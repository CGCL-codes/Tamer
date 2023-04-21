package org.escapek.core.dto.security;

import org.escapek.core.dto.AbstractDTO;

/**
 * 
 * @author nicolasjouanin
 *
 */
public class UserDTO extends AbstractDTO {

    private static final long serialVersionUID = -4040382525637910883L;

    private Long Id;

    private String loginString;

    private String description;

    private String password;

    public UserDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        firePropertyChange("description", this.description, description);
        this.description = description;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getLoginString() {
        return loginString;
    }

    public void setLoginString(String login) {
        firePropertyChange("loginString", this.loginString, loginString);
        this.loginString = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        firePropertyChange("password", this.password, password);
        this.password = password;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ((obj == null) || !(obj instanceof UserDTO)) return false;
        UserDTO test = (UserDTO) obj;
        return Id == test.getId() || (Id != null && Id.equals(test.getId()));
    }
}
