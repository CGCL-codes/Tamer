package com.mobiwebinc.compconn.communication;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author suraj
 */
public class SystemController {

    Robot robot;

    static SystemController[] systems;

    private SystemController(GraphicsDevice gd) {
        try {
            robot = new Robot(gd);
        } catch (AWTException ex) {
            Logger.getLogger(SystemController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SystemController[] getInstances() {
        if (systems == null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] gds = ge.getScreenDevices();
            systems = new SystemController[gds.length];
            for (int i = 0; i < gds.length; i++) {
                systems[i] = new SystemController(gds[i]);
            }
        }
        return systems;
    }

    public static SystemController getInstance() {
        return getInstances()[0];
    }

    public void mouseMove(int x, int y) {
        robot.mouseMove(x, y);
    }

    public void mousePress(int button) {
        robot.mousePress(button);
    }

    public void mouseWheel(int i) {
        robot.mouseWheel(i);
    }

    public void mouseRelease(int button) {
        robot.mouseRelease(button);
    }

    public void keyPress(int code) {
        robot.keyPress(code);
    }

    public void keyRelease(int code) {
        robot.keyRelease(code);
    }

    public BufferedImage createScreenCapture(Rectangle rect) {
        return robot.createScreenCapture(rect);
    }

    public int getMonitorCount() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return ge.getScreenDevices().length;
    }
}
