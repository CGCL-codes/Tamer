package jat.forces;

/**
 * @author
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GravityModelType {

    private String name;

    private GravityModelType(String nm) {
        name = nm;
    }

    public String toString() {
        return name;
    }

    public static final GravityModelType EGM96 = new GravityModelType("EGM96"), GEMT1 = new GravityModelType("GEMT1"), JGM2 = new GravityModelType("JGM2"), JGM3 = new GravityModelType("JGM3"), WGS84 = new GravityModelType("WGS84"), WGS84_EGM96 = new GravityModelType("WGS84_EGM96");

    public static final GravityModelType[] index = { EGM96, GEMT1, JGM2, JGM3, WGS84, WGS84_EGM96 };

    public static final int num_models = 6;
}
