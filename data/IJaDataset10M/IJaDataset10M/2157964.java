package org.snipsnap.graph.dimensions.rec;

import java.util.*;
import java.awt.*;
import org.snipsnap.graph.context.*;
import org.snipsnap.graph.dimensions.*;
import org.snipsnap.graph.graph.*;
import org.snipsnap.graph.renderer.*;

public class RecDimCalculationDirectedAcyclicGraph extends RecDimCalculation {

    private int oldRow = 1;

    private FontInfo fontInfo;

    private DirectedGraph directedGraph;

    private ImageGraphRenderer renderer;

    private Graphics2D g2;

    private boolean fontDecrease;

    private HashMap nodeMap = new HashMap();

    public RecDimCalculationDirectedAcyclicGraph(RendererContext context, DirectedGraph directedGraph, Renderer renderer) {
        super(context, directedGraph, renderer);
        this.fontInfo = ((FontInfo) context.getFontInfo());
        this.g2 = ((GraphRendererContext) context).getGraphics();
        this.directedGraph = directedGraph;
        this.renderer = (ImageGraphRenderer) renderer;
    }

    public void calculateRecDims(boolean fontDecrease) {
        setLevel((GraphNode) directedGraph.getRoot(), 0);
        Dim[] recDim = calculate(fontDecrease);
        for (int i = 0; i < graph.getDepth(); i++) {
            recDim[i].setWidth(recDim[i].getWidth() + 8);
        }
        ConverterForDirectedAcyclicGraph convert = new ConverterForDirectedAcyclicGraph(directedGraph);
        convert.convertRecDimToNodeDim(recDim);
    }

    private void setLevel(GraphNode node, int row) {
        if (node.getLevel() <= row) {
            node.setLevel(row);
        }
        ArrayList nodelist = node.getChildrenList();
        Iterator it = nodelist.iterator();
        while (it.hasNext()) {
            node = (GraphNode) (it.next());
            setLevel(node, row + 1);
        }
    }

    public Dim[] iterateForRecDim(Node node, int row, Dim[] recDim, boolean fontDecrease) {
        iterateForRecWid(node, row, recDim, fontDecrease);
        iterateForRecHei(node, fontDecrease);
        return recDim;
    }

    public void iterateForRecHei(Node node, boolean fontDecrease) {
        int level = ((GraphNode) node).getLevel();
        ArrayList nodelist = node.getChildrenList();
        int recHeiRow = this.getRecHeiRow(node, level);
        Iterator it = nodelist.iterator();
        while (it.hasNext()) {
            node = (Node) (it.next());
            iterateForRecHei(node, fontDecrease);
        }
    }

    public Dim[] iterateForRecWid(Node node, int row, Dim[] recDim, boolean fontDecrease) {
        int level = ((GraphNode) node).getLevel();
        ArrayList nodelist = node.getChildrenList();
        int recWidRow = this.getRecWidRow(node, level);
        recDim[((GraphNode) node).getLevel()] = new Dim(recWidRow + 5, 0);
        Iterator it = nodelist.iterator();
        while (it.hasNext()) {
            node = (Node) (it.next());
            iterateForRecWid(node, row + 1, recDim, fontDecrease);
        }
        return recDim;
    }

    public int getRecHeiRow(Node node, int row) {
        FontMetrics fm = g2.getFontMetrics(fontInfo.getFont());
        int recHei = (int) (fm.getAscent() * (2.5 + node.getNameAttributes().size()));
        int lengthOfNodeName = fm.stringWidth(node.getName());
        if (lengthOfNodeName - 8 > recWid[((GraphNode) node).getLevel()] && row >= 1) {
            recHei = recHei + fm.getAscent();
        }
        node.setHeight(recHei);
        return recHei;
    }
}
