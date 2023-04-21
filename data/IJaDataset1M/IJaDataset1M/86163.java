package security;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import users.domain.Role;
import users.domain.User;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Class that is used by the UserDetailsServiceImpl
 * (spring-manage-acegi-security.xml) to auth the user against the roles
 */
public class UserDetailsAdapter implements UserDetails {

    private User aUser;

    private List<Role> roles;

    /**
   * Creates a new AuthUser object.
   * 
   * @param user required
   */
    public UserDetailsAdapter(User user, List<Role> roles) {
        super();
        aUser = user;
        this.roles = roles;
    }

    /**
   * @see java.lang.Object#toString()
   */
    public String toString() {
        return getUsername();
    }

    /**
   * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
   */
    public GrantedAuthority[] getAuthorities() {
        return getAuthRoles();
    }

    /**
   * Converts RoleAdapter objects from Role domain objects.
   * 
   * @return not null
   */
    private RoleAdapter[] getAuthRoles() {
        RoleAdapter[] auths = new RoleAdapter[this.roles.size()];
        int i = 0;
        for (Iterator<Role> iter = this.roles.iterator(); iter.hasNext(); i++) {
            Role aRole = iter.next();
            auths[i] = new RoleAdapter(aRole);
        }
        return auths;
    }

    /**
   * @see org.acegisecurity.userdetails.UserDetails#isAccountNonExpired()
   */
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
   * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
   */
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
   * @see org.acegisecurity.userdetails.UserDetails#isCredentialsNonExpired()
   */
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
   * @see org.acegisecurity.userdetails.UserDetails#isEnabled()
   */
    public boolean isEnabled() {
        return true;
    }

    /**
   * @see org.acegisecurity.userdetails.UserDetails#getPassword()
   */
    public String getPassword() {
        return aUser.getPassword();
    }

    /**
   * @see org.acegisecurity.userdetails.UserDetails#getUsername()
   */
    public String getUsername() {
        return aUser.getUsername();
    }

    /**
   * @return returns a user built from the user
   */
    public User getUser() {
        return aUser;
    }
}
