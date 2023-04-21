package br.ufrj.dcc.engine;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

/** 
 * Draws the learning problem�s graph, accordingly to each case.
 * Should be improved later, lacks esthetic.
 * @author Pedro Rougemont
 * 
 */
public class GraphBuilder {

    public static JGraph drawDecisionTree(List<String[]> output) {
        GraphModel model = new DefaultGraphModel();
        JGraph graph = new JGraph(model);
        graph.setCloneable(true);
        graph.setInvokesStopCellEditing(true);
        graph.setJumpToDefaultPort(true);
        DefaultGraphCell[] cells = generateTree(output);
        graph.getGraphLayoutCache().insert(cells);
        return graph;
    }

    public static JGraph drawVersionSpaces(List<String[]>[] geS) {
        GraphModel model = new DefaultGraphModel();
        JGraph graph = new JGraph(model);
        graph.setCloneable(true);
        graph.setInvokesStopCellEditing(true);
        graph.setJumpToDefaultPort(true);
        DefaultGraphCell[] cells = generateReticulated(geS);
        graph.getGraphLayoutCache().insert(cells);
        return graph;
    }

    private static DefaultGraphCell[] generateTree(List<String[]> output) {
        int nodeTreeHeight = 1;
        int nodeVerticalDelta = 100;
        int rootNodeHorizontalPosition = 140;
        int nodeWidth = 60;
        int nodeHeight = 20;
        int nodeX = rootNodeHorizontalPosition;
        int nodeY = nodeVerticalDelta;
        Color nodeColor = Color.YELLOW;
        DefaultGraphCell[] cells;
        Map<String, DefaultGraphCell> nodes = new HashMap<String, DefaultGraphCell>();
        List<String> nodeNames = new Vector<String>();
        List<DefaultGraphCell> edges = new Vector<DefaultGraphCell>();
        Map<String, EdgeDetailsStruct> edgeDetailsBuffer = new HashMap<String, EdgeDetailsStruct>();
        long uniqueIdentifier = 0;
        Iterator<String[]> iterator = output.iterator();
        String[] line = iterator.next();
        nodes.put(line[0] + uniqueIdentifier, createVertex(line[0], nodeX, nodeY, nodeWidth, nodeHeight, nodeColor, false));
        nodeNames.add(line[0] + uniqueIdentifier);
        for (int i = 1; i < line.length; i++) {
            EdgeDetailsStruct edgeDetails = new EdgeDetailsStruct();
            edgeDetails.parentName = line[0] + uniqueIdentifier;
            edgeDetails.label = line[i];
            edgeDetails.numberOfBrothers = line.length - 1;
            edgeDetails.brotherIndex = i;
            edgeDetails.parentNodeTreeHeight = nodeTreeHeight;
            edgeDetails.parentX = nodeX;
            edgeDetails.parentY = nodeY;
            edgeDetailsBuffer.put(edgeDetails.label, edgeDetails);
        }
        uniqueIdentifier++;
        String previousNode = "";
        EdgeDetailsStruct edgeVo = null;
        while (edgeDetailsBuffer.size() > 0 && iterator.hasNext()) {
            line = iterator.next();
            if (line[0].equals(previousNode)) {
                for (int i = 1; i < line.length; i++) {
                    EdgeDetailsStruct edgeDetails = new EdgeDetailsStruct();
                    edgeDetails.parentName = line[0] + uniqueIdentifier;
                    edgeDetails.label = line[i];
                    edgeDetails.numberOfBrothers = line.length - 1;
                    edgeDetails.brotherIndex = i;
                    edgeDetails.parentNodeTreeHeight = edgeVo.parentNodeTreeHeight + 1;
                    edgeDetails.parentX = nodeX;
                    edgeDetails.parentY = nodeY;
                    edgeDetailsBuffer.put(edgeDetails.label, edgeDetails);
                }
                line = iterator.next();
            }
            uniqueIdentifier++;
            edgeVo = edgeDetailsBuffer.get(line[0]);
            nodeX = generateX(edgeVo.brotherIndex, edgeVo.numberOfBrothers, edgeVo.parentX, rootNodeHorizontalPosition, nodeWidth);
            nodeY = generateY(edgeVo.parentY, edgeVo.parentNodeTreeHeight, nodeVerticalDelta);
            nodes.put(line[1] + uniqueIdentifier, createVertex(line[1], nodeX, nodeY, nodeWidth, nodeHeight, nodeColor, false));
            nodeNames.add(line[1] + uniqueIdentifier);
            AttributeMap edgeAttributes = new AttributeMap();
            DefaultEdge edge = new DefaultEdge(edgeVo.label);
            GraphConstants.setLineEnd(edgeAttributes, GraphConstants.ARROW_CLASSIC);
            GraphConstants.setEndFill(edgeAttributes, true);
            edge.setSource(nodes.get(edgeVo.parentName).getChildAt(0));
            edge.setTarget(nodes.get(line[1] + uniqueIdentifier).getChildAt(0));
            edge.setAttributes(edgeAttributes);
            edges.add(edge);
            previousNode = line[1];
        }
        cells = new DefaultGraphCell[edges.size() + nodes.size()];
        int j;
        int size = nodeNames.size();
        for (j = 0; j < size; j++) {
            cells[j] = nodes.remove(nodeNames.get(j));
        }
        size = edges.size();
        for (int k = 0; k < size; k++) {
            cells[k + j] = edges.remove(0);
        }
        return cells;
    }

