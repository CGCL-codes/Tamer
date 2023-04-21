package com.bbn.vessel.author.graphEditor.views;

import java.awt.Point;

/**
 * Methods that a Draggable object must implement
 * 
 * @author RTomlinson
 */
public interface Draggable {

    /**
     * Update the location of the object to the specified location. Additional
     * appearance changes may be made based on bestView which specifies the
     * nearest object of an appropriate class.
     * 
     * @param point
     * @param bestView
     */
    void updateLocation(Point point, View bestView);

    /**
     * Stop dragging the Draggable object and adjust its value depending on the
     * View that it was dropped onto
     * 
     * @param nearestView
     *            The View onto which the draggable was dropped
     */
    void drop(View nearestView);

    /**
     * Perform changes to allow the Draggable object to be dragged
     * 
     * @param pickupPoint
     *            TODO
     */
    void pickUp(Point pickupPoint);

    /**
     * @return the name of this Draggable
     */
    String getName();
}
