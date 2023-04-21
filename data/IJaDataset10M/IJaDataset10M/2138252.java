package org.eaasyst.eaa.data.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.SizeLimitExceededException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.data.DataConnector;
import org.eaasyst.eaa.data.RecordFactory;
import org.eaasyst.eaa.data.RecordSet;
import org.eaasyst.eaa.data.UserProfileDab;
import org.eaasyst.eaa.security.User;
import org.eaasyst.eaa.security.UserProfileManager;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.syst.data.persistent.BasicUser;
import org.eaasyst.eaa.syst.data.persistent.Contact;
import org.eaasyst.eaa.syst.data.persistent.Person;
import org.eaasyst.eaa.syst.data.transients.SearchFilter;
import org.eaasyst.eaa.syst.data.transients.SearchSpecification;
import org.eaasyst.eaa.syst.data.transients.SecurityConfiguration;
import org.eaasyst.eaa.utils.StringUtils;

/**
 * <p>Data accesss bean for User Profile information. This bean supports
 * the following commands:
 * <ul>
 * <li>get - Returns a single <code>UserProfile</code> object
 * <li>getByEmail - Returns a single <code>UserProfile</code> object
 * <li>All - Returns all <code>UserProfile</code> objects in a List
 * <li>READ (key) - Returns a <code>RecordSet</code> object containing
 * a single row for the profile associated with the specified key
 * <li>READ (filter) - Returns a <code>RecordSet</code> object containing
 * zero to many rows based on the specified search filter
 * <li>INSERT - inserts a new profile record into the database from the
 * <code>DynaActionForm</code> object passed in the bean parameters
 * <li>UPDATE - updates the database from the <code>DynaActionForm</code>
 * object passed in the bean parameters
 * <li>updateLastLogon - updates the last logon date
 * <li>updateForcedApplications - updates the database from the
 * <code>List</code> of forced applications passed in the bean parameters
 * <li>Reset - updates the database with a new password from the
 * <code>UserProfile</code> object passed in the bean parameters
 * <li>DELETE - deletes from the database the information for the profile
 * associated with the specified key
 * </ul>
 * </p>
 * <p>User Profile information for this bean is stored in an LDAP directory.
 * Parameters necessary to connect to the LDAP directory are obtained from
 * the Eaasy Street system properties.</p>
 *
 * @version 2.6.1
 * @author Jeff Chilton
 * 		   Peter Ruan
 */
public class LdapUserProfileDab extends UserProfileDab {

    private static final RecordFactory factory = new UserProfileRecordFactory("single");

    private static final RecordFactory searchFactory = new UserProfileRecordFactory("multiple");

    private String INIT_CONTEXT_FACTORY = EaasyStreet.getProperty(Constants.CFG_LDAP_INIT_CONTEXT_FACTORY);

    private String PROVIDER_URL = EaasyStreet.getProperty(Constants.CFG_LDAP_PROVIDER_URL);

    private String SECURITY_AUTHENTICATION = EaasyStreet.getProperty(Constants.CFG_LDAP_SECURITY_AUTHENTICATION);

    private String SECURITY_PRINCIPAL = EaasyStreet.getProperty(Constants.CFG_LDAP_SECURITY_PRINCIPAL);

    private String SECURITY_CREDENTIALS = EaasyStreet.getProperty(Constants.CFG_LDAP_SECURITY_CREDENTIALS);

    private String SEARCH_BASE = EaasyStreet.getProperty(Constants.CFG_LDAP_SEARCH_BASE);

    private String SEARCH_STRING = EaasyStreet.getProperty(Constants.CFG_LDAP_SEARCH_STRING);

    private String USER_ID_ATTR = EaasyStreet.getProperty(Constants.CFG_LDAP_USER_ID_ATTR);

    private String USE_NAME_ATTR = EaasyStreet.getProperty(Constants.CFG_LDAP_USE_NAME_ATTR);

    private String NAME_PREFIX_ATTR = EaasyStreet.getProperty(Constants.CFG_LDAP_NAME_PREFIX_ATTR);

    private String FIRST_NAME_ATTR = EaasyStreet.getProperty(Constants.CFG_LDAP_FIRST_NAME_ATTR);

    private String MIDDLE_NAME_ATTR = EaasyStreet.getProperty(Constants.CFG_LDAP_MIDDLE_NAME_ATTR);

