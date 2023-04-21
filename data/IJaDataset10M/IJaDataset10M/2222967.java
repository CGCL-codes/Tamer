package org.megadix.jfcm.impl;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import org.megadix.jfcm.*;
import org.megadix.jfcm.utils.StringUtils;
import org.w3c.dom.*;

public class DefaultCognitiveMap extends AbstractCognitiveMap {

    private String name;

    private Map<String, Concept> concepts = new TreeMap<String, Concept>();

    private Map<String, FcmConnection> connections = new TreeMap<String, FcmConnection>();

    public DefaultCognitiveMap() {
    }

    public DefaultCognitiveMap(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Iterator<Concept> iter = concepts.values().iterator();
        while (iter.hasNext()) {
            Concept c = iter.next();
            sb.append(c.getName());
            sb.append(" = ");
            sb.append(c.getOutput());
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public DefaultConcept newConcept(String name, ConceptActivator conceptActivator) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Empty name");
        }
        if (concepts.containsKey(name)) {
            throw new IllegalArgumentException("Concept name \"" + name + "\" already used");
        }
        DefaultConcept concept = new DefaultConcept(name, null, conceptActivator, null, null, false);
        concepts.put(name, concept);
        concept.setMap(this);
        return concept;
    }

    public void removeConcept(String conceptName) {
        if (StringUtils.isBlank(conceptName)) {
            return;
        }
        Concept c = getConcept(conceptName);
        if (c == null) {
            return;
        }
        for (FcmConnection conn : c.getOutConnections()) {
            conn.setFrom(null);
        }
        for (FcmConnection conn : c.getInConnections()) {
            conn.setTo(null);
        }
        concepts.remove(conceptName);
    }

    /**
     * Creates a {@link WeightedConnection}
     */
    public WeightedConnection newConnection(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Empty name");
        }
        if (connections.containsKey(name)) {
            throw new IllegalArgumentException("FcmConnection name \"" + name + "\" already used");
        }
        WeightedConnection conn = new WeightedConnection(name, null, 0.0);
        connections.put(name, conn);
        conn.setMap(this);
        return conn;
    }

    public void removeConnection(String connectionName) {
        FcmConnection conn = getConnection(connectionName);
        if (conn == null) {
            return;
        }
        if (conn.getFrom() != null) {
            conn.getFrom().removeOutputConnection(conn);
        }
        if (conn.getTo() != null) {
            conn.getTo().removeInputConnection(conn);
        }
        connections.remove(connectionName);
    }

    public Concept getConcept(String name) {
        return concepts.get(name);
    }

    public FcmConnection getConnection(String name) {
        return connections.get(name);
    }

    public Map<String, Concept> getConcepts() {
        return Collections.unmodifiableMap(concepts);
    }

    public Iterator<Concept> getConceptsIterator() {
        return concepts.values().iterator();
    }

    public Map<String, FcmConnection> getConnections() {
        return Collections.unmodifiableMap(connections);
    }

    public Iterator<FcmConnection> getConnectionsIterator() {
        return connections.values().iterator();
    }

    public void saveAsXml(OutputStream outputStream) {
        List<CognitiveMap> maps = new ArrayList<CognitiveMap>(1);
        maps.add(this);
        saveAsXml(maps, outputStream);
    }

    public static void saveAsXml(List<CognitiveMap> maps, OutputStream outputStream) {
        try {
            DocumentBuilder documentBuilder = DefaultXmlMapParser.getDocumentBuilder(true);
            Document doc = documentBuilder.newDocument();
            Element mapsElem = doc.createElement("maps");
            doc.appendChild(mapsElem);
            for (CognitiveMap map : maps) {
                DefaultXmlMapParser.appendMap(map, doc, mapsElem);
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
            DocumentBuilder documentBuilder = DefaultXmlMapParser.getDocumentBuilder(true);
            Document doc = documentBuilder.parse(inputStream);
            XPath xpath = XPathFactory.newInstance().newXPath();
            List<CognitiveMap> maps = new ArrayList<CognitiveMap>();
            NodeList nodelist = (NodeList) xpath.evaluate("/maps/map", doc, XPathConstants.NODESET);
            for (int i = 0; i < nodelist.getLength(); i++) {
                Element mapElem = (Element) nodelist.item(i);
                maps.add(DefaultXmlMapParser.parseMap(xpath, mapElem));
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
