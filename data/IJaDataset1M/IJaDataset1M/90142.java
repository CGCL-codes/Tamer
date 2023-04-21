package edu.ksu.cis.bnj.ver3.inference.approximate.edgedeletion;

import java.util.StringTokenizer;
import java.util.Vector;
import edu.ksu.cis.bnj.ver3.core.BeliefNetwork;
import edu.ksu.cis.bnj.ver3.core.BeliefNode;
import edu.ksu.cis.bnj.ver3.core.CPF;
import edu.ksu.cis.bnj.ver3.inference.Inference;
import edu.ksu.cis.bnj.ver3.inference.exact.CliqueTree;
import edu.ksu.cis.bnj.ver3.inference.exact.LS;
import edu.ksu.cis.util.RMSE;
import edu.ksu.cis.util.driver.Options;
import edu.ksu.cis.util.graph.algorithms.BCT_AlreadyTriang;
import edu.ksu.cis.util.graph.algorithms.Moralization;
import edu.ksu.cis.util.graph.algorithms.RemoveDirectionality;
import edu.ksu.cis.util.graph.core.Graph;
import edu.ksu.cis.util.graph.core.Vertex;

/**
 * This class performs approximate inference of Bayesian networks
 * by deleting edges using Bodlaender's pre-processing rules for
 * triangulation to force the maximum clique size in the final
 * triangulated graph to be 5 or less.
 * 
 * It chooses edges for deletion whose removal maximizes the number
 * of unnecessary moral edges.
 * 
 * For information on running this algorithm, see the comments for
 * the main method at the bottom of the file.
 * 
 * @author Julie Thornton
 *
 */
public class BCSPreProcessing implements Inference {

    private Graph network;

    private Graph moralizedGraph;

    private BeliefNetwork origNetwork;

    private BeliefNetwork unchangedNet;

    private int low;

    private Vector removedVertices;

    private CPF[] marginals = null;

    private BeliefNode[] beliefnodes;

    private int[] alpha;

    private Vertex[] alphainv;

    private Vertex removeSource;

    private Vertex removeSink;

    private Vector moralEdges;

    public String getName() {
        return "Approximate Inference by Bounding Clique Sizes with Pre-Processing";
    }

    public void run(BeliefNetwork bn) {
        network = bn.getGraph();
        origNetwork = bn.copy();
        unchangedNet = bn.copy();
        low = 1;
        removedVertices = new Vector();
        long start = System.currentTimeMillis();
        applyReductionRules();
        doInference();
        long stop = System.currentTimeMillis();
        System.out.println("total time: " + (stop - start));
    }

    public CPF queryMarginal(BeliefNode bnode) {
        for (int i = 0; i < beliefnodes.length; i++) {
            if (bnode.getName().equals(beliefnodes[i].getName())) {
                return marginals[i];
            }
        }
        return null;
    }

    public void applyReductionRules() {
        moralize();
        findMoralEdges();
        moralizedGraph = network.copy();
        boolean appliedRule = false;
        long reductStart = System.currentTimeMillis();
        while (low < 5) {
            appliedRule = false;
            appliedRule = isletRule();
            if (!appliedRule) {
                appliedRule = twigRule();
            }
            if (!appliedRule && low >= 2) {
                appliedRule = seriesRule();
            }
            if (!appliedRule && low >= 3) {
                appliedRule = triangleRule();
            }
            if (!appliedRule && low >= 3) {
                appliedRule = buddyRule();
            }
            if (!appliedRule && low >= 3) {
                appliedRule = cubeRule();
            }
            if (!appliedRule) {
                appliedRule = simplicialRule();
            }
            if (!appliedRule) {
                low++;
            }
        }
        int oldEdges;
        while (moralizedGraph.getNumberOfVertices() != 0) {
            oldEdges = moralizedGraph.getNumberOfEdges();
            removeEdge();
            simplicialRule();
        }
    }

