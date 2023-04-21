package com.aurecon.kwb.ldap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import mgr.ListModelMapSharer;
import org.acegisecurity.providers.ldap.authenticator.LdapShaPasswordEncoder;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.ListModelMap;
import com.aurecon.kwb.core.KWBConstants;
import com.sleepycat.dbxml.XmlException;
import data.ContainerEditor;
import data.DbModule;
import data.IViewable;

/**
 * The main controller for all LDAP operations.
 * 
 * @author williamsrc
 *
 */
public final class LdapController {

    /**
	 * The server name.
	 * (will always be the local host that the webapp is running on)
	 */
    private static final String LDAP_SERVER_NAME = "localhost";

    /**
	 * Root DN for administrator access.
	 */
    private static final String ROOT_DN = "cn=administrator,dc=kwbsecurity,dc=kwb";

    /**
	 * Password for that root DN.
	 */
    private static final String ROOT_PASS = "secret";

    /**
	 * Root context for the database.
	 */
    private static final String ROOT_CONTEXT = "dc=kwbsecurity,dc=kwb";

    /**
	 * The base context.
	 */
    private LdapContext ctx = null;

    /**
	 * The user list.
	 */
    private ListModelMap systemUserList;

    /**
	 * ListModelSharer for the users ListModel.
	 */
    private ListModelMapSharer userListSharer;

    /**
	 * The group list.
	 */
    private LdapListModelMap systemGroupList;

    /**
	 * ListModelSharer for the groups ListModel.
	 */
    private ListModelMapSharer groupListSharer;

    /**
	 * Identifier for SURNAME in LDAP.
	 */
    private static final String SURNAME = "sn";

    /**
	 * Identifier for given name in LDAP.
	 */
    private static final String GIVEN_NAME = "givenName";

    /**
	 * Identifier for common name in LDAP.
	 */
    private static final String COMMON_NAME = "cn";

    /**
	 * Identifier for email address in LDAP.
	 */
    private static final String EMAIL_ADDRESS = "mail";

    /**
	 * Identifier for telephone number in LDAP.
	 */
    private static final String TELEPHONE_NUMBER = "telephoneNumber";

    /**
	 * Identifier for the user password in LDAP.
	 */
    private static final String USER_PASSWORD = "userPassword";

    /**
	 * Identifier for organisational unit in LDAP.
	 */
    private static final String ORGANISATIONAL_UNIT = "ou";

    /**
	 * Identifier for DESCRIPTION in LDAP.
	 */
    private static final String DESCRIPTION = "description";

    /**
	 * Identifier for a member of a groupOfNames in LDAP.
	 */
    private static final String GROUP_MEMBER = "member";

    /**
	 * Array to hold attribute selection for user information queries.
	 */
    private String[] uaids = { SURNAME, GIVEN_NAME, COMMON_NAME, EMAIL_ADDRESS, TELEPHONE_NUMBER, USER_PASSWORD };

    /**
	 * Array to hold attribute selection for group information queries.
	 */
    private String[] gaids = { ORGANISATIONAL_UNIT, COMMON_NAME, DESCRIPTION, GROUP_MEMBER };

    /**
	 * Admin Role.
	 */
    private LdapRole adminRole = null;

    /**
	 * User Role.
	 */
    private LdapRole userRole = null;

    /**
	 * The password encoder.
	 * (used for encoding passwords before putting into LDAP and checking
	 * the validity of a raw password against an encoded one)
	 */
    private LdapShaPasswordEncoder passwordEncoder = new LdapShaPasswordEncoder();

    /**
	 * An empty group for use when no groups have been selected for a user.
	 */
    private LdapGroup emptySelectedGroup = new LdapGroup(this, "", "0", "(No Groups Selected)", "No Groups Selected", new LdapListModelMap());

    /**
	 * An empty group for use when no groups are left available to be selected for a user.
	 */
    private LdapGroup emptyAvailableGroup = new LdapGroup(this, "", "0", "(No Groups Available)", "No Groups Available", new LdapListModelMap());

    /**
	 * Indicates a password has been saved successfully.
	 */
    public static final int PW_SUCCESS = 5;

    /**
	 * Indicates the current password entered is incorrect.
	 */
    public static final int PW_CURRENT = 2;

    /**
	 * Indicates that one the new password entries does not match the other entry.
	 */
    public static final int PW_NEW_MATCH = 1;

