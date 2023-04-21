package org.acegisecurity.providers.ldap.populator;

import org.acegisecurity.providers.ldap.LdapAuthoritiesPopulator;
import org.acegisecurity.ldap.LdapDataAccessException;
import org.acegisecurity.ldap.InitialDirContextFactory;
import org.acegisecurity.ldap.LdapUtils;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import javax.naming.directory.Attributes;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.directory.DirContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.Set;
import java.util.HashSet;

/**
 * The default strategy for obtaining user role information from the directory.
 * <p>
 * It obtains roles by
 * <ul>
 * <li>Reading the values of the roles specified by the attribute names in the
 * <tt>userRoleAttributes</tt> </li>
 * <li>Performing a search for "groups" the user is a member of and adding
 * those to the list of roles.</li>
 * </ul>
 * </p>
 * <p>
 * If the <tt>userRolesAttributes</tt> property is set, any matching
 * attributes amongst those retrieved for the user will have their values added
 * to the list of roles.
 * If <tt>userRolesAttributes</tt> is null, no attributes will be mapped to roles.
 * </p>
 * <p>
 * A typical group search scenario would be where each group/role is specified using
 * the <tt>groupOfNames</tt> (or <tt>groupOfUniqueNames</tt>) LDAP objectClass
 * and the user's DN is listed in the <tt>member</tt> (or <tt>uniqueMember</tt>) attribute
 * to indicate that they should be assigned that role. The following LDIF sample
 * has the groups stored under the DN <tt>ou=groups,dc=acegisecurity,dc=org</tt>
 * and a group called "developers" with "ben" and "marissa" as members:
 *
 * <pre>
 * dn: ou=groups,dc=acegisecurity,dc=org
 * objectClass: top
 * objectClass: organizationalUnit
 * ou: groups
 *
 * dn: cn=developers,ou=groups,dc=acegisecurity,dc=org
 * objectClass: groupOfNames
 * objectClass: top
 * cn: developers
 * description: Acegi Security Developers
 * member: uid=ben,ou=people,dc=acegisecurity,dc=org
 * member: uid=marissa,ou=people,dc=acegisecurity,dc=org
 * ou: developer
 * </pre>
 * </p>
 * <p>
 * The group search is performed within a DN specified by the <tt>groupSearchBase</tt>
 * property, which should be relative to the root DN of its <tt>InitialDirContextFactory</tt>.
 * If the search base is null, group searching is disabled. The filter used in the search is defined by the
 * <tt>groupSearchFilter</tt> property, with the filter argument {0} being the full DN of the user. You can also specify which attribute defines the role name by
 * setting the <tt>groupRoleAttribute</tt> property (the default is "cn").
 * </p>
 * <p>
 * The configuration below shows how the group search might be performed with the above schema.
 * <pre>
 * &lt;bean id="ldapAuthoritiesPopulator" class="org.acegisecurity.providers.ldap.populator.DefaultLdapAuthoritiesPopulator">
 *   &lt;constructor-arg>&lt;ref local="initialDirContextFactory"/>&lt;/constructor-arg>
 *   &lt;constructor-arg>&lt;value>ou=groups&lt;/value>&lt;/constructor-arg>
 *   &lt;property name="groupRoleAttribute">&lt;value>ou&lt;/value>&lt;/property>
 *
 * &lt;!-- the following properties are shown with their default values -->
 *
 *   &lt;property name="searchSubTree">&lt;value>false&lt;/value>&lt;/property>
 *   &lt;property name="rolePrefix">&lt;value>ROLE_&lt;/value>&lt;/property>
 *   &lt;property name="convertToUpperCase">&lt;value>true&lt;/value>&lt;/property>
 * &lt;/bean>
 * </pre>
 * A search for roles for user "uid=ben,ou=people,dc=acegisecurity,dc=org" would return the single
 * granted authority "ROLE_DEVELOPER".
 * </p>
 *
 *
 * @author Luke Taylor
 * @version $Id: DefaultLdapAuthoritiesPopulator.java,v 1.10 2006/04/16 16:00:32 luke_t Exp $
 */
