package inyo;

import java.util.ArrayList;

class RtOctTree extends RtBoundingBox {

    boolean isLeaf = false;

    RtOctTree octant[] = null;

    RtTriangle child[] = null;

    /**
	 * Place all triangles between <b>firstTriangle</b> and <b>lastTriangle</b> into
	 * the octree.
	 * 
	 * @param world
	 * @param firstTriangle
	 * @param lastTriangle
	 */
    public RtOctTree(int maxDepth, int maxItems, ArrayList triangleList) {
        for (int i = 0; i < triangleList.size(); i++) {
            RtTriangle triangle = (RtTriangle) triangleList.get(i);
            this.add(triangle);
        }
        this.buildOctants(maxDepth, maxItems, triangleList, 1, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    /**
	 * Creates a new node, and builds a list of all the triangles that fit into the node.
	 * It then calls <b>buildOctants</b> to recurse to the bottom of the nodes, and 
	 * add the triangles to the nodes.
	 * 
	 * @param world
	 * @param minX
	 * @param minY
	 * @param minZ
	 * @param maxX
	 * @param maxY
	 * @param maxZ
	 * @param depth
	 * @param childList
	 */
    public RtOctTree(int maxDepth, int maxItems, ArrayList triangleList, int depth, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        ArrayList newList = new ArrayList();
        this.add(minX, minY, minZ);
        this.add(maxX, maxY, maxZ);
        for (int i = 0; i < triangleList.size(); i++) {
            RtTriangle triangle = (RtTriangle) triangleList.get(i);
            if (this.intersects(triangle)) {
                newList.add((Object) triangle);
            }
        }
        this.buildOctants(maxDepth, maxItems, newList, depth, minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Builds octants into an octree's node, and adds children from <b>childList</b>
     * into the appropriate octants of the octree.
     * 
     * @param world
     * @param minX
     * @param minY
     * @param minZ
     * @param maxX
     * @param maxY
     * @param maxZ
     * @param depth
     * @param childList
     */
    final void buildOctants(int maxDepth, int maxItems, ArrayList triangleList, int depth, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (triangleList.size() == 0) {
            this.isLeaf = true;
        } else if ((depth > maxDepth) || (triangleList.size() < maxItems)) {
            this.isLeaf = true;
            child = new RtTriangle[triangleList.size()];
            for (int i = 0; i < triangleList.size(); i++) {
                child[i] = (RtTriangle) triangleList.get(i);
            }
        } else {
            double midX = (minX + maxX) / 2;
            double midY = (minY + maxY) / 2;
            double midZ = (minZ + maxZ) / 2;
            isLeaf = false;
            this.octant = new RtOctTree[8];
            octant[0] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, minX, minY, minZ, midX, midY, midZ);
            octant[1] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, midX, minY, minZ, maxX, midY, midZ);
            octant[2] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, minX, minY, midZ, midX, midY, maxZ);
            octant[3] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, midX, minY, midZ, maxX, midY, maxZ);
            octant[4] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, minX, midY, minZ, midX, maxY, midZ);
            octant[5] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, midX, midY, minZ, maxX, maxY, midZ);
            octant[6] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, minX, midY, midZ, midX, maxY, maxZ);
            octant[7] = new RtOctTree(maxDepth, maxItems, triangleList, depth + 1, midX, midY, midZ, maxX, maxY, maxZ);
            for (int i = 0; i < 8; i++) {
                if ((octant[i].isLeaf) && (octant[i].child == null)) {
                    octant[i] = null;
                }
            }
        }
    }

    /**
	 * Returns true if ray described by <b>pathNode</b> hits the octree.
	 * 
	 * @param world
	 * @param pathNode
	 */
    final void hitTest(RtPathNode pathNode) {
        if (!(this.hitsBox(pathNode))) {
            return;
        }
        if (isLeaf) {
            for (int i = 0; i < child.length; i++) {
                RtTriangle t = child[i];
                t.hitTest(pathNode);
            }
        } else {
            for (int i = 0; i < 8; i++) {
                if (octant[i] != null) {
                    if (pathNode.maxDistance == 0.0 || octant[i].withinDistance(pathNode.origin, pathNode.maxDistance)) {
                        octant[i].hitTest(pathNode);
                        if (pathNode.hit && pathNode.stopAtFirstHit) {
                            return;
                        }
                    }
                }
            }
        }
    }
}
