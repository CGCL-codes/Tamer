package de.uni_trier.st.nevada.view.central;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import de.uni_trier.st.nevada.graphs.PresentGraph;
import de.uni_trier.st.nevada.graphs.Int.Edge;
import de.uni_trier.st.nevada.graphs.Int.Graph;
import de.uni_trier.st.nevada.graphs.Int.InvalidGraphTransformationException;
import de.uni_trier.st.nevada.graphs.Int.Node;
import de.uni_trier.st.nevada.io.Exporter;
import de.uni_trier.st.nevada.io.FileTypeNotSupportedException;
import de.uni_trier.st.nevada.io.Importer;
import de.uni_trier.st.nevada.io.InvalidDataException;
import de.uni_trier.st.nevada.io.gaml.GAMLExporter;
import de.uni_trier.st.nevada.io.gaml.GAMLImporter;
import de.uni_trier.st.nevada.io.graphml.GraphMLExporter;
import de.uni_trier.st.nevada.io.newsgroups.TraceReader;
import de.uni_trier.st.nevada.io.pajek.PajekConnector;
import de.uni_trier.st.nevada.io.refactorings.RefReader;
import de.uni_trier.st.nevada.io.svg.SVGExport;
import de.uni_trier.st.nevada.io.svg.SVGExportOptions;
import de.uni_trier.st.nevada.layout.FLTAlgorithm;
import de.uni_trier.st.nevada.sight.Sight;

/**
 * This class holds all graph data and provides functions to access them.
 *
 * @author mathias
 *
 */
public class GraphAnimationModel {

    private Set<Node> allNodes;

    private Sight currentSight;

    private Exporter graphExporter;

    private Importer graphImporter;

    public List<Graph> graphs;

    private Map<Graph, NodeTree> nodeStorage;

    private boolean saved;

    private UndoEngine undoEngine;

    public GraphAnimationModel() {
        this.graphs = new ArrayList<Graph>();
        this.graphExporter = new GAMLExporter();
        this.graphImporter = new GAMLImporter();
        this.saved = true;
        this.undoEngine = new UndoEngine();
        this.allNodes = new HashSet<Node>();
        this.nodeStorage = new HashMap<Graph, NodeTree>();
        this.currentSight = null;
        this.clear();
    }

    /**
	 * Adds a new graph to end of sequence
	 *
	 * @param o
	 */
    void add(final Graph o) {
        this.graphs.add(o);
        this.nodeStorage.put(o, new NodeTree(o, 5));
        this.undoEngine.store(this.graphs);
        this.saved = false;
        this.allNodes.addAll(o.getVisibleNodes());
    }

    /**
	 * Adds a new graph at specified position in sequence
	 *
	 * @param o
	 * @param index
	 */
    void add(final Graph o, int index) {
        this.graphs.add(index, o);
        this.nodeStorage.put(o, new NodeTree(o, 5));
        this.undoEngine.store(this.graphs);
        this.allNodes.addAll(o.getVisibleNodes());
    }

    /**
	 * Removes graph o from sequence
	 *
	 * @param o
	 */
    public void remove(final Graph o) {
        this.graphs.remove(o);
        this.nodeStorage.remove(o);
    }

    /**
	 * Removes graph at specified index from sequence
	 *
	 * @param o
	 */
    public void remove(int index) {
        if (index < 0 || index >= this.graphs.size()) return;
        Graph o = this.graphs.remove(index);
        this.nodeStorage.remove(o);
    }

    /**
	 * Returns the graph at position index if possible
	 *
	 * @param index
	 * @return Den Graph an position <code>index</code>
	 */
    Graph get(final int index) {
        if (index < 0 || index >= this.graphs.size()) return null;
        return this.graphs.get(index);
    }

