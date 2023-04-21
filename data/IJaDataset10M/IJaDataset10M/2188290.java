package TangramCore;

import java.awt.BasicStroke;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

public class Obrys extends Area {

    public static final float TOLERANCE_AREA = 1e-3f;

    public static final double TOLERANCE_AREA2 = 1e-3, TOLERANCE_UHEL = 1, TOLERANCE_UHEL_RAD = Math.toRadians(TOLERANCE_UHEL);

    private static final int SEG_MEZERA = -1, SEG_NONE = -2;

    private static final int INV_ZMENA = 1, INV_VALID = 0;

    private static final Stroke cachedStroke = new BasicStroke(TOLERANCE_AREA, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public Skladacka skladacka;

    public ObrysData od;

    private int invalidate;

    private Dilek dilkyVzor[];

    private Dilek.Validity dilkyValid[];

    public static class ObrysData {

        public int segType[], nSegs = 0;

        public Point2D segCoords[];

        public double insideAngle[], segLength[];

        public ObrysData subShape[] = null;

        public Area a = null;

        public Area aStrokedCache = null;

        public double cacheTol;

        private static Point2D mid = new Point2D(), perp = new Point2D(), ptest = new Point2D();

        void saveToFile(DataOutputStream out) throws IOException {
            out.writeInt(nSegs);
            if (subShape == null) {
                out.writeInt(1);
                for (int i = 0; i < nSegs; i++) {
                    out.writeInt(segType[i]);
                    out.writeDouble(segCoords[i].x);
                    out.writeDouble(segCoords[i].y);
                    out.writeDouble(insideAngle[i]);
                }
            } else {
                out.writeInt(subShape.length);
                for (int i = 0; i < subShape.length; i++) subShape[i].saveToFile(out);
            }
        }

        void readFromFile(DataInputStream in) throws IOException {
            nSegs = in.readInt();
            if (nSegs < 0) return;
            int subCount = in.readInt();
            segType = new int[nSegs];
            segCoords = new Point2D[nSegs];
            insideAngle = new double[nSegs];
            if (subCount == 1) {
                for (int i = 0; i < nSegs; i++) {
                    segType[i] = in.readInt();
                    segCoords[i] = new Point2D(in.readDouble(), in.readDouble());
                    insideAngle[i] = in.readDouble();
                }
            } else {
                subShape = new ObrysData[subCount];
                for (int i = 0, j = 0; i < subCount; i++) {
                    subShape[i] = new ObrysData();
                    subShape[i].readFromFile(in);
                    for (int k = 0; k < subShape[i].nSegs; k++) {
                        segType[j] = subShape[i].segType[k];
                        segCoords[j] = subShape[i].segCoords[k];
                        insideAngle[j] = subShape[i].insideAngle[k];
                        j++;
                    }
                }
            }
        }

        public boolean isEmpty() {
            return nSegs <= 0;
        }

        private static Point2D findInsidePoint(Point2D dest, Polygon2D pol, Point2D p1, Point2D p2) {
            Vect.midpoint(mid, p1, p2);
            Vect.normal(perp, p1, p2);
            Vect.normalize(perp, TOLERANCE_AREA);
            int count = 0;
            do {
                count++;
                ptest.copyFrom(mid);
                ptest.translate(perp);
                if (pol.contains(ptest)) {
                    dest.copyFrom(ptest);
                    return dest;
                }
                Vect.negative(perp);
                ptest.copyFrom(mid);
                ptest.translate(perp);
                if (pol.contains(ptest)) {
                    dest.copyFrom(ptest);
                    return dest;
                }
                Vect.midpoint(mid, p1, mid);
            } while (mid.distance(p1) > Obrys.TOLERANCE_AREA);
            return null;
        }

        @SuppressWarnings("unchecked")
        public boolean initSubShapes() {
            int count = 0;
            for (int i = 0; i < nSegs; i++) if (segType[i] == PathIterator.SEG_MOVETO) count++;
            insideAngle = new double[nSegs];
            segLength = new double[nSegs];
            Polygon2D sub[] = new Polygon2D[count];
            Point2D subPoint[] = new Point2D[count];
            int subStartSeg[] = new int[count];
            int nSubSegs[] = new int[count];
            int subParent[] = new int[count];
            boolean isHole[] = new boolean[count];
            count = 0;
            int start = 0;
            for (int i = 1, j = 0; i <= nSegs; i++) {
                if (i == nSegs || segType[i] == PathIterator.SEG_MOVETO) {
                    Point2D coords[] = new Point2D[j];
                    subStartSeg[count] = start;
                    nSubSegs[count] = j + 1;
                    for (j = 0; j < coords.length; j++) coords[j] = new Point2D(segCoords[start + j]);
                    sub[count] = new Polygon2D(coords);
                    Point2D pin = new Point2D();
                    int seg = 0;
                    do {
                        subPoint[count] = findInsidePoint(pin, sub[count], coords[seg], coords[seg + 1]);
                        seg++;
                    } while (subPoint[count] == null && seg < (j - 1));
                    if (subPoint[count] == null) {
                        subPoint[count] = coords[0];
                        System.out.println("error: cannot find inside point, forced to first outline point");
                    }
                    subParent[count] = -1;
                    isHole[count] = false;
                    count++;
                    j = 0;
                    start = i;
                } else j++;
            }
            for (int i = 0; i < sub.length; i++) for (int j = 0; j < sub.length; j++) {
                if (i == j) continue;
                if (sub[i].contains(subPoint[j])) {
                    if (subParent[j] == -1 || sub[subParent[j]].contains(subPoint[i])) subParent[j] = i;
                    isHole[j] = !isHole[j];
                }
            }
            Point2D pin = new Point2D();
            for (int i = 0; i < sub.length; i++) {
                int p1i = subStartSeg[i];
                int j = p1i + 1, last = p1i + nSubSegs[i];
                Point2D p0 = segCoords[last - 2], p1 = segCoords[p1i], p2;
                for (; j < last; j++) {
                    p2 = segCoords[j];
                    double angle = Math.asin((p1.y - p0.y) / p0.distance(p1));
                    if ((p1.x - p0.x) < 0) angle = Math.PI - angle;
                    segLength[j] = p1.distance(p2);
                    double angle2 = Math.asin((p2.y - p1.y) / segLength[j]);
                    if ((p2.x - p1.x) < 0) angle2 = Math.PI - angle2;
                    angle -= angle2;
                    angle = Math.PI - angle;
                    if (angle < 0) angle += 2 * Math.PI; else if (angle > 2 * Math.PI) angle -= 2 * Math.PI;
                    if (findInsidePoint(pin, sub[i], p0, p1) != null) {
                        double a = p1.y - p0.y, b = p0.x - p1.x, c = -(a * p0.x + b * p0.y);
                        if ((((a * p2.x + b * p2.y + c) > 0 == (a * pin.x + b * pin.y + c) < 0) ^ isHole[i]) && angle < Math.PI) angle = 2 * Math.PI - angle;
                    } else {
                        angle = 2 * Math.PI - angle;
                        System.out.println("mensi problem, jedna hrana je jakasi divna, jelikoz neinciduje s vnitrnim bodem");
                    }
                    insideAngle[p1i] = angle;
                    p0 = p1;
                    p1 = p2;
                    p1i = j;
                }
                insideAngle[last - 1] = insideAngle[subStartSeg[i]];
            }
            if (count < 2) return false;
            int subObrys[] = new int[sub.length];
            count = 0;
            for (int i = 0; i < sub.length; i++) {
                if (!isHole[i]) {
                    subObrys[i] = count;
                    count++;
                } else subObrys[i] = -1;
            }
            if (count == 1) return false;
            Vector obrys[] = new Vector[count];
            int obrysPointCount[] = new int[count];
            for (int i = 0; i < sub.length; i++) if (subObrys[i] > -1) {
                int j = subObrys[i];
                obrys[j] = new Vector();
                obrys[j].add(i);
                obrysPointCount[j] += nSubSegs[i];
            }
            for (int i = 0; i < sub.length; i++) if (subObrys[i] == -1) {
                int j = subObrys[subParent[i]];
                obrys[j].add(i);
                obrysPointCount[j] += nSubSegs[i];
            }
            subShape = new ObrysData[obrys.length];
            for (int i = 0; i < obrys.length; i++) {
                subShape[i] = new ObrysData();
                subShape[i].nSegs = count = obrysPointCount[i];
                subShape[i].segCoords = new Point2D[count];
                subShape[i].segType = new int[count];
                subShape[i].insideAngle = new double[count];
                int segIndex = 0;
                for (int j = obrys[i].size() - 1; j >= 0; j--) {
                    int subIndex = (Integer) obrys[i].get(j);
                    for (int k = 0; k < nSubSegs[subIndex]; k++) {
                        int orig = subStartSeg[subIndex] + k;
                        subShape[i].segCoords[segIndex] = segCoords[orig];
                        subShape[i].segType[segIndex] = segType[orig];
                        subShape[i].insideAngle[segIndex] = insideAngle[orig];
                        segIndex++;
                    }
                }
            }
            return true;
        }

        public boolean hasIncompatibleSide(double shortestSide, double smallestAngle) {
            for (int i = 0; i < nSegs; i++) {
                if (segType[i] == PathIterator.SEG_MOVETO) continue;
                if (segLength[i] < shortestSide && insideAngle[i - 1] < Math.PI && insideAngle[i] < Math.PI) return true;
                if (insideAngle[i - 1] < smallestAngle) return true;
            }
            return false;
        }

        public void debugSegTypes() {
            for (int i = 0; i < nSegs; i++) if (segType[i] == PathIterator.SEG_MOVETO) System.out.print("MOVETO "); else if (segType[i] == PathIterator.SEG_LINETO) System.out.print("LINETO "); else System.out.print("OTHER ");
            System.out.println("");
        }

        public Shape getShape() {
            if (nSegs == 0) return new Rectangle(0, 0);
            GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD, nSegs);
            for (int i = 0; i < nSegs; i++) {
                if (segType[i] == PathIterator.SEG_MOVETO) gp.moveTo((float) segCoords[i].x, (float) segCoords[i].y); else if (segType[i] == PathIterator.SEG_LINETO) gp.lineTo((float) segCoords[i].x, (float) segCoords[i].y);
            }
            gp.closePath();
            a = new Area(gp);
            aStrokedCache = null;
            return gp;
        }
    }

