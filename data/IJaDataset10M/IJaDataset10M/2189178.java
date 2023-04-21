package org.ujac.chart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;

/**
 * Name: DonutChart3D<br>
 * Description: A class rendering 3D donut charts.
 * 
 * @author lauerc
 */
public class DonutChart3D extends BasePieChart3D {

    /** Constant for the attribute 'hole-size'. */
    String ATTR_HOLE_SIZE = "hole-size";

    /** The default hole size. */
    public static final float DEFAULT_HOLE_SIZE = 40.0F;

    /** The half height of a slice. */
    private double halfSliceHeight = 0.0;

    /** The 'quarter' height of a slice. */
    private double quarterSliceHeight = 0.0;

    /** The hole size. */
    private double holeSize = 0.0;

    /** The hole height. */
    private double holeHeight = 0.0;

    /** The half hole height. */
    private double halfHoleHeight = 0.0;

    /** The hole width. */
    private double holeWidth = 0.0;

    /** The half hole width. */
    private double halfHoleWidth = 0.0;

    /** The outer pie width. */
    private double outerPieHeight = 0.0;

    /** The outer width. */
    private double outerPieWidth = 0.0;

    /** The half outer pie width. */
    private double halfOuterPieHeight = 0.0;

    /** The half outer width. */
    private double halfOuterPieWidth = 0.0;

    /** The quarter outer pie width. */
    private double quarterOuterPieHeight = 0.0;

    /** The quarter outer width. */
    private double quarterOuterPieWidth = 0.0;

    /** The half quarter outer pie width. */
    private double quarterHalfOuterPieHeight = 0.0;

    /** The half quarter outer width. */
    private double quarterHalfOuterPieWidth = 0.0;

    /** The inner pie width. */
    private double innerPieHeight = 0.0;

    /** The inner width. */
    private double innerPieWidth = 0.0;

    /** The half inner pie width. */
    private double halfInnerPieHeight = 0.0;

    /** The half inner width. */
    private double halfInnerPieWidth = 0.0;

    /** The quarter inner pie width. */
    private double quarterInnerPieHeight = 0.0;

    /** The quarter inner width. */
    private double quarterInnerPieWidth = 0.0;

    /** The half quarter inner pie width. */
    private double quarterHalfInnerPieHeight = 0.0;

    /** The half quarter inner width. */
    private double quarterHalfInnerPieWidth = 0.0;

    /**
   * Gets the hole size.
   * @return The current hole size.
   */
    public float getHoleSize() {
        if (attributes.isDefined(ATTR_HOLE_SIZE)) {
            return attributes.getFloat(ATTR_HOLE_SIZE);
        }
        return DEFAULT_HOLE_SIZE;
    }

    /**
   * Gets the number of drawing steps to perform to draw the slices.
   * @return The number of drawing steps.
   */
    protected int getNumDrawingSteps() {
        return 5;
    }

    /**
   * Gets the index of the step before which to draw the chart
   * description.
   * @return The index of the step before which to draw the description.
   */
    protected int getDescriptionDrawingStep() {
        return 2;
    }

    /**
   * Initializes the data structures for the chart metrics.
   * @param numSegments The number of metrics for which to 
   */
    protected void init(int numSegments) {
        super.init(numSegments);
        this.halfSliceHeight = getSliceHeight() * 0.5;
        this.quarterSliceHeight = halfSliceHeight * 0.25;
        this.holeSize = getHoleSize();
        this.halfHoleHeight = halfPieHeight * holeSize / 100.0;
        this.holeHeight = halfHoleHeight * 2.0F;
        this.halfHoleWidth = (float) (halfHoleHeight / getDeltaY());
        this.holeWidth = halfHoleWidth * 2.0F;
        double halfOuterFactor = 1.0925;
        this.outerPieWidth = getPieSize().getWidth() * halfOuterFactor;
        this.outerPieHeight = getPieSize().getHeight() * halfOuterFactor;
        this.halfOuterPieWidth = halfPieWidth * halfOuterFactor;
        this.halfOuterPieHeight = halfPieHeight * halfOuterFactor;
        double quarterOuterFactor = 1.0625;
        this.quarterOuterPieWidth = getPieSize().getWidth() * quarterOuterFactor;
        this.quarterOuterPieHeight = getPieSize().getHeight() * quarterOuterFactor;
        this.quarterHalfOuterPieWidth = halfPieWidth * quarterOuterFactor;
        this.quarterHalfOuterPieHeight = halfPieHeight * quarterOuterFactor;
        double innerFactor = 1.25;
        this.innerPieWidth = holeWidth / innerFactor;
        this.innerPieHeight = holeHeight / innerFactor;
        this.halfInnerPieWidth = halfHoleWidth / innerFactor;
        this.halfInnerPieHeight = halfHoleHeight / innerFactor;
        double quarterInnerFactor = 0.82;
        this.quarterInnerPieWidth = holeWidth * quarterInnerFactor;
        this.quarterInnerPieHeight = holeHeight * quarterInnerFactor;
        this.quarterHalfInnerPieWidth = halfHoleWidth * quarterInnerFactor;
        this.quarterHalfInnerPieHeight = halfHoleHeight * quarterInnerFactor;
    }

