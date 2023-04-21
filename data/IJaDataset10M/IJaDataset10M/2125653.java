package cn.edu.thss.iise.beehivez.server.petrinetwithdata;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import javax.xml.parsers.DocumentBuilderFactory;
import org.processmining.framework.log.LogEvent;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.Token;
import org.processmining.framework.models.petrinet.Transition;
import org.processmining.framework.models.petrinet.TransitionCluster;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * change PnmlReader from ProM
 * 
 * data operations are considered in Petri net. In addition to the PNML schema,
 * the following tags are supported as shown in the examples.
 * 
 * <data id="data_1">
 *   <name>x</name> 
 * </data>
 * 
 * <datawriting>
 *   <writing>
 *     <transition>tran_1</transition>
 *     <data>data_1</data>
 *     <data>data_2</data>
 *   </writing>
 *   <writing>
 *     <transition>tran_2</transition>
 *     <data>dataitem_2</data>
 *   </writing>
 * </datawriting>
 * 
 * <datareading>
 *   <reading>
 *     <transition>tran_1</transition>
 *     <data>data_1</data>
 *     <data>data_2</data>
 *   </reading>
 *   <reading>
 *     <transition>tran_2</transition>
 *     <data>data_2</data> 
 *   </reading> 
 * </datareading>
 * 
 * @author Tao Jin
 * 
 * @date 2012-4-27
 * 
 */
public class DPnmlReader {

    private HashMap<String, Place> places;

    private HashMap<String, Transition> transitions;

    private HashMap<String, DataItem> variables;

    public DPnmlReader() {
    }

