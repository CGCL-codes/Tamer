package com.armatiek.infofuze.source.ldap;

import java.io.Reader;
import java.util.ArrayList;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.armatiek.infofuze.error.InfofuzeException;
import com.armatiek.infofuze.source.AbstractStreamSource;
import com.armatiek.infofuze.source.Parameter;
import com.armatiek.infofuze.stream.ldap.LDAPReader;
import com.armatiek.infofuze.utils.XPathUtils;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchScope;

/**
 * The LDAPSource can be used to stream the result of a search operation on a
 * directory service using the Lightweight Directory Access Protocol (LDAP).
 * Examples of directory services that can be searched using LDAP are the
 * Microsoft Active Directory, Novell eDirectory, Apache Directory Service
 * (ApacheDS), Oracle Internet Directory (OID), and slapd, part of OpenLDAP. The
 * result of a search operation is written to the stream in the Directory
 * Services Markup Language (DSML) format.
 * 
 * @author Maarten Kroon
 */
public class LDAPSource extends AbstractStreamSource {

    protected static final Logger logger = LoggerFactory.getLogger(LDAPSource.class);

    protected LDAPConnectionPool pool;

    protected String directoryService;

    protected String baseDN;

    protected String scope;

    protected String filter;

    protected String[] attributes;

    public LDAPSource(Element configElem) throws InfofuzeException {
        super(configElem);
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            directoryService = XPathUtils.getRequiredStringValue(xpath, "directory-service", configElem);
            String host = XPathUtils.getRequiredStringValue(xpath, "host", configElem);
            int port = XPathUtils.getIntegerValue(xpath, "post", configElem, 389);
            String bindDN = XPathUtils.getRequiredStringValue(xpath, "bindDN", configElem);
            String bindPassword = XPathUtils.getRequiredStringValue(xpath, "bindPassword", configElem);
            baseDN = XPathUtils.getStringValue(xpath, "baseDN", configElem, null);
            scope = XPathUtils.getStringValue(xpath, "scope", configElem, null);
            filter = XPathUtils.getStringValue(xpath, "filter", configElem, null);
            NodeList attrElems = (NodeList) xpath.evaluate("attributes/attribute", configElem, XPathConstants.NODESET);
            if (attrElems.getLength() > 0) {
                ArrayList<String> attrList = new ArrayList<String>();
                for (int i = 0; i < attrElems.getLength(); i++) {
                    Element attrElem = (Element) attrElems.item(i);
                    attrList.add(attrElem.getTextContent());
                }
                attributes = attrList.toArray(new String[attrList.size()]);
            }
            LDAPConnection connection = new LDAPConnection(host, port, bindDN, bindPassword);
            pool = new LDAPConnectionPool(connection, 1, 10);
        } catch (Exception e) {
            throw new InfofuzeException("Error initializing LDAPSource \"" + name + "\"", e);
        }
    }

    public String getDirectoryService() {
        return directoryService;
    }

    @Override
    public Reader getInternalReader() {
        if (!isOpen) {
            throw new InfofuzeException("Could not get LDAPReader; Source \"" + name + "\" is not open");
        }
        try {
            String baseDN = null;
            String scope = null;
            String filter = null;
            String[] attributes = null;
            if (params != null) {
                for (Parameter param : params) {
                    String name = (String) param.getName();
                    if (name.equals("baseDN")) {
                        baseDN = (String) param.getValue();
                    } else if (name.equals("scope")) {
                        scope = (String) param.getValue();
                    } else if (name.equals("filter")) {
                        filter = (String) param.getValue();
                    } else if (name.equals("attributes")) {
                        attributes = ((String) param.getValue()).trim().split("[,|;]");
                        for (int i = 0; i < attributes.length; i++) {
                            attributes[i] = attributes[i].trim();
                        }
                    } else {
                        logger.warn("Unsupported param \"" + name + "\" in Source \"" + this.name + "\"");
                    }
                }
            }
            baseDN = (baseDN == null) ? this.baseDN : baseDN;
            if (baseDN == null) {
                throw new InfofuzeException("Parameter \"baseDN\" can not be null");
            }
            scope = (scope == null) ? this.scope : scope;
            if (scope == null) {
                throw new InfofuzeException("Parameter \"scope\" can not be null");
            }
            filter = (filter == null) ? this.filter : filter;
            if (filter == null) {
                throw new InfofuzeException("Parameter \"filter\" can not be null");
            }
            attributes = (attributes == null) ? this.attributes : attributes;
            SearchScope searchScope;
            if (scope.equalsIgnoreCase("BASE")) {
                searchScope = SearchScope.BASE;
            } else if (scope.equalsIgnoreCase("ONE")) {
                searchScope = SearchScope.ONE;
            } else if (scope.equalsIgnoreCase("SUB")) {
                searchScope = SearchScope.SUB;
            } else if (scope.equalsIgnoreCase("SUBORDINATE_SUBTREE")) {
                searchScope = SearchScope.SUBORDINATE_SUBTREE;
            } else {
                throw new InfofuzeException("Value \"" + scope + "\" for parameter \"scope\" not supported");
            }
            SearchResult searchResult = pool.search(baseDN, searchScope, filter, attributes);
            return new LDAPReader(searchResult);
        } catch (Exception e) {
            throw new InfofuzeException("Could not get LDAPReader", e);
        }
    }
}
