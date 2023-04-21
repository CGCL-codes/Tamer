package com.insanityengine.ghia.m3;

/**
 *
 * <P>
 * Methods pertaining to like the display on the like screen and stuff 
 * which has like width, height and maybe some kinda depth thingie.
 * </P>
 *
 * @author BrianHammond
 *
 * $Header: /cvsroot/ghia/ghia/src/java/com/insanityengine/ghia/m3/Frustrum.java,v 1.8 2006/11/29 06:15:32 brianin3d Exp $
 *
 */
public class Frustrum {

    /**
	 *
	 * Constructor
	 *
	 */
    public Frustrum() {
    }

    /**
	 *
	 * Constructor
	 *
	 * @param width of the display (in pixels)
	 * @param height of the display (in pixels)
	 * @param depth of the display (in gubertaters)
	 *
	 */
    public Frustrum(int width, int height) {
        setSize(width, height);
    }

    /**
	 *
	 * Constructor
	 *
	 * @param width of the display (in pixels)
	 * @param height of the display (in pixels)
	 * @param depth of the display (in gubertaters)
	 *
	 */
    public Frustrum(int width, int height, double depth) {
        setSize(width, height, depth);
    }

    /**
	 *
	 * Set the size of the display
	 *
	 * @param width of the display (in pixels)
	 * @param height of the display (in pixels)
	 *
	 */
    public final void setSize(int width, int height) {
        setSize(width, height, DEFAULT_DEPTH);
    }

    /**
	 *
	 * Set the size of the display
	 *
	 * @param width of the display (in pixels)
	 * @param height of the display (in pixels)
	 * @param depth of the display (in gubertaters)
	 *
	 */
    public final void setSize(int width, int height, double depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        w2 = width / 2;
        h2 = height / 2;
    }

    /**
	 *
	 * Set the depth of the display
	 *
	 * @param depth of the display (in gubertaters)
	 *
	 */
    public final void setDepth(double depth) {
        this.depth = depth;
    }

    /**
	 *
	 * Set the depth of the display
	 *
	 * @param depth of the display (in gubertaters)
	 *
	 */
    public final double getDepth() {
        return depth;
    }

    /**
	 *
	 * Test the polygon to see if it is entirely offscreen, ie:
	 * outside the viewing frustrum. 
	 *
	 * @param number of points
	 * @param ptz in the polygon
	 *
	 * @return true if the polygon is entirely offscreen, else false
	 *
	 */
    public final boolean offScreen(int number, Pt3 ptz[]) {
        return Frustrum.offScreen(width, height, depth, number, ptz);
    }

    /**
	 *
	 * Convert from points in space to points on the screen 
	 * with (0,0,0) in the middle of the display
	 *
	 * @param number of points to convert
	 * @param ptz in the polygon
	 *
	 */
    public final void toScreen(int number, Pt3 ptz[]) {
        toScreen(w2, h2, depth, number, ptz);
    }

    /**
	 *
	 * Convert from a point in space to point on the screen 
	 * with (0,0,0) in the middle of the display
	 *
	 * @param point to convert
	 *
	 */
    public final void toScreen(Pt3 point) {
        toScreen(w2, h2, point);
    }

    /**
	 *
	 * Convert from points in space to points on the screen 
	 * with (0,0,0) in the middle of the display
	 *
	 * @param middleX of the display (in pixels)
	 * @param middleY of the display (in pixels)
	 *
	 * @param number of points to convert
	 * @param ptz in the polygon
	 *
	 */
    public static final void toScreen(int middleX, int middleY, double depth, int number, Pt3 ptz[]) {
        for (int i = 0; i < number; i++) {
            toScreen(middleX, middleY, depth, ptz[i]);
        }
    }

    /**
	 *
	 * Convert from a point in space to point on the screen 
	 * with (0,0,0) in the middle of the display
	 *
	 * @param point to convert
	 *
	 */
    public static final void toScreen(int middleX, int middleY, double depth, Pt3 point) {
        point.x = middleX + (point.x * middleX * 0.5f) * zScale(point.z, depth);
        point.y = middleY + (point.y * middleY * 0.5f) * zScale(point.z, depth);
    }

    /**
	 *
	 *
	 *
	 *
	 */
    public static final double zScale(double z, double depth) {
        return ((depth - z) / depth + 0.1f);
    }

    /**
	 *
	 * Convert from a point in space to point on the screen 
	 * with (0,0,0) in the middle of the display
	 *
	 * @param point to convert
	 *
	 */
    public static final void toScreen(int middleX, int middleY, Pt3 point) {
        toScreen(middleX, middleY, DEFAULT_DEPTH, point);
    }

