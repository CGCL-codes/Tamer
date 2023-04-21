package org.ximtec.igesture.io.tuio.tuio3D;

import org.ximtec.igesture.io.tuio.TuioTime;
import org.ximtec.igesture.io.tuio.interfaces.AbstractTuioPoint;

/**
 * The TuioPoint3D class on the one hand is a simple container and utility class to handle TUIO positions in general, 
 * on the other hand the TuioPoint3D is the base class for the TuioCursor3D and TuioObject3D classes.
 *
 * @author Martin Kaltenbrunner, Bjorn Puype
 * @version 1.4
 */
public class TuioPoint3D implements AbstractTuioPoint {

    /** XY plane */
    public static final int XY = 0;

    /** XZ plane */
    public static final int XZ = 1;

    /** YZ plane */
    public static final int YZ = 2;

    /**
	 * X coordinate, represented as a floating point value in a range of 0..1  
	 */
    protected float xpos;

    /**
	 * Y coordinate, represented as a floating point value in a range of 0..1  
	 */
    protected float ypos;

    /**
	 * Z coordinate, represented as a floating point value in a range of 0..1  
	 */
    protected float zpos;

    /**
	 * The time stamp of the last update represented as TuioTime (time since session start)
	 */
    protected TuioTime currentTime;

    /**
	 * The creation time of this TuioPoint3D represented as TuioTime (time since session start)
	 */
    protected TuioTime startTime;

    /**
	 * The default constructor takes no arguments and sets   
	 * its coordinate attributes to zero and its time stamp to the current session time.
	 */
    public TuioPoint3D() {
        xpos = 0.0f;
        ypos = 0.0f;
        zpos = 0.0f;
        currentTime = TuioTime.getSessionTime();
        startTime = new TuioTime(currentTime);
    }

    /**
	 * This constructor takes three floating point coordinate arguments and sets   
	 * its coordinate attributes to these values and its time stamp to the current session time.
	 *
	 * @param	xp	the X coordinate to assign
	 * @param	yp	the Y coordinate to assign
	 * @param	zp	the Z coordinate to assign
	 */
    public TuioPoint3D(float xp, float yp, float zp) {
        xpos = xp;
        ypos = yp;
        zpos = zp;
        currentTime = TuioTime.getSessionTime();
        startTime = new TuioTime(currentTime);
    }

    /**
	 * This constructor takes a TuioPoint3D argument and sets its coordinate attributes 
	 * to the coordinates of the provided TuioPoint3D and its time stamp to the current session time.
	 *
	 * @param	tpoint	the TuioPoint3D to assign
	 */
    public TuioPoint3D(TuioPoint3D tpoint) {
        xpos = tpoint.getX();
        ypos = tpoint.getY();
        zpos = tpoint.getZ();
        currentTime = TuioTime.getSessionTime();
        startTime = new TuioTime(currentTime);
    }

    /**
	 * This constructor takes a TuioTime object and three floating point coordinate arguments and sets   
	 * its coordinate attributes to these values and its time stamp to the provided TUIO time object.
	 *
	 * @param	ttime	the TuioTime to assign
	 * @param	xp	the X coordinate to assign
	 * @param	yp	the Y coordinate to assign
	 * @param	zp	the Z coordinate to assign
	 */
    public TuioPoint3D(TuioTime ttime, float xp, float yp, float zp) {
        xpos = xp;
        ypos = yp;
        zpos = zp;
        currentTime = new TuioTime(ttime);
        startTime = new TuioTime(currentTime);
    }

    /**
	 * Takes a TuioPoint3D argument and updates its coordinate attributes 
	 * to the coordinates of the provided TuioPoint3D and leaves its time stamp unchanged.
	 *
	 * @param	tpoint	the TuioPoint to assign
	 */
    public void update(TuioPoint3D tpoint) {
        xpos = tpoint.getX();
        ypos = tpoint.getY();
        zpos = tpoint.getZ();
    }

