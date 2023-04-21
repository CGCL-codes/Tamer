package org.kablink.teaming.spring.security.ldap;

import javax.naming.directory.SearchResult;

public class DefaultDomainMatcher implements DomainMatcher {

    public boolean matches(String domainName, String dn, SearchResult searchResult) {
        return (dn.toLowerCase().contains("=" + domainName.toLowerCase()));
    }
}
