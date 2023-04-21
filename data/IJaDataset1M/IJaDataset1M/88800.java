package pogvue.analysis;

import pogvue.datamodel.SequenceI;
import pogvue.datamodel.SequenceNode;
import pogvue.gui.schemes.ResidueProperties;
import pogvue.util.Comparison;
import pogvue.datamodel.*;
import java.util.Vector;

public final class NJTree {

    private Vector cluster;

    private SequenceI[] sequence;

    private int[] done;

    private int noseqs;

    private int noClus;

    private float[][] distance;

    private int mini;

    private int minj;

    private float ri;

    private float rj;

    private final Vector groups = new Vector();

    private SequenceNode maxdist;

    private SequenceNode top;

    private float maxDistValue;

    private float maxheight;

    private int ycount;

    private Vector node;

    private String type;

    private String pwtype;

    private Object found = null;

    final Object leaves = null;

    private int start;

    private int end;

    public NJTree(SequenceNode node) {
        top = node;
        maxheight = findHeight(top);
    }

    public NJTree(SequenceI[] sequence, int start, int end) {
        this(sequence, "NJ", "BL", start, end);
    }

    public NJTree(SequenceI[] sequence, String type, String pwtype, int start, int end) {
        this.sequence = sequence;
        this.node = new Vector();
        this.type = type;
        this.pwtype = pwtype;
        this.start = start;
        this.end = end;
        System.out.println("Start " + start + "  : " + end);
        if (!(type.equals("NJ"))) {
            type = "AV";
        }
        if (!(pwtype.equals("PID"))) {
            type = "BL";
        }
        int i = 0;
        done = new int[sequence.length];
        while (i < sequence.length && sequence[i] != null) {
            done[i] = 0;
            i++;
        }
        noseqs = i++;
        distance = findDistances();
        makeLeaves();
        noClus = cluster.size();
        cluster();
    }

    private void cluster() {
        while (noClus > 2) {
            if (type.equals("NJ")) {
                float mind = findMinNJDistance();
            } else {
                float mind = findMinDistance();
            }
            Cluster c = joinClusters(mini, minj);
            done[minj] = 1;
            cluster.setElementAt(null, minj);
            cluster.setElementAt(c, mini);
            noClus--;
        }
        boolean onefound = false;
        int one = -1;
        int two = -1;
        for (int i = 0; i < noseqs; i++) {
            if (done[i] != 1) {
                if (!onefound) {
                    two = i;
                    onefound = true;
                } else {
                    one = i;
                }
            }
        }
        Cluster c = joinClusters(one, two);
        top = (SequenceNode) (node.elementAt(one));
        reCount(top);
        findHeight(top);
        findMaxDist(top);
    }

    private Cluster joinClusters(int i, int j) {
        System.out.println("Size " + i + " " + j + " " + distance);
        float dist = distance[i][j];
        int noi = ((Cluster) cluster.elementAt(i)).value.length;
        int noj = ((Cluster) cluster.elementAt(j)).value.length;
        int[] value = new int[noi + noj];
        for (int ii = 0; ii < noi; ii++) {
            value[ii] = ((Cluster) cluster.elementAt(i)).value[ii];
        }
        for (int ii = noi; ii < noi + noj; ii++) {
            value[ii] = ((Cluster) cluster.elementAt(j)).value[ii - noi];
        }
        Cluster c = new Cluster(value);
        ri = findr(i, j);
        rj = findr(j, i);
        if (type.equals("NJ")) {
            findClusterNJDistance(i, j);
        } else {
            findClusterDistance(i, j);
        }
        SequenceNode sn = new SequenceNode();
        sn.setLeft((SequenceNode) (node.elementAt(i)));
        sn.setRight((SequenceNode) (node.elementAt(j)));
        SequenceNode tmpi = (SequenceNode) (node.elementAt(i));
        SequenceNode tmpj = (SequenceNode) (node.elementAt(j));
        if (type.equals("NJ")) {
            findNewNJDistances(tmpi, tmpj, dist);
        } else {
            findNewDistances(tmpi, tmpj, dist);
        }
        tmpi.setParent(sn);
        tmpj.setParent(sn);
        node.setElementAt(sn, i);
        return c;
    }

