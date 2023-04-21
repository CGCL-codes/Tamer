package org.fao.geonet.services.metadata;

import java.util.List;
import jeeves.constants.Jeeves;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.AccessManager;
import org.fao.geonet.kernel.DataManager;
import org.fao.geonet.lib.Lib;
import org.jdom.Element;

/** Returns all categories.
  */
public class PrepareMassiveUpdateCategories implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
        DataManager dataMan = gc.getDataManager();
        AccessManager am = gc.getAccessManager();
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        Element isOwner = new Element("owner").setText("true");
        Element elCateg = Lib.local.retrieve(dbms, "Categories");
        List list = elCateg.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Element el = (Element) list.get(i);
            el.setName(Geonet.Elem.CATEGORY);
        }
        Element elRes = new Element(Jeeves.Elem.RESPONSE).addContent(elCateg).addContent(isOwner);
        return elRes;
    }
}
