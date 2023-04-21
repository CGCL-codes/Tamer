package net.sf.jdmf.data.output.clustering;

import java.util.Vector;

/**
 * Represents a point in a nD space. Additional data about the point can be 
 * included - when the clusters are formed, this additional data remains 
 * associated with the same point.
 * 
 * @author quorthon
 */
public class Point {

    private Vector<Double> coordinates = new Vector<Double>();

    private Object additionalData;

    public void addCoordinate(Double coordinate) {
        coordinates.add(coordinate);
    }

    /**
     * Retrieves the coordinate with the given number (valid numbers: [0..n-1]).
     */
    public Double retrieveCoordinate(int coordinateNumber) {
        return coordinates.get(coordinateNumber);
    }

    public int numberOfCoordinates() {
        return coordinates.size();
    }

    public Vector<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Vector<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Object getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Object additionalData) {
        this.additionalData = additionalData;
    }
}
