package jopt.csp.spi.arcalgorithm.graph.arc.generic;

import java.util.Iterator;
import jopt.csp.spi.arcalgorithm.graph.node.BooleanNode;
import jopt.csp.spi.arcalgorithm.graph.node.GenericBooleanNode;
import jopt.csp.spi.arcalgorithm.graph.node.GenericNodeIndexManager;
import jopt.csp.spi.arcalgorithm.graph.node.Node;
import jopt.csp.spi.arcalgorithm.graph.node.NumNode;
import jopt.csp.spi.arcalgorithm.variable.GenericBooleanConstant;
import jopt.csp.variable.CspGenericIndex;
import jopt.csp.variable.PropagationFailureException;

/**
 * Base class for generic boolean arcs
 */
public abstract class GenericBoolArc extends GenericArc {

    protected Boolean xconst;

    protected GenericBooleanConstant xGenConst;

    protected BooleanNode nx;

    protected GenericBooleanNode gx;

    protected boolean xzIdxSame;

    protected Boolean yconst;

    protected GenericBooleanConstant yGenConst;

    protected BooleanNode ny;

    protected GenericBooleanNode gy;

    protected boolean yzIdxSame;

    protected BooleanNode nz;

    protected GenericBooleanNode gz;

    protected boolean notX;

    protected boolean notY;

    protected boolean notZ;

    protected int operation;

    protected GenericNodeIndexManager idxMgr;

    protected Boolean currentXConst;

    protected GenericBooleanConstant currentXGenConst;

    protected Boolean currentYConst;

    protected GenericBooleanConstant currentYGenConst;

    protected BooleanNode currentNz;

    protected GenericBooleanNode currentGz;

    protected BooleanNode currentNx;

    protected int currentChangedXStart;

    protected int currentChangedXEnd;

    protected GenericBooleanNode currentGx;

    protected BooleanNode currentNy;

    protected int currentChangedYStart;

    protected int currentChangedYEnd;

    protected GenericBooleanNode currentGy;

    /**
     * Constructor where X and Z are the only variables
     *
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    protected GenericBoolArc(Node x, boolean notX, Node z, boolean notZ) {
        super(new Node[] { x }, new Node[] { z });
        init(x, notX, null, null, null, false, null, null, z, notZ);
    }

    /**
     * Constructor where x is a constant and Y and Z are variables
     *
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if right side is equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    protected GenericBoolArc(Boolean x, Node y, boolean notY, Node z, boolean notZ) {
        super(new Node[] { y }, new Node[] { z });
        init(null, false, x, null, y, notY, null, null, z, notZ);
    }

    /**
     * Constructor where x is a constant and Y and Z are variables
     *
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if right side is equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    protected GenericBoolArc(GenericBooleanConstant x, Node y, boolean notY, Node z, boolean notZ) {
        super(new Node[] { y }, new Node[] { z });
        init(null, false, null, x, y, notY, null, null, z, notZ);
    }

    /**
     * Constructor where y is a constant and X and Z are variables
     *
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if right side is equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    protected GenericBoolArc(Node x, boolean notX, Boolean y, Node z, boolean notZ) {
        super(new Node[] { x }, new Node[] { z });
        init(x, notX, null, null, null, false, y, null, z, notZ);
    }

    /**
     * Constructor where y is a constant and X and Z are variables
     *
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if right side is equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    protected GenericBoolArc(Node x, boolean notX, GenericBooleanConstant y, Node z, boolean notZ) {
        super(new Node[] { x }, new Node[] { z });
        init(x, notX, null, null, null, false, null, y, z, notZ);
    }

    /**
     * Constructor for three nodes
     *
     * @param x         X portion of equation
     * @param notX      True if X portion of equation is equal to !X, false if right side is equal to X
     * @param y         Y portion of equation
     * @param notY      True if Y portion of equation is equal to !Y, false if right side is equal to Y
     * @param z         Left side of equation
     * @param notZ      True if left side of equation is equal to !Z, false if left side is equal to Z
     */
    protected GenericBoolArc(Node x, boolean notX, Node y, boolean notY, Node z, boolean notZ) {
        super(new Node[] { x, y }, new Node[] { z });
        init(x, notX, null, null, y, notY, null, null, z, notZ);
    }

