package net.sourceforge.hlm.gui.math.draw;

import java.util.*;
import net.sourceforge.hlm.gui.math.*;
import org.eclipse.swt.graphics.*;

public class ParenthesesView extends ObjectViewImpl implements DrawView {

    public ParenthesesView(ObjectFrame frame, Style left, Style right, int margin) {
        super(frame);
        this.main = this.createFrame();
        this.left = left;
        this.right = right;
        this.margin = margin;
    }

    public ObjectFrame getMain() {
        return this.main;
    }

    public void update() {
        this.width = this.main.getWidth();
        if (this.width < 1) {
            this.width = 1;
        }
        this.parenthesisHeight = this.main.getParenthesesHeight();
        if (this.parenthesisHeight < MIN_HEIGHT) {
            this.parenthesisHeight = MIN_HEIGHT;
        }
        this.heightAbove = this.main.getHeightAbove();
        this.heightBelow = this.main.getHeightBelow();
        if (this.left != null || this.right != null) {
            if (this.heightAbove < this.parenthesisHeight) {
                this.heightAbove = this.parenthesisHeight;
            }
            if (this.heightBelow < this.parenthesisHeight + 1) {
                this.heightBelow = this.parenthesisHeight + 1;
            }
        }
        int parenthesisWidth;
        if (this.parenthesisHeight >= 28) {
            parenthesisWidth = 7;
        } else if (this.parenthesisHeight >= 15) {
            parenthesisWidth = 6;
        } else if (this.parenthesisHeight >= 9) {
            parenthesisWidth = 5;
        } else if (this.parenthesisHeight >= 6) {
            parenthesisWidth = 4;
        } else {
            parenthesisWidth = 3;
        }
        int curlyBraceWidth;
        if (this.parenthesisHeight >= 4) {
            curlyBraceWidth = 6;
        } else {
            curlyBraceWidth = 4;
        }
        boolean hasNormalParentheses = false;
        boolean hasCurlyBraces = false;
        if (this.left == Style.NORMAL) {
            this.width += parenthesisWidth + this.margin;
            this.main.setXOffset(parenthesisWidth + this.margin);
            hasNormalParentheses = true;
        } else if (this.left == Style.CURLY) {
            this.width += curlyBraceWidth + this.margin;
            this.main.setXOffset(curlyBraceWidth + this.margin);
            hasCurlyBraces = true;
        }
        if (this.right == Style.NORMAL) {
            this.width += parenthesisWidth + this.margin;
            hasNormalParentheses = true;
        } else if (this.right == Style.CURLY) {
            this.width += curlyBraceWidth + this.margin;
            hasCurlyBraces = true;
        }
        if (hasNormalParentheses && !normalPoints.containsKey(this.parenthesisHeight)) {
            int[] yOffsets = new int[parenthesisWidth - 2];
            int y = this.parenthesisHeight;
            for (int index = 0; index < yOffsets.length; index++) {
                yOffsets[index] = y;
                int offset = index + 2;
                y -= offset;
                if (index > 0 || parenthesisWidth <= 4) {
                    int x = parenthesisWidth - offset;
                    int divisor = (x + 2) * 4;
                    int maxYOffset = (this.parenthesisHeight * (x * 8 - 9) + divisor - 1) / divisor;
                    if (y > maxYOffset) {
                        if (index > 0) {
                            y = maxYOffset;
                        } else {
                            y--;
                        }
                    }
                }
                if (y < 4) {
                    y = 4;
                }
            }
            int[][] points;
            int[] outerPoints = this.makePoints(yOffsets, 0, parenthesisWidth - 1, 0);
            if (parenthesisWidth > 4) {
                int[] innerPoints = this.makePoints(yOffsets, 1, parenthesisWidth, 1);
                points = new int[][] { outerPoints, innerPoints };
            } else {
                points = new int[][] { outerPoints };
            }
            normalPoints.put(this.parenthesisHeight, points);
        }
        if (hasCurlyBraces && !curlyPoints.containsKey(this.parenthesisHeight)) {
            int extent = curlyBraceWidth / 2 - 1;
            int left = extent;
            int right = left + 1;
            int rightEnd = curlyBraceWidth - 1;
            int rightMid = rightEnd - 1;
            int height = this.parenthesisHeight;
            int innerHeight = height - 1;
            if (extent > this.parenthesisHeight) {
                extent = this.parenthesisHeight;
            }
            if (innerHeight < extent) {
                innerHeight = extent;
            }
            curlyPoints.put(this.parenthesisHeight, new int[][] { new int[] { right, height, left, innerHeight, left, extent, 0, 0, left, -extent, left, -innerHeight, right, -height }, new int[] { rightEnd, height, rightMid, height, right, innerHeight, right, extent, 0, 0, right, -extent, right, -innerHeight, rightMid, -height, rightEnd, -height } });
        }
    }

