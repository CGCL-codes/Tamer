package com.xenoage.zong.renderer.beams;

import com.xenoage.util.color.ColorInfo;
import com.xenoage.util.math.Point2f;
import com.xenoage.zong.renderer.context.GLGraphicsContext;

/**
 * Strategy to form a {@link Beam}.
 * 
 * There is a method which draws the beam with OpenGL.
 * 
 * @author Andreas Wenger
 */
public interface BeamShape {

    /**
	 * Draws the beam with OpenGL, using the given context and color.
	 * The beam is positioned at the given four points in clockwise order,
   * beginning with the lower left point.
   * The current interline space in px must also be given.
	 */
    public void draw(GLGraphicsContext gl, ColorInfo color, Point2f[] points, float interlineSpace);
}
