package org.fao.geonet.services.main;

import java.net.URL;
import java.util.List;
import jeeves.exceptions.MissingParameterEx;
import jeeves.exceptions.UserNotFoundEx;
import jeeves.interfaces.Service;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import jeeves.utils.XmlRequest;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.lib.Lib;
import org.jdom.Element;

public class Forward implements Service {

    public void init(String appPath, ServiceConfig config) throws Exception {
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        Element site = Util.getChild(params, "site");
        Element par = Util.getChild(params, "params");
        Element acc = site.getChild("account");
        String url = Util.getParam(site, "url");
        String type = Util.getParam(site, "type", "generic");
        String username = (acc == null) ? null : Util.getParam(acc, "username");
        String password = (acc == null) ? null : Util.getParam(acc, "password");
        List list = par.getChildren();
        if (list.size() == 0) throw new MissingParameterEx("<request>", par);
        params = (Element) list.get(0);
        XmlRequest req = new XmlRequest(new URL(url));
        if (username != null) authenticate(req, username, password, type);
        Lib.net.setupProxy(context, req);
        req.setRequest(params);
        return req.execute();
    }

    private void authenticate(XmlRequest req, String username, String password, String type) throws Exception {
        if (!type.equals("geonetwork")) req.setCredentials(username, password); else {
            String addr = req.getAddress();
            int pos = addr.lastIndexOf("/");
            req.setAddress(addr.substring(0, pos + 1) + Geonet.Service.XML_LOGIN);
            req.addParam("username", username);
            req.addParam("password", password);
            Element response = req.execute();
            if (!response.getName().equals("ok")) throw new UserNotFoundEx(username);
            req.setAddress(addr);
        }
    }
}
