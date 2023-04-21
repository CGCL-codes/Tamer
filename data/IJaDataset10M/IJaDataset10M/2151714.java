package AccordionLRACDrawer;

import java.awt.Color;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.TreeSet;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLDrawable;
import javax.swing.JProgressBar;
import AccordionDrawer.AccordionDrawer;
import AccordionDrawer.CellGeom;
import AccordionDrawer.DrawableRange;
import AccordionDrawer.GridCell;
import AccordionDrawer.InteractionBox;
import AccordionDrawer.OlduvaiObject;
import AccordionDrawer.SplitAxis;
import AccordionDrawer.SplitLine;

/**
 * 
 * The middle layer drawing infrastructure that holds all the data structures
 * for doing AD gridding as well as drawing code. 
 * 
 * @author Peter McLachlan (spark343@cs.ubc.ca)
 *
 */
public abstract class AccordionLRACDrawer extends AccordionDrawer {

    static final int GUARANTEED_PIXELS = 1;

    /**
	 * Contains all data objects and operations that manipulate those objects.
	 */
    public DataStore datastore;

    /**
	 * Class containing all the group handling functions.  Groups are seeded 
	 * at the beginning of the draw cycle and therefore are the first items drawn.
	 */
    protected Groups seededGroups;

    /**
	 * Equivelent of getSplitAxis(X)
	 */
    protected SplitAxis deviceAxis;

    /**
	 * Equivelent of getSplitAxis(Y)
	 */
    protected SplitAxis metricAxis;

    public int minFontHeight = 14;

    public int maxFontHeight = 14;

    public int popupFontHeight = 14;

    private double minStuckX = 0.3f;

    private double maxStuckX = SplitAxis.defaultMaxStuckValue;

    private double minStuckY = 0.05f;

    private double maxStuckY = SplitAxis.defaultMaxStuckValue;

    protected int labeloffset[] = new int[2];

    protected int labelbuffer[] = new int[2];

    /**
	 * framebuffer pixels from last mouseover label drawn
	 */
    protected FloatBuffer pixels;

    /**
	 * GL rasterpos of last mouseover label drawn
	 */
    protected int drawnlabelpos[] = new int[2];

    /**
	 * width and height of last mouseover label drawn
	 */
    protected int drawnlabelsize[] = new int[2];

    boolean biggerThanScreen = false;

    public static boolean complementFlag = false;

    protected static MetricRangeList gapRangeList = null;

    protected double forestShrinkFactor;

    protected double pickoffset[] = new double[2];

    protected int pickFuzz;

    public static int countDrawnFrame;

    public static int countDrawnScene;

    public static int cumFrameTime;

    public int[] defaultFocusRange = new int[2];

    public AccordionLRACDrawer(int width, int height) {
        super(width, height);
        datastore = new DataStore(this);
        seededGroups = new Groups(this);
        horiz = true;
        labeloffset[X] = 2;
        labeloffset[Y] = 4;
        labelbuffer[X] = 10;
        labelbuffer[Y] = 1;
        pickoffset[0] = .1f;
        pickoffset[1] = .1f;
        pickFuzz = 5;
        forestShrinkFactor = -0.9f;
        expandleaves = 0;
        initCells(null);
        splitAxis[X].setMinStuckValue(minStuckX);
        splitAxis[X].setMaxStuckValue(maxStuckX);
        splitAxis[Y].setMinStuckValue(minStuckY);
        splitAxis[Y].setMaxStuckValue(maxStuckY);
        flashGeomOld = null;
        doBox = false;
        defaultFocusRange[0] = 0;
        defaultFocusRange[1] = 0;
        init(canvas);
    }

    /**
	 * Initialize the split line grid.  
	 * Although this takesa JProgressBar as an argument we don't do anything with it.  
	 * Unfortunately it is required from AccordionDrawer.  Because LiveRAC is using dynamic 
	 * data and the grid is created almost instantly, a progress bar is not very relevant 
	 * to us right here.  
	 * 
	 */
    public void initCells(JProgressBar bar) {
        gridDepth = new int[2];
        for (int xy = 0; xy < 2; xy++) {
            gridDepth[xy] = 5;
        }
        initSplitLines(true, false);
        metricAxis = getSplitAxis(AccordionDrawer.X);
        deviceAxis = getSplitAxis(AccordionDrawer.Y);
    }

