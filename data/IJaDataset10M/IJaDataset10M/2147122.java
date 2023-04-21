package idraw;

import geometry.*;
import colors.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
 * Imperative Canvas - allows the drawing of shapes, lines, and text 
 * in the window of the given size, window closing and re-opening.
 * 
 * @author Viera K. Proulx
 * @since July 12 2007, August 2, 2007, April 14, 2008
 */
public class Canvas {

    /** records the number of canvases currently open */
    protected static int WINDOWS_OPEN = 0;

    /** the frame that holds the canvas */
    protected JFrame f;

    /** the panel that allows us to paint graphics */
    protected CanvasPanel panel;

    /** the width of the panel */
    protected int width;

    /** the height of the panel */
    protected int height;

    /**
   * <P>Construct a new frame with the 
   * <CODE>{@link CanvasPanel CanvasPanel} panel</CODE> as its component.<P>
   * 
   * @param width the width of the panel
   * @param height the height of the panel
   * @param title the title of the panel
   */
    public Canvas(int width, int height, String title) {
        this.width = width;
        this.height = height;
        f = new JFrame(title);
        f.setLayout(new BorderLayout());
        f.addWindowListener(winapt);
        f.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        panel = new CanvasPanel(width, height);
        panel.addNotify();
        f.getContentPane().add(panel, BorderLayout.CENTER);
        f.pack();
        Graphics g = panel.getGraphics();
        f.update(g);
        f.setVisible(false);
    }

    /**
   * A WindowAdapter that allows us to close a window and re-open, 
   * provided there is at least one open window. The program ends 
   * when all windows have been closed.
   */
    protected WindowAdapter winapt = new WindowAdapter() {

        public void windowClosing(WindowEvent e) {
            WINDOWS_OPEN = WINDOWS_OPEN - 1;
            panel.clearPanel();
            if (WINDOWS_OPEN == 0) System.exit(0);
        }
    };

    /**
   * Create a new canvas with the default title "Canvas"
   * 
   * @param width the width of the canvas
   * @param height the height of the canvas
   */
    public Canvas(int width, int height) {
        this(width, height, "Canvas");
    }

    /**
   * Draw a circle with the given <code>center</code>,  
   * <code>radius</code>, and <code>color</code>.
   * 
   * @param center the center
   * @param radius the radius of the disk
   * @param color the color of the circle
   */
    public void drawCircle(Posn center, int radius, Color color) {
        ((CanvasPanel) panel).drawCircle(center, radius, color);
    }

    public void drawCircle(Posn center, int radius, IColor color) {
        this.drawCircle(center, radius, color.thisColor());
    }

    /**
   * Draw a disk with the center at the given position with the given 
   * <code>radius</code> and <code>color</code>.
   * 
   * @param center the position of the center
   * @param radius the radius of the disk
   * @param color the color of the disk
   */
    public void drawDisk(Posn center, int radius, Color color) {
        ((CanvasPanel) panel).drawDisk(center, radius, color);
    }

    public void drawDisk(Posn center, int radius, IColor color) {
        this.drawDisk(center, radius, color.thisColor());
    }

    /**
   * Draw a rectangle with the NW corner at <code>nw</code> with the given 
   * <code>width</code>, <code>height</code>, and <code>color</code>.
   * 
   * @param nw the NW corner
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   * @param color the color of the rectangle
   */
    public void drawRect(Posn nw, int width, int height, Color color) {
        ((CanvasPanel) panel).drawRect(nw, width, height, color);
    }

    public void drawRect(Posn nw, int width, int height, IColor color) {
        this.drawRect(nw, width, height, color.thisColor());
    }

    /**
   * Draw a line from <code>p1</code> to  
   * <code>p2</code> in the given <code>color</code>.
   * 
   * @param p1 the position of the start of the line
   * @param p2 the position of the end of the line
   * @param color the color of the line
   */
    public void drawLine(Posn p1, Posn p2, Color color) {
        ((CanvasPanel) panel).drawLine(p1, p2, color);
    }

    public void drawLine(Posn p1, Posn p2, IColor color) {
        this.drawLine(p1, p2, color.thisColor());
    }

    /**
   * Draw a <code>String</code> with <code>p</code> 
   * identifying the bottom left position.
   * 
   * @param p the position of the bottom left of the <code>String</code>
   * @param s the <code>String</code> to display
   */
    public void drawString(Posn p, String s) {
        ((CanvasPanel) panel).drawString(p, s);
    }

    /**
   * Show the window with the canvas cleared
   */
    public void show() {
        if (!f.isVisible()) {
            WINDOWS_OPEN = WINDOWS_OPEN + 1;
            f.setVisible(true);
        } else {
        }
    }

    /**
   * Close the window - if it is currently open, do nothing otherwise
   */
    public void close() {
        if (f.isVisible()) {
            WINDOWS_OPEN = WINDOWS_OPEN - 1;
            f.setVisible(false);
            panel.clearPanel();
        }
    }

    /**
   * Clear the canvas before painting the next scene
   */
    protected void clear() {
        this.panel.clearPanel();
    }

    /**
   * Helper method to display a message and await RETURN before proceeding
   * 
   * @param message the message to display
   */
    private static void nextStep(String message) {
        try {
            System.out.println(message);
            System.out.println("Press RETURN");
            int n = System.in.read();
        } catch (IOException e) {
            System.out.println("Next step");
        }
    }

    /**
   * Produce a <code>String</code> representation of this color
   */
    public String toString() {
        return "new Canvas(" + this.width + ", " + this.height + ")";
    }

    /**
   * Self test for the Canvas class
   * 
   * @param argv
   */
    public static void main(String[] argv) {
        nextStep("Canvas with default name is constructed");
        Canvas sm1 = new Canvas(200, 200);
        nextStep("To show the canvas ... ");
        sm1.show();
        nextStep("Canvas shown - should be blank - add red and blue disk");
        sm1.drawCircle(new Posn(50, 50), 20, new Red());
        sm1.drawCircle(new Posn(150, 50), 50, new Blue());
        nextStep("Show the canvas again - it should not do anything");
        sm1.show();
        nextStep("Draw a green disk");
        sm1.drawCircle(new Posn(50, 150), 50, new Green());
        nextStep("Close the Canvas");
        sm1.close();
        nextStep("Show the canvas again - it should be cleared");
        sm1.show();
        nextStep("Paint one disks on the canvas");
        sm1.drawCircle(new Posn(50, 150), 25, new Black());
        nextStep("Construct a second canvas with the name Smiley");
        Canvas sm2 = new Canvas(200, 200, "Smiley");
        nextStep("Show the second canvas");
        sm2.show();
        nextStep("Paint two disks on the Smiley canvas");
        sm2.drawCircle(new Posn(50, 50), 20, new Red());
        sm2.drawCircle(new Posn(150, 150), 50, new Blue());
        nextStep("Manually close the 'Canvas' window\n" + "and see if we can bring it back to life");
        sm1.show();
        nextStep("The first canvas should be shown - cleared");
        sm1.drawCircle(new Posn(50, 50), 30, new Red());
        sm1.drawCircle(new Posn(150, 50), 30, new Blue());
        sm1.drawCircle(new Posn(50, 150), 30, new Green());
        nextStep("The first canvas has three disks drawn");
        System.out.println("Close both canvas windows to end the program");
    }
}
