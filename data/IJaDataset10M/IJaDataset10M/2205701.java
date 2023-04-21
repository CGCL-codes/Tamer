package org.dancecues.gui;

import java.awt.Point;

/**
 * GUI components that are Cursible are ones that can have a cursor.
 * They must be focusable (at least while they don't have focus... once
 * they obtain focus they can do whatever they want with it).
 */
public interface Cursible {

    /**
     * Can be used when iterating or moving the cursor to determine where
     * to position it next.
     * 
     * @return the number of cursible children that this Cursible has.
     */
    public int numCursibleChildren();

    /**
     * Returns the children of this component in the order that the cursor should
     * move over them.  Throws IndexOutOfBoundsException if index > numCursibleChildren().
     * 
     * @param index Which child is requested.
     * @return the 'index'th child of this component.
     */
    public Cursible getCursibleChild(int index);

    /**
     * Tells this cursible to become the active one and display a cursor.  Two points are given,
     * the point where the cursor last was, and the point where the cursor should be.  Both are optional,
     * and both can be ignored if the Cursible so desires.  They exist as suggestions, for example to try to keep
     * the cursor in more-or-less the same place vertically as the user moves through lines.
     * If the Cursible has children, it may delegate this to a child as necessary, keeping in mind the
     * need to reframe the coordinates on the parameters to the child's coordinate space.
     * 
     * @param oldLoc    Location where the cursor last was, relative to this component's coordinate space.
     *                  Optional, can be null.
     * @param newLoc    Location where the cursor "should" be, relative to this component's coordinate space.
     *                  Optional, can be null.
     */
    public void activateCursor(Point oldLoc, Point newLoc);
}
