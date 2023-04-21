package org.fao.geonet.guiservices.metadata;

import java.util.Iterator;
import java.util.Set;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.AccessManager;
import org.jdom.Element;

/** Service used to return all categories in the system
  */
public class GetLatestUpdated implements Service {

    private int _maxItems;

    private long _timeBetweenUpdates;

    private Element _response;

    private long _lastUpdateTime;

    public void init(String appPath, ServiceConfig config) throws Exception {
        String sMaxItems = config.getValue("maxItems", "10");
        String sTimeBetweenUpdates = config.getValue("timeBetweenUpdates", "60");
        _maxItems = Integer.parseInt(sMaxItems);
        _timeBetweenUpdates = Integer.parseInt(sTimeBetweenUpdates) * 1000;
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        if (System.currentTimeMillis() > _lastUpdateTime + _timeBetweenUpdates) {
            GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
            AccessManager am = gc.getAccessManager();
            Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
            Set<String> groups = am.getUserGroups(dbms, context.getUserSession(), context.getIpAddress());
            String query = "SELECT DISTINCT id, changeDate FROM Metadata, OperationAllowed " + "WHERE id=metadataId AND isTemplate='n' AND operationId=0 AND (";
            String aux = "";
            for (String grpId : groups) aux += " OR groupId=" + grpId;
            query += aux.substring(4);
            query += ") ORDER BY changeDate DESC";
            Element result = dbms.select(query);
            _response = new Element("response");
            int numItems = 0;
            for (Iterator iter = result.getChildren().iterator(); iter.hasNext() && numItems++ < _maxItems; ) {
                Element rec = (Element) iter.next();
                String id = rec.getChildText("id");
                Element md = gc.getDataManager().getMetadata(context, id, false, true);
                _response.addContent(md);
            }
            _lastUpdateTime = System.currentTimeMillis();
        }
        return (Element) _response.clone();
    }
}
