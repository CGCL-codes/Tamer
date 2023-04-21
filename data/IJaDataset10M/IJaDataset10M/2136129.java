package com.hp.hpl.jena.graph.query;

import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.util.CollectionFactory;
import com.hp.hpl.jena.util.iterator.*;
import java.util.*;

/**
    Incomplete class. Do not use.
*/
public class SimpleTreeQueryPlan implements TreeQueryPlan {

    private Graph pattern;

    private Graph target;

    public SimpleTreeQueryPlan(Graph target, Graph pattern) {
        this.target = target;
        this.pattern = pattern;
    }

    public Graph executeTree() {
        Graph result = Factory.createGraphMem();
        Set roots = getRoots(pattern);
        for (Iterator it = roots.iterator(); it.hasNext(); handleRoot(result, (Node) it.next(), CollectionFactory.createHashedSet())) {
        }
        return result;
    }

    private Iterator findFromTriple(Graph g, Triple t) {
        return g.find(asPattern(t.getSubject()), asPattern(t.getPredicate()), asPattern(t.getObject()));
    }

    private Node asPattern(Node x) {
        return x.isBlank() ? null : x;
    }

    private void handleRoot(Graph result, Node root, Set pending) {
        ClosableIterator it = pattern.find(root, null, null);
        if (!it.hasNext()) {
            absorb(result, pending);
            return;
        }
        while (it.hasNext()) {
            Triple base = (Triple) it.next();
            Iterator that = findFromTriple(target, base);
            while (that.hasNext()) {
                Triple x = (Triple) that.next();
                pending.add(x);
                handleRoot(result, base.getObject(), pending);
            }
        }
    }

    private void absorb(Graph result, Set triples) {
        for (Iterator it = triples.iterator(); it.hasNext(); result.add((Triple) it.next())) {
        }
        triples.clear();
    }

    public static Set getRoots(Graph pattern) {
        Set roots = CollectionFactory.createHashedSet();
        ClosableIterator sub = GraphUtil.findAll(pattern);
        while (sub.hasNext()) roots.add(((Triple) sub.next()).getSubject());
        ClosableIterator obj = GraphUtil.findAll(pattern);
        while (obj.hasNext()) roots.remove(((Triple) obj.next()).getObject());
        return roots;
    }
}
