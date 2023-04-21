package com.hp.hpl.jena.reasoner;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.util.iterator.*;

/**
 * Wrapper round a Graph to implement the slighly modified Finder
 * interface.
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.10 $ on $Date: 2006/03/22 13:52:53 $
 */
public class FGraph implements Finder {

    /** The graph being searched */
    protected Graph graph;

    /**
     * Constructor
     */
    public FGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * Basic pattern lookup interface.
     * @param pattern a TriplePattern to be matched against the data
     * @return a ClosableIterator over all Triples in the data set
     *  that match the pattern
     */
    public ExtendedIterator find(TriplePattern pattern) {
        if (graph == null) return WrappedIterator.create(new NullIterator());
        return graph.find(pattern.asTripleMatch());
    }

    /**
     * Extended find interface used in situations where the implementator
     * may or may not be able to answer the complete query. It will
     * attempt to answer the pattern but if its answers are not known
     * to be complete then it will also pass the request on to the nested
     * Finder to append more results.
     * @param pattern a TriplePattern to be matched against the data
     * @param continuation either a Finder or a normal Graph which
     * will be asked for additional match results if the implementor
     * may not have completely satisfied the query.
     */
    public ExtendedIterator findWithContinuation(TriplePattern pattern, Finder continuation) {
        if (graph == null) return WrappedIterator.create(new NullIterator());
        if (continuation == null) {
            return graph.find(pattern.asTripleMatch());
        } else {
            return graph.find(pattern.asTripleMatch()).andThen(continuation.find(pattern));
        }
    }

    /**
     * Returns the graph.
     * @return Graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Return true if the given pattern occurs somewhere in the find sequence.
     */
    public boolean contains(TriplePattern pattern) {
        return graph.contains(pattern.getSubject(), pattern.getPredicate(), pattern.getObject());
    }
}
