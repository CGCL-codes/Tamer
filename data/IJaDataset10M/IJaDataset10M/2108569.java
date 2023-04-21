package org.fao.geonet.services.metadata;

import jeeves.interfaces.Service;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import jeeves.utils.Xml;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.constants.Params;
import org.fao.geonet.kernel.setting.SettingManager;
import org.fao.geonet.util.MailSender;
import org.jdom.Element;

/** Stores the simple metadata from a form into an iso19139 record and sends 
  * an e-mail
  */
public class InsertSimpleMetadata implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    public Element exec(Element params, final ServiceContext context) throws Exception {
        GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
        SettingManager sm = gc.getSettingManager();
        String name = Util.getParam(params, "Depositor_individualName");
        String email = Util.getParam(params, "Depositor_Address_electronicMailAddress");
        String org = Util.getParam(params, "Depositor_organisationName");
        String subject = "New Metadata Info from " + name + " (" + email + ")";
        String host = sm.getValue("system/feedback/mailServer/host");
        String port = sm.getValue("system/feedback/mailServer/port");
        String to = sm.getValue("system/feedback/email");
        MailSender sender = new MailSender(context);
        sender.send(host, Integer.parseInt(port), email, name + " (" + org + ")", to, null, subject, Xml.getString(params));
        return new Element("response").addContent(params.cloneContent());
    }
}
