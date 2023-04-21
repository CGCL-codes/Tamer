package com.oldportal.objectsbuilder.document;

/**
 * User (document editor).
 */
public class User extends Object implements XMLSerializable {

    public User() {
    }

    String userName = new String();

    public void load(org.jdom.Element element) {
        if (element.getName() != "Type") return;
        org.jdom.Element superElement = element.getChild("super");
        super.load((org.jdom.Element) superElement.getChildren().get(0));
        userName = element.getAttribute("userName").getValue();
    }

    public org.jdom.Element save() {
        org.jdom.Element element = new org.jdom.Element("Object");
        element.setAttribute("userName", userName);
        return element;
    }

    public void resolveDependencies() {
        ;
    }
}
