package org.eclipse.swt.internal.gtk;

public class GdkEventScroll extends GdkEvent {

    public int window;

    public byte send_event;

    public int time;

    public double x;

    public double y;

    public int state;

    public int direction;

    public int device;

    public double x_root;

    public double y_root;

    public static final int sizeof = OS.GdkEventScroll_sizeof();
}