    /**
	 *
	 * Test the polygon to see if it is entirely offscreen, ie:
	 * outside the viewing frustrum. 
	 *
	 * May be slightly slower than offScreen method
	 *
	 * @param width of the display (in pixels)
	 * @param height of the display (in pixels)
	 * @param depth of the display (in gubertaters)
	 *
	 * @param number of points
	 * @param ptz in the polygon
	 *
	 * @return true if the polygon is entirely offscreen, else false
	 *
	 */
    public static final boolean offScreenClassic(int width, int height, double depth, int number, Pt3 ptz[]) {
        int lf_count, rt_count, up_count, dn_count, bk_count;
        lf_count = rt_count = up_count = dn_count = bk_count = 0;
        for (int i = 0; i < number; i++) {
            if (ptz[i].x < 0) lf_count++;
            if (ptz[i].x > width) rt_count++;
            if (ptz[i].y < 0) up_count++;
            if (ptz[i].y > height) dn_count++;
            if (ptz[i].z < depth) bk_count++;
        }
        return (false || (number == lf_count) || (number == rt_count) || (number == up_count) || (number == dn_count) || (number == bk_count));
    }

    /**
	 *
	 * Test if all the points are off the left side of the display
	 *
	 * @param number of points
	 * @param ptz in the polygon
	 *
	 * @return true if the polygon is entirely offscreen, else false
	 *
	 */
    public static final boolean wayLeft(int number, Pt3 ptz[]) {
        for (int i = 0; i < number; i++) {
            if (ptz[i].x >= 0) return false;
        }
        return true;
    }

    /**
	 *
	 * Test if all the points are off the right side of the display
	 *
	 * @param width of the display
	 *
	 * @param number of points
	 * @param ptz in the polygon
	 *
	 * @return true if the polygon is entirely offscreen, else false
	 *
	 */
    public static final boolean wayRight(int width, int number, Pt3 ptz[]) {
        for (int i = 0; i < number; i++) {
            if (ptz[i].x < width) return false;
        }
        return true;
    }

    /**
	 *
	 * Test if all the points are off the top side of the display
	 *
	 * @param number of points
	 * @param ptz in the polygon
	 *
	 * @return true if the polygon is entirely offscreen, else false
	 *
	 */
    public static final boolean wayUp(int number, Pt3 ptz[]) {
        for (int i = 0; i < number; i++) {
            if (ptz[i].y >= 0) return false;
        }
        return true;
    }

    /**
	 *
	 * Test if all the points are off the bottom side of the display
	 *
	 * @param height of the display
	 *
	 * @param number of points
	 * @param ptz in the polygon
	 *
	 * @return true if the polygon is entirely offscreen, else false
	 *
	 */
    public static final boolean wayDown(int height, int number, Pt3 ptz[]) {
        for (int i = 0; i < number; i++) {
            if (ptz[i].y < height) return false;
        }
        return true;
    }

    /**
	 *
	 * Test if all the points are off the back side of the display
	 *
	 * @param depth of the display
	 *
	 * @param number of points
	 * @param ptz in the polygon
	 *
	 * @return true if the polygon is entirely offscreen, else false
	 *
	 */
    public static final boolean wayBack(double depth, int number, Pt3 ptz[]) {
        for (int i = 0; i < number; i++) {
            if (ptz[i].z > depth) return false;
        }
        return true;
    }

    /**
	 *
	 * Test if all the points are off the back side of the display
	 *
	 * @param depth of the display
	 *
	 * @param number of points
	 * @param ptz in the polygon
	 *
	 * @return true if the polygon is entirely offscreen, else false
	 *
	 */
    public static final boolean wayFront(int number, Pt3 ptz[]) {
        for (int i = 0; i < number; i++) {
            if (ptz[i].z < 0) return false;
        }
        return true;
    }

    /**
	 *
	 * Test the polygon to see if it is entirely offscreen, ie:
	 * outside the viewing frustrum. 
	 *
	 * May be slightly faster than offScreenClassic method
	 *
	 * @param width of the display (in pixels)
	 * @param height of the display (in pixels)
	 * @param depth of the display (in gubertaters)
	 *
	 * @param number of points
	 * @param ptz in the polygon
	 *
	 * @return true if the polygon is entirely offscreen, else false
	 *
	 */
    public static final boolean offScreen(int width, int height, double depth, int number, Pt3 ptz[]) {
        return (wayLeft(number, ptz) || wayRight(width, number, ptz) || wayUp(number, ptz) || wayDown(height, number, ptz));
    }

    private int width = 0;

    private int height = 0;

    private int w2 = 0, h2 = 0;

    private double depth = DEFAULT_DEPTH;

    public static final int DEFAULT_DEPTH = -1000;
}

;