    private String LAST_NAME_ATTR = EaasyStreet.getProperty(Constants.CFG_LDAP_LAST_NAME_ATTR);

    private String NAME_SUFFIX_ATTR = EaasyStreet.getProperty(Constants.CFG_LDAP_NAME_SUFFIX_ATTR);

    private String ADDITIONAL_ATTRS = EaasyStreet.getProperty(Constants.CFG_LDAP_ADDITIONAL_ATTRS);

    private List attributeIds = new ArrayList();

    private SecurityConfiguration config = SecurityConfigurationFactory.getConfiguration();

    /**
	 * <p>Constructs a new "LdapUserProfileDab" object.</p>
	 *
	 * @since 2.0
	 */
    public LdapUserProfileDab() {
        className = StringUtils.computeClassName(getClass());
        if (ADDITIONAL_ATTRS != null) {
            attributeIds = StringUtils.split(ADDITIONAL_ATTRS, ",");
        }
    }

    /**
	 * Used to execute the key-based "read" command after all command
	 * parameters have been set and the command is "ready to excute".
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeRead() {
        executeGet();
        User profile = (User) executionResults;
        if (profile != null) {
            executionResults = new RecordSet(factory.getMetaData());
            HashMap thisRecord = null;
            if (attributeIds.isEmpty()) {
                thisRecord = factory.createRecord(new Object[] { profile.getUserId(), profile.getPerson().getUseName(), profile.getPerson().getNamePrefix(), profile.getPerson().getFirstName(), profile.getPerson().getMiddleName(), profile.getPerson().getLastName(), profile.getPerson().getNameSuffix() });
            } else {
                int x = 7;
                Object[] fields = new Object[x + attributeIds.size()];
                fields[0] = profile.getUserId();
                fields[1] = profile.getPerson().getUseName();
                fields[2] = profile.getPerson().getNamePrefix();
                fields[3] = profile.getPerson().getFirstName();
                fields[4] = profile.getPerson().getMiddleName();
                fields[5] = profile.getPerson().getLastName();
                fields[6] = profile.getPerson().getNameSuffix();
                Iterator i = attributeIds.iterator();
                while (i.hasNext()) {
                    fields[x] = profile.getAttribute(i.next().toString());
                    x = x + 1;
                }
                thisRecord = factory.createRecord(fields);
            }
            ((RecordSet) executionResults).addRecord(thisRecord);
            responseCode = 0;
            responseString = "Execution complete";
        }
    }

    /**
	 * Used to execute the filter-based "read" command after all command
	 * parameters have been set and the command is "ready to excute".
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeSearch() {
        DirContext context = getContext();
        Attributes atrs = null;
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration res = null;
        try {
            res = context.search(SEARCH_BASE, getSearchString(), controls);
            if (res == null || !res.hasMore()) {
                responseCode = 1;
                responseString = "No users on file";
                return;
            }
        } catch (PartialResultException e) {
            responseCode = 1;
            responseString = "No users on file";
            return;
        } catch (NamingException e) {
            EaasyStreet.logError("Could not search for users", e);
            responseCode = 2;
            responseString = e.toString();
            return;
        }
        TreeMap users = new TreeMap();
        try {
            while (res.hasMore()) {
                Attribute thisAttr = null;
                String attrValue = null;
                SearchResult user = (SearchResult) res.next();
                atrs = user.getAttributes();
                String userId = atrs.get(USER_ID_ATTR).get().toString();
                int x = 2;
                Object[] fields = new Object[x + attributeIds.size()];
                fields[0] = userId;
                if (USE_NAME_ATTR != null) {
                    thisAttr = atrs.get(USE_NAME_ATTR);
                    if (thisAttr != null) {
                        attrValue = thisAttr.get().toString();
                        fields[1] = attrValue;
                    }
                }
                List listAttrs = config.getListAttributeNames();
                if (listAttrs != null && !listAttrs.isEmpty()) {
                    Iterator i = listAttrs.iterator();
                    while (i.hasNext()) {
                        String attrId = (String) i.next();
                        thisAttr = atrs.get(EaasyStreet.getProperty(Constants.CFG_LDAP_NAMED_ATTRIBUTE_ATTR + attrId));
                        if (thisAttr != null) {
                            attrValue = thisAttr.get().toString();
                            fields[x] = attrValue;
                        }
                        x = x + 1;
                    }
                }
                users.put(userId, fields);
            }
        } catch (SizeLimitExceededException e) {
        } catch (NamingException e) {
            if (users.isEmpty()) {
                EaasyStreet.logError("Could not search for users", e);
                responseCode = 2;
                responseString = e.toString();
                return;
            }
        }
        try {
            context.close();
        } catch (NamingException e) {
            EaasyStreet.logError("Could not close context", e);
        }
        if (users.isEmpty()) {
            responseCode = 1;
            responseString = "No users on file";
        } else {
            executionResults = new RecordSet(searchFactory.getMetaData());
            Iterator i = users.keySet().iterator();
            while (i.hasNext()) {
                String userId = i.next().toString();
                HashMap thisRecord = searchFactory.createRecord((Object[]) users.get(userId));
                ((RecordSet) executionResults).addRecord(thisRecord);
            }
            responseCode = 0;
            responseString = "Execution complete";
        }
    }

    /**
	 * Used to execute the "insert" command after all command parameters
	 * have been set and the command is "ready to excute".
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeInsert() {
        responseCode = 4;
        responseString = "Action not available when using LDAP";
    }

    /**
	 * Used to execute the "update" command after all command parameters
	 * have been set and the command is "ready to excute".
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeUpdate() {
        responseCode = 4;
        responseString = "Action not available when using LDAP";
    }

    /**
	 * Used to execute the "delete" command after all command parameters
	 * have been set and the command is "ready to excute".
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeDelete() {
        responseCode = 4;
        responseString = "Action not available when using LDAP";
    }

    /**
	 * Used to execute the "all" command after all command parameters
	 * have been set and the command is "ready to excute".
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeAll() {
        DirContext context = getContext();
        Attributes atrs = null;
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration res = null;
        try {
            res = context.search(SEARCH_BASE, SEARCH_STRING, controls);
            if (res == null || !res.hasMore()) {
                responseCode = 1;
                responseString = "No users on file";
                return;
            }
        } catch (NamingException e) {
            EaasyStreet.logError("Could not search for users", e);
            responseCode = 2;
            responseString = e.toString();
            return;
        }
        TreeMap users = new TreeMap();
        try {
            while (res.hasMore()) {
                Attribute thisAttr = null;
                String attrValue = null;
                SearchResult user = (SearchResult) res.next();
                atrs = user.getAttributes();
                String userId = atrs.get(USER_ID_ATTR).get().toString();
                int x = 2;
                Object[] fields = new Object[x + attributeIds.size()];
                fields[0] = userId;
                if (USE_NAME_ATTR != null) {
                    thisAttr = atrs.get(USE_NAME_ATTR);
                    if (thisAttr != null) {
                        attrValue = thisAttr.get().toString();
                        fields[1] = attrValue;
                    }
                }
                users.put(userId, fields[1]);
            }
        } catch (SizeLimitExceededException e) {
        } catch (NamingException e) {
            if (users.isEmpty()) {
                EaasyStreet.logError("Could not search for users", e);
                responseCode = 2;
                responseString = e.toString();
                return;
            }
        }
        try {
            context.close();
        } catch (NamingException e) {
            EaasyStreet.logError("Could not close context", e);
        }
        if (users.isEmpty()) {
            responseCode = 1;
            responseString = "No users on file";
        } else {
            executionResults = new ArrayList();
            Iterator i = users.keySet().iterator();
            while (i.hasNext()) {
                String userId = i.next().toString();
                User profile = new BasicUser();
                profile.setPerson(new Person());
                profile.setUserId(userId);
                profile.getPerson().setUseName((String) users.get(userId));
                ((ArrayList) executionResults).add(profile);
            }
            responseCode = 0;
            responseString = "Execution complete";
        }
    }

    /**
	 * Used to execute the "get" command after all command parameters
	 * have been set and the command is "ready to excute".
	 *
	 * @since Eaasy Street 2.0
	 */
    public void executeGet() {
        String userId = (String) parameters.get(DataConnector.RECORD_KEY_PARAMETER);
        Attributes attributes = getUserAttributes(userId);
        if (attributes != null) {
            Date rightNow = new Date();
            String currentUserId = UserProfileManager.getUserId();
            User profile = new BasicUser();
            profile.setUserId(userId);
            profile.setCreationDate(rightNow);
            profile.setCreatedBy(currentUserId);
            profile.setLastUpdate(rightNow);
            profile.setLastUpdateBy(currentUserId);
            Person person = new Person();
            person.setCreationDate(rightNow);
            person.setCreatedBy(currentUserId);
            person.setLastUpdate(rightNow);
            person.setLastUpdateBy(currentUserId);
            profile.setPerson(person);
            Attribute thisAttr = null;
            String attrValue = null;
            try {
                if (USE_NAME_ATTR != null) {
                    thisAttr = attributes.get(USE_NAME_ATTR);
                    if (thisAttr != null) {
                        attrValue = thisAttr.get().toString();
                        person.setUseName(attrValue);
                    }
                }
                if (NAME_PREFIX_ATTR != null) {
                    thisAttr = attributes.get(NAME_PREFIX_ATTR);
                    if (thisAttr != null) {
                        attrValue = thisAttr.get().toString();
                        person.setNamePrefix(attrValue);
                    }
                }
                if (FIRST_NAME_ATTR != null) {
                    thisAttr = attributes.get(FIRST_NAME_ATTR);
                    if (thisAttr != null) {
                        attrValue = thisAttr.get().toString();
                        person.setFirstName(attrValue);
                    }
                }
                if (MIDDLE_NAME_ATTR != null) {
                    thisAttr = attributes.get(MIDDLE_NAME_ATTR);
                    if (thisAttr != null) {
                        attrValue = thisAttr.get().toString();
                        person.setMiddleName(attrValue);
                    }
                }
                if (LAST_NAME_ATTR != null) {
                    thisAttr = attributes.get(LAST_NAME_ATTR);
                    if (thisAttr != null) {
                        attrValue = thisAttr.get().toString();
                        person.setLastName(attrValue);
                    }
                }
                if (NAME_SUFFIX_ATTR != null) {
                    thisAttr = attributes.get(NAME_SUFFIX_ATTR);
                    if (thisAttr != null) {
                        attrValue = thisAttr.get().toString();
                        person.setNameSuffix(attrValue);
                    }
                }
                Map contacts = new HashMap();
                if (config.getContactIds() != null) {
                    Iterator i = config.getContactIds().iterator();
                    while (i.hasNext()) {
                        String contactId = i.next().toString();
                        Contact contact = new Contact(null, contactId);
                        contact.setPerson(person);
                        contact.setCreationDate(rightNow);
                        contact.setCreatedBy(currentUserId);
                        contact.setLastUpdate(rightNow);
                        contact.setLastUpdateBy(currentUserId);
                        String ldapAttrName = EaasyStreet.getProperty(Constants.CFG_LDAP_NAMED_CONTACT_ATTR + contactId);
                        if (StringUtils.nullOrBlank(ldapAttrName)) {
                            EaasyStreet.logWarn("LDAP attribute name not specified for person contact \"" + contactId + "\".");
                        } else {
                            thisAttr = attributes.get(ldapAttrName);
                            if (thisAttr != null) {
                                attrValue = thisAttr.get().toString();
                                contact.setValue(attrValue);
                            }
                        }
                        contacts.put(contactId, contact);
                    }
                }
                person.setContacts(contacts);
                Map personAttributes = new HashMap();
                if (config.getPersonAttributeNames() != null) {
                    Iterator i = config.getPersonAttributeNames().iterator();
                    while (i.hasNext()) {
                        String attributeName = i.next().toString();
                        String ldapAttrName = EaasyStreet.getProperty(Constants.CFG_LDAP_NAMED_ATTRIBUTE_ATTR + attributeName);
                        if (StringUtils.nullOrBlank(ldapAttrName)) {
                            EaasyStreet.logWarn("LDAP attribute name not specified for person attribute \"" + attributeName + "\".");
                        } else {
                            thisAttr = attributes.get(ldapAttrName);
                            if (thisAttr != null) {
                                attrValue = thisAttr.get().toString();
                                personAttributes.put(attributeName, attrValue);
                            }
                        }
                    }
                }
                person.setAttributes(personAttributes);
                Map userAttributes = new HashMap();
                if (config.getUserAttributeNames() != null) {
                    Iterator i = config.getUserAttributeNames().iterator();
                    while (i.hasNext()) {
                        String attributeName = i.next().toString();
                        String ldapAttrName = EaasyStreet.getProperty(Constants.CFG_LDAP_NAMED_ATTRIBUTE_ATTR + attributeName);
                        if (StringUtils.nullOrBlank(ldapAttrName)) {
                            EaasyStreet.logWarn("LDAP attribute name not specified for user attribute \"" + attributeName + "\".");
                        } else {
                            thisAttr = attributes.get(ldapAttrName);
                            if (thisAttr != null) {
                                attrValue = thisAttr.get().toString();
                                userAttributes.put(attributeName, attrValue);
                            }
                        }
                    }
                }
                profile.setAttributes(userAttributes);
                Iterator i = attributeIds.iterator();
                while (i.hasNext()) {
                    String thisId = i.next().toString();
                    thisAttr = attributes.get(thisId);
                    if (thisAttr != null) {
                        attrValue = thisAttr.get().toString();
                        profile.setAttribute(thisId, attrValue);
                    }
                }
                executionResults = profile;
                responseCode = 0;
                responseString = "Execution complete";
            } catch (Throwable t) {
                responseCode = 2;
                responseString = "Exception processing LDAP data: " + t.toString();
                EaasyStreet.logWarn("Exception processing LDAP data: " + t.toString(), t);
            }
        } else {
            responseCode = 1;
            responseString = "User not on file";
        }
    }

