package com.trapezium.attractor;

import com.trapezium.space.*;
import java.util.BitSet;

/** The CoordAttractor attracts one set of coordinates to a second set.
 *  The end result creates a set of floater coordinates for each attractor
 *  coordinates.
 *
 *  @author          Johannes N. Johannsen
 *  @version         1.0, 7 Oct 1998
 *
 *  @since           1.0
 */
public class CoordAttractor {

    int[] preservedFloaterOffsets;

    float[] preservedFloaterDistances;

    BitSet[] attractedFloaters;

    SpaceEntitySet attractors;

    int numberAttractors;

    AttractorSelector attractorSelector;

    /** class constructor */
    public CoordAttractor(SpaceEntitySet attractors) {
        this.attractors = attractors;
        numberAttractors = attractors.getNumberEntities();
        preservedFloaterOffsets = new int[numberAttractors];
        preservedFloaterDistances = new float[numberAttractors];
        attractedFloaters = new BitSet[numberAttractors];
        for (int i = 0; i < numberAttractors; i++) {
            preservedFloaterOffsets[i] = -1;
        }
    }

    /** attract floaters to attractors */
    public void attract(SpaceEntitySet floaters) {
        attract(floaters, null);
    }

    /** attract floaters to attractors, mark floaters to be preserved.
     *
     *  @param floaters the floaters to attract
     *  @param preservedFloaters if present, a BitSet size of floater set,
     *      used to mark which floaters are to be preserved.
     */
    public void attract(SpaceEntitySet floaters, BitSet preservedFloaters) {
        int numberFloaters = floaters.getNumberEntities();
        for (int i = 0; i < numberAttractors; i++) {
            attractedFloaters[i] = new BitSet(numberFloaters);
        }
        attractorSelector = createAttractorSelector();
        float[] floaterEntity = new float[3];
        for (int i = 0; i < numberAttractors; i++) {
            floaters.getLocation(i, floaterEntity);
            attractFloater(i, floaterEntity);
        }
        markPreservedFloaters(preservedFloaters);
    }

    /** factory method, creates object used to select nearest attractor, 
     *  subclasses override this to also select which floater in the
     *  attracted sets get preserved (see NearMarkingAttractorSelector and
     *  FarMarkingAttractorSelector).
     */
    public AttractorSelector createAttractorSelector() {
        return (new AttractorSelector());
    }

    /** Attract a floater to the nearest attractor.  This is done by
     *  the attractorSelector object.  This method finds the distance
     *  from the floater to each attractor, and the attractorSelector
     *  keeps track of which is the currently selected attractor.
     *
     *  @param floaterOffset the offset of the floater
     *  @param floater the SpaceEntity vertex
     */
    void attractFloater(int floaterOffset, float[] floaterEntity) {
        attractorSelector.reset();
        float[] attractorEntity = new float[3];
        for (int i = 0; i < numberAttractors; i++) {
            attractors.getLocation(i, attractorEntity);
            attractorSelector.attemptAttractorSelection(i, distanceBetween(floaterEntity, attractorEntity));
        }
        attractorSelector.updatePreservedInfo(preservedFloaterOffsets, preservedFloaterDistances, floaterOffset);
        attractedFloaters[attractorSelector.getSelection()].set(floaterOffset);
    }

    float distanceBetween(float[] f1, float[] f2) {
        float xDis = f1[0] - f2[0];
        float yDis = f1[1] - f2[1];
        float zDis = f1[2] - f2[2];
        xDis = xDis * xDis;
        yDis = yDis * yDis;
        zDis = zDis * zDis;
        float dis = xDis + yDis + zDis;
        try {
            return ((float) Math.sqrt(dis));
        } catch (Exception e) {
            return ((float) 0);
        }
    }

    /** mark the floaters that are to be preserved, this information is
     *  actually set up in "attractorSelector.updatePreservedInfo" above,
     *  so is entirely dependent on the AttractorSelector subclass.
     *  The base class (this one) uses the base class AttractorSelector
     *  which doesn't use this information, so its "updatePreservedInfo"
     *  method does nothing, and it always calls "attract" without any
     *  preservedFloater BitSet.
     */
    void markPreservedFloaters(BitSet preservedFloaters) {
        if (preservedFloaters != null) {
            for (int i = 0; i < numberAttractors; i++) {
                if (preservedFloaterOffsets[i] != -1) {
                    preservedFloaters.set(preservedFloaterOffsets[i]);
                }
            }
        }
    }

    /** Get the offset of the attractor that has attracted a particular floater
     *
     * @param floaterOffset the floater that has been attracted.
     * @return the offset of the associated tractor, -1 if none found
     */
    public int getAttractorOffset(int floaterOffset) {
        for (int i = 0; i < numberAttractors; i++) {
            if (attractedFloaters[i].get(floaterOffset)) {
                return (i);
            }
        }
        return (-1);
    }

    /** Get the offset of the preserved floater associated with a
     *  particular attractor.
     *
     *  @param attractorOffset the attractor to check
     *  @return the preserved floater associated with the attractor
     */
    public int getPreservedFloaterOffset(int attractorOffset) {
        return (preservedFloaterOffsets[attractorOffset]);
    }
}
