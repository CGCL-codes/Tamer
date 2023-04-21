package de.fuberlin.wiwiss.jenaext.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import com.hp.hpl.jena.graph.BulkUpdateHandler;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.TripleMatch;
import com.hp.hpl.jena.graph.impl.GraphBase;
import com.hp.hpl.jena.graph.query.QueryHandler;
import com.hp.hpl.jena.shared.ReificationStyle;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import de.fuberlin.wiwiss.jenaext.DecodingTriplesIterator;
import de.fuberlin.wiwiss.jenaext.EmptyIterator;
import de.fuberlin.wiwiss.jenaext.IdBasedGraph;
import de.fuberlin.wiwiss.jenaext.IdBasedTriple;
import de.fuberlin.wiwiss.jenaext.NodeDictionary;

/**
 * An RDF graph implemented using on six main memory-based indexes (S, P, O,
 * SP, PO, SO).
 * This implementation is optimized for read-only access. Removing triples from
 * this graph is not supported.
 *
 * @author Olaf Hartig
 */
public class IdBasedGraphMem extends GraphBase implements IdBasedGraph {

    protected final Index<IdBasedTriple> indexS = new Index<IdBasedTriple>();

    protected final Index<IdBasedTriple> indexP = new Index<IdBasedTriple>();

    protected final Index<IdBasedTriple> indexO = new Index<IdBasedTriple>();

    protected final Index2 indexSP = new Index2();

    protected final Index2 indexSO = new Index2();

    protected final Index2 indexPO = new Index2();

    /** the node dictionary */
    protected final NodeDictionary nodeDict;

    /**
	 * A set that holds the identifiers of all nodes that occur in the triples
	 * of this graph.
	 * This set allows to answer triple pattern queries (i.e. find queries) that
	 * contain unknown nodes (unknown to this graph) very fast because for this
	 * graph cannot contain triples that match the pattern.
	 */
    protected final Set<Integer> containedIds = new HashSet<Integer>();

    /**
	 * Creates a graph with reification style Minimal.
	 *
	 * @param nodeDict the node dictionary used to get and create identifiers
	 *                 for RDF nodes that occur in triple pattern queries issued
	 *                 to this graph ({@link #graphBaseFind}) and for RDF nodes
	 *                 that occur in triples added to this graph
	 */
    public IdBasedGraphMem(NodeDictionary nodeDict) {
        super();
        assert nodeDict != null;
        this.nodeDict = nodeDict;
    }

    /**
	 * Creates a graph with the given reification style.
	 *
	 * @param nodeDict the node dictionary used to get and create identifiers
	 *                 for RDF nodes that occur in triple pattern queries issued
	 *                 to this graph ({@link #graphBaseFind}) and for RDF nodes
	 *                 that occur in triples added to this graph
	 * @param style the reification style to be used for this graph
	 */
    public IdBasedGraphMem(NodeDictionary nodeDict, ReificationStyle style) {
        super(style);
        assert nodeDict != null;
        this.nodeDict = nodeDict;
    }

    /**
	 * The standard method that implements the execution of a triple pattern
	 * query.
	 * This method obtains the identifiers for the concrete components of the
	 * given triple pattern and facilitates the identifier-based find method
	 * (i.e. {@link #find}).
	 * 
	 * @see com.hp.hpl.jena.graph.impl.GraphBase#graphBaseFind(com.hp.hpl.jena.graph.TripleMatch)
	 */
    @Override
    protected ExtendedIterator<Triple> graphBaseFind(TripleMatch m) {
        return new DecodingTriplesIterator(findIdBased(m));
    }

    /**
	 * Adds the given triple to this graph.
	 * Uses the node dictionary to obtain or (if necessary) create identifiers
	 * for the RDF nodes in the given triple.
	 * 
	 * @see com.hp.hpl.jena.graph.impl.GraphBase#performAdd(com.hp.hpl.jena.graph.Triple)
	 */
    @Override
    public void performAdd(Triple t) {
        assert (t.isConcrete());
        checkOpen();
        IdBasedTriple tIDb = new IdBasedTriple(t, nodeDict.createId(t.getSubject()), nodeDict.createId(t.getPredicate()), nodeDict.createId(t.getObject()));
        indexS.put(tIDb.s, tIDb);
        indexP.put(tIDb.p, tIDb);
        indexO.put(tIDb.o, tIDb);
        indexSP.put(tIDb.s, tIDb.p, tIDb);
        indexSO.put(tIDb.s, tIDb.o, tIDb);
        indexPO.put(tIDb.p, tIDb.o, tIDb);
        containedIds.add(tIDb.s);
        containedIds.add(tIDb.p);
        containedIds.add(tIDb.o);
    }

