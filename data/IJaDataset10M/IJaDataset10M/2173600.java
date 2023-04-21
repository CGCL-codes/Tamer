package com.nubotech.gwt.oss.client;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * The Grantee of a Group can either be a User or a Group.
 * @author jonnakkerud
 */
public abstract class Grantee {

    public static final String CANONICAL_USER = "CanonicalUser";

    public static final String GROUP = "Group";

    public static final String EMAIL_USER = "AmazonCustomerByEmail";

    private String type;

    private Map properties;

    public static Grantee createGrantee(String type) {
        if (CANONICAL_USER.equals(type)) {
            return new CanonicalUser();
        } else if (GROUP.equals(type)) {
            return new Group();
        } else if (EMAIL_USER.equals(type)) {
            return new EmailUser();
        }
        return null;
    }

    /**
     * Return the type of Grantee. Can be a Group, CanonicalUser or AmazonCustomerByEmail
     * @return The Grantee type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the Grantee type: CanonicalUser, Group or AmazonCustomerByEmail.
     * @param type The type of Grantee
     */
    protected void setType(String type) {
        this.type = type;
    }

    /**
     * Return the propeties associated with the Grantee
     * @return Return the Grantee properties
     */
    public Map getProperties() {
        if (properties == null) {
            properties = new HashMap();
        }
        return properties;
    }
}