    /**
	 * Takes three floating point coordinate arguments and updates its coordinate attributes 
	 * to the coordinates of the provided TuioPoint3D and leaves its time stamp unchanged.
	 *
	 * @param	xp	the X coordinate to assign
	 * @param	yp	the Y coordinate to assign
	 * @param 	zp 	the Z coordinate to assign
	 */
    public void update(float xp, float yp, float zp) {
        xpos = xp;
        ypos = yp;
        zpos = zp;
    }

    /**
	 * Takes a TuioTime object and three floating point coordinate arguments and updates its coordinate attributes 
	 * to the coordinates of the provided TuioPoint3D and its time stamp to the provided TUIO time object.
	 *
	 * @param	ttime	the TuioTime to assign
	 * @param	xp	the X coordinate to assign
	 * @param	yp	the Y coordinate to assign
	 * @param	zp	the Z coordinate to assign
	 */
    public void update(TuioTime ttime, float xp, float yp, float zp) {
        xpos = xp;
        ypos = yp;
        zpos = zp;
        currentTime = new TuioTime(ttime);
    }

    /**
	 * Returns the X coordinate of this TuioPoint3D. 
	 * @return	the X coordinate of this TuioPoint3D
	 */
    public float getX() {
        return xpos;
    }

    /**
	 * Returns the Y coordinate of this TuioPoint3D. 
	 * @return	the Y coordinate of this TuioPoint3D
	 */
    public float getY() {
        return ypos;
    }

    /**
	 * Returns the Z coordinate of this TuioPoint3D. 
	 * @return	the Z coordinate of this TuioPoint3D
	 */
    public float getZ() {
        return zpos;
    }

