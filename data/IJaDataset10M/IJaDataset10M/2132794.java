package de.javagis.jgis.util;

import de.javagis.jgis.geometry.Chain;
import de.javagis.jgis.geometry.Extent;
import de.javagis.jgis.geometry.GeoObject;
import de.javagis.jgis.geometry.Hole;
import de.javagis.jgis.geometry.MultiPolygon;
import de.javagis.jgis.geometry.Point;
import de.javagis.jgis.geometry.PointFactory;
import de.javagis.jgis.geometry.Polygon;
import de.javagis.jgis.geometry.Ring;
import de.javagis.jgis.geometry.Segment;
import and.awt.geom.*;
import java.util.*;
import and.awt.Shape;

/**
 * 
 * GeometryHelper
 * 
 * Funktion: Sammlung von Methoden zur L�sung geometrischer Probleme
 * 
 * @author Bjoern Koos, Michael Herter
 */
public abstract class GeometryHelper {

    /**
   * Nicht sichtbarer Konstruktor, da keine Instanz von Hilfsklasse erzeugt
   * werden soll
   */
    private GeometryHelper() {
    }

    /**
   * Konstante f�r Drehsinn, der gegen den Uhrzeigersinn l�uft.
   */
    public static final int DREHSINN_GEGENUHRZEIGER = 1;

    /**
   * Konstante f�r Drehsinn, der mit dem Uhrzeigersinn l�uft.
   */
    public static final int DREHSINN_MITUHRZEIGER = -1;

    /**
   * Gibt den Drehsinn einer Punktliste (Polygon) an.
   * @param points Punktliste
   * @return DREHSINN_GEGENUHRZEIGER oder DREHSINN_MITUHRZEIGER
   */
    public static int getDrehsinn(Point[] points) {
        double sum = 0;
        int size = points.length;
        for (int i = 1; i <= size; i++) {
            sum += getAngleBetween(points[(i - 1) % size], points[i % size], points[(i + 1) % size]);
        }
        Logger.log("Summe Innenwinkel = " + sum, Logger.DEBUG);
        if (sum > 0) {
            return DREHSINN_MITUHRZEIGER;
        }
        return DREHSINN_GEGENUHRZEIGER;
    }

    public static Extent mergeAllExtents(GeoObject[] objects) {
        if (objects == null) {
            return null;
        }
        Extent ext = null;
        for (int i = 0; i < objects.length; i++) {
            if (i == 0) {
                ext = objects[i].getExtent().copy();
            } else {
                ext.mergeExtent(objects[i].getExtent());
            }
        }
        return ext;
    }

    /**
     * 
     * @param x
     * @param y
     * @param ax
     * @param ay
     * @param bx
     * @param by
     * @return true, wenn x zwischen ax-bx und y zwischen ay-by liegt
     */
    private static boolean pointInsideExtent(double x, double y, double ax, double ay, double bx, double by) {
        return (ax <= x) && (x <= bx) && (ay <= y) && (y <= by);
    }

    public static boolean extentsOverlap(Extent a, Extent b) throws Exception {
        double a_xmin = a.getXmin();
        double a_ymin = a.getYmin();
        double a_xmax = a.getXmax();
        double a_ymax = a.getYmax();
        double b_xmin = b.getXmin();
        double b_ymin = b.getYmin();
        double b_xmax = b.getXmax();
        double b_ymax = b.getYmax();
        return (pointInsideExtent(a_xmin, a_ymin, b_xmin, b_ymin, b_xmax, b_ymax) || pointInsideExtent(a_xmax, a_ymin, b_xmin, b_ymin, b_xmax, b_ymax) || pointInsideExtent(a_xmax, a_ymax, b_xmin, b_ymin, b_xmax, b_ymax) || pointInsideExtent(a_xmin, a_ymax, b_xmin, b_ymin, b_xmax, b_ymax) || pointInsideExtent(b_xmin, b_ymin, a_xmin, a_ymin, a_xmax, a_ymax) || pointInsideExtent(b_xmax, b_ymin, a_xmin, a_ymin, a_xmax, a_ymax) || pointInsideExtent(b_xmax, b_ymax, a_xmin, a_ymin, a_xmax, a_ymax) || pointInsideExtent(b_xmin, b_ymax, a_xmin, a_ymin, a_xmax, a_ymax) || Extent2Polygon(a).intersects(Extent2Polygon(b)));
    }

    public static Polygon Extent2Polygon(Extent ext) throws Exception {
        double xmin = ext.getXmin();
        double xmax = ext.getXmax();
        double ymin = ext.getYmin();
        double ymax = ext.getYmax();
        Point[] pl = new Point[5];
        Point p1 = PointFactory.createPoint(xmin, ymin);
        pl[0] = p1;
        Point p2 = PointFactory.createPoint(xmax, ymin);
        pl[1] = p2;
        Point p3 = PointFactory.createPoint(xmax, ymax);
        pl[2] = p3;
        Point p4 = PointFactory.createPoint(xmin, ymax);
        pl[3] = p4;
        pl[4] = PointFactory.createPoint(xmin, ymin);
        Polygon poly = new Polygon(pl);
        return poly;
    }

