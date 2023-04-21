package Race;

import java.lang.*;

public class TrackType {

    public String name = new String(" ");

    public String texture = new String(" ");

    public double xcollisionPlanes[] = new double[10];

    public int xcpfreepointer = 0;

    public double zcollisionPlanes[] = new double[10];

    public int zcpfreepointer = 0;

    public double collisionThreshold = .01;

    public int numBBoxes = 0;

    public double pxBBoxes[] = new double[50];

    public int pxBBfreepointer = 0;

    public double pzBBoxes[] = new double[50];

    public int pzBBfreepointer = 0;

    public double widthBBoxes[] = new double[50];

    public int wBBfreepointer = 0;

    public double lengthBBoxes[] = new double[50];

    public int lBBfreepointer = 0;
}
