package com.vmware.spring.workshop.dto.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import com.vmware.spring.workshop.dto.AbstractNamedIdentifiedDTO;
import com.vmware.spring.workshop.dto.LocatedDTO;

/**
 * @author lgoldstein
 */
@XmlRootElement(name = "user")
@XmlType(name = "user")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class UserDTO extends AbstractNamedIdentifiedDTO implements LocatedDTO, Cloneable {

    private static final long serialVersionUID = -6783252908584943839L;

    private String _loginName, _password, _homeAddress;

    private UserRoleTypeDTO _role;

    public UserDTO() {
        super();
    }

    @XmlAttribute(name = "loginName", required = true)
    public String getLoginName() {
        return _loginName;
    }

    public void setLoginName(String loginName) {
        _loginName = loginName;
    }

    @XmlAttribute(name = "password", required = true)
    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    @XmlAttribute(name = "role", required = true)
    public UserRoleTypeDTO getRole() {
        return _role;
    }

    public void setRole(UserRoleTypeDTO role) {
        _role = role;
    }

    @XmlElement(name = "homeAddress", required = true, nillable = false)
    public String getHomeAddress() {
        return _homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        _homeAddress = homeAddress;
    }

    @Override
    @XmlTransient
    public String getLocation() {
        return getHomeAddress();
    }

    @Override
    public void setLocation(String location) {
        setHomeAddress(location);
    }

    @Override
    public UserDTO clone() {
        try {
            return getClass().cast(super.clone());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Failed to clone " + this + ": " + e.getMessage(), e);
        }
    }
}
