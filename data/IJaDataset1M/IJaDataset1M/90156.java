package util;

import context.*;
import geometry3D.Point3D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import object3D.Block;

public class Projection implements Context.Constants {

    public static Point2D.Double pointProjection(Point3D p0, int direction, Point2D.Double origo2D, double scale) {
        double x1 = 0, y1 = 0;
        switch(direction) {
            case HCUT:
            case ABOVE:
                x1 = origo2D.x + p0.x;
                y1 = origo2D.y + p0.z;
                break;
            case BELOW:
                x1 = origo2D.x + p0.x;
                y1 = origo2D.y - p0.z;
                break;
            case VCUT_SOUTH:
            case SOUTH:
                x1 = origo2D.x + p0.x;
                y1 = origo2D.y - p0.y;
                break;
            case VCUT_NORTH:
            case NORTH:
                x1 = origo2D.x - p0.x;
                y1 = origo2D.y - p0.y;
                break;
            case VCUT_EAST:
            case EAST:
                x1 = origo2D.x - p0.z;
                y1 = origo2D.y - p0.y;
                break;
            case VCUT_WEST:
            case WEST:
                x1 = origo2D.x + p0.z;
                y1 = origo2D.y - p0.y;
                break;
        }
        return (new Point2D.Double(x1 * scale, y1 * scale));
    }

    public static Rectangle2D.Double blockProjection(Block block, int direction, Point2D.Double origo2D, double scale) {
        double x1 = 0, y1 = 0, w1 = 0, h1 = 0;
        switch(direction) {
            case HCUT:
            case ABOVE:
                x1 = origo2D.x + block.p0.x;
                y1 = origo2D.y + block.p0.z;
                w1 = block.width;
                h1 = block.depth;
                break;
            case BELOW:
                x1 = origo2D.x + block.p0.x;
                y1 = origo2D.y - block.p0.z - block.height;
                w1 = block.width;
                h1 = block.depth;
                break;
            case VCUT_SOUTH:
            case SOUTH:
                x1 = origo2D.x + block.p0.x;
                y1 = origo2D.y - block.p0.y - block.height;
                w1 = block.width;
                h1 = block.height;
                break;
            case VCUT_NORTH:
            case NORTH:
                x1 = origo2D.x - block.p0.x - block.width;
                y1 = origo2D.y - block.p0.y - block.height;
                w1 = block.width;
                h1 = block.height;
                break;
            case VCUT_EAST:
            case EAST:
                x1 = origo2D.x - block.p0.z - block.depth;
                y1 = origo2D.y - block.p0.y - block.height;
                w1 = block.depth;
                h1 = block.height;
                break;
            case VCUT_WEST:
            case WEST:
                x1 = origo2D.x + block.p0.z;
                y1 = origo2D.y - block.p0.y - block.height;
                w1 = block.depth;
                h1 = block.height;
                break;
        }
        return (new Rectangle2D.Double(x1 * scale, y1 * scale, w1 * scale, h1 * scale));
    }
}