    private static Area getTolDilek(Dilek d) {
        Area ret = new Area(d);
        ret.add(new Area(cachedStroke.createStrokedShape(d)));
        return ret;
    }

    private static Area getArea(Dilek d[]) {
        Area a = new Area();
        for (int i = 0; i < d.length; i++) {
            a.add(getTolDilek(d[i]));
        }
        return a;
    }

    static Area getArea(Dilek d[], boolean vyber) {
        Area a = new Area();
        for (int i = 0; i < d.length; i++) {
            if (d[i].jeVybran() == vyber) a.add(getTolDilek(d[i]));
        }
        return a;
    }

    public Obrys() {
    }

    public Obrys(Area a) {
        dilkyVzor = null;
        dilkyValid = null;
        skladacka = null;
        init(a);
    }

    private void init(Dilek d[]) {
        dilkyVzor = d;
        dilkyValid = Dilek.Validity.getAll(d);
        skladacka = d[0].skladacka;
    }

    private void initSubAreas(ObrysData od[]) {
        if (od == null || od.length < 2) return;
        for (int i = 0; i < od.length; i++) {
            od[i].getShape();
        }
    }

    public Obrys(Dilek d[]) {
        init(d);
        init(getArea(d));
    }

    public Obrys(Dilek d[], ObrysData od) {
        super(od.getShape());
        this.od = od;
        init(d);
        if (od.isEmpty()) {
            this.reset();
            init(getArea(d));
        } else initSubAreas(od.subShape);
    }

