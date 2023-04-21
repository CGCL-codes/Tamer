package org.openscience.compchem;

import java.util.*;

public class SetOfRingsFinder {

    static boolean verbose = false;

    /** Finds the Smallest Set of Smallest Rings.
        This is an implementation of the algorithm published in
        John Figueras, "Ring Perception Using Breadth-First Search", 
        J. Chem. Inf. Comput Sci. 1996, 36, 986-991.
    */
    public static setOfRings findSSSR(Node[] atSet) {
        setOfRings sssr = new setOfRings();
        Node[] tempAtomSet = DataStructureTools.cloneAtomSet(atSet);
        int smallest, smallestDegree;
        Ring ring;
        Edge edgeToRemove;
        int[] remembernodes;
        int nodesToBreakCounter;
        Vector fullSet = new Vector();
        Vector trimSet = new Vector();
        Vector nodesN2 = new Vector();
        for (int f = 0; f < tempAtomSet.length; f++) {
            fullSet.addElement(new Integer(tempAtomSet[f].number));
        }
        sssr = new setOfRings();
        do {
            smallestDegree = 7;
            smallest = -1;
            nodesN2.removeAllElements();
            for (int f = 0; f < tempAtomSet.length; f++) {
                if (tempAtomSet[f].degree == 0) {
                    if (!trimSet.contains(new Integer(f))) {
                        trimSet.addElement(new Integer(f));
                    }
                }
                if (tempAtomSet[f].degree == 2) {
                    nodesN2.addElement(new Integer(f));
                }
                if (tempAtomSet[f].degree < smallestDegree && tempAtomSet[f].degree > 0) {
                    smallest = f;
                    smallestDegree = tempAtomSet[f].degree;
                }
            }
            if (smallest == -1) break;
            if (tempAtomSet[smallest].degree == 1) {
                trim(smallest, tempAtomSet);
                trimSet.addElement(new Integer(smallest));
            } else if (tempAtomSet[smallest].degree == 2) {
                remembernodes = new int[nodesN2.size()];
                nodesToBreakCounter = 0;
                for (int f = 0; f < nodesN2.size(); f++) {
                    ring = findSRing(tempAtomSet[((Integer) nodesN2.elementAt(f)).intValue()], tempAtomSet);
                    if (ring.size() > 0) {
                        if (!sssr.ringAlreadyInSet(ring)) {
                            sssr.addElement(ring);
                            remembernodes[nodesToBreakCounter] = ((Integer) nodesN2.elementAt(f)).intValue();
                            nodesToBreakCounter++;
                        }
                    }
                }
                if (nodesToBreakCounter == 0) {
                    nodesToBreakCounter = 1;
                    remembernodes[0] = ((Integer) nodesN2.elementAt(0)).intValue();
                }
                for (int f = 0; f < nodesToBreakCounter; f++) {
                    breakBond(remembernodes[f], tempAtomSet);
                }
            } else if (tempAtomSet[smallest].degree == 3) {
                ring = findSRing(tempAtomSet[smallest], tempAtomSet);
                if (ring.size() > 0) {
                    if (!sssr.ringAlreadyInSet(ring)) {
                        sssr.addElement(ring);
                    }
                }
                edgeToRemove = checkEdges(ring, tempAtomSet);
                breakBond(edgeToRemove.from, edgeToRemove.to, tempAtomSet);
            }
        } while (trimSet.size() < fullSet.size());
        return sssr;
    }

