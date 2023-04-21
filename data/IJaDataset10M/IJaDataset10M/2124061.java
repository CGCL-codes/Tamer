package com.oldportal.objectsbuilder.document.cpp;

import com.oldportal.objectsbuilder.document.*;

public class Member extends com.oldportal.objectsbuilder.document.Object implements com.oldportal.objectsbuilder.document.XMLSerializable {

    public Member() {
    }

    public TypeValue type = new TypeValue();

    public int accessPrivilege = ACCESS_PUBLIC;

    public boolean memberIsArray = false;

    public String memberArraySize = "";

    public boolean memberMutable = false;

    public boolean memberStatic = false;

    public String initialValue = new String();

    public void load(org.jdom.Element element) {
        if (element.getName() != "com.oldportal.objectsbuilder.document.cpp.Member") return;
        org.jdom.Element superElement = element.getChild("super");
        super.load((org.jdom.Element) superElement.getChildren().get(0));
    }

    public org.jdom.Element save() {
        org.jdom.Element ret = new org.jdom.Element("com.oldportal.objectsbuilder.document.cpp.Member");
        org.jdom.Element superElement = new org.jdom.Element("super");
        superElement.addContent(super.save());
        ret.addContent(superElement);
        return ret;
    }

    public void resolveDependencies() {
        ;
    }
}
