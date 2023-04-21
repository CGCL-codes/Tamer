package org.wfp.vam.intermap.services.map;

import java.util.*;
import org.jdom.*;
import jeeves.interfaces.*;
import jeeves.server.*;
import jeeves.server.context.*;
import org.wfp.vam.intermap.kernel.map.*;
import org.wfp.vam.intermap.Constants;

/** main.result service. shows search results
  */
public class SetContext implements Service {

    public void init(String appPath, ServiceConfig config) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        int id = Integer.parseInt(params.getChildText(Constants.CONTEXT_ID));
        MapMerger mm = new MapMerger();
        Element mapContext = DefaultMapServers.getContext(id);
        List lServers = mapContext.getChildren("server");
        for (Iterator i = lServers.iterator(); i.hasNext(); ) {
            Element elServer = (Element) i.next();
            String serverType = elServer.getAttributeValue(Constants.MAP_SERVER_TYPE);
            String serverUrl = elServer.getAttributeValue(Constants.MAP_SERVER_URL);
            List elLayers = elServer.getChildren(Constants.MAP_LAYER);
            for (Iterator j = elLayers.iterator(); j.hasNext(); ) {
                try {
                    Element elLayer = (Element) j.next();
                    String serviceName = elLayer.getAttributeValue("name");
                    MapUtil.addService(Integer.parseInt(serverType), serverUrl, serviceName, "", mm);
                } catch (Exception e) {
                }
            }
        }
        MapUtil.setBoundingBox(mm);
        String size = (String) context.getUserSession().getProperty(Constants.SESSION_SIZE);
        if (size == null) context.getUserSession().setProperty(Constants.SESSION_SIZE, MapUtil.getDefaultImageSize());
        context.getUserSession().setProperty(Constants.SESSION_MAP, mm);
        return null;
    }
}
