package com.mam.gui;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Random;
import java.util.Vector;

/**
 * Contains the minimum information for a simulated robot. This
 * includes the location on the field, the orientation, the
 * unique ID, a timestamp, whether the agent is "selected" or
 * not, the body width and length, the mass, and a vector of
 * devices. Note that the x direction is always referred to
 * as width; y is always length.
 *
 * Subclasses Observable in case a GUI is used; when the robot's
 * state changes, updating the GUI can be automatic.
 *
 * @author Jim Kramer
 */
public class BodyStateBasic extends Observable implements Serializable {

    public static final double twoPI = 2 * Math.PI;

    protected Integer myBodyID;

    protected String myBodyName;

    protected double xPos;

    protected double yPos;

    protected double zPos;

    protected double orientation;

    protected boolean selected;

    protected long simTimeStamp;

    protected long timeStamp;

    protected double bodyWidth;

    protected double bodyLength;

    protected double bodyHeight;

    protected double mass;

    protected BodyGuiBasic bodyGui;

    public static final double DEF_BODYLENGTH = 220.0;

    public static final double DEF_BODYWIDTH = 220.0;

    public static final double DEF_BODYHEIGHT = 100.0;

    public static final double DEF_MASS = 1000.0;

    private String prg = "BodyStateBasic";

    private boolean debug = false;

    public BodyStateBasic(Integer id, String name) {
        this(id, name, DEF_BODYLENGTH, DEF_BODYWIDTH, DEF_BODYHEIGHT, DEF_MASS);
    }

    public BodyStateBasic(Integer id, String name, double bl, double bw, double bh, double m) {
        myBodyID = id;
        myBodyName = name;
        bodyLength = bl;
        bodyWidth = bw;
        bodyHeight = bh;
        mass = m;
        bodyGui = null;
        xPos = 0.0;
        yPos = 0.0;
        zPos = 0.0;
        orientation = 90.0;
        selected = false;
        simTimeStamp = 0;
        timeStamp = System.currentTimeMillis();
    }

    public Integer getID() {
        return myBodyID;
    }

    public String getName() {
        return myBodyName;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public double getZPos() {
        return zPos;
    }

    public double getOrientation() {
        return orientation;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean getSelected() {
        return selected;
    }

    public double getBodyLength() {
        return bodyLength;
    }

    public double getBodyWidth() {
        return bodyWidth;
    }

    public double getBodyHeight() {
        return bodyHeight;
    }

    public double getMass() {
        return mass;
    }

    public BodyGuiBasic getBodyGui() {
        return bodyGui;
    }

    public void createBodyGui(int mmpp) {
        BodyGuiBasic bgb = new BodyGuiBasic(this, mmpp);
        setBodyGui(bgb);
    }

    public void createBodyGui(int mmpp, int nl) {
        BodyGuiBasic bgb = new BodyGuiBasic(this, mmpp, nl);
        setBodyGui(bgb);
    }

    public void setBodyGui(BodyGuiBasic bgb) {
        bodyGui = bgb;
        addObserver(bodyGui);
    }

    public void setTimeStamps(long st) {
        simTimeStamp = st;
        timeStamp = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        long returnVal = System.currentTimeMillis() - timeStamp;
        timeStamp = System.currentTimeMillis();
        return (returnVal);
    }

    public void setLocation(double[] pos) {
        boolean need = false;
        if ((int) xPos != (int) pos[0]) {
            xPos = pos[0];
            need = true;
        }
        if ((int) yPos != (int) pos[1]) {
            yPos = pos[1];
            need = true;
        }
        if ((int) orientation != (int) pos[2]) {
            orientation = pos[2];
            need = true;
        }
        if (need) updateState("xyPos");
    }

    public void setX(double x) {
        xPos = x;
        updateState("xPos");
    }

    public void setY(double y) {
        yPos = y;
        updateState("yPos");
    }

    public void setZ(double z) {
        zPos = z;
        updateState("zPos");
    }

    public void setXY(double x, double y) {
        xPos = x;
        yPos = y;
        updateState("xyPos");
    }

    public void setXZ(double x, double z) {
        xPos = x;
        zPos = z;
        updateState("xzPos");
    }

    public void setYZ(double y, double z) {
        yPos = y;
        zPos = z;
        updateState("yzPos");
    }

    public void setXYZ(double x, double y, double z) {
        xPos = x;
        yPos = y;
        zPos = z;
        updateState("xyzPos");
    }

    /**
	 * Places the agent in a random location within some bounds
	 * @param xMax the maximum width of the field
	 * @param yMax the maximum length of the field 
	 * @param zMax the maximum height of the field */
    public void setXYZRandom(double xMax, double yMax, double zMax) {
        Random rnum = new Random(System.currentTimeMillis());
        xPos = rnum.nextDouble() * xMax;
        yPos = rnum.nextDouble() * yMax;
        zPos = rnum.nextDouble() * zMax;
        updateState("xyzPos");
    }

    public void setOrientationRad(double o) {
        orientation = (o < 0) ? (twoPI + o) % twoPI : o % twoPI;
        updateState("orientation");
    }

    public void setOrientationDeg(int o) {
        setOrientationRad(Math.toRadians(o));
        updateState("orientation");
    }

    /**
	 * Makes the agent's orientation random */
    public void setOrientationRandom() {
        Random rnum = new Random(System.currentTimeMillis());
        orientation = rnum.nextDouble() * twoPI;
        updateState("orientation");
    }

    public void turnBody(double rad) {
        setOrientationRad(orientation + rad);
    }

    public void turnBody(int deg) {
        setOrientationRad(orientation + Math.toRadians(deg));
    }

    public void setSelected(boolean sel) {
        selected = sel;
        updateState("selected");
    }

    public void toggleSelected() {
        selected = !selected;
        updateState("selected");
    }

    protected void updateState(String field) {
        setChanged();
        notifyObservers(field);
    }

    private void debugPrint(String str) {
        if (debug) {
            System.err.println(str);
            System.err.flush();
        }
    }
}
