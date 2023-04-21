package src.gui;

import src.parser.Workout;
import ewe.fx.BufferedGraphics;
import ewe.fx.Color;
import ewe.fx.Graphics;
import ewe.fx.Point;
import ewe.fx.Rect;
import ewe.ui.Canvas;
import ewe.util.Vector;

/**
 * Pad for drawing curves (heartrate, speed ...)
 * 
 * @author Karl Neuhold
 */
public class DiagramDrawPad extends Canvas {

    /**
     * The width of the border at the top of the canvas.
     */
    public static final int BORDER_TOP = 20;

    /**
     * The width of the border at the bottom of the canvas.
     */
    public static final int BORDER_BOTTOM = 20;

    /**
     * The width of the border at the left of the canvas.
     */
    public static final int BORDER_LEFT = 20;

    /**
     * The width of the border at the right of the canvas.
     */
    public static final int BORDER_RIGHT = 30;

    /** workout to draw */
    private Workout workout;

    /**
     * List of possible value distances between two main lines on the exercise
     * values axis.
     */
    private static final int[] LINE_DISTANCE_LIST_VALUES = { 1, 2, 5, 10, 25, 50, 100, 200, 500, 1000, 2000, 5000 };

    /** Minimum distance between two vertical main diagram lines. */
    private static final int MIN_LINE_DISTANCE_HORIZENTAL = 50;

    /** Minimum distance between two vertical main diagram lines. */
    private static final int MIN_LINE_DISTANCE_VERTICAL = 25;

    /** Curves to plot */
    private Vector curvesToPlot;

    /** Time/km for bottom axis */
    private boolean timeForBottomAxis = true;

    /**
     * Constructor
     * 
     * @param workout -
     *            Displaying data from this workout.
     * @param curvesToPlot
     *            curves to plot.
     * @param timeForBottomAxis
     *            Time used for bottom axis.
     */
    public DiagramDrawPad(Workout workout, Vector curvesToPlot, boolean timeForBottomAxis) {
        super();
        backGround = Color.White;
        setPreferredSize(240, 280);
        this.workout = workout;
        this.curvesToPlot = curvesToPlot;
        this.timeForBottomAxis = timeForBottomAxis;
    }

    /**
     * painting curves
     * 
     * @param g
     *            graphics object
     * @param r
     *            drawing area
     */
    public void doPaint(Graphics g, Rect r) {
        doBackground(g);
        if (origin == null) {
            origin = new Point(0, 0);
        }
        for (int i = 0; i < curvesToPlot.getCount(); i++) {
            drawCurve(g, r, curvesToPlot.get(i).toString());
        }
        updateScrollServer(0);
        checkScrolls();
    }

    /**
     * Drawing heart rate curve.
     * 
     * @param g
     *            graphics object
     * @param r
     *            Rect Area
     * @param which
     *            Which = which curve to plot (HR, speed, ..)
     */
    private void drawCurve(Graphics g, Rect r, String which) {
        int currentValue = 0;
        float zoomX = 1;
        int zoomY = getZoomY(r);
        currentValue = calculateZoomHeartRate(r, which, currentValue, zoomY);
        currentValue = calculateZoomSpeed(r, which, currentValue, zoomY);
        if (which.equals("altitude")) {
            currentValue = workout.getSampleList()[0].getAltitude();
            zoomX = calculateZoomAltitude(r, which, zoomY);
        }
        BufferedGraphics bg = new BufferedGraphics(g, virtualSize);
        Graphics draw = bg.getGraphics();
        initialzeDiagram(draw, which);
        int nullPointX = getNullPointX();
        int startY = getStartPointY();
        int startX = nullPointX - currentValue;
        drawCurves(which, currentValue, zoomX, zoomY, draw, nullPointX, startY, startX);
        bg.release();
        g.flush();
    }

