package au.edu.diasb.springsecurity;

import java.util.Collection;
import java.util.Properties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * A generic user details object for externally authenticated principals 
 * who don't have an account in our user details store.
 * 
 * @author scrawley
 */
public class ExternalUserDetails implements UserDetails {

    private static final long serialVersionUID = -4814684350652160497L;

    private Collection<GrantedAuthority> authorities;

    private String userName;

    private String userId;

    private Properties attributes;

    public ExternalUserDetails(String userName, String userId, Collection<GrantedAuthority> authorities, Properties attributes) {
        super();
        this.authorities = authorities;
        this.userName = userName;
        this.userId = userId;
        this.attributes = attributes;
    }

    @Override
    public final Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public final String getPassword() {
        return "N/A";
    }

    @Override
    public final String getUsername() {
        return userName;
    }

    public final String getUserId() {
        return userId;
    }

    @Override
    public final boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public final boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public final boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public final boolean isEnabled() {
        return true;
    }

    public final Properties getAttributes() {
        return attributes;
    }

    @Override
    public final String toString() {
        return "ExternalUserDetails{userId=" + userId + ",userName=" + userName + ",attributes=" + attributes + ",authorities=" + authorities + "}";
    }
}
