package com.mam.gui;

import java.io.Serializable;
import java.util.*;

/**
 * The basic field for simulations. Sets up a layered pane with a
 * specified total area and field size (in millimeters). It uses
 * those specifications and a millimeter/pixel setting to calculate
 * the display. Note that the x direction is always referred to as
 * width and that y is always length to be consistent with the
 * BodyGuiBasic (which has the potential to be 3D, so height is
 * reserved for the z axis). Also, the fields are integers; I
 * didn't think that the granularity should be less than a
 * millimeter. Also, we can make our total area upwards of 2
 * million meters before hitting the maximum integer value.
 * 
 * The default is to create a 6mx6m area, which has a 5mx5m green
 * field (which means the black border is .5m on each side) and
 * white border lines that are .1m wide. Default mm/pixel setting
 * is 10, which results in a 600x600 pixel display. */
public class SimFieldStateBasic extends Observable implements Serializable {

    protected double totalWid;

    protected double totalLen;

    protected double fieldWid;

    protected double fieldLen;

    protected double borderWid;

    protected SimFieldGuiBasic fieldGui;

    protected static final double DEF_FD_X = 5000.0;

    protected static final double DEF_FD_Y = 6000.0;

    protected static final double DEF_BORDER = 500.0;

    private static String prg = "SimFieldStateBasic";

    private boolean debug = true;

    /**
	 * Constructor that uses all defaults */
    public SimFieldStateBasic() {
        this(DEF_FD_X, DEF_FD_Y, DEF_BORDER);
    }

    /**
	 * Constructor that takes field size settings */
    public SimFieldStateBasic(double fw, double fl) {
        this(fw, fl, 0);
    }

    /**
	 * Constructor that takes field and border size settings */
    public SimFieldStateBasic(double fw, double fl, double bw) {
        debugPrint(prg + ": in Constructor(" + fw + "," + fl + "," + bw + ")");
        fieldWid = fw;
        fieldLen = fl;
        borderWid = bw;
        totalWid = fieldWid + (2 * borderWid);
        totalLen = fieldLen + (2 * borderWid);
        fieldGui = null;
    }

    public double getFieldX() {
        return fieldWid;
    }

    public double getFieldY() {
        return fieldLen;
    }

    public double getTotalX() {
        return totalWid;
    }

    public double getTotalY() {
        return totalLen;
    }

    public double getBorderX() {
        return borderWid;
    }

    public double getBorderY() {
        return borderWid;
    }

    public SimFieldGuiBasic getFieldGui() {
        return fieldGui;
    }

    public int getMMPerPixel() {
        if (fieldGui != null) {
            return fieldGui.getMMPerPixel();
        }
        return -1;
    }

    public void createFieldGui() {
        SimFieldGuiBasic sfgb = new SimFieldGuiBasic(this);
        sfgb.addBackgroundComponents();
        setFieldGui(sfgb);
    }

    public void createFieldGui(int mmpp) {
        SimFieldGuiBasic sfgb = new SimFieldGuiBasic(this, mmpp);
        sfgb.addBackgroundComponents();
        setFieldGui(sfgb);
    }

    public void setFieldGui(SimFieldGuiBasic sfgb) {
        fieldGui = sfgb;
        addObserver(fieldGui);
    }

    public boolean outOfBounds(double x, double y) {
        return (x < 0 || x > fieldWid || y < 0 || y > fieldLen);
    }

    public void debugPrint(String str) {
        if (debug) {
            System.out.println(str);
            System.out.flush();
        }
    }
}