    /**
	 * Deletes the given triple from this graph.
	 * Attention: This method is very inefficient. This {@link IdBasedGraph}
	 *            implementation is optimized for read-only access.
	 * 
	 * @see com.hp.hpl.jena.graph.impl.GraphBase#performDelete(com.hp.hpl.jena.graph.Triple)
	 */
    @Override
    public void performDelete(Triple t) {
        assert (t.isConcrete());
        Iterator<IdBasedTriple> it = findIdBased(t);
        if (it.hasNext()) {
            IdBasedTriple tIDb = it.next();
            indexS.remove(tIDb.s, tIDb);
            indexP.remove(tIDb.p, tIDb);
            indexO.remove(tIDb.o, tIDb);
            indexSP.remove(tIDb.s, tIDb.p, tIDb);
            indexSO.remove(tIDb.s, tIDb.o, tIDb);
            indexPO.remove(tIDb.p, tIDb.o, tIDb);
        }
    }

    /**
	 * Returns a query handler (see {@link IdBasedQueryHandler} that is based on
	 * the identifiers used to represent RDF nodes in this RDF graph
	 * implementation.
	 * 
	 * @see com.hp.hpl.jena.graph.impl.GraphBase#queryHandler()
	 */
    @Override
    public QueryHandler queryHandler() {
        if (queryHandler == null) {
            queryHandler = new IdBasedQueryHandler(this);
        }
        return queryHandler;
    }

    @Override
    public BulkUpdateHandler getBulkUpdateHandler() {
        if (bulkHandler == null) {
            bulkHandler = new IdBasedBulkUpdateHandler(this);
        }
        return bulkHandler;
    }

    @Override
    protected int graphBaseSize() {
        return indexS.size();
    }

    public NodeDictionary getNodeDictionary() {
        return nodeDict;
    }

    public boolean contains(int sId, int pId, int oId) {
        return (find(sId, pId, oId).hasNext());
    }

    public Iterator<IdBasedTriple> find(int sId, int pId, int oId) {
        checkOpen();
        if (sId != -1 && !containedIds.contains(sId)) {
            return EmptyIterator.emptyIdBasedTripleIterator;
        }
        if (pId != -1 && !containedIds.contains(pId)) {
            return EmptyIterator.emptyIdBasedTripleIterator;
        }
        if (oId != -1 && !containedIds.contains(oId)) {
            return EmptyIterator.emptyIdBasedTripleIterator;
        }
        if (sId < 0) {
            if (pId < 0) {
                if (oId < 0) {
                    return indexS.getAll();
                } else {
                    return new IteratorO(indexO, oId);
                }
            } else {
                if (oId < 0) {
                    return new IteratorP(indexP, pId);
                } else {
                    return new IteratorPO(indexPO, pId, oId);
                }
            }
        } else {
            if (pId < 0) {
                if (oId < 0) {
                    return new IteratorS(indexS, sId);
                } else {
                    return new IteratorSO(indexSO, sId, oId);
                }
            } else {
                if (oId < 0) {
                    return new IteratorSP(indexSP, sId, pId);
                } else {
                    return findOne(sId, pId, oId);
                }
            }
        }
    }

    /**
	 * Executes a triple pattern query and {@link IdBasedTriple}s that encode
	 * matching triples.
	 */
    public Iterator<IdBasedTriple> findIdBased(TripleMatch m) {
        Node matchSubject = m.getMatchSubject();
        Node matchPredicate = m.getMatchPredicate();
        Node matchObject = m.getMatchObject();
        int sId = (matchSubject == null) ? -1 : nodeDict.getId(matchSubject);
        int pId = (matchPredicate == null) ? -1 : nodeDict.getId(matchPredicate);
        int oId = (matchObject == null) ? -1 : nodeDict.getId(matchObject);
        if ((matchSubject != null && sId < 0) || (matchPredicate != null && pId < 0) || (matchObject != null && oId < 0)) {
            return EmptyIterator.emptyIdBasedTripleIterator;
        }
        return find(sId, pId, oId);
    }

    /**
	 * Executes a triple pattern query and {@link IdBasedTriple}s that encode
	 * matching triples.
	 */
    public Iterator<IdBasedTriple> findIdBased(Node s, Node p, Node o) {
        return findIdBased(Triple.createMatch(s, p, o));
    }

