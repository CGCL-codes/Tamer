package org.fao.geonet.services.metadata;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import jeeves.constants.Jeeves;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.UserSession;
import jeeves.server.context.ServiceContext;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.AccessManager;
import org.fao.geonet.kernel.DataManager;
import org.fao.geonet.kernel.MdInfo;
import org.fao.geonet.kernel.SelectionManager;
import org.jdom.Element;

/** Stores all operations allowed for a metadata
  */
public class MassiveUpdatePrivileges implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
        DataManager dm = gc.getDataManager();
        AccessManager accessMan = gc.getAccessManager();
        UserSession us = context.getUserSession();
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        context.info("Get selected metadata");
        SelectionManager sm = SelectionManager.getManager(us);
        Set<Integer> metadata = new HashSet<Integer>();
        Set<Integer> notFound = new HashSet<Integer>();
        Set<Integer> notOwner = new HashSet<Integer>();
        for (Iterator<String> iter = sm.getSelection("metadata").iterator(); iter.hasNext(); ) {
            String uuid = (String) iter.next();
            String id = dm.getMetadataId(dbms, uuid);
            MdInfo info = dm.getMetadataInfo(dbms, id);
            if (info == null) {
                notFound.add(new Integer(id));
            } else if (!accessMan.isOwner(context, id)) {
                notOwner.add(new Integer(id));
            } else {
                boolean skip = false;
                boolean isAdmin = Geonet.Profile.ADMINISTRATOR.equals(us.getProfile());
                boolean isReviewer = Geonet.Profile.REVIEWER.equals(us.getProfile());
                if (us.getUserId().equals(info.owner) && !isAdmin && !isReviewer) skip = true;
                dm.deleteMetadataOper(dbms, id, skip);
                List list = params.getChildren();
                for (int i = 0; i < list.size(); i++) {
                    Element el = (Element) list.get(i);
                    String name = el.getName();
                    if (name.startsWith("_")) {
                        StringTokenizer st = new StringTokenizer(name, "_");
                        String groupId = st.nextToken();
                        String operId = st.nextToken();
                        dm.setOperation(dbms, id, groupId, operId);
                    }
                }
                metadata.add(new Integer(id));
            }
        }
        for (int mdId : metadata) {
            dm.indexMetadata(dbms, Integer.toString(mdId));
        }
        return new Element(Jeeves.Elem.RESPONSE).addContent(new Element("done").setText(metadata.size() + "")).addContent(new Element("notOwner").setText(notOwner.size() + "")).addContent(new Element("notFound").setText(notFound.size() + ""));
    }
}
