package com.googlecode.grs.robot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import com.googlecode.grs.core.GRTObject;

/**
 * The SimplePather collects path data from the robot and draws it.
 * 
 * @author ajc
 * 
 */
public class SimplePather extends GRTObject implements DrawnRobotComponent {

    public static final int TIMESTEP = 150;

    private final Moveable robot;

    private ArrayList<Point> path = new ArrayList<Point>();

    private boolean running;

    /**
	 * A SimplePather is constructed from a robot it uses to poll data from
	 * 
	 * @param robot
	 */
    public SimplePather(Moveable robot) {
        this.robot = robot;
    }

    /**
	 * Collects path data on a slow clock
	 */
    public void run() {
        running = true;
        while (running) {
            path.add(new Point((int) robot.getX(), (int) robot.getY()));
            try {
                sleep(TIMESTEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 1; i < path.size() - 1; i++) {
            if (path.size() > 1) {
                g.drawLine((int) path.get(i - 1).getX(), (int) path.get(i - 1).getY(), (int) path.get(i).getX(), (int) path.get(i).getY());
            }
        }
    }
}
