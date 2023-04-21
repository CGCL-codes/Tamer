package org.fao.geonet.services.logo;

import jeeves.constants.Jeeves;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.setting.SettingManager;
import org.fao.geonet.lib.Lib;
import org.jdom.Element;

/** Retrieves all alternate logos in the system
  */
public class List implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        Element result = new Element(Jeeves.Elem.RESPONSE);
        Element logos = Lib.logos.list(context);
        result.addContent(logos);
        return result;
    }
}
