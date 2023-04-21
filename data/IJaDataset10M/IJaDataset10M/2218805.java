package de.fhg.igd.earth.model.graph;

import java.io.Serializable;
import java.util.BitSet;

/**
 * This class represents a SwitchGroup.
 * The children can switched switched on or off controlled by the childMask
 * (BitSet) and the witchChild value.
 * If the witchChild value is set to CHILD_MASK the projector filters the
 * children with the childMask. This class is used to realizes the LOD-Groups
 *
 * Title        : Earth
 * Copyright    : Copyright (c) 2001
 * Organisation : IGD FhG
 * @author       : Werner Beutel
 * @version      : 1.0
 */
public abstract class SwitchGroup extends Group implements Serializable, Cloneable {

    /**
     * don't show any children
     */
    public static final int CHILD_NONE = -1;

    /**
     * show all children
     */
    public static final int CHILD_ALL = -2;

    /**
     * show only the childMask filtered children
     */
    public static final int CHILD_MASK = -3;

    /**
     * childMask
     */
    private BitSet childMask_;

    /**
     * main filter (NONE,ALL,MASK)
     */
    private int witchChild_;

    /*************************************************************************
     * Creates an instance of this class. witchChild is set to CHILD_NONE
     ************************************************************************/
    public SwitchGroup() {
        super();
        childMask_ = new BitSet();
        witchChild_ = CHILD_NONE;
    }

    /*************************************************************************
     * Creates an instance of this class and set witchChild.
     * @param wc witchChild
     ************************************************************************/
    public SwitchGroup(int wc) {
        super();
        childMask_ = new BitSet();
        witchChild_ = wc;
    }

    /*************************************************************************
     * Creates an instance of this class and set witchChild, childMask.
     * @param wc witchChild
     * @param cm childMask
     ************************************************************************/
    public SwitchGroup(int wc, BitSet cm) {
        super();
        childMask_ = cm;
        witchChild_ = wc;
    }

    /*************************************************************************
     * Returns the childMask
     * @return childMask
     ************************************************************************/
    public BitSet getChildMask() {
        return childMask_;
    }

    /*************************************************************************
     * Sets the childMask.
     * @param childMask
     ************************************************************************/
    public void setChildMask(BitSet cm) {
        childMask_ = cm;
        this.setChanges(CHANGE_Update);
    }

    /*************************************************************************
     * Returns the witchChild value
     * @return witchChild
     ************************************************************************/
    public int getWitchChild() {
        return witchChild_;
    }

    /*************************************************************************
     * Sets the witchChild value. CHILD_NONE, CHILD_ALL, CHILD_MASK
     * @param witchChild
     ************************************************************************/
    public void setWitchChild(int wc) {
        witchChild_ = wc;
        this.setChanges(CHANGE_Update);
    }

    /*************************************************************************
     * This method can be overwritten by subclasses.
     ************************************************************************/
    protected void childAddedEvent(int index) {
    }

    /*************************************************************************
     * This method can be overwritten by subclasses.
     ************************************************************************/
    protected void childRemovedEvent(int index) {
    }

    /*************************************************************************
     * Clones the SwitchGroup
     ************************************************************************/
    public Object clone() {
        try {
            SwitchGroup sg = (SwitchGroup) super.clone();
            sg.childMask_ = (BitSet) this.childMask_.clone();
            return sg;
        } catch (Exception e) {
            return null;
        }
    }
}
