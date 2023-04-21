package com.controltier.ctl.authorization;

import com.controltier.ctl.authorization.AuthorizationException;
import com.controltier.ctl.jndi.JndiConfig;
import com.controltier.ctl.utils.IPropertyLookup;

/**
 * ControlTier Software Inc.
 * User: alexh
 * Date: Aug 17, 2005
 * Time: 4:29:25 PM
 */
public class JndiConfigParser {

    private final IPropertyLookup propertyLookup;

    /**
     * property keys used to lookup jndi config info
     */
    static final String PREFIX = "jndi.";

    static final String CONNECTTION_NAME = PREFIX + "connectionName";

    static final String CONNECTION_PASS = PREFIX + "connectionPassword";

    static final String CONNECTION_URL = PREFIX + "connectionUrl";

    static final String ROLE_BASE = PREFIX + "roleBase";

    static final String ROLE_NAME = PREFIX + "roleNameRDN";

    static final String ROLE_MEMBER = PREFIX + "roleMemberRDN";

    static final String USER_BASE = PREFIX + "userBase";

    static final String USER_NAME_RDN = PREFIX + "userNameRDN";

    public static final String JNDI_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    static final String[] REQUIRED_PROPS = new String[] { CONNECTTION_NAME, CONNECTION_PASS, CONNECTION_URL, ROLE_BASE, ROLE_NAME, ROLE_MEMBER, USER_BASE, USER_NAME_RDN };

    protected JndiConfigParser(final IPropertyLookup propertyLookup) {
        this.propertyLookup = propertyLookup;
    }

    private String lookup(final String key) {
        if (!propertyLookup.hasProperty(key)) {
            throw new AuthorizationException(key + " jndi config property not found");
        }
        final String value = propertyLookup.getProperty(key);
        if (null == value || "".equals(value)) {
            throw new AuthorizationException(key + " property has null or empty value");
        }
        return value;
    }

    /**
     * Access the supplied Map object to pull out the properties that contain the JNDI config
     *
     * @return
     */
    protected JndiConfig parse() {
        return new JndiConfig(lookup(CONNECTTION_NAME), lookup(CONNECTION_PASS), lookup(CONNECTION_URL), lookup(ROLE_BASE), lookup(ROLE_NAME), lookup(ROLE_MEMBER), lookup(USER_BASE), lookup(USER_NAME_RDN));
    }
}
