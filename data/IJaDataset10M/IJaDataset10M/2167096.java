package sonia;

import java.util.Arrays;
import java.util.Vector;
import cern.colt.list.IntArrayList;

/**
 * Provides a set of static utility methods which can be called by the LayoutEngine
 * for comonly used layout functions.  All of these methods modify the coordinates
 * stored in the LayoutSlice, they do not effect the file coordinates stored in
 * the NodeAttributes.
 */
public class LayoutUtils {

    public LayoutUtils() {
    }

    /**
   * Randomly positions nodes on layout.  Called internally before
   * update layout is called for the first time to insure that nodes
   * have starting coordinates.
   */
    public static void randomizeLayout(SoniaController control, LayoutSlice slice, int width, int height) {
        int n = slice.getMaxNumNodes();
        double xLimit = width;
        double yLimit = height;
        for (int i = 0; i < n; i++) {
            double x = control.getUniformRand(0.0, xLimit);
            double y = control.getUniformRand(0.0, yLimit);
            slice.setCoords(i, x, y);
        }
    }

    /**
   * Positions nodes in a circle which fits within the rectangle defined by
   * width and height.
   */
    public static void circleLayout(LayoutSlice slice, int width, int height) {
        double radius = Math.min((width / 2.0), (height / 2.0));
        double originX = (width / 2.0);
        double originY = (height / 2.0);
        int n = slice.getMaxNumNodes();
        for (int i = 0; i < n; i++) {
            double x = (radius * Math.cos(2 * Math.PI * i / n));
            double y = (radius * Math.sin(2 * Math.PI * i / n));
            slice.setCoords(i, originX + x, originY + y);
        }
    }

    /**
   * Sets the coordinates in fromSlice to match those in toSlice.
   */
    public static void copyLayout(LayoutSlice fromSlice, LayoutSlice toSlice) {
        int nFrom = fromSlice.getMaxNumNodes();
        int nTo = toSlice.getMaxNumNodes();
        if (nFrom == nTo) {
            double[] fromX = fromSlice.getXCoords();
            double[] fromY = fromSlice.getYCoords();
            for (int i = 0; i < nFrom; i++) {
                toSlice.setCoords(i, fromX[i], fromY[i]);
            }
        }
    }

    /**
  * Recenters nodes so that the <B>average</B> position (bary center) is in the middle
  * of the window. Can be more useful than regular recentering because it is
  * not as impacted by isolates and outliers and is more likely to focus on
  * the main component. NOTE: Calculates the averages from the passed coordinate array,
  * rather than getting them from the slice.
  * @param isoIndepen wether or not to include the isolates when calculating
  * the averages.
  */
    public static void barycenterLayout(LayoutSlice slice, int width, int height, double[] xPos, double[] yPos, boolean isoIndepen) {
        IntArrayList presentNodes = slice.getPresentNodes();
        IntArrayList isolates;
        if (isoIndepen) {
            isolates = slice.getIsolates();
        } else {
            isolates = new IntArrayList();
        }
        int nNodes = presentNodes.size();
        int maxNodes = slice.getMaxNumNodes();
        double xAvg = 0;
        double yAvg = 0;
        int count = 0;
        for (int i = 0; i < nNodes; i++) {
            int index = presentNodes.get(i);
            if (!isolates.contains(index)) {
                xAvg += xPos[index];
                yAvg += yPos[index];
                count++;
            }
        }
        xAvg = xAvg / count;
        yAvg = yAvg / count;
        double xDiff = width / 2 - xAvg;
        double yDiff = height / 2 - yAvg;
        for (int i = 0; i < maxNodes; i++) {
            xPos[i] = xPos[i] + xDiff;
            yPos[i] = yPos[i] + yDiff;
            slice.setCoords(i, xPos[i], yPos[i]);
        }
    }

    /**
    * Recenters nodes so that the <B>average</B> position (bary center) is in the middle
    * of the window. Can be more useful than regular recentering because it is
    * not as impacted by isolates and outlisers and is more likely to focus on
    * the main component. Calls barycenterLayout after getting the coords from
    * the slice.
    * @param isoIndepen if false, don't include the isolates when calculating
    * the averages.
  */
    public static void barycenterLayout(LayoutSlice slice, int width, int height, boolean isoIndepen) {
        double[] xPos = slice.getXCoords();
        double[] yPos = slice.getYCoords();
        barycenterLayout(slice, width, height, xPos, yPos, isoIndepen);
    }

