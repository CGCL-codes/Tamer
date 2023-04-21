package com.bulletphysics.extras.gimpact;

import com.bulletphysics.extras.gimpact.BoxCollision.AABB;
import com.bulletphysics.extras.gimpact.BoxCollision.BoxBoxTransformCache;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.IntArrayList;
import javax.vecmath.Vector3f;

/**
 *
 * @author jezek2
 */
class GImpactBvh {

    protected BvhTree box_tree = new BvhTree();

    protected PrimitiveManagerBase primitive_manager;

    /**
	 * This constructor doesn't build the tree. you must call buildSet.
	 */
    public GImpactBvh() {
        primitive_manager = null;
    }

    /**
	 * This constructor doesn't build the tree. you must call buildSet.
	 */
    public GImpactBvh(PrimitiveManagerBase primitive_manager) {
        this.primitive_manager = primitive_manager;
    }

    public AABB getGlobalBox(AABB out) {
        getNodeBound(0, out);
        return out;
    }

    public void setPrimitiveManager(PrimitiveManagerBase primitive_manager) {
        this.primitive_manager = primitive_manager;
    }

    public PrimitiveManagerBase getPrimitiveManager() {
        return primitive_manager;
    }

    protected void refit() {
        AABB leafbox = new AABB();
        AABB bound = new AABB();
        AABB temp_box = new AABB();
        int nodecount = getNodeCount();
        while ((nodecount--) != 0) {
            if (isLeafNode(nodecount)) {
                primitive_manager.get_primitive_box(getNodeData(nodecount), leafbox);
                setNodeBound(nodecount, leafbox);
            } else {
                bound.invalidate();
                int child_node = getLeftNode(nodecount);
                if (child_node != 0) {
                    getNodeBound(child_node, temp_box);
                    bound.merge(temp_box);
                }
                child_node = getRightNode(nodecount);
                if (child_node != 0) {
                    getNodeBound(child_node, temp_box);
                    bound.merge(temp_box);
                }
                setNodeBound(nodecount, bound);
            }
        }
    }

    /**
	 * This attemps to refit the box set.
	 */
    public void update() {
        refit();
    }

    /**
	 * This rebuild the entire set.
	 */
    public void buildSet() {
        BvhDataArray primitive_boxes = new BvhDataArray();
        primitive_boxes.resize(primitive_manager.get_primitive_count());
        AABB tmpAABB = new AABB();
        for (int i = 0; i < primitive_boxes.size(); i++) {
            primitive_manager.get_primitive_box(i, tmpAABB);
            primitive_boxes.setBound(i, tmpAABB);
            primitive_boxes.setData(i, i);
        }
        box_tree.build_tree(primitive_boxes);
    }

    /**
	 * Returns the indices of the primitives in the primitive_manager field.
	 */
    public boolean boxQuery(AABB box, IntArrayList collided_results) {
        int curIndex = 0;
        int numNodes = getNodeCount();
        AABB bound = new AABB();
        while (curIndex < numNodes) {
            getNodeBound(curIndex, bound);
            boolean aabbOverlap = bound.has_collision(box);
            boolean isleafnode = isLeafNode(curIndex);
            if (isleafnode && aabbOverlap) {
                collided_results.add(getNodeData(curIndex));
            }
            if (aabbOverlap || isleafnode) {
                curIndex++;
            } else {
                curIndex += getEscapeNodeIndex(curIndex);
            }
        }
        if (collided_results.size() > 0) {
            return true;
        }
        return false;
    }

    /**
	 * Returns the indices of the primitives in the primitive_manager field.
	 */
    public boolean boxQueryTrans(AABB box, Transform transform, IntArrayList collided_results) {
        AABB transbox = new AABB(box);
        transbox.appy_transform(transform);
        return boxQuery(transbox, collided_results);
    }

    /**
	 * Returns the indices of the primitives in the primitive_manager field.
	 */
    public boolean rayQuery(Vector3f ray_dir, Vector3f ray_origin, IntArrayList collided_results) {
        int curIndex = 0;
        int numNodes = getNodeCount();
        AABB bound = new AABB();
        while (curIndex < numNodes) {
            getNodeBound(curIndex, bound);
            boolean aabbOverlap = bound.collide_ray(ray_origin, ray_dir);
            boolean isleafnode = isLeafNode(curIndex);
            if (isleafnode && aabbOverlap) {
                collided_results.add(getNodeData(curIndex));
            }
            if (aabbOverlap || isleafnode) {
                curIndex++;
            } else {
                curIndex += getEscapeNodeIndex(curIndex);
            }
        }
        if (collided_results.size() > 0) {
            return true;
        }
        return false;
    }

