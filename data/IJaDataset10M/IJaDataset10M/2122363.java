package org.libreplan.business.common.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.libreplan.business.common.BaseEntity;
import org.libreplan.business.users.entities.UserRole;

/**
 *
 * This entity will be used to store the LDAP connection properties for
 * authentication
 *
 * @author Ignacio Diaz Teijido <ignacio.diaz@comtecsf.es>
 * @author Cristina Alvarino Perez<cristina.alvarino@comtecsf.es>
 *
 */
public class LDAPConfiguration extends BaseEntity {

    public static LDAPConfiguration create() {
        return create(new LDAPConfiguration());
    }

    private String ldapUserId;

    private String ldapHost;

    private String ldapPort;

    private String ldapBase;

    private String ldapUserDn;

    private String ldapPassword;

    private String ldapGroupPath;

    private String ldapRoleProperty;

    private Boolean ldapSavePasswordsDB = true;

    private Boolean ldapAuthEnabled = false;

    private Boolean ldapSaveRolesDB = false;

    private Boolean ldapGroupStrategy = true;

    /**
     * A list which stores the matching between LDAP roles and LibrePlan roles.
     * {@link ConfigurationRolesLDAP} is a component.
     *
     * For each matching a new {@link ConfigurationRolesLDAP} would be stored in
     * this list.
     *
     * E.g., if we have that ROLE_ADMINISTRATION in LibrePlan matches with
     * admins and editors roles in LDAP there will be 2 objects of
     * {@link ConfigurationRolesLDAP} class: ROLE_ADMINISTRATION - admins and
     * ROLE_ADMINISTRATION - editors
     */
    private Set<ConfigurationRolesLDAP> configurationRolesLdap = new HashSet<ConfigurationRolesLDAP>();

    private Map<String, List<String>> mapMatchingRoles = new HashMap<String, List<String>>();

    private String ldapSearchQuery;

    public String getLdapUserId() {
        return ldapUserId;
    }

    public void setLdapUserId(String ldapUserId) {
        this.ldapUserId = ldapUserId;
    }

    public String getLdapHost() {
        return ldapHost;
    }

    public void setLdapHost(String ldapHost) {
        this.ldapHost = ldapHost;
    }

    public String getLdapPort() {
        return ldapPort;
    }

    public void setLdapPort(String ldapPort) {
        this.ldapPort = ldapPort;
    }

    public String getLdapBase() {
        return ldapBase;
    }

    public void setLdapBase(String ldapBase) {
        this.ldapBase = ldapBase;
    }

    public String getLdapUserDn() {
        return ldapUserDn;
    }

    public void setLdapUserDn(String ldapUserDn) {
        this.ldapUserDn = ldapUserDn;
    }

    public String getLdapPassword() {
        return ldapPassword;
    }

    public void setLdapPassword(String ldapPassword) {
        this.ldapPassword = ldapPassword;
    }

    public Boolean isLdapSavePasswordsDB() {
        return ldapSavePasswordsDB;
    }

    public void setLdapSavePasswordsDB(Boolean ldapSavePasswordsDB) {
        this.ldapSavePasswordsDB = ldapSavePasswordsDB;
    }

    public Boolean getLdapAuthEnabled() {
        return ldapAuthEnabled;
    }

    public void setLdapAuthEnabled(Boolean ldapAuthEnabled) {
        this.ldapAuthEnabled = ldapAuthEnabled;
    }

    public Boolean getLdapSaveRolesDB() {
        return ldapSaveRolesDB;
    }

    public void setLdapSaveRolesDB(Boolean ldapSaveRolesDB) {
        this.ldapSaveRolesDB = ldapSaveRolesDB;
    }

    public Boolean getLdapSavePasswordsDB() {
        return ldapSavePasswordsDB;
    }

    public String getLdapGroupPath() {
        return ldapGroupPath;
    }

    public void setLdapGroupPath(String ldapGroupPath) {
        this.ldapGroupPath = ldapGroupPath;
    }

    public String getLdapRoleProperty() {
        return ldapRoleProperty;
    }

    public void setLdapRoleProperty(String ldapRoleProperty) {
        this.ldapRoleProperty = ldapRoleProperty;
    }

    public Set<ConfigurationRolesLDAP> getConfigurationRolesLdap() {
        return Collections.unmodifiableSet(configurationRolesLdap);
    }

    public void setConfigurationRolesLdap(String roleLibreplan, Set<String> rolesLdap) {
        removeConfigurationRolesLdapForRoleLibreplan(roleLibreplan);
        for (String roleLdap : rolesLdap) {
            configurationRolesLdap.add(new ConfigurationRolesLDAP(roleLdap, roleLibreplan));
        }
    }

    private void removeConfigurationRolesLdapForRoleLibreplan(String roleLibreplan) {
        List<ConfigurationRolesLDAP> toRemove = new ArrayList<ConfigurationRolesLDAP>();
        for (ConfigurationRolesLDAP each : configurationRolesLdap) {
            if (each.getRoleLibreplan().equals(roleLibreplan)) {
                toRemove.add(each);
            }
        }
        configurationRolesLdap.removeAll(toRemove);
    }

    public Map<String, List<String>> getMapMatchingRoles() {
        for (UserRole role : UserRole.values()) {
            List<String> listRolesLdap = new ArrayList<String>();
            for (ConfigurationRolesLDAP roleLdap : this.configurationRolesLdap) {
                if (roleLdap != null && role.name().equals(roleLdap.getRoleLibreplan())) {
                    listRolesLdap.add(roleLdap.getRoleLdap());
                }
            }
            mapMatchingRoles.put(role.name(), listRolesLdap);
        }
        return mapMatchingRoles;
    }

    public void setMapMatchingRoles(Map<String, List<String>> mapMatchingRoles) {
        this.mapMatchingRoles = mapMatchingRoles;
    }

    /**
     * @return the ldapSearchQuery
     */
    public String getLdapSearchQuery() {
        return ldapSearchQuery;
    }

    /**
     * @param ldapSearchQuery
     *            the ldapSearchQuery to set
     */
    public void setLdapSearchQuery(String ldapSearchQuery) {
        this.ldapSearchQuery = ldapSearchQuery;
    }

    public Boolean getLdapGroupStrategy() {
        return ldapGroupStrategy;
    }

    public void setLdapGroupStrategy(Boolean ldapGroupStrategy) {
        this.ldapGroupStrategy = ldapGroupStrategy;
    }
}
