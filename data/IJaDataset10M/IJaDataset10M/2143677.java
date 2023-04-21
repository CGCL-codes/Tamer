package com.avometric.SHARD.reasoner.owlLite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.avometric.SHARD.TripleCounter;
import com.avometric.SHARD.reasoner.Reasoner;

public class ReasonerOWLLite extends Reasoner {

    HashMap<String, Collection<String>> _subClassMap;

    HashMap<String, Collection<String>> _subPropertyMap;

    public ReasonerOWLLite(String rdfSchema, String tripleDir, String reasonDir, String system, String tracker, String replication, String jar, String mappers, String reducers) throws SAXException, IOException, ParserConfigurationException {
        super(rdfSchema, tripleDir, reasonDir, system, tracker, replication, jar, mappers, reducers);
        _subClassMap = new HashMap<String, Collection<String>>();
        _subPropertyMap = new HashMap<String, Collection<String>>();
        setMaps();
    }

    private void printMaps() {
        System.out.println("-------- subClassOf --------");
        printMap(_subClassMap);
        System.out.println("-------- subPropertyOf --------");
        printMap(_subPropertyMap);
    }

    private void printMap(HashMap<String, Collection<String>> classMap) {
        for (String key : classMap.keySet()) {
            System.out.println(key + " - " + classMap.get(key));
        }
    }

    private void setMaps() throws SAXException, IOException, ParserConfigurationException {
        intitializeMaps();
        condenseMaps();
    }

    private void condenseMaps() {
        condenseMap(_subClassMap);
        condenseMap(_subPropertyMap);
    }

    private void condenseMap(HashMap<String, Collection<String>> classMap) {
        for (String key : classMap.keySet()) {
            Collection<String> startVals = classMap.get(key);
            Collection<String> retVals = new HashSet<String>();
            List<String> toProcess = new ArrayList<String>();
            toProcess.addAll(startVals);
            while (toProcess.size() > 0) {
                String current = toProcess.remove(0);
                if (!retVals.contains(current)) {
                    retVals.add(current);
                    if (classMap.keySet().contains(current)) {
                        for (String val : classMap.get(current)) {
                            if (!toProcess.contains(val) && !retVals.contains(val)) {
                                toProcess.add(val);
                            }
                        }
                    }
                }
            }
            startVals.removeAll(startVals);
            startVals.addAll(retVals);
        }
    }

    private void intitializeMaps() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File f = new File(_rdfSchema);
        Document document = builder.parse(f);
        NodeList nodes = document.getDocumentElement().getChildNodes();
        String id = null;
        String superClass = null;
        String superProp = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (node.getAttributes().getLength() > 0) {
                    Node attr = node.getAttributes().getNamedItem("rdf:ID");
                    if (attr != null) {
                        id = ":" + attr.getNodeValue();
                        processElement(element, "rdfs:subClassOf", _subClassMap, id);
                        processElement(element, "rdfs:subPropertyOf", _subPropertyMap, id);
                    }
                }
            }
        }
    }

    private void processElement(Element element, String string, HashMap<String, Collection<String>> classMap, String id) {
        NodeList nodeList;
        nodeList = element.getElementsByTagName(string);
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node subPropNode = nodeList.item(j);
            Node val = subPropNode.getAttributes().item(j);
            if (val != null) {
                String superVal = ":" + val.getNodeValue().substring(1);
                if (!classMap.keySet().contains(id)) {
                    classMap.put(id, new HashSet<String>());
                }
                Collection<String> col = classMap.get(id);
                col.add(superVal);
            }
        }
    }

    @Override
    protected void generateReasonedData() throws Exception {
        String[] mr_args = new String[2];
        mr_args[0] = _tripleDir;
        mr_args[1] = _reasonDir;
        int res = ToolRunner.run(new Configuration(), new OWLLiteReasoner(_system, _tracker, _replication, _jar, _mappers, _reducers, _subClassMap, _subPropertyMap), mr_args);
    }
}
