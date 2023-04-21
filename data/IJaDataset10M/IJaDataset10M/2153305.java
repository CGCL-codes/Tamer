package org.xaware.server.engine.instruction.bizcomps.config.ldap;

/**
 * This class contains the logic to handle child elements of xa:search which are follows
 * xa:search_base, xa:search_scope, xa:search_filter, xa:search_timelimit, xa:search_countlimit.
 * 
<br>
 * <br>
 * As far as LDAP BizComponent it deals with xa:name and xa:value attribute. <br>
 * <br>
 * 
 * <br>
 * <br>
 * Please refer to <br>
 * <br>
 * org.xaware.server.engine.instruction.bizcomps.config.ldap.LDAPAttributesConfig
 * org.xaware.server.engine.instruction.bizcomps.config.ldap.LDAPAttributeValueConfig
 * org.xaware.server.engine.instruction.bizcomps.config.ldap.LDAPEntryConfig
 * org.xaware.server.engine.instruction.bizcomps.config.ldap.RequestConfig
 * org.xaware.server.engine.instruction.bizcomps.config.ldap.LDAPAttributeIDConfig
 * org.xaware.server.engine.instruction.bizcomps.config.ldap.SearchConfig, for
 * more details.
 * 
 * @author Vasu Thadaka
 */
public class SearchAttributeConfig {

    /** Search Base constant */
    private static final String LDAP_SEARCH_BASE = "search_base";

    /** Search Count Limit constant */
    private static final String LDAP_SEARCH_COUNTLIMIT = "search_countlimit";

    /** Search Filter constant */
    private static final String LDAP_SEARCH_FILTER = "search_filter";

    /** Search Scope constant */
    private static final String LDAP_SEARCH_SCOPE = "search_scope";

    /** Search Time Limit constant */
    private static final String LDAP_SEARCH_TIMELIMIT = "search_timelimit";

    /** Name value of the attribute */
    private String name;

    /** value of attribute */
    private String value;

    /** Default Constructor */
    public SearchAttributeConfig(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return the value
	 */
    public String getValue() {
        return value;
    }

    /** Determines whether attribute is Search Base or not */
    public boolean isSearchBase() {
        if (name.equals(LDAP_SEARCH_BASE)) return true;
        return false;
    }

    /** Determines whether attribute is Search Countlimit or not */
    public boolean isSearchCountLimit() {
        if (name.equals(LDAP_SEARCH_COUNTLIMIT)) return true;
        return false;
    }

    /** Determines whether attribute is Search Filter or not */
    public boolean isSearchFilter() {
        if (name.equals(LDAP_SEARCH_FILTER)) return true;
        return false;
    }

    /** Determines whether attribute is Search Scope or not */
    public boolean isSearchScope() {
        if (name.equals(LDAP_SEARCH_SCOPE)) return true;
        return false;
    }

    /** Determines whether attribute is Search Timelimit or not */
    public boolean isSearchTimeLimit() {
        if (name.equals(LDAP_SEARCH_TIMELIMIT)) return true;
        return false;
    }
}
