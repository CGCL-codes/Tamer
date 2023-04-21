package utils.linear.grid;

import utils.linear.LVect3d;

/**
 *
 * @author Calvin Ashmore
 */
public class Buffer_v3 extends Buffer<LVect3d> {

    /** Creates a new instance of Buffer_d */
    public Buffer_v3(int xres, int yres) {
        super(xres, yres, 3);
    }

    public LVect3d getValue(int x, int y) {
        return new LVect3d(data[3 * (x + xres * y)], data[3 * (x + xres * y) + 1], data[3 * (x + xres * y) + 2]);
    }

    public void setValue(int x, int y, LVect3d value) {
        data[3 * (x + xres * y) + 0] = value.x;
        data[3 * (x + xres * y) + 1] = value.y;
        data[3 * (x + xres * y) + 2] = value.z;
    }
}