    /**
	 * Adds Node n to Graph originalGraph
	 *
	 * @param n
	 * @param originalGraph
	 */
    void addNode(Node n, Graph originalGraph) {
        if (this.currentSight != null) {
            this.currentSight.addNode(n, originalGraph);
            this.nodeStorage.get(this.currentSight.getGraph(originalGraph)).addNode(this.currentSight.getNode(n));
        } else {
            originalGraph.addNode(n);
            this.nodeStorage.get(originalGraph).addNode(n);
        }
    }

    /**
	 * Adds Edge e to Graph originalGraph
	 *
	 * @param e
	 * @param originalGraph
	 */
    void addEdge(Edge e, Graph originalGraph) {
        if (this.currentSight != null) {
            this.currentSight.addEdge(e, originalGraph);
        } else {
            try {
                originalGraph.addEdge(e);
            } catch (InvalidGraphTransformationException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
	 * Clears the model. Removes all graphs and all other data stored here
	 *
	 */
    void clear() {
        this.undoEngine.clear();
        this.graphs.clear();
        this.allNodes.clear();
        this.add(new PresentGraph());
        this.saved = true;
    }

    /**
	 * Deletes Node n from Graph originalGraph.
	 *
	 * @param n
	 * @param originalGraph
	 */
    void deleteNode(Node n, Graph originalGraph) {
        if (this.currentSight != null) {
            originalGraph.removeNode(n);
            this.currentSight.deleteNode(n, originalGraph);
        } else {
            Set<Edge> remove = new HashSet<Edge>();
            for (Edge e : originalGraph.getVisibleEdges()) {
                if ((e.getSource() == n) || (e.getTarget() == n)) remove.add(e);
            }
            originalGraph.removeEdges(remove);
            originalGraph.removeNode(n);
            this.undoEngine.store(this.graphs);
            this.nodeStorage.get(originalGraph).deleteNode(n);
        }
    }

    /**
	 * Deletes Node n from all Graphs
	 *
	 * @param n
	 */
    void deleteNode(final Node n) {
        for (final Graph g : this.graphs) {
            this.deleteNode(n, g);
        }
    }

    /**
	 * Deletes Edge e from all Graphs
	 *
	 * @param e
	 */
    void deleteEdge(final Edge e) {
        for (final Graph g : this.graphs) {
            this.deleteEdge(e, g);
        }
    }

    /**
	 * Deletes Edge e from Graph originalGraph.
	 *
	 * @param ed
	 * @param originalGraph
	 */
    void deleteEdge(Edge ed, Graph originalGraph) {
        if (this.currentSight != null) {
            this.currentSight.deleteEdge(ed, originalGraph);
        } else {
            Set<Edge> remove = new HashSet<Edge>();
            remove.add(ed);
            originalGraph.removeEdges(remove);
            this.undoEngine.store(this.graphs);
        }
    }

    /**
	 * Exports data to file named filename
	 *
	 * @param filename
	 * @return export successfull?
	 */
    boolean export(final String filename) {
        try {
            if (this.graphExporter.save(this.graphs, new FileOutputStream(filename))) {
                this.saved = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this.saved;
    }

    /**
	 *
	 * @param index
	 * @param file
	 * @return <code>true</code> wenn speichern erfolgreich war
	 */
    boolean export2GraphML(int index, final File file) {
        GraphMLExporter graphMLexporter = new GraphMLExporter();
        try {
            return graphMLexporter.save(get(index), new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    /**
	 * SVG export
	 *
	 * @param filename
	 * @return <code>true</code> wenn export erfolgreich war
	 */
    boolean export2SVG(final String filename) {
        try {
            SVGExport s = new SVGExport(new File(filename));
            SVGExportOptions o = new SVGExportOptions();
            s.export2svg(this.graphs, o);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
	 * Returns a set of all Edges. Result will not be changeable use other
	 * functions to change it.
	 *
	 * @return The set of all available edges.
	 */
    Set<Edge> getAllEdges() {
        Set<Edge> retVal = new HashSet<Edge>();
        for (Graph g : this.graphs) {
            retVal.addAll(g.getVisibleEdges());
        }
        return Collections.unmodifiableSet(retVal);
    }

    /**
	 * Returns a set of all Nodes. Result will not be changeable use other
	 * functions to change it.
	 *
	 * @return The set of all nodes
	 */
    Set<Node> getAllNodes() {
        Set<Node> retVal = new HashSet<Node>();
        for (Graph g : this.graphs) {
            retVal.addAll(g.getVisibleNodes());
        }
        return Collections.unmodifiableSet(retVal);
    }

    /**
	 * Returns a Set of all Nodes of type <code>type</code>. Type might not
	 * be specified for all nodes.
	 *
	 * @param type
	 * @return The set of all available nodes.
	 */
    Set<Node> getAllOfType(String type) {
        Set<Node> nodes = new HashSet<Node>();
        for (Node n : getAllNodes()) {
            if ((n.getProperty("type") != null) && (type.equals(n.getProperty("type")))) {
                nodes.add(n);
            }
        }
        return nodes;
    }

    /**
	 * Returns a Set of all available types if any.
	 *
	 * @return The set of all available types
	 */
    Set<String> getAllTypes() {
        Set<String> types = new HashSet<String>();
        for (Node n : getAllNodes()) {
            String type = n.getProperty("type");
            if (type != null) {
                types.add(type);
            }
        }
        return types;
    }

    /**
	 * Returns the current Sight if any
	 *
	 * @return The current sight.
	 */
    Sight getCurrentSight() {
        return this.currentSight;
    }

    /**
	 * Returns the graph sequence
	 */
    List<Graph> getGraphs() {
        return this.graphs;
    }

    /**
	 * Returns the NodeTree of Graph with index index
	 *
	 * @param index
	 * @return The node tree at given index.
	 */
    NodeTree getNodeTree(final int index) {
        return this.nodeStorage.get(get(index));
    }

    boolean isSaved() {
        return this.saved;
    }

    /**
	 * Creates a NodeStorage for each graph. NodeStorage will store positionData
	 * of graph's nodes.
	 *
	 * @param l
	 * @return
	 */
    private boolean createNodeStorage(List<Graph> l) {
        this.nodeStorage.clear();
        if (l != null && l.size() != 0) {
            if (this.currentSight != null) this.graphs = this.currentSight.cloneGraphList(l); else this.graphs = l;
            for (Graph g : l) this.nodeStorage.put(g, new NodeTree(g, 5));
            return true;
        }
        return false;
    }

    /**
	 * Tries to load filename
	 *
	 * @param filename
	 * @return Successfull?
	 */
    boolean load(final String filename) {
        List<Graph> l = null;
        try {
            l = this.graphImporter.load(new FileInputStream(filename));
        } catch (IOException exn) {
            return false;
        } catch (FileTypeNotSupportedException e) {
            return false;
        } catch (InvalidDataException e) {
            return false;
        }
        return createNodeStorage(l);
    }

    public void loadPajek(File filename) {
        System.out.println("Loading Pajek...");
        PajekConnector reader = new PajekConnector();
        List<Graph> l = null;
        try {
            l = reader.load(new FileInputStream(filename));
        } catch (FileNotFoundException exn) {
            exn.printStackTrace();
        } catch (FileTypeNotSupportedException exn) {
            exn.printStackTrace();
        } catch (InvalidDataException exn) {
            exn.printStackTrace();
        }
        if (l != null) {
            System.out.println("finished!");
            this.graphs = new ArrayList<Graph>();
            for (Graph g : l) {
                if (g.getVisibleNodes().size() != 0) {
                    this.graphs.add(g);
                }
            }
        }
    }

    /**
	 * Loads a graph from a pajek-file and appends it to the current graph-sequence
	 *
	 */
    public void appendPajek(File filename) {
        System.out.println("Loading Pajek...");
        PajekConnector reader = new PajekConnector();
        List<Graph> l = null;
        try {
            l = reader.load(new FileInputStream(filename));
        } catch (FileNotFoundException exn) {
            exn.printStackTrace();
        } catch (FileTypeNotSupportedException exn) {
            exn.printStackTrace();
        } catch (InvalidDataException exn) {
            exn.printStackTrace();
        }
        if (l != null) {
            System.out.println("finished!");
            for (Graph g : l) {
                if (g.getVisibleNodes().size() != 0) {
                    add(g);
                }
            }
        }
    }

    boolean loadNTrace(final String filename) {
        List<Graph> l;
        l = (new TraceReader()).load(filename);
        if (l != null) {
            if (l.size() != 0) {
                this.graphs = l;
                this.saved = true;
                return true;
            }
            return false;
        }
        return false;
    }

    void loadRF(String filename, int choice) {
        RefReader imp = new RefReader();
        List<Graph> l = imp.load(filename, choice);
        this.graphs = new ArrayList<Graph>();
        for (Graph g : l) {
            if (g.getVisibleNodes().size() != 0) {
                this.graphs.add(g);
            }
        }
    }

    /**
	 * Performes layout on current graph sequence.
	 *
	 * @param f
	 *            the layout algorithm (object) to be used
	 */
    void performGraphLayout(final FLTAlgorithm f) {
        try {
            if (this.currentSight != null) {
                this.currentSight.performGraphLayout(f, this.nodeStorage);
            } else {
                this.graphs = f.processGraphs(this.graphs);
                this.nodeStorage.clear();
                for (Iterator<Graph> it = graphs.iterator(); it.hasNext(); ) {
                    Graph g = it.next();
                    this.nodeStorage.put(g, new NodeTree(g, 5));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void redo() {
        final List<Graph> gr = this.undoEngine.loadNext();
        if (gr != null) {
            this.graphs = gr;
        }
    }

    /**
	 * Searches Node near point p
	 *
	 * @param p
	 * @param index
	 * @return The node in graph index at position p
	 */
    Node searchNode(final Point2D p, int index) {
        if (this.nodeStorage.get(get(index)) != null) {
            return this.nodeStorage.get(get(index)).getNodeIt(p);
        }
        return null;
    }

    /**
	 * Searches Nodes within rectangle r
	 *
	 * @param r
	 * @param index
	 * @return all nodes in given rectangle
	 */
    Set<Node> searchNodesInRect(Rectangle2D r, int index) {
        if (r.getWidth() == 0) r = new Rectangle2D.Double(r.getX(), r.getY(), 1.0, r.getHeight());
        if (r.getHeight() == 0) r = new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth(), 1.0);
        if (this.nodeStorage.get(get(index)) != null) {
            return this.nodeStorage.get(get(index)).getNodesInRect(r);
        }
        return null;
    }

    /**
	 * Sets the current sight
	 *
	 * @param s
	 */
    void setCurrentSight(Sight s) {
        this.currentSight = s;
        this.currentSight.cloneGraphList(this.graphs);
        this.nodeStorage.clear();
        for (Graph g : this.graphs) this.nodeStorage.put(g, new NodeTree(g, 5));
    }

    void setGraphs(List<Graph> graphs) {
        this.nodeStorage.clear();
        this.graphs = graphs;
        this.saved = true;
        if (this.currentSight != null) {
            this.graphs = this.currentSight.cloneGraphList(this.graphs);
            for (Graph g : this.graphs) this.nodeStorage.put(g, new NodeTree(g, 5));
        }
        this.saved = true;
    }

    /**
	 * Returns the size of the graph sequence
	 *
	 * @return the length of the graph animation
	 */
    int size() {
        return this.graphs.size();
    }

    void storeUndoBackup() {
        this.saved = false;
        this.undoEngine.store(this.graphs);
    }

    void undo() {
        final List<Graph> gr = this.undoEngine.loadPrev();
        if (gr != null) {
            this.graphs = gr;
        }
    }
}
