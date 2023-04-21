package org.weasis.dicom.codec.geometry;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

/**
 * @author dclunie
 */
public class IntersectSlice extends LocalizerPoster {

    public IntersectSlice(Vector3d row, Vector3d column, Point3d tlhc, Tuple3d voxelSpacing, Tuple3d dimensions) {
        localizerRow = row;
        localizerColumn = column;
        localizerTLHC = tlhc;
        localizerVoxelSpacing = voxelSpacing;
        localizerDimensions = dimensions;
        doCommonConstructorStuff();
    }

    public IntersectSlice(GeometryOfSlice geometry) {
        localizerRow = geometry.getRow();
        localizerColumn = geometry.getColumn();
        localizerTLHC = geometry.getTLHC();
        localizerVoxelSpacing = geometry.getVoxelSpacing();
        localizerDimensions = geometry.getDimensions();
        doCommonConstructorStuff();
    }

    private boolean allTrue(boolean[] array) {
        boolean all = true;
        for (int i = 0; i < array.length; ++i) {
            if (!array[i]) {
                all = false;
                break;
            }
        }
        return all;
    }

    private boolean oppositeEdges(boolean[] array) {
        return array[0] && array[2] || array[1] && array[3];
    }

    private boolean adjacentEdges(boolean[] array) {
        return array[0] && array[1] || array[1] && array[2] || array[2] && array[3] || array[3] && array[0];
    }

    private boolean[] classifyCornersOfRectangleIntoEdgesCrossingZPlane(Point3d[] corners) {
        int size = corners.length;
        double[] thisArray = new double[3];
        double[] nextArray = new double[3];
        boolean classification[] = new boolean[size];
        for (int i = 0; i < size; ++i) {
            int next = (i == size - 1) ? 0 : i + 1;
            classification[i] = classifyCornersIntoEdgeCrossingZPlane(corners[i], corners[next]);
        }
        return classification;
    }

    @Override
    public List<Point2D> getOutlineOnLocalizerForThisGeometry(Vector3d row, Vector3d column, Point3d tlhc, Tuple3d voxelSpacing, double sliceThickness, Tuple3d dimensions) {
        Point3d[] corners = getCornersOfSourceRectangleInSourceSpace(row, column, tlhc, voxelSpacing, dimensions);
        for (int i = 0; i < 4; ++i) {
            corners[i] = transformPointFromSourceSpaceIntoLocalizerSpace(corners[i]);
        }
        boolean edges[] = classifyCornersOfRectangleIntoEdgesCrossingZPlane(corners);
        Vector shapes = null;
        if (allTrue(edges)) {
            shapes = drawOutlineOnLocalizer(corners);
        } else if (oppositeEdges(edges)) {
            shapes = drawLinesBetweenAnyPointsWhichIntersectPlaneWhereZIsZero(corners);
        } else if (adjacentEdges(edges)) {
            shapes = drawLinesBetweenAnyPointsWhichIntersectPlaneWhereZIsZero(corners);
        } else {
        }
        if (shapes != null && shapes.size() > 0) {
            int size = shapes.size();
            List<Point2D> pts = new ArrayList<Point2D>(size);
            for (int i = 0; i < size; ++i) {
                Line2D.Double line = (Line2D.Double) shapes.get(i);
                pts.add(new Point2D.Double(line.getX2(), line.getY2()));
            }
            return pts;
        }
        return null;
    }
}