    /**
	 * Given a range of split lines, and the (already aggregated) contents of
	 * those cells, perform the OpenGL operations. This method should be a
	 * barebones draw call. No culling or aggregation should happen here.
	 * 
	 * TODO: performance this could probably be adapted to be a completely static call which would allow it to be inlined. 
	 * 
	 * @param col
	 *            Color to draw
	 * @param label
	 *            Label to draw
	 * @param devSplits
	 *            Device boundary lines (Y)
	 * @param metricSplits
	 *            Site boundary lines (X)
	 * @param verticalDrawingRange
	 * @param plane
	 *            Z plane to draw on
	 * @param gv
	 */
    public void drawInCells(Color col, SplitLine[] devSplits, SplitLine[] metricSplits, double[] horizontalDrawingRange, double[] drawingRange, double plane, boolean gv) {
        countDrawnFrame++;
        GridCell.incrementCountBBoxDraw();
        if (nogeoms) return;
        double cellMin[] = { splitAxis[X].getAbsoluteValue(metricSplits[0], getFrameNum()), splitAxis[Y].getAbsoluteValue(devSplits[0], getFrameNum()) };
        double cellMax[] = { splitAxis[X].getAbsoluteValue(metricSplits[1], getFrameNum()), splitAxis[Y].getAbsoluteValue(devSplits[1], getFrameNum()) };
        if (gv) {
            double minCellDims = GUARANTEED_PIXELS;
            final double minSizeWorld = s2w(minCellDims, Y);
            cellMin[Y] = cellMax[Y] - minSizeWorld;
        } else {
            final double cellHeightRange = cellMax[Y] - cellMin[Y];
            cellMax[Y] = cellMin[Y] + drawingRange[1] * cellHeightRange;
            cellMin[Y] = cellMin[Y] + drawingRange[0] * cellHeightRange;
            final double cellWidthRange = cellMax[X] - cellMin[X];
            cellMax[X] = cellMin[X] + horizontalDrawingRange[1] * cellWidthRange;
            cellMin[X] = cellMin[X] + horizontalDrawingRange[0] * cellWidthRange;
        }
        if (takeSnapshot) {
            int cellMinPix[] = { w2s(cellMin[X], X), w2s(cellMin[Y], Y) };
            int cellMaxPix[] = { w2s(cellMax[X], X), w2s(cellMax[Y], Y) };
            try {
                snapShotWriter.write("newpath " + cellMinPix[X] + " " + cellMinPix[Y] + " moveto " + cellMinPix[X] + " " + cellMaxPix[Y] + " lineto " + cellMaxPix[X] + " " + cellMaxPix[Y] + " lineto " + cellMaxPix[X] + " " + cellMinPix[Y] + " lineto " + "closepath " + "gsave " + col.getRed() / 255f + " " + col.getGreen() / 255f + " " + col.getBlue() / 255f + " setrgbcolor " + "eofill grestore\n");
            } catch (IOException ioe) {
                System.out.println("Error: IOException while trying to write cells to file: " + snapShotWriter.toString());
            }
        } else {
            setColorGL(col);
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex3d(cellMin[X], cellMin[Y], plane);
            gl.glVertex3d(cellMin[X], cellMax[Y], plane);
            gl.glVertex3d(cellMax[X], cellMax[Y], plane);
            gl.glVertex3d(cellMax[X], cellMin[Y], plane);
            gl.glEnd();
        }
    }

    /**
	 * Given a splitline that lies in the center of a range, perform a draw
	 * operation on cell(s) within the line's bounds. The range could
	 * potentially be a single object or multiple objects. If it is multiple
	 * objects culling should be performed.
	 * 
	 * For LRAC the rangeLine is a horizontal split Line representing the center of a column.  
	 * 
	 * @param rangeLine
	 */
    public void drawRange(SplitLine rangeLine) {
        final double[] drawingRangeFull = { 0.00, 1.0 };
        if (!splitAxis[Y].isReal(rangeLine)) {
            drawCell(rangeLine, drawingRangeFull);
        } else {
            drawDeviceRange(rangeLine, drawingRangeFull);
        }
    }

    /**
	 * A group is a GV range that is drawn before the remainder of the display is filled in.  This method 
	 * draws a specifies range using a call to drawInCells().  
	 * 
	 * @param range
	 */
    public void drawGroup(DrawableRange range) {
        MetricRange ris = (MetricRange) range;
        double[] verticalDrawingRange = ((MetricRangeList) ris.getGroup()).getVerticalDrawingRange();
        double[] horizontalDrawingRange = ((MetricRangeList) ris.getGroup()).getHorizontalDrawingRange();
        Color color = ris.getGroup().getColor();
        float depth;
        if (color != null) depth = MetricRangeList.getDepth(hiliteplane, color.getAlpha()); else depth = hiliteplane;
        int[] metricIndexes = { ris.getMin(), ris.getMax() };
        SplitLine[] metricLines = { splitAxis[X].getSplitFromIndex(metricIndexes[0] - 1), splitAxis[X].getSplitFromIndex(metricIndexes[1]) };
        splitAxis[X].computePlaceThisFrame(metricLines[0], frameNum);
        splitAxis[X].computePlaceThisFrame(metricLines[1], frameNum);
        for (int i = 0; i < 2; i++) splitAxis[X].getAbsoluteValue(metricLines[i], getFrameNum());
        SplitLine siteSCMin = SplitLine.getOverlapSplitCell(splitAxis[X].getPartitionedList(), metricLines[0]);
        metricLines[0] = splitAxis[X].getMinBound(siteSCMin);
        if (metricIndexes[0] == metricIndexes[1]) metricLines[1] = metricLines[1] = splitAxis[X].getMaxBound(siteSCMin); else {
            SplitLine siteSCMax = SplitLine.getOverlapSplitCell(splitAxis[X].getPartitionedList(), metricLines[1]);
            metricLines[1] = splitAxis[X].getMinBound(siteSCMax);
        }
        int seq = ris.getDevice().getKey();
        SplitLine devLine = splitAxis[Y].getSplitFromIndex(seq);
        splitAxis[Y].computePlaceThisFrame(devLine, frameNum);
        SplitLine resultLine = SplitLine.getOverlapSplitCell(splitAxis[Y].getPartitionedList(), devLine);
        SplitLine[] deviceLines = new SplitLine[2];
        if (resultLine == null) {
            deviceLines[0] = deviceLines[1] = devLine;
        } else {
            deviceLines[0] = splitAxis[Y].getMinBound(resultLine);
            deviceLines[1] = splitAxis[Y].getMaxBound(resultLine);
        }
        drawInCells(color, deviceLines, metricLines, horizontalDrawingRange, verticalDrawingRange, depth, true);
    }