    /** finds the smallest ring of which rootNode is part of.
        This routine is called 'getRing() in Figueras original article */
    public static Ring findSRing(Node rootNode, Node[] tempAtomSet) {
        Node node, neighbor;
        int source, m, frontNode;
        int OKatoms = tempAtomSet.length;
        Queue queue = new Queue();
        Ring ringset = new Ring();
        Vector path[] = new Vector[OKatoms];
        Vector intersection = new Vector();
        Vector ring = new Vector();
        for (int f = 0; f < OKatoms; f++) {
            path[f] = new Vector();
        }
        for (int f = 0; f < tempAtomSet[rootNode.number].degree; f++) {
            neighbor = tempAtomSet[tempAtomSet[rootNode.number].nodeTable[f]];
            if (neighbor.degree > 0) {
                neighbor.source = rootNode.number;
                queue.push(neighbor);
                path[neighbor.number].addElement(new Integer(tempAtomSet[rootNode.number].nodeTable[f]));
                path[neighbor.number].addElement(new Integer(rootNode.number));
            }
        }
        while (true && queue.size() > 0) {
            node = (Node) queue.pop();
            frontNode = node.number;
            source = node.source;
            for (int f = 0; f < node.degree; f++) {
                m = node.nodeTable[f];
                if (tempAtomSet[m].degree > 0 && m != source) {
                    if (path[m].size() > 0) {
                        intersection = getIntersection(path[frontNode], path[m]);
                        if (intersection.size() == 1) {
                            if (verbose) {
                                System.out.println("Ring closure found at: " + m);
                                System.out.println("Path of frontnode: " + path[frontNode].toString());
                                System.out.println("Path of m: " + path[m].toString());
                            }
                            ring = getUnion(path[frontNode], path[m]);
                            if (verbose) System.out.println(prepareRing(ring, tempAtomSet).toString());
                            return prepareRing(ring, tempAtomSet);
                        }
                    } else {
                        path[m] = merge(path[m], path[frontNode]);
                        path[m].insertElementAt(new Integer(m), 0);
                        tempAtomSet[m].source = frontNode;
                        queue.push(tempAtomSet[m]);
                    }
                }
            }
        }
        return new Ring();
    }

    /** Return a Vector that contains the intersection of Vectors vec1
        and vec2 */
    public static Vector getIntersection(Vector vec1, Vector vec2) {
        Vector is = new Vector();
        for (int f = 0; f < vec1.size(); f++) {
            if (vec2.contains((Integer) vec1.elementAt(f))) is.addElement((Integer) vec1.elementAt(f));
        }
        return is;
    }

    /** Return a Vector that contains the union of Vectors vec1
        and vec2 */
    public static Vector getUnion(Vector vec1, Vector vec2) {
        Vector is = (Vector) vec1.clone();
        for (int f = vec2.size() - 1; f > -1; f--) {
            if (!vec1.contains((Integer) vec2.elementAt(f))) is.addElement((Integer) vec2.elementAt(f));
        }
        return is;
    }

    /** merges two vectors into one */
    public static Vector merge(Vector vec1, Vector vec2) {
        Vector result = (Vector) vec1.clone();
        for (int f = 0; f < vec2.size(); f++) {
            result.addElement((Integer) vec2.elementAt(f));
        }
        return result;
    }

    /** prepares a Ring, i.e. a Vector with Nodes from
        a Vector with numbers of nodes */
    public static Ring prepareRing(Vector vec, Node[] tempAtomSet) {
        Ring r = new Ring();
        for (int f = 0; f < vec.size(); f++) {
            r.addElement(tempAtomSet[((Integer) vec.elementAt(f)).intValue()]);
        }
        r.sort();
        return r;
    }

    /** removes from the connection table all connections to
        node n, leaving this node with degree zero.*/
    public static void trim(int n, Node[] tempAtomSet) {
        int conn;
        for (int f = 0; f < tempAtomSet[n].degree; f++) {
            conn = tempAtomSet[n].nodeTable[f];
            for (int g = 0; g < tempAtomSet[conn].degree; g++) {
                if (tempAtomSet[conn].nodeTable[g] == n) {
                    if (g < (tempAtomSet[conn].nodeTable.length - 1)) {
                        for (int h = g; h < tempAtomSet[conn].degree - 1; h++) {
                            tempAtomSet[conn].nodeTable[h] = tempAtomSet[conn].nodeTable[h + 1];
                        }
                    }
                    tempAtomSet[conn].nodeTable[tempAtomSet[conn].degree - 1] = 0;
                    tempAtomSet[conn].degree--;
                }
            }
            tempAtomSet[n].nodeTable[f] = 0;
        }
        tempAtomSet[n].degree = 0;
    }

