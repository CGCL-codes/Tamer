package org.xith3d.scenegraph;

import org.xith3d.scenegraph.traversal.DetailedTraversalCallback;

/**
 * A BranchGroup is the root of a SceneGraph. More exactly it is the branch
 * group of a RenderPass. It is not intended to be used as a child of another
 * Group.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 * @author Amos Wenger (aka BlueSky)
 */
public class BranchGroup extends GroupNode {

    private SceneGraph sceneGraph = null;

    private boolean refillForeced = true;

    private boolean recullForeced = true;

    /**
     * Marks this BranchGroup as refill-forced.<br>
     * If the BranchGroup is marked refill-foreced, it will be completely retraversed
     * in the next rendering pass.
     * 
     * @param force
     */
    final void forceRefill(boolean force) {
        this.refillForeced = force;
    }

    /**
     * @return this BranchGroup's refill-forced flag state.<br>
     * If the BranchGroup is marked refill-foreced, it will be completely retraversed
     * in the next rendering pass.
     */
    final boolean isRefillForeced() {
        return (refillForeced);
    }

    /**
     * Sets this BranchGroup's recull-forced flag state.<br>
     * If the BranchGroup is marked recull-forced, it will be reculled
     * in the next rendering pass.
     * 
     * @param force
     */
    final void forceRecull(boolean force) {
        this.recullForeced = force;
    }

    /**
     * @return this BranchGroup's recull-forced flag state.<br>
     * If the BranchGroup is marked recull-forced, it will be reculled
     * in the next rendering pass.
     */
    final boolean isRecullForeced() {
        return (recullForeced);
    }

    final void setSceneGraph(SceneGraph sceneGraph) {
        this.sceneGraph = sceneGraph;
        this.setLive(this.sceneGraph != null);
    }

    public final SceneGraph getSceneGraph() {
        return (sceneGraph);
    }

    /**
     * Traverses the scenegraph from this node on.
     * If this Node is a Group it will recusively run through each child.
     * 
     * @param callback the listener is notified of any traversed Node on the way
     * @return if false, the whole traversal will stop
     */
    @Override
    public boolean traverse(DetailedTraversalCallback callback) {
        if (!callback.traversalOperationCommon(this)) return (false);
        if (!callback.traversalOperation(this)) return (false);
        if (callback.traversalCheckGroupCommon(this) && callback.traversalCheckGroup(this)) {
            final int numChildren = numChildren();
            for (int i = 0; i < numChildren; i++) {
                if (!getChild(i).traverse(callback)) return (false);
            }
        }
        return (callback.traversalOperationCommonAfter(this) && callback.traversalOperationAfter(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BranchGroup newInstance() {
        boolean gib = Node.globalIgnoreBounds;
        Node.globalIgnoreBounds = this.isIgnoreBounds();
        BranchGroup bg = new BranchGroup();
        Node.globalIgnoreBounds = gib;
        return (bg);
    }

    /**
     * Constructs a new BranchGroup instance and directly
     * adds the given child Node to it.<br>
     * <br>
     * <i>This is a convenience method.</i>
     */
    protected BranchGroup(GroupNode hostGroup, Node firstChild) {
        super(hostGroup);
        if (firstChild != null) this.addChild(firstChild);
    }

    /**
     * Constructs a new BranchGroup instance and directly
     * adds the given child Node to it.<br>
     * <br>
     * <i>This is a convenience method.</i>
     * 
     * @param firstChild
     */
    public BranchGroup(Node firstChild) {
        this(null, firstChild);
    }

    /**
     * Constructs a new BranchGroup instance.
     */
    public BranchGroup() {
        this(null, null);
    }
}
