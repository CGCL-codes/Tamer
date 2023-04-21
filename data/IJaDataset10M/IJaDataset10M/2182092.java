package net.sf.ldaptemplate.samples.person.dao;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import net.sf.ldaptemplate.samples.person.domain.Person;
import net.sf.ldaptemplate.support.DirContextAdapter;
import net.sf.ldaptemplate.support.DirContextOperations;
import org.apache.commons.lang.StringUtils;

/**
 * Traditional implementation of PersonDao. This implementation uses the basic
 * JNDI interfaces and classes {@link DirContext}, {@link Attributes},
 * {@link Attribute}, and {@link NamingEnumeration}. The purpose is to
 * contrast this implementation with that of
 * {@link net.sf.ldaptemplate.samples.person.dao.PersonDaoImpl}.
 * 
 * @author Ulrik Sandberg
 * TODO Add search method so we can use a search page
 */
public class TraditionalPersonDaoImpl implements PersonDao {

    private String url;

    private String base;

    DirContextOperations getContextToBind(Person person) {
        DirContextAdapter adapter = new DirContextAdapter();
        adapter.setAttributeValue("objectclass", new String[] { "top", "person" });
        adapter.setAttributeValue("cn", person.getFullName());
        adapter.setAttributeValue("sn", person.getLastName());
        adapter.setAttributeValue("description", person.getDescription());
        return adapter;
    }

    public void create(Person person) {
    }

    public void update(Person person) {
    }

    public void delete(Person person) {
    }

    public List findAll() {
        DirContext ctx = createContext();
        LinkedList list = new LinkedList();
        NamingEnumeration results = null;
        try {
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = ctx.search("", "(objectclass=person)", controls);
            while (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();
                String dn = searchResult.getName();
                Attributes attributes = searchResult.getAttributes();
                list.add(mapToPerson(dn, attributes));
            }
        } catch (NameNotFoundException e) {
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                }
            }
        }
        return list;
    }

    public Person findByPrimaryKey(String country, String company, String fullname) {
        DirContext ctx = createContext();
        String dn = buildDn(country, company, fullname);
        try {
            Attributes attributes = ctx.getAttributes(dn);
            return mapToPerson(dn, attributes);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private String buildDn(String country, String company, String fullname) {
        StringBuffer sb = new StringBuffer();
        sb.append("cn=");
        sb.append(fullname);
        sb.append(",");
        sb.append("ou=");
        sb.append(company);
        sb.append(",");
        sb.append("c=");
        sb.append(country);
        String dn = sb.toString();
        return dn;
    }

    private DirContext createContext() {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        String tempUrl = createUrl();
        env.put(Context.PROVIDER_URL, tempUrl);
        DirContext ctx;
        try {
            ctx = new InitialDirContext(env);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return ctx;
    }

    private Person mapToPerson(String dn, Attributes attributes) throws NamingException {
        Person person = new Person();
        person.setFullName((String) attributes.get("cn").get());
        person.setLastName((String) attributes.get("sn").get());
        person.setDescription((String) attributes.get("description").get());
        person.setPhone((String) attributes.get("telephoneNumber").get());
        String countryMarker = ",c=";
        String country = dn.substring(dn.lastIndexOf(countryMarker) + countryMarker.length());
        person.setCountry(country);
        String companyMarker = ",ou=";
        String company = dn.substring(dn.lastIndexOf(companyMarker) + companyMarker.length(), dn.lastIndexOf(countryMarker));
        person.setCompany(company);
        return person;
    }

    private String createUrl() {
        String tempUrl = url;
        if (!tempUrl.endsWith("/")) {
            tempUrl += "/";
        }
        if (StringUtils.isNotEmpty(base)) {
            tempUrl += base;
        }
        return tempUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
