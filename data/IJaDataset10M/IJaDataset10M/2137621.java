package org.commonlibrary.lcms.model;

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.PatternTokenizerFactory;
import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A user of the application
 * <p/>
 * @author Jeff Wysong
 *         Date: Jul 29, 2008
 *         Time: 3:35:46 PM
 * @see Person
 */
@DataTransferObject(converter = org.directwebremoting.hibernate.H3BeanConverter.class)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "user")
@Entity(name = "CLv2User")
@Table(name = "clv2_users")
@Indexed(index = "clv2_users")
@AnalyzerDef(name = "email", tokenizer = @TokenizerDef(factory = PatternTokenizerFactory.class, params = { @Parameter(name = "pattern", value = "\\.|\\@") }), filters = { @TokenFilterDef(factory = LowerCaseFilterFactory.class) })
public class User extends Person implements UserDetails {

    /**
     * Username
     */
    @RemoteProperty
    @Column(unique = true)
    @Field(index = Index.TOKENIZED, store = Store.NO, analyzer = @Analyzer(definition = "email"))
    protected String username;

    /**
     * Password (will be clear for transient instances of User and encrypted for persistent instances)
     */
    @RemoteProperty
    protected String password;

    /**
     * Whether the user is enabled (allowed to log in) or not in the application
     */
    @RemoteProperty
    protected boolean enabled;

    /**
     * The user profile in the system
     */
    @OneToOne
    protected UserProfile userProfile;

    /**
     * Whether or not the user needs to change their password.
     */
    @RemoteProperty
    protected boolean passwordNeedChange;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *  WARNING - A transient user's password will appear clear.  All
     *  persisted users passwords will be encrypted.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns <code>true</code> if the user has been activated, <code>false</code> otherwise.
     *
     * @return whether the user is enabled or not
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @RemoteProperty
    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public boolean isPasswordNeedChange() {
        return passwordNeedChange;
    }

    public void setPasswordNeedChange(boolean passwordNeedChange) {
        this.passwordNeedChange = passwordNeedChange;
    }

    /**
     * TODO: implement
     *
     * @return array of granted authorities
     */
    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
        GrantedAuthority grantedAuthority = new GrantedAuthorityImpl("ROLE_USER");
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(grantedAuthority);
        return grantedAuthorities;
    }

    /**
     * TODO: implement
     *
     * @return whether the account has expired or not
     */
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * TODO: implement
     *
     * @return whether the account is locked or not
     */
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * TODO: implement
     *
     * @return whether the credentials expire or not
     */
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean onEquals(Object o) {
        if (o instanceof User) {
            User u = (User) o;
            return username == null ? u.getUsername() == null : username.equals(u.getUsername()) && password == null ? u.getPassword() == null : password.equals(u.getPassword());
        }
        return false;
    }

    public int onHashCode(int result) {
        result = 29 * result + (username != null ? username.hashCode() : 0);
        result = 29 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    protected StringBuilder toStringBuilder() {
        StringBuilder sb = super.toStringBuilder();
        sb.append(", username = ").append(getUsername());
        sb.append(", enabled = ").append(isEnabled());
        sb.append(", passwordNeedChange = ").append(isPasswordNeedChange());
        return sb;
    }
}
