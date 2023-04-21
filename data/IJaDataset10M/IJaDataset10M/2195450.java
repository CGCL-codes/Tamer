package de.hdm.cefx.concurrency.operations;

import java.io.Serializable;
import de.hdm.cefx.exceptions.NodePositionException;

/**
 * The position of the node at which it is located in the document.
 *
 * @author Ansgar Gerlicher
 *
 */
public class NodePosition implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int relativeInsertPosition = 0;

    public static final int INSERT_BEFORE = 0;

    public static final int INSERT_AFTER = 1;

    public String parentNodeId = null;

    public String fixNodeId = null;

    /**
	 * Class constructor.
	 *
	 * @param parentNodeId
	 *            the parent node UUID.
	 * @param fixNodeId
	 *            the fixnode UUID.
	 * @param pos
	 *            the relative position (before or after).
	 * @throws NodePositionException
	 */
    public NodePosition(String parentNodeId, String fixNodeId, int pos) throws NodePositionException {
        if (parentNodeId == null || parentNodeId.equals("null") || parentNodeId.equals("")) throw new NodePositionException("parentid is null or empty");
        if (pos < INSERT_BEFORE || pos > INSERT_AFTER) throw new NodePositionException("relative position is invalid: " + pos);
        relativeInsertPosition = pos;
        this.parentNodeId = parentNodeId;
        this.fixNodeId = fixNodeId;
    }

    /**
	 * Retrieves the fix node UUID.
	 *
	 * @return the fix node UUID.
	 */
    public String getFixNodeId() {
        return fixNodeId;
    }

    /**
	 * Retrieves the relative postion (before or after). 0=INSERT_BEFORE,
	 * 1=INSERT_AFTER.
	 *
	 * @return the relative position.
	 */
    public int getRelativeInsertPosition() {
        return relativeInsertPosition;
    }

    /**
	 * Set the relative insert position.
	 *
	 * @param relativeInsertPosition
	 *            the relative position. INSERT_BEFORE or INSERT_AFTER.
	 */
    public void setRelativeInsertPosition(int relativeInsertPosition) {
        this.relativeInsertPosition = relativeInsertPosition;
    }

    /**
	 * Compares two node positions in order to check if they are identical.
	 *
	 * @param npos
	 *            the node position this position should be compared with.
	 * @return true if the node positions are identical.
	 */
    public boolean equals(NodePosition npos) {
        if (!parentNodeId.equals(npos.parentNodeId)) return false; else {
            if (npos.fixNodeId == null) {
                if (fixNodeId == null) return true; else return false;
            } else if (npos.fixNodeId.equals(fixNodeId)) {
                return npos.relativeInsertPosition == relativeInsertPosition;
            } else {
                return false;
            }
        }
    }

    /**
	 * Retrieves the parent's UUID.
	 *
	 * @return the UUID of the parent node.
	 */
    public String getParentNodeId() {
        return parentNodeId;
    }

    /**
	 * Sets the UUID of the parent node.
	 *
	 * @param parentNodeId
	 *            UUID of the parent node.
	 */
    public void setParentNodeId(String parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    /**
	 * Sets the UUID of the fix node.
	 *
	 * @param fixNodeId
	 *            the UUID of the fix node.
	 */
    public void setFixNodeId(String fixNodeId) {
        this.fixNodeId = fixNodeId;
    }
}
