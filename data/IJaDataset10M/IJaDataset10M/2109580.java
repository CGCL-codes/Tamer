package vizz3d_data.layouts;

import grail.interfaces.DirectedGraphInterface;
import grail.interfaces.DirectedNodeInterface;
import grail.interfaces.GraphInterface;
import grail.interfaces.NodeInterface;
import grail.interfaces.UndirectedGraphInterface;
import grail.iterators.NodeIterator;
import grail.properties.GraphProperties;
import visgraph.visualgraph.interfaces.VisualGraphInterface;
import visgraph.visualgraph.interfaces.VisualNodeInterface;
import visgraph.visualgraph.iterators.VisualNodeIterator;
import vizz3d.common.abstractClasses.LayoutEngine;
import vizz3d.common.interfaces.AnimateInterface;
import vizz3d.common.interfaces.LayoutInterface;
import vizz3d.util.LayoutDebug;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JOptionPane;

/**
 * Leveller Layout Algorithm
 *
 * @author Thomas Panas
 */
public class Leveller extends LayoutEngine implements LayoutInterface {

    private int animations = 10;

    private HashSet roots = null;

    private HashSet roots2 = null;

    private HashSet roots3 = null;

    private HashSet done = null;

    private HashSet notdone = null;

    private HashSet allNodes = null;

    private HashSet changedCoord = null;

    private int orderedNodes = 1;

    private int hightchange = 10;

    private int circleDiv = 30;

    private double circleDivDouble = 3;

    private int rootPosHight = 20;

    private int radiusMax = 50;

    private int facFirst = 10;

    private double facFirstDouble = 0.01;

    private int d2d3 = 0;

    private GraphInterface dataGraph;

    double deltaPackages = 0;

    double thetaPackages = 0;

    /**
     * Creates a new instance of Leveller
     */
    public Leveller() {
        super("Leveller", "Nodes in different levels (depending on type)");
    }

    public void reLayout() {
        retrieve();
        doLayout();
    }

    public void retrieve() {
        getArguments("Leveller");
        hightchange = Integer.valueOf(getArgument("Leveller", "Hight")).intValue();
        circleDiv = Integer.valueOf(getArgument("Leveller", "Level Division")).intValue();
        circleDivDouble = (double) circleDiv / (double) 10;
        radiusMax = Integer.valueOf(getArgument("Leveller", "Radius")).intValue();
        facFirst = Integer.valueOf(getArgument("Leveller", "Level Fac")).intValue();
        facFirstDouble = (double) facFirst / (double) 1000;
        d2d3 = Integer.valueOf(getArgument("Leveller", "2D3D")).intValue();
        animations = Integer.valueOf(getArgument("Leveller", "Animation")).intValue();
        System.err.println("circleDouble:" + circleDivDouble + "   fac:" + facFirstDouble);
    }

    public void init(VisualGraphInterface graph, AnimateInterface ani) {
        dataGraph = vizzGUI.getPlugIn().getDataGraph();
        anim = ani;
        currentGraph = graph;
        if (!graphsInit.contains(graph)) {
            graphsInit.add(graph);
            double amountNodes = graph.nodesSize();
            radiusMax = Math.round((float) (amountNodes / 10 * 2));
            if (circleDiv > 50) circleDiv = 50;
            setArgument("Leveller", "Hight", hightchange, 0, 100, true);
            setArgument("Leveller", "Level Division", circleDiv, 0, 50, true);
            setArgument("Leveller", "Radius", radiusMax, 0, radiusMax, true);
            setArgument("Leveller", "Level Fac", facFirst, 0, 100, true);
            setArgument("Leveller", "2D3D", d2d3, 0, 1, true);
            setArgument("Leveller", "Animation", animations, 0, 100, true);
            setArgument("Leveller", "Description", "Leveller Algorithm by\nThomas Panas," + "\nVaxjo University. 25Jul2004.\n\nUsage:\nHight: Length of Edges\n" + "Level Division: Stretch Level on all Levels except first\n" + "Radius: Root Radius\nLevel Fac: Stretch Level on first Level\n" + "2D3D: Collapse to 2D\nAnimation: Animation steps");
        } else retrieve();
        showDialog("Leveller", this);
    }

    public synchronized boolean doLayout() {
        stopButton.setVisible(this);
        super.runThread();
        return true;
    }

    @Override
    public synchronized void run() {
        while (true) {
            level(currentGraph);
            anim.animate(animations);
            stopButton.setVisible(false);
            super.pauseThread();
        }
    }