    private void findNewNJDistances(SequenceNode tmpi, SequenceNode tmpj, float dist) {
        float ih = 0;
        float jh = 0;
        SequenceNode sni = tmpi;
        SequenceNode snj = tmpj;
        tmpi.dist = (dist + ri - rj) / 2;
        tmpj.dist = (dist - tmpi.dist);
        if (tmpi.dist < 0) {
            tmpi.dist = 0;
        }
        if (tmpj.dist < 0) {
            tmpj.dist = 0;
        }
    }

    private void findNewDistances(SequenceNode tmpi, SequenceNode tmpj, float dist) {
        float ih = 0;
        float jh = 0;
        SequenceNode sni = tmpi;
        SequenceNode snj = tmpj;
        while (sni != null) {
            ih = ih + sni.dist;
            sni = (SequenceNode) sni.left();
        }
        while (snj != null) {
            jh = jh + snj.dist;
            snj = (SequenceNode) snj.left();
        }
        tmpi.dist = (dist / 2 - ih);
        tmpj.dist = (dist / 2 - jh);
    }

    private void findClusterDistance(int i, int j) {
        int noi = ((Cluster) cluster.elementAt(i)).value.length;
        int noj = ((Cluster) cluster.elementAt(j)).value.length;
        float[] newdist = new float[noseqs];
        for (int l = 0; l < noseqs; l++) {
            if (l != i && l != j) {
                newdist[l] = (distance[i][l] * noi + distance[j][l] * noj) / (noi + noj);
            } else {
                newdist[l] = 0;
            }
        }
        for (int ii = 0; ii < noseqs; ii++) {
            distance[i][ii] = newdist[ii];
            distance[ii][i] = newdist[ii];
        }
    }

    private void findClusterNJDistance(int i, int j) {
        int noi = ((Cluster) cluster.elementAt(i)).value.length;
        int noj = ((Cluster) cluster.elementAt(j)).value.length;
        float[] newdist = new float[noseqs];
        for (int l = 0; l < noseqs; l++) {
            if (l != i && l != j) {
                newdist[l] = (distance[i][l] + distance[j][l] - distance[i][j]) / 2;
            } else {
                newdist[l] = 0;
            }
        }
        for (int ii = 0; ii < noseqs; ii++) {
            distance[i][ii] = newdist[ii];
            distance[ii][i] = newdist[ii];
        }
    }

    private float findr(int i, int j) {
        float tmp = 1;
        for (int k = 0; k < noseqs; k++) {
            if (k != i && k != j && done[k] != 1) {
                tmp = tmp + distance[i][k];
            }
        }
        if (noClus > 2) {
            tmp = tmp / (noClus - 2);
        }
        return tmp;
    }

    private float findMinNJDistance() {
        float min = 100000;
        for (int i = 0; i < noseqs - 1; i++) {
            for (int j = i + 1; j < noseqs; j++) {
                if (done[i] != 1 && done[j] != 1) {
                    float tmp = distance[i][j] - (findr(i, j) + findr(j, i));
                    if (tmp < min) {
                        mini = i;
                        minj = j;
                        min = tmp;
                    }
                }
            }
        }
        return min;
    }

    private float findMinDistance() {
        float min = 100000;
        for (int i = 0; i < noseqs - 1; i++) {
            for (int j = i + 1; j < noseqs; j++) {
                if (done[i] != 1 && done[j] != 1) {
                    if (distance[i][j] < min) {
                        mini = i;
                        minj = j;
                        min = distance[i][j];
                    }
                }
            }
        }
        return min;
    }

    private float[][] findDistances() {
        float[][] distance = new float[noseqs][noseqs];
        if (pwtype.equals("PID")) {
            for (int i = 0; i < noseqs - 1; i++) {
                for (int j = i; j < noseqs; j++) {
                    if (j == i) {
                        distance[i][i] = 0;
                    } else {
                        distance[i][j] = 100 - Comparison.compare(sequence[i], sequence[j], start, end);
                        distance[j][i] = distance[i][j];
                    }
                }
            }
        } else if (pwtype.equals("BL")) {
            int maxscore = 0;
            for (int i = 0; i < noseqs - 1; i++) {
                for (int j = i; j < noseqs; j++) {
                    int score = 0;
                    for (int k = 0; k < sequence[i].getLength(); k++) {
                        score += ResidueProperties.getBLOSUM62(sequence[i].getSequence(k, k + 1), sequence[j].getSequence(k, k + 1));
                    }
                    distance[i][j] = (float) score;
                    if (score > maxscore) {
                        maxscore = score;
                    }
                }
            }
            for (int i = 0; i < noseqs - 1; i++) {
                for (int j = i; j < noseqs; j++) {
                    distance[i][j] = (float) maxscore - distance[i][j];
                    distance[j][i] = distance[i][j];
                }
            }
        } else if (pwtype.equals("SW")) {
            float max = -1;
            for (int i = 0; i < noseqs - 1; i++) {
                for (int j = i; j < noseqs; j++) {
                    AlignSeq as = new AlignSeq(sequence[i], sequence[j], "pep");
                    as.calcScoreMatrix();
                    as.traceAlignment();
                    as.printAlignment();
                    distance[i][j] = (float) as.maxscore;
                    if (max < distance[i][j]) {
                        max = distance[i][j];
                    }
                }
            }
            for (int i = 0; i < noseqs - 1; i++) {
                for (int j = i; j < noseqs; j++) {
                    distance[i][j] = max - distance[i][j];
                    distance[j][i] = distance[i][j];
                }
            }
        }
        return distance;
    }

