package edu.ucsd.ncmir.jinx.contour_edit;

import edu.ucsd.ncmir.jinx.core.JxPoint;
import edu.ucsd.ncmir.jinx.objects.JxPointInPolygon;
import edu.ucsd.ncmir.jinx.objects.trace.JxTrace;
import java.util.ArrayList;

/**
 *
 * @author spl
 */
public class JxCropInsideToOutside extends JxChecker {

    public JxCropInsideToOutside(ArrayList<JxPoint> points, JxTrace inner, JxPoint[] ipoints, JxTrace outer, JxPoint[] opoints, int index, int next_intersection, int[][][] intersections) {
        super(points, inner, ipoints, outer, opoints, index, next_intersection, intersections);
    }

    public void check(int deflection) {
        do {
            JxPoint p = super._opoints[super._index];
            super._index = (super._index + 1) % super._opoints.length;
            p = super.deflect(1 * deflection, p, super._opoints[super._index]);
            super._points.add(p);
        } while (super._inner.isPointInside(super._opoints[super._index]).equals(JxPointInPolygon.INSIDE));
        if (super._next_intersection < super._intersections.length) {
            int index = super._intersections[super._next_intersection][0][1];
            new JxAcceptInside(super._points, super._inner, super._ipoints, super._outer, super._opoints, index, super._next_intersection, super._intersections).check(deflection);
        }
    }
}