    /**
     * Initializes internal variables and flags based on arguments that were passed to the constructor
     */
    protected void init(Node xnode, boolean notX, Boolean xconst, GenericBooleanConstant xGenConst, Node ynode, boolean notY, Boolean yconst, GenericBooleanConstant yGenConst, Node znode, boolean notZ) {
        if ((xnode == null || !(xnode instanceof GenericBooleanNode)) && (ynode == null || !(ynode instanceof GenericBooleanNode)) && (!(znode instanceof GenericBooleanNode))) {
            throw new IllegalStateException("generic arcs require at least one generic node");
        }
        if (znode instanceof GenericBooleanNode) this.gz = (GenericBooleanNode) znode; else this.nz = (BooleanNode) znode;
        if (xconst != null) {
            this.xconst = xconst;
            this.currentXConst = xconst;
        } else if (xGenConst != null) {
            this.xGenConst = xGenConst;
            this.currentXGenConst = xGenConst;
        } else if (xnode instanceof GenericBooleanNode) this.gx = (GenericBooleanNode) xnode; else this.nx = (BooleanNode) xnode;
        if (yconst != null) {
            this.yconst = yconst;
            this.currentYConst = yconst;
        } else if (yGenConst != null) {
            this.yGenConst = yGenConst;
            this.currentYGenConst = yGenConst;
        } else if (ynode instanceof GenericBooleanNode) this.gy = (GenericBooleanNode) ynode; else this.ny = (BooleanNode) ynode;
        this.idxMgr = createIndexManager(xnode, xGenConst, ynode, yGenConst, znode);
    }

    /**
     * Creates index manager to use during propagation
     */
    protected GenericNodeIndexManager createIndexManager(Node xnode, GenericBooleanConstant xGenConst, Node ynode, GenericBooleanConstant yGenConst, Node znode) {
        return new GenericNodeIndexManager(xnode, xGenConst, ynode, yGenConst, znode, null, false);
    }

    /**
     * Called when a specific X or Y node has been changed and initiates propagation
     */
    protected void propagateForInitiatingSource(Node n) throws PropagationFailureException {
        if (n instanceof NumNode) {
            propagateAllSources();
        } else if (n.equals(gx)) {
            propagateForChangeToGenericX();
        } else {
            propagateForChangeToGenericY();
        }
    }

    /**
     * Propagate changes based on the fact that the generic node X has had changes
     * to internal nodes
     */
    protected void propagateForChangeToGenericX() throws PropagationFailureException {
        if (!idxMgr.useCombinedX()) {
            int start = -1;
            int end = -1;
            start = gx.domainModifiedMinOffset();
            end = gx.domainModifiedMaxOffset();
            if (start == end && end == -1) return;
            for (int i = start; i <= end; i++) {
                gx.setIndicesToNodeOffset(i);
                currentNx = (BooleanNode) gx.getNode(i);
                propagateUsingIterator(idxMgr.xControlledIterator(), false, true);
            }
        } else {
            currentNx = null;
            currentGx = gx;
            currentChangedXStart = gx.domainModifiedMinOffset();
            currentChangedXEnd = gx.domainModifiedMaxOffset();
            currentChangedYStart = -1;
            currentChangedYEnd = -1;
            if (currentChangedXEnd < currentChangedXStart) return;
            propagateUsingIterator(idxMgr.xControlledIterator(), false, true);
        }
    }

    /**
     * Propagate changes based on the fact that the generic node Y has had changes
     * to internal nodes
     */
    protected void propagateForChangeToGenericY() throws PropagationFailureException {
        if (!idxMgr.useCombinedY()) {
            int start = -1;
            int end = -1;
            start = gy.domainModifiedMinOffset();
            end = gy.domainModifiedMaxOffset();
            if (start == end && end == -1) return;
            for (int i = start; i <= end; i++) {
                gy.setIndicesToNodeOffset(i);
                currentNy = (BooleanNode) gy.getNode(i);
                propagateUsingIterator(idxMgr.yControlledIterator(), true, false);
            }
        } else {
            currentNy = null;
            currentGy = gy;
            currentChangedXStart = -1;
            currentChangedXEnd = -1;
            currentChangedYStart = gy.domainModifiedMinOffset();
            currentChangedYEnd = gy.domainModifiedMaxOffset();
            if (currentChangedYEnd < currentChangedYStart) return;
            propagateUsingIterator(idxMgr.yControlledIterator(), true, false);
        }
    }

