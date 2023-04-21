package org.exist.security.utils;

import org.exist.EXistException;
import org.exist.config.ConfigurationException;
import org.exist.security.Account;
import org.exist.security.Group;
import org.exist.security.PermissionDeniedException;
import org.exist.security.SecurityManager;
import org.exist.security.Subject;
import org.exist.security.internal.RealmImpl;
import org.exist.security.internal.aider.GroupAider;
import org.exist.security.internal.aider.UserAider;
import org.exist.util.EXistInputSource;
import org.exist.xmldb.XmldbURI;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 * 
 */
public class ConverterFrom1_0 {

    public static final String LEGACY_USERS_DOCUMENT_PATH = "/db/system/users.xml";

    private static final String GROUP = "group";

    private static final String NAME = "name";

    private static final String PASS = "password";

    private static final String DIGEST_PASS = "digest-password";

    private static final String USER_ID = "uid";

    private static final String HOME = "home";

    public static void convert(EXistInputSource is) {
    }

    public static void convert(Subject invokingUser, SecurityManager sm, Document acl) throws PermissionDeniedException, EXistException {
        Element docElement = null;
        if (acl != null) docElement = acl.getDocumentElement();
        if (docElement == null) {
        } else {
            Element root = acl.getDocumentElement();
            Attr version = root.getAttributeNode("version");
            int major = 0;
            int minor = 0;
            if (version != null) {
                String[] numbers = version.getValue().split("\\.");
                major = Integer.parseInt(numbers[0]);
                minor = Integer.parseInt(numbers[1]);
            }
            NodeList nl = root.getChildNodes();
            Node node;
            Element next;
            Account account;
            NodeList ul;
            Group group;
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
                next = (Element) nl.item(i);
                if (next.getTagName().equals("users")) {
                    ul = next.getChildNodes();
                    for (int j = 0; j < ul.getLength(); j++) {
                        node = ul.item(j);
                        if (node.getNodeType() == Node.ELEMENT_NODE && node.getLocalName().equals("user")) {
                            account = createAccount(major, minor, (Element) node);
                            if (sm.hasAccount(account.getName())) {
                                sm.updateAccount(invokingUser, account);
                            } else {
                                sm.addAccount(account);
                            }
                        }
                    }
                } else if (next.getTagName().equals("groups")) {
                    ul = next.getChildNodes();
                    for (int j = 0; j < ul.getLength(); j++) {
                        node = ul.item(j);
                        if (node.getNodeType() == Node.ELEMENT_NODE && node.getLocalName().equals("group")) {
                            group = createGroup((Element) node);
                            if (sm.hasGroup(group.getName())) {
                                sm.updateGroup(invokingUser, group);
                            } else {
                                sm.addGroup(group);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * Read a account information from the given DOM node
	 * 
	 * @param node
	 *            Description of the Parameter
	 *@exception DatabaseConfigurationException
	 *                Description of the Exception
	 * @throws ConfigurationException 
	 * @throws PermissionDeniedException 
	 * @throws DOMException 
	 */
    public static Account createAccount(int majorVersion, int minorVersion, Element node) throws ConfigurationException, DOMException, PermissionDeniedException {
        String password = null;
        String digestPassword = null;
        int id = -1;
        XmldbURI home = null;
        String name = node.getAttribute(NAME);
        if (name == null) throw new ConfigurationException("account needs a name");
        Attr attr;
        if (majorVersion == 0) {
            attr = node.getAttributeNode(PASS);
            digestPassword = attr == null ? null : attr.getValue();
        } else {
            attr = node.getAttributeNode(PASS);
            password = attr == null ? null : attr.getValue();
            attr = node.getAttributeNode(DIGEST_PASS);
            digestPassword = attr == null ? null : attr.getValue();
        }
        Attr userId = node.getAttributeNode(USER_ID);
        if (userId == null) throw new ConfigurationException("attribute id is missing");
        try {
            id = Integer.parseInt(userId.getValue());
        } catch (NumberFormatException e) {
            throw new ConfigurationException("illegal user id: " + userId + " for account " + name);
        }
        Attr homeAttr = node.getAttributeNode(HOME);
        home = homeAttr == null ? null : XmldbURI.create(homeAttr.getValue());
        if (id == 1 && password == null) password = "";
        Account new_account = new UserAider(RealmImpl.ID, name);
        new_account.setPassword(password);
        new_account.setHome(home);
        NodeList gl = node.getChildNodes();
        Node group;
        for (int i = 0; i < gl.getLength(); i++) {
            group = gl.item(i);
            if (group.getNodeType() == Node.ELEMENT_NODE && group.getLocalName().equals(GROUP)) new_account.addGroup(group.getFirstChild().getNodeValue());
        }
        return new_account;
    }

    public static Group createGroup(Element element) {
        return new GroupAider(RealmImpl.ID, element.getAttribute("name"));
    }
}