    public static boolean isWithin(Extent queryExt, GeoObject obj) throws Exception {
        return GeometryHelper.isWithin(obj, GeometryHelper.Extent2Polygon(queryExt));
    }

    /** Liefert true, wenn sich zwei Segmente schneiden.
     * @param Segment segA
     * @param Segment segB
     * @return boolean
     */
    public static boolean intersects(Segment segA, Segment segB) {
        IntersectionInfo sI = generateIntersectionInfo(segA, segB);
        return (sI.getSchnittTyp() != IntersectionInfo.KEIN_SCHNITTPUNKT);
    }

    public static class IntersectionInfo {

        public static final int KEIN_SCHNITTPUNKT = 0;

        public static final int EIN_SCHNITTPUNKT = 1;

        public static final int UNENDLICH_SCHNITTPUNKT = 2;

        protected int typ = KEIN_SCHNITTPUNKT;

        protected Point point = null;

        public IntersectionInfo() {
        }

        public IntersectionInfo(int typ, Point p) {
            this.typ = typ;
            this.point = p;
        }

        public int getSchnittTyp() {
            return typ;
        }

        public void setSchnittTyp(int typ) {
            this.typ = typ;
            if ((typ == UNENDLICH_SCHNITTPUNKT) || (typ == KEIN_SCHNITTPUNKT)) {
                this.point = null;
            }
        }

        public Point getIntersectionPoint() {
            return point;
        }

        public void setIntersectionPoint(Point p) {
            this.setSchnittTyp(EIN_SCHNITTPUNKT);
            point = p;
        }
    }

    private static boolean between(double a_x, double a_y, double b_x, double b_y, double c_x, double c_y) {
        if (!collinear(a_x, a_y, b_x, b_y, c_x, c_y)) {
            return false;
        }
        if (a_x != b_x) {
            return (((a_x <= c_x) && (c_x <= b_x)) || ((a_x >= c_x) && (c_x >= b_x)));
        } else {
            return (((a_y <= c_y) && (c_y <= b_y)) || ((a_y >= c_y) && (c_y >= b_y)));
        }
    }

    /**
	 * @param extent
	 * @param queryExt
	 * @return
	 */
    public static boolean isWithin(Extent extent, Extent queryExt) {
        double a_xmin = extent.getXmin();
        double a_ymin = extent.getYmin();
        double a_xmax = extent.getXmax();
        double a_ymax = extent.getYmax();
        double q_xmin = queryExt.getXmin();
        double q_ymin = queryExt.getYmin();
        double q_xmax = queryExt.getXmax();
        double q_ymax = queryExt.getYmax();
        return pointInsideExtent(a_xmin, a_ymin, q_xmin, q_ymin, q_xmax, q_ymax) && pointInsideExtent(a_xmin, a_ymax, q_xmin, q_ymin, q_xmax, q_ymax) && pointInsideExtent(a_xmax, a_ymin, q_xmin, q_ymin, q_xmax, q_ymax) && pointInsideExtent(a_xmax, a_ymax, q_xmin, q_ymin, q_xmax, q_ymax);
    }

    /**
	 * @param extent
	 * @param queryExt
	 * @return
	 */
    public static boolean pointInside(Extent extent, Extent queryExt) {
        double a_xmin = extent.getXmin();
        double a_ymin = extent.getYmin();
        double a_xmax = extent.getXmax();
        double a_ymax = extent.getYmax();
        double q_xmin = queryExt.getXmin();
        double q_ymin = queryExt.getYmin();
        double q_xmax = queryExt.getXmax();
        double q_ymax = queryExt.getYmax();
        return pointInsideExtent(a_xmin, a_ymin, q_xmin, q_ymin, q_xmax, q_ymax) || pointInsideExtent(a_xmin, a_ymax, q_xmin, q_ymin, q_xmax, q_ymax) || pointInsideExtent(a_xmax, a_ymin, q_xmin, q_ymin, q_xmax, q_ymax) || pointInsideExtent(a_xmax, a_ymax, q_xmin, q_ymin, q_xmax, q_ymax);
    }

    /**
   * Berechnet den Winkel zwischen den Liniensegmenten (a, b) und (b, c)
   * @param a Anfangspunkt
   * @param b Mittlerer St�tzpunkt
   * @param c Endpunkt
   * @return Winkel
   */
    private static double getAngleBetween(Point a, Point b, Point c) {
        double x0 = a.getX();
        double y0 = a.getY();
        double x1 = b.getX();
        double y1 = b.getY();
        double x2 = c.getX();
        double y2 = c.getY();
        double erg = (x2 - x0) * (y1 - y0) - (x1 - x0) * (y2 - y0);
        return erg * 180 / Math.PI;
    }