    public PetriNetWithData read(InputStream input) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc;
        PetriNetWithData dpn = new PetriNetWithData();
        NodeList netNodes;
        dbf.setValidating(false);
        dbf.setIgnoringComments(true);
        dbf.setIgnoringElementContentWhitespace(true);
        doc = dbf.newDocumentBuilder().parse(input);
        if (!doc.getDocumentElement().getTagName().equals("pnml")) {
            throw new Exception("pnml tag not found");
        }
        netNodes = doc.getDocumentElement().getElementsByTagName("net");
        if (netNodes.getLength() > 0) {
            parseNet(netNodes.item(0), dpn);
        }
        return dpn;
    }

    public PetriNetWithData read(Node node) throws Exception {
        PetriNetWithData result = new PetriNetWithData();
        parseNet(node, result);
        return result;
    }

    private void parseNet(Node node, PetriNetWithData dpn) throws Exception {
        Node id = node.getAttributes().getNamedItem("id");
        Node type = node.getAttributes().getNamedItem("type");
        if (id == null || id.getNodeValue() == null) {
            throw new Exception("net tag is missing the id attribute");
        }
        if (type == null || type.getNodeValue() == null) {
            throw new Exception("net tag is missing the type attribute)");
        }
        places = new HashMap<String, Place>();
        transitions = new HashMap<String, Transition>();
        variables = new HashMap<String, DataItem>();
        PetriNet pn = new PetriNet();
        parsePlaces(node, pn);
        parseTransitions(node, pn);
        parseArcs(node, pn);
        dpn.setPetriNet(pn);
        parseVariables(node, dpn);
        parseDataWriting(node, dpn);
        parseDataReading(node, dpn);
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("toolspecific") && n.getAttributes().getNamedItem("tool").getNodeValue().equals("ProM")) {
                foundToolSpecific();
                parseClusters(n, pn);
            }
        }
    }

    /**
	 * parseClusters
	 * 
	 */
    private void parseClusters(Node node, PetriNet net) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("cluster")) {
                String name = n.getAttributes().getNamedItem("name").getNodeValue();
                TransitionCluster tc = new TransitionCluster(name);
                NodeList trans = n.getChildNodes();
                for (int j = 0; j < trans.getLength(); j++) {
                    if (trans.item(j).getNodeName().equals("trans")) {
                        String transName = trans.item(j).getFirstChild().getNodeValue();
                        Transition t = (Transition) transitions.get(transName);
                        tc.add(t);
                    }
                }
                net.addCluster(tc);
            }
        }
    }

    private void parseVariables(Node node, PetriNetWithData dpn) throws Exception {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("data")) {
                String name = "";
                for (int j = 0; j < n.getChildNodes().getLength(); j++) {
                    Node n2 = n.getChildNodes().item(j);
                    if (n2.getNodeName().equals("name")) {
                        name = n2.getFirstChild().getNodeValue();
                    }
                }
                Node nid = n.getAttributes().getNamedItem("id");
                String id = nid.getNodeValue();
                DataItem di = new DataItem();
                di.setId(id);
                di.setName(name);
                variables.put(id, di);
                dpn.addVariable(di);
            }
        }
    }

    private void parsePlaces(Node node, PetriNet net) throws Exception {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("place")) {
                String name = "";
                int noTok = 0;
                for (int j = 0; j < n.getChildNodes().getLength(); j++) {
                    Node n2 = n.getChildNodes().item(j);
                    if (n2.getNodeName().equals("name")) {
                        NodeList nameChildren = n2.getChildNodes();
                        for (int k = 0; k < nameChildren.getLength(); k++) {
                            Node gn = nameChildren.item(k);
                            if (gn.getNodeName().equals("text") && gn.hasChildNodes()) {
                                name = gn.getFirstChild().getNodeValue();
                            }
                        }
                    }
                    if (n2.getNodeName().equals("initialMarking")) {
                        NodeList nameChildren = n2.getChildNodes();
                        for (int k = 0; k < nameChildren.getLength(); k++) {
                            Node gn = nameChildren.item(k);
                            if ((gn.getNodeName().equals("text") || gn.getNodeName().equals("value")) && gn.hasChildNodes()) {
                                noTok = Integer.parseInt(gn.getFirstChild().getNodeValue());
                            }
                        }
                    }
                }
                Node id = n.getAttributes().getNamedItem("id");
                Place p;
                if (id == null || id.getNodeValue() == null) {
                    throw new Exception("place tag is missing the id attribute)");
                }
                p = new Place(id.getNodeValue(), net);
                net.addPlace(p);
                places.put(id.getNodeValue(), p);
                if (!name.equals("")) {
                    p.setIdentifier(name);
                }
                for (int j = 0; j < noTok; j++) {
                    p.addToken(new Token());
                }
            }
        }
    }

    private void parseTransitions(Node node, PetriNet net) throws Exception {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("transition")) {
                Node id = n.getAttributes().getNamedItem("id");
                Transition t;
                if (id == null || id.getNodeValue() == null) {
                    throw new Exception("transition tag is missing the id attribute)");
                }
                t = parseTrans(n, net);
                net.addTransition(t);
                transitions.put(id.getNodeValue(), t);
            }
        }
    }

    private LogEvent parseLogEvent(Node node) throws Exception {
        NodeList children = node.getChildNodes();
        String logeventName = null;
        String logeventType = null;
        String name = null;
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("logevent")) {
                NodeList logeventChildren = n.getChildNodes();
                for (int j = 0; j < logeventChildren.getLength(); j++) {
                    Node gn = logeventChildren.item(j);
                    if (gn.getNodeName().equals("name") && gn.hasChildNodes()) {
                        logeventName = gn.getFirstChild().getNodeValue();
                    }
                    if (gn.getNodeName().equals("type") && gn.hasChildNodes()) {
                        logeventType = gn.getFirstChild().getNodeValue();
                    }
                }
            }
        }
        if (logeventName != null && logeventType != null) {
            return new LogEvent(logeventName, logeventType);
        } else {
            return null;
        }
    }

    private Transition parseTrans(Node node, PetriNet net) throws Exception {
        NodeList children = node.getChildNodes();
        LogEvent e = null;
        String name = null;
        String valueName = null;
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("name")) {
                NodeList nameChildren = n.getChildNodes();
                for (int j = 0; j < nameChildren.getLength(); j++) {
                    Node gn = nameChildren.item(j);
                    if ((gn.getNodeName().equals("text") || gn.getNodeName().equals("value")) && gn.hasChildNodes()) {
                        name = gn.getFirstChild().getNodeValue();
                    }
                    if (gn.getNodeName().equals("value") && gn.hasChildNodes()) {
                        valueName = gn.getFirstChild().getNodeValue();
                    }
                }
            }
            if (n.getNodeName().equals("toolspecific") && n.getAttributes().getNamedItem("tool").getNodeValue().equals("ProM")) {
                foundToolSpecific();
                e = parseLogEvent(n);
            }
            if ((valueName != null) && (!valueName.equals(""))) {
                e = new LogEvent(name, "auto");
            }
        }
        if (e != null) {
            Transition t = new Transition(e, net);
            if (name != null) {
                t.setIdentifier(name);
            }
            return t;
        } else if (name != null) {
            return new Transition(name, net);
        } else {
            return new Transition(node.getAttributes().getNamedItem("id").getNodeValue(), net);
        }
    }

    private void parseDataWriting(Node node, PetriNetWithData dpn) throws Exception {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("datawriting")) {
                for (int j = 0; j < n.getChildNodes().getLength(); j++) {
                    Node n1 = n.getChildNodes().item(j);
                    if (n1.getNodeName().equals("writing")) {
                        String tid = null;
                        String vid = null;
                        Transition t = null;
                        DataItem di = null;
                        HashSet<DataItem> dis = new HashSet<DataItem>();
                        for (int k = 0; k < n1.getChildNodes().getLength(); k++) {
                            Node n2 = n1.getChildNodes().item(k);
                            if (n2.getNodeName().equals("transition")) {
                                tid = n2.getFirstChild().getNodeValue();
                                if (tid != null) {
                                    t = transitions.get(tid);
                                }
                            }
                            if (n2.getNodeName().equals("data")) {
                                vid = n2.getFirstChild().getNodeValue();
                                if (vid != null) {
                                    di = variables.get(vid);
                                }
                                if (di != null) {
                                    dis.add(di);
                                }
                            }
                        }
                        if (t != null && dis.size() > 0) {
                            dpn.addDataWriting(t, dis);
                        }
                    }
                }
            }
        }
    }

    private void parseDataReading(Node node, PetriNetWithData dpn) throws Exception {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("datareading")) {
                for (int j = 0; j < n.getChildNodes().getLength(); j++) {
                    Node n1 = n.getChildNodes().item(j);
                    if (n1.getNodeName().equals("reading")) {
                        String tid = null;
                        String vid = null;
                        Transition t = null;
                        DataItem di = null;
                        HashSet<DataItem> dis = new HashSet<DataItem>();
                        for (int k = 0; k < n1.getChildNodes().getLength(); k++) {
                            Node n2 = n1.getChildNodes().item(k);
                            if (n2.getNodeName().equals("transition")) {
                                tid = n2.getFirstChild().getNodeValue();
                                if (tid != null) {
                                    t = transitions.get(tid);
                                }
                            }
                            if (n2.getNodeName().equals("data")) {
                                vid = n2.getFirstChild().getNodeValue();
                                if (vid != null) {
                                    di = variables.get(vid);
                                }
                                if (di != null) {
                                    dis.add(di);
                                }
                            }
                        }
                        if (t != null && dis.size() > 0) {
                            dpn.addDataReading(t, dis);
                        }
                    }
                }
            }
        }
    }

    private void parseArcs(Node node, PetriNet net) throws Exception {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeName().equals("arc")) {
                Node id = n.getAttributes().getNamedItem("id");
                Node source = n.getAttributes().getNamedItem("source");
                Node target = n.getAttributes().getNamedItem("target");
                if (id == null || id.getNodeValue() == null || source == null || source.getNodeValue() == null || target == null || target.getNodeValue() == null) {
                    throw new Exception("arc tag is missing id, source or target attribute)");
                }
                if (places.get(source.getNodeValue()) != null) {
                    net.addEdge((Place) places.get(source.getNodeValue()), (Transition) transitions.get(target.getNodeValue()));
                } else {
                    net.addEdge((Transition) transitions.get(source.getNodeValue()), (Place) places.get(target.getNodeValue()));
                }
            }
        }
    }

    protected void foundToolSpecific() {
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
    }
}
