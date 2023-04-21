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

public class Winkel1 extends WellKnownProjections {

    public static String ABBREVIATION = "WINKEL1";

    public Winkel1() {
        super("Winkel 1", ABBREVIATION);
    }

    public Winkel1(Document doc, Element element) throws IOException {
        this();
        load(doc, element);
    }

    public Winkel1(AngularUnit angularunit, LinearUnit linearunit, Ellipsoid ellipsoid, double CentralMeridian, double stndard_paral, double false_e, double false_n) {
        this();
        initializeProjection(angularunit, linearunit, ellipsoid);
        central_meridian = CentralMeridian;
        standard_parallel = stndard_paral;
        false_easting = false_e;
        false_northing = false_n;
        initWinkel1();
    }

    private void initWinkel1() {
        semiMajorAxis = super.theellipsoid.getSemiMajorAxis();
        e = Math.cos(AngularUnit.radian.convertClipY(standard_parallel, super.theangularunit));
        lambda0 = AngularUnit.radian.convertClipX(central_meridian, super.theangularunit);
        standard_parallel_rad = AngularUnit.radian.convertClipY(standard_parallel, super.theangularunit);
    }

    public boolean equals(Projection projection) {
        if (projection instanceof Winkel1) {
            Winkel1 winkel = (Winkel1) projection;
            return checkEquals(winkel) && central_meridian == winkel.central_meridian && standard_parallel == winkel.standard_parallel && false_easting == winkel.false_easting && false_northing == winkel.false_northing;
        } else {
            return false;
        }
    }

    public void forward(double srcPts[], int srcOff, double dstPts[], int dstOff, int numPts) {
        while (numPts-- > 0) {
            double x = AngularUnit.radian.convertClipX(srcPts[srcOff++], super.theangularunit);
            double y = AngularUnit.radian.convertClipY(srcPts[srcOff++], super.theangularunit);
            double d1 = MathTool.longitude180(x - lambda0);
            x = (semiMajorAxis * d1 * (Math.cos(standard_parallel_rad) + Math.cos(y))) / 2D;
            y = semiMajorAxis * y;
            dstPts[dstOff++] = super.thelinearunit.convert(x, ellipsoidaxisunit) + false_easting;
            dstPts[dstOff++] = super.thelinearunit.convert(y, ellipsoidaxisunit) + false_northing;
        }
    }

    public int getType() {
        return TYPE_ECKERT4;
    }

    public String getWellKnownText() {
        return "PROJECTION[\"" + getName() + "\"]," + "PARAMETER[\"CentralMeridian\"," + central_meridian + "]";
    }

    public ParamList getParameters() {
        ParamList paramlist = new ParamList();
        paramlist.put("central_meridian", central_meridian);
        return paramlist;
    }

    public void inverse(double srcPts[], int srcOff, double dstPts[], int dstOff, int numPts) {
        while (numPts-- > 0) {
            double lambda = ellipsoidaxisunit.convert(srcPts[srcOff++] - false_easting, super.thelinearunit);
            double phi = ellipsoidaxisunit.convert(srcPts[srcOff++] - false_northing, super.thelinearunit);
            phi = phi / semiMajorAxis;
            lambda = MathTool.longitude180(lambda0 + (2D * lambda) / (semiMajorAxis * (Math.cos(standard_parallel_rad) + Math.cos(phi))));
            dstPts[dstOff++] = super.theangularunit.convert(lambda, AngularUnit.radian);
            dstPts[dstOff++] = super.theangularunit.convert(phi, AngularUnit.radian);
        }
    }

    public void save(Document doc, Element ele) throws IOException {
        XMLUtil.setType(doc, ele, this);
        super.save(doc, ele);
        ele.setAttribute("central_meridian", Double.toString(central_meridian));
        ele.setAttribute("standard_parallel", Double.toString(standard_parallel));
        ele.setAttribute("false_easting", Double.toString(false_easting));
        ele.setAttribute("false_northing", Double.toString(false_northing));
    }

    public void load(Document doc, Element ele) throws IOException {
        super.load(doc, ele);
        XMLUtil.checkType(doc, ele, this);
        central_meridian = Double.parseDouble(ele.getAttribute("central_meridian"));
        standard_parallel = Double.parseDouble(ele.getAttribute("standard_parallel"));
        false_easting = Double.parseDouble(ele.getAttribute("false_easting"));
        false_northing = Double.parseDouble(ele.getAttribute("false_northing"));
        initWinkel1();
    }

    public static void main(String arg[]) {
        Winkel1 eckert = new Winkel1(AngularUnit.degree, LinearUnit.meter, Ellipsoid.WGS84, 0.0, 50.45977625218981, 0.0, 0.0);
        double input[] = { -75, 50 };
        eckert.forward(input, 0, input, 0, 1);
        System.out.println(input[0] + ", " + input[1]);
        eckert.inverse(input, 0, input, 0, 1);
        System.out.println(input[0] + ", " + input[1]);
    }

    private double central_meridian;

    private double standard_parallel;

    private double false_easting;

    private double false_northing;

    private double semiMajorAxis;

    private double lambda0;

    private double standard_parallel_rad;

    private double e;
}
