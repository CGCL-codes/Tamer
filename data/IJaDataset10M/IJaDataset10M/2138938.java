package org.jpox.samples.one_many.bidir_2;

/**
 * Detached house.
 * @version $Revision: 1.1 $
 */
public class Bungalow extends House {

    double floorArea;

    /**
     * Constructor.
     * @param number
     * @param street
     */
    public Bungalow(int number, String street) {
        super(number, street);
    }

    public void setFloorArea(double area) {
        this.floorArea = area;
    }

    public double getFloorArea() {
        return floorArea;
    }
}