    /**
     * @param which
     *            Curve to plot.
     * @param currentValue
     *            Value now.
     * @param zoomX
     *            Zooming x-direction
     * @param zoomY
     *            Zooming y-direction
     * @param draw
     *            Graphics Object
     * @param nullPointX
     *            Null point x-axis
     * @param startY
     *            Start point y-axis
     * @param startX
     *            Start point x-axis.
     */
    private void drawCurves(String which, int currentValue, float zoomX, int zoomY, Graphics draw, int nullPointX, int startY, int startX) {
        for (int i = 0; i < workout.getSampleList().length; i += zoomY) {
            currentValue = getCurrentValue(which, currentValue, zoomX, draw, i);
            int endX = nullPointX - currentValue;
            if (endX <= BORDER_LEFT) {
                endX = BORDER_LEFT;
            }
            draw.drawLine(startX, startY, endX, startY--);
            startX = endX;
        }
    }

    /**
     * @param which
     *            Curve to plot.
     * @param currentValue
     *            Value now.
     * @param zoomX
     *            Zooming x-direction
     * @param draw
     *            Graphics object.
     * @param i
     *            Index
     * @return Current value to draw.
     */
    private int getCurrentValue(String which, int currentValue, float zoomX, Graphics draw, int i) {
        currentValue = getCurrentValueHeartRate(which, currentValue, zoomX, i);
        currentValue = getCurrentValueSpeed(which, currentValue, zoomX, draw, i);
        currentValue = getcurrentValueAltitude(which, currentValue, zoomX, draw, i);
        return currentValue;
    }

    /**
     * @param which
     *            Curve to plot.
     * @param currentValue
     *            Value now.
     * @param zoomX
     *            Zooming x-direction   
     * @param i
     *            Index
     * @param draw
     *            Graphics object.
     * @return Current value to draw.
     */
    private int getcurrentValueAltitude(String which, int currentValue, float zoomX, Graphics draw, int i) {
        if (which.equals("altitude")) {
            draw.setColor(new Color(0, 255, 0));
            currentValue = Math.round(workout.getSampleList()[i].getAltitude() * zoomX);
        }
        return currentValue;
    }

    /**
     * @param which
     *            Curve to plot.
     * @param currentValue
     *            Value now.
     * @param zoomX
     *            Zooming x-direction   
     * @param i
     *            Index
     * @param draw
     *            Graphics object.
     * @return Current value to draw.
     */
    private int getCurrentValueSpeed(String which, int currentValue, float zoomX, Graphics draw, int i) {
        if (which.equals("speed")) {
            draw.setColor(Color.DarkBlue);
            currentValue = Math.round(workout.getSampleList()[i].getSpeed() * zoomX);
        }
        return currentValue;
    }

    /**
     * @param which
     *            Curve to plot.
     * @param currentValue
     *            Value now.
     * @param zoomX
     *            Zooming x-direction
     * @param i
     *            Index
     * @return Current value to draw.
     */
    private int getCurrentValueHeartRate(String which, int currentValue, float zoomX, int i) {
        if (which.equals("heartrate")) {
            currentValue = Math.round((workout.getSampleList()[i].getHeartReate()) * zoomX);
        }
        return currentValue;
    }

    /**
     * @param r
     *            Area
     * @param which
     *            Selected item
     * @param zoomY
     *            Zooming y-direction
     * @return Zoom factor
     */
    private float calculateZoomAltitude(Rect r, String which, int zoomY) {
        float zoomX;
        if (curvesToPlot.get(0).equals(which)) {
            setVirtualSize(r, zoomY, workout.getAltitude().getAltitudeMax());
            zoomX = getZoomX(workout.getAltitude().getAltitudeMax());
        } else {
            zoomX = 1;
        }
        return zoomX;
    }

