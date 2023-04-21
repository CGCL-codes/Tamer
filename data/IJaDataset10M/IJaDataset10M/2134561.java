package jat.traj;

import java.io.*;

/**
* <P>
* The CentralBody.java Class provides the means for specifying the 
* central body used in creating a trajectory.
*
* @author 
* @version 1.0
*/
public final class CentralBody implements Serializable {

    private String name;

    private CentralBody(String nm) {
        name = nm;
    }

    public String toString() {
        return name;
    }

    public static final CentralBody EARTH = new CentralBody("Earth"), SUN = new CentralBody("Sun"), MERCURY = new CentralBody("Mercury"), VENUS = new CentralBody("Venus"), MARS = new CentralBody("Mars"), JUPITER = new CentralBody("Jupiter"), SATURN = new CentralBody("Saturn"), URANUS = new CentralBody("Uranus"), NEPTUNE = new CentralBody("Neptune"), PLUTO = new CentralBody("Pluto"), MOON = new CentralBody("Moon"), OTHER = new CentralBody("Other");

    public static final CentralBody[] index = { EARTH, SUN, MERCURY, VENUS, MARS, JUPITER, SATURN, URANUS, NEPTUNE, PLUTO, MOON, OTHER };

    public static void main(String[] args) {
        CentralBody m = CentralBody.EARTH;
        System.out.println(m);
        m = CentralBody.index[4];
        System.out.println(m);
        System.out.println(m == CentralBody.MARS);
        System.out.println(m.equals(CentralBody.JUPITER));
    }
}
