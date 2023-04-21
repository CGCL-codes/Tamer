package org.gjt.gamp.gamp;

import java.util.*;
import javax.vecmath.*;

/**
Some SceneObject (Polygon mesh primitives, Spline patch primitives) will
have a varying number of control points that define their shape. For these
objects control points can be grouped, assigned a name and remembered. This
interface defines the behavior of these objects.
*/
public interface HasControlPoints {

    /**
	Get the number of control points that this object has
	*/
    public int getNumControlPoints();

    /**
	Get all the control points of this object
	*/
    public void getControlPoints(Vector3d v);

    /**
	get a vector containing all the groups of control points defined by
	the user for this SceneObject
	*/
    public Vector getGroups();

    /**
	cpg is a subset of this Objects control points. It will get stored
	so that it can be conveniently retreived later
	*/
    public void addGroup(Set cpg);

    /**
	remove a group of control points from this object. Note that this does
	not remove the control points, just that particular grouping.
	*/
    public void removeGroup(Set cpg);
}
