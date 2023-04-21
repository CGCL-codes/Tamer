package org.compas.client;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ResultDrawer {

    public static JPanel drawServiceResults(Element serviceElement) {
        if ("graphics".equals(serviceElement.getElementsByTagName("output").item(0).getTextContent())) {
            return drawResults(getServiceSVG(serviceElement));
        } else {
            JEditorPane text = new JEditorPane("text/html", printTextServiceResults(serviceElement));
            text.setEditable(false);
            JPanel complete = new JPanel();
            complete.add(text);
            return complete;
        }
    }

    public static JPanel drawSequenceResults(Element sequenceElement) {
        JPanel svgpanel = drawResults(getSequenceSVG(sequenceElement));
        JEditorPane text = new JEditorPane("text/html", printSequenceResults(sequenceElement));
        text.setEditable(false);
        JScrollPane textscroll = new JScrollPane(text);
        textscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane svgscroll = new JScrollPane(svgpanel);
        JPanel complete = new JPanel();
        complete.setLayout(new BoxLayout(complete, BoxLayout.PAGE_AXIS));
        complete.add(svgscroll);
        complete.add(textscroll);
        return complete;
    }

    private static String printTextServiceResults(Element serviceElement) {
        String name = serviceElement.getElementsByTagName("name").item(0).getTextContent();
        String ret = "<html><head><title>Results for " + name + "</title></head><body>\n<h1>Results from webservice <em>" + name + "</em></h1>\n";
        Element root = serviceElement.getOwnerDocument().getDocumentElement();
        NodeList sequenceList = root.getElementsByTagName("sequenceElement");
        for (int i = 0; i < sequenceList.getLength(); i++) {
            Element seqEl = (Element) sequenceList.item(i);
            String head = seqEl.getElementsByTagName("header").item(0).getTextContent();
            NodeList serviceResultList = seqEl.getElementsByTagName("serviceResult");
            ret += "<h3>Sequence: " + head + "</h3>\n";
            for (int j = 0; j < serviceResultList.getLength(); j++) {
                if (((Element) serviceResultList.item(j)).getElementsByTagName("service").item(0).getTextContent().equals(name)) {
                    ret += "<pre>" + ((Element) serviceResultList.item(j)).getElementsByTagName("data").item(0).getTextContent() + "</pre><hr>\n";
                    break;
                }
            }
        }
        ret += "</body></html>";
        return ret;
    }

    private static String printSequenceResults(Element sequenceElement) {
        NodeList serviceResultList = sequenceElement.getElementsByTagName("serviceResult");
        String ret = "<html><head><title>Results</title></head><body>\n";
        for (int i = 0; i < serviceResultList.getLength(); i++) {
            Element serviceResultElement = (Element) serviceResultList.item(i);
            if ("text".equals(serviceResultElement.getElementsByTagName("display").item(0).getTextContent())) {
                String resultString = serviceResultElement.getElementsByTagName("data").item(0).getTextContent();
                String serviceName = serviceResultElement.getElementsByTagName("service").item(0).getTextContent();
                ret += "<h3>Results for Service " + serviceName + "</h3><pre>" + resultString + "</pre><hr>\n";
            }
        }
        ret += "</body></html>";
        return ret;
    }

    public static String exportSequenceResults(Element sequenceElement) {
        NodeList serviceResultList = sequenceElement.getElementsByTagName("serviceResult");
        String ret = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"><head><title>CoMPAS Pro Sequence Results</title></head><body>\n";
        for (int i = 0; i < serviceResultList.getLength(); i++) {
            Element serviceResultElement = (Element) serviceResultList.item(i);
            if ("text".equals(serviceResultElement.getElementsByTagName("display").item(0).getTextContent())) {
                String resultString = serviceResultElement.getElementsByTagName("data").item(0).getTextContent();
                String serviceName = serviceResultElement.getElementsByTagName("service").item(0).getTextContent();
                ret += "<h3>Results for Service " + serviceName + "</h3><pre>" + resultString + "</pre><hr />\n";
            }
        }
        NodeList svgList = sequenceElement.getElementsByTagName("svg");
        Vector<Document> allSvgs = new Vector<Document>();
        for (int i = 0; i < svgList.getLength(); i++) {
            allSvgs.add(parseSVG((Element) svgList.item(i)));
        }
        Document docSVG = mergeSVGs(allSvgs);
        String svgString = svgToString(docSVG.getDocumentElement());
        svgString = svgString.substring(svgString.indexOf("\n"));
        ret += "<h3>Graphical Results</h3>\n" + svgString + "\n<hr />\n";
        ret += "</body></html>";
        return ret;
    }

    private static String svgToString(Element svg) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{ http://xml.apache.org/xslt }indent-amount", "2");
            DOMSource source = new DOMSource(svg);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(os);
            transformer.transform(source, result);
            return os.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "exception";
    }

    private static JPanel drawResults(Document svgDoc) {
        JPanel svgPanel = new JPanel();
        svgPanel.setLayout(new BoxLayout(svgPanel, BoxLayout.PAGE_AXIS));
        JSVGCanvas canvas = new JSVGCanvas();
        canvas.setDocument(svgDoc);
        svgPanel.add(canvas);
        svgPanel.add(new JPanel());
        svgPanel.setMaximumSize(new Dimension(1000, 700));
        svgPanel.repaint();
        return svgPanel;
    }

    private static Document getSequenceSVG(Element sequenceElement) {
        NodeList svgList = sequenceElement.getElementsByTagName("svg");
        Vector<Document> allSvgs = new Vector<Document>();
        for (int i = 0; i < svgList.getLength(); i++) {
            allSvgs.add(parseSVG((Element) svgList.item(i)));
        }
        return mergeSVGs(allSvgs);
    }

    private static Document parseSVG(Element svg) {
        Document svgDoc = null;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{ http://xml.apache.org/xslt }indent-amount", "2");
            DOMSource source = new DOMSource(svg);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(os);
            transformer.transform(source, result);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            svgDoc = f.createDocument("http://a.b", is);
            System.err.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return svgDoc;
    }

    private static Document getServiceSVG(Element serviceElement) {
        Element root = serviceElement.getOwnerDocument().getDocumentElement();
        String whichService = serviceElement.getElementsByTagName("name").item(0).getTextContent();
        Element sequences = (Element) root.getElementsByTagName("sequences").item(0);
        NodeList svgList = sequences.getElementsByTagName("service");
        Vector<Document> allSvgs = new Vector<Document>();
        for (int i = 0; i < svgList.getLength(); i++) {
            Element currentItem = (Element) svgList.item(i);
            if (currentItem.getTextContent().equals(whichService)) {
                Element scale = (Element) ((Element) currentItem.getParentNode().getParentNode()).getElementsByTagName("scale").item(0);
                scale = (Element) scale.getElementsByTagName("svg").item(0);
                allSvgs.add(parseSVG(scale));
                allSvgs.add(parseSVG((Element) ((Element) currentItem.getParentNode()).getElementsByTagName("svg").item(0)));
            }
        }
        return mergeSVGs(allSvgs);
    }

    private static Document mergeSVGs(Vector svgDocs) {
        Iterator iter = svgDocs.iterator();
        Document superDoc = (Document) iter.next();
        int yoffset = Integer.parseInt(superDoc.getDocumentElement().getAttribute("height"));
        yoffset += 10;
        while (iter.hasNext()) {
            Document svgDoc = (Document) iter.next();
            NodeList items = svgDoc.getDocumentElement().getElementsByTagName("*");
            for (int i = 0; i < items.getLength(); i++) {
                Element it = (Element) items.item(i);
                if (it.hasAttribute("y1")) {
                    it.setAttribute("y1", Integer.toString(Integer.parseInt(it.getAttribute("y1")) + yoffset));
                    it.setAttribute("y2", Integer.toString(Integer.parseInt(it.getAttribute("y2")) + yoffset));
                }
                if (it.hasAttribute("y")) {
                    it.setAttribute("y", Integer.toString(Integer.parseInt(it.getAttribute("y")) + yoffset));
                }
                Element newIt = superDoc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, it.getTagName());
                NamedNodeMap attributes = it.getAttributes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    Node att = attributes.item(j);
                    newIt.setAttributeNS(null, att.getNodeName(), att.getNodeValue());
                }
                if (it.getTagName().equals("text")) {
                    Text text = superDoc.createTextNode(it.getChildNodes().item(0).getNodeValue());
                    newIt.appendChild(text);
                }
                superDoc.getDocumentElement().appendChild(newIt);
            }
            yoffset += Integer.parseInt(svgDoc.getDocumentElement().getAttribute("height")) + 10;
        }
        superDoc.getDocumentElement().setAttribute("height", Integer.toString(yoffset));
        superDoc.getDocumentElement().setAttribute("viewBox", "0 0 3125 " + Integer.toString(yoffset));
        return superDoc;
    }

    public static String exportServiceResults(Element serviceElement) {
        if ("text".equals(serviceElement.getElementsByTagName("output").item(0).getTextContent())) {
            return printTextServiceResults(serviceElement);
        } else {
            String serviceName = serviceElement.getElementsByTagName("name").item(0).getTextContent();
            String ret = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:svg=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"><head><title>CoMPAS Pro Results for Service " + serviceName + "</title></head><body>\n";
            String svgString = svgToString(getServiceSVG(serviceElement).getDocumentElement());
            svgString = svgString.substring(svgString.indexOf("\n"));
            ret += "<h3>Graphical Results</h3>\n" + svgString + "\n<hr />\n";
            ret += "</body></html>";
            return ret;
        }
    }
}
