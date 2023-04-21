package sun.font;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public final class StrikeMetrics {

    public float ascentX;

    public float ascentY;

    public float descentX;

    public float descentY;

    public float baselineX;

    public float baselineY;

    public float leadingX;

    public float leadingY;

    public float maxAdvanceX;

    public float maxAdvanceY;

    StrikeMetrics() {
        ascentX = ascentY = Integer.MAX_VALUE;
        descentX = descentY = leadingX = leadingY = Integer.MIN_VALUE;
        baselineX = baselineX = maxAdvanceX = maxAdvanceY = Integer.MIN_VALUE;
    }

    StrikeMetrics(float ax, float ay, float dx, float dy, float bx, float by, float lx, float ly, float mx, float my) {
        ascentX = ax;
        ascentY = ay;
        descentX = dx;
        descentY = dy;
        baselineX = bx;
        baselineY = by;
        leadingX = lx;
        leadingY = ly;
        maxAdvanceX = mx;
        maxAdvanceY = my;
    }

    public float getAscent() {
        return -ascentY;
    }

    public float getDescent() {
        return descentY;
    }

    public float getLeading() {
        return leadingY;
    }

    public float getMaxAdvance() {
        return maxAdvanceX;
    }

    void merge(StrikeMetrics other) {
        if (other == null) {
            return;
        }
        if (other.ascentX < ascentX) {
            ascentX = other.ascentX;
        }
        if (other.ascentY < ascentY) {
            ascentY = other.ascentY;
        }
        if (other.descentX > descentX) {
            descentX = other.descentX;
        }
        if (other.descentY > descentY) {
            descentY = other.descentY;
        }
        if (other.baselineX > baselineX) {
            baselineX = other.baselineX;
        }
        if (other.baselineY > baselineY) {
            baselineY = other.baselineY;
        }
        if (other.leadingX > leadingX) {
            leadingX = other.leadingX;
        }
        if (other.leadingY > leadingY) {
            leadingY = other.leadingY;
        }
        if (other.maxAdvanceX > maxAdvanceX) {
            maxAdvanceX = other.maxAdvanceX;
        }
        if (other.maxAdvanceY > maxAdvanceY) {
            maxAdvanceY = other.maxAdvanceY;
        }
    }

    void convertToUserSpace(AffineTransform invTx) {
        Point2D.Float pt2D = new Point2D.Float();
        pt2D.x = ascentX;
        pt2D.y = ascentY;
        invTx.deltaTransform(pt2D, pt2D);
        ascentX = pt2D.x;
        ascentY = pt2D.y;
        pt2D.x = descentX;
        pt2D.y = descentY;
        invTx.deltaTransform(pt2D, pt2D);
        descentX = pt2D.x;
        descentY = pt2D.y;
        pt2D.x = baselineX;
        pt2D.y = baselineY;
        invTx.deltaTransform(pt2D, pt2D);
        baselineX = pt2D.x;
        baselineY = pt2D.y;
        pt2D.x = leadingX;
        pt2D.y = leadingY;
        invTx.deltaTransform(pt2D, pt2D);
        leadingX = pt2D.x;
        leadingY = pt2D.y;
        pt2D.x = maxAdvanceX;
        pt2D.y = maxAdvanceY;
        invTx.deltaTransform(pt2D, pt2D);
        maxAdvanceX = pt2D.x;
        maxAdvanceY = pt2D.y;
    }

    public String toString() {
        return "ascent:x=" + ascentX + " y=" + ascentY + " descent:x=" + descentX + " y=" + descentY + " baseline:x=" + baselineX + " y=" + baselineY + " leading:x=" + leadingX + " y=" + leadingY + " maxAdvance:x=" + maxAdvanceX + " y=" + maxAdvanceY;
    }
}
