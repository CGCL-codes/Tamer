package org.jnet.g2d;

import org.jnet.g3d.Cylinder3D;
import org.jnet.g3d.Line3D;

/**
 * This is a 2D version of Cylinder 3D - flattened - which really is just a rectangle
 * this should be renamed Rectangle2D - Cylinders are inherently 3D!
 * actually cylinder3d is really a cylinder as it optionally has spherically caps
 * its a Pill3D and Pill2D - but caps not done here ?
 * @author mgibson
 */
class Cylinder2D extends Cylinder3D {

    private Graphics2D g2d;

    public Cylinder2D(Graphics2D g2d) {
        super(g2d.g3d());
        this.g2d = g2d;
    }

    private Graphics2D g2d() {
        return g2d;
    }

    /** This is the fundamental thing that makes 3D - z coord - override for 2D */
    @Override
    protected double calcZRotatedPoint(double xR, double yR) {
        return 0;
    }

    @Override
    protected float calcZIntensity(double xR, double yR, double zR) {
        return (float) super.calcZRotatedPoint(xR, yR);
    }

    @Override
    protected void renderSphericalEndcaps() {
        if (setColixOk(getColixA())) g2d.fillSphereCentered(diameter(), xA(), yA(), zA() + 1);
        if (setColixOk(getColixB())) g2d.fillSphereCentered(diameter(), xA() + dxB(), yA() + dyB(), zA() + dzB() + 1);
    }

    /** returns false if colix is 0 or fails to set */
    private boolean setColixOk(short colix) {
        if (colix == 0) return false;
        return g2d().setColix(colix);
    }
}
