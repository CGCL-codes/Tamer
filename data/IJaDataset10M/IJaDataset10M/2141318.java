package com.nrg.richie.server;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.nrg.richie.client.XmlDataControllerService;
import com.nrg.richie.server.utils.StringUtils;
import com.nrg.richie.server.utils.XMLUtils;
import com.nrg.richie.shared.NodeData;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class XmlDataControllerServiceImpl extends RemoteServiceServlet implements XmlDataControllerService {

    Logger logger = Logger.getLogger(XmlDataControllerServiceImpl.class.getName());

    private static final String[] unneededTags = new String[] { "agent-name", "agent-address", "agent-phone", "agent-fax", "agent-email", "price" };

    @SuppressWarnings("unchecked")
    @Override
    public List<NodeData> getNodeDataList() {
        ArrayList<NodeData> nodeDataList = new ArrayList<NodeData>();
        try {
            nodeDataList = (ArrayList<NodeData>) getThreadLocalRequest().getSession().getAttribute(UploadServlet.NODE_DATA_LIST);
        } catch (Exception e) {
            logger.severe(StringUtils.getStacktraceAsString(e));
        }
        return nodeDataList == null ? new ArrayList<NodeData>() : nodeDataList;
    }

    @Override
    public void setExportNodeDataList(List<NodeData> nodeDataList) {
        try {
            List<String> idsToKeep = new ArrayList<String>();
            for (NodeData nodeData : nodeDataList) {
                idsToKeep.add(nodeData.getId());
            }
            String xml = (String) getThreadLocalRequest().getSession().getAttribute(UploadServlet.XML_DOCUMENT);
            Document document = XMLUtils.getXml(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            NodeList nodes = document.getElementsByTagName("property");
            for (int i = nodes.getLength() - 1; nodes.getLength() > idsToKeep.size(); i--) {
                Element element = (Element) nodes.item(i);
                String id = element.getAttribute("source-system-id");
                if (!idsToKeep.contains(id)) {
                    logger.fine("" + i + ". of " + nodes.getLength() + " remove: " + id);
                    element.getParentNode().removeChild(element);
                    i = nodes.getLength();
                } else {
                    logger.fine("" + i + ". of " + nodes.getLength() + " don't remove: " + id);
                }
            }
            for (String tag : unneededTags) {
                NodeList nodesToEmpty = document.getElementsByTagName(tag);
                for (int i = 0; i < nodesToEmpty.getLength(); i++) {
                    Element element = (Element) nodesToEmpty.item(i);
                    element.removeChild(element.getChildNodes().item(0));
                }
            }
            getThreadLocalRequest().getSession().setAttribute(UploadServlet.XML_DOCUMENT_FILTERED, XMLUtils.getXmlString(document));
        } catch (Exception e) {
            logger.severe(StringUtils.getStacktraceAsString(e));
        }
    }

    @Override
    public void resetSession() {
        HttpSession session = getThreadLocalRequest().getSession();
        session.removeAttribute(UploadServlet.NODE_DATA_LIST);
        session.removeAttribute(UploadServlet.XML_DOCUMENT);
        session.removeAttribute(UploadServlet.XML_DOCUMENT_FILTERED);
    }
}
