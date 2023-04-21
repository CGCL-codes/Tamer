package edu.ksu.cis.util.graph.algorithms;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import edu.ksu.cis.util.data.OrderedList;
import edu.ksu.cis.util.graph.core.Edge;
import edu.ksu.cis.util.graph.core.Graph;
import edu.ksu.cis.util.graph.core.Vertex;
import edu.ksu.cis.util.graph.visualization.Annotation;
import edu.ksu.cis.util.graph.visualization.operators.CodePageSelect;
import edu.ksu.cis.util.graph.visualization.operators.CodePageSelectLine;
import edu.ksu.cis.util.graph.visualization.operators.CodePageUpdateEnvironment;
import edu.ksu.cis.util.graph.visualization.operators.ColorLegendMap;
import edu.ksu.cis.util.graph.visualization.operators.EdgeCreate;
import edu.ksu.cis.util.graph.visualization.operators.GraphBatch;
import edu.ksu.cis.util.graph.visualization.operators.NewColorLegend;
import edu.ksu.cis.util.graph.visualization.operators.TempEdgeCreate;
import edu.ksu.cis.util.graph.visualization.operators.TempEdgeFlush;
import edu.ksu.cis.util.graph.visualization.operators.VertexColor;

/**
 * @author Julie Thornton
 */
public class BCT_AlreadyTriang extends BuildCliqueTree {

    private Graph _CliqueTree;

    private Vertex[] _CliquesSet;

    private HashSet[] _Cliques_S;

    private HashSet[] _CliquesBaseNodes;

    private HashSet[] cliqueOrder;

    private Graph original;

    public BCT_AlreadyTriang() {
        _Name = "Build Clique Tree from Already Triangulated Graph";
    }

    public Graph getCliqueTree() {
        return _CliqueTree;
    }

    public int getNumberOfCliques() {
        return _CliquesSet.length;
    }

    public Vertex getClique(int i) {
        return _CliquesSet[i];
    }

    public void setOriginal(Graph g) {
        original = g.copy();
    }

    public HashSet getCliqueSet(int i) {
        return (HashSet) _CliquesSet[i].getObject();
    }

    public HashSet getCliqueS(int i) {
        return _Cliques_S[i];
    }

    public HashSet getCliqueBases(int i) {
        return _CliquesBaseNodes[i];
    }

    public Vertex[] getChildren(int i) {
        return _CliqueTree.getChildren(_CliquesSet[i]);
    }

    public void printGraph() {
        int count = 0;
        for (int i = 0; i < _Graph.getVertices().length; i++) {
            for (int j = 0; j < _Graph.getChildren(_Graph.getVertices()[i]).length; j++) {
                System.out.print(_Graph.getVertices()[i].getName() + "->");
                System.out.println(_Graph.getChildren(_Graph.getVertices()[i])[j].getName());
                count++;
            }
        }
        System.out.println();
    }

