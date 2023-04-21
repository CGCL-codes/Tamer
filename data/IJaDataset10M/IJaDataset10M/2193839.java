package eu.fbk.soa.stradj.bpel.printstruct;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import eu.fbk.soa.stradj.bpel.graph.BPELGraph;
import eu.fbk.soa.stradj.bpel.graph.GraphNode;

public class PrintContainer {

    public static final int ctGRAPH = 700;

    public static final int ctSEQUENCE = 701;

    public static final int ctFLOW = 702;

    public static final int ctSIMPLE = 703;

    public static final int ctPICK = 704;

    public static final int ctONMSG = 705;

    public static final int ctEMPTY = 706;

    public static final int ctIF = 707;

    public static final int ctELSEIF = 708;

    public static final int ctELSE = 709;

    private ArrayList<PrintContainer> elements;

    private int type;

    private PrintContainer parent;

    private GraphNode simple;

    private LinkedList<GraphNode> queue;

    public PrintContainer(GraphNode node, boolean isRoot) {
        elements = new ArrayList<PrintContainer>();
        queue = new LinkedList<GraphNode>();
        parent = null;
        simple = null;
        if (isRoot) {
            this.type = ctSEQUENCE;
            PrintContainer root_node = new PrintContainer(node, this);
            node.setPrinted();
            processContainer(root_node);
        } else {
            this.type = ctSIMPLE;
            this.simple = node;
        }
    }

    public PrintContainer(ArrayList<GraphNode> nodes) {
        elements = new ArrayList<PrintContainer>();
        queue = new LinkedList<GraphNode>();
        parent = null;
        simple = null;
        if (nodes.size() > 1) {
            this.type = ctFLOW;
        } else {
            this.type = ctSEQUENCE;
        }
        for (int i = 0; i < nodes.size(); i++) {
            GraphNode node = nodes.get(i);
            PrintContainer root_node = new PrintContainer(node, this);
            node.setPrinted();
            processContainer(root_node);
        }
    }

    private PrintContainer(GraphNode node, PrintContainer parent) {
        if (node.getClass().getSimpleName().equals("GraphNode")) {
            this.type = ctSIMPLE;
        } else if (node.getOperation() != null) {
            if (node.getOperation().equals("pick")) {
                this.type = ctPICK;
            } else if (node.getOperation().equals("onMessage")) {
                this.type = ctONMSG;
            } else if (node.getOperation().equals("if")) {
                this.type = ctIF;
            } else if (node.getOperation().equals("elseif")) {
                this.type = ctELSEIF;
            } else if (node.getOperation().equals("else")) {
                this.type = ctELSE;
            }
        } else {
            this.type = ctGRAPH;
        }
        elements = new ArrayList<PrintContainer>();
        queue = new LinkedList<GraphNode>();
        this.parent = parent;
        this.simple = node;
        this.parent.addElement(this);
        node.setPrintContainer(this);
    }

    public PrintContainer(int type, PrintContainer parent) {
        this.type = type;
        elements = new ArrayList<PrintContainer>();
        queue = new LinkedList<GraphNode>();
        this.parent = parent;
        this.parent.addElement(this);
        this.simple = null;
    }

    private void setParentContainer(PrintContainer container) {
        this.parent = container;
    }

    public PrintContainer getParentContainer() {
        return this.parent;
    }

    public void addElement(PrintContainer element) {
        StringBuffer sb = new StringBuffer();
        sb.append("Add element ").append(element).append(" of type ").append(element.type);
        if (element.simple != null) {
            sb.append(" with simple ").append(element.simple.getOperation());
        }
        sb.append(" to container ").append(this).append(" of type ").append(this.type);
        if (this.simple != null) {
            sb.append(" with simple ").append(this.simple.getOperation());
        }
        if (this.elements.contains(element)) {
            try {
                throw new PrintContainerException("Contains!");
            } catch (PrintContainerException e) {
                e.printStackTrace();
            }
        } else {
            this.elements.add(element);
            element.setParentContainer(this);
        }
    }

