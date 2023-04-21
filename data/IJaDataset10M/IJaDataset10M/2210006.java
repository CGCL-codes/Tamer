package uk.ac.liv.jt.types;

/**
 * 
 * 32 bit precision float coordinate, expressed as x,y,z
 * @author fabio
 *
 */
public class CoordF32 {

    public float x;

    public float y;

    public float z;

    public CoordF32(float x, float y, float z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "(X: " + x + " Y: " + y + " Z: " + z + ")";
    }

    public double[] getVectorDouble() {
        return new double[] { x, y, z };
    }
}
