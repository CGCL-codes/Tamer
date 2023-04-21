package uchicago.src.sim.gui;

import java.awt.Dimension;
import java.util.ArrayList;

/**
 * Abstract class upon which custom displays can be built.
 *
 * @author Nick Collier
 * @version $Revision: 1.6 $ $Date: 2004/11/03 19:50:59 $
 */
public abstract class Display implements Displayable {

    protected boolean view = true;

    protected int height, width;

    /**
   * Constructs a Display with the specified width and height.
   *
   * @param width the width of the display
   * @param height the height of the display
   */
    public Display(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
   * Gets the size of this Display.
   */
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /**
   * Does the actual drawing using the SimGraphics parameter.
   *
   * @param g the graphics context with which to draw
   */
    public abstract void drawDisplay(SimGraphics g);

    /**
   * Gets a list of the DisplayInfo object associated with this Display.
   */
    public ArrayList getDisplayableInfo() {
        ArrayList list = new ArrayList();
        list.add(new DisplayInfo("", TOGGLE_VIEW, this));
        return list;
    }

    /**
   * Invoked when a viewEvent for this display is fired by the
   * DisplaySurface.
   */
    public void viewEventPerformed(ViewEvent evt) {
        view = evt.showView();
    }

    /**
   * Resizes the display to this new pixel width and pixel height.
   */
    public void reSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