    private void findMoralEdges() {
        Vertex node1, node2;
        moralEdges = new Vector();
        for (int i = 0; i < network.getVertices().length; i++) {
            node1 = network.getVertices()[i];
            for (int j = 0; j < network.getParents(node1).length; j++) {
                node2 = network.getParents(node1)[j];
                if (!oldEdge(node1, node2)) {
                    moralEdges.addElement(node1.getName() + "->" + node2.getName());
                }
            }
        }
    }

    private boolean oldEdge(Vertex node1, Vertex node2) {
        for (int i = 0; i < origNetwork.getGraph().getParents(node1).length; i++) {
            if (origNetwork.getGraph().getParents(node1)[i].getName().equals(node2.getName())) {
                return true;
            }
        }
        for (int i = 0; i < origNetwork.getGraph().getParents(node2).length; i++) {
            if (origNetwork.getGraph().getParents(node2)[i].getName().equals(node1.getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isOrigAndMoral(String name1, String name2) {
        Vertex node1 = origNetwork.getGraph().getVertices()[getVertex(name1).loc()];
        Vertex node2 = origNetwork.getGraph().getVertices()[getVertex(name2).loc()];
        Vertex current;
        Vertex[] parents;
        boolean mark1 = false;
        boolean mark2 = false;
        for (int i = 0; i < origNetwork.getGraph().getVertices().length; i++) {
            current = origNetwork.getGraph().getVertices()[i];
            parents = origNetwork.getGraph().getParents(current);
            for (int j = 0; j < parents.length; j++) {
                if (parents[j].getName().equals(node1.getName())) {
                    mark1 = true;
                }
                if (parents[j].getName().equals(node2.getName())) {
                    mark2 = true;
                }
            }
            if (mark1 && mark2) {
                return true;
            }
            mark1 = false;
            mark2 = false;
        }
        return false;
    }

    private void moralize() {
        Moralization m = new Moralization();
        m.setGraph(network);
        m.execute();
        network = m.getGraph();
        RemoveDirectionality rd = new RemoveDirectionality();
        rd.setGraph(network);
        rd.execute();
        network = rd.getGraph();
    }

    private void removeEdge() {
        Vertex v1, v2;
        double bestScore = -1;
        Vertex bestSink = null;
        Vertex bestSource = null;
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            v1 = moralizedGraph.getVertices()[i];
            for (int j = 0; j < moralizedGraph.getChildren(v1).length; j++) {
                v2 = moralizedGraph.getChildren(v1)[j];
                if (isOrigEdge(getOrigVertex(v1.getName()), getOrigVertex(v2.getName())) && !isOrigAndMoral(v1.getName(), v2.getName())) {
                    double score = countMorals(v1, v2);
                    if (score > bestScore) {
                        removeSource = getOrigVertex(v1.getName());
                        removeSink = getOrigVertex(v2.getName());
                        bestSource = v1;
                        bestSink = v2;
                        bestScore = score;
                    }
                }
            }
        }
        if (bestSource != null && bestSink != null) {
            deleteExtraMorals();
            deleteFromOrig(bestSource.getName(), bestSink.getName());
            deleteFromMoral(bestSource.getName(), bestSink.getName());
        } else {
            for (int i = 0; i < origNetwork.getGraph().getVertices().length; i++) {
                v1 = origNetwork.getGraph().getVertices()[i];
                for (int j = 0; j < origNetwork.getGraph().getChildren(v1).length; j++) {
                    v2 = origNetwork.getGraph().getChildren(v1)[j];
                    if (isOrigEdge(getOrigVertex(v1.getName()), getOrigVertex(v2.getName())) && !isOrigAndMoral(v1.getName(), v2.getName())) {
                        double score = countMorals(v1, v2);
                        if (score > bestScore) {
                            removeSource = getOrigVertex(v1.getName());
                            removeSink = getOrigVertex(v2.getName());
                            bestSource = v1;
                            bestSink = v2;
                            bestScore = score;
                        }
                    }
                }
            }
            deleteExtraMorals();
            deleteFromOrig(bestSource.getName(), bestSink.getName());
            deleteFromMoral(bestSource.getName(), bestSink.getName());
        }
    }

    private Vector timesMoralNeeded(String name1, String name2) {
        Vertex node1 = origNetwork.getGraph().getVertices()[getVertex(name1).loc()];
        Vertex node2 = origNetwork.getGraph().getVertices()[getVertex(name2).loc()];
        Vertex current;
        Vertex[] parents;
        boolean mark1 = false;
        boolean mark2 = false;
        Vector moralSinks = new Vector();
        for (int i = 0; i < origNetwork.getGraph().getVertices().length; i++) {
            current = origNetwork.getGraph().getVertices()[i];
            parents = origNetwork.getGraph().getParents(current);
            for (int j = 0; j < parents.length; j++) {
                if (parents[j].getName().equals(node1.getName())) {
                    mark1 = true;
                }
                if (parents[j].getName().equals(node2.getName())) {
                    mark2 = true;
                }
            }
            if (mark1 && mark2) {
                moralSinks.addElement(current.getName());
            }
            mark1 = false;
            mark2 = false;
        }
        return moralSinks;
    }

    private int countMorals(Vertex v1, Vertex v2) {
        Vertex source, sink;
        Vertex delSource = getDelSource(v1, v2);
        if (delSource.getName().equals(v1.getName())) {
            sink = v2;
        } else {
            sink = v1;
        }
        Vertex[] otherParents = unchangedNet.getGraph().getParents(unchangedNet.getGraph().getVertices()[sink.loc()]);
        int count = 0;
        int timesNeeded;
        String moralSink;
        Vector needed;
        for (int i = 0; i < otherParents.length; i++) {
            source = otherParents[i];
            needed = timesMoralNeeded(source.getName(), delSource.getName());
            timesNeeded = needed.size();
            if (moralEdges.contains(source.getName() + "->" + delSource.getName()) && !oldEdge(source, delSource) && timesNeeded > 0 && timesNeeded < 2) {
                moralSink = needed.elementAt(0).toString();
                if (moralSink.equals(sink.getName())) {
                    count++;
                }
            }
        }
        return count;
    }

    private void deleteExtraMorals() {
        Vertex source, sink;
        Vertex delSource = getDelSource(removeSource, removeSink);
        if (delSource.getName().equals(removeSource.getName())) {
            sink = removeSink;
        } else {
            sink = removeSource;
        }
        Vertex[] otherParents = unchangedNet.getGraph().getParents(unchangedNet.getGraph().getVertices()[sink.loc()]);
        int timesNeeded;
        Vector needed;
        String moralSink;
        for (int i = 0; i < otherParents.length; i++) {
            source = otherParents[i];
            needed = timesMoralNeeded(source.getName(), delSource.getName());
            timesNeeded = needed.size();
            if (moralEdges.contains(source.getName() + "->" + delSource.getName()) && !oldEdge(source, delSource) && timesNeeded > 0 && timesNeeded < 2) {
                moralSink = needed.elementAt(0).toString();
                if (moralSink.equals(sink.getName())) {
                    network.removeEdge(network.getVertices()[source.loc()], network.getVertices()[delSource.loc()]);
                    network.removeEdge(network.getVertices()[delSource.loc()], network.getVertices()[source.loc()]);
                    origNetwork.disconnect(origNetwork.getNodes()[source.loc()], origNetwork.getNodes()[delSource.loc()]);
                    origNetwork.disconnect(origNetwork.getNodes()[delSource.loc()], origNetwork.getNodes()[source.loc()]);
                    Vertex moral1 = getMoralVertex(source.getName());
                    Vertex moral2 = getMoralVertex(delSource.getName());
                    if (moral1 != null && moral2 != null) {
                        moralizedGraph.removeEdge(getMoralVertex(source.getName()), getMoralVertex(delSource.getName()));
                        moralizedGraph.removeEdge(getMoralVertex(delSource.getName()), getMoralVertex(source.getName()));
                    }
                    moralEdges.remove(source.getName() + "->" + delSource.getName());
                    moralEdges.remove(delSource.getName() + "->" + source.getName());
                }
            }
        }
    }

    private void deleteFromMoral(String source, String sink) {
        Vertex sourceVertex, sinkVertex;
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            sourceVertex = moralizedGraph.getVertices()[i];
            for (int j = 0; j < moralizedGraph.getChildren(sourceVertex).length; j++) {
                sinkVertex = moralizedGraph.getChildren(sourceVertex)[j];
                if (sourceVertex.getName().equals(source) && sinkVertex.getName().equals(sink)) {
                    moralizedGraph.removeEdge(sourceVertex, sinkVertex);
                }
            }
        }
    }

    private Vertex getOrigVertex(String name) {
        for (int i = 0; i < origNetwork.getNodes().length; i++) {
            if (origNetwork.getNodes()[i].getName().equals(name)) {
                return origNetwork.getNodes()[i].getOwner();
            }
        }
        return null;
    }

    private boolean isOrigEdge(Vertex v1, Vertex v2) {
        Vertex[] children = origNetwork.getGraph().getChildren(v1);
        for (int i = 0; i < children.length; i++) {
            if (children[i].getName().equals(v2.getName())) {
                return true;
            }
        }
        children = origNetwork.getGraph().getChildren(v2);
        for (int i = 0; i < children.length; i++) {
            if (children[i].getName().equals(v1.getName())) {
                return true;
            }
        }
        return false;
    }

    private Vertex getMoralVertex(String name) {
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            if (moralizedGraph.getVertices()[i].getName().equals(name)) {
                return moralizedGraph.getVertices()[i];
            }
        }
        return null;
    }

    private Vertex getDelSource(Vertex v1, Vertex v2) {
        Vertex[] children1 = unchangedNet.getGraph().getChildren(unchangedNet.getGraph().getVertices()[v1.loc()]);
        for (int i = 0; i < children1.length; i++) {
            if (children1[i].getName().equals(v2.getName())) {
                return v1;
            }
        }
        return v2;
    }

    public void printGraph() {
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            for (int j = 0; j < moralizedGraph.getChildren(moralizedGraph.getVertices()[i]).length; j++) {
                System.out.print(moralizedGraph.getVertices()[i].getName() + "->");
                System.out.println(moralizedGraph.getChildren(moralizedGraph.getVertices()[i])[j].getName());
            }
        }
        System.out.println();
    }

