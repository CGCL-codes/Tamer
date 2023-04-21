package com.welma.des3per.segments;

/**
 * This interface extends {@link Des3PerSegment Des3PerSegment} to allow functionality
 * for segments that owns subsegments
 * @author ragutimar
 */
public interface Des3PerBranchedSegment extends Des3PerSegment {

    /**
     * Adds a child segment
     * @param child child segment
     */
    public void addChild(Des3PerSegment child);

    /**
     * Clear the childs list
     */
    public void clearChilds();

    /**
     * Gets a child by index
     * @param index index of child
     * @return Returns the child segment if index between limits
     */
    public Des3PerSegment getChild(int index);

    /**
     * Gets an array with all childs
     * @return Returns an array
     */
    public Des3PerSegment[] getChilds();

    /**
     * Gets the childs count
     * @return Returns the childs count
     */
    public int getChildsCount();

    /**
     * gets the first child of all
     * @return Returns the first child if exists or null if not
     */
    public Des3PerSegment getFirstChild();

    /**
     * Inserts a child in a position. The childs placed in position an succesives are moved up
     * one position
     * @param child child to insert
     * @param position position to insert
     */
    public void insertChild(Des3PerSegment child, int position);
}