    /**
   * Centers nodes so that center of layout is in the center of the window.
   * only repositions coords of nodes present in the layout at that time.  NOTE:
   * calculation is based on the passed coordinate arrays, not on the slice's coords.
   * @param isoIndepen if true, don't include isolates in the calculation
   */
    public static void centerLayout(LayoutSlice slice, int width, int height, double[] xPos, double[] yPos, boolean isoIndepen) {
        IntArrayList presentNodes = slice.getPresentNodes();
        IntArrayList isolates;
        if (isoIndepen) {
            isolates = slice.getIsolates();
        } else {
            isolates = new IntArrayList();
        }
        int nNodes = presentNodes.size();
        int maxNodes = slice.getMaxNumNodes();
        double xMax = Double.NEGATIVE_INFINITY;
        double xMin = Double.POSITIVE_INFINITY;
        double yMax = Double.NEGATIVE_INFINITY;
        double yMin = Double.POSITIVE_INFINITY;
        for (int i = 0; i < nNodes; i++) {
            int index = presentNodes.get(i);
            if (!isolates.contains(index)) {
                xMax = Math.max(xPos[index], xMax);
                xMin = Math.min(xPos[index], xMin);
                yMax = Math.max(yPos[index], yMax);
                yMin = Math.min(yPos[index], yMin);
            }
        }
        double xRange = xMax - xMin;
        double yRange = yMax - yMin;
        double xDiff = width / 2 - (xMin + xRange / 2);
        double yDiff = height / 2 - (yMin + yRange / 2);
        for (int i = 0; i < maxNodes; i++) {
            xPos[i] += xDiff;
            yPos[i] += yDiff;
            slice.setCoords(i, xPos[i], yPos[i]);
        }
    }

    /**
   * Recenters nodes so that center of layout is in the center of the window.
   * only repositions coords of nodes present in the layout at that time.  Calls
   * centerLayout after getting coords from slice.
   * @param isoIndepen if true, don't include isolates in the calculation
   */
    public static void centerLayout(LayoutSlice slice, int width, int height, boolean isoIndepen) {
        double[] xPos = slice.getXCoords();
        double[] yPos = slice.getYCoords();
        centerLayout(slice, width, height, xPos, yPos, isoIndepen);
    }

    /**
    * Rescales the node coordinates so that the layout will fit within the layout
    * window.
    */
    public static void rescalePositions(LayoutSlice slice, int width, int height, double[] xPos, double[] yPos, boolean isolatesIndependent) {
        IntArrayList presentNodes = slice.getPresentNodes();
        int nNodes = presentNodes.size();
        int maxNodes = slice.getMaxNumNodes();
        IntArrayList isolates;
        if (isolatesIndependent) {
            isolates = slice.getIsolates();
        } else {
            isolates = new IntArrayList();
        }
        double xMax = xPos[0];
        double yMax = yPos[0];
        double xMin = xPos[0];
        double yMin = yPos[0];
        for (int i = 0; i < nNodes; i++) {
            int index = presentNodes.get(i);
            if (!isolates.contains(index)) {
                xMax = Math.max(xMax, xPos[index]);
                yMax = Math.max(yMax, yPos[index]);
                xMin = Math.min(xMin, xPos[index]);
                yMin = Math.min(yMin, yPos[index]);
            }
        }
        double xDiff = xMax - xMin;
        double yDiff = yMax - yMin;
        double xScale = width / xDiff;
        double yScale = height / yDiff;
        if (Double.isInfinite(xScale)) {
            xScale = 1;
        }
        if (Double.isInfinite(yScale)) {
            yScale = 1;
        }
        double scale = Math.min(xScale, yScale);
        for (int i = 0; i < maxNodes; i++) {
            xPos[i] = (xPos[i] - xMin) * scale;
            yPos[i] = (yPos[i] - yMin) * scale;
            slice.setCoords(i, xPos[i], yPos[i]);
        }
    }