    /** 
     * Function that iterates over appropriate X, Y and Z combinations and calls
     * propagateCurrentNodes for appropriate combinations
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateAllSources() throws PropagationFailureException {
        currentChangedXStart = -1;
        currentChangedXEnd = -1;
        currentChangedYStart = -1;
        currentChangedYEnd = -1;
        propagateUsingIterator(idxMgr.iterator(), true, true);
    }

    /** 
     * Function that iterates over appropriate X, Y and Z combinations and calls
     * setCurrentNodeAndPropagate to complete propagation
     *
     * @param idxIterator   iterator object to use when looping over X, Y and Z combinations
     * @param setX          true if current X node should be set as iterator is processed
     * @param setY          true if current Y node should be set as iterator is processed
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void propagateUsingIterator(Iterator<CspGenericIndex> idxIterator, boolean setX, boolean setY) throws PropagationFailureException {
        if (idxIterator == null) {
            setCurrentNodeAndPropagate(setX, setY);
        } else {
            while (idxIterator.hasNext()) {
                idxIterator.next();
                setCurrentNodeAndPropagate(setX, setY);
            }
        }
    }

    /** 
     * Function that will set nodes based on current index settings and
     * and flags indicating which nodes should be set then calls propagateCurrentNodes for 
     * appropriate combinations
     *
     * @param setX  true if current X node should be set as iterator is processed
     * @param setY  true if current Y node should be set as iterator is processed
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected void setCurrentNodeAndPropagate(boolean setX, boolean setY) throws PropagationFailureException {
        if (!idxMgr.zIndexIsRestricted()) {
            currentNz = null;
            currentGz = gz;
            if (nz != null) currentNz = nz; else if (!idxMgr.useCombinedZ()) currentNz = (BooleanNode) gz.getNodeForIndex();
        }
        if (setX && !idxMgr.xIndexIsRestricted()) {
            currentNx = null;
            currentGx = gx;
            currentXConst = null;
            currentXGenConst = xGenConst;
            if (nx != null) currentNx = nx; else if (gx != null && !idxMgr.useCombinedX()) currentNx = (BooleanNode) gx.getNodeForIndex(); else if (xGenConst != null && !idxMgr.useCombinedX()) currentXConst = new Boolean(xGenConst.getBooleanForIndex()); else if (xconst != null) currentXConst = xconst;
        }
        if (setY && !idxMgr.yIndexIsRestricted()) {
            currentNy = null;
            currentGy = gy;
            currentYConst = null;
            currentYGenConst = yGenConst;
            if (ny != null) currentNy = ny; else if (gy != null && !idxMgr.useCombinedY()) currentNy = (BooleanNode) gy.getNodeForIndex(); else if (yGenConst != null && !idxMgr.useCombinedY()) currentYConst = new Boolean(yGenConst.getBooleanForIndex()); else if (yconst != null) currentYConst = yconst;
        }
        propagateCurrentNodes();
    }

    /** 
     * Performs actual propagation of changes between x, y and z nodes
     *
     * @throws PropagationFailureException  If domain of target node becomes empty
     */
    protected abstract void propagateCurrentNodes() throws PropagationFailureException;

    /**
     * Returns true if any X node is false
     */
    public boolean isAnyXFalse() {
        if (currentXConst != null) return !(currentXConst.booleanValue() ^ notX);
        if (currentXGenConst != null) return (notX) ? currentXGenConst.isAnyTrue() : currentXGenConst.isAnyFalse();
        if (currentNx != null) return (notX) ? currentNx.isTrue() : currentNx.isFalse();
        if (currentChangedXStart >= 0) {
            return (notX) ? currentGx.isAnyTrue(currentChangedXStart, currentChangedXEnd) : currentGx.isAnyFalse(currentChangedXStart, currentChangedXEnd);
        }
        return (notX) ? currentGx.isAnyTrue() : currentGx.isAnyFalse();
    }

