package br.jabuti.gvf;

import java.awt.*;

public interface GVFDisplayable {

    public static final int ADJUST_X = 50, ADJUST_Y = 50;

    /**
     * Move the Object to a particular (X,Y) position.
     */
    public void moveTo(int X, int Y);

    /**
     * Move the Object a certain distance from its original position.
     */
    public void translate(int deltaX, int deltaY);

    /**
     * Return TRUE if the point (X,Y) is within the boundaries of the Object.
     */
    public boolean itsMe(int X, int Y);

    /**
     * Return TRUE if the Object is within the boundaries of the Rectangle.
     */
    public boolean itsMe(Rectangle r);

    /**
     * Method the defines how the Object should be drawn.
     */
    public void draw(Graphics g);

    /**
     * Changes the selected state of the Object.
     */
    public void selected(boolean s);

    /**
     * Returns TRUE if the Object is currently selected, FALSE otherwise.
     */
    public boolean isSelected();

    /**
     * Returns TRUE if the Object contains the point (X,Y).
     */
    public boolean contains(int x, int y);

    /**
     * Returns a String with the Object's Label.
     */
    public String getLabel();
}