    public void removeElement(PrintContainer element) {
        StringBuffer sb = new StringBuffer();
        sb.append("Remove element ").append(element).append(" of type ").append(element.type);
        if (element.simple != null) {
            sb.append(" with simple ").append(element.simple.getOperation());
        }
        sb.append(" from container ").append(this).append(" of type ").append(this.type);
        if (this.simple != null) {
            sb.append(" with simple ").append(this.simple.getOperation());
        }
        this.elements.remove(element);
    }

    public int getContainerType() {
        return this.type;
    }

    private GraphNode getSimple() {
        return this.simple;
    }

    /**
     * cont in general case is a simple container which includes a single node
     * */
    public void processContainer(PrintContainer cont) {
        if (cont.getContainerType() == ctSIMPLE) {
            GraphNode node = cont.getSimple();
            processNode(cont, node);
        } else if (cont.getContainerType() == ctPICK) {
            BPELGraph pickGraph = (BPELGraph) cont.getSimple();
            processPick(cont, pickGraph);
            processNode(cont, pickGraph);
        } else if (cont.getContainerType() == ctONMSG) {
            BPELGraph onmGraph = (BPELGraph) cont.getSimple();
            processOnMsg(cont, onmGraph);
        } else if (cont.getContainerType() == ctIF) {
            BPELGraph ifGraph = (BPELGraph) cont.getSimple();
            processIf(cont, ifGraph);
            processNode(cont, ifGraph);
        } else if (cont.getContainerType() == ctELSEIF) {
            BPELGraph elseifGraph = (BPELGraph) cont.getSimple();
            processElseIf(cont, elseifGraph);
        } else if (cont.getContainerType() == ctELSE) {
            BPELGraph elseGraph = (BPELGraph) cont.getSimple();
            processElse(cont, elseGraph);
        } else if (cont.getContainerType() == ctGRAPH) {
            BPELGraph graph = (BPELGraph) cont.getSimple();
            PrintContainer rcont = new PrintContainer(graph.getRoots());
            PrintContainer parent = cont.getParentContainer();
            parent.removeElement(cont);
            parent.addElement(rcont);
        } else {
        }
    }

    private void processNode(PrintContainer cont, GraphNode node) {
        if (node.getChildrenCount() > 1) {
            processMultipleChildren(cont, node);
        } else if (node.getChildrenCount() == 1) {
            processOnezeroChildren(cont, node);
        } else if (node.getChildrenCount() < 1) {
            PrintContainer pcont = cont.getParentContainer();
            if (pcont.getContainerType() == ctONMSG) {
                pcont.removeElement(cont);
                PrintContainer seq = new PrintContainer(ctSEQUENCE, pcont);
                PrintContainer empty = new PrintContainer(ctEMPTY, seq);
            }
        }
    }

    private void processPick(PrintContainer cont, BPELGraph pickGraph) {
        GraphNode node = pickGraph.getRoots().get(0);
        PrintContainer pick = new PrintContainer(node, cont);
        node.setPrinted();
        for (int i = 0; i < node.getChildrenCount(); i++) {
            BPELGraph onmGraph = (BPELGraph) node.getChild(i);
            PrintContainer onm = new PrintContainer(onmGraph, cont);
            processContainer(onm);
        }
    }

    private void processOnMsg(PrintContainer cont, BPELGraph onmGraph) {
        GraphNode node = onmGraph.getRoots().get(0);
        PrintContainer onmCont = new PrintContainer(node, cont);
        node.setPrinted();
        processNode(onmCont, node);
    }

    private void processIf(PrintContainer cont, BPELGraph ifGraph) {
        GraphNode node = ifGraph.getRoots().get(0);
        PrintContainer ifNode = new PrintContainer(node, cont);
        node.setPrinted();
        for (int i = 0; i < node.getChildrenCount(); i++) {
            BPELGraph ifsub = (BPELGraph) node.getChild(i);
            PrintContainer ifcont = new PrintContainer(ifsub, cont);
            if (ifcont.getContainerType() == 0) {
            }
            processContainer(ifcont);
        }
    }