    /**
	 * Given a range, draw the nodes defined within
	 */
    public void drawRange(DrawableRange r) {
        if (r.getGroup() != null) {
            if (r.getGroup().getColor() == null) System.out.println("null color for group: " + r);
            drawGroup(r);
        } else {
            System.out.println("Error in drawRange(DrawableRange r), range is null");
        }
    }

    /**
	 * Draw a metric cell, 
	 * 
	 * @param deviceRangeLine The 'fake' line down the middle of this device (the previous bound of this represents the object the device is actually attached to)
	 * @param drawingRange Clipping range for this draw operation 
	 * 
	 */
    private void drawCell(SplitLine deviceRangeLine, double[] drawingRange) {
        SplitLine bounds[] = splitAxis[Y].getBounds(deviceRangeLine);
        Device dev = (Device) bounds[0].getRowObject();
        Iterator iter = getListOfMetrics().iterator();
        SplitLine columnLine = null;
        SplitLine[] columnBounds = null;
        int metricNum;
        MetricCell metric = null;
        while (iter.hasNext()) {
            columnLine = (SplitLine) iter.next();
            columnBounds = splitAxis[X].getBounds(columnLine);
            metricNum = splitAxis[X].getSplitIndex(columnBounds[0]) + 1;
            metric = dev.getMetric(metricNum);
            if (metric != null) metric.drawInCell(null, objplane, drawingRange, false, false);
        }
        return;
    }

    /**
	 * Given a range line which represents a SplitLine which has other SplitLines in the 
	 * tree underneath it (an internal node), draw the full range of this line using a 
	 * computed aggregated color.  
	 *  
	 * @param deviceRangeLine an internal node representing a range of devices
	 * @param drawingRangeFull The clipping range in world space of this draw operation. 
	 */
    private void drawDeviceRange(SplitLine deviceRangeLine, double[] drawingRangeFull) {
        final double[] drawHorizontalRangefull = { 0d, 1d };
        final SplitLine[] rowBounds = splitAxis[Y].getBounds(deviceRangeLine);
        Iterator iter = getListOfMetrics().iterator();
        SplitLine currColumnLine = null;
        SplitLine[] currColumnBounds = null;
        Color rangeColor = null;
        while (iter.hasNext()) {
            currColumnLine = (SplitLine) iter.next();
            currColumnBounds = splitAxis[X].getBounds(currColumnLine);
            rangeColor = computeRangeColor(deviceRangeLine, currColumnLine, datastore.getUpdateNumber());
            drawInCells(rangeColor, rowBounds, currColumnBounds, drawHorizontalRangefull, drawingRangeFull, objplane, false);
        }
    }

    /**
	 * Given an internal SplitLine representing a range of devices, and a column line
	 * parse through the underlying culled device objects and derive a color & saturation
	 * value to represent the status of this metric for this device.  
	 * 
	 * @param deviceRangeLine
	 * @param columnLine
	 * @return
	 */
    private Color computeRangeColor(SplitLine deviceRangeLine, SplitLine columnLine, long updateNumber) {
        Color col = null;
        RowColorCache rcc = (RowColorCache) deviceRangeLine.getCullingObject();
        if (rcc == null) {
            rcc = new RowColorCache();
            deviceRangeLine.setCullingObject(rcc);
        }
        SplitLine colMinBound = splitAxis[X].getMinBound(columnLine);
        int metricID = splitAxis[X].getSplitIndex(colMinBound) + 1;
        RowColorAggregator agg = rcc.getColorAggregator(metricID, updateNumber);
        if (agg == null) {
            agg = new RowColorAggregator();
            recurseCulledRowRange(deviceRangeLine, metricID, agg);
            agg.dataIndex = updateNumber;
            rcc.addColorAggregator(agg, metricID);
        }
        float saturation = MetricCell.BASESATURATION + (agg.numFound * .1f);
        if (saturation > 1) saturation = 1f;
        col = MetricCell.getSeverityColor(agg.worstAlarm, saturation);
        return col;
    }

    /**
	 * This is a recursive function to traverse all nodes underneath deviceRangeLine, 
	 * derive the alarm state and modify the agg object as required
	 *   
	 * @param deviceRangeLine
	 * @param metricID
	 * @param agg
	 */
    private void recurseCulledRowRange(SplitLine deviceRangeLine, int metricID, RowColorAggregator agg) {
        Device dev = (Device) deviceRangeLine.getRowObject();
        MetricCell cell = dev.getMetric(metricID);
        if (cell != null) {
            int severity = cell.getHighestSeverity();
            if (severity > agg.worstAlarm) {
                agg.worstAlarm = severity;
                agg.numFound = 1;
            } else if (severity == agg.worstAlarm) {
                agg.numFound++;
            }
        }
        if (deviceRangeLine.getLeftChild() != null) {
            recurseCulledRowRange(deviceRangeLine.getLeftChild(), metricID, agg);
            deviceRangeLine.getLeftChild().cullingObject = null;
        }
        if (deviceRangeLine.getRightChild() != null) {
            recurseCulledRowRange(deviceRangeLine.getRightChild(), metricID, agg);
            deviceRangeLine.getRightChild().cullingObject = null;
        }
        return;
    }

    /** 
	 * x and y are pixel positions
	 */
    public CellGeom pickAttached(int x, int y) {
        return pickGeom(x, y);
    }

    /**
	 * pick the Device at pixel position y
	 */
    private Device pickDevice(int y, double pixelSize) {
        double position = s2w(y, Y);
        SplitLine pickedLine = splitAxis[Y].getSplitFromAbsolute(position, pixelSize, frameNum);
        if (pickedLine == null) return null;
        return (Device) pickedLine.getRowObject();
    }