    /**
	 * Used to execute the "getByEmail" command after all command parameters
	 * have been set and the command is "ready to excute".
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeGetByEmail() {
        responseCode = 4;
        responseString = "Action not available when using LDAP";
    }

    /**
	 * <p>Used to execute the "getUserIdsAndNames" command after all command
	 * parameters have been set and the command is "ready to excute".</p>
	 * <p>
	 * <strong>Parameters:</strong>
	 * <ul>
	 * <li><code>"filter"</code> - <code>Map</code> (search criteria - optional)</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <strong>ExecutionResults:</strong>
	 * <br><code>List</code>
	 * </p>
	 *
	 * @since Eaasy Street 2.2.1
	 */
    public void executeGetUserIdsAndNames() {
        responseCode = 4;
        responseString = "Action not available when using LDAP";
    }

    /**
	 * Used to execute the "reset" command after all command parameters
	 * have been set and the command is "ready to excute".
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeReset() {
        responseCode = 4;
        responseString = "Action not available when using LDAP";
    }

    /**
	 * Used to execute the "updateLastLogon" command after all command
	 * parameters have been set and the command is "ready to excute".
	 * <p>
	 * <strong>Parameters:</strong>
	 * <ul>
	 * <li><code>DataConnector.RECORD_KEY_PARAMETER</code> - <code>String</code>
	 * (userId)</li>
	 * <li><code>DataConnector.RECORD_PARAMETER</code> -
	 * <code>UserProfile</code> (profile fields)</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <strong>ExecutionResults:</strong>
	 * <br>(none)
	 * </p>
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeUpdateLastLogon() {
        responseCode = 0;
        responseString = "Nothing really happenned!!";
    }

    /**
	 * Used to execute the "updateSecurityQuestions" command after
	 * all command parameters have been set and the command is "ready
	 * to excute".
	 * <p>
	 * <strong>Parameters:</strong>
	 * <ul>
	 * <li><code>DataConnector.RECORD_KEY_PARAMETER</code> - <code>String</code>
	 * (userId)</li>
	 * <li><code>DataConnector.RECORD_PARAMETER</code> -
	 * <code>List</code> (security questions)</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <strong>ExecutionResults:</strong>
	 * <br>(none)
	 * </p>
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeUpdateSecurityQuestions() {
        responseCode = 4;
        responseString = "Action not available when using Domino";
    }

    /**
	 * Used to execute the "updateForcedApplications" command after all
	 * command parameters have been set and the command is "ready to excute".
	 *
	 * @since	Eaasy Street 2.0
	 */
    public void executeUpdateForcedApplications() {
        responseCode = 4;
        responseString = "Action not available when using LDAP";
    }