    private static int generateY(int pY, int pH, int delta) {
        return pY + delta;
    }

    private static int generateX(int i, int N, int pX, int delta, int W) {
        return i * pX - i * pX / N;
    }

    private static DefaultGraphCell[] generateReticulated(List<String[]>[] GeS) {
        List<NodeDetailsStruct> nodeDetails = new ArrayList<NodeDetailsStruct>();
        int gcount = GeS[0].size();
        List<DefaultGraphCell> tempCells = new ArrayList<DefaultGraphCell>();
        List<String[]> resultSet = GeS[0];
        int reticulatedHeight = hypothesisDiff(resultSet, GeS[1].get(0));
        int currentHeight = reticulatedHeight;
        do {
            resultSet = generateSubSet(resultSet, GeS[1].get(0));
            if (resultSet != null) {
                for (Iterator<String[]> iterator = resultSet.iterator(); iterator.hasNext(); ) {
                    String[] strings = iterator.next();
                    for (int i = 0; i < strings.length; i++) {
                        System.out.print(strings[i] + " ");
                    }
                    System.out.println("");
                }
                currentHeight = hypothesisDiff(resultSet, GeS[1].get(0));
                int k = 0;
                int resultSetSize = resultSet.size();
                for (int i = 0; i < resultSetSize; i++) {
                    String[] strings = resultSet.get(i);
                    String y = "";
                    for (int j = 0; j < strings.length; j++) {
                        y += strings[j] + " ";
                    }
                    nodeDetails.add(new NodeDetailsStruct(y, currentHeight, y.length() * 10 * k + 15, 20 + 100 * currentHeight, y.length() * 7, 20));
                    tempCells.add(createVertex(y, y.length() * 10 * k + 15, 20 + 100 * currentHeight, y.length() * 7, 20, new Color(0), false));
                    k++;
                }
            }
        } while (resultSet != null);
        DefaultGraphCell[] cells = new DefaultGraphCell[gcount + 1 + tempCells.size()];
        int k;
        for (k = 0; k < gcount; k++) {
            String y = "";
            String[] x = GeS[0].get(k);
            for (int j = 0; j < x.length; j++) {
                y += x[j] + " ";
            }
            nodeDetails.add(new NodeDetailsStruct(y, reticulatedHeight, y.length() * 10 * k + 15, 20 + 100 * reticulatedHeight, y.length() * 7, 20));
            cells[k] = createVertex(y, y.length() * 10 * k + 15, 20 + 100 * reticulatedHeight, y.length() * 7, 20, new Color(0), false);
        }
        String y = "";
        String[] x = GeS[1].get(0);
        for (int j = 0; j < x.length; j++) {
            y += x[j] + " ";
        }
        nodeDetails.add(new NodeDetailsStruct(y, reticulatedHeight, y.length() * 10 * (k - 1) / 2 - 15, 20, y.length() * 7, 20));
        cells[k] = createVertex(y, y.length() * 10 * (k - 1) / 2 - 15, 20, y.length() * 7, 20, new Color(0), false);
        int i = 0;
        List<NodeDetailsStruct> alreadyAddedNodes = new ArrayList<NodeDetailsStruct>();
        List<DefaultGraphCell> nodes = new ArrayList<DefaultGraphCell>();
        for (Iterator<NodeDetailsStruct> iterator = nodeDetails.iterator(); iterator.hasNext(); i++) {
            NodeDetailsStruct nd = iterator.next();
            if (!nd.amIAlreadyInThisList(alreadyAddedNodes)) {
                DefaultGraphCell novaCell = createVertex(nd.label, nd.x, nd.y, nd.width, nd.height, new Color(0), false);
                nodes.add(novaCell);
            }
            alreadyAddedNodes.add(nd);
        }
        i = 0;
        cells = new DefaultGraphCell[nodes.size()];
        for (Iterator<DefaultGraphCell> iterator = nodes.iterator(); iterator.hasNext(); ) {
            DefaultGraphCell defaultGraphCell = iterator.next();
            cells[i++] = defaultGraphCell;
        }
        return cells;
    }

