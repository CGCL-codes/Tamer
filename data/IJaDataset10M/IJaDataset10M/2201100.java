package com.gempukku.animator.composite.switcheffect;

import com.gempukku.animator.Animated;
import com.gempukku.animator.DisplayInfo;
import com.gempukku.animator.decorator.AnimatedContainer;
import com.gempukku.animator.decorator.PerspectiveWarpAnimated;
import javax.media.jai.InterpolationNearest;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

public class FlipEffect implements SwitchEffect<Animated> {

    private final AnimatedContainer _animatedContainer = new AnimatedContainer();

    private final PerspectiveWarpAnimated _perspectiveWarpAnimated = new PerspectiveWarpAnimated(_animatedContainer, new InterpolationNearest(), null, null, null, null);

    @Override
    public void paintEffect(double alpha, Animated animatedFrom, Animated animatedTo, Graphics2D gr, DisplayInfo displayInfo, long time, Animated.AnimatedCallback callback) {
        double rotation = alpha * Math.PI;
        if (alpha < 0.5f) {
            if (animatedFrom != null) {
                Rectangle rect = animatedFrom.getBounds(displayInfo, time);
                double xDist = Math.sin(rotation) * rect.width / 4;
                double yDist = Math.sin(rotation) * rect.height / 2;
                if (yDist < rect.height - yDist - 2) {
                    _perspectiveWarpAnimated.setWarpPoints(new Point2D.Double(rect.x, rect.y + yDist), new Point2D.Double(rect.x + rect.width, rect.y + yDist), new Point2D.Double(rect.x + xDist, rect.y + rect.height - yDist), new Point2D.Double(rect.x + rect.width - xDist, rect.y + rect.height - yDist));
                    _animatedContainer.setAnimated(animatedFrom);
                    _perspectiveWarpAnimated.paintAnimated(gr, displayInfo, time, callback);
                }
            }
        } else {
            if (animatedTo != null) {
                Rectangle rect = animatedTo.getBounds(displayInfo, time);
                double xDist = Math.sin(rotation) * rect.width / 4;
                double yDist = Math.sin(rotation) * rect.height / 2;
                if (yDist < rect.height - yDist - 2) {
                    _perspectiveWarpAnimated.setWarpPoints(new Point2D.Double(rect.x + xDist, rect.y + yDist), new Point2D.Double(rect.x + rect.width - xDist, rect.y + yDist), new Point2D.Double(rect.x, rect.y + rect.height - yDist), new Point2D.Double(rect.x + rect.width, rect.y + rect.height - yDist));
                    _animatedContainer.setAnimated(animatedTo);
                    _perspectiveWarpAnimated.paintAnimated(gr, displayInfo, time, callback);
                }
            }
        }
    }

    @Override
    public Rectangle getBounds(double alpha, Animated animatedFrom, Animated animatedTo, DisplayInfo displayInfo, long time) {
        Animated animated = null;
        if (alpha < 0.5f) animated = animatedFrom; else animated = animatedTo;
        if (animated == null) return new Rectangle(1, 1); else return animated.getBounds(displayInfo, time);
    }
}
