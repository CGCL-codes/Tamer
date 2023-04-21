package DE.FhG.IGD.earth.model.graph;

import DE.FhG.IGD.earth.utils.*;
import java.io.*;
import java.lang.*;

/**
 * This class represents a cylinder (graphic object, leaf).
 * A cylinder is defined by it's radius and it's height.
 * Also you can define divisions. As higher the division value as
 * smoother the cylinder will be created.
 *
 * @Title        : Earth
 * @Copyright    : Copyright (c) 2001
 * @Organisation : IGD FhG
 * @author       : Werner Beutel
 * @version      : 1.0
 */
public class Cylinder extends Primitive implements Serializable, Cloneable {

    /**
     * radius
     */
    private float radius_;

    /**
     * height
     */
    private float height_;

    /**
     * x-division
     */
    private int xdivision_;

    /**
     * y-division
     */
    private int ydivision_;

    /*************************************************************************
     * Creates and initializes a default cylinder.
     * radius=1, height=1, xdivision=10, ydivision=10.
     ************************************************************************/
    public Cylinder() {
        super();
        radius_ = 1;
        height_ = 1;
        xdivision_ = 10;
        ydivision_ = 10;
    }

    /*************************************************************************
     * Creates and initializes a cylinder with the given values.
     * @param rad Radius
     * @param h Height
     * @param xdiv xdivision
     * @param ydiv ydivision
     ************************************************************************/
    public Cylinder(float rad, float h, int xdiv, int ydiv) {
        super();
        radius_ = rad;
        height_ = h;
        xdivision_ = xdiv;
        ydivision_ = ydiv;
    }

    /*************************************************************************
     * Calculates the bounding box of this cylinder. This method is used by
     * Node.getBoundingBox().
     * @param boundingBox which will be combined with
     ************************************************************************/
    protected void calcBoundingBox(cBoundingBox boundingBox) {
        if (this.getBoundless()) return;
        boundingBox.combine(new cPoint3d((double) (radius_ / 2), (double) (height_ / 2), (double) (radius_ / 2)));
        boundingBox.combine(new cPoint3d((double) (-radius_ / 2), (double) (-height_ / 2), (double) (-radius_ / 2)));
    }

    /*************************************************************************
     * Returns TYPE_LEAF
     * @return TYPE_LEAF
     ************************************************************************/
    public int getType() {
        return this.TYPE_Leaf;
    }

    /*************************************************************************
     * Returns SUBTYPE_CYLINDER
     * @return SUBTYPE_CYLINDER
     ************************************************************************/
    public int getSubType() {
        return this.SUBTYPE_Cylinder;
    }

    /*************************************************************************
     * Sets a new radius (blocked by mutable).
     * Released changes: CHANGE_UPDATE, CHANGE_SHAPE
     * @param rad Radius
     * @return <code>true</code> on success.
     ************************************************************************/
    public boolean setRadius(float rad) {
        if (!mutable_) return false;
        radius_ = rad;
        this.setChanges(this.CHANGE_Update);
        this.setChanges(this.CHANGE_Shape);
        return true;
    }

    /*************************************************************************
     * Returns the radius
     * @return Radius
     ************************************************************************/
    public float getRadius() {
        return radius_;
    }

    /*************************************************************************
     * Sets a new height (blocked by mutable).
     * Released changes: CHANGE_UPDATE, CHANGE_SHAPE
     * @param h Height
     * @return <code>true</code> on success.
     ************************************************************************/
    public boolean setHeight(float h) {
        if (!mutable_) return false;
        height_ = h;
        this.setChanges(this.CHANGE_Update);
        this.setChanges(this.CHANGE_Shape);
        return true;
    }

    /*************************************************************************
     * Returns the height
     * @return Height
     ************************************************************************/
    public float getHeight() {
        return height_;
    }

    /*************************************************************************
     * Sets a new xdivision (blocked by mutable).
     * Released changes: CHANGE_UPDATE, CHANGE_SHAPE
     * @param xdiv xdivision
     * @return <code>true</code> on success.
     ************************************************************************/
    public boolean setXdivision(int xdiv) {
        if (!mutable_) return false;
        xdivision_ = xdiv;
        this.setChanges(this.CHANGE_Update);
        this.setChanges(this.CHANGE_Shape);
        return true;
    }

    /*************************************************************************
     * Returns the xdivision
     * @return xdivision
     ************************************************************************/
    public int getXdiv() {
        return xdivision_;
    }

    /*************************************************************************
     * Sets a new ydivision (blocked by mutable).
     * Released changes: CHANGE_UPDATE, CHANGE_SHAPE
     * @param ydiv ydivision
     * @return <code>true</code> on success.
     ************************************************************************/
    public boolean setYdiv(int ydiv) {
        if (!mutable_) return false;
        ydivision_ = ydiv;
        this.setChanges(this.CHANGE_Update);
        this.setChanges(this.CHANGE_Shape);
        return true;
    }

    /*************************************************************************
     * Returns the ydivision
     * @return ydivision
     ************************************************************************/
    public int getYdiv() {
        return ydivision_;
    }

    /*************************************************************************
     * Clones this cylinder
     ************************************************************************/
    public Object clone() {
        try {
            Cylinder c = (Cylinder) super.clone();
            return c;
        } catch (Exception e) {
            return null;
        }
    }
}
