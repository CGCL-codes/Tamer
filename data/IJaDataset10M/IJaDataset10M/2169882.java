package com.excitingtechnology.tep;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import com.excitingtechnology.tep.imaging.ByteImage;
import com.excitingtechnology.tep.imaging.ImageRegion;
import com.excitingtechnology.tep.imaging.Region;
import com.excitingtechnology.tep.imaging.RegionHashSet;

/**
 * Abstraction for modeling 2 corneal reflections on a cornea created by 2 IR LEDs.
 * 
 * @author David McManamon
 */
public class CornealReflections {

    private static Logger log = Logger.getLogger(CornealReflections.class);

    private Region regionA;

    private Region regionB;

    public static int MAX_DELTA_Y = 3;

    public static int MIN_DELTA_X = 10;

    public static int MAX_DELTA_X = 25;

    private boolean regionARemoved = false;

    private boolean regionBRemoved = false;

    /**
     * Allows sorting of pixels within a reflection.
     */
    static class PixelComparator implements Comparator<Point> {

        @Override
        public int compare(Point o1, Point o2) {
            Integer a, b;
            if (o1.x == o2.x) {
                a = o1.y;
                b = o2.y;
            } else {
                a = o1.x;
                b = o2.x;
            }
            return a.compareTo(b);
        }
    }

    public CornealReflections(Region regionA, Region regionB) {
        this.regionA = regionA;
        this.regionB = regionB;
    }

    public static List<CornealReflections> findCRs(List<Region> reflections, ByteImage image) {
        List<CornealReflections> crs = new LinkedList<CornealReflections>();
        Collections.sort(reflections);
        Collections.reverse(reflections);
        for (int i = 0; i < reflections.size(); i++) {
            Region pgA = reflections.get(i);
            for (int j = i + 1; j < reflections.size(); j++) {
                Region pgB = reflections.get(j);
                if (reflectionsMatch(pgA, pgB, image)) {
                    CornealReflections cr = new CornealReflections(pgA, pgB);
                    log.debug("Located " + cr);
                    crs.add(cr);
                    if (crs.size() == 2) return crs;
                }
            }
        }
        if (crs.size() == 0) log.error("Failed to find corneal reflections");
        return crs;
    }

    /**
     * CRs can be ignored or removed, removing only non-edge CRs
     * allows a very simple/fast interpolation algorithm to be used. 
     * <BR>
     * 
     * @param image
     * @return region containing pixels not removed.
     */
    public RegionHashSet removeNonEdgeCRs(ByteImage image) {
        Region edgeA = regionA.getPixelsOutsideEdge();
        ImageRegion irA = new ImageRegion(image, edgeA);
        int maxA = irA.getMax();
        int minA = irA.getMin();
        if (maxA - minA < 10) {
            irA.setRegion((int) irA.getMean());
            regionARemoved = true;
        }
        Region edgeB = regionA.getPixelsOutsideEdge();
        ImageRegion irB = new ImageRegion(image, edgeB);
        int maxB = irB.getMax();
        int minB = irB.getMin();
        if (maxB - minB < 10) {
            irB.setRegion((int) irB.getMean());
            regionBRemoved = true;
        }
        return getNonRemovedCRs();
    }

    public RegionHashSet getNonRemovedCRs() {
        RegionHashSet r = new RegionHashSet();
        if (!regionARemoved) r.addAll(regionA);
        if (!regionBRemoved) r.addAll(regionB);
        return r;
    }

    protected static void removeCR(Set<Point> pixels, ByteImage image) {
        for (Point pixel : pixels) {
            Point x1 = new Point(pixel.x, pixel.y + 1);
            while (pixels.contains(x1)) x1.y += 1;
            Point x0 = new Point(pixel.x, pixel.y - 1);
            while (pixels.contains(x0)) x0.y -= 1;
            double val1 = interpolate(image.get(x0), image.get(x1), pixel.y, x0.y, x1.y);
            Point x2 = new Point(pixel.x + 1, pixel.y);
            while (pixels.contains(x2)) x2.x += 1;
            Point x3 = new Point(pixel.x - 1, pixel.y);
            while (pixels.contains(x3)) x3.x -= 1;
            double val2 = interpolate(image.get(x2), image.get(x3), pixel.x, x2.x, x3.x);
            Point xyPosA = new Point(pixel.x - 1, pixel.y - 1);
            while (pixels.contains(xyPosA)) {
                xyPosA.y -= 1;
                xyPosA.x -= 1;
            }
            Point xyPosB = new Point(pixel.x + 1, pixel.y + 1);
            while (pixels.contains(xyPosB)) {
                xyPosB.y += 1;
                xyPosB.x += 1;
            }
            double val3 = interpolate(image.get(xyPosA), image.get(xyPosB), pixel.y, xyPosA.y, xyPosB.y);
            Point xyNegA = new Point(pixel.x - 1, pixel.y + 1);
            while (pixels.contains(xyNegA)) {
                xyNegA.y += 1;
                xyNegA.x -= 1;
            }
            Point xyNegB = new Point(pixel.x + 1, pixel.y - 1);
            while (pixels.contains(xyNegB)) {
                xyNegB.y -= 1;
                xyNegB.x += 1;
            }
            double val4 = interpolate(image.get(xyNegA), image.get(xyNegB), pixel.x, xyNegA.x, xyPosB.x);
            int res = (int) (val1 + val2 + val3 + val4) / 4;
            System.out.println("TO: " + res + " FROM: " + image.get(pixel.x, pixel.y));
            image.set(pixel.x, pixel.y, res);
        }
    }

    protected static double interpolate(int val0, int val1, int x, int x0, int x1) {
        return val0 + (x - x0) * (val1 - val0) / (double) (x1 - x0);
    }

    protected static boolean reflectionsMatch(Region pgA, Region pgB, ByteImage image) {
        if (pgB.size() < 3) return false;
        double xDelta = Math.abs(pgA.getCenter().x - pgB.getCenter().x);
        double yDelta = Math.abs(pgA.getCenter().y - pgB.getCenter().y);
        if (xDelta > MIN_DELTA_X && xDelta < MAX_DELTA_X && yDelta <= MAX_DELTA_Y) {
            int y = (int) pgA.getCenter().y;
            int xStart = (int) pgA.getCenter().x;
            int xEnd = (int) pgB.getCenter().x;
            if (xStart > xEnd) {
                int tmp = xStart;
                xStart = xEnd;
                xEnd = tmp;
            }
            for (int i = xStart; i < xEnd; i++) {
                if (image.get(i, y) == 0) return true;
            }
            return false;
        } else return false;
    }

    @Override
    public String toString() {
        return "CR: " + regionA + " " + regionB;
    }

    public Point2D.Double getPointBetweenGlints() {
        Point2D.Double centerA = regionA.getCenter();
        Point2D.Double centerB = regionB.getCenter();
        return new Point2D.Double(centerA.x + centerB.x / 2.0, centerA.y + centerB.y / 2.0);
    }

    public Point guessPupilCenter(ByteImage image) {
        Point2D.Double crPoint = getPointBetweenGlints();
        return new Point((int) Math.round(crPoint.x), (int) Math.round(crPoint.y));
    }
}
