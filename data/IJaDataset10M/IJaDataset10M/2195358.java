package com.portalwizard.gen.wsdl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import java.io.Serializable;
import java.util.Iterator;

/**
 * Created by Allen, Aug 27, 2006 6:51:08 AM
 */
public class DocumentGenerator implements Serializable {

    protected Log log = LogFactory.getLog(this.getClass());

    protected DocumentParser parser;

    public DocumentGenerator(DocumentParser parser) {
        this.parser = parser;
    }

    public void generate() {
        if (parser.getOperations().size() > 0) {
            for (Iterator operItr = parser.getOperations().iterator(); operItr.hasNext(); ) {
                Element operation = (Element) operItr.next();
                generateOperation(operation);
            }
        } else {
            for (Iterator msgItr = parser.getMessages().values().iterator(); msgItr.hasNext(); ) {
                Element message = (Element) msgItr.next();
                generateServerRequest(message);
            }
        }
    }

    public void generateOperation(Element operation) {
        Element input = getInputMessage(operation);
        Element output = getOutputMessage(operation);
        if (CommandOptions.genServer()) {
            generateServerAdapter(operation);
            generateServerRequest(input);
            generateServerResponse(output);
        } else {
            generateClientAdapter(operation);
            generateClientRequest(input);
            generateClientResponse(output);
        }
    }

    protected Element getInputMessage(Element operation) {
        Element inputEle = operation.getChild("input", operation.getNamespace());
        String messageName = inputEle.getAttributeValue("message");
        return ((Element) parser.getMessages().get(messageName));
    }

    protected Element getOutputMessage(Element operation) {
        Element outputEle = operation.getChild("output", operation.getNamespace());
        String messageName = outputEle.getAttributeValue("message");
        return ((Element) parser.getMessages().get(messageName));
    }

    public void generateServerRequest(Element message) {
    }

    public void generateServerResponse(Element message) {
    }

    public void generateClientRequest(Element message) {
    }

    public void generateClientResponse(Element message) {
    }

    public void generateServerAdapter(Element operation) {
    }

    public void generateClientAdapter(Element operation) {
    }
}
