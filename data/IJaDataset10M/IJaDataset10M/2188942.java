package hszt.tbd.pimp.persistency.hibernate.vo;

import hszt.tbd.pimp.vo.Address;
import hszt.tbd.pimp.vo.Login;
import hszt.tbd.pimp.vo.Role;
import hszt.tbd.pimp.vo.User;
import java.util.ArrayList;
import java.util.List;

/**
 * represents a user which comes from the db
 * 
 * @author Beat Durrer - dube@sonnenkinder.org
 */
public class UserImpl implements User {

    Login login;

    Integer id;

    List<Address> addresses;

    List<Role> roles;

    public UserImpl(Login aLogin) {
        login = aLogin;
        addresses = new ArrayList<Address>();
        roles = new ArrayList<Role>();
    }

    public UserImpl() {
        this(new Login());
    }

    public String getPassword() {
        return login.getPassword();
    }

    public String getUsername() {
        return login.getUsername();
    }

    public void setPassword(String aPassword) {
        login.setPassword(aPassword);
    }

    public void setUsername(String aUsername) {
        login.setUsername(aUsername);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer aId) {
        id = aId;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login aLogin) {
        login = aLogin;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> aRoles) {
        roles = aRoles;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    @SuppressWarnings("unchecked")
    public void setAddresses(List aAddresses) {
        addresses = aAddresses;
    }

    /**
	 * adds a new address to this user. don't forget to make it persistent!
	 * @param anAddress
	 */
    public void addAddress(Address anAddress) {
        ((AddressImpl) anAddress).setUserImpl(this);
        addresses.add(anAddress);
    }
}