    public void update(Dilek d[]) {
        this.reset();
        init(d);
        init(getArea(d));
    }

    public void update() {
        boolean changed = false;
        for (int i = 0; i < dilkyValid.length; i++) if (dilkyValid[i].validate() >= Dilek.INV_POLOHA) changed = true;
        if (changed) {
            this.reset();
            init(getArea(dilkyVzor));
        }
    }

    private void init(Area a) {
        invalidate += INV_ZMENA;
        this.add(a);
        PathIterator p = super.getPathIterator(null);
        double b[] = new double[6];
        int i = 0, t;
        while (!p.isDone()) {
            t = p.currentSegment(b);
            if (t != PathIterator.SEG_CUBICTO && t != PathIterator.SEG_QUADTO) i += 2;
            p.next();
        }
        int segType[] = new int[2 * i];
        Point2D segCoords[] = new Point2D[i];
        p = super.getPathIterator(null);
        Point2D actPoint = new Point2D();
        Point2D firstPoint = new Point2D();
        Point2D lastPoint = new Point2D();
        i = 0;
        for (; !p.isDone(); p.next()) {
            switch(t = p.currentSegment(b)) {
                case PathIterator.SEG_MOVETO:
                    segType[i++] = PathIterator.SEG_MOVETO;
                    actPoint.setLocation(b[0], b[1]);
                    firstPoint.copyFrom(actPoint);
                    continue;
                case PathIterator.SEG_CUBICTO:
                    actPoint.setLocation(b[4], b[5]);
                    continue;
                case PathIterator.SEG_QUADTO:
                    actPoint.setLocation(b[2], b[3]);
                    continue;
                case PathIterator.SEG_LINETO:
                    lastPoint.copyFrom(actPoint);
                    actPoint.setLocation(b[0], b[1]);
                    break;
                case PathIterator.SEG_CLOSE:
                    lastPoint.copyFrom(actPoint);
                    actPoint.copyFrom(firstPoint);
                    break;
                default:
                    continue;
            }
            if (actPoint.distance(lastPoint) > TOLERANCE_AREA2) {
                segType[i] = SEG_MEZERA;
                segCoords[i++] = new Point2D(lastPoint);
                segType[i] = PathIterator.SEG_LINETO;
                segCoords[i++] = new Point2D(actPoint);
            }
            if (t == PathIterator.SEG_CLOSE) {
                segType[i++] = PathIterator.SEG_CLOSE;
            }
        }
        int first = 0, last = 0, i2, i3, i4;
        Point2D p1 = new Point2D(), p2 = new Point2D(), p3 = new Point2D(), p4 = new Point2D();
        od = new ObrysData();
        od.nSegs = 0;
        for (int j = 0; j < i; j++) {
            if (segType[j] == PathIterator.SEG_MOVETO) first = j + 1; else if (segType[j] == PathIterator.SEG_CLOSE && (j - first) > 5) {
                last = j - 1;
                p1.copyFrom(segCoords[last - 1]);
                p2.copyFrom(segCoords[i2 = last]);
                for (i3 = first; i3 < last; i3 += 2) {
                    i4 = i3 + 1;
                    p3.copyFrom(segCoords[i3]);
                    p4.copyFrom(segCoords[i4]);
                    if (Vect.getAngle(Vect.vect(p1, p2), Vect.vect(p3, p4)) < TOLERANCE_UHEL) {
                        segType[i2] = SEG_NONE;
                        segType[i3] = SEG_NONE;
                    } else {
                        segType[i3] = PathIterator.SEG_LINETO;
                        segCoords[i3] = Vect.intersection(p1, p2, p4, p3);
                        segType[i2] = SEG_NONE;
                        od.nSegs++;
                    }
                    i2 = i4;
                    p1.copyFrom(p3);
                    p2.copyFrom(p4);
                    od.nSegs++;
                }
            } else segType[j] = SEG_NONE;
        }
        od.segType = new int[od.nSegs];
        od.segCoords = new Point2D[od.nSegs];
        int k = 0, typ = -1, substart = -1;
        for (int j = 0; j < i; j++) {
            switch(segType[j]) {
                case PathIterator.SEG_LINETO:
                    od.segType[k] = (typ == -1) ? PathIterator.SEG_MOVETO : PathIterator.SEG_LINETO;
                    if (od.segType[k] == PathIterator.SEG_MOVETO) {
                        substart = k;
                        typ = j;
                    }
                    od.segCoords[k++] = new Point2D(segCoords[j]);
                    break;
                case PathIterator.SEG_CLOSE:
                    if (k - substart < 3) {
                        k = substart;
                        typ = -1;
                        break;
                    }
                    od.segType[k] = PathIterator.SEG_LINETO;
                    od.segCoords[k++] = new Point2D(segCoords[typ]);
                    typ = -1;
                    break;
            }
        }
        reset();
        if (k > 0) {
            od.nSegs = k;
            add(new Area(od.getShape()));
            if (od.initSubShapes()) initSubAreas(od.subShape);
        } else od.nSegs = 0;
    }