    private void makeLeaves() {
        cluster = new Vector();
        for (int i = 0; i < noseqs; i++) {
            SequenceNode sn = new SequenceNode();
            sn.setElement(sequence[i]);
            sn.setName(sequence[i].getName());
            node.addElement(sn);
            int[] value = new int[1];
            value[0] = i;
            Cluster c = new Cluster(value);
            cluster.addElement(c);
        }
    }

    public Vector findLeaves(SequenceNode node, Vector leaves) {
        if (node == null) {
            return leaves;
        }
        if (node.left() == null && node.right() == null) {
            leaves.addElement(node);
            return leaves;
        } else {
            findLeaves((SequenceNode) node.left(), leaves);
            findLeaves((SequenceNode) node.right(), leaves);
        }
        return leaves;
    }

    public Object findLeaf(SequenceNode node, int count) {
        found = _findLeaf(node, count);
        return found;
    }

    private Object _findLeaf(SequenceNode node, int count) {
        if (node == null) {
            return null;
        }
        if (node.ycount == count) {
            found = node.element();
            return found;
        } else {
            _findLeaf((SequenceNode) node.left(), count);
            _findLeaf((SequenceNode) node.right(), count);
        }
        return found;
    }

    public void printNode(SequenceNode node) {
        if (node == null) {
            return;
        }
        System.out.println("Node " + node);
        if (node.left() == null && node.right() == null) {
            System.out.println("Leaf = " + ((SequenceI) node.element()).getName());
            System.out.println("Dist " + node.dist);
            System.out.println("Boot " + node.getBootstrap());
        } else {
            System.out.println("Dist " + node.dist);
            printNode((SequenceNode) node.left());
            printNode((SequenceNode) node.right());
        }
    }

    private void findMaxDist(SequenceNode node) {
        if (node == null) {
            return;
        }
        if (node.left() == null && node.right() == null) {
            float dist = node.dist;
            if (dist > maxDistValue) {
                maxdist = node;
                maxDistValue = dist;
            }
        } else {
            findMaxDist((SequenceNode) node.left());
            findMaxDist((SequenceNode) node.right());
        }
    }

    public Vector getGroups() {
        return groups;
    }

    public float getMaxHeight() {
        return maxheight;
    }

    public void groupNodes(SequenceNode node, float threshold) {
        if (node == null) {
            return;
        }
        if (node.height / maxheight > threshold) {
            groups.addElement(node);
        } else {
            groupNodes((SequenceNode) node.left(), threshold);
            groupNodes((SequenceNode) node.right(), threshold);
        }
    }

    public float findHeight(SequenceNode node) {
        if (node == null) {
            return maxheight;
        }
        if (node.left() == null && node.right() == null) {
            node.height = ((SequenceNode) node.parent()).height + node.dist;
            if (node.height > maxheight) {
                return node.height;
            } else {
                return maxheight;
            }
        } else {
            if (node.parent() != null) {
                node.height = ((SequenceNode) node.parent()).height + node.dist;
            } else {
                maxheight = 0;
                node.height = (float) 0.0;
            }
            maxheight = findHeight((SequenceNode) (node.left()));
            maxheight = findHeight((SequenceNode) (node.right()));
        }
        return maxheight;
    }

