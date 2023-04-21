package org.fao.geonet.services.thumbnail;

import java.io.File;
import jeeves.exceptions.OperationAbortedEx;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.constants.Params;
import org.fao.geonet.exceptions.ConcurrentUpdateEx;
import org.fao.geonet.kernel.DataManager;
import org.fao.geonet.lib.Lib;
import org.jdom.Element;

public class Unset implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        String id = Util.getParam(params, Params.ID);
        String type = Util.getParam(params, Params.TYPE);
        String version = Util.getParam(params, Params.VERSION);
        Lib.resource.checkEditPrivilege(context, id);
        GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
        DataManager dataMan = gc.getDataManager();
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        if (version != null && !dataMan.getVersion(id).equals(version)) throw new ConcurrentUpdateEx(id);
        Element result = dataMan.getThumbnails(dbms, id);
        if (result == null) throw new OperationAbortedEx("Metadata not found", id);
        result = result.getChild(type);
        if (result == null) throw new OperationAbortedEx("Metadata has no thumbnail", id);
        String file = Lib.resource.getDir(context, Params.Access.PUBLIC, id) + result.getText();
        dataMan.unsetThumbnail(dbms, id, type.equals("small"));
        if (!new File(file).delete()) context.error("Error while deleting thumbnail : " + file);
        Element response = new Element("a");
        response.addContent(new Element("id").setText(id));
        response.addContent(new Element("version").setText(dataMan.getNewVersion(id)));
        return response;
    }

    public void removeThumbnailFile(String id, String type, ServiceContext context) throws Exception {
        GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
        DataManager dataMan = gc.getDataManager();
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        Element result = dataMan.getThumbnails(dbms, id);
        if (result == null) throw new OperationAbortedEx("Metadata not found", id);
        if (type == null) {
            remove(result, "thumbnail", id, context);
            remove(result, "large_thumbnail", id, context);
        } else {
            remove(result, type, id, context);
        }
    }

    private void remove(Element result, String type, String id, ServiceContext context) throws Exception {
        result = result.getChild(type);
        if (result == null) throw new OperationAbortedEx("Metadata has no thumbnail", id);
        String file = Lib.resource.getDir(context, Params.Access.PUBLIC, id) + result.getText();
        if (!new File(file).delete()) context.error("Error while deleting thumbnail : " + file);
    }
}
