package org.kablink.teaming.spring.security.ldap;

import java.util.HashSet;
import java.util.Set;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kablink.teaming.util.ReflectHelper;
import org.kablink.teaming.util.SPropsUtil;
import org.kablink.teaming.util.WindowsUtil;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.core.ContextExecutor;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;

public class PreAuthenticatedSpringSecurityLdapTemplate extends SpringSecurityLdapTemplate {

    private static final Log logger = LogFactory.getLog(PreAuthenticatedSpringSecurityLdapTemplate.class);

    private static DomainMatcher domainMatcher;

    private SearchControls searchControls = new SearchControls();

    public PreAuthenticatedSpringSecurityLdapTemplate(ContextSource contextSource) {
        super(contextSource);
    }

    public DirContextOperations searchForSingleEntry(final String base, final String filter, String accountname) {
        final String username = WindowsUtil.getSamaccountname(accountname);
        if (username == null) throw new IllegalArgumentException("username must be specified");
        final String domainname = WindowsUtil.getDomainname(accountname);
        return (DirContextOperations) executeReadOnly(new ContextExecutor() {

            public Object executeWithContext(DirContext ctx) throws NamingException {
                DistinguishedName ctxBaseDn = new DistinguishedName(ctx.getNameInNamespace());
                NamingEnumeration resultsEnum = ctx.search(base, filter, new String[] { username }, searchControls);
                Set results = new HashSet();
                try {
                    while (resultsEnum.hasMore()) {
                        SearchResult searchResult = (SearchResult) resultsEnum.next();
                        StringBuffer dn = new StringBuffer(searchResult.getName());
                        if (base.length() > 0) {
                            dn.append(",");
                            dn.append(base);
                        }
                        String dnStr = dn.toString();
                        if (domainname != null && domainname.length() > 0) {
                            if (getDomainMatcher().matches(domainname, dnStr, searchResult)) {
                                if (logger.isDebugEnabled()) logger.debug("Found a domain match [" + dnStr + "]");
                                results.add(new DirContextAdapter(searchResult.getAttributes(), new DistinguishedName(dnStr), ctxBaseDn));
                            } else {
                                logger.info("Discarding [" + dnStr + "] because it does not match domain " + domainname);
                            }
                        } else {
                            if (logger.isDebugEnabled()) logger.debug("Found a match [" + dnStr + "]");
                            results.add(new DirContextAdapter(searchResult.getAttributes(), new DistinguishedName(dnStr), ctxBaseDn));
                        }
                    }
                } catch (PartialResultException e) {
                    logger.info("Ignoring PartialResultException: " + e.toString());
                }
                if (results.size() == 0) {
                    throw new IncorrectResultSizeDataAccessException(1, 0);
                }
                if (results.size() > 1) {
                    throw new IncorrectResultSizeDataAccessException(1, results.size());
                }
                return results.toArray()[0];
            }
        });
    }

    public void setSearchControls(SearchControls searchControls) {
        super.setSearchControls(searchControls);
        this.searchControls = searchControls;
    }

    private DomainMatcher getDomainMatcher() {
        if (domainMatcher == null) {
            String className = SPropsUtil.getString("ldap.domain.matcher.class", "org.kablink.teaming.spring.security.ldap.DefaultDomainMatcher");
            domainMatcher = (DomainMatcher) ReflectHelper.getInstance(className);
        }
        return domainMatcher;
    }
}
