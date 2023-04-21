package de.tewdreyer.android.quaffed;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class AxisImpl implements Axis {

    private Point start;

    private Point end;

    private int numSections;

    private double mSectionLength;

    private Point mUnitVector;

    private Paint mPaint;

    public AxisImpl(int startX, int startY, int endX, int endY) {
        start = new Point(startX, startY);
        end = new Point(endX, endY);
        numSections = 1;
        mSectionLength = Length() / numSections;
        mUnitVector = new Point((int) ((end.x - start.x) / Length()), (int) ((end.y - start.y) / Length()));
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setColor(0xFF000000);
    }

    public Point getStart() {
        return start;
    }

    public void setStart(int x, int y) {
        this.start = new Point(x, y);
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(int x, int y) {
        this.end = new Point(x, y);
    }

    public int getNumSections() {
        return numSections;
    }

    public void setNumSections(int numSections) {
        this.numSections = numSections;
        this.mSectionLength = Length() / numSections;
    }

    public double Length() {
        return Length(start, end);
    }

    public double Length(Point start, Point end) {
        return Math.sqrt(Math.pow((end.x - start.x), 2) + Math.pow((end.y - start.y), 2));
    }

    public void draw(Canvas canvas) {
        canvas.drawLine((float) start.x, (float) start.y, (float) end.x, (float) end.y, mPaint);
        drawSections(canvas, start, end);
    }

    public void drawSections(Canvas canvas, Point start, Point end) {
        float[] intersectingLines = getIntersectingLines(start, end);
        canvas.drawLines(intersectingLines, mPaint);
    }

    public Point[] getIntersectionPoints() {
        Point[] points = new Point[numSections];
        Log.d("###############", String.format("%s %s %s %s", numSections, mUnitVector.x, mUnitVector.y, mSectionLength));
        for (int i = 0; i < numSections; i++) {
            int x = (int) (start.x + mUnitVector.x * (i + 1) * mSectionLength);
            int y = (int) (start.y + mUnitVector.y * (i + 1) * mSectionLength);
            Log.d("##########", x + " " + y);
            points[i] = new Point(x, y);
        }
        return points;
    }

    public Point[] getSectionMidPoints() {
        Point[] midPoints = getIntersectionPoints();
        for (int i = 0; i < midPoints.length; i++) {
            midPoints[i].x = midPoints[i].x - (int) (mUnitVector.x * mSectionLength / 2);
            midPoints[i].y = midPoints[i].y - (int) (mUnitVector.y * mSectionLength / 2);
        }
        return midPoints;
    }

    private float[] getIntersectingLines(Point start, Point end) {
        float[] lines = new float[numSections * 4];
        int i = 0;
        for (Point p : getIntersectionPoints()) {
            Point o = new Point(-1 * (end.y - start.y), (end.x - start.x));
            Point oe1 = new Point((int) (o.x / Length(new Point(0, 0), o)), (int) (o.y / Length(new Point(0, 0), o)));
            Point oe2 = new Point(-1 * oe1.x, -1 * oe1.y);
            Point t1 = new Point(p.x + oe1.x * 5, p.y + oe1.y * 5);
            Point t2 = new Point(p.x + oe2.x * 5, p.y + oe2.y * 5);
            lines[4 * i] = t1.x;
            lines[4 * i + 1] = t1.y;
            lines[4 * i + 2] = t2.x;
            lines[4 * i + 3] = t2.y;
            i++;
        }
        return lines;
    }
}