    public boolean compare(Obrys o) {
        return compare(o, skladacka.TOLERANCE_RESENI, false);
    }

    public static boolean compare(ObrysData odConst, ObrysData odVzor, double tol, Dilek.Typ typ) {
        if (tol < TOLERANCE_AREA2) tol = TOLERANCE_AREA2;
        Point2D p1 = odConst.segCoords[0], p2;
        double d, max = 0.0, maxAngle = 0.0;
        int maxi = 0;
        for (int i = 1; i < odConst.nSegs; i++) {
            if (odConst.segType[i] == PathIterator.SEG_MOVETO) {
                p1 = odConst.segCoords[i];
                continue;
            }
            p2 = odConst.segCoords[i];
            if ((d = p2.distance(p1)) > max) {
                max = d;
                maxi = i - 1;
                maxAngle = Math.asin((p2.y - p1.y) / d);
                if ((p2.x - p1.x) < 0) maxAngle = Math.PI - maxAngle;
            }
            p1 = p2;
        }
        Area vzor;
        java.awt.geom.Rectangle2D r1, r2;
        ObrysData od = odVzor;
        int firstSide[] = new int[2], secondSide[] = new int[2];
        firstSide[0] = firstSide[1] = secondSide[0] = secondSide[1] = -1;
        double firstAngle[] = new double[2], secondAngle[] = new double[2];
        firstAngle[0] = firstAngle[1] = secondAngle[0] = secondAngle[1] = 2 * Math.PI;
        boolean ret = false;
        for (int f = 0; f < 2; f++) {
            p1 = od.segCoords[0];
            rotation: for (int i = 1; i < od.nSegs; i++) {
                if (od.segType[i] == PathIterator.SEG_MOVETO) {
                    p1 = od.segCoords[i];
                    continue;
                }
                p2 = od.segCoords[i];
                d = p2.distance(p1);
                if (Math.abs(d - max) <= tol) {
                    vzor = new Area(od.a);
                    d = Math.asin((p2.y - p1.y) / d);
                    if ((p2.x - p1.x) < 0) d = Math.PI - d;
                    d = maxAngle - d;
                    vzor.transform(AffineTransform.getRotateInstance(d, p1.x, p1.y));
                    vzor.transform(AffineTransform.getTranslateInstance(odConst.segCoords[maxi].x - p1.x, odConst.segCoords[maxi].y - p1.y));
                    r1 = odConst.a.getBounds2D();
                    r2 = vzor.getBounds2D();
                    if (Math.abs(r1.getX() - r2.getX()) <= tol && Math.abs(r1.getY() - r2.getY()) <= tol && Math.abs(r1.getWidth() - r2.getWidth()) <= tol && Math.abs(r1.getHeight() - r2.getHeight()) <= tol) {
                        if (odConst.aStrokedCache == null || odConst.cacheTol != tol) {
                            odConst.aStrokedCache = new Area((new BasicStroke((float) tol, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)).createStrokedShape(odConst.a));
                            odConst.aStrokedCache.add(odConst.a);
                            odConst.cacheTol = tol;
                        }
                        vzor.subtract(odConst.aStrokedCache);
                        if (vzor.isEmpty()) {
                            ret = true;
                            if (typ == null) return true;
                            if (firstSide[f] == -1) {
                                firstSide[f] = i - 1;
                                firstAngle[f] = d;
                            } else if (firstSide[f] > -1) {
                                secondSide[f] = i - 1;
                                secondAngle[f] = d;
                                break rotation;
                            }
                        }
                    }
                }
                p1 = p2;
            }
            vzor = new Area(od.a);
            vzor.transform(AffineTransform.getScaleInstance(-1, 1));
            od = new Obrys(vzor).od;
        }
        if (typ != null) {
            if (firstSide[0] > -1 && firstSide[1] > -1) {
                typ.flipSymetry = true;
                typ.flipSymetryAngle = Vect.normalizeAngle(firstAngle[0] - firstAngle[1]);
            } else {
                typ.flipSymetry = false;
                typ.flipSymetryAngle = 0;
            }
            typ.rotationSymetryAngle = 2 * Math.PI;
            typ.rotationSymetrySides = od.nSegs - 1;
            int which = -1;
            if (firstSide[0] > -1 && secondSide[0] > -1) which = 0; else if (firstSide[1] > -1 && secondSide[1] > -1) which = 1;
            if (which > -1) {
                typ.rotationSymetrySides = secondSide[which] - firstSide[which];
                typ.rotationSymetryAngle = Vect.normalizeAngle(secondAngle[which] - firstAngle[which]);
                while (typ.flipSymetryAngle > typ.rotationSymetryAngle) typ.flipSymetryAngle -= typ.rotationSymetryAngle;
            }
        }
        return ret;
    }

