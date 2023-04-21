package org.fao.geonet.services.metadata;

import jeeves.constants.Jeeves;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.constants.Params;
import org.fao.geonet.kernel.DataManager;
import org.jdom.Element;

/** Creates a metadata copying data from a given template
  */
public class Create implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
        String template = Util.getParam(params, Params.ID);
        String groupOwner = Util.getParam(params, Params.GROUP);
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        DataManager dm = gc.getDataManager();
        String id = dm.createMetadata(dbms, template, groupOwner, context.getSerialFactory(), gc.getSiteId(), context.getUserSession().getUserIdAsInt());
        return new Element(Jeeves.Elem.RESPONSE).addContent(new Element(Geonet.Elem.ID).setText(id));
    }
}