    /**
	 * Indicates that an unknown error has occurred, contact support.
	 */
    public static final int PW_UNKNOWN = 7;

    /**
	 * Sole-Constructor.
	 */
    public LdapController() {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + LDAP_SERVER_NAME + "/" + ROOT_CONTEXT);
        env.put(Context.SECURITY_PRINCIPAL, ROOT_DN);
        env.put(Context.SECURITY_CREDENTIALS, ROOT_PASS);
        try {
            ctx = new InitialLdapContext(env, null);
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        } finally {
            this.createUserList();
            this.createGroupList();
            this.createRoles();
        }
        System.out.println("KWB: LDAP Service Started.");
    }

    /**
	 * Creates the user list from the current database.
	 * (only called from the constructor)
	 */
    private void createUserList() {
        HashMap<String, LdapUser> map = new HashMap<String, LdapUser>();
        String name = "ou=users";
        LdapUser user = null;
        String dn = "";
        String sn = "";
        String givenName = "";
        String mail = "";
        String telephoneNumber = "";
        String role = "disabled";
        String password = "";
        try {
            NamingEnumeration<?> bindings = ctx.listBindings(name);
            while (bindings.hasMore()) {
                Binding currentBinding = (Binding) bindings.next();
                LdapContext lc = (LdapContext) currentBinding.getObject();
                Attributes atts = lc.getAttributes("", uaids);
                NamingEnumeration<?> ne = atts.getAll();
                Attribute att = null;
                String attid = "";
                telephoneNumber = "";
                while (ne.hasMore()) {
                    att = (Attribute) ne.next();
                    attid = att.getID();
                    if (attid.equals(EMAIL_ADDRESS)) {
                        mail = (String) att.get(0);
                    } else if (attid.equals(SURNAME)) {
                        sn = (String) att.get(0);
                    } else if (attid.equals(GIVEN_NAME)) {
                        givenName = (String) att.get(0);
                    } else if (attid.equals(TELEPHONE_NUMBER)) {
                        telephoneNumber = (String) att.get(0);
                    } else if (attid.equals("USER_PASSWORD")) {
                        byte[] a = (byte[]) att.get();
                        password = new String(a);
                    }
                }
                dn = currentBinding.getName() + "," + name + "," + ROOT_CONTEXT;
                user = new LdapUser(this, dn, sn, givenName, mail, telephoneNumber, role, password);
                map.put(dn, user);
            }
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
        systemUserList = new LdapListModelMap(map);
        userListSharer = new ListModelMapSharer(systemUserList);
    }

    /**
	 * Creates the group list from the current database.
	 * (only called from the constructor)
	 */
    private void createGroupList() {
        HashMap<String, LdapGroup> map = new HashMap<String, LdapGroup>();
        String name = "ou=groups,ou=groupings";
        LdapGroup group = null;
        String ou = "";
        String cn = "";
        String description = "";
        String member = "";
        String dn = "";
        LdapListModelMap groupUsers = null;
        try {
            NamingEnumeration<?> bindings = ctx.listBindings(name);
            while (bindings.hasMore()) {
                Binding currentBinding = (Binding) bindings.next();
                LdapContext lc = (LdapContext) currentBinding.getObject();
                Attributes atts = lc.getAttributes("", gaids);
                NamingEnumeration<?> ne = atts.getAll();
                Attribute att = null;
                String attid = "";
                while (ne.hasMore()) {
                    att = (Attribute) ne.next();
                    attid = att.getID();
                    if (attid.equals(ORGANISATIONAL_UNIT)) {
                        ou = (String) att.get(0);
                    } else if (attid.equals(COMMON_NAME)) {
                        cn = (String) att.get(0);
                    } else if (attid.equals(DESCRIPTION)) {
                        description = (String) att.get(0);
                    } else if (attid.equals(GROUP_MEMBER)) {
                        groupUsers = new LdapListModelMap();
                        int size = att.size();
                        for (int i = 0; i < size; i++) {
                            member = (String) att.get(i);
                            if (!member.equals("0")) {
                                LdapUser user = (LdapUser) systemUserList.get(member);
                                groupUsers.put(member, user);
                            }
                        }
                    }
                }
                dn = currentBinding.getName() + "," + name + "," + ROOT_CONTEXT;
                group = new LdapGroup(this, dn, ou, cn, description, groupUsers);
                map.put(ou, group);
            }
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
        systemGroupList = new LdapListModelMap(map);
        groupListSharer = new ListModelMapSharer(systemGroupList);
    }

    /**
	 * Creates the role groups for the entire system.
	 * (mainly for checking a users role on initialisation adding new users
	 * to the roles and changing users between the roles)
	 */
    private void createRoles() {
        String name = "ou=roles,ou=groupings";
        LdapRole role = null;
        String ou = "";
        String cn = "";
        String description = "";
        String member = "";
        LdapListModelMap groupUsers = null;
        try {
            NamingEnumeration<?> bindings = ctx.listBindings(name);
            while (bindings.hasMore()) {
                Binding currentBinding = (Binding) bindings.next();
                LdapContext lc = (LdapContext) currentBinding.getObject();
                Attributes atts = lc.getAttributes("", gaids);
                NamingEnumeration<?> ne = atts.getAll();
                Attribute att = null;
                String attid = "";
                while (ne.hasMore()) {
                    att = (Attribute) ne.next();
                    attid = att.getID();
                    if (attid.equals(ORGANISATIONAL_UNIT)) {
                        ou = (String) att.get(0);
                    } else if (attid.equals(COMMON_NAME)) {
                        cn = (String) att.get(0);
                    } else if (attid.equals(LdapController.DESCRIPTION)) {
                        description = (String) att.get(0);
                    } else if (attid.equals(GROUP_MEMBER)) {
                        groupUsers = new LdapListModelMap();
                        int size = att.size();
                        for (int i = 0; i < size; i++) {
                            member = (String) att.get(i);
                            if (!member.equals("0")) {
                                LdapUser user = (LdapUser) systemUserList.get(member);
                                user.setRole(ou);
                                groupUsers.put(member, user);
                            }
                        }
                    }
                }
                role = new LdapRole(this, ou, cn, description, groupUsers);
                if (role.getOU().equals("admin")) {
                    adminRole = role;
                } else if (role.getOU().equals("user")) {
                    userRole = role;
                }
            }
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
    }

    /**
	 * Returns the LdapUser after it has been added to a group.
	 * (Called from the LdapGroup class)
	 * 
	 * @param groupId	{@link String} group id of the group to add the user to
	 * @param userDN	{@link String} full distinguished name of the user
	 * @return			{@link LdapUser} the user's wrapper object
	 */
    synchronized LdapUser addMemberToGroup(final String groupId, final String userDN) {
        String name = "ou=" + groupId + ",ou=groups,ou=groupings";
        try {
            Attributes atts = (Attributes) ctx.getAttributes(name);
            Attribute att = atts.get("member");
            int index = att.size();
            if (index == 1) {
                if (att.get(0).equals("mail=kwadmin@sourceforge.net,ou=users," + ROOT_CONTEXT)) {
                    att.set(0, userDN);
                } else {
                    att.add(att.size(), userDN);
                }
            } else {
                att.add(att.size(), userDN);
            }
            ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, atts);
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
        return (LdapUser) systemUserList.get(userDN);
    }

    /**
	 * Removes the LdapUser from a group.
	 * (Called from the LdapGroup class - may be deprecated when 
	 * the method this is called from is deprecated)
	 * 
	 * @param groupId	{@link String} id of the group to add the user to
	 * @param userDN	{@link String}full distinguished name of the user to add to the group
	 */
    synchronized void removeMemberFromGroup(final String groupId, final String userDN) {
        String name = "ou=" + groupId + ",ou=groups,ou=groupings";
        try {
            Attributes atts = (Attributes) ctx.getAttributes(name);
            Attribute att = atts.get("member");
            int size = att.size();
            if (size == 1) {
                att.set(0, "mail=kwadmin@sourceforge.net,ou=users," + ROOT_CONTEXT);
            } else {
                for (int i = 0; i < size; i++) {
                    String user = (String) att.get(i);
                    if (user.equals(userDN)) {
                        att.remove(i);
                        break;
                    }
                }
            }
            ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, atts);
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
    }

    /**
	 * Returns the LdapUser after it has been added to a role.  (Called from the LdapGroup class)
	 * 
	 * @param roleId	{@link String} role id of the role to add the user to
	 * @param userDN	{@link String} full distinguished name of the user
	 * @return			{@link LdapUser} the user's wrapper object
	 */
    synchronized LdapUser addMemberToRole(final String roleId, final String userDN) {
        String name = "ou=" + roleId + ",ou=roles,ou=groupings";
        try {
            Attributes atts = (Attributes) ctx.getAttributes(name);
            Attribute att = atts.get("member");
            int index = att.size();
            if (index == 1) {
                if (att.get(0).equals("mail=kwadmin@sourceforge.net,ou=users," + ROOT_CONTEXT)) {
                    att.set(0, userDN);
                } else {
                    att.add(att.size(), userDN);
                }
            } else {
                att.add(att.size(), userDN);
            }
            ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, atts);
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
        return null;
    }

    /**
	 * Removes the LdapUser from a role.  
	 * (Called from the LdapGroup class - may be deprecated when 
	 * the method this is called from is deprecated)
	 * 
	 * @param roleId	{@link String} id of the role to add the user to
	 * @param userDN	{@link String}full distinguished name of the user to add to the role
	 */
    synchronized void removeMemberFromRole(final String roleId, final String userDN) {
        String name = "ou=" + roleId + ",ou=roles,ou=groupings";
        try {
            Attributes atts = (Attributes) ctx.getAttributes(name);
            Attribute att = atts.get("member");
            for (int i = 0; i < att.size(); i++) {
                String user = (String) att.get(i);
                if (user.equals(userDN)) {
                    att.remove(i);
                    break;
                }
            }
            ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, atts);
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
    }

    /**
	 * Modifies user attributes (called from the LdapUser class).
	 * 
	 * @param currentDN				{@link String} current distinguished name of the user to modify
	 * @param newSN					{@link String} new SURNAME
	 * @param newGivenName			{@link String} new given name
	 * @param newMail				{@link String} new email address
	 * @param newTelephoneNumber	{@link String} new telephone number
	 * @param newRole				{@link String} new role on the system
	 */
    @SuppressWarnings("unchecked")
    synchronized void modifyUserAttributes(final String currentDN, final String newSN, final String newGivenName, final String newMail, final String newTelephoneNumber, final String newRole) {
        LdapUser user = (LdapUser) systemUserList.get(currentDN);
        String name = "mail=" + user.getMail() + ",ou=users";
        boolean changed = false;
        boolean renamed = false;
        boolean telephoneFound = false;
        boolean commonNameChange = !user.getGivenName().equals(newGivenName) || !user.getSurname().equals(newSN);
        try {
            Attributes atts = (Attributes) ctx.getAttributes(name, uaids);
            NamingEnumeration<?> ne = atts.getAll();
            Attribute att = null;
            String attid = "";
            while (ne.hasMore()) {
                att = (Attribute) ne.next();
                attid = att.getID();
                if (attid.equals(EMAIL_ADDRESS)) {
                    if (!user.getMail().equals(newMail)) {
                        renamed = true;
                    }
                } else if (attid.equals(SURNAME)) {
                    if (!user.getSurname().equals(newSN)) {
                        att.set(0, newSN);
                        user.setSurname(newSN);
                    }
                } else if (attid.equals(GIVEN_NAME)) {
                    if (!user.getGivenName().equals(newGivenName)) {
                        att.set(0, newGivenName);
                        user.setGivenName(newGivenName);
                    }
                } else if (attid.equals(TELEPHONE_NUMBER)) {
                    if (!user.getTelephoneNumber().equals(newTelephoneNumber)) {
                        att.set(0, newTelephoneNumber);
                        user.setTelephoneNumber(newTelephoneNumber);
                        changed = true;
                    }
                } else if (attid.equals(COMMON_NAME)) {
                    if (commonNameChange) {
                        att.set(0, newGivenName + " " + newSN);
                        changed = true;
                    }
                }
            }
            if (!telephoneFound) {
                if (!user.getTelephoneNumber().equals(newTelephoneNumber)) {
                    Attribute phone = new BasicAttribute(TELEPHONE_NUMBER);
                    phone.add(newTelephoneNumber);
                    atts.put(phone);
                    user.setTelephoneNumber(newTelephoneNumber);
                }
            }
            ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, atts);
            if (!user.getRole().equals(newRole)) {
                if (user.getRole().equals("admin")) {
                    adminRole.removeMember(user.getDN());
                } else if (user.getRole().equals("user")) {
                    userRole.removeMember(user.getDN());
                }
                if (newRole.equals("admin")) {
                    adminRole.addMember(user.getDN());
                } else if (newRole.equals("user")) {
                    userRole.addMember(user.getDN());
                }
                user.setRole(newRole);
                changed = true;
            }
            if (renamed) {
                ctx.rename(name, "mail=" + newMail + ",ou=users");
                user.setDN("mail=" + newMail + ",ou=users," + ROOT_CONTEXT);
                user.setMail(newMail);
                systemUserList.remove(currentDN);
                systemUserList.put(user.getDN(), user);
                Iterator<String> itr = systemGroupList.keySet().iterator();
                LdapGroup group = null;
                while (itr.hasNext()) {
                    group = (LdapGroup) systemGroupList.get(itr.next());
                    if (group.containsMember(currentDN)) {
                        group.removeMember(currentDN);
                        group.addMember(user.getDN());
                    }
                }
                if (adminRole.containsMember(currentDN)) {
                    adminRole.removeMember(currentDN);
                    adminRole.addMember(user.getDN());
                }
                if (userRole.containsMember(currentDN)) {
                    userRole.removeMember(currentDN);
                    userRole.addMember(user.getDN());
                }
            } else if (changed) {
                systemUserList.put(user.getDN(), user);
                Iterator<String> itr = systemGroupList.keySet().iterator();
                LdapGroup group = null;
                while (itr.hasNext()) {
                    group = (LdapGroup) systemGroupList.get(itr.next());
                    if (group.containsMember(user.getDN())) {
                        group.refreshMember(user.getDN(), user);
                    }
                }
            }
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
    }

    /**
	 * Modifies the user's password.
	 * 
	 * @param userDN			{@link String} user's DN
	 * @param currentPassword	{@link String} the user's current password (security check)
	 * @param password			{@link String} new password
	 * @param passwordRepeat	{@link String} repeat of the new password
	 * @return					{@link Integer} status of operation
	 */
    synchronized int modifyUserPassword(final String userDN, final String currentPassword, final String password, final String passwordRepeat) {
        LdapUser user = (LdapUser) systemUserList.get(userDN);
        String encodedCurrent = passwordEncoder.encodePassword(currentPassword, null);
        String encodedPassword = passwordEncoder.encodePassword(password, null);
        String encodedPasswordRepeat = passwordEncoder.encodePassword(passwordRepeat, null);
        if (!encodedCurrent.equals(user.getPassword())) {
            return PW_CURRENT;
        } else if (!encodedPassword.equals(encodedPasswordRepeat)) {
            return PW_NEW_MATCH;
        } else {
            String name = "mail=" + user.getMail() + ",ou=users";
            try {
                Attributes atts = (Attributes) ctx.getAttributes(name, uaids);
                NamingEnumeration<?> ne = atts.getAll();
                Attribute att = null;
                String attid = "";
                while (ne.hasMore()) {
                    att = (Attribute) ne.next();
                    attid = att.getID();
                    if (attid.equals(USER_PASSWORD)) {
                        att.set(0, encodedPassword);
                    }
                }
                ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, atts);
                user.setPassword(encodedPassword);
                return PW_SUCCESS;
            } catch (NamingException ne) {
                ne.printStackTrace(System.err);
                return PW_UNKNOWN;
            }
        }
    }

    /**
	 * Reset the user's password.
	 * 
	 * @param userDN	{@link String} user's DN
	 * @param password	{@link String} password to set
	 * @return			{@link Integer} status of operation
	 */
    synchronized int resetUserPassword(final String userDN, final String password) {
        LdapUser user = (LdapUser) systemUserList.get(userDN);
        String encodedPassword = passwordEncoder.encodePassword(password, null);
        String name = "mail=" + user.getMail() + ",ou=users";
        try {
            Attributes atts = (Attributes) ctx.getAttributes(name, uaids);
            NamingEnumeration<?> ne = atts.getAll();
            Attribute att = null;
            String attid = "";
            while (ne.hasMore()) {
                att = (Attribute) ne.next();
                attid = att.getID();
                if (attid.equals(USER_PASSWORD)) {
                    att.set(0, encodedPassword);
                }
            }
            ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, atts);
            user.setPassword(encodedPassword);
            return PW_SUCCESS;
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
            return PW_UNKNOWN;
        }
    }

    /**
	 * Modifies Group attributes (called from the LdapGroup class).
	 * 	
	 * @param currentOU			{@link String} current OU of the group
	 * @param newOU				{@link String} new organisational unit identifier for the group
	 * @param newCN				{@link String} new common name
	 * @param newDescription	{@link String} new description
	 */
    synchronized void modifyGroupAttributes(final String currentOU, final String newOU, final String newCN, final String newDescription) {
        LdapGroup group = (LdapGroup) systemGroupList.get(currentOU);
        String name = "ou=" + group.getOU() + ",ou=groups,ou=groupings";
        boolean changed = false;
        boolean renamed = false;
        try {
            Attributes atts = (Attributes) ctx.getAttributes(name, gaids);
            NamingEnumeration<?> ne = atts.getAll();
            Attribute att = null;
            String attid = "";
            while (ne.hasMore()) {
                att = (Attribute) ne.next();
                attid = att.getID();
                if (attid.equals(ORGANISATIONAL_UNIT)) {
                    if (!group.getOU().equals(newOU)) {
                        renamed = true;
                    }
                } else if (attid.equals(COMMON_NAME)) {
                    if (!group.getCommonName().equals(newCN)) {
                        att.set(0, newCN);
                        group.setCommonName(newCN);
                        changed = true;
                    }
                } else if (attid.equals(DESCRIPTION)) {
                    if (!group.getDescription().equals(newDescription)) {
                        att.set(0, newDescription);
                        group.setDescription(newDescription);
                        changed = true;
                    }
                }
            }
            ctx.modifyAttributes(name, DirContext.REPLACE_ATTRIBUTE, atts);
            if (renamed) {
                ctx.rename(name, "ou=" + newOU + ",ou=groups,ou=groupings");
                group.setDN("ou=" + newOU + ",ou=groups,ou=groupings," + ROOT_CONTEXT);
                group.setOU(newOU);
                systemGroupList.put(group.getOU(), group);
                systemGroupList.remove(currentOU);
            } else if (changed) {
                systemGroupList.put(group.getOU(), group);
            }
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
    }

    /**
	 * Creates a new user in the LDAP database.
	 * 
	 * @param surname			{@link String} surname
	 * @param givenName			{@link String} given name
	 * @param mail				{@link String} email address (used as the main identifier)
	 * @param telephoneNumber	{@link String} telephone number
	 * @param role				{@link String} user role
	 * @return					{@link LdapUser}
	 */
    public synchronized LdapUser createNewUser(final String surname, final String givenName, final String mail, final String telephoneNumber, final String role) {
        Attributes attrs = new BasicAttributes();
        Attribute objclass = new BasicAttribute("objectClass");
        objclass.add("top");
        objclass.add("organizationalPerson");
        objclass.add("inetOrgPerson");
        objclass.add("person");
        attrs.put(objclass);
        Attribute cn = new BasicAttribute(COMMON_NAME);
        cn.add(givenName + " " + surname);
        attrs.put(cn);
        Attribute sn = new BasicAttribute(SURNAME);
        sn.add(surname);
        attrs.put(sn);
        Attribute gName = new BasicAttribute(GIVEN_NAME);
        gName.add(givenName);
        attrs.put(gName);
        if (!telephoneNumber.equals("")) {
            Attribute phone = new BasicAttribute(TELEPHONE_NUMBER);
            phone.add(telephoneNumber);
            attrs.put(phone);
        }
        String pass = mail.substring(0, mail.indexOf("@"));
        pass = passwordEncoder.encodePassword(pass, null);
        Attribute password = new BasicAttribute(USER_PASSWORD);
        password.add(pass.getBytes());
        attrs.put(password);
        try {
            ctx.bind("mail=" + mail + ",ou=users", null, attrs);
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
        LdapUser user = new LdapUser(this, "mail=" + mail + ",ou=users," + ROOT_CONTEXT, surname, givenName, mail, telephoneNumber, role, pass);
        systemUserList.put(user.getDN(), user);
        if (role.equals("user")) {
            userRole.addMember(user.getDN());
        } else if (role.equals("admin")) {
            adminRole.addMember(user.getDN());
        }
        return user;
    }

    /**
	 * Creates a new client group in the LDAP database.
	 * 
	 * @param name			{@link String} group name
	 * @param identifier	{@link String} unique identifier
	 * @param description	{@link String} description
	 */
    public synchronized void createNewGroup(final String name, final String identifier, final String description) {
        Attributes attrs = new BasicAttributes();
        Attribute objclass = new BasicAttribute("objectClass");
        objclass.add("top");
        objclass.add("groupOfNames");
        attrs.put(objclass);
        Attribute cn = new BasicAttribute(COMMON_NAME);
        cn.add(name);
        attrs.put(cn);
        Attribute ou = new BasicAttribute(ORGANISATIONAL_UNIT);
        ou.add(identifier);
        attrs.put(ou);
        Attribute desc = new BasicAttribute(DESCRIPTION);
        desc.add(description);
        attrs.put(desc);
        Attribute member = new BasicAttribute("member");
        member.add("mail=kwadmin@sourceforge.net,ou=users," + ROOT_CONTEXT);
        attrs.put(member);
        try {
            ctx.bind("ou=" + identifier + ",ou=groups,ou=groupings", null, attrs);
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
        LdapGroup group = new LdapGroup(this, "ou=" + identifier + ",ou=groups,ou=groupings," + ROOT_CONTEXT, identifier, name, description, new LdapListModelMap());
        systemGroupList.put(group.getOU(), group);
        DbModule dbModule = DbModule.getInstance();
        int containers = 0;
        try {
            containers = dbModule.getXmlManager().existsContainer(identifier + ".dbxml");
        } catch (XmlException xe) {
            xe.printStackTrace(System.err);
        }
        if (containers == 0) {
            ContainerEditor editor = dbModule.getContainerEditor(KWBConstants.SYSTEM_DIRECORY);
            IViewable object = editor.getObjectWrapper(editor.getBaseObject());
            int location = object.readProperties().getRootElement().getChild("OBJECT__STRUCTURES").getChildren().size();
            dbModule.createContainer(identifier + ".dbxml", name, DESCRIPTION, DbModule.ORGANISER_CONTAINER, KWBConstants.SYSTEM_DIRECORY, "kwadmin@sourceforge.com", identifier, location);
        }
    }

    /**
	 * Returns a ListModel for the users on the system for use in the interface.
	 * 
	 * @param desktop	{@link Desktop} desktop of the user making the request	
	 * @return			{@link ListModelMap} returned
	 */
    public ListModelMap getUserListModel(final Desktop desktop) {
        return (ListModelMap) userListSharer.getProxy(desktop);
    }

    /**
	 * Returns a ListModel for the groups on the system for use in the interface.
	 * 
	 * @param desktop	{@link Desktop} desktop of the user making the request
	 * @return			{@link ListModelMap} returned
	 */
    public ListModelMap getGroupListModel(final Desktop desktop) {
        return (ListModelMap) groupListSharer.getProxy(desktop);
    }

    /**
	 * Returns a ListModel for the general users on the system (no admin) for use in the interface.
	 * 
	 * @param desktop	{@link Desktop} desktop of the user making the request
	 * @return			{@link ListModelMap} returned
	 */
    public ListModelMap getUserRoleListModel(final Desktop desktop) {
        return userRole.getRoleListModel(desktop);
    }

    /**
	 * Returns a map of the current groups on the system.
	 * (for use by the user editor window)
	 * 
	 * @return	{@link HashMap} current group list
	 */
    @SuppressWarnings("unchecked")
    public HashMap<String, LdapGroup> getCurrentGroupList() {
        return new HashMap<String, LdapGroup>(systemGroupList.getInnerMap());
    }

    /**
	 * Returns a map of the current users on the system.
	 * 
	 * @return	{@link HashMap} current group list
	 */
    @SuppressWarnings("unchecked")
    public HashMap<String, LdapUser> getCurrentUserList() {
        return new HashMap<String, LdapUser>(systemUserList.getInnerMap());
    }

    /**
	 * Returns the wrapper for a user.
	 * 
	 * @param userId	{@link String} user id
	 * @return			{@link LdapUser} requested
	 */
    public LdapUser getUserWrapper(final String userId) {
        LdapUser user = null;
        String fullDN = "mail=" + userId + ",ou=users," + ROOT_CONTEXT;
        if (systemUserList.containsKey(fullDN)) {
            user = (LdapUser) systemUserList.get(fullDN);
        }
        return user;
    }

    /**
	 * Close the context.
	 *
	 */
    public void close() {
        try {
            ctx.close();
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
        }
    }

    /**
	 * Returns the empty selected group.
	 * @return	{@link LdapGroup} empty selected group
	 */
    public LdapGroup getEmptySelectedGroup() {
        return emptySelectedGroup;
    }

    /**
	 * Returns the empty available group.
	 * @return	{@link LdapGroup} empty available group
	 */
    public LdapGroup getEmptyAvailableGroup() {
        return emptyAvailableGroup;
    }
}
