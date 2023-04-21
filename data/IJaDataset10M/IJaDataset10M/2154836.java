package spidr.webapp;

import java.util.*;
import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;
import vobs.datamodel.*;
import vobs.dbaccess.*;
import wdc.settings.Settings;
import java.lang.reflect.*;
import java.net.*;
import java.io.*;
import org.jdom.input.*;
import org.jdom.*;

public final class OuterSearchProxyAction extends Action {

    private Logger log = Logger.getLogger(OuterSearchProxyAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        String spidrMetadataServlet = Settings.get("locations.metadataServlet");
        try {
            URL url = new URL(spidrMetadataServlet + (spidrMetadataServlet.endsWith("/") ? "" : "/") + "outersearch?" + request.getQueryString());
            SAXBuilder builder = new SAXBuilder();
            Document metaDoc = builder.build(url);
            request.setAttribute("metaElm", metaDoc.getRootElement());
        } catch (Exception e) {
            Element errorElm = new Element("result");
            errorElm.setAttribute("error", e.toString());
            request.setAttribute("metaElm", errorElm);
        }
        return (mapping.findForward("success"));
    }
}
