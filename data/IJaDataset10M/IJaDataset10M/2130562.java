package org.eclipse.swt.internal.gtk;

public class GdkEventMotion extends GdkEvent {

    public int window;

    public byte send_event;

    public int time;

    public double x;

    public double y;

    public int axes;

    public int state;

    public short is_hint;

    public int device;

    public double x_root;

    public double y_root;

    public static final int sizeof = OS.GdkEventMotion_sizeof();
}