    /**
     * Returns true if any Y node is false
     */
    public boolean isAnyYFalse() {
        if (currentYConst != null) return !(currentYConst.booleanValue() ^ notY);
        if (currentYGenConst != null) return (notY) ? currentYGenConst.isAnyTrue() : currentYGenConst.isAnyFalse();
        if (currentNy != null) return (notY) ? currentNy.isTrue() : currentNy.isFalse();
        if (currentChangedYStart >= 0) {
            return (notY) ? currentGy.isAnyTrue(currentChangedYStart, currentChangedYEnd) : currentGy.isAnyFalse(currentChangedYStart, currentChangedYEnd);
        }
        return (notY) ? currentGy.isAnyTrue() : currentGy.isAnyFalse();
    }

    /**
     * Returns true if any Z node is false
     */
    public boolean isAnyZFalse() {
        if (currentNz != null) return currentNz.isFalse() ^ notZ;
        return (notZ) ? currentGz.isAnyTrue() : currentGz.isAnyFalse();
    }

    /**
     * Returns true if any X node is true
     */
    public boolean isAnyXTrue() {
        if (currentXConst != null) return currentXConst.booleanValue() ^ notX;
        if (currentXGenConst != null) return currentXGenConst.isAnyTrue() ^ notX;
        if (currentNx != null) return (notX) ? currentNx.isFalse() : currentNx.isTrue();
        if (currentChangedXStart >= 0) {
            return (notX) ? currentGx.isAnyFalse(currentChangedXStart, currentChangedXEnd) : currentGx.isAnyTrue(currentChangedXStart, currentChangedXEnd);
        }
        return (notX) ? currentGx.isAnyFalse() : currentGx.isAnyTrue();
    }

    /**
     * Returns true if any Y node is true
     */
    public boolean isAnyYTrue() {
        if (currentYConst != null) return currentYConst.booleanValue() ^ notY;
        if (currentYGenConst != null) return currentYGenConst.isAnyTrue() ^ notY;
        if (currentNy != null) return (notY) ? currentNy.isFalse() : currentNy.isTrue();
        if (currentChangedYStart >= 0) {
            return (notY) ? currentGy.isAnyFalse(currentChangedYStart, currentChangedYEnd) : currentGy.isAnyTrue(currentChangedYStart, currentChangedYEnd) ^ notY;
        }
        return (notY) ? currentGy.isAnyFalse() : currentGy.isAnyTrue();
    }

    /**
     * Returns true if any Z node is false
     */
    public boolean isAnyZTrue() {
        if (currentNz != null) return currentNz.isTrue() ^ notZ;
        return (notZ) ? currentGz.isAnyFalse() : currentGz.isAnyTrue();
    }

    /**
     * Sets Z node to true taking into account if target Z is a generic
     * node and whether notZ flag is set
     */
    protected void setTargetTrue() throws PropagationFailureException {
        if (currentNz != null) {
            if (notZ) currentNz.setFalse(); else currentNz.setTrue();
        } else if (currentGz != null) {
            if (notZ) {
                for (int i = 0; i < currentGz.getNodeCount(); i++) ((BooleanNode) currentGz.getNode(i)).setFalse();
            } else {
                for (int i = 0; i < currentGz.getNodeCount(); i++) ((BooleanNode) currentGz.getNode(i)).setTrue();
            }
        }
    }

    /**
     * Sets Z node to false taking into account if target Z is a generic
     * node and whether notZ flag is set
     */
    protected void setTargetFalse() throws PropagationFailureException {
        if (currentNz != null) {
            if (notZ) currentNz.setTrue(); else currentNz.setFalse();
        } else if (currentGz != null) {
            if (notZ) {
                for (int i = 0; i < currentGz.getNodeCount(); i++) ((BooleanNode) currentGz.getNode(i)).setTrue();
            } else {
                for (int i = 0; i < currentGz.getNodeCount(); i++) ((BooleanNode) currentGz.getNode(i)).setFalse();
            }
        }
    }

    public void propagate() throws PropagationFailureException {
        propagateAllSources();
    }

    public void propagate(Node src) throws PropagationFailureException {
        propagateForInitiatingSource(src);
    }
}
