package com.googlecode.grtframework.vis;

/**
 * MountedPosition describes the 2D position of an object with reference to a
 * Mountable root, which is treated as an origin of sorts.
 * 
 * A MountedPosition always refers to the body
 * 
 * @author ajc
 * 
 */
public class MountedPosition implements IMountedPosition {

    private final Mountable parent;

    /**
	 * 2D relative orientation (angle)
	 */
    private double mountedAngle;

    /**
	 * 'r' from polar notation: used for 2D position in polar form
	 */
    private double polarR;

    /**
	 * theta from polar notation: used for 2D position in polar form
	 */
    private double polarTheta;

    /**
	 * 
	 * @param root
	 */
    public MountedPosition(Mountable root) {
        this.parent = root;
    }

    /**
	 * 
	 * @param root
	 *            what this mounted position is mounted on
	 * @param x
	 *            relative X offset
	 * @param y
	 *            relative Y offset
	 * @param mountedAngle
	 *            relative heading
	 */
    public MountedPosition(Mountable root, int x, int y, double mountedAngle) {
        this.parent = root;
        this.mountedAngle = mountedAngle;
        setPositionRelative(x, y);
        setHeadingRelative(mountedAngle);
    }

    @Override
    public void setHeadingRelative(double heading) {
        mountedAngle = heading;
    }

    @Override
    public void setHeadingAbsolute(double heading) {
    }

    @Override
    public void setPositionRelative(double x, double y) {
        polarR = Math.sqrt((x * x) + (y * y));
        polarTheta = Math.atan2(y, x);
    }

    @Override
    public void setPositionAbsolute(double x, double y) {
        setPositionRelative(x - parent.getX(), y - parent.getY());
    }

    @Override
    public double getR() {
        return polarR;
    }

    @Override
    public double getTheta() {
        return polarTheta;
    }

    @Override
    public double getHeadingRelative() {
        return mountedAngle;
    }

    @Override
    public double getHeading() {
        return parent.getHeading() + mountedAngle;
    }

    @Override
    public int getX() {
        return (int) (parent.getX() + polarR * Math.cos(parent.getHeading() + polarTheta));
    }

    @Override
    public int getY() {
        return (int) (parent.getY() + polarR * Math.sin(parent.getHeading() + polarTheta));
    }

    /**
	 * @deprecated poor word choice; not really a root
	 * @return
	 */
    public Mountable getRoot() {
        return parent;
    }

    @Override
    public Mountable[] getParents() {
        return new Mountable[] { parent };
    }

    public MountedPosition clone() {
        return new MountedPosition(parent, (int) (polarR * Math.cos(parent.getHeading() + polarTheta)), (int) (polarR * Math.sin(parent.getHeading() + polarTheta)), mountedAngle);
    }

    @Override
    public double getXRelative() {
        return polarR * Math.cos(polarTheta);
    }

    @Override
    public double getYRelative() {
        return polarR * Math.sin(polarTheta);
    }
}
