package org.geoforge.worldwind.builder.utils;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.view.orbit.OrbitView;
import java.util.*;

/**
 * @author dcollins
 * @version $Id: ShapeUtils.java 1 2011-07-16 23:22:47Z dcollins $
 * 
 * modified bantchao
 */
public class OurShapeUtils {

    public static double s_getViewportScaleFactor(WorldWindow wwd) {
        return ((OrbitView) wwd.getView()).getZoom() / 8.0;
    }

    public static Position s_getNewShapePosition(WorldWindow wwd) {
        Line ray = new Line(wwd.getView().getEyePoint(), wwd.getView().getForwardVector());
        Intersection[] intersection = wwd.getSceneController().getTerrain().intersect(ray);
        if (intersection != null && intersection.length != 0) {
            return wwd.getModel().getGlobe().computePositionFromPoint(intersection[0].getIntersectionPoint());
        } else if (wwd.getView() instanceof OrbitView) {
            return ((OrbitView) wwd.getView()).getCenterPosition();
        }
        return Position.ZERO;
    }

    public static Angle s_getNewShapeHeading(WorldWindow wwd, boolean matchViewHeading) {
        if (matchViewHeading) {
            if (wwd.getView() instanceof OrbitView) {
                return ((OrbitView) wwd.getView()).getHeading();
            }
        }
        return Angle.ZERO;
    }

    public static List<LatLon> s_createSquareInViewport(WorldWindow wwd, Position position, Angle heading, double sizeInMeters) {
        Globe globe = wwd.getModel().getGlobe();
        Matrix transform = Matrix.IDENTITY;
        transform = transform.multiply(globe.computeSurfaceOrientationAtPosition(position));
        transform = transform.multiply(Matrix.fromRotationZ(heading.multiply(-1)));
        double widthOver2 = sizeInMeters / 2.0;
        double heightOver2 = sizeInMeters / 2.0;
        Vec4[] points = new Vec4[] { new Vec4(-widthOver2, -heightOver2, 0.0).transformBy4(transform), new Vec4(widthOver2, -heightOver2, 0.0).transformBy4(transform), new Vec4(widthOver2, heightOver2, 0.0).transformBy4(transform), new Vec4(-widthOver2, heightOver2, 0.0).transformBy4(transform) };
        LatLon[] locations = new LatLon[points.length];
        for (int i = 0; i < locations.length; i++) {
            locations[i] = new LatLon(globe.computePositionFromPoint(points[i]));
        }
        return Arrays.asList(locations);
    }

    public static List<Position> s_createPositionSquareInViewport(WorldWindow wwd, Position position, Angle heading, double sizeInMeters) {
        Globe globe = wwd.getModel().getGlobe();
        Matrix transform = Matrix.IDENTITY;
        transform = transform.multiply(globe.computeSurfaceOrientationAtPosition(position));
        transform = transform.multiply(Matrix.fromRotationZ(heading.multiply(-1)));
        double widthOver2 = sizeInMeters / 2.0;
        double heightOver2 = sizeInMeters / 2.0;
        double depthOver2 = sizeInMeters / 2.0;
        Vec4[] points = new Vec4[] { new Vec4(-widthOver2, -heightOver2, depthOver2).transformBy4(transform), new Vec4(widthOver2, -heightOver2, depthOver2).transformBy4(transform), new Vec4(widthOver2, heightOver2, depthOver2).transformBy4(transform), new Vec4(-widthOver2, heightOver2, depthOver2).transformBy4(transform) };
        Position[] locations = new Position[points.length];
        for (int i = 0; i < locations.length; i++) {
            locations[i] = globe.computePositionFromPoint(points[i]);
            locations[i] = new Position(locations[i].getLatitude(), locations[i].getLongitude(), 1000);
        }
        return Arrays.asList(locations);
    }

    public static List<Position> s_createPositionTriangleInViewport(WorldWindow wwd, Position position, Angle heading, double sizeInMeters) {
        Globe globe = wwd.getModel().getGlobe();
        Matrix transform = Matrix.IDENTITY;
        transform = transform.multiply(globe.computeSurfaceOrientationAtPosition(position));
        transform = transform.multiply(Matrix.fromRotationZ(heading.multiply(-1)));
        double widthOver2 = sizeInMeters / 2.0;
        double heightOver2 = sizeInMeters / 2.0;
        double depthOver2 = sizeInMeters / 2.0;
        Vec4[] points = new Vec4[] { new Vec4(-widthOver2, -heightOver2, depthOver2).transformBy4(transform), new Vec4(widthOver2, heightOver2, depthOver2).transformBy4(transform) };
        int intMax = 2;
        Position[] locations = new Position[points.length];
        for (int i = 0; i < locations.length; i++) {
            locations[i] = globe.computePositionFromPoint(points[i]);
            locations[i] = new Position(locations[i].getLatitude(), locations[i].getLongitude(), 1000);
        }
        return Arrays.asList(locations);
    }
}
