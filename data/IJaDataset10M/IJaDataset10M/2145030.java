package org.gvsig.gpe.containers;

import java.io.IOException;
import org.gvsig.gpe.parser.ICoordinateIterator;
import org.gvsig.gpe.writer.ICoordinateSequence;

/**
 * @author Jorge Piera Llodr� (jorge.piera@iver.es)
 */
public class CoordinatesSequence implements ICoordinateSequence, ICoordinateIterator {

    private double[][] coordinates;

    private int next = 0;

    public CoordinatesSequence(double x, double y, double z) {
        coordinates = new double[3][1];
        coordinates[0][0] = x;
        coordinates[1][0] = y;
        coordinates[2][0] = z;
    }

    public CoordinatesSequence(double[] x, double[] y, double[] z) {
        coordinates = new double[3][1];
        coordinates[0] = x;
        coordinates[1] = y;
        coordinates[2] = z;
    }

    public int getDimension() {
        return coordinates.length;
    }

    public boolean hasNext() throws IOException {
        return (next < coordinates[0].length);
    }

    public void next(double[] buffer) throws IOException {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = coordinates[i][next];
        }
        next++;
    }

    public int getSize() {
        return coordinates[0].length;
    }

    public ICoordinateIterator iterator() {
        return this;
    }
}
