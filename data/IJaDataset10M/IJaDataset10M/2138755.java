package syn3d.util;

/**
 * @author nicolas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AxisMaker {

    /**
	 * Creates lines and arrows to visualize the axis
	 * @return an array of line coordinates for the axis, 
	 * composed of float numbers in the order (l1p1, l1p2, l2p1, l2p2...)
	 * with l and p the line and points, p made of 3 floats x y z
	 */
    public static float[] makeAxis(int n) {
        switch(n) {
            case 0:
                return new float[] { 0, 0, 0, 1, 0, 0, 0.1f, 0.9f, 0, 0, 1, 0, -0.1f, 0.9f, 0, 0, 1, 0, 0.1f, 0, 0.9f, 0, 0, 1, -0.1f, 0, 0.9f, 0, 0, 1 };
            case 1:
                return new float[] { 0, 0, 0, 0, 1, 0, 0, 0.1f, 0.9f, 0, 0, 1, 0, -0.1f, 0.9f, 0, 0, 1, 0.9f, 0.1f, 0, 1, 0, 0, 0.9f, -0.1f, 0, 1, 0, 0 };
            case 2:
                return new float[] { 0, 0, 0, 0, 0, 1, 0, 0.9f, 0.1f, 0, 1, 0, 0, 0.9f, -0.1f, 0, 1, 0, 0.9f, 0, 0.1f, 1, 0, 0, 0.9f, 0, -0.1f, 1, 0, 0 };
        }
        return null;
    }
}