    /**
   * Draws a pie slice.
   * @param graphics The graphics object to draw width.
   * @param color The slice's color.
   * @param sliceIdx The index of the slice to draw.
   * @param stepIdx The index of the drawing step to perform.
   */
    protected void drawSlice(Graphics2D graphics, Color color, int sliceIdx, int stepIdx) {
        boolean fill = true;
        double startAngle = startAngles[sliceIdx];
        double endAngle = endAngles[sliceIdx];
        double angleDiff = angleDiffs[sliceIdx];
        double startArc = startArcs[sliceIdx];
        double endArc = endArcs[sliceIdx];
        double centerX = getPieCenter().getX();
        double centerY = getPieCenter().getY();
        double pieWidth = getPieSize().getWidth();
        double pieHeight = getPieSize().getHeight();
        double sliceHeight = getSliceHeight();
        Line2D.Double startLine = new Line2D.Double(0, halfPieHeight, 0, halfHoleHeight);
        AffineTransform rotate = new AffineTransform();
        rotate.rotate(-startArc);
        PathIterator pIterStart = startLine.getPathIterator(rotate);
        double[] coordFrom = new double[2];
        double[] holeFrom = new double[2];
        pIterStart.currentSegment(coordFrom);
        pIterStart.next();
        pIterStart.currentSegment(holeFrom);
        double deltaY = getDeltaY();
        coordFrom[0] /= deltaY;
        holeFrom[0] /= deltaY;
        rotate = new AffineTransform();
        rotate.rotate(-endArc);
        PathIterator pIterEnd = startLine.getPathIterator(rotate);
        double[] coordTo = new double[2];
        double[] holeTo = new double[2];
        pIterEnd.currentSegment(coordTo);
        pIterEnd.next();
        pIterEnd.currentSegment(holeTo);
        coordTo[0] /= deltaY;
        holeTo[0] /= deltaY;
        if (stepIdx == 0) {
            graphics.setColor(color.darker());
            GeneralPath path = null;
            path = new GeneralPath();
            path.append(new Arc2D.Double(-quarterHalfOuterPieWidth, -quarterHalfOuterPieHeight + sliceHeight - quarterSliceHeight, quarterOuterPieWidth, quarterOuterPieHeight, startAngle, angleDiff, Arc2D.OPEN), true);
            path.append(new Arc2D.Double(-halfPieWidth, -halfPieHeight + sliceHeight, pieWidth, pieHeight, endAngle, -angleDiff, Arc2D.OPEN), true);
            path.transform(moveToCenter);
            if (fill) {
                graphics.fill(path);
            } else {
                graphics.draw(path);
            }
            path = new GeneralPath();
            path.append(new Arc2D.Double(-quarterHalfInnerPieWidth, -quarterHalfInnerPieHeight + sliceHeight - quarterSliceHeight, quarterInnerPieWidth, quarterInnerPieHeight, startAngle, angleDiff, Arc2D.OPEN), true);
            path.append(new Arc2D.Double(-halfHoleWidth, -halfHoleHeight + sliceHeight, holeWidth, holeHeight, endAngle, -angleDiff, Arc2D.OPEN), true);
            path.transform(moveToCenter);
            if (fill) {
                graphics.fill(path);
            } else {
                graphics.draw(path);
            }
        } else if (stepIdx == 1) {
            graphics.setColor(color.darker());
            AffineTransform move = new AffineTransform();
            move.translate(centerX, centerY);
            GeneralPath path = null;
            path = new GeneralPath();
            path.append(new Arc2D.Double(-quarterHalfOuterPieWidth, -quarterHalfOuterPieHeight + sliceHeight - quarterSliceHeight, quarterOuterPieWidth, quarterOuterPieHeight, startAngle, angleDiff, Arc2D.OPEN), true);
            path.append(new Arc2D.Double(-halfOuterPieWidth, -halfOuterPieHeight + halfSliceHeight, outerPieWidth, outerPieHeight, endAngle, -angleDiff, Arc2D.OPEN), true);
            path.transform(moveToCenter);
            if (fill) {
                graphics.fill(path);
            } else {
                graphics.draw(path);
            }
            path = new GeneralPath();
            path.append(new Arc2D.Double(-quarterHalfInnerPieWidth, -quarterHalfInnerPieHeight + sliceHeight - quarterSliceHeight, quarterInnerPieWidth, quarterInnerPieHeight, startAngle, angleDiff, Arc2D.OPEN), true);
            path.append(new Arc2D.Double(-halfInnerPieWidth, -halfInnerPieHeight + halfSliceHeight, innerPieWidth, innerPieHeight, endAngle, -angleDiff, Arc2D.OPEN), true);
            path.transform(moveToCenter);
            if (fill) {
                graphics.fill(path);
            } else {
                graphics.draw(path);
            }
        } else if (stepIdx == 2) {
            graphics.setColor(color.darker());
            AffineTransform move = new AffineTransform();
            move.translate(centerX, centerY);
            GeneralPath path = null;
            path = new GeneralPath();
            path.append(new Arc2D.Double(-quarterHalfOuterPieWidth, -quarterHalfOuterPieHeight + quarterSliceHeight, quarterOuterPieWidth, quarterOuterPieHeight, startAngle, angleDiff, Arc2D.OPEN), true);
            path.append(new Arc2D.Double(-halfOuterPieWidth, -halfOuterPieHeight + halfSliceHeight, outerPieWidth, outerPieHeight, endAngle, -angleDiff, Arc2D.OPEN), true);
            path.transform(moveToCenter);
            if (fill) {
                graphics.fill(path);
            } else {
                graphics.draw(path);
            }
            path = new GeneralPath();
            path.append(new Arc2D.Double(-halfInnerPieWidth, -halfInnerPieHeight + halfSliceHeight, innerPieWidth, innerPieHeight, startAngle, angleDiff, Arc2D.OPEN), true);
            path.append(new Arc2D.Double(-quarterHalfInnerPieWidth, -quarterHalfInnerPieHeight + quarterSliceHeight, quarterInnerPieWidth, quarterInnerPieHeight, endAngle, -angleDiff, Arc2D.OPEN), true);
            path.transform(moveToCenter);
            if (fill) {
                graphics.fill(path);
            } else {
                graphics.draw(path);
            }
        } else if (stepIdx == 3) {
            graphics.setColor(color.darker());
            AffineTransform move = new AffineTransform();
            move.translate(centerX, centerY);
            GeneralPath path = null;
            path = new GeneralPath();
            path.append(new Arc2D.Double(-quarterHalfOuterPieWidth, -quarterHalfOuterPieHeight + quarterSliceHeight, quarterOuterPieWidth, quarterOuterPieHeight, startAngle, angleDiff, Arc2D.OPEN), true);
            path.append(new Arc2D.Double(-halfPieWidth, -halfPieHeight, pieWidth, pieHeight, endAngle, -angleDiff, Arc2D.OPEN), true);
            path.transform(moveToCenter);
            graphics.draw(path);
            if (fill) {
                graphics.fill(path);
            } else {
                graphics.draw(path);
            }
            path = new GeneralPath();
            path.append(new Arc2D.Double(-quarterHalfInnerPieWidth, -quarterHalfInnerPieHeight + quarterSliceHeight, quarterInnerPieWidth, quarterInnerPieHeight, startAngle, angleDiff, Arc2D.OPEN), true);
            path.append(new Arc2D.Double(-halfHoleWidth, -halfHoleHeight, holeWidth, holeHeight, endAngle, -angleDiff, Arc2D.OPEN), true);
            path.transform(moveToCenter);
            if (fill) {
                graphics.fill(path);
            } else {
                graphics.draw(path);
            }
        } else {
            GeneralPath path = new GeneralPath();
            path.append(new Arc2D.Double(-halfPieWidth, -halfPieHeight, pieWidth, pieHeight, startAngle, angleDiff, Arc2D.OPEN), true);
            path.append(new Line2D.Double(coordTo[0], coordTo[1], holeTo[0], holeTo[1]), true);
            path.append(new Arc2D.Double(-halfHoleWidth, -halfHoleHeight, holeWidth, holeHeight, endAngle, -angleDiff, Arc2D.OPEN), true);
            path.append(new Line2D.Double(holeFrom[0], holeFrom[1], coordFrom[0], coordFrom[1]), true);
            AffineTransform move = new AffineTransform();
            move.translate(centerX, centerY);
            path.transform(moveToCenter);
            graphics.setColor(color);
            if (fill) {
                graphics.fill(path);
            } else {
                graphics.draw(path);
            }
        }
    }

