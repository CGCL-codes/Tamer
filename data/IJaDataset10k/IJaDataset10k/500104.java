package stixar.graph.paths;

import stixar.graph.Algorithm;
import stixar.graph.Filtering;
import stixar.graph.Node;
import stixar.graph.Edge;
import stixar.graph.Digraph;
import stixar.graph.attr.NativeMap;
import stixar.graph.attr.NativeNodeMap;
import stixar.graph.attr.NativeEdgeMap;
import stixar.graph.attr.IntNodeMap;
import stixar.graph.attr.FloatNodeMap;
import stixar.graph.attr.DoubleNodeMap;
import stixar.graph.attr.LongNodeMap;
import stixar.graph.attr.IntEdgeMap;
import stixar.graph.attr.FloatEdgeMap;
import stixar.graph.attr.DoubleEdgeMap;
import stixar.graph.attr.LongEdgeMap;
import stixar.graph.attr.NodeMap;
import stixar.graph.attr.ArrayNodeMap;
import stixar.graph.attr.EdgeMap;
import stixar.graph.attr.EdgeSource;
import stixar.util.CList;
import stixar.util.ListCell;
import java.util.BitSet;
import java.util.Arrays;

/**
   Tarjan's subtree disassembly variant of the Bellman-Ford-Moore
   algorithm for natively attributed digraphs.
 */
public class BFMNative extends BFMBase {

    protected NativeEdgeMap weights;

    protected NativeNodeMap distMap;

    protected IntNodeMap intNodeMap;

    protected IntEdgeMap intEdgeMap;

    protected LongNodeMap longNodeMap;

    protected LongEdgeMap longEdgeMap;

    protected FloatNodeMap floatNodeMap;

    protected FloatEdgeMap floatEdgeMap;

    protected DoubleNodeMap doubleNodeMap;

    protected DoubleEdgeMap doubleEdgeMap;

    public BFMNative(Digraph g, Node s, NativeEdgeMap weights, NativeNodeMap distMap, NodeMap<Edge> predMap) {
        super(g, s, predMap);
        this.weights = weights;
        this.distMap = distMap;
    }

    public void run() {
        reset();
        NodeInfo ni = source.get(niA);
        ni.cell = queue.append(ni);
        switch(distMap.type()) {
            case Int:
                while (!queue.isEmpty()) {
                    ni = queue.remove();
                    if ((cycleEdge = scanInt(ni)) != null) break;
                }
                break;
            case Long:
                while (!queue.isEmpty()) {
                    ni = queue.remove();
                    if ((cycleEdge = scanLong(ni)) != null) break;
                }
                break;
            case Float:
                while (!queue.isEmpty()) {
                    ni = queue.remove();
                    if ((cycleEdge = scanFloat(ni)) != null) break;
                }
                break;
            case Double:
                while (!queue.isEmpty()) {
                    ni = queue.remove();
                    if ((cycleEdge = scanDouble(ni)) != null) break;
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }

    protected Edge scanInt(NodeInfo sInfo) {
        Node s = sInfo.node;
        int sid = s.nodeId();
        for (Edge e = s.out(); e != null; e = e.next()) {
            if (filter != null && filter.filter(e)) {
                continue;
            }
            Node t = e.target();
            NodeInfo tInfo = t.get(niA);
            int newTDist = intNodeMap.get(s) + intEdgeMap.get(e);
            if (newTDist < intNodeMap.get(t)) {
                int tid = t.nodeId();
                intNodeMap.set(t, newTDist);
                Edge nc = disassemble(sInfo, tInfo, e);
                if (nc != null) return nc;
            }
        }
        return null;
    }

    protected Edge scanLong(NodeInfo sInfo) {
        Node s = sInfo.node;
        int sid = s.nodeId();
        for (Edge e = s.out(); e != null; e = e.next()) {
            if (filter != null && filter.filter(e)) {
                continue;
            }
            Node t = e.target();
            NodeInfo tInfo = t.get(niA);
            long newTDist = longNodeMap.get(s) + longEdgeMap.get(e);
            if (newTDist < longNodeMap.get(t)) {
                int tid = t.nodeId();
                longNodeMap.set(t, newTDist);
                Edge nc = disassemble(sInfo, tInfo, e);
                if (nc != null) return nc;
            }
        }
        return null;
    }

    protected Edge scanFloat(NodeInfo sInfo) {
        Node s = sInfo.node;
        int sid = s.nodeId();
        for (Edge e = s.out(); e != null; e = e.next()) {
            if (filter != null && filter.filter(e)) {
                continue;
            }
            Node t = e.target();
            NodeInfo tInfo = t.get(niA);
            float newTDist = floatNodeMap.get(s) + floatEdgeMap.get(e);
            if (newTDist < floatNodeMap.get(t)) {
                int tid = t.nodeId();
                floatNodeMap.set(t, newTDist);
                Edge nc = disassemble(sInfo, tInfo, e);
                if (nc != null) return nc;
            }
        }
        return null;
    }

    protected Edge scanDouble(NodeInfo sInfo) {
        Node s = sInfo.node;
        int sid = s.nodeId();
        for (Edge e = s.out(); e != null; e = e.next()) {
            if (filter != null && filter.filter(e)) {
                continue;
            }
            Node t = e.target();
            NodeInfo tInfo = t.get(niA);
            double newTDist = doubleNodeMap.get(s) + doubleEdgeMap.get(e);
            if (newTDist < doubleNodeMap.get(t)) {
                int tid = t.nodeId();
                doubleNodeMap.set(t, newTDist);
                Edge nc = disassemble(sInfo, tInfo, e);
                if (nc != null) return nc;
            }
        }
        return null;
    }

    protected void reset() {
        super.reset();
        switch(distMap.type()) {
            case Int:
                intNodeMap = (IntNodeMap) distMap;
                intEdgeMap = (IntEdgeMap) weights;
                for (Node n : digraph.nodes()) if (n == source) intNodeMap.set(n, 0); else intNodeMap.set(n, Integer.MAX_VALUE);
                break;
            case Long:
                longNodeMap = (LongNodeMap) distMap;
                longEdgeMap = (LongEdgeMap) weights;
                for (Node n : digraph.nodes()) if (n == source) longNodeMap.set(n, 0); else longNodeMap.set(n, Long.MAX_VALUE);
                break;
            case Float:
                floatNodeMap = (FloatNodeMap) distMap;
                floatEdgeMap = (FloatEdgeMap) weights;
                for (Node n : digraph.nodes()) if (n == source) floatNodeMap.set(n, 0); else floatNodeMap.set(n, Float.POSITIVE_INFINITY);
                break;
            case Double:
                doubleNodeMap = (DoubleNodeMap) distMap;
                doubleEdgeMap = (DoubleEdgeMap) weights;
                for (Node n : digraph.nodes()) if (n == source) doubleNodeMap.set(n, 0); else doubleNodeMap.set(n, Double.POSITIVE_INFINITY);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
