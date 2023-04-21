package org.fao.geonet.services.thumbnail;

import java.util.List;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.constants.Params;
import org.fao.geonet.kernel.DataManager;
import org.fao.geonet.services.metadata.Update;
import org.jdom.Element;

public class Get implements Service {

    private Update update = new Update();

    public void init(String appPath, ServiceConfig params) throws Exception {
        update.init(appPath, params);
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        if (saveEditData(params)) update.exec(params, context);
        GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
        DataManager dataMan = gc.getDataManager();
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        String id = Util.getParam(params, Params.ID);
        Element result = dataMan.getThumbnails(dbms, id);
        if (result == null) throw new IllegalArgumentException("Metadata not found --> " + id);
        result.addContent(new Element("version").setText(dataMan.getNewVersion(id)));
        return result;
    }

    private boolean saveEditData(Element params) {
        List list = params.getChildren();
        for (int i = 0; i < list.size(); i++) {
            Element el = (Element) list.get(i);
            if (el.getName().startsWith("_")) return true;
        }
        return false;
    }
}
