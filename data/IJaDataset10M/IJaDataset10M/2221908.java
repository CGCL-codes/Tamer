package ytex.kernel.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ytex.kernel.metric.LCSPath;
import com.google.common.collect.ImmutableSet;

public class ConcRel implements java.io.Serializable {

    private static final Logger log = Logger.getLogger(ConcRel.class.getName());

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static List<String> crListToString(List<ConcRel> crList) {
        if (crList != null) {
            List<String> path = new ArrayList<String>(crList.size());
            for (ConcRel cr : crList) path.add(cr.getConceptID());
            return path;
        } else {
            return null;
        }
    }

    /**
	 * get least common subsumer of the specified concepts and its distance from
	 * root.
	 * 
	 * @deprecated
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
    public static ObjPair<ConcRel, Integer> getLeastCommonConcept(ConcRel c1, ConcRel c2) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("getLeastCommonConcept(" + c1 + "," + c2 + ")");
        }
        ObjPair<ConcRel, Integer> res = new ObjPair<ConcRel, Integer>(null, Integer.MAX_VALUE);
        Map<ConcRel, Integer> cand1 = new HashMap<ConcRel, Integer>();
        Map<ConcRel, Integer> cand2 = new HashMap<ConcRel, Integer>();
        HashSet<ConcRel> parC1 = new HashSet<ConcRel>();
        parC1.add(c1);
        HashSet<ConcRel> parC2 = new HashSet<ConcRel>();
        parC2.add(c2);
        HashSet<ConcRel> tmp = new HashSet<ConcRel>();
        HashSet<ConcRel> tmp2;
        int dist = 0;
        while (!parC1.isEmpty() || !parC2.isEmpty()) {
            tmp.clear();
            for (Iterator<ConcRel> it = parC1.iterator(); it.hasNext(); ) {
                ConcRel cr = it.next();
                if (cand2.containsKey(cr)) {
                    res.v1 = cr;
                    res.v2 = dist + cand2.get(cr).intValue();
                    return res;
                }
                cand1.put(cr, dist);
                tmp.addAll(cr.parents);
            }
            tmp.removeAll(cand1.keySet());
            tmp2 = parC1;
            parC1 = tmp;
            tmp = tmp2;
            tmp.clear();
            for (Iterator<ConcRel> it = parC2.iterator(); it.hasNext(); ) {
                ConcRel cr = it.next();
                if (cand1.containsKey(cr)) {
                    res.v1 = cr;
                    res.v2 = dist + cand1.get(cr).intValue();
                    return res;
                }
                cand2.put(cr, dist);
                tmp.addAll(cr.parents);
            }
            tmp.removeAll(cand2.keySet());
            tmp2 = parC2;
            parC2 = tmp;
            tmp = tmp2;
            ++dist;
        }
        return res;
    }

    /**
	 * 
	 * @param c1
	 *            concept1
	 * @param c2
	 *            concept2
	 * @param lcses
	 *            least common subsumers, required
	 * @param paths
	 *            paths between concepts via lcses, optional. Key - lcs. Value -
	 *            2 element list corresponding to paths to lcs from c1 and c2
	 * @return path length, -1 if no lcs
	 */
    public static int getLeastCommonConcept(ConcRel c1, ConcRel c2, Set<ConcRel> lcses, Map<ConcRel, LCSPath> paths) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("getLeastCommonConcept(" + c1 + "," + c2 + ")");
        }
        Map<ConcRel, Integer> cand1 = new HashMap<ConcRel, Integer>();
        Map<ConcRel, Integer> cand2 = new HashMap<ConcRel, Integer>();
        Map<ConcRel, List<ConcRel>> paths1 = paths != null ? new HashMap<ConcRel, List<ConcRel>>() : null;
        Map<ConcRel, List<ConcRel>> paths2 = paths != null ? new HashMap<ConcRel, List<ConcRel>>() : null;
        HashSet<ConcRel> parC1 = new HashSet<ConcRel>();
        parC1.add(c1);
        HashSet<ConcRel> parC2 = new HashSet<ConcRel>();
        parC2.add(c2);
        HashSet<ConcRel> tmp = new HashSet<ConcRel>();
        HashSet<ConcRel> candidateLCSes = new HashSet<ConcRel>();
        int maxIter = -1;
        int dist = 0;
        int minDist = Integer.MAX_VALUE - 1;
        while ((!parC1.isEmpty() || !parC2.isEmpty()) && (maxIter < 0 || maxIter != 0)) {
            updateParent(cand1, parC1, tmp, dist, paths1);
            updateParent(cand2, parC2, tmp, dist, paths2);
            tmp.clear();
            tmp.addAll(cand1.keySet());
            tmp.retainAll(cand2.keySet());
            tmp.removeAll(candidateLCSes);
            if (!tmp.isEmpty()) {
                candidateLCSes.addAll(tmp);
                removeParents(tmp, parC1);
                removeParents(tmp, parC2);
                for (ConcRel lcs : tmp) {
                    int distTmp = cand1.get(lcs) + cand2.get(lcs) + 1;
                    if (distTmp <= minDist) {
                        if (distTmp < minDist) {
                            lcses.clear();
                        }
                        minDist = distTmp;
                        lcses.add(lcs);
                    }
                    int minLcsToConceptLen = Math.min(cand1.get(lcs), cand2.get(lcs));
                    if (maxIter < 0 || maxIter > minLcsToConceptLen) {
                        maxIter = minLcsToConceptLen;
                    }
                }
            }
            maxIter--;
            ++dist;
        }
        if (lcses.isEmpty()) return -1; else {
            if (paths != null) {
                for (ConcRel lcs : lcses) {
                    LCSPath lcsPath = new LCSPath();
                    lcsPath.setLcs(lcs.getConceptID());
                    lcsPath.setConcept1Path(crListToString(paths1.get(lcs)));
                    lcsPath.setConcept2Path(crListToString(paths2.get(lcs)));
                    paths.put(lcs, lcsPath);
                }
            }
            return minDist;
        }
    }

    /**
	 * remove the parents of candidate lcses from the list of parents we were
	 * planning on looking at in the next iteration
	 * 
	 * @param lcses
	 * @param parents
	 */
    private static void removeParents(HashSet<ConcRel> lcses, HashSet<ConcRel> parents) {
        for (ConcRel lcs : lcses) {
            parents.removeAll(lcs.parents);
        }
    }

    /**
	 * perform 1 iteration of breadth-first search on lcs. update the various
	 * collections with the next iteration of ancestors.
	 * 
	 * @param cand1
	 * @param parC1
	 * @param tmp
	 * @param dist
	 */
    private static void updateParent(Map<ConcRel, Integer> cand1, HashSet<ConcRel> parC1, HashSet<ConcRel> tmp, int dist, Map<ConcRel, List<ConcRel>> paths) {
        tmp.clear();
        for (Iterator<ConcRel> it = parC1.iterator(); it.hasNext(); ) {
            ConcRel cr = it.next();
            if (!cand1.containsKey(cr)) {
                cand1.put(cr, dist);
                tmp.addAll(cr.parents);
                if (paths != null) {
                    List<ConcRel> pathCR = paths.get(cr);
                    for (ConcRel parent : cr.parents) {
                        if (!paths.containsKey(parent)) {
                            List<ConcRel> path = new ArrayList<ConcRel>(pathCR != null ? pathCR.size() + 1 : 1);
                            if (pathCR != null) path.addAll(pathCR);
                            path.add(cr);
                            paths.put(parent, path);
                        }
                    }
                }
            }
        }
        tmp.removeAll(cand1.keySet());
        parC1.clear();
        parC1.addAll(tmp);
    }

    /**
	 * children of this concept
	 */
    private Set<ConcRel> children;

    private int[] childrenArray;

    private short depth;

    private double intrinsicInfoContent;

    /**
	 * id of this concept
	 */
    private String nodeCUI;

    private int nodeIndex;

    /**
	 * parents of this concept
	 */
    private Set<ConcRel> parents;

    /**
	 * for java object serialization, need to avoid default serializer behavior
	 * of writing out entire object graph. just write the parent/children object
	 * ids and resolve the connections after loading this object.
	 */
    private int[] parentsArray;

    public ConcRel(String cui, int nodeIndex) {
        nodeCUI = cui;
        parents = new HashSet<ConcRel>();
        children = new HashSet<ConcRel>();
        parentsArray = null;
        childrenArray = null;
        this.nodeIndex = nodeIndex;
    }

    /**
	 * reconstruct the relationships to other ConcRel objects
	 * 
	 * @param db
	 */
    public void constructRel(List<ConcRel> db) {
        ImmutableSet.Builder<ConcRel> pBuilder = new ImmutableSet.Builder<ConcRel>();
        for (int c : parentsArray) pBuilder.add(db.get(c));
        parents = pBuilder.build();
        parentsArray = null;
        ImmutableSet.Builder<ConcRel> cBuilder = new ImmutableSet.Builder<ConcRel>();
        for (int c : childrenArray) cBuilder.add(db.get(c));
        children = cBuilder.build();
        childrenArray = null;
    }

    public int depthMax() {
        int d = 0;
        for (Iterator<ConcRel> it = children.iterator(); it.hasNext(); ) {
            ConcRel child = it.next();
            int dm = child.depthMax() + 1;
            if (dm > d) d = dm;
        }
        return d;
    }

    public Set<ConcRel> getChildren() {
        return children;
    }

    public String getConceptID() {
        return nodeCUI;
    }

    public short getDepth() {
        return depth;
    }

    public double getIntrinsicInfoContent() {
        return intrinsicInfoContent;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public Set<ConcRel> getParents() {
        return parents;
    }

    /**
	 * recursively build all paths to root from a concept - add elements from
	 * set of parents.
	 * 
	 * @param lpath
	 *            current path from children to this concept
	 * @param allPaths
	 *            list of all paths
	 * @param depth
	 *            current depth
	 * @param depthMax
	 */
    public void getPath(List<ConcRel> lpath, List<List<ConcRel>> allPaths, int depth, int depthMax) {
        if (depth >= depthMax) return;
        if (lpath == null) lpath = new ArrayList<ConcRel>();
        lpath.add(this);
        if (isRoot()) {
            allPaths.add(new ArrayList<ConcRel>(lpath));
        } else {
            for (ConcRel p : parents) {
                p.getPath(lpath, allPaths, depth + 1, depthMax);
            }
        }
        lpath.remove(lpath.size() - 1);
    }

    /**
	 * is the specified concept an ancestor of this concept?
	 * 
	 * @param cui
	 * @return
	 */
    public boolean hasAncestor(String cui) {
        if (nodeCUI.equals(cui)) return true;
        for (ConcRel c : parents) {
            if (c.hasAncestor(cui)) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return nodeIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ConcRel other = (ConcRel) obj;
        if (nodeIndex != other.nodeIndex) return false;
        return true;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public boolean isRoot() {
        return parents.isEmpty();
    }

    /**
	 * read parent/children concept ids, not the objects
	 */
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        nodeCUI = (String) in.readObject();
        this.nodeIndex = in.readInt();
        this.intrinsicInfoContent = in.readDouble();
        this.depth = in.readShort();
        parentsArray = (int[]) in.readObject();
        childrenArray = (int[]) in.readObject();
        parents = new HashSet<ConcRel>(parentsArray.length);
        children = new HashSet<ConcRel>(childrenArray.length);
    }

    public void setConceptID(String nodeCUI) {
        this.nodeCUI = nodeCUI;
    }

    public void setDepth(short depth) {
        this.depth = depth;
    }

    public void setIntrinsicInfoContent(double intrinsicInfoContent) {
        this.intrinsicInfoContent = intrinsicInfoContent;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    @Override
    public String toString() {
        return "ConcRel [nodeCUI=" + nodeCUI + "]";
    }

    /**
	 * serialize parent/children concept ids, not the objects
	 */
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.writeObject(nodeCUI);
        out.writeInt(this.nodeIndex);
        out.writeDouble(this.intrinsicInfoContent);
        out.writeShort(this.depth);
        if (parentsArray == null) {
            parentsArray = new int[parents.size()];
            int i = 0;
            for (ConcRel c : parents) parentsArray[i++] = c.getNodeIndex();
        }
        if (childrenArray == null) {
            childrenArray = new int[children.size()];
            int i = 0;
            for (ConcRel c : children) childrenArray[i++] = c.getNodeIndex();
        }
        out.writeObject(parentsArray);
        out.writeObject(childrenArray);
        parentsArray = null;
        childrenArray = null;
    }
}