    /**
	 * given some pixel position, return the gridCell
	 */
    public CellGeom pickGeom(int x, int y) {
        if (getListOfDevices() == null || getListOfMetrics() == null) return null;
        Device dev = pickDevice(y, s2w(getMinCellDims(Y), Y));
        if (dev == null) return null;
        MetricCell n = dev.pickMetricCell(s2w(x, X), splitAxis[X], s2w(getMinCellDims(X), X));
        if (n == null) return null;
        return n;
    }

    /**
	 * Create box enclosing the subtree beneath input node/edge. The resulting
	 * box is the nearest GridLine boundaries to that point, which are cached so
	 * that redraws to erase the box can happen after the lines change position.
	 * 
	 * @param cg
	 *            TreeNode to act upon, passed in as CellGeom from
	 *            AccordionDrawer (where TreeNodes are not known)
	 * @see IntersectionBox
	 * @author Tamara Munzner
	 */
    public InteractionBox makeBox(CellGeom cg) {
        if (null == cg) return null;
        MetricCell sn = (MetricCell) cg;
        SplitLine[] minSplits = new SplitLine[2];
        SplitLine[] maxSplits = new SplitLine[2];
        SplitLine leftSplit = sn.getMinLine(X);
        SplitLine foundSiteSC = SplitLine.getOverlapSplitCell(getListOfMetrics(), leftSplit);
        SplitLine[] siteBounds = splitAxis[X].getBounds(foundSiteSC);
        if (foundSiteSC == null) {
            minSplits[X] = splitAxis[X].getPreviousSplit(leftSplit);
            maxSplits[X] = leftSplit;
        } else {
            minSplits[X] = siteBounds[SplitAxis.minBound];
            maxSplits[X] = siteBounds[SplitAxis.maxBound];
        }
        SplitLine topSplit = sn.getMinLine(Y);
        SplitLine foundSeqSC = SplitLine.getOverlapSplitCell(getListOfDevices(), topSplit);
        SplitLine[] seqBounds = splitAxis[Y].getBounds(foundSeqSC);
        if (foundSeqSC == null) {
            minSplits[Y] = splitAxis[Y].getPreviousSplit(topSplit);
            maxSplits[Y] = topSplit;
        } else {
            minSplits[Y] = seqBounds[SplitAxis.minBound];
            maxSplits[Y] = seqBounds[SplitAxis.maxBound];
        }
        return new InteractionBox(minSplits, maxSplits, sn, this);
    }

    /**
         * On mouseover, flash the object under the mouse and draw its label at
         * maximum size.
         * 
         * @author Tamara Munzner 
         * 
         * 	   represent flash over a range of cells - remove
         *         flash geoms, keep flash boxes don't flash until view has
         *         finished drawing: a block that hasn't been rendered will be
         *         saved as the background color if that region hasn't been
         *         drawn, then next move will overwrite the new color with
         *         background color entering in to this function, depth tests
         *         are off and stay off (i.e. don't change functions in
         *         AccordionDrawer called from here)
         *         
         * @see AccordionDrawer.GridCell
         * @see AccordionDrawer.Tree
         * @see AccordionDrawer.TreeNode
         */
    public void doFlash() {
        if (noflash || keepDrawing()) return;
        doingFlash = true;
        canvas.display();
        doingFlash = false;
    }

    public void drawNextFancy(CellGeom g) {
        return;
    }

    /**
	 * Return the number of split lines in the partitioned list for the specified axis
	 * 
	 * @param xy
	 * @return
	 */
    public int getBlocksIn(int xy) {
        if (xy == X) return splitAxis[X].getPartitionedList().size(); else return splitAxis[Y].getPartitionedList().size();
    }

    /**
	 * Executed before the rendering cycle.  Integrates new data received from 
	 * the SWIFT server.
	 * 
	 * @param canvas The OpenGL canvas for LiveRAC
	 */
    protected void preDraw(GLAutoDrawable canvas) {
        datastore.processDataResponses();
    }

    /**
	 * Create the partitioned lists and draw the labels.  
	 */
    protected void drawPreNewFrame() {
        countDrawnFrame = 0;
        countDrawnScene = 0;
        cumFrameTime = 0;
        splitAxis[Y].makePixelRanges(frameNum);
        splitAxis[X].makePixelRanges(frameNum);
        drawMetricLabels();
        drawDeviceLabels();
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glEnable(GL.GL_DEPTH_TEST);
        int xSize = winsize[X];
        int ySize = winsize[Y];
        if (takeSnapshot) {
            try {
                snapShotWriter.write("%!PS-Adobe-2.0 EPSF-2.0\n" + "%%BoundingBox: 0 0 " + xSize + " " + ySize + "\n" + "%%Magnification: 1.0000\n" + "%%EndComments\n" + "gsave\n" + "newpath " + "0 " + ySize + " moveto " + "0 0 lineto " + xSize + " 0 lineto " + xSize + " " + ySize + " lineto " + "closepath " + "clip newpath\n" + "1 -1 scale\n" + "0 -" + ySize + " translate\n");
            } catch (IOException ioe) {
                System.out.println("Error: unable to write to file: " + snapShotWriter.toString());
            }
        }
    }