    private void level(VisualGraphInterface graph) {
        if (dataGraph instanceof UndirectedGraphInterface) {
            JOptionPane.showMessageDialog(dialog, "Sorry, this layout algorithm does not\n" + "work for undirected graphs.", "Layout Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        roots = new HashSet();
        roots2 = new HashSet();
        roots3 = new HashSet();
        done = new HashSet();
        allNodes = new HashSet();
        changedCoord = new HashSet();
        VisualNodeIterator ni = graph.sceneGraphNodes();
        int minusPack = 0;
        while (ni.hasNext()) {
            VisualNodeInterface node = ni.next();
            if (stopMe) {
                stopMe = false;
                return;
            }
            if (!node.isVisible()) continue;
            int nrClasses = 0;
            NodeInterface dataNode = graph.getNode(node);
            nrClasses = ((DirectedGraphInterface) dataGraph).inDegree((DirectedNodeInterface) dataNode);
            int out = ((DirectedGraphInterface) dataGraph).outDegree((DirectedNodeInterface) node.getDataNode());
            if (nrClasses == 0) {
                roots.add(node);
                roots2.add(node);
                roots3.add(node);
                if (out == 0) minusPack++;
            }
        }
        deltaPackages = (2 * Math.PI) / (roots.size() - minusPack);
        thetaPackages = 0;
        Iterator it = roots.iterator();
        String name = null;
        while (it.hasNext()) {
            VisualNodeInterface node = (VisualNodeInterface) it.next();
            if (stopMe) {
                stopMe = false;
                return;
            }
            if (done.contains(node)) continue;
            if (!node.isVisible()) continue;
            layoutroot(node, graph);
            done.add(node);
            name = (String) node.getDataNode().getProperty(GraphProperties.LABEL);
            if (name == null) name = "";
            int pos = -1;
            if (name != null) pos = name.indexOf(".");
            String name2dot = name;
            if (pos != -1) {
                name = name.substring(0, pos);
                name2dot = name2dot.substring(pos + 1, name2dot.length());
            }
            pos = -1;
            if (name2dot != null) pos = name2dot.indexOf(".");
            if (pos != -1) {
                name2dot = name2dot.substring(0, pos);
                name2dot = name + "." + name2dot;
            }
            notdone = new HashSet();
            Iterator it2 = roots2.iterator();
            while (it2.hasNext()) {
                VisualNodeInterface nodeNext = (VisualNodeInterface) it2.next();
                if (done.contains(nodeNext)) continue;
                String name2 = (String) nodeNext.getDataNode().getProperty(GraphProperties.LABEL);
                int pos2 = -1;
                if (name2 != null) {
                    pos2 = name2.indexOf(".");
                } else name2 = "";
                if (pos2 != -1) name2 = name2.substring(0, pos2);
                if (name.equals(name2)) {
                    DirectedGraphInterface dg = (DirectedGraphInterface) graph.getDataGraph();
                    NodeIterator it3 = dg.getSuccessors((DirectedNodeInterface) nodeNext.getDataNode());
                    while (it3.hasNext()) {
                        it3.next();
                        NodeInterface nc = it3.getNode();
                        boolean doneAdd = false;
                        NodeIterator itr = dg.getPredecessors((DirectedNodeInterface) nc);
                        while (itr.hasNext()) {
                            itr.next();
                            NodeInterface nodeRR = itr.getNode();
                            VisualNodeInterface nodeR = graph.getNode(nodeRR);
                            if (done.contains(nodeR)) continue;
                            String nameR = (String) nodeRR.getProperty(GraphProperties.LABEL);
                            pos = nameR.indexOf(".");
                            String nameRR = "";
                            if (pos != -1) {
                                nameRR = nameR.substring(0, pos);
                                nameR = nameR.substring(pos + 1, nameR.length());
                            }
                            pos = nameR.indexOf(".");
                            if (pos != -1) {
                                nameR = nameR.substring(0, pos);
                                nameR = nameRR + "." + nameR;
                            }
                            if (name2dot.equals(nameR)) {
                                if (!done.contains(nodeR)) {
                                    if (roots.contains(nodeR)) {
                                        layoutroot(nodeR, graph);
                                        done.add(nodeR);
                                        doneAdd = true;
                                    }
                                }
                            }
                        }
                        if (!doneAdd) notdone.add(graph.getNode(nc));
                    }
                }
            }
            Iterator itndone = roots3.iterator();
            while (itndone.hasNext()) {
                VisualNodeInterface nodeNext2 = (VisualNodeInterface) itndone.next();
                if (notdone.contains(nodeNext2)) {
                    if (!done.contains(nodeNext2)) {
                        if (roots.contains(nodeNext2)) {
                            layoutroot(nodeNext2, graph);
                            done.add(nodeNext2);
                        }
                    }
                }
            }
        }
        VisualNodeIterator it2 = graph.sceneGraphNodes();
        while (it2.hasNext()) {
            VisualNodeInterface node = it2.next();
            if (!node.isVisible()) continue;
            if (!changedCoord.contains(node)) {
                LayoutDebug.printlnErrMessage("Node was not layed out : " + node);
                layoutClasses(graph, node, 0, 0, 0, radiusMax * (facFirstDouble));
            }
        }
    }

    private void layoutroot(VisualNodeInterface node, VisualGraphInterface graph) {
        changedCoord.add(node);
        double packdX = 0;
        double packdY = rootPosHight;
        double packdZ = 0;
        if (roots.size() != 1) {
            if (d2d3 == 1) {
                packdX = (thetaPackages * radiusMax) / 2;
                packdY = rootPosHight;
                packdZ = 0;
            } else {
                packdX = radiusMax * Math.cos(thetaPackages);
                packdY = rootPosHight;
                packdZ = radiusMax * Math.sin(thetaPackages);
            }
        }
        int out = ((DirectedGraphInterface) dataGraph).outDegree((DirectedNodeInterface) node.getDataNode());
        if (out == 0) node.setFinalPosition((float) 0, (float) packdY, (float) 0); else {
            node.setFinalPosition((float) packdX, (float) packdY, (float) packdZ);
            thetaPackages += deltaPackages;
            layoutClasses(graph, node, packdX, packdY - hightchange, packdZ, radiusMax * (facFirstDouble));
        }
    }

    private void layoutClasses(VisualGraphInterface graph, VisualNodeInterface packNode, double dX, double dY, double dZ, double fac) {
        double thetaClasses = 0.0;
        VisualNodeInterface[] list = minToMaxChildesPerNodeSort(graph, packNode, orderedNodes);
        double radiusClass = list.length;
        double deltaClass = (2 * Math.PI) / radiusClass;
        if (dataGraph.isDirected()) {
            for (int i = 0; i < list.length; i++) {
                VisualNodeInterface classNode = list[i];
                if (classNode == null) continue;
                if (!classNode.isVisible()) continue;
                double classdX = dX + (radiusClass * Math.cos(thetaClasses) * fac);
                double classdY = dY;
                double classdZ = 0;
                if (d2d3 == 1) classdZ = 0; else classdZ = dZ + (radiusClass * Math.sin(thetaClasses) * fac);
                if (!changedCoord.contains(classNode)) {
                    changedCoord.add(classNode);
                    thetaClasses += deltaClass;
                    classNode.setFinalPosition((float) classdX, (float) classdY, (float) classdZ);
                }
                NodeInterface dataNode = graph.getNode(classNode);
                if (((DirectedGraphInterface) dataGraph).outDegree((DirectedNodeInterface) dataNode) > 0) {
                    if (!allNodes.contains(classNode)) {
                        allNodes.add(classNode);
                        layoutClasses(graph, classNode, classdX, classdY - hightchange, classdZ, fac / (circleDivDouble));
                    } else {
                    }
                }
            }
        }
    }

    private VisualNodeInterface[] minToMaxChildesPerNodeSort(VisualGraphInterface graph, VisualNodeInterface packNode, int sorted) {
        NodeIterator nod = null;
        NodeInterface dataNode = graph.getNode(packNode);
        int childrenParent = ((DirectedGraphInterface) dataGraph).outDegree((DirectedNodeInterface) dataNode);
        VisualNodeInterface[] list = new VisualNodeInterface[childrenParent];
        int i = 0;
        if (dataGraph.isDirected()) {
            nod = ((DirectedGraphInterface) dataGraph).getSuccessors((DirectedNodeInterface) dataNode);
            while (nod.hasNext()) {
                nod.next();
                NodeInterface datNode = nod.getNode();
                VisualNodeInterface classNode = graph.getNode(datNode);
                if (classNode == null) continue;
                if (!classNode.isVisible()) continue;
                list[i] = classNode;
                i++;
            }
        }
        int max = i;
        int[] listAmount = null;
        if ((sorted == 1) && dataGraph.isDirected()) {
            listAmount = new int[max];
            for (int c = 0; c < max; c++) {
                VisualNodeInterface classNode = list[c];
                NodeInterface dNode = graph.getNode(classNode);
                listAmount[c] = ((DirectedGraphInterface) dataGraph).outDegree((DirectedNodeInterface) dNode);
            }
            for (int k = 0; k < (max - 1); k++) {
                for (int j = (k + 1); j < max; j++) {
                    if (listAmount[j] < listAmount[k]) {
                        int temp = listAmount[k];
                        listAmount[k] = listAmount[j];
                        listAmount[j] = temp;
                        VisualNodeInterface tempNode = list[k];
                        list[k] = list[j];
                        list[j] = tempNode;
                    }
                }
            }
        }
        return list;
    }
}