public class DefaultLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    private static final Log logger = LogFactory.getLog(DefaultLdapAuthoritiesPopulator.class);

    /** Attributes of the User's LDAP Object that contain role name information. */
    private String[] userRoleAttributes = null;

    private String rolePrefix = "ROLE_";

    /** The base DN from which the search for group membership should be performed */
    private String groupSearchBase = null;

    /** The pattern to be used for the user search. {0} is the user's DN */
    private String groupSearchFilter = "(member={0})";

    /** The ID of the attribute which contains the role name for a group */
    private String groupRoleAttribute = "cn";

    /** Internal variable, tied to searchSubTree property */
    private int searchScope = SearchControls.ONELEVEL_SCOPE;

    private boolean convertToUpperCase = true;

    /** A default role which will be assigned to all authenticated users if set */
    private GrantedAuthority defaultRole = null;

    /** An initial context factory is only required if searching for groups is required. */
    private InitialDirContextFactory initialDirContextFactory = null;

    /**
     * Constructor for non-group search scenarios. Typically in this case
     * the <tt>userRoleAttributes</tt> property will be set to obtain roles directly
     * from the user's directory entry attributes. The <tt>defaultRole</tt> property
     * may also be set and will be assigned to all users.
     */
    public DefaultLdapAuthoritiesPopulator() {
    }

    /**
     * Constructor for group search scenarios. <tt>userRoleAttributes</tt> may still be
     * set as a property.
     *
     * @param initialDirContextFactory supplies the contexts used to search for user roles.
     * @param groupSearchBase if this is an empty string the search will be performed from the root DN of the
     * context factory.
     */
    public DefaultLdapAuthoritiesPopulator(InitialDirContextFactory initialDirContextFactory, String groupSearchBase) {
        Assert.notNull(initialDirContextFactory, "InitialDirContextFactory must not be null");
        Assert.notNull(groupSearchBase, "The groupSearchBase (name to search under), must not be null.");
        this.initialDirContextFactory = initialDirContextFactory;
        this.groupSearchBase = groupSearchBase;
        if (groupSearchBase.length() == 0) {
            logger.info("groupSearchBase is empty. Searches will be performed from the root: " + initialDirContextFactory.getRootDn());
        }
    }

    /**
     *
     * @param username the login name passed to the authentication provider.
     * @param userDn the user's DN.
     * @param userAttributes the attributes retrieved from the user's directory entry.
     * @return the full set of roles granted to the user.
     */
    public GrantedAuthority[] getGrantedAuthorities(String username, String userDn, Attributes userAttributes) {
        logger.debug("Getting authorities for user " + userDn);
        Set roles = getRolesFromUserAttributes(userDn, userAttributes);
        Set groupRoles = getGroupMembershipRoles(userDn, userAttributes);
        if (groupRoles != null) {
            roles.addAll(groupRoles);
        }
        if (defaultRole != null) {
            roles.add(defaultRole);
        }
        return (GrantedAuthority[]) roles.toArray(new GrantedAuthority[roles.size()]);
    }

    protected Set getRolesFromUserAttributes(String userDn, Attributes userAttributes) {
        Set userRoles = new HashSet();
        for (int i = 0; userRoleAttributes != null && i < userRoleAttributes.length; i++) {
            Attribute roleAttribute = userAttributes.get(userRoleAttributes[i]);
            addAttributeValuesToRoleSet(roleAttribute, userRoles);
        }
        return userRoles;
    }

    /**
     * Searches for groups the user is a member of.
     *
     * @param userDn the user's distinguished name.
     * @param userAttributes the retrieved user's attributes (unused by default).
     * @return the set of roles obtained from a group membership search, or null if
     *         <tt>groupSearchBase</tt> has been set.
     */
    protected Set getGroupMembershipRoles(String userDn, Attributes userAttributes) {
        Set userRoles = new HashSet();
        if (groupSearchBase == null) {
            return null;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Searching for roles for user '" + userDn + "', with filter " + groupSearchFilter + " in search base '" + groupSearchBase + "'");
        }
        DirContext ctx = initialDirContextFactory.newInitialDirContext();
        SearchControls ctls = new SearchControls();
        ctls.setSearchScope(searchScope);
        ctls.setReturningAttributes(new String[] { groupRoleAttribute });
        try {
            NamingEnumeration groups = ctx.search(groupSearchBase, groupSearchFilter, new String[] { userDn }, ctls);
            while (groups.hasMore()) {
                SearchResult result = (SearchResult) groups.next();
                Attributes attrs = result.getAttributes();
                NamingEnumeration groupRoleAttributes = attrs.getAll();
                while (groupRoleAttributes.hasMore()) {
                    Attribute roleAttribute = (Attribute) groupRoleAttributes.next();
                    addAttributeValuesToRoleSet(roleAttribute, userRoles);
                }
            }
        } catch (NamingException e) {
            throw new LdapDataAccessException("Group search failed for user " + userDn, e);
        } finally {
            LdapUtils.closeContext(ctx);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Roles from search: " + userRoles);
        }
        return userRoles;
    }

    private void addAttributeValuesToRoleSet(Attribute roleAttribute, Set roles) {
        if (roleAttribute == null) {
            return;
        }
        try {
            NamingEnumeration attributeRoles = roleAttribute.getAll();
            while (attributeRoles.hasMore()) {
                Object role = attributeRoles.next();
                if (role instanceof String) {
                    if (convertToUpperCase) {
                        role = ((String) role).toUpperCase();
                    }
                    roles.add(new GrantedAuthorityImpl(rolePrefix + role));
                } else {
                    logger.warn("Non-String value found for role attribute " + roleAttribute.getID());
                }
            }
        } catch (NamingException ne) {
            throw new LdapDataAccessException("Error retrieving values for role attribute " + roleAttribute.getID(), ne);
        }
    }

    protected InitialDirContextFactory getInitialDirContextFactory() {
        return initialDirContextFactory;
    }

    protected String[] getUserRoleAttributes() {
        return userRoleAttributes;
    }

    public void setUserRoleAttributes(String[] userRoleAttributes) {
        this.userRoleAttributes = userRoleAttributes;
    }

    public void setRolePrefix(String rolePrefix) {
        Assert.notNull(rolePrefix, "rolePrefix must not be null");
        this.rolePrefix = rolePrefix;
    }

    public void setGroupSearchFilter(String groupSearchFilter) {
        Assert.notNull(groupSearchFilter, "groupSearchFilter must not be null");
        this.groupSearchFilter = groupSearchFilter;
    }

    public void setGroupRoleAttribute(String groupRoleAttribute) {
        Assert.notNull(groupRoleAttribute, "groupRoleAttribute must not be null");
        this.groupRoleAttribute = groupRoleAttribute;
    }

    public void setSearchSubtree(boolean searchSubtree) {
        this.searchScope = searchSubtree ? SearchControls.SUBTREE_SCOPE : SearchControls.ONELEVEL_SCOPE;
    }

    public void setConvertToUpperCase(boolean convertToUpperCase) {
        this.convertToUpperCase = convertToUpperCase;
    }

    /**
     * The default role which will be assigned to all users.
     *
     * @param defaultRole the role name, including any desired prefix.
     */
    public void setDefaultRole(String defaultRole) {
        Assert.notNull(defaultRole, "The defaultRole property cannot be set to null");
        this.defaultRole = new GrantedAuthorityImpl(defaultRole);
    }
}