    /** Eliminate the last bond of this node from the connectiontable*/
    public static void breakBond(int thisNode, Node[] tempAtomSet) {
        int degree = tempAtomSet[thisNode].degree;
        int partner = tempAtomSet[thisNode].nodeTable[degree - 1];
        tempAtomSet[thisNode].nodeTable[degree - 1] = 0;
        tempAtomSet[thisNode].degree--;
        for (int f = 0; f < tempAtomSet[partner].degree; f++) {
            if (tempAtomSet[partner].nodeTable[f] == thisNode) {
                tempAtomSet[partner].nodeTable[f] = 0;
                for (int g = f; g < tempAtomSet[partner].degree - 1; g++) {
                    tempAtomSet[partner].nodeTable[g] = tempAtomSet[partner].nodeTable[g + 1];
                }
                tempAtomSet[partner].nodeTable[tempAtomSet[partner].degree - 1] = 0;
                tempAtomSet[partner].degree--;
                break;
            }
        }
    }

    /** Eliminate the last bond of this node from the
        connectiontable*/
    public static void breakBond(int from, int to, Node[] tempAtomSet) {
        int degree;
        for (int f = 0; f < tempAtomSet[from].degree; f++) {
            if (tempAtomSet[from].nodeTable[f] == to) {
                degree = tempAtomSet[from].degree;
                for (int g = f; g < tempAtomSet[from].degree - 1; g++) {
                    tempAtomSet[from].nodeTable[g] = tempAtomSet[from].nodeTable[g + 1];
                }
                tempAtomSet[from].nodeTable[tempAtomSet[from].degree - 1] = 0;
                tempAtomSet[from].degree--;
                break;
            }
        }
        for (int f = 0; f < tempAtomSet[to].degree; f++) {
            if (tempAtomSet[to].nodeTable[f] == from) {
                for (int g = f; g < tempAtomSet[to].degree - 1; g++) {
                    tempAtomSet[to].nodeTable[g] = tempAtomSet[to].nodeTable[g + 1];
                }
                tempAtomSet[to].nodeTable[tempAtomSet[to].degree - 1] = 0;
                tempAtomSet[to].degree--;
                break;
            }
        }
    }

    /** Restores a bond betwenn nodes 'from' and 'to' of an array of
        nodes 'tempAtomSet' */
    protected static void restoreBond(int from, int to, Node[] tempAtomSet) {
        tempAtomSet[from].nodeTable[tempAtomSet[from].degree] = to;
        tempAtomSet[from].degree++;
        tempAtomSet[to].nodeTable[tempAtomSet[to].degree] = from;
        tempAtomSet[to].degree++;
    }

    /** Selects an optimum edge for elimination in structures without
        N2 nodes. */
    protected static Edge checkEdges(Ring ring, Node[] tAS) {
        Edge edge;
        Ring r1, r2;
        setOfRings tempSoR = new setOfRings();
        int minMax, minMaxSize;
        Node[] tempAtomSet = new Node[tAS.length];
        for (int f = 0; f < tAS.length; f++) {
            tempAtomSet[f] = (Node) tAS[f].clone();
        }
        for (int f = 0; f < ring.size(); f++) {
            edge = ring.getEdge(f);
            breakBond(edge.from, edge.to, tempAtomSet);
            r1 = findSRing(tempAtomSet[edge.from], tempAtomSet);
            r2 = findSRing(tempAtomSet[edge.to], tempAtomSet);
            if (r1.size() > r2.size()) tempSoR.addElement(r1); else tempSoR.addElement(r1);
            restoreBond(edge.from, edge.to, tempAtomSet);
        }
        minMaxSize = ((Ring) tempSoR.elementAt(0)).size();
        minMax = 0;
        for (int f = 0; f < tempSoR.size(); f++) {
            if (((Ring) tempSoR.elementAt(f)).size() < minMaxSize) {
                minMaxSize = ((Ring) tempSoR.elementAt(f)).size();
                minMax = f;
            }
        }
        return ring.getEdge(minMax);
    }
}
