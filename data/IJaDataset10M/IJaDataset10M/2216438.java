package org.fcrepo.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.fcrepo.common.Constants;
import org.fcrepo.server.management.FedoraAPIM;
import org.fcrepo.utilities.Base64;
import org.fcrepo.utilities.NamespaceContextImpl;

/**
 * Utility class for translating an existing Fedora DO into one where DC, RELS-EXT and RELS-INT are
 * managed content rather than inline XML.
 *
 * @author Stephen Bayliss
 * @version $Id: ManagedContentTranslator.java 8656 2010-06-21 10:17:05Z stephenbayliss $
 */
public class ManagedContentTranslator {

    public static void main(String args[]) {
        if (args.length != 2) {
            System.out.println("Supply input and output file names as parameters");
        } else {
            try {
                FileInputStream fis = new FileInputStream(new File(args[0]));
                byte[] res = translate(fis, "changeme:managed");
                FileOutputStream fos = new FileOutputStream(new File(args[1]));
                fos.write(res);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a managed content DO from an existing DO, ingesting the new object.
     * Datastreams DC, RELS-EXT and RELS-INT are managed content in the new object
     *
     * @param apim
     * @param pidToClone - PID of original object
     * @param newPid - PID for new managed content object
     * @throws Exception
     */
    public static void createManagedClone(FedoraAPIM apim, String pidToClone, String newPid) throws Exception {
        ByteArrayInputStream existingFoxml = new ByteArrayInputStream(apim.export(pidToClone, Constants.FOXML1_1.uri, "archive"));
        byte[] newFoxml = translate(existingFoxml, newPid);
        apim.ingest(newFoxml, Constants.FOXML1_1.uri, "Creating managed content version of " + pidToClone);
    }

    protected static byte[] translate(InputStream input, String newPID) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document doc = builder.parse(input);
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        NamespaceContextImpl nsCtx = new NamespaceContextImpl();
        nsCtx.addNamespace("foxml", Constants.FOXML.uri);
        nsCtx.addNamespace("oai_dc", Constants.OAI_DC.uri);
        nsCtx.addNamespace("dc", Constants.DC.uri);
        nsCtx.addNamespace("rdf", Constants.RDF.uri);
        xpath.setNamespaceContext(nsCtx);
        TransformerFactory transfac = TransformerFactory.newInstance();
        Object objectNode = xpath.evaluate("/foxml:digitalObject/@PID", doc, XPathConstants.NODE);
        Node node = (Node) objectNode;
        String oldPID = node.getTextContent();
        node.setTextContent(newPID);
        Object objectNodes = xpath.evaluate("/foxml:digitalObject/foxml:datastream[@ID='DC']/foxml:datastreamVersion/foxml:xmlContent/oai_dc:dc/dc:identifier[.='" + oldPID + "']", doc, XPathConstants.NODESET);
        NodeList versions = (NodeList) objectNodes;
        if (versions.getLength() == 1) {
            versions.item(0).setTextContent(newPID);
        }
        objectNodes = xpath.evaluate("/foxml:digitalObject/foxml:datastream[@ID='RELS-EXT']/foxml:datastreamVersion/foxml:xmlContent/rdf:RDF/rdf:Description/@rdf:about", doc, XPathConstants.NODESET);
        versions = (NodeList) objectNodes;
        if (versions.getLength() == 1) {
            versions.item(0).setTextContent("info:fedora/" + newPID);
        }
        objectNodes = xpath.evaluate("/foxml:digitalObject/foxml:datastream[@ID='RELS-INT']/foxml:datastreamVersion/foxml:xmlContent/rdf:RDF/rdf:Description/@rdf:about", doc, XPathConstants.NODESET);
        versions = (NodeList) objectNodes;
        for (int i = 0; i < versions.getLength(); i++) {
            String existing = versions.item(i).getTextContent();
            versions.item(i).setTextContent(existing.replace("info:fedora/" + oldPID, "info:fedora/" + newPID));
        }
        objectNodes = xpath.evaluate("/foxml:digitalObject/foxml:datastream[@ID='DC' or @ID='RELS-EXT' or @ID='RELS-INT']/foxml:datastreamVersion", doc, XPathConstants.NODESET);
        versions = (NodeList) objectNodes;
        for (int i = 0; i < versions.getLength(); i++) {
            NodeList xmlContents = (NodeList) xpath.evaluate("foxml:xmlContent", versions.item(i), XPathConstants.NODESET);
            if (xmlContents.getLength() != 1) {
                throw new Exception("datastream version did not contain excactly one foxml:xmlContent node");
            }
            for (int j = 0; j < xmlContents.item(0).getChildNodes().getLength(); j++) {
                Node xml = xmlContents.item(0).getChildNodes().item(j);
                if (xml.getNodeType() == Node.ELEMENT_NODE) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    Transformer trans = transfac.newTransformer();
                    trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                    trans.setOutputProperty(OutputKeys.INDENT, "yes");
                    trans.transform(new DOMSource(xml), new StreamResult(bytes));
                    Node binaryContent = doc.createElementNS(Constants.FOXML.uri, "foxml:binaryContent");
                    binaryContent.setTextContent(Base64.encodeToString(bytes.toByteArray()));
                    versions.item(i).replaceChild(binaryContent, xmlContents.item(0));
                    Node CGAttr = versions.item(i).getParentNode().getAttributes().getNamedItem("CONTROL_GROUP");
                    CGAttr.setTextContent("M");
                    break;
                }
            }
        }
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(baos);
        trans.transform(new DOMSource(doc), result);
        return baos.toByteArray();
    }
}
