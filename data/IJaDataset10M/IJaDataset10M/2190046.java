package com.xenoage.zong.renderer.stampings;

import com.xenoage.util.annotations.Demo;
import com.xenoage.util.color.ColorInfo;
import com.xenoage.util.math.Point2f;
import com.xenoage.util.math.Size2f;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.TestStamping;
import com.xenoage.zong.renderer.RenderingParams;

/**
 * Renderer for a test stamping,
 * used for testing purposes.
 * 
 * Draws the border of a rectangle with
 * a given color.
 *
 * @author Andreas Wenger
 */
@Demo
public class TestStampingRenderer extends StampingRenderer {

    /**
	 * Draws the given stamping, using the given parameters.
	 */
    @Override
    public void draw(Stamping stamping, RenderingParams params) {
        TestStamping s = (TestStamping) stamping;
        drawWith(s.position, s.size, s.color, params);
    }

    public static void drawWith(Point2f position, Size2f size, ColorInfo color, RenderingParams params) {
        Point2f pNW = position;
        Point2f pSE = new Point2f(position.x + size.width, position.y + size.height);
        Point2f pNE = new Point2f(pSE.x, pNW.y);
        Point2f pSW = new Point2f(pNW.x, pSE.y);
        params.renderTarget.drawLine(pNW, pNE, color, 1);
        params.renderTarget.drawLine(pNE, pSE, color, 1);
        params.renderTarget.drawLine(pSE, pSW, color, 1);
        params.renderTarget.drawLine(pSW, pNW, color, 1);
    }
}
