package org.geotools.shapefile;

import java.io.IOException;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jump.io.EndianDataInputStream;
import com.vividsolutions.jump.io.EndianDataOutputStream;

/**
 * Wrapper for a Shapefile arc.
 */
public class MultiLineHandler implements ShapeHandler {

    int myShapeType = -1;

    public MultiLineHandler() {
        myShapeType = 3;
    }

    public MultiLineHandler(int type) throws InvalidShapefileException {
        if ((type != 3) && (type != 13) && (type != 23)) throw new InvalidShapefileException("MultiLineHandler constructor - expected type to be 3,13 or 23");
        myShapeType = type;
    }

    public Geometry read(EndianDataInputStream file, GeometryFactory geometryFactory, int contentLength) throws IOException, InvalidShapefileException {
        double junk;
        int actualReadWords = 0;
        int shapeType = file.readIntLE();
        actualReadWords += 2;
        if (shapeType == 0) {
            return new MultiLineString(null, new PrecisionModel(), 0);
        }
        if (shapeType != myShapeType) {
            throw new InvalidShapefileException("MultilineHandler.read()  - file says its type " + shapeType + " but i'm expecting type " + myShapeType);
        }
        junk = file.readDoubleLE();
        junk = file.readDoubleLE();
        junk = file.readDoubleLE();
        junk = file.readDoubleLE();
        actualReadWords += 4 * 4;
        int numParts = file.readIntLE();
        int numPoints = file.readIntLE();
        actualReadWords += 4;
        int[] partOffsets = new int[numParts];
        for (int i = 0; i < numParts; i++) {
            partOffsets[i] = file.readIntLE();
            actualReadWords += 2;
        }
        LineString lines[] = new LineString[numParts];
        Coordinate[] coords = new Coordinate[numPoints];
        for (int t = 0; t < numPoints; t++) {
            coords[t] = new Coordinate(file.readDoubleLE(), file.readDoubleLE());
            actualReadWords += 8;
        }
        if (myShapeType == 13) {
            junk = file.readDoubleLE();
            junk = file.readDoubleLE();
            actualReadWords += 8;
            for (int t = 0; t < numPoints; t++) {
                coords[t].z = file.readDoubleLE();
                actualReadWords += 4;
            }
        }
        if (myShapeType >= 13) {
            int fullLength;
            if (myShapeType == 13) {
                fullLength = 22 + 2 * numParts + (numPoints * 8) + 4 + 4 + 4 * numPoints + 4 + 4 + 4 * numPoints;
            } else {
                fullLength = 22 + 2 * numParts + (numPoints * 8) + 4 + 4 + 4 * numPoints;
            }
            if (contentLength >= fullLength) {
                junk = file.readDoubleLE();
                junk = file.readDoubleLE();
                actualReadWords += 8;
                for (int t = 0; t < numPoints; t++) {
                    junk = file.readDoubleLE();
                    actualReadWords += 4;
                }
            }
        }
        while (actualReadWords < contentLength) {
            int junk2 = file.readShortBE();
            actualReadWords += 1;
        }
        int offset = 0;
        int start, finish, length;
        for (int part = 0; part < numParts; part++) {
            start = partOffsets[part];
            if (part == numParts - 1) {
                finish = numPoints;
            } else {
                finish = partOffsets[part + 1];
            }
            length = finish - start;
            Coordinate points[] = new Coordinate[length];
            for (int i = 0; i < length; i++) {
                points[i] = coords[offset];
                offset++;
            }
            lines[part] = geometryFactory.createLineString(points);
        }
        if (numParts == 1) return lines[0]; else return geometryFactory.createMultiLineString(lines);
    }

    public void write(Geometry geometry, EndianDataOutputStream file) throws IOException {
        MultiLineString multi = (MultiLineString) geometry;
        int npoints;
        Coordinate[] coords;
        file.writeIntLE(getShapeType());
        Envelope box = multi.getEnvelopeInternal();
        file.writeDoubleLE(box.getMinX());
        file.writeDoubleLE(box.getMinY());
        file.writeDoubleLE(box.getMaxX());
        file.writeDoubleLE(box.getMaxY());
        int numParts = multi.getNumGeometries();
        file.writeIntLE(numParts);
        npoints = multi.getNumPoints();
        file.writeIntLE(npoints);
        LineString[] lines = new LineString[numParts];
        int idx = 0;
        for (int i = 0; i < numParts; i++) {
            lines[i] = (LineString) multi.getGeometryN(i);
            file.writeIntLE(idx);
            idx = idx + lines[i].getNumPoints();
        }
        coords = multi.getCoordinates();
        for (int t = 0; t < npoints; t++) {
            file.writeDoubleLE(coords[t].x);
            file.writeDoubleLE(coords[t].y);
        }
        if (myShapeType == 13) {
            double[] zExtreame = zMinMax(multi);
            if (Double.isNaN(zExtreame[0])) {
                file.writeDoubleLE(0.0);
                file.writeDoubleLE(0.0);
            } else {
                file.writeDoubleLE(zExtreame[0]);
                file.writeDoubleLE(zExtreame[1]);
            }
            for (int t = 0; t < npoints; t++) {
                double z = coords[t].z;
                if (Double.isNaN(z)) file.writeDoubleLE(0.0); else file.writeDoubleLE(z);
            }
        }
        if (myShapeType >= 13) {
            file.writeDoubleLE(-10E40);
            file.writeDoubleLE(-10E40);
            for (int t = 0; t < npoints; t++) {
                file.writeDoubleLE(-10E40);
            }
        }
    }

    /**
     * Get the type of shape stored (Shapefile.ARC)
     */
    public int getShapeType() {
        return myShapeType;
    }

    public int getLength(Geometry geometry) {
        MultiLineString multi = (MultiLineString) geometry;
        int numlines, numpoints;
        numlines = multi.getNumGeometries();
        numpoints = multi.getNumPoints();
        if (myShapeType == 3) {
            return 22 + 2 * numlines + (numpoints * 8);
        }
        if (myShapeType == 23) {
            return 22 + 2 * numlines + (numpoints * 8) + 4 + 4 + 4 * numpoints;
        }
        return 22 + 2 * numlines + (numpoints * 8) + 4 + 4 + 4 * numpoints + 4 + 4 + 4 * numpoints;
    }

    double[] zMinMax(Geometry g) {
        double zmin, zmax;
        boolean validZFound = false;
        Coordinate[] cs = g.getCoordinates();
        double[] result = new double[2];
        zmin = Double.NaN;
        zmax = Double.NaN;
        double z;
        for (int t = 0; t < cs.length; t++) {
            z = cs[t].z;
            if (!(Double.isNaN(z))) {
                if (validZFound) {
                    if (z < zmin) zmin = z;
                    if (z > zmax) zmax = z;
                } else {
                    validZFound = true;
                    zmin = z;
                    zmax = z;
                }
            }
        }
        result[0] = (zmin);
        result[1] = (zmax);
        return result;
    }
}
