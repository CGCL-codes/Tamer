package org.broadleafcommerce.gwt.server.security.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import org.broadleafcommerce.gwt.client.presentation.SupportedFieldType;
import org.broadleafcommerce.presentation.AdminPresentation;
import org.broadleafcommerce.presentation.ConfigurationItem;
import org.broadleafcommerce.presentation.ValidationConfiguration;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * 
 * @author jfischer
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_ADMIN_USER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
public class AdminUserImpl implements AdminUser {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "AdminUserId", strategy = GenerationType.TABLE)
    @TableGenerator(name = "AdminUserId", table = "SEQUENCE_GENERATOR", pkColumnName = "ID_NAME", valueColumnName = "ID_VAL", pkColumnValue = "AdminUserImpl", allocationSize = 50)
    @Column(name = "ADMIN_USER_ID")
    @AdminPresentation(friendlyName = "Admin User ID", group = "Primary Key", hidden = true)
    private Long id;

    @Column(name = "NAME", nullable = false)
    @Index(name = "ADMINUSER_NAME_INDEX", columnNames = { "NAME" })
    @AdminPresentation(friendlyName = "Name", order = 1, group = "User", prominent = true)
    protected String name;

    @Column(name = "LOGIN", nullable = false)
    @AdminPresentation(friendlyName = "Login", order = 2, group = "User", prominent = true)
    protected String login;

    @Column(name = "PASSWORD", nullable = false)
    @AdminPresentation(friendlyName = "Password", order = 3, group = "User", fieldType = SupportedFieldType.PASSWORD, validationConfigurations = { @ValidationConfiguration(validationImplementation = "com.smartgwt.client.widgets.form.validator.MatchesFieldValidator", configurationItems = { @ConfigurationItem(itemName = "errorMessageKey", itemValue = "passwordNotMatchError") }) })
    protected String password;

    @Column(name = "EMAIL", nullable = false)
    @Index(name = "ADMINPERM_EMAIL_INDEX", columnNames = { "EMAIL" })
    @AdminPresentation(friendlyName = "Email Address", order = 4, group = "User")
    protected String email;

    /** All roles that this user has */
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminRoleImpl.class)
    @JoinTable(name = "BLC_ADMIN_USER_ROLE_XREF", joinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
    @BatchSize(size = 50)
    protected Set<AdminRole> allRoles = new HashSet<AdminRole>();

    @Transient
    protected String unencodedPassword;

    public String getUnencodedPassword() {
        return unencodedPassword;
    }

    public void setUnencodedPassword(String unencodedPassword) {
        this.unencodedPassword = unencodedPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<AdminRole> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(Set<AdminRole> allRoles) {
        this.allRoles = allRoles;
    }
}
