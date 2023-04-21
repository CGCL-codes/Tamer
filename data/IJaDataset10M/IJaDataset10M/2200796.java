package org.fao.geonet.guiservices.util;

import jeeves.interfaces.Service;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import org.fao.geonet.constants.Geonet;
import org.jdom.Element;

/** This service returns application path
 * mainly used for XSL call to static java
 * function which could need such information.
 * 
 * TODO : Could we make this better ? from static context
 * get app path ?
  */
public class GetAppPath implements Service {

    private String appPath = null;

    public void init(String appPath, ServiceConfig params) throws Exception {
        this.appPath = appPath;
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        Element root = new Element("root");
        root.addContent(new Element(Geonet.Elem.APP_PATH).setText(appPath));
        return root;
    }
}