    void delete(IdBasedTriple t) {
        indexS.remove(t.s, t);
        indexP.remove(t.p, t);
        indexO.remove(t.o, t);
        indexSP.remove(t.s, t.p, t);
        indexSO.remove(t.s, t.o, t);
        indexPO.remove(t.p, t.o, t);
    }

    /**
	 * Executes a triple pattern query without wildcards.
	 * None of the given identifiers must be -1.
	 */
    protected Iterator<IdBasedTriple> findOne(int sId, int pId, int oId) {
        assert sId >= 0;
        assert pId >= 0;
        assert oId >= 0;
        return new IteratorSPO(indexSP, sId, pId, oId);
    }

    /**
	 * An iterator that provides only a single element.
	 */
    static class SingleElementIterator<E> implements Iterator<E> {

        private E element;

        public SingleElementIterator(E e) {
            this.element = e;
        }

        public boolean hasNext() {
            return (element != null);
        }

        public E next() {
            E e = this.element;
            this.element = null;
            return e;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
	 * Base class for all iterators over one of the indexes.
	 */
    abstract static class IteratorIndex1 implements Iterator<IdBasedTriple> {

        private final Iterator<IdBasedTriple> base;

        protected final int reqId;

        private IdBasedTriple nextTriple;

        public IteratorIndex1(Index<IdBasedTriple> index, int reqId) {
            this.base = index.get(reqId);
            this.reqId = reqId;
        }

        protected IteratorIndex1(Iterator<IdBasedTriple> base, int reqId) {
            this.base = base;
            this.reqId = reqId;
        }

        public final boolean hasNext() {
            if (nextTriple != null) {
                return true;
            }
            IdBasedTriple t;
            while (base.hasNext()) {
                t = base.next();
                if (matches(t)) {
                    nextTriple = t;
                    break;
                }
            }
            return (nextTriple != null);
        }

        public final IdBasedTriple next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            IdBasedTriple t = this.nextTriple;
            this.nextTriple = null;
            return t;
        }

        public final void remove() {
            throw new UnsupportedOperationException();
        }

        protected abstract boolean matches(IdBasedTriple t);
    }

    static class IteratorS extends IteratorIndex1 {

        public IteratorS(Index<IdBasedTriple> index, int reqId) {
            super(index, reqId);
        }

        protected final boolean matches(IdBasedTriple t) {
            return t.s == reqId;
        }
    }

    static class IteratorP extends IteratorIndex1 {

        public IteratorP(Index<IdBasedTriple> index, int reqId) {
            super(index, reqId);
        }

        protected final boolean matches(IdBasedTriple t) {
            return t.p == reqId;
        }
    }

    static class IteratorO extends IteratorIndex1 {

        public IteratorO(Index<IdBasedTriple> index, int reqId) {
            super(index, reqId);
        }

        protected final boolean matches(IdBasedTriple t) {
            return t.o == reqId;
        }
    }

    abstract static class IteratorIndex2 extends IteratorIndex1 {

        protected final int reqId2;

        protected IteratorIndex2(Index2 index, int reqId1, int reqId2) {
            super(index.get(reqId1, reqId2), reqId1);
            this.reqId2 = reqId2;
        }
    }

    static class IteratorSP extends IteratorIndex2 {

        public IteratorSP(Index2 index, int reqId1, int reqId2) {
            super(index, reqId1, reqId2);
        }

        protected final boolean matches(IdBasedTriple t) {
            return (t.s == reqId) && (t.p == reqId2);
        }
    }

    static class IteratorSO extends IteratorIndex2 {

        public IteratorSO(Index2 index, int reqId1, int reqId2) {
            super(index, reqId1, reqId2);
        }

        protected final boolean matches(IdBasedTriple t) {
            return (t.s == reqId) && (t.o == reqId2);
        }
    }

    static class IteratorPO extends IteratorIndex2 {

        public IteratorPO(Index2 index, int reqId1, int reqId2) {
            super(index, reqId1, reqId2);
        }

        protected final boolean matches(IdBasedTriple t) {
            return (t.p == reqId) && (t.o == reqId2);
        }
    }

    static class IteratorSPO extends IteratorIndex2 {

        protected final int reqId3;

        public IteratorSPO(Index2 index, int sId, int pId, int oId) {
            super(index, sId, pId);
            this.reqId3 = oId;
        }

        @Override
        protected final boolean matches(IdBasedTriple t) {
            return (t.o == reqId3) && (t.s == reqId) && (t.p == reqId2);
        }
    }
}