    public void printOrigGraph() {
        for (int i = 0; i < unchangedNet.getGraph().getVertices().length; i++) {
            for (int j = 0; j < unchangedNet.getGraph().getChildren(unchangedNet.getGraph().getVertices()[i]).length; j++) {
                System.out.print(unchangedNet.getGraph().getVertices()[i].getName() + "->");
                System.out.println(unchangedNet.getGraph().getChildren(unchangedNet.getGraph().getVertices()[i])[j].getName());
            }
        }
        System.out.println();
    }

    private boolean simplicialRule() {
        Vertex current;
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            current = moralizedGraph.getVertices()[i];
            if (isSimplicial(current) && moralizedGraph.getParents(current).length <= low && moralizedGraph.getParents(current).length <= 4) {
                removedVertices.addElement(current.getName());
                deleteNode(current);
                return true;
            }
        }
        return false;
    }

    private boolean isSimplicial(Vertex node) {
        Vertex[] parents = moralizedGraph.getParents(node);
        for (int i = 0; i < parents.length; i++) {
            for (int j = i + 1; j < parents.length; j++) {
                if (!adjacent(parents[i], parents[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean twigRule() {
        Vertex current;
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            current = moralizedGraph.getVertices()[i];
            if (moralizedGraph.getChildren(current).length == 1) {
                removedVertices.addElement(current.getName());
                deleteNode(current);
                return true;
            }
        }
        return false;
    }

    private boolean isletRule() {
        Vertex current;
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            current = moralizedGraph.getVertices()[i];
            if (moralizedGraph.getChildren(current).length == 0) {
                removedVertices.addElement(current.getName());
                deleteNode(current);
                return true;
            }
        }
        return false;
    }

    private boolean seriesRule() {
        Vertex current;
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            current = moralizedGraph.getVertices()[i];
            Vertex child1, child2;
            if (moralizedGraph.getChildren(current).length == 2) {
                child1 = moralizedGraph.getChildren(current)[0];
                child2 = moralizedGraph.getChildren(current)[1];
                connect(child1, child2);
                removedVertices.addElement(current.getName());
                deleteNode(current);
                return true;
            }
        }
        return false;
    }

    private boolean triangleRule() {
        Vertex current;
        Vertex neighbor0, neighbor1, neighbor2;
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            current = moralizedGraph.getVertices()[i];
            if (moralizedGraph.getChildren(current).length == 3) {
                neighbor0 = moralizedGraph.getChildren(current)[0];
                neighbor1 = moralizedGraph.getChildren(current)[1];
                neighbor2 = moralizedGraph.getChildren(current)[2];
                if (adjacent(neighbor0, neighbor1)) {
                    connect(neighbor0, neighbor2);
                    connect(neighbor1, neighbor2);
                    removedVertices.addElement(current.getName());
                    deleteNode(current);
                    return true;
                } else if (adjacent(neighbor0, neighbor2)) {
                    connect(neighbor1, neighbor2);
                    connect(neighbor0, neighbor1);
                    removedVertices.addElement(current.getName());
                    deleteNode(current);
                    return true;
                } else if (adjacent(neighbor1, neighbor2)) {
                    connect(neighbor0, neighbor2);
                    connect(neighbor0, neighbor1);
                    removedVertices.addElement(current.getName());
                    deleteNode(current);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean adjacent(Vertex bn1, Vertex bn2) {
        Vertex[] bn1parents = moralizedGraph.getParents(bn1);
        for (int i = 0; i < bn1parents.length; i++) {
            if (bn1parents[i].getName().equals(bn2.getName())) {
                return true;
            }
        }
        return false;
    }

    private void connect(Vertex bn1, Vertex bn2) {
        moralizedGraph.addUndirectedEdge(bn1, bn2);
    }

    private boolean buddyRule() {
        Vertex node1, node2;
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            for (int j = i + 1; j < moralizedGraph.getVertices().length; j++) {
                node1 = moralizedGraph.getVertices()[i];
                node2 = moralizedGraph.getVertices()[j];
                if (moralizedGraph.getParents(node1).length == 3 && moralizedGraph.getParents(node2).length == 3 && sameParents(node1, node2)) {
                    connect(moralizedGraph.getParents(node1)[0], moralizedGraph.getParents(node1)[1]);
                    connect(moralizedGraph.getParents(node1)[0], moralizedGraph.getParents(node1)[2]);
                    connect(moralizedGraph.getParents(node1)[1], moralizedGraph.getParents(node1)[2]);
                    deleteNode(node1);
                    deleteNode(node2);
                    removedVertices.addElement(node1.getName());
                    removedVertices.addElement(node2.getName());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean sameParents(Vertex node1, Vertex node2) {
        Vertex[] parents1 = moralizedGraph.getParents(node1);
        Vertex[] parents2 = moralizedGraph.getParents(node2);
        boolean found;
        for (int i = 0; i < parents1.length; i++) {
            found = false;
            for (int j = 0; j < parents2.length; j++) {
                if (parents1[i].getName().equals(parents2[j].getName())) {
                    found = true;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private boolean cubeRule() {
        Vertex a, b, c, d, v, w, x;
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            d = moralizedGraph.getVertices()[i];
            if (moralizedGraph.getParents(d).length == 3) {
                a = moralizedGraph.getParents(d)[0];
                b = moralizedGraph.getParents(d)[1];
                c = moralizedGraph.getParents(d)[2];
                if (moralizedGraph.getParents(a).length == 2 && moralizedGraph.getParents(b).length == 2 && moralizedGraph.getParents(c).length == 2) {
                    v = findCommonNode(a, b);
                    w = findCommonNode(a, c);
                    x = findCommonNode(b, c);
                    if (v == null || w == null || x == null) {
                        break;
                    }
                    if (moralizedGraph.getParents(v).length != 2 || moralizedGraph.getParents(w).length != 2 || moralizedGraph.getParents(x).length != 2) {
                        break;
                    }
                    if (!v.getName().equals(w.getName()) && !v.getName().equals(x.getName()) && !w.getName().equals(x.getName())) {
                        connect(w, v);
                        connect(v, x);
                        connect(w, x);
                        deleteNode(a);
                        deleteNode(b);
                        deleteNode(c);
                        deleteNode(d);
                        removedVertices.addElement(a.getName());
                        removedVertices.addElement(b.getName());
                        removedVertices.addElement(c.getName());
                        removedVertices.addElement(d.getName());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Vertex findCommonNode(Vertex v1, Vertex v2) {
        Vertex[] parents1 = moralizedGraph.getParents(v1);
        Vertex[] parents2 = moralizedGraph.getParents(v2);
        boolean common = false;
        String name = "";
        for (int i = 0; i < parents1.length; i++) {
            for (int j = 0; j < parents2.length; j++) {
                if (parents1[i].getName().equals(parents2[j].getName()) && !common) {
                    common = true;
                    name = parents1[i].getName();
                } else if (common) {
                    return null;
                }
            }
        }
        if (common) {
            for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
                if (name.equals(moralizedGraph.getVertices()[i].getName())) {
                    return moralizedGraph.getVertices()[i];
                }
            }
        }
        return null;
    }

    private void printMorals() {
        for (int i = 0; i < moralEdges.size(); i++) {
            System.out.println(moralEdges.elementAt(i).toString());
        }
    }

    public void doInference() {
        int count = 0;
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            disconnectFromOrig(moralizedGraph.getVertices()[i].getName());
        }
        BCT_AlreadyTriang bct = new BCT_AlreadyTriang();
        bct.setGraph(network);
        calcAlpha();
        moralizedGraph = null;
        bct.setOriginal(origNetwork.getGraph());
        bct.execute(alpha, alphainv);
        network = null;
        long start = System.currentTimeMillis();
        CliqueTree CT = new CliqueTree(bct, origNetwork);
        CT.begin();
        long stop = System.currentTimeMillis();
        System.out.println("inference time: " + (stop - start));
        marginals = CT.Marginalize();
        beliefnodes = CT.getNodes();
    }

    private void disconnectFromOrig(String name) {
        BeliefNode node1 = null;
        for (int i = 0; i < origNetwork.getNodes().length; i++) {
            if (origNetwork.getNodes()[i].getName().equals(name)) {
                node1 = origNetwork.getNodes()[i];
            }
        }
        BeliefNode[] parents = origNetwork.getParents(node1);
        BeliefNode[] children = origNetwork.getChildren(node1);
        for (int i = 0; i < parents.length; i++) {
            if (!inReducedGraph(parents[i].getName())) {
                origNetwork.disconnect(parents[i], node1);
                network.removeEdge(network.getVertices()[parents[i].loc()], network.getVertices()[node1.loc()]);
                network.removeEdge(network.getVertices()[node1.loc()], network.getVertices()[parents[i].loc()]);
                removeSource = getOrigVertex(node1.getName());
                removeSink = getOrigVertex(parents[i].getName());
                deleteExtraMorals();
            }
        }
        for (int i = 0; i < children.length; i++) {
            if (!inReducedGraph(children[i].getName())) {
                origNetwork.disconnect(node1, children[i]);
                network.removeEdge(network.getVertices()[children[i].loc()], network.getVertices()[node1.loc()]);
                network.removeEdge(network.getVertices()[node1.loc()], network.getVertices()[children[i].loc()]);
            }
        }
    }

    private boolean inReducedGraph(String name) {
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            if (moralizedGraph.getVertices()[i].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void calcAlpha() {
        alpha = new int[network.getNumberOfVertices()];
        alphainv = new Vertex[alpha.length + 1];
        String name;
        Vertex v;
        int index = alpha.length;
        for (int i = 0; i < removedVertices.size(); i++) {
            name = removedVertices.elementAt(i).toString();
            v = getVertex(name);
            alpha[v.loc()] = index;
            alphainv[index] = v;
            index--;
        }
        for (int i = 0; i < moralizedGraph.getVertices().length; i++) {
            name = moralizedGraph.getVertices()[i].getName();
            System.out.println("NOT DELETED: " + name);
            v = getVertex(name);
            alpha[v.loc()] = index;
            alphainv[index] = v;
            index--;
        }
    }

    private Vertex getVertex(String name) {
        for (int i = 0; i < network.getVertices().length; i++) {
            if (network.getVertices()[i].getName().equals(name)) {
                return network.getVertices()[i];
            }
        }
        return null;
    }

    private void deleteFromOrig(String source, String sink) {
        Vertex sourceVertex, sinkVertex;
        BeliefNode sourceNode, sinkNode;
        for (int i = 0; i < network.getVertices().length; i++) {
            sourceVertex = network.getVertices()[i];
            for (int j = 0; j < network.getChildren(sourceVertex).length; j++) {
                sinkVertex = network.getChildren(sourceVertex)[j];
                if (sourceVertex.getName().equals(source) && sinkVertex.getName().equals(sink)) {
                    network.removeEdge(sourceVertex, sinkVertex);
                }
            }
        }
        for (int i = 0; i < origNetwork.getNodes().length; i++) {
            sourceNode = origNetwork.getNodes()[i];
            for (int j = 0; j < origNetwork.getChildren(sourceNode).length; j++) {
                sinkNode = origNetwork.getChildren(sourceNode)[j];
                if (sourceNode.getName().equals(source) && sinkNode.getName().equals(sink)) {
                    origNetwork.disconnect(sourceNode, sinkNode);
                }
            }
        }
        for (int i = 0; i < origNetwork.getNodes().length; i++) {
            sourceNode = origNetwork.getNodes()[i];
            for (int j = 0; j < origNetwork.getChildren(sourceNode).length; j++) {
                sinkNode = origNetwork.getChildren(sourceNode)[j];
                if (sourceNode.getName().equals(sink) && sinkNode.getName().equals(source)) {
                    origNetwork.disconnect(sourceNode, sinkNode);
                }
            }
        }
    }

    private void deleteNode(Vertex node) {
        Vertex[] parents = moralizedGraph.getParents(node);
        Vertex[] children = moralizedGraph.getChildren(node);
        for (int i = 0; i < parents.length; i++) {
            moralizedGraph.removeEdge(parents[i], node);
        }
        for (int i = 0; i < children.length; i++) {
            moralizedGraph.removeEdge(node, children[i]);
        }
        moralizedGraph.removeVertex(node);
    }

    public static void main(String[] args) {
        String filename = "cpcs-179.xml";
        BeliefNetwork bn = Options.load(filename);
        BeliefNetwork orig = bn.copy();
        BCSPreProcessing tr = new BCSPreProcessing();
        tr.run(bn);
        long start = System.currentTimeMillis();
        LS ls = new LS();
        ls.run(orig);
        long stop = System.currentTimeMillis();
        System.out.println("LS time: " + (stop - start));
        BeliefNode[] nodes = bn.getNodes();
        CPF[] lsMarginals = new CPF[nodes.length];
        CPF[] approxMarginals = new CPF[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            BeliefNode x = nodes[i];
            lsMarginals[i] = ls.queryMarginal(x);
            approxMarginals[i] = tr.queryMarginal(x);
        }
        System.out.println("The RMSE is: " + RMSE.computeRMSE(lsMarginals, approxMarginals).getExpr());
    }
}