    /**
   * Draws the shadow of a pie slice.
   * @param graphics The graphics object to draw width.
   * @param color The slice's color.
   * @param sliceIdx The index of the slice to draw.
   */
    protected void drawSliceShadow(Graphics2D graphics, Color color, int sliceIdx) {
        double startAngle = startAngles[sliceIdx];
        double endAngle = endAngles[sliceIdx];
        double angleDiff = angleDiffs[sliceIdx];
        double startArc = startArcs[sliceIdx];
        double endArc = endArcs[sliceIdx];
        double centerX = getPieCenter().getX();
        double centerY = getPieCenter().getY();
        double sliceHeight = getSliceHeight();
        AffineTransform rotate = new AffineTransform();
        rotate.rotate(-startArc);
        rotate = new AffineTransform();
        rotate.rotate(-endArc);
        GeneralPath path = new GeneralPath();
        path.append(new Arc2D.Double(-halfOuterPieWidth, -halfOuterPieHeight + sliceHeight, outerPieWidth, outerPieHeight, startAngle, angleDiff, Arc2D.OPEN), true);
        path.append(new Arc2D.Double(-halfInnerPieWidth, -halfInnerPieHeight + sliceHeight, innerPieWidth, innerPieHeight, endAngle, -angleDiff, Arc2D.OPEN), true);
        AffineTransform move = new AffineTransform();
        move.translate(centerX, centerY);
        path.transform(moveToCenter);
        graphics.setColor(color);
        graphics.fill(path);
    }
}
