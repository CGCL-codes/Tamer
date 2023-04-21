package com.gampire.pc.swing.cursor;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Window;

public class CursorUtil {

    public static void positionCursor(Window parentWindow, Point relativePosition) {
        try {
            Point parentWindowLocation = parentWindow.getLocation();
            Robot robot = new Robot();
            int x = parentWindowLocation.x + 4 + relativePosition.x;
            int y = parentWindowLocation.y + 22 + relativePosition.y;
            robot.mouseMove(x, y);
        } catch (AWTException e) {
        }
    }

    public static void positionCursor(Window parentWindow, Component component) {
        Point componentLocation = component.getLocation();
        Dimension componentSize = component.getSize();
        Point relativePosition = new Point(componentLocation.x + componentSize.width / 2, componentLocation.y + componentSize.height / 2);
        positionCursor(parentWindow, relativePosition);
    }
}
