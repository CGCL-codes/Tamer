package playground.scnadine.MapMatching.mapMatching;

import org.postgis.LineString;
import org.postgis.Point;

/** Variant of LinkCoordinate with a link pointer instead of
	a link ID. For internal use.
	*/
public class InternalLinkCoordinate {

    public Link link;

    public Point pt = new Point();

    public double offset;

    public double dist;

    public double lineDist;

    private int numberInLine;

    private void set(Link link, Point pt, double offset, double dist, int numberInLine) {
        this.link = link;
        this.pt = pt;
        this.offset = offset;
        this.dist = dist;
        this.numberInLine = numberInLine;
    }

    public void distLine(InternalLinkCoordinate last, Point currentpt, Point lastpt) throws Exception {
        lineDist = 0;
        if (last.link == link) {
            if (last.numberInLine == numberInLine) lineDist = distance(last.pt, pt); else if (last.numberInLine < numberInLine) {
                LineString ls = ((RealLink) last.link.realLinks.elementAt(0)).geom;
                Point point = ls.getPoint(last.numberInLine + 1);
                lineDist = distance(last.pt, point);
                for (int i = last.numberInLine + 1; i < numberInLine; i++) lineDist += distance(ls.getPoint(i), ls.getPoint(i + 1));
                point = ls.getPoint(numberInLine);
                lineDist += distance(pt, point);
            } else if (last.numberInLine > numberInLine) {
                LineString ls = ((RealLink) last.link.realLinks.elementAt(0)).geom;
                Point point = ls.getPoint(last.numberInLine);
                lineDist = distance(last.pt, point);
                for (int i = numberInLine + 1; i < last.numberInLine; i++) lineDist += distance(ls.getPoint(i), ls.getPoint(i + 1));
                point = ls.getPoint(numberInLine + 1);
                lineDist += distance(pt, point);
            }
        } else {
            LineString lastls = ((RealLink) last.link.realLinks.elementAt(0)).geom;
            LineString ls = ((RealLink) link.realLinks.elementAt(0)).geom;
            boolean lastdirect, direct;
            if (equal(ls.getFirstPoint(), lastls.getFirstPoint())) {
                lastdirect = false;
                direct = true;
            } else if (equal(ls.getLastPoint(), lastls.getLastPoint())) {
                lastdirect = true;
                direct = false;
            } else if (equal(ls.getFirstPoint(), lastls.getLastPoint())) {
                lastdirect = true;
                direct = true;
            } else if (equal(ls.getLastPoint(), lastls.getFirstPoint())) {
                lastdirect = false;
                direct = false;
            } else throw new Exception("Link and last link are not connected");
            lineDist = 0;
            if (lastdirect) {
                Point point = lastls.getPoint(last.numberInLine + 1);
                lineDist += distance(last.pt, point);
                for (int i = last.numberInLine + 1; i < lastls.numPoints() - 1; i++) lineDist += distance(lastls.getPoint(i), lastls.getPoint(i + 1));
            } else {
                Point point = lastls.getPoint(last.numberInLine);
                lineDist += distance(last.pt, point);
                for (int i = 0; i < last.numberInLine; i++) lineDist += distance(lastls.getPoint(i), lastls.getPoint(i + 1));
            }
            if (direct) {
                Point point = ls.getPoint(numberInLine);
                lineDist += distance(pt, point);
                for (int i = 0; i < numberInLine; i++) lineDist += distance(ls.getPoint(i), ls.getPoint(i + 1));
            } else {
                Point point = ls.getPoint(numberInLine + 1);
                lineDist += distance(pt, point);
                for (int i = numberInLine + 1; i < ls.numPoints() - 1; i++) lineDist += distance(ls.getPoint(i), ls.getPoint(i + 1));
            }
        }
        lineDist = Math.abs(lineDist - distance(currentpt, lastpt));
    }

    /**
		 * Calcule la distance entre un lien et un point et cr�e un
		 * InternalLinkCoordinate avec ses valeurs
		 * En cas de multilignes, choisit le minimum de la distance
		 * du point aux lignes.
		 *
		 * @param x
		 * @param y
		 * @param link
		 */
    public void dist(Point pt, Link link) {
        InternalLinkCoordinate temp, best = null;
        LineString ls = ((RealLink) link.realLinks.elementAt(0)).geom;
        Point ptFrom;
        Point ptTo;
        double minDist = Double.MAX_VALUE;
        for (int p = 0; p < ls.numPoints() - 1; p++) {
            ptFrom = ls.getPoint(p);
            ptTo = ls.getPoint(p + 1);
            temp = distance(ptFrom, ptTo, pt, link);
            if (temp.dist < minDist) {
                best = temp;
                best.numberInLine = p;
                minDist = temp.dist;
            }
        }
        set(best.link, best.pt, best.offset, best.dist, best.numberInLine);
    }

    public void dist3(Point pt, Link link) {
        InternalLinkCoordinate temp = null;
        LineString ls = ((RealLink) link.realLinks.elementAt(0)).geom;
        Point ptFrom;
        Point ptTo;
        ptFrom = ls.getFirstPoint();
        ptTo = ls.getLastPoint();
        temp = distance(ptFrom, ptTo, pt, link);
        set(temp.link, temp.pt, temp.offset, temp.dist, temp.numberInLine);
    }

    /**
		 * Calcul la distance entre un point et un segment
		 * @return
		 */
    private InternalLinkCoordinate distance(Point pt1, Point pt2, Point pt, Link link) {
        InternalLinkCoordinate res = new InternalLinkCoordinate();
        double lx, ly, dx, dy, offset;
        lx = pt2.x - pt1.x;
        ly = pt2.y - pt1.y;
        dx = pt.x - pt1.x;
        dy = pt.y - pt1.y;
        offset = (lx * dx + ly * dy) / (lx * lx + ly * ly);
        if (offset < 0) offset = 0;
        if (offset > 1) offset = 1;
        res.link = link;
        res.offset = offset;
        res.pt.x = pt1.x + offset * lx;
        res.pt.y = pt1.y + offset * ly;
        dx = pt.x - res.pt.x;
        dy = pt.y - res.pt.y;
        res.dist = Math.sqrt(dx * dx + dy * dy);
        res.pt.m = pt.m;
        return res;
    }

    private double distance(Point p1, Point p2) {
        return Math.sqrt(sqr(p1.x - p2.x) + sqr(p1.y - p2.y));
    }

    private double sqr(double x) {
        return x * x;
    }

    private boolean equal(Point p1, Point p2) {
        return ((p1.x == p2.x) && (p1.y == p2.y));
    }
}