    private void processElseIf(PrintContainer cont, BPELGraph elseifGraph) {
        GraphNode node = elseifGraph.getRoots().get(0);
        PrintContainer elseifCont = new PrintContainer(node, cont);
        node.setPrinted();
        processNode(elseifCont, node);
    }

    private void processElse(PrintContainer cont, BPELGraph elseGraph) {
        GraphNode node = elseGraph.getRoots().get(0);
        PrintContainer elseCont = new PrintContainer(node, cont);
        node.setPrinted();
        processNode(elseCont, node);
    }

    private void processOnezeroChildren(PrintContainer cont, GraphNode node) {
        PrintContainer sequence;
        if (cont.getParentContainer().getContainerType() != ctSEQUENCE) {
            sequence = new PrintContainer(ctSEQUENCE, cont.getParentContainer());
            cont.getParentContainer().removeElement(cont);
            sequence.addElement(cont);
        } else {
            sequence = cont.getParentContainer();
        }
        if (node.getChildrenCount() == 1) {
            GraphNode single_child = node.getChild(0);
            if (single_child.hasAllParentsPrinted()) {
                if (!queue.contains(single_child)) {
                    single_child.setPrinted();
                    PrintContainer child_container = new PrintContainer(single_child, sequence);
                    processContainer(child_container);
                } else {
                    processQueue(single_child);
                }
            } else {
                queue.add(single_child);
            }
        } else {
        }
    }

    private void processQueue(GraphNode single_child, GraphNode single_child_parent) {
        PrintContainer place = processQueue(single_child);
        for (int i = 0; i < single_child_parent.getChildrenCount(); i++) {
            GraphNode graph_node = single_child_parent.getChild(i);
            if (graph_node != single_child) {
                queue.remove(graph_node);
                graph_node.setPrinted();
                PrintContainer sequence_for_child = new PrintContainer(ctSEQUENCE, place);
                PrintContainer child_container = new PrintContainer(graph_node, sequence_for_child);
                processContainer(child_container);
            }
        }
    }

    private PrintContainer processQueue(GraphNode single_child) {
        PrintContainer common4parents = single_child.getCommonParentContainer();
        if (common4parents.getContainerType() != ctFLOW) {
            new PrintContainerException("Unexpected container type");
        }
        PrintContainer newsequence = new PrintContainer(ctSEQUENCE, common4parents);
        PrintContainer common_parents = new PrintContainer(ctFLOW, newsequence);
        ArrayList<PrintContainer> parentContainers = single_child.getMaxParentContainers(common4parents);
        for (int i = 0; i < parentContainers.size(); i++) {
            PrintContainer parentContainer = parentContainers.get(i);
            common4parents.removeElement(parentContainer);
            common_parents.addElement(parentContainer);
        }
        PrintContainer common_children = new PrintContainer(this.ctFLOW, newsequence);
        int qsize = queue.size();
        LinkedList<PrintContainer> queue_children = new LinkedList<PrintContainer>();
        while (!queue.isEmpty() && qsize > 0) {
            GraphNode qnode = queue.remove();
            qsize--;
            if (qnode.hasAllParentsPrinted()) {
                qnode.setPrinted();
                PrintContainer child_container = new PrintContainer(qnode, common_children);
                queue_children.add(child_container);
            } else {
                queue.add(qnode);
            }
        }
        while (queue_children.size() > 0) {
            PrintContainer child_container = queue_children.remove();
            processContainer(child_container);
        }
        return common_children;
    }

    private void processMultipleChildren(PrintContainer cont, GraphNode node) {
        boolean obstacled = false;
        boolean requires_following_flow = false;
        GraphNode node_requires_following_flow = null;
        for (int i = 0; i < node.getChildrenCount() && !obstacled; i++) {
            GraphNode graph_node = node.getChild(i);
            if (!graph_node.hasAllParentsPrinted()) {
                obstacled = true;
            }
            if (queue.contains(graph_node)) {
                requires_following_flow = true;
                node_requires_following_flow = graph_node;
            }
        }
        if (!requires_following_flow) {
            if (!obstacled) {
                PrintContainer flow = new PrintContainer(ctFLOW, cont.getParentContainer());
                for (int i = 0; i < node.getChildrenCount(); i++) {
                    GraphNode graph_node = node.getChild(i);
                    if (!queue.contains(graph_node)) {
                        graph_node.setPrinted();
                        PrintContainer sequence_for_child = new PrintContainer(ctSEQUENCE, flow);
                        PrintContainer child_container = new PrintContainer(graph_node, sequence_for_child);
                        processContainer(child_container);
                    } else {
                        processQueue(graph_node);
                    }
                }
            } else {
                for (int i = 0; i < node.getChildrenCount(); i++) {
                    GraphNode graph_node = node.getChild(i);
                    queue.add(graph_node);
                }
            }
        } else {
            processQueue(node_requires_following_flow, node);
        }
    }

