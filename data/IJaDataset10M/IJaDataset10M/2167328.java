package org.snipsnap.graph.calculation;

import java.util.*;
import org.snipsnap.graph.context.*;
import org.snipsnap.graph.dimensions.*;
import org.snipsnap.graph.dimensions.rec.*;
import org.snipsnap.graph.graph.*;

public class CalculateDirectedAcyclicGraph implements Calculate {

    private DirectedGraph directedGraph;

    private GraphRendererContext context;

    private int oldRow = 1;

    private int oldYValue = 10;

    private int oldXValue = 10;

    private HashMap nodeList;

    private Dim recDim[];

    private int lastY[];

    public CalculateDirectedAcyclicGraph(RendererContext context, DirectedGraph directedGraph) {
        this.directedGraph = directedGraph;
        this.context = (GraphRendererContext) context;
    }

    public void calculateGraph() {
        nodeList = new HashMap();
        lastY = new int[directedGraph.getDepth() + 1];
        recDim = new ConverterForDirectedAcyclicGraph(directedGraph).convertNodeDimToRecDim((GraphNode) directedGraph.getRoot(), 0);
        iterateForCalculateGraph((GraphNode) directedGraph.getRoot(), 0);
    }

    private void iterateForCalculateGraph(GraphNode node, int row) {
        ArrayList nodelist = node.getChildrenList();
        if (!node.equals(directedGraph.getRoot()) && !(node.equals(nodeList.get(node.getName())))) {
            int yvalue = getYValue(row, node);
            int xvalue = getXValue(row, node);
            node.setX(xvalue);
            node.setY(yvalue);
            oldRow = row;
            lastY[row] = yvalue + node.getHeight();
        }
        nodeList.put(node.getName(), node);
        Iterator it = nodelist.iterator();
        while (it.hasNext()) {
            node = (GraphNode) (it.next());
            iterateForCalculateGraph(node, row + 1);
        }
    }

    private int getYValue(int row, GraphNode node) {
        if (oldRow > row || (oldRow == row)) {
            int y = getLastY(row) + ((PicInfo) context.getPicInfo()).getDSameRow();
            oldYValue = y;
            return y;
        }
        return oldYValue;
    }

    private int getXValue(int row, GraphNode node) {
        int level = node.getLevel();
        int x = 10 + (level - 1) * ((PicInfo) context.getPicInfo()).getDBetweenRows();
        for (int i = 1; i < level; i++) {
            x = x + recDim[i].getWidth();
        }
        return x;
    }

    private int getLastY(int row) {
        int maxY = 10;
        for (int i = row; i <= oldRow; i++) {
            if (maxY < lastY[i]) {
                maxY = lastY[i];
            }
        }
        return maxY;
    }
}