    /**
	 * Draw a formatted label box.  
	 * 
	 * @param name
	 * @param fontheight
	 * @param cellMin
	 * @param cellMax
	 * @param centerX
	 */
    public void drawLabelBox(String name, int fontheight, double[] cellMin, double[] cellMax, boolean centerX) {
        int X = 0;
        int Y = 1;
        if (!horiz) {
            X = 1;
            Y = 0;
        }
        int cellMinPix[] = { w2s(cellMin[X], X), w2s(cellMin[Y], Y) };
        int cellMaxPix[] = { w2s(cellMax[X], X), w2s(cellMax[Y], Y) };
        int diff[] = { cellMaxPix[X] - cellMinPix[X], cellMaxPix[Y] - cellMinPix[Y] };
        int namewidth = AccordionDrawer.stringWidth(name, fontheight);
        while (namewidth > diff[X] - 3 && name.length() > 0) {
            name = name.substring(0, name.length() / 2);
            namewidth = AccordionDrawer.stringWidth(name, fontheight);
        }
        if (name.length() > 0 && fontheight <= (diff[Y] - 3)) {
            double pos[] = { centerX ? s2w(cellMinPix[X] + (diff[X] / 2.0) - (namewidth / 2.0), X) : s2w(cellMinPix[X], X), s2w(cellMinPix[Y] + (diff[Y] / 2.0f) + (fontheight / 2.0f), Y) };
            drawText(pos[X], pos[Y], name, fontheight, getLabelColor(), getLabelplane(), getLabelBackColor());
        } else {
        }
    }

    /**
	 * Draws a 3d shaded label box that simulates a Java Metal button
	 * appearance.
	 * 
	 * @see drawColumnLabel
	 * @see drawRowLabel
	 * 
	 * @param name
	 *            Name to be drawn on the label
	 * @param fontheight
	 *            Font size to be used
	 * @param cellMin
	 *            World space coordinates indicating top left corner of cell
	 * @param cellMax
	 *            World space coordinates indicating bottom right corner of cell
	 * @param centerX
	 *            Whether to center in the X coordinate
	 * @param flash
	 *            Whether the label box is being 'flashed', eg. on a mouseover
	 *            event
	 * @param borders
	 *            An object defining which borders should be drawn. (Pass in
	 *            null for no borders)
	 */
    public void drawFancyLabelBox(String name, int fontheight, double[] cellMin, double[] cellMax, boolean centerX, boolean flash, BorderObject borders) {
        int cellMinPix[] = { w2s(cellMin[X], X), w2s(cellMin[Y], Y) };
        int cellMaxPix[] = { w2s(cellMax[X], X), w2s(cellMax[Y], Y) };
        final double labelPlane = getLabelplane();
        final double buttonPlane = labelPlane - 0.1;
        int diffPixels[] = { cellMaxPix[X] - cellMinPix[X], cellMaxPix[Y] - cellMinPix[Y] };
        double diffWorld[] = { cellMax[X] - cellMin[X], cellMax[Y] - cellMin[Y] };
        final Color newLightBlue = new Color(225, 235, 244);
        final Color newDarkBlue = new Color(190, 211, 231);
        gl.glPushAttrib(GL.GL_CURRENT_BIT);
        gl.glShadeModel(GL.GL_SMOOTH);
        gl.glBegin(GL.GL_QUADS);
        setColorGL(Color.WHITE);
        gl.glVertex3d(cellMin[X], cellMin[Y] + (diffWorld[Y] * .25), buttonPlane);
        gl.glVertex3d(cellMax[X], cellMin[Y] + (diffWorld[Y] * .25), buttonPlane);
        setColorGL(newLightBlue);
        gl.glVertex3d(cellMax[X], cellMin[Y], buttonPlane);
        gl.glVertex3d(cellMin[X], cellMin[Y], buttonPlane);
        setColorGL(newDarkBlue);
        gl.glVertex3d(cellMin[X], cellMax[Y], buttonPlane);
        gl.glVertex3d(cellMax[X], cellMax[Y], buttonPlane);
        setColorGL(Color.WHITE);
        gl.glVertex3d(cellMax[X], cellMin[Y] + (diffWorld[Y] * .25), buttonPlane);
        gl.glVertex3d(cellMin[X], cellMin[Y] + (diffWorld[Y] * .25), buttonPlane);
        gl.glEnd();
        gl.glPopAttrib();
        final double[] onePixWorld = { s2w(1, X), s2w(1, Y) };
        final double[] tCellMin = { cellMin[X] + onePixWorld[X], cellMin[Y] + onePixWorld[Y] };
        final double[] tCellMax = { cellMax[X] - onePixWorld[X], cellMax[Y] - onePixWorld[Y] };
        if (borders != null) {
            gl.glPushAttrib(GL.GL_CURRENT_BIT);
            gl.glPushAttrib(GL.GL_LINE_BIT);
            gl.glLineWidth(1.0f);
            if (!flash) {
                setColorGL(borders.lineColor);
                gl.glBegin(GL.GL_LINES);
                if (borders.bottomBorder == true) {
                    gl.glVertex3d(tCellMin[X], tCellMax[Y], buttonPlane);
                    gl.glVertex3d(tCellMax[X], tCellMax[Y], buttonPlane);
                }
                if (borders.topBorder == true) {
                    gl.glVertex3d(tCellMin[X], tCellMin[Y], buttonPlane);
                    gl.glVertex3d(tCellMax[X], tCellMin[Y], buttonPlane);
                }
                if (borders.leftBorder == true) {
                    gl.glVertex3d(tCellMin[X], tCellMin[Y], buttonPlane);
                    gl.glVertex3d(tCellMin[X], tCellMax[Y], buttonPlane);
                }
                if (borders.rightBorder == true) {
                    gl.glVertex3d(tCellMax[X], tCellMin[Y], buttonPlane);
                    gl.glVertex3d(tCellMax[X], tCellMax[Y], buttonPlane);
                }
                gl.glEnd();
                gl.glPopAttrib();
                gl.glPopAttrib();
            } else {
                final Color selectedColor = new Color(184, 207, 229);
                gl.glBegin(GL.GL_LINES);
                for (int i = 0; i < 3; i++) {
                    if (i != 1) setColorGL(selectedColor); else setColorGL(borders.lineColor);
                    if (borders.bottomBorder == true) {
                        gl.glVertex3d(tCellMin[X] + (i * onePixWorld[X]), tCellMax[Y] - (i * onePixWorld[Y]), buttonPlane);
                        gl.glVertex3d(tCellMax[X] - (i * onePixWorld[X]), tCellMax[Y] - (i * onePixWorld[Y]), buttonPlane);
                    }
                    if (borders.topBorder == true) {
                        gl.glVertex3d(tCellMin[X] + (i * onePixWorld[X]), tCellMin[Y] + (i * onePixWorld[Y]), buttonPlane);
                        gl.glVertex3d(tCellMax[X] - (i * onePixWorld[X]), tCellMin[Y] + (i * onePixWorld[Y]), buttonPlane);
                    }
                    if (borders.leftBorder == true) {
                        gl.glVertex3d(tCellMin[X] + (i * onePixWorld[X]), tCellMin[Y] + (i * onePixWorld[Y]), buttonPlane);
                        gl.glVertex3d(tCellMin[X] + (i * onePixWorld[X]), tCellMax[Y] - (i * onePixWorld[Y]), buttonPlane);
                    }
                    if (borders.rightBorder == true) {
                        gl.glVertex3d(tCellMax[X] - (i * onePixWorld[X]), tCellMin[Y] + (i * onePixWorld[Y]), buttonPlane);
                        gl.glVertex3d(tCellMax[X] - (i * onePixWorld[X]), tCellMax[Y] - (i * onePixWorld[Y]), buttonPlane);
                    }
                }
                gl.glEnd();
            }
        }
        int namewidth = AccordionDrawer.stringWidth(name, fontheight);
        while (namewidth > diffPixels[X] - 3 && name.length() > 0) {
            name = name.substring(0, name.length() / 2);
            namewidth = AccordionDrawer.stringWidth(name, fontheight);
        }
        if (name.length() > 0 && fontheight <= (diffPixels[Y] - 3)) {
            double pos[] = { centerX ? s2w(cellMinPix[X] + (diffPixels[X] / 2.0) - (namewidth / 2.0), X) : s2w(cellMinPix[X], X), s2w(cellMinPix[Y] + (diffPixels[Y] / 2.0f) + (fontheight / 2.0f), Y) };
            drawText(pos[X], pos[Y], name, fontheight, getLabelColor(), labelPlane, null);
        }
    }

