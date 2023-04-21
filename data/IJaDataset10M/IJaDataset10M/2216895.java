package com.xith3d.spatial.octree;

/**
 * <p>Interface which supplies different techniques for traversing and
 * octree and culling objects.</p>
 * <p> </p>
 * <p>Copyright: Copyright (c) 2000,2001</p>
 * <p>Company: Teseract Software, LLP</p>
 * @author David Yazel
 *
 */
public interface OcCuller {

    /**
     * Checks the cell and returns whether it should culled or not.  In general this
     * is a box test performed against the cell.
     * @param cell
     * @return
     */
    int checkCell(OcCell cell);

    /**
     * Checks the node and returns whether it should be culled or not.  In general
     * this is a sphere test applied to the node.
     * @param node
     * @return
     */
    int checkNode(OcNode node);
}
