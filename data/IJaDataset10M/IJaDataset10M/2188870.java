package uchicago.src.sim.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import uchicago.src.collection.BaseMatrix;
import uchicago.src.collection.DoubleMatrix;
import uchicago.src.sim.space.Discrete2DSpace;

/**
 * Displays 2d arrays of values (Integers or ints). As a Displayable this class
 * iterates through a Discrete2DSpace, turns the integers into
 * colors according to a {@link uchicago.src.sim.gui.ColorMap ColorMap}, and
 * draws these colors in the appropriate cells.
 *
 */
public class Value2DHexaDisplay extends HexaDisplay2D {

    private ColorMap colorMap;

    private int mapping = 1;

    private int mappingColor = 0;

    private boolean zeroTrans = false;

    /**
   * Creates a Value2DDisplay to display the specified Discrete2DSpace
   * using the specified ColorMap.
   */
    public Value2DHexaDisplay(Discrete2DSpace grid, ColorMap map) {
        super(grid);
        colorMap = map;
    }

    /**
   * Linear transform of states (doubles, floats, integers etc.) to
   * colors for drawing. color = state / m + c
   */
    public void setDisplayMapping(int m, int c) {
        mapping = m;
        mappingColor = c;
    }

    /**
   * Sets whether or not a zero value in space drawn by this grid is
   * transparent or drawn according to the color map. Default is to
   * draw according to the color map.
   *
   * @param val if true, zero value will be drawn as transparent, otherwise
   * the value in the color map is drawn.
   */
    public void setZeroTransparent(boolean val) {
        zeroTrans = val;
    }

    /**
   * Returns whether a zero value in space drawn by this grid is
   * transparent or drawn according to the color map.
   */
    public boolean isZeroTransparent() {
        return zeroTrans;
    }

    /**
   * Draws the Discrete2DSpace converting the Numbers contained therein to
   * Colors according to the ColorMap specified in the constructor
   */
    public void drawDisplay(SimGraphics g) {
        if (!view) return;
        BaseMatrix matrix = grid.getMatrix();
        if (matrix instanceof DoubleMatrix) {
            DoubleMatrix dm = (DoubleMatrix) matrix;
            double value;
            for (int i = 0; i < grid.getSizeX(); i++) {
                for (int j = 0; j < grid.getSizeY(); j++) {
                    value = dm.getDoubleAt(i, j);
                    int colorValue = (int) (value / mapping + mappingColor);
                    drawAt(g, i, j, colorValue);
                }
            }
        } else {
            Number value;
            for (int i = 0; i < grid.getSizeX(); i++) {
                for (int j = 0; j < grid.getSizeY(); j++) {
                    value = (Number) matrix.get(i, j);
                    int colorValue = (int) (value.doubleValue() / mapping + mappingColor);
                    drawAt(g, i, j, colorValue);
                }
            }
        }
    }

    private void drawAt(SimGraphics g, int x, int y, int colorValue) {
        super.setHexagons();
        y = (x % 2 != 0) ? (y * yTrans) : (y * yTrans + yTransHalf);
        x = (x * xTrans - x * xTrans1q);
        Polygon q = new Polygon(polyClip.xpoints, polyClip.ypoints, polyClip.npoints);
        q.translate(x, y);
        Graphics2D g2d = g.getGraphics();
        if ((!zeroTrans) || (colorValue != 0)) {
            g2d.setColor(colorMap.getColor(colorValue));
            g2d.fillPolygon(q);
        }
        if (isFramed) {
            g2d.setColor(frameColor);
            g2d.draw(q);
        }
    }

    /**
   * Gets an ArrayList of Objects at an x, y screen (pixel) coordinate.
   * Implements the probeable interface.
   */
    public ArrayList getObjectsAt(int x, int y) {
        Dimension d = getCoordinates(x, y);
        ArrayList list = new ArrayList();
        Object o = grid.getObjectAt(x, y);
        if (o != null) {
            list.add(new ProbeableNumber(d.width, d.height, grid, o));
        }
        return list;
    }
}
