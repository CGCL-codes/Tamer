package self.amigo.elem;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public final class PolygonEffectUtils {

    /**
   *
   *    5  6  7
   *      \|/
   *    4 -+- 0
   *      /|\
   *    3  2  1
   *
   */
    public static int[] initOriginPoint(Rectangle fig, int dir) {
        int[] ret = { fig.x, fig.y };
        int midHeight = fig.height / 2;
        int midWidth = fig.width / 2;
        if (dir == 5 || dir == 4 || dir == 3) ret[0] = fig.x + fig.width;
        if (dir == 5 || dir == 6 || dir == 7) ret[1] = fig.y + fig.height;
        if (dir == 2 || dir == 6) ret[0] = fig.x + midWidth;
        if (dir == 4 || dir == 0) ret[1] = fig.y + midHeight;
        return ret;
    }

    public static int[] initDestinationPoint(int[] origin, Rectangle fig, int dir, int len) {
        boolean fromPerimeter = (dir < 8);
        boolean lenZero = (len == 0);
        if (!fromPerimeter) dir = dir - 8;
        int[] ret = new int[2];
        ret[0] = origin[0];
        ret[1] = origin[1];
        if (dir == 7 || dir == 0 || dir == 1) ret[0] = (lenZero ? fig.x + fig.width : origin[0] + len);
        if (dir == 5 || dir == 4 || dir == 3) ret[0] = (lenZero ? fig.x : origin[0] - len);
        if (dir == 5 || dir == 6 || dir == 7) ret[1] = (lenZero ? fig.y : origin[1] - len);
        if (dir == 3 || dir == 2 || dir == 1) ret[1] = (lenZero ? fig.y + fig.height : origin[1] + len);
        return ret;
    }

    private static Area[] getCentreEffectAreas(Polygon polygon, Rectangle fig) {
        int top = fig.y, left = fig.x, bottom = top + fig.height, right = left + fig.width;
        int centerx = left + (fig.width / 2), centery = top + (fig.height / 2);
        Polygon north = new Polygon(new int[] { left, right, centerx }, new int[] { top, top, centery }, 3);
        Polygon south = new Polygon(new int[] { left, right, centerx }, new int[] { bottom, bottom, centery }, 3);
        Polygon east = new Polygon(new int[] { right, right, centerx }, new int[] { top, bottom, centery }, 3);
        Polygon west = new Polygon(new int[] { left, left, centerx }, new int[] { top, bottom, centery }, 3);
        Area polygonArea = new Area(polygon);
        Area northArea = new Area(north);
        Area southArea = new Area(south);
        Area eastArea = new Area(east);
        Area westArea = new Area(west);
        northArea.intersect(polygonArea);
        southArea.intersect(polygonArea);
        eastArea.intersect(polygonArea);
        westArea.intersect(polygonArea);
        Area[] ret = new Area[] { northArea, southArea, eastArea, westArea };
        return ret;
    }

    public static ArrayList assembleEffect(Polygon source, Color c1, Color c2, int dir, int len, boolean cyclic) {
        ArrayList ret = new ArrayList();
        Rectangle fig = source.getBounds();
        int[] origin;
        if (dir > 7 && dir <= 16) origin = new int[] { fig.x + (fig.width) / 2, fig.y + (fig.height / 2) }; else origin = PolygonEffectUtils.initOriginPoint(fig, dir);
        if (dir == 16) {
            Area[] directionAreas = getCentreEffectAreas(source, fig);
            int[] dirs = new int[] { 6, 2, 0, 4 };
            for (int cntr = 0; cntr < dirs.length; cntr++) {
                int[] dest = PolygonEffectUtils.initDestinationPoint(origin, fig, dirs[cntr] + 8, len);
                GradientPaint gp = new GradientPaint(origin[0], origin[1], c1, dest[0], dest[1], c2, cyclic);
                GradientPaintShape effect = new GradientPaintShape(gp, directionAreas[cntr]);
                ret.add(effect);
            }
        } else {
            int[] dest = PolygonEffectUtils.initDestinationPoint(origin, fig, dir, len);
            GradientPaint gp = new GradientPaint(origin[0], origin[1], c1, dest[0], dest[1], c2, cyclic);
            GradientPaintShape effect = new GradientPaintShape(gp, source);
            ret.add(effect);
        }
        return ret;
    }

    public static class GradientPaintShape {

        public GradientPaint gradientPaint;

        public Shape shape;

        public GradientPaintShape(GradientPaint gp, Shape s) {
            gradientPaint = gp;
            shape = s;
        }
    }

    public static void main(String[] args) {
        int dir = 15;
        int len = 0;
        Rectangle rect = new Rectangle(0, 0, 200, 100);
        int orig[] = new int[] { rect.x + (rect.width) / 2, rect.y + (rect.height / 2) };
        int dest[] = initDestinationPoint(orig, rect, dir, len);
        System.out.println("rect=" + rect);
        System.out.println("o->" + orig[0] + ", " + orig[1]);
        System.out.println("d->" + dest[0] + ", " + dest[1]);
    }
}
