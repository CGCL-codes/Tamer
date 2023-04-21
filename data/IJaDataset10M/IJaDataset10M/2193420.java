package edu.umd.cs.mouseprecision;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolox.nodes.PLine;

/**
 * The canvas of the application.
 * </br></br>
 * Implements all view and control for performing one task:
 * </br>- each task starts with clicking one the START area
 * </br>- following the expected mouse path
 * </br>- entering the FINISH area
 * </br>- saving captured data
 * 
 */
public class TaskCanvas extends PCanvas {

    /**
	 * Parent window (application window)
	 */
    private MainJFrame parentFrame;

    /**
	 * The actually traveled mouse path:
	 * </br>- MAX_POINTS: number of maximum points saved
	 * </br>- each point contains: x-position, y-position 
	 *   and flag if mouse was inside or outside path (0.0 or 1.0)
	 * </br>- counter for the next point
	 */
    private static int MAX_POINTS = 50000;

    private double[][] gonePathPoints = new double[MAX_POINTS][3];

    private int gonePathNextPoint = 0;

    /**
	 * Time measurements
	 */
    long startTime;

    long finishTime;

    /**
	 * Task types enumeration:
	 * TRAIN: training task
	 * SQUARE: only horizontal and vertical bars
	 * DIAGONAL: diagonal bars
	 * ROUND: round path
	 */
    public enum TaskType {

        TRAIN, SQUARE, DIAGONAL, ROUND
    }

    ;

    private TaskType taskType;

    /**
	 * internal flag: has user started task yet (= clicked on start area)
	 */
    private boolean taskStarted = false;

    /**
	 * internal flag: has user finished task yet (= entered finish area)
	 */
    private boolean taskFinished = false;

    /**
	 * internal flag: is mouse inside path a.t.m.
	 */
    private boolean mouseInsidePath = false;

    /**
	 * Expected mouse path.
	 */
    private MousePath expectedMousePath;

    /**
	 * Start area component.
	 */
    private StartArea startArea;

    /**
	 * Finish are component.
	 */
    FinishArea finishArea;

    /**
	 * Constructor
	 * @param parentFrame
	 */
    public TaskCanvas(MainJFrame parentFrame, TaskType taskType) {
        this.parentFrame = parentFrame;
        this.taskType = taskType;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        this.setDefaultRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
        expectedMousePath = new MousePath(this, taskType);
        this.getLayer().addChild(expectedMousePath);
        startArea = new StartArea(this);
        this.getLayer().addChild(startArea);
        startArea.animateToPositionScaleRotation(expectedMousePath.getPositionForStartArea()[0] * Main.SCALE, expectedMousePath.getPositionForStartArea()[1] * Main.SCALE, Main.SCALE, 0.0, 0L);
        finishArea = new FinishArea(this);
        this.getLayer().addChild(finishArea);
        finishArea.animateToPositionScaleRotation(expectedMousePath.getPositionForFinishArea()[0] * Main.SCALE, expectedMousePath.getPositionForFinishArea()[1] * Main.SCALE, Main.SCALE, 0.0, 0L);
        this.addInputEventListener(new PBasicInputEventHandler() {

            public void mouseMoved(PInputEvent event) {
                if (taskStarted && !taskFinished) {
                    gonePathPoints[gonePathNextPoint][0] = event.getPosition().getX();
                    gonePathPoints[gonePathNextPoint][1] = event.getPosition().getY();
                    if (mouseInsidePath) gonePathPoints[gonePathNextPoint][2] = 1.0; else gonePathPoints[gonePathNextPoint][2] = 0.0;
                    gonePathNextPoint++;
                }
            }

            public void mouseDragged(PInputEvent event) {
                if (taskStarted && !taskFinished) {
                    gonePathPoints[gonePathNextPoint][0] = event.getPosition().getX();
                    gonePathPoints[gonePathNextPoint][1] = event.getPosition().getY();
                    if (mouseInsidePath) gonePathPoints[gonePathNextPoint][2] = 1.0; else gonePathPoints[gonePathNextPoint][2] = 0.0;
                    gonePathNextPoint++;
                }
            }
        });
    }

    public void setMouseInside(boolean inside) {
        if (taskStarted && !taskFinished) {
            mouseInsidePath = inside;
        }
    }

    private void saveScreenAsImage() {
        BufferedImage screencapture;
        try {
            screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            File file = new File("screencapture.gif");
            ImageIO.write(screencapture, "gif", file);
        } catch (HeadlessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Called by the start button when the task is started.
	 */
    public void taskStarted() {
        taskStarted = true;
        finishArea.enable();
        startTime = System.currentTimeMillis();
    }

    /**
	 * Called by the finish button when the task is complete.
	 */
    public void taskFinished() {
        taskFinished = true;
        finishTime = System.currentTimeMillis();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        saveDataToFile();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        parentFrame.taskFinished();
    }

    /**
	 * Writes captured data to file.
	 * 
	 * Filename: [timestamp].data
	 * Filecontent:
	 * 
	 * Time: [measured time in seconds]
	 * Mousepath:
	 * [point id] [x coordinate] [y coordinate] [1.0/0.0 = inside/outside]
	 * ...
	 *
	 */
    private void saveDataToFile() {
        DataFile f = new DataFile((((double) finishTime - startTime)) / 1000, this.taskType, gonePathPoints, gonePathNextPoint);
        f.writeToDisk();
    }

    public void makeTraveledPathVisible() {
        PLine traveledPath = new PLine();
        float[] hsbColor = Color.RGBtoHSB(255, 0, 0, null);
        traveledPath.setStrokePaint(Color.getHSBColor(hsbColor[0], hsbColor[1], hsbColor[2]));
        int resolution = 1;
        for (int i = 0; i < gonePathNextPoint; i = i + resolution) {
            traveledPath.addPoint(i / resolution, gonePathPoints[i][0], gonePathPoints[i][1]);
        }
        this.getLayer().addChild(traveledPath);
    }

    public void makePathViolationsVisible() {
        float[] hsbColor = Color.RGBtoHSB(0, 255, 0, null);
        for (int i = 0; i < gonePathNextPoint; i++) {
            if (i > 0 && gonePathPoints[i - 1][2] == 1.0 && gonePathPoints[i][2] == 0.0) {
                PPath circle = PPath.createEllipse(0, 0, 6, 6);
                circle.setStrokePaint(Color.getHSBColor(hsbColor[0], hsbColor[1], hsbColor[2]));
                this.getLayer().addChild(circle);
                circle.translate(gonePathPoints[i][0] - 3, gonePathPoints[i][1] - 3);
            }
        }
    }

    public void setGonePathPoints(double[][] gonePathPoints) {
        this.gonePathPoints = gonePathPoints;
    }

    public void setGonePathNextPoint(int gonePathNextPoint) {
        this.gonePathNextPoint = gonePathNextPoint;
    }
}
