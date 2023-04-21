package org.fao.waicent.xmap2D.coordsys.proj;

import java.io.IOException;
import org.fao.waicent.util.XMLUtil;
import org.fao.waicent.xmap2D.coordsys.AngularUnit;
import org.fao.waicent.xmap2D.coordsys.Ellipsoid;
import org.fao.waicent.xmap2D.coordsys.LinearUnit;
import org.fao.waicent.xmap2D.coordsys.Projection;
import org.fao.waicent.xmap2D.coordsys.WellKnownProjections;
import org.fao.waicent.xmap2D.util.MathTool;
import org.fao.waicent.xmap2D.util.ParamList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Cassini extends WellKnownProjections {

    public static String ABBREVIATION = "CASSINI";

    public Cassini() {
        super("Cassini", ABBREVIATION);
    }

    public Cassini(Document doc, Element element) throws IOException {
        this();
        load(doc, element);
    }

    public Cassini(AngularUnit angularunit, LinearUnit linearunit, Ellipsoid ellipsoid, double Central_Meridian) {
        this(angularunit, linearunit, ellipsoid, Central_Meridian, 0, 0, 0);
    }

    public Cassini(AngularUnit angularunit, LinearUnit linearunit, Ellipsoid ellipsoid, double Central_Meridian, double cl, double fe, double fn) {
        this();
        initializeProjection(angularunit, linearunit, ellipsoid);
        central_meridian = Central_Meridian;
        central_parallel = cl;
        false_easting = fe;
        false_northing = fn;
        initCassini();
    }

    public ParamList getParameters() {
        ParamList paramlist = new ParamList();
        paramlist.put("central_meridian", central_meridian);
        paramlist.put("central_parallel", central_parallel);
        paramlist.put("false_easting", false_easting);
        paramlist.put("false_northing", false_northing);
        return paramlist;
    }

    private void initCassini() {
        cl_rad = AngularUnit.radian.convertClipY(central_parallel, super.theangularunit);
        cm_rad = AngularUnit.radian.convertClipX(central_meridian, super.theangularunit);
        e = super.theellipsoid.meridianDistance(cl_rad);
    }

    public boolean equals(Projection projection) {
        if (projection instanceof Cassini) {
            Cassini Cassini = (Cassini) projection;
            return checkEquals(Cassini) && central_meridian == Cassini.central_meridian && central_parallel == Cassini.central_parallel && false_easting == Cassini.false_easting && false_northing == Cassini.false_northing;
        } else {
            return false;
        }
    }

    public void forward(double srcPts[], int srcOff, double dstPts[], int dstOff, int numPts) {
        while (numPts-- > 0) {
            double x = AngularUnit.radian.convertClipX(srcPts[srcOff++], super.theangularunit);
            double y = AngularUnit.radian.convertClipY(srcPts[srcOff++], super.theangularunit);
            if (super.theellipsoid.isSphere()) {
                double d1 = MathTool.angle180(x - cm_rad);
                double d3 = Math.cos(y) * Math.sin(d1);
                x = super.theellipsoid.getSemiMajorAxis() * Math.asin(d3);
                y = super.theellipsoid.getSemiMajorAxis() * (Math.atan(Math.tan(y) / Math.cos(d1)) - cl_rad);
            } else {
                double d2 = super.theellipsoid.radiusOfCurvatureN(y);
                double d4 = Math.tan(y);
                double d5 = d4 * d4;
                double d6 = Math.cos(y);
                double d7 = MathTool.angle180(x - cm_rad) * d6;
                double d8 = (super.theellipsoid.getEccentricitySquared() * d6 * d6) / (1.0D - super.theellipsoid.getEccentricitySquared());
                double d9 = super.theellipsoid.meridianDistance(y);
                double d10 = d7 * d7;
                double d11 = d10 * d7;
                double d12 = d10 * d10;
                double d13 = d11 * d10;
                x = d2 * (d7 - (d5 * d11) / 6D - (((8D - d5) + 8D * d8) * d5 * d13) / 120D);
                y = (d9 - e) + d2 * d4 * (d10 / 2D + (((5D - d5) + 6D * d8) * d12) / 24D);
            }
            dstPts[dstOff++] = super.thelinearunit.convert(x, ellipsoidaxisunit) + false_easting;
            dstPts[dstOff++] = super.thelinearunit.convert(y, ellipsoidaxisunit) + false_northing;
        }
    }

    public int getType() {
        return TYPE_CASSINI;
    }

    public String getWellKnownText() {
        return "PROJECTION[\"" + getName() + "\"]," + "PARAMETER[\"Central_Meridian\"," + central_meridian + "]";
    }

    public void inverse(double srcPts[], int srcOff, double dstPts[], int dstOff, int numPts) {
        while (numPts-- > 0) {
            double lambda = ellipsoidaxisunit.convert(srcPts[srcOff++] - false_easting, super.thelinearunit);
            double phi = ellipsoidaxisunit.convert(srcPts[srcOff++] - false_northing, super.thelinearunit);
            if (super.theellipsoid.isSphere()) {
                double d1 = lambda / super.theellipsoid.getSemiMajorAxis();
                double d3 = phi / super.theellipsoid.getSemiMajorAxis() + cl_rad;
                phi = Math.asin(Math.sin(d3) * Math.cos(d1));
                lambda = cm_rad + Math.atan(Math.tan(d1) / Math.cos(d3));
            } else {
                double d2 = e + phi;
                double d4 = super.theellipsoid.meridianDistanceToRectifying(d2);
                double d5 = super.theellipsoid.rectifyingToLatitude(d4);
                double d6 = super.theellipsoid.radiusOfCurvatureR(d5);
                double d7 = super.theellipsoid.radiusOfCurvatureN(d5);
                double d8 = Math.tan(d5);
                double d9 = d8 * d8;
                double d10 = lambda / d7;
                double d11 = d10 * d10;
                double d12 = d11 * d10;
                double d13 = d11 * d11;
                double d14 = d11 * d12;
                phi = d5 - ((d7 * d8) / d6) * (d11 / 2D - ((1.0D + 3D * d9) * d13) / 24D);
                lambda = cm_rad + ((d10 - (d9 * d12) / 3D) + ((1.0D + 3D * d9) * d9 * d14) / 15D) / Math.cos(d5);
            }
            dstPts[dstOff++] = super.theangularunit.convert(lambda, AngularUnit.radian);
            dstPts[dstOff++] = super.theangularunit.convert(phi, AngularUnit.radian);
        }
    }

    public void save(Document doc, Element ele) throws IOException {
        XMLUtil.setType(doc, ele, this);
        super.save(doc, ele);
        ele.setAttribute("central_meridian", Double.toString(central_meridian));
        ele.setAttribute("central_parallel", Double.toString(central_parallel));
    }

    public void load(Document doc, Element ele) throws IOException {
        super.load(doc, ele);
        XMLUtil.checkType(doc, ele, this);
        central_meridian = Double.parseDouble(ele.getAttribute("central_meridian"));
        central_parallel = Double.parseDouble(ele.getAttribute("central_parallel"));
        initCassini();
    }

    public static void main(String arg[]) {
        Cassini eckert = new Cassini(AngularUnit.degree, LinearUnit.meter, Ellipsoid.WGS84, -100.0, 29.999999999999996, 0.0, 0.0);
        double input[] = { -75, -35 };
        eckert.forward(input, 0, input, 0, 1);
        System.out.println(input[0] + ", " + input[1]);
        eckert.inverse(input, 0, input, 0, 1);
        System.out.println(input[0] + ", " + input[1]);
    }

    private double central_meridian;

    private double central_parallel = 0;

    ;

    private double false_easting = 0;

    private double false_northing = 0;

    private double e;

    private double cl_rad;

    private double cm_rad;
}
