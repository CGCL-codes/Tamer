package org.coosproject.modules.colab.colabserv.restlet.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.coos.messaging.util.Log;
import org.coos.messaging.util.LogFactory;
import org.restlet.data.MediaType;
import org.restlet.resource.DomRepresentation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ch.qos.logback.classic.Logger;

public class ResourceDescription {

    Document doc;

    private static final Log LOGGER = LogFactory.getLog(ResourceDescription.class);

    ArrayList<String> tags = new ArrayList<String>();

    public ResourceDescription(String name, String rai_description, String resource_id, String RAIUrl, ArrayList<String> tags, String expiration_time) {
        DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
        DocumentBuilder build;
        try {
            build = dFact.newDocumentBuilder();
            doc = build.newDocument();
            Element root = doc.createElement("Resources");
            doc.appendChild(root);
            Element resource = doc.createElement("Resource-Description");
            root.appendChild(resource);
            Element e_name = doc.createElement("Name");
            e_name.setTextContent(name);
            resource.appendChild(e_name);
            Element ident = doc.createElement("Resource-ID");
            ident.setTextContent(resource_id);
            resource.appendChild(ident);
            Element RAI = doc.createElement("RAI-Description");
            resource.appendChild(RAI);
            Element rai_ID = doc.createElement("RAI-ID");
            rai_ID.setTextContent(rai_description);
            RAI.appendChild(rai_ID);
            if (rai_description != null) {
                Element desc = doc.createElement("Description");
                desc.setTextContent(rai_description);
                RAI.appendChild(desc);
            }
            Element rep_locator = doc.createElement("REP-Locator");
            rep_locator.setTextContent(RAIUrl);
            rep_locator.setAttribute("Expiration-Time", expiration_time);
            RAI.appendChild(rep_locator);
            if (tags != null) {
                for (Iterator iterator = tags.iterator(); iterator.hasNext(); ) {
                    String tag = (String) iterator.next();
                    Element tag_elem = doc.createElement("Tag");
                    tag_elem.setTextContent(tag);
                    resource.appendChild(tag_elem);
                }
            }
            if (expiration_time != null) {
                Element exp = doc.createElement("Expiration-Time");
                exp.setTextContent(expiration_time);
                resource.appendChild(exp);
            }
            doc.normalizeDocument();
            printIt(doc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void printIt(Document doc2) {
        Source source = new DOMSource(doc2);
        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        Transformer xformer;
        try {
            xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, result);
            LOGGER.debug("\nResource description: \n" + writer.toString());
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void addTag(String tag) {
        tags.add(tag);
        NodeList list = doc.getElementsByTagName("Resource-Description");
        Node n = list.item(list.getLength() - 1);
        Element e = doc.createElement("Tag");
        e.setTextContent(tag);
        n.appendChild(e);
    }

    public DomRepresentation getDOMRepresentation() {
        DomRepresentation domrep = new DomRepresentation(MediaType.TEXT_XML, doc);
        return domrep;
    }

    public void addRAIDescription() {
        Element rai_desc = doc.createElement("Rai-Description");
        Element url = doc.createElement("URL");
        url.setNodeValue("http://joda.no");
        rai_desc.appendChild(url);
        Element doc_link = doc.createElement("Document-Link");
        doc.setNodeValue("http://testing.no");
        rai_desc.appendChild(doc_link);
    }

    /**
	 * Simple main for testing during development; comment out when code is mature
	 */
    public static void main(String[] args) {
        ResourceDescription rd = new ResourceDescription("A name", "My RAI description", "#id123", "http://localhost:8182/s/Temp", null, null);
        try {
            System.out.println("rd=" + rd.getDOMRepresentation().getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
