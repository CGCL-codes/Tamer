package edu.iastate.fgmine.gspan;

import static edu.iastate.fgmine.util.Constants.NULL_INT;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import edu.iastate.fgmine.graph.Edge;
import edu.iastate.fgmine.graph.Graph;
import edu.iastate.fgmine.graph.Vertex;

public class SubgraphIsomorphismChecker {

    public static SubgraphIsomorphismChecker INSTANCE = new SubgraphIsomorphismChecker();

    private List<GSpanEdge> mPatternCode;

    private List<Vertex> mPatternVertices;

    private int[] mForwardEdgeIndicesInPatternCode;

    private List<Vertex> mGraphVertices;

    private Stack<Vertex> mDFSOrderInGraph = new Stack<Vertex>();

    private int[] mGraphVertexPos;

    private SubgraphIsomorphismChecker() {
    }

    public boolean check(DFSCode pattern, Graph graph) {
        mPatternCode = pattern.getCode();
        mPatternVertices = pattern.getGraph().getVertices();
        mForwardEdgeIndicesInPatternCode = pattern.getForwardEdgeIndices();
        mGraphVertices = graph.getVertices();
        mDFSOrderInGraph.clear();
        mGraphVertexPos = new int[mGraphVertices.size()];
        Arrays.fill(mGraphVertexPos, NULL_INT);
        final Vertex firstInPattern = mPatternVertices.get(0);
        for (int i = mGraphVertices.size() - 1; i >= 0; i--) {
            final Vertex v = mGraphVertices.get(i);
            if (!canMatch(firstInPattern, v)) continue;
            pushDFSVertex(v);
            if (hasPattern()) return true;
            popDFSVertex();
        }
        return false;
    }

    private boolean canMatch(Vertex vInPattern, Vertex vInGraph) {
        return vInPattern.getLabel() == vInGraph.getLabel();
    }

    private boolean hasPattern() {
        if (mDFSOrderInGraph.size() > 2 && !hasBackwardEdgePattern()) return false;
        if (mDFSOrderInGraph.size() == mPatternVertices.size()) return true;
        final int forwardEdgePos = mDFSOrderInGraph.size() - 1;
        final int forwardEdgeIndexInPattern = mForwardEdgeIndicesInPatternCode[forwardEdgePos];
        final GSpanEdge forwardEdgeInPattern = mPatternCode.get(forwardEdgeIndexInPattern);
        final Vertex trgInPattern = mPatternVertices.get(forwardEdgeInPattern.getTrgIndex());
        final Vertex srcInGraph = mDFSOrderInGraph.get(forwardEdgeInPattern.getSrcIndex());
        final List<Edge> es = srcInGraph.getEdges();
        for (int i = es.size() - 1; i >= 0; i--) {
            final Vertex trgInGraph = es.get(i).getTrg();
            final int trgIndexInGraph = trgInGraph.getIndex();
            final int trgPosInGraph = mGraphVertexPos[trgIndexInGraph];
            if (trgPosInGraph == NULL_INT && canMatch(trgInPattern, trgInGraph)) {
                pushDFSVertex(trgInGraph);
                if (hasPattern()) return true;
                popDFSVertex();
            }
        }
        return false;
    }

    private boolean hasBackwardEdgePattern() {
        final Vertex srcInGraph = mDFSOrderInGraph.peek();
        final int nextForwardEdgePos = mDFSOrderInGraph.size() - 1;
        final int thisForwardEdgePos = nextForwardEdgePos - 1;
        final int iBegin = mForwardEdgeIndicesInPatternCode[thisForwardEdgePos] + 1;
        final int iEnd = mForwardEdgeIndicesInPatternCode[nextForwardEdgePos];
        for (int i = iBegin; i < iEnd; i++) {
            final int trgDFSOrderInPattern = mPatternCode.get(i).getTrgIndex();
            final Vertex trgInGraph = mDFSOrderInGraph.get(trgDFSOrderInPattern);
            if (srcInGraph.getEdge(trgInGraph) == null) return false;
        }
        return true;
    }

    private void pushDFSVertex(Vertex v) {
        final int index = v.getIndex();
        mGraphVertexPos[index] = mDFSOrderInGraph.size();
        mDFSOrderInGraph.push(v);
    }

    private void popDFSVertex() {
        final Vertex vPop = mDFSOrderInGraph.pop();
        mGraphVertexPos[vPop.getIndex()] = NULL_INT;
    }
}
