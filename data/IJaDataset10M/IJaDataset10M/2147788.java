package uchicago.src.sim.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.util.ArrayList;

/**
 * Abstract class for local and remote painting objects that do the actual
 * work of getting Displayables to paint themselves. Used by DisplaySurface.
 *
 * @author Nick Collier
 * @version $Revision: 1.9 $ $Date: 2004/11/03 19:50:59 $
 * @see LocalPainter
 */
public abstract class Painter {

    protected ArrayList orderedDisplayables = new ArrayList();

    protected DisplaySurface surface;

    protected BufferedImage buffImage = null;

    protected Graphics2D g2 = null;

    protected int width, height;

    protected float origWidth, origHeight;

    protected ArrayList displayables = new ArrayList();

    protected SimGraphics simGraphics = new SimGraphics();

    protected Toolkit toolkit;

    protected Color bgColor = Color.black;

    /**
   * Creates a Painter. Before this Painter can be used
   * its init method must be called. This will done automatically by
   * the DisplaySurface if this Painter is an argument to a DisplaySurface's
   * constructor.
   */
    public Painter() {
    }

    /**
   * Creates a Painter using the specified displaySurface, and with
   * the specified width and height.
   *
   * @param s the displaySurface associated with this Painter
   * @param w the width of the painter
   * @param h the height of the painter
   */
    public Painter(DisplaySurface s, int w, int h) {
        surface = s;
        origWidth = width = w;
        origHeight = height = h;
        toolkit = s.getToolkit();
    }

    /**
   * Associates this Painter with the specified DisplaySurface and initializes
   * the width and height. This can be used in conjunction with the no-arg
   * constructor to complete the construction of a Painter in situations
   * when creating a DisplaySurface that takes a Painter as part of its
   * construction. In these cases, the DisplaySurface will call this init
   * method.
   *
   * @param s the displaySurface associated with this Painter
   * @param w the width of the painter
   * @param h the height of the painter
   */
    public void init(DisplaySurface s, int w, int h) {
        surface = s;
        toolkit = s.getToolkit();
        origWidth = width = w;
        origHeight = height = h;
    }

    /**
   * Creates a BufferedImage for use by sub class painters.
   */
    protected void createBufferedImage() {
        buffImage = surface.getGraphicsConfiguration().createCompatibleImage(width, height);
    }

    /**
   * Resizes the buffered image on which all drawing is done to the specified
   * newWidth and height
   *
   * @param newWidth the newWidth of the new buffered image
   * @param newHeight the height of the new buffered image
   */
    public void reSize(int newWidth, int newHeight) {
        float wScale = newWidth / origWidth;
        float hScale = newHeight / origHeight;
        this.width = newWidth;
        this.height = newHeight;
        if (buffImage != null) {
            buffImage.flush();
            buffImage = null;
            buffImage = surface.getGraphicsConfiguration().createCompatibleImage(newWidth, newHeight);
            simGraphics.setYScale(hScale);
            simGraphics.setXScale(wScale);
        }
    }

    /**
   * Creates and sets the drawing context.
   */
    protected void createGraphics2D() {
        g2 = buffImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    }

    /**
   * Adds a Displayable to the list of displayables to be displayed.
   *
   * @param d the displayable to add
   */
    public void addDisplayable(Displayable d) {
        displayables.add(d);
    }

    /**
   * Addes a displayable order to the list of ordered displayables.
   * 
   * @param displayableOrder the DisplayableOrder to add
   */
    public void addDisplayable(DisplaySurface.DisplayableOrder displayableOrder) {
        orderedDisplayables.add(displayableOrder);
    }

    /**
   * Removes a Displayable to the list of displayables to be displayed.
   *
   * @param d the displayable to remove
   */
    public void removeDisplayable(Displayable d) {
        displayables.remove(d);
    }

    /**
   * Gets the graphics context on which to draw.
   */
    public Graphics2D getGraphics() {
        return g2;
    }

    /**
   * Paints the displayables.
   */
    public abstract void paint(Graphics g);

    public abstract void drawRect(Graphics g, int left, int top, int width, int height);

    public abstract void eraseRect(Graphics g);

    public void finalize() {
        if (g2 != null) g2.dispose();
    }

    /**
   * Disposes of the graphics object
   */
    public void dispose() {
        if (g2 != null) g2.dispose();
        displayables.clear();
    }

    /**
   * Sets the background color.
   */
    public void setBackgroundColor(Color c) {
        bgColor = c;
    }

    /**
   * Paints the background to the currently specified color.
   */
    protected void paintBackground() {
        g2.setColor(bgColor);
        g2.fillRect(0, 0, width, height);
    }

    /**
   * Takes a snapshot of the current screen image.
   */
    public abstract void takeSnapshot(DataOutputStream os);

    /**
   * Gets the current BufferedImage
   */
    public Image getCurrentImage() {
        return buffImage;
    }
}
