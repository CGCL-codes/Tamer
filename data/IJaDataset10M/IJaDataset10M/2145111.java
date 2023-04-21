package org.vardb.util.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.Email;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.vardb.util.CStringHelper;

@MappedSuperclass
@SuppressWarnings("serial")
public class CUserDetails implements UserDetails {

    protected String id;

    protected String username;

    protected String password = "";

    protected Boolean enabled = true;

    protected Boolean anonymous = false;

    protected Boolean administrator = false;

    protected String salt;

    protected String firstname = "";

    protected String lastname = "";

    protected String email = "";

    protected String affiliation = "";

    protected Date created = new Date();

    protected Date updated = new Date();

    ;

    public CUserDetails() {
    }

    @Column(insertable = false, updatable = false)
    @JsonProperty
    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @JsonProperty
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty
    public Boolean getAnonymous() {
        return this.anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    @JsonProperty
    public Boolean getAdministrator() {
        return this.administrator;
    }

    public void setAdministrator(Boolean administrator) {
        this.administrator = administrator;
    }

    @JsonProperty
    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @JsonProperty
    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Email
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty
    public String getAffiliation() {
        return this.affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(final Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return this.updated;
    }

    public void setUpdated(final Date updated) {
        this.updated = updated;
    }

    @Transient
    @JsonProperty
    public String getName() {
        if (this.anonymous) return "guest";
        if (CStringHelper.isEmpty(this.firstname) && CStringHelper.isEmpty(this.lastname)) return this.username;
        String name = this.firstname + " " + this.lastname;
        return name.trim();
    }

    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    public List<String> getRoles() {
        List<String> roles = new ArrayList<String>();
        roles.add("ROLE_USER");
        if (!this.anonymous) roles.add("ROLE_LOGIN_USER");
        if (this.administrator) roles.add("ROLE_ADMIN");
        return roles;
    }

    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
        IAcegiService acegi = new CAcegiHelper();
        return acegi.getAuthorities(getRoles());
    }

    public String encodePassword(String password, PasswordEncoder passwordEncoder, ReflectionSaltSource saltSource) {
        String encoded = passwordEncoder.encodePassword(password, saltSource.getSalt(this));
        System.out.println("password=" + password + ", encoded=" + encoded);
        return encoded;
    }

    public void setPassword(String password, PasswordEncoder passwordEncoder, ReflectionSaltSource saltSource) {
        setPassword(encodePassword(password, passwordEncoder, saltSource));
    }
}
