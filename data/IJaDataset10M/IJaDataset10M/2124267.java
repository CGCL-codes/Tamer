package org.wfp.vam.intermap.services.map;

import org.jdom.*;
import jeeves.interfaces.*;
import jeeves.server.*;
import jeeves.server.context.*;
import org.wfp.vam.intermap.kernel.map.*;
import org.wfp.vam.intermap.Constants;

public class Get implements Service {

    public void init(String appPath, ServiceConfig config) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        MapMerger mm = MapUtil.getMapMerger(context);
        String url = MapUtil.getTempUrl() + "/" + mm.getImageName();
        String tool = MapUtil.getTool(context);
        return new Element("response").addContent(mm.getBoundingBox().toElement()).addContent(new Element(Constants.URL).setText(url)).addContent(new Element("tool").setText(tool)).addContent(mm.toElement()).addContent(new Element("imageWidth").setText(MapUtil.getImageWidth(context) + "")).addContent(new Element("imageHeight").setText(MapUtil.getImageHeight(context) + "")).addContent(new Element("imageSize").setText((String) context.getUserSession().getProperty(Constants.SESSION_SIZE)));
    }
}