    /**
     * @param r
     *            Area
     * @param which
     *            Selected item
     * @param currentValue
     *            Value now
     * @param zoomY
     *            Zooming y-direction
     * @return Zoom factor
     */
    private int calculateZoomSpeed(Rect r, String which, int currentValue, int zoomY) {
        float zoomX;
        if (which.equals("speed")) {
            currentValue = Math.round(workout.getSampleList()[0].getSpeed());
            float speedMax = workout.getSpeed().getSpeedMax();
            if (curvesToPlot.get(0).equals(which)) {
                setVirtualSize(r, zoomY, Math.round(speedMax));
                zoomX = getZoomX(speedMax);
            } else {
                zoomX = 1;
            }
        }
        return currentValue;
    }

    /**
     * @param r
     *            Area
     * @param which
     *            Selected item
     * @param currentValue
     *            Value now
     * @param zoomY
     *            Zooming y-direction
     * @return Zoom factor
     */
    private int calculateZoomHeartRate(Rect r, String which, int currentValue, int zoomY) {
        float zoomX;
        if (which.equals("heartrate")) {
            currentValue = workout.getSampleList()[0].getHeartReate();
            if (curvesToPlot.get(0).equals(which)) {
                setVirtualSize(r, zoomY, workout.getHeartRateMax());
                zoomX = getZoomX((float) workout.getHeartRateMax());
            } else {
                zoomX = 1;
            }
        }
        return currentValue;
    }

    /**
     * Compute start point for x.
     * 
     * @return start point for x
     */
    private int getStartPointY() {
        int startY = -origin.y + virtualSize.height - BORDER_TOP;
        return startY;
    }

    /**
     * Get the null point for x.
     * 
     * @return Null point for x.
     */
    private int getNullPointX() {
        int nullPointX = -origin.x + virtualSize.width - BORDER_RIGHT;
        return nullPointX;
    }

    /**
     * @param maxValue
     *            Maximum value (max speed, max HR ..)
     * @return Zooming in x-direction.
     */
    private float getZoomX(float maxValue) {
        if (Math.round(maxValue) < 1) {
            maxValue = 1;
        }
        float zoomX = (virtualSize.width - BORDER_LEFT - BORDER_RIGHT - 2) / maxValue;
        if (Math.round(zoomX) <= 0) {
            zoomX = 1;
        }
        return zoomX;
    }

    /**
     * Initialze the Diagram (border, axis ..).
     * 
     * @param draw
     *            Grafics area
     * @param which
     *            Speed, HR, Altitude
     */
    private void initialzeDiagram(Graphics draw, String which) {
        drawBorder(draw);
        drawYAxis(draw);
        drawXAxis(draw, which);
        draw.setColor(new Color(255, 0, 0));
    }

    /**
     * Compute zooming in Y-direction.
     * 
     * @param r
     *            Area
     * @return Zooming factor in y-direction.
     */
    private int getZoomY(Rect r) {
        int zoomY = Math.round(workout.getSampleList().length / r.height);
        if (zoomY <= 0) {
            zoomY = 1;
        }
        return zoomY;
    }

    /**
     * Draw the x-Axis
     * 
     * @param g
     *            Graphics object
     * @param which
     *            Curve (HR, speed, ..) to labeling.
     */
    private void drawXAxis(Graphics g, String which) {
        int totalValue = 0;
        float height = virtualSize.height;
        int numOfLines = 1;
        int moving = 9;
        int nullPointY = -origin.y + virtualSize.height - BORDER_TOP;
        float nullPointX = -origin.x + virtualSize.width - BORDER_RIGHT;
        totalValue = setTotalValueXAxis(g, which, totalValue);
        if (curvesToPlot.getCount() > 1 && curvesToPlot.get(1).equals(which) && !which.equals("altitude")) {
            numOfLines = 3;
            height = totalValue / (float) numOfLines;
        } else {
            height = getBestDistance(totalValue, MIN_LINE_DISTANCE_VERTICAL, totalValue);
            height = drawNullCurve(g, which, totalValue, height, nullPointY);
            numOfLines = getNumOfLinesXAxis(height);
        }
        for (int i = 1; i <= numOfLines; i++) {
            int pointX = (int) (nullPointX - (i * height));
            g.drawLine(pointX, nullPointY, pointX, -origin.y + BORDER_TOP);
            int currentValue = totalValue / (numOfLines + 1);
            if (currentValue <= 100) {
                moving = 12;
            }
            if (curvesToPlot.get(0).equals(which)) {
                g.drawText(" " + (i * currentValue), pointX - moving, nullPointY);
                g.drawText(totalValue + "", -origin.x + BORDER_LEFT - moving - 6, nullPointY);
            } else {
                g.drawText(" " + Math.round(i * height), pointX - moving, -origin.y);
            }
        }
        g.drawLine(-origin.x + BORDER_LEFT, nullPointY, -origin.x + BORDER_LEFT, -origin.y + BORDER_TOP);
    }

