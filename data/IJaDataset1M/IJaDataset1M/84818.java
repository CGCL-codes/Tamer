package org.gjt.universe.gui;

import org.gjt.universe.Coord;

final class InternalCoord implements Cloneable {

    private float val[] = new float[3];

    InternalCoord() {
        val[0] = (float) 0.0;
        val[1] = (float) 0.0;
        val[2] = (float) 0.0;
    }

    InternalCoord(Coord center, Coord c) {
        setCoord(center, c);
    }

    /**
 *  Scale center to (0.0, 0.0, 0.0)
 */
    void setCoord(Coord center, Coord c) {
        val[0] = center.getX() - c.getX();
        val[1] = center.getY() - c.getY();
        val[2] = center.getZ() - c.getZ();
    }

    void ApplyXForm(AffineBase XF) {
        float tmpval[] = new float[3];
        float XFval[][] = XF.getVal();
        tmpval[0] = XFval[0][0] * val[0] + XFval[0][1] * val[1] + XFval[0][2] * val[2] + XFval[0][3];
        tmpval[1] = XFval[1][0] * val[0] + XFval[1][1] * val[1] + XFval[1][2] * val[2] + XFval[1][3];
        tmpval[2] = XFval[2][0] * val[0] + XFval[2][1] * val[1] + XFval[2][2] * val[2] + XFval[2][3];
        val[0] = tmpval[0];
        val[1] = tmpval[1];
        val[2] = tmpval[2];
    }

    float getX() {
        return val[0];
    }

    float getY() {
        return val[1];
    }

    float getZ() {
        return val[2];
    }
}