    public static boolean fit(int naRade, ObrysData odConst[], ObrysData od[], boolean zbyva[], double tol) {
        int zbyvaCount = 0;
        for (int i = 0; i < zbyva.length; i++) {
            if (!zbyva[i]) continue;
            zbyvaCount++;
            if (compare(odConst[naRade], od[i], tol, null)) {
                zbyva[i] = false;
                if (fit(naRade + 1, odConst, od, zbyva, tol)) return true; else zbyva[i] = true;
            }
        }
        if (zbyvaCount == 0) return true;
        return false;
    }

    public boolean compare(Obrys o, double tol, boolean strictPlacement) {
        if ((od.subShape == null && o.od.subShape == null) || strictPlacement) return compare(od, o.od, tol, null);
        if (od.subShape == null && o.od.subShape != null) return false;
        if (od.subShape != null && o.od.subShape == null) return false;
        if (od.subShape.length != o.od.subShape.length) return false;
        boolean zbyva[] = new boolean[o.od.subShape.length];
        for (int i = 0; i < zbyva.length; i++) zbyva[i] = true;
        return fit(0, od.subShape, o.od.subShape, zbyva, tol);
    }

    public Stroke getToleranceStroke() {
        return cachedStroke;
    }

    public static class Validity {

        Obrys o;