    /**
     * @param g
     *            Graphics object
     * @param which
     *            Curve to plot.
     * @param totalValue
     *            Max. value.
     * @param height
     *            Heigh of curve.
     * @param nullPointY
     *            Null point
     * @return New height.
     */
    private float drawNullCurve(Graphics g, String which, int totalValue, float height, int nullPointY) {
        if (totalValue <= 0) {
            height = virtualSize.height;
            g.setColor(new Color(255, 0, 0));
            g.drawText(" " + which.toUpperCase() + " not recorded!", -origin.x + BORDER_LEFT, nullPointY - (virtualSize.height / 2));
        }
        return height;
    }

    /**
     * @param g
     *            Graphics object.
     * @param which
     *            Curve to plot.
     * @param totalValue
     *            Max. value.
     * @return Maximum value.
     */
    private int setTotalValueXAxis(Graphics g, String which, int totalValue) {
        if (which.equals("heartrate")) {
            totalValue = workout.getHeartRateMax();
            g.setColor(new Color(255, 0, 0));
        }
        if (which.equals("speed")) {
            totalValue = Math.round(workout.getSpeed().getSpeedMax());
            g.setColor(Color.DarkBlue);
        }
        if (which.equals("altitude")) {
            totalValue = Math.round(workout.getAltitude().getAltitudeMax());
            g.setColor(new Color(0, 255, 0));
        }
        return totalValue;
    }

    /**
     * @param height
     *            Distance beetween two lines.
     * @return Number of lines for the X-Axis.
     */
    private int getNumOfLinesXAxis(float height) {
        int numOfLines = (int) ((virtualSize.width - BORDER_LEFT - BORDER_RIGHT) / height);
        if (numOfLines < 1) {
            numOfLines = 1;
        }
        return numOfLines;
    }

    /**
     * Draws the bottom Axis
     * 
     * @param g
     *            Graphic object
     */
    private void drawYAxis(Graphics g) {
        int totalBottomValue;
        totalBottomValue = setBottomAxis();
        float height = getBestDistance(totalBottomValue, MIN_LINE_DISTANCE_HORIZENTAL, virtualSize.height);
        int numOfLines = getNumOfLinesYAxis(height);
        float nullPointY = -origin.y + virtualSize.height - BORDER_BOTTOM;
        int nullPointX = -origin.x + virtualSize.width - BORDER_RIGHT;
        for (int i = 0; i < numOfLines; i++) {
            g.setColor(Color.Black);
            g.drawLine(nullPointX, (int) (nullPointY - (i * height)), -origin.x + BORDER_LEFT, (int) (nullPointY - (i * height)));
            if (totalBottomValue > 0) {
                drawYAxisText(g, height, nullPointY, nullPointX, i, totalBottomValue, numOfLines);
            }
            g.setColor(Color.LighterGray);
            g.drawLine(nullPointX, (int) (nullPointY - ((i * height) + (height / 2))), -origin.x + BORDER_LEFT, (int) (nullPointY - ((i * height) + (height / 2))));
        }
    }

