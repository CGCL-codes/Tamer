package org.fao.waicent.xmap2D.coordsys;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import org.fao.waicent.util.XMLUtil;
import org.fao.waicent.util.XMLUtilOld;
import org.fao.waicent.util.XMLable;
import org.fao.waicent.xmap2D.util.DoubleRect;
import org.fao.waicent.xmap2D.util.MathTool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CoordSys implements XMLable, NamedObject {

    public CoordSys() {
    }

    public CoordSys(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public CoordSys(CoordSys coordsys, AffineTransform affinetransform) throws Exception {
        this(coordsys, affinetransform, coordsys.m_Quadrant);
    }

    public CoordSys(CoordSys coordsys, AffineTransform affinetransform, int i) throws Exception {
        this(coordsys, affinetransform, i, coordsys.m_Precision);
    }

    private CoordSys(CoordSys coordsys, AffineTransform affinetransform, int i, double precision) throws Exception {
        this(coordsys.m_RefSys, ApplAffnTransform.do_concatenate(coordsys.m_AffineTransform, affinetransform), i, precision);
    }

    private CoordSys(CoordSys coordsys, Rectangle2D doublerect, Rectangle2D doublerect1) throws Exception {
        this(coordsys, doublerect, doublerect1, 4, true, true, new DoubleRect(doublerect).center().y, 1);
    }

    private CoordSys(CoordSys coordsys, Rectangle2D doublerect2D, Rectangle2D doublerect2D1, int quadrant, boolean flag, boolean flag1, double d, int precision) throws Exception {
        DoubleRect doublerect = new DoubleRect(doublerect2D);
        DoubleRect doublerect1 = new DoubleRect(doublerect2D1);
        ;
        if (flag) {
            double d1 = 1.0D;
            if (flag1 && coordsys.isLongLat() && coordsys.getAffineTransform().isIdentity()) {
                d1 = Math.cos(AngularUnit.radian.convertClip(d, (AngularUnit) coordsys.getUnit(), -80D, 80D));
            }
            doublerect = new DoubleRect(doublerect);
            double d2 = doublerect.getHeight() * doublerect1.getWidth();
            double d3 = doublerect.getWidth() * doublerect1.getHeight() * d1;
            if (d2 > d3) {
                doublerect.expand(d2 / d1 / doublerect1.getHeight() - doublerect.getWidth(), 0.0D);
            } else {
                doublerect.expand(0.0D, d3 / doublerect1.getWidth() - doublerect.getHeight());
            }
        }
        boolean flag2 = (quadrant == 1 || quadrant == 4) != (coordsys.m_Quadrant == 1 || coordsys.m_Quadrant == 4);
        boolean flag3 = (quadrant == 1 || quadrant == 2) != (coordsys.m_Quadrant == 1 || coordsys.m_Quadrant == 2);
        ApplAffnTransform affinetransform = ApplAffnTransform.getAffineTransform(doublerect, doublerect1, flag2, flag3);
        ApplAffnTransform affinetransform1 = ApplAffnTransform.do_concatenate(coordsys.m_AffineTransform, affinetransform);
        testAffineTransform(affinetransform1);
        set(coordsys.m_RefSys, affinetransform1, quadrant, precision);
    }

    private CoordSys(SpatialReferenceSystem spatialreferencesystem) {
        this(spatialreferencesystem, 1);
    }

    private CoordSys(SpatialReferenceSystem spatialreferencesystem, int quadrant) {
        set(spatialreferencesystem, new ApplAffnTransform(), quadrant, 1E-010D);
    }

    public CoordSys(SpatialReferenceSystem spatialreferencesystem, AffineTransform affinetransform) throws Exception {
        this(spatialreferencesystem, affinetransform, 1);
    }

    private CoordSys(SpatialReferenceSystem spatialreferencesystem, AffineTransform affinetransform, int quadrant) throws Exception {
        this(spatialreferencesystem, affinetransform, quadrant, 1E-010D);
    }

    private CoordSys(SpatialReferenceSystem spatialreferencesystem, AffineTransform affinetransform, int quadrant, double precision) throws Exception {
        testAffineTransform(affinetransform);
        set(spatialreferencesystem, affinetransform, quadrant, precision);
    }

    public void concatenate(AffineTransform at) throws Exception {
        AffineTransform affinetransform = ApplAffnTransform.do_concatenate(m_AffineTransform, at);
        testAffineTransform(affinetransform);
        set(m_RefSys, affinetransform, m_Quadrant, m_Precision);
    }

    public void initFromCoordSys(CoordSys coordsys) throws Exception {
        testAffineTransform(coordsys.getAffineTransform());
        set(coordsys.getReferenceSystem(), coordsys.getAffineTransform(), coordsys.getQuadrant(), coordsys.getPrecision());
    }

    public CoordSys getClone() {
        try {
            Document doc = XMLUtilOld.saveDocument(this, "clone");
            return getCoordinateSystem(doc, doc.getDocumentElement());
        } catch (Exception e) {
        }
        return null;
    }

    public void save(Document doc, Element ele) throws IOException {
        XMLUtil.setType(doc, ele, this);
        Element r_ele = null;
        if (m_RefSys != null) {
            r_ele = doc.createElement("SpatialReferenceSystem");
            ele.appendChild(r_ele);
            m_RefSys.save(doc, r_ele);
        }
        if (m_AffineTransform != null) {
            r_ele = doc.createElement("AffineTransform");
            ele.appendChild(r_ele);
            m_AffineTransform.save(doc, r_ele);
        }
        ele.setAttribute("Quadrant", Integer.toString(m_Quadrant));
        ele.setAttribute("Precision", Double.toString(m_Precision));
    }

    public void load(Document doc, Element ele) throws IOException {
        XMLUtil.checkType(doc, ele, this);
        m_Quadrant = Integer.parseInt(ele.getAttribute("Quadrant"));
        m_Precision = Double.parseDouble(ele.getAttribute("Precision"));
        Element child = XMLUtil.getChild(doc, ele, "SpatialReferenceSystem");
        if (child != null) {
            m_RefSys = SpatialReferenceSystem.getSpatialReferenceSystem(doc, child);
        }
        child = XMLUtil.getChild(doc, ele, "AffineTransform");
        if (child != null) {
            m_AffineTransform = new ApplAffnTransform(doc, child);
        }
        child = XMLUtil.getChild(doc, ele, "GeographicCoordinateSystem");
        set(m_RefSys, m_AffineTransform, m_Quadrant, m_Precision);
    }

    public static CoordSys getCoordinateSystem(Document doc, Element ele) throws IOException {
        return (CoordSys) XMLUtil.load(doc, ele);
    }

    public CoordSys getUntrasformedCoordSys() {
        try {
            return new CoordSys(new CoordSys(this, getAffineTransform().createInverse()), new AffineTransform(0, 0, 0, -1, 0, 0));
        } catch (Exception e) {
            return this;
        }
    }

    public void adjustPointsUsingDistance(Point2D A, Point2D B, double distance, LinearUnit linearunit, boolean moveA, boolean Yfixed) {
        distance = Math.abs(distance);
        for (int i = 0; i < 10; i++) {
            double distaAB = cartesianDistance(A, B, linearunit);
            double xf;
            double yf;
            if (distaAB == 0.0D) {
                if (Yfixed) {
                    xf = A.getX() != 0.0D ? A.getX() / -100D : -1D;
                    yf = 0.0D;
                } else {
                    xf = 0.0D;
                    yf = A.getY() != 0.0D ? A.getY() / -100D : -1D;
                }
            } else {
                double d4 = distance / distaAB - 1.0D;
                if (Math.abs(d4) < 1.0000000000000002E-006D) {
                    return;
                }
                xf = (B.getX() - A.getX()) * d4;
                yf = (B.getY() - A.getY()) * d4;
            }
            if (moveA) {
                xf /= 2D;
                yf /= 2D;
                A.setLocation(A.getX() - xf, A.getY() - yf);
            }
            B.setLocation(B.getX() + xf, B.getY() + yf);
        }
    }

    public double cartesianDistance(double x1, double y1, double x2, double y2, Unit unit) {
        double square[] = { x1, y1, x2, y2 };
        m_AffineTransform.safeInverse(square, 0, square, 0, 2);
        Unit ref_sys_unit = getUnit();
        if (ref_sys_unit instanceof LinearUnit) {
            double dist = MathTool.cartesianDistance(square[0], square[1], square[2], square[3]);
            return unit.convert(dist, ref_sys_unit);
        } else {
            AngularUnit.radian.convert(square, 0, 2, ref_sys_unit);
            square[1] = AngularUnit.radian.clipY(square[1]);
            square[3] = AngularUnit.radian.clipY(square[3]);
            double x2x = (square[2] - square[0]) * Math.cos((square[1] + square[3]) / 2D);
            double y2y = square[3] - square[1];
            double dist = Math.sqrt(x2x * x2x + y2y * y2y);
            return unit.convert(dist, AngularUnit.radian);
        }
    }

    public double cartesianDistance(Point2D A, Point2D B, Unit unit) {
        return cartesianDistance(A.getX(), A.getY(), B.getX(), B.getY(), unit);
    }

    public boolean equals(CoordSys coordsys) {
        return m_RefSys.equals(coordsys.m_RefSys) && m_AffineTransform.equals(coordsys.m_AffineTransform) && m_Quadrant == coordsys.m_Quadrant && m_Precision == coordsys.m_Precision;
    }

    public boolean equalsReferenceSystem(CoordSys coordsys) {
        return m_RefSys.equals(coordsys.m_RefSys);
    }

    public void forward(double source[], int i, double dest[], int j, int k) {
        if (m_Projection != null) {
            m_Projection.forward(source, i, dest, j, k);
            source = dest;
            i = j;
        }
        m_AffineTransform.forward(source, i, dest, j, k);
    }

    public static AffineTransform getDefaultAffineTransform() {
        return new AffineTransform(1, 0, 0, -1, 0, 0);
    }

    public ApplAffnTransform getAffineTransform() {
        return m_AffineTransform;
    }

    public GeographicCoordinateSystem getGeographicCoordinateSystem() {
        return m_GeoCoordSys;
    }

    public double getPrecision() {
        return m_Precision;
    }

    public Projection getProjection() {
        return m_Projection;
    }

    public int getQuadrant() {
        return m_Quadrant;
    }

    public SpatialReferenceSystem getReferenceSystem() {
        return m_RefSys;
    }

    public Unit getUnit() {
        return m_RefSys.getUnit();
    }

    public void inverse(double source[], int i, double dest[], int j, int k) {
        m_AffineTransform.safeInverse(source, i, dest, j, k);
        if (m_Projection != null) {
            m_Projection.inverse(dest, j, dest, j, k);
        }
    }

    public boolean isLongLat() {
        return !isProjected() && !isNonEarth();
    }

    public boolean isNonEarth() {
        return m_GeoCoordSys == null;
    }

    public boolean isProjected() {
        return m_Projection != null;
    }

    private void set(SpatialReferenceSystem spatialreferencesystem, AffineTransform affinetransform, int quadrant, double precision) {
        m_RefSys = spatialreferencesystem;
        m_AffineTransform = new ApplAffnTransform(affinetransform);
        m_Quadrant = quadrant;
        m_Precision = precision;
        if (m_RefSys instanceof ProjectedCoordinateSystem) {
            m_Projection = ((ProjectedCoordinateSystem) m_RefSys).getProjection();
        }
        if (m_RefSys instanceof GeodeticSpatialReferenceSystem) {
            m_GeoCoordSys = ((GeodeticSpatialReferenceSystem) m_RefSys).getGeographicCoordinateSystem();
        }
    }

    public double sphericalDistance(double x1, double y1, double x2, double y2, Unit unit) {
        double square[] = { x1, y1, x2, y2 };
        inverse(square, 0, square, 0, 2);
        if (isNonEarth()) {
            double d4 = MathTool.cartesianDistance(square[0], square[1], square[2], square[3]);
            return unit.convert(d4, getUnit());
        } else {
            AngularUnit.radian.convert(square, 0, 2, m_GeoCoordSys.getAngularUnit());
            square[1] = AngularUnit.radian.clipY(square[1]);
            square[3] = AngularUnit.radian.clipY(square[3]);
            double pdiff = Math.sin((square[2] - square[0]) / 2D);
            double d6 = Math.sin((square[3] - square[1]) / 2D);
            double rval = d6 * d6 + Math.cos(square[1]) * Math.cos(square[3]) * pdiff * pdiff;
            rval = Math.sqrt(Math.max(rval, 0.0D));
            rval = Math.asin(Math.min(rval, 1.0D)) * 2D;
            return unit.convert(rval, AngularUnit.radian);
        }
    }

    public double sphericalDistance(Point2D A, Point2D B, Unit unit) {
        return sphericalDistance(A.getX(), A.getY(), B.getX(), B.getY(), unit);
    }

    public String getLongDescription() {
        return m_Remarks;
    }

    public String getDescription() {
        return m_Remarks;
    }

    public String getName() {
        return _name;
    }

    String _name = null;

    public void setName(String n) {
        _name = n;
    }

    private int _id = 0;

    public int getCoordSysID() {
        return _id;
    }

    private static void testAffineTransform(AffineTransform affinetransform) throws Exception {
        if (affinetransform.getDeterminant() == 0) {
            throw new Exception("Noninvertible affine transform");
        } else {
            return;
        }
    }

    private SpatialReferenceSystem m_RefSys;

    private ApplAffnTransform m_AffineTransform;

    private int m_Quadrant;

    private double m_Precision;

    private Projection m_Projection;

    private GeographicCoordinateSystem m_GeoCoordSys;

    protected String m_Remarks = null;

    public static final CoordSys longLatDatumless;

    public static final CoordSys longLatNAD27;

    public static final CoordSys longLatNAD83;

    public static final CoordSys longLatWGS84;

    static {
        longLatDatumless = new CoordSys(GeographicCoordinateSystem.datumless);
        longLatNAD27 = new CoordSys(GeographicCoordinateSystem.NAD27);
        longLatNAD83 = new CoordSys(GeographicCoordinateSystem.NAD83);
        longLatWGS84 = new CoordSys(GeographicCoordinateSystem.WGS84);
    }

    public static void main(String arg[]) throws Exception {
        XMLUtilOld.dump(longLatWGS84, "e");
        XMLUtilOld.dump(longLatWGS84.getClone(), "e");
    }
}