    /**
	 * Tells if this set has hierarchy.
	 */
    public boolean hasHierarchy() {
        return true;
    }

    /**
	 * Tells if this set is a trimesh.
	 */
    public boolean isTrimesh() {
        return primitive_manager.is_trimesh();
    }

    public int getNodeCount() {
        return box_tree.getNodeCount();
    }

    /**
	 * Tells if the node is a leaf.
	 */
    public boolean isLeafNode(int nodeindex) {
        return box_tree.isLeafNode(nodeindex);
    }

    public int getNodeData(int nodeindex) {
        return box_tree.getNodeData(nodeindex);
    }

    public void getNodeBound(int nodeindex, AABB bound) {
        box_tree.getNodeBound(nodeindex, bound);
    }

    public void setNodeBound(int nodeindex, AABB bound) {
        box_tree.setNodeBound(nodeindex, bound);
    }

    public int getLeftNode(int nodeindex) {
        return box_tree.getLeftNode(nodeindex);
    }

    public int getRightNode(int nodeindex) {
        return box_tree.getRightNode(nodeindex);
    }

    public int getEscapeNodeIndex(int nodeindex) {
        return box_tree.getEscapeNodeIndex(nodeindex);
    }

    public void getNodeTriangle(int nodeindex, PrimitiveTriangle triangle) {
        primitive_manager.get_primitive_triangle(getNodeData(nodeindex), triangle);
    }

    public BvhTreeNodeArray get_node_pointer() {
        return box_tree.get_node_pointer();
    }

    private static boolean _node_collision(GImpactBvh boxset0, GImpactBvh boxset1, BoxBoxTransformCache trans_cache_1to0, int node0, int node1, boolean complete_primitive_tests) {
        AABB box0 = new AABB();
        boxset0.getNodeBound(node0, box0);
        AABB box1 = new AABB();
        boxset1.getNodeBound(node1, box1);
        return box0.overlapping_trans_cache(box1, trans_cache_1to0, complete_primitive_tests);
    }

    /**
	 * Stackless recursive collision routine.
	 */
    private static void _find_collision_pairs_recursive(GImpactBvh boxset0, GImpactBvh boxset1, PairSet collision_pairs, BoxBoxTransformCache trans_cache_1to0, int node0, int node1, boolean complete_primitive_tests) {
        if (_node_collision(boxset0, boxset1, trans_cache_1to0, node0, node1, complete_primitive_tests) == false) {
            return;
        }
        if (boxset0.isLeafNode(node0)) {
            if (boxset1.isLeafNode(node1)) {
                collision_pairs.push_pair(boxset0.getNodeData(node0), boxset1.getNodeData(node1));
                return;
            } else {
                _find_collision_pairs_recursive(boxset0, boxset1, collision_pairs, trans_cache_1to0, node0, boxset1.getLeftNode(node1), false);
                _find_collision_pairs_recursive(boxset0, boxset1, collision_pairs, trans_cache_1to0, node0, boxset1.getRightNode(node1), false);
            }
        } else {
            if (boxset1.isLeafNode(node1)) {
                _find_collision_pairs_recursive(boxset0, boxset1, collision_pairs, trans_cache_1to0, boxset0.getLeftNode(node0), node1, false);
                _find_collision_pairs_recursive(boxset0, boxset1, collision_pairs, trans_cache_1to0, boxset0.getRightNode(node0), node1, false);
            } else {
                _find_collision_pairs_recursive(boxset0, boxset1, collision_pairs, trans_cache_1to0, boxset0.getLeftNode(node0), boxset1.getLeftNode(node1), false);
                _find_collision_pairs_recursive(boxset0, boxset1, collision_pairs, trans_cache_1to0, boxset0.getLeftNode(node0), boxset1.getRightNode(node1), false);
                _find_collision_pairs_recursive(boxset0, boxset1, collision_pairs, trans_cache_1to0, boxset0.getRightNode(node0), boxset1.getLeftNode(node1), false);
                _find_collision_pairs_recursive(boxset0, boxset1, collision_pairs, trans_cache_1to0, boxset0.getRightNode(node0), boxset1.getRightNode(node1), false);
            }
        }
    }

    public static void find_collision(GImpactBvh boxset0, Transform trans0, GImpactBvh boxset1, Transform trans1, PairSet collision_pairs) {
        if (boxset0.getNodeCount() == 0 || boxset1.getNodeCount() == 0) {
            return;
        }
        BoxBoxTransformCache trans_cache_1to0 = new BoxBoxTransformCache();
        trans_cache_1to0.calc_from_homogenic(trans0, trans1);
        _find_collision_pairs_recursive(boxset0, boxset1, collision_pairs, trans_cache_1to0, 0, 0, true);
    }
}
