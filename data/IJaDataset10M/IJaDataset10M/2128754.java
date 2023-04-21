package org.wfp.vam.intermap.services.banner;

import org.jdom.*;
import jeeves.interfaces.*;
import jeeves.server.*;
import jeeves.server.context.*;
import jeeves.constants.*;

/** This service returns all information needed to build the banner with XSL
  */
public class Get implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        Element res = new Element(Jeeves.Elem.RESPONSE).addContent(getStack(context)).addContent(getUserInfo(context));
        if (getInvertValue(context.getLanguage())) res.addContent(new Element("invert"));
        return res;
    }

    private Element getStack(ServiceContext srvContext) {
        Element stackElem = new Element("stack");
        String service = srvContext.getService();
        stackElem.addContent(new Element("current").addContent(service));
        stackElem.addContent(new Element("language").addContent(srvContext.getLanguage()));
        return stackElem;
    }

    private Element getUserInfo(ServiceContext srvContext) {
        UserSession session = srvContext.getUserSession();
        return new Element("user").addContent(new Element("username").setText(session.getUsername())).addContent(new Element("name").setText(session.getName())).addContent(new Element("surname").setText(session.getSurname()));
    }

    private boolean getInvertValue(String lang) {
        return lang.equals("ar");
    }
}