    public int getElementSize() {
        return elements.size();
    }

    public PrintContainer getElement(int index) {
        return elements.get(index);
    }

    private ArrayList<PrintContainer> getAncestorContainers(PrintContainer cont, int type) {
        ArrayList<PrintContainer> ancestors = new ArrayList<PrintContainer>();
        PrintContainer cont_parent = cont.getParentContainer();
        while (cont_parent != null) {
            if (cont_parent.getContainerType() == type) {
                ancestors.add(cont_parent);
            }
            cont_parent = cont_parent.getParentContainer();
        }
        return ancestors;
    }

    private String openTag(String ns, String tag, ArrayList<String> attributeNames, ArrayList<String> attributeValues) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n<");
        if (ns != null && ns.length() > 0) {
            sb.append(ns).append(":");
        }
        sb.append(tag);
        if (attributeNames != null && attributeValues != null && attributeNames.size() == attributeValues.size() && attributeNames.size() > 0) {
            for (int i = 0; i < attributeNames.size(); i++) {
                sb.append(" ").append(attributeNames.get(i)).append("=\"").append(attributeValues.get(i)).append("\"");
            }
        }
        sb.append(">\n");
        return sb.toString();
    }

    private String openTag(String ns, String tag, boolean close) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n<");
        if (close) {
            sb.append("/");
        }
        if (ns != null && ns.length() > 0) {
            sb.append(ns).append(":");
        }
        sb.append(tag).append(">\n");
        return sb.toString();
    }

    private String openTag(String ns, String tag) {
        return openTag(ns, tag, false);
    }

    private String closeTag(String ns, String tag) {
        return openTag(ns, tag, true);
    }

    public void print(BufferedOutputStream bos, String name_space) throws PrintContainerException {
        try {
            if (this.type == ctSEQUENCE) {
                bos.write(openTag(name_space, "sequence").getBytes());
                for (int i = 0; i < elements.size(); i++) {
                    elements.get(i).print(bos, name_space);
                }
                bos.write(closeTag(name_space, "sequence").getBytes());
                bos.flush();
            } else if (this.type == ctFLOW) {
                bos.write(openTag(name_space, "flow").getBytes());
                for (int i = 0; i < elements.size(); i++) {
                    elements.get(i).print(bos, name_space);
                }
                bos.write(closeTag(name_space, "flow").getBytes());
                bos.flush();
            } else if (this.type == ctEMPTY) {
                printEmpty(bos, name_space);
            } else if (this.type == ctSIMPLE) {
                if ("onmessage".equalsIgnoreCase(this.simple.getOperation())) {
                } else if ("pick".equalsIgnoreCase(this.simple.getOperation())) {
                } else if ("if".equalsIgnoreCase(this.simple.getOperation())) {
                } else if ("else".equalsIgnoreCase(this.simple.getOperation())) {
                } else if ("elseif".equalsIgnoreCase(this.simple.getOperation())) {
                } else {
                    String nodeText = nodeToString(this.simple.getOriginalNode());
                    bos.write(nodeText.getBytes());
                }
                bos.flush();
            } else if (this.type == ctPICK) {
                String str = "\n<" + this.getSimple().getOriginalNode().getNodeName() + ">\n";
                bos.write(str.getBytes());
                for (int i = 0; i < elements.size(); i++) {
                    elements.get(i).print(bos, name_space);
                }
                str = "\n</" + this.getSimple().getOriginalNode().getNodeName() + ">\n";
                bos.write(str.getBytes());
            } else if (this.type == ctONMSG) {
                String str = "\n<" + this.getSimple().getOriginalNode().getNodeName() + " ";
                bos.write(str.getBytes());
                Node node = this.simple.getOriginalNode();
                NamedNodeMap onmessage_attributes = node.getAttributes();
                int attr_count = onmessage_attributes.getLength();
                for (int iattr = 0; iattr < attr_count; iattr++) {
                    Node attribute = onmessage_attributes.item(iattr);
                    if (attribute.getNodeType() == Node.ATTRIBUTE_NODE) {
                        String nodeText = attribute.getNodeName() + "=\"" + attribute.getNodeValue() + "\" ";
                        bos.write(nodeText.getBytes());
                    }
                }
                bos.write(">\n".getBytes());
                NodeList onmsg_children = node.getChildNodes();
                for (int imsgch = 0; imsgch < onmsg_children.getLength(); imsgch++) {
                    Node item = onmsg_children.item(imsgch);
                    if (item.getNodeType() == Node.ELEMENT_NODE && item.getLocalName().equals("correlations")) {
                        String nodeText = nodeToString(item);
                        bos.write(nodeText.getBytes());
                    }
                }
                if (elements.size() == 1) {
                    elements.get(0).print(bos, name_space);
                } else {
                    for (int i = 0; i < elements.size(); i++) {
                        elements.get(i).print(bos, name_space);
                    }
                }
                str = "\n</" + this.getSimple().getOriginalNode().getNodeName() + ">\n";
                bos.write(str.getBytes());
            } else if (this.type == ctIF) {
                String str = "\n<" + this.getSimple().getOriginalNode().getNodeName() + " ";
                bos.write(str.getBytes());
                Node node = this.simple.getOriginalNode();
                NamedNodeMap if_attributes = node.getAttributes();
                int attr_count = if_attributes.getLength();
                for (int iattr = 0; iattr < attr_count; iattr++) {
                    Node attribute = if_attributes.item(iattr);
                    if (attribute.getNodeType() == Node.ATTRIBUTE_NODE) {
                        String nodeText = attribute.getNodeName() + "=\"" + attribute.getNodeValue() + "\" ";
                        bos.write(nodeText.getBytes());
                    }
                }
                bos.write(">\n".getBytes());
                NodeList if_children = node.getChildNodes();
                for (int imsgch = 0; imsgch < if_children.getLength(); imsgch++) {
                    Node item = if_children.item(imsgch);
                    if (item.getNodeType() == Node.ELEMENT_NODE && item.getLocalName().equals("condition")) {
                        String nodeText = nodeToString(item);
                        bos.write(nodeText.getBytes());
                    }
                }
                for (int i = 0; i < elements.size(); i++) {
                    elements.get(i).print(bos, name_space);
                }
                str = "\n</" + this.getSimple().getOriginalNode().getNodeName() + ">\n";
                bos.write(str.getBytes());
            } else if (this.type == ctELSE) {
                String str = "\n<" + this.getSimple().getOriginalNode().getNodeName() + ">\n";
                bos.write(str.getBytes());
                for (int i = 0; i < elements.size(); i++) {
                    elements.get(i).print(bos, name_space);
                }
                str = "\n</" + this.getSimple().getOriginalNode().getNodeName() + ">\n";
                bos.write(str.getBytes());
            } else {
                System.out.println(this.getContainerType());
                throw new PrintContainerException(PrintContainerException.pceWRONG_CONTAINER_TYPE + " bhsfvjs ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printEmpty(BufferedOutputStream bos, String name_space) throws IOException {
        StringBuffer sbEmpty = new StringBuffer();
        ArrayList<String> attributeNames = new ArrayList<String>();
        attributeNames.add("name");
        ArrayList<String> attributeValues = new ArrayList<String>();
        attributeValues.add("Empty");
        sbEmpty.append(openTag(name_space, "empty", attributeNames, attributeValues)).append(closeTag(name_space, "empty"));
        bos.write(sbEmpty.toString().getBytes());
    }

    private String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }
}