    /**
	 * Returns the user attributes from the LDAP data source.
	 * 
	 * @return	the user attributes from the LDAP data source
	 */
    private Attributes getUserAttributes(String userId) {
        DirContext context = getContext();
        Attributes atrs = null;
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningObjFlag(true);
        String searchStr = "(" + USER_ID_ATTR + "=" + userId + ")";
        NamingEnumeration res = null;
        try {
            res = context.search(SEARCH_BASE, searchStr, controls);
        } catch (NamingException e) {
            EaasyStreet.logError("Could not search for the user " + userId, e);
        }
        try {
            if (res == null || !res.hasMore()) {
                EaasyStreet.logError("Invalid User ID -- " + userId);
            }
        } catch (NamingException e) {
            EaasyStreet.logError("Could not search for the user " + userId, e);
        }
        try {
            SearchResult user = (SearchResult) res.next();
            atrs = user.getAttributes();
        } catch (NamingException e) {
            EaasyStreet.logError("Could not search for the user " + userId, e);
        }
        try {
            context.close();
        } catch (NamingException e) {
            EaasyStreet.logError("Could not close context", e);
        }
        return atrs;
    }

    /**
	 * Used to obtain an LDAP directory context.
	 *
	 * @since	Eaasy Street 2.0
	 */
    private DirContext getContext() {
        DirContext context = null;
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INIT_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, PROVIDER_URL);
        env.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
        env.put(Context.SECURITY_PRINCIPAL, SECURITY_PRINCIPAL);
        env.put(Context.SECURITY_CREDENTIALS, SECURITY_CREDENTIALS);
        try {
            context = new InitialDirContext(env);
        } catch (NamingException e) {
            EaasyStreet.logError("Could not create context", e);
        }
        return context;
    }

    /**
	 * Used to obtain the search string.
	 *
	 * @since	Eaasy Street 2.0
	 */
    private String getSearchString() {
        StringBuffer buffer = new StringBuffer();
        if (parameters.containsKey("filter")) {
            buffer.append("(&");
            buffer.append(SEARCH_STRING);
            SearchSpecification spec = (SearchSpecification) parameters.get("filter");
            SearchFilter[] filterSpec = spec.getFilter();
            for (int i = 0; i < filterSpec.length; i++) {
                SearchFilter thisSpec = filterSpec[i];
                String[] value = thisSpec.getFieldValue();
                if (value != null && !value.equals(Constants.EMPTY_STRING)) {
                    buffer.append(" (");
                    if (thisSpec.getFieldName().equals("userId")) {
                        buffer.append(USER_ID_ATTR);
                    } else if (thisSpec.getFieldName().equals("useName")) {
                        buffer.append(USE_NAME_ATTR);
                    } else if (thisSpec.getFieldName().equals("namePrefix")) {
                        buffer.append(NAME_PREFIX_ATTR);
                    } else if (thisSpec.getFieldName().equals("firstName")) {
                        buffer.append(FIRST_NAME_ATTR);
                    } else if (thisSpec.getFieldName().equals("middleName")) {
                        buffer.append(MIDDLE_NAME_ATTR);
                    } else if (thisSpec.getFieldName().equals("lastName")) {
                        buffer.append(LAST_NAME_ATTR);
                    } else if (thisSpec.getFieldName().equals("nameSuffix")) {
                        buffer.append(NAME_SUFFIX_ATTR);
                    } else {
                        buffer.append(thisSpec.getFieldName());
                    }
                    if (thisSpec.getFilterType().equals(SearchFilter.TYPE_EQUALS)) {
                        buffer.append("=");
                        for (int j = 0; j < value.length; j++) {
                            buffer.append(value[j]);
                            if ((value.length) != j + 1) buffer.append(" or ");
                        }
                    } else if (thisSpec.getFilterType().equals(SearchFilter.TYPE_STARTS_WITH)) {
                        buffer.append("=");
                        for (int j = 0; j < value.length; j++) {
                            buffer.append(value[j]);
                            if ((value.length) != j + 1) buffer.append(" or ");
                        }
                        buffer.append("*");
                    } else if (thisSpec.getFilterType().equals(SearchFilter.TYPE_ENDS_WITH)) {
                        buffer.append("=*");
                        for (int j = 0; j < value.length; j++) {
                            buffer.append(value[j]);
                            if ((value.length) != j + 1) buffer.append(" or ");
                        }
                    } else if (thisSpec.getFilterType().equals(SearchFilter.TYPE_CONTAINS)) {
                        buffer.append("=*");
                        for (int j = 0; j < value.length; j++) {
                            buffer.append(value[j]);
                            if ((value.length) != j + 1) buffer.append(" or ");
                        }
                        buffer.append("*");
                    }
                    buffer.append(")");
                }
            }
            buffer.append(")");
        } else {
            buffer.append(SEARCH_STRING);
        }
        return buffer.toString();
    }
}
