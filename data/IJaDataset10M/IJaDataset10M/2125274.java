package skycastle.geometry.operations;

import skycastle.geometry.FigureVisitor;
import skycastle.geometry.Geometry;
import skycastle.geometry.GeometryAccessor;
import skycastle.geometry.figures.Area;
import skycastle.geometry.figures.Line;
import skycastle.geometry.figures.Point;
import skycastle.geometry.figures.Polygon;
import skycastle.util.ParameterChecker;
import skycastle.util.region.RegionOfInterest;
import java.util.HashSet;
import java.util.Set;

/**
 * A Generator joining together several Geometries
 *
 * @author Hans H�ggstr�m
 */
public class CombineGeometries extends AbstractGeometry implements Geometry {

    public GeometryAccessor createGeometryAccessor(RegionOfInterest region) {
        throw new UnsupportedOperationException("This Geometry implementation has not yet been implemented.");
    }

    private Set<Geometry> mySourceGeometries = new HashSet<Geometry>();

    public void visitPolygons(RegionOfInterest regionOfInterest, FigureVisitor<Polygon> polygonVisitor) {
        for (Geometry sourceGeometry : mySourceGeometries) {
            sourceGeometry.visitPolygons(regionOfInterest, polygonVisitor);
        }
    }

    public void visitAreas(RegionOfInterest regionOfInterest, FigureVisitor<Area> areaVisitor) {
        for (Geometry sourceGeometry : mySourceGeometries) {
            sourceGeometry.visitAreas(regionOfInterest, areaVisitor);
        }
    }

    public void visitLines(RegionOfInterest regionOfInterest, FigureVisitor<Line> lineVisitor) {
        for (Geometry sourceGeometry : mySourceGeometries) {
            sourceGeometry.visitLines(regionOfInterest, lineVisitor);
        }
    }

    public void visitPoints(RegionOfInterest regionOfInterest, FigureVisitor<Point> pointVisitor) {
        for (Geometry sourceGeometry : mySourceGeometries) {
            sourceGeometry.visitPoints(regionOfInterest, pointVisitor);
        }
    }

    /**
     * Adds the specified Geometry.
     *
     * @param addedGeometry should not be null or already added.
     */
    public void addGeometry(Geometry addedGeometry) {
        ParameterChecker.checkNotNull(addedGeometry, "addedGeometry");
        ParameterChecker.checkNotAlreadyContained(addedGeometry, mySourceGeometries, "mySourceGeometries");
        mySourceGeometries.add(addedGeometry);
    }

    /**
     * Removes the specified Geometry.
     *
     * @param removedGeometry should not be null.
     */
    public void removeGeometry(Geometry removedGeometry) {
        ParameterChecker.checkNotNull(removedGeometry, "removedGeometry");
        mySourceGeometries.remove(removedGeometry);
    }
}
