package clustering.implementations;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import clustering.framework.*;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DQPTreeConstructor implements IClusterTreeConstructor {

    QNode[] qNodes = null;

    int last_operation = -1;

    Hashtable htPath = new Hashtable();

    public String[] filesList = null;

    int[] selected_q = null;

    public Quartet getRandomQuartet(ArrayList alQuartets) {
        Random rand = new Random();
        int index = rand.nextInt(alQuartets.size());
        while (selected_q[index] == 1) {
            index = rand.nextInt(alQuartets.size());
        }
        selected_q[index] = 1;
        return (Quartet) alQuartets.get(index);
    }

    public int[] generateRandomNodesList(Quartet q, int n) {
        Random rand = new Random();
        int index = rand.nextInt(n);
        int[] selected_t = new int[n];
        int[] list = new int[n - 4];
        for (int i = 0; i < n - 4; i++) {
            while (selected_t[index] == 1 || index == q.nodes[0] || index == q.nodes[1] || index == q.nodes[2] || index == q.nodes[3]) {
                index = rand.nextInt(n);
            }
            list[i] = index;
            selected_t[index] = 1;
        }
        return list;
    }

    ArrayList alEdges = null;

    double last_tree_score;

    void addNodeInBestPlace(QuartetTree qt, int node_index, double[][] dm) {
        QNode qn = getBestNode(node_index, dm);
        TreeEdge te = new TreeEdge(qn, qn.adj[0]);
        connectNode(qt, te, node_index, new QNode(-1, null, null, null, -1), new QNode(-1, null, null, null, -1));
    }

    QNode getBestNode(int node_index, double[][] dm) {
        QNode min_node = null;
        double min_dist = Double.MAX_VALUE;
        for (int i = 0; i < node_count; i++) {
            QNode node = qNodes[i];
            if (node.label == -1) continue;
            if (dm[node.label][node_index] < min_dist) {
                min_dist = dm[node.label][node_index];
                min_node = node;
            }
        }
        return min_node;
    }

    void populateEdgesList() {
        alEdges.clear();
        for (int i = 0; i < node_count; i++) {
            QNode qn = qNodes[i];
            if (qn.label == -1) {
                for (int j = 0; j < 3; j++) {
                    TreeEdge te = new TreeEdge(qn, qn.adj[j]);
                    if (!containsEdge(te)) {
                        alEdges.add(te);
                    }
                }
            }
        }
    }

    boolean containsEdge(TreeEdge te) {
        for (int i = 0; i < alEdges.size(); i++) {
            TreeEdge t = (TreeEdge) alEdges.get(i);
            if ((t.q1 == te.q1 && t.q2 == te.q2) || (t.q2 == te.q1 && t.q1 == te.q2)) return true;
        }
        return false;
    }

    boolean treeContainsQuartet(Quartet q) {
        int count = 0;
        for (int i = 0; i < node_count; i++) {
            QNode qn = (QNode) qNodes[i];
            if (qn.label == q.nodes[0] || qn.label == q.nodes[1] || qn.label == q.nodes[2] || qn.label == q.nodes[3]) count++;
        }
        if (count == 4) {
            return true;
        }
        return false;
    }

    double computeTreeScore(QuartetTree qt) {
        double Ct = 0;
        double count = 0;
        for (int i = 0; i < alQuartets.size(); i++) {
            Quartet q = (Quartet) alQuartets.get(i);
            if (treeContainsQuartet(q)) {
                if (isConsistent(q, qt)) {
                    Ct += q.weight;
                    count++;
                }
            }
        }
        return Ct;
    }

    void disconnectNode(QNode qn) {
        QNode nr = qn.adj[0];
        QNode qn1 = nr.adj[0];
        QNode qn2 = nr.adj[1];
        for (int i = 0; i < 3; i++) {
            if (qn1.adj[i] == nr) qn1.adj[i] = qn2;
            if (qn2.adj[i] == nr) qn2.adj[i] = qn1;
        }
        node_count -= 2;
    }

    void connectNode(QuartetTree qt, TreeEdge te, int node_index, QNode cn_qn, QNode cn_qr) {
        cn_qn.label = node_index;
        cn_qr.label = -1;
        cn_qr.adj[0] = te.q1;
        cn_qr.adj[1] = te.q2;
        cn_qr.adj[2] = cn_qn;
        cn_qn.adj[0] = cn_qr;
        cn_qn.adj[1] = null;
        cn_qn.adj[2] = null;
        for (int i = 0; i < 3; i++) {
            if (te.q1.adj[i] == te.q2) te.q1.adj[i] = cn_qr;
        }
        for (int i = 0; i < 3; i++) {
            if (te.q2.adj[i] == te.q1) te.q2.adj[i] = cn_qr;
        }
        qNodes[node_count++] = cn_qr;
        qNodes[node_count++] = cn_qn;
    }

    ArrayList alQuartets = null;

    public ArrayList generateQuartetList(double[][] dm, Quartet q) {
        ArrayList alQuartets = new ArrayList();
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                for (int k = j + 1; k < 4; k++) {
                    for (int l = k + 1; l < 4; l++) {
                        Quartet q1 = new Quartet(q.nodes[i], q.nodes[j], q.nodes[k], q.nodes[l], dm);
                        alQuartets.add(q1);
                        Quartet q2 = new Quartet(q.nodes[i], q.nodes[k], q.nodes[j], q.nodes[l], dm);
                        alQuartets.add(q2);
                        Quartet q3 = new Quartet(q.nodes[i], q.nodes[l], q.nodes[j], q.nodes[k], dm);
                        alQuartets.add(q3);
                    }
                }
            }
        }
        return alQuartets;
    }

    public String ConstructXMLTree(double[][] dDistanceMatrix) throws Exception {
        qNodes = new QNode[2 * dDistanceMatrix.length - 2];
        ArrayList alBestQuartets = Quartet.generateBestQuartetList(dDistanceMatrix);
        alQuartets = Quartet.generateFullQuartetList(dDistanceMatrix);
        QuartetTree qt = null;
        selected_q = new int[alBestQuartets.size()];
        alEdges = new ArrayList();
        QNode[] best_nodes = null;
        double best_score = Double.MAX_VALUE;
        for (int i = 0; i < 100; i++) {
            alEdges.clear();
            qt = new QuartetTree();
            Quartet q = null;
            q = getRandomQuartet(alBestQuartets);
            initializeFromQuartet(qt, q);
            int[] nodes_list = generateRandomNodesList(q, dDistanceMatrix.length);
            for (int j = 0; j < nodes_list.length; j++) {
                addNodeInBestPlace(qt, nodes_list[j], dDistanceMatrix);
            }
            last_tree_score = computeTreeScore(qt);
            if (last_tree_score < best_score) {
                best_score = last_tree_score;
                best_nodes = this.cloneQuartetNodes(qNodes, qt);
            }
        }
        qNodes = best_nodes;
        qt.root = this.getRandomInternNode();
        qt.St = (Quartet.worst_cost.doubleValue() - best_score) / (Quartet.worst_cost.doubleValue() - Quartet.best_cost.doubleValue());
        return qt.toString();
    }

    int getNodeIndex(QNode qn, QNode[] nodesList) {
        for (int i = 0; i < nodesList.length; i++) {
            if (nodesList[i] == qn) return i;
        }
        return -1;
    }

    QNode[] cloneQuartetNodes(QNode[] listNodes, QuartetTree qt) {
        QNode[] newNodes = new QNode[listNodes.length];
        for (int i = 0; i < newNodes.length; i++) newNodes[i] = new QNode(-1, null, null, null, -1);
        for (int i = 0; i < listNodes.length; i++) {
            newNodes[i].label = listNodes[i].label;
            for (int j = 0; j < newNodes[i].adj.length; j++) {
                int index = getNodeIndex(listNodes[i].adj[j], listNodes);
                if (index != -1) newNodes[i].adj[j] = newNodes[index];
            }
        }
        qt.root = getRandomInternNode();
        return newNodes;
    }

    boolean isConsistent(Quartet q, QuartetTree qt) {
        QNode[] qNodes = new QNode[node_count];
        for (int i = 0; i < node_count; i++) {
            qNodes[i] = this.qNodes[i];
        }
        QNode a = getNodeByLabel(q.nodes[0]);
        QNode b = getNodeByLabel(q.nodes[1]);
        QNode c = getNodeByLabel(q.nodes[2]);
        QNode d = getNodeByLabel(q.nodes[3]);
        if (a == null || b == null || c == null || d == null) return false;
        if (htPath.get(a) == null) {
            htPath.put(a, qt.Dijkstra(a, qNodes));
        }
        if (htPath.get(b) == null) {
            htPath.put(b, qt.Dijkstra(b, qNodes));
        }
        if (htPath.get(c) == null) {
            htPath.put(c, qt.Dijkstra(c, qNodes));
        }
        if (htPath.get(d) == null) {
            htPath.put(d, qt.Dijkstra(d, qNodes));
        }
        ArrayList path1 = (ArrayList) ((Hashtable) htPath.get(a)).get(b);
        ArrayList path2 = (ArrayList) ((Hashtable) htPath.get(c)).get(d);
        for (int i = 1; i < path1.size() - 1; i++) {
            QNode qn1 = (QNode) path1.get(i);
            for (int j = 1; j < path2.size() - 1; j++) {
                QNode qn2 = (QNode) path2.get(j);
                if (qn1 == qn2) return false;
            }
        }
        return true;
    }

    QNode getNodeByLabel(int label) {
        for (int i = 0; i < node_count; i++) {
            QNode qn = qNodes[i];
            if (qn.label == label) return qn;
        }
        return null;
    }

    Random rand = new Random();

    QNode getRandomNode() {
        int k = rand.nextInt(qNodes.length);
        return qNodes[k];
    }

    QNode getRandomLeafNode() {
        QNode qn = getRandomNode();
        while (qn.label == -1) qn = getRandomNode();
        return qn;
    }

    QNode getRandomInternNode() {
        QNode qn = getRandomNode();
        while (qn.label != -1) qn = getRandomNode();
        return qn;
    }

    public void initializeFromQuartet(QuartetTree qt, Quartet q) {
        node_count = 0;
        qNodes[node_count++] = qt.root;
        qt.root.adj[0] = new QNode(-1, qt.root, null, null, -1);
        qNodes[node_count++] = qt.root.adj[0];
        qt.root.adj[1] = new QNode(q.nodes[0], qt.root, null, null, -1);
        qNodes[node_count++] = qt.root.adj[1];
        qt.root.adj[2] = new QNode(q.nodes[1], qt.root, null, null, -1);
        qNodes[node_count++] = qt.root.adj[2];
        qt.root.adj[0].adj[1] = new QNode(q.nodes[2], qt.root.adj[0], null, null, -1);
        qNodes[node_count++] = qt.root.adj[0].adj[1];
        qt.root.adj[0].adj[2] = new QNode(q.nodes[3], qt.root.adj[0], null, null, -1);
        qNodes[node_count++] = qt.root.adj[0].adj[2];
    }

    int node_count = 0;
}
