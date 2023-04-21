package com.googlecode.webduff.authentication;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.googlecode.webduff.WebDuffConfiguration;
import com.googlecode.webduff.WebdavStatus;
import com.googlecode.webduff.authentication.provider.Credential;
import com.googlecode.webduff.exceptions.MethodResponseError;

public class WebDuffXMLAuthentication implements WebdavAuthentication {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WebDuffXMLAuthentication.class);

    private Map<String, WebDuffUser> users;

    public void init(WebDuffConfiguration config) {
        SAXReader theReader = new SAXReader();
        try {
            Document authDocument = theReader.read("file://" + config.getPath("src").getPath(System.getProperty("file.separator")));
            Element root = authDocument.getRootElement();
            users = new Hashtable<String, WebDuffUser>();
            for (Iterator<?> i = root.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                users.put(element.attributeValue("name"), new WebDuffUser(element.attributeValue("name"), element.attributeValue("password")));
            }
        } catch (DocumentException e) {
            log.fatal("XML authorization:", e);
        }
    }

    public void authenticate(Credential credential) throws MethodResponseError {
        WebDuffUser user = users.get(credential.getUsername());
        if (!(credential.getUsername().equals(user.getUsername()) && credential.checkPassword(user.getPassword()))) {
            throw new MethodResponseError(WebdavStatus.SC_UNAUTHORIZED);
        }
    }
}