    /**
	 * Draw the column labels at the top of the asd frame. 
	 * 
	 * @param xMin World space starting x coordinate 
	 * @param xMax World space maximum x coordinate
	 */
    private void drawMetricLabels() {
        if (getListOfMetrics().size() == 0) return;
        Iterator it = getListOfMetrics().iterator();
        SplitLine colSplit;
        SplitLine[] bounds = null;
        int splitIndex = 0;
        double xMin, xMax;
        while (it.hasNext()) {
            colSplit = (SplitLine) it.next();
            bounds = metricAxis.getBounds(colSplit);
            splitIndex = metricAxis.getSplitIndex(bounds[SplitAxis.minBound]) + 1;
            xMin = metricAxis.getAbsoluteValue(bounds[SplitAxis.minBound], frameNum);
            xMax = metricAxis.getAbsoluteValue(bounds[SplitAxis.maxBound], frameNum);
            int[] fontHeight = { getMinFontHeight(), getMaxFontHeight() };
            double cellMin[] = { xMin, 0.001f };
            double cellMax[] = { xMax, splitAxis[Y].getMinStuckValue() };
            drawFancyLabelBox(Device.getMetricName(splitIndex), fontHeight[1], cellMin, cellMax, true, false, new BorderObject(true, true, true, true, new Color(122, 138, 153)));
        }
    }

    /**
	 * Given an index value, return the mod 7 of that value as a color
	 * 
	 * @param colorIndex
	 * @return a color that will be different from the previous.
	 */
    private static final Color nextColor(int colorIndex) {
        switch(colorIndex % 7) {
            case 0:
                return Color.BLACK;
            case 1:
                return Color.ORANGE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.BLUE;
            case 5:
                return Color.GRAY;
            case 6:
                return Color.CYAN;
            default:
                return Color.PINK;
        }
    }

    /**
	 * Draws all of the row labels
	 * 
	 */
    private void drawDeviceLabels() {
        if (getListOfDevices().size() == 0) {
            return;
        }
        double[] custColMin = { 0.00001f, 0f };
        double[] custColMax = { 0.009999f, 0f };
        Color custColColor = null;
        int colorIndex = 1;
        double labelMin[] = { 0.01f, 0.0 };
        double labelMax[] = { splitAxis[X].getMinStuckValue(), 0.0 };
        Iterator iter = getListOfDevices().iterator();
        Device currDev;
        String deviceName;
        String prevCustName = null;
        String custName = null;
        while (iter.hasNext()) {
            SplitLine sl = (SplitLine) iter.next();
            SplitLine[] devBounds = splitAxis[Y].getBounds(sl);
            currDev = (Device) devBounds[SplitAxis.minBound].getRowObject();
            deviceName = currDev.getName();
            custName = currDev.getCust();
            if (!custName.equals(prevCustName)) {
                colorIndex++;
                custColColor = nextColor(colorIndex);
            }
            labelMin[Y] = splitAxis[Y].getAbsoluteValue(devBounds[SplitAxis.minBound], frameNum);
            labelMax[Y] = splitAxis[Y].getAbsoluteValue(devBounds[SplitAxis.maxBound], frameNum);
            custColMin[Y] = labelMin[Y];
            custColMax[Y] = labelMax[Y];
            drawLabelDiffCol(custColColor, custColMin, custColMax);
            drawRowLabel(deviceName + " : " + custName, labelMin, labelMax, false);
            prevCustName = custName;
        }
    }

