package org.xith3d.behaviors;

/*********************************************************************
 * Equivalent to javax.media.j3d.RotationInterpolator.
 * 
 * @version
 *   $Date: 2007-01-31 22:02:10 -0500 (Wed, 31 Jan 2007) $
 * @since
 *   2005-10-13
 * @author
 *   <a href="http://www.CroftSoft.com/">David Wallace Croft</a>
 *********************************************************************/
public interface RotationInterpolator extends TransformInterpolator {

    public float getMinimumAngle();

    public float getMaximumAngle();

    public void setMinimumAngle(float minimumAngle);

    public void setMaximumAngle(float maximumAngle);
}
