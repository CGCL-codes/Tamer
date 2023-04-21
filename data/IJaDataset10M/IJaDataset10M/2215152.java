package Terrain5;

import java.io.*;
import java.awt.*;
import java.math.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.vecmath.*;
import javax.media.j3d.*;
import java.util.*;
import javax.imageio.*;
import com.sun.image.codec.jpeg.*;
import General.*;

/** 
 * TextFrontEnd is the main class that implements a text-based front end for interacting with a {@link World} geological/tectonic simulation.
 * @author Tom Groves
 */
public class TextFrontEnd implements Runnable {

    private World world = null;

    private double rotation1 = 0, rotation2 = 0;

    private double zoom = 6600;

    private boolean running = false, paintGuard = false, painting = false;

    private int steps = 1;

    private PointViewer pointViewer = null;

    private ColorMap temperatureColorMap;

    /**
   * The <code>main</code> method is the entry-point for calls from the command line.
   * Sets up a new FrontEnd and starts the GUI.
   * @param arg	the array of command line arguments
   */
    public static void main(String[] arg) {
        try {
            TextFrontEnd tfe = new TextFrontEnd();
            if (arg.length == 1) tfe.init(arg[0]); else if (arg.length == 2) tfe.init(arg[0], arg[1]); else System.out.println("Usage: java TextFrontEnd <timesteps> [filename]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(String s) {
        newWorld();
        steps = Integer.parseInt(s);
        new Thread(this).start();
    }

    public void init(String s, String f) {
        loadWorld(f);
        steps = Integer.parseInt(s);
        new Thread(this).start();
    }

    public void run() {
        running = true;
        System.out.print("Timestepping...");
        long time = System.currentTimeMillis();
        for (int i = 0; i < steps; i++) {
            timeStep();
            Thread.yield();
            System.out.println("*** Finished step " + (i + 1) + " of " + (steps) + " ***");
            double average = (System.currentTimeMillis() - time) / (1000.0f * (i + 1));
            double remaining = average * (steps - i - 1);
            System.out.println("*** Estimated time remaining: " + (remaining / 60f) + " minutes (" + ((int) remaining) + " seconds)\n");
            if (Tet.goneBad) steps = 1;
        }
        System.out.println("done, in " + (System.currentTimeMillis() - time) / 60000.0f + " minutes! (" + (System.currentTimeMillis() - time) / 1000.0f + " seconds)");
        running = false;
        Tet.goneBad = false;
    }

    private void initGUI() {
        temperatureColorMap = new ColorMap();
        temperatureColorMap.add(-30, new Color(0, 0, 100));
        temperatureColorMap.add(-5, Color.blue);
        temperatureColorMap.add(10, Color.green);
        temperatureColorMap.add(20, Color.yellow);
        temperatureColorMap.add(35, Color.red);
    }

    public double radiusSq(double x1, double y1, double x2, double y2) {
        return Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
    }

    public void newWorld() {
        world = new World();
        for (int i = 0; i < world.getNumPoints(); i++) world.getPoint(i).setColor(world.getColorMap().map(world.getPoint(i).getSurfaceHeight()));
    }

    public void loadWorld(String filename) {
        try {
            File file = new File(filename);
            world = new World(file.getCanonicalPath());
        } catch (Exception e) {
            System.err.println("Error loading world: " + filename);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void timeStep() {
        world.timeStep();
        for (int i = 0; i < world.getNumPoints(); i++) world.getPoint(i).setColor(world.getColorMap().map(world.getPoint(i).getSurfaceHeight()));
        if (world.getEpoch() % 20 == 0) world.saveJPGsequence();
        if (world.getEpoch() % 20 == 0) world.save();
    }
}
