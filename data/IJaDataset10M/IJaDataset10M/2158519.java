package org.tripcom.distribution.querypreprocessor.rdfgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.tripcom.distribution.querypreprocessor.Util;

/**
 * 
 * a custom class for representing RDF graphs in order to perform
 * standard graph algorithms
 *
 */
public class RDFGraph {

    public HashMap<String, RDFNode> nodes;

    public Set<RDFNode> nodeSet;

    public List<RDFPredicate> predicates;

    public Set<String> cutSpaces;

    public RDFGraph() {
        nodes = new HashMap<String, RDFNode>();
        nodeSet = new HashSet<RDFNode>();
        predicates = new LinkedList<RDFPredicate>();
        cutSpaces = new HashSet<String>();
    }

    public void addNode(RDFNode n) {
        if (!nodes.containsKey(n.value)) {
            nodes.put(n.value, n);
            nodeSet.add(n);
        }
    }

    public RDFNode createNode(String s) {
        if (!nodes.containsKey(s)) addNode(new RDFNode(s));
        return nodes.get(s);
    }

    public RDFNode getNode(String s) {
        if (nodes.containsKey(s)) return nodes.get(s); else return null;
    }

    public boolean intersects(RDFGraph g) {
        Iterator<RDFPredicate> pit = predicates.iterator();
        Iterator<RDFPredicate> pit2 = g.predicates.iterator();
        while (pit.hasNext()) while (pit2.hasNext()) {
            RDFPredicate p1 = pit.next();
            RDFPredicate p2 = pit2.next();
            if (p1.subject.value == p2.subject.value && p1.value == p2.value && p1.object.value == p2.object.value) return true;
        }
        return false;
    }

    public boolean containsIsoPredicate(RDFPredicate p) {
        Iterator<RDFPredicate> pit = predicates.iterator();
        while (pit.hasNext()) {
            RDFPredicate p2 = pit.next();
            if (p.subject.value != p2.subject.value) continue;
            if (p.value != p2.value) continue;
            if (p.object.value != p2.object.value) continue;
            return true;
        }
        return false;
    }

    public void collectPredicates() {
        predicates = new LinkedList<RDFPredicate>();
        Iterator<RDFNode> it = nodeSet.iterator();
        while (it.hasNext()) {
            RDFNode n = it.next();
            Iterator<RDFPredicate> pIt = n.predicateList.iterator();
            while (pIt.hasNext()) {
                predicates.add(pIt.next());
            }
        }
    }

    public void doSpaceCut() {
        Iterator<RDFPredicate> it = predicates.iterator();
        cutSpaces = new HashSet<String>();
        cutSpaces.addAll(it.next().spaces);
        while (it.hasNext()) {
            Set<String> n = it.next().spaces;
            cutSpaces = Util.cutSets(cutSpaces, n);
        }
    }

    public boolean isConnected() {
        GraphTools.setAllUnvisited(this);
        Set<RDFNode> r = GraphTools.DFS(this, nodeSet.iterator().next());
        if (r.size() != nodeSet.size()) return false; else return true;
    }

    public int countVars() {
        int vars = 0;
        Iterator<RDFNode> nit = nodeSet.iterator();
        while (nit.hasNext()) if (nit.next().value.startsWith("?")) vars++;
        return vars;
    }

    public boolean isAdjactent(RDFGraph g) {
        for (RDFNode n1 : g.nodeSet) {
            for (RDFNode n2 : nodeSet) {
                if (n1.value.compareTo(n2.value) == 0) return true;
            }
        }
        return false;
    }

    public List<String> getCommonVars(RDFGraph g) {
        List<String> ret = new LinkedList<String>();
        for (RDFNode n1 : g.nodeSet) {
            for (RDFNode n2 : nodeSet) {
                if (n1.value.compareTo(n2.value) == 0) if (n1.value.charAt(0) == '?') ret.add(n1.value);
            }
        }
        return ret;
    }

    public void clonePredicate(RDFPredicate p) {
        RDFNode s = new RDFNode(p.subject.value);
        RDFNode o = new RDFNode(p.object.value);
        RDFPredicate p2 = new RDFPredicate(s, p.value, o);
        s.addPredicate(p2);
        o.addNeighbourSubject(s);
        p2.spaces.addAll(p.spaces);
        this.predicates.add(p2);
    }
}