    /**
     * @return Value for bottom axis (time or speed).
     */
    private int setBottomAxis() {
        int totalBottomValue;
        if (timeForBottomAxis) {
            totalBottomValue = Math.round(workout.getDuration() / 60f);
        } else {
            totalBottomValue = workout.getSpeed().getDistance();
        }
        return totalBottomValue;
    }

    /**
     * @param height
     *            Distance between two lines
     * @return Number of lines for the Y-Axis
     */
    private int getNumOfLinesYAxis(float height) {
        int numOfLines = (int) ((virtualSize.height - BORDER_BOTTOM - BORDER_TOP) / height);
        if (numOfLines < 1) {
            numOfLines = 1;
        }
        return numOfLines;
    }

    /**
     * 
     * @param g
     *            Graphics Object
     * @param height
     *            Height used per line.
     * @param nullPointY
     *            null point x-axis
     * @param nullPointX
     *            null point y-axis
     * @param i
     *            index
     * @param totalValue
     *            Summ of time, km ...
     * @param numOfLines
     *            numbers of lines
     */
    private void drawYAxisText(Graphics g, float height, float nullPointY, int nullPointX, int i, int totalValue, int numOfLines) {
        int distanceForCentering = 7;
        int distanceToBorder = 2;
        int divisionForKM = 10;
        if (timeForBottomAxis) {
            divisionForKM = 1;
        }
        g.drawText((int) ((i * (totalValue / numOfLines)) / divisionForKM) + "", nullPointX + distanceToBorder, (int) (nullPointY - (i * height) - distanceForCentering));
        g.drawText((int) (((i * (totalValue / numOfLines)) / divisionForKM) + ((totalValue / numOfLines) / divisionForKM) / 2) + "", nullPointX + distanceToBorder, (int) (nullPointY - ((i * height) + (height / 2)) - distanceForCentering));
        g.drawText((totalValue / divisionForKM) + "", nullPointX, -origin.y + BORDER_TOP - distanceForCentering);
    }

    /**
     * Compute the best distance between two main value lines.
     * 
     * @param totalValue
     *            Totale value (km,..)
     * @param length
     *            Lenght
     * @param minDistance
     *            Min. distance between two lines
     * @return Best distance.
     */
    private float getBestDistance(int totalValue, int minDistance, int length) {
        float linesDistance = 1f;
        if (totalValue <= 0) {
            totalValue = 1;
        }
        for (int i = 0; i < LINE_DISTANCE_LIST_VALUES.length; i++) {
            linesDistance = (LINE_DISTANCE_LIST_VALUES[i] * length) / totalValue;
            if (minDistance < linesDistance) {
                break;
            }
        }
        return linesDistance;
    }

    /**
     * Set the virtual size of the drawing area.
     * 
     * @param r
     *            size of display
     * @param zoomY
     *            zooming factor
     * @param maximumValue
     *            maximum value (max HR, ...)
     */
    private void setVirtualSize(Rect r, int zoomY, int maximumValue) {
        int virtualWidth = maximumValue + BORDER_LEFT + BORDER_RIGHT;
        int virtualHeight = ((workout.getSampleList().length) / zoomY) + BORDER_BOTTOM + BORDER_TOP;
        if (virtualWidth < r.width) {
            virtualWidth = r.width;
        }
        if (virtualHeight < r.height) {
            virtualHeight = r.height;
        }
        virtualSize = new Rect(0, 0, virtualWidth, virtualHeight);
    }

    /**
     * Draw border.
     * 
     * @param g
     *            Graphics object
     */
    private void drawBorder(Graphics g) {
        g.setColor(Color.Black);
        g.drawRect(-origin.x + BORDER_LEFT, -origin.y + BORDER_TOP, virtualSize.width - BORDER_LEFT - BORDER_RIGHT, virtualSize.height - BORDER_BOTTOM - BORDER_TOP);
    }

    /**
     * Used by scrollpanel
     * 
     * @return False for scrolling
     */
    public boolean canScreenScroll() {
        return false;
    }

    ;
}