    /**
   * Liefert true, wenn Point p innerhalb vom awtPolygon.
   * @param p Punkt
   * @param awtPoly Polygon
   * @return true, wenn Punkt innerhalb liegt
   */
    private static boolean isWithin(Point p, and.awt.geom.GeneralPath awtPoly) {
        return awtPoly.contains(p.getX(), p.getY());
    }

    /**
   * Liefert Kreis als Ellipse2D.Double.
   * @param center Point
   * @param radius Double
   * @return Ellipse2D.Double
   */
    public static Ellipse2D.Double createCircle(Point center, double radius) {
        Ellipse2D.Double circle = new Ellipse2D.Double(center.getX() - radius, center.getY() - radius, radius * 2, radius * 2);
        return circle;
    }

    /**
   * Liefert Extent eines Awt-Shapes.
   * @param s Shape
   * @return Extent
   */
    public static Extent returnExtentofAwtShape(Shape s) {
        return new Extent(s.getBounds2D().getMinX(), s.getBounds2D().getMinY(), s.getBounds2D().getMaxX(), s.getBounds2D().getMaxY());
    }

    /**
   * Liefert true, wenn alle Punkte der Punktliste pl innerhalb vom awtPoly.
   * @param pts Punkte
   * @param awtPoly Polygon
   * @return true, wenn alle Punkte innerhalb liegen
   */
    private static boolean isWithin(Point[] pts, and.awt.geom.GeneralPath awtPoly) {
        for (int i = 0; i < pts.length; i++) {
            if (!GeometryHelper.isWithin(pts[i], awtPoly)) {
                return false;
            }
        }
        return true;
    }

