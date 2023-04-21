package org.scopemvc.util.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.security.auth.Subject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.ResourceProvider;
import org.scopemvc.core.security.Permissions;
import org.xml.sax.SAXException;

/**
 * Implementation of Permissions, where the permissions are defined in a XML
 * file. The location of the file is defined in the properties supplied to the
 * initialize method. <p>
 *
 * Example of a XML document defining the permissions: <pre>
 * <?xml version="1.0" ?>
 * <policy>
 *
 * <grant principal="guests"> <permission>
 *
 * ViewChart</permission> <permission>
 *
 * navigation.*</permission> </grant> <grant principal="users"> <permission>
 *
 * ViewChart</permission> <permission>
 *
 * CreateProject</permission> <permission>
 *
 * navigation.*</permission> </grant> <grant principal="administrators">
 * <permission>
 *
 * *</permission> </grant> </policy> </pre>
 *
 * @author Patrik Nordwall
 * @version $Revision: 1.4 $
 * @created December 3, 2002
 */
public class XmlPermissionResources implements Permissions {

    /**
     * @errorId permission.invalid_xml_format Invalid format of permissions XML
     *      file
     */
    public static final String XML_PERMISSION_ERRID = "permission.invalid_xml_format";

    /**
     * @errorId permission.not_found Permissions XML file not found
     */
    public static final String PERMISSION_NOT_FOUND_ERRID = "permission.not_found";

    /**
     * @appProperty permissions.xml_url URL to the XML file containing the
     *      permission definitions, the file is loaded as a resource
     */
    public static final String XML_URL_PROP = "permissions.xml_url";

    private static final Log LOG = LogFactory.getLog(XmlPermissionResources.class);

    /**
     * Map containing Principal names as keys and Set of grants for resource
     * names as values.
     */
    private HashMap mPermissionsForPrincipal = new HashMap();

    /**
     * Map containing Principal names as keys and Set of grants for resource
     * names ending with wildcard as values. The wildcard character '*' is
     * omitted.
     */
    private HashMap mWildcardPermissionsForPrincipal = new HashMap();

    private ResourceProvider resourceProvider;

    /**
     * Constructor for the XmlPermissionResources object
     */
    public XmlPermissionResources() {
    }

    /**
     * Set the resource provider owning this resource. <p>
     *
     * The properties define in the provider should contain the following key:
     * <br>
     * 'permissions.xml_url' that defines the URL to the XML file containing the
     * permission definitions; the file is loaded as a resource, if the property
     * is not defined 'permissions.xml' is used as default
     *
     * @param inProvider The resource provider
     */
    public void setResourceProvider(ResourceProvider inProvider) {
        resourceProvider = inProvider;
        if (resourceProvider != null) {
            init();
        }
    }

    /**
     * Checks if permission is allowed to the requested resource. The caller
     * must have done Subject.doAs, so the code runs as the logged in Subject.
     *
     * @param resourceName id of the resource to check
     * @return true if access is granted to the resource
     */
    public boolean checkPermission(String resourceName) {
        Iterator principals = getSubject().getPrincipals().iterator();
        while (principals.hasNext()) {
            Principal p = (Principal) principals.next();
            if (checkPermission(resourceName, p.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return subject Subject that represents current logged in and
     *      authenticated user
     */
    protected Subject getSubject() {
        Subject subject = Subject.getSubject(AccessController.getContext());
        if (subject == null) {
            String m = "Subject could not be retrieved from AccessController.";
            LOG.error(m);
            throw new RuntimeException(m);
        }
        return subject;
    }

    /**
     * TODO: document the method
     *
     * @param resourceName TODO: Describe the Parameter
     * @param principalName TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    protected boolean checkPermission(String resourceName, String principalName) {
        Set permissionSet = (Set) mPermissionsForPrincipal.get(principalName);
        if (permissionSet != null) {
            if (permissionSet.contains(resourceName)) {
                return true;
            }
        }
        permissionSet = (Set) mWildcardPermissionsForPrincipal.get(principalName);
        if (permissionSet != null) {
            Iterator iter = permissionSet.iterator();
            while (iter.hasNext()) {
                String p = (String) iter.next();
                if (p.equals("") || resourceName.startsWith(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void init() {
        Properties properties = resourceProvider.getProperties();
        String permissionsUrl = properties.getProperty(XML_URL_PROP, "permissions.xml");
        if (permissionsUrl.startsWith("/")) {
            permissionsUrl = permissionsUrl.substring(1);
        }
        try {
            InputStream in = resourceProvider.getInputStream(permissionsUrl, null, null);
            if (in == null) {
                String msg = "[" + PERMISSION_NOT_FOUND_ERRID + "] " + "Permissions XML file not found: " + permissionsUrl;
                LOG.warn(msg);
                throw new RuntimeException(msg);
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document document = parser.parse(in);
            NodeList grants = document.getElementsByTagName("grant");
            int numGrants = grants.getLength();
            for (int i = 0; i < numGrants; i++) {
                Element grant = (Element) grants.item(i);
                String principal = grant.getAttribute("principal");
                if (principal == null || principal.equals("")) {
                    String m = "[" + XML_PERMISSION_ERRID + "] " + "Principal attribute not defined";
                    LOG.error(m);
                    throw new RuntimeException(m);
                }
                NodeList permissions = grant.getElementsByTagName("permission");
                int numPermissions = permissions.getLength();
                for (int j = 0; j < numPermissions; j++) {
                    Node permission = permissions.item(j);
                    if (permission != null) {
                        String permissionText = permission.getFirstChild().getNodeValue();
                        if (permissionText.endsWith("*")) {
                            Set permissionSet = (Set) mWildcardPermissionsForPrincipal.get(principal);
                            if (permissionSet == null) {
                                permissionSet = new HashSet();
                                mWildcardPermissionsForPrincipal.put(principal, permissionSet);
                            }
                            permissionSet.add(permissionText.substring(0, permissionText.length() - 1));
                        } else {
                            Set permissionSet = (Set) mPermissionsForPrincipal.get(principal);
                            if (permissionSet == null) {
                                permissionSet = new HashSet();
                                mPermissionsForPrincipal.put(principal, permissionSet);
                            }
                            permissionSet.add(permissionText);
                        }
                    }
                }
            }
            in.close();
        } catch (ParserConfigurationException e) {
            String m = "[" + XML_PERMISSION_ERRID + "] " + e.getMessage();
            LOG.error(m, e);
            throw new RuntimeException(m);
        } catch (SAXException e) {
            String m = "[" + XML_PERMISSION_ERRID + "] " + e.getMessage();
            LOG.error(m, e);
            throw new RuntimeException(m);
        } catch (IOException e) {
            String m = "[" + XML_PERMISSION_ERRID + "] " + e.getMessage();
            LOG.error(m, e);
            throw new RuntimeException(m);
        }
    }
}
