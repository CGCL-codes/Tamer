package se.devoteam.nexus.ldap;

import java.util.HashSet;
import java.util.Set;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.SizeLimitExceededException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.jsecurity.realm.ldap.LdapContextFactory;
import org.jsecurity.realm.ldap.LdapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.jsecurity.locators.users.PlexusUser;
import org.sonatype.jsecurity.locators.users.PlexusUserLocator;
import org.sonatype.jsecurity.locators.users.PlexusUserSearchCriteria;

/**
 * <code>PlexusUserLocator</code> implementation for the LDAP realm.
 * 
 * @author anders.hammar
 */
@Component(role = PlexusUserLocator.class, hint = "LDAP", description = "LDAP Users")
public class LdapUserLocator implements PlexusUserLocator {

    private static final String SOURCE = "LDAP";

    private final Logger logger = LoggerFactory.getLogger(NexusLdapContextFactory.class);

    @Requirement
    private LdapConfigurationManager configuration;

    @Requirement
    private LdapContextFactory ctxFactory;

    protected Logger getLogger() {
        return logger;
    }

    public String getSource() {
        return SOURCE;
    }

    public PlexusUser getUser(String userId) {
        getLogger().debug("Retrieving LDAP user '{}'", userId);
        PlexusUser user;
        LdapContext ctx = null;
        try {
            String searchFilterExpr = this.configuration.getSearchUserFilterExpr();
            Object[] searchFilterArgs = { userId };
            getLogger().debug("LDAP user search filter: {}", searchFilterExpr);
            String userIdNameAttrId = this.configuration.getUserIdAttribute();
            String nameNameAttrId = this.configuration.getNameAttribute();
            String emailAddressNameAttrId = this.configuration.getEmailAddressAttribute();
            String searchAttributes[] = { userIdNameAttrId, nameNameAttrId, emailAddressNameAttrId };
            ctx = this.ctxFactory.getSystemLdapContext();
            NamingEnumeration<SearchResult> answers = search(ctx, searchFilterExpr, searchFilterArgs, searchAttributes);
            if (answers.hasMore()) {
                SearchResult tmpAnswer = answers.next();
                Attributes attributes = tmpAnswer.getAttributes();
                user = convertToPlexusUser(attributes);
                getLogger().debug("User object retrieved through LDAP");
            } else {
                user = null;
                getLogger().debug("LDAP user not found");
            }
        } catch (PartialResultException e) {
            if (this.configuration.getIgnorePartialResultException()) {
                getLogger().debug("PartialResultException encountered and ignored while retrieving LDAP user", e);
                user = null;
                getLogger().debug("LDAP user not found");
            } else {
                getLogger().error("Error while retrieving LDAP user", e);
                throw new RuntimeException("Error while retrieving LDAP user", e);
            }
        } catch (NamingException e) {
            getLogger().error("Error while retrieving LDAP user", e);
            throw new RuntimeException("Error while retrieving LDAP user", e);
        } finally {
            LdapUtils.closeContext(ctx);
        }
        return user;
    }

    /**
	 * Always returns <code>true</code>, regardless of the realm is enabled or not.<br>
	 * The method will be removed in future versions of Nexus.
	 */
    public boolean isPrimary() {
        return true;
    }

