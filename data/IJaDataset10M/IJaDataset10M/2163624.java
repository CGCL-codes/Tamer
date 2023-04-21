package org.fao.geonet.services.logo;

import jeeves.constants.Jeeves;
import jeeves.interfaces.Service;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import org.fao.geonet.constants.Params;
import org.fao.geonet.lib.Lib;
import org.jdom.Element;

/** Update the information of a logo (currently name only)
  */
public class Update implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        String id = params.getChildText(Params.ID);
        String name = Util.getParam(params, Params.NAME);
        String logoFile = Util.getParam(params, "logoFile", null);
        Element elRes = new Element(Jeeves.Elem.RESPONSE);
        if (id == null) {
            Lib.logos.add(context, name, logoFile);
            elRes.addContent(new Element(Jeeves.Elem.OPERATION).setText(Jeeves.Text.ADDED));
        } else {
            Lib.logos.update(context, id, name, logoFile);
            elRes.addContent(new Element(Jeeves.Elem.OPERATION).setText(Jeeves.Text.UPDATED));
        }
        return elRes;
    }
}