    public SequenceNode root(SequenceNode node) {
        SequenceNode newtop = new SequenceNode();
        newtop.setLeft(node.left());
        newtop.setRight(node);
        SequenceNode left = (SequenceNode) newtop.left();
        SequenceNode right = (SequenceNode) newtop.right();
        left.dist = left.dist / 2;
        right.dist = left.dist;
        System.out.println("Creating new top node " + newtop.left() + " " + newtop.right());
        SequenceNode tmp = (SequenceNode) node.parent;
        node.parent = newtop;
        node.setLeft(tmp);
        while (tmp.parent != null) {
            System.out.println("Node " + tmp + " " + tmp.parent);
            SequenceNode tmp2 = (SequenceNode) tmp.left();
            System.out.println("Tmp2 " + tmp2);
            tmp.setLeft((SequenceNode) tmp.parent);
            tmp.parent = tmp2;
            tmp = (SequenceNode) tmp.left();
            System.out.println("Node " + tmp + " " + tmp.parent);
        }
        System.out.println("Left node is " + tmp.left());
        SequenceNode tmp3 = (SequenceNode) tmp.left();
        tmp3.setLeft(tmp.right());
        tmp.right().parent = tmp3;
        ((SequenceNode) tmp.right()).dist = ((SequenceNode) tmp.right()).dist + ((SequenceNode) tmp3).dist;
        System.out.println("Right node is " + tmp.right());
        pogvue.io.TreeFile.checkNode(newtop);
        top = newtop;
        reCount(top);
        findHeight(top);
        return top;
    }

    public void deleteNode(SequenceNode node) {
        node.setLeft(null);
        node.setRight(null);
        node.setName("NewNode");
        node.setElement(new Sequence("NewNode", "", 0, 0));
        reCount(top);
        findHeight(top);
    }

    public void addNode(SequenceNode node) {
        SequenceNode newnode1 = new SequenceNode();
        SequenceNode newnode2 = new SequenceNode();
        if (node.parent.left() == node) {
            node.parent.setLeft(newnode1);
        } else {
            node.parent.setRight(newnode1);
        }
        newnode2.setName("NewNode");
        newnode2.dist = node.dist;
        newnode2.parent = newnode1;
        newnode2.setElement(new Sequence("NewNode", "", 0, 0));
        newnode1.parent = node.parent;
        newnode1.dist = node.dist / 2;
        newnode1.setLeft(node);
        newnode1.setRight(newnode2);
        node.dist = node.dist / 2;
        node.parent = newnode1;
        reCount(top);
        findHeight(top);
        pogvue.io.TreeFile.checkNode(top);
    }

    public void reCount(SequenceNode node) {
        ycount = 0;
        _reCount(node);
    }

    private void _reCount(SequenceNode node) {
        if (node == null) {
            return;
        }
        if (node.left() != null && node.right() != null) {
            _reCount((SequenceNode) node.left());
            _reCount((SequenceNode) node.right());
            SequenceNode l = (SequenceNode) node.left();
            SequenceNode r = (SequenceNode) node.right();
            node.count = l.count + r.count;
            node.ycount = (l.ycount + r.ycount) / 2;
        } else {
            node.count = 1;
            node.ycount = ycount++;
        }
    }

    public void swapNodes(SequenceNode node) {
        if (node == null) {
            return;
        }
        SequenceNode tmp = (SequenceNode) node.left();
        node.setLeft(node.right());
        node.setRight(tmp);
    }

    private void changeDirection(SequenceNode node, SequenceNode dir) {
        if (node == null) {
            return;
        }
        if (node.parent() != top) {
            changeDirection((SequenceNode) node.parent(), node);
            SequenceNode tmp = (SequenceNode) node.parent();
            if (dir == node.left()) {
                node.setParent(dir);
                node.setLeft(tmp);
            } else if (dir == node.right()) {
                node.setParent(dir);
                node.setRight(tmp);
            }
        } else {
            if (dir == node.left()) {
                node.setParent(node.left());
                if (top.left() == node) {
                    node.setRight(top.right());
                } else {
                    node.setRight(top.left());
                }
            } else {
                node.setParent(node.right());
                if (top.left() == node) {
                    node.setLeft(top.right());
                } else {
                    node.setLeft(top.left());
                }
            }
        }
    }

    public void setMaxDist(SequenceNode node) {
        this.maxdist = maxdist;
    }

    public SequenceNode getMaxDist() {
        return maxdist;
    }

    public SequenceNode getTopNode() {
        return top;
    }
}

final class Cluster {

    final int[] value;

    public Cluster(int[] value) {
        this.value = value;
    }
}