    /**
	 * Returns the distance to the provided coordinates 
	 *
	 * @param	xp	the X coordinate of the distant point
	 * @param	yp	the Y coordinate of the distant point
	 * @param	zp	the Z coordinate of the distant point
	 * @return	the distance to the provided coordinates
	 */
    public float getDistance(float xp, float yp, float zp) {
        float dx = xpos - xp;
        float dy = ypos - yp;
        float dz = zpos - zp;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
	 * Returns the distance to the provided coordinates 
	 *
	 * @param	planes	the planes of which to calculate the distance (XY, XZ or YZ)
	 * @param	ap	the coordinate of the first plane of the distant point
	 * @param	bp	the coordinate of the second plane of the distant point
	 * @return	the distance to the provided coordinates
	 */
    private float getDistance(int planes, float ap, float bp) {
        float da = 0, db = 0;
        switch(planes) {
            case XY:
                da = xpos - ap;
                db = ypos - bp;
                break;
            case XZ:
                da = xpos - ap;
                db = zpos - bp;
                break;
            case YZ:
                da = ypos - ap;
                db = zpos - bp;
                break;
            default:
                System.err.println("Unknown planes entered");
                break;
        }
        return (float) Math.sqrt(da * da + db * db);
    }

    /**
	 * Returns the distance to the provided TuioPoint3D 
	 *
	 * @param	tpoint	the distant TuioPoint3D
	 * @return	the distance to the provided TuioPoint3D
	 */
    public float getDistance(TuioPoint3D tpoint) {
        return getDistance(tpoint.getX(), tpoint.getY(), tpoint.getZ());
    }

    /**
	 * Returns the distance to the provided TuioPoint3D 
	 *
	 * @param	planes	the planes of which to calculate the distance (XY, XZ or YZ)
	 * @param	tpoint	the distant TuioPoint3D
	 * @return	the distance to the provided TuioPoint3D
	 */
    private float getDistance(int planes, TuioPoint3D tpoint) {
        float distance = 0;
        switch(planes) {
            case XY:
                distance = getDistance(XY, tpoint.getX(), tpoint.getY());
                break;
            case XZ:
                distance = getDistance(XZ, tpoint.getX(), tpoint.getZ());
                break;
            case YZ:
                distance = getDistance(YZ, tpoint.getY(), tpoint.getZ());
                break;
            default:
                System.err.println("Unknown planes entered");
        }
        return distance;
    }

    /**
	 * Returns the angle to the provided coordinates 
	 *
	 * @param	planes	the planes of which to calculate the angle (XY, XZ or YZ)
	 * @param	ap	the coordinate of the distant point in the first plane
	 * @param	bp	the  coordinate of the distant point in the second plane
	 * @return	the angle to the provided coordinates
	 */
    public float getAngle(int planes, float ap, float bp) {
        float side = 0, height = 0, distance = 0;
        switch(planes) {
            case XY:
                side = xpos - ap;
                height = ypos - bp;
                distance = getDistance(XY, ap, bp);
                break;
            case XZ:
                side = xpos - ap;
                height = zpos - bp;
                distance = getDistance(XZ, ap, bp);
                break;
            case YZ:
                side = ypos - ap;
                height = zpos - bp;
                distance = getDistance(YZ, ap, bp);
                break;
            default:
                System.err.println("Unknown planes entered");
                break;
        }
        float angle = (float) (Math.asin(side / distance) + Math.PI / 2);
        if (height < 0) angle = 2.0f * (float) Math.PI - angle;
        return angle;
    }

    /**
	 * Returns the angle to the provided TuioPoint3D 
	 *
	 * @param	planes	the planes of which to calculate the angle (XY, XZ or YZ)
	 * @param	tpoint	the distant TuioPoint3D
	 * @return	the angle to the provided TuioPoint3D
	 */
    public float getAngle(int planes, TuioPoint3D tpoint) {
        float angle = 0;
        switch(planes) {
            case XY:
                angle = getAngle(planes, tpoint.getX(), tpoint.getY());
                break;
            case XZ:
                angle = getAngle(planes, tpoint.getX(), tpoint.getZ());
                break;
            case YZ:
                angle = getAngle(planes, tpoint.getY(), tpoint.getZ());
                break;
            default:
                System.err.println("Unknown planes entered");
        }
        return angle;
    }

    /**
	 * Returns the angle in degrees to the provided coordinates 
	 *
	 * @param	planes	the planes of which to calculate the angle (XY, XZ or YZ)
	 * @param	ap	the coordinate of the first plane of the distant point
	 * @param	bp	the coordinate of the second plane of the distant point
	 * @return	the angle in degrees to the provided TuioPoint3D
	 */
    public float getAngleDegrees(int planes, float ap, float bp) {
        return (getAngle(planes, ap, bp) / (float) Math.PI) * 180.0f;
    }

    /**
	 * Returns the angle in degrees to the provided TuioPoint 
	 *
	 * @param	planes	the planes of which to calculate the angle (XY, XZ or YZ)
	 * @param	tpoint	the distant TuioPoint
	 * @return	the angle in degrees to the provided TuioPoint
	 */
    public float getAngleDegrees(int planes, TuioPoint3D tpoint) {
        return (getAngle(planes, tpoint) / (float) Math.PI) * 180.0f;
    }

    /**
	 * Returns the X coordinate in pixels relative to the provided screen width. 
	 *
	 * @param	width	the screen width
	 * @return	the X coordinate of this TuioPoint3D in pixels relative to the provided screen width
	 */
    public int getScreenX(int width) {
        return (int) Math.round(xpos * width);
    }

    /**
	 * Returns the Y coordinate in pixels relative to the provided screen height. 
	 *
	 * @param	height	the screen height
	 * @return	the Y coordinate of this TuioPoint3D in pixels relative to the provided screen height
	 */
    public int getScreenY(int height) {
        return (int) Math.round(ypos * height);
    }

    public int getScreenZ(int z) {
        return (int) Math.round(zpos * z);
    }

    /**
	 * Returns the time stamp of this TuioPoint3D as TuioTime. 
	 *
	 * @return	the time stamp of this TuioPoint3D as TuioTime
	 */
    public TuioTime getTuioTime() {
        return new TuioTime(currentTime);
    }

    /**
	 * Returns the start time of this TuioPoint3D as TuioTime. 
	 *
	 * @return	the start time of this TuioPoint3D as TuioTime
	 */
    public TuioTime getStartTime() {
        return new TuioTime(startTime);
    }
}
