package net.sf.jrcss.core.event;

import java.awt.Point;

/**
 * @author damien.liottier@laposte.net
 * 
 */
public interface MouseEvent extends Event {

    public static String name = "mouseEvent";

    public static byte BT_LEFT = 1;

    public static byte BT_RIGHT = 2;

    public Point getMousePosition();

    public byte getButtonState();

    public boolean isLeftClick();

    public boolean isRightClick();
}
