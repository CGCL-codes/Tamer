package net.larsbehnke.petclinicplus.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;

/**
 * Persistent user capable of collaborating with Acegi security.
 * <p>
 * Corresponding mapping:
 * </p>
 * 
 * <pre>
 *  &lt;entity class=&quot;User&quot;&gt;
 *        &lt;table name=&quot;USERS&quot;&gt;
 *        &lt;/table&gt;
 *        &lt;attributes&gt;
 *            &lt;basic name=&quot;firstName&quot;&gt;
 *                &lt;column name=&quot;FIRST_NAME&quot; /&gt;
 *            &lt;/basic&gt;
 *            &lt;basic name=&quot;lastName&quot;&gt;
 *                &lt;column name=&quot;LAST_NAME&quot; /&gt;
 *            &lt;/basic&gt;
 *            &lt;basic name=&quot;loginName&quot;&gt;
 *                &lt;column name=&quot;LOGIN_NAME&quot; unique=&quot;true&quot;/&gt;
 *            &lt;/basic&gt;
 *            &lt;basic name=&quot;password&quot;&gt;
 *                &lt;column name=&quot;PASSWORD&quot; /&gt;
 *            &lt;/basic&gt;
 *            &lt;basic name=&quot;credentialsNonExpired&quot;&gt;
 *                &lt;column name=&quot;CRED_NON_EXP&quot; /&gt;
 *            &lt;/basic&gt;
 *            &lt;basic name=&quot;accountNonLocked&quot;&gt;
 *                &lt;column name=&quot;ACC_NON_LOCKED&quot; /&gt;
 *            &lt;/basic&gt;
 *            &lt;basic name=&quot;accountNonExpired&quot;&gt;
 *                &lt;column name=&quot;ACC_NON_EXP&quot; /&gt;
 *            &lt;/basic&gt;
 *            &lt;basic name=&quot;enabled&quot;&gt;
 *                &lt;column name=&quot;ENABLED&quot; /&gt;
 *            &lt;/basic&gt;
 *            &lt;many-to-many name=&quot;rolesInternal&quot; target-entity=&quot;Role&quot; fetch=&quot;EAGER&quot;&gt;
 *                &lt;join-table name=&quot;USER_ROLES&quot;&gt;
 *                    &lt;join-column name=&quot;USER_ID&quot; /&gt;
 *                    &lt;inverse-join-column name=&quot;ROLE_ID&quot; /&gt;
 *                &lt;/join-table&gt;
 *                &lt;cascade&gt;
 *                    &lt;cascade-all /&gt;
 *                &lt;/cascade&gt;
 *            &lt;/many-to-many&gt;
 * 
 *            &lt;transient name=&quot;authorities&quot; /&gt;
 * 
 *        &lt;/attributes&gt;
 *    &lt;/entity&gt;
 * </pre>
 * 
 * @author Lars Behnke
 */
@Entity
@Table(name = "USERS")
public class User extends BaseEntity implements UserDetails, Serializable {

    private static final long serialVersionUID = -1306581235758909330L;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Set<Role> rolesInternal;

    private boolean credentialsNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean accountNonExpired = true;

    private boolean enabled = true;

    public void addRole(String role) {
        getRolesInternal().add(new Role(role));
    }

    @Transient
    public GrantedAuthority[] getAuthorities() {
        return getRolesInternal().toArray(new GrantedAuthority[] {});
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return this.firstName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return this.lastName;
    }

    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    protected Set<Role> getRolesInternal() {
        if (rolesInternal == null) {
            rolesInternal = new HashSet<Role>();
        }
        return rolesInternal;
    }

    @Column(name = "LOGIN_NAME", unique = true)
    public String getUsername() {
        return username;
    }

    @Column(name = "ACC_NON_EXP")
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Column(name = "ACC_NON_LOCKED")
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Column(name = "CRED_NON_EXP")
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Column(name = "ENABLED")
    public boolean isEnabled() {
        return enabled;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    protected void setRolesInternal(Set<Role> roles) {
        this.rolesInternal = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
