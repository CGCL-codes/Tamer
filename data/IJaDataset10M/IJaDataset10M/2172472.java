package com.hp.hpl.jena.graph;

import com.hp.hpl.jena.shared.*;
import com.hp.hpl.jena.util.iterator.Filter;
import java.util.*;

/**
    Triples are the basis for RDF statements; they have a subject, predicate, and
    object field (all nodes) and express the notion that the relationship named
    by the predicate holds between the subject and the object.
    
    @author Jeremy Carroll, kers
 */
public class Triple implements TripleMatch {

    private final Node subj, pred, obj;

    public Triple(Node s, Node p, Node o) {
        if (s == null) throw new UnsupportedOperationException("subject cannot be null");
        if (p == null) throw new UnsupportedOperationException("predicate cannot be null");
        if (o == null) throw new UnsupportedOperationException("object cannot be null");
        subj = s;
        pred = p;
        obj = o;
    }

    /**
        return a human-readable string "subject @predicate object" describing the triple
    */
    public String toString() {
        return toString(PrefixMapping.Standard);
    }

    public String toString(PrefixMapping pm) {
        return subj.toString(pm, true) + " @" + pred.toString(pm, true) + " " + obj.toString(pm, true);
    }

    /**
        @return the subject of the triple
    */
    public final Node getSubject() {
        return subj;
    }

    /**
        @return the predicate of the triple
    */
    public final Node getPredicate() {
        return pred;
    }

    /**
        @return the object of the triple
    */
    public final Node getObject() {
        return obj;
    }

    public Node getMatchSubject() {
        return anyToNull(subj);
    }

    public Node getMatchPredicate() {
        return anyToNull(pred);
    }

    public Node getMatchObject() {
        return anyToNull(obj);
    }

    private static Node anyToNull(Node n) {
        return Node.ANY.equals(n) ? null : n;
    }

    private static Node nullToAny(Node n) {
        return n == null ? Node.ANY : n;
    }

    public Triple asTriple() {
        return this;
    }

    public boolean isConcrete() {
        return subj.isConcrete() && pred.isConcrete() && obj.isConcrete();
    }

    /** 
         Answer true if <code>o</code> is a Triple with the same subject, predicate,
         and object as this triple.
    */
    public boolean equals(Object o) {
        return o instanceof Triple && ((Triple) o).sameAs(subj, pred, obj);
    }

    /** 
        Answer true iff this triple has subject s, predicate p, and object o.
    */
    public boolean sameAs(Node s, Node p, Node o) {
        return subj.equals(s) && pred.equals(p) && obj.equals(o);
    }

    public boolean matches(Triple other) {
        return other.matchedBy(subj, pred, obj);
    }

    public boolean matches(Node s, Node p, Node o) {
        return subj.matches(s) && pred.matches(p) && obj.matches(o);
    }

    private boolean matchedBy(Node s, Node p, Node o) {
        return s.matches(subj) && p.matches(pred) && o.matches(obj);
    }

    public boolean subjectMatches(Node s) {
        return subj.matches(s);
    }

    public boolean predicateMatches(Node p) {
        return pred.matches(p);
    }

    public boolean objectMatches(Node o) {
        return obj.matches(o);
    }

    /**
        The hash-code of a triple is the hash-codes of its components munged
        together: see hashCode(S, P, O).
    */
    public int hashCode() {
        return hashCode(subj, pred, obj);
    }

    /**
        Return the munged hashCodes of the specified nodes, an exclusive-or of 
        the slightly-shifted component hashcodes; this means (almost) all of the bits 
        count, and the order matters, so (S @P O) has a different hash from 
        (O @P S), etc.
    */
    public static int hashCode(Node s, Node p, Node o) {
        return (s.hashCode() >> 1) ^ p.hashCode() ^ (o.hashCode() << 1);
    }

    /**
        Factory method for creating triples, allows caching opportunities. Attempts
        to use triples from the cache, if any suitable ones exist.
        
        @return a triple with subject=s, predicate=p, object=o
    */
    public static Triple create(Node s, Node p, Node o) {
        Triple already = cache.get(s, p, o);
        return already == null ? cache.put(new Triple(s, p, o)) : already;
    }

    /**
        The cache of already-created triples.
    */
    protected static TripleCache cache = new TripleCache();

    public static Triple createMatch(Node s, Node p, Node o) {
        return Triple.create(nullToAny(s), nullToAny(p), nullToAny(o));
    }

    /**
        Utility factory method for creating a triple based on the content of an
        "S P O" string. The S, P, O are processed by Node.create, see which for
        details of the supported syntax. This method exists to support test code.
        Nodes are interpreted using the Standard prefix mapping.
    */
    public static Triple create(String fact) {
        return create(PrefixMapping.Standard, fact);
    }

    /**
        Utility factory as for create(String), but allowing the PrefixMapping to
        be specified explicitly.
    */
    public static Triple create(PrefixMapping pm, String fact) {
        StringTokenizer st = new StringTokenizer(fact);
        Node sub = Node.create(pm, st.nextToken());
        Node pred = Node.create(pm, st.nextToken());
        Node obj = Node.create(pm, st.nextToken());
        return Triple.create(sub, pred, obj);
    }

    /**
        A Triple that is wildcarded in all fields. 
    */
    public static final Triple ANY = Triple.create(Node.ANY, Node.ANY, Node.ANY);

    /**
        A Field is a selector from Triples; it allows selectors to be passed
        around as if they were functions, hooray. 
    */
    public abstract static class Field {

        public abstract Node getField(Triple t);

        public abstract Filter filterOn(Node n);

        public final Filter filterOn(Triple t) {
            return filterOn(getField(t));
        }

        public static final Field getSubject = new Field() {

            public Node getField(Triple t) {
                return t.subj;
            }

            public Filter filterOn(final Node n) {
                return n.isConcrete() ? new Filter() {

                    public boolean accept(Object x) {
                        return n.equals(((Triple) x).subj);
                    }
                } : Filter.any;
            }
        };

        public static final Field getObject = new Field() {

            public Node getField(Triple t) {
                return t.obj;
            }

            public Filter filterOn(final Node n) {
                return n.isConcrete() ? new Filter() {

                    public boolean accept(Object x) {
                        return n.sameValueAs(((Triple) x).obj);
                    }
                } : Filter.any;
            }
        };

        public static final Field getPredicate = new Field() {

            public Node getField(Triple t) {
                return t.pred;
            }

            public Filter filterOn(final Node n) {
                return n.isConcrete() ? new Filter() {

                    public boolean accept(Object x) {
                        return n.equals(((Triple) x).pred);
                    }
                } : Filter.any;
            }
        };
    }
}