    private int[] makePoints(int[] yOffsets, int indexStart, int xStart, int yShift) {
        int[] xCoords = new int[yOffsets.length * 2 + 1];
        int[] yCoords = new int[xCoords.length];
        int count = 0;
        int last = this.parenthesisHeight;
        xCoords[count] = xStart - indexStart;
        yCoords[count++] = last;
        for (int index = indexStart; index < yOffsets.length; index++) {
            int nextOffset = yOffsets[index] + yShift;
            int extent = last - nextOffset;
            int maxExtent = (index + 1 < yOffsets.length ? (nextOffset - (yOffsets[index + 1] + yShift) - 1) / 2 : nextOffset);
            if (extent > maxExtent) {
                extent = maxExtent;
                xCoords[count] = xStart - index;
                yCoords[count++] = nextOffset + extent;
            }
            xCoords[count] = xStart - index - 1;
            yCoords[count++] = last = nextOffset - extent - 1;
        }
        int totalCount = 2 * count;
        if (yCoords[count - 1] == 0) {
            totalCount--;
        }
        int[] points = new int[2 * totalCount];
        for (int index = 0; index < count; index++) {
            points[2 * index] = xCoords[index];
            points[2 * index + 1] = yCoords[index];
            points[2 * (totalCount - index - 1)] = xCoords[index];
            points[2 * (totalCount - index - 1) + 1] = -yCoords[index];
        }
        return points;
    }

    @Override
    public int getParenthesesHeight() {
        return this.parenthesisHeight + ADDED_HEIGHT;
    }

    public void draw(GC gc, int x, int y) {
        this.drawPoints(gc, this.left, x, y, false);
        this.drawPoints(gc, this.right, x + this.width - 1, y, true);
    }

    private void drawPoints(GC gc, Style style, int x, int y, boolean reverse) {
        if (style != null) {
            for (int[] points : getPoints(style).get(this.parenthesisHeight)) {
                this.drawPoints(gc, points, x, y, reverse);
            }
        }
    }

    private void drawPoints(GC gc, int[] points, int x, int y, boolean reverse) {
        points = points.clone();
        for (int index = 0; index < points.length; index += 2) {
            if (reverse) {
                points[index] = -points[index];
            }
            points[index] += x;
        }
        for (int index = 1; index < points.length; index += 2) {
            points[index] += y;
        }
        gc.drawPolyline(points);
    }

    private static Map<Integer, int[][]> getPoints(Style style) {
        switch(style) {
            case NORMAL:
                return normalPoints;
            case CURLY:
                return curlyPoints;
            default:
                return null;
        }
    }

    private ChildFrame main;

    private Style left;

    private Style right;

    private int margin;

    private int parenthesisHeight;

    private static Map<Integer, int[][]> normalPoints = new HashMap<Integer, int[][]>();

    private static Map<Integer, int[][]> curlyPoints = new HashMap<Integer, int[][]>();

    private static final int MIN_HEIGHT = 3;

    private static final int ADDED_HEIGHT = 2;

    public enum Style {

        NORMAL, CURLY
    }
}
