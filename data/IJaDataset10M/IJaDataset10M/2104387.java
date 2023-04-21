package org.opennms.jpsadmin.service;

import org.springframework.ws.server.endpoint.AbstractDomPayloadEndpoint;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: CE136452
 * Date: Aug 19, 2008
 * Time: 6:58:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class JpsPsHomeServiceEndpoint extends AbstractDomPayloadEndpoint {

    private Logger logger = Logger.getLogger(JpsPsHomeServiceEndpoint.class);

    IJpsPsHomeService PsHomeService;

    public JpsPsHomeServiceEndpoint(IJpsPsHomeService psHomeService) {
        this.PsHomeService = psHomeService;
    }

    protected Element invokeInternal(Element element, Document document) throws Exception {
        String requestText = element.getTextContent();
        logger.debug("Request text: " + element);
        Element responseElement = document.createElementNS("http://xmlns.opennms.org/xsd/jpsadmin/messages", "getAllPsHomesResponse");
        responseElement.setTextContent("foo");
        return responseElement;
    }
}