    public Set<String> listUserIds() {
        getLogger().debug("Retrieving list of LDAP user IDs");
        Set<String> userIds = null;
        LdapContext ctx = null;
        try {
            String searchFilterExpr = this.configuration.getListUsersFilterExpr();
            getLogger().debug("LDAP user IDs search filter: {}", searchFilterExpr);
            String userIdNameAttrId = this.configuration.getUserIdAttribute();
            String searchAttributes[] = { userIdNameAttrId };
            ctx = this.ctxFactory.getSystemLdapContext();
            userIds = new HashSet<String>();
            NamingEnumeration<SearchResult> answers = search(ctx, searchFilterExpr, null, searchAttributes);
            while (answers.hasMore()) {
                SearchResult tmpAnswer = answers.next();
                Attributes attributes = tmpAnswer.getAttributes();
                Attribute attribute = attributes.get(userIdNameAttrId);
                userIds.add((String) attribute.get());
            }
        } catch (PartialResultException e) {
            if (this.configuration.getIgnorePartialResultException()) {
                getLogger().debug("PartialResultException encountered and ignored while retrieving list of LDAP user IDs", e);
            } else {
                getLogger().error("Error while retrieving list of LDAP user IDs", e);
                throw new RuntimeException("Error while retrieving list of LDAP user IDs", e);
            }
        } catch (SizeLimitExceededException e) {
            getLogger().debug("Size limit exceeded while retrieving list of LDAP user IDs - not all user IDs were retrieved");
        } catch (NamingException e) {
            getLogger().error("Error while retrieving list of LDAP user IDs", e);
            throw new RuntimeException("Error while retrieving list of LDAP user IDs", e);
        } finally {
            LdapUtils.closeContext(ctx);
        }
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("List of LDAP user IDs retrieved ({} users)", userIds.size());
        }
        return userIds;
    }

    public Set<PlexusUser> listUsers() {
        getLogger().debug("Retrieving list of LDAP users");
        Set<PlexusUser> users = null;
        LdapContext ctx = null;
        try {
            String searchFilterExpr = this.configuration.getListUsersFilterExpr();
            getLogger().debug("LDAP user IDs search filter: {}", searchFilterExpr);
            String userIdNameAttrId = this.configuration.getUserIdAttribute();
            String nameNameAttrId = this.configuration.getNameAttribute();
            String emailAddressNameAttrId = this.configuration.getEmailAddressAttribute();
            String searchAttributes[] = { userIdNameAttrId, nameNameAttrId, emailAddressNameAttrId };
            ctx = this.ctxFactory.getSystemLdapContext();
            users = new HashSet<PlexusUser>();
            NamingEnumeration<SearchResult> answers = search(ctx, searchFilterExpr, null, searchAttributes);
            while (answers.hasMore()) {
                SearchResult tmpAnswer = answers.next();
                Attributes attributes = tmpAnswer.getAttributes();
                users.add(convertToPlexusUser(attributes));
            }
        } catch (PartialResultException e) {
            if (this.configuration.getIgnorePartialResultException()) {
                getLogger().debug("PartialResultException encountered and ignored while retrieving list of LDAP users", e);
            } else {
                getLogger().error("Error while retrieving list of LDAP users", e);
                throw new RuntimeException("Error while retrieving list of LDAP users", e);
            }
        } catch (SizeLimitExceededException e) {
            getLogger().debug("Size limit exceeded while retrieving list of LDAP users - not all users were retrieved");
        } catch (NamingException e) {
            getLogger().error("Error while retrieving list of LDAP users", e);
            throw new RuntimeException("Error while retrieving list of LDAP users", e);
        } finally {
            LdapUtils.closeContext(ctx);
        }
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("List of LDAP users retrieved ({} users)", users.size());
        }
        return users;
    }

    public Set<PlexusUser> searchUsers(PlexusUserSearchCriteria criteria) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Searching for LDAP users (criteria: {})", getLogString(criteria));
        }
        Set<PlexusUser> users = null;
        LdapContext ctx = null;
        try {
            String searchFilterExpr = this.configuration.getSearchUsersFilterExpr();
            Object[] searchFilterArgs = { criteria.getUserId() };
            getLogger().debug("LDAP users search filter: {}", searchFilterExpr);
            String userIdNameAttrId = this.configuration.getUserIdAttribute();
            String nameNameAttrId = this.configuration.getNameAttribute();
            String emailAddressNameAttrId = this.configuration.getEmailAddressAttribute();
            String searchAttributes[] = { userIdNameAttrId, nameNameAttrId, emailAddressNameAttrId };
            ctx = this.ctxFactory.getSystemLdapContext();
            users = new HashSet<PlexusUser>();
            NamingEnumeration<SearchResult> answers = search(ctx, searchFilterExpr, searchFilterArgs, searchAttributes);
            while (answers.hasMore()) {
                SearchResult tmpAnswer = answers.next();
                Attributes attributes = tmpAnswer.getAttributes();
                users.add(convertToPlexusUser(attributes));
            }
        } catch (PartialResultException e) {
            if (this.configuration.getIgnorePartialResultException()) {
                getLogger().debug("PartialResultException encountered and ignored while searching LDAP users", e);
            } else {
                getLogger().error("Error while searching LDAP users", e);
                throw new RuntimeException("Error while searching LDAP users", e);
            }
        } catch (SizeLimitExceededException e) {
            getLogger().debug("Size limit exceeded while searching LDAP users - not all matching users were retrieved");
        } catch (NamingException e) {
            getLogger().error("Error while searching LDAP users", e);
            throw new RuntimeException("Error while searching LDAP users", e);
        } finally {
            LdapUtils.closeContext(ctx);
        }
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("List of LDAP users retrieved ({} users)", users.size());
        }
        return users;
    }

    NamingEnumeration<SearchResult> search(LdapContext ctx, String searchFilterExpr, Object[] searchFilterArgs, String searchAttributes[]) throws NamingException {
        String searchBase = this.configuration.getUserBaseDn();
        SearchControls searchCtls = new SearchControls();
        int searchScope = this.configuration.getUserSearchScope();
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("LDAP search scope: {}", searchScope == SearchControls.SUBTREE_SCOPE ? "subtree" : "onelevel");
        }
        searchCtls.setSearchScope(searchScope);
        searchCtls.setReturningAttributes(searchAttributes);
        NamingEnumeration<SearchResult> answers = ctx.search(searchBase, searchFilterExpr, searchFilterArgs, searchCtls);
        return answers;
    }

    private PlexusUser convertToPlexusUser(Attributes attributes) throws NamingException {
        String userIdNameAttrId = this.configuration.getUserIdAttribute();
        String nameNameAttrId = this.configuration.getNameAttribute();
        String emailAddressNameAttrId = this.configuration.getEmailAddressAttribute();
        PlexusUser user = new PlexusUser();
        Attribute userId = attributes.get(userIdNameAttrId);
        String userIdValue = (String) userId.get();
        getLogger().debug("Converting LDAP user '{}' to internal format", userIdValue);
        user.setUserId((String) userId.get());
        Attribute name = attributes.get(nameNameAttrId);
        if (name != null) {
            user.setName((String) name.get());
        } else {
            getLogger().debug("Attribute '{}' not set", name);
        }
        Attribute emailAddress = attributes.get(emailAddressNameAttrId);
        if (emailAddress != null) {
            user.setEmailAddress((String) emailAddress.get());
        } else {
            getLogger().debug("Attribute '{}' not set", emailAddressNameAttrId);
        }
        user.setSource(SOURCE);
        return user;
    }

    private String getLogString(PlexusUserSearchCriteria criteria) {
        StringBuilder str = new StringBuilder();
        str.append("userId=");
        str.append(criteria.getUserId());
        str.append(",oneOfRolesIds=");
        str.append(criteria.getOneOfRoleIds());
        return str.toString();
    }
}
