package g3pd.gradep.virdgm;

import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class VirdProcLoader {

    String inputFile;

    Vector<VirdProcElem> virdProcElem;

    VirdGraph virdGraph;

    VirdNode virdNode;

    int[][] adjMatrix;

    int procID = 0;

    VirdProcLoader(String inputFile) {
        this.inputFile = inputFile;
        this.virdProcElem = new Vector<VirdProcElem>();
        virdGraph = new VirdGraph();
        try {
            openFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        createAdjMatrix();
    }

    public Vector getVirdProcElem() {
        return this.virdProcElem;
    }

    public int[][] getAdjMatrix() {
        return this.adjMatrix;
    }

    public VirdGraph getVirdGraph() {
        return this.virdGraph;
    }

    public void openFile() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(inputFile);
        Element elem = doc.getDocumentElement();
        NodeList nodelist = elem.getChildNodes();
        Node node = nodelist.item(1);
        Vector list = new Vector();
        parseFile(list, node);
    }

    public Object parseFile(Object previousProc, Node node) {
        if (getNodeAttribute(node, "repr").equals("conselem")) {
            String actionAttr = getNodeAttribute(node, "acao");
            String outputPosAttr = getNodeAttribute(node, "pos");
            String inputPosAttr = getNodeAttribute(node, "parametro");
            VirdProcElem virdProcElem = new VirdProcElem(actionAttr, outputPosAttr, inputPosAttr, procID);
            this.virdProcElem.add(virdProcElem);
            if (previousProc instanceof Integer) {
                Vector previous = new Vector();
                previous.add(previousProc);
                this.virdNode = new VirdNode(procID, previous);
            } else if (previousProc instanceof Vector) {
                Vector previous = new Vector();
                Vector aux = new Vector();
                for (int i = 0; i < ((Vector) previousProc).size(); i++) {
                    aux = (Vector) ((Vector) previousProc).get(i);
                    for (int j = 0; j < aux.size(); j++) previous.add(aux.get(j));
                }
                this.virdNode = new VirdNode(procID, previous);
            } else this.virdNode = new VirdNode(procID, previousProc);
            System.out.println(virdGraph);
            this.virdGraph.addNode(virdNode);
            procID++;
            return procID - 1;
        }
        if (getNodeAttribute(node, "repr").equals("env")) {
            if (getNodeAttribute(node, "tipo").equals("seq")) {
                Vector readNodes = new Vector();
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    if (Node.ELEMENT_NODE == childNodes.item(i).getNodeType()) {
                        readNodes.add(childNodes.item(i));
                    }
                }
                for (int i = 0; i < readNodes.size(); i++) {
                    if (getNodeAttribute((Node) readNodes.get(i), "repr").equals("terminicio") != true && getNodeAttribute((Node) readNodes.get(i), "repr").equals("termfim") != true) {
                        previousProc = parseFile(previousProc, (Node) readNodes.get(i));
                    }
                }
                return previousProc;
            }
            if (getNodeAttribute(node, "tipo").equals("paralelo")) {
                Vector readNodes = new Vector();
                NodeList filhos = node.getChildNodes();
                for (int i = 0; i < filhos.getLength(); i++) {
                    if (Node.ELEMENT_NODE == filhos.item(i).getNodeType()) readNodes.add(filhos.item(i));
                }
                Vector procList = new Vector();
                for (int i = 0; i < readNodes.size(); i++) procList.add(parseFile(previousProc, (Node) readNodes.get(i)));
                return procList;
            }
        }
        return -1;
    }

    public int[][] createAdjMatrix() {
        int idNode;
        Integer val;
        Vector adj;
        adjMatrix = new int[this.virdGraph.size()][this.virdGraph.size()];
        for (int i = 0; i < this.virdGraph.size(); i++) for (int j = 0; j < this.virdGraph.size(); j++) adjMatrix[i][j] = 0;
        for (int x = 0; x < this.virdGraph.size(); x++) {
            idNode = this.virdGraph.getNode(x).getId();
            adj = ((Vector) (this.virdGraph.getNode(x)).getAdj());
            for (int i = 0; i < adj.size(); i++) {
                val = (Integer) adj.get(i);
                adjMatrix[val][idNode] = 1;
            }
        }
        for (int i = 0; i < this.virdGraph.size(); i++) {
            for (int j = 0; j < this.virdGraph.size(); j++) System.out.print(adjMatrix[i][j]);
            System.out.println();
        }
        System.out.println(virdGraph);
        return adjMatrix;
    }

    private String getNodeAttribute(Node nodo, String nome) {
        for (int i = 0; i < nodo.getAttributes().getLength(); i++) if (nodo.getAttributes().item(i).getNodeName().equals(nome)) return nodo.getAttributes().item(i).getNodeValue();
        return "";
    }
}
