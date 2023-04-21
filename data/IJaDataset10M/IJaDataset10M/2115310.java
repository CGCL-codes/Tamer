package org.wfp.vam.intermap.services.map;

import org.jdom.*;
import jeeves.interfaces.*;
import jeeves.server.*;
import jeeves.server.context.*;
import org.wfp.vam.intermap.kernel.map.*;
import org.wfp.vam.intermap.Constants;
import java.util.List;
import java.util.Iterator;

/** main.result service. shows search results
  */
public class AddServices implements Service {

    public void init(String appPath, ServiceConfig config) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        String serverUrl = params.getChildText(Constants.MAP_SERVER_URL);
        int serverType = Integer.parseInt(params.getChildText(Constants.MAP_SERVER_TYPE));
        List lServices = params.getChildren(Constants.MAP_SERVICE);
        Element response = new Element("response");
        if (lServices.size() == 0) {
            response.addContent(new Element("status").setAttribute("services", "false"));
            response.addContent(new Element("mapserver").setText("-" + serverType + ""));
            response.addContent(new Element("url").setText(serverUrl));
            return response;
        } else response.addContent(new Element("status").setAttribute("services", "true"));
        String vsp = params.getChildText("vendor_spec_par");
        String bbox = params.getChildText("BBOX");
        if (lServices.size() > 0) {
            MapMerger mm = MapUtil.getMapMerger(context);
            boolean flag = false;
            if (mm.size() == 0) flag = true;
            for (Iterator i = lServices.iterator(); i.hasNext(); ) {
                String serviceName = ((Element) i.next()).getText();
                MapUtil.addService(serverType, serverUrl, serviceName, vsp, mm);
            }
            if (bbox != null) {
                MapUtil.setBBoxFromUrl(bbox, mm);
            } else if (flag) {
                MapUtil.setBoundingBox(mm);
            }
            context.getUserSession().setProperty(Constants.SESSION_MAP, mm);
        }
        String size = (String) context.getUserSession().getProperty(Constants.SESSION_SIZE);
        if (size == null) {
            context.getUserSession().setProperty(Constants.SESSION_SIZE, MapUtil.getDefaultImageSize());
        }
        return null;
    }
}