    /**
   * Sets the coordinates of the LayoutSlice to the coordinates of the passed
   * arrays.
   */
    public static void applyCoordsToSlice(double[] xPos, double[] yPos, LayoutSlice slice) {
        int nNodes = slice.getMaxNumNodes();
        for (int i = 0; i < nNodes; i++) {
            slice.setCoords(i, xPos[i], yPos[i]);
        }
    }

    /**
  * positions all the isolates in a row along the bottom of the layout
  */
    public static void pinIsolatesBottom(LayoutSlice slice, int width, int height) {
        IntArrayList isolates = slice.getIsolates();
        int numIsos = isolates.size();
        double spacing = (double) width / (double) numIsos;
        for (int i = 0; i < numIsos; i++) {
            int index = isolates.get(i);
            slice.setCoords(index, (i * spacing) + (spacing / 2), height);
        }
    }

    public static void pinIsolatesOrig(LayoutSlice slice) {
        IntArrayList isolates = slice.getIsolates();
        int numIsos = isolates.size();
        for (int i = 0; i < numIsos; i++) {
            int index = isolates.get(i);
            NodeAttribute node = slice.getNodeEvent(index);
            slice.setCoords(index, node.getObsXCoord(), node.getObsYCoord());
        }
    }

    /**
  * positions all the isolates in a circle independent of the rest of the layout
  * */
    public static void pinIsolatesCircle(LayoutSlice slice, int width, int height) {
        double radius = Math.min((width / 2.0), (height / 2.0));
        double originX = (width / 2.0);
        double originY = (height / 2.0);
        IntArrayList isolates = slice.getIsolates();
        int numIsos = isolates.size();
        for (int i = 0; i < numIsos; i++) {
            int index = isolates.get(i);
            double x = (radius * Math.cos(2 * Math.PI * i / numIsos));
            double y = (radius * Math.sin(2 * Math.PI * i / numIsos));
            slice.setCoords(index, originX + x, originY + y);
        }
    }

    /**
	 * scales the layout up or down (dilates or contracts) around the center
	 * of the layout (not the coordinate origin)
	 * @author skyebend
	 * @param slice  the slice to transform the coords of
	 * @param width  the width of the layout
	 * @param height the height of the layout
	 * @param scaleFactor the scaling factor
	 */
    public static void scaleLayout(LayoutSlice slice, double scaleFactor, int width, int height) {
        double[] xCoords = slice.getXCoords();
        double[] yCoords = slice.getYCoords();
        double halfw = width / 2;
        double halfh = height / 2;
        for (int i = 0; i < yCoords.length; i++) {
            xCoords[i] = ((xCoords[i] - halfw) * scaleFactor) + halfw;
            yCoords[i] = ((yCoords[i] - halfh) * scaleFactor) + halfh;
        }
    }

    public static void rotateLayout(LayoutSlice slice, double degrees, int width, int height) {
        double theta = Math.toRadians(degrees);
        double[] x = slice.getXCoords();
        double[] y = slice.getYCoords();
        for (int i = 0; i < y.length; i++) {
            double xt = x[i] - width / 2;
            double yt = y[i] - height / 2;
            double d = Math.sqrt(xt * xt + yt * yt);
            double th1 = Math.atan(xt / yt);
            if (yt < 0) d *= -1;
            x[i] = (d * Math.sin(th1 - theta)) + width / 2;
            y[i] = (d * Math.cos(th1 - theta)) + height / 2;
        }
    }

    /**
	 * shifts the layout by the specified number of pixels
	 * @author skyebend
	 * @param slice slice to transform
	 * @param deltaX  pixels to shift in the x direction
	 * @param deltaY  pixels to shift in tye y diection 
	 */
    public static void panLayout(LayoutSlice slice, int deltaX, int deltaY) {
        double[] x = slice.getXCoords();
        double[] y = slice.getYCoords();
        for (int i = 0; i < y.length; i++) {
            x[i] = x[i] + deltaX;
            y[i] = y[i] + deltaY;
        }
    }
}