    /**
   * Liefert true, wenn GeoObjekt o in dem ContainerObj liegt. Wenn Geoobjekt
   * kein Fl�chenobjekt, dann gibt isWithin den Wert false zur�ck.
   * Fl�chenobjekt: Ring, Polygon, Hole oder MultiPolygon.
   * @param obj Objekt
   * @param containerObj Testobjekt
   * @return true, wenn obj innerhalb containerObj liegt
   */
    public static boolean isWithin(GeoObject obj, GeoObject containerObj) throws Exception {
        Point[] objPoints = obj.asPoints();
        if (containerObj instanceof Ring) {
            Ring ring = (Ring) containerObj;
            and.awt.geom.GeneralPath awtPoly = GeometryHelper.convertPolyToAwtPoly(ring);
            return GeometryHelper.isWithin(objPoints, awtPoly);
        } else if (containerObj instanceof MultiPolygon) {
            MultiPolygon multi = (MultiPolygon) containerObj;
            if (!multi.isClean()) {
                System.out.println("CLEAN now");
                try {
                    multi.clean();
                } catch (Exception e) {
                    System.out.println("Fehler beim Clean-Vorgang " + e);
                    e.printStackTrace();
                }
            }
            Polygon[] polys = multi.getPolygons();
            for (int i = 0; i < polys.length; i++) {
                Polygon poly = polys[i];
                and.awt.geom.GeneralPath awtPoly = GeometryHelper.convertPolyToAwtPoly(poly);
                if (GeometryHelper.isWithin(objPoints, awtPoly)) {
                    if (!multi.hasPolygonHoles(poly)) {
                        return true;
                    }
                    Hole[] holes = multi.getPolygonHoles(poly);
                    for (int n = 0; n < holes.length; n++) {
                        Hole h = holes[n];
                        and.awt.geom.GeneralPath awtHole = GeometryHelper.convertPolyToAwtPoly(h);
                        for (int f = 0; f < objPoints.length; f++) {
                            if (GeometryHelper.isWithin(objPoints[f], awtHole)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
   * Schnitt-Test f�r Segmente
   * @param segments Segemte
   * @return true, wenn sich Segmente einander schneiden
   */
    public static boolean intersects(Segment[] segments) {
        for (int i = 0; i < segments.length; i++) {
            for (int j = 0; j < i; j++) {
                if (GeometryHelper.intersects(segments[i], segments[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * Liefert true, wenn ein Segment der Segmentliste segments das Segment seg
   * schneidet
   * @param segments Segmentliste
   * @param seg Testsegment
   * @return true, wenn ein Segment des Arrays das Segment seg schneidet
   */
    public static boolean intersects(Segment[] segments, Segment seg) {
        for (int i = 0; i < segments.length; i++) {
            if (intersects(seg, segments[i])) {
                return true;
            }
        }
        return false;
    }

    /**
   * Test auf Schnitt zwischen GeoObjekt und Segment
   * @param obj GeoObjekt
   * @param seg Segment
   * @return true, wenn sich ein GeoObjekt mit einem Segment schneidet
   */
    public static boolean intersects(GeoObject obj, Segment seg) {
        Segment[] segments = obj.getSegments();
        if (segments != null) {
            return intersects(segments, seg);
        }
        Point[] pl = obj.asPoints();
        for (int i = 0; i < pl.length; i++) {
            if (intersects(seg, pl[i])) {
                return true;
            }
        }
        return false;
    }

    /**
   * Schnitt-Test zwischen GeoObjekt und Punkt
   * @param obj GeoObjekt
   * @param p Punkt
   * @return true, wenn ein Punkt ein GeoObjekt schneidet (=ber�hrt).
   */
    public static boolean intersects(GeoObject obj, Point p) {
        Segment[] segments = obj.getSegments();
        if (segments != null) {
            for (int i = 0; i < segments.length; i++) {
                if (GeometryHelper.returnDistance(segments[i], p) == 0.0) {
                    return true;
                }
            }
        } else {
            Point[] pl = obj.asPoints();
            for (int i = 0; i < pl.length; i++) {
                if (p.equals(pl[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * Liefert die Distanz zwischen Segment und Punkt. Wenn Distanz = 0, dann
   * liegt Punkt auf dem Segment
   * @param seg Segment
   * @param p Punkt
   * @return Entfernung
   */
    public static double returnDistance(Segment seg, Point p) {
        Point2D.Double pd = GeometryHelper.convertPointToAwtPoint(p);
        Line2D.Double line = GeometryHelper.convertSegmentToAwtLine(seg);
        return line.ptLineDist(pd);
    }

    /**
   * Liefert die Distanz zwischen zwei Punkten.
   * @param p1 Point
   * @param p2 Point
   * @return Entfernung
   */
    public static double returnDistance(Point p1, Point p2) {
        Point2D.Double pd1 = GeometryHelper.convertPointToAwtPoint(p1);
        Point2D.Double pd2 = GeometryHelper.convertPointToAwtPoint(p2);
        return pd1.distance(pd2);
    }

    /**
   * Test auf �berkreuzung von Segmenten
   * @param segments Segmente
   * @return true, wenn sich Segmente in der Segmentliste kreuzen. d.h. es
   * entsteht ein neuer Schnittpunkt.
   */
    public static boolean crosses(Segment[] segments) {
        for (int i = 0; i < segments.length; i++) {
            for (int j = 0; j < i; j++) {
                if (crosses(segments[i], segments[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * Test auf Kreuzung von Segmenten
   * @param segA Segment
   * @param segB Segment
   * @return true, wenn Segment A und Segment B kreuzen, d.h. es entsteht ein
   * neuer Schnittpunkt, der weder Anfangs- oder Endpunkt von Segment A noch von
   * Segment B ist.
   */
    public static boolean crosses(Segment segA, Segment segB) {
        Point p = returnSegmentIntersection(segA, segB);
        if (p == null) {
            return false;
        }
        return (!(isConnected(segA, segB) || touches(segA, segB, p)));
    }

    /**
   * Test auf Ber�hrung der Segmente
   * @param segA erstes Segment
   * @param segB zweites Segment
   * @return true, wenn sich beide ber�hren (und nicht kreuzen)
   */
    public static boolean touches(Segment segA, Segment segB) {
        return touches(segA, segB, returnSegmentIntersection(segA, segB));
    }

    /**
   * Test auf Ber�hrung zweier Segmente, deren Schnittpunkt bereits ermittelt
   * ist
   * @param segA erstes Segment
   * @param segB zweites Segment
   * @param iP evtl. Schnittpunkt der beiden Segmente
   * @return true, wenn Ber�hrung vorliegt
   */
    private static boolean touches(Segment segA, Segment segB, Point iP) {
        if (iP == null) {
            return false;
        }
        return (isConnected(iP, segA) ^ isConnected(iP, segB));
    }

    /**
   * Test auf Verbindung beider Segmente
   * @param segA erstes Segment
   * @param segB zweites Segment
   * @return true, wenn Segmente einen gemeinsamen St�tzpunkt haben
   */
    public static boolean isConnected(Segment segA, Segment segB) {
        return (isConnected(segA.getStartpunkt(), segB) || isConnected(segA.getEndpunkt(), segB));
    }

    /**
   * Test auf Verbindung von Punkt und Segment
   * @param p Punkt
   * @param segB Segment
   * @return true, wenn Punkt p ein St�tzpunkt des Segments ist
   */
    private static boolean isConnected(Point p, Segment segB) {
        return (p.equals(segB.getStartpunkt()) || p.equals(segB.getEndpunkt()));
    }

    /**
   * L�st eine Kette in sich nicht schneidende Ringe auf
   * @param c Kette
   * @return Array von Ringen, die sich nicht schneiden
   * @throws Exception im Fehlerfall
   */
    public static Ring[] dissolve(Chain c) throws Exception {
        Segment[] s = c.getSegments();
        return (Ring[]) dissolve(s, 0).toArray(new Ring[0]);
    }

    /**
   * Hilfsmethode zur Aufl�sung von Schnitten in einem Ring. Arbeitet rekursiv.
   * @param segments Segmente, die noch Schnitt haben k�nnen
   * @param recursionLevel Z�hler der Rekursionstiefe
   * @return Liste mit sich nicht schneidenden Ringen
   * @throws Exception im Fehlerfall
   */
    private static List dissolve(Segment[] segments, int recursionLevel) throws Exception {
        List erg = new ArrayList();
        Logger.log("Recursion " + recursionLevel, Logger.DEBUG);
        if (segments.length <= 3) {
            if (segments.length == 3) {
                segments = cleanNeighbourhoodSegments(segments);
                try {
                    erg.add(new Ring(asPoints(segments)));
                } catch (Exception ex) {
                    Logger.log("Fehler? bei dissolve() " + ex.getLocalizedMessage(), Logger.WARNING);
                }
                return erg;
            }
            Logger.log("Weniger als 3 Segmente in dissolve() !", Logger.ERROR);
            return null;
        }
        if (intersectsInner(segments)) {
            System.out.println("Segmente schneiden sich.. dissolve()...");
            List teile = splitAtFirstIntersection(segments);
            if (recursionLevel > 10) {
                System.out.println("Rekursion " + recursionLevel);
            }
            if (teile.size() < 2) {
                System.out.println("FEHLER: INTERSECTION DETECTED BUT NOT RESOLVED DURING DISSOLVE!!!");
                erg.add(new Ring(asPoints(segments)));
            } else {
                for (int i = 0; i < teile.size(); i++) {
                    List teilergebnis = dissolve((Segment[]) teile.get(i), recursionLevel + 1);
                    if (teilergebnis != null) {
                        erg.addAll(teilergebnis);
                    }
                }
            }
        } else {
            erg.add(new Ring(asPoints(segments)));
        }
        return erg;
    }

    /**
   * @return true, wenn Extends an ganz verschiedenen Orten liegen, 
   * z.B. komplett der Extents des Objekts ganz links/rechts/oben/unten vom Suchextent
   */
    public static boolean extentOutside(Extent objExt, Extent queryExt) {
        double objXmin = objExt.getXmin();
        double objXmax = objExt.getXmax();
        double objYmin = objExt.getYmin();
        double objYmax = objExt.getYmax();
        double quXmin = queryExt.getXmin();
        double quXmax = queryExt.getXmax();
        double quYmin = queryExt.getYmin();
        double quYmax = queryExt.getYmax();
        return (quXmax < objXmin || quXmin > objXmax || quYmax < objYmin || quYmin > objYmax);
    }

    /**
   * Hilfsfunktion, die Segment in zwei Segmente teilt. Neuer St�tzpunkt beider
   * Segmenth�lften wird Punkt p
   * @param seg zu teilendes Segment
   * @param p neuer St�tzpunkt
   * @return Array mit zwei neuen Segmenten
   */
    private static Segment[] splitSegment(Segment seg, Point p) {
        return new Segment[] { new Segment(seg.getStartpunkt(), p), new Segment(p, seg.getEndpunkt()) };
    }

    /**
   * Konvertiert Ring in ein awtPolygon (=GeneralPath)
   * @param r Ring
   * @return GeneralPath
   */
    public static and.awt.geom.GeneralPath convertPolyToAwtPoly(Ring r) {
        Segment[] segmente = r.getSegments();
        and.awt.geom.GeneralPath awtPoly = new and.awt.geom.GeneralPath();
        for (int i = 0; i < segmente.length; i++) {
            awtPoly.append(GeometryHelper.convertSegmentToAwtLine(segmente[i]), true);
        }
        return awtPoly;
    }

    /**
   * Konvertiert ein Segment in ein Java2D-Linienobjekt
   * @param segment umzuwandelndes Segment
   * @return Linie
   */
    public static and.awt.geom.Line2D.Double convertSegmentToAwtLine(Segment segment) {
        Point start = segment.getStartpunkt();
        Point end = segment.getEndpunkt();
        and.awt.geom.Line2D.Double line2d = new and.awt.geom.Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY());
        return line2d;
    }

    /**
   * Wandelt Punktobjekt in Java2D-Punkt um
   * @param p jgis-Punkt
   * @return Java2D-Punkt
   */
    public static Point2D.Double convertPointToAwtPoint(Point p) {
        return new Point2D.Double(p.getX(), p.getY());
    }

    /**
   * Liefert die St�tzpunkt der Segmente
   * @param segments Segmente
   * @return St�tzpunkte
   */
    private static Point[] asPoints(Segment[] segments) {
        List liste = new ArrayList();
        liste.add(segments[0].getStartpunkt());
        for (int i = 0; i < segments.length; i++) {
            liste.add(segments[i].getEndpunkt());
        }
        return (Point[]) liste.toArray(new Point[segments.length + 1]);
    }

    /**
   * Bereinigt 3 Segmente, die im Moment nicht mit einander verbunden sind
   * @param segments Segmente
   * @return Verbundene Segmente (mit gemeinsamen St�tzpunkten)
   * @throws Exception im Fehlerfall
   */
    private static Segment[] cleanNeighbourhoodSegments(Segment[] segments) throws Exception {
        if ((segments == null) || (segments.length != 3)) {
            return segments;
        }
        if (!segments[0].getEndpunkt().equals(segments[1].getStartpunkt())) {
            segments[0] = new Segment(segments[0].getStartpunkt(), (Point) segments[1].getStartpunkt().copy());
        } else if (!segments[1].getEndpunkt().equals(segments[2].getStartpunkt())) {
            segments[1] = new Segment(segments[1].getStartpunkt(), (Point) segments[2].getStartpunkt().copy());
        } else if (!segments[2].getEndpunkt().equals(segments[0].getStartpunkt())) {
            segments[2] = new Segment(segments[2].getStartpunkt(), (Point) segments[0].getStartpunkt().copy());
        }
        return segments;
    }

    /**
   * Teilt die Segmente beim ersten Schnittpunkt auf.
   * @param segments Segmente
   * @return List mit Segment[]-Eintraegen
   */
    private static List splitAtFirstIntersection(Segment[] segments) {
        List erg = new ArrayList();
        Segment aktSegment;
        Segment testSegment;
        Point p;
        for (int i = 0; i < segments.length; i++) {
            aktSegment = segments[i];
            for (int k = 0; k < (i - 1); k++) {
                testSegment = segments[k];
                if ((p = aktSegment.returnIntersectionPoint(testSegment)) != null) {
                    Segment[] ergK = splitSegment(testSegment, p);
                    Segment[] ergI = splitSegment(aktSegment, p);
                    List ersterTeil = new ArrayList();
                    ersterTeil.add(ergK[1]);
                    for (int ersterIndex = k + 1; ersterIndex < i; ersterIndex++) {
                        ersterTeil.add(segments[ersterIndex]);
                    }
                    ersterTeil.add(ergI[0]);
                    List zweiterTeil = new ArrayList();
                    for (int zweiterIndex = 0; zweiterIndex < k; zweiterIndex++) {
                        zweiterTeil.add(segments[zweiterIndex]);
                    }
                    zweiterTeil.add(ergK[0]);
                    zweiterTeil.add(ergI[1]);
                    for (int zweiterIndex = i + 1; zweiterIndex < segments.length; zweiterIndex++) {
                        zweiterTeil.add(segments[zweiterIndex]);
                    }
                    erg.add(ersterTeil.toArray(new Segment[ersterTeil.size()]));
                    erg.add(zweiterTeil.toArray(new Segment[zweiterTeil.size()]));
                    return erg;
                }
            }
        }
        erg.add(segments);
        return erg;
    }

    /**
   * Test auf Kollinearit�t
   * @param aX X-Koordinate des Punktes a
   * @param aY Y-Koordinate des Punktes a
   * @param bX X-Koordinate des Punktes b
   * @param bY Y-Koordinate des Punktes b
   * @param cX X-Koordinate des Punktes c
   * @param cY Y-Koordinate des Punktes c
   * @return true, wenn das Segment [a,b] und ein Punkt c auf einer Geraden
   * liegen
   */
    private static boolean collinear(double aX, double aY, double bX, double bY, double cX, double cY) {
        return (calculateArea(aX, aY, bX, bY, cX, cY) == 0);
    }

    /**
   * Kollinearit�tstest f�r Segmente [a,b] und [c,d]
   * @param aX X-Koordinate des Punktes a
   * @param aY Y-Koordinate des Punktes a
   * @param bX X-Koordinate des Punktes b
   * @param bY Y-Koordinate des Punktes b
   * @param cX X-Koordinate des Punktes c
   * @param cY Y-Koordinate des Punktes c
   * @param dX X-Koordinate des Punktes d
   * @param dY Y-Koordinate des Punktes d
   * @return true, wenn [a,b] und [c,d] parallel sind (die gleiche steigung
   * besitzen)
   */
    private static boolean collinear(double aX, double aY, double bX, double bY, double cX, double cY, double dX, double dY) {
        double deltaxAB = bX - aX;
        double deltayAB = bY - aY;
        double deltaxCD = dX - cX;
        double deltayCD = dY - cY;
        if (deltayAB == 0.0 && deltayCD == 0.0) {
            return true;
        }
        return (deltaxAB / deltayAB == deltaxCD / deltayCD);
    }

    /**
   * Liefert die Fl�che -/+ zwischen abc, wenn Fl�che - dann Punkt c rechts wenn
   * Fl�che + dann Lage von Punkt c rechts
   * @param aX X-Koordinate des Punktes a
   * @param aY Y-Koordinate des Punktes a
   * @param bX X-Koordinate des Punktes b
   * @param bY Y-Koordinate des Punktes b
   * @param cX X-Koordinate des Punktes c
   * @param cY Y-Koordinate des Punktes c
   * @return Fl�che
   */
    private static double calculateArea(double aX, double aY, double bX, double bY, double cX, double cY) {
        return (((aX * bY) - (aY * bX) + (aY * cX)) - (aX * cY) + (bX * cY)) - (cX * bY);
    }

    /**
   * Liefert die Fl�che eines Polygons oder Holes
   * @param r Ring
   * @return Fl�che
   */
    public static double calculateArea(Ring r) {
        throw new RuntimeException("Noch nicht implementiert!");
    }

    /**
   * Testet, ob sich zwei Segmente schneiden und gibt die Information zur�ck
   * @param s1 erstes Segment
   * @param s2 zweites Segment
   * @return Schnittinformationen
   */
    public static IntersectionInfo generateIntersectionInfo(Segment s1, Segment s2) {
        IntersectionInfo sI = new IntersectionInfo();
        Point startS1 = s1.getStartpunkt();
        Point endS1 = s1.getEndpunkt();
        Point startS2 = s2.getStartpunkt();
        Point endS2 = s2.getEndpunkt();
        double ax = startS1.getX();
        double ay = startS1.getY();
        double bx = endS1.getX();
        double by = endS1.getY();
        double cx = startS2.getX();
        double cy = startS2.getY();
        double dx = endS2.getX();
        double dy = endS2.getY();
        double nenner = ((bx - ax) * (dy - cy)) - ((by - ay) * (dx - cx));
        if (nenner == 0.0) {
            if ((ax == bx) && (ay == by)) {
                if (between(cx, cy, dx, dy, ax, ay)) {
                    sI.setIntersectionPoint(PointFactory.createPoint(ax, ay));
                }
            } else if ((cx == dx) && (cy == dy)) {
                if (between(ax, ay, bx, by, cx, cy)) {
                    sI.setIntersectionPoint(PointFactory.createPoint(cx, cy));
                }
            } else {
                if (collinear(ax, ay, bx, by, cx, cy, dx, dy)) {
                    if (between(ax, ay, bx, by, cx, cy) || between(ax, ay, bx, by, dx, dy) || between(cx, cy, dx, dy, ax, ay) || between(cx, cy, dx, dy, bx, by)) {
                        sI.setSchnittTyp(IntersectionInfo.UNENDLICH_SCHNITTPUNKT);
                    } else {
                        sI.setSchnittTyp(IntersectionInfo.KEIN_SCHNITTPUNKT);
                    }
                } else {
                    Logger.log("FEHLER???", Logger.ERROR);
                    throw new RuntimeException("Fehler in Schnittberechnung ?! unbehandelter Sonderfall !");
                }
            }
        } else {
            double fs1 = (((dy - cy) * (cx - ax)) + ((dx - cx) * (ay - cy))) / nenner;
            double fs2 = (((by - ay) * (cx - ax)) + ((bx - ax) * (ay - cy))) / nenner;
            if ((fs1 >= 0.0) && (fs1 <= 1.0) && (fs2 >= 0.0) && (fs2 <= 1.0)) {
                sI.setSchnittTyp(IntersectionInfo.EIN_SCHNITTPUNKT);
                double schnittx = cx + (fs2 * (dx - cx));
                double schnitty = cy + (fs2 * (dy - cy));
                sI.setIntersectionPoint(PointFactory.createPoint(schnittx, schnitty));
            } else {
                sI.setSchnittTyp(IntersectionInfo.KEIN_SCHNITTPUNKT);
            }
        }
        return sI;
    }

    private static boolean Quick_collinear(double a_x, double a_y, double b_x, double b_y, double c_x, double c_y) {
        return (Quick_Area2(a_x, a_y, b_x, b_y, c_x, c_y) == 0);
    }

    private static boolean Quick_Xor(boolean a, boolean b) {
        return !(a && b) && (a || b);
    }

    private static boolean Quick_Left(double a_x, double a_y, double b_x, double b_y, double c_x, double c_y) {
        return Quick_Area2(a_x, a_y, b_x, b_y, c_x, c_y) > 0;
    }

    private static double Quick_Area2(double a_x, double a_y, double b_x, double b_y, double c_x, double c_y) {
        return (((a_x * b_y) - (a_y * b_x) + (a_y * c_x)) - (a_x * c_y) + (b_x * c_y)) - (c_x * b_y);
    }

    private static boolean Quick_betweenInner(double a_x, double a_y, double b_x, double b_y, double c_x, double c_y) {
        if (!Quick_collinear(a_x, a_y, b_x, b_y, c_x, c_y)) {
            return false;
        }
        if (a_x != b_x) {
            return (((a_x < c_x) && (c_x < b_x)) || ((a_x > c_x) && (c_x > b_x)));
        } else {
            return (((a_y < c_y) && (c_y < b_y)) || ((a_y > c_y) && (c_y > b_y)));
        }
    }

    private static boolean Quick_intersectProp(double a_x, double a_y, double b_x, double b_y, double c_x, double c_y, double d_x, double d_y) {
        if (Quick_collinear(a_x, a_y, b_x, b_y, c_x, c_y) || Quick_collinear(a_x, a_y, b_x, b_y, d_x, d_y) || Quick_collinear(c_x, c_y, d_x, d_y, a_x, a_y) || Quick_collinear(c_x, c_y, d_x, d_y, b_x, b_y)) {
            return false;
        }
        return Quick_Xor(Quick_Left(a_x, a_y, b_x, b_y, c_x, c_y), Quick_Left(a_x, a_y, b_x, b_y, d_x, d_y)) && Quick_Xor(Quick_Left(c_x, c_y, d_x, d_y, a_x, a_y), Quick_Left(c_x, c_y, d_x, d_y, b_x, b_y));
    }

    public static boolean intersectsInner(GeoObject o, Segment s) {
        boolean is = false;
        Segment[] segments = o.getSegments();
        if (segments != null) {
            Point startp = s.getStartpunkt();
            Point endp = s.getEndpunkt();
            double a_x = startp.getX();
            double a_y = startp.getY();
            double b_x = endp.getX();
            double b_y = endp.getY();
            for (int i = 0; i < segments.length; i++) {
                Point startp_t = segments[i].getStartpunkt();
                Point endp_t = segments[i].getEndpunkt();
                double c_x = startp_t.getX();
                double c_y = startp_t.getY();
                double d_x = endp_t.getX();
                double d_y = endp_t.getY();
                if (Quick_intersectProp(a_x, a_y, b_x, b_y, c_x, c_y, d_x, d_y)) {
                    return true;
                } else if (Quick_betweenInner(a_x, a_y, b_x, b_y, c_x, c_y) || Quick_betweenInner(a_x, a_y, b_x, b_y, d_x, d_y) || Quick_betweenInner(c_x, c_y, d_x, d_y, a_x, a_y) || Quick_betweenInner(c_x, c_y, d_x, d_y, b_x, b_y)) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            Point[] pl = o.asPoints();
            for (int i = 0; i < pl.length; i++) {
                if (GeometryHelper.returnDistance(s, pl[i]) == 0.0) {
                    return true;
                }
            }
        }
        return is;
    }

    public static boolean intersectsInner(Segment[] segments) {
        for (int i = 0; i < segments.length; i++) {
            for (int j = 0; j < i; j++) {
                if (GeometryHelper.intersectsInner(segments[i], segments[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * Gibt Schnittpunkt der beiden Segmente zur�ck
   * @param segA erstes Segment
   * @param segB zweites Segment
   * @return Schnittpunkt, wenn vorhanden, sonst null
   */
    public static Point returnSegmentIntersection(Segment segA, Segment segB) {
        IntersectionInfo sI = generateIntersectionInfo(segA, segB);
        if (sI.getSchnittTyp() == IntersectionInfo.EIN_SCHNITTPUNKT) {
            return sI.getIntersectionPoint();
        }
        return null;
    }

    /**
   * Konvertiert Pixel in Weltkoordinaten
   */
    public static double convertFromPixels(double coord, double min, double max, double anzpixel) {
        return ((coord / anzpixel) * (max - min)) + min;
    }

    /** 
     * Konvertiert WeltKoordinaten in Pixel
     */
    public static double convertToPixels(double coord, double min, double max, double anzpixel) {
        return (coord - min) * (anzpixel / (max - min));
    }

    /**
	 * @param extent
	 * @param queryExt
	 * @return
	 */
    public static boolean extentInside(Extent extent, Extent queryExt) {
        double a_xmin = extent.getXmin();
        double a_ymin = extent.getYmin();
        double a_xmax = extent.getXmax();
        double a_ymax = extent.getYmax();
        double q_xmin = queryExt.getXmin();
        double q_ymin = queryExt.getYmin();
        double q_xmax = queryExt.getXmax();
        double q_ymax = queryExt.getYmax();
        return pointInsideExtent(a_xmin, a_ymin, q_xmin, q_ymin, q_xmax, q_ymax) && pointInsideExtent(a_xmin, a_ymax, q_xmin, q_ymin, q_xmax, q_ymax) && pointInsideExtent(a_xmax, a_ymin, q_xmin, q_ymin, q_xmax, q_ymax) && pointInsideExtent(a_xmax, a_ymax, q_xmin, q_ymin, q_xmax, q_ymax);
    }
}
