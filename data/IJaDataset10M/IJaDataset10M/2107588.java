package gov.nasa.ltl.graph;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

/**
 * DOCUMENT ME!
 */
public class SFSReduction {

    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("usage:");
            System.out.println("\tjava gov.nasa.ltl.graph.SFSReduction [<filename>]");
            return;
        }
        Graph g = null;
        try {
            if (args.length == 0) {
                g = Graph.load();
            } else {
                g = Graph.load(args[0]);
            }
        } catch (IOException e) {
            System.out.println("Can't load the graph.");
            return;
        }
        Graph reduced = reduce(g);
        reduced.save();
    }

    public static Graph reduce(Graph g) {
        int currNumColors;
        int prevNumColors = 1;
        int currNumPO = 3;
        int prevNumPO = 1;
        TreeSet<ColorPair> newColorSet = null;
        LinkedList<Pair<ColorPair>> newColorList = null;
        boolean accepting = false;
        boolean nonaccepting = false;
        List<Node> nodes = g.getNodes();
        for (Iterator<Node> i = nodes.iterator(); i.hasNext(); ) {
            Node currNode = i.next();
            currNode.setIntAttribute("_prevColor", 1);
            if (isAccepting(currNode)) {
                currNode.setIntAttribute("_currColor", 1);
                accepting = true;
            } else {
                currNode.setIntAttribute("_currColor", 2);
                nonaccepting = true;
            }
        }
        if (accepting && nonaccepting) {
            currNumColors = 2;
        } else {
            currNumColors = 1;
        }
        boolean[][] currPO = new boolean[2][2];
        boolean[][] prevPO;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i >= j) {
                    currPO[i][j] = true;
                } else {
                    currPO[i][j] = false;
                }
            }
        }
        while ((currNumColors != prevNumColors) || (currNumPO != prevNumPO)) {
            for (Iterator<Node> i = nodes.iterator(); i.hasNext(); ) {
                Node currNode = i.next();
                currNode.setIntAttribute("_prevColor", currNode.getIntAttribute("_currColor"));
            }
            prevPO = currPO;
            prevNumColors = currNumColors;
            newColorList = new LinkedList<Pair<ColorPair>>();
            newColorSet = new TreeSet<ColorPair>();
            for (Iterator<Node> i = nodes.iterator(); i.hasNext(); ) {
                Node currNode = i.next();
                ColorPair currPair = new ColorPair(currNode.getIntAttribute("_prevColor"), getPrevN(currNode, prevPO));
                newColorList.add(new Pair<ColorPair>(currNode.getId(), currPair));
                newColorSet.add(currPair);
            }
            currNumColors = newColorSet.size();
            LinkedList<ColorPair> ordered = new LinkedList<ColorPair>();
            for (Iterator<ColorPair> i = newColorSet.iterator(); i.hasNext(); ) {
                ColorPair currPair = i.next();
                ordered.add(currPair);
            }
            for (Iterator<Pair<ColorPair>> i = newColorList.iterator(); i.hasNext(); ) {
                Pair<ColorPair> cPair = i.next();
                ColorPair currPair = cPair.getElement();
                g.getNode(cPair.getValue()).setIntAttribute("_currColor", ordered.indexOf(currPair) + 1);
            }
            prevNumPO = currNumPO;
            currNumPO = 0;
            currPO = new boolean[currNumColors][currNumColors];
            for (Iterator<Pair<ColorPair>> i = newColorList.iterator(); i.hasNext(); ) {
                ColorPair currPairOne = i.next().getElement();
                for (Iterator<Pair<ColorPair>> j = newColorList.iterator(); j.hasNext(); ) {
                    ColorPair currPairTwo = j.next().getElement();
                    boolean po = prevPO[currPairTwo.getColor() - 1][currPairOne.getColor() - 1];
                    boolean dominate = iDominateSet(currPairOne.getIMaxSet(), currPairTwo.getIMaxSet(), prevPO);
                    if (po && dominate) {
                        currPO[ordered.indexOf(currPairTwo)][ordered.indexOf(currPairOne)] = true;
                        currNumPO++;
                    } else {
                        currPO[ordered.indexOf(currPairTwo)][ordered.indexOf(currPairOne)] = false;
                    }
                }
            }
        }
        Graph result;
        if (newColorList == null) {
            result = g;
        } else {
            result = new Graph();
            Node[] newNodes = new Node[currNumColors];
            for (int i = 0; i < currNumColors; i++) {
                Node n = new Node(result);
                newNodes[i] = n;
            }
            for (Iterator<Pair<ColorPair>> i = newColorList.iterator(); i.hasNext(); ) {
                Pair<ColorPair> nodePair = i.next();
                int origNodeId = nodePair.getValue();
                ColorPair colPair = nodePair.getElement();
                if (newColorSet != null && newColorSet.contains(colPair)) {
                    newColorSet.remove(colPair);
                    TreeSet<ITypeNeighbor> pairSet = colPair.getIMaxSet();
                    int color = colPair.getColor();
                    Node currNode = newNodes[color - 1];
                    for (Iterator<ITypeNeighbor> j = pairSet.iterator(); j.hasNext(); ) {
                        ITypeNeighbor neigh = j.next();
                        int neighPos = neigh.getColor() - 1;
                        new Edge(currNode, newNodes[neighPos], neigh.getTransition());
                    }
                    if (g.getInit().getId() == origNodeId) {
                        result.setInit(currNode);
                    }
                    if (isAccepting(g.getNode(origNodeId))) {
                        currNode.setBooleanAttribute("accepting", true);
                    }
                } else {
                }
            }
        }
        return reachabilityGraph(result);
    }

    private static boolean isAccepting(Node nodeIn) {
        return (nodeIn.getBooleanAttribute("accepting"));
    }

    private static TreeSet<ITypeNeighbor> getPrevN(Node currNode, boolean[][] prevPO) {
        List<Edge> edges = currNode.getOutgoingEdges();
        LinkedList<ITypeNeighbor> neighbors = new LinkedList<ITypeNeighbor>();
        ITypeNeighbor iNeigh;
        TreeSet<ITypeNeighbor> prevN = new TreeSet<ITypeNeighbor>();
        for (Iterator<Edge> i = edges.iterator(); i.hasNext(); ) {
            Edge currEdge = i.next();
            iNeigh = new ITypeNeighbor(currEdge.getNext().getIntAttribute("_prevColor"), currEdge.getGuard());
            neighbors.add(iNeigh);
        }
        if (neighbors.size() == 0) {
            return prevN;
        }
        boolean useless;
        do {
            useless = false;
            iNeigh = neighbors.removeFirst();
            for (Iterator<ITypeNeighbor> i = neighbors.iterator(); i.hasNext(); ) {
                ITypeNeighbor nNeigh = i.next();
                ITypeNeighbor dominating = iDominates(iNeigh, nNeigh, prevPO);
                if (dominating == iNeigh) {
                    i.remove();
                }
                if (dominating == nNeigh) {
                    useless = true;
                    break;
                }
            }
            if (!useless) {
                prevN.add(iNeigh);
            }
        } while (neighbors.size() > 0);
        return prevN;
    }

    private static boolean iDominateSet(TreeSet<ITypeNeighbor> setOne, TreeSet<ITypeNeighbor> setTwo, boolean[][] prevPO) {
        TreeSet<ITypeNeighbor> working = new TreeSet<ITypeNeighbor>(setTwo);
        for (Iterator<ITypeNeighbor> i = working.iterator(); i.hasNext(); ) {
            ITypeNeighbor neighTwo = i.next();
            for (Iterator<ITypeNeighbor> j = setOne.iterator(); j.hasNext(); ) {
                ITypeNeighbor neighOne = j.next();
                ITypeNeighbor dominating = iDominates(neighOne, neighTwo, prevPO);
                if (dominating == neighOne) {
                    i.remove();
                    break;
                }
            }
        }
        if (working.size() == 0) {
            return true;
        }
        return false;
    }

    /** Returns the neighbor that dominates. If none dominates the
   * other, then returns null
   */
    private static ITypeNeighbor iDominates(ITypeNeighbor iNeigh, ITypeNeighbor nNeigh, boolean[][] prevPO) {
        String iTerm = iNeigh.getTransition();
        String nTerm = nNeigh.getTransition();
        int iColor = iNeigh.getColor();
        int nColor = nNeigh.getColor();
        String theSubterm = subterm(iTerm, nTerm);
        if (theSubterm == iTerm) {
            if (prevPO[nColor - 1][iColor - 1]) {
                return iNeigh;
            } else {
                return null;
            }
        }
        if (theSubterm == nTerm) {
            if (prevPO[iColor - 1][nColor - 1]) {
                return nNeigh;
            } else {
                return null;
            }
        }
        if (theSubterm.equals("true")) {
            if (prevPO[nColor - 1][iColor - 1]) {
                return iNeigh;
            } else if (prevPO[iColor - 1][nColor - 1]) {
                return nNeigh;
            }
        }
        return null;
    }

    private static Graph reachabilityGraph(Graph g) {
        Vector<Node> work = new Vector<Node>();
        Vector<Node> reachable = new Vector<Node>();
        work.add(g.getInit());
        while (!work.isEmpty()) {
            Node currNode = work.firstElement();
            reachable.add(currNode);
            if (currNode != null) {
                List<Edge> outgoingEdges = currNode.getOutgoingEdges();
                for (Iterator<Edge> i = outgoingEdges.iterator(); i.hasNext(); ) {
                    Edge currEdge = i.next();
                    Node nextNode = currEdge.getNext();
                    if (!(work.contains(nextNode) || reachable.contains(nextNode))) {
                        work.add(nextNode);
                    }
                }
            }
            if (work.remove(0) != currNode) {
                System.out.println("ERROR");
            }
        }
        List<Node> nodes = g.getNodes();
        if (nodes != null) {
            for (Iterator<Node> i = nodes.iterator(); i.hasNext(); ) {
                Node n = i.next();
                if (!reachable.contains(n)) {
                    g.removeNode(n);
                }
            }
        }
        return g;
    }

    private static String subterm(String pred1, String pred2) {
        if (pred1.equals("-") && pred2.equals("-")) {
            return "true";
        }
        if (pred1.equals("-")) {
            return pred1;
        }
        if (pred2.equals("-")) {
            return pred2;
        }
        if ((pred1.indexOf("true") != -1) && (pred2.indexOf("true") != -1)) {
            return "true";
        }
        if (pred1.indexOf("true") != -1) {
            return pred1;
        }
        if (pred2.indexOf("true") != -1) {
            return pred2;
        }
        String alphaStr;
        String tauStr;
        if (pred1.length() <= pred2.length()) {
            alphaStr = pred1;
            tauStr = pred2;
        } else {
            alphaStr = pred2;
            tauStr = pred1;
        }
        StringTokenizer alphaTk = new StringTokenizer(alphaStr, "&");
        StringTokenizer tauTk = new StringTokenizer(tauStr, "&");
        LinkedList<String> tauLst = new LinkedList<String>();
        while (tauTk.hasMoreTokens()) {
            String token = tauTk.nextToken();
            tauLst.add(token);
        }
        while (alphaTk.hasMoreTokens()) {
            String alphaLit = alphaTk.nextToken();
            if (!tauLst.contains(alphaLit)) {
                return "false";
            }
        }
        if (pred1.length() == pred2.length()) {
            return "true";
        }
        return alphaStr;
    }
}