    /**
	 * Draw color swatches
	 * @param color
	 * @param cellMin
	 * @param cellMax
	 */
    private void drawLabelDiffCol(Color color, double cellMin[], double cellMax[]) {
        final double labelPlane = getLabelplane();
        gl.glBegin(GL.GL_QUADS);
        setColorGL(color);
        gl.glVertex3d(cellMin[X], cellMin[Y], labelPlane);
        gl.glVertex3d(cellMax[X], cellMin[Y], labelPlane);
        gl.glVertex3d(cellMax[X], cellMax[Y], labelPlane);
        gl.glVertex3d(cellMin[X], cellMax[Y], labelPlane);
        gl.glEnd();
    }

    /**
	 * Draws a row label 'button' 
	 * 
	 * @link drawRowLabels()
	 * @param devName
	 *            String label of button.
	 * @param cellMin
	 *            World space top left corner
	 * @param cellMax
	 *            World space bottom right corner
	 * @param flash
	 *            Whether a mouseover event is happening now
	 */
    private void drawRowLabel(String devName, double[] cellMin, double[] cellMax, boolean flash) {
        int[] fontHeight = { getMinFontHeight(), getMaxFontHeight() };
        int maxNameWidth = (int) (0.3 * getWinsize(X));
        int nameWidth = AccordionDrawer.stringWidth(devName, getMaxFontHeight());
        if (nameWidth > maxNameWidth) {
            int nameResidue = nameWidth - maxNameWidth;
            int charsToChop = (int) ((double) nameResidue / (double) nameWidth * devName.length());
            if (charsToChop <= devName.length() && charsToChop >= 0) devName = devName.substring(0, devName.length() - charsToChop); else devName = "";
        }
        if (drawlabels && devName.length() > 0) {
            drawFancyLabelBox(devName, fontHeight[1], cellMin, cellMax, true, flash, new BorderObject(true, true, true, true, new Color(122, 138, 153)));
        }
    }

    protected void drawPreContFrame() {
        countDrawnFrame = 0;
    }

    protected void drawPostScene() {
        gl.glDisable(GL.GL_DEPTH_TEST);
        if (takeSnapshot) {
            try {
                snapShotWriter.write("grestore\n" + "showpage\n");
                takeSnapshot = false;
                snapShotWriter.close();
            } catch (IOException ioe) {
                System.out.println("Error: can not write to file: " + snapShotWriter);
            }
        }
    }

    public int getLabelBuffer(int xy) {
        return labelbuffer[xy];
    }

    public void setLabelBuffer(int buffer) {
        if (buffer < 1) buffer = 1;
        labelbuffer[X] = buffer;
        labelbuffer[Y] = buffer;
        requestRedraw();
    }

    public void setLabelBuffer(int buffer, int xy) {
        if (buffer < 1) buffer = 1;
        labelbuffer[xy] = buffer;
        requestRedraw();
    }

    public void increaseLabelBuffer(int xy) {
        setLabelBuffer(labelbuffer[xy] + 1, xy);
    }

    public void increaseLabelBuffer() {
        setLabelBuffer(labelbuffer[X] + 1);
    }

    public void decreaseLabelBuffer(int xy) {
        setLabelBuffer(labelbuffer[xy] - 1, xy);
    }

    public void decreaseLabelBuffer() {
        setLabelBuffer(labelbuffer[X] - 1);
    }

    public int getMaxFontHeight() {
        return maxFontHeight;
    }

    public void setMaxFontHeight(int fontheight) {
        if (fontheight < 1) fontheight = 1;
        maxFontHeight = fontheight;
        requestRedraw();
    }

    public void increaseMaxFontHeight() {
        setMaxFontHeight(maxFontHeight + 1);
    }

    public void decreaseMaxFontHeight() {
        setMaxFontHeight(maxFontHeight - 1);
    }

    public int getMinFontHeight() {
        return minFontHeight;
    }

    public void setMinFontHeight(int fontheight) {
        if (fontheight < 1) fontheight = 1;
        minFontHeight = fontheight;
        requestRedraw();
    }

    public void increaseMinFontHeight() {
        setMinFontHeight(minFontHeight + 1);
    }

    public void decreaseMinFontHeight() {
        setMinFontHeight(minFontHeight - 1);
    }

    public void addToDrawQueue(Object r) {
        if (r != null) ToDrawQ.add(r);
    }

    /**
	 * Return the sizes of the underlying splitline hierarchies
	 */
    public int getBottomGrid(boolean horizontal) {
        if (horizontal) {
            if (splitAxis[X] == null) return 0;
            return splitAxis[X].getSize();
        } else {
            if (splitAxis[Y] == null) return 0;
            return splitAxis[Y].getSize();
        }
    }

    /**
	 * @return Returns the listOfMetrics.
	 */
    public TreeSet getListOfMetrics() {
        return splitAxis[X].getPartitionedList();
    }

    /**
	 * @return Returns the listOfDevices.
	 */
    public TreeSet getListOfDevices() {
        return splitAxis[Y].getPartitionedList();
    }

