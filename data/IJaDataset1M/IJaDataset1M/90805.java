package scam.webdav.login;

import scam.*;
import scam.share.User;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

/**
 * The SSO-specification designed by Altcom for Skolverket.
 * This design also allows extraction of groups, mail and other
 * properties. However not used at this moment.
 * 
 * @author Jan Danils
 * @author J�ran Stark
 * @version $Revision: 1.1.1.1 $
 */
public class AltcomSSO extends Login {

    public static final String SSO_USER_HEADER = "SSO-user";

    public static final String SSO_FORCE_LOGIN_HEADER = "SSO-forcelogin";

    public static final String LOGIN_TYPE = "any";

    public AltcomSSO() {
        super();
    }

    public User getUser(HttpServletRequest req) throws LoginException {
        User user = null;
        String userHeader = req.getHeader(SSO_USER_HEADER);
        if (userHeader == null || userHeader.equals("")) return null;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(new InputSource(new StringReader(userHeader)));
            Element rootElement = document.getDocumentElement();
            NodeList childList = rootElement.getChildNodes();
            for (int i = 0; i < childList.getLength() && user == null; i++) {
                Node currentNode = childList.item(i);
                switch(currentNode.getNodeType()) {
                    case Node.TEXT_NODE:
                        break;
                    case Node.ELEMENT_NODE:
                        if (currentNode.getNodeName().endsWith("uid")) {
                            String username = currentNode.getNodeValue();
                            DEBUG.println("AltcomSSO: Found UID: '" + username + "'");
                            if (!username.equals("")) user = new User(username);
                        }
                        break;
                }
            }
        } catch (Throwable e) {
            DEBUG.println(DEBUG.HIGH, "AltcomSSO (Login): Error ", e);
            throw new LoginException(LoginException.BAD_REQUEST);
        }
        return user;
    }

    public void requestLogin(HttpServletResponse resp) {
        DEBUG.println("Login: Adding AltcomSSO login-header.");
        resp.addHeader(SSO_FORCE_LOGIN_HEADER, LOGIN_TYPE);
    }
}
