package de.fuberlin.wiwiss.ng4j.sparql;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.shared.LockMutex;
import com.hp.hpl.jena.sparql.core.DatasetGraph;
import com.hp.hpl.jena.sparql.core.Quad;
import com.hp.hpl.jena.sparql.util.Context;
import de.fuberlin.wiwiss.ng4j.NamedGraph;
import de.fuberlin.wiwiss.ng4j.NamedGraphSet;
import de.fuberlin.wiwiss.ng4j.impl.NamedGraphImpl;

/**
 * Implementation of ARQ's Dataset interface on top of an NG4J NamedGraphSet
 */
public class NamedGraphDataset implements Dataset, DatasetGraph {

    private NamedGraphSet set;

    private Graph defaultGraph;

    private Lock lock;

    /**
	 * Creates a new instance whose default graph is the merge of
	 * all named graphs. 
	 */
    public NamedGraphDataset(NamedGraphSet baseNamedGraphSet) {
        this(baseNamedGraphSet, baseNamedGraphSet.asJenaGraph(null));
    }

    /**
	 * Creates a new instance where one of the named graph is
	 * used as the default graph. The graph must already exist
	 * in the NamedGraphSet.
	 */
    public NamedGraphDataset(NamedGraphSet baseNamedGraphSet, Node defaultGraphName) {
        this(baseNamedGraphSet, baseNamedGraphSet.getGraph(defaultGraphName));
    }

    /**
	 * Creates a new instance with a given default graph.
	 */
    public NamedGraphDataset(NamedGraphSet baseNamedGraphSet, Graph defaultGraph) {
        this.set = baseNamedGraphSet;
        this.defaultGraph = defaultGraph;
    }

    public boolean containsNamedModel(String uri) {
        return this.set.containsGraph(uri);
    }

    public Model getDefaultModel() {
        return new ModelCom(this.defaultGraph);
    }

    public Model getNamedModel(String graphName) {
        return new ModelCom(this.set.getGraph(graphName));
    }

    public Iterator<String> listNames() {
        final Iterator<NamedGraph> it = this.set.listGraphs();
        return new Iterator<String>() {

            public String next() {
                return (it.next()).getGraphName().getURI();
            }

            public boolean hasNext() {
                return it.hasNext();
            }

            public void remove() {
                it.remove();
            }
        };
    }

    public Lock getLock() {
        if (this.lock == null) {
            this.lock = new LockMutex();
        }
        return this.lock;
    }

    public DatasetGraph asDatasetGraph() {
        return this;
    }

    public boolean containsGraph(Node graphNode) {
        return set.containsGraph(graphNode);
    }

    public Graph getDefaultGraph() {
        return defaultGraph;
    }

    public void setDefaultGraph(Graph g) {
        defaultGraph = g;
    }

    public Graph getGraph(Node graphNode) {
        return set.getGraph(graphNode);
    }

    public void addGraph(Node graphName, Graph graph) {
        set.addGraph(new NamedGraphImpl(graphName, graph));
    }

    public void removeGraph(Node graphName) {
        set.removeGraph(graphName);
    }

    public void add(Quad quad) {
        set.addQuad(convert(quad));
    }

    public void delete(Quad quad) {
        set.removeQuad(convert(quad));
    }

    public void deleteAny(Node g, Node s, Node p, Node o) {
        de.fuberlin.wiwiss.ng4j.Quad q = new de.fuberlin.wiwiss.ng4j.Quad((g == null) ? Node.ANY : g, (s == null) ? Node.ANY : s, (p == null) ? Node.ANY : p, (o == null) ? Node.ANY : o);
        set.removeQuad(q);
    }

    public Iterator<Quad> find() {
        return find(Node.ANY, Node.ANY, Node.ANY, Node.ANY);
    }

    public Iterator<Quad> find(Quad quad) {
        return new ConvertingIterator(set.findQuads(convert(quad)));
    }

    public Iterator<Quad> find(Node g, Node s, Node p, Node o) {
        de.fuberlin.wiwiss.ng4j.Quad q = new de.fuberlin.wiwiss.ng4j.Quad((g == null) ? Node.ANY : g, (s == null) ? Node.ANY : s, (p == null) ? Node.ANY : p, (o == null) ? Node.ANY : o);
        return new ConvertingIterator(set.findQuads(q));
    }

    public Iterator<Quad> findNG(Node g, Node s, Node p, Node o) {
        return find(g, s, p, o);
    }

    public boolean contains(Node g, Node s, Node p, Node o) {
        de.fuberlin.wiwiss.ng4j.Quad q = new de.fuberlin.wiwiss.ng4j.Quad((g == null) ? Node.ANY : g, (s == null) ? Node.ANY : s, (p == null) ? Node.ANY : p, (o == null) ? Node.ANY : o);
        return set.containsQuad(q);
    }

    public boolean contains(Quad arqQuad) {
        return set.containsQuad(convert(arqQuad));
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public Iterator<Node> listGraphNodes() {
        Set<Node> graphNodes = new HashSet<Node>();
        for (Iterator<NamedGraph> it = set.listGraphs(); it.hasNext(); ) {
            NamedGraph ng = it.next();
            graphNodes.add(ng.getGraphName());
        }
        return graphNodes.iterator();
    }

    public long size() {
        long numGraphs = set.countGraphs();
        int graphNum = (int) numGraphs;
        return graphNum;
    }

    public Context getContext() {
        return new Context();
    }

    /**
	 * @see com.hp.hpl.jena.query.Dataset#close()
	 */
    public void close() {
        set.close();
        defaultGraph.close();
    }

    class ConvertingIterator implements Iterator<Quad> {

        protected final Iterator<de.fuberlin.wiwiss.ng4j.Quad> base;

        public ConvertingIterator(Iterator<de.fuberlin.wiwiss.ng4j.Quad> base) {
            this.base = base;
        }

        public boolean hasNext() {
            return base.hasNext();
        }

        public Quad next() {
            return convert(base.next());
        }

        public void remove() {
            base.remove();
        }
    }

    public static de.fuberlin.wiwiss.ng4j.Quad convert(Quad arqQuad) {
        return new de.fuberlin.wiwiss.ng4j.Quad((arqQuad.getGraph() == null) ? Node.ANY : arqQuad.getGraph(), (arqQuad.getSubject() == null) ? Node.ANY : arqQuad.getSubject(), (arqQuad.getPredicate() == null) ? Node.ANY : arqQuad.getPredicate(), (arqQuad.getObject() == null) ? Node.ANY : arqQuad.getObject());
    }

    public static Quad convert(de.fuberlin.wiwiss.ng4j.Quad ng4jQuad) {
        return new Quad(ng4jQuad.getGraphName(), ng4jQuad.getSubject(), ng4jQuad.getPredicate(), ng4jQuad.getObject());
    }
}
