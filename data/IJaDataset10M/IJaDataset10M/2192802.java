package org.xith3d.behaviors;

import javax.vecmath.Vector3f;
import org.xith3d.scenegraph.TransformGroup;

/*********************************************************************
 * Updates a translation with a speed and direction.
 * 
 * @version
 *   $Id: VelocityBehavior.java 1126 2007-02-01 03:02:10Z qudus $
 * @since
 *   2005-09-29
 * @author
 *   <a href="http://www.CroftSoft.com/">David Wallace Croft</a>
 *********************************************************************/
public interface VelocityBehavior extends Behavior {

    public float getSpeed();

    public Vector3f getDirectionVector3f();

    public TransformGroup getTargetTransformGroup();

    public void setSpeed(float speed);

    public void setDirectionVector3f(Vector3f directionVector3f);

    public void setTargetTransformGroup(TransformGroup targetTransformGroup);
}
