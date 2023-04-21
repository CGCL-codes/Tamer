package de.fhkl.helloWorld.interfaces.model.account.profile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import de.fhkl.helloWorld.interfaces.model.attribute.SingleAttribute;
import de.fhkl.helloWorld.interfaces.model.attribute.StructuredAttribute;

public class OpenID extends StructuredAttribute<String, SingleAttribute<String>> {

    public OpenID() {
        super("openID");
        attributes.put("url", new SingleAttribute<String>("url"));
        attributes.put("username", new SingleAttribute<String>("username"));
        attributes.put("password", new SingleAttribute<String>("password"));
    }

    public OpenID(String url, String username, String password) {
        this();
        setUrl(url);
        setPassword(password);
        setUsername(username);
    }

    public String getUrl() {
        return getAttribute("url").getValue();
    }

    public void setUrl(String url) {
        getAttribute("url").setValue(url);
    }

    public String getUsername() {
        return getAttribute("username").getValue();
    }

    public void setUsername(String username) {
        getAttribute("username").setValue(username);
    }

    public String getPassword() {
        return getAttribute("password").getValue();
    }

    public void setPassword(String password) {
        getAttribute("password").setValue(password);
    }

    public Element parseToXML(Document dom, boolean isSubProfile) {
        Element elem = super.parseToXML(dom);
        return elem;
    }
}
