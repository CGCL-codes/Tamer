package com.hp.hpl.jena.mem.faster;

import java.util.*;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.graph.Triple.Field;
import com.hp.hpl.jena.graph.impl.TripleStore;
import com.hp.hpl.jena.graph.query.*;
import com.hp.hpl.jena.mem.*;

public class FasterTripleStore extends GraphTripleStoreBase implements TripleStore {

    public FasterTripleStore(Graph parent) {
        super(parent, new NodeToTriplesMapFaster(Field.getSubject, Field.getPredicate, Field.getObject), new NodeToTriplesMapFaster(Field.getPredicate, Field.getObject, Field.getSubject), new NodeToTriplesMapFaster(Field.getObject, Field.getSubject, Field.getPredicate));
    }

    public NodeToTriplesMapFaster getSubjects() {
        return (NodeToTriplesMapFaster) subjects;
    }

    protected NodeToTriplesMapFaster getObjects() {
        return (NodeToTriplesMapFaster) objects;
    }

    public Applyer createApplyer(ProcessedTriple pt) {
        if (pt.hasNoVariables()) return containsApplyer(pt);
        if (pt.S instanceof QueryNode.Fixed) return getSubjects().createFixedSApplyer(pt);
        if (pt.O instanceof QueryNode.Fixed) return getObjects().createFixedOApplyer(pt);
        if (pt.S instanceof QueryNode.Bound) return getSubjects().createBoundSApplyer(pt);
        if (pt.O instanceof QueryNode.Bound) return getObjects().createBoundOApplyer(pt);
        return varSvarOApplyer(pt);
    }

    protected Applyer containsApplyer(final ProcessedTriple pt) {
        return new Applyer() {

            public void applyToTriples(Domain d, Matcher m, StageElement next) {
                Triple t = new Triple(pt.S.finder(d), pt.P.finder(d), pt.O.finder(d));
                if (objects.containsBySameValueAs(t)) next.run(d);
            }
        };
    }

    protected Applyer varSvarOApplyer(final QueryTriple pt) {
        return new Applyer() {

            protected final QueryNode p = pt.P;

            public Iterator find(Domain d) {
                Node P = p.finder(d);
                if (P.isConcrete()) return predicates.iterator(P, Node.ANY, Node.ANY); else return subjects.iterateAll();
            }

            public void applyToTriples(Domain d, Matcher m, StageElement next) {
                Iterator it = find(d);
                while (it.hasNext()) if (m.match(d, (Triple) it.next())) next.run(d);
            }
        };
    }
}