        private Rectangle2D rect;

        private int invalidate;

        Validity(Obrys o) {
            this.o = o;
            invalidate = o.invalidate - Obrys.INV_ZMENA;
            rect = new Rectangle2D.Double(0, 0, 0, 0);
        }

        static Validity[] getAll(Obrys o[]) {
            Validity v[] = new Validity[o.length];
            for (int i = 0; i < v.length; i++) v[i] = new Validity(o[i]);
            return v;
        }

        Rectangle2D getLastValid() {
            return rect;
        }

        int validate(boolean cleared, Rectangle2D r) {
            int i = o.invalidate - invalidate;
            if (cleared) {
                rect = o.getBounds2D();
                r.setRect(rect);
            } else {
                if (i == Obrys.INV_VALID) r.setRect(0, 0, 0, 0);
                if (i >= Obrys.INV_ZMENA) {
                    r.setRect(rect);
                    rect = o.getBounds2D();
                    r.add(rect);
                }
            }
            invalidate += i;
            return i;
        }

        void change(Obrys o) {
            this.o = o;
            invalidate = o.invalidate - Obrys.INV_ZMENA;
        }
    }

    public static class MyPathIterator implements PathIterator {

        PathIterator orig;

        boolean start = true;

        double dcoords[] = new double[6];

        float fcoords[] = new float[6];

        public MyPathIterator(PathIterator orig) {
            this.orig = orig;
        }

        public boolean isDone() {
            return orig.isDone() || (!start && orig.currentSegment(dcoords) == PathIterator.SEG_MOVETO);
        }

        public int currentSegment(double coords[]) {
            return orig.currentSegment(coords);
        }

        public int currentSegment(float coords[]) {
            return orig.currentSegment(coords);
        }

        public int getWindingRule() {
            return orig.getWindingRule();
        }

        public void next() {
            start = false;
            orig.next();
        }
    }
}