    private static List<String[]> generateSubSet(List<String[]> oldResultSet, String[] strings) {
        List<String[]> resultSet = new ArrayList<String[]>();
        if (hypothesisDiff(oldResultSet, strings) <= 1) return null;
        for (Iterator<String[]> iterator = oldResultSet.iterator(); iterator.hasNext(); ) {
            List<Integer> markedPositions = new ArrayList<Integer>();
            String[] hypothesis = iterator.next();
            for (int i = 0; i < strings.length; i++) {
                if (!strings[i].equals(hypothesis[i])) {
                    markedPositions.add(i);
                    System.out.println(i);
                }
            }
            if (markedPositions.size() > 0) {
                for (Iterator<Integer> iterator2 = markedPositions.iterator(); iterator2.hasNext(); ) {
                    Integer posicao = iterator2.next();
                    resultSet.add(modifyXpositions(posicao, hypothesis, strings));
                }
            }
        }
        if (resultSet.size() == 0) return null;
        return resultSet;
    }

    private static String[] modifyXpositions(int posicao, String[] hypothesis, String[] strings) {
        String[] result = new String[hypothesis.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = hypothesis[i];
        }
        result[posicao] = strings[posicao];
        return result;
    }

    private static int hypothesisDiff(List<String[]> list, String[] list2) {
        Iterator<String[]> iterator = list.iterator();
        int diffs = 0;
        if (iterator.hasNext()) {
            String[] strings = iterator.next();
            for (int i = 0; i < strings.length; i++) {
                if (!strings[i].equals(list2[i])) diffs++;
            }
        }
        return diffs;
    }

    public static DefaultGraphCell createVertex(String name, double x, double y, double w, double h, Color bg, boolean raised) {
        DefaultGraphCell cell = new DefaultGraphCell(name);
        GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(x, y, w, h));
        if (bg != null) {
            GraphConstants.setGradientColor(cell.getAttributes(), Color.orange);
            GraphConstants.setOpaque(cell.getAttributes(), true);
        }
        if (raised) GraphConstants.setBorder(cell.getAttributes(), BorderFactory.createRaisedBevelBorder()); else GraphConstants.setBorderColor(cell.getAttributes(), Color.black);
        DefaultPort port = new DefaultPort();
        cell.add(port);
        port.setParent(cell);
        return cell;
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        List<String[]> output = new Vector<String[]>();
        frame.getContentPane().add(new JScrollPane(GraphBuilder.drawDecisionTree(output)));
        frame.pack();
        frame.setVisible(true);
    }

    private static class EdgeDetailsStruct {

        public String parentName;

        public String label;

        public int numberOfBrothers;

        public int brotherIndex;

        public int parentY;

        public int parentX;

        public int parentNodeTreeHeight;
    }

    private static class NodeDetailsStruct {

        public String label;

        public int nodeTreeHeight;

        public double x, y;

        public double width, height;

        public NodeDetailsStruct(String label, int nodeTreeHeight, double x, double y, double width, double height) {
            this.label = label;
            this.nodeTreeHeight = nodeTreeHeight;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean amIAlreadyInThisList(List<NodeDetailsStruct> list) {
            boolean repeated = false;
            for (Iterator<NodeDetailsStruct> iterator = list.iterator(); iterator.hasNext(); ) {
                NodeDetailsStruct nodeDetailsStruct = iterator.next();
                if (this.label.equals(nodeDetailsStruct.label)) repeated = true;
            }
            return repeated;
        }
    }
}