    public void execute(int[] alpha, Vertex[] alphainv) {
        if (_Graph.getVertices().length == 0) {
            return;
        }
        LinkedList cliques = new LinkedList();
        Vertex[] vertices = _Graph.getVertices();
        Vertex T;
        _CliqueTree = new Graph();
        fillIn(alphainv);
        for (int i = vertices.length; i > 0; i--) {
            Vertex v = alphainv[i];
            Vertex[] parents = _Graph.getParents(v);
            HashSet clique = new HashSet();
            clique.add(v);
            if (parents != null) {
                for (int j = 0; j < parents.length; j++) {
                    Vertex p = parents[j];
                    int order = alpha[p.loc()];
                    if (order <= i) {
                        clique.add(p);
                    }
                }
            }
            cliques.add(clique);
        }
        HashSet[] cliqueSet = (HashSet[]) cliques.toArray(new HashSet[0]);
        int max = cliqueSet.length;
        cliques.clear();
        for (int i = 0; i < max; i++) {
            boolean isContained = false;
            for (int j = 0; j < max; j++) {
                if (i != j && cliqueSet[j].containsAll(cliqueSet[i])) {
                    isContained = true;
                    break;
                }
            }
            if (!isContained) {
                String cliqueName = "{";
                boolean first = true;
                for (Iterator z = cliqueSet[i].iterator(); z.hasNext(); ) {
                    Vertex node = (Vertex) z.next();
                    if (!first) {
                        cliqueName += ",";
                    }
                    first = false;
                    cliqueName += node.getName();
                }
                cliqueName += "}";
                Vertex clique = new Vertex(cliqueName);
                clique.setObject(cliqueSet[i]);
                _CliqueTree.addVertex(clique);
                cliques.addFirst(clique);
            }
        }
        _CliquesSet = (Vertex[]) cliques.toArray(new Vertex[0]);
        max = _CliquesSet.length;
        _Cliques_S = new HashSet[max];
        HashSet union = new HashSet();
        union.addAll((HashSet) _CliquesSet[0].getObject());
        Hashtable parentTable = new Hashtable();
        _CliquesBaseNodes = new HashSet[_CliquesSet.length];
        _CliquesBaseNodes[0] = new HashSet();
        _Cliques_S[0] = new HashSet();
        for (int i = 1; i < max; i++) {
            _CliquesBaseNodes[i] = new HashSet();
            HashSet s = new HashSet();
            HashSet nodeSet = (HashSet) (_CliquesSet[i].getObject());
            s.addAll(nodeSet);
            s.retainAll(union);
            _Cliques_S[i] = s;
            union.addAll(nodeSet);
            for (int j = i - 1; j >= 0; j--) {
                HashSet csj = (HashSet) (_CliquesSet[j].getObject());
                if (csj.containsAll(s)) {
                    parentTable.put(_CliquesSet[i], _CliquesSet[j]);
                }
            }
        }
        for (Enumeration e = parentTable.keys(); e.hasMoreElements(); ) {
            Vertex child = (Vertex) e.nextElement();
            Vertex parent = (Vertex) parentTable.get(child);
            _CliqueTree.addDirectedEdge(parent, child);
        }
        Vertex[] orig_vert = original.getVertices();
        for (int z = 0; z < orig_vert.length; z++) {
            Vertex v = orig_vert[z];
            Vertex[] p = original.getParents(v);
            HashSet total = new HashSet();
            total.add(vertices[v.loc()]);
            if (p != null) for (int zz = 0; zz < p.length; zz++) {
                total.add(vertices[p[zz].loc()]);
            }
            for (int csi = 0; csi < max; csi++) {
                HashSet set = (HashSet) _CliquesSet[csi].getObject();
                if (set.containsAll(total)) {
                    _CliquesBaseNodes[csi].add(v);
                    csi = max;
                }
            }
        }
        int cliqueMax = 0;
        int temp;
        StringTokenizer st;
        for (int i = 0; i < _CliquesSet.length; i++) {
            st = new StringTokenizer(_CliquesSet[i].getName(), ",{}");
            temp = st.countTokens();
            if (cliqueMax < temp) {
                cliqueMax = temp;
            }
        }
        System.out.println("max clique size: " + cliqueMax);
    }

    private void fillIn(Vertex[] alphainv) {
        Vertex current;
        Vertex[] neighbors;
        for (int i = alphainv.length - 1; i > 0; i--) {
            current = alphainv[i];
            neighbors = _Graph.getChildren(current);
            fillInNode(current, i, neighbors, alphainv);
        }
    }

    private void fillInNode(Vertex current, int index, Vertex[] neighbors, Vertex[] alphainv) {
        Vertex node1, node2;
        for (int i = 0; i < neighbors.length; i++) {
            node1 = neighbors[i];
            for (int j = i + 1; j < neighbors.length; j++) {
                node2 = neighbors[j];
                if (isGreater(node1, index, alphainv) && isGreater(node2, index, alphainv)) {
                    _Graph.addUndirectedEdge(node1, node2);
                }
            }
        }
    }

    private boolean isGreater(Vertex current, int index, Vertex[] alphainv) {
        for (int i = 1; i < alphainv.length; i++) {
            if (alphainv[i].getName().equals(current.getName()) && i < index) {
                return true;
            }
        }
        return false;
    }
}
