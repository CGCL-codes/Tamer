package br.ufmg.saotome.arangi.model.dao.ldap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.log4j.Logger;
import br.ufmg.saotome.arangi.commons.BasicException;
import br.ufmg.saotome.arangi.commons.MessageFactory;
import br.ufmg.saotome.arangi.dto.LDAPConfigService;

/**
 * Service to retrieve information from LDAP servers.
 * @author C�sar Correia
 *
 */
public class LDAPService {

    protected static Logger log = Logger.getLogger(LDAPService.class);

    /**
	 * Retrieve data from a LDAP server. All information necessary to 
	 * search must be in LDAPConfigService parameter.
	 * @param configData
	 * @return
	 */
    public static java.util.List retrieveData(LDAPConfigService configData) throws BasicException {
        List results = new ArrayList();
        try {
            if (configData != null) {
                Hashtable env = new Hashtable(11);
                env.put(Context.INITIAL_CONTEXT_FACTORY, configData.getInitialContextFactory());
                env.put(Context.PROVIDER_URL, configData.getProviderUrl());
                env.put(Context.SECURITY_AUTHENTICATION, configData.getSecurityAuthenticationMethod());
                env.put(Context.SECURITY_PRINCIPAL, configData.getUser());
                env.put(Context.SECURITY_CREDENTIALS, configData.getPassword());
                DirContext ctx = new InitialDirContext(env);
                if (log.isDebugEnabled()) {
                    log.debug("Conectou no LDAP  ");
                }
                SearchControls ctls = new SearchControls();
                ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                ctls.setReturningAttributes(configData.getRetrievingAttributeNames());
                String filter = createSearchFilter(configData);
                if (log.isDebugEnabled()) {
                    log.debug("Antes search no LDAP  ");
                }
                NamingEnumeration answer = ctx.search(configData.getBaseNode(), filter, ctls);
                int attributesNumber = configData.getRetrievingAttributeNames().length;
                String[] attributes = configData.getRetrievingAttributeNames();
                String[] valorAtributo = null;
                while (answer.hasMore()) {
                    log.debug("Atributos do LDAP  ");
                    valorAtributo = new String[attributesNumber];
                    SearchResult sr = (SearchResult) answer.next();
                    if (log.isDebugEnabled()) {
                        log.debug(">>>" + sr.getName());
                    }
                    Attributes attrs = sr.getAttributes();
                    if (attrs != null) {
                        for (int i = 0; i < attributesNumber; i++) {
                            if (log.isDebugEnabled()) {
                                log.debug("attribute: " + attributes[i]);
                            }
                            Attribute attr = attrs.get(attributes[i]);
                            if (attr != null) {
                                valorAtributo[i] = attr.get().toString();
                                if (log.isDebugEnabled()) {
                                    log.debug("value: " + valorAtributo[i]);
                                    log.debug("size: " + valorAtributo[i].length());
                                }
                            } else {
                                if (log.isDebugEnabled()) {
                                    log.debug("value: NULL");
                                }
                            }
                        }
                        results.add(valorAtributo);
                    }
                }
                ctx.close();
                if (log.isDebugEnabled()) {
                    log.debug("After desconnect from LDAP  ");
                }
            }
            return results;
        } catch (Exception e) {
            String rootMessage = "Search LDAP server error";
            String message = MessageFactory.composeMessageFromException(e);
            log.error(rootMessage + ": " + message);
            throw new BasicException(rootMessage + ": " + message, "searchLDAPServerError", new String[] { message }, "ArangiResources");
        }
    }

    /**
	 * Make a LDAP search filter
	 */
    protected static String createSearchFilter(LDAPConfigService configData) {
        Iterator it = configData.getParametersIterator();
        StringBuffer searchFilter = new StringBuffer();
        searchFilter.append("(&");
        while (it.hasNext()) {
            String name = (String) it.next();
            String value = configData.getParameterValue(name);
            searchFilter.append(" (" + name + "=" + value + ")");
        }
        searchFilter.append(")");
        if (log.isDebugEnabled()) {
            log.debug("LDAPService - searchFilter = " + searchFilter.toString());
        }
        return searchFilter.toString();
    }
}