    /**
	 * One component of the byzantian flash drawing code.  Flash drawing is the process
	 * for drawing the region underneath the mouse cursor.  
	 */
    public void flashDraw() {
        final double[] nodeDrawingRange = { 0.0, 1.0 };
        boolean doubleBufferedOriginal = getDoubleBuffer();
        setDoubleBuffer(false);
        final int flashBoxBuffer = 4;
        if (doBox && flashGeom != null) {
            flashBox = makeBox(flashGeom);
        }
        if (doBox && flashBoxOld != null) {
            flashBoxOld.undraw();
        }
        if (flashGeom != null && flashBoxOld != null) {
            double x = s2w(flashBoxOld.getMin(X) - flashBoxBuffer, X);
            double y = s2w(flashBoxOld.getMax(Y) + flashBoxBuffer, Y);
            if (x < 0) x = 0;
            gl.glRasterPos3d(x, y, getLabelplane());
            gl.glDrawPixels(drawnlabelsize[0], drawnlabelsize[1], GL.GL_RGB, GL.GL_FLOAT, pixels);
        }
        if (null != flashGeom) {
            drawnlabelpos[X] = flashBox.getMin(X) - flashBoxBuffer;
            if (drawnlabelpos[X] < 0) drawnlabelpos[X] = 0;
            drawnlabelsize[X] = flashBox.getMax(X) - drawnlabelpos[X] + flashBoxBuffer;
            drawnlabelsize[Y] = flashBox.getMax(Y) - flashBox.getMin(Y) + 2 * flashBoxBuffer;
            drawnlabelpos[Y] = flashBox.getMax(Y) + flashBoxBuffer;
            if (debugOutput) {
                if (drawnlabelsize[X] <= 0) System.out.println("Invalid drawnlabelsize[X]=" + drawnlabelsize[X] + " flashbox:" + flashBox);
                if (drawnlabelsize[Y] <= 0) System.out.println("Invalid drawnlabelsize[Y]=" + drawnlabelsize[Y] + " flashbox:" + flashBox);
            }
            pixels = FloatBuffer.allocate(drawnlabelsize[X] * drawnlabelsize[Y] * 3);
            gl.glReadBuffer(GL.GL_FRONT);
            gl.glReadPixels(drawnlabelpos[X], getWinsize(Y) - drawnlabelpos[Y], drawnlabelsize[X], drawnlabelsize[Y], GL.GL_RGB, GL.GL_FLOAT, pixels);
            SplitLine[] devLines = { flashBox.getMinLine(Y), flashBox.getMaxLine(Y) };
            SplitLine[] metricLines = { flashBox.getMinLine(X), flashBox.getMaxLine(X) };
            double cellMin[] = { splitAxis[X].getAbsoluteValue(metricLines[0], getFrameNum()), splitAxis[Y].getAbsoluteValue(devLines[0], getFrameNum()) };
            double cellMax[] = { splitAxis[X].getAbsoluteValue(metricLines[1], getFrameNum()), splitAxis[Y].getAbsoluteValue(devLines[1], getFrameNum()) };
            gl.glBegin(GL.GL_LINE_LOOP);
            gl.glVertex3d(cellMin[X], cellMax[Y], interactionplane);
            gl.glVertex3d(cellMax[X], cellMax[Y], interactionplane);
            gl.glVertex3d(cellMax[X], cellMin[Y], interactionplane);
            gl.glVertex3d(cellMin[X], cellMin[Y], interactionplane);
            gl.glEnd();
        }
        if (doBox && flashBox != null) {
            flashBox.draw(rubberbandColor, flashBoxWidth, interactionplane);
        }
        if (doBox) flashBoxOld = flashBox;
        flashGeomOld = flashGeom;
        flashXOld = flashX;
        flashYOld = flashY;
        setDoubleBuffer(doubleBufferedOriginal);
    }

    /**
	 * Use this function to flush cached splitline objects for example the flash geom
	 * interaction box, rubber  band etc.  this needs to be called before objects 
	 * are removed from the split line hierarchy, and might also be a good idea when things
	 * are being moved around inside of it or added. 
	 *
	 */
    public void flushSplitCaches() {
        setFlashBox(null);
        setFlashBoxOld(null);
        setFlashGeom(null);
        setFlashGeomOld(null);
    }

    /**
	 * Find a device based on an index number.  Generally you should have 
	 * the SplitLine and not an index number so this call should be avoided.  
	 * O (log n) 
	 * 
	 * @deprecated
	 */
    public Device getDevice(int deviceNum) {
        SplitLine deviceLine = splitAxis[Y].getSplitFromIndex(deviceNum);
        return (Device) deviceLine.rowObject;
    }

    /**
	 * Unused
	 * 
	 * @deprecated
	 */
    public OlduvaiObject getOlduvaiObject() {
        return null;
    }

    protected void drawPostFrame() {
        datastore.timeChanged = false;
    }

    /**
	 * Unimplemented
	 * @deprecated
	 */
    protected void drawBruteForce() {
    }

    /**
	 * Unimplemented
	 * @deprecated
	 */
    public void resetSplitValues() {
    }

    /**
	 * Unimplemented
	 * @deprecated
	 */
    public void subpixelDraw(GridCell c) {
    }

    /**
	 * 		// do nothing, no site nodes are in the seeded drawing queue
	 * @deprecated
	 */
    public void drawGeom(CellGeom cg) {
    }

    /**
	 * @return Returns the seededGroups.
	 */
    public Groups getSeededGroups() {
        return seededGroups;
    }

    public void setFlashGeom(CellGeom newFlashGeom) {
        flashGeom = newFlashGeom;
    }

    public void setFlashGeomOld(CellGeom flashGeomOld) {
        this.flashGeomOld = flashGeomOld;
    }
}
