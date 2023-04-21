package org.fao.geonet.services.relations;

import jeeves.constants.Jeeves;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.constants.Params;
import org.jdom.Element;

/** Insert the relation between two metadata records
 * 
  */
public class Insert implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        int parentId = Util.getParamAsInt(params, Params.PARENT_ID);
        int childId = Util.getParamAsInt(params, Params.CHILD_ID);
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        String query = "INSERT INTO Relations (id, relatedId) " + "VALUES (?, ?)";
        dbms.execute(query, parentId, childId);
        Element response = new Element(Jeeves.Elem.RESPONSE).addContent(new Element("parentId").setText(String.valueOf(parentId))).addContent(new Element("childId").setText(String.valueOf(childId)));
        return response;
    }
}
