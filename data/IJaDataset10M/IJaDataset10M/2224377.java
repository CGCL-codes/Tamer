package org.megadix.jfcm.utils;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.*;
import org.apache.commons.lang3.StringUtils;
import org.megadix.jfcm.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class FcmIO {

    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    public static void saveAsXml(CognitiveMap map, OutputStream outputStream) {
        List<CognitiveMap> maps = new ArrayList<CognitiveMap>(1);
        maps.add(map);
        saveAsXml(maps, outputStream);
    }

    public static void saveAsXml(List<CognitiveMap> maps, OutputStream outputStream) {
        try {
            DocumentBuilder documentBuilder = getDocumentBuilder(true);
            Document doc = documentBuilder.newDocument();
            Element mapsElem = doc.createElement("maps");
            doc.appendChild(mapsElem);
            for (CognitiveMap map : maps) {
                appendMap(map, doc, mapsElem);
            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(outputStream));
        } catch (RuntimeException rtex) {
            throw rtex;
        } catch (Exception ex) {
            throw new RuntimeException("Error saving XML file", ex);
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException ioex) {
                throw new RuntimeException("Error closing stream", ioex);
            }
        }
    }

    public static List<CognitiveMap> loadXml(InputStream inputStream) throws ParseException {
        try {
            DocumentBuilder documentBuilder = getDocumentBuilder(true);
            Document doc = documentBuilder.parse(inputStream);
            XPath xpath = XPathFactory.newInstance().newXPath();
            List<CognitiveMap> maps = new ArrayList<CognitiveMap>();
            NodeList nodelist = (NodeList) xpath.evaluate("/maps/map", doc, XPathConstants.NODESET);
            for (int i = 0; i < nodelist.getLength(); i++) {
                Element mapElem = (Element) nodelist.item(i);
                maps.add(parseMap(xpath, mapElem));
            }
            return maps;
        } catch (ParseException pex) {
            throw pex;
        } catch (Exception ex) {
            throw new RuntimeException("Error loading XML file", ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ioex) {
                throw new RuntimeException("Error closing stream", ioex);
            }
        }
    }

    private static DocumentBuilder getDocumentBuilder(boolean strict) throws SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        if (strict) {
            Source schemaSource = new StreamSource(FcmIO.class.getResourceAsStream("/JFCM-map-v-1.1.xsd"));
            Schema schema = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema").newSchema(schemaSource);
            documentBuilderFactory.setAttribute(JAXP_SCHEMA_SOURCE, schema);
        }
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder;
    }

    private static void appendMap(CognitiveMap map, Document doc, Element mapsElem) {
        Element mapElem = doc.createElement("map");
        mapElem.setAttribute("name", map.getName());
        mapsElem.appendChild(mapElem);
        appendConcepts(map, doc, mapElem);
        appendConnections(map, doc, mapElem);
    }

    private static void appendConcepts(CognitiveMap map, Document doc, Element elem) {
        Element elemConcepts = doc.createElement("concepts");
        elem.appendChild(elemConcepts);
        Iterator<Concept> cIter = map.getConceptsIterator();
        while (cIter.hasNext()) {
            Concept c = cIter.next();
            Element elemConcept = doc.createElement("concept");
            elemConcept.setAttribute("name", c.getName());
            if (c.getDescription() != null) {
                Element elemDescription = doc.createElement("description");
                elemDescription.setTextContent(c.getDescription());
                elemConcept.appendChild(elemDescription);
            }
            Element paramsElem = doc.createElement("params");
            elemConcept.appendChild(paramsElem);
            if (c.getConceptActivator() != null) {
                if (BaseConceptActivator.class.isAssignableFrom(c.getConceptActivator().getClass())) {
                    BaseConceptActivator act = (BaseConceptActivator) c.getConceptActivator();
                    addXmlParam(paramsElem, "threshold", Double.toString(act.getThreshold()));
                }
                if (c.getConceptActivator() instanceof LinearActivator) {
                    LinearActivator actImpl = (LinearActivator) c.getConceptActivator();
                    addXmlParam(paramsElem, "factor", Double.toString(actImpl.getFactor()));
                    addXmlParam(paramsElem, "min", Double.toString(actImpl.getMin()));
                    addXmlParam(paramsElem, "max", Double.toString(actImpl.getMax()));
                    elemConcept.setAttribute("act", Constants.ConceptActivatorTypes.LINEAR.name());
                } else if (c.getConceptActivator() instanceof SignumActivator) {
                    SignumActivator actImpl = (SignumActivator) c.getConceptActivator();
                    elemConcept.setAttribute("act", Constants.ConceptActivatorTypes.SIGNUM.name());
                } else if (c.getConceptActivator() instanceof SigmoidActivator) {
                    SigmoidActivator actImpl = (SigmoidActivator) c.getConceptActivator();
                    elemConcept.setAttribute("act", Constants.ConceptActivatorTypes.SIGMOID.name());
                    addXmlParam(paramsElem, "k", Double.toString(actImpl.getK()));
                } else if (c.getConceptActivator() instanceof HyperbolicTangentActivator) {
                    HyperbolicTangentActivator actImpl = (HyperbolicTangentActivator) c.getConceptActivator();
                    elemConcept.setAttribute("act", Constants.ConceptActivatorTypes.TANH.name());
                } else {
                    throw new IllegalArgumentException("Unsupported ConceptActivator: " + c.getConceptActivator().getClass().getName());
                }
            }
            if (c.getInput() != null) {
                elemConcept.setAttribute("input", c.getInput().toString());
            }
            if (c.getOutput() != null) {
                elemConcept.setAttribute("output", c.getOutput().toString());
            }
            if (c.isFixedOutput()) {
                elemConcept.setAttribute("fixed", "true");
            }
            elemConcepts.appendChild(elemConcept);
        }
    }

    private static void addXmlParam(Element paramsElem, String name, String value) {
        Element paramElem = paramsElem.getOwnerDocument().createElement("param");
        paramElem.setAttribute("name", name);
        paramElem.setAttribute("value", value);
        paramsElem.appendChild(paramElem);
    }

    private static void appendConnections(CognitiveMap map, Document doc, Element elem) {
        Element elemConnections = doc.createElement("connections");
        elem.appendChild(elemConnections);
        Iterator<FcmConnection> connIter = map.getConnectionsIterator();
        while (connIter.hasNext()) {
            FcmConnection conn = connIter.next();
            Element elemConn = doc.createElement("connection");
            elemConn.setAttribute("name", conn.getName());
            if (conn.getDescription() != null) {
                Element elemDescription = doc.createElement("description");
                elemDescription.setTextContent(conn.getDescription());
                elemConn.appendChild(elemDescription);
            }
            if (conn.getFrom() != null) {
                elemConn.setAttribute("from", conn.getFrom().getName());
            }
            if (conn.getTo() != null) {
                elemConn.setAttribute("to", conn.getTo().getName());
            }
            if (conn instanceof WeightedConnection) {
                elemConn.setAttribute("type", "WEIGHTED");
                WeightedConnection wc = (WeightedConnection) conn;
                Element paramsElem = doc.createElement("params");
                elemConn.appendChild(paramsElem);
                Element paramElem = doc.createElement("param");
                paramElem.setAttribute("name", "weight");
                paramElem.setAttribute("value", Double.toString(wc.getWeight()));
                paramsElem.appendChild(paramElem);
            } else {
                throw new UnsupportedOperationException("FcmConnection implementation not supported: " + conn.getClass().getName());
            }
            elemConnections.appendChild(elemConn);
        }
    }

    private static CognitiveMap parseMap(XPath xpath, Element mapElem) throws Exception {
        NodeList nodelist;
        CognitiveMap map = new CognitiveMap();
        map.setName(xpath.evaluate("@name", mapElem));
        nodelist = (NodeList) xpath.evaluate("concepts/concept", mapElem, XPathConstants.NODESET);
        for (int i = 0; i < nodelist.getLength(); i++) {
            Element conceptElem = (Element) nodelist.item(i);
            map.addConcept(parseConcept(xpath, conceptElem));
        }
        nodelist = (NodeList) xpath.evaluate("connections/connection", mapElem, XPathConstants.NODESET);
        for (int i = 0; i < nodelist.getLength(); i++) {
            Element connElem = (Element) nodelist.item(i);
            map.addConnection(parseConnection(map, xpath, connElem));
        }
        Iterator<FcmConnection> iter = map.getConnectionsIterator();
        while (iter.hasNext()) {
            FcmConnection conn = iter.next();
            map.connect(conn.getFrom().getName(), conn.getName(), conn.getTo().getName());
        }
        return map;
    }

    private static Concept parseConcept(XPath xpath, Element conceptElem) throws XPathExpressionException, ParseException {
        Concept c = new Concept();
        c.setName(xpath.evaluate("@name", conceptElem));
        String description = xpath.evaluate("description/text()", conceptElem);
        if (StringUtils.isNotBlank(description)) {
            c.setDescription(description);
        }
        String actAttr = conceptElem.getAttribute("act");
        Map<String, String> params = parseParams(conceptElem, xpath);
        Double threshold = null;
        if (params.containsKey("threshold")) {
            threshold = Double.parseDouble(params.get("threshold"));
        }
        if (Constants.ConceptActivatorTypes.LINEAR.name().equals(actAttr)) {
            LinearActivator act = new LinearActivator();
            if (threshold != null) {
                act.setThreshold(threshold);
            }
            if (params.containsKey("factor")) {
                act.setFactor(Double.parseDouble(params.get("factor")));
            }
            if (params.containsKey("min")) {
                act.setMin(Double.parseDouble(params.get("min")));
            }
            if (params.containsKey("max")) {
                act.setMax(Double.parseDouble(params.get("max")));
            }
            c.setConceptActivator(act);
        } else if (Constants.ConceptActivatorTypes.SIGNUM.name().equals(actAttr)) {
            SignumActivator act = new SignumActivator();
            if (threshold != null) {
                act.setThreshold(threshold);
            }
            c.setConceptActivator(act);
        } else if (Constants.ConceptActivatorTypes.SIGMOID.name().equals(actAttr)) {
            SigmoidActivator act = new SigmoidActivator();
            Element kParam = (Element) xpath.evaluate("params/param[@name='k']", conceptElem, XPathConstants.NODE);
            Double k = null;
            if (kParam != null) {
                k = Double.parseDouble(xpath.evaluate("@value", kParam));
            }
            if (k != null) {
                act.setK(k);
            }
            c.setConceptActivator(act);
        } else if (Constants.ConceptActivatorTypes.TANH.name().equals(actAttr)) {
            HyperbolicTangentActivator act = new HyperbolicTangentActivator();
            c.setConceptActivator(act);
            if (threshold != null) {
                act.setThreshold(threshold);
            }
            c.setConceptActivator(act);
        } else {
            throw new ParseException("ConceptActivator not supported: \"" + StringUtils.defaultString(actAttr) + "\"", 0);
        }
        String s;
        s = xpath.evaluate("@input", conceptElem);
        if (StringUtils.isNotBlank(s)) {
            c.setInput(Double.parseDouble(s));
        }
        s = xpath.evaluate("@output", conceptElem);
        if (StringUtils.isNotBlank(s)) {
            c.setOutput(Double.parseDouble(s));
        }
        s = xpath.evaluate("@fixed", conceptElem);
        if (StringUtils.isNotBlank(s)) {
            c.setFixedOutput(Boolean.parseBoolean(s));
        }
        return c;
    }

    private static Map<String, String> parseParams(Element elem, XPath xpath) throws XPathExpressionException {
        Map<String, String> params = new HashMap<String, String>();
        Element paramsElem = (Element) xpath.evaluate("params", elem, XPathConstants.NODE);
        if (paramsElem != null) {
            NodeList paramsList = (NodeList) xpath.evaluate("param", paramsElem, XPathConstants.NODESET);
            for (int i = 0; i < paramsList.getLength(); i++) {
                Element param = (Element) paramsList.item(i);
                params.put(param.getAttribute("name"), param.getAttribute("value"));
            }
        }
        return params;
    }

    private static FcmConnection parseConnection(CognitiveMap map, XPath xpath, Element connElem) throws Exception {
        FcmConnection conn;
        String s;
        String name = xpath.evaluate("@name", connElem);
        if (StringUtils.isBlank(name)) {
            throw new Exception("Missing connection name");
        }
        Map<String, String> params = parseParams(connElem, xpath);
        String type = xpath.evaluate("@type", connElem);
        if ("WEIGHTED".equalsIgnoreCase(type)) {
            WeightedConnection wConn = new WeightedConnection();
            if (params.containsKey("weight")) {
                wConn.setWeight(Double.parseDouble(params.get("weight")));
            }
            conn = wConn;
        } else {
            throw new ParseException("Connection type not supported: \"" + type + "\"", 0);
        }
        conn.setName(name);
        String description = xpath.evaluate("description/text()", connElem);
        if (StringUtils.isNotBlank(description)) {
            conn.setDescription(description);
        }
        Concept c;
        s = xpath.evaluate("@from", connElem);
        if (StringUtils.isBlank(s)) {
            throw new Exception("Missing \"from\" reference in connection \"" + conn.getName() + "\"");
        }
        c = map.getConcept(s);
        if (c == null) {
            throw new Exception("Missing \"from\" reference in connection \"" + conn.getName() + "\"");
        }
        conn.setFrom(c);
        s = xpath.evaluate("@to", connElem);
        if (StringUtils.isBlank(s)) {
            throw new Exception("Missing \"to\" reference in connection \"" + conn.getName() + "\"");
        }
        c = map.getConcept(s);
        if (c == null) {
            throw new Exception("Missing \"to\" reference in connection \"" + conn.getName() + "\"");
        }
        conn.setTo(c);
        return conn;
    }
}
